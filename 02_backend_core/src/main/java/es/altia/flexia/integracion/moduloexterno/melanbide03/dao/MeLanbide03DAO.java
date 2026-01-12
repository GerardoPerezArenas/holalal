/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.registro.exception.AnotacionRegistroException;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import static java.lang.Math.log;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.CallableStatement;
import java.util.HashMap;

/**
 *
 * @author leires
 */
public class MeLanbide03DAO {
    private static Logger log = LogManager.getLogger(MeLanbide03ReportDAO.class);
    
    public String compruebaExpedientes(String numExp, String certif, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String hay = "0";       
         
        try
        {
            String sql = "Select Ext_Num, Ter_Doc, Ter_Nom, Ter_Ap1, Ter_Ap2,\n" +
                        "TER_TLF, TER_DCE, TER_NOC, TER_TID,EXT_ROL, COD_CERTIFICADO  \n" +
                        "FROM E_EXT \n" +
                        "Inner Join T_Ter On Ext_Ter = Ter_Cod\n" +
                        "Inner Join E_Tdet On Tdet_Num = Ext_Num And Tdet_Pro = Ext_Pro  And Tdet_Cod = 'VALORACION2' \n" +
                        "Inner Join E_Tde On Tde_Num = Ext_Num And Tde_Cod= 'TIPOACREDITACION'\n" +
                        "left JOIN MELANBIDE03_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM AND COD_PROCEDIMIENTO = EXT_PRO\n" +
                        "Where (Tdet_Valor = 'T') And Ext_Rol = 1\n" +
                        "And Tde_Valor = 'CP' And Cod_Procedimiento = 'CEPAP'\n" +
                        "And Ter_Doc In (\n" +
                        "  Select Ter_Doc\n" +
                        "  From T_Ter\n" +
                        "  Inner Join E_Ext On Ext_Ter = Ter_Cod \n" +
                        "  Where Ext_Num = '"+numExp+"'\n" +
                        ") and Ext_Num <> '"+numExp+"' and COD_CERTIFICADO = '"+certif+"'";

           log.debug("compruebaExpedientes: " + sql.toString());

           st = con.createStatement();
           rs = st.executeQuery(sql.toString());
           
           if(rs.next()){
               hay = rs.getString("Ext_Num");
           }           
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }// finally
        return hay;
    }

    public ArrayList<String> obtenerCodigoVisibleTramitesActivosExpt(String numExpediente, Connection con) throws Exception {
        log.error("obtenerCodigoVisibleTramitesActivosExpt - dao -  BEGIN()" );
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> listaReturn = new ArrayList<String>();
        try {
            String sql = "SELECT DISTINCT E_TRA.TRA_COU COD_TRA_VISIBLE  FROM E_CRO " 
                    + " INNER JOIN E_TRA ON E_TRA.TRA_MUN=E_CRO.CRO_MUN AND E_TRA.TRA_PRO=E_CRO.CRO_PRO AND E_TRA.TRA_COD=E_CRO.CRO_TRA AND E_TRA.TRA_FBA IS NULL " 
                    + " WHERE CRO_NUM='"+numExpediente+"' AND E_CRO.CRO_FEF IS NULL";
            log.error("obtenerCodigoVisibleTramitesActivosExpt: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()) {
                listaReturn.add(rs.getString("COD_TRA_VISIBLE"));
            }
        } catch (SQLException e) {
            log.error("SQLException Consultando lista de codigos visibles de tramites activos - Expediente- "+ numExpediente,e);
        } finally {
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }// finally
        log.error("obtenerCodigoVisibleTramitesActivosExpt - dao -  BEGIN()" );
        return listaReturn;
    }

    public Map<String, String> avanzarExpedienteToEsperaExpedicion(String numExpediente, int codOrganizacion, Connection con) throws Exception {
        log.error("avanzarExpedienteToEsperaExpedicion - dao -  BEGIN()");
        Map<String, String> listaReturn = new HashMap<String, String>();
        CallableStatement cs11=null;
        String codigoOperacion = "";
        String mensajeOperacion = "";
        try {
            // en los procedures hay que pasar el esquema, para aseguar que busque los  datos correctos.
             String prefijoEsquema = ConfigurationParameter.getParameter(codOrganizacion+ConstantesMeLanbide03.BARRA_SEPARADORA+ConstantesMeLanbide03.PREFIJO_ESQUEMA_BBDD,ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            cs11 = con.prepareCall("{call " + prefijoEsquema + ".CEPAP_AVANZA_TO_ESPERA_EXPED(?,?,?)}");
            log.error("VAMOS A LLMAR AL PROCEDURE {call CEPAP_AVANZA_TO_ESPERA_EXPED(?,?,?)}");
            cs11.setString("numExpediente",numExpediente);
            cs11.registerOutParameter("codigoOperacion",java.sql.Types.VARCHAR);
            cs11.registerOutParameter("mensajeOperacion",java.sql.Types.VARCHAR);
            log.error("NoError - Parametros - ? In : " + numExpediente + " ? Out (java.sql.Types.VARCHAR) : " + java.sql.Types.VARCHAR + " ,? Out (java.sql.Types.VARCHAR) : " + java.sql.Types.VARCHAR);
            cs11.execute();
            codigoOperacion = cs11.getString("codigoOperacion");
            mensajeOperacion = cs11.getString("mensajeOperacion");
            log.error("NoError - avanzarExpedienteToEsperaExpedicion - Procedure Ejecutado:"
                    + " codigoOperacion :  " + codigoOperacion 
                    + " mensajeOperacion : " + mensajeOperacion);
            listaReturn.put("codigoOperacion", codigoOperacion);
            listaReturn.put("mensajeOperacion", mensajeOperacion);
            log.error("HashMap cargado con resultados del Procedure: " + listaReturn.size() + " - " + listaReturn.toString());
        } catch (SQLException e) {
            log.error("SQLException Ejecutando Proecedure  CEPAP_AVANZA_TO_ESPERA_EXPED - Expediente- " + numExpediente, e);
            codigoOperacion = "10";
            mensajeOperacion = "Error en BD al Ejecutar el Procedimiento Almacenado" + e.getMessage()!=null&&!"".equals(e.getMessage())?e.getMessage():e.toString();
            listaReturn.put("codigoOperacion", codigoOperacion);
            listaReturn.put("mensajeOperacion", mensajeOperacion);
        } finally {
            /** Cerramos el CallableStatement
            * Segun la documentacion si se cierra la conexion que lo ha inicializado se cierra el CallableStatement
            * pero nos aseguramos de cerrarlo
            */
           if(cs11!=null){
               cs11.close();
               log.debug("CallableStatement  - Cerrado");
           }
        }// finally
        log.error("avanzarExpedienteToEsperaExpedicion - dao -  BEGIN()");
        return listaReturn;
    }
}
