package es.altia.flexia.integracion.moduloexterno.melanbide72.jobs.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide72.job.manager.M72GestionAlarmasDECEXBatchManager;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.AlarmaVO;
import es.altia.util.conexion.AdaptadorSQL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author gerardo.perez
 */
public class M72GestionAlarmasDECEXBatchDAO {

    //Logger
     private static final Logger log = LogManager.getLogger(M72GestionAlarmasDECEXBatchDAO.class);
     
    public List<AlarmaVO> getListaExpedienteMailAlarma1( Connection con) throws Exception {
        log.info("Entramos en getListaExpedienteMailAlarma1 DAO " );
        Statement st = null;
        ResultSet rs = null;
             
        String numeroExpediente;
        String fechaAltaRegistro;
        String numeroEntradaRegistro;
        
        List<AlarmaVO> listaAlarmas = new ArrayList<AlarmaVO>();
        AlarmaVO expedientes = new AlarmaVO();
        try {
            String query =" SELECT " +
                        "    exp_num, " +
                        "    res_eje || '/' || res_num             entradaregistro, " +
                        "    to_char(res_fec, 'dd/MM/yyyy')        fechacreacionregistroiniciaexp, " +
                        "    e_exp.exp_fei                         fechainicioexpediente, " +
                        "    to_char((sysdate - 50), 'dd/MM/yyyy') fecreferencia " +
                        " FROM " +
                        "    e_exp " +
                        "    LEFT JOIN e_exr ON exr_num = exp_num " +
                        "                       AND exr_top = 0 " +
                        "    LEFT JOIN r_res ON r_res.res_dep = e_exr.exr_dep " +
                        "                       AND r_res.res_uor = e_exr.exr_uor " +
                        "                       AND res_tip = e_exr.exr_tip " +
                        "                       AND res_eje = e_exr.exr_ejr " +
                        "                       AND res_num = e_exr.exr_nre " +
                        " WHERE " +
                        "        exp_pro = 'DECEX' " +
                        "    AND res_fec < ( sysdate - 50 ) " +
                        "    AND exp_est = 0 " +
                        " ORDER BY " +
                        "    exp_num";
            
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                
            expedientes.setNumeroExpediente(rs.getString("EXP_NUM"));
            expedientes.setFechaAltaRegistro(rs.getString("fechacreacionregistroiniciaexp"));
            expedientes.setNumeroEntradaRegistro(rs.getString("entradaregistro"));
                  
            listaAlarmas.add(expedientes);
            expedientes = new AlarmaVO();
        
            }                                           
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando lista expediente" );
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
            return listaAlarmas;
    }
    public List<AlarmaVO> getListaExpedienteMailAlarma2(Connection con) throws Exception {
        log.info("Entramos en getListaExpedienteMailAlarma2 DAO " );
        Statement st = null;
        ResultSet rs = null;
        
        List<AlarmaVO> listaAlarmas = new ArrayList<AlarmaVO>();
        AlarmaVO expedientes = new AlarmaVO();
        try {
            ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
            String codigVisibleTramAcuRecNotiSolic = propmail.getString("alarma2.codigo.visible.tramite.notifRequeSolic");
            String codigVisibleTramAcuRecNoti3M = propmail.getString("alarma2.codigo.visible.tramite.notifReque3M");
            String codigVisibleTramAcuRecNotiModif = propmail.getString("alarma2.codigo.visible.tramite.notifRequeModi");
            String codigVisibleTramAcuRecNotiAnual = propmail.getString("alarma2.codigo.visible.tramite.notifRequeAnual");
            
            
            
            String query =" SELECT " +
                    "    exp_num," +
                    "    CASE " +
                    "        WHEN tramite101.cro_fef IS NULL" +
                    "             AND tramite101.cro_fei < ( sysdate - 15 ) THEN" +
                    "            tramite101.tra_cou || ' - ' || tramite101.tml_valor" +
                    "        WHEN tramite131.cro_fef IS NULL" +
                    "             AND tramite131.cro_fei < ( sysdate - 15 ) THEN" +
                    "            tramite131.tra_cou || ' - ' || tramite131.tml_valor" +
                    "        WHEN tramite191.cro_fef IS NULL" +
                    "             AND tramite191.cro_fei < ( sysdate - 15 ) THEN" +
                    "            tramite191.tra_cou || ' - ' || tramite191.tml_valor" +
                    "        WHEN tramite181.cro_fef IS NULL" +
                    "             AND tramite181.cro_fei < ( sysdate - 15 ) THEN" +
                    "            tramite181.tra_cou || ' - ' || tramite181.tml_valor" +
                    "    END AS caso" +
                    " FROM " +
                    "    e_exp " +
                    "    LEFT JOIN ( (" +
                    "        SELECT " +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "            ,e_tml.tml_valor" +
                    "        FROM " +
                    "                 e_cro " +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "            left join e_tml on tml_pro=tra_pro and tml_tra=tra_cod " +
                    "    ) ) tramite101 ON exp_eje = tramite101.cro_eje" +
                    "                      AND exp_pro = tramite101.cro_pro" +
                    "                      AND exp_num = tramite101.cro_num" +
                    "                      AND tramite101.tra_cou = '" +  codigVisibleTramAcuRecNotiSolic +"'" +
                    "    LEFT JOIN ( (" +
                    "        SELECT" +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "            ,e_tml.tml_valor" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "            left join e_tml on tml_pro=tra_pro and tml_tra=tra_cod " +
                    "    ) ) tramite131 ON exp_eje = tramite131.cro_eje" +
                    "                      AND exp_pro = tramite131.cro_pro" +
                    "                      AND exp_num = tramite131.cro_num" +
                    "                      AND tramite131.tra_cou = '" +  codigVisibleTramAcuRecNoti3M +"'" +
                    "    LEFT JOIN ( (" +
                    "        SELECT " +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "            ,e_tml.tml_valor" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "            left join e_tml on tml_pro=tra_pro and tml_tra=tra_cod " +
                    "    ) ) tramite191 ON exp_eje = tramite191.cro_eje" +
                    "                      AND exp_pro = tramite191.cro_pro" +
                    "                      AND exp_num = tramite191.cro_num" +
                    "                      AND tramite191.tra_cou = '" +  codigVisibleTramAcuRecNotiModif +"'" +
                    "    LEFT JOIN ( (" +
                    "        SELECT " +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "            ,e_tml.tml_valor" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "            left join e_tml on tml_pro=tra_pro and tml_tra=tra_cod " +
                    "    ) ) tramite181 ON exp_eje = tramite181.cro_eje" +
                    "                      AND exp_pro = tramite181.cro_pro" +
                    "                      AND exp_num = tramite181.cro_num" +
                    "                      AND tramite181.tra_cou = '" +  codigVisibleTramAcuRecNotiAnual +"'" +
                    " WHERE " +
                    "        exp_pro = 'DECEX'" +
                    "    AND ( ( tramite101.cro_fef IS NULL" +
                    "            AND tramite101.cro_fei < ( sysdate - 15 ) )" +
                    "          OR ( tramite131.cro_fef IS NULL" +
                    "               AND tramite131.cro_fei < ( sysdate - 15 ) )" +
                    "          OR ( tramite191.cro_fef IS NULL" +
                    "               AND tramite191.cro_fei < ( sysdate - 15 ) )" +
                    "          OR ( tramite181.cro_fef IS NULL" +
                    "               AND tramite181.cro_fei < ( sysdate - 15 ) ) )" +
                    "               ";


                    
                    
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                
            expedientes.setNumeroExpediente(rs.getString("EXP_NUM"));
            expedientes.setsubCasoAlarma(rs.getString("CASO"));
           //expedientes.setFechaAltaRegistro(rs.getString("fechacreacionregistroiniciaexp"));
            //expedientes.setNumeroEntradaRegistro(rs.getString("entradaregistro"));
            
            listaAlarmas.add(expedientes);
            expedientes = new AlarmaVO();
        
            }  
            } catch (Exception ex) {
                //ListaAlarmas.clear();
                log.error("Se ha producido un error recuperando lista alarma 2 " +  ex);
                throw new Exception(ex);
            } finally {
             log.debug("Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            return listaAlarmas;
        }
    public List<AlarmaVO> getListaExpedienteMailAlarma3( Connection con) throws Exception {
        log.info("Entramos en getListaExpedienteMailAlarma3 DAO - " );
        Statement st = null;
        ResultSet rs = null;
        List<AlarmaVO> listaAlarmas = new ArrayList<AlarmaVO>();
        AlarmaVO expedientes = new AlarmaVO();
        try {
            ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
            String codigVisibleRegisEntr = propmail.getString("alarma3.codigo.visible.tramite.notifacuse3M");
            
            String query ="SELECT" +
                    "    exp_num" +
                    " FROM " +
                    "    e_exp" +
                    "    LEFT JOIN ( (" +
                    "        SELECT " +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "    ) ) tramite300 ON exp_eje = tramite300.cro_eje" +
                    "                      AND exp_pro = tramite300.cro_pro" +
                    "                      AND exp_num = tramite300.cro_num" +
                    "                      AND tramite300.tra_cou = '" +  codigVisibleRegisEntr +"'" +
                    " WHERE " +
                    "        exp_pro = 'DECEX'" +
                    "    AND ( tramite300.cro_fef IS NULL" +
                    "          AND tramite300.cro_fei < ( sysdate - 15 ) )";
             if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }                                     
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                
            expedientes.setNumeroExpediente(rs.getString("EXP_NUM"));
//            expedientes.setFechaAltaRegistro(rs.getString("fechacreacionregistroiniciaexp"));
//            expedientes.setNumeroEntradaRegistro(rs.getString("entradaregistro"));
//            
            listaAlarmas.add(expedientes);
            expedientes = new AlarmaVO();
        
            }  
            } catch (Exception ex) {
                //ListaAlarmas.clear();
                log.error("Se ha producido un error recuperando lista alarma 3 " +  ex);
                throw new Exception(ex);
            } finally {
             log.debug("Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            return listaAlarmas;
        }
    public List<AlarmaVO> getListaExpedienteMailAlarma4( Connection con) throws Exception {
        log.info("Entramos en getListaExpedienteMailAlarma4 DAO - " );
        Statement st = null;
        ResultSet rs = null;
        List<AlarmaVO> listaAlarmas = new ArrayList<AlarmaVO>();
         AlarmaVO expedientes = new AlarmaVO();
        try {
            ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
            String codigVisibleRegisAnual = propmail.getString("alarma4.codigo.visible.tramite.notiRegisAnual");
            String codigVisibleRegisBi = propmail.getString("alarma4.codigo.visible.tramite.notiRegisBi");
            String codigVisibleRegisTri = propmail.getString("alarma4.codigo.visible.tramite.notiRegisTri");
            
            String query =" SELECT " +
                    "    exp_num" +
                    " FROM " +
                    "    e_exp" +
                    "    LEFT JOIN ( (" +
                    "        SELECT " +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "    ) ) tramite1000 ON exp_eje = tramite1000.cro_eje" +
                    "                       AND exp_pro = tramite1000.cro_pro" +
                    "                       AND exp_num = tramite1000.cro_num" + 
                    "                       AND tramite1000.tra_cou = '" +  codigVisibleRegisAnual +"'" +
                    "    LEFT JOIN ( (" +
                    "        SELECT " +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "    ) ) tramite2000 ON exp_eje = tramite2000.cro_eje" +
                    "                       AND exp_pro = tramite2000.cro_pro" +
                    "                       AND exp_num = tramite2000.cro_num" +
                    "                       AND tramite2000.tra_cou = '" +  codigVisibleRegisBi +"'" +
                    "    LEFT JOIN ( (" +
                    "        SELECT" +
                    "            e_cro.*," +
                    "            tra_cou" +
                    "        FROM " +
                    "                 e_cro" +
                    "            INNER JOIN e_tra ON cro_pro = tra_pro" +
                    "                                AND tra_cod = cro_tra" +
                    "    ) ) tramite3000 ON exp_eje = tramite3000.cro_eje" +
                    "                       AND exp_pro = tramite3000.cro_pro" +
                    "                       AND exp_num = tramite3000.cro_num" +
                    "                       AND tramite3000.tra_cou = '" +  codigVisibleRegisTri +"'" +
                    " WHERE " +
                    "        exp_pro = 'DECEX'" +
                    "    AND ( ( tramite1000.cro_fef IS NULL" +
                    "            AND tramite1000.cro_fei < ( sysdate - 15 ) )" +
                    "          OR ( tramite2000.cro_fef IS NULL" +
                    "               AND tramite2000.cro_fei < ( sysdate - 15 ) )" +
                    "          OR ( tramite3000.cro_fef IS NULL" +
                    "               AND tramite3000.cro_fei < ( sysdate - 15 ) ) )";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                
            expedientes.setNumeroExpediente(rs.getString("EXP_NUM"));
            //expedientes.setFechaAltaRegistro(rs.getString("fechacreacionregistroiniciaexp"));
            //expedientes.setNumeroEntradaRegistro(rs.getString("entradaregistro"));
            
            listaAlarmas.add(expedientes);
            expedientes = new AlarmaVO();
        
            }  
           
            } catch (Exception ex) {
                //ListaAlarmas.clear();
                log.error("Se ha producido un error recuperando lista alrma 4 " + ex);
                throw new Exception(ex);
            } finally {
             log.debug("Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            return listaAlarmas;
        }
}
    

