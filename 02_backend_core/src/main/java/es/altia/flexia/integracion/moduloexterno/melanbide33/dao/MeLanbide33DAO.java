/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide33.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConstantesMeLanbide33;
import es.altia.flexia.integracion.moduloexterno.melanbide33.util.MeLanbide33MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.FilaMinimisVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import java.util.Arrays;


/**
 *
 * @author santiagoc
 */
public class MeLanbide33DAO 
{

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide33DAO.class);
    
    //Instancia
    private static MeLanbide33DAO instance = null;
    
    private MeLanbide33DAO()
    {
        
    }
    
    public static MeLanbide33DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide33DAO.class)
            {
                instance = new MeLanbide33DAO();
            }
        }
        return instance;
    }    
    
    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        Long tram = null;
        try
        {
            String query = null;
            query = "select MAX(CRO_TRA) as CRO_TRA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_E_CRO, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                        +" where CRO_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide33.NOMBRE_PROCEDIMIENTO, ConstantesMeLanbide33.FICHERO_PROPIEDADES)+"'"
                        +" and CRO_EJE = "+ejercicio+" and CRO_NUM = '"+numExp+"' and CRO_OCU = 1 and CRO_FEF is null";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                tram = rs.getLong("CRO_TRA");
                if(rs.wasNull())
                    tram = null;
            }
            return tram;
        }
        catch(Exception ex)
        {
            con.rollback();
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
    }    
    
    public Long obtenerCodigoInternoTramite(Integer entorno, String codTramite, Connection con)throws Exception
    {
        Long cod=0L;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select TRA_COD from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_TRAMITES, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                        +" where TRA_MUN = "+entorno
                        +" and TRA_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide33.NOMBRE_PROCEDIMIENTO, ConstantesMeLanbide33.FICHERO_PROPIEDADES)+"'"
                        +" and TRA_COU = "+codTramite;

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                cod = rs.getLong("TRA_COD");
                if(rs.wasNull())
                    cod = 0L;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código interno del trámite : "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
    	return cod;
    }
    
    public boolean tieneTramiteIniciado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
        boolean tramiteIniciado = false;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_E_CRO, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    +" where CRO_MUN = "+codOrganizacion
                    +" and CRO_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide33.NOMBRE_PROCEDIMIENTO, ConstantesMeLanbide33.FICHERO_PROPIEDADES)+"'"
                    +" and CRO_EJE = "+ejercicio
                    +" and CRO_NUM = '"+numExp+"'"
                    +" and CRO_TRA = "+codTramite;

            if(log.isDebugEnabled()) 
                    log.debug("sql = " + sql);
            
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if(rs.next())
            {
                tramiteIniciado = true;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el código interno del trámite : "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
    	return tramiteIniciado;
    }
    
    public boolean eliminarValorSubvencion(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
         boolean resultado = false;
         Statement st = null; 
         Statement st2 = null;
 
        try
        {
            String sql = "delete from  "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_VALORDESPLEGCAMPOSUPL, ConstantesMeLanbide33.FICHERO_PROPIEDADES)                            
                    +" where TDE_MUN = "+codOrganizacion                  
                    +" and TDE_EJE = "+ejercicio
                    +" and TDE_NUM = '"+numExp+"'"
                    +" and (TDE_COD = 'RESULSUBV' OR TDE_COD='MOTDEN1'  OR TDE_COD='MOTDEN2' OR TDE_COD='MOTDEN3')";

            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.debug("Número de filas eliminadas " + rowsDeleted);
            
            String sql2 = "delete from  "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_VALORNUMCAMPOSUPL, ConstantesMeLanbide33.FICHERO_PROPIEDADES)                            
                    +" where TNU_MUN = "+codOrganizacion                  
                    +" and TNU_EJE = "+ejercicio
                    +" and TNU_NUM = '"+numExp+"'"
                    +" and (TNU_COD = 'IMPCALC' OR TNU_COD='CONINV' OR TNU_COD='CONPU1' OR TNU_COD='CONPU2' OR TNU_COD='CONPU3' OR TNU_COD='CONPU4'"+
                        " OR TNU_COD='CONTP1' OR TNU_COD='CONTP2' OR TNU_COD='CONTP3' OR TNU_COD='CONTP4' "+
                        " OR TNU_COD='CONTI1' OR TNU_COD='CONTI2' OR TNU_COD='CONTI3' OR TNU_COD='CONTI4' "+
                        " OR TNU_COD='CONEST' OR TNU_COD='CONAUC' OR TNU_COD='CONFORM' "+
                        ")";
            if(log.isDebugEnabled()) log.debug("sql2 = " + sql2);
            st2 = con.createStatement();
            int rowsDeleted2 = st2.executeUpdate(sql2);
            log.debug("Número de filas eliminadas " + rowsDeleted2);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando el resultado de la subvención del estudio técnico : "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(st2!=null) 
                st2.close();
        }
         return resultado;
    }
    
    public boolean eliminarValoresCalculadosET(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
         boolean resultado = false;
         Statement st = null; Statement st2 = null;Statement st3 = null;        
        try
        {
            String sql = "update S75CONCEPTOS set CNP_CUANCAL=NULL "                         
                    +" where CNP_MUN = "+codOrganizacion                  
                    +" and CNP_EJE = "+ejercicio
                    +" and CNP_NUM = '"+numExp+"'";

            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.debug("Número de filas eliminadas " + rowsDeleted);
            
            String sql2 = "update s75puestos set PST_CUANCAL=NULL, PST_IMPAN1=NULL, PST_IMPAN2=NULL, PST_IMPAN3=NULL, PST_IMPAN4=NULL "                         
                    +" where PST_MUN = "+codOrganizacion                  
                    +" and PST_EJE = "+ejercicio
                    +" and PST_NUM = '"+numExp+"'";

            if(log.isDebugEnabled()) log.debug("sql2 = " + sql2);
            st2 = con.createStatement();
            int rowsDeleted2 = st.executeUpdate(sql2);
            log.debug("Número de filas eliminadas " + rowsDeleted2);
            
            String sql3 = "update S75RELTECPUESTO set RTP_IMPAN1=NULL, RTP_IMPAN2=NULL, RTP_IMPAN3=NULL, RTP_IMPAN4=NULL "                         
                    +" where RTP_MUN = "+codOrganizacion                  
                    +" and RTP_EJE = "+ejercicio
                    +" and RTP_NUM = '"+numExp+"'";

            if(log.isDebugEnabled()) log.debug("sql3 = " + sql3);
            st3 = con.createStatement();
            int rowsDeleted3 = st.executeUpdate(sql3);
            log.debug("Número de filas eliminadas " + rowsDeleted3);
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando los importes concedidos: "+codTramite, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(st2!=null) 
                 st2.close();
            if(st3!=null) 
                 st3.close();
        }
         return resultado;
    }
    
    public boolean eliminarValorSubvET_PagosS75(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
        boolean resultado = false;
        Statement st = null; 
        try
        {
            String sql = "delete from  "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_S75PAGOS, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    +" where PAG_MUN = "+codOrganizacion                  
                    +" and PAG_EJE = "+ejercicio
                    +" and PAG_NUM = '"+numExp+"'"
                    ;

            log.error("sql Elimina pagos s75 = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.error("Número de filas eliminadas " + rowsDeleted);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando pagos al retroceder estudio técnico s75 - : " + numExp);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
         return resultado;
    }
    
    public boolean eliminarValorSubvET_RelaTecPuestos(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
        boolean resultado = false;
        Statement st = null; 
        try
        {
            String sql = "delete from  "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_S75RELACIONTECNICOPUESTOS, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    +" where RTP_MUN = "+codOrganizacion                  
                    +" and RTP_EJE = "+ejercicio
                    +" and RTP_NUM = '"+numExp+"'"
                    ;

            log.error("sql Elimina relacion tecnicos puestos s75 = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.error("Número de filas eliminadas " + rowsDeleted);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando relacions tecnicos puetsos al retroceder estudio técnico s75 - : " + numExp);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
         return resultado;
    }
    
    public boolean eliminarValorSubvET_EtiquetasPlant(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception
    {
        boolean resultado = false;
        Statement st = null; 
        try
        {
            String sql = "delete from  "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_S75ETIQUETAS, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    +" where ETQ_MUN = "+codOrganizacion                  
                    +" and ETQ_EJE = "+ejercicio
                    +" and ETQ_NUM = '"+numExp+"'"
                    ;

            log.error("sql Elimina etiquetas s75 = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.error("Número de filas eliminadas " + rowsDeleted);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando etquetas de plantillas al retroceder estudio técnico s75 - : " + numExp) ;
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
         return resultado;
    }

    
    public String getAnyoResolucion(int entorno, Long tramResolucion, String numExp,  Connection con) throws Exception{
        
        Statement st = null;
        ResultSet rs = null;
        String anyo = "";
        try
        {
            String query = null;
            
            query="SELECT TO_CHAR(TFET_VALOR,'YYYY') as ANYO FROM E_TRA "
                    +" LEFT JOIN E_TFET  ON TRA_COD=TFET_TRA AND TRA_PRO=TFET_PRO  AND TRA_MUN=TFET_MUN "
                    +" WHERE TFET_PRO='SEI' AND TFET_COD='FECHARESO' AND TFET_NUM='"+numExp+"'  and TRA_COU='"+tramResolucion+"' AND TRA_MUN="+entorno+" ";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
               // fecha = rs.getDate("TFET_VALOR");   
                anyo = rs.getString("ANYO");                
                if(rs.wasNull())
                    anyo = null;
            }
            return anyo;
        }
        catch(Exception ex)
        {
            con.rollback();
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
       }
        
}
    
public Long getPrimerAnoPago(int entorno, String codProc, int ejercicio, String numExp, Connection con) throws SQLException
	{
            Statement st = null;Statement st2 = null;
            ResultSet rs = null;ResultSet rs2 = null;
		Long anyo = null;
		try
		{
			String sql = "select count(PAG_MUN) as cont from S75PAGOS "
                                + "where PAG_MUN = "+entorno+" and PAG_PRO = '"+codProc+"' and PAG_EJE = "+ejercicio+" and PAG_NUM = '"+numExp+"' ";
			
			st = con.createStatement();
                        rs = st.executeQuery(sql);
                        if(rs.next())
                        {
                            long cont = rs.getLong("cont");                
                            if(!rs.wasNull()){
                                String sql2 = "select min(PAG_ANOPAGO) AS ANYO from S75PAGOS "
                                        + "where PAG_MUN = "+entorno+" and PAG_PRO = '"+codProc+"' and PAG_EJE = "+ejercicio+"  and  PAG_NUM = '"+numExp+"' ";
                                st2 = con.createStatement();
                                rs2 = st2.executeQuery(sql2);
                                if(rs2.next()){
                                    anyo = rs2.getLong("ANYO");                
                                    if(rs2.wasNull())
                                        anyo = null;
                                }
                                return anyo;
                                
                            }   
                        }
                       
		}
		catch(Exception ex)
		{
			
		}
                finally
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(st2!=null) 
                         st2.close();
                    if(rs!=null) 
                        rs.close();
                    if(rs2!=null) 
                         rs2.close();
                }
		return anyo;
	}
    
    public int actualizarAnoPago(int entorno, String codProc, int ejercicio, String numExp, Long incremento,Connection con) throws SQLException
	{
		 int result= 0;                
                Statement st = null; 
		try
		{
			String sql = "update S75PAGOS "
                                + "set PAG_ANOPAGO = PAG_ANOPAGO + "+incremento+" "
                                + "where PAG_MUN = "+entorno+" and PAG_PRO = '"+codProc+"' and PAG_EJE = "+ejercicio+" and PAG_NUM = '"+numExp+"' ";
			
			if(log.isDebugEnabled()) log.debug("sql = " + sql);
                        st = con.createStatement();
                        int rowsUpdated = st.executeUpdate(sql);
                        log.debug("Número de filas modificadas " + rowsUpdated);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
                finally
                {
                    if(st!=null) 
                        st.close();
                }
		return result;
	}

    public boolean eliminarValorSubvET_Conceptos(int codOrganizacion, int ejercicio, String numExp, Long codTramite, Connection con) throws Exception {
        
        boolean resultado = false;
        Statement st = null; 
        try
        {
            String sql = "update "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_S75CONCEPTOS, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    +" SET CNP_CUANLIM=NULL, CNP_P1=NULL, CNP_P2=NULL, CNP_P3=NULL, CNP_P4=NULL, CNP_CUANCAL=NULL, CNP_CUANPRO=NULL, CNP_CUANDES=0"
                    +" where CNP_MUN = "+codOrganizacion                  
                    +" and CNP_EJE = "+ejercicio
                    +" and CNP_NUM = '"+numExp+"'"
                    ;
            log.error("sql update conceptos  s75 = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
            log.error("Número de filas actualizadas " + rowsDeleted);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error borrando datos de subvencion de conceptos de plantillas al retroceder estudio técnico s75 - : " + numExp) ;
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
        }
        return resultado;
    }

    public String getDocumentoInteresado(String numExpediente, Connection con) throws Exception {
        String documento = "";
        Statement st = null; 
        ResultSet rs = null; 
        try
        {
            String sql = "SELECT  HTE_DOC documento FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_RELACIONTERCEROSXEXPTS, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                        + " INNER JOIN T_HTE ON T_HTE.HTE_TER=E_EXT.EXT_TER AND T_HTE.HTE_NVR=E_EXT.EXT_NVR " 
                        + " WHERE E_EXT.EXT_NUM='"+numExpediente+"' AND E_EXT.EXT_PRO='"+ConfigurationParameter.getParameter(ConstantesMeLanbide33.NOMBRE_PROCEDIMIENTO, ConstantesMeLanbide33.FICHERO_PROPIEDADES)+"'"
                        + " AND E_EXT.EXT_ROL=(SELECT ROL_COD FROM E_ROL WHERE ROL_PRO='SEI' AND ROL_PDE=1)"
                    ;
            log.error("getDocumentoInteresado dao - sql obtiene documento interesado expediente s75 = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                documento  = rs.getString("documento");
                log.error("getDocumentoInteresado dao - documento obtenido : " + documento);
                }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error obteniendo el documento del interado del expediente - Al cargar las convocatorias anteriores :  " + numExpediente, ex) ;
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
         return documento;
    }

    public ArrayList<String> getEptsDosConvoAnteriores(Integer ejercicio, String documentoInteresado, String numExpediente, Connection con) throws Exception {
        ArrayList<String> listaExpts = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT EXT_NUM FROM E_EXT " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_RELACIONTERCEROSXEXPTS, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " INNER JOIN T_HTE ON T_HTE.HTE_TER=E_EXT.EXT_TER AND T_HTE.HTE_NVR=E_EXT.EXT_NVR  "
                    + " WHERE T_HTE.HTE_DOC='"+documentoInteresado+"' AND E_EXT.EXT_ROL=(SELECT ROL_COD FROM E_ROL WHERE ROL_PRO='SEI' AND ROL_PDE=1) AND E_EXT.EXT_PRO='" + ConfigurationParameter.getParameter(ConstantesMeLanbide33.NOMBRE_PROCEDIMIENTO, ConstantesMeLanbide33.FICHERO_PROPIEDADES) + "'"
                    + " AND EXT_EJE BETWEEN "+(ejercicio-2)+" AND "+(ejercicio-1)
                    + " ORDER BY EXT_NUM DESC ";
            log.error("getEptsDosConvoAnteriores dao - sql obtiene expediente dos convocatorias ante. s75 = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()) {
                listaExpts.add(rs.getString("EXT_NUM"));
            }
            log.error("getEptsDosConvoAnteriores dao - Total expts obtenidos : " + listaExpts.size());
        } catch (Exception ex) {
            log.error("Se ha producido un error obteniendo expedientes   - Al cargar las convocatorias anteriores :  " + numExpediente + " Documento " + documentoInteresado, ex);
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
        return listaExpts;
    }

    public void guardarRelacionConvocatoria(Integer codOrganizacion, String ejercicio, String numExpediente, Integer ejercicioExpteRela, String num_exp_relacionado, Connection con) throws Exception {
        log.error("guardarRelacionConvocatoria dao - BEGIN()");
        Statement st = null;
        try {
            String sql = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_RELACIONEXPTSFLEXIA, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " (REX_MUN, REX_EJE, REX_NUM, REX_MUNR, REX_EJER, REX_NUMR) "
                    + " VALUES (" + codOrganizacion + "," + ejercicio + ",'"+numExpediente+"',"   
                    + codOrganizacion + "," + ejercicioExpteRela +",'" +num_exp_relacionado+"'"
                    + " )";
            log.error("guardarRelacionConvocatoria dao - sql insert relacion convocatorias anteriores s75 = " + sql);
            st = con.createStatement();
            st.executeUpdate(sql);
            log.error("guardarRelacionConvocatoria dao - Expediente relacionado Correctamente " + num_exp_relacionado + " al expediente :" + numExpediente);
        } catch (Exception ex) {
            log.error("Se ha producido un error insertando la relacion de expediente de convocatorias anteriores s75 - : Entre " + num_exp_relacionado + " y(principal) " + numExpediente);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
        log.error("guardarRelacionConvocatoria dao - END()");
    }

    public boolean comprobarDatosConv_ConcepGuardadosDAO(int codOrganizacion, String ejercicio, String codProcedimiento, Integer numeroExpInt, String numExpediente, Connection con) throws Exception {
        log.error("comprobarDatosConv_ConcepGuardadosDAO  - BEGIN()");
        boolean resultado = false;
        ResultSet rs = null;
        PreparedStatement ps =null;
        int rowsConvocatoria = 0;
        int rowsConceptos = 0;
        int rowsPuestos = 0;
        try {
            String sql = "select NVL(count(RCV_NUM),0) ROWSS75RELCONVOCA, NVL(ROWSS75CONCEPTOS,0) ROWSS75CONCEPTOS, NVL(ROWSS75PUESTOS,0)ROWSS75PUESTOS " +
                        " from S75RELCONVOCA " +
                        " left join (select CNP_MUN, CNP_PRO, CNP_EJE, CNP_NUM, count (CNP_NUM) as ROWSS75CONCEPTOS from S75CONCEPTOS" +
                        " group by CNP_MUN, CNP_PRO, CNP_EJE, CNP_NUM" +
                        " ) S75CONCEPTOS" +
                        "  on S75CONCEPTOS.CNP_MUN=rcv_mun and" +
                        "  S75CONCEPTOS.CNP_PRO=rcv_pro " +
                        "  AND S75CONCEPTOS.cnp_EJE=RCV_EJE" +
                        "  AND TO_NUMBER(SUBSTR(S75CONCEPTOS.cnp_num,-6))=RCV_NUM" +
                        " left join (select PST_MUN, PST_PRO, PST_EJE, PST_NUM, count (PST_NUM) as ROWSS75PUESTOS from S75PUESTOS" +
                        " group by PST_MUN, PST_PRO, PST_EJE, PST_NUM" +
                        " ) S75PUESTOS" +
                        "  on S75PUESTOS.PST_MUN=rcv_mun and" +
                        "  S75PUESTOS.PST_PRO=rcv_pro " +
                        "  AND S75PUESTOS.PST_EJE=RCV_EJE" +
                        "  AND TO_NUMBER(SUBSTR(S75PUESTOS.PST_NUM,-6))=RCV_NUM" +
                        " where " +
                        " rcv_mun=? and rcv_pro=? AND RCV_EJE=? AND RCV_NUM=?" +
                        " group by RCV_NUM, ROWSS75CONCEPTOS, ROWSS75PUESTOS  ";
            log.error("comprobarDatosConv_ConcepGuardadosDAO - sql = " + sql);
            ps = con.prepareStatement(sql);
            ps.setInt(1,codOrganizacion);
            ps.setString(2,codProcedimiento);
            ps.setInt(3,Integer.valueOf(ejercicio));
            ps.setInt(4,numeroExpInt);
            log.error("comprobarDatosConv_ConcepGuardadosDAO - parametros = 1-codOrganizacion: " + codOrganizacion+
                    " 2-codProcedimiento:" + codProcedimiento+
                    " 3-ejercicio:" + ejercicio+
                    " 4-numeroExpInt:" + numeroExpInt
            );
            rs = ps.executeQuery();
            while(rs.next()) {
                rowsConvocatoria = rs.getInt("ROWSS75RELCONVOCA");
                rowsConceptos = rs.getInt("ROWSS75CONCEPTOS");
                rowsPuestos = rs.getInt("ROWSS75PUESTOS");
            }
            log.error("comprobarDatosConv_ConcepGuardadosDAO - Datos Obtenidos : " +
                    "rowsConvocatoria: " + rowsConvocatoria
                    +"rowsConceptos: " + rowsConceptos
                    +"rowsPuestos: " + rowsPuestos
            );
            if((rowsConvocatoria)>0 && (rowsConceptos+rowsPuestos)>0){
                resultado=true;
            }
        } catch (Exception ex) {
            log.error("comprobarDatosConv_ConcepGuardadosDAO - Se ha producido un error consultando datos de convocatoria,puesto y conceptos : " + numExpediente);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("comprobarDatosConv_ConcepGuardadosDAO - Procedemos a cerrar el preparedstatement y el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.error("comprobarDatosConv_ConcepGuardadosDAO  - END()");
        return resultado;
    }

    public boolean guardarTotalPago3Anualidad3DAO(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, int codTramite,  Connection con) throws Exception {
        log.info("guardarTotalPago3Anualidad3DAO dao - BEGIN()");
        boolean result = true;
        double importe3pago3Anua=0;
       // int entra4a1P = 1;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT NVL(SUM(nvl(PAG_IMPCON,0)),-1) importe3pago3Anua " +
                    " FROM (SELECT S75PAGOS.*" +
                    "       ,(MIN(PAG_ANOPAGO) OVER (PARTITION BY PAG_MUN,PAG_EJE,PAG_PRO,PAG_NUM))+2 TERCER_ANIO " +
                    "       FROM S75PAGOS " +
                    "       ORDER BY PAG_MUN,PAG_EJE,PAG_PRO,PAG_NUM, PAG_ANOPAGO, PAG_NUMPAGO) S75PAGOS " +
                    " WHERE PAG_MUN=? AND PAG_EJE=? AND  PAG_PRO=? AND PAG_NUM=? AND PAG_NUMPAGO=3 AND PAG_ANOPAGO=TERCER_ANIO";
            log.error("guardarTotalPago3Anualidad3DAO - sql consulta total pago 3 anualidad 3 " + sql);
            pt = con.prepareStatement(sql);
            pt.setInt(1, codOrganizacion);
            pt.setInt(2, Integer.valueOf(ejercicio));
            pt.setString(3, codProcedimiento);
            pt.setString(4, numExpediente);
            log.error("guardarTotalPago3Anualidad3DAO - consulta parametros : ?,?,?,? :" + codOrganizacion + "-" + ejercicio + "-"+ codProcedimiento + "-"+ numExpediente);
            rs=pt.executeQuery();
            if(rs.next()){
                importe3pago3Anua=rs.getDouble("importe3pago3Anua");
                log.info("guardarTotalPago3Anualidad3DAO - Importe recogido de BD-importe3pago3Anua- : " + importe3pago3Anua);
            }
           /* if(importe3pago3Anua>0){
                // Si hay importes que pagar en tercer año 3 pago asignamos un 0 en el control de entrada del 4a1p
                // para que no abra de momento el trámite
                entra4a1P=0;
            }*/
            pt.clearParameters();
            log.info("PreparedStatement Limpiado parametros - preparamos nueva sql con delete e insert");
            // borramos por si hay algun registo en caso de que se haya hecho retroceder
            String codigoCampoSuple = ConfigurationParameter.getParameter(ConstantesMeLanbide33.COD_CAMPOSUP_IMPTOTA_3ANIO3PAGO, ConstantesMeLanbide33.FICHERO_PROPIEDADES);
           /*String codigoVisibleTramPag3Anio3 = ConfigurationParameter.getParameter(ConstantesMeLanbide33.COD_VISIBLE_TRAMITE_PAGO3_ANUALIDAD3, ConstantesMeLanbide33.FICHERO_PROPIEDADES);
            log.error("guardarTotalPago3Anualidad3DAO - Codigo visible Tramite 3a3P desde properties " + codigoVisibleTramPag3Anio3);
            String codigoVisibleTramiteLLamaOpera=getCodigoVisibleTramiteByCodInternoy(codOrganizacion, codProcedimiento, codTramite, con);
            log.error("guardarTotalPago3Anualidad3DAO - Codigo visible Tramite que llama a operacion " + codigoVisibleTramiteLLamaOpera);
            if(codigoVisibleTramPag3Anio3.equals(codigoVisibleTramiteLLamaOpera)){
                log.error("guardarTotalPago3Anualidad3DAO - Llamamos operacion desde tramite tercer pago tercer año");
                // actualizamos a 1 el valor delcampo de control de entrada en a4p1 
                // porque ya se ha pagado el 3p3a ya podemos entrar e 4a1p
                entra4a1P=1;
            }
                   */
            sql = "delete e_tnu where TNU_MUN=? and TNU_EJE=? and TNU_NUM=? and TNU_COD=? ";
            log.info("guardarTotalPago3Anualidad3DAO - sql delete datos campos sumplementario TOTIMPCON3A3P(TOTAL IMPORTE CONCEDIDO 3 ANUALIDAD 3 PAGO): " + sql);
            pt = con.prepareStatement(sql);
            pt.setInt(1, codOrganizacion);
            pt.setInt(2, Integer.valueOf(ejercicio));
            pt.setString(3, numExpediente);
            pt.setString(4, codigoCampoSuple);
            log.info("guardarTotalPago3Anualidad3DAO - delete parametros : ?,?,?,? : " + codOrganizacion + "-" + ejercicio + "-"+ numExpediente + "-"+ codigoCampoSuple);
            int lineasEliminadas = pt.executeUpdate();
            log.info("Lineas eliminadas del campo TOTIMPCON3A3P" + lineasEliminadas + " al expediente :" + numExpediente);
            pt.clearParameters();
            sql="insert into e_tnu(TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) values (?,?,?,?,?)";
            log.info("guardarTotalPago3Anualidad3DAO - sql insert datos campos sumplementario TOTIMPCON3A3P(TOTAL IMPORTE CONCEDIDO 3 ANUALIDAD 3 PAGO): " + sql);
            pt = con.prepareStatement(sql);
            pt.setInt(1, codOrganizacion);
            pt.setInt(2, Integer.valueOf(ejercicio));
            pt.setString(3, numExpediente);
            pt.setString(4, codigoCampoSuple);
            pt.setDouble(5, importe3pago3Anua);
            log.info("guardarTotalPago3Anualidad3DAO - insert parametros : ?,?,?,?,? : " + codOrganizacion + "-" + ejercicio + "-"+ numExpediente + "-"+ codigoCampoSuple +"-"+ importe3pago3Anua);
            int lineasGuardadas = pt.executeUpdate();
            log.info("guardarTotalPago3Anualidad3DAO - Campos sumplementario TOTIMPCON3A3P Guardado valor " + importe3pago3Anua + ". Lineas insertadas " + lineasGuardadas + " al expediente :" + numExpediente);
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando el campo TOTIMPCON3A3P(TOTAL IMPORTE CONCEDIDO 3 ANUALIDAD 3 PAGO - Expediente : " + numExpediente);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("guardarTotalPago3Anualidad3DAO - Procedemos a cerrar el preparedstatement y el resultset");
                }
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("guardarTotalPago3Anualidad3DAO - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            } finally {
                if (pt != null) pt.close();
                if (rs != null) rs.close();
            }
        }
        log.info("guardarTotalPago3Anualidad3DAO - END()");
        return result;
    }
    
    public String getCodigoVisibleTramiteByCodInternoy(int codOrganizacion, String codProcedimiento, int codTramite,  Connection con) throws Exception {
        log.info("getCodigoVisibleTramiteByCodInternoy  - BEGIN()");
        String result = "";
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String sql = "select TRA_COU from e_tra where TRA_MUN=? and TRA_PRO=? AND TRA_COD=?";
            log.info("getCodigoVisibleTramiteByCodInternoy - sql consulta codigo visible CON COD INTERNO Y  cod procedimiento : " + sql);
            pt = con.prepareStatement(sql);
            pt.setInt(1, codOrganizacion);
            pt.setString(2, codProcedimiento);
            pt.setInt(3, codTramite);
            log.info("getCodigoVisibleTramiteByCodInternoy - consulta parametros : ?,?,? :" + codOrganizacion + "-" + codProcedimiento + "-" + codTramite);
            rs=pt.executeQuery();
            if(rs.next()){
                result=rs.getString("TRA_COU");
                log.info("getCodigoVisibleTramiteByCodInternoy - Codigo Visible recogido : " + result + "Para Tramite COD_Interno " + codTramite + " Procedimiento " + codProcedimiento);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando codigo visible tramite " + codTramite + " Pro : " + codProcedimiento);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("getCodigoVisibleTramiteByCodInternoy - Procedemos a cerrar el preparedstatement y el resultset");
            }
            if (pt != null) {
                pt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.error("getCodigoVisibleTramiteByCodInternoy - END()");
        return result;
    }

    public boolean validarPasoaET_SEI_DAO(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, int codTramite, Connection con) throws Exception {
        log.info("validarPasoaET_SEI_DAO  - BEGIN()");
        boolean result = true;
        PreparedStatement pt = null;
        String codigoCampoSuple="";
        try {
            String sql="";
            // borramos por si hay algun registo en caso de que se haya hecho retroceder
            codigoCampoSuple = ConfigurationParameter.getParameter(ConstantesMeLanbide33.COD_CAMPOSUP_PASOETSEI, ConstantesMeLanbide33.FICHERO_PROPIEDADES);
            String codVisibleTramAutorizados = ConfigurationParameter.getParameter(ConstantesMeLanbide33.COD_VIS_TRAM_VALIDAPASO_ET, ConstantesMeLanbide33.FICHERO_PROPIEDADES);
            log.error("validarPasoaET_SEI_DAO - codVisibleTramAutorizados " + codVisibleTramAutorizados);
            String[] listCodVisibleTramAutorizados =codVisibleTramAutorizados.split(ConstantesMeLanbide33.DOT_COMMA);
            String codigoVisibleTramiteLLamaOpera = getCodigoVisibleTramiteByCodInternoy(codOrganizacion, codProcedimiento, codTramite, con);
            log.error("validarPasoaET_SEI_DAO - Codigo visible Tramite que llama a operacion " + codigoVisibleTramiteLLamaOpera);
            // Borramos por si ya existe algun valor para el campo.
            sql = "delete e_tnu where TNU_MUN=? and TNU_EJE=? and TNU_NUM=? and TNU_COD=? ";
            log.error("validarPasoaET_SEI_DAO - sql delete datos campos sumplementario " + codigoCampoSuple + " " + sql);
            pt = con.prepareStatement(sql);
            pt.setInt(1, codOrganizacion);
            pt.setInt(2, Integer.valueOf(ejercicio));
            pt.setString(3, numExpediente);
            pt.setString(4, codigoCampoSuple);
            log.error("validarPasoaET_SEI_DAO - delete parametros : ?,?,?,? : " + codOrganizacion + "-" + ejercicio + "-" + numExpediente + "-" + codigoCampoSuple);
            int lineasEliminadas = pt.executeUpdate();
            log.error("Lineas eliminadas del campo " + codigoCampoSuple + ": " + lineasEliminadas + " al expediente :" + numExpediente);
            pt.clearParameters();
            // Si esta en la lista para permitir el paso guardamos con 1 sino con 0
            List<String> listaTramValida = Arrays.asList(listCodVisibleTramAutorizados);
            if(listaTramValida!=null && listaTramValida.contains(codigoVisibleTramiteLLamaOpera)){
                log.info("validarPasoaET_SEI_DAO - Tramite encontrado en el fichero properties procedemos a guardar 1 en el campo " + codigoCampoSuple);
                sql = "insert into e_tnu(TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) values (?,?,?,?,?)";
                log.info("validarPasoaET_SEI_DAO - sql insert datos campos sumplementario " + codigoCampoSuple + " " + sql);
                pt = con.prepareStatement(sql);
                pt.setInt(1, codOrganizacion);
                pt.setInt(2, Integer.valueOf(ejercicio));
                pt.setString(3, numExpediente);
                pt.setString(4, codigoCampoSuple);
                pt.setDouble(5, 1);
                log.error("validarPasoaET_SEI_DAO - insert parametros : ?,?,?,?,? : " + codOrganizacion + "-" + ejercicio + "-" + numExpediente + "-" + codigoCampoSuple + "-1");
                int lineasGuardadas = pt.executeUpdate();
                log.error("validarPasoaET_SEI_DAO - Campos sumplementario " + codigoCampoSuple + " Guardado valor 1 Lineas insertadas " + lineasGuardadas + " al expediente :" + numExpediente);
                
            }else{
                log.error("validarPasoaET_SEI_DAO - Lista de configuración vacía , o el trámite desde el que se llama la operación no esta configurado en el properties,"
                        + "guardamo cero en el campo");
                sql = "insert into e_tnu(TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) values (?,?,?,?,?)";
                log.error("validarPasoaET_SEI_DAO - sql insert datos campos sumplementario " + codigoCampoSuple + ": " + sql);
                pt = con.prepareStatement(sql);
                pt.setInt(1, codOrganizacion);
                pt.setInt(2, Integer.valueOf(ejercicio));
                pt.setString(3, numExpediente);
                pt.setString(4, codigoCampoSuple);
                pt.setDouble(5, 0);
                log.error("validarPasoaET_SEI_DAO - insert parametros : ?,?,?,?,? : " + codOrganizacion + "-" + ejercicio + "-" + numExpediente + "-" + codigoCampoSuple + "-0");
                int lineasGuardadas = pt.executeUpdate();
                log.error("validarPasoaET_SEI_DAO - Campos sumplementario " + codigoCampoSuple + " Guardado valor  0. Lineas insertadas " + lineasGuardadas + " al expediente :" + numExpediente);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error guardando el campo " + codigoCampoSuple + " " + numExpediente);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("validarPasoaET_SEI_DAO - Procedemos a cerrar el preparedstatement y el resultset");
            }
            if (pt != null) {
                pt.close();
            }
       }
        log.error("validarPasoaET_SEI_DAO - END()");
        return result;
    }
        public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.MELANBIDE33_SUBSOLIC, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide33MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(minimis);
                minimis = new FilaMinimisVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Minimis ", ex);
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
        return lista;
    }    
/**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
        
    public FilaMinimisVO getMinimisPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.MELANBIDE33_SUBSOLIC, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide33MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Minimis : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return minimis;
    }

    public int eliminarMinimis(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.MELANBIDE33_SUBSOLIC, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Minimis ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (nuevaMinimis != null && nuevaMinimis.getFecha() != null && !nuevaMinimis.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(nuevaMinimis.getFecha());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide33.SEQ_MELANBIDE33_SUBSOLIC, ConstantesMeLanbide33.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.MELANBIDE33_SUBSOLIC, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (" + id
                    + ", '" + nuevaMinimis.getNumExp()
                    + "', '" + nuevaMinimis.getEstado()
                    + "', '" + nuevaMinimis.getOrganismo()
                    + "', '" + nuevaMinimis.getObjeto()
                    + "', " + nuevaMinimis.getImporte()
                    + " , TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva Minimis ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva Minimis" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (datModif != null && datModif.getFecha() != null && !datModif.getFecha().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(datModif.getFecha());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.MELANBIDE33_SUBSOLIC, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " SET ESTADO='" + datModif.getEstado() + "'"
                    + ", ORGANISMO='" + datModif.getOrganismo() + "'"
                    + ", OBJETO='" + datModif.getObjeto() + "'"
                    + ", IMPORTE=" + datModif.getImporte()
                    + ", FECHA=TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una Minimis - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }
     /**
     *
     * @param sequence
     * @param con
     * @return
     * @throws Exception
     */
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el n?mero de ID para inserci?n al llamar la secuencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide33.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide33.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "' order by DES_NOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide33MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }
    
}
