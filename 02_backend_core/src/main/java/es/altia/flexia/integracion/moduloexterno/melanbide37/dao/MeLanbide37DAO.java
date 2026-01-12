package es.altia.flexia.integracion.moduloexterno.melanbide37.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide37.manager.MeLanbide37Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide37.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide37.util.ConstantesMeLanbide37;
import es.altia.flexia.integracion.moduloexterno.melanbide37.util.MeLanbide37InformeUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.CerUnidadeCompetencialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompAPAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoExpedientesEMPNLVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.Melanbide37RelacionExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpCEPAPCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpedienteCEPAPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.comun.SelectItem;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
//import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.CerCertificadoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.CallableStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.ResultSetMetaData;
import java.sql.Struct;
import java.sql.Array;
//import oracle.jdbc.OracleConnection;
//import oracle.jdbc.oracore.OracleTypeADT;
//import oracle.sql.ArrayDescriptor;
//import oracle.sql.STRUCT;
//import oracle.sql.StructDescriptor;
import org.apache.log4j.xml.SAXErrorHandler;


/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 17-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide37DAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide37DAO.class);
    
    //Instancia
    private static MeLanbide37DAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide37DAO, si no existe la crea
     * @return MeLanbide37DAO
     */
    public static MeLanbide37DAO getInstance(){
        if(log.isDebugEnabled()) log.error("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide37DAO.class){
                if(instance == null){
                    instance = new MeLanbide37DAO();
                }//if(instance == null)
            }//synchronized(MeLanbide37DAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.error("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve el código del certificado para un expediente
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return String
     * @throws Exception 
     */
    public String getCertificadoExpediente(String numExpediente, int codOrganizacion,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.error("getCertificadoExpediente( numExpediente = " + numExpediente +  " codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        String codCertificado = null;
        Statement st = null;
        ResultSet rs = null;
        if(log.isDebugEnabled()) log.error("Buscamos en la tabla MELANBIDE37_CERTIFICADO el certificado seleccionado para el expediente");
        try{
           String sql = "Select COD_CERTIFICADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_CERTIFICADO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " where NUM_EXPEDIENTE like '" + numExpediente + "'"
                    + " and COD_ORGANIZACION = " + codOrganizacion; 
           
            if(log.isDebugEnabled()) log.error("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
           
           while(rs.next()){
               codCertificado = rs.getString("COD_CERTIFICADO");
           }//while(rs.next())
           
           if(log.isDebugEnabled()) log.error("Recuperamos los datos del certificado de la BBDD");
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.error("getCertificadoExpediente() : END");
        return codCertificado;
    }//getCertificadoExpediente
    
    public String  getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try
        {
            String query = null;
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio 
                    + "' and TXT_NUM = '"+numExp+"' and TXT_COD = '"+codigoCampo+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                valor = rs.getString("TXT_VALOR");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las Áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return valor;
    }
    
    public Integer  getValorCampoFechaPresentacion(int codOrganizacion, String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        Date fecha = null;
        Integer ano = null;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            String query = null;
            query = "select TFE_VALOR from E_TFE "
                    + " where TFE_MUN = '" + codOrganizacion + "' " 
                    + " and TFE_NUM = '"+numExp+"' and TFE_COD = 'FECHAPRESENTACION'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                valor = rs.getString("TFE_VALOR");
                if(valor != null && !valor.equals(""))
                    fecha = formato.parse(valor);
                GregorianCalendar gC=new GregorianCalendar();
                gC.setTime(fecha);
                ano = gC.get(GregorianCalendar.YEAR);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la fecha de presentacion ", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return ano;
    }
    public int guardarValorCampoTexto(int codOrganizacion, String ejercicio, String numExp, String codigoCampo, String valor, boolean nuevo, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        try
        {
            String query = null;
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " values("+codOrganizacion
                    + ", "+ejercicio
                    + ", '"+numExp+"'"
                    + ", '"+codigoCampo+"'"                    + ", '"+valor
                    + "')";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TIPO_DATO_TEXTO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " set TXT_VALOR = '"+valor
                    + "' where TXT_MUN = '"+codOrganizacion+"'"
                    + " and TXT_EJE = "+ejercicio
                    + " and TXT_NUM = '"+numExp+"'"
                    + " and TXT_COD = '"+codigoCampo+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario ("+numExp+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
        return result;
    }
    
    
    public int guardarContador(int codOrganizacion, String ejercicio, String tipoContador, String num_contador, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            query = "select NUM_CONTADOR from "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_CONTADORES , ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    +" where COD_ORGANIZACION = '"+codOrganizacion+"'"
                    + " and ANO_CONTADOR = "+ejercicio
                    + " and TIPO_CONTADOR = '"+tipoContador+"'";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
                nuevo = false;
            
            
            
            if(nuevo)
            {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_CONTADORES, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " (COD_ORGANIZACION, TIPO_CONTADOR, ANO_CONTADOR, NUM_CONTADOR) "
                    + " values("+codOrganizacion
                    + ", '"+tipoContador+"'"
                    + ", "+ejercicio                   
                    + ", "+Integer.parseInt(num_contador)
                    + ")";
            }
            else
            {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_CONTADORES, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " set NUM_CONTADOR = '"+Integer.parseInt(num_contador)
                    + "' where COD_ORGANIZACION = '"+codOrganizacion+"'"
                    + " and ANO_CONTADOR = "+ejercicio
                    + " and TIPO_CONTADOR = '"+tipoContador+"'";
            }
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
        return result;
    }
   
     public Integer getNumContador(int codOrganizacion, String ejercicio, String tipoContador, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Integer valor = null;
        try
        {
            String query = null;
            query = "select max(NUM_CONTADOR) as NUM from " +ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_CONTADORES, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                     + " where COD_ORGANIZACION = '" + codOrganizacion + "' and ANO_CONTADOR = '" + ejercicio 
                     + "' and TIPO_CONTADOR = '"+tipoContador+"'";            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){               
                valor = rs.getInt("NUM");           
                if (rs.wasNull()) valor = 0;
            }
                   }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el contador", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return valor;
    }
       
    
    public ArrayList<CerUnidadeCompetencialVO> getCodigosUnidades (String codCertificado, String organizacion, String numExp ,Connection con) throws Exception
    {
        if(log.isDebugEnabled()) log.error("getCodigosUnidades() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadas = new ArrayList<CerUnidadeCompetencialVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_CERT_CENTRO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " where cod_certificado = '" + codCertificado + "' " 
                    +" and cod_procedimiento = 'CEPAP' "
                    + " and num_expediente = '" + numExp + "'";
            
            log.error("MeLanbide37DAO.getCodigosUnidades() sql: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){
                CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                    unidad.setCodUnidad(rs.getString("COD_UNIDAD"));
                    unidad.setCentroAcreditado(rs.getString("CENTRO_ACREDITADO"));
                    if(rs.getString("COD_CENTRO") != null){
                        unidad.setCodCentro(rs.getString("COD_CENTRO"));
                        //unidad.setDescCentro(getDescripcionCentro(unidad.getCodCentro(), con));
                        unidad.setDescCentro("");
                    }//if(rs.getString("COD_CENTRO") != null)
                    unidad.setCodOrigenAcred(rs.getString("COD_ORIGEN_ACREDITACION"));
                    unidad.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                    unidad.setCodMotNoAcreditado(rs.getString("COD_MOTIVO_ACREDITADO"));
                    String claveRegistral = rs.getString("CLAVE_REGISTRAL");
                    unidad.setClaveRegistral("");
                    unidad.setCodCertificado(rs.getString("COD_CERTIFICADO"));
                     unidad.setCodOrganizacion(rs.getString("COD_ORGANIZACION"));
                      unidad.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                    if(claveRegistral!=null && !"".equals(claveRegistral) && !"null".equalsIgnoreCase(claveRegistral))
                        unidad.setClaveRegistral(claveRegistral);
                                        
                unidadesAcreditadas.add(unidad);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error eliminando los centros ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally 
        if(log.isDebugEnabled()) log.error("getCodigosUnidades() : END");
        return unidadesAcreditadas;
    }//getCodigosUnidadesYaAcreditadas
    
    private String getDescripcionCentro (String codCentro, Connection con) throws Exception
    {
        if(log.isDebugEnabled()) log.error("getDescripcionCentro () : BEGIN");
        String valor = "";
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select TITULAR from "
                    + GlobalNames.ESQUEMA_GENERICO 
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide37.CERT_CENTROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " where COD_CENTRO = '" + codCentro + "'";
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                valor = rs.getString("TITULAR");
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la descripción de un centro ", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.error("getDescripcionCentro () : END");
        return valor;
    }//getDescripcionCentro
    
    /**
     * Actualiza las unidades de competencia  de un expediente.
     * Primero elimina todas las que hay y luego crea las que hay en la lista.
     * @param unidades
     * @param con
     * @throws SQLException 
     */
    public int actualizarClaveCentro (CerUnidadeCompetencialVO unidad, String clave, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.error("actualizarClaveCentro() : BEGIN");       
        String query = null;
        Statement st = null;
        int result = 0;
        try{
            
            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_CERT_CENTRO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                    + " set CLAVE_REGISTRAL = '"+clave
                    + "' where COD_ORGANIZACION = '"+unidad.getCodOrganizacion()+"'"
                    + " and COD_PROCEDIMIENTO = '"+unidad.getCodProcedimiento()+"'"
                    + " and NUM_EXPEDIENTE = '"+unidad.getNumExpediente()+"'"
                    + " and COD_CERTIFICADO = '"+unidad.getCodCertificado()+"'"
                    + " and COD_UNIDAD = '"+unidad.getCodUnidad()+"'"
                    ;
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        }catch(SQLException ex){
            log.error("Se ha producido un error actualizando las unidades competenciales", ex);
            throw ex;
        }finally{
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.error("actualizarClaveCentro() : END");
        return result;
    }//actualizarCentros
    
     public List<SelectItem> getListaDesplegable( String codDesplegable, int idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();        
        try {
            String query = "select DES_VAL_COD, DES_NOM from "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_VALORES_DESPLEGABLE, ConstantesMeLanbide37.FICHERO_PROPIEDADES)
                            +" where DES_COD LIKE '"+codDesplegable+"' ";

             if(log.isDebugEnabled()) 
                    log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            SelectItem si = null;/*new SelectItem();
            si.setId(-1);
            si.setLabel("");
            lista.add(si);*/
            String id = null;
            String nombre = null;
            MeLanbide37Manager clase = new MeLanbide37Manager();
            while(rs.next())
            {
                id = rs.getString("DES_VAL_COD");                
                if(!rs.wasNull())
                {
                    
                    nombre = clase.getDescripcionDesplegableByIdioma(idioma, rs.getString("DES_NOM"));
                    si = new SelectItem();
                    si.setId(id);
                    si.setLabel(nombre);
                    lista.add(si);
                }
            }
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la lista desplegable ", e);
            throw new Exception(e);             
        } finally {
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resulset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();            
        }
       return lista;
    }
    
     
    public List<FilaListadoCPVO> getDatosListadoCP(String tipoAcreditacion, String valoracion, String codigoCP, String fecDesde, String fecHasta,Connection con) throws Exception
    {
        List<FilaListadoCPVO> retList = new ArrayList<FilaListadoCPVO>();
        
        Statement st = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try
        {
            
            String query = 
                    "select * from("+
                "select distinct EXP_NUM AS NUMEXP, "
                    + "TO_CHAR(FECHA.TFE_VALOR, 'dd/mm/yyyy')  AS FECSOLICITUD,"
                    + "TIPOAC.TDE_VALOR as TIPOACREDITACION, VALOR.DES_NOM AS VALORACION,"
                    + "CENTRO.TDE_VALOR AS CENTROEXP,"
                    + "CLAVE.TXT_VALOR AS CLAVEREGISTRAL," 
                    + "aa.cod_certificado as CODIGOCP, DESCERTIFICADO_C ,DESCERTIFICADO_E,  DECRETO, FECHA_RD, DECRETO_MOD, FECHA_MODIF_RD, "
                    + "tmp.doc as NIFINTERESADO, tmp.nombre as NOMINTERESADO, TO_CHAR(FECNACIMIENTO, 'dd/mm/yyyy') as FECNACIMIENTO, " 
                    + "TML_VALOR AS TRAMITE, "
                    + " COD_MODULO, DESMODULO_C, DESMODULO_E, " 
                                + " CASE WHEN MODULO_ACREDITADO=0 then MOTACREDMOD.DES_NOM WHEN MODULO_ACREDITADO=1 THEN MOTNOACREDMOD.DES_NOM ELSE '' END AS MOTIVOACREDITADOMOD," 
                                + "  case when MODULO_ACREDITADO=0 then 'SI' WHEN MODULO_ACREDITADO=1 THEN 'NO' ELSE ''  END AS MODULOACREDITADO, "
                    // NUEVO DATOS ORIGEN GAITUZ Y ENVIADO SILCOI 24062015
                    + " DES_SINO.DES_NOM AS ORIGEN_GAITUZ, ENVIADOSILCOI.TNU_VALOR AS ENVIADO_SILCOI "
                    + ", DATOS_TH.DNN_PRV COD_PROVINCIA, DATOS_TH.PRV_NOM PROVINCIA, DATOS_TH.DNN_MUN COD_MUNICIPIO, DATOS_TH.MUN_NOM MUNICIPIO, "
                    + " TIT.TDE_VALOR TIT_RECOGIDO "
                + "FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPEDIENTES, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" " 
                + "LEFT JOIN (" 
                      + "SELECT TFE_VALOR,TFE_NUM FROM E_TFE " 
                      + "WHERE TFE_COD='FECHAPRESENTACION' " 
                      + ") FECHA ON FECHA.TFE_NUM=EXP_NUM "   
                + "LEFT JOIN ( "
                     + "SELECT DES_NOM, TDET_NUM, TDET_VALOR  FROM E_TDET, E_TCA, E_DES_VAL WHERE  TDET_MUN = TCA_MUN AND TDET_COD = TCA_COD AND TCA_DESPLEGABLE = DES_COD AND TDET_VALOR = DES_VAL_COD AND TDET_COD='VALORACION2' "
                      + ") VALOR ON VALOR.TDET_NUM=EXP_NUM "
                + "LEFT JOIN ( "
                    + " SELECT TDE_VALOR, TDE_NUM  FROM E_TDE,E_PCA, E_DES_VAL WHERE  TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD AND TDE_VALOR = DES_VAL_COD AND TDE_COD='CENTRO' "
                 + ") CENTRO ON CENTRO.TDE_NUM=EXP_NUM "
                + "LEFT JOIN E_TXT CLAVE  ON CLAVE.TXT_NUM=EXP_NUM AND TXT_COD='CLAVEREGISTRALCP' "
                + "LEFT join ( "
                    + "SELECT TDE_VALOR,TDE_NUM FROM E_TDE,E_PCA, E_DES_VAL  "
                    + "WHERE TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD "
                    + "AND TDE_VALOR = DES_VAL_COD AND TDE_COD='TIPOACREDITACION' "
                  + ") TIPOAC on TIPOAC.TDE_NUM=EXP_NUM "               
                + "left join  (    "
                   //   + "  select ext_num, ter_doc,ter_noc, valor AS FECNACIMIENTO "
                   //   + "from (SELECT ext_num ,ext_ter FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPTERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" where ext_rol = 1 )ext    "
                   //   + "inner join (SELECT ter_cod,ter_doc, ter_noc FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" ) ter      on ter.ter_cod = ext.ext_ter   "
                    
                    + " select ext_num,"
                    + " CASE WHEN t.ter_cod is not null then t.ter_doc  else hte_doc end as doc,"
                    + " CASE WHEN t.ter_cod is not null then t.ter_noc  else hte_noc end as nombre, "
                    + " valor AS FECNACIMIENTO "
                    + " FROM "
                        + "(SELECT ext_num ,ext_ter,EXT_NVR FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPTERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" WHERE ext_rol = 1   )ext "
                        + "left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" t on t.ter_cod = ext_ter and ter_nve = ext_nvr "
                        + "left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_HIST_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" h on h.hte_ter = ext_ter and hte_nvr = ext_nvr "
                    
                      + "LEFT JOIN T_CAMPOS_FECHA ON COD_TERCERO=ext_ter AND COD_CAMPO='TFECNACIMIENTO'   "            
                + ")tmp   on tmp.ext_num = exp_num   "
                + "LEFT JOIN ( select cod_certificado, num_expediente, DESCERTIFICADO_C, DESCERTIFICADO_E,    "
                    + " DECRETO, TO_CHAR(FECHA_RD, 'dd/mm/yyyy')as  FECHA_RD, DECRETO_MOD, TO_CHAR(FECHA_MODIF_RD, 'dd/mm/yyyy') as FECHA_MODIF_RD   "
                    + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_CERTIFICADO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+"  "
                    + " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_S_CERTIFICADOS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" on CODCERTIFICADO=cod_certificado  "
                + " )aa  on aa.num_expediente=exp_num  " 
                    
                + " left join e_cro on CRO_NUM=EXP_NUM  AND CRO_FEF IS NULL "
                + " LEFT JOIN E_TRA ON TRA_MUN=CRO_MUN AND  TRA_PRO=CRO_PRO AND TRA_COD=CRO_TRA "
                + " LEFT JOIN E_TML ON TML_MUN=TRA_MUN AND TML_PRO=TRA_PRO AND TML_TRA=TRA_COD "     
                    
                    //nuevo -  info módulo
                    + " LEFT JOIN MELANBIDE03_MOD_PRACT MP on mp.NUM_EXPEDIENTE=EXP_NUM and MP.cod_certificado=aa.cod_certificado "
                    + " left join S_CER_MODULOS_FORMATIVOS on CODMODULO=COD_MODULO "
                    + " LEFT JOIN E_DES_VAL  MOTACREDMOD ON MOTACREDMOD.DES_VAL_COD=MP.COD_MOTIVO_ACREDITADO AND MOTACREDMOD.DES_COD='ACCF' "
                    + " LEFT JOIN E_DES_VAL  MOTNOACREDMOD ON MOTNOACREDMOD.DES_VAL_COD=MP.COD_MOTIVO_ACREDITADO AND MOTNOACREDMOD.DES_COD='NOAM' "
                    
                    // NUEVOS DATOS ORIGEN GAITUZ Y ENVIADO SILCOI 24062015
                    + " LEFT JOIN E_TDE ORIGAITUZ ON ORIGAITUZ.TDE_MUN=E_EXP.EXP_MUN AND ORIGAITUZ.TDE_EJE=E_EXP.EXP_EJE AND ORIGAITUZ.TDE_NUM=E_EXP.EXP_NUM AND ORIGAITUZ.TDE_COD='ORIGAITUZ' "
                    + " LEFT JOIN E_DES_VAL DES_SINO ON DES_SINO.DES_COD='SINO' AND DES_SINO.DES_VAL_COD=ORIGAITUZ.TDE_VALOR "
                    + " LEFT JOIN E_TNU ENVIADOSILCOI ON ENVIADOSILCOI.TNU_MUN=E_EXP.EXP_MUN AND ENVIADOSILCOI.TNU_EJE=E_EXP.EXP_EJE AND ENVIADOSILCOI.TNU_NUM=E_EXP.EXP_NUM AND ENVIADOSILCOI.TNU_COD='ENVSILCOI' "
                    
                    // NUEVO INCLUIR PROVINCIA Y MUNICIPIO 2016-01-19
                    + " LEFT JOIN ( SELECT E_EXT.EXT_MUN, E_EXT.EXT_EJE, E_EXT.EXT_PRO, E_EXT.EXT_NUM,E_EXT.EXT_DOT, E_EXT.EXT_TER, E_EXT.EXT_NVR "
                    + "                          , T_DNN.DNN_PRV, DATOS_PROV_MUN.PRV_NOM, T_DNN.DNN_MUN, DATOS_PROV_MUN.MUN_NOM "
                    + "                   FROM E_EXT "
                    + "                   LEFT JOIN T_DOT ON T_DOT.DOT_TER=E_EXT.EXT_TER AND T_DOT.DOT_DOM=E_EXT.EXT_DOT "
                    + "                   LEFT JOIN (SELECT T_DNN.DNN_DOM,T_DNN.DNN_PRV,T_DNN.DNN_MUN,T_DNN.DNN_CPO,T_DNN.DNN_DMC "
                    + "                               FROM T_DNN "
                    + "                               ) T_DNN "
                    + "                   ON T_DOT.DOT_DOM = T_DNN.DNN_DOM AND E_EXT.EXT_DOT =T_DNN.DNN_DOM "
                    + "                   LEFT JOIN (SELECT PRO.PRV_COD, PRO.PRV_NOM,MUN.MUN_COD, MUN.MUN_NOM "
                    + "                             FROM FLBGEN.T_PRV PRO "
                    + "                             INNER JOIN FLBGEN.T_MUN MUN ON PRO.PRV_COD=MUN.MUN_PRV "
                    + "                             WHERE MUN_PAI=108 ORDER BY PRV_COD, PRV_NOM, MUN_COD, MUN_NOM) DATOS_PROV_MUN "
                    + "                   ON DATOS_PROV_MUN.PRV_COD=T_DNN.DNN_PRV AND DATOS_PROV_MUN.MUN_COD=T_DNN.DNN_MUN "
                    + "                   WHERE E_EXT.EXT_PRO='CEPAP') DATOS_TH "
                    + "             ON E_EXP.EXP_MUN=DATOS_TH.EXT_MUN AND E_EXP.EXP_EJE=DATOS_TH.EXT_EJE AND E_EXP.EXP_PRO=DATOS_TH.EXT_PRO AND E_EXP.EXP_NUM=DATOS_TH.EXT_NUM "
                
                // CAMPO DESPLEGABLE TITULO ENTREGADO - RECOGIDO
                + " LEFT JOIN E_TDE TIT ON EXP_NUM=TIT.TDE_NUM and TIT.TDE_COD='TITENTREG' "
                    
                + " WHERE EXP_PRO ='CEPAP'  ";
            
                if(tipoAcreditacion!=null && !tipoAcreditacion.equals(""))
                {
                    query += " AND TIPOAC.TDE_VALOR = ? "; 
                }
                if(valoracion!=null && !valoracion.equals(""))
                {
                    query += " AND VALOR.TDET_VALOR = ? ";
                }
                // KEPA
                if (codigoCP!=null && !codigoCP.equals("")){
                    query += " AND aa.cod_certificado = ?";
                }
                if(fecDesde!=null && !fecDesde.equals(""))
                {
                    query += "  and FECHA.TFE_VALOR>= ? ";
                }
                if(fecHasta!=null && !fecHasta.equals(""))
                {
                    query += " and FECHA.TFE_VALOR<= ? ";
                }
                query += " ORDER BY EXP_NUM ";
                // limite de 50.000 registros
                query += ")WHERE ROWNUM<=50000 ";
                
                pstmt = con.prepareStatement(query);
                
                int x = 1;
                if(tipoAcreditacion!=null && !tipoAcreditacion.equals(""))
                {
                    pstmt.setString(x, tipoAcreditacion);
                    x++;
                }
                if(valoracion!=null && !valoracion.equals(""))
                {
                    pstmt.setString(x, valoracion);
                    x++;
                }
                // KEPA
                if (codigoCP!=null && !codigoCP.equals("")){
                    pstmt.setString(x, codigoCP);
                    x++;
                }
                if(fecDesde!=null && !fecDesde.equals(""))
                {
                    Date fec = new Date(fecDesde);
                    pstmt.setDate(x, new java.sql.Date(fec.getTime()));
                    x++;
                }
                if(fecHasta!=null && !fecHasta.equals(""))
                {
                    Date fec = new Date(fecHasta);
                    pstmt.setDate(x, new java.sql.Date(fec.getTime()));
                    x++;
                }
                
//                + (tipoAcreditacion!=null && !tipoAcreditacion.equals("")?" AND TIPOAC.TDE_VALOR ='"+tipoAcreditacion+"'":" ")
//                + (valoracion!=null && !valoracion.equals("")?" AND VALOR.TDET_VALOR ='"+valoracion+"'":" ")
//                + (codigoCP!=null && !codigoCP.equals("") ?" AND "+
//                    " aa.cod_certificado = '"+codigoCP+"'":" ") 
//                +   (fecDesde!=null && !fecDesde.equals("")?" AND "+
//                    " FECHA.TFE_VALOR>=TO_DATE('"+fecDesde+"', 'dd/mm/yyyy')":"")
//                +   (fecHasta!=null && !fecHasta.equals("")?" AND "+
//                    "FECHA.TFE_VALOR<=TO_DATE('"+fecHasta+"', 'dd/mm/yyyy')":"")
//                +" ORDER BY EXP_NUM ";
           
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            //st = con.createStatement();
            rs = pstmt.executeQuery();
            retList = MeLanbide37InformeUtils.getInstance().extraerDatosListadoCP(rs);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos del Listado CP", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null)
                rs.close();
        }
        return retList;
    } 
    
     public List<FilaListadoCompCPVO> getDatosListadoCompCP(String codigoCP, String fecDesde, String fecHasta,Connection con) throws Exception
    {
        List<FilaListadoCompCPVO> retList = new ArrayList<FilaListadoCompCPVO>();
        
        Statement st = null;
        ResultSet rs = null;
        try
        {
           String query = "select * from("+
                "select distinct EXP_NUM AS NUMEXP, "
                    + "TO_CHAR(FECHA.TFE_VALOR, 'dd/mm/yyyy')  AS FECSOLICITUD,"
                    + "VALOR.DES_NOM AS VALORACION,"
                    + "CENTRO.TDE_VALOR AS CENTROEXP,"
                    + "CLAVE.TXT_VALOR AS CLAVEREGISTRAL," 
                    + "aa.cod_certificado as CODIGOCP, DESCERTIFICADO_C ,DESCERTIFICADO_E,  DECRETO, FECHA_RD, DECRETO_MOD, FECHA_MODIF_RD, "
                    + "tmp.ter_noc as NOMINTERESADO, TO_CHAR(FECNACIMIENTO, 'dd/mm/yyyy') as FECNACIMIENTO " 
                + "FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPEDIENTES, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" " 
                + "LEFT JOIN (" 
                      + "SELECT TFE_VALOR,TFE_NUM FROM E_TFE " 
                      + "WHERE TFE_COD='FECHAPRESENTACION' " 
                      + ") FECHA ON FECHA.TFE_NUM=EXP_NUM "   
                + "LEFT JOIN ( "
                     + "SELECT DES_NOM, TDET_NUM, TDET_VALOR  FROM E_TDET, E_TCA, E_DES_VAL WHERE  TDET_MUN = TCA_MUN AND TDET_COD = TCA_COD AND TCA_DESPLEGABLE = DES_COD AND TDET_VALOR = DES_VAL_COD AND TDET_COD='VALORACION2' "
                      + ") VALOR ON VALOR.TDET_NUM=EXP_NUM "
                + "LEFT JOIN ( "
                    + " SELECT TDE_VALOR, TDE_NUM  FROM E_TDE,E_PCA, E_DES_VAL WHERE  TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD AND TDE_VALOR = DES_VAL_COD AND TDE_COD='CENTRO' "
                 + ") CENTRO ON CENTRO.TDE_NUM=EXP_NUM "
                + "LEFT JOIN E_TXT CLAVE  ON CLAVE.TXT_NUM=EXP_NUM AND TXT_COD='CLAVEREGISTRALCP' "
                + "INNER join ( "
                    + "SELECT TDE_VALOR,TDE_NUM FROM E_TDE,E_PCA, E_DES_VAL  "
                    + "WHERE TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD "
                    + "AND TDE_VALOR = DES_VAL_COD AND TDE_COD='TIPOACREDITACION' AND TDE_VALOR IN ('CP','APA')  "
                  + ") TIPOAC on TIPOAC.TDE_NUM=EXP_NUM "
                + "LEFT join ( "
                     + "SELECT TDET_VALOR,TDET_NUM FROM E_TDET,E_TCA, E_DES_VAL WHERE TDET_MUN = TCA_MUN AND TDET_COD = TCA_COD AND TCA_DESPLEGABLE = DES_COD  AND TDET_VALOR = DES_VAL_COD AND TDET_COD='VALORACION2' AND TDET_VALOR='C' "
                + ") VAL ON  VAL.TDET_NUM=EXP_NUM  "
                + "left join  (    "
                      + "  select ext_num, ter_noc, valor AS FECNACIMIENTO "
                      + "from (SELECT ext_num ,ext_ter FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPTERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" where ext_rol = 1 )ext    "
                      + "inner join (SELECT ter_cod,ter_noc FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" ) ter      on ter.ter_cod = ext.ext_ter   "
                      + "LEFT JOIN T_CAMPOS_FECHA ON COD_TERCERO=ter.ter_cod AND COD_CAMPO='TFECNACIMIENTO'   "            
                + ")tmp   on tmp.ext_num = exp_num   "
                + "LEFT JOIN ( select cod_certificado, num_expediente, DESCERTIFICADO_C, DESCERTIFICADO_E,    "
                    + " DECRETO, TO_CHAR(FECHA_RD, 'dd/mm/yyyy')as  FECHA_RD, DECRETO_MOD, TO_CHAR(FECHA_MODIF_RD, 'dd/mm/yyyy') as FECHA_MODIF_RD   "
                    + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_CERTIFICADO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+"  "
                    + " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_S_CERTIFICADOS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" on CODCERTIFICADO=cod_certificado  "
                + " )aa  on aa.num_expediente=exp_num  "
                + " WHERE EXP_PRO ='CEPAP'  "
                + (codigoCP!=null && !codigoCP.equals("") ?" AND "+
                    " aa.cod_certificado = '"+codigoCP+"'":" ") 
                +   (fecDesde!=null && !fecDesde.equals("")?" AND "+
                    " FECHA.TFE_VALOR>=TO_DATE('"+fecDesde+"', 'dd/mm/yyyy')":"")
                +   (fecHasta!=null && !fecHasta.equals("")?" AND "+
                    "FECHA.TFE_VALOR<=TO_DATE('"+fecHasta+"', 'dd/mm/yyyy')":"") 
                + " and (TIPOAC.TDE_VALOR='CP' OR (TIPOAC.TDE_VALOR='APA' AND VAL.TDET_VALOR IS NOT NULL)) "
                +   " ORDER BY EXP_NUM)WHERE ROWNUM<=50000 ";  
            // limite de 50.000 registros
          //      query += ")WHERE ROWNUM<=50000 ";
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            retList = MeLanbide37InformeUtils.getInstance().extraerDatosListadoCompCP(rs);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos del Listado CP", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null)
                rs.close();
        }
        return retList;
    } 
     
      public List<FilaListadoCompAPAVO> getDatosListadoCompAPA(String codigoCP, String fecDesde, String fecHasta,Connection con) throws Exception
    {
        log.error(" getDatosListadoCompAPA - BEGIN - ");
        List<FilaListadoCompAPAVO> retList = new ArrayList<FilaListadoCompAPAVO>();
        
        //Statement st = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        //ResultSet rs2 = null;
        //ResultSet rs3 = null;
        try
        {
            
            /*String query="SELECT E_EXP.EXP_NUM NUMEXP" +
                        "      , TIPOACREDITACION.TDE_VALOR TIPO " +
                        "      , TO_CHAR(FECHAPRESENTACION.TFE_VALOR,'dd/mm/yyyy') FECSOLICITUD" +
                        "      , VALORACION2.TDET_VALOR VALORACION" +
                        "      , CENTRO_EXPTE.TDE_VALOR CENTROEXP" +
                        "      , CLAVEREGISTRALCP.TXT_VALOR CLAVEREGISTRAL" +
                        "      , DATOS_MP_UC.COD_CERTIFICADO CODIGOCP" +
                        "      , DATOS_MP_UC.DESCERTIFICADO_C" +
                        "      , DATOS_MP_UC.DESCERTIFICADO_E" +
                        "      , DATOS_MP_UC.DECRETO" +
                        "      , DATOS_MP_UC.FECHA_RD" +
                        "      , DATOS_MP_UC.DECRETO_MOD" +
                        "      , DATOS_MP_UC.FECHA_MODIF_RD" +
                        "      , DATOS_MP_UC.CLAVE_REGISTRAL_APA CLAVEREGISTRAL_UC" +
                        "      , DATOS_MP_UC.COD_UNIDAD UNIDADCOMPETENCIA" +
                        "      , DATOS_MP_UC.DESUNIDAD_C, DATOS_MP_UC.DESUNIDAD_E" +
                        "      , DATOS_MP_UC.COD_CENTRO CENTROUC" +
                        "      , CASE WHEN DATOS_MP_UC.CENTRO_ACREDITADO=0 THEN 'SI' WHEN DATOS_MP_UC.CENTRO_ACREDITADO=1 THEN 'NO' ELSE '' END CENTRO_ACREDITADO" +
                        "      , DATOS_MP_UC.COD_ORIGEN_ACREDITACION" +
                        "      , TERCERO.DOCUMENTO_TERCERO NIFINTERESADO" +
                        "      , TERCERO.NOMBRE_COMPLETO_TERCERO NOMINTERESADO" +
                        "      , TERCERO.FECHA_NACI_TERCERO FECNACIMIENTO" +
                        "      , DATOS_TRAMITE.TRAMITE " +
                        "      , ORIGAITUZ.TDE_VALOR ORIGEN_GAITUZ" +
                        "      , ENVSILCOI.TNU_VALOR ENVIADO_SILCOI" +
                        " FROM E_EXP" +
                        " LEFT JOIN E_EXT ON E_EXP.EXP_NUM=E_EXT.EXT_NUM " +
                        " LEFT JOIN E_TFE FECHAPRESENTACION ON FECHAPRESENTACION.TFE_MUN=E_EXP.EXP_MUN AND FECHAPRESENTACION.TFE_EJE=E_EXP.EXP_EJE AND FECHAPRESENTACION.TFE_NUM=E_EXP.EXP_NUM and FECHAPRESENTACION.TFE_COD='FECHAPRESENTACION'" +
                        " LEFT JOIN E_TDE TIPOACREDITACION ON TIPOACREDITACION.TDE_MUN=E_EXP.EXP_MUN AND TIPOACREDITACION.TDE_EJE=E_EXP.EXP_EJE AND TIPOACREDITACION.TDE_NUM=E_EXP.EXP_NUM AND TIPOACREDITACION.TDE_COD='TIPOACREDITACION' " +
                        " LEFT JOIN E_TDE CENTRO_EXPTE ON CENTRO_EXPTE.TDE_MUN=E_EXP.EXP_MUN AND CENTRO_EXPTE.TDE_EJE=E_EXP.EXP_EJE AND CENTRO_EXPTE.TDE_NUM=E_EXP.EXP_NUM AND CENTRO_EXPTE.TDE_COD='CENTRO' " +
                        " LEFT JOIN E_TXT CLAVEREGISTRALCP ON CLAVEREGISTRALCP.TXT_MUN=E_EXP.EXP_MUN AND CLAVEREGISTRALCP.TXT_EJE=E_EXP.EXP_EJE AND CLAVEREGISTRALCP.TXT_NUM=E_EXP.EXP_NUM AND CLAVEREGISTRALCP.TXT_COD='CLAVEREGISTRALCP' " +
                        " LEFT JOIN E_TDET VALORACION2 ON VALORACION2.TDET_MUN=E_EXP.EXP_MUN AND VALORACION2.TDET_EJE=E_EXP.EXP_EJE AND VALORACION2.TDET_NUM=E_EXP.EXP_NUM and VALORACION2.TDET_COD='VALORACION2' AND VALORACION2.TDET_PRO='CEPAP'" +
                        " LEFT JOIN E_TNU ENVSILCOI ON ENVSILCOI.TNU_MUN=E_EXP.EXP_MUN AND ENVSILCOI.TNU_EJE=E_EXP.EXP_EJE AND ENVSILCOI.TNU_NUM=E_EXP.EXP_NUM AND ENVSILCOI.TNU_COD='ENVSILCOI' " +
                        " LEFT JOIN E_TDE ORIGAITUZ ON ORIGAITUZ.TDE_MUN=E_EXP.EXP_MUN AND ORIGAITUZ.TDE_EJE=E_EXP.EXP_EJE AND ORIGAITUZ.TDE_NUM=E_EXP.EXP_NUM AND ORIGAITUZ.TDE_COD='ORIGAITUZ' " +
                        " LEFT JOIN (SELECT HTE_TER CODIGO_TERCERO, HTE_NVR VERSION_TERCERO, HTE_TID TIPO_DOC_TERCERO, HTE_DOC DOCUMENTO_TERCERO, HTE_AP1 APELLIDO1_TERCERO,HTE_AP2 APELLIDO2_TERCERO, HTE_NOM NOMBRE_TERCERO, HTE_NOC NOMBRE_COMPLETO_TERCERO, HTE_TLF TELEFONO_TERCERO" +
                        "             , TO_CHAR(TFECNACIMIENTO.VALOR,'dd/mm/yyyy') FECHA_NACI_TERCERO, TSEXOTERCERO.VALOR SEXO_TERCERO" +
                        "             ,TNACIONTERCERO.VALOR NACIONALIDAD_TERCERO" +
                        "          FROM T_HTE" +
                        "          LEFT JOIN T_CAMPOS_FECHA TFECNACIMIENTO ON TFECNACIMIENTO.COD_TERCERO=T_HTE.HTE_TER AND TFECNACIMIENTO.COD_CAMPO='TFECNACIMIENTO' " +
                        "          LEFT JOIN T_CAMPOS_DESPLEGABLE TSEXOTERCERO ON TSEXOTERCERO.COD_TERCERO=T_HTE.HTE_TER AND TSEXOTERCERO.COD_CAMPO='TSEXOTERCERO' " +
                        "          LEFT JOIN T_CAMPOS_DESPLEGABLE TNACIONTERCERO ON TNACIONTERCERO.COD_TERCERO=T_HTE.HTE_TER AND TNACIONTERCERO.COD_CAMPO='TNACIONTERCERO' " +
                        "          ) TERCERO" +
                        "    ON TERCERO.CODIGO_TERCERO=E_EXT.EXT_TER AND TERCERO.VERSION_TERCERO=E_EXT.EXT_NVR" +
                        " LEFT JOIN (SELECT MELANBIDE03_CERTIFICADO.COD_CERTIFICADO, MELANBIDE03_CERTIFICADO.NUM_EXPEDIENTE, MELANBIDE03_CERTIFICADO.COD_ORGANIZACION, MELANBIDE03_CERTIFICADO.COD_PROCEDIMIENTO " +
                        "                , MELANBIDE03_MOD_PRACT.COD_MODULO, MELANBIDE03_MOD_PRACT.MODULO_ACREDITADO, MELANBIDE03_MOD_PRACT.COD_MOTIVO_ACREDITADO" +
                        "                , DATOS_UC.COD_UNIDAD, DATOS_UC.COD_CENTRO, DATOS_UC.CENTRO_ACREDITADO, DATOS_UC.COD_MOTIVO_NO_ACREDITADO_UC" +
                        "                , DATOS_UC.COD_ORIGEN_ACREDITACION, DATOS_UC.CLAVE_REGISTRAL CLAVE_REGISTRAL_APA, DATOS_UC.DESUNIDAD_C, DATOS_UC.DESUNIDAD_E" +
                        "                , S_CER_CERTIFICADOS.DESCERTIFICADO_C, S_CER_CERTIFICADOS.DESCERTIFICADO_E,S_CER_CERTIFICADOS.DECRETO,TO_CHAR(S_CER_CERTIFICADOS.FECHA_RD,'dd/mm/yyyy') FECHA_RD,S_CER_CERTIFICADOS.DECRETO_MOD,TO_CHAR(S_CER_CERTIFICADOS.FECHA_MODIF_RD,'dd/mm/yyyy') FECHA_MODIF_RD" +
                        "          FROM " +
                        "          MELANBIDE03_CERTIFICADO" +
                        "          LEFT JOIN (SELECT COD_CERTIFICADO, NUM_EXPEDIENTE, COD_ORGANIZACION, COD_PROCEDIMIENTO, COD_UNIDAD, COD_ORIGEN_ACREDITACION, CLAVE_REGISTRAL, COD_CENTRO, CENTRO_ACREDITADO, COD_MOTIVO_ACREDITADO COD_MOTIVO_NO_ACREDITADO_UC" +
                        "                            , S_CER_UNIDADES_COMPETENCIA.DESUNIDAD_C, S_CER_UNIDADES_COMPETENCIA.DESUNIDAD_E" +
                        "                         FROM MELANBIDE03_CERT_CENTRO" +
                        "                      LEFT JOIN S_CER_UNIDADES_COMPETENCIA ON MELANBIDE03_CERT_CENTRO.COD_UNIDAD=S_CER_UNIDADES_COMPETENCIA.CODUNIDAD) DATOS_UC" +
                        "               ON DATOS_UC.COD_CERTIFICADO=MELANBIDE03_CERTIFICADO.COD_CERTIFICADO AND DATOS_UC.NUM_EXPEDIENTE=MELANBIDE03_CERTIFICADO.NUM_EXPEDIENTE AND DATOS_UC.COD_ORGANIZACION=MELANBIDE03_CERTIFICADO.COD_ORGANIZACION AND DATOS_UC.COD_PROCEDIMIENTO=MELANBIDE03_CERTIFICADO.COD_PROCEDIMIENTO" +
                        "          LEFT JOIN MELANBIDE03_MOD_PRACT ON MELANBIDE03_CERTIFICADO.COD_ORGANIZACION=MELANBIDE03_MOD_PRACT.COD_ORGANIZACION AND MELANBIDE03_CERTIFICADO.COD_PROCEDIMIENTO=MELANBIDE03_MOD_PRACT.COD_PROCEDIMIENTO AND MELANBIDE03_CERTIFICADO.NUM_EXPEDIENTE=MELANBIDE03_MOD_PRACT.NUM_EXPEDIENTE AND MELANBIDE03_CERTIFICADO.COD_CERTIFICADO=MELANBIDE03_MOD_PRACT.COD_CERTIFICADO AND MELANBIDE03_CERTIFICADO.COD_PROCEDIMIENTO=MELANBIDE03_MOD_PRACT.COD_PROCEDIMIENTO" +
                        "          LEFT JOIN S_CER_CERTIFICADOS ON MELANBIDE03_CERTIFICADO.COD_CERTIFICADO=S_CER_CERTIFICADOS.CODCERTIFICADO" +
                        "          ) DATOS_MP_UC" +
                        "    ON DATOS_MP_UC.NUM_EXPEDIENTE=E_EXP.EXP_NUM AND DATOS_MP_UC.COD_ORGANIZACION=E_EXP.EXP_MUN AND DATOS_MP_UC.COD_PROCEDIMIENTO=E_EXP.EXP_PRO" +
                        " LEFT JOIN (SELECT E_CRO.CRO_MUN, E_CRO.CRO_PRO, E_CRO.CRO_EJE, E_CRO.CRO_NUM,E_CRO.CRO_TRA, E_TML.TML_VALOR TRAMITE  " +
                        "            FROM E_CRO " +
                        "            INNER JOIN E_TML ON E_TML.TML_MUN=E_CRO.CRO_MUN AND E_TML.TML_PRO=E_CRO.CRO_PRO AND E_TML.TML_TRA=E_CRO.CRO_TRA AND TML_CMP='NOM'" +
                        "            WHERE E_CRO.CRO_PRO='CEPAP' AND E_CRO.CRO_FEF IS NULL" +
                        "            ) DATOS_TRAMITE" +
                        "      ON DATOS_TRAMITE.CRO_MUN=E_EXP.EXP_MUN AND DATOS_TRAMITE.CRO_PRO=E_EXP.EXP_PRO AND DATOS_TRAMITE.CRO_EJE=E_EXP.EXP_EJE AND DATOS_TRAMITE.CRO_NUM=E_EXP.EXP_NUM" +
                        " WHERE E_EXP.EXP_PRO='CEPAP' AND E_EXT.EXT_PRO='CEPAP'" +
                        "      AND E_EXP.EXP_EST!=1" +
                        "      AND DATOS_MP_UC.COD_UNIDAD IS NOT NULL"
                 */                           
                   /*     String query="SELECT MELANBIDE03_CERT_CENTRO.NUM_EXPEDIENTE NUMEXP" +
                        "      , TIPOACREDITACION.TDE_VALOR TIPO " +
                        "      , TO_CHAR(FECHAPRESENTACION.TFE_VALOR,'dd/mm/yyyy') FECSOLICITUD" +
                        "      , VALORACION2.TDET_VALOR VALORACION" +
                        "      , CENTRO_EXTTE.TDE_VALOR CENTROEXP" +
                        "      , CLAVEREGISTRALCP.TXT_VALOR CLAVEREGISTRAL" +
                        "      , MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO CODIGOCP" +
                    *//*    "      , DATOS_CP_UC.DESCERTIFICADO_C DESCERTIFICADO_C" +
                        "      , DATOS_CP_UC.DESCERTIFICADO_E DESCERTIFICADO_E" +
                        "      , DATOS_CP_UC.DECRETO DECRETO" +
                        "      , DATOS_CP_UC.FECHA_RD FECHA_RD" +
                        "      , DATOS_CP_UC.DECRETO_MOD DECRETO_MOD, DATOS_CP_UC.FECHA_MODIF_RD FECHA_MODIF_RD" +
                    *//*  "      , S_CER_CERTIFICADOS.DESCERTIFICADO_C, S_CER_CERTIFICADOS.DESCERTIFICADO_E,S_CER_CERTIFICADOS.DECRETO, S_CER_CERTIFICADOS.FECHA_RD, S_CER_CERTIFICADOS.DECRETO_MOD, S_CER_CERTIFICADOS.FECHA_MODIF_RD " +
                        "      , MELANBIDE03_CERT_CENTRO.CLAVE_REGISTRAL CLAVEREGISTRAL_UC" +
                        "      , MELANBIDE03_CERT_CENTRO.COD_UNIDAD UNIDADCOMPETENCIA" +
                        //    "      , DATOS_CP_UC.DESUNIDAD_C DESUNIDAD_C, DATOS_CP_UC.DESUNIDAD_E DESUNIDAD_E" +
                        "      , S_CER_UNIDADES_COMPETENCIA.DESUNIDAD_C, S_CER_UNIDADES_COMPETENCIA.DESUNIDAD_E " +
                        "      , MELANBIDE03_CERT_CENTRO.COD_CENTRO CENTROUC" +
                        "      , CASE WHEN MELANBIDE03_CERT_CENTRO.CENTRO_ACREDITADO=0 THEN 'SI' WHEN MELANBIDE03_CERT_CENTRO.CENTRO_ACREDITADO=1 THEN 'NO' ELSE '' END CENTRO_ACREDITADO" +
                        "      , MELANBIDE03_CERT_CENTRO.COD_ORIGEN_ACREDITACION COD_ORIGEN_ACREDITACION" +
                        "      , TERCERO.DOCUMENTO_TERCERO NIFINTERESADO" +
                        "      , TERCERO.NOMBRE_COMPLETO_TERCERO NOMINTERESADO" +
                        "      , TERCERO.FECHA_NACI_TERCERO FECNACIMIENTO" +
                        "      , DATOS_TRAMITE.TRAMITE TRAMITE " +
                        "      , ORIGAITUZ.TDE_VALOR ORIGEN_GAITUZ" +
                        "      , ENVSILCOI.TNU_VALOR ENVIADO_SILCOI " +
                        " FROM MELANBIDE03_CERT_CENTRO" +
                   */    //" INNER JOIN FLBGEN.CP_UC ON MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO=FLBGEN.CP_UC.CODCERTIFICADO AND MELANBIDE03_CERT_CENTRO.COD_UNIDAD=FLBGEN.CP_UC.UNIDADCOMPETENCIA " +
                   /*     //" INNER JOIN FLBGEN.S_CER_CERTIFICADOS ON FLBGEN.S_CER_CERTIFICADOS.CODCERTIFICADO=FLBGEN.CP_UC.CODCERTIFICADO " +
                        //" INNER JOIN FLBGEN.S_CER_UNIDADES_COMPETENCIA ON FLBGEN.S_CER_UNIDADES_COMPETENCIA.CODUNIDAD=FLBGEN.CP_UC.UNIDADCOMPETENCIA AND FLBGEN.CP_UC.UNIDADCOMPETENCIA LIKE 'UC%' " +
                        " LEFT JOIN S_CER_CERTIFICADOS ON S_CER_CERTIFICADOS.CODCERTIFICADO=MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO " +
                        " LEFT JOIN S_CER_UNIDADES_COMPETENCIA ON S_CER_UNIDADES_COMPETENCIA.CODUNIDAD=MELANBIDE03_CERT_CENTRO.COD_UNIDAD AND S_CER_UNIDADES_COMPETENCIA.CODUNIDAD LIKE 'UC%' " +         
                        " INNER JOIN E_EXT ON E_EXT.EXT_NUM=MELANBIDE03_CERT_CENTRO.NUM_EXPEDIENTE AND E_EXT.EXT_PRO=MELANBIDE03_CERT_CENTRO.COD_PROCEDIMIENTO AND E_EXT.EXT_MUN=MELANBIDE03_CERT_CENTRO.COD_ORGANIZACION AND E_EXT.EXT_PRO='CEPAP' " +
                        " INNER JOIN (SELECT HTE_TER CODIGO_TERCERO, HTE_NVR VERSION_TERCERO, HTE_TID TIPO_DOC_TERCERO, HTE_DOC DOCUMENTO_TERCERO, HTE_AP1 APELLIDO1_TERCERO,HTE_AP2 APELLIDO2_TERCERO, HTE_NOM NOMBRE_TERCERO, HTE_NOC NOMBRE_COMPLETO_TERCERO, HTE_TLF TELEFONO_TERCERO" +
                        "             , TO_CHAR(TFECNACIMIENTO.VALOR,'dd/mm/yyyy') FECHA_NACI_TERCERO" +
                    */    //"             --, TSEXOTERCERO.VALOR SEXO_TERCERO" +
                        // "             --,TNACIONTERCERO.VALOR NACIONALIDAD_TERCERO" +
                   //     "          FROM T_HTE" +
                   //     "          LEFT JOIN T_CAMPOS_FECHA TFECNACIMIENTO ON TFECNACIMIENTO.COD_TERCERO=T_HTE.HTE_TER AND TFECNACIMIENTO.COD_CAMPO='TFECNACIMIENTO' " +
                        //"          --LEFT JOIN T_CAMPOS_DESPLEGABLE TSEXOTERCERO ON TSEXOTERCERO.COD_TERCERO=T_HTE.HTE_TER AND TSEXOTERCERO.COD_CAMPO='TSEXOTERCERO' " +
                        //"          --LEFT JOIN T_CAMPOS_DESPLEGABLE TNACIONTERCERO ON TNACIONTERCERO.COD_TERCERO=T_HTE.HTE_TER AND TNACIONTERCERO.COD_CAMPO='TNACIONTERCERO' " +
                    /*    "          ) TERCERO" +
                        "    ON TERCERO.CODIGO_TERCERO=E_EXT.EXT_TER AND TERCERO.VERSION_TERCERO=E_EXT.EXT_NVR" +
                        " LEFT JOIN (SELECT E_CRO.CRO_MUN, E_CRO.CRO_PRO, E_CRO.CRO_EJE, E_CRO.CRO_NUM,E_CRO.CRO_TRA, E_TML.TML_VALOR TRAMITE  " +
                        "            FROM E_CRO " +
                        "            INNER JOIN E_TML ON E_TML.TML_MUN=E_CRO.CRO_MUN AND E_TML.TML_PRO=E_CRO.CRO_PRO AND E_TML.TML_TRA=E_CRO.CRO_TRA AND TML_CMP='NOM' AND E_TML.TML_PRO='CEPAP'" +
                        "            WHERE E_CRO.CRO_PRO='CEPAP' AND E_TML.TML_PRO='CEPAP' AND E_CRO.CRO_FEF IS NULL" +
                        "            ) DATOS_TRAMITE" +
                        "    ON DATOS_TRAMITE.CRO_MUN=E_EXT.EXT_MUN AND DATOS_TRAMITE.CRO_PRO=E_EXT.EXT_PRO AND DATOS_TRAMITE.CRO_EJE=E_EXT.EXT_EJE AND DATOS_TRAMITE.CRO_NUM=E_EXT.EXT_NUM" +
                    */  /*" INNER JOIN (SELECT FLBGEN.CP_UC.CODCERTIFICADO COD_CERTIFICADO, FLBGEN.S_CER_CERTIFICADOS.DESCERTIFICADO_C, FLBGEN.S_CER_CERTIFICADOS.DESCERTIFICADO_E, FLBGEN.S_CER_CERTIFICADOS.DECRETO, FLBGEN.S_CER_CERTIFICADOS.FECHA_RD, FLBGEN.S_CER_CERTIFICADOS.DECRETO_MOD, FLBGEN.S_CER_CERTIFICADOS.FECHA_MODIF_RD " +
                        "                   , FLBGEN.CP_UC.UNIDADCOMPETENCIA COD_UNIDAD, FLBGEN.S_CER_UNIDADES_COMPETENCIA.DESUNIDAD_C, FLBGEN.S_CER_UNIDADES_COMPETENCIA.DESUNIDAD_E " +
                        "            FROM FLBGEN.CP_UC " +
                        "            INNER JOIN FLBGEN.S_CER_CERTIFICADOS ON FLBGEN.S_CER_CERTIFICADOS.CODCERTIFICADO=FLBGEN.CP_UC.CODCERTIFICADO " +
                        "            INNER JOIN FLBGEN.S_CER_UNIDADES_COMPETENCIA ON FLBGEN.S_CER_UNIDADES_COMPETENCIA.CODUNIDAD=FLBGEN.CP_UC.UNIDADCOMPETENCIA AND FLBGEN.CP_UC.UNIDADCOMPETENCIA LIKE 'UC%' " +
                        "            ) DATOS_CP_UC" +
                        "  ON DATOS_CP_UC.COD_CERTIFICADO=MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO AND DATOS_CP_UC.COD_UNIDAD=MELANBIDE03_CERT_CENTRO.COD_UNIDAD" +
                        *//*" LEFT JOIN E_TFE FECHAPRESENTACION ON FECHAPRESENTACION.TFE_MUN=E_EXT.EXT_MUN AND FECHAPRESENTACION.TFE_EJE=E_EXT.EXT_EJE AND FECHAPRESENTACION.TFE_NUM=E_EXT.EXT_NUM and FECHAPRESENTACION.TFE_COD='FECHAPRESENTACION'  AND  UTL_MATCH.JARO_WINKLER(FECHAPRESENTACION.TFE_NUM, 'CEPAP')>=0.63" +
                        " LEFT JOIN E_TDE TIPOACREDITACION ON TIPOACREDITACION.TDE_MUN=E_EXT.EXT_MUN AND TIPOACREDITACION.TDE_EJE=E_EXT.EXT_EJE AND TIPOACREDITACION.TDE_NUM=E_EXT.EXT_NUM AND TIPOACREDITACION.TDE_COD='TIPOACREDITACION' AND  UTL_MATCH.JARO_WINKLER(TIPOACREDITACION.TDE_NUM, 'CEPAP')>=0.63" +
                        " LEFT JOIN E_TDE CENTRO_EXTTE ON CENTRO_EXTTE.TDE_MUN=E_EXT.EXT_MUN AND CENTRO_EXTTE.TDE_EJE=E_EXT.EXT_EJE AND CENTRO_EXTTE.TDE_NUM=E_EXT.EXT_NUM AND CENTRO_EXTTE.TDE_COD='CENTRO' AND  UTL_MATCH.JARO_WINKLER_SIMILARITY(CENTRO_EXTTE.TDE_NUM, 'CEPAP')>=0.63" +
                        " LEFT JOIN E_TXT CLAVEREGISTRALCP ON CLAVEREGISTRALCP.TXT_MUN=E_EXT.EXT_MUN AND CLAVEREGISTRALCP.TXT_EJE=E_EXT.EXT_EJE AND CLAVEREGISTRALCP.TXT_NUM=E_EXT.EXT_NUM AND CLAVEREGISTRALCP.TXT_COD='CLAVEREGISTRALCP'  AND UTL_MATCH.JARO_WINKLER(CLAVEREGISTRALCP.TXT_NUM, 'CEPAP')>=0.63" +
                        " LEFT JOIN E_TDET VALORACION2 ON VALORACION2.TDET_MUN=E_EXT.EXT_MUN AND VALORACION2.TDET_EJE=E_EXT.EXT_EJE AND VALORACION2.TDET_NUM=E_EXT.EXT_NUM and VALORACION2.TDET_COD='VALORACION2' AND VALORACION2.TDET_PRO='CEPAP'" +
                        " LEFT JOIN E_TNU ENVSILCOI ON ENVSILCOI.TNU_MUN=E_EXT.EXT_MUN AND ENVSILCOI.TNU_EJE=E_EXT.EXT_EJE AND ENVSILCOI.TNU_NUM=E_EXT.EXT_NUM AND ENVSILCOI.TNU_COD='ENVSILCOI' AND  UTL_MATCH.JARO_WINKLER(ENVSILCOI.TNU_NUM, 'CEPAP')>=0.63" +
                        " LEFT JOIN E_TDE ORIGAITUZ ON ORIGAITUZ.TDE_MUN=E_EXT.EXT_MUN AND ORIGAITUZ.TDE_EJE=E_EXT.EXT_EJE AND ORIGAITUZ.TDE_NUM=E_EXT.EXT_NUM AND ORIGAITUZ.TDE_COD='ORIGAITUZ' AND  UTL_MATCH.JARO_WINKLER(ORIGAITUZ.TDE_NUM, 'CEPAP')>=0.63" +
                        " WHERE E_EXT.EXT_PRO='CEPAP' AND MELANBIDE03_CERT_CENTRO.NUM_EXPEDIENTE LIKE E_EXT.EXT_EJE || '%' "
                        + (codigoCP!=null && !codigoCP.equals("") ?" AND  MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO = '"+codigoCP+"'":" ")                     
                        +   (fecDesde!=null && !fecDesde.equals("")?" AND FECHAPRESENTACION.TFE_VALOR>=TO_DATE('"+fecDesde+"', 'dd/mm/yyyy')":"")
                        +   (fecHasta!=null && !fecHasta.equals("")?" AND FECHAPRESENTACION.TFE_VALOR<=TO_DATE('"+fecHasta+"', 'dd/mm/yyyy')":"")              
                        + " ORDER BY  NUMEXP, CODIGOCP, UNIDADCOMPETENCIA, TRAMITE"
                    ;
            */        
            // Leemos de la vista materializada
                    String query ="select * from("
                            + "select distinct NUMEXP" 
                            +",TIPO" 
                            +",FECSOLICITUD"
                            +",VALORACION"
                            +",CENTROEXP"
                            +",CLAVEREGISTRAL"
                            +",CODIGOCP"
                            +",DESCERTIFICADO_C, DESCERTIFICADO_E,DECRETO,FECHA_RD,DECRETO_MOD,FECHA_MODIF_RD"
                            +",CLAVEREGISTRAL_UC"
                            +",UNIDADCOMPETENCIA"
                            +",DESUNIDAD_C,DESUNIDAD_E"
                            +",CENTROUC" 
                            +",CENTRO_ACREDITADO"
                            +",COD_ORIGEN_ACREDITACION" 
                            +",NIFINTERESADO" 
                            +",NOMINTERESADO"
                            +",FECNACIMIENTO" 
                            +",TRAMITE" 
                            +",ORIGEN_GAITUZ"
                            +",ENVIADO_SILCOI"
                            +" from CEPAP_LISTADOS_TODAS_UC " 
                            +" where NUMEXP is not null"
                            + (codigoCP!=null && !codigoCP.equals("") ?" AND  CODIGOCP = '"+codigoCP+"'":" ")                     
                            + (fecDesde!=null && !fecDesde.equals("")?" AND FECSOLICITUD>=TO_DATE('"+fecDesde+"', 'dd/mm/yyyy')":"")
                            + (fecHasta!=null && !fecHasta.equals("")?" AND FECSOLICITUD<=TO_DATE('"+fecHasta+"', 'dd/mm/yyyy')":"")              
                            + " ORDER BY  NUMEXP, CODIGOCP, UNIDADCOMPETENCIA, TRAMITE"
                              + ")WHERE ROWNUM<=50000 ";
                            
            
          /*  String query = "select distinct EXP_NUM AS NUMEXP, TIPOAC.TDE_VALOR AS TIPO, "
                                +" TO_CHAR(FECHA.TFE_VALOR, 'dd/mm/yyyy')  AS FECSOLICITUD, "
                                +" VALOR.DES_NOM AS VALORACION, "
                                +" CENTRO.TDE_VALOR AS CENTROEXP, "
                                +" CLAVE.TXT_VALOR AS CLAVEREGISTRAL, "
                                +" aa.cod_certificado as CODIGOCP, DESCERTIFICADO_C ,DESCERTIFICADO_E,  DECRETO, TO_CHAR(FECHA_RD, 'dd/mm/yyyy') as FECHA_RD, DECRETO_MOD,  "
                                +" TO_CHAR(FECHA_MODIF_RD, 'dd/mm/yyyy') as FECHA_MODIF_RD, CLAVE_REGISTRAL AS CLAVEREGISTRAL_UC, UNIDADCOMPETENCIA, DESUNIDAD_C,  "
                                +" DESUNIDAD_E, COD_CENTRO AS CENTROUC, "
                                +" CASE WHEN CENTRO_ACREDITADO=0 THEN 'SI' WHEN CENTRO_ACREDITADO=1 THEN 'NO' ELSE '' END AS CENTRO_ACREDITADO, "
                                +" COD_ORIGEN_ACREDITACION,"
                                +" tmp.doc as NIFINTERESADO, tmp.nombre as NOMINTERESADO,TO_CHAR(FECNACIMIENTO, 'dd/mm/yyyy') as FECNACIMIENTO,  "
                                +" TML_VALOR AS TRAMITE, "
                                + " DES_SINO.DES_NOM AS ORIGEN_GAITUZ, ENVIADOSILCOI.TNU_VALOR AS ENVIADO_SILCOI "
            */        
                                /* //modulo practicas
                                 + " ,COD_MODULO, DESMODULO_C, DESMODULO_E, " 
                                + " CASE WHEN MODULO_ACREDITADO=0 then MOTACREDMOD.DES_NOM WHEN MODULO_ACREDITADO=1 THEN MOTNOACREDMOD.DES_NOM ELSE '' END AS MOTIVOACREDITADOMOD," 
                                + "  case when MODULO_ACREDITADO=0 then 'SI' WHEN MODULO_ACREDITADO=1 THEN 'NO' ELSE ''  END AS MODULOACREDITADO " */
            /*                
                            +" FROM  "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPEDIENTES, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+"  "
                            +" LEFT JOIN ( "
                              +" SELECT TFE_VALOR,TFE_NUM FROM E_TFE  "
                              +" WHERE TFE_COD='FECHAPRESENTACION'  "
                              +" ) FECHA ON FECHA.TFE_NUM=EXP_NUM  "
                            +" LEFT JOIN ( "
                                +"   SELECT DES_NOM, TDET_NUM, TDET_VALOR  FROM E_TDET, E_TCA, E_DES_VAL WHERE  TDET_MUN = TCA_MUN AND TDET_COD = TCA_COD AND TCA_DESPLEGABLE = DES_COD AND TDET_VALOR = DES_VAL_COD AND TDET_COD='VALORACION2' "
                            +" ) VALOR ON VALOR.TDET_NUM=EXP_NUM "
                            +" LEFT JOIN ( "
                                  +" SELECT TDE_VALOR, TDE_NUM  FROM E_TDE,E_PCA, E_DES_VAL WHERE  TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD AND TDE_VALOR = DES_VAL_COD AND TDE_COD='CENTRO' "
                                +" ) CENTRO ON CENTRO.TDE_NUM=EXP_NUM "
                            +" LEFT JOIN E_TXT CLAVE  ON CLAVE.TXT_NUM=EXP_NUM AND TXT_COD='CLAVEREGISTRALCP' "
                            +" left join   "
                                +" (    "                    
                                    + " select ext_num,"
                                    + " CASE WHEN t.ter_cod is not null then t.ter_doc  else hte_doc end as doc,"
                                    + " CASE WHEN t.ter_cod is not null then t.ter_noc  else hte_noc end as nombre, "
                                    + " valor AS FECNACIMIENTO "
                                    + " FROM "
                                        + "(SELECT ext_num ,ext_ter,EXT_NVR,ext_rol FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPTERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" WHERE ext_rol = 1   )ext "
                                        + "left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" t on t.ter_cod = ext_ter and ter_nve = ext_nvr "
                                        + "left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_HIST_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" h on h.hte_ter = ext_ter and hte_nvr = ext_nvr "
                    
                                  //+" select ext_num, ter_doc, ter_noc, valor AS FECNACIMIENTO    "
                                  //+" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_EXPTERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" ext    "
                                 // +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.TABLA_TERCEROS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" ter      on ter.ter_cod = ext.ext_ter   "
                                  +" LEFT JOIN T_CAMPOS_FECHA ON COD_TERCERO=ext_ter AND COD_CAMPO='TFECNACIMIENTO'       where ext.ext_rol = 1     "
                                +" )tmp     on tmp.ext_num = exp_num   "
                            +" LEFT JOIN  "
                                +" (  "
                                  +" select cod_certificado, num_expediente, DESCERTIFICADO_C, DESCERTIFICADO_E,   DECRETO, FECHA_RD, DECRETO_MOD, FECHA_MODIF_RD   "
                                  +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_CERTIFICADO, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+"   "
                                +"   inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide37.MELANBIDE37_S_CERTIFICADOS, ConstantesMeLanbide37.FICHERO_PROPIEDADES)+" on CODCERTIFICADO=cod_certificado  "
                                +" )aa on aa.num_expediente=exp_num   "
                    
                            +" LEFT JOIN S_CER_REL_MOD_CERT MC ON MC.CODCERTIFICADO=aa.COD_CERTIFICADO "
                            +" LEFT JOIN S_CER_MODULOS_FORMATIVOS M ON MC.CODMODULO=M.CODMODULO AND M.UNIDADCOMPETENCIA IS NOT NULL "
                    
                            +" LEFT JOIN melanbide03_cert_centro cc on aa.COD_CERTIFICADO=cc.cod_certificado and exp_num=cc.num_expediente  AND cc.cod_unidad=M.UNIDADCOMPETENCIA  "
                            +" LEFT JOIN S_CER_UNIDADES_COMPETENCIA uc on M.UNIDADCOMPETENCIA=uc.CODUNIDAD   "
                            +" INNER join ( "
                                  +" SELECT TDE_VALOR, TDE_NUM FROM E_TDE,E_PCA, E_DES_VAL  "
                                  +" WHERE TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD AND PCA_DESPLEGABLE = DES_COD "
                                  +" AND TDE_VALOR = DES_VAL_COD AND TDE_COD='TIPOACREDITACION' AND TDE_VALOR IN ('CP','APA')  "
                                  +" ) TIPOAC on TIPOAC.TDE_NUM=EXP_NUM "
                            +" LEFT join ( "
                                  +" SELECT TDET_VALOR, TDET_NUM FROM E_TDET,E_TCA, E_DES_VAL WHERE TDET_MUN = TCA_MUN AND TDET_COD = TCA_COD AND TCA_DESPLEGABLE = DES_COD  AND TDET_VALOR = DES_VAL_COD AND TDET_COD='VALORACION2' AND TDET_VALOR in ('P','T') "
                                +" ) VAL ON  VAL.TDET_NUM=EXP_NUM  "
                         //   +" inner join (select cod_certificado,num_expediente from melanbide03_certificado )cert on cert.num_expediente=exp_num "
                                        
                            + " left join e_cro on CRO_NUM=EXP_NUM  AND CRO_FEF IS NULL "
                            + " LEFT JOIN E_TRA ON TRA_MUN=CRO_MUN AND  TRA_PRO=CRO_PRO AND TRA_COD=CRO_TRA "
                            + " LEFT JOIN E_TML ON TML_MUN=TRA_MUN AND TML_PRO=TRA_PRO AND TML_TRA=TRA_COD "     
            */        
                    /*//NUEVO MODULO PARCTICAS
                   
                    + " LEFT JOIN MELANBIDE03_MOD_PRACT MP on MP.NUM_EXPEDIENTE=EXP_NUM and MP.cod_certificado=aa.cod_certificado "                   
                    + " LEFT JOIN E_DES_VAL  MOTACREDMOD ON MOTACREDMOD.DES_VAL_COD=MP.COD_MOTIVO_ACREDITADO AND MOTACREDMOD.DES_COD='ACCF' "
                    + " LEFT JOIN E_DES_VAL  MOTNOACREDMOD ON MOTNOACREDMOD.DES_VAL_COD=MP.COD_MOTIVO_ACREDITADO AND MOTNOACREDMOD.DES_COD='NOAM' "
                      */      
                    
                    // NUEVOS DATOS ORIGEN GAITUZ Y ENVIADO SILCOI 24062015
            /*
                    + " LEFT JOIN E_TDE ORIGAITUZ ON ORIGAITUZ.TDE_MUN=E_EXP.EXP_MUN AND ORIGAITUZ.TDE_EJE=E_EXP.EXP_EJE AND ORIGAITUZ.TDE_NUM=E_EXP.EXP_NUM AND ORIGAITUZ.TDE_COD='ORIGAITUZ' "
                    + " LEFT JOIN E_DES_VAL DES_SINO ON DES_SINO.DES_COD='SINO' AND DES_SINO.DES_VAL_COD=ORIGAITUZ.TDE_VALOR "
                    + " LEFT JOIN E_TNU ENVIADOSILCOI ON ENVIADOSILCOI.TNU_MUN=E_EXP.EXP_MUN AND ENVIADOSILCOI.TNU_EJE=E_EXP.EXP_EJE AND ENVIADOSILCOI.TNU_NUM=E_EXP.EXP_NUM AND ENVIADOSILCOI.TNU_COD='ENVSILCOI' "
                    
                    +" WHERE  "
                                  +" EXP_PRO ='CEPAP'  AND M.UNIDADCOMPETENCIA IS NOT NULL  "
                                  //+" and (  (TIPOAC.TDE_VALOR='CP' AND VAL.TDET_VALOR ='P') OR (TIPOAC.TDE_VALOR='APA' AND VAL.TDET_VALOR IS NOT NULL) ) "
                                + (codigoCP!=null && !codigoCP.equals("") ?" AND  aa.cod_certificado = '"+codigoCP+"'":" ")                     
                                +   (fecDesde!=null && !fecDesde.equals("")?" AND FECHA.TFE_VALOR>=TO_DATE('"+fecDesde+"', 'dd/mm/yyyy')":"")
                                +   (fecHasta!=null && !fecHasta.equals("")?" AND FECHA.TFE_VALOR<=TO_DATE('"+fecHasta+"', 'dd/mm/yyyy')":"")              
                                +" ORDER BY EXP_NUM ";
            */
           /*
           String sql="SELECT MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO, FLBGEN.CP_UC.CODCERTIFICADO, MELANBIDE03_CERT_CENTRO.COD_UNIDAD, FLBGEN.CP_UC.UNIDADCOMPETENCIA FROM MELANBIDE03_CERT_CENTRO "
                   +"INNER JOIN FLBGEN.CP_UC ON MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO=FLBGEN.CP_UC.CODCERTIFICADO AND MELANBIDE03_CERT_CENTRO.COD_UNIDAD=FLBGEN.CP_UC.UNIDADCOMPETENCIA "
                   +"INNER JOIN FLBGEN.S_CER_CERTIFICADOS ON FLBGEN.S_CER_CERTIFICADOS.CODCERTIFICADO=FLBGEN.CP_UC.CODCERTIFICADO "
                   +"INNER JOIN FLBGEN.S_CER_UNIDADES_COMPETENCIA ON FLBGEN.S_CER_UNIDADES_COMPETENCIA.CODUNIDAD=FLBGEN.CP_UC.UNIDADCOMPETENCIA AND FLBGEN.CP_UC.UNIDADCOMPETENCIA LIKE 'UC%' ";
                   */
            
            //final String typeName = "prueba_reg";
            //final String typeTableName = "lista_prueba_reg";
            //Object[] atributos = new Object[]{"NUMEXP","TIPO","FECSOLICITUD","VALORACION","CENTROEXP","CLAVEREGISTRAL","CODIGOCP","DESCERTIFICADO_C","DESCERTIFICADO_E","DECRETO","FECHA_RD","DECRETO_MOD","FECHA_MODIF_RD","CLAVEREGISTRAL_UC","UNIDADCOMPETENCIA","DESUNIDAD_C","DESUNIDAD_E","CENTROUC","CENTRO_ACREDITADO","COD_ORIGEN_ACREDITACION","NIFINTERESADO","NOMINTERESADO","FECNACIMIENTO","TRAMITE","ORIGEN_GAITUZ","ENVIADO_SILCOI"};
            //Connection	orInrDgConn  = ((org.apache.commons.dbcp.DelegatingConnection) con).getInnermostDelegate(); 
            //final StructDescriptor structDescriptor = StructDescriptor.createDescriptor(typeName,orInrDgConn);
            //ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor(typeTableName, orInrDgConn);
            //final ResultSetMetaData metaData = structDescriptor.getMetaData();
            //String uhno="-";
        /*
            CallableStatement cs=null;
            cs = con.prepareCall("{call CEPAP_LISTA_TODAS_UC.CEPAP_RECUPERA_TODASUC_1(?,?,?,?)}");
            log.error("NoError - prueba :  {call CEPAP_LISTA_TODAS_UC.CEPAP_RECUPERA_TODASUC_1(?,?,?,?)} ");
            cs.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.ARRAY,typeTableName.toUpperCase());
            cs.setString(2,(codigoCP != null ? codigoCP : "0"));
            cs.setString(3, (fecDesde != null ? fecDesde : "0"));
            cs.setString(4, (fecHasta != null ? fecHasta : "0"));
            
            //cs.registerOutParameter(1,oracle.jdbc.driver.OracleTypes.VARCHAR);
            log.error("NoError - registerOutParameter - cs.registerOutParameter(1,Types.ARRAY,typeTableName); - ? " + oracle.jdbc.driver.OracleTypes.ARRAY + '-' +typeTableName.toUpperCase());
            cs.execute();
            //rs = (ResultSet)cs.getObject(1);
            log.error("NoError - getDatosListadoCompAPA - Procedure llamado ");
            Array array1 = cs.getArray(1);
            Object[] arregloDatos = (Object[])array1.getArray();
            if(arregloDatos!=null) 
                log.error("Arreglo de resutados contiene registros : " + arregloDatos.length);
        */
            //String query = "select * from TABLE(CEPAP_LISTA_TODAS_UC.CEPAP_FRECUPERA_TODASUC('0','0','0')) tabla";
            if(log.isDebugEnabled()) 
                log.error("-NoError- Traza Control - sql Todas UC - Desde Vista MAterializada= " + query);
            //st = con.createStatement();
            //st = con.createStatement(sql);
            stmt = con.prepareStatement(query);
            //stmt.setFetchSize(5000);
            log.error("-NoError- Traza Control - antes de ejeucutar executeQuery");
            rs = stmt.executeQuery();
            //log.error("-NoError- Traza Control - ahora obtenemos el rs");
            //rs2 = stmt.getResultSet();
            //log.error("-NoError- Traza Control - rs2? : " + rs2);
            //log.error("-NoError- Traza Control - ahora ejecutamos executequery");
            //rs= stmt.executeQuery();
            //rs = st.executeQuery(sql);
            log.error(" getDatosListadoCompAPA - Consulta ejecutada correctamente - Procedemos a Mapear los datos ");
            retList = MeLanbide37InformeUtils.getInstance().extraerDatosListadoCompAPA(rs);
            log.error(" getDatosListadoCompAPA - extraerDatosListadoCompAPA - Datos Mapeados - Correctamente ");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos del Listado CP", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.error("Procedemos a cerrar el Preparestatement y el resultset");
            //if(st!=null) 
            //    st.close();
            if(stmt!=null) 
                stmt.close();
            if(rs!=null)
                rs.close();
            //if(rs2!=null)
            //    rs2.close();
        }
        log.error(" getDatosListadoCompAPA - END - LineasRecuperadas : " + retList.size());        
        return retList;
    } 
      
    public String actualizaDatosTodasUC_VistaMate(String nombreVistaMaterializada, Connection con) throws SQLException 
    {
        log.error("NoError - actualizaDatosTodasUC_VistaMate - BEGIN - Dao");
        String salida = "";
        CallableStatement cs=null;
        try{
            /*String plsql = "DECLARE " 
                    +"    salida varchar(150):='-1';" 
                    +"    nombre_vista varchar(150);" 
                    +" BEGIN " 
                    +"  BEGIN " 
                    +"   nombre_vista := ?;" 
                    +"   DBMS_MVIEW.REFRESH(nombre_vista);"
                    +"   salida:='0';" 
                    +"   EXCEPTION " 
                    +"    WHEN OTHERS THEN" 
                    +"     salida := '-2_' || SQLCODE || ' - ' || SQLERRM;" 
                    +"  END;"
                    +"  ?:=salida;"
                    +" END;";
            */
            
            cs = con.prepareCall("{call CEPAP_REFRESH_VISTA_TODASUC(?,?)}");
            log.error("NoError - actualizaDatosTodasUC_VistaMate - prepareCall:  {call CEPAP_REFRESH_VISTA_TODASUC(?,?)} ");
            cs.setString(1,nombreVistaMaterializada);
            cs.registerOutParameter(2, Types.VARCHAR);
            log.error("NoError - actualizaDatosTodasUC_VistaMate - ? " + nombreVistaMaterializada);
            cs.execute();
            log.error("NoError - actualizaDatosTodasUC_VistaMate - Procedure Ejecutado ");

            //if(!cs.wasNull()){
               if(cs.getObject(2)!= null){
                   salida = String.valueOf(cs.getObject(2));
                   // 0:OK / -1:Exception no capturada en SQL / -2:EsceptionCapturada en SQL
                   //  -3 No Obtenemos respuesta del Procedure. Execute. / -4 Exception General capturada en Java
                   if(!salida.equals("0")){
                       log.error("Error - actualizaDatosTodasUC_VistaMate - No se pudo actualizar los datos de la vista materializada - El procedure devuelve un error : " 
                           + " - " + salida);    
                   }else{
                       log.error("NoError - actualizaDatosTodasUC_VistaMate - Vista Actualizada Correctamente :  " + salida); 
                   }
               }else{
                   log.error("Error - actualizaDatosTodasUC_VistaMate - No se pudo actualiar los datos, variable de salida del o procedure null ");    
               }
            /*}else{
                log.error("Error - actualizaDatosTodasUC_VistaMate - No se pudo actualizar los datos de la vista materializada - Salida del procedure Null");    
                salida="-3 No Obtenemos respuesta del Procedure. Execute.";
            }*/
            //cs.close();
        }catch(Exception ex){
            log.error("Error - actualizaDatosTodasUC_VistaMate - " + ex.getMessage());  
            salida="-4 Exception General capturada en Java";
        }finally{
            if(cs!=null){
                //if(!cs.isClosed())
                    cs.close();
            }
        }
        log.error("NoError - actualizaDatosTodasUC_VistaMate - Salida : " + salida);
        log.error("NoError - actualizaDatosTodasUC_VistaMate - END - Dao");
        return salida;
    }
      
      public ArrayList<Melanbide37RelacionExpVO> getDatosRelacion (Connection con) 
            throws SQLException, Exception{
        if(log.isDebugEnabled()) log.error("getDatosRelacion() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Melanbide37RelacionExpVO> listaReports = new ArrayList<Melanbide37RelacionExpVO>();
        try{
            String sql = "SELECT DISTINCT EXT_NUM,  EXPE, TFE_VALOR,TER_DOC,  TER_NOM, TER_AP1, TER_AP2 " +
                        ", COD_CERTIFICADO, DESCERTIFICADO_C " +
                        "FROM ( " +
                        "  select EXT_NUM, TER_DOC, DNN_DOM, P.EXPE AS EXPE, P.COD AS COD, " +
                        "  TER_NOM, TER_AP1, TER_AP2, COD_CERTIFICADO, DESCERTIFICADO_C " +
                        "  FROM T_TER  " +
                        "  INNER JOIN T_DNN ON DNN_DOM = TER_DOM " +
                        "  INNER JOIN E_EXT ON TER_COD = EXT_TER " +
                        "  INNER JOIN MELANBIDE03_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM " +
                        "  INNER JOIN S_CER_CERTIFICADOS ON CODCERTIFICADO = COD_CERTIFICADO " +
                        "  INNER JOIN ( " +
                        "      select TER_DOC AS COD, DNN_DOM AS DOM,COD_CERTIFICADO AS CODCER,   " +
                        "      TER_DOC AS DOC, EXT_NUM AS EXPE " +
                        "      FROM T_TER " +
                        "      INNER JOIN T_DNN ON DNN_DOM = TER_DOM " +
                        "      INNER JOIN E_EXT ON TER_COD = EXT_TER  " +
                        "      INNER JOIN MELANBIDE40_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM " +
                        "      WHERE EXT_PRO = 'EMPNL' " +
                        "  ) P ON TER_DOC = P.COD AND COD_CERTIFICADO = CODCER " +
                        "  WHERE EXT_PRO = 'CEPAP' " +
                        ")" +
                        "LEFT JOIN E_TFE ON TFE_NUM = EXT_NUM AND TFE_COD = 'FECHAPRESENTACION' " +
                        "LEFT JOIN E_TDE ON TDE_NUM  = EXT_NUM AND TDE_COD = 'MOTEXENC'  " +
                        "LEFT JOIN E_DES_VAL ON DES_vAL_COD = TDE_VALOR";
            
            if(log.isDebugEnabled()) log.error("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()){
                Melanbide37RelacionExpVO report = new Melanbide37RelacionExpVO();
                report.setCodExpediente(rs.getString("EXT_NUM"));
                report.setCodExpedienteRel(rs.getString("EXPE"));
                report.setFechaSoli(rs.getString("TFE_VALOR"));
                report.setNif(rs.getString("TER_DOC"));
                String nombre = ""; String ape1 = ""; String ape2 ="";
                nombre = rs.getString("TER_NOM")!= null ? rs.getString("TER_NOM"):"";
                ape1 = rs.getString("TER_AP1")!= null ? rs.getString("TER_AP1"):"";
                ape2 = rs.getString("TER_AP2")!= null ? rs.getString("TER_AP2"):"";
                report.setNombreApe(nombre + " " + ape1 + " " + ape2);
                report.setCodigoCP(rs.getString("COD_CERTIFICADO"));
                report.setDescCP(rs.getString("DESCERTIFICADO_C"));
                listaReports.add(report);
            }//while (rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.error("getReports() : END");
        return listaReports;
    }
      
      public ArrayList<FilaListadoExpedientesEMPNLVO> listadoExpedientesEMPNL (Connection con) 
            throws SQLException, Exception{
        if(log.isDebugEnabled()) log.error("listadoExpedientesEMPNL() : BEGIN - DAO -");
        Statement st = null;
        ResultSet rs = null;
        ArrayList<FilaListadoExpedientesEMPNLVO> listaReports = new ArrayList<FilaListadoExpedientesEMPNLVO>();
        try{
            String sql = "SELECT E_EXP.EXP_NUM AS NUM_EXPEDIENTE,"
                    + " E_EXP.EXP_MUN,E_EXP.EXP_EJE,E_EXP.EXP_PRO, "
                    + "       HTE_DOC AS DOCUMENTO,  HTE_NOC AS NOMBRE_COMPLETO, "
                    + "       MOTEXENC.TDE_VALOR AS MOTIVO_EXENCION, "
                    + "       DES_MEXE.DES_NOM AS DESC_MOTIVO_EXENCION, "
                    + "       ORIGSOL.TDE_VALOR AS ORIGEN_SOLICITUD, "
                    + "       DES_ORSO.DES_NOM AS DESC_ORIGEN_SOLICITUD, "
                    + "       CENTRO.TDE_VALOR AS CENTRO, "
                    + "       DES_CENT.DES_NOM AS DESC_CENTRO, "
                    + "       OFICLANBIDE.TDE_VALOR AS OFICINA_LANBIDE, "
                    + "       DES_OFLA.DES_NOM AS DESC_OFICINA_LANBIDE, "
                    + "       TO_CHAR(E_TFE.TFE_VALOR,'dd/mm/yyyy') AS FECHA_SOLICITUD, "
                    + "       E_TDET.TDET_VALOR AS VALORACION_EXENCION, "
                    + "       DES_FADE.DES_NOM AS DESC_VALORACION_EXENCION, "
                    + "       MELANBIDE40_CERTIFICADO.COD_CERTIFICADO,MELANBIDE40_CERTIFICADO.DES_CERTIFICADO_CAS,MELANBIDE40_CERTIFICADO.DES_CERTIFICADO_EUS "
                    + "FROM E_EXP "
                    + "    LEFT JOIN E_EXT ON E_EXP.EXP_NUM=E_EXT.EXT_NUM "
                    + "    LEFT JOIN T_HTE ON E_EXT.EXT_TER=T_HTE.HTE_TER AND E_EXT.EXT_NVR=T_HTE.HTE_NVR "
                    + "    LEFT JOIN E_TFE ON E_TFE.TFE_MUN=E_EXP.EXP_MUN AND E_TFE.TFE_EJE=E_EXP.EXP_EJE AND E_TFE.TFE_NUM=E_EXP.EXP_NUM and E_TFE.TFE_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CAMPO_SUPL_FECHA_PRESENTACION, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "    LEFT JOIN E_TDE MOTEXENC ON MOTEXENC.TDE_MUN=E_EXP.EXP_MUN AND MOTEXENC.TDE_EJE=E_EXP.EXP_EJE AND MOTEXENC.TDE_NUM=E_EXP.EXP_NUM AND MOTEXENC.TDE_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CAMPO_SUPL_MOTIVO_EXENCION, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "    LEFT JOIN E_TDE ORIGSOL ON ORIGSOL.TDE_MUN=E_EXP.EXP_MUN AND ORIGSOL.TDE_EJE=E_EXP.EXP_EJE AND ORIGSOL.TDE_NUM=E_EXP.EXP_NUM AND ORIGSOL.TDE_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CAMPO_SUPL_ORIGEN_SOLICITUD, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "    LEFT JOIN E_TDE CENTRO ON CENTRO.TDE_MUN=E_EXP.EXP_MUN AND CENTRO.TDE_EJE=E_EXP.EXP_EJE AND CENTRO.TDE_NUM=E_EXP.EXP_NUM AND CENTRO.TDE_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CAMPO_SUPL_CENTRO, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "    LEFT JOIN E_TDE OFICLANBIDE ON OFICLANBIDE.TDE_MUN=E_EXP.EXP_MUN AND OFICLANBIDE.TDE_EJE=E_EXP.EXP_EJE AND OFICLANBIDE.TDE_NUM=E_EXP.EXP_NUM AND OFICLANBIDE.TDE_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CAMPO_SUPL_OFICINA_LANBIDE, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "    LEFT JOIN E_TDET ON E_TDET.TDET_MUN=E_EXP.EXP_MUN AND E_TDET.TDET_EJE=E_EXP.EXP_EJE AND E_TDET.TDET_NUM=E_EXP.EXP_NUM AND E_TDET.TDET_TRA=" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CODIGO_INTERNO_TRAMITE_VALORACION, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + " AND E_TDET.TDET_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CAMPO_SUPL_VALORACION_EXENCION, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "    LEFT JOIN E_DES_VAL DES_MEXE ON DES_MEXE.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_COD_DESP_MOTIV_EXENCION, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' AND DES_MEXE.DES_VAL_COD=MOTEXENC.TDE_VALOR "
                    + "    LEFT JOIN E_DES_VAL DES_ORSO ON DES_ORSO.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_COD_DESP_ORIGEN_SOLICITUD, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' AND DES_ORSO.DES_VAL_COD=ORIGSOL.TDE_VALOR "
                    + "    LEFT JOIN E_DES_VAL DES_CENT ON DES_CENT.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_COD_DESP_CENTRO, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' AND DES_CENT.DES_VAL_COD=CENTRO.TDE_VALOR "
                    + "    LEFT JOIN E_DES_VAL DES_OFLA ON DES_OFLA.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_COD_DESP_OFICINA_LANBIDE, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' AND DES_OFLA.DES_VAL_COD=OFICLANBIDE.TDE_VALOR "
                    + "    LEFT JOIN E_DES_VAL DES_FADE ON DES_FADE.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_COD_DESP_FAVORABLE_DESFAVO, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' AND DES_FADE.DES_VAL_COD=E_TDET.TDET_VALOR "
                    + "    LEFT JOIN MELANBIDE40_CERTIFICADO ON MELANBIDE40_CERTIFICADO.NUM_EXPEDIENTE=EXP_NUM "
                    + "WHERE EXP_PRO='" + ConfigurationParameter.getParameter(ConstantesMeLanbide37.EMPNL_CODIGO_PROCEDIMIENTO, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "' "
                    + "ORDER BY E_EXP.EXP_NUM ";
            
            if(log.isDebugEnabled()) log.error("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()){
                FilaListadoExpedientesEMPNLVO report = new FilaListadoExpedientesEMPNLVO();
                report.setCodOrganizacion(rs.getInt("EXP_MUN"));
                report.setEjercicio(rs.getInt("EXP_EJE"));
                report.setCodProcedimiento(rs.getString("EXP_PRO"));
                report.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                report.setDocumento(rs.getString("DOCUMENTO"));
                report.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                report.setMotivoExencion(rs.getString("MOTIVO_EXENCION"));
                report.setDescMotivoExencion(rs.getString("DESC_MOTIVO_EXENCION"));
                report.setOrigenSolicitud(rs.getString("ORIGEN_SOLICITUD"));
                report.setDescOrigenSolicitud(rs.getString("DESC_ORIGEN_SOLICITUD"));
                report.setCentro(rs.getString("CENTRO"));
                report.setDescCentro(rs.getString("DESC_CENTRO"));
                report.setOficinaLanbide(rs.getString("OFICINA_LANBIDE"));
                report.setDescOficinaLanbide(rs.getString("DESC_OFICINA_LANBIDE"));
                report.setFechaSolicitud(rs.getString("FECHA_SOLICITUD"));
                report.setValoracionExencion(rs.getString("VALORACION_EXENCION"));
                report.setDescValoracionExencion(rs.getString("DESC_VALORACION_EXENCION"));
                report.setCodCertificado(rs.getString("COD_CERTIFICADO"));
                report.setDesCertificadoCas(rs.getString("DES_CERTIFICADO_CAS"));
                report.setDesCertificadoEus(rs.getString("DES_CERTIFICADO_EUS"));
                listaReports.add(report);
            }//while (rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente -- listadoExpedientesEMPNL - DAO", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente -- listadoExpedientesEMPNL - DAO", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset -- listadoExpedientesEMPNL - DAO");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.error("listadoExpedientesEMPNL() : END - DAO");
        return listaReports;
    }
      
    public ArrayList<ExpedienteCEPAPVO> getExpedientesOficina(String oficina, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        ArrayList<ExpedienteCEPAPVO> listExpedientes = new ArrayList<ExpedienteCEPAPVO>();
        try {
            query = "SELECT DISTINCT EXT_NUM NUM_EXPEDIENTE, \n" +
                    "       T_HTE.HTE_DOC DOC_INTERESADO, \n" +
                    "       T_HTE.HTE_NOM || ' ' || T_HTE.HTE_AP1 || ' ' || T_HTE.HTE_AP2 NOMBRE_APELLIDOS, \n" +
                    "       MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO CP, \n" +
                    "       tx1.TXT_VALOR LUGAR_ENVIO, \n" +
                    "       tx2.TXT_VALOR LOTE, \n" +
                    "       CASE WHEN t1.TDE_VALOR IS NULL THEN 'N' ELSE t1.TDE_VALOR END TITULO_ENTREGADO,\n" +
                    "       T_HTE.HTE_TLF TELEFONO\n" +
                    "FROM E_EXT  \n" +
                    "INNER JOIN E_EXP ON E_EXP.EXP_PRO=E_EXT.EXT_PRO AND E_EXP.EXP_EJE=E_EXT.EXT_EJE AND E_EXP.EXP_NUM=E_EXT.EXT_NUM AND E_EXP.EXP_MUN=E_EXT.EXT_MUN  \n" +
                    "INNER JOIN E_CRO ON E_CRO.CRO_PRO=E_EXT.EXT_PRO AND E_CRO.CRO_EJE=E_EXT.EXT_EJE AND E_CRO.CRO_NUM=E_EXT.EXT_NUM AND E_CRO.CRO_MUN=E_EXT.EXT_MUN  \n" +
                    "INNER JOIN E_ROL ON E_ROL.ROL_COD=E_EXT.EXT_ROL AND E_ROL.ROL_PCW=1 AND E_ROL.ROL_PRO='CEPAP' \n" +
                    "INNER JOIN T_HTE ON T_HTE.HTE_TER=E_EXT.EXT_TER AND T_HTE.HTE_NVR=E_EXT.EXT_NVR \n" +
                    "INNER JOIN E_TDET ON E_TDET.TDET_NUM=E_EXT.EXT_NUM AND E_TDET.TDET_COD='VALORACION2' \n" +
                    "LEFT JOIN E_TXT tx1 ON tx1.TXT_NUM=E_EXT.EXT_NUM AND tx1.TXT_COD='OFICASIGM' \n" +
                    "LEFT JOIN E_TDE t1 ON t1.TDE_NUM=E_EXT.EXT_NUM AND t1.TDE_COD='TITENTREG' \n" +
                    "LEFT JOIN E_TXT tx2 ON tx2.TXT_NUM=E_EXT.EXT_NUM AND tx2.TXT_COD='LOTE' \n" +
                    "INNER JOIN MELANBIDE03_CERT_CENTRO ON MELANBIDE03_CERT_CENTRO.NUM_EXPEDIENTE=E_EXT.EXT_NUM \n" +
                    "INNER JOIN E_TDE t2 ON t2.TDE_NUM=E_EXT.EXT_NUM AND t2.TDE_COD='TIPOACREDITACION' \n" +
                    "WHERE EXT_PRO='CEPAP' \n" +
                    "AND E_TDET.TDET_VALOR='T'  \n" +
                    "AND t2.TDE_VALOR='CP' \n" +
                    "AND E_CRO.CRO_TRA=  " + ConfigurationParameter.getParameter(ConstantesMeLanbide37.TRAMITE_ENVIO_OFICINA, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "   \n" +
                    "AND tx1.TXT_VALOR like '%" + oficina + "%'  \n" +
                    "ORDER BY EXT_NUM";
            

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                ExpedienteCEPAPVO expediente = new ExpedienteCEPAPVO();
                expediente.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
                expediente.setDocInteresado(rs.getString("DOC_INTERESADO"));
                expediente.setNombreInteresado(rs.getString("NOMBRE_APELLIDOS"));
                expediente.setCP(rs.getString("CP"));
                expediente.setLugarEnvio(rs.getString("LUGAR_ENVIO"));
                expediente.setLote(rs.getString("LOTE"));
                expediente.setTituloEntregado(rs.getString("TITULO_ENTREGADO"));
                expediente.setTelefono(rs.getString("TELEFONO"));
                listExpedientes.add(expediente);
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error en lista de expedientes al filtrar - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listExpedientes;
    }
      
    private String preparaWhereFechas(boolean conWhere, String nombreCampoBD, String fechaInicio, String fechaFin) {
        String where = "";
        if (fechaInicio != null && !fechaInicio.equals("") && fechaFin != null && !fechaFin.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') BETWEEN TO_DATE('" + fechaInicio + "','dd/mm/yyyy') AND TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            } else {
                where += " AND  (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') BETWEEN TO_DATE('" + fechaInicio + "','dd/mm/yyyy') AND TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            }
        } else if (fechaInicio != null && !fechaInicio.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') >= TO_DATE('" + fechaInicio + "','dd/mm/yyyy'))";
            } else {
                where += " AND  (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') >= TO_DATE('" + fechaInicio + "','dd/mm/yyyy'))";
            }
        } else if (fechaFin != null && !fechaFin.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') <= TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            } else {
                where += " AND  (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') <= TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            }
        }
        return where;
    } 
      
    private String preparaClausulaWhere(ExpCEPAPCriteriosFiltroVO _criterioBusqueda) throws Exception {
        String where = "";
        try {
            if (_criterioBusqueda.getDniInteresado() != null && !_criterioBusqueda.getDniInteresado().equals("")) {
                where += " AND  (UPPER(T_HTE.HTE_DOC)='" + _criterioBusqueda.getDniInteresado().toUpperCase() + "')";
            }
            if (_criterioBusqueda.getNumeroExpediente() != null && !_criterioBusqueda.getNumeroExpediente().equals("")) {
                //where += " AND  (UPPER(EXT_NUM) LIKE'%" + _criterioBusqueda.getNumeroExpediente().toUpperCase() + "%')";
                where += " AND  (UPPER(EXT_NUM)='" + _criterioBusqueda.getNumeroExpediente().toUpperCase() + "')";
            }
            // para filtros de Fechas (se elimina)
            /*boolean conClausulaWhere = false;
            where += preparaWhereFechas(conClausulaWhere, "EXP_FEI",
                    _criterioBusqueda.getFechaExpInicio(), _criterioBusqueda.getFechaExpFin());*/
        } catch (Exception ex) {
            log.error("Se ha producido un error preparando la clausula where de la sentencia de consulta con filtros para la lista de expedientes CEPAP", ex);
            throw new Exception(ex);
        }
        return where;
    }
      
    public List<ExpedienteCEPAPVO> busquedaFiltrandoListaExpedientes(ExpCEPAPCriteriosFiltroVO _criterioBusqueda, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        List<ExpedienteCEPAPVO> listExpedientes = new ArrayList<ExpedienteCEPAPVO>();
        try {
            String where1 = preparaClausulaWhere(_criterioBusqueda);
            
            query = "SELECT * FROM (\n" +
                    "    SELECT ROWNUM AS NUMREG ,COUNT(0) OVER (PARTITION BY 0) AS NUMTOTALREGISTROS, DATOS.* FROM (\n" +
                    "        SELECT * FROM (SELECT DISTINCT EXT_NUM NUM_EXPEDIENTE,\n" +
                    "               T_HTE.HTE_DOC DOC_INTERESADO,\n" +
                    "               T_HTE.HTE_NOM || ' ' || T_HTE.HTE_AP1 || ' ' || T_HTE.HTE_AP2 NOMBRE_APELLIDOS,\n" +
                    "               MELANBIDE03_CERT_CENTRO.COD_CERTIFICADO CP,\n" +
                    "               tx1.TXT_VALOR LUGAR_ENVIO,\n" +
                    "               tx2.TXT_VALOR LOTE,\n" +
                    "               CASE WHEN t1.TDE_VALOR IS NULL THEN 'N' ELSE t1.TDE_VALOR END TITULO_ENTREGADO\n" +
                    "        FROM E_EXT \n" +
                    "        INNER JOIN E_EXP ON E_EXP.EXP_PRO=E_EXT.EXT_PRO AND E_EXP.EXP_EJE=E_EXT.EXT_EJE AND E_EXP.EXP_NUM=E_EXT.EXT_NUM AND E_EXP.EXP_MUN=E_EXT.EXT_MUN \n" +
                    "        INNER JOIN E_CRO ON E_CRO.CRO_PRO=E_EXT.EXT_PRO AND E_CRO.CRO_EJE=E_EXT.EXT_EJE AND E_CRO.CRO_NUM=E_EXT.EXT_NUM AND E_CRO.CRO_MUN=E_EXT.EXT_MUN \n" +
                    "        INNER JOIN E_ROL ON E_ROL.ROL_COD=E_EXT.EXT_ROL AND E_ROL.ROL_PCW=1 AND E_ROL.ROL_PRO='CEPAP'\n" +
                    "        INNER JOIN T_HTE ON T_HTE.HTE_TER=E_EXT.EXT_TER AND T_HTE.HTE_NVR=E_EXT.EXT_NVR\n" +
                    "        INNER JOIN E_TDET ON E_TDET.TDET_NUM=E_EXT.EXT_NUM AND E_TDET.TDET_COD='VALORACION2'\n" +
                    "        LEFT JOIN E_TXT tx1 ON tx1.TXT_NUM=E_EXT.EXT_NUM AND tx1.TXT_COD='OFICASIGM'\n" +
                    "        LEFT JOIN E_TDE t1 ON t1.TDE_NUM=E_EXT.EXT_NUM AND t1.TDE_COD='TITENTREG'\n" +
                    "        LEFT JOIN E_TXT tx2 ON tx2.TXT_NUM=E_EXT.EXT_NUM AND tx2.TXT_COD='LOTE'\n" +
                    "        INNER JOIN MELANBIDE03_CERT_CENTRO ON MELANBIDE03_CERT_CENTRO.NUM_EXPEDIENTE=E_EXT.EXT_NUM\n" +
                    "        INNER JOIN E_TDE t2 ON t2.TDE_NUM=E_EXT.EXT_NUM AND t2.TDE_COD='TIPOACREDITACION'\n" +
                    "        WHERE EXT_PRO='CEPAP'\n" +
                    "        AND E_TDET.TDET_VALOR='T'\n" + 
                    //"        AND (E_TDE.TDE_VALOR IS NULL OR E_TDE.TDE_VALOR = 'N')\n" +
                    "        AND t2.TDE_VALOR='CP'\n" +
                    "        AND E_CRO.CRO_TRA=" + 
                    ConfigurationParameter.getParameter(ConstantesMeLanbide37.TRAMITE_ENVIO_OFICINA, ConstantesMeLanbide37.FICHERO_PROPIEDADES) + "\n" +
                    " " + where1 + "\n" +
                    "        ORDER BY EXT_NUM) WHERE ROWNUM <= 100\n" +
                    "    ) DATOS\n" +
                    ")";

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                ExpedienteCEPAPVO expediente = new ExpedienteCEPAPVO();
                expediente.setNoRegEnLaConsulta(rs.getInt("NUMREG"));
                expediente.setNoTotalRegConsulta(rs.getInt("NUMTOTALREGISTROS"));
                expediente.setNumeroExpediente(rs.getString("NUM_EXPEDIENTE"));
                expediente.setNombreInteresado(rs.getString("NOMBRE_APELLIDOS"));
                expediente.setCP(rs.getString("CP"));
                expediente.setLugarEnvio(rs.getString("LUGAR_ENVIO"));
                expediente.setLote(rs.getString("LOTE"));
                expediente.setTituloEntregado(rs.getString("TITULO_ENTREGADO"));
                listExpedientes.add(expediente);
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error en lista de expedientes al filtrar - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listExpedientes;
    }
    
    public int marcarTituloEntregado(int codOrganizacion,int codTramite, String ejercicio, String numExpediente, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        String query = "";
        try {
            query = "MERGE INTO E_TDE USING (SELECT 1 FROM DUAL) ON (TDE_MUN=" + codOrganizacion + " AND TDE_NUM = '" + numExpediente + "' AND TDE_COD = 'TITENTREG') WHEN MATCHED THEN UPDATE SET TDE_VALOR = 'S' \n" +
                    "  WHEN NOT MATCHED THEN INSERT (TDE_MUN, TDE_EJE, TDE_NUM, TDE_COD, TDE_VALOR) VALUES (" + codOrganizacion + ", " + ejercicio + ", '" + numExpediente + "', 'TITENTREG', 'S')";

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error en lista de expedientes a marcar título entregado - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
        return result;
    }
      
    
        public String grabarFechaEntregaTitulo(int codOrganizacion,String numExpediente, String ejercicio, String meLanbide37Fecha_busqS, Connection con) throws SQLException, Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        String fecha = "";
        int result = 0;
        try {

            query= 
                          
            "MERGE INTO E_TFE USING (SELECT 1 FROM DUAL) ON (TFE_MUN=" + codOrganizacion + " AND TFE_NUM = '" + numExpediente + "' AND TFE_COD = 'FECENTTITULO') WHEN MATCHED THEN UPDATE SET TFE_VALOR = TO_DATE('"+ meLanbide37Fecha_busqS +"','dd/MM/yyyy')" +
            "  WHEN NOT MATCHED THEN INSERT (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR) VALUES (" + codOrganizacion + ", " + ejercicio + " ,'" + numExpediente + "', 'FECENTTITULO', TO_DATE('"+ meLanbide37Fecha_busqS +"', 'dd/MM/yyyy'))";
                               
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            result = ps.executeUpdate();
            
           
              } catch (SQLException ex) {
            log.error("Se ha producido un error getcampo FECENTTITULO ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return String.valueOf(result > 0) ;
        }
        
        
}//class
