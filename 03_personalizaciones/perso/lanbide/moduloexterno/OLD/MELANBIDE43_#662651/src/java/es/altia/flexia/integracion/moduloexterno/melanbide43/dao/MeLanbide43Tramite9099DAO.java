
package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Util;
import es.altia.flexia.integracion.moduloexterno.melanbide43.job.GestorExpedientesCierreExptsAnuladosMiCarpetaJob;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpAvisos;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpTram;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoMisGestiones;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaLlamadasMisGestVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaNotificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoSeleccionadoV0;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tml;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099CerrarVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099VO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;


public class MeLanbide43Tramite9099DAO {
    
    //Logger
    private static Logger log = Logger.getLogger(MeLanbide43Tramite9099DAO.class);
    SimpleDateFormat formatDateLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");


    //Instancia
    private static MeLanbide43Tramite9099DAO instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide43Tramite9099DAO, si no existe la crea
     * @return MeLanbide43Tramite9099DAO
     */
    public static MeLanbide43Tramite9099DAO getInstance(){
        if(log.isDebugEnabled()) log.info("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide43Tramite9099DAO.class){
                if(instance == null){
                    instance = new MeLanbide43Tramite9099DAO();
                }//if(instance == null)
            }//synchronized(MeLanbide43DAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    
    public List<Tramite9099VO> getProcedimientosConTramite9099( Connection con) throws Exception {
        
        
      
        List<Tramite9099VO> tramite9099VOList = new ArrayList<Tramite9099VO>();
        Statement st = null;
        ResultSet rs = null;
       
        try {
            String query = null;
            //Seleccionamos todos los que tengan el tramite 9099 se ignora la fecha de fin y se presupone que sea un mes 31 dias
            //Se cogen solo los de la lista de la #600341
            query = "SELECT TRA_PRO,TRA_COD,TRA_COU, TRA_PLZ,TRA_UND FROM E_TRA WHERE TRA_COU=9099 AND TRA_FBA IS NULL AND TRA_PRO IN ('AERTE', 'APEA', 'APEC', 'APEI', 'APES', 'CCEE', 'CEESC', 'COLVU', 'CONCM', 'CUOTS', 'DECEX', 'DISCP', 'DISCT', 'DLDUR', 'ECA', 'ENTAP', 'GEL', 'GO', 'IGCEE', 'IKER', 'ININ', 'LAK', 'LAKCC', 'LEI', 'LPEEL', 'PEX', 'REINT', 'REPLE', 'RGCF', 'RGCFM', 'SEI', 'TRECO', 'UAAP')";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
               
               Tramite9099VO tramite9099VO = new Tramite9099VO(rs.getString("TRA_PRO"),rs.getInt("TRA_COD"),31 ,"M");
                tramite9099VOList.add(tramite9099VO);
            }
        } catch (Exception ex) {
            log.error("Error en getProcedimientosConTramite9099 " + ex.getMessage(), ex);
            throw ex;
        } finally {
            log.debug("Procedemos a cerrar el statement y el resultset");
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info("getProcedimientosConTramite9099 END: " + tramite9099VOList.size());
        return tramite9099VOList;
    }
    
    
     public List<Tramite9099CerrarVO> getProcedimientosTramites9099PorProcedimiento(String procedimiento, Integer tramite, Connection con) throws Exception {
        
        
      
       
        Statement st = null;
        ResultSet rs = null;
        List<Tramite9099CerrarVO> expedientes = new ArrayList<Tramite9099CerrarVO>();
        if(!ConfigurationParameter.getParameter("CODIGO_RESOLUCION_"+procedimiento, ConstantesMeLanbide43.FICHERO_PROPIEDADES).isEmpty()){
        try {
            String query = null;
            //Seleccionamos todos los que tengan el tramite 9099 y una fecha de fin
            query ="SELECT t.* FROM E_CRO t, E_EXP e WHERE  t.cro_num IN (SELECT t.CRO_NUM FROM E_CRO t, E_EXP e WHERE e.exp_pro='"+procedimiento+"' AND e.exp_est=0 AND t.cro_pro=e.exp_pro and t.cro_eje=e.exp_eje and t.cro_num=e.exp_num AND t.cro_tra="+tramite+" AND CRO_FEF IS NULL) AND e.exp_est=0 AND t.cro_pro=e.exp_pro and t.cro_eje=e.exp_eje and t.cro_num=e.exp_num AND t.cro_tra=(SELECT TRA_COD FROM E_TRA WHERE TRA_COU = "+ConfigurationParameter.getParameter("CODIGO_RESOLUCION_"+procedimiento, ConstantesMeLanbide43.FICHERO_PROPIEDADES)+" AND TRA_PRO='"+procedimiento+"' AND TRA_FBA IS NULL) AND CRO_FEF IS NOT NULL"; 
            
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
             while (rs.next()) {
                Tramite9099CerrarVO expte = new Tramite9099CerrarVO();
                String numExp = rs.getString("CRO_NUM");
                log.info("numExp = " + numExp);
                expte.setCodOrganizacion(rs.getInt("CRO_MUN"));
                expte.setCodProcedimiento(rs.getString("CRO_PRO"));
                expte.setCodTramiteActal(rs.getInt("CRO_TRA"));
                expte.setEjercicio(rs.getInt("CRO_EJE"));
                expte.setNumeroExpediente(rs.getString("CRO_NUM"));
                expte.setOcurrenciaTramiteActual(rs.getInt("CRO_OCU"));
                //Fecha en que empieza a contar para que se cierre es el del anterior tramite
                expte.setFechaInicioTramite(rs.getDate("CRO_FEF"));              
                expedientes.add(expte);
            }
        } catch (Exception ex) {
            log.error("Error en getProcedimientosConTramite9099 " + ex.getMessage(), ex);
            throw ex;
        } finally {
            log.debug("Procedemos a cerrar el statement y el resultset");
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        } else {
            log.error("No hay informacion en el properties de el procedimiento"+ procedimiento);
        }
        log.info("getProcedimientosTramites9099PorProcedimiento END: " + expedientes.size());
        return expedientes;
    }
     
     
     public Tramite9099CerrarVO getProcedimientosTramites9099Cerrar(String procedimiento, Integer tramite,String numExpediente, Connection con, Integer ocurrenciaTramite) throws Exception {
        
        
      
        Tramite9099CerrarVO expte = new Tramite9099CerrarVO();
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            //Seleccionamos todos los que tengan el tramite 9099 y una fecha de fin 
            query = "SELECT t.* FROM E_CRO t, E_EXP e WHERE e.exp_pro='"+procedimiento+"' AND t.CRO_NUM='"+numExpediente+"' AND t.CRO_OCU="+ocurrenciaTramite+" AND e.exp_est=0 AND t.cro_pro=e.exp_pro and t.cro_eje=e.exp_eje and t.cro_num=e.exp_num AND t.cro_tra="+tramite+" AND CRO_FEF IS NULL";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            
             while (rs.next()) {
               
                String numExp = rs.getString("CRO_NUM");
                log.info("numExp = " + numExp);
                expte.setCodOrganizacion(rs.getInt("CRO_MUN"));
                expte.setCodProcedimiento(rs.getString("CRO_PRO"));
                expte.setCodTramiteActal(rs.getInt("CRO_TRA"));
                expte.setEjercicio(rs.getInt("CRO_EJE"));
                expte.setNumeroExpediente(rs.getString("CRO_NUM"));
                expte.setOcurrenciaTramiteActual(rs.getInt("CRO_OCU"));
                expte.setFechaInicioTramite(rs.getDate("CRO_FEI"));              
               expte.setUor(rs.getInt("CRO_UTR"));
               expte.setUsuario(rs.getInt("CRO_USU"));
            }
        } catch (Exception ex) {
            log.error("Error en getProcedimientosConTramite9099 " + ex.getMessage(), ex);
            throw ex;
        } finally {
            log.debug("Procedemos a cerrar el statement y el resultset");
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info("getProcedimientosTramites9099PorProcedimiento END: " + expte.getNumeroExpediente());
        return expte;
    }
    
     public String cerrarTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, Connection con, Integer ocurrenciaTramite) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            String query = "UPDATE E_CRO set CRO_FEF=SYSDATE"
                    + ", CRO_OBS = cro_obs  || chr(13) || 'Tramite cerrado por job cerrar tramites 9099'"
                    + ", CRO_USF = 5"
                    + " where CRO_MUN = " + codOrganizacion
                    + " and CRO_PRO = '" + procedimiento + "'"
                    + " and CRO_NUM = '" + numExp + "'"
                    + " and CRO_OCU = " + ocurrenciaTramite + ""
                    + " and CRO_TRA = " + codTramite;
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);

        } catch (SQLException ex) {
            log.error("Se ha producido un error al cerrar el tr?mite " + codTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor != null ? valor.toString() : null;
    }
     
     public int insertarLineasLogJob(Connection con, String numeroExpediente, String estado, String mensaje) throws Exception {
        log.info("insertarLineasLogJob MELANBIDE_43_TRAMITE9099_JOB - Begin () ");
        int id = 0;
       CallableStatement  pt = null;
        ResultSet rs = null;

        try {
            String query = "INSERT INTO MELANBIDE43_TRAMITE9099_JOB"
                    + " ( NUMEXPEDIENTE, ESTADOLOG, DESCRIPCION) "
                    + " VALUES (?,?,?)";
                log.info("query: " +query);
                log.info("----Fin aplicar condiciones----");
            
          

            pt = con.prepareCall(query);
            pt.setString(1, numeroExpediente);
            pt.setString(2, estado);
            pt.setString(3, mensaje);
           
            log.info("sql = " + query);
            pt.executeUpdate();
       
            
           
             
            
        } catch (Exception ex) {
            log.info("Se ha producido un error al registrar la linea en el log del job ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("insertarLineasLogJob MELANBIDE_43_TRAMITE9099_JOB - End () ");
        return id;
    }
     
     
     
public String getTramiteSalida(int codOrganizacion, Connection con, String procedimiento, int codigoTramite ) throws Exception {
        log.info("getTramiteSalida - Begin () " );
        String retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try{
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "SELECT FLS_TRA,FLS_NUS,TRA_COU,FLS_CTS,TML_VALOR,SAL_OBL FROM E_FLS,e_tra,e_tml,e_sal WHERE FLS_PRO='"+procedimiento+"'	AND FLS_TRA="+codigoTramite+" AND FLS_MUN="+codOrganizacion+"	AND FLS_NUC= (CASE WHEN FLS_NUC=0 THEN 0 ELSE 2 END) AND TRA_FBA IS	null AND e_fls.FLS_MUN=e_tra.TRA_MUN AND e_fls.FLS_PRO=e_tra.TRA_PRO AND e_fls.FLS_CTS=e_tra.TRA_COD AND e_fls.FLS_MUN=e_tml.TML_MUN AND e_fls.FLS_PRO=e_tml.TML_PRO AND e_fls.FLS_CTS=e_tml.TML_TRA AND e_tml.TML_CMP='NOM' AND e_tml.TML_LENG='1' AND e_fls.FLS_MUN=e_sal.SAL_MUN AND e_fls.FLS_PRO=e_sal.SAL_PRO AND e_fls.FLS_TRA=e_sal.SAL_TRA ORDER BY 1" ;


            
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
             

                
               retorno = rs.getString("FLS_CTS");
              
               
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getTramiteSalida - End () ");
        return retorno;
    }

public Integer getOcurrencia(int codOrganizacion, String numExp, String codTramite,String procedimiento, Integer uor, Integer usuario, Connection con) throws Exception {
        log.info("getOcurrencia - Begin () " );
        Integer retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try{
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "SELECT NVL(max(CRO_OCU),0) AS OCURRENCIA FROM E_CRO WHERE CRO_PRO='"+procedimiento+"' AND CRO_TRA="+codTramite+" AND CRO_NUM='"+numExp+"'" ;


            
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
             

              
               retorno = rs.getInt("OCURRENCIA")+1;
              
               
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getOcurrencia - End () ");
        return retorno;
    }

 public String abrirTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, Integer uor, Integer usuario, Connection con) throws Exception {
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            if (numExp != null && !"".equals(numExp)) {
                String datos[] = numExp.split("/");
                String query = "INSERT into E_CRO"
                        + " (CRO_PRO,CRO_EJE,CRO_NUM, CRO_TRA,CRO_FEI,CRO_USU,CRO_UTR,CRO_MUN, CRO_OCU, CRO_OBS)values (?,?,?,?,SYSDATE,?,?,?,?,?)";
                log.debug("sql = " + query);

                ps = con.prepareStatement(query);
                  log.debug("parametro 1 = " + procedimiento);
                ps.setString(1, procedimiento);
                log.debug("parametro 2 = " + Integer.valueOf(datos[0]));
                ps.setInt(2, Integer.parseInt(datos[0]));
                log.debug("parametro 3 = " + numExp);
                ps.setString(3, numExp);
                log.debug("parametro 4 = " + codTramite);
                ps.setString(4, codTramite);
                log.debug("parametro 5 = " + usuario);
                ps.setInt(5, usuario);
                log.debug("parametro 6 = " + uor);
                ps.setInt(6, uor);
                //Calculamos el numero de Ocurrencia que tiene que tener
               Integer ocurrencia =  getOcurrencia(codOrganizacion,  numExp,  codTramite, procedimiento,  uor,  usuario,  con);
                log.debug("parametro 7 = " + ocurrencia);
                ps.setInt(7, ocurrencia);
                log.debug("parametro 8 = " + codOrganizacion);
                ps.setInt(8, codOrganizacion);
                
                ps.setString(9, "Tramite abierto por job cerrar tramites 9099");
                rs = ps.executeQuery();
            }
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error al insertar el tramite " + codTramite, ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar el tramite " + codTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor != null ? valor.toString() : null;
    }
    
}//class
