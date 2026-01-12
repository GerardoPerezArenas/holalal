package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.lanbide.impresion.ImpresionExpedientesLanbideValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CertificacionPositiva;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.MeLanbide03ReportVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.Melanbide03RelacionExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.Participantes;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Clase DAO para las operaciones de la entidad MELANBIDE03_REPORT
 * 
 * @author david.caamano
 * @version 15/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 15-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide03ReportDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03ReportDAO.class);
    
    private static MeLanbide03ReportDAO instance = null;
    
    public static MeLanbide03ReportDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized (MeLanbide03ReportDAO.class){
                if(instance == null){
                    instance = new MeLanbide03ReportDAO();
                }//if(instance == null)
            }//synchronized (MeLanbide03ReportDAO.class)
        }//if(instance == null) 
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    public void insertReport (MeLanbide03ReportVO report, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertReport() : BEGIN");
        try{
            if(log.isDebugEnabled()) log.debug("Comprobamos si existen reports generados anteriormente");
            Boolean existe = existeReport(report, con);
            if(existe){
                if(log.isDebugEnabled()) log.debug("Borramos el report existente");
                deleteReport(report, con);
            }//if(existe)
            if(log.isDebugEnabled()) log.debug("Damos de alta el report");
            altaReport(report, con);
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("insertReport() : END");
    }//insertReport
    
    private void altaReport (MeLanbide03ReportVO report, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("altaReport() : BEGIN");
        PreparedStatement pstmt = null;
        try{
            String sql = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " (NUM_EXPEDIENTE, COD_ORGANIZACION, FECHA_CREACION, NOMBRE, REPORT, MIME_TYPE)"
                    + " values (?, ?, ?, ?, ?, ?)";
            
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, report.getNumExpediente());
            pstmt.setInt(2, report.getCodOrganizacion());
            pstmt.setDate(3, new Date(report.getFechaCreacion().getTime()));
            pstmt.setString(4, report.getNombre());
            pstmt.setBinaryStream(5, new ByteArrayInputStream(report.getReport()), report.getReport().length);
            pstmt.setString(6, report.getMimeType());
                
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            int columnasInsertadas = pstmt.executeUpdate();
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(pstmt!=null) pstmt.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("altaReport() : END");
    }//insertReport
    
    private void deleteReport (MeLanbide03ReportVO report, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("deleteReport() : BEGIN");
        Statement st = null;
        try{
            //Descomponemos el nombre del report para quedarnos con el codigo de nombre que le hayamos puesto no cogiendo el expediente ni fecha
            String nombreReport[] = report.getNombre().split("-");
            
            String sql="Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                   + " where NUM_EXPEDIENTE = '" + report.getNumExpediente() + "' and NOMBRE like '%" + nombreReport[0] + "%'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            int columnasBorradas = st.executeUpdate(sql);
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("deleteReport() : END");
    }//deleteReport
    
    private Boolean existeReport (MeLanbide03ReportVO report, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("existeReport() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        Boolean existe = false;
        try{
            //Descomponemos el nombre del report para quedarnos con el codigo de nombre que le hayamos puesto no cogiendo el expediente ni fecha
            String nombreReport[] = report.getNombre().split("-");
            
            String sql = "Select NOMBRE" 
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " where NOMBRE like '%" + nombreReport[0] + "%' and COD_ORGANIZACION = " + report.getCodOrganizacion()
                    + " and NUM_EXPEDIENTE ='" + report.getNumExpediente() + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                existe = true;
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existeReport() : END");
        return existe;
    }//existeReport
    
    public ArrayList<MeLanbide03ReportVO> getReports (String numExpediente, Integer codIdioma ,Integer codOrganizacion, Connection con) 
            throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getReports() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList<MeLanbide03ReportVO>();
        MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
        try{
            String sql = "Select NOMBRE, FECHA_CREACION, COD_ORGANIZACION, NUM_EXPEDIENTE, MIME_TYPE" 
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " where NUM_EXPEDIENTE = '" + numExpediente + "' and COD_ORGANIZACION = " + codOrganizacion;
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()){
                MeLanbide03ReportVO report = new MeLanbide03ReportVO();
                report.setNombre(rs.getString("NOMBRE"));
                Calendar fecha = Calendar.getInstance();
                    fecha.setTime(rs.getDate("FECHA_CREACION"));
                report.setFechaCreacion(fecha.getTime());
                report.setFechaString(meLanbide03I18n.getFechaConvertida(codIdioma, fecha.getTime()));
                report.setCodOrganizacion(rs.getInt("COD_ORGANIZACION"));
                report.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                report.setMimeType(rs.getString("MIME_TYPE"));
                listaReports.add(report);
            }//while (rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getReports() : END");
        return listaReports;
    }//getReport
    
    public MeLanbide03ReportVO getReport (String nombreReport, String numExpediente, Integer codOrganizacion, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getReport() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        MeLanbide03ReportVO report = new MeLanbide03ReportVO();
        try{
            String sql = "Select NOMBRE, FECHA_CREACION, COD_ORGANIZACION, NUM_EXPEDIENTE, REPORT, MIME_TYPE,ID_DOKUSI"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES) 
                    + " where NOMBRE = '" + nombreReport + "' and COD_ORGANIZACION = " + codOrganizacion
                    + " and NUM_EXPEDIENTE ='" + numExpediente + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()){
                report.setNombre(rs.getString("NOMBRE"));
                Calendar fecha = Calendar.getInstance();
                    fecha.setTime(rs.getDate("FECHA_CREACION"));
                report.setFechaCreacion(fecha.getTime());
                report.setCodOrganizacion(rs.getInt("COD_ORGANIZACION"));
                report.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                report.setReport(rs.getBytes("REPORT"));
                report.setMimeType(rs.getString("MIME_TYPE"));
                report.setIdDokusi(rs.getString("ID_DOKUSI"));
            }//while (rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando el report", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el report", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getReport() : END");
        return report;
    }//getReport
    
    public ArrayList<ImpresionExpedientesLanbideValueObject> getListaExpedientesDocumento(int codOrganizacion, String nombreFichero, Connection conexion) throws SQLException
   {
     ArrayList salida = new ArrayList();
     ResultSet rs = null;
     Statement st = null;
     try
     {
       String sql = "SELECT NUM_EXPEDIENTE FROM MELANBIDE03_EXP_IMPR_APA WHERE NOMBRE_FICHERO='" + nombreFichero + "' " + "ORDER BY NUM_EXPEDIENTE DESC";
 
       this.log.debug(sql);
       st = conexion.createStatement();
       rs = st.executeQuery(sql);
 
       AnotacionRegistroDAO anotacionDAO = AnotacionRegistroDAO.getInstance();
       while (rs.next()) {
         String numExpediente = rs.getString("NUM_EXPEDIENTE");
         GeneralValueObject anotacion = anotacionDAO.getAnotacionMasAntigua(numExpediente, conexion);

         String fechaEntrada = (String)anotacion.getAtributo("FECHA_ENTRADA_REGISTRO_ANOTACION");
         String numRegistro = (String)anotacion.getAtributo("NUMERO_REGISTRO_ANOTACION");
 
         ImpresionExpedientesLanbideValueObject vo = new ImpresionExpedientesLanbideValueObject();
         vo.setNumExpediente(numExpediente);
         vo.setFechaPresentacionRegistroInicio(fechaEntrada);
         vo.setNumRegistroInicio(numRegistro);
 
         salida.add(vo);
       }
        ArrayList interesados= null;
       for (int i = 0; i < salida.size(); i++) {
         ImpresionExpedientesLanbideValueObject exp = (ImpresionExpedientesLanbideValueObject)salida.get(i);
         String[] datos = exp.getNumExpediente().split("/");
         try
         {
            interesados = getListaInteresadosExpediente(codOrganizacion, exp.getNumExpediente(), Integer.parseInt(datos[0]), conexion);
         }
         catch(Exception ex){}
         String interesado = "";
         for (int j = 0; j < interesados.size(); j++) {
           interesado = interesado + (String)interesados.get(j);
           if (interesados.size() - j <= 1) continue; interesado = interesado + "\\r\\n\\r\\n";
         }
         exp.setInteresados(interesado);
       }
     }
     catch (SQLException e) {
       this.log.error("Error al recuperar ocurrencia del trÃ¡mite: " + e.getMessage());
      e.printStackTrace();
     } catch (AnotacionRegistroException e) {
       e.printStackTrace();
     } catch (TechnicalException e) {
      e.printStackTrace();
     }
     finally {
        if (rs != null) rs.close(); 
        if (st != null) st.close();
    }
     return salida;
   }


    public List<GeneralValueObject> getExpedientesApaPendientes(Connection con) throws AnotacionRegistroException, TechnicalException, Exception
    {
        List<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            
            String sql = "SELECT * FROM (select distinct exp.EXP_NUM, exp.EXP_EJE, exp.EXP_MUN, fec.TFE_VALOR, DESCRIP, HTE_AP1"
                        +" from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_EXP, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" exp"                    
                        + " INNER JOIN E_EXT ON EXT_NUM = EXP_NUM AND EXT_EJE = EXP_EJE"
                        //+ " INNER JOIN T_TER ON EXT_TER = TER_COD"
                        + " Inner Join T_Hte On Ext_Ter = Hte_Ter And Ext_Nvr = Hte_Nvr"
                        + " LEFT JOIN T_DOT ON Dot_Dom = Ext_Dot And Ext_Ter = Hte_Ter"
                        + " LEFT JOIN T_DNN ON DOT_DOM = DNN_DOM AND EXT_DOT = DNN_DOM"
                        + " LEFT JOIN CP_OFICINAS ON LPAD(COD_POSTAL,5,'0') = DNN_CPO"
                        +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TDE, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" acr on exp.EXP_EJE = acr.TDE_EJE and exp.EXP_NUM = acr.TDE_NUM"
                        +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_CRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" cro on exp.EXP_PRO = cro.CRO_PRO and exp.EXP_EJE = cro.CRO_EJE and exp.EXP_NUM = cro.CRO_NUM"
                        +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TRA, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" tra on cro.CRO_PRO = tra.TRA_PRO and cro.CRO_TRA = tra.TRA_COD"
                        //+" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" cen on cen.NUM_EXPEDIENTE = exp.EXP_NUM"
                        +" left join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TFE, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" fec on fec.TFE_MUN = exp.EXP_MUN and fec.TFE_EJE = exp.EXP_EJE and fec.TFE_NUM = exp.EXP_NUM"
                        +" LEFT join E_TDE GAI ON exp.EXP_EJE = GAI.TDE_EJE and exp.EXP_NUM = GAI.TDE_NUM AND GAI.TDE_COD= 'ORIGAITUZ' "
                        +" where acr.TDE_COD = '"+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.CAMPO_SUPL_TIPOACREDITACION, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +"' and acr.TDE_VALOR = '"+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TIPOACREDITACION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)+"'"
                        +" and tra.TRA_COU = "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.CODIGO_TRAMITE_IMPRESION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " And (Gai.Tde_Valor Is Null Or Gai.Tde_Valor <> 'X')"
                        +" and cro.CRO_FEF is null order by fec.TFE_VALOR ASC, DESCRIP, HTE_AP1) "
                        //+" and cen.CENTRO_ACREDITADO = 0"
                        +" WHERE ROWNUM < 125 ";

           log.debug("ExpedienteImpresionDAO:getExpedientesImpresion: " + sql);
           

           st = con.createStatement();
           rs = st.executeQuery(sql);

           AnotacionRegistroDAO anotacionDAO = AnotacionRegistroDAO.getInstance();

           while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();                  
                String fecPres = "";
                String numExpediente = rs.getString("EXP_NUM");
                String ejercicio = rs.getString("EXP_EJE");
                String codMunicipio = rs.getString("EXP_MUN");
                GeneralValueObject anotacion = anotacionDAO.getAnotacionMasAntigua(numExpediente, con);                
                String fechaEntrada = (String)anotacion.getAtributo(ConstantesDatos.FECHA_ENTRADA_REGISTRO_ANOTACION);
                String numRegistro  = (String)anotacion.getAtributo(ConstantesDatos.NUMERO_REGISTRO_ANOTACION);

                gVO.setAtributo("numExpediente",numExpediente);
                //gVO.setAtributo("interesados",interesado);
                gVO.setAtributo("numRegistro",numRegistro);
                gVO.setAtributo("fechaEntrada",fechaEntrada);
                gVO.setAtributo("ejercicio",ejercicio);
                gVO.setAtributo("codMunicipio",codMunicipio);
                gVO.setAtributo("oficina",rs.getString("DESCRIP"));
                
                java.util.Date fec = null;
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                if(rs.getString("TFE_VALOR") != null && !rs.getString("TFE_VALOR").equals("")){
                    fec = rs.getDate("TFE_VALOR");
                    try
                    {                          
                          fecPres = formatoFecha.format(fec);
                    }catch(Exception e){this.log.debug("Error en getExpedientesImpresion: " + e);}
                    //fecPres = fec.toString();                      
                }                
                gVO.setAtributo("fecPres", fecPres);
                lista.add(gVO);
            }// while
           
             
             for(int j=0;j<lista.size();j++){                 
                 GeneralValueObject exp = (GeneralValueObject)lista.get(j);                 
                 String numExpediente = (String)exp.getAtributo("numExpediente");
                 String ejercicio     = (String)exp.getAtributo("ejercicio");
                 String codMunicipio  = (String)exp.getAtributo("codMunicipio");
                 
                 ArrayList<String> interesados = getListaInteresadosExpediente(Integer.parseInt(codMunicipio),numExpediente,Integer.parseInt(ejercicio),con);           
                                
                 String interesado = "";
                 for(int z=0;z<interesados.size();z++){
                    interesado = interesado + interesados.get(z);
                    if(interesados.size()-z>1) interesado = interesado + "<br>";                    
                 }// for
                exp.setAtributo("interesados",interesado);                 
                 
             }// for
           
        log.debug("getExpedientesImpresion: lista resultante: " + lista.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }catch(TechnicalException e){
            e.printStackTrace();
        }catch(AnotacionRegistroException e){
            e.printStackTrace();
        }finally{
            if(rs!=null) rs.close();            
            if(st!=null) st.close();
        }// finally

        return lista;
    }
    
    public List<GeneralValueObject> getExpedientesOficiosPendientes(Connection con) throws AnotacionRegistroException, TechnicalException, Exception
    {
        List<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        Statement st = null;
        ResultSet rs = null;
        try
        {            
            String sql = "SELECT * FROM (select distinct exp.EXP_NUM, exp.EXP_EJE, exp.EXP_MUN, fec.TFE_VALOR, DESCRIP, HTE_AP1"
                        +" from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_EXP, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" exp"                    
                        + " INNER JOIN E_EXT ON EXT_NUM = EXP_NUM AND EXT_EJE = EXP_EJE"
                        //+ " INNER JOIN T_TER ON EXT_TER = TER_COD"
                        + " Inner Join T_Hte On Ext_Ter = Hte_Ter And Ext_Nvr = Hte_Nvr"
                        + " LEFT JOIN T_DOT ON Dot_Dom = Ext_Dot And Ext_Ter = Hte_Ter"
                        + " LEFT JOIN T_DNN ON DOT_DOM = DNN_DOM AND EXT_DOT = DNN_DOM"
                        + " LEFT JOIN CP_OFICINAS ON LPAD(COD_POSTAL,5,'0') = DNN_CPO"
                        +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TDE, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" acr on exp.EXP_EJE = acr.TDE_EJE and exp.EXP_NUM = acr.TDE_NUM"
                        +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_CRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" cro on exp.EXP_PRO = cro.CRO_PRO and exp.EXP_EJE = cro.CRO_EJE and exp.EXP_NUM = cro.CRO_NUM"
                        +" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TRA, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" tra on cro.CRO_PRO = tra.TRA_PRO and cro.CRO_TRA = tra.TRA_COD"
                        //+" inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_CERT_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" cen on cen.NUM_EXPEDIENTE = exp.EXP_NUM"
                        +" left join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TFE, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +" fec on fec.TFE_MUN = exp.EXP_MUN and fec.TFE_EJE = exp.EXP_EJE and fec.TFE_NUM = exp.EXP_NUM"
                        //+" LEFT join E_TDE GAI ON exp.EXP_EJE = GAI.TDE_EJE and exp.EXP_NUM = GAI.TDE_NUM AND GAI.TDE_COD= 'ORIGAITUZ' "
                        //+" where acr.TDE_COD = '"+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.CAMPO_SUPL_TIPOACREDITACION, ConstantesMeLanbide03.FICHERO_PROPIEDADES) +"' and acr.TDE_VALOR = '"+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TIPOACREDITACION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)+"'"
                        +" where tra.TRA_COU = "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.CODIGO_TRAMITE_OFICIOS, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " And EXP_PRO = 'CEPAP' "
                        +" and cro.CRO_FEF is null order by DESCRIP, HTE_AP1) "
                        //+" and cen.CENTRO_ACREDITADO = 0"
                        +" WHERE ROWNUM < 50 ";

           log.debug("ExpedienteImpresionDAO:getExpedientesImpresion: " + sql);
           

           st = con.createStatement();
           rs = st.executeQuery(sql);

           AnotacionRegistroDAO anotacionDAO = AnotacionRegistroDAO.getInstance();

           while(rs.next()){
                GeneralValueObject gVO = new GeneralValueObject();                  
                String fecPres = "";
                String numExpediente = rs.getString("EXP_NUM");
                String ejercicio = rs.getString("EXP_EJE");
                String codMunicipio = rs.getString("EXP_MUN");
                GeneralValueObject anotacion = anotacionDAO.getAnotacionMasAntigua(numExpediente, con);                
                String fechaEntrada = (String)anotacion.getAtributo(ConstantesDatos.FECHA_ENTRADA_REGISTRO_ANOTACION);
                String numRegistro  = (String)anotacion.getAtributo(ConstantesDatos.NUMERO_REGISTRO_ANOTACION);

                gVO.setAtributo("numExpediente",numExpediente);
                //gVO.setAtributo("interesados",interesado);
                gVO.setAtributo("numRegistro",numRegistro);
                gVO.setAtributo("fechaEntrada",fechaEntrada);
                gVO.setAtributo("ejercicio",ejercicio);
                gVO.setAtributo("codMunicipio",codMunicipio);
                gVO.setAtributo("oficina",rs.getString("DESCRIP"));
                
                java.util.Date fec = null;
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                if(rs.getString("TFE_VALOR") != null && !rs.getString("TFE_VALOR").equals("")){
                    fec = rs.getDate("TFE_VALOR");
                    try
                    {                          
                          fecPres = formatoFecha.format(fec);
                    }catch(Exception e){this.log.debug("Error en getExpedientesImpresion: " + e);}
                    //fecPres = fec.toString();                      
                }                
                gVO.setAtributo("fecPres", fecPres);
                lista.add(gVO);
            }// while
           
             
             for(int j=0;j<lista.size();j++){                 
                 GeneralValueObject exp = (GeneralValueObject)lista.get(j);                 
                 String numExpediente = (String)exp.getAtributo("numExpediente");
                 String ejercicio     = (String)exp.getAtributo("ejercicio");
                 String codMunicipio  = (String)exp.getAtributo("codMunicipio");
                 
                 ArrayList<String> interesados = getListaInteresadosExpediente(Integer.parseInt(codMunicipio),numExpediente,Integer.parseInt(ejercicio),con);           
                                
                 String interesado = "";
                 for(int z=0;z<interesados.size();z++){
                    interesado = interesado + interesados.get(z);
                    if(interesados.size()-z>1) interesado = interesado + "<br>";                    
                 }// for
                exp.setAtributo("interesados",interesado);                 
                 
             }// for
           
        log.debug("getExpedientesImpresion: lista resultante: " + lista.toString());
        }catch(SQLException e){
            e.printStackTrace();
        }catch(TechnicalException e){
            e.printStackTrace();
        }catch(AnotacionRegistroException e){
            e.printStackTrace();
        }finally{
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }// finally

        return lista;
    }
    
    private ArrayList<String> getListaInteresadosExpediente(int codOrganizacion,String numExpediente,int ejercicio,Connection con) throws SQLException, Exception
    {
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList<String> salida = new ArrayList<String>();
        
        try {            
            // Creamos la select con los parametros adecuados.
            String sql = "SELECT HTE_NOM,HTE_AP1,HTE_AP2 "
                        + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_EXT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " , "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_T_HTE, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " WHERE E_EXT.EXT_NUM=? AND E_EXT.EXT_EJE=? AND E_EXT.EXT_MUN=? AND E_EXT.EXT_TER=HTE_TER AND E_EXT.EXT_NVR=HTE_NVR";                 
            
            log.debug(sql);        
            ps = con.prepareStatement(sql);
            int i =1;
            ps.setString(i++,numExpediente);
            ps.setInt(i++, ejercicio);
            ps.setInt(i++,codOrganizacion);
            
            rs = ps.executeQuery();
                        
            while(rs.next()){
                String apellido2 = rs.getString("HTE_AP2");
                String apellido1 = rs.getString("HTE_AP1");
                String nombre = rs.getString("HTE_NOM");
                String nombreCompleto = null;
                
                if(apellido2!=null && !"".equals(apellido2)){
                    nombreCompleto = apellido1 + " " + apellido2 + "," + nombre;                             
                }else
                    nombreCompleto = apellido1 + "," + nombre;                             
                
                salida.add(nombreCompleto);                
            }
            
        } catch (SQLException e) {
            log.error("Error al recuperar la lista de interesados del expediente: " + e.getMessage());
            e.printStackTrace();                                    
        } 
        finally {
            if(rs!=null) rs.close(); 
            if(ps!=null) ps.close();
        }
        return salida;        
    }

    public List<GeneralValueObject>  getFicherosImpresionGenerados(Connection con) throws SQLException, Exception
    {
        List<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT NOMBRE_FICHERO, FECHA_GENERACION "
                       + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_IMPRESION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                       + " ORDER BY FECHA_GENERACION DESC, NOMBRE_FICHERO DESC";
            
//            String sql = "SELECT I.NOMBRE_FICHERO AS NOMBRE_FICHERO, FECHA_GENERACION, NUM_EXPEDIENTE, DESCRIP  "
//                + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_IMPRESION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)+" I "
//                + " INNER JOIN MELANBIDE03_EXP_IMPR_APA E ON I.NOMBRE_FICHERO = E.NOMBRE_FICHERO"
//                + " INNER JOIN E_EXP ON NUM_EXPEDIENTE = EXP_NUM"
//                + " INNER JOIN E_EXT ON EXT_NUM = EXP_NUM AND EXT_EJE = EXP_EJE"
//                + " INNER JOIN T_TER ON EXT_TER = TER_COD"
//                + " LEFT JOIN T_DOT ON DOT_TER = TER_COD"
//                + " LEFT JOIN T_DNN ON DOT_DOM = DNN_DOM"
//                + " LEFT JOIN CP_OFICINAS ON LPAD(COD_POSTAL,5,'0') = DNN_CPO"
//                + " ORDER BY DESCRIP ASC, FECHA_GENERACION DESC";

           log.debug("ExpedienteImpresionDAO:getFicherosImpresionGenerados " + sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);

           while(rs.next())
           {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("numExpediente",rs.getString("NOMBRE_FICHERO"));
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                String fechGeneracion = formato.format(rs.getTimestamp("FECHA_GENERACION"));
                gVO.setAtributo("fechaGeneracion",fechGeneracion);
                lista.add(gVO);
            }
        } catch(SQLException e){
            throw new Exception(e);
        } finally {
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }

        return lista;
    }
    
    public List<GeneralValueObject>  getFicherosImpresionOficiosGenerados(Connection con) throws SQLException, Exception
    {
        List<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT NOMBRE_FICHERO, FECHA_GENERACION "
                       + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_OFICIOS_IMPRESION, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                       + " ORDER BY FECHA_GENERACION DESC, NOMBRE_FICHERO DESC";


           log.debug("ExpedienteImpresionDAO:getFicherosImpresionGenerados " + sql);
           st = con.createStatement();
           rs = st.executeQuery(sql);

           while(rs.next())
           {
                GeneralValueObject gVO = new GeneralValueObject();
                gVO.setAtributo("numExpediente",rs.getString("NOMBRE_FICHERO"));
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
                String fechGeneracion = formato.format(rs.getTimestamp("FECHA_GENERACION"));
                gVO.setAtributo("fechaGeneracion",fechGeneracion);
                lista.add(gVO);
            }
        }
        catch(SQLException e){
            throw new Exception(e);
        } finally {
            if(rs!=null) rs.close();            
            if(st!=null) st.close();
        }

        return lista;
    }
    
    public int guardarPdfApaPendientes(String nombreFichero, byte[] pdf, Connection con) throws SQLException, Exception
    {
        int result = 0;
        PreparedStatement pstmt = null;
        
        try {
            String sql = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_IMPRESION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " values(?, sysdate, ?)";
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, nombreFichero);
            pstmt.setBytes(2, pdf);
            result = pstmt.executeUpdate();
        } catch(SQLException ex) {
            throw new Exception(ex);
        } finally {
            if(pstmt!=null)pstmt.close();
        }
        return result;
    }
    
    public byte[] getPdfPorNombre(String nomDoc, Connection con) throws SQLException, Exception
    {
        ResultSet rs = null;
        Statement st = null;
        byte[] pdf = null;
        
        try {            
            // Creamos la select con los parametros adecuados.
            String sql = "SELECT CONTENIDO "
                        + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_MELANBIDE03_IMPRESION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " WHERE NOMBRE_FICHERO = '"+nomDoc+"'";                 
            
            log.debug(sql);      
            st = con.createStatement();
            rs = st.executeQuery(sql);
                        
            if(rs.next()){
                pdf = rs.getBytes("CONTENIDO");
            }
            
        } catch (SQLException e) {
            log.error("Error al recuperar la lista de interesados del expediente: " + e.getMessage());
            e.printStackTrace();                                    
        } finally {
            if(rs!=null) rs.close(); 
            if(st!=null) st.close();
        }
        return pdf;        
    }
    
    
    
    public String getCodigoInternoTramite(int codOrganizacion, String codProc, String codTramite, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            String query = "select TRA_COD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TRA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                            +" where TRA_MUN = "+codOrganizacion
                            +" and TRA_PRO = '"+codProc+"'"
                            +" and TRA_COU = "+codTramite;
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                valor = rs.getLong("TRA_COD");
                if(rs.wasNull())
                {
                    valor = null;
                }
            }
        } catch(Exception ex) {
            log.error("Se ha producido un error recuperando el código interno del trámite "+codTramite, ex);
            throw new Exception(ex);
        } finally {
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }
        return valor != null ? valor.toString() : null;
    }
    
    public ArrayList<Melanbide03RelacionExpVO> getDatosRelacion (Connection con) 
            throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getDatosRelacion() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        ArrayList<Melanbide03RelacionExpVO> listaReports = new ArrayList<Melanbide03RelacionExpVO>();
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
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while (rs.next()){
                Melanbide03RelacionExpVO report = new Melanbide03RelacionExpVO();
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
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getReports() : END");
        return listaReports;
    }//getReport
    
    public ArrayList<String> getExpediente(String nombre, Connection conexion) throws SQLException
    {
        ArrayList<String> expedientes = new ArrayList<String>();
        ResultSet rs = null;
        Statement st = null;
        try {
            String SQL_OCC_TRAMITE = "SELECT DISTINCT NUM_EXPEDIENTE, fec.TFE_VALOR, DESCRIP, HTE_AP1 FROM MELANBIDE03_EXP_IMPR_APA "                 
                +" INNER JOIN E_EXT   ON EXT_NUM  = NUM_EXPEDIENTE"
                +" LEFT JOIN E_TFE FEC  ON FEC.TFE_MUN  = EXT_MUN  AND FEC.TFE_EJE = EXT_EJE  AND FEC.TFE_NUM = EXT_NUM"
                +" INNER JOIN T_Hte  ON Ext_Ter  = Hte_Ter  AND Ext_Nvr = Hte_Nvr"
                +" LEFT JOIN T_DOT  ON Dot_Dom  = Ext_Dot  AND Ext_Ter = Hte_Ter"
                +" LEFT JOIN T_DNN  ON DOT_DOM  = DNN_DOM  AND EXT_DOT = DNN_DOM"
                +" LEFT JOIN CP_OFICINAS  ON LPAD(COD_POSTAL,5,'0') = DNN_CPO"
                +" WHERE NOMBRE_FICHERO='" + nombre + "'"                 
                +" ORDER BY fec.TFE_VALOR ASC, DESCRIP, HTE_AP1";

            this.log.debug(SQL_OCC_TRAMITE);

            st = conexion.createStatement();
            rs = st.executeQuery(SQL_OCC_TRAMITE);

            while (rs.next()) {
                String is = rs.getString("NUM_EXPEDIENTE");
                expedientes.add(is);
            }
        } catch (SQLException e) {
            this.log.error("Error en getExpediente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (st != null) st.close();
        }
        return expedientes;
    }
    
    public ArrayList<Participantes> leerDatosParticipantes (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        ArrayList<Participantes> parti = new ArrayList<Participantes>();
        try
        {            
            Participantes exp = null;
            String query = null;
            
                query = "SELECT EXT_NUM, HTE_DOC, HTE_NOM, HTE_PA1, HTE_PA2,"
                      + "HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID, "
                      + "PAI_COD, PRV_COD, MUN_COD, VIA_COD, DNN_PLT, DNN_PTA, DNN_CPO "
                      + ", PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL  " +
                        "FROM E_EXT " +
                        "Left Join T_Hte On Ext_Ter =Hte_Ter And Ext_Nvr = Hte_Nvr  "
                      + "Left Join T_Dot On Ext_Ter = Dot_Ter And Ext_Dot=Dot_Dom " +
                        "LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM " +
                        "LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI " +
                        "LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV " +
                        "LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN " +
                        "LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA " +
                        "WHERE EXT_NUM = '"+numExp+"'";

                if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            while(rs.next()){
                exp = new Participantes();
                exp.setNumExp(rs.getString("EXT_NUM"));
                exp.setNif(rs.getString("HTE_DOC"));
                exp.setNombre(rs.getString("HTE_NOM"));
//                exp.setApe1(rs.getString("HTE_PA1"));
//                exp.setApe2(rs.getString("HTE_PA2"));
                exp.setTlf(rs.getString("HTE_TLF"));
                exp.setMail(rs.getString("HTE_DCE"));
                exp.setNomC(rs.getString("HTE_NOC"));
                exp.setTipoID(rs.getInt("HTE_TID"));
                exp.setIdPais(rs.getString("PAI_COD"));
                exp.setIdProv(rs.getString("PRV_NOM"));
                exp.setIdMuni(rs.getString("MUN_COD"));
                exp.setIdCalle(rs.getString("VIA_COD"));
                exp.setPais(rs.getString("PAI_NOM"));
                exp.setProv(rs.getString("PRV_NOM"));
                exp.setMuni(rs.getString("MUN_NOM"));
                exp.setCalle(rs.getString("VIA_NOM"));
                exp.setNum(rs.getString("DNN_NUD"));
                exp.setCodPostal(rs.getString("DNN_CPO"));
                exp.setPlanta(rs.getString("DNN_PLT"));
                exp.setPuerta(rs.getString("DNN_PTA"));
                exp.setLetra(rs.getString("DNN_LED"));
                if(exp.getCalle() == null )
                    exp.setCalle(rs.getString("DNN_DMC"));
                
                
                //exp.setRol(rs.getString("EXT_ROL"));
                parti.add(exp);
            }
                log.error("Participantes recogidos");
        } catch(Exception ex) {
            ex.printStackTrace();            
            log.error("Error en leerDatosParticipantes " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }
        return parti;
    }
    
    public int getAnoFechaPresentacion(String numExpediente, Connection con) throws SQLException, Exception
    {
        int result = 0;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            String query = null;
            //SELECT EXTRACT(YEAR FROM TFE_VALOR) as FECANO FROM E_TFE WHERE TFE_NUM LIKE '2013/CEPAP/001717' AND TFE_COD='FECHAPRESENTACION';
                query = "SELECT EXTRACT(YEAR FROM TFE_VALOR) as FECANO "
                      + "FROM E_TFE "
                      + "WHERE TFE_NUM = '"+numExpediente+"' "
                      + "AND TFE_COD = 'FECHAPRESENTACION'";

                if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next()){
                return rs.getInt("FECANO");
            }
            
        } catch(SQLException ex) {
            throw new Exception(ex);
        } finally {
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }
        return result;
    }
    
    public ArrayList<CertificacionPositiva> leerDatosCertificaciones (String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        Statement st3 = null;
        ResultSet rs3 = null;
        ArrayList<CertificacionPositiva> certi = new ArrayList<CertificacionPositiva>();
        try
        {            
            CertificacionPositiva exp = null;
            String query = null;
            String query2 = null;
            String query3 = null;
            
            query = "select c.COD_CERTIFICADO as CODIGOCP,g.nivel as NIVEL,g.descertificado_e as DESCRIPCIONE,g.descertificado_c as DESCRIPCIONC,"
                + " TO_CHAR(g.FECHA_RD, 'yyyy/MM/dd')AS RDFECHAEUSK,F.DESMODULO_E AS DECMODTEXTEUSK,g.decreto as DECRETO,TO_CHAR(g.FECHA_RD, 'dd/MM/yyyy')AS FECHARD,F.DESMODULO_C  as DECMODTEXT"
                + " from flbgen.s_cer_certificados g, melanbide03_certificado c,MELANBIDE03_MOD_PRACT P, flbgen.S_CER_MODULOS_FORMATIVOS F"
                + " WHERE c.num_expediente = '"+numExp+"'"
                + " and p.num_expediente= '"+numExp+"'"
                + " and c.cod_certificado= g.codcertificado"
                + " and F.CODMODULO=P.COD_MODULO";
            
            query2 = "SELECT t_hte.hte_nom as HTE_NOM,t_hte.hte_ap1 as HTE_APE1,t_hte.hte_ap2 as HTE_APE2,hte_doc as HTE_DOC"
                + " FROM t_hte,e_ext"
                + " WHERE hte_ter = ext_ter AND hte_nvr = ext_nvr"
                + " AND ext_num = '"+numExp+"'";
            
            query3 = "SELECT T_DNN.*,PAI_NOM,PRV_NOM,MUN_NOM,tvi_des,via_nom"
                + " FROM FLBGEN.T_PAI T_PAI,FLBGEN.T_MUN T_MUN,FLBGEN.T_PRV T_PRV,E_EXT"
                + " LEFT JOIN T_HTE ON (EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR),T_DNN"
                + " LEFT JOIN T_TVI ON (DNN_TVI = TVI_COD)"
                + " LEFT JOIN T_VIA ON (DNN_PAI = VIA_PAI AND DNN_PRV = VIA_PRV AND DNN_MUN = VIA_MUN AND DNN_VIA = VIA_COD)"
                + " WHERE ext_num = '"+numExp+"'"
                + " and dnn_dom=ext_dot AND PAI_COD = DNN_PAI AND PRV_PAI = DNN_PAI AND MUN_PAI = DNN_PAI AND PRV_COD = DNN_PRV AND MUN_PRV = DNN_PRV AND MUN_COD = DNN_MUN";
            
            if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            st2 = con.createStatement();
            rs2 = st2.executeQuery(query2);
            st3 = con.createStatement();
            rs3 = st3.executeQuery(query3);
            int id = 0;
            if(rs.next()){
                exp = new CertificacionPositiva();
                exp.setCODIGOCP(rs.getString("CODIGOCP"));
                exp.setNIVEL(rs.getString("NIVEL"));
                exp.setDESCRIPCIONE(rs.getString("DESCRIPCIONE"));
                exp.setDESCRIPCIONC(rs.getString("DESCRIPCIONC"));
                exp.setRDFECHAEUSK(rs.getString("RDFECHAEUSK"));
                exp.setDECMODTEXTEUSK(rs.getString("DECMODTEXTEUSK"));
                exp.setDECRETO(rs.getString("DECRETO"));
                exp.setFECHARD(rs.getString("FECHARD"));
                exp.setDECMODTEXT(rs.getString("DECMODTEXT"));
                
                 if(rs2.next()){
                    String ape1="";
                    String ape2="";
                    if(rs2.getString("HTE_APE1")== null){
                        ape1="";
                    } else{
                        ape1=rs2.getString("HTE_APE1");
                    }
                    
                    if(rs2.getString("HTE_APE2")== null){
                        ape2="";
                    } else{
                        ape2=rs2.getString("HTE_APE2");
                    }
                    
                    String nomAp=rs2.getString("HTE_NOM")+" "+ape1+" "+ape2;
                    exp.setINTERESADO_SOLICITANTE(nomAp);
                    exp.setINTERESADO_DocSOLICITANTE(rs2.getString("HTE_DOC"));
                 }
                 
                 if(rs3.next()){
                     String dom="";
                     String cp="";
                     String mun="";
                     String pro="";
                     
                    if(rs3.getString("VIA_NOM")== null){
                        dom="";
                    } else{
                        dom=rs3.getString("VIA_NOM");
                    }
                    
                    if(dom.trim().equals("")){
                        if(rs3.getString("DNN_DMC")== null){
                        dom="";
                    } else{
                        dom=rs3.getString("DNN_DMC");
                    }
                    }
                    
                    if(rs3.getString("DNN_NUD")== null){
                        dom=dom;
                    } else{
                        dom=dom+" "+rs3.getString("DNN_NUD");
                    }
                    
                    if(rs3.getString("DNN_LED")== null){
                        dom=dom;
                    } else{
                        dom=dom+" "+rs3.getString("DNN_LED");
                    }
                     
                    exp.setINTERESADO_DomSOLICITANTE(dom);
                    if(rs3.getString("DNN_CPO")== null){
                        cp="";
                    } else{
                        cp=rs3.getString("DNN_CPO");
                    }
                    exp.setINTERESADO_CodPostalSOLICITAN(cp);
                    if(rs3.getString("MUN_NOM")== null){
                        mun="";
                    } else{
                        mun=rs3.getString("MUN_NOM");
                    }
                    exp.setINTERESADO_PobSOLICITANTE(mun);
                    if(rs3.getString("PRV_NOM")== null){
                        pro="";
                    } else{
                        pro=rs3.getString("PRV_NOM");
                    }
                    exp.setINTERESADO_ProvinciaSOLICITAN(pro);
                 }
                certi.add(exp);
            }
                log.error("Certificaciones Positivos recogidos");
        }
        catch(Exception ex) {
            ex.printStackTrace();            
            log.error("Error en leerDatosParticipantes " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(rs2!=null) rs2.close();
            if(rs3!=null) rs3.close();            
            if(st!=null) st.close();
            if(st2!=null) st2.close();
            if(st3!=null) st3.close();
        }
        return certi;
    }
    
    public ArrayList<ArrayList> getListaAcreditadas3(String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<ArrayList> listaAcreditadas3 = new ArrayList<ArrayList>();
        ArrayList<String> listaAcreditadas3Cast = new ArrayList<String>();
        ArrayList<String> listaAcreditadas3Eusk = new ArrayList<String>();
        try
        {            
            String acred="";
            String query = null;
 
                    query = "select P.COD_UNIDAD || '- ' || G.DESUNIDAD_C ||' / '|| P.COD_UNIDAD || '- ' || G.DESUNIDAD_E|| CHR(10)  as etiqueta from melanbide03_cert_centro p, flbgen.s_cer_unidades_competencia g, e_des_val origen, flbgen.s_cer_certificados cer"
                    + " where (( NUM_EXPEDIENTE in (  select distinct(EXT_NUM) from E_EXT,T_HTE where EXT_TER=HTE_TER and HTE_NVR=EXT_NVR and HTE_DOC in (select HTE_DOC from T_HTE,E_EXT where EXT_TER=HTE_TER and HTE_NVR=EXT_NVR and EXT_NUM= '"+numExp+"'"
                    + " ))))and COD_UNIDAD in(select UNIDADCOMPETENCIA from FLBGEN.CP_UC where CODCERTIFICADO in(select COD_CERTIFICADO from MELANBIDE03_CERTIFICADO where NUM_EXPEDIENTE= '"+numExp+"'"
                    + " ) and UNIDADCOMPETENCIA is not null) and p.cod_motivo_acreditado = 'null' and p.cod_unidad=g.codunidad and origen.des_cod='ORAC' and p.cod_origen_acreditacion= origen.des_val_cod and p.cod_certificado= cer.codcertificado";

 
                if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            while(rs.next()){
                acred=rs.getString("ETIQUETA");
                String[] castEusk = acred.split("/");
                
                listaAcreditadas3Cast.add(castEusk[0]);
                listaAcreditadas3Eusk.add(castEusk[1]);
            }
            listaAcreditadas3.add(listaAcreditadas3Cast);
            listaAcreditadas3.add(listaAcreditadas3Eusk);
            log.error("ListaAcreditadas3 recogidos");
        } catch(Exception ex) {
            ex.printStackTrace();            
            log.error("Error en getListaAcreditadas3 " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if(log.isDebugEnabled()) log.error("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }
        return listaAcreditadas3;
    }

 public String getFechaPresentacion(String numExpediente, Connection con) throws Exception {
        String result ="";
        Statement st = null;
        ResultSet rs = null;
        
        try {
            String query = null;
            //SELECT EXTRACT(YEAR FROM TFE_VALOR) as FECANO FROM E_TFE WHERE TFE_NUM LIKE '2013/CEPAP/001717' AND TFE_COD='FECHAPRESENTACION';
                query = "SELECT to_char(TFE_VALOR,'yyyy/mm/dd') as FECANO "
                      + "FROM E_TFE "
                      + "WHERE TFE_NUM = '"+numExpediente+"' "
                      + "AND TFE_COD = 'FECHAPRESENTACION'";

                if(log.isDebugEnabled()) 
                log.error("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
            if(rs.next()){
                return rs.getString("FECANO");
            }
            
        } catch(SQLException ex) {
            throw new Exception(ex);
        } finally {
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }
        return result;
    }


    public ArrayList<MeLanbide03ReportVO> getReportsNoDokusi(Integer codOrganizacion, Connection con) throws SQLException, Exception {
        if (log.isDebugEnabled()) {log.debug("getReportsNoDokusi() : BEGIN");}

        Statement st = null;
        ResultSet rs = null;
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList();
        MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
        try {
            String sql = "Select NOMBRE, FECHA_CREACION, COD_ORGANIZACION, NUM_EXPEDIENTE, MIME_TYPE FROM ( Select NOMBRE, FECHA_CREACION, COD_ORGANIZACION, NUM_EXPEDIENTE, MIME_TYPE   from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, ConstantesMeLanbide03.FICHERO_PROPIEDADES) + " where ID_DOKUSI IS NULL ) WHERE rownum <= 100";
            if (log.isDebugEnabled()) {log.debug("sql = " + sql);}
            st = con.createStatement();
            rs = st.executeQuery(sql);
            log.info("Query recuperada, asignamos a la lista los resultados");
            while (rs.next())
            {
                MeLanbide03ReportVO report = new MeLanbide03ReportVO();
                report.setNombre(rs.getString("NOMBRE"));
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(rs.getDate("FECHA_CREACION"));
                report.setFechaCreacion(fecha.getTime());
                report.setCodOrganizacion(Integer.valueOf(rs.getInt("COD_ORGANIZACION")));
                report.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                report.setMimeType(rs.getString("MIME_TYPE"));

                listaReports.add(report);
            }
        }
        catch (SQLException e)
        {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);
            throw e;
        }
        finally
        {
            if (log.isDebugEnabled()) {log.debug("Procedemos a cerrar el statement y el resultset");}
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        if (log.isDebugEnabled()) {log.debug("getReportsNoDokusi() : END");}
        return listaReports;
    }

    public ArrayList<MeLanbide03ReportVO> getReportsNoLocalizados(Integer codOrganizacion, Connection con) throws SQLException, Exception {
        if (log.isDebugEnabled()) {log.debug("getReportsNoLocalizados() : BEGIN");}

        Statement st = null;
        ResultSet rs = null;
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList();
        MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
        try
        {
            String sql = "Select NOMBRE, FECHA_CREACION, COD_ORGANIZACION, NUM_EXPEDIENTE, MIME_TYPE,ID_DOKUSI  from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, ConstantesMeLanbide03.FICHERO_PROPIEDADES) + " where ID_DOKUSI IS NOT NULL AND LOCALIZADO=0";
            if (log.isDebugEnabled()) {log.debug("sql = " + sql);}
            st = con.createStatement();
            rs = st.executeQuery(sql);
            log.info("Query recuperada asignamos a la lista los resultados");
            while (rs.next())
            {
                MeLanbide03ReportVO report = new MeLanbide03ReportVO();
                report.setNombre(rs.getString("NOMBRE"));
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(rs.getDate("FECHA_CREACION"));
                report.setFechaCreacion(fecha.getTime());
                report.setCodOrganizacion(Integer.valueOf(rs.getInt("COD_ORGANIZACION")));
                report.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                report.setMimeType(rs.getString("MIME_TYPE"));
                report.setIdDokusi(rs.getString("ID_DOKUSI"));
                listaReports.add(report);
            }
        }
        catch (SQLException e)
        {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("Se ha producido un error recuperando la lista de reports para un expediente", e);
            throw e;
        }
        finally
        {
            if (log.isDebugEnabled()) {log.debug("Procedemos a cerrar el statement y el resultset");}
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        if (log.isDebugEnabled()) {log.debug("getReportsNoLocalizados() : END");}
        return listaReports;
    }

    public void updateIdDokusiReport(MeLanbide03ReportVO report, Connection con) throws SQLException, Exception {
        if (log.isDebugEnabled()) {log.debug("updateIdDokusiReport() : BEGIN");}

        Statement st = null;
        try
        {
            String[] nombreReport = report.getNombre().split("-");

            String sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " SET ID_DOKUSI ='" + report.getIdDokusi() + "' " + " where NUM_EXPEDIENTE = '" + report.getNumExpediente() + "' and NOMBRE like '%" + nombreReport[0] + "%'";
            if (log.isDebugEnabled()) {log.debug("sql = " + sql);}
            st = con.createStatement();
            st.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            log.error("Se ha producido un error updateando el módulo de formación", e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("Se ha producido un error updateando el módulo de formación", e);
            throw e;
        }
        finally
        {
            if (log.isDebugEnabled()) {log.debug("Procedemos a cerrar el statement y el resultset");}
            if (st != null) {
                st.close();
            }
        }
        if (log.isDebugEnabled()) {log.debug("updateIdDokusiReport() : END");}
    }

    public void updateLocalizadoReport(MeLanbide03ReportVO report, Connection con) throws SQLException, Exception {
        if (log.isDebugEnabled()) {log.debug("updateIdDokusiReport() : BEGIN");}

        Statement st = null;
        try
        {
            String[] nombreReport = report.getNombre().split("-");

            String sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_REPORT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " SET LOCALIZADO =1 " + " where NUM_EXPEDIENTE = '" + report.getNumExpediente() + "' and NOMBRE like '%" + nombreReport[0] + "%'";
            if (log.isDebugEnabled()) {log.debug("sql = " + sql);}
            st = con.createStatement();
            st.executeUpdate(sql);
        }
        catch (SQLException e)
        {
            log.error("Se ha producido un error updateando el módulo de formación", e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("Se ha producido un error updateando el módulo de formación", e);
            throw e;
        }
        finally
        {
            if (log.isDebugEnabled()) {log.debug("Procedemos a cerrar el statement y el resultset");}
            if (st != null) {
                st.close();
            }
        }
        if (log.isDebugEnabled()) {log.debug("updateIdDokusiReport() : END");}
    }

    //select case when tfe_valor <= '01/02/23' then 'S' else 'N' end antiguo from e_tfe where tfe_cod='FECHAPRESENTACION' and tfe_num='2022/CEPAP/000845';
    //devuelve 'S' si el expediente tiene el campo FECHAPRESENTACION y es <= '01/02/23'
    public String esAntiguoAPA(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String esAntiguo = "N";

        try {
            String sql = "select case when tfe_valor <= '01/02/23' then 'S' else 'N' end antiguo from e_tfe where tfe_cod='FECHAPRESENTACION' and tfe_num='"+numExp+"'";
            log.debug("esAntiguoAPA: " + sql.toString());

            st = con.createStatement();
            rs = st.executeQuery(sql.toString());

            if(rs.next()){
                esAntiguo = rs.getString("antiguo");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }// finally
        return esAntiguo;
    }
    

}//Class
