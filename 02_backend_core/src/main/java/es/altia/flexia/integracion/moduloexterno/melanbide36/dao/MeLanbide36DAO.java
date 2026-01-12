/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide36.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide36.util.ConstantesMeLanbide36;
import es.altia.flexia.integracion.moduloexterno.melanbide36.util.MeLanbide36MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.FilaExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.InfoContactoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.TerceroVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide36DAO 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide36DAO.class);
    
    //Instancia
    private static MeLanbide36DAO instance = null;
    
    private MeLanbide36DAO()
    {
        
    }
    
    public static MeLanbide36DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide36DAO.class)
            {
                instance = new MeLanbide36DAO();
            }
        }
        return instance;
    }     
    
    public List<FilaExpedienteVO> getListaExpedientesRelacionados(String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaExpedienteVO> retList = new ArrayList<FilaExpedienteVO>();
        try
        {
            List<String> roles = new ArrayList<String>();
            roles.add(ConstantesMeLanbide36.COD_ROL_EMPRESA_SOLICITANTE);
            roles.add(ConstantesMeLanbide36.COD_ROL_PERSONA_SUSTITUIDA);
            HashMap<String, String> tercerosRol = this.getCodigoTercerosPorExpedienteYRol(numExp, roles, con);
            if(tercerosRol != null && tercerosRol.size() > 0)
            {
                String idTer1 = tercerosRol.get(ConstantesMeLanbide36.COD_ROL_EMPRESA_SOLICITANTE);
                String idTer3 = tercerosRol.get(ConstantesMeLanbide36.COD_ROL_PERSONA_SUSTITUIDA);
                
                //idTer1 = "F20080784";
                //idTer3 = "35770360R";
                
                if(idTer1 != null && idTer3 != null && !idTer1.equals("") && !idTer3.equals(""))
                {
                    String query = null;
                    
                          query = "SELECT\r\n"
                                +" EXPRELA1.EXT_NUM AS NUM_EXP,\r\n"
                                +" aa.TXT_VALOR AS NUMEXP_P29,\r\n"
                                +" NVL(bb.dias,0) AS NUMDIAS,\r\n"
                                +" NVL(cc.TNU_VALOR,0) AS IMPPAG1,\r\n"
                                +" NVL(dd.TNU_VALOR,0) AS IMPPAG2,\r\n"
                                +" NVL(EE.TNU_VALOR,0) AS IMPPAG3,\r\n"
                                +" NVL(LL.TNU_VALOR,0) AS SUBVTOTAL,\r\n"
                                +" CASE WHEN (NVL(CC.TNU_VALOR,0) +NVL(DD.TNU_VALOR,0) + NVL(EE.TNU_VALOR,0))=0 THEN  NVL(LL.TNU_VALOR,0)\r\n"
                                +" ELSE (NVL(CC.TNU_VALOR,0) +NVL(DD.TNU_VALOR,0) +NVL(EE.TNU_VALOR,0)) END\r\n"
                                +" AS TOTALABONADO,\r\n"
                                +" ff.TDE_VALOR AS RESULESTTEC,\r\n"
                                +" gg.DES_NOM AS SITUDENECONC,\r\n"
                                +" HH.TDE_VALOR AS TIPACTUSUB,\r\n"
                                +" ii.DES_NOM AS DESTIPACTUSUB,\r\n"
                                +" jj.TFE_VALOR AS FECINIACTCON,\r\n"
                                +" kk.TFE_VALOR AS FECFINACTCON,\r\n"
                                +" ll.TFE_VALOR AS FECNAPDEPEN\r\n"
                                +" FROM (\r\n"
                                +" SELECT DISTINCT EXT_NUM\r\n"
                                +" FROM E_EXT\r\n"
                                //+" LEFT JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR\r\n"
                                +" WHERE EXT_PRO='"+ConstantesMeLanbide36.COD_PROC_CONCM+"'\r\n"
                                //+"       AND HTE_DOC='"+idTer1+"' AND EXT_ROL='"+ConstantesMeLanbide36.COD_ROL_EMPRESA_SOLICITANTE+"'\r\n"
                                +" ) EXPRELA1\r\n"
                                +" INNER JOIN (SELECT DISTINCT EXT_NUM\r\n"
                                +"             FROM E_EXT\r\n"
                                +"             LEFT JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR\r\n"
                                +"             WHERE EXT_PRO='"+ConstantesMeLanbide36.COD_PROC_CONCM+"'\r\n"
                                //+"                   AND EXT_EJE >= 2011\r\n"
                                +"                   AND EXT_EJE >= "+ ConfigurationParameter.getParameter(ConstantesMeLanbide36.ANO_MIN_VISION_EXP_REL, ConstantesMeLanbide36.FICHERO_PROPIEDADES) + "\r\n"
                                +"                   AND HTE_DOC='"+idTer3+"' AND EXT_ROL='"+ConstantesMeLanbide36.COD_ROL_PERSONA_SUSTITUIDA+"'\r\n"
                                +"            ) EXPRELA2 ON EXPRELA1.EXT_NUM=EXPRELA2.EXT_NUM\r\n"
                                +" left join\r\n"
                                +"   (\r\n"
                                +"     SELECT TXT_VALOR ,TXT_NUM\r\n"
                                +"     FROM E_TXT\r\n"
                                +"     WHERE TXT_COD='"+ConstantesMeLanbide36.CAMPO_NUMEXP_P29+"'\r\n"
                                +"   ) aa on aa.TXT_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" left join\r\n"
                                +"   (\r\n"
                                +"     SELECT\r\n"
                                +"       SUM(NVL(NUM_DIAS,0)) dias ,mel2.NUM_EXPEDIENTE\r\n"
                                +"     FROM MELANBIDE01_PERIODO mel1\r\n"
                                //+"     inner join MELANBIDE01_DATOS_CALCULO mel2\r\n"
                                +"     inner join (select MELANBIDE01_DATOS_CALCULO.*, max(id) over (PARTITION BY COD_MUNICIPIO, EJERCICIO, COD_PROCEDIMIENTO, NUM_EXPEDIENTE) as maxid from MELANBIDE01_DATOS_CALCULO) mel2\r\n"  
                                +"     on mel1.ID_DATOS_CALCULO=mel2.ID\r\n"
                                +"     WHERE MEL2.MAXID=mel1.ID_DATOS_CALCULO\r\n"
                                +"     group by mel2.NUM_EXPEDIENTE\r\n"
                                +"   ) bb on bb.NUM_EXPEDIENTE=EXPRELA1.EXT_NUM\r\n"
                                +" left join (SELECT NVL(TNU_VALOR,0)TNU_VALOR,TNU_NUM FROM E_TNU WHERE TNU_COD='"+ConstantesMeLanbide36.CAMPO_IMPPAG1+"' ) cc on cc.TNU_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" left join (SELECT NVL(TNU_VALOR,0)TNU_VALOR,TNU_NUM FROM E_TNU WHERE TNU_COD='"+ConstantesMeLanbide36.CAMPO_IMPPAG2+"' ) dd on dd.TNU_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" LEFT JOIN (SELECT NVL(TNU_VALOR,0)TNU_VALOR,TNU_NUM FROM E_TNU WHERE TNU_COD='"+ConstantesMeLanbide36.CAMPO_IMPPAG3+"' ) EE ON EE.TNU_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" LEFT JOIN (SELECT NVL(TNU_VALOR,0)TNU_VALOR,TNU_NUM FROM E_TNU WHERE TNU_COD='"+ConstantesMeLanbide36.CAMPO_SUBVTOTAL+"' ) LL ON LL.TNU_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" left join (SELECT TDE_VALOR,TDE_NUM FROM E_TDE WHERE TDE_COD='"+ConstantesMeLanbide36.CAMPO_RESULESTTEC+"' ) ff on ff.TDE_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" left join (SELECT DES_NOM,DES_VAL_COD FROM E_DES_VAL WHERE DES_COD='"+ConstantesMeLanbide36.CAMPO_BOOL+"')gg on gg.DES_VAL_COD=ff.TDE_VALOR\r\n" 
                                +" left join (SELECT TDE_VALOR,TDE_NUM FROM E_TDE WHERE TDE_COD='"+ConstantesMeLanbide36.CAMPO_ACTUACION_SUBVENCIONABLE+"' ) hh on hh.TDE_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" left join (SELECT DES_NOM,DES_VAL_COD FROM E_DES_VAL WHERE DES_COD='"+ConstantesMeLanbide36.DESPLEGABLE_ACTUACION_SUBVENCIONABLE+"' )ii on ii.DES_VAL_COD=hh.tde_valor\r\n"
                                +" left join (SELECT TFE_VALOR,TFE_NUM FROM E_TFE WHERE TFE_COD='"+ConstantesMeLanbide36.CAMPO_FECINIACCON+"' )jj on jj.TFE_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" LEFT JOIN (SELECT TFE_VALOR,TFE_NUM FROM E_TFE WHERE TFE_COD='"+ConstantesMeLanbide36.CAMPO_FECFINACCON+"' )KK ON KK.TFE_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" left join (select TFE_VALOR,TFE_NUM from E_TFE where TFE_COD='"+ConstantesMeLanbide36.CAMPO_FECNAPDEPEN+"' )ll on ll.TFE_NUM=EXPRELA1.EXT_NUM\r\n"
                                +" ORDER BY FECINIACTCON";
                    if(log.isDebugEnabled()) 
                        log.debug("sql = " + query);
                    st = con.createStatement();
                    rs = st.executeQuery(query);
                    while(rs.next())
                    {
                        try
                        {
                            retList.add((FilaExpedienteVO)MeLanbide36MappingUtils.getInstance().map(rs, FilaExpedienteVO.class));
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de expedientes relacionados", ex);
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
        return retList;
    }
    
    private HashMap<String, String> getCodigoTercerosPorExpedienteYRol(String numExp, List<String> roles, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        HashMap<String, String> retMap = new HashMap<String, String>();
        try
        {
            String query = null;
            /*query = "select ext_rol, ter_doc from t_ter inner join e_ext on ter_cod = ext_ter"
                    +" where ext_num = '"+numExp+"' and ext_rol in (";*/
            
            query = "select ext_rol, t.ter_doc, h.hte_doc from e_ext"
                   +" left join t_ter t on t.ter_cod = ext_ter and ter_nve = ext_nvr"
                   +" left join t_hte h on h.hte_ter = ext_ter and hte_nvr = ext_nvr"
                   +" where ext_num = '"+ numExp +"' and ext_rol in (";
            
                    for(int i = 0; i < roles.size(); i++)
                    {
                        if(i > 0)
                            query += ",";
                        query += roles.get(i);
                    }
            query += ")";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                retMap.put(rs.getString(1), rs.getString(2) != null && !rs.getString(2).equals("") ? rs.getString(2) : rs.getString(3));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de terceros por rol", ex);
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
        return retMap;
    }

// --- clases DAO para recibir los NIF de rol de persona sustituida y rol de persona contratada (si los hay) ---- Rosa 2018/01/16 -----------   
    
    public InfoContactoVO getNIFSustituida(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;            
            query = "select HTE_DOC "
                  + "  from E_EXT   "
                  + "left join T_HTE    on T_HTE.HTE_TER = E_EXT.EXT_TER"
                  + "                  and T_HTE.HTE_NVR = E_EXT.EXT_NVR"
                  + " where EXT_MUN = " + infoContact.getCodOrganizacion()  
                  + "   and EXT_NUM = '"+ infoContact.getNumExpediente() + "'" 
                  + "   and EXT_ROL = 3";
            
                
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                infoContact.setCodTercero(rs.getString("HTE_DOC"));     
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el tercero sustituido", ex);
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
        return infoContact;
    }
    
    public InfoContactoVO getNIFContratada(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;            
            query = "select HTE_DOC "
                  + "  from E_EXT   "
                  + "left join T_HTE    on T_HTE.HTE_TER = E_EXT.EXT_TER"
                  + "                  and T_HTE.HTE_NVR = E_EXT.EXT_NVR"
                  + " where EXT_MUN = "  + infoContact.getCodOrganizacion() 
                  + "   and EXT_NUM = '" + infoContact.getNumExpediente() + "'" 
                  + "   and EXT_ROL = 4";
            
                
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                infoContact.setCodTercero(rs.getString("HTE_DOC"));     
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el tercero contratado", ex);
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
        return infoContact;
    }
    
// --- clases DAO para ver si existe el NIF de persona sustituida y de persona contratada que han escrito y para grabarlo ---- Rosa 2018/01/16 -----------  
    public TerceroVO existeTercero(TerceroVO tercero, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;            
            query = "select TER_COD," +
                    "       TER_NVE," +
                    "       TER_DOM" +
                    "  from T_TER" +
                    " where TER_DOC = '"+ tercero.getCodDocumento() +"'";
                
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                tercero.setExisteTer(true);
                tercero.setCodTercero(rs.getString("TER_COD"));  
                tercero.setVerTercero(rs.getInt("TER_NVE"));  
                tercero.setCodDomicilio(rs.getString("TER_DOM"));
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el tercero sustituido o contratado", ex);
            throw new Exception(ex);
        }
        return tercero;
    }
    
    public TerceroVO grabaNIFSust(int codOrganizacion, String numExped, TerceroVO tercero, Connection con) throws SQLException, Exception{         
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(numExped.substring(0, 4));
        
        try {
        //--------- borro la fila con los datos del tercero sustituido en el expediente -----------
            query = "delete from E_EXT " +
                    " where EXT_MUN = " + codOrganizacion + 
                    "   and EXT_EJE = " + ejer + 
                    "   and EXT_NUM = '" +  numExped + "' " +
                    "   and EXT_ROL = 3 "  ;
            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (codOrganizacion==1) {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            st.executeUpdate(query); 
        //--------- inserto la fila con los datos del tercero sustituido en el expediente -----------
            query = "insert into E_EXT (EXT_MUN,EXT_EJE,EXT_NUM,EXT_TER,EXT_NVR,EXT_DOT,EXT_ROL,EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA)" +
                    "  values( " + codOrganizacion + ", " + ejer + ", '" +  numExped + "', " + tercero.getCodTercero() + ", " + tercero.getVerTercero() + ", "+ tercero.getCodDomicilio() + ", 3, 'CONCM', 0, '0')"  ;
            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (codOrganizacion==1) {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            tercero.setNumGraba(st.executeUpdate(query));
            return tercero;  
        } catch(Exception ex){
            log.error("Se ha producido un error insertando el rol de persona sustituida", ex);
            throw new SQLException(ex);
        }
    }	

    public InfoContactoVO getAnteriorExpedienteSustituida(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        //--------- leo el anterior numero de expediente cuya tercero sustituido sea el mismo que la del expediente que estoy procesando --------     
        //--------- y tenga datos suplementarios de tercero sustituido --------     
        try {
            query = "select max(EXT_NUM) as MAX_NUM" +
                    "  from E_EXT       " +
                    "inner join E_TNU " +
                    "        on EXT_MUN = TNU_MUN" +
                    "       and EXT_NUM = TNU_NUM" +
                    "       and TNU_COD in ( 'REDUCPERSSUST', 'JORNPERSSUST', 'GRADMINUSDEPEN') " +
                    "inner join E_TXT " +
                    "        on EXT_MUN = TXT_MUN" +
                    "       and EXT_NUM = TXT_NUM" +
                    "       and TXT_COD in ( 'NOMBAPEDEPEN') " +
                    "inner join E_TFE " +
                    "        on EXT_MUN = TFE_MUN" +
                    "       and EXT_NUM = TFE_NUM" +
                    "       and TFE_COD in ( 'FECNAPDEPEN') " +
                    "inner join E_TDE " +
                    "        on EXT_MUN = TDE_MUN" +
                    "       and EXT_NUM = TDE_NUM" +
                    "       and TDE_COD in ( 'TIPOJORNPERSSUST', 'RELACPERSDEPEN', 'NIVELSTUDIOSPERSSUST', 'PERSDEPENMINUS') " +
                    " where EXT_PRO = 'CONCM'" +
                    "   and EXT_ROL = 3" +
                    "   and EXT_TER = '" + infoContact.getCodTercero()+"'" +
            //     +  "   and EXT_NVR = '"+infoContact.getVerTercero()+"'"
                    "   and EXT_NUM <> '" + infoContact.getNumExpediente() + "'"          // ---- para que no me de el mismo expediente que estoy grabando
                    ;

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                infoContact.setNumExpAnterior(rs.getString("MAX_NUM"));
                if(log.isDebugEnabled()) {
                    log.debug("Va a copiar los datos de la persona sustituida del expediente: " + rs.getString("MAX_NUM"));
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.debug("Va a copiar los datos de la persona sustituida del expediente:  " + rs.getString("MAX_NUM"));
                }
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando datos del contacto", ex);
            throw new SQLException(ex);
        }
        return infoContact;        
    }
        
    public void copiarDatosSustituidoTNU(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campos suplementarios numericos del tercero sustituido ------------------------------
            query = "delete " + 
                    "  from E_TNU" +
                    " where TNU_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TNU_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TNU_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_NUM_REDUCPERSSUST +"', '" +
                                             ConstantesMeLanbide36.CAMPO_NUM_JORNPERSSUST +"', '" +
                                             ConstantesMeLanbide36.CAMPO_NUM_GRADMINUSDEPEN +"') ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TNU " +
                    "     select TNU_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TNU_COD , " +
                    "            TNU_VALOR " +  
                    "       from E_TNU" +
                    "      where TNU_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TNU_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "        and TNU_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_NUM_REDUCPERSSUST +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_NUM_JORNPERSSUST +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_NUM_GRADMINUSDEPEN +"') ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    

        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos numericos del tercero sustituido", ex);
            throw new SQLException(ex);
        }
    }
    
    public void copiarDatosSustituidoTXT(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campo suplementarios de texto del tercero sustituido ------------------------------
            query = "delete " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_TEXTO_NOMBAPEDEPEN +"') ";

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TXT " +
                    "     select TXT_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TXT_COD , " +
                    "            TXT_VALOR " +  
                    "       from E_TXT" +
                    "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "        and TXT_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_TEXTO_NOMBAPEDEPEN +"') ";
            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    

        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos tipo texto del tercero sustituido", ex);
            throw new SQLException(ex);
        }
    }
    
    public void copiarDatosSustituidoTFE(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campo suplementarios de tipo fecha del tercero sustituido ------------------------------
            query = "delete " + 
                    "  from E_TFE" +
                    " where TFE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TFE_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TFE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_FECHA_FECNAPDEPEN +"') ";

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TFE " +
                    "     select TFE_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TFE_COD , " +
                    "            TFE_VALOR, " + 
                    "            null, " +
                    "            null " +
                    "       from E_TFE" +
                    "      where TFE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TFE_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "        and TFE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_FECHA_FECNAPDEPEN +"') ";
            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    
            
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos tipo fecha del tercero sustituido", ex);
            throw new SQLException(ex);
        }
    }
    
    public void copiarDatosSustituidoTDE(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campo suplementarios de tipo desplegable del tercero sustituido ------------------------------
            query = "delete " + 
                    "  from E_TDE" +
                    " where TDE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TDE_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TDE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_DESP_TIPOJORNPERSSUST +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_RELACPERSDEPEN +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_NIVELSTUDIOSPERSSUST +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_PERSDEPENMINUS +"' ) ";

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos de tipo desplegable copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TDE " +
                    "     select TDE_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TDE_COD , " +
                    "            TDE_VALOR " +  
                    "       from E_TDE" +
                    "      where TDE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TDE_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "        and TDE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_DESP_TIPOJORNPERSSUST +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_RELACPERSDEPEN +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_NIVELSTUDIOSPERSSUST +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_PERSDEPENMINUS +"') ";

            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    
            
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos tipo desplegable del tercero sustituido", ex);
            throw new SQLException(ex);
        }
    }
    
    public TerceroVO grabaNIFContr(int codOrganizacion, String numExped, TerceroVO tercero, Connection con) throws SQLException, Exception{         
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(numExped.substring(0, 4));
        
        try {
        //--------- borro la fila con los datos del tercero contratado en el expediente -----------
            query = "delete from E_EXT " +
                    " where EXT_MUN = " + codOrganizacion + 
                    "   and EXT_EJE = " + ejer + 
                    "   and EXT_NUM = '" +  numExped + "' " +
                    "   and EXT_ROL = 4 "  ;
            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (codOrganizacion==1) {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            st.executeUpdate(query); 
        //--------- inserto la fila con los datos del tercero contratado en el expediente -----------
            query = "insert into E_EXT (EXT_MUN,EXT_EJE,EXT_NUM,EXT_TER,EXT_NVR,EXT_DOT,EXT_ROL,EXT_PRO,MOSTRAR,EXT_NOTIFICACION_ELECTRONICA)" +
                    "  values( " + codOrganizacion + ", " + ejer + ", '" +  numExped + "', " + tercero.getCodTercero() + ", " + tercero.getVerTercero() + ", "+ tercero.getCodDomicilio() + ", 4, 'CONCM', 0, '0')"  ;
            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (codOrganizacion==1) {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            tercero.setNumGraba(st.executeUpdate(query));
            return tercero;  
        } catch(Exception ex){
            log.error("Se ha producido un error insertando el rol de persona contratada", ex);
            throw new SQLException(ex);
        }
    }
    
        public InfoContactoVO getAnteriorExpedienteContratada(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        //--------- leo el anterior numero de expediente cuya tercero contratado sea el mismo que la del expediente que estoy procesando --------     
        //--------- y tenga datos suplementarios de tercero contratado --------     
        try {
            query = "select max(EXT_NUM) as MAX_NUM" +
                    "  from E_EXT       " +
                    "inner join E_TNU " +
                    "        on EXT_MUN = TNU_MUN" +
                    "       and EXT_NUM = TNU_NUM" +
                    "       and TNU_COD in ( 'JORNPERSCONT', 'MESESDESEMPERSCONT') " +
                    "inner join E_TFE " +
                    "        on EXT_MUN = TFE_MUN" +
                    "       and EXT_NUM = TFE_NUM" +
                    "       and TFE_COD in ( 'FECFINCONTRATO', 'FECBAJASS', 'FECINICONTRATO', 'FECALTASS') " +
                    "inner join E_TDE " +
                    "        on EXT_MUN = TDE_MUN" +
                    "       and EXT_NUM = TDE_NUM" +
                    "       and TDE_COD in ( 'TIPOJORNPERSCONT', 'DURCONTPERSCONTR', 'NIVELSTUDIOSPERSCONT', 'INMIGPERCON', " +
                    "                        'MINUSPERCON', 'RMLPERSCONT', 'OTROPERSCONT', 'COOPREGAUTONPERSCONT', 'TRABPERSCONT', 'PLDPERSCONT') " +	
                    " where EXT_PRO = 'CONCM'" +
                    "   and EXT_ROL = 4" +
                    "   and EXT_TER = '"+infoContact.getCodTercero()+"'" +  
            //     +  "   and EXT_NVR = '"+infoContact.getVerTercero()+"'"
                    "   and EXT_NUM <> '" + infoContact.getNumExpediente() + "'"         // ---- para que no me de el mismo expediente que estoy grabando
                    ;

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                infoContact.setNumExpAnterior(rs.getString("MAX_NUM"));
                if(log.isDebugEnabled()) {
                    log.debug("Va a copiar los datos de la persona contratada del expediente: " + rs.getString("MAX_NUM"));
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.debug("Va a copiar los datos de la persona contratada del expediente:  " + rs.getString("MAX_NUM"));
                }
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando datos del contacto", ex);
            throw new SQLException(ex);
        }
        return infoContact;        
    }
        
    public void copiarDatosContratadoTNU(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // ------------------------ Campos  numericos suplementarios persona contratada -----------------------------------------------------------
            query = "delete " + 
                    "  from E_TNU" +
                    " where TNU_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TNU_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TNU_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_NUM_JORNPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_NUM_MESESDESEMPERSCONT +"') ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos numericos copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TNU " +
                    "     select TNU_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TNU_COD , " +
                    "            TNU_VALOR " +  
                    "       from E_TNU" +
                    "      where TNU_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TNU_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "        and TNU_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_NUM_JORNPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_NUM_MESESDESEMPERSCONT +"') ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    

        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos numericos del tercero contratado", ex);
            throw new SQLException(ex);
        }
    }
    
    public void copiarDatosContratadoTFE(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campos suplementarios de tipo fecha del la persona contratada ------------------------------
            query = "delete " + 
                    "  from E_TFE" +
                    " where TFE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TFE_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TFE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_FECHA_FECFINCONTRATO +"', '" +
                                             ConstantesMeLanbide36.CAMPO_FECHA_FECBAJASS +"', '" +
                                             ConstantesMeLanbide36.CAMPO_FECHA_FECINICONTRATO +"', '" +
                                             ConstantesMeLanbide36.CAMPO_FECHA_FECALTASS +"') ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TFE " +
                    "     select TFE_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TFE_COD , " +
                    "            TFE_VALOR, " +
                    "            null, " +
                    "            null " +
                    "       from E_TFE" +
                    "      where TFE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TFE_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "   and TFE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_FECHA_FECFINCONTRATO +"', '" +
                                             ConstantesMeLanbide36.CAMPO_FECHA_FECBAJASS +"', '" +
                                             ConstantesMeLanbide36.CAMPO_FECHA_FECINICONTRATO +"', '" +
                                             ConstantesMeLanbide36.CAMPO_FECHA_FECALTASS +"') ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    
            
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos tipo fecha del tercero contratado", ex);
            throw new SQLException(ex);
        }
    }
    
    public void copiarDatosContratadoTDE(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campos suplementarios de tipo desplegable del la persona contratada ------------------------------
            query = "delete " + 
                    "  from E_TDE" +
                    " where TDE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TDE_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TDE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_DESP_TIPOJORNPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_DURCONTPERSCONTR +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_NIVELSTUDIOSPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_INMIGPERCON +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_MINUSPERCON +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_RMLPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_OTROPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_COOPREGAUTONPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_TRABPERSCONT +"', '" +
                                             ConstantesMeLanbide36.CAMPO_DESP_PLDPERSCONT +"' ) ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);
            
            //--------- inserto los valores de los campos de tipo desplegable copiandolos del ultimo expediente que los tiene informados -----------
            query = "insert into E_TDE " +
                    "     select TDE_MUN , " + 
                                 ejer + ", " +
                    "            '"+ infoContact.getNumExpediente() +"' , " +
                    "            TDE_COD , " +
                    "            TDE_VALOR " +  
                    "       from E_TDE" +
                    "      where TDE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "        and TDE_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                    "        and TDE_COD IN ( '"+ ConstantesMeLanbide36.CAMPO_DESP_TIPOJORNPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_DURCONTPERSCONTR +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_NIVELSTUDIOSPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_INMIGPERCON +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_MINUSPERCON +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_RMLPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_OTROPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_COOPREGAUTONPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_TRABPERSCONT +"', '" +
                                                  ConstantesMeLanbide36.CAMPO_DESP_PLDPERSCONT +"' ) ";

            
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);    
            
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos tipo desplegable del tercero contratado", ex);
            throw new SQLException(ex);
        }
    }
        
    public InfoContactoVO getEmpresaExpediente(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        //------- leo el dato de la empresa del expediente que estoy procesando ----------        
        try {
            query = "select EXT_ROL, EXT_TER, EXT_NVR" +
                    "  from E_EXT                    " +
                    " where EXT_MUN = " + infoContact.getCodOrganizacion() + 
                    "   and EXT_NUM = '"+ infoContact.getNumExpediente() + "'"    +
                    "   and EXT_ROL = 1 ";

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                infoContact.setCodTercero(rs.getString("EXT_TER"));
                infoContact.setVerTercero(rs.getInt("EXT_NVR"));               
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando valores de datos del contacto", ex);
            throw new SQLException(ex);
        }
          finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return infoContact;        
    }
    
    public InfoContactoVO getAnteriorExpediente(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        //--------- leo el anterior numero de expediente cuya empresa sea la misma que la del expediente que estoy procesando --------     
        //--------- y tenga datos suplementarios de empresa --------     
        try {
            query = "select max(EXT_NUM) as MAX_NUM" +
                    "  from E_EXT       " +
                    "inner join E_TXT " +
                    "        on EXT_MUN = TXT_MUN" +
                    "       and EXT_NUM = TXT_NUM" +
                    "       and TXT_COD in ('NOMCOMPPERCONT', 'CARGPERCONT', 'CODCNAE', 'DESCNAE', 'TIPOEMPRESA', 'TELFPERCONT', 'EMAILPERCONT', " +
                    "                       'CODFORJUREMP', 'DESCFORMJUREMP', 'CODSECTOREMP', 'DESSECTOREMP') " +
                    " where EXT_PRO = 'CONCM'" +
                    "   and EXT_ROL = 1" +
                    "   and EXT_TER = '"+infoContact.getCodTercero()+"'" 
            //     +  "   and EXT_NVR = '"+infoContact.getVerTercero()+"'"
                    ;

            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
                log.error("Va a copiar del expediente: " + rs.getString("MAX_NUM"));
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next()){
                infoContact.setNumExpAnterior(rs.getString("MAX_NUM"));             
            }
        } catch(Exception ex){
            log.error("Se ha producido un error recuperando datos del contacto", ex);
            throw new SQLException(ex);
        }
          finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return infoContact;        
    }
    
    public void copiarDatosContactoTXT(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));

        try {
            // --------- Campo texto 'NOMCOMPPERCONT' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_NOMCOMPPERCONT  +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_NOMCOMPPERCONT +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);
            }
            // --------- Campo texto 'CARGPERCONT' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_CARGPERCONT  +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_CARGPERCONT +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'DESCNAE' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_DESCNAE  +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_DESCNAE +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'TELFPERCONT' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_TELFPERCONT  +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_TELFPERCONT +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'EMAILPERCONT' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_EMAILPERCONT +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_EMAILPERCONT +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'CODFORJUREMP' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_CODFORJUREMP +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_CODFORJUREMP +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'DESCFORMJUREMP' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_DESCFORMJUREMP +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_DESCFORMJUREMP +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'CODSECTOREMP' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_CODSECTOREMP +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_CODSECTOREMP +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
            // --------- Campo texto 'DESSECTOREMP' ------------------------------
            query = "select * " + 
                    "  from E_TXT" +
                    " where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TXT_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_DESSECTOREMP +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TXT " +
                        "     select TXT_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TXT_COD , " +
                        "            TXT_VALOR " +  
                        "       from E_TXT" +
                        "      where TXT_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TXT_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TXT_COD = '"+ ConstantesMeLanbide36.CAMPO_TEXTO_DESSECTOREMP +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            }             
            
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos de texto del contacto", ex);
            throw new SQLException(ex);
        }
          finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }      
    }
    
    public void copiarDatosContactoTNU(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campo numerico 'CODCNAE' ------------------------------
            query = "select * " + 
                    "  from E_TNU" +
                    " where TNU_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TNU_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TNU_COD = '"+ ConstantesMeLanbide36.CAMPO_NUM_CODCNAE +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TNU " +
                        "     select TNU_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TNU_COD , " +
                        "            TNU_VALOR " +  
                        "       from E_TNU" +
                        "      where TNU_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TNU_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TNU_COD = '"+ ConstantesMeLanbide36.CAMPO_NUM_CODCNAE  +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos numericos del contacto", ex);
            throw new SQLException(ex);
        }
          finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }      
    }
    
     public void copiarDatosContactoDES(InfoContactoVO infoContact, Connection con) throws SQLException, Exception{
            
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        
        Integer ejer = Integer.parseInt(infoContact.getNumExpediente().substring(0, 4));
        
        try {
        // --------- Campo desplegable 'TIPOEMPRESA' ------------------------------
            query = "select * " + 
                    "  from E_TDE" +
                    " where TDE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                    "   and TDE_NUM = '"+ infoContact.getNumExpediente() +"'" + 
                    "   and TDE_COD = '"+ ConstantesMeLanbide36.CAMPO_DESP_TIPOEMPRESA  +"' ";
            if(log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            if (infoContact.getCodOrganizacion()=='1') {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(!rs.next()){            
                //--------- inserto los valores de los campos de tipo texto copiandolos del ultimo expediente que los tiene informados -----------
                query = "insert into E_TDE " +
                        "     select TDE_MUN , " + 
                                     ejer + ", " +
                        "            '"+ infoContact.getNumExpediente() +"' , " +
                        "            TDE_COD , " +
                        "            TDE_VALOR " +  
                        "       from E_TDE" +
                        "      where TDE_MUN = '"+ infoContact.getCodOrganizacion() +"'" + 
                        "        and TDE_NUM = '"+ infoContact.getNumExpAnterior() +"'" + 
                        "        and TDE_COD = '"+ ConstantesMeLanbide36.CAMPO_DESP_TIPOEMPRESA   +"' ";
                if(log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                if (infoContact.getCodOrganizacion()=='1') {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);    
            } 

            st = con.createStatement();
            st.executeUpdate(query);  
        } catch(Exception ex){
            log.error("Se ha producido un error insertando valores de datos desplegable del contacto", ex);
            throw new SQLException(ex);
        }
          finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null)
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }      
    }    
}
