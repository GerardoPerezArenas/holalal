/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao;

import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01RolesCONCMEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Utilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class Melanbide01Dao {
    
    private Logger log = LogManager.getLogger(Melanbide01Dao.class);    
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    
    public ArrayList<TipoDocumentoVO> getTiposDocumento(Connection con){
        ArrayList<TipoDocumentoVO> tipos = new ArrayList<TipoDocumentoVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT TID_COD,TID_DES FROM T_TID ORDER BY TID_COD ASC";
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                TipoDocumentoVO tipo = new TipoDocumentoVO();
                tipo.setCodigo(rs.getString("TID_COD"));
                tipo.setDescripcion(rs.getString("TID_DES"));
                tipos.add(tipo);
            }
            st.close();
            rs.close();
        }catch(SQLException e){
            log.error("Error getTiposDocumento : " + e.getErrorCode() + " - " + e.getSQLState() + " - " +e.getMessage(), e);
        }finally{
            try{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Error getTiposDocumento : " + e.getErrorCode() + " - " + e.getSQLState() + " - " +e.getMessage(), e);
            }
        }
        return tipos;
    }
    
    public Map<String,String> getDatosDocumentoRolExpediente(String numeroExpediente,Connection con) throws SQLException, Exception{
        log.info(" getDatosDocumentoRolExpediente - Begin " + numeroExpediente + " " +  formatFechaLog.format(new Date()));
        Map<String,String> respuesta = new HashMap<String, String>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "SELECT EXT_ROL as rol, upper(HTE_DOC) as documento  "
                    + " FROM E_EXT "
                    + " LEFT JOIN T_HTE ON HTE_TER=EXT_TER AND HTE_NVR=EXT_NVR "
                    + " WHERE ext_num=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParam=1;
            ps.setString(contParam++, numeroExpediente);
            log.info("params = " + numeroExpediente);
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta.put(rs.getString("rol"),rs.getString("documento"));
            }
        } catch (SQLException e) {
            log.error("SQLException  getDatosDocumentoRolExpediente " + e.getErrorCode() + " - " + e.getSQLState() + " - " +e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception getDatosDocumentoRolExpediente ", e);
            throw e;
        } finally {
            try {
                if (ps != null) {ps.close();}
                if (rs != null) {rs.close();}
            } catch (SQLException e) {
                log.error("Error Cerrrando ResultSet : " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            }
        }
        log.debug("respuesta: " + respuesta.toString());
        log.info(" getDatosDocumentoRolExpediente - End " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        return respuesta;
    }
    
    public List<String> getExptsMismaEmpresaPerContratadaDifePerSust(String numeroExpediente, Map<String,String> datosDocuRolesExpte, Connection con) throws SQLException, Exception {
        log.info(" getExptsMismaEmpresaPerContratadaDifePerSust - Begin " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        List<String> respuesta = new ArrayList<String>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numeroExpediente);
            query = "SELECT expedientes_psustiuida.EXT_NUM  "
                    + " FROM  E_EXT expedientes_psustiuida "
                    + " INNER JOIN ( SELECT expedientes_pcontratada.EXT_NUM FROM  "
                    + " E_EXT expedientes_pcontratada "
                    + " INNER JOIN (SELECT exp_empresa.EXT_NUM FROM  "
                    + "                 E_EXT exp_empresa "
                    + "                 LEFT JOIN T_HTE emp ON emp.HTE_TER=exp_empresa.EXT_TER AND emp.HTE_NVR=exp_empresa.EXT_NVR "
                    + "                 LEFT JOIN E_ROL roldes ON roldes.ROL_PRO=exp_empresa.EXT_PRO AND roldes.ROL_COD=exp_empresa.EXT_ROL "
                    + "                 WHERE exp_empresa.EXT_PRO=? "
                    + "                 AND "
                    + "                 ((exp_empresa.EXT_ROL=? AND UPPER(emp.HTE_DOC)=UPPER(?))) " // Rol 1
                    + "              ) expedientes_empresa "
                    + "         on expedientes_empresa.ext_num=expedientes_pcontratada.ext_num "
                    + "     LEFT JOIN T_HTE pc ON pc.HTE_TER=expedientes_pcontratada.EXT_TER AND pc.HTE_NVR=expedientes_pcontratada.EXT_NVR "
                    + "     LEFT JOIN E_ROL roldes1 ON roldes1.ROL_PRO=expedientes_pcontratada.EXT_PRO AND roldes1.ROL_COD=expedientes_pcontratada.EXT_ROL "
                    + "     WHERE expedientes_pcontratada.EXT_PRO=? "
                    + "     AND((expedientes_pcontratada.EXT_ROL=? AND UPPER(pc.HTE_DOC)=UPPER(?))) " //Rol 4
                    + "     and expedientes_pcontratada.ext_num!=? "
                    + " ) datos_emp_pc on datos_emp_pc.ext_num=expedientes_psustiuida.ext_num "
                    + " INNER JOIN T_HTE ps ON ps.HTE_TER=expedientes_psustiuida.EXT_TER AND ps.HTE_NVR=expedientes_psustiuida.EXT_NVR "
                    + " LEFT JOIN E_ROL roldes2 ON roldes2.ROL_PRO=expedientes_psustiuida.EXT_PRO AND roldes2.ROL_COD=expedientes_psustiuida.EXT_ROL "
                    + " WHERE expedientes_psustiuida.EXT_PRO=? "
                    + " AND ((expedientes_psustiuida.EXT_ROL=? AND UPPER(ps.HTE_DOC)!= UPPER(?))) " // Rol 3
                    + " order by expedientes_psustiuida.ext_num ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParam = 1;
            ps.setString(contParam++, (codProcedimiento!=null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++, Integer.valueOf(MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo()));
            ps.setString(contParam++, datosDocuRolesExpte.get(MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo()));
            ps.setString(contParam++, (codProcedimiento!=null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++, Integer.valueOf(MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.getCodigo()));
            ps.setString(contParam++, datosDocuRolesExpte.get(MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.getCodigo()));
            ps.setString(contParam++, numeroExpediente);
            ps.setString(contParam++, (codProcedimiento!=null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++, Integer.valueOf(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()));
            ps.setString(contParam++, datosDocuRolesExpte.get(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()));
            log.info("params = " + contParam
                    + " "+(codProcedimiento!=null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + ","+ MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo()
                    + ","+ datosDocuRolesExpte.get(MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo())
                    + ","+(codProcedimiento!=null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + ","+ MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.getCodigo()
                    + ","+ datosDocuRolesExpte.get(MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.getCodigo())
                    + ","+ numeroExpediente
                    + ","+(codProcedimiento!=null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + ","+ MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()
                    + ","+ datosDocuRolesExpte.get(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo())
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta.add(rs.getString("EXT_NUM"));
            }
        } catch (SQLException e) {
            log.error("SQLException  getExptsMismaEmpresaPerContratadaDifePerSust " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getExptsMismaEmpresaPerContratadaDifePerSust " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception getExptsMismaEmpresaPerContratadaDifePerSust ", e);
            throw e;
        } finally {
            try {
                if (ps != null) {ps.close();}
                if (rs != null) {rs.close();}
            } catch (SQLException e) {
                log.error("Error Cerrrando ResultSet : " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            }
        }
        log.debug("respuesta: " + Arrays.toString(respuesta.toArray()));
        log.info(" getExptsMismaEmpresaPerContratadaDifePerSust - End " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        return respuesta;
    }
    
    /**
     * Obtiene los expedientes relacionados asociados a la misma persona sustituida (Rol 3)
     * @param numeroExpediente
     * @param con
     * @return Devuelve un mapa con la lista de expediente mas los datos sobre las personas que casuan la subvencion valores en el map:
     * numExpediente datosCSCausante(Datos causates desde Campo suplementari) listMECausantes(Lista separada por ; del modulo extension) decretoCodigo(decreo que aplica al expediente) mismoDecretoExpteConsultado
     * @throws SQLException
     * @throws Exception 
     */
    public List<Map<String,String>> getExpedientesRelacionados(String numeroExpediente, Connection con) throws SQLException, Exception {
        log.info(" getExpedientesRelacionados - Begin " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        List<Map<String,String>> respuesta = new ArrayList<Map<String,String>>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numeroExpediente);
            query = "SELECT DISTINCT exp_misma_ps.EXT_NUM numExpediente,exp_misma_ps.tfe_valor datosCSCausante,exp_rela_listaFecNac listMECausantes "
                    + ", DECRETO.decretoCodigo "
                    + ", case WHEN exp_base.EXT_NUM=exp_misma_ps.EXT_NUM THEN 1 ELSE 0 END AS expteBaseConsultado "
                    + ", case WHEN exp_base_DECRETO.decretoCodigo=DECRETO.decretoCodigo THEN 1 ELSE 0 END AS mismoDecretoExpteConsultado "
                    + " FROM E_EXT exp_base "
                    + " inner JOIN T_HTE exp_base_ps ON exp_base_ps.HTE_TER=exp_base.EXT_TER AND exp_base_ps.HTE_NVR=exp_base.EXT_NVR "
                    + " INNER JOIN (SELECT exp_rela.*,exp_rela_ps.hte_doc,to_char(fecnapdepen.tfe_valor,'dd/MM/yyyy') tfe_valor "
                    + "            ,CASE WHEN exp_misma_ps_FECINIPERIODO.periodos_fecha_inicio IS NOT NULL THEN exp_misma_ps_FECINIPERIODO.periodos_fecha_inicio ELSE FECINIACCON.TFE_VALOR END AS  periodos_fecha_inicio "
                    + "            ,melanbide01_depen_persut.CORRELATIVO, melanbide01_depen_persut.TIPODEPENDIENTE, melanbide01_depen_persut.NUMERODOCUMENTO, melanbide01_depen_persut.FECHANACIMIENTO "
                    + "            ,LISTAGG(to_char(melanbide01_depen_persut.FECHANACIMIENTO,'dd/MM/yyyy'), ';') WITHIN GROUP (ORDER BY melanbide01_depen_persut.numeroexpediente,melanbide01_depen_persut.FECHANACIMIENTO) "
                    + "                OVER (PARTITION BY melanbide01_depen_persut.numeroexpediente) as exp_rela_listaFecNac "
                    + "            FROM E_EXT exp_rela  "
                    + "            inner JOIN T_HTE exp_rela_ps ON exp_rela_ps.HTE_TER=exp_rela.EXT_TER AND exp_rela_ps.HTE_NVR=exp_rela.EXT_NVR "
                    + "            left join (SELECT melanbide01_datos_calculo.num_expediente, MIN(melanbide01_periodo.fecha_inicio) periodos_fecha_inicio FROM melanbide01_datos_calculo  "
                    + "                        INNER JOIN MELANBIDE01_PERIODO ON melanbide01_datos_calculo.id=melanbide01_periodo.id_datos_calculo group by melanbide01_datos_calculo.num_expediente "
                    + "                 ) exp_misma_ps_FECINIPERIODO  "
                    + "            ON  exp_misma_ps_FECINIPERIODO.num_expediente=exp_rela.EXT_NUM  "
                    + "            left join E_TFE FECNAPDEPEN ON  FECNAPDEPEN.TFE_NUM=exp_rela.EXT_NUM AND FECNAPDEPEN.TFE_COD='FECNAPDEPEN'  "
                    + "            LEFT JOIN E_TFE FECINIACCON ON FECINIACCON.TFE_NUM=exp_rela.EXT_NUM AND FECINIACCON.TFE_COD='FECINIACCON'  "
                    + "            LEFT JOIN melanbide01_depen_persut ON melanbide01_depen_persut.numeroexpediente=exp_rela.EXT_NUM "
                    + "            WHERE exp_rela.EXT_PRO=? AND exp_rela.EXT_ROL=? "
                    + "        ) exp_misma_ps "
                    + "    on UPPER(exp_base_ps.hte_doc)=UPPER(exp_misma_ps.hte_doc) "
                    + " left join (SELECT melanbide01_datos_calculo.num_expediente, MIN(melanbide01_periodo.fecha_inicio) periodos_fecha_inicio FROM melanbide01_datos_calculo  "
                    + "            INNER JOIN MELANBIDE01_PERIODO ON melanbide01_datos_calculo.id=melanbide01_periodo.id_datos_calculo group by melanbide01_datos_calculo.num_expediente "
                    + "           ) exp_base_FECINIPERIODO "
                    + "    ON  exp_base_FECINIPERIODO.num_expediente=exp_base.EXT_NUM "
                    + " LEFT JOIN E_TFE exp_base_FECINIACCON ON exp_base_FECINIACCON.TFE_NUM=exp_base.EXT_NUM AND exp_base_FECINIACCON.TFE_COD='FECINIACCON'  "
                    + " LEFT JOIN MELANBIDE01_DECRETO DECRETO ON DECRETO.decretoFecEntradaVigor <= exp_misma_ps.periodos_fecha_inicio and (nvl(DECRETO.decretoFecFinAplicacion,sysdate+1)) > exp_misma_ps.periodos_fecha_inicio  "
                    + " LEFT JOIN MELANBIDE01_DECRETO exp_base_DECRETO ON  "
                    + "        exp_base_DECRETO.decretoFecEntradaVigor <= (CASE WHEN exp_base_FECINIPERIODO.periodos_fecha_inicio IS NOT NULL THEN exp_base_FECINIPERIODO.periodos_fecha_inicio ELSE exp_base_FECINIACCON.TFE_VALOR END) "
                    + "        and (nvl(exp_base_DECRETO.decretoFecFinAplicacion,sysdate+1)) > (CASE WHEN exp_base_FECINIPERIODO.periodos_fecha_inicio IS NOT NULL THEN exp_base_FECINIPERIODO.periodos_fecha_inicio ELSE exp_base_FECINIACCON.TFE_VALOR END)  "
                    + " WHERE exp_base.EXT_PRO=? "
                    + " AND exp_base.EXT_ROL=?  "
                    + " AND exp_base.EXT_NUM=? "
                    + " order by exp_misma_ps.EXT_NUM desc,listMECausantes asc";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParam = 1;
            ps.setString(contParam++, (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++, Integer.valueOf(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()));
            ps.setString(contParam++, (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++, Integer.valueOf(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()));
            ps.setString(contParam++, numeroExpediente);
            log.info("params = " + contParam
                    + " " + (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + "," + MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()
                    + "," + (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + "," + MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo()
                    + "," + numeroExpediente
            );
            rs = ps.executeQuery();
            while (rs.next()) { 
                // Ignoramos el expediente consultado:
                if(!"1".equalsIgnoreCase(rs.getString("expteBaseConsultado"))){
                    Map<String, String> respuestaMap = new HashMap<String, String>();
                    respuestaMap.put("numExpediente", rs.getString("numExpediente"));
                    respuestaMap.put("datosCSCausante", rs.getString("datosCSCausante"));
                    respuestaMap.put("listMECausantes", rs.getString("listMECausantes"));
                    respuestaMap.put("decretoCodigo", rs.getString("decretoCodigo"));
                    respuestaMap.put("expteBaseConsultado", rs.getString("expteBaseConsultado"));
                    respuestaMap.put("mismoDecretoExpteConsultado", rs.getString("mismoDecretoExpteConsultado"));
                    respuesta.add(respuestaMap);
                }
            }
        } catch (SQLException e) {
            log.error("SQLException  getExpedientesRelacionados " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getExpedientesRelacionados " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception getExpedientesRelacionados ", e);
            throw e;
        } finally {
            try {
                if (ps != null) {ps.close();}
                if (rs != null) {rs.close();}
            } catch (SQLException e) {
                log.error("Error Cerrrando ResultSet : " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            }
        }
        log.debug("respuesta: " + Arrays.toString(respuesta.toArray()));
        log.info(" getExpedientesRelacionados - End " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        return respuesta;
    }

    public Map<String, Integer> getDatosCalculoNumMaxDias_Dec164_2019(Integer codigoOrganizacion, String codProcedimiento, String numeroExpediente, String codigoRolPSustituida, String codigoRolPContratada, Connection con) throws SQLException, Exception {
        log.info("  getDatosCalculoNumMaxDias_Dec164_2019 - Begin " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        Map<String, Integer> respuesta = new HashMap<String, Integer>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select distinct EXT_NUM  "
                    + " ,max(CASE WHEN exp_rela.ext_rol=3 then terceroSexo.valor else '0' end) as sexoRol_3 "
                    + " ,max(CASE WHEN exp_rela.ext_rol=4 then terceroSexo.valor else '0' end) as sexoRol_4 "
                    + " ,RELACPERSDEPEN.tde_valor motivoSubvencion"
                    + " , totalHijosCausantes.totalHijosCausantesSubv totalHijosCausantesSubv "
                    + " ,totalHijosCausantes.factorBaseMaximoDias factorBaseMaximoDias,totalHijosCausantes.factorPlus factorPlus "
                    + " FROM E_EXT exp_rela  "
                    + " inner JOIN T_HTE exp_rela_ps ON exp_rela_ps.HTE_TER=exp_rela.EXT_TER AND exp_rela_ps.HTE_NVR=exp_rela.EXT_NVR "
                    + " inner JOIN E_ROL roldes ON roldes.ROL_PRO=exp_rela.EXT_PRO AND roldes.ROL_COD=exp_rela.EXT_ROL "
                    + " left join t_campos_desplegable terceroSexo on terceroSexo.COD_MUNICIPIO=exp_rela.ext_mun and  terceroSexo.COD_CAMPO='TSEXOTERCERO'and terceroSexo.COD_TERCERO=exp_rela.ext_ter "
                    + " LEFT JOIN E_DES_VAL terceroSexoDesc ON terceroSexoDesc.DES_COD='SEXO' AND terceroSexoDesc.DES_VAL_COD=terceroSexo.VALOR "
                    + " LEFT JOIN E_TDE RELACPERSDEPEN ON relacpersdepen.tde_mun=exp_rela.ext_mun and RELACPERSDEPEN.tde_eje=exp_rela.ext_eje and RELACPERSDEPEN.tde_num=exp_rela.ext_num and RELACPERSDEPEN.tde_cod='RELACPERSDEPEN' "
                    + " LEFT JOIN ("
                    //+ " select numeroexpediente,count(id) totalHijosCausantesSubv from melanbide01_depen_persut where tipodependiente = 0 group by numeroexpediente"
                    + " select numeroexpediente,sum(factorBaseMaximoDias)  factorBaseMaximoDias " 
                    + "        ,sum(case when totalhijoscausantessubv>1 then totalhijoscausantessubv-1 else 0 end) factorPlus " 
                    + "        ,sum(totalhijoscausantessubv) totalhijoscausantessubv " 
                    + "    from ( SELECT bb.numeroexpediente,COUNT(bb.id) totalhijoscausantessubv " 
                    + "            ,ROW_NUMBER() OVER (PARTITION BY bb.numeroexpediente,bb.fechanacimiento ORDER BY bb.numeroexpediente,bb.fechanacimiento) factorBaseMaximoDias " 
                    + "        FROM melanbide01_depen_persut bb " 
                    + "        WHERE bb.tipodependiente = 0 " 
                    + "            group by bb.numeroexpediente,bb.fechanacimiento " 
                    + "            order  by bb.numeroexpediente,bb.fechanacimiento " 
                    + "    ) GROUP BY numeroexpediente "
                    + " ) totalHijosCausantes on totalHijosCausantes.numeroExpediente=exp_rela.ext_num "
                    + " where exp_rela.EXT_PRO= ? "
                    + " AND (exp_rela.EXT_ROL=? or exp_rela.EXT_ROL=?) "
                    + " and exp_rela.EXT_NUM= ? "
                    + " group by EXT_NUM, RELACPERSDEPEN.tde_valor, totalHijosCausantes.totalHijosCausantesSubv "
                    + " ,totalHijosCausantes.factorBaseMaximoDias,totalHijosCausantes.factorPlus ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParam = 1;
            ps.setString(contParam++, (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++,(codigoRolPSustituida!=null && !codigoRolPSustituida.isEmpty() ? Integer.valueOf(codigoRolPSustituida):0));
            ps.setInt(contParam++,(codigoRolPContratada!=null && !codigoRolPContratada.isEmpty() ? Integer.valueOf(codigoRolPContratada):0));
            ps.setString(contParam++, numeroExpediente);
            log.info("params = " + contParam
                    + " " + (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + "," + (codigoRolPSustituida != null && !codigoRolPSustituida.isEmpty() ? codigoRolPSustituida : 0)
                    + "," + (codigoRolPContratada != null && !codigoRolPContratada.isEmpty() ? codigoRolPContratada : 0)
                    + "," + numeroExpediente
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta.put(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.toString(), rs.getInt("sexoRol_3"));
                respuesta.put(MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.toString(), rs.getInt("sexoRol_4"));
                respuesta.put("motivoSubvencion", rs.getInt("motivoSubvencion"));
                respuesta.put("totalHijosCausantesSubv", rs.getInt("totalHijosCausantesSubv"));
                respuesta.put("factorBaseMaximoDias", rs.getInt("factorBaseMaximoDias"));
                respuesta.put("factorPlus", rs.getInt("factorPlus"));
            }
        } catch (SQLException e) {
            log.error("SQLException   getDatosCalculoNumMaxDias_Dec164_2019 " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException   getDatosCalculoNumMaxDias_Dec164_2019 " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception  getDatosCalculoNumMaxDias_Dec164_2019 ", e);
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Error Cerrrando ResultSet : " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            }
        }
        log.info("  getDatosCalculoNumMaxDias_Dec164_2019 - End " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        return respuesta;
    }
    
    public double getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios(Integer codigoOrganizacion, String codProcedimiento, String numeroExpediente,Connection con) throws SQLException, Exception {
        log.info("  getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios - Begin " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        double respuesta = 0.0;
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "SELECT expediente_base.ext_num, expedientes_empresa.cifEmpresa, SUM(NVL(SUBVTOTAL.TNU_VALOR,0))IMPTOT_SUBV_RECI_EMP_ULT3ANIOS "
                    + " FROM E_EXT expediente_base "
                    + " LEFT JOIN T_HTE pc ON pc.HTE_TER=expediente_base.EXT_TER AND pc.HTE_NVR=expediente_base.EXT_NVR "
                    + " LEFT JOIN E_ROL roldes1 ON roldes1.ROL_PRO=expediente_base.EXT_PRO AND roldes1.ROL_COD=expediente_base.EXT_ROL "
                    + " INNER JOIN (SELECT exp_empresa.EXT_EJE,exp_empresa.EXT_NUM,UPPER(emp.HTE_DOC) cifEmpresa,FECHARESO_Expts.tfet_valor fechaConcesion FROM  "
                    + "            E_EXT exp_empresa "
                    + "            LEFT JOIN T_HTE emp ON emp.HTE_TER=exp_empresa.EXT_TER AND emp.HTE_NVR=exp_empresa.EXT_NVR "
                    + "            LEFT JOIN E_ROL roldes ON roldes.ROL_PRO=exp_empresa.EXT_PRO AND roldes.ROL_COD=exp_empresa.EXT_ROL "
                    + "            LEFT JOIN E_TFET FECHARESO_Expts ON FECHARESO_Expts.TFET_COD='FECHARESO' AND  FECHARESO_Expts.TFET_EJE=exp_empresa.ext_EJE AND FECHARESO_Expts.TFET_NUM=exp_empresa.ext_num AND FECHARESO_Expts.TFET_TRA=3 "
                    + "            WHERE exp_empresa.EXT_PRO=? "
                    + "            AND exp_empresa.EXT_ROL=? "
                    + "         ) expedientes_empresa "
                    + "        on expedientes_empresa.cifEmpresa=pc.hte_doc "
                    + " LEFT JOIN E_TNU SUBVTOTAL ON SUBVTOTAL.TNU_COD='SUBVTOTAL' AND  SUBVTOTAL.TNU_EJE=expedientes_empresa.ext_EJE AND SUBVTOTAL.TNU_NUM=expedientes_empresa.ext_num "
                    + " LEFT JOIN E_TFET FECHARESO ON FECHARESO.TFET_COD='FECHARESO' AND  FECHARESO.TFET_EJE=expediente_base.ext_EJE AND FECHARESO.TFET_NUM=expediente_base.ext_num AND FECHARESO.TFET_TRA=3 "
                    + " WHERE expediente_base.EXT_PRO=? "
                    + " AND expediente_base.EXT_ROL=? "
                    + " and expediente_base.ext_num=? "
                    + "  AND expediente_base.ext_num >= expedientes_empresa.EXT_NUM "
                    + " and ( " 
                    + " (FECHARESO.tfet_valor is null and  expedientes_empresa.EXT_EJE >=(expediente_base.ext_eje-2)) " 
                    + " or (FECHARESO.tfet_valor is not null and expedientes_empresa.EXT_EJE >= (to_number(to_char(FECHARESO.tfet_valor,'yyyy'))-2) AND to_date(to_char(expedientes_empresa.fechaConcesion,'dd/MM/yyyy')) <= to_date(to_char(FECHARESO.tfet_valor,'dd/MM/yyyy'))) " 
                    + " ) "
                    + " group by expediente_base.ext_num,expedientes_empresa.cifEmpresa ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParam = 1;
            ps.setString(contParam++, (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++,(Integer.valueOf(MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo())));
            ps.setString(contParam++, (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : ""));
            ps.setInt(contParam++,(Integer.valueOf(MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo())));
            ps.setString(contParam++, numeroExpediente);
            log.info("params = " + contParam
                    + " " + (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + "," + (MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo())
                    + " " + (codProcedimiento != null && !codProcedimiento.isEmpty() ? codProcedimiento : "")
                    + "," + (MeLanbide01RolesCONCMEnum.EMPRESA_SOLICITANTE.getCodigo())
                    + "," + numeroExpediente
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                respuesta=rs.getDouble("IMPTOT_SUBV_RECI_EMP_ULT3ANIOS");
            }
        } catch (SQLException e) {
            log.error("SQLException   getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException   getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception  getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios ", e);
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Error Cerrrando ResultSet : " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            }
        }
        log.info("  getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios - End " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        return respuesta;
    }
    
    public List<Map<String, String>> getDatosCargarValidarEdadPersonasContraYSusti(String numeroExpediente,Connection con) throws SQLException, Exception {
        log.info("  getDatosCargarValidarEdadPersonasContraYSusti - Begin " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        List<Map<String, String>> respuesta = new ArrayList<Map<String, String>>();
        String query = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            query = "select dato.* " 
                    + ", case  "
                    + " WHEN dato.fechaNacimiento is null then 'NONE' "
                    + " when dato.VALCS_PERCONTEDAD_PERSUSTEDAD is null then 'INSERT'  " 
                    + " WHEN dato.VALCS_PERCONTEDAD_PERSUSTEDAD is NOT null and dato.edadCalculada != dato.VALCS_PERCONTEDAD_PERSUSTEDAD then 'UPDATE' " 
                    + " WHEN dato.VALCS_PERCONTEDAD_PERSUSTEDAD is NOT null and dato.edadCalculada = dato.VALCS_PERCONTEDAD_PERSUSTEDAD then 'NONE' " 
                    + " END tipoOperacion " 
                    + " from ( " 
                    + " SELECT " 
                    + " ext_num numeroExpediente,ext_mun codigoOrganizacion, ext_rol codigoRol, to_char(fechanacimientoter.valor,'dd/MM/yyyy') fechaNacimiento,  trunc((MONTHS_BETWEEN(to_date(to_char(SYSDATE,'dd/MM/yyyy'),'dd/MM/yyyy'),fechanacimientoter.valor)/12)) edadCalculada "
                    + " , case  " 
                    + "    when ext_rol=4 then PERCONTEDAD.tnu_valor  " 
                    + "    when ext_rol=3 then persustedad.tnu_valor " 
                    + " else null end VALCS_PERCONTEDAD_PERSUSTEDAD " 
                    + ", case  " 
                    + "    when ext_rol=4 then 'PERCONTEDAD' " 
                    + "    when ext_rol=3 then 'PERSUSTEDAD'  " 
                    + " else null end campoSuplementario " 
                    + " FROM E_EXT " 
                    + " left join t_campos_fecha fechaNacimientoTer on fechanacimientoter.cod_campo='TFECNACIMIENTO' and fechanacimientoter.cod_tercero=ext_ter " 
                    + " left join e_tnu PERCONTEDAD on percontedad.tnu_mun=ext_mun and percontedad.tnu_eje=ext_eje and percontedad.tnu_num=ext_num and percontedad.tnu_cod='PERCONTEDAD' " 
                    + " left join e_tnu PERSUSTEDAD on PERSUSTEDAD.tnu_mun=ext_mun and PERSUSTEDAD.tnu_eje=ext_eje and PERSUSTEDAD.tnu_num=ext_num and PERSUSTEDAD.tnu_cod='PERSUSTEDAD' " 
                    + " WHERE  " 
                    + " ext_num=? and ext_rol in(3,4) " 
                    + " ) dato";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int contParam = 1;
            ps.setString(contParam++, numeroExpediente);
            log.info("params = " + contParam
                    + "," + numeroExpediente
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                // CODIGOORGANIZACION,CODIGOROL, FECHANACIMIENTO, EDADCALCULADA, VALCS_PERCONTEDAD_PERSUSTEDAD, CAMPOSUPLEMENTARIO, TIPOOPERACION
                Map<String, String> respuestaConsulta = new HashMap<String, String>();
                respuestaConsulta.put("NUMEROEXPEDIENTE", rs.getString("NUMEROEXPEDIENTE"));
                respuestaConsulta.put("CODIGOORGANIZACION", rs.getString("CODIGOORGANIZACION"));
                respuestaConsulta.put("CODIGOROL", rs.getString("CODIGOROL"));
                respuestaConsulta.put("FECHANACIMIENTO", rs.getString("FECHANACIMIENTO"));
                respuestaConsulta.put("EDADCALCULADA", rs.getString("EDADCALCULADA"));
                respuestaConsulta.put("VALCS_PERCONTEDAD_PERSUSTEDAD", rs.getString("VALCS_PERCONTEDAD_PERSUSTEDAD"));
                respuestaConsulta.put("CAMPOSUPLEMENTARIO", rs.getString("CAMPOSUPLEMENTARIO"));
                respuestaConsulta.put("TIPOOPERACION", rs.getString("TIPOOPERACION"));
                respuesta.add(respuestaConsulta);
            }
        } catch (SQLException e) {
            log.error("SQLException   getDatosCargarValidarEdadPersonasContraYSusti " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException   getDatosCargarValidarEdadPersonasContraYSusti " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Exception  getDatosCargarValidarEdadPersonasContraYSusti ", e);
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Error Cerrrando ResultSet : " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            }
        }
        log.info("  getDatosCargarValidarEdadPersonasContraYSusti - End " + numeroExpediente + " " + formatFechaLog.format(new Date()));
        return respuesta;
    }
}
