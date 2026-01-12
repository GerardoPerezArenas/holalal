/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44InformeUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeDesglose;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaResumenInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaValorListaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.DatosAnexosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaProspectorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResDetalleInsercionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResProspectoresVO;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide44DAO 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide44DAO.class);
    
    //Instancia
    private static MeLanbide44DAO instance = null;
    
    private MeLanbide44DAO()
    {
        
    }
    
    public static MeLanbide44DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide44DAO.class)
            {
                instance = new MeLanbide44DAO();
            }
        }
        return instance;
    }       
    
    public EcaSolicitudVO getDatosSolicitud(String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSolicitudVO retSol = null;
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    + " where ECA_NUMEXP = '" + numExpediente +"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retSol = (EcaSolicitudVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolicitudVO.class);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
        return retSol;
    }  
    
    public DatosAnexosVO getDatosSolicitudAnexos(EcaSolicitudVO solAsociada, Connection con) throws Exception
    {
        if(solAsociada != null && solAsociada.getSolicitudCod() != null)
        {
            EcaConfiguracionVO conf = null;
            String anoExp = null;
            try
            {
                if(solAsociada.getNumExp() != null)
                {
                    String[] datos = solAsociada.getNumExp().split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                    conf = this.getConfiguracionEca(Integer.parseInt(datos[0]), con);
                    anoExp = datos[0];
                }
            }
            catch(Exception ex)
            {
                
            }
            Statement st = null;
            ResultSet rs = null;
            DatosAnexosVO datos = null;
            try
            {
                String query = null;
                
                query = "select tmp1.*, tmp2.*, tmp3.*,  tmp4.*, "
                        + " (tmp3.importe_preparadores + tmp4.importe_prospectores) * "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+" as GASTOS,"
                        + " case"
                        + "     when (nvl(tmp3.importe_preparadores, 0) + nvl(tmp4.importe_prospectores, 0)) + (nvl(tmp3.importe_preparadores, 0) + nvl(tmp4.importe_prospectores, 0)) * "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+" - nvl(tmp6.otras_ayudas, 0) < 0 then"
                        + "         0"
                        + "     else"
                        + "         (nvl(tmp3.importe_preparadores, 0) + nvl(tmp4.importe_prospectores, 0)) + (nvl(tmp3.importe_preparadores, 0) + nvl(tmp4.importe_prospectores, 0)) * "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+" - nvl(tmp6.otras_ayudas, 0)"
                        + " end as max_subvencionable"
                      + " from "
                      + "  ("
                      + "       select"
                      + "       sum(a.ECA_PREP_INS_C1_H) C1_H, sum(a.ECA_PREP_INS_C1_M) C1_M, sum(a.ECA_PREP_INS_C1) C1_T,"
                      + "       sum(a.ECA_PREP_INS_C2_H) C2_H, sum(a.ECA_PREP_INS_C2_M) C2_M, sum(a.ECA_PREP_INS_C2) C2_T,"
                      + "       sum(a.ECA_PREP_INS_C3_H) C3_H, sum(a.ECA_PREP_INS_C3_M) C3_M, sum(a.ECA_PREP_INS_C3) C3_T,"
                      + "       sum(a.ECA_PREP_INS_C4_H) C4_H, sum(a.ECA_PREP_INS_C4_M) C4_M, sum(a.ECA_PREP_INS_C4) C4_T,"
                       + "       sum(a.ECA_PREP_INS_C5_H) C5_H, sum(a.ECA_PREP_INS_C5_M) C5_M, sum(a.ECA_PREP_INS_C5) C5_T,"
                        + "       sum(a.ECA_PREP_INS_C6_H) C6_H, sum(a.ECA_PREP_INS_C6_M) C6_M, sum(a.ECA_PREP_INS_C6) C6_T"
                      + "       from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                      + "       a where a.eca_prep_solicitud = "+solAsociada.getSolicitudCod()
                      + "  )tmp1,"
                      + "  ("
                      + "       SELECT"
                      + "       sum(nvl(b.ECA_PREP_SEG_ANT,0)) seguimientos_ant"
                      + "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "       INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      +"        ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      + "       where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "  )tmp2,"
                      + "  ("
                      /*+ "       SELECT COUNT(*) num_preparadores,sum(nvl(eca_prep_imp_ss_eca,0)) importe_preparadores"
                      + "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "       INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "       ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      + "       where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()*/
                        
                        
                        +"  select count(*) num_preparadores, sum(importe) importe_preparadores from ("
                      +"        select"
                      +"        coalesce(b.ECA_PREP_CODORIGEN,b.ECA_SOL_PREPARADORES_COD),"
                      +"        sum(case when"
                      +"        nvl(b.eca_prep_ins_c1_h,0)*IM_C1_H +"
                      +"        nvl(b.eca_prep_ins_c1_m,0)*IM_C1_m +"
                      +"        nvl(b.eca_prep_ins_c2_h,0)*IM_C2_H +"
                      +"        nvl(b.eca_prep_ins_c2_m,0)*IM_C2_m +"
                      +"        nvl(b.eca_prep_ins_c3_h,0)*IM_C3_H +"
                      +"        nvl(b.eca_prep_ins_c3_m,0)*IM_C3_m +"
                      +"        nvl(b.eca_prep_ins_c4_h,0)*IM_C4_H +"
                      +"        nvl(b.eca_prep_ins_c4_m,0)*IM_C4_m +"
                      +"        nvl(b.eca_prep_ins_c5_h,0)*IM_C5_H +"
                      +"        nvl(b.eca_prep_ins_c5_m,0)*IM_C5_m +"
                      +"        nvl(b.eca_prep_ins_c6_h,0)*IM_C6_H +"
                      +"        nvl(b.eca_prep_ins_c6_m,0)*IM_C6_m +"
                      +"        case when PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA<b.ECA_PREP_IMP_SEG_ANT"
                      +"        then PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA"
                      +"        else b.ECA_PREP_IMP_SEG_ANT end > b.ECA_PREP_IMP_SS_ECA then b.eca_prep_imp_ss_eca else"
                      +"        nvl(b.eca_prep_ins_c1_h,0)*IM_C1_H +"
                      +"        nvl(b.eca_prep_ins_c1_m,0)*IM_C1_m +"
                      +"        nvl(b.eca_prep_ins_c2_h,0)*IM_C2_H +"
                      +"        nvl(b.eca_prep_ins_c2_m,0)*IM_C2_m +"
                      +"        nvl(b.eca_prep_ins_c3_h,0)*IM_C3_H +"
                      +"        nvl(b.eca_prep_ins_c3_m,0)*IM_C3_m +"
                      +"        nvl(b.eca_prep_ins_c4_h,0)*IM_C4_H +"
                      +"        nvl(b.eca_prep_ins_c4_m,0)*IM_C4_m +"
                      +"        nvl(b.eca_prep_ins_c5_h,0)*IM_C5_H +"
                      +"        nvl(b.eca_prep_ins_c5_m,0)*IM_C5_m +"
                      +"        nvl(b.eca_prep_ins_c6_h,0)*IM_C6_H +"
                      +"        nvl(b.eca_prep_ins_c6_m,0)*IM_C6_m +"
                      +"        case when PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA<b.ECA_PREP_IMP_SEG_ANT"
                      +"        then PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA"
                      +"        else b.ECA_PREP_IMP_SEG_ANT end end) importe"
                      +"        FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      +"        INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      +"        ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      +"        inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CONFIGURACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" c on 1=1 and c.ano='"+anoExp+"'"
                    //#275825 - Si dos personas sustituyen al titular, duplica el coste del titular, en detalle anexos, total subvenci�n solicitada.
                        // +"         left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" c on c.ECA_PREP_CODORIGEN=b.ECA_SOL_PREPARADORES_COD"
                      +"          where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      +"        group by coalesce(b.ECA_PREP_CODORIGEN,b.ECA_SOL_PREPARADORES_COD)"
                      +"    )"
                        
                        
                      + "  )tmp3,"
                      + "  ("
                      /*+ "       SELECT COUNT(*) num_prospectores, sum(nvl(eca_pros_imp_ss_eca,0)) importe_prospectores"
                      + "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "       INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "       ON A.ECA_SOLICITUD_COD=B.ECA_PROS_SOLICITUD"
                      + "       where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "       and B.ECA_PROS_CODORIGEN is null AND ECA_PROS_NIF_CARGA IS NULL "*/
                        
                      + "       select"
                      + "       count(*)  num_prospectores, sum(case when importe > 39100 then 39100 else importe end) importe_prospectores"
                      + "       from ("
                      + "               select"
                      + "               coalesce (b.eca_pros_codorigen,b.eca_sol_prospectores_cod)agrupar,"
                      + "               SUM(NVL(b.ECA_PROS_COSTE,0)) importe"
                      + "               FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "               INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B  ON A.ECA_SOLICITUD_COD    =B.ECA_PROS_SOLICITUD"
                    //#275825 - Si dos personas sustituyen al titular, duplica el coste del titular, en detalle anexos, total subvenci�n solicitada.                     
                    // + "               left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" c on b.eca_sol_prospectores_cod=c.eca_pros_codorigen"
                      + "               where"
                      + "               A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "               group by coalesce (b.eca_pros_codorigen,b.eca_sol_prospectores_cod)"
                      + "           )"
                        
                      + "  )tmp4,"
                      /*+ "  ("
                      + "       select sum(nvl(importe,0)) * "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+" as GASTOS"
                      + "       from "
                      + "       ("
                      + "           SELECT sum(nvl(eca_prep_imp_ss_eca,0)) importe"
                      + "           FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "           INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "           ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      + "           where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "           UNION"
                      + "           SELECT sum(nvl(eca_pros_imp_ss_eca,0)) importe"
                      + "           FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "           INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "           ON A.ECA_SOLICITUD_COD=B.ECA_PROS_SOLICITUD"
                      + "           where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "       )"
                      + "  )tmp5,"*/
                      + "  ("
                      /*+ "       SELECT imp-NVL(ECA_SUB_PUB,0)-NVL(ECA_SUB_PRIV,0)max_subvencionable "
                        + "     FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" AA"
                      + "       INNER JOIN"
                      + "       ("
                      + "           select eca_numexp, sum(nvl(importe,0)) * (1 + "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+")imp"
                      + "           FROM"
                      + "           ("
                      + "               SELECT A.ECA_NUMEXP, COUNT(*) personas ,sum(nvl(eca_prep_imp_ss_eca,0)) importe"
                      + "               FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "               INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "               ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      + "               GROUP BY A.ECA_NUMEXP"
                      + "               UNION"
                      + "               SELECT A.ECA_NUMEXP, COUNT(*) personas, sum(nvl(eca_pros_imp_ss_eca,0)) importe"
                      + "               FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "               INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "               ON A.ECA_SOLICITUD_COD=B.ECA_PROS_SOLICITUD"
                      + "               GROUP BY A.ECA_NUMEXP"
                      + "          )"
                      + "           GROUP BY ECA_NUMEXP"
                      + "       ) BB"
                      + "       ON AA.eca_numexp=BB.eca_numexp"
                      + "       where AA.eca_solicitud_cod = "+solAsociada.getSolicitudCod()*/
                        
                        
                      + "       SELECT NVL(ECA_SUB_PUB,0)+NVL(ECA_SUB_PRIV,0) otras_ayudas"
                        + "     FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" AA"
                      
                      + "       where AA.eca_solicitud_cod = "+solAsociada.getSolicitudCod()
                      + "  )tmp6";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    datos = (DatosAnexosVO)MeLanbide44MappingUtils.getInstance().map(rs, DatosAnexosVO.class);
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando las áreas", ex);
                throw new Exception(ex);
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
            return datos;
        }
        else
        {
            return null;
        }
    }
    
    //Datos de carga masiva mediante Excel
     public DatosAnexosVO getDatosSolicitudCarga(EcaSolicitudVO solAsociada, Connection con) throws Exception
    {
        if(solAsociada != null && solAsociada.getSolicitudCod() != null)
        {
            EcaConfiguracionVO conf = null;
            String anoExp = null;
            try
            {
                if(solAsociada.getNumExp() != null)
                {
                    String[] datos = solAsociada.getNumExp().split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                    conf = this.getConfiguracionEca(Integer.parseInt(datos[0]), con);
                    anoExp = datos[0];
                }
            }
            catch(Exception ex)
            {
                
            }
            Statement st = null;
            ResultSet rs = null;
            DatosAnexosVO datos = null;
            try
            {
                String query = null;
                
                query = "select tmp1.*, tmp2.*, tmp3.*,  tmp4.*, tmp6.*, "
                      + " (tmp3.importe_preparadores + tmp4.importe_prospectores) * "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+" as GASTOS"
                      + " from "
                      + "  ("
                      + "       select"
                      + "       sum(a.ECA_PREP_INS_C1_H_CARGA) C1_H, sum(a.ECA_PREP_INS_C1_M_CARGA) C1_M, sum(a.ECA_PREP_INS_C1_CARGA) C1_T,"
                      + "       sum(a.ECA_PREP_INS_C2_H_CARGA) C2_H, sum(a.ECA_PREP_INS_C2_M_CARGA) C2_M, sum(a.ECA_PREP_INS_C2_CARGA) C2_T,"
                      + "       sum(a.ECA_PREP_INS_C3_H_CARGA) C3_H, sum(a.ECA_PREP_INS_C3_M_CARGA) C3_M, sum(a.ECA_PREP_INS_C3_CARGA) C3_T,"
                      + "       sum(a.ECA_PREP_INS_C4_H_CARGA) C4_H, sum(a.ECA_PREP_INS_C4_M_CARGA) C4_M, sum(a.ECA_PREP_INS_C4_CARGA) C4_T"
                      + "       from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                      + "       a where a.eca_prep_solicitud = "+solAsociada.getSolicitudCod()
                      + "       and ECA_PREP_NIF_CARGA IS NOT NULL "
                      + "  )tmp1,"
                      + "  ("
                      + "       SELECT"
                      + "       sum(nvl(b.ECA_PREP_SEG_ANT_CARGA,0)) seguimientos_ant"
                      + "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "       INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      +"        ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      + "       where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "       and ECA_PREP_NIF_CARGA IS NOT NULL "
                      + "  )tmp2,"
                      + "  ("
                      //+ "       SELECT COUNT(*) num_preparadores,sum(nvl(eca_prep_imp_ss_eca_CARGA,0)) importe_preparadores"
                      //+ "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      //+ "       INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      //+ "       ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      //+ "       where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      //+ "       and ECA_PREP_NIF_CARGA IS NOT NULL "
                        
                        
                        
                      +"  select count(*) num_preparadores, sum(importe) importe_preparadores from ("
                      +"        select"
                      +"        coalesce(b.ECA_PREP_CODORIGEN,b.ECA_SOL_PREPARADORES_COD),"
                      +"        sum(case when"
                      +"        nvl(b.eca_prep_ins_c1_h_carga,0)*IM_C1_H +"
                      +"        nvl(b.eca_prep_ins_c1_m_carga,0)*IM_C1_m +"
                      +"        nvl(b.eca_prep_ins_c2_h_carga,0)*IM_C2_H +"
                      +"        nvl(b.eca_prep_ins_c2_m_carga,0)*IM_C2_m +"
                      +"        nvl(b.eca_prep_ins_c3_h_carga,0)*IM_C3_H +"
                      +"        nvl(b.eca_prep_ins_c3_m_carga,0)*IM_C3_m +"
                      +"        nvl(b.eca_prep_ins_c4_h_carga,0)*IM_C4_H +"
                      +"        nvl(b.eca_prep_ins_c4_m_carga,0)*IM_C4_m +"
                      +"        case when PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA_CARGA<b.ECA_PREP_IMP_SEG_ANT_CARGA"
                      +"        then PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA_CARGA"
                      +"        else b.ECA_PREP_IMP_SEG_ANT_CARGA end > b.ECA_PREP_IMP_SS_ECA_CARGA then b.eca_prep_imp_ss_eca_carga else"
                      +"        nvl(b.eca_prep_ins_c1_h_carga,0)*IM_C1_H +"
                      +"        nvl(b.eca_prep_ins_c1_m_carga,0)*IM_C1_m +"
                      +"        nvl(b.eca_prep_ins_c2_h_carga,0)*IM_C2_H +"
                      +"        nvl(b.eca_prep_ins_c2_m_carga,0)*IM_C2_m +"
                      +"        nvl(b.eca_prep_ins_c3_h_carga,0)*IM_C3_H +"
                      +"        nvl(b.eca_prep_ins_c3_m_carga,0)*IM_C3_m +"
                      +"        nvl(b.eca_prep_ins_c4_h_carga,0)*IM_C4_H +"
                      +"        nvl(b.eca_prep_ins_c4_m_carga,0)*IM_C4_m +"
                      +"        case when PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA_CARGA<b.ECA_PREP_IMP_SEG_ANT_CARGA"
                      +"        then PO_MAX_SEGUIMIENTOS*b.ECA_PREP_IMP_SS_ECA_CARGA"
                      +"        else b.ECA_PREP_IMP_SEG_ANT_CARGA end end) importe"
                      +"        FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      +"        INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      +"        ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      +"        inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CONFIGURACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" c on 1=1 and c.ano='"+anoExp+"'"
                      +"         left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" c on c.ECA_PREP_CODORIGEN=b.ECA_SOL_PREPARADORES_COD"
                      +"          where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      +"        and b.ECA_PREP_NIF_CARGA IS NOT NULL"
                      +"        group by coalesce(b.ECA_PREP_CODORIGEN,b.ECA_SOL_PREPARADORES_COD)"
                      +"    )"
                        
                        
                        
                        
                        
                      + "  )tmp3,"
                      + "  ("
                      //+ "       SELECT COUNT(*) num_prospectores, sum(nvl(eca_pros_imp_ss_eca_CARGA,0)) importe_prospectores"
                      //+ "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      //+ "       INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      //+ "       ON A.ECA_SOLICITUD_COD=B.ECA_PROS_SOLICITUD"
                      //+ "       where A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      //+ "       and ECA_PROS_NIF_CARGA IS NOT NULL  and ECA_PROS_CODORIGEN IS NULL"
                        
                      + "       select"
                      + "       count(*)  num_prospectores, sum(case when importe > 39100 then 39100 else importe end) importe_prospectores"
                      + "       from ("
                      + "               select"
                      + "               coalesce (b.eca_pros_codorigen,b.eca_sol_prospectores_cod)agrupar,"
                      + "               SUM(NVL(b.ECA_PROS_COSTE_CARGA,0)) importe"
                      + "               FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "               INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B  ON A.ECA_SOLICITUD_COD    =B.ECA_PROS_SOLICITUD"
                      + "               left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" c on b.eca_sol_prospectores_cod=c.eca_pros_codorigen"
                      + "               where"
                      + "               A.ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                      + "               group by coalesce (b.eca_pros_codorigen,b.eca_sol_prospectores_cod)"
                      + "           )"
                        
                        
                        
                      + "  )tmp4,"
                      /*+ "  ("
                      + "       select sum(nvl(importe,0)) * "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+" as GASTOS"
                      + "       from "
                      + "       ("
                      + "           SELECT sum(nvl(eca_prep_imp_ss_eca_CARGA,0)) importe"
                      + "           FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "           INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "           ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD"
                      + "           where ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                        + "       and ECA_PREP_NIF_CARGA IS NOT NULL "
                      + "           UNION ALL"
                      + "           SELECT sum(nvl(eca_pros_imp_ss_eca_CARGA,0)) importe"
                      + "           FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "           INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "           ON A.ECA_SOLICITUD_COD=B.ECA_PROS_SOLICITUD"
                      + "           where ECA_SOLICITUD_COD = "+solAsociada.getSolicitudCod()
                        + "       and ECA_PROS_NIF_CARGA IS NOT NULL "
                      + "       )"
                      + "  )tmp5,"*/
                      + "  ("
                      + "       SELECT imp-NVL(ECA_SUB_PUB,0)-NVL(ECA_SUB_PRIV,0)max_subvencionable "
                      + "       FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" AA"
                      + "       INNER JOIN"
                      + "       ("
                      + "           select eca_numexp, sum(nvl(importe,0)) * (1 + "+(conf != null && conf.getPoGastos() != null ? conf.getPoGastos().toPlainString() : 0)+")imp"
                      + "           FROM"
                      + "           ("
                      + "               SELECT A.ECA_NUMEXP, COUNT(*) personas ,sum(nvl(eca_prep_imp_ss_eca_CARGA,0)) importe"
                      + "               FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "               INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "               ON A.ECA_SOLICITUD_COD=B.ECA_PREP_SOLICITUD "
                      + "               where ECA_PREP_NIF_CARGA IS NOT NULL "
                      + "               GROUP BY A.ECA_NUMEXP"
                      + "               UNION"
                      + "               SELECT A.ECA_NUMEXP, COUNT(*) personas, sum(nvl(eca_pros_imp_ss_eca_CARGA,0)) importe"
                      + "               FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" A"
                      + "               INNER JOIN "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" B"
                      + "               ON A.ECA_SOLICITUD_COD=B.ECA_PROS_SOLICITUD "
                      + "               where ECA_PROS_NIF_CARGA IS NOT NULL "
                      + "               GROUP BY A.ECA_NUMEXP"
                      + "          )"
                      + "           GROUP BY ECA_NUMEXP"
                      + "       ) BB"
                      + "       ON AA.eca_numexp=BB.eca_numexp"
                      + "       where AA.eca_solicitud_cod = "+solAsociada.getSolicitudCod()
                      + "  )tmp6";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    datos = (DatosAnexosVO)MeLanbide44MappingUtils.getInstance().map(rs, DatosAnexosVO.class);
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando las áreas", ex);
                throw new Exception(ex);
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
            return datos;
        }
        else
        {
            return null;
        }
    }
    
    public EcaSolicitudVO guardarDatosSolicitud(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(sol.getSolicitudCod() == null)
            {
                sol.setSolicitudCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_SOLICITUD_COD, ECA_NUMEXP, ECA_INSER_C1_H, ECA_INSER_C1_M, ECA_INSER_C2_H, ECA_INSER_C2_M, ECA_INSER_C3_H, ECA_INSER_C3_M, ECA_INSER_C4_H, ECA_INSER_C4_M, ECA_INSER_C5_H, ECA_INSER_C5_M, ECA_INSER_C6_H, ECA_INSER_C6_M, ECA_SEG_H, ECA_SEG_M, ECA_SEG_ACTUACIONES, ECA_PROSPECTORES_NUM, ECA_PROSPECTORES_IMP, ECA_PREPARADORES_NUM, ECA_PREPARADORES_IMP, ECA_GASTOS, ECA_OTRAS_SUB, ECA_SUB_PUB, ECA_SUB_PRIV, ECA_TOTAL_SUBVENCION, ECA_TOTAL_APROBADO, ECA_EXP_EJE, ECA_SUB_PUB_CARGAM, ECA_SUB_PRIV_CARGAM"
                        + ",ECA_INSER_C1_H_CONC, ECA_INSER_C1_M_CONC, ECA_INSER_C2_H_CONC, ECA_INSER_C2_M_CONC, ECA_INSER_C3_H_CONC, ECA_INSER_C3_M_CONC, ECA_INSER_C4_H_CONC, ECA_INSER_C4_M_CONC, ECA_INSER_C5_H_CONC, ECA_INSER_C5_M_CONC,ECA_INSER_C6_H_CONC, ECA_INSER_C6_M_CONC,"
                        + " ECA_SEG_H_CONC, ECA_SEG_M_CONC, ECA_SEG_ACTUACIONES_CONC, ECA_PROSPECTORES_NUM_CONC, ECA_PROSPECTORES_IMP_CONC, ECA_PREPARADORES_NUM_CONC, ECA_PREPARADORES_IMP_CONC, ECA_GASTOS_CONC,"
                        + " ECA_SUB_PUB_CONCEDIDO, ECA_SUB_PRIV_CONCEDIDO"
                        + ")"
                        + " values("+sol.getSolicitudCod()
                        + ", '"+sol.getNumExp()+"'"
                        + ", "+(sol.getInserC1H() != null ? sol.getInserC1H().toString() : "null")
                        + ", "+(sol.getInserC1M() != null ? sol.getInserC1M().toString() : "null")
                        + ", "+(sol.getInserC2H() != null ? sol.getInserC2H().toString() : "null")
                        + ", "+(sol.getInserC2M() != null ? sol.getInserC2M().toString() : "null")
                        + ", "+(sol.getInserC3H() != null ? sol.getInserC3H().toString() : "null")
                        + ", "+(sol.getInserC3M() != null ? sol.getInserC3M().toString() : "null")
                        + ", "+(sol.getInserC4H() != null ? sol.getInserC4H().toString() : "null")
                        + ", "+(sol.getInserC4M() != null ? sol.getInserC4M().toString() : "null")
                        + ", "+(sol.getInserC5H() != null ? sol.getInserC5H().toString() : "null")
                        + ", "+(sol.getInserC5M() != null ? sol.getInserC5M().toString() : "null")
                        + ", "+(sol.getInserC6H() != null ? sol.getInserC6H().toString() : "null")
                        + ", "+(sol.getInserC6M() != null ? sol.getInserC6M().toString() : "null")
                        + ", "+(sol.getSegH() != null ? sol.getSegH().toString() : "null")
                        + ", "+(sol.getSegM() != null ? sol.getSegM().toString() : "null")
                        + ", "+(sol.getSegActuaciones() != null ? sol.getSegActuaciones().toPlainString() : "null")
                        + ", "+(sol.getProspectoresNum() != null ? sol.getProspectoresNum().toString() : "null")
                        + ", "+(sol.getProspectoresImp() != null ? sol.getProspectoresImp().toPlainString() : "null")
                        + ", "+(sol.getPreparadoresNum() != null ? sol.getPreparadoresNum().toString() : "null")
                        + ", "+(sol.getPreparadoresImp() != null ? sol.getPreparadoresImp().toPlainString() : "null")
                        + ", "+(sol.getGastos() != null ? sol.getGastos().toPlainString() : "null")
                        + ", "+(sol.getOtrasSub() != null ? (sol.getOtrasSub() == true ? "1" : "0") : "null")
                        + ", "+(sol.getSubPub() != null ? sol.getSubPub().toPlainString() : "null")
                        + ", "+(sol.getSubPriv() != null ? sol.getSubPriv().toPlainString() : "null")
                        + ", "+(sol.getTotalSubvencion() != null ? sol.getTotalSubvencion().toPlainString() : "null")
                        + ", "+(sol.getTotalAprobado() != null ? sol.getTotalAprobado().toPlainString() : "null")
                        + ", "+(sol.getExpEje() != null ? sol.getExpEje() : "null")
                        + ", "+(sol.getSubPubCargaManual() != null ? sol.getSubPubCargaManual().toPlainString() : "null")
                        + ", "+(sol.getSubPrivCargaManual() != null ? sol.getSubPrivCargaManual().toPlainString() : "null")
                        + ", "+(sol.getInserC1HConc() != null ? sol.getInserC1HConc().toString() : "null")
                        + ", "+(sol.getInserC1MConc() != null ? sol.getInserC1MConc().toString() : "null")
                        + ", "+(sol.getInserC2HConc() != null ? sol.getInserC2HConc().toString() : "null")
                        + ", "+(sol.getInserC2MConc() != null ? sol.getInserC2MConc().toString() : "null")
                        + ", "+(sol.getInserC3HConc() != null ? sol.getInserC3HConc().toString() : "null")
                        + ", "+(sol.getInserC3MConc() != null ? sol.getInserC3MConc().toString() : "null")
                        + ", "+(sol.getInserC4HConc() != null ? sol.getInserC4HConc().toString() : "null")
                        + ", "+(sol.getInserC4MConc() != null ? sol.getInserC4MConc().toString() : "null")   
                        + ", "+(sol.getInserC5HConc() != null ? sol.getInserC5HConc().toString() : "null")
                        + ", "+(sol.getInserC5MConc() != null ? sol.getInserC5MConc().toString() : "null")
                        + ", "+(sol.getInserC6HConc() != null ? sol.getInserC6HConc().toString() : "null")
                        + ", "+(sol.getInserC6MConc() != null ? sol.getInserC6MConc().toString() : "null")
                        + ", "+(sol.getSegHConc() != null ? sol.getSegHConc().toString() : "null")
                        + ", "+(sol.getSegMConc() != null ? sol.getSegMConc().toString() : "null")
                        + ", "+(sol.getSegActuacionesConc() != null ? sol.getSegActuacionesConc().toPlainString() : "null")
                        + ", "+(sol.getProspectoresNumConc() != null ? sol.getProspectoresNumConc().toString() : "null")
                        + ", "+(sol.getProspectoresImpConc() != null ? sol.getProspectoresImpConc().toPlainString() : "null")
                        + ", "+(sol.getPreparadoresNumConc() != null ? sol.getPreparadoresNumConc().toString() : "null")
                        + ", "+(sol.getPreparadoresImpConc() != null ? sol.getPreparadoresImpConc().toPlainString() : "null")
                        + ", "+(sol.getGastosConc() != null ? sol.getGastosConc().toPlainString() : "null")
                        + ", "+(sol.getSubPubConcedidos() != null ? sol.getSubPubConcedidos().toPlainString() : "null")
                        + ", "+(sol.getSubPrivConcedidos() != null ? sol.getSubPrivConcedidos().toPlainString() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_NUMEXP = '"+sol.getNumExp()+"'"
                        + ", ECA_INSER_C1_H = "+(sol.getInserC1H() != null ? sol.getInserC1H().toString() : "null")
                        + ", ECA_INSER_C1_M = "+(sol.getInserC1M() != null ? sol.getInserC1M().toString() : "null")
                        + ", ECA_INSER_C2_H = "+(sol.getInserC2H() != null ? sol.getInserC2H().toString() : "null")
                        + ", ECA_INSER_C2_M = "+(sol.getInserC2M() != null ? sol.getInserC2M().toString() : "null")
                        + ", ECA_INSER_C3_H = "+(sol.getInserC3H() != null ? sol.getInserC3H().toString() : "null")
                        + ", ECA_INSER_C3_M = "+(sol.getInserC3M() != null ? sol.getInserC3M().toString() : "null")
                        + ", ECA_INSER_C4_H = "+(sol.getInserC4H() != null ? sol.getInserC4H().toString() : "null")
                        + ", ECA_INSER_C4_M = "+(sol.getInserC4M() != null ? sol.getInserC4M().toString() : "null")
                        + ", ECA_INSER_C5_H = "+(sol.getInserC5H() != null ? sol.getInserC5H().toString() : "null")
                        + ", ECA_INSER_C5_M = "+(sol.getInserC5M() != null ? sol.getInserC5M().toString() : "null")
                        + ", ECA_INSER_C6_H = "+(sol.getInserC6H() != null ? sol.getInserC6H().toString() : "null")
                        + ", ECA_INSER_C6_M = "+(sol.getInserC6M() != null ? sol.getInserC6M().toString() : "null")
                        + ", ECA_SEG_H = "+(sol.getSegH() != null ? sol.getSegH().toString() : "null")
                        + ", ECA_SEG_M = "+(sol.getSegM() != null ? sol.getSegM().toString() : "null")
                        + ", ECA_SEG_ACTUACIONES = "+(sol.getSegActuaciones() != null ? sol.getSegActuaciones().toPlainString() : "null")
                        + ", ECA_PROSPECTORES_NUM = "+(sol.getProspectoresNum() != null ? sol.getProspectoresNum().toString() : "null")
                        + ", ECA_PROSPECTORES_IMP = "+(sol.getProspectoresImp() != null ? sol.getProspectoresImp().toPlainString() : "null")
                        + ", ECA_PREPARADORES_NUM = "+(sol.getPreparadoresNum() != null ? sol.getPreparadoresNum().toString() : "null")
                        + ", ECA_PREPARADORES_IMP = "+(sol.getPreparadoresImp() != null ? sol.getPreparadoresImp().toPlainString() : "null")
                        + ", ECA_GASTOS = "+(sol.getGastos() != null ? sol.getGastos().toPlainString() : "null")
                        + ", ECA_OTRAS_SUB = "+(sol.getOtrasSub() != null ? (sol.getOtrasSub() == true ? "1" : "0") : "null")
                        + ", ECA_SUB_PUB = "+(sol.getSubPub() != null ? sol.getSubPub().toPlainString() : "null")
                        + ", ECA_SUB_PRIV = "+(sol.getSubPriv() != null ? sol.getSubPriv().toPlainString() : "null")
                        + ", ECA_TOTAL_SUBVENCION = "+(sol.getTotalSubvencion() != null ? sol.getTotalSubvencion().toPlainString() : "null")
                        + ", ECA_TOTAL_APROBADO = "+(sol.getTotalAprobado() != null ? sol.getTotalAprobado().toPlainString() : "null")
                        + ", ECA_EXP_EJE = "+(sol.getExpEje() != null ? sol.getExpEje() : "null")
                        + ", ECA_SUB_PUB_CARGAM = "+(sol.getSubPubCargaManual() != null ? sol.getSubPubCargaManual().toPlainString() : "null")
                        + ", ECA_SUB_PRIV_CARGAM = "+(sol.getSubPrivCargaManual() != null ? sol.getSubPrivCargaManual().toPlainString() : "null")
                        + ", ECA_INSER_C1_H_CONC = "+(sol.getInserC1HConc() != null ? sol.getInserC1HConc().toString() : "null")
                        + ", ECA_INSER_C1_M_CONC = "+(sol.getInserC1MConc() != null ? sol.getInserC1MConc().toString() : "null")
                        + ", ECA_INSER_C2_H_CONC = "+(sol.getInserC2HConc() != null ? sol.getInserC2HConc().toString() : "null")
                        + ", ECA_INSER_C2_M_CONC = "+(sol.getInserC2MConc() != null ? sol.getInserC2MConc().toString() : "null")
                        + ", ECA_INSER_C3_H_CONC = "+(sol.getInserC3HConc() != null ? sol.getInserC3HConc().toString() : "null")
                        + ", ECA_INSER_C3_M_CONC = "+(sol.getInserC3MConc() != null ? sol.getInserC3MConc().toString() : "null")
                        + ", ECA_INSER_C4_H_CONC = "+(sol.getInserC4HConc() != null ? sol.getInserC4HConc().toString() : "null")
                        + ", ECA_INSER_C4_M_CONC = "+(sol.getInserC4MConc() != null ? sol.getInserC4MConc().toString() : "null")
                        + ", ECA_INSER_C5_H_CONC = "+(sol.getInserC5HConc() != null ? sol.getInserC5HConc().toString() : "null")
                        + ", ECA_INSER_C5_M_CONC = "+(sol.getInserC5MConc() != null ? sol.getInserC5MConc().toString() : "null")
                        + ", ECA_INSER_C6_H_CONC = "+(sol.getInserC6HConc() != null ? sol.getInserC6HConc().toString() : "null")
                        + ", ECA_INSER_C6_M_CONC = "+(sol.getInserC6MConc() != null ? sol.getInserC6MConc().toString() : "null")
                        + ", ECA_SEG_H_CONC = "+(sol.getSegHConc() != null ? sol.getSegHConc().toString() : "null")
                        + ", ECA_SEG_M_CONC = "+(sol.getSegMConc() != null ? sol.getSegMConc().toString() : "null")
                        + ", ECA_SEG_ACTUACIONES_CONC = "+(sol.getSegActuacionesConc() != null ? sol.getSegActuacionesConc().toPlainString() : "null")
                        + ", ECA_PROSPECTORES_NUM_CONC = "+(sol.getProspectoresNumConc() != null ? sol.getProspectoresNumConc().toString() : "null")
                        + ", ECA_PROSPECTORES_IMP_CONC = "+(sol.getProspectoresImpConc() != null ? sol.getProspectoresImpConc().toPlainString() : "null")
                        + ", ECA_PREPARADORES_NUM_CONC = "+(sol.getPreparadoresNumConc() != null ? sol.getPreparadoresNumConc().toString() : "null")
                        + ", ECA_PREPARADORES_IMP_CONC = "+(sol.getPreparadoresImpConc() != null ? sol.getPreparadoresImpConc().toPlainString() : "null")
                        + ", ECA_GASTOS_CONC = "+(sol.getGastosConc() != null ? sol.getGastosConc().toPlainString() : "null")
                        + ", ECA_SUB_PUB_CONCEDIDO = "+(sol.getSubPubConcedidos() != null ? sol.getSubPubConcedidos().toPlainString() : "null")
                        + ", ECA_SUB_PRIV_CONCEDIDO = "+(sol.getSubPrivConcedidos()!= null ? sol.getSubPrivConcedidos().toPlainString() : "null")
                        + " where ECA_SOLICITUD_COD = "+sol.getSolicitudCod();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return sol;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    
    public String guardarHistorico(String numExp, String procedimiento, Connection con) throws Exception
    {
         CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call "+procedimiento+"(?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, numExp);
           // st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            //String result = st.getString(1);
            return "";
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando hist�rico", ex);
            throw new Exception(ex);
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
    
    public EcaSolicitudVO getSolicitudPorCodigo(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSolicitudVO retSol = null;
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    + " where ECA_SOLICITUD_COD = '" + sol.getSolicitudCod().toString() +"'";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retSol = (EcaSolicitudVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolicitudVO.class);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando solicitud con id "+sol.getSolicitudCod(), ex);
            throw new Exception(ex);
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
        return retSol;
    }
    
    private Integer getNextId(String seqName, Connection con) throws Exception
    {
        
        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try
        {
            String query = null;
            //Creo el id con la secuencia
            query = "select "+ seqName+".nextval from dual";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                numSec = rs.getInt(1);
                if(rs.wasNull())
                {
                    throw new Exception();
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia "+seqName, ex);
            throw new Exception(ex);
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
        return numSec;
    }
    
    public EcaSolPreparadoresVO guardarEcaSolPreparadoresVO(EcaSolPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(prep.getSolPreparadoresCod() == null)
            {
                prep.setSolPreparadoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_SOL_PREPARADORES_COD, ECA_PREP_NIF, ECA_PREP_NOMBRE, ECA_PREP_FECINI, ECA_PREP_FECFIN, ECA_PREP_HORAS_JC, ECA_PREP_HORAS_CONT, ECA_PREP_HORAS_ECA, ECA_PREP_IMP_SS_JC, ECA_PREP_IMP_SS_JR, ECA_PREP_IMP_SS_ECA, ECA_PREP_SEG_ANT, ECA_PREP_IMP_SEG_ANT, ECA_PREP_INS_C1_H, ECA_PREP_INS_C1_M, ECA_PREP_INS_C1, ECA_PREP_INS_C2_H, ECA_PREP_INS_C2_M, ECA_PREP_INS_C2, ECA_PREP_INS_C3_H, ECA_PREP_INS_C3_M, ECA_PREP_INS_C3, ECA_PREP_INS_C4_H, ECA_PREP_INS_C4_M, ECA_PREP_INS_C4, ECA_PREP_INS_C5_H, ECA_PREP_INS_C5_M, ECA_PREP_INS_C5,ECA_PREP_INS_C6_H, ECA_PREP_INS_C6_M, ECA_PREP_INS_C6, ECA_PREP_INS_IMPORTE, ECA_PREP_INS_SEG_IMPORTE, ECA_PREP_COSTE, ECA_PREP_SOLICITUD,"
                        + " ECA_PREP_IMPTE_CONCEDIDO, "
                        + " ECA_PREP_NIF_CARGA, ECA_PREP_NOMBRE_CARGA, ECA_PREP_FECINI_CARGA, ECA_PREP_FECFIN_CARGA, ECA_PREP_HORAS_JC_CARGA, ECA_PREP_HORAS_CONT_CARGA, ECA_PREP_HORAS_ECA_CARGA, ECA_PREP_IMP_SS_JC_CARGA, ECA_PREP_IMP_SS_JR_CARGA, ECA_PREP_IMP_SS_ECA_CARGA, ECA_PREP_SEG_ANT_CARGA, ECA_PREP_IMP_SEG_ANT_CARGA, ECA_PREP_INS_C1_H_CARGA, ECA_PREP_INS_C1_M_CARGA, ECA_PREP_INS_C1_CARGA, ECA_PREP_INS_C2_H_CARGA, ECA_PREP_INS_C2_M_CARGA, ECA_PREP_INS_C2_CARGA, ECA_PREP_INS_C3_H_CARGA, ECA_PREP_INS_C3_M_CARGA, ECA_PREP_INS_C3_CARGA, ECA_PREP_INS_C4_H_CARGA, ECA_PREP_INS_C4_M_CARGA, ECA_PREP_INS_C4_CARGA, ECA_PREP_INS_C5_H_CARGA, ECA_PREP_INS_C5_M_CARGA, ECA_PREP_INS_C5_CARGA,ECA_PREP_INS_C6_H_CARGA, ECA_PREP_INS_C6_M_CARGA, ECA_PREP_INS_C6_CARGA, ECA_PREP_INS_IMPORTE_CARGA, ECA_PREP_INS_SEG_IMPORTE_CARGA, ECA_PREP_COSTE_CARGA,"
                        + " ECA_PREP_TIPO_SUST, ECA_PREP_CODORIGEN)"
                        + " values("+prep.getSolPreparadoresCod()
                        + ", "+(prep.getNif() != null ? "'"+prep.getNif()+"'" : "null")
                        + ", "+(prep.getNombre() != null ? "'"+prep.getNombre()+"'" : "null")
                        + ", "+(prep.getFecIni() != null ? "TO_DATE('"+format.format(prep.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(prep.getFecFin() != null ? "TO_DATE('"+format.format(prep.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(prep.getHorasJC() != null ? prep.getHorasJC().toPlainString() : "null")
                        + ", "+(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString() : "null")
                        + ", "+(prep.getHorasEca() != null ? prep.getHorasEca().toPlainString() : "null")
                        + ", "+(prep.getImpSSJC() != null ? prep.getImpSSJC().toPlainString() : "null")
                        + ", "+(prep.getImpSSJR() != null ? prep.getImpSSJR().toPlainString() : "null")
                        + ", "+(prep.getImpSSECA() != null ? prep.getImpSSECA().toPlainString() : "null")
                        + ", "+(prep.getSegAnt() != null ? prep.getSegAnt().toString() : "null")
                        + ", "+(prep.getImpSegAnt() != null ? prep.getImpSegAnt().toPlainString() : "null")
                        + ", "+(prep.getInsC1H() != null ? prep.getInsC1H().toPlainString() : "null")
                        + ", "+(prep.getInsC1M() != null ? prep.getInsC1M().toPlainString() : "null")
                        + ", "+(prep.getInsC1() != null ? prep.getInsC1().toPlainString() : "null")
                        + ", "+(prep.getInsC2H() != null ? prep.getInsC2H().toPlainString() : "null")
                        + ", "+(prep.getInsC2M() != null ? prep.getInsC2M().toPlainString() : "null")
                        + ", "+(prep.getInsC2() != null ? prep.getInsC2().toPlainString() : "null")
                        + ", "+(prep.getInsC3H() != null ? prep.getInsC3H().toPlainString() : "null")
                        + ", "+(prep.getInsC3M() != null ? prep.getInsC3M().toPlainString() : "null")
                        + ", "+(prep.getInsC3() != null ? prep.getInsC3().toPlainString() : "null")
                        + ", "+(prep.getInsC4H() != null ? prep.getInsC4H().toPlainString() : "null")
                        + ", "+(prep.getInsC4M() != null ? prep.getInsC4M().toPlainString() : "null")
                        + ", "+(prep.getInsC4() != null ? prep.getInsC4().toPlainString() : "null")
                        + ", "+(prep.getInsC5H() != null ? prep.getInsC5H().toPlainString() : "null")
                        + ", "+(prep.getInsC5M() != null ? prep.getInsC5M().toPlainString() : "null")
                        + ", "+(prep.getInsC5() != null ? prep.getInsC5().toPlainString() : "null")
                        + ", "+(prep.getInsC6H() != null ? prep.getInsC6H().toPlainString() : "null")
                        + ", "+(prep.getInsC6M() != null ? prep.getInsC6M().toPlainString() : "null")
                        + ", "+(prep.getInsC6() != null ? prep.getInsC6().toPlainString() : "null")
                        + ", "+(prep.getInsImporte() != null ? prep.getInsImporte().toPlainString() : "null")
                        + ", "+(prep.getInsSegImporte() != null ? prep.getInsSegImporte().toPlainString() : "null")
                        + ", "+(prep.getCoste() != null ? prep.getCoste().toPlainString() : "null")
                        + ", "+(prep.getSolicitud() != null ? prep.getSolicitud().toString() : "null")
                        + ", "+(prep.getImpteConcedido() != null ? prep.getImpteConcedido().toPlainString() : "null")
                        + ", "+(prep.getNif_Carga() != null ? "'"+prep.getNif_Carga()+"'" : "null")
                        + ", "+(prep.getNombre_Carga() != null ? "'"+prep.getNombre_Carga()+"'" : "null")
                        + ", "+(prep.getFecIni_Carga() != null ? "TO_DATE('"+format.format(prep.getFecIni_Carga())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(prep.getFecFin_Carga() != null ? "TO_DATE('"+format.format(prep.getFecFin_Carga())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(prep.getHorasJC_Carga() != null ? prep.getHorasJC_Carga().toPlainString() : "null")
                        + ", "+(prep.getHorasCont_Carga() != null ? prep.getHorasCont_Carga().toPlainString() : "null")
                        + ", "+(prep.getHorasEca_Carga() != null ? prep.getHorasEca_Carga().toPlainString() : "null")
                        + ", "+(prep.getImpSSJC_Carga() != null ? prep.getImpSSJC_Carga().toPlainString() : "null")
                        + ", "+(prep.getImpSSJR_Carga() != null ? prep.getImpSSJR_Carga().toPlainString() : "null")
                        + ", "+(prep.getImpSSECA_Carga() != null ? prep.getImpSSECA_Carga().toPlainString() : "null")
                        + ", "+(prep.getSegAnt_Carga() != null ? prep.getSegAnt_Carga().toString() : "null")
                        + ", "+(prep.getImpSegAnt_Carga() != null ? prep.getImpSegAnt_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC1H_Carga() != null ? prep.getInsC1H_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC1M_Carga() != null ? prep.getInsC1M_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC1_Carga() != null ? prep.getInsC1_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC2H_Carga() != null ? prep.getInsC2H_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC2M_Carga() != null ? prep.getInsC2M_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC2_Carga() != null ? prep.getInsC2_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC3H_Carga() != null ? prep.getInsC3H_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC3M_Carga() != null ? prep.getInsC3M_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC3_Carga() != null ? prep.getInsC3_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC4H_Carga() != null ? prep.getInsC4H_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC4M_Carga() != null ? prep.getInsC4M_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC4_Carga() != null ? prep.getInsC4_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC5H_Carga() != null ? prep.getInsC5H_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC5M_Carga() != null ? prep.getInsC5M_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC5_Carga() != null ? prep.getInsC5_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC6H_Carga() != null ? prep.getInsC6H_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC6M_Carga() != null ? prep.getInsC6M_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsC6_Carga() != null ? prep.getInsC6_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsImporte_Carga() != null ? prep.getInsImporte_Carga().toPlainString() : "null")
                        + ", "+(prep.getInsSegImporte_Carga() != null ? prep.getInsSegImporte_Carga().toPlainString() : "null")
                        + ", "+(prep.getCoste_Carga() != null ? prep.getCoste_Carga().toPlainString() : "null")
                        + ", "+(prep.getTipoSust() != null ? prep.getTipoSust() : "null")
                        + ", "+(prep.getSolPreparadorOrigen() != null ? prep.getSolPreparadorOrigen().toString() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_PREP_NIF = "+(prep.getNif() != null ? "'"+prep.getNif()+"'" : "null")
                        + ", ECA_PREP_NOMBRE = "+(prep.getNombre() != null ? "'"+prep.getNombre()+"'" : "null")
                        + ", ECA_PREP_FECINI = "+(prep.getFecIni() != null ? "TO_DATE('"+format.format(prep.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PREP_FECFIN = "+(prep.getFecFin() != null ? "TO_DATE('"+format.format(prep.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PREP_HORAS_JC = "+(prep.getHorasJC() != null ? prep.getHorasJC().toPlainString() : "null")
                        + ", ECA_PREP_HORAS_CONT = "+(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString() : "null")
                        + ", ECA_PREP_HORAS_ECA = "+(prep.getHorasEca() != null ? prep.getHorasEca().toPlainString() : "null")
                        + ", ECA_PREP_IMP_SS_JC = "+(prep.getImpSSJC() != null ? prep.getImpSSJC().toPlainString() : "null")
                        + ", ECA_PREP_IMP_SS_JR = "+(prep.getImpSSJR() != null ? prep.getImpSSJR().toPlainString() : "null")
                        + ", ECA_PREP_IMP_SS_ECA = "+(prep.getImpSSECA() != null ? prep.getImpSSECA().toPlainString() : "null")
                        + ", ECA_PREP_SEG_ANT = "+(prep.getSegAnt() != null ? prep.getSegAnt().toString() : "null")
                        + ", ECA_PREP_IMP_SEG_ANT = "+(prep.getImpSegAnt() != null ? prep.getImpSegAnt().toPlainString() : "null")
                        + ", ECA_PREP_INS_C1_H = "+(prep.getInsC1H() != null ? prep.getInsC1H().toPlainString() : "null")
                        + ", ECA_PREP_INS_C1_M = "+(prep.getInsC1M() != null ? prep.getInsC1M().toPlainString() : "null")
                        + ", ECA_PREP_INS_C1 = "+(prep.getInsC1() != null ? prep.getInsC1().toPlainString() : "null")
                        + ", ECA_PREP_INS_C2_H = "+(prep.getInsC2H() != null ? prep.getInsC2H().toPlainString() : "null")
                        + ", ECA_PREP_INS_C2_M = "+(prep.getInsC2M() != null ? prep.getInsC2M().toPlainString() : "null")
                        + ", ECA_PREP_INS_C2 = "+(prep.getInsC2() != null ? prep.getInsC2().toPlainString() : "null")
                        + ", ECA_PREP_INS_C3_H = "+(prep.getInsC3H() != null ? prep.getInsC3H().toPlainString() : "null")
                        + ", ECA_PREP_INS_C3_M = "+(prep.getInsC3M() != null ? prep.getInsC3M().toPlainString() : "null")
                        + ", ECA_PREP_INS_C3 = "+(prep.getInsC3() != null ? prep.getInsC3().toPlainString() : "null")
                        + ", ECA_PREP_INS_C4_H = "+(prep.getInsC4H() != null ? prep.getInsC4H().toPlainString() : "null")
                        + ", ECA_PREP_INS_C4_M = "+(prep.getInsC4M() != null ? prep.getInsC4M().toPlainString() : "null")
                        + ", ECA_PREP_INS_C4 = "+(prep.getInsC4() != null ? prep.getInsC4().toPlainString() : "null")
                        + ", ECA_PREP_INS_C5_H = "+(prep.getInsC5H() != null ? prep.getInsC5H().toPlainString() : "null")
                        + ", ECA_PREP_INS_C5_M = "+(prep.getInsC5M() != null ? prep.getInsC5M().toPlainString() : "null")
                        + ", ECA_PREP_INS_C5 = "+(prep.getInsC5() != null ? prep.getInsC5().toPlainString() : "null")
                        + ", ECA_PREP_INS_C6_H = "+(prep.getInsC6H() != null ? prep.getInsC6H().toPlainString() : "null")
                        + ", ECA_PREP_INS_C6_M = "+(prep.getInsC6M() != null ? prep.getInsC6M().toPlainString() : "null")
                        + ", ECA_PREP_INS_C6 = "+(prep.getInsC6() != null ? prep.getInsC6().toPlainString() : "null")
                        + ", ECA_PREP_INS_IMPORTE = "+(prep.getInsImporte() != null ? prep.getInsImporte().toPlainString() : "null")
                        + ", ECA_PREP_INS_SEG_IMPORTE = "+(prep.getInsSegImporte() != null ? prep.getInsSegImporte().toPlainString() : "null")
                        + ", ECA_PREP_COSTE = "+(prep.getCoste() != null ? prep.getCoste().toPlainString() : "null")
                        + ", ECA_PREP_IMPTE_CONCEDIDO = "+(prep.getImpteConcedido() != null ? prep.getImpteConcedido().toPlainString() : "null") 
                        + ", ECA_PREP_SOLICITUD = "+(prep.getSolicitud() != null ? prep.getSolicitud().toString() : "null")
                        + ", ECA_PREP_TIPO_SUST = "+(prep.getTipoSust() != null ? prep.getTipoSust() : "null")
                        + ", ECA_PREP_CODORIGEN = "+(prep.getSolPreparadorOrigen() != null ? prep.getSolPreparadorOrigen().toString() : "null")
                        + " where ECA_SOL_PREPARADORES_COD = "+prep.getSolPreparadoresCod();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return prep;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del preparador "+(prep != null ? prep.getNif() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaPreparadorSolicitudVO> getListaPreparadoresSolicitud(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaPreparadorSolicitudVO> retList = new ArrayList<FilaPreparadorSolicitudVO>();
        try
        {
            String query = "select p.*,"
                          +" case when p.ECA_PREP_CODORIGEN IS NULL THEN p.ECA_SOL_PREPARADORES_COD ELSE p.ECA_PREP_CODORIGEN END AS ORIGEN"
                          +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" p"
                          +" where ECA_PREP_SOLICITUD = "+sol.getSolicitudCod()
                          +" order by ORIGEN, ECA_PREP_CODORIGEN NULLS FIRST,  ECA_PREP_FECINI, ECA_PREP_FECFIN, ECA_PREP_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaPreparadorSolicitudVO fila = null;
            while(rs.next())
            {
                fila = (FilaPreparadorSolicitudVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaPreparadorSolicitudVO.class);
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public int eliminarPreparadorSolicitud(EcaSolPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_SOL_PREPARADORES_COD = "+prep.getSolPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaSolPreparadoresVO getPreparadorSolicitudPorId(EcaSolPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSolPreparadoresVO retPrep = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_SOL_PREPARADORES_COD = "+prep.getSolPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retPrep =  (EcaSolPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolPreparadoresVO.class);
            }
            return retPrep;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando preparador solicitud "+(prep != null ? prep.getSolPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                    rs.close();
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaSolProspectoresVO guardarEcaSolProspectoresVO(EcaSolProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(pros.getSolProspectoresCod() == null)
            {
                pros.setSolProspectoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_SOL_PROSPECTORES_COD, ECA_PROS_NIF, ECA_PROS_NOMBRE, ECA_PROS_FECINI, ECA_PROS_FECFIN, ECA_PROS_HORAS_JC, ECA_PROS_HORAS_CONT, ECA_PROS_HORAS_ECA, ECA_PROS_IMP_SS_JC, ECA_PROS_IMP_SS_JR, ECA_PROS_IMP_SS_ECA, ECA_PROS_VISITAS, ECA_PROS_VISITAS_IMP, ECA_PROS_COSTE, ECA_PROS_SOLICITUD,"
                        + " ECA_PROS_IMPTE_CONCEDIDO, "
                        + " ECA_PROS_NIF_CARGA, ECA_PROS_NOMBRE_CARGA, ECA_PROS_FECINI_CARGA, ECA_PROS_FECFIN_CARGA, ECA_PROS_HORAS_JC_CARGA, ECA_PROS_HORAS_CONT_CARGA, ECA_PROS_HORAS_ECA_CARGA, ECA_PROS_IMP_SS_JC_CARGA, ECA_PROS_IMP_SS_JR_CARGA, ECA_PROS_IMP_SS_ECA_CARGA, ECA_PROS_VISITAS_CARGA, ECA_PROS_VISITAS_IMP_CARGA, ECA_PROS_COSTE_CARGA,"
                        + " ECA_PROS_TIPO_SUST, ECA_PROS_CODORIGEN)"
                        + " values("+pros.getSolProspectoresCod()
                        + ", "+(pros.getNif() != null ? "'"+pros.getNif()+"'" : "null")
                        + ", "+(pros.getNombre() != null ? "'"+pros.getNombre()+"'" : "null")
                        + ", "+(pros.getFecIni() != null ? "TO_DATE('"+format.format(pros.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(pros.getFecFin() != null ? "TO_DATE('"+format.format(pros.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(pros.getHorasJC() != null ? pros.getHorasJC().toPlainString() : "null")
                        + ", "+(pros.getHorasCont() != null ? pros.getHorasCont().toPlainString() : "null")
                        + ", "+(pros.getHorasEca() != null ? pros.getHorasEca().toPlainString() : "null")
                        + ", "+(pros.getImpSSJC() != null ? pros.getImpSSJC().toPlainString() : "null")
                        + ", "+(pros.getImpSSJR() != null ? pros.getImpSSJR().toPlainString() : "null")
                        + ", "+(pros.getImpSSECA() != null ? pros.getImpSSECA().toPlainString() : "null")
                        + ", "+(pros.getVisitas() != null ? pros.getVisitas().toString() : "null")
                        + ", "+(pros.getVisitasImp() != null ? pros.getVisitasImp().toPlainString() : "null")
                        + ", "+(pros.getCoste() != null ? pros.getCoste().toPlainString() : "null")
                        + ", "+(pros.getSolicitud() != null ? pros.getSolicitud().toString() : "null")
                        + ", "+(pros.getImpteConcedido()!= null ? pros.getImpteConcedido().toPlainString() : "null")
                        + ", "+(pros.getNif_Carga() != null ? "'"+pros.getNif_Carga()+"'" : "null")
                        + ", "+(pros.getNombre_Carga() != null ? "'"+pros.getNombre_Carga()+"'" : "null")
                        + ", "+(pros.getFecIni_Carga() != null ? "TO_DATE('"+format.format(pros.getFecIni_Carga())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(pros.getFecFin_Carga() != null ? "TO_DATE('"+format.format(pros.getFecFin_Carga())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(pros.getHorasJC_Carga() != null ? pros.getHorasJC_Carga().toPlainString() : "null")
                        + ", "+(pros.getHorasCont_Carga() != null ? pros.getHorasCont_Carga().toPlainString() : "null")
                        + ", "+(pros.getHorasEca_Carga() != null ? pros.getHorasEca_Carga().toPlainString() : "null")
                        + ", "+(pros.getImpSSJC_Carga() != null ? pros.getImpSSJC_Carga().toPlainString() : "null")
                        + ", "+(pros.getImpSSJR_Carga() != null ? pros.getImpSSJR_Carga().toPlainString() : "null")
                        + ", "+(pros.getImpSSECA_Carga() != null ? pros.getImpSSECA_Carga().toPlainString() : "null")
                        + ", "+(pros.getVisitas_Carga() != null ? pros.getVisitas_Carga().toString() : "null")
                        + ", "+(pros.getVisitasImp_Carga() != null ? pros.getVisitasImp_Carga().toPlainString() : "null")
                        + ", "+(pros.getCoste_Carga() != null ? pros.getCoste_Carga().toPlainString() : "null")
                        + ", "+(pros.getTipoSust() != null ? pros.getTipoSust() : "null")
                        + ", "+(pros.getSolProspectorOrigen() != null ? pros.getSolProspectorOrigen().toString() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_PROS_NIF = "+(pros.getNif() != null ? "'"+pros.getNif()+"'" : "null")
                        + ", ECA_PROS_NOMBRE = "+(pros.getNombre() != null ? "'"+pros.getNombre()+"'" : "null")
                        + ", ECA_PROS_FECINI = "+(pros.getFecIni() != null ? "TO_DATE('"+format.format(pros.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PROS_FECFIN = "+(pros.getFecFin() != null ? "TO_DATE('"+format.format(pros.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PROS_HORAS_JC = "+(pros.getHorasJC() != null ? pros.getHorasJC().toPlainString() : "null")
                        + ", ECA_PROS_HORAS_CONT = "+(pros.getHorasCont() != null ? pros.getHorasCont().toPlainString() : "null")
                        + ", ECA_PROS_HORAS_ECA = "+(pros.getHorasEca() != null ? pros.getHorasEca().toPlainString() : "null")
                        + ", ECA_PROS_IMP_SS_JC = "+(pros.getImpSSJC() != null ? pros.getImpSSJC().toPlainString() : "null")
                        + ", ECA_PROS_IMP_SS_JR = "+(pros.getImpSSJR() != null ? pros.getImpSSJR().toPlainString() : "null")
                        + ", ECA_PROS_IMP_SS_ECA = "+(pros.getImpSSECA() != null ? pros.getImpSSECA().toPlainString() : "null")
                        + ", ECA_PROS_VISITAS = "+(pros.getVisitas() != null ? pros.getVisitas().toString() : "null")
                        + ", ECA_PROS_VISITAS_IMP = "+(pros.getVisitasImp() != null ? pros.getVisitasImp().toPlainString() : "null")
                        + ", ECA_PROS_COSTE = "+(pros.getCoste() != null ? pros.getCoste().toPlainString() : "null")
                        + ", ECA_PROS_IMPTE_CONCEDIDO = "+(pros.getImpteConcedido() != null ? pros.getImpteConcedido().toPlainString() : "null")
                        + ", ECA_PROS_SOLICITUD = "+(pros.getSolicitud() != null ? pros.getSolicitud().toString() : "null")
                        + ", ECA_PROS_TIPO_SUST = "+(pros.getTipoSust() != null ? pros.getTipoSust() : "null")
                        + ", ECA_PROS_CODORIGEN = "+(pros.getSolProspectorOrigen() != null ? pros.getSolProspectorOrigen().toString() : "null")
                        + " where ECA_SOL_PROSPECTORES_COD = "+pros.getSolProspectoresCod();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return pros;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del prospector "+(pros != null ? pros.getNif() : "(prospector = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaProspectorSolicitudVO> getListaProspectoresSolicitud(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaProspectorSolicitudVO> retList = new ArrayList<FilaProspectorSolicitudVO>();
        try
        {
            String query = "select p.*,"
                        + " case when p.ECA_PROS_CODORIGEN IS NULL THEN p.ECA_SOL_PROSPECTORES_COD ELSE p.ECA_PROS_CODORIGEN END AS ORIGEN"
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" p"
                        + " where ECA_PROS_SOLICITUD = "+sol.getSolicitudCod() + " "
                        //+ " order by ECA_PROS_NIF";
                        + " order by ORIGEN, p.ECA_PROS_CODORIGEN NULLS FIRST, ECA_PROS_FECINI, ECA_PROS_FECFIN, ECA_PROS_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaProspectorSolicitudVO fila = null;
            while(rs.next())
            {
                fila = (FilaProspectorSolicitudVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaProspectorSolicitudVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public EcaSolProspectoresVO getProspectorSolicitudPorId(EcaSolProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSolProspectoresVO retPros = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_SOL_PROSPECTORES_COD = "+pros.getSolProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retPros =  (EcaSolProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolProspectoresVO.class);
            }
            return retPros;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                    rs.close();
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarProspectorSolicitud(EcaSolProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_SOL_PROSPECTORES_COD = "+pros.getSolProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando prospector solicitud "+(pros != null ? pros.getSolProspectoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaSolValoracionVO getValoracionSolicitud(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSolValoracionVO valoracion = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_VALORACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_SOL_VAL_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                valoracion = (EcaSolValoracionVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolValoracionVO.class);
            }
            return valoracion;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public EcaSolValoracionVO guardarDatosValoracionSolicitud(EcaSolValoracionVO val, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(val.getSolValoracionCod() == null)
            {
                val.setSolValoracionCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_VALORACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_VALORACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_SOL_VALORACION_COD, ECA_SOL_VAL_SOLICITUD, ECA_SOL_VAL_EXPERIENCIA_NUM, ECA_SOL_VAL_EXPERIENCIA_VAL, ECA_SOL_VAL_INS_MUJERES_NUM, ECA_SOL_VAL_INS_MUJERES_VAL, ECA_SOL_VAL_SENSIBILIDAD_NUM, ECA_SOL_VAL_SENSIBILIDAD_VAL, ECA_SOL_VAL_TOTAL)"
                        + " values("+val.getSolValoracionCod()
                        + ", "+val.getSolicitud()
                        + ", "+(val.getExperienciaNum() != null ? val.getExperienciaNum().toString() : "null")
                        + ", "+(val.getExperienciaVal() != null ? val.getExperienciaVal().toString() : "null")
                        + ", "+(val.getInsMujeresNum() != null ? val.getInsMujeresNum().toString() : "null")
                        + ", "+(val.getInsMujeresVal() != null ? val.getInsMujeresVal().toString() : "null")
                        + ", "+(val.getSensibilidadNum() != null ? val.getSensibilidadNum().toString() : "null")
                        + ", "+(val.getSensibilidadVal() != null ? val.getSensibilidadVal().toString() : "null")
                        + ", "+(val.getTotal() != null ? val.getTotal().toString() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_VALORACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_SOL_VAL_SOLICITUD = "+(val.getSolicitud() != null ? val.getSolicitud().toString() : "null")
                        + ", ECA_SOL_VAL_EXPERIENCIA_NUM = "+(val.getExperienciaNum() != null ? val.getExperienciaNum().toString() : "null")
                        + ", ECA_SOL_VAL_EXPERIENCIA_VAL = "+(val.getExperienciaVal() != null ? val.getExperienciaVal().toString() : "null")
                        + ", ECA_SOL_VAL_INS_MUJERES_NUM = "+(val.getInsMujeresNum() != null ? val.getInsMujeresNum().toString() : "null")
                        + ", ECA_SOL_VAL_INS_MUJERES_VAL = "+(val.getInsMujeresVal() != null ? val.getInsMujeresVal().toString() : "null")
                        + ", ECA_SOL_VAL_SENSIBILIDAD_NUM = "+(val.getSensibilidadNum() != null ? val.getSensibilidadNum().toString() : "null")
                        + ", ECA_SOL_VAL_SENSIBILIDAD_VAL = "+(val.getSensibilidadVal() != null ? val.getSensibilidadVal().toString() : "null")
                        + ", ECA_SOL_VAL_TOTAL = "+(val.getTotal() != null ? val.getTotal().toString() : "null")
                        + " where ECA_SOL_VALORACION_COD = "+val.getSolValoracionCod();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return val;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaConfiguracionVO getConfiguracionEca(int ano, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CONFIGURACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    +" where ANO = "+ano;
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                conf = (EcaConfiguracionVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaConfiguracionVO.class);
            }
            return conf;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public int eliminarTodosPreparadoresSol_Solicitud(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    +" where ECA_PREP_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarTodosProspectoresSol_Solicitud(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    +" where ECA_PROS_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    /*************
     * 
     * PESTA�A JUSTIFICACION
     *  
     ***************/
    
    public EcaJusPreparadoresVO getPreparadorJustificacionPorId(EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaJusPreparadoresVO retPrep = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retPrep =  (EcaJusPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusPreparadoresVO.class);
            }
            return retPrep;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener el preparador justificacion "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaJusPreparadoresVO getPreparadorJustificacionPorNIF(EcaSolicitudVO sol, String nif, Date feInicio, /*String feFin*/ Connection con) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
        if(sol != null && sol.getSolicitudCod() != null && !sol.getSolicitudCod().equals("") && nif != null && !nif.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            EcaJusPreparadoresVO retPrep = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where upper(ECA_PREP_NIF) = '"+nif.toUpperCase()+"' AND ECA_PREP_SOLICITUD="+sol.getSolicitudCod() 
                            //+ " AND ("
                                   //+ "(ECA_PREP_FECINI <= TO_DATE('"+(feInicio != null ? format.format(feInicio) : "00/00/0000")+"', 'dd/mm/yyyy') AND ECA_PREP_FECFIN >= TO_DATE('"+(feInicio != null ? format.format(feInicio) : "00/00/0000")+"', 'dd/mm/yyyy')) " 
                                   //+ " OR (ECA_PREP_FECINICIO <= TO_DATE('"+feFin+"', 'dd/mm/yyyy') AND ECA_PREP_FECFIN >= TO_DATE('"+feInicio+"', 'dd/mm/yyyy')) " 
                                //+ " )"
                            ;
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPrep =  (EcaJusPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusPreparadoresVO.class);
                }
                return retPrep;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el preparador justificacion por nif "+nif , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    public EcaJusPreparadoresVO getPreparadorJustificacionSustituto(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaJusPreparadoresVO retPrep = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PREP_CODORIGEN = "+codPrep 
                            + " order by ECA_PREP_FECINI, ECA_PREP_FECFIN, ECA_PREP_NIF";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPrep =  (EcaJusPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusPreparadoresVO.class);
                }
                return retPrep;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el susutituto del preparador "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    public EcaJusProspectoresVO getProspectorJustificacionSustituto(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaJusProspectoresVO retPros = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PROS_CODORIGEN = "+codPrep 
                            + " order by ECA_PROS_FECINI, ECA_PROS_FECFIN, ECA_PROS_NIF";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPros =  (EcaJusProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusProspectoresVO.class);
                }
                return retPros;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el sustituto del prospector "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    
    public List<FilaPreparadorJustificacionVO> getListaPreparadoresJustificacion(EcaSolicitudVO sol, Integer idioma, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        //String[] datosexp = sol.getNumExp().split("/");
        EcaConfiguracionVO conf = null;
            try
            {
                if(sol.getNumExp() != null)
                {
                    String[] datos = sol.getNumExp().split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                    conf = this.getConfiguracionEca(Integer.parseInt(datos[0]), con);
                }
            }
            catch(Exception ex)
            {  
                log.debug("No existe configuraci�nd e datos para ese a�o");
                
            }
        
        List<FilaPreparadorJustificacionVO> retList = new ArrayList<FilaPreparadorJustificacionVO>();
        try
        {
            String query = "select PJUS.ECA_JUS_PREPARADORES_COD,PJUS.ECA_SOL_PREPARADORES_COD,PJUS.ECA_PREP_NIF,PJUS.ECA_PREP_NOMBRE, PJUS.ECA_PREP_FECINI, PJUS.ECA_PREP_FECFIN, PJUS.ECA_PREP_HORAS_JC, PJUS.ECA_PREP_CODORIGEN,"+
                            " case when PJUS.ECA_PREP_CODORIGEN IS NULL THEN PJUS.ECA_JUS_PREPARADORES_COD ELSE PJUS.ECA_PREP_CODORIGEN END AS ORIGEN, "+
                            " PJUS.ECA_PREP_HORAS_CONT, PJUS.ECA_PREP_HORAS_ECA, PJUS.ECA_PREP_IMP_SS_JC, PJUS.ECA_PREP_IMP_SS_JR, PJUS.ECA_PREP_IMP_SS_ECA,"+
                            " PSOL.ECA_PREP_SEG_ANT, PJUS.ECA_PREP_TIPOCONTRATO, PJUS.ECA_PREP_TIPO_SUST,"+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPOCONTRATO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = PJUS.ECA_PREP_TIPOCONTRATO  ) AS TIPOCONTRATO, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = PJUS.ECA_PREP_TIPOCONTRATO  ) AS TIPOCONTRATO, ")+
                            //"PJUS.ECA_PREP_IMP_SEG_ANT, "+
                            //" PSOL.ECA_PREP_INS_IMPORTE, PSOL.ECA_PREP_INS_SEG_IMPORTE, "+
                            " case when nvl(NUMSEG,0) * "+conf.getImSeguimiento()+" <  nvl(PJUS.ECA_PREP_IMP_SS_ECA,0) * "+conf.getPoMaxSeguimientos() +" then nvl(NUMSEG,0) * "+conf.getImSeguimiento()+
                                " else nvl(PJUS.ECA_PREP_IMP_SS_ECA,0) * "+conf.getPoMaxSeguimientos()+" end as IMPORTESEGANT, "+
                            " PJUS.ECA_PREP_IMP_SS_ECA AS COSTE,  PJUS.ECA_PREP_SOLICITUD, "+
                            " PSOL.ECA_PREP_INS_C1_H, PSOL.ECA_PREP_INS_C1_M, PSOL.ECA_PREP_INS_C1, "+
                            " PSOL.ECA_PREP_INS_C2_H, PSOL.ECA_PREP_INS_C2_M, PSOL.ECA_PREP_INS_C2, "+
                            " PSOL.ECA_PREP_INS_C3_H, PSOL.ECA_PREP_INS_C3_M, PSOL.ECA_PREP_INS_C3, "+
                            " C1H_JUS, C1M_JUS, C1_JUS, C2H_JUS, C2M_JUS, C2_JUS, C3H_JUS, C3M_JUS, C3_JUS, C4H_JUS, C4M_JUS, C4_JUS, C5H_JUS, C5M_JUS, C5_JUS, C6H_JUS, C6M_JUS, C6_JUS, NUMSEG,"+
                            " PSOL.ECA_PREP_INS_C4_H, PSOL.ECA_PREP_INS_C4_M, PSOL.ECA_PREP_INS_C4, "+  
                    " PSOL.ECA_PREP_INS_C5_H, PSOL.ECA_PREP_INS_C5_M, PSOL.ECA_PREP_INS_C5, "+  
                    " PSOL.ECA_PREP_INS_C6_H, PSOL.ECA_PREP_INS_C6_M, PSOL.ECA_PREP_INS_C6, "+  
                            " (C1H_JUS * "+conf.getImC1h()+") + (C1M_JUS * "+conf.getImC1m()+")"+" + (C2H_JUS * "+conf.getImC2h()+") + (C2M_JUS * "+conf.getImC2m()+") + "+
                            " (C3H_JUS * "+conf.getImC3h()+") + (C3M_JUS * "+conf.getImC3m()+")"+" + (C4H_JUS * "+conf.getImC4h()+") + (C4M_JUS * "+conf.getImC4m()+") "+" + (C5H_JUS * "+conf.getImC5h()+") + (C5M_JUS * "+conf.getImC5m()+") "+" + (C6H_JUS * "+conf.getImC6h()+") + (C6M_JUS * "+conf.getImC6m()+") "+"  AS IMPINSERCIONES "+
                                        
                            " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " PJUS "
                        + " left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " PSOL "
                            + " on PSOL.ECA_PREP_SOLICITUD = PJUS.ECA_PREP_SOLICITUD and PSOL.ECA_SOL_PREPARADORES_COD = PJUS.ECA_SOL_PREPARADORES_COD "
                        + " left join (SELECT eca_jus_preparadores_cod, SUM(c1h) as C1H_JUS, SUM(c1m) as C1M_JUS, SUM(c1) as C1_JUS, "+
                                        "SUM(c2h) as C2H_JUS, SUM(c2m) as C2M_JUS, SUM(c2) as C2_JUS, SUM(c3h) as C3H_JUS, SUM(c3m) as C3M_JUS, SUM(c3) as C3_JUS,"+
                                        "SUM(c4h) as C4H_JUS, SUM(c4m) as C4M_JUS, SUM(c4) AS C4_JUS, SUM(c5h) as C5H_JUS, SUM(c5m) as C5M_JUS, SUM(c5) AS C5_JUS,SUM(c6h) as C6H_JUS, SUM(c6m) as C6M_JUS, SUM(c6) AS C6_JUS, SUM(seguimientos) AS NUMSEG "+
                                        "FROM ( "+
                                            "select eca_jus_preparadores_cod, "+
                                            /*"case when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 and eca_seg_tipo = 1  then round(eca_seg_porcjornada/100,2) else 0 end as c1h,"+
                                            "case when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c1m,"+
                                            "case when eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c1,"+
                                            "case when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c2h,"+ 
                                            "case when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c2m,"+
                                            "case when eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c2,"+
                                            "case when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 ))  and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2)  else 0 end as c3h,"+
                                            "case when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 ))  and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2)  else 0 end as c3m, "+
                                            "case when  ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 ))  and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c3,"+
                                            "case when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 )) and months_between(eca_seg_fecfin,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c4h,"+
                                            "case when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 )) and months_between(eca_seg_fecfin,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c4m,"+
                                            "case when  ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 )) and months_between(eca_seg_fecfin,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,2) else 0 end as c4, "+
                                            "case when  to_char(ECA_SEG_FECINI,'YYYY') < '"+datosexp[0]+"' and eca_seg_tipo = 0  then round(eca_seg_porcjornada/100,2) else 0 end as seguimientos "+*/
                                            "case when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then round(eca_seg_porcjornada/100,4) else 0 end as c1h,"+
                                            "case when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c1m,"+
                                            "case when (eca_seg_sexo=0 or eca_seg_sexo=1) and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c1,"+
                                            "case when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c2h,"+ 
                                            "case when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c2m,"+
                                            "case when (eca_seg_sexo=0 or eca_seg_sexo=1) and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c2,"+
                                            "case when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4)  else 0 end as c3h,"+
                                            "case when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4)  else 0 end as c3m, "+
                                            "case when (eca_seg_sexo=0 or eca_seg_sexo=1) and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c3,"+
                                            "case when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c4h,"+
                                            "case when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c4m,"+
                                            "case when (eca_seg_sexo=0 or eca_seg_sexo=1) and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c4, "+
 "case when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c5h,"+
                                            "case when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c5m,"+
                                            "case when (eca_seg_sexo=0 or eca_seg_sexo=1) and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c5, "+
                     "case when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c6h,"+
                                            "case when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c6m,"+
                                            "case when (eca_seg_sexo=0 or eca_seg_sexo=1) and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then round(eca_seg_porcjornada/100,4) else 0 end as c6, "+
                    


//"case when to_char(ECA_SEG_FECINI,'YYYY') < '"+datosexp[0]+"' and eca_seg_tipo = 0  then round(eca_seg_porcjornada/100,4) else 0 end as seguimientos "+
                                            //"case when to_char(ECA_SEG_FECINI,'YYYY') < '"+datosexp[0]+"' and eca_seg_tipo = 0  then 1 else 1 end as seguimientos "+
                                            
                                            /*
                                             * 17-03-2014 NOTA: 
                                             * La linea anterios se comenta para que la query devuelva todos los seguimientos del preparador ya que luego por
                                             * codigo se quitaran los que contengan errores. El hecho de que la fecha de inicio de un seguimiento no sea < que el a�o del expediente
                                             * ya es un error por tanto estos seguimientos se estaban quitando 2 veces.
                                             */
                                            "case when eca_seg_tipo = 0  then 1 else 0 end as seguimientos "+
                                            " from eca14_seg_preparadores "+                                           
                                        ") GROUP BY eca_jus_preparadores_cod "+   
                            ") colectivos on colectivos.eca_jus_preparadores_cod=PJUS.eca_jus_preparadores_cod "
                        + " where PJUS.ECA_PREP_SOLICITUD = "+sol.getSolicitudCod()
                        + " order by ORIGEN, PJUS.ECA_PREP_CODORIGEN NULLS FIRST, ECA_PREP_FECINI, ECA_PREP_FECFIN, ECA_PREP_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaPreparadorJustificacionVO fila = null;
            while(rs.next())
            {
                fila = (FilaPreparadorJustificacionVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaPreparadorJustificacionVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public EcaJusPreparadoresVO guardarEcaJusPreparadoresVO(EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(prep.getJusPreparadoresCod() == null)
            {
                prep.setJusPreparadoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_JUS_PREPARADORES_COD, ECA_SOL_PREPARADORES_COD, ECA_PREP_NIF, ECA_PREP_NOMBRE, ECA_PREP_FECINI, ECA_PREP_FECFIN, ECA_PREP_HORAS_JC, ECA_PREP_HORAS_CONT, ECA_PREP_HORAS_ECA, ECA_PREP_IMP_SS_JC, ECA_PREP_IMP_SS_JR, ECA_PREP_IMP_SS_ECA, ECA_PREP_SOLICITUD, "
                        + "ECA_PREP_TIPOCONTRATO, ECA_PREP_CODORIGEN, ECA_PREP_TIPO_SUST)"
                        + " values("+prep.getJusPreparadoresCod()
                        + ", "+(prep.getSolPreparadoresCod() != null ? prep.getSolPreparadoresCod().toString() : "null")
                        + ", "+(prep.getNif() != null ? "'"+prep.getNif()+"'" : "null")
                        + ", "+(prep.getNombre() != null ? "'"+prep.getNombre()+"'" : "null")
                        + ", "+(prep.getFecIni() != null ? "TO_DATE('"+format.format(prep.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(prep.getFecFin() != null ? "TO_DATE('"+format.format(prep.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(prep.getHorasJC() != null ? prep.getHorasJC().toPlainString() : "null")
                        + ", "+(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString() : "null")
                        + ", "+(prep.getHorasEca() != null ? prep.getHorasEca().toPlainString() : "null")
                        + ", "+(prep.getImpSSJC() != null ? prep.getImpSSJC().toPlainString() : "null")
                        + ", "+(prep.getImpSSJR() != null ? prep.getImpSSJR().toPlainString() : "null")
                        + ", "+(prep.getImpSSECA() != null ? prep.getImpSSECA().toPlainString() : "null")
                        + ", "+(prep.getSolicitud() != null ? prep.getSolicitud().toString() : "null")
                        + ", "+(prep.getTipoContrato() != null ? prep.getTipoContrato() : "null")
                        + ", "+(prep.getJusPreparadorOrigen() != null ? prep.getJusPreparadorOrigen() : "null")
                        + ", "+(prep.getTipoSust() != null ? prep.getTipoSust() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_PREP_NIF = "+(prep.getNif() != null ? "'"+prep.getNif()+"'" : "null")
                        + ", ECA_PREP_NOMBRE = "+(prep.getNombre() != null ? "'"+prep.getNombre()+"'" : "null")
                        + ", ECA_PREP_FECINI = "+(prep.getFecIni() != null ? "TO_DATE('"+format.format(prep.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PREP_FECFIN = "+(prep.getFecFin() != null ? "TO_DATE('"+format.format(prep.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PREP_HORAS_JC = "+(prep.getHorasJC() != null ? prep.getHorasJC().toPlainString() : "null")
                        + ", ECA_PREP_HORAS_CONT = "+(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString() : "null")
                        + ", ECA_PREP_HORAS_ECA = "+(prep.getHorasEca() != null ? prep.getHorasEca().toPlainString() : "null")
                        + ", ECA_PREP_IMP_SS_JC = "+(prep.getImpSSJC() != null ? prep.getImpSSJC().toPlainString() : "null")
                        + ", ECA_PREP_IMP_SS_JR = "+(prep.getImpSSJR() != null ? prep.getImpSSJR().toPlainString() : "null")
                        + ", ECA_PREP_IMP_SS_ECA = "+(prep.getImpSSECA() != null ? prep.getImpSSECA().toPlainString() : "null")
                        + ", ECA_PREP_SOLICITUD = "+(prep.getSolicitud() != null ? prep.getSolicitud().toString() : "null")
                        + ", ECA_SOL_PREPARADORES_COD = "+(prep.getSolPreparadoresCod() != null ? prep.getSolPreparadoresCod().toString() : "null")
                        + ", ECA_PREP_TIPOCONTRATO = "+(prep.getTipoContrato() != null ? prep.getTipoContrato() : "null")
                        + ", ECA_PREP_CODORIGEN="+(prep.getJusPreparadorOrigen() != null ? prep.getJusPreparadorOrigen() : "null")
                        + ", ECA_PREP_TIPO_SUST = "+(prep.getTipoSust() != null ? prep.getTipoSust() : "null")
                        + " where ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return prep;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del preparador "+(prep != null ? prep.getNif() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
     /*public EcaJusPreparadoresVO actualizarSustituto(String idPrepOrigen, EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_PREP_CODSUSTITUTO = "+(prep.getJusPreparadoresCod() != null ? "'"+prep.getJusPreparadoresCod()+"'" : "null")
                        + " where ECA_JUS_PREPARADORES_COD = "+idPrepOrigen;
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return prep;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del preparador "+(prep != null ? prep.getNif() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }*/
    
    public int eliminarPreparadorJustificacion(EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando preparador justificacion "+(prep != null ? prep.getJusPreparadoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public Integer obtenerSiguienteCodSeguimiento(Integer codPrep, Connection con) {
        Integer num =0;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = "SELECT MAX(ECA_SEG_PREPARADORES_COD) "+
                            " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+
                            " WHERE ECA_JUS_PREPARADORES_COD = "+codPrep;
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()){
                num = rs.getInt(1);
                if(rs.wasNull())
                {
                    //throw new Exception();
                    num=0;
                }
            }
            num=num+1;
        }catch(Exception e){
            log.error("Se ha producido un error al intentar obtener la secuencia del seguimiento",e);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(rs != null)
                    rs.close();
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }
        }
        return num;
    }
     
    public EcaSegPreparadoresVO guardarEcaSegPreparadoresVO(EcaSegPreparadoresVO seg, Connection con) throws Exception
    {
        Statement st = null;
        String query = null;
        try
        {            
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(seg.getSegPreparadoresCod() == null)
            {
                if(log.isDebugEnabled()) log.debug("INSERT");
                //cambiar clave primaria son dos campos
                //seg.setSegPreparadoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                seg.setSegPreparadoresCod(obtenerSiguienteCodSeguimiento(seg.getJusPreparadoresCod(), con));
                //Es un seguimiento nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " (ECA_SEG_PREPARADORES_COD, ECA_JUS_PREPARADORES_COD, ECA_SEG_TIPO, ECA_SEG_NIF, ECA_SEG_NOMBRE, ECA_SEG_FECNACIMIENTO, "
                        + "ECA_SEG_SEXO, ECA_SEG_TIPODISCAPACIDAD, ECA_SEG_DISCGRAVEDAD, ECA_SEG_TIPOCONTRATO, ECA_SEG_PORCJORNADA,  ECA_SEG_FECINI, "
                        + "ECA_SEG_FECFIN, ECA_SEG_EMPRESA, ECA_SEG_HORAS, ECA_SEG_FECSEGUIMIENTO,  ECA_SEG_OBSERVACIONES" //ECA_SEG_PERSCONTACTO,
                        + " ,ECA_SEG_FINCONTRATO"
                        +")"
                        + " values("+seg.getSegPreparadoresCod()
                        + ", "+(seg.getJusPreparadoresCod() != null ? seg.getJusPreparadoresCod() : "null")
                        + ", "+(seg.getTipo() != null ? seg.getTipo() : "null")
                        + ", "+(seg.getNif() != null ? "'"+seg.getNif()+"'" : "null")
                        + ", "+(seg.getNombre() != null ? "'"+seg.getNombre()+"'" : "null")
                        + ", "+(seg.getFecNacimiento() != null ? "TO_DATE('"+format.format(seg.getFecNacimiento())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(seg.getSexo() != null ? +seg.getSexo()+"" : "null")
                        + ", "+(seg.getTipoDiscapacidad() != null ? seg.getTipoDiscapacidad() : "null")
                        + ", "+(seg.getGravedad() != null ? seg.getGravedad(): "null")
                        + ", "+(seg.getTipoContrato() != null ? seg.getTipoContrato() : "null")
                        + ", "+(seg.getPorcJornada() != null ? seg.getPorcJornada().toPlainString() : "null")
                        + ", "+(seg.getFecIni() != null ? "TO_DATE('"+format.format(seg.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(seg.getFecFin() != null ? "TO_DATE('"+format.format(seg.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(seg.getEmpresa() != null ? "'"+seg.getEmpresa()+"'" : "null")
                        + ", "+(seg.getHorasCont() != null ? seg.getHorasCont().toPlainString() : "null")        
                        + ", "+(seg.getFecSeguimiento() != null ? "TO_DATE('"+format.format(seg.getFecSeguimiento())+"', 'dd/mm/yyyy')" : "null")
                       // + ", "+(seg.getNomPersContacto() != null ? "'"+seg.getNomPersContacto()+"'" : "null")
                        + ", "+(seg.getObservaciones() != null ? "'"+seg.getObservaciones()+"'" : "null")
                        + ", "+(seg.getFinContratoDespido() != null ? seg.getFinContratoDespido() : "null")
                        + ")";
                        
            }
            else
            {
                if(log.isDebugEnabled()) log.debug("UPDATE");
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "  set "
                        + " ECA_SEG_NIF = "+(seg.getNif() != null ? "'"+seg.getNif()+"'" : "null")
                        + ", ECA_SEG_NOMBRE = "+(seg.getNombre() != null ? "'"+seg.getNombre()+"'" : "null")
                        + ", ECA_SEG_FECNACIMIENTO = "+(seg.getFecNacimiento() != null ? "TO_DATE('"+format.format(seg.getFecNacimiento())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_SEG_SEXO = "+(seg.getSexo() != null ? +seg.getSexo(): "null")
                        + ", ECA_SEG_TIPODISCAPACIDAD = "+(seg.getTipoDiscapacidad() != null ? seg.getTipoDiscapacidad() : "null")
                        + ", ECA_SEG_DISCGRAVEDAD = "+(seg.getGravedad() != null ? seg.getGravedad() : "null")
                        + ", ECA_SEG_TIPOCONTRATO = "+(seg.getTipoContrato() != null ? seg.getTipoContrato() : "null")  
                        + ", ECA_SEG_PORCJORNADA = "+(seg.getPorcJornada() != null ? seg.getPorcJornada().toPlainString() : "null")
                        + ", ECA_SEG_FECINI = "+(seg.getFecIni() != null ? "TO_DATE('"+format.format(seg.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_SEG_FECFIN = "+(seg.getFecFin() != null ? "TO_DATE('"+format.format(seg.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_SEG_EMPRESA = "+(seg.getEmpresa() != null ? "'"+seg.getEmpresa()+"'" : "null")
                        + ", ECA_SEG_HORAS = "+(seg.getHorasCont() != null ? seg.getHorasCont().toPlainString() : "null")
                        + ", ECA_SEG_FECSEGUIMIENTO = "+(seg.getFecSeguimiento() != null ? "TO_DATE('"+format.format(seg.getFecSeguimiento())+"', 'dd/mm/yyyy')" : "null")
                      //  + ", ECA_SEG_PERSCONTACTO = "+(seg.getNomPersContacto() != null ? "'"+seg.getNomPersContacto()+"'" : "null")
                        + ", ECA_SEG_OBSERVACIONES = "+(seg.getObservaciones() != null ? "'"+seg.getObservaciones()+"'" : "null")
                        + ", ECA_SEG_FINCONTRATO = "+(seg.getFinContratoDespido() != null ? seg.getFinContratoDespido() : "null")
                        + " WHERE ECA_JUS_PREPARADORES_COD = "+(seg.getJusPreparadoresCod() != null ? seg.getJusPreparadoresCod() : "null")  
                        + " and ECA_SEG_PREPARADORES_COD = "+(seg.getSegPreparadoresCod() != null ? seg.getSegPreparadoresCod() : "null");                          
                                              
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return seg;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del seguimiento "+(seg != null ? seg.getNif() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    } 
    
     public EcaSegPreparadores2016VO guardarEcaSegPreparadoresVO_2016(EcaSegPreparadores2016VO seg, Connection con) throws Exception
    {
        Statement st = null;
        String query = null;
        try
        {            
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(seg.getSegPreparadoresCod() == null)
            {
                if(log.isDebugEnabled()) log.debug("INSERT");
                //cambiar clave primaria son dos campos
                //seg.setSegPreparadoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                seg.setSegPreparadoresCod(obtenerSiguienteCodSeguimiento(seg.getJusPreparadoresCod(), con));
                //Es un seguimiento nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " (ECA_SEG_PREPARADORES_COD, ECA_JUS_PREPARADORES_COD, ECA_SEG_TIPO, ECA_SEG_NIF, ECA_SEG_NOMBRE, ECA_SEG_FECNACIMIENTO, "
                        + "ECA_SEG_SEXO, ECA_SEG_TIPODISCAPACIDAD, ECA_SEG_DISCGRAVEDAD, ECA_SEG_TIPOCONTRATO, ECA_SEG_PORCJORNADA,  ECA_SEG_FECINI, "
                        + "ECA_SEG_FECFIN, ECA_SEG_EMPRESA, ECA_SEG_HORAS, ECA_SEG_FECSEGUIMIENTO,  ECA_SEG_OBSERVACIONES" //ECA_SEG_PERSCONTACTO,
                        + " ,ECA_SEG_FINCONTRATO"
                        +")"
                        + " values("+seg.getSegPreparadoresCod()
                        + ", "+(seg.getJusPreparadoresCod() != null ? seg.getJusPreparadoresCod() : "null")
                        + ", "+(seg.getTipo() != null ? seg.getTipo() : "null")
                        + ", "+(seg.getNif() != null ? "'"+seg.getNif()+"'" : "null")
                        + ", "+(seg.getNombre() != null ? "'"+seg.getNombre()+"'" : "null")
                        + ", "+(seg.getFecNacimiento() != null ? "TO_DATE('"+format.format(seg.getFecNacimiento())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(seg.getSexo() != null ? +seg.getSexo()+"" : "null")
                        + ", "+(seg.getTipoDiscapacidad() != null ? seg.getTipoDiscapacidad() : "null")
                        + ", "+(seg.getGravedad() != null ? seg.getGravedad(): "null")
                        + ", "+(seg.getTipoContrato() != null ? seg.getTipoContrato() : "null")
                        + ", "+(seg.getPorcJornada() != null ? seg.getPorcJornada().toPlainString() : "null")
                        + ", "+(seg.getFecIni() != null ? "TO_DATE('"+format.format(seg.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(seg.getFecFin() != null ? "TO_DATE('"+format.format(seg.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(seg.getEmpresa() != null ? "'"+seg.getEmpresa()+"'" : "null")
                        + ", "+(seg.getHorasCont() != null ? seg.getHorasCont().toPlainString() : "null")        
                        + ", "+(seg.getFecSeguimiento() != null ? "TO_DATE('"+format.format(seg.getFecSeguimiento())+"', 'dd/mm/yyyy')" : "null")
                       // + ", "+(seg.getNomPersContacto() != null ? "'"+seg.getNomPersContacto()+"'" : "null")
                        + ", "+(seg.getObservaciones() != null ? "'"+seg.getObservaciones()+"'" : "null")
                        + ", "+(seg.getFinContratoDespido() != null ? seg.getFinContratoDespido() : "null")
                        + ")";
                        
            }
            else
            {
                if(log.isDebugEnabled()) log.debug("UPDATE");
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "  set "
                        + " ECA_SEG_NIF = "+(seg.getNif() != null ? "'"+seg.getNif()+"'" : "null")
                        + ", ECA_SEG_NOMBRE = "+(seg.getNombre() != null ? "'"+seg.getNombre()+"'" : "null")
                        + ", ECA_SEG_FECNACIMIENTO = "+(seg.getFecNacimiento() != null ? "TO_DATE('"+format.format(seg.getFecNacimiento())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_SEG_SEXO = "+(seg.getSexo() != null ? +seg.getSexo(): "null")
                        + ", ECA_SEG_TIPODISCAPACIDAD = "+(seg.getTipoDiscapacidad() != null ? seg.getTipoDiscapacidad() : "null")
                        + ", ECA_SEG_DISCGRAVEDAD = "+(seg.getGravedad() != null ? seg.getGravedad() : "null")
                        + ", ECA_SEG_TIPOCONTRATO = "+(seg.getTipoContrato() != null ? seg.getTipoContrato() : "null")  
                        + ", ECA_SEG_PORCJORNADA = "+(seg.getPorcJornada() != null ? seg.getPorcJornada().toPlainString() : "null")
                        + ", ECA_SEG_FECINI = "+(seg.getFecIni() != null ? "TO_DATE('"+format.format(seg.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_SEG_FECFIN = "+(seg.getFecFin() != null ? "TO_DATE('"+format.format(seg.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_SEG_EMPRESA = "+(seg.getEmpresa() != null ? "'"+seg.getEmpresa()+"'" : "null")
                        + ", ECA_SEG_HORAS = "+(seg.getHorasCont() != null ? seg.getHorasCont().toPlainString() : "null")
                        + ", ECA_SEG_FECSEGUIMIENTO = "+(seg.getFecSeguimiento() != null ? "TO_DATE('"+format.format(seg.getFecSeguimiento())+"', 'dd/mm/yyyy')" : "null")
                      //  + ", ECA_SEG_PERSCONTACTO = "+(seg.getNomPersContacto() != null ? "'"+seg.getNomPersContacto()+"'" : "null")
                        + ", ECA_SEG_OBSERVACIONES = "+(seg.getObservaciones() != null ? "'"+seg.getObservaciones()+"'" : "null")
                        + ", ECA_SEG_FINCONTRATO = "+(seg.getFinContratoDespido() != null ? seg.getFinContratoDespido() : "null")
                        + " WHERE ECA_JUS_PREPARADORES_COD = "+(seg.getJusPreparadoresCod() != null ? seg.getJusPreparadoresCod() : "null")  
                        + " and ECA_SEG_PREPARADORES_COD = "+(seg.getSegPreparadoresCod() != null ? seg.getSegPreparadoresCod() : "null");                          
                                              
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return seg;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del seguimiento "+(seg != null ? seg.getNif() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    } 
     
    public List<FilaSegPreparadoresVO> getListaSeguimientos(EcaSolicitudVO sol, EcaJusPreparadoresVO prep, String tipo, String colectivo, String sexo, Integer idioma, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaSegPreparadoresVO> retList = new ArrayList<FilaSegPreparadoresVO>();
        try
        {
            String query = "select ECA_SEG_PREPARADORES_COD, B.ECA_JUS_PREPARADORES_COD, ECA_SEG_TIPO, ECA_PREP_NIF, ECA_SEG_TIPODISCAPACIDAD,"+
                            " ECA_SEG_NIF, ECA_SEG_NOMBRE, ECA_SEG_FECNACIMIENTO, ECA_SEG_SEXO, ECA_SEG_TIPODISCAPACIDAD,  "+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPODISCAPACIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TDISCAPACIDAD = ECA_SEG_TIPODISCAPACIDAD  ) AS TDISCAPACIDAD, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPODISCAPACIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TDISCAPACIDAD = ECA_SEG_TIPODISCAPACIDAD  ) AS TDISCAPACIDAD, ")+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_GRAVEDAD = ECA_SEG_DISCGRAVEDAD  ) AS DISCGRAVEDAD, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_GRAVEDAD = ECA_SEG_DISCGRAVEDAD  ) AS DISCGRAVEDAD, ")+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPOCONTRATO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = ECA_SEG_TIPOCONTRATO  ) AS TIPOCONTRATO, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = ECA_SEG_TIPOCONTRATO  ) AS TIPOCONTRATO, ")+
                            " ECA_SEG_DISCGRAVEDAD, ECA_SEG_TIPOCONTRATO, ECA_SEG_PORCJORNADA, ECA_SEG_FECINI, ECA_SEG_FECFIN, ECA_SEG_EMPRESA, ECA_SEG_HORAS, "+
                            " ECA_SEG_FECSEGUIMIENTO,  ECA_SEG_OBSERVACIONES "+ //ECA_SEG_PERSCONTACTO,
                            " , ECA_SEG_FINCONTRATO " +
                            " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" A "+ 
                            " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" B "+
                                " ON B.ECA_JUS_PREPARADORES_COD = A.ECA_JUS_PREPARADORES_COD "
                        + " where ECA_SEG_TIPO="+tipo +
                         " and ECA_PREP_SOLICITUD ="+sol.getSolicitudCod() + 
                            (prep!=null?
                            " and A.ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod():"")+
                                (sexo.equals("")?"":" and ECA_SEG_SEXO="+sexo)+" ";
            //FALTA FILTAR POR COLECTIVOS            
             /*if (!colectivo.equals("")){
                 if (colectivo.equals("1"))
                    query += " and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 ";
                 else if (colectivo.equals("2"))
                    query += " and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini)<12 ";
                 else if (colectivo.equals("3"))
                    query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 ))  and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 ";
                 else if (colectivo.equals("4"))
                     query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 )) and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini) <12";
             }   */       
             if (!colectivo.equals("")){
                 if (colectivo.equals("1"))
                    query += " and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 ";
                 else if (colectivo.equals("2"))
                    query += " and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 ";
                 else if (colectivo.equals("3"))
                    query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad = 3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null)";
                     //query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad = 3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad = 2 ))  and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 ";
                 else if (colectivo.equals("4"))
                     query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad = 3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3))) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini) <12";
             }                                      
                    query = query+ " order by ECA_SEG_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaSegPreparadoresVO fila = null;
            while(rs.next())
            {
                fila = (FilaSegPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaSegPreparadoresVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public List<FilaInsPreparadoresVO> getListaInserciones(EcaSolicitudVO sol, EcaJusPreparadoresVO prep, String tipo, String colectivo, String sexo, Integer idioma, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaInsPreparadoresVO> retList = new ArrayList<FilaInsPreparadoresVO>();
        try
        {
            String query = "select ECA_SEG_PREPARADORES_COD, B.ECA_JUS_PREPARADORES_COD, ECA_SEG_TIPO, ECA_PREP_NIF, ECA_SEG_TIPODISCAPACIDAD,"+
                            " ECA_SEG_NIF, ECA_SEG_NOMBRE, ECA_SEG_FECNACIMIENTO, ECA_SEG_SEXO, ECA_SEG_TIPODISCAPACIDAD,  "
                    
                    +" case"
                    +" when (eca_seg_sexo=0 or eca_seg_sexo=1) and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3)"
                    +" and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then 'C1'"

                    +" when (eca_seg_sexo=0 or eca_seg_sexo=1) and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3)"
                    +" and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then 'C2'"


                    +" when (eca_seg_sexo=0 or eca_seg_sexo=1) and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))"
                    +" and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then 'C3'"


                    +" when (eca_seg_sexo=0 or eca_seg_sexo=1) and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))"
                    +" and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then 'C4'"

                    +" else null"

                    +" end as COLECTIVO, "+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPODISCAPACIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TDISCAPACIDAD = ECA_SEG_TIPODISCAPACIDAD  ) AS TDISCAPACIDAD, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPODISCAPACIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TDISCAPACIDAD = ECA_SEG_TIPODISCAPACIDAD  ) AS TDISCAPACIDAD, ")+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_GRAVEDAD = ECA_SEG_DISCGRAVEDAD  ) AS DISCGRAVEDAD, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_GRAVEDAD = ECA_SEG_DISCGRAVEDAD  ) AS DISCGRAVEDAD, ")+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPOCONTRATO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = ECA_SEG_TIPOCONTRATO  ) AS TIPOCONTRATO, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = ECA_SEG_TIPOCONTRATO  ) AS TIPOCONTRATO, ")+
                            " ECA_SEG_DISCGRAVEDAD, ECA_SEG_TIPOCONTRATO, ECA_SEG_PORCJORNADA, ECA_SEG_FECINI, ECA_SEG_FECFIN, ECA_SEG_EMPRESA, ECA_SEG_HORAS, "+
                            " ECA_SEG_FECSEGUIMIENTO, ECA_SEG_PERSCONTACTO, ECA_SEG_OBSERVACIONES "+
                            " , ECA_SEG_FINCONTRATO " +
                            " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" A "+ 
                            " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" B "+
                                " ON B.ECA_JUS_PREPARADORES_COD = A.ECA_JUS_PREPARADORES_COD "
                        + " where ECA_SEG_TIPO="+tipo +
                         " and ECA_PREP_SOLICITUD ="+sol.getSolicitudCod() + 
                            (prep!=null?
                            " and A.ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod():"")+
                                (sexo.equals("")?"":" and ECA_SEG_SEXO="+sexo)+" ";
            //FALTA FILTAR POR COLECTIVOS            
             /*if (!colectivo.equals("")){
                 if (colectivo.equals("1"))
                    query += " and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 ";
                 else if (colectivo.equals("2"))
                    query += " and eca_seg_tipodiscapacidad = 1 and eca_seg_discgravedad = 2 and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini)<12 ";
                 else if (colectivo.equals("3"))
                    query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 ))  and months_between(eca_seg_fecfin,eca_seg_fecini) >=12 ";
                 else if (colectivo.equals("4"))
                     query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad =2 )) and months_between(eca_seg_fecfin,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin,eca_seg_fecini) <12";
             }   */       
             if (!colectivo.equals("")){
                 if (colectivo.equals("1"))
                    query += " and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) ";
                 else if (colectivo.equals("2"))
                    query += " and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 ";
                 else if (colectivo.equals("3"))
                    query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad = 3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null)";
                 else if (colectivo.equals("4"))
                     query += " and ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad = 3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3))) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini) <12";
                 else query += " and not ("
                            + " (eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6) "
                            + " or (( (eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad = 3 ) or (eca_seg_tipodiscapacidad = 3 and eca_seg_discgravedad = 2 ))  and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 )"
                         + ")";
             }                                      
                    query = query+ " order by ECA_SEG_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaInsPreparadoresVO fila = null;
            while(rs.next())
            {
                fila = (FilaInsPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaInsPreparadoresVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public List<SelectItem> getListaTipoContrato( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();
        try{
            String query = "select COD_TIPOCONTRATO, UPPER(NOMBRE_"+(idioma==1?"C":"E")+") as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPOCONTRATO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"  ";

             if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                /*SelectItem itemVacio = new SelectItem();
                itemVacio.setId("");
                itemVacio.setLabel("");
                lista.add(itemVacio);*/
                SelectItem si = null;
                Integer id = null;
                String nombre = null;
                while(rs.next())
                {
                    id = rs.getInt("COD_TIPOCONTRATO");
                    if(!rs.wasNull())
                    {
                        nombre = rs.getString("NOMBRE");
                        si = new SelectItem();
                        si.setId(id);
                        si.setLabel(nombre);
                        lista.add(si);
                    }
                }
           return lista;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener TipoContrato", ex);
            throw new Exception(ex);
        }
        finally
        {          
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) {
                st.close();
            }
            if(rs!=null){               
                   rs.close();              
            }           
        }       
    }

    public List<SelectItem> getListaTipodiscapacidad( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList();        
        try{
            String query = "select COD_TDISCAPACIDAD, UPPER(NOMBRE_"+(idioma==1?"C":"E")+") as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPODISCAPACIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"  ";

             if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                SelectItem si = null;/*new SelectItem();
                si.setId("");
                si.setLabel("");
                lista.add(si);*/
                Integer id = null;
                String nombre = null;
                while(rs.next())
                {
                    id = rs.getInt("COD_TDISCAPACIDAD");                
                    if(!rs.wasNull())
                    {
                        nombre = rs.getString("NOMBRE");
                        si = new SelectItem();
                        si.setId(id);
                        si.setLabel(nombre);
                        lista.add(si);               
                    }
                }
           return lista;
       }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener Tipodiscapacidad", ex);
            throw new Exception(ex);
        }
        finally
        {          
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) {
                st.close();
            }
            if(rs!=null){               
                   rs.close();              
            }           
        }
    }
    
    public List<SelectItem> getListaGravedad( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        ArrayList lista = new ArrayList(); 
        try{
            String query = "select COD_GRAVEDAD, UPPER(NOMBRE_"+(idioma==1?"C":"E")+") as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"  ";
            if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                SelectItem si = null;/*new SelectItem();
                si.setId("");
                si.setLabel("");
                lista.add(si);*/
                Integer id = null;
                String nombre = null;
                while(rs.next())
                {
                    id = rs.getInt("COD_GRAVEDAD");                
                    if(!rs.wasNull())
                    {
                        nombre = rs.getString("NOMBRE");
                        si = new SelectItem();
                        si.setId(id);
                        si.setLabel(nombre);
                        lista.add(si);               
                    }
                }
           return lista;
       }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener Gravedad", ex);
            throw new Exception(ex);
        }
        finally
        {          
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) {
                st.close();
            }
            if(rs!=null){               
                   rs.close();              
            }           
        }
    }
    
    
    
    public List<FilaAuditoriaProcesosVO> filtrarAuditoriaProcesos(String nomApellidos, Date feDesde, Date feHasta, Integer codProc, int codIdioma, Connection con) throws Exception
    {
        List<FilaAuditoriaProcesosVO> retList = new ArrayList<FilaAuditoriaProcesosVO>();
            
        Statement st = null;
        ResultSet rs = null;
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String query = null;
            boolean tieneWhere = false;
            query = "select au.*, u.usu_nom from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_AUDITORIA, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" au"
                   +" left join "+ GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_USUARIOS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" u"
                   +" on au.usu_cod = u.usu_cod";
            
            if(nomApellidos != null && !nomApellidos.equalsIgnoreCase(""))
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                query += " UPPER(u.usu_nom) like '%"+nomApellidos.toUpperCase()+"%'";
            }
            
            if(feDesde != null && feHasta != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.fec_ejecucion between to_timestamp('"+format.format(feDesde)+"', 'dd/mm/yyyy hh24:mi:ss') and to_timestamp('"+format.format(feHasta)+"', 'dd/mm/yyyy hh24:mi:ss')";
            }
            else if(feDesde != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.fec_ejecucion >= to_timestamp('"+format.format(feDesde)+"', 'dd/mm/yyyy hh24:mi:ss')";
            }
            else if(feHasta != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.fec_ejecucion <= to_timestamp('"+format.format(feHasta)+"', 'dd/mm/yyyy hh24:mi:ss')";
            }
            
            if(codProc != null)
            {
                if(!tieneWhere)
                {
                    query += " where";
                    tieneWhere = true;
                }
                else
                {
                    query += " and";
                }
                query += " au.proceso = "+codProc.toString();
            }
            
            query += " order by au.fec_ejecucion desc";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaAuditoriaProcesosVO fila = null;
            Timestamp fecha = null;
            String na = null;
            int cproceso = -1;
            String descProceso = null;
            String resultado = null;
            while(rs.next())
            {
                fila = new FilaAuditoriaProcesosVO();
                fecha = rs.getTimestamp("FEC_EJECUCION");
                fila.setFecha(fecha != null ? format.format(fecha) : "");
                na = rs.getString("USU_NOM");
                fila.setNomApellidos(na != null ? na.toUpperCase() : "");
                cproceso = rs.getInt("PROCESO");
                if(rs.wasNull())
                {
                    cproceso = -1;
                }
                descProceso = MeLanbide44Utils.obtenerNombreProceso(cproceso, codIdioma);
                fila.setProceso(descProceso != null ? descProceso.toUpperCase() : "");
                resultado = rs.getString("RESULTADO");
                fila.setResultado(resultado != null ? resultado.toUpperCase() : "");
                retList.add(fila);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
        return retList;
    }
    public int eliminarTodosSeguimientosPrep_Solicitud(EcaSolicitudVO sol, String tipo, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " s "
                    +" where exists ("
                        +" select  * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" p where s.eca_jus_preparadores_cod=p.eca_jus_preparadores_cod and p.eca_prep_solicitud="+sol.getSolicitudCod()
                    + " ) and ECA_SEG_TIPO ="+tipo;
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando los seguimientos", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarSegPreparador(EcaSegPreparadoresVO seg, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PREPARADORES_COD = "+seg.getJusPreparadoresCod().toString() 
                        + " and ECA_SEG_PREPARADORES_COD = "+seg.getSegPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando seguimiento preparador "+(seg != null ? seg.getJusPreparadoresCod()+"_"+seg.getSegPreparadoresCod(): "(seguimiento = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaInformeDesglose> getDatosInformeDesglose(String ejercicio, Connection con) throws Exception
    {
        List<FilaInformeDesglose> retList = new ArrayList<FilaInformeDesglose>();
        if(ejercicio != null && !ejercicio.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                /*String query = "select"
                                +" sol.eca_solicitud_cod, sol.eca_numexp, tmp.ter_noc, ps.eca_seg_tipodiscapacidad, td.nombre_c, ps.eca_seg_discgravedad, dg.nombre_c,"
                                +" ps.eca_seg_tipocontrato, tc.nombre_c, ps.eca_seg_sexo"
                                +" from"
                                +" eca_seg_preparadores ps"
                                +" left join"
                                +" eca_tipodiscapacidad td"
                                +" on ps.eca_seg_tipodiscapacidad = td.cod_tdiscapacidad"
                                +" left join"
                                +" eca_discapgravedad dg"
                                +" on ps.eca_seg_discgravedad = dg.cod_gravedad"
                                +" left join"
                                +" eca_tipocontrato tc"
                                +" on ps.eca_seg_tipocontrato = tc.cod_tipocontrato"
                                +" inner join"
                                +" eca_jus_preparadores pj"
                                +" on pj.eca_jus_preparadores_cod = ps.eca_jus_preparadores_cod"
                                +" inner join eca_solicitud sol"
                                +" on pj.eca_prep_solicitud = sol.eca_solicitud_cod"
                                +" left join"
                                +" ("
                                  +" select ext_num, ter_noc from e_ext ext"
                                  +" inner join t_ter ter"
                                  +" on ter.ter_cod = ext.ext_ter"
                                  +" where ext.ext_rol = "+ConstantesMeLanbide44.ROLES.INTERESADO
                                +" )tmp"
                                +" on tmp.ext_num = sol.eca_numexp"
                                +" where sol.eca_exp_eje = "+ejercicio
                                +" group by sol.eca_solicitud_cod, sol.eca_numexp, ter_noc, ps.eca_seg_tipodiscapacidad, td.nombre_c, ps.eca_seg_discgravedad, dg.nombre_c, ps.eca_seg_tipocontrato, tc.nombre_c, ps.eca_seg_sexo"
                                +" order by eca_solicitud_cod, ter_noc, ps.eca_seg_tipodiscapacidad, ps.eca_seg_discgravedad desc, ps.eca_seg_tipocontrato, ps.eca_seg_sexo";*/
                
                
                String query = "SELECT eca_solicitud_cod,eca_numexp, ECA_SEG_EMPRESA,\r\n"
                             +" SUM(PSIC_MAS_65_INDEF_h) h_ind_psi_65,\r\n"
                             +" SUM(PSIC_MAS_65_INDEF_m) m_ind_psi_65,\r\n"
                             +" SUM(PSIC_MAS_65_TEMP_h) h_temp_psi_65,\r\n"
                             +" SUM(PSIC_MAS_65_TEMP_m) m_temp_psi_65,\r\n"
                             +"\r\n"
                             +" SUM(PSIC_33_65_INDEF_h) h_ind_psi_33_65,\r\n"
                             +" SUM(PSIC_33_65_INDEF_m) m_ind_psi_33_65,\r\n"
                             +" SUM(PSIC_33_65_TEMP_h) h_temp_psi_33_65,\r\n"
                             +" SUM(PSIC_33_65_TEMP_m) m_temp_psi_33_65,\r\n"
                             +"\r\n"
                             +" SUM(FIS_33_65_INDEF_h) h_ind_fis_65,\r\n"
                             +" SUM(FIS_33_65_INDEF_m) m_ind_fis_65,\r\n"
                             +" SUM(FIS_33_65_TEMP_h) h_temp_fis_65,\r\n"
                             +" SUM(FIS_33_65_TEMP_m) m_temp_fis_65,\r\n"
                             +"\r\n"
                             +" SUM(SENS_MAS_33_INDEF_h) h_ind_sens_mas_33,\r\n"
                             +" SUM(SENS_MAS_33_INDEF_m) m_ind_sens_mas_33,\r\n"
                             +" SUM(SENS_MAS_33_TEMP_h) h_temp_sens_mas_33,\r\n"
                             +" SUM(SENS_MAS_33_65_TEMP_m) m_temp_sens_mas_33\r\n"
                             +" FROM (\r\n"
                             +" select eca_solicitud_cod,eca_numexp, ECA_SEG_EMPRESA,\r\n"
                             +" ECA_SEG_TIPODISCAPACIDAD,ECA_SEG_DISCGRAVEDAD,ECA_SEG_TIPOCONTRATO,ECA_SEG_SEXO,\r\n"
                             +" --psiquicos + del 65 %\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=0 then 1 else 0 end PSIC_MAS_65_INDEF_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=1 then 1 else 0 end PSIC_MAS_65_INDEF_m,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=0 then 1 else 0 end PSIC_MAS_65_TEMP_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=1 then 1 else 0 end PSIC_MAS_65_TEMP_m,\r\n"
                             +" --psiquicos 33 al 65 %\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=2 AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=0 then 1 else 0 end PSIC_33_65_INDEF_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=2 AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=1 then 1 else 0 end PSIC_33_65_INDEF_m,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=2 AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=0 then 1 else 0 end PSIC_33_65_TEMP_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=1 and ECA_SEG_DISCGRAVEDAD=2 AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=1 then 1 else 0 end PSIC_33_65_TEMP_m,\r\n"
                             +" --fisico + del 65 %\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=2 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=0 then 1 else 0 end FIS_33_65_INDEF_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=2 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=1 then 1 else 0 end FIS_33_65_INDEF_m,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=2 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=0 then 1 else 0 end FIS_33_65_TEMP_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=2 and ECA_SEG_DISCGRAVEDAD=3 AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=1 then 1 else 0 end FIS_33_65_TEMP_m,\r\n"

                             +" case when ECA_SEG_TIPODISCAPACIDAD=3 and ECA_SEG_DISCGRAVEDAD IN (2,3) AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=0 then 1 else 0 end SENS_MAS_33_INDEF_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=3 and ECA_SEG_DISCGRAVEDAD IN (2,3) AND ECA_SEG_TIPOCONTRATO=1 and ECA_SEG_SEXO=1 then 1 else 0 end SENS_MAS_33_INDEF_m,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=3 and ECA_SEG_DISCGRAVEDAD IN (2,3) AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=0 then 1 else 0 end SENS_MAS_33_TEMP_h,\r\n"
                             +" case when ECA_SEG_TIPODISCAPACIDAD=3 and ECA_SEG_DISCGRAVEDAD IN (2,3) AND ECA_SEG_TIPOCONTRATO=2 and ECA_SEG_SEXO=1 then 1 else 0 end SENS_MAS_33_65_TEMP_m\r\n"
                             +" --,\r\n"
                             +" --b.*,c.*\r\n"
                             +" from eca_solicitud a\r\n"
                             +" left join eca_jus_preparadores b on b.ECA_PREP_SOLICITUD=a.ECA_SOLICITUD_COD\r\n"
                             +" left join (SELECT * FROM eca_seg_preparadores ) c on b.ECA_JUS_PREPARADORES_COD=c.ECA_JUS_PREPARADORES_COD\r\n"
                             +" --LEFT JOIN T_TER D ON D.TER_DOC=C.ECA_SEG_NIF\r\n"
                             +" where \r\n"
                             +" --ECA_NUMEXP='2013/ECA2/000004'and \r\n"
                             +" ECA_NUMEXP like '"+ejercicio+"/"+ConstantesMeLanbide44.NOMBRE_PROCEDIMIENTO_MELANBIDE44+"/%' and \r\n"
                             +" eca_seg_nif is not null\r\n"
                             +" )\r\n"
                             +" GROUP BY eca_solicitud_cod,eca_numexp, ECA_SEG_EMPRESA";
                
            
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                retList = MeLanbide44InformeUtils.getInstance().extraerDatosInformeDesglose(rs);
                
                String[] datosTercero = null;
                HashMap<String, String[]> datosTerceros = new HashMap<String, String[]>();
                
                for(FilaInformeDesglose fila : retList)
                {
                    if(datosTerceros.containsKey(fila.getNumExp()))
                    {
                        datosTercero = datosTerceros.get(fila.getNumExp());
                    }
                    else
                    {
                        datosTercero = this.getDatosTercerosPorExpedienteYRol(fila.getNumExp(), ConstantesMeLanbide44.ROLES.INTERESADO, con);
                        datosTerceros.put(fila.getNumExp(), datosTercero);
                    }
                    
                    if(datosTercero != null)
                    {
                        fila.setEntidadPromotora(datosTercero[0]);
                        fila.setCodProvincia(datosTercero[1]);
                        fila.setDescProvincia(datosTercero[2]);
                    }
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando datos del informe desglose", ex);
                throw new Exception(ex);
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
        return retList;
    }
    public EcaSegPreparadoresVO getSeguimientoPreparadorPorId(EcaSegPreparadoresVO seg, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSegPreparadoresVO retSeg = null;
        try
        {
            String query = "select S.*,"
                        +" (select ECA_PREP_NIF from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "
                    + "         where  ECA_JUS_PREPARADORES_COD ="+seg.getJusPreparadoresCod()+") as NIFPREPARADOR "
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" S "
                        + " where ECA_SEG_PREPARADORES_COD = "+seg.getSegPreparadoresCod().toString()
                        + " and ECA_JUS_PREPARADORES_COD = "+seg.getJusPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retSeg =  (EcaSegPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSegPreparadoresVO.class);
            }
            return retSeg;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener el seguimiento del preparador "+(seg != null ? seg.getJusPreparadoresCod()+'_'+seg.getSegPreparadoresCod() : "(seguimiento = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<FilaProspectorJustificacionVO> getListaProspectoresJustificacion(EcaSolicitudVO sol, Integer idioma, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        //String[] datosexp = sol.getNumExp().split("/");
        EcaConfiguracionVO conf = null;
            try
            {
                if(sol.getNumExp() != null)
                {
                    String[] datos = sol.getNumExp().split(ConstantesMeLanbide44.BARRA_SEPARADORA);
                    conf = this.getConfiguracionEca(Integer.parseInt(datos[0]), con);
                }
            }
            catch(Exception ex)
            {  
                log.debug("No existe configuraci�nd e datos para ese a�o");
                
            }
        
        List<FilaProspectorJustificacionVO> retList = new ArrayList<FilaProspectorJustificacionVO>();
        try
        {
            String query = "select PJUS.ECA_JUS_PROSPECTORES_COD,PJUS.ECA_SOL_PROSPECTORES_COD,PJUS.ECA_PROS_NIF,PJUS.ECA_PROS_NOMBRE, PJUS.ECA_PROS_FECINI, PJUS.ECA_PROS_FECFIN, PJUS.ECA_PROS_HORAS_JC, PJUS.ECA_PROS_CODORIGEN,"+
                     " case when PJUS.ECA_PROS_CODORIGEN IS NULL THEN PJUS.ECA_JUS_PROSPECTORES_COD ELSE PJUS.ECA_PROS_CODORIGEN END AS ORIGEN, "+        
                    " PJUS.ECA_PROS_HORAS_CONT, PJUS.ECA_PROS_HORAS_ECA, PJUS.ECA_PROS_IMP_SS_JC, PJUS.ECA_PROS_IMP_SS_JR, PJUS.ECA_PROS_IMP_SS_ECA,"+
                            " PSOL.ECA_PROS_VISITAS,PSOL.ECA_PROS_COSTE, PJUS.ECA_PROS_TIPOCONTRATO, PJUS.ECA_PROS_TIPO_SUST, "+
                    /*(idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPOCONTRATO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = PJUS.ECA_PROS_TIPOCONTRATO  ) AS TIPOCONTRATO, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_TIPOCONTRATO = PJUS.ECA_PROS_TIPOCONTRATO  ) AS TIPOCONTRATO, ")+    */                       
                            " case when nvl(VISITASREAL,0)<"+conf.getMinEmpVisit()+" then 0 "+
                    
                                "  when nvl(VISITASREAL,0)>"+conf.getMaxEmpVisit()+"  "
                                        + " AND "+conf.getMaxEmpVisit() +" * "+conf.getImpVisita()+" <   nvl(PJUS.ECA_PROS_IMP_SS_ECA,0)  then "+conf.getMaxEmpVisit()+" * "+conf.getImpVisita()+                    
                               // "  when nvl(VISITASREAL,0)>"+conf.getMaxEmpVisit()+" then "+conf.getMaxEmpVisit()+" * "+conf.getImpVisita()+
                                "  when nvl(VISITASREAL,0) * "+conf.getImpVisita()+" <  nvl(PJUS.ECA_PROS_IMP_SS_ECA,0) then nvl(VISITASREAL,0) * "+conf.getImpVisita()+
                                " else nvl(PJUS.ECA_PROS_IMP_SS_ECA,0) end as IMPORTEVISITAS, "+
                            " PJUS.ECA_PROS_IMP_SS_ECA AS COSTE,  PJUS.ECA_PROS_SOLICITUD, "+                           
                            "  VISITASREAL "+
                            " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " PJUS "
                        + " left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " PSOL "
                            + " on PSOL.ECA_PROS_SOLICITUD = PJUS.ECA_PROS_SOLICITUD and PSOL.ECA_SOL_PROSPECTORES_COD = PJUS.ECA_SOL_PROSPECTORES_COD "
                        + " left join (SELECT ECA_JUS_PROSPECTORES_COD, COUNT(ECA_VIS_PROSPECTORES_COD) AS VISITASREAL "+
                                        "from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " group by ECA_JUS_PROSPECTORES_COD "+   
                            ") visitas on visitas.ECA_JUS_PROSPECTORES_COD=PJUS.ECA_JUS_PROSPECTORES_COD "
                        + " where PJUS.ECA_PROS_SOLICITUD = "+sol.getSolicitudCod()
                        + " order by ORIGEN, PJUS.ECA_PROS_CODORIGEN NULLS FIRST, ECA_PROS_FECINI, ECA_PROS_FECFIN, PJUS.ECA_PROS_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaProspectorJustificacionVO fila = null;
            while(rs.next())
            {
                fila = (FilaProspectorJustificacionVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaProspectorJustificacionVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public EcaJusProspectoresVO getProspectorJustificacionPorId(EcaJusProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaJusProspectoresVO retPros = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PROSPECTORES_COD = "+pros.getJusProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retPros =  (EcaJusProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusProspectoresVO.class);
            }
            return retPros;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener el preparador justificacion "+(pros != null ? pros.getJusProspectoresCod() : "(prospector = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaJusProspectoresVO guardarEcaJusProspectoresVO(EcaJusProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(pros.getJusProspectoresCod() == null)
            {
                pros.setJusProspectoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_JUS_PROSPECTORES_COD, ECA_SOL_PROSPECTORES_COD, ECA_PROS_NIF, ECA_PROS_NOMBRE, ECA_PROS_FECINI, ECA_PROS_FECFIN, ECA_PROS_HORAS_JC, ECA_PROS_HORAS_CONT, ECA_PROS_HORAS_ECA, ECA_PROS_IMP_SS_JC, ECA_PROS_IMP_SS_JR, ECA_PROS_IMP_SS_ECA, ECA_PROS_SOLICITUD, "
                        + "ECA_PROS_CODORIGEN, ECA_PROS_TIPO_SUST)"
                        + " values("+pros.getJusProspectoresCod()
                        + ", "+pros.getSolProspectoresCod()
                        + ", "+(pros.getNif() != null ? "'"+pros.getNif()+"'" : "null")
                        + ", "+(pros.getNombre() != null ? "'"+pros.getNombre()+"'" : "null")
                        + ", "+(pros.getFecIni() != null ? "TO_DATE('"+format.format(pros.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(pros.getFecFin() != null ? "TO_DATE('"+format.format(pros.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(pros.getHorasJC() != null ? pros.getHorasJC().toPlainString() : "null")
                        + ", "+(pros.getHorasCont() != null ? pros.getHorasCont().toPlainString() : "null")
                        + ", "+(pros.getHorasEca() != null ? pros.getHorasEca().toPlainString() : "null")
                        + ", "+(pros.getImpSSJC() != null ? pros.getImpSSJC().toPlainString() : "null")
                        + ", "+(pros.getImpSSJR() != null ? pros.getImpSSJR().toPlainString() : "null")
                        + ", "+(pros.getImpSSECA() != null ? pros.getImpSSECA().toPlainString() : "null")
                        + ", "+(pros.getSolicitud() != null ? pros.getSolicitud().toString() : "null")
                        + ", "+(pros.getJusProspectorOrigen() != null ? pros.getJusProspectorOrigen() : "null")
                        + ", "+(pros.getTipoSust() != null ? pros.getTipoSust() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_PROS_NIF = "+(pros.getNif() != null ? "'"+pros.getNif()+"'" : "null")
                        + ", ECA_PROS_NOMBRE = "+(pros.getNombre() != null ? "'"+pros.getNombre()+"'" : "null")
                        + ", ECA_PROS_FECINI = "+(pros.getFecIni() != null ? "TO_DATE('"+format.format(pros.getFecIni())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PROS_FECFIN = "+(pros.getFecFin() != null ? "TO_DATE('"+format.format(pros.getFecFin())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_PROS_HORAS_JC = "+(pros.getHorasJC() != null ? pros.getHorasJC().toPlainString() : "null")
                        + ", ECA_PROS_HORAS_CONT = "+(pros.getHorasCont() != null ? pros.getHorasCont().toPlainString() : "null")
                        + ", ECA_PROS_HORAS_ECA = "+(pros.getHorasEca() != null ? pros.getHorasEca().toPlainString() : "null")
                        + ", ECA_PROS_IMP_SS_JC = "+(pros.getImpSSJC() != null ? pros.getImpSSJC().toPlainString() : "null")
                        + ", ECA_PROS_IMP_SS_JR = "+(pros.getImpSSJR() != null ? pros.getImpSSJR().toPlainString() : "null")
                        + ", ECA_PROS_IMP_SS_ECA = "+(pros.getImpSSECA() != null ? pros.getImpSSECA().toPlainString() : "null")
                        + ", ECA_PROS_SOLICITUD = "+(pros.getSolicitud() != null ? pros.getSolicitud().toString() : "null")
                        + ", ECA_PROS_CODORIGEN="+(pros.getJusProspectorOrigen() != null ? pros.getJusProspectorOrigen() : "null")
                        + ", ECA_PROS_TIPO_SUST = "+(pros.getTipoSust() != null ? pros.getTipoSust(): "null")
                        + " where ECA_JUS_PROSPECTORES_COD = "+pros.getJusProspectoresCod();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return pros;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos del prospector "+(pros != null ? pros.getNif() : "(prospector = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarProspectorJustificacion(EcaJusProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PROSPECTORES_COD = "+pros.getJusProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando prospector justificacion "+(pros != null ? pros.getJusProspectoresCod() : "(preparador = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    
    public List<FilaVisProspectoresVO> getListaVisitas(EcaSolicitudVO sol, EcaJusProspectoresVO prep,  Integer idioma, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaVisProspectoresVO> retList = new ArrayList<FilaVisProspectoresVO>();
        try
        {
            String query = "select ECA_VIS_PROSPECTORES_COD, B.ECA_JUS_PROSPECTORES_COD, ECA_PROS_NIF, ECA_VIS_CIF, ECA_VIS_EMPRESA, "+
                            " ECA_VIS_SECTOR, ECA_VIS_DIRECCION, ECA_VIS_CP, ECA_VIS_PROVINCIA, ECA_VIS_LOCALIDAD,  "+                        
                    
                    "(select NOMBRE FROM ("
                       // + "select rownum as COD, UPPER(PRV_NO"+(idioma==1?"M":"L")+") as NOMBRE  from "
                    + "select row_number() over (order by PRV_NOM) COD, UPPER(PRV_NO"+(idioma==1?"M":"L")+") as NOMBRE  from "
                        + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" P "
                        +" WHERE PRV_PAI=108  and prv_aut=14 order by PRV_COD)"
                    + " WHERE  ECA_VIS_PROVINCIA = COD) AS PROVINCIA, "+
                    
                   /* (idioma==1?
                            " (SELECT PRV_NOM FROM "+GlobalNames.ESQUEMA_GENERICO +ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE PRV_COD = ECA_VIS_PROVINCIA  ) AS PROVINCIA, "
                    :" (SELECT PRV_NOL FROM "+GlobalNames.ESQUEMA_GENERICO+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE PRV_COD = ECA_VIS_PROVINCIA  ) AS PROVINCIA, ")+
                    */(idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SECTORACTIVIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_SECTOR = ECA_VIS_SECTOR  ) AS SECTOR, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SECTORACTIVIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_SECTOR = ECA_VIS_SECTOR  ) AS SECTOR, ")+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CUMPLELISMI, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_CUMPLE = ECA_VIS_CUMPLELISMI  ) AS CUMPLELISMI, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CUMPLELISMI, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_CUMPLE = ECA_VIS_CUMPLELISMI  ) AS CUMPLELISMI, ")+
                    (idioma==1?
                            " (SELECT NOMBRE_C FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESULTADO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_RESULTADO = ECA_VIS_RESULTADOFINAL  ) AS RESULTADO, "
                    :" (SELECT NOMBRE_E FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESULTADO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                            "   WHERE COD_RESULTADO = ECA_VIS_RESULTADOFINAL  ) AS RESULTADO, ")+
                            " ECA_VIS_FECVISITA, ECA_VIS_PERSCONTACTO,  ECA_VIS_CARGOPUESTO,  ECA_VIS_EMAIL,  ECA_VIS_TELEFONO, "+
                              "  ECA_VIS_NUMTRAB, ECA_VIS_NUMTRABDISC, ECA_VIS_CUMPLELISMI, ECA_VIS_RESULTADOFINAL, ECA_VIS_OBSERVACIONES "+
                            " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" A "+ 
                            " inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" B "+
                                " ON B.ECA_JUS_PROSPECTORES_COD = A.ECA_JUS_PROSPECTORES_COD "
                        + " where ECA_PROS_SOLICITUD ="+sol.getSolicitudCod() + 
                            (prep!=null?
                            " and A.ECA_JUS_PROSPECTORES_COD = "+prep.getJusProspectoresCod():"")+" ";
                                                 
                    query = query+ " order by ECA_VIS_FECVISITA";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaVisProspectoresVO fila = null;
            while(rs.next())
            {
                fila = (FilaVisProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaVisProspectoresVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las VISITAS", ex);
            throw new Exception(ex);
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
    /*public List<FilaInformeProyectos> getDatosInformeProyectos(String ejercicio, Connection con) throws Exception
    { 
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        if(ejercicio != null && !ejercicio.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = "select"
                                +" sol.eca_solicitud_cod, ps.eca_seg_sexo, eca_seg_fecnacimiento"
                                +" from"
                                +" eca_seg_preparadores ps"
                                +" inner join"
                                +" eca_jus_preparadores pj"
                                +" on pj.eca_jus_preparadores_cod = ps.eca_jus_preparadores_cod"
                                +" inner join eca_solicitud sol"
                                +" on pj.eca_prep_solicitud = sol.eca_solicitud_cod"
                                +" where sol.eca_exp_eje = "+ejercicio
                                +" group by sol.eca_solicitud_cod, ps.eca_seg_sexo, eca_seg_fecnacimiento"
                                +" order by eca_solicitud_cod";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                retList = MeLanbide44InformeUtils.getInstance().extraerDatosInformeProyectos(rs);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando datos del informe proyectos", ex);
                throw new Exception(ex);
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
        return retList;
    }*/
    
    public List<FilaResumenInformeProyectos> getDatosResumenInformeProyectos(String ejercicio, Connection con) throws Exception
    { 
        List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();
        if(ejercicio != null && !ejercicio.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = "select 1 ORDEN, eca_solicitud_cod, eca_numexp, NVL(eca_preparadores_imp,0)*1.10 IMPORTE_SUBVENCION"
                             +" from eca_solicitud where substr(ECA_NUMEXP, 1,4) = '"+ejercicio+"'";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                
                retList = MeLanbide44InformeUtils.getInstance().extraerDatosResumenInformeProyectos(0, rs);
                
                String nomApe = null;
                FilaResumenInformeProyectos aux = null;
                String[] datosTercero = null;
                for(FilaResumenInformeProyectos fila : retList)
                {
                    datosTercero = this.getDatosTercerosPorExpedienteYRol(fila.getNumExp(), ConstantesMeLanbide44.ROLES.INTERESADO, con);
                    if(datosTercero != null && datosTercero.length == 3)
                    {
                        nomApe = datosTercero[0];
                        fila.setEntidad(nomApe != null ? nomApe.toUpperCase() : "");
                        fila.setCodProvincia(datosTercero[1] != null ? datosTercero[1] : "");
                        fila.setDescProvincia(datosTercero[2] != null ? datosTercero[2].toUpperCase() : "");
                        aux = this.getDatosInformeProyectosParte4(fila.getNumExp(), con);
                        if(aux != null)
                        {
                            fila.setTotalPrep(aux.getTotalPrep());
                            fila.setPrepIndef(aux.getPrepIndef());
                            fila.setPrepTempo(aux.getPrepTempo());
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando datos del informe proyectos", ex);
                throw new Exception(ex);
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
        return retList;
    }
    
    public List<FilaInformeProyectos> getDatosInformeProyectosParte2(String numExp, Connection con) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        if(numExp != null && !numExp.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = "select 2 ORDEN,eca_numexp,ECA_SEG_EMPRESA,INS_SEG,\r\n"
                            +"  sum (h_menor_25) +sum(h_entre_25_55) +sum(h_mayor_55) +sum (m_menor_25) +sum(m_entre_25_55) +sum(m_mayor_55)  total,\r\n"
                                +"  sum (h_menor_25) h_menor_25,\r\n"
                                +"  sum(h_entre_25_55) h_entre_25_55,\r\n"
                                +"  sum(h_mayor_55) h_mayor_55,\r\n"
                                +"  sum (m_menor_25) m_menor_25,\r\n"
                                +"  sum(m_entre_25_55) m_entre_25_55,\r\n"
                                +"  sum(m_mayor_55) m_mayor_55\r\n"

                                +"  from (\r\n"
                                +"          select eca_solicitud_cod,eca_numexp, \r\n"
                                +"          ECA_SEG_TIPO,\r\n"
                                +"          case when ECA_SEG_TIPO=1 then 'INSERCION' ELSE 'SEGUIMIENTO' END INS_SEG,\r\n"
                                +"          case when (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365<25 and ECA_SEG_SEXO=0 then 1 else 0 end h_menor_25,\r\n"
                                +"          case when (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365<55 and (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365>=25  and ECA_SEG_SEXO=0 then 1 else 0 end h_entre_25_55,\r\n"
                                +"          case when (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365>=55 and ECA_SEG_SEXO=0 then 1 else 0 end h_mayor_55,\r\n"
                                +"          case when (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365<25 and ECA_SEG_SEXO=1 then 1 else 0 end m_menor_25,\r\n"
                                +"          case when (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365<55 and (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365>=25  and ECA_SEG_SEXO=1 then 1 else 0 end m_entre_25_55,\r\n"
                                +"          case when (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365>=55 and ECA_SEG_SEXO=1 then 1 else 0 end m_mayor_55,\r\n"
                                +"          (SYSDATE-ECA_SEG_FECNACIMIENTO+1)/365,ECA_SEG_FECNACIMIENTO,\r\n"
                                +"          ECA_SEG_TIPODISCAPACIDAD,ECA_SEG_DISCGRAVEDAD,ECA_SEG_TIPOCONTRATO,ECA_SEG_SEXO,ECA_SEG_EMPRESA\r\n"
                                +"          from eca_solicitud a\r\n"
                                +"          left join eca_jus_preparadores b on b.ECA_PREP_SOLICITUD=a.ECA_SOLICITUD_COD\r\n"
                                +"          left join eca_seg_preparadores \r\n"
                                +"          --ECA_SEG_TIPO=1) \r\n"
                                +"          c on b.ECA_JUS_PREPARADORES_COD=c.ECA_JUS_PREPARADORES_COD\r\n"
                                +"          where \r\n"
                                +"          ECA_NUMEXP='"+numExp+"'and \r\n"
                                +"          eca_seg_nif is not null\r\n"
                                +"  )\r\n"
                                +"  group by \r\n"
                                +"  eca_numexp,INS_SEG,ECA_SEG_EMPRESA\r\n"
                                +"  order by eca_numexp,ECA_SEG_EMPRESA,INS_SEG";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                retList = MeLanbide44InformeUtils.getInstance().extraerDatosInformeProyectos(2, rs);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando datos del informe proyectos", ex);
                throw new Exception(ex);
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
        return retList;
    }
    
    public List<FilaInformeProyectos> getDatosInformeProyectosParte3(String numExp, Connection con) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        if(numExp != null && !numExp.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = "select 3 ORDEN, eca_numexp, ECA_SEG_EMPRESA, ECA_SEG_TIPO, INS_SEG,count(*) TOT_PREP, SUM(INDEF) INDEF, SUM(TEMPO) TEMPO\r\n"
                             +" from (\r\n"
                             +"    select distinct eca_numexp, ECA_SEG_EMPRESA,\r\n"
                             +"                 ECA_SEG_TIPO,\r\n"
                             +"                 case when ECA_SEG_TIPO=1 then 'INSERCION' ELSE 'SEGUIMIENTO' END INS_SEG,\r\n"
                             +"                 c.ECA_JUS_PREPARADORES_COD,\r\n"

                             +"    CASE WHEN ECA_PREP_TIPOCONTRATO=1 THEN 1 ELSE 0 END INDEF,\r\n"
                             +"     CASE WHEN ECA_PREP_TIPOCONTRATO=1 THEN 0 ELSE 1 END TEMPO\r\n"
                             +"                 from eca_solicitud a\r\n"
                             +"                 left join eca_jus_preparadores b on b.ECA_PREP_SOLICITUD=a.ECA_SOLICITUD_COD\r\n"
                             +"                 left join eca_seg_preparadores \r\n"
                             +"                 --ECA_SEG_TIPO=1) \r\n"
                             +"                 c on b.ECA_JUS_PREPARADORES_COD=c.ECA_JUS_PREPARADORES_COD\r\n"
                             +"                 where \r\n"
                             +"                 ECA_NUMEXP='"+numExp+"'and \r\n"
                             +"                 --ECA_SEG_TIPO=1 AND \r\n"
                             +"                 eca_seg_nif is not null\r\n"
                             +"                 )group by eca_numexp, ECA_SEG_EMPRESA, ECA_SEG_TIPO, INS_SEG";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                retList = MeLanbide44InformeUtils.getInstance().extraerDatosInformeProyectos(3, rs);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando datos del informe proyectos", ex);
                throw new Exception(ex);
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
        return retList;
    }
    
    public FilaResumenInformeProyectos getDatosInformeProyectosParte4(String numExp, Connection con) throws Exception
    {
     List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();   
        if(numExp != null && !numExp.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            try
            {
                String query = "select 4 ORDEN, eca_numexp, count(*) TOT_PREP, SUM(INDEF) INDEF, SUM(TEMPO) TEMPO\r\n"
                             +" from (\r\n"
                             +" select distinct eca_numexp, B.ECA_JUS_PREPARADORES_COD,\r\n"
                             +" CASE WHEN ECA_PREP_TIPOCONTRATO=1 THEN 1 ELSE 0 END INDEF,\r\n"
                             +" CASE WHEN ECA_PREP_TIPOCONTRATO=1 THEN 0 ELSE 1 END TEMPO\r\n"

                             +" from eca_solicitud a\r\n"
                             +" left join eca_jus_preparadores b on b.ECA_PREP_SOLICITUD=a.ECA_SOLICITUD_COD\r\n"
                             +" where\r\n"
                             +" ECA_NUMEXP='"+numExp+"'\r\n"
                             +" --and\r\n"
                             +" --ECA_SEG_TIPO=1 AND\r\n"
                             +" )group by eca_numexp";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                retList = MeLanbide44InformeUtils.getInstance().extraerDatosResumenInformeProyectos(4, rs);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando datos del informe proyectos", ex);
                throw new Exception(ex);
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
        return retList != null && retList.size() > 0 ? retList.get(0) : null;
    }
    
    
    public List<SelectItem> getListaSectorActividad( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();       
        try{
            String query = "select COD_SECTOR, UPPER(NOMBRE_"+(idioma==1?"C":"E")+") as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SECTORACTIVIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"  ";

             if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                SelectItem si = null;/*new SelectItem();
                si.setId(-1);
                si.setLabel("");
                lista.add(si);*/
                Integer id = null;
                String nombre = null;
                while(rs.next())
                {
                    id = rs.getInt("COD_SECTOR");                
                    if(!rs.wasNull())
                    {
                        nombre = rs.getString("NOMBRE");
                        si = new SelectItem();
                        si.setId(id);
                        si.setLabel(nombre);
                        lista.add(si);
                    }
                }
           return lista;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener sector actividad", ex);
            throw new Exception(ex);
        }
        finally
        {          
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) {
                st.close();
            }
            if(rs!=null){               
                   rs.close();              
            }           
        }
    }
    
    public List<Integer> getListaCodigosSectorActividad(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = "select COD_SECTOR from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SECTORACTIVIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD_SECTOR");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos sector de actividad", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<SelectItem> getListaProvincias( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>(); 
        //List<EcaValorListaVO> listapro = new  ArrayList<EcaValorListaVO>();
        try{
            String query = /*"select rownum as COD, PRV_COD, UPPER(PRV_NO"+(idioma==1?"M":"L")+") as NOMBRE from "
                    + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" P "
                    +" WHERE PRV_PAI=108  and prv_aut=14 order by PRV_COD";*/
                    "select row_number() over (order by PRV_NOM) COD, PRV_COD, UPPER(PRV_NO"+(idioma==1?"M":"L")+") as NOMBRE from "
                    + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" P "
                    +" WHERE PRV_PAI=108  and prv_aut=14 ";//1: ARABA, 2:BIZKAIA, 3 GIPUZKOA
            
            /*String query = "select PRV_COD, UPPER(PRV_NO"+(idioma==1?"M":"L")+") as NOMBRE from "
                    + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" P "
                    +" WHERE PRV_PAI=108  and prv_aut=14 order by PRV_COD";
             */if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            EcaValorListaVO prov = new EcaValorListaVO();
            Integer id = null;
            String nombre = null;
            SelectItem si = null;/*new SelectItem();
            si.setId(-1);
            si.setLabel("");
            lista.add(si);*/
            while(rs.next())
            {
                //int id = rs.getInt("PRV_COD");                
                id= rs.getInt("COD");        
                if(!rs.wasNull())
                {
                    nombre = rs.getString("NOMBRE");
                    si = new SelectItem();
                    si.setId(id);
                    si.setLabel(nombre);
                    lista.add(si);
                }
            }

        }catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando provincias", ex);
                throw new Exception(ex);
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
       
       return lista;
    }
    
    public List<Integer> getListaCodigosProvincias(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            /*String query = "select rownum as COD, PRV_COD from "
                    + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" P "
                    +" WHERE PRV_PAI=108  and prv_aut=14 order by PRV_COD";
                    * */
            String query = "select row_number() over (order by PRV_NOM) COD, PRV_COD from "
                    + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_PROVINCIAS, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" P "
                    +" WHERE PRV_PAI=108  and prv_aut=14 order by PRV_COD";//1: ARABA, 2:BIZKAIA, 3 GIPUZKOA
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos provincias", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<SelectItem> getListaCumpleLismi( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();        
        try{
            String query = "select COD_CUMPLE, UPPER(NOMBRE_"+(idioma==1?"C":"E")+") as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CUMPLELISMI, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"  ";

             if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                String nombre = null;
                Integer id = null;
                SelectItem si = null;/*new SelectItem();
                si.setId(-1);
                si.setLabel("");*/
                while(rs.next())
                {
                    id = rs.getInt("COD_CUMPLE");                
                    if(!rs.wasNull())
                    {
                        nombre = rs.getString("NOMBRE");
                        si = new SelectItem();
                        si.setId(id);
                        si.setLabel(nombre);
                        lista.add(si);
                    }
                }
           return lista;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener Cumple Lismi", ex);
            throw new Exception(ex);
        }
        finally
        {          
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) {
                st.close();
            }
            if(rs!=null){               
                   rs.close();              
            }           
        }
    }
    
    public List<Integer> getListaCodigosCumple(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = "select COD_CUMPLE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CUMPLELISMI, ConstantesMeLanbide44.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD_CUMPLE");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos CUMPLE LISMI", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<SelectItem> getListaResultadoFinal( Integer idioma, Connection con) throws Exception{
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> lista = new ArrayList<SelectItem>();        
        try{
            String query = "select COD_RESULTADO, UPPER(NOMBRE_"+(idioma==1?"C":"E")+") as NOMBRE from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESULTADO, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"  ";

             if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                Integer id = null;
                String nombre = null;
                SelectItem si = null;/*new SelectItem();
                si.setId(-1);
                si.setLabel("");
                lista.add(si);*/
                while(rs.next())
                {
                    id = rs.getInt("COD_RESULTADO");                
                    if(!rs.wasNull())
                    {
                        nombre = rs.getString("NOMBRE");
                        si = new SelectItem();
                        si.setId(id);
                        si.setLabel(nombre);
                        lista.add(si);          
                    }

                }
           return lista;
       }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener Resultado Final", ex);
            throw new Exception(ex);
        }
        finally
        {          
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) {
                st.close();
            }
            if(rs!=null){               
                   rs.close();              
            }           
        }
    }
    
    public List<Integer> getListaCodigosResultado(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = "select COD_RESULTADO from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESULTADO, ConstantesMeLanbide44.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD_RESULTADO");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos Resultado final", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarTodasVisitasPros(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) + " s "
                    +" where exists ("
                        +" select  * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES) +" p where s.eca_jus_prospectores_cod=p.eca_jus_prospectores_cod and p.eca_pros_solicitud="+sol.getSolicitudCod()
                    + " )";
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando los seguimientos", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    
     //public EcaVisProspectoresVO guardarEcaVisProspectoresVO(EcaVisProspectoresVO vis, Connection con) throws Exception
    public EcaVisProspectoresVO guardarEcaVisProspectoresVO(EcaVisProspectoresVO vis, Statement st,Connection con) throws Exception
    {
        //Statement st = null;
        String query = null;
        try
        {            
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(vis.getVisProspectoresCod() == null)
            {
                if(log.isDebugEnabled()) log.debug("INSERT");
                //cambiar clave primaria son dos campos
                //seg.setSegPreparadoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                vis.setVisProspectoresCod(obtenerSiguienteCodVisita(vis.getJusProspectoresCod(), st,con));
                //Es un seguimiento nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " (ECA_VIS_PROSPECTORES_COD, ECA_JUS_PROSPECTORES_COD, ECA_VIS_CIF, ECA_VIS_EMPRESA, ECA_VIS_FECVISITA, "
                        + "ECA_VIS_SECTOR, ECA_VIS_DIRECCION, ECA_VIS_CP, ECA_VIS_PROVINCIA, ECA_VIS_LOCALIDAD,  ECA_VIS_PERSCONTACTO, "
                        + "ECA_VIS_CARGOPUESTO, ECA_VIS_EMAIL, ECA_VIS_TELEFONO, ECA_VIS_NUMTRAB, ECA_VIS_NUMTRABDISC, ECA_VIS_CUMPLELISMI,"
                        + "ECA_VIS_RESULTADOFINAL, ECA_VIS_OBSERVACIONES"
                        + ")"
                        + " values("+vis.getVisProspectoresCod()
                        + ", "+(vis.getJusProspectoresCod() != null ? vis.getJusProspectoresCod() : "null")
                        + ", "+(vis.getCif() != null ? "'"+vis.getCif()+"'" : "null")                       
                        + ", "+(vis.getEmpresa() != null ? "'"+vis.getEmpresa()+"'" : "null")
                        + ", "+(vis.getFecVisita() != null ? "TO_DATE('"+format.format(vis.getFecVisita())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(vis.getSector() != null ? +vis.getSector()+"" : "null")
                        + ", "+(vis.getDireccion() != null ? "'"+vis.getDireccion()+"'" : "null")
                        + ", "+(vis.getCpostal() != null ? "'"+vis.getCpostal()+"'": "null")
                        + ", "+(vis.getProvincia() != null ? vis.getProvincia() : "null")
                        + ", "+(vis.getLocalidad() != null ? "'"+vis.getLocalidad()+"'" : "null")
                        + ", "+(vis.getPersContacto() != null ? "'"+vis.getPersContacto()+"'" : "null")
                        + ", "+(vis.getPuesto() != null ? "'"+vis.getPuesto()+"'" : "null")
                        + ", "+(vis.getMail() != null ? "'"+vis.getMail()+"'" : "null")
                        + ", "+(vis.getTelefono() != null ? "'"+vis.getTelefono()+"'" : "null")
                        + ", "+(vis.getNumTrab() != null ? vis.getNumTrab()+"" : "null")        
                        + ", "+(vis.getNumTrabDisc() != null ? vis.getNumTrabDisc()+"" : "null")        
                        + ", "+(vis.getCumpleLismi() != null ? +vis.getCumpleLismi()+"" : "null")
                        + ", "+(vis.getResultadoFinal() != null ? +vis.getResultadoFinal()+"" : "null")
                        + ", "+(vis.getObservaciones() != null ? "'"+vis.getObservaciones()+"'" : "null")
                        + ")";
                        
            }
            else
            {
                if(log.isDebugEnabled()) log.debug("UPDATE");
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "  set "
                        + " ECA_VIS_CIF = "+(vis.getCif() != null ? "'"+vis.getCif()+"'" : "null")
                        + ", ECA_VIS_EMPRESA = "+(vis.getEmpresa() != null ? "'"+vis.getEmpresa()+"'" : "null")
                        + ", ECA_VIS_FECVISITA = "+(vis.getFecVisita() != null ? "TO_DATE('"+format.format(vis.getFecVisita())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_VIS_SECTOR = "+(vis.getSector() != null ? +vis.getSector(): "null")
                        + ", ECA_VIS_DIRECCION = "+(vis.getDireccion() != null ? "'"+vis.getDireccion()+"'" : "null")
                        + ", ECA_VIS_CP = "+(vis.getCpostal() != null ? "'"+vis.getCpostal()+"'" : "null")
                        + ", ECA_VIS_PROVINCIA = "+(vis.getProvincia() != null ? vis.getProvincia() : "null")  
                        + ", ECA_VIS_LOCALIDAD = "+(vis.getLocalidad() != null ? "'"+vis.getLocalidad()+"'" : "null")
                        + ", ECA_VIS_PERSCONTACTO = "+(vis.getPersContacto() != null ? "'"+vis.getPersContacto()+"'" : "null")
                        + ", ECA_VIS_CARGOPUESTO = "+(vis.getPuesto() != null ? "'"+vis.getPuesto()+"'" : "null")
                        + ", ECA_VIS_EMAIL = "+(vis.getMail() != null ? "'"+vis.getMail()+"'" : "null")
                        + ", ECA_VIS_TELEFONO = "+(vis.getTelefono() != null ? "'"+vis.getTelefono()+"'" : "null")
                        + ", ECA_VIS_NUMTRAB = "+(vis.getNumTrab() != null ? "'"+vis.getNumTrab()+"'" : "null")
                        + ", ECA_VIS_NUMTRABDISC = "+(vis.getNumTrabDisc() != null ? "'"+vis.getNumTrabDisc()+"'" : "null")
                        + ", ECA_VIS_CUMPLELISMI = "+(vis.getCumpleLismi() != null ? "'"+vis.getCumpleLismi()+"'" : "null")
                        + ", ECA_VIS_RESULTADOFINAL = "+(vis.getResultadoFinal() != null ? "'"+vis.getResultadoFinal()+"'" : "null")                        
                        + ", ECA_VIS_OBSERVACIONES = "+(vis.getObservaciones() != null ? "'"+vis.getObservaciones()+"'" : "null")
                        + " WHERE ECA_JUS_PROSPECTORES_COD = "+(vis.getJusProspectoresCod() != null ? vis.getJusProspectoresCod() : "null")  
                        + " and ECA_VIS_PROSPECTORES_COD = "+(vis.getVisProspectoresCod() != null ? vis.getVisProspectoresCod() : "null");                          
                                              
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            //st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {                
                return vis;
            }
            else
            {
                return null;
            }
                        
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la visita "+(vis != null ? vis.getCif() : "(visita = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {            
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                /*if(st!=null) 
                    st.close();*/
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    } 
     
    public EcaVisProspectores2016VO guardarEcaVisProspectoresVO_2016(EcaVisProspectores2016VO vis, Statement st,Connection con) throws Exception
    {
        //Statement st = null;
        String query = null;
        try
        {            
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
            if(vis.getVisProspectoresCod() == null)
            {
                if(log.isDebugEnabled()) log.debug("INSERT");
                //cambiar clave primaria son dos campos
                //seg.setSegPreparadoresCod(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                vis.setVisProspectoresCod(obtenerSiguienteCodVisita(vis.getJusProspectoresCod(), st,con));
                //Es un seguimiento nuevo
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " (ECA_VIS_PROSPECTORES_COD, ECA_JUS_PROSPECTORES_COD, ECA_VIS_CIF, ECA_VIS_EMPRESA, ECA_VIS_FECVISITA, "
                        + "ECA_VIS_SECTOR, ECA_VIS_DIRECCION, ECA_VIS_CP, ECA_VIS_PROVINCIA, ECA_VIS_LOCALIDAD,  ECA_VIS_PERSCONTACTO, "
                        + "ECA_VIS_CARGOPUESTO, ECA_VIS_EMAIL, ECA_VIS_TELEFONO, ECA_VIS_NUMTRAB, ECA_VIS_NUMTRABDISC, ECA_VIS_CUMPLELISMI,"
                        + "ECA_VIS_RESULTADOFINAL, ECA_VIS_OBSERVACIONES"
                        + ")"
                        + " values("+vis.getVisProspectoresCod()
                        + ", "+(vis.getJusProspectoresCod() != null ? vis.getJusProspectoresCod() : "null")
                        + ", "+(vis.getCif() != null ? "'"+vis.getCif()+"'" : "null")                       
                        + ", "+(vis.getEmpresa() != null ? "'"+vis.getEmpresa()+"'" : "null")
                        + ", "+(vis.getFecVisita() != null ? "TO_DATE('"+format.format(vis.getFecVisita())+"', 'dd/mm/yyyy')" : "null")
                        + ", "+(vis.getSector() != null ? +vis.getSector()+"" : "null")
                        + ", "+(vis.getDireccion() != null ? "'"+vis.getDireccion()+"'" : "null")
                        + ", "+(vis.getCpostal() != null ? "'"+vis.getCpostal()+"'": "null")
                        + ", "+(vis.getProvincia() != null ? vis.getProvincia() : "null")
                        + ", "+(vis.getLocalidad() != null ? "'"+vis.getLocalidad()+"'" : "null")
                        + ", "+(vis.getPersContacto() != null ? "'"+vis.getPersContacto()+"'" : "null")
                        + ", "+(vis.getPuesto() != null ? "'"+vis.getPuesto()+"'" : "null")
                        + ", "+(vis.getMail() != null ? "'"+vis.getMail()+"'" : "null")
                        + ", "+(vis.getTelefono() != null ? "'"+vis.getTelefono()+"'" : "null")
                        + ", "+(vis.getNumTrab() != null ? vis.getNumTrab()+"" : "null")        
                        + ", "+(vis.getNumTrabDisc() != null ? vis.getNumTrabDisc()+"" : "null")        
                        + ", "+(vis.getCumpleLismi() != null ? +vis.getCumpleLismi()+"" : "null")
                        + ", "+(vis.getResultadoFinal() != null ? +vis.getResultadoFinal()+"" : "null")
                        + ", "+(vis.getObservaciones() != null ? "'"+vis.getObservaciones()+"'" : "null")
                        + ")";
                        
            }
            else
            {
                if(log.isDebugEnabled()) log.debug("UPDATE");
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "  set "
                        + " ECA_VIS_CIF = "+(vis.getCif() != null ? "'"+vis.getCif()+"'" : "null")
                        + ", ECA_VIS_EMPRESA = "+(vis.getEmpresa() != null ? "'"+vis.getEmpresa()+"'" : "null")
                        + ", ECA_VIS_FECVISITA = "+(vis.getFecVisita() != null ? "TO_DATE('"+format.format(vis.getFecVisita())+"', 'dd/mm/yyyy')" : "null")
                        + ", ECA_VIS_SECTOR = "+(vis.getSector() != null ? +vis.getSector(): "null")
                        + ", ECA_VIS_DIRECCION = "+(vis.getDireccion() != null ? "'"+vis.getDireccion()+"'" : "null")
                        + ", ECA_VIS_CP = "+(vis.getCpostal() != null ? "'"+vis.getCpostal()+"'" : "null")
                        + ", ECA_VIS_PROVINCIA = "+(vis.getProvincia() != null ? vis.getProvincia() : "null")  
                        + ", ECA_VIS_LOCALIDAD = "+(vis.getLocalidad() != null ? "'"+vis.getLocalidad()+"'" : "null")
                        + ", ECA_VIS_PERSCONTACTO = "+(vis.getPersContacto() != null ? "'"+vis.getPersContacto()+"'" : "null")
                        + ", ECA_VIS_CARGOPUESTO = "+(vis.getPuesto() != null ? "'"+vis.getPuesto()+"'" : "null")
                        + ", ECA_VIS_EMAIL = "+(vis.getMail() != null ? "'"+vis.getMail()+"'" : "null")
                        + ", ECA_VIS_TELEFONO = "+(vis.getTelefono() != null ? "'"+vis.getTelefono()+"'" : "null")
                        + ", ECA_VIS_NUMTRAB = "+(vis.getNumTrab() != null ? "'"+vis.getNumTrab()+"'" : "null")
                        + ", ECA_VIS_NUMTRABDISC = "+(vis.getNumTrabDisc() != null ? "'"+vis.getNumTrabDisc()+"'" : "null")
                        + ", ECA_VIS_CUMPLELISMI = "+(vis.getCumpleLismi() != null ? "'"+vis.getCumpleLismi()+"'" : "null")
                        + ", ECA_VIS_RESULTADOFINAL = "+(vis.getResultadoFinal() != null ? "'"+vis.getResultadoFinal()+"'" : "null")                        
                        + ", ECA_VIS_OBSERVACIONES = "+(vis.getObservaciones() != null ? "'"+vis.getObservaciones()+"'" : "null")
                        + " WHERE ECA_JUS_PROSPECTORES_COD = "+(vis.getJusProspectoresCod() != null ? vis.getJusProspectoresCod() : "null")  
                        + " and ECA_VIS_PROSPECTORES_COD = "+(vis.getVisProspectoresCod() != null ? vis.getVisProspectoresCod() : "null");                          
                                              
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            //st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {                
                return vis;
            }
            else
            {
                return null;
            }
                        
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error guardando los datos de la visita "+(vis != null ? vis.getCif() : "(visita = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {            
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                /*if(st!=null) 
                    st.close();*/
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    } 
     public EcaJusProspectoresVO getProspectorJustificacionPorNIF(EcaSolicitudVO sol, String nif, Date fecha, Connection con) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
        Statement st = null;
        ResultSet rs = null;
        EcaJusProspectoresVO retPros = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where upper(ECA_PROS_NIF) = '"+(nif!=null && !nif.isEmpty() ? nif.toUpperCase() : "")+"' AND ECA_PROS_SOLICITUD="+sol.getSolicitudCod() 
                        + " AND ECA_PROS_FECINI <= TO_DATE('"+(fecha != null ? format.format(fecha) : "00/00/0000")+"', 'dd/mm/yyyy') "
                        + " AND nvl(ECA_PROS_FECFIN, TO_DATE('"+(fecha != null ? format.format(fecha) : "00/00/0000")+"', 'dd/mm/yyyy')) >= TO_DATE('"+(fecha != null ? format.format(fecha) : "00/00/0000")+"', 'dd/mm/yyyy') " 
                        + " ";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retPros =  (EcaJusProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusProspectoresVO.class);
            }
            return retPros;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener el prospector justificacion por nif "+nif , ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
     
     
    public Integer obtenerSiguienteCodVisita(Integer codPrep, Statement st, Connection con) {
        Integer num =0;
        //Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = "SELECT MAX(ECA_VIS_PROSPECTORES_COD) "+
                            " FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+
                            " WHERE ECA_JUS_PROSPECTORES_COD = "+codPrep;
           // st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()){
                num = rs.getInt(1);
                if(rs.wasNull())
                {
                    //throw new Exception();
                    num=0;
                }
            }
            num=num+1;
        }catch(Exception e){
            log.error("Se ha producido un error al intentar obtener la secuencia de la visita",e);
        }
        return num;
    } 


public EcaVisProspectoresVO getVisitaProspectorPorId(EcaVisProspectoresVO vis, Connection con) throws Exception
{
        Statement st = null;
        ResultSet rs = null;
        EcaVisProspectoresVO retVis = null;
        try
        {
            String query = "select S.*,"
                        +" (select ECA_PROS_NIF from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "
                    + "         where  ECA_JUS_PROSPECTORES_COD ="+vis.getJusProspectoresCod()+") as NIFPROSPECTOR "
                        + " from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" S "
                        + " where ECA_VIS_PROSPECTORES_COD = "+vis.getVisProspectoresCod().toString()
                        + " and ECA_JUS_PROSPECTORES_COD = "+vis.getJusProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retVis =  (EcaVisProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaVisProspectoresVO.class);
            }
            return retVis;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener la visita del prospector "+(vis != null ? vis.getJusProspectoresCod()+'_'+vis.getVisProspectoresCod() : "(visita = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public List<Integer> getListaCodigosTipoContrato(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = "select COD_TIPOCONTRATO from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPOCONTRATO, ConstantesMeLanbide44.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD_TIPOCONTRATO");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos tipo de contrato", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public List<Integer> getListaCodigosTipoDiscapacidad(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = "select COD_TDISCAPACIDAD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_TIPODISCAPACIDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD_TDISCAPACIDAD");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos tipo de discapacidad", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public List<Integer> getListaCodigosGravedad(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<Integer> retList = new ArrayList<Integer>();
        try
        {
            String query = "select COD_GRAVEDAD from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_DISCAPGRAVEDAD, ConstantesMeLanbide44.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            Integer act = null;
            while(rs.next())
            {
                act = rs.getInt("COD_GRAVEDAD");
                if(!rs.wasNull())
                {
                    retList.add(act);
                }
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener codigos gravedad", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaSolPreparadoresVO getPreparadorSolicitudPorNIF(EcaSolicitudVO sol, String nif, Connection con) throws Exception
    {
        if(sol != null && sol.getSolicitudCod() != null && !sol.getSolicitudCod().equals("") && nif != null && !nif.equals(""))
        {
            Statement st = null;
            ResultSet rs = null;
            EcaSolPreparadoresVO retPrep = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where upper(ECA_PREP_NIF) = '"+nif.toUpperCase()+"' AND ECA_PREP_SOLICITUD="+sol.getSolicitudCod() ;
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPrep =  (EcaSolPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolPreparadoresVO.class);
                }
                return retPrep;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el preparador justificacion por nif "+nif , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
     
     
     public EcaSolProspectoresVO getProspectorSolicitudPorNIF(EcaSolicitudVO sol, String nif, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaSolProspectoresVO retPros = null;
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where upper(ECA_PROS_NIF) = '"+nif.toUpperCase()+"' AND ECA_PROS_SOLICITUD="+sol.getSolicitudCod() ;
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                retPros =  (EcaSolProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolProspectoresVO.class);
            }
            return retPros;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al obtener el prospector justificacion por nif "+nif , ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null){
                   try{
                       rs.close();
                   }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                   }
                   
                }
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public EcaSolProspectoresVO getProspectorSolicitudSustituto(Integer codPros, Connection con) throws Exception
    {
        if(codPros != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaSolProspectoresVO retPros = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PROS_CODORIGEN = "+codPros 
                            + " order by ECA_PROS_FECINI, ECA_PROS_FECFIN, ECA_PROS_NIF";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPros =  (EcaSolProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolProspectoresVO.class);
                }
                return retPros;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el sustituto del prospector "+codPros , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    public EcaSolPreparadoresVO getPreparadorSolicitudSustituto(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaSolPreparadoresVO retPrep = null;
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PREP_CODORIGEN = "+codPrep 
                            + " order by ECA_PREP_FECINI, ECA_PREP_FECFIN, ECA_PREP_NIF";
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPrep =  (EcaSolPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolPreparadoresVO.class);
                }
                return retPrep;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el susutituto del preparador "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
     
     public int eliminarVisitaProspectorJustificacion(EcaVisProspectoresVO vis, Connection con) throws Exception
     {
        Statement st = null;
        EcaSolProspectoresVO retPros = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_VIS_PROSPECTORES_COD = "+vis.getVisProspectoresCod().toString()+" and ECA_JUS_PROSPECTORES_COD = "+vis.getJusProspectoresCod().toString() ;
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error en la BBDD eliminando visita :"+(vis != null ? " codigo vis = "+vis.getVisProspectoresCod()+" codigo pros = "+vis.getJusProspectoresCod() : " (visita = null)") , ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
     }
     
    public Map<String, Integer> getNumeroSeguimientosInsercionesPreparador(EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Map<String, Integer> valores = new HashMap<String, Integer>();
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = "select ECA_SEG_TIPO as TIPO, count(*) as SUMA from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        +" where ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod().toString() 
                        +" group by ECA_SEG_TIPO";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int valor = 0;
            int tipo = -1;
            while(rs.next())
            {
                valor = rs.getInt("SUMA");
                if(!rs.wasNull())
                {
                    tipo = rs.getInt("TIPO");
                    if(!rs.wasNull())
                    {
                        valores.put(String.valueOf(tipo), valor);
                    }
                }
            }
            return valores;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero seguimientos preparador : "+(prep != null && prep.getJusPreparadoresCod() != null ? prep.getJusPreparadoresCod() : "null"), ex);
            throw new Exception(ex);
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
    
    public int eliminarTodosSeguimientosPreparador(EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PREPARADORES_COD = "+prep.getJusPreparadoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando seguimiento preparador "+(prep != null && prep.getJusPreparadoresCod() != null ? prep.getJusPreparadoresCod() : "null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
     
    public Integer getNumeroVisitasProspector(EcaJusProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = "select count(*) as SUMA from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        +" where ECA_JUS_PROSPECTORES_COD = "+pros.getJusProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int valor = 0;
            if(rs.next())
            {
                valor = rs.getInt("SUMA");
                if(!rs.wasNull())
                {
                    return valor;
                }
                else
                {
                    valor = 0;
                }
            }
            return valor;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando numero visitas prospector : "+(pros != null && pros.getJusProspectoresCod() != null ? pros.getJusProspectoresCod() : "null"), ex);
            throw new Exception(ex);
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
    
    public int eliminarTodasVisitasProspector(EcaJusProspectoresVO pros, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = "delete from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_VIS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " where ECA_JUS_PROSPECTORES_COD = "+pros.getJusProspectoresCod().toString();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando visitas prospector "+(pros != null && pros.getJusProspectoresCod() != null ? pros.getJusProspectoresCod() : "null"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarTodosPreparadoresJus(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    +" where ECA_PREP_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando Preparadores Justificaci�n", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean hayPreparadoresParaCopiar(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select count(*) as NUM_PREPARADORES "
                  + " from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                  + " where ECA_PREP_SOLICITUD = "+sol.getSolicitudCod();
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int numPreparadores = 0;
            if(rs.next())
            {
                numPreparadores = rs.getInt("NUM_PREPARADORES");
            }
            return numPreparadores > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando si hay preparadores para copiar", ex);
            throw new Exception(ex);
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
    
    /*public int copiarPreparadoresFromSolToJus(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "insert into "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
                    "(ECA_JUS_PREPARADORES_COD,"+
                    "ECA_SOL_PREPARADORES_COD,"+
                    "ECA_PREP_NIF,ECA_PREP_NOMBRE,"+
                    "ECA_PREP_FECINI,ECA_PREP_FECFIN,"+
                    "ECA_PREP_HORAS_JC,ECA_PREP_HORAS_CONT,ECA_PREP_HORAS_ECA,"+
                    "ECA_PREP_IMP_SS_JC,ECA_PREP_IMP_SS_JR,ECA_PREP_IMP_SS_ECA,"+
                    "ECA_PREP_SOLICITUD,"+
                    "ECA_PREP_TIPOCONTRATO) "+

                    "select  "+
                    "seq_eca_jus_preparadores.nextval,"+
                    "ECA_SOL_PREPARADORES_COD,"+
                    "ECA_PREP_NIF,ECA_PREP_NOMBRE,"+
                    "ECA_PREP_FECINI,ECA_PREP_FECFIN,"+
                    "ECA_PREP_HORAS_JC,ECA_PREP_HORAS_CONT,ECA_PREP_HORAS_ECA,"+
                    "ECA_PREP_IMP_SS_JC,ECA_PREP_IMP_SS_JR,ECA_PREP_IMP_SS_ECA,"+
                    "ECA_PREP_SOLICITUD, "+
                    "null "+
                    " from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" a "+
                    " inner join eca_solicitud b on a.ECA_PREP_SOLICITUD =b.ECA_SOLICITUD_COD "+
                    "where "+
                        "eca_prep_solicitud = "+sol.getSolicitudCod();
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error copiando Preparadores Justificaci�n", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }*/
    
    public int copiarPreparadoresFromSolToJus(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement stSol = null;
        Statement stJus = null;
        Statement stSeq = null;
        ResultSet rsSol = null;
        ResultSet rsSeq = null;
        String queryJus = null;
        Integer newId = null;
        Integer solPreparadoresCod = null;
        Integer codOrigen = null;
        HashMap<Integer, Integer> mapaIds = new HashMap<Integer, Integer>();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
        int filasInsertadas = 0;
        try
        {
            String querySeq = "select "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+".nextval NEW_ID from dual";
            
            if(log.isDebugEnabled()) 
                log.debug("sqlSeq = " + querySeq);
            
            String querySol = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                             +" where eca_prep_solicitud = "+sol.getSolicitudCod()
                             +" order by ECA_SOL_PREPARADORES_COD";
            
            if(log.isDebugEnabled()) 
                log.debug("sqlSol = " + querySol);
            
            stSol = con.createStatement();
            rsSol = stSol.executeQuery(querySol);
            
            while(rsSol.next())
            {
                solPreparadoresCod = rsSol.getInt("ECA_SOL_PREPARADORES_COD");
                if(!rsSol.wasNull())
                {
                    stSeq = con.createStatement();
                    rsSeq = stSeq.executeQuery(querySeq);
                    if(rsSeq.next())
                    {
                        newId = rsSeq.getInt("NEW_ID");
                        if(!rsSeq.wasNull())
                        {
                            mapaIds.put(solPreparadoresCod, newId);
                            codOrigen = rsSol.getInt("ECA_PREP_CODORIGEN");
                            if(rsSol.wasNull())
                            {
                                codOrigen = null;
                            }
                            queryJus = "insert into "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                                      +" (ECA_JUS_PREPARADORES_COD,"
                                      +" ECA_SOL_PREPARADORES_COD,"
                                      +" ECA_PREP_NIF,ECA_PREP_NOMBRE,"
                                      +" ECA_PREP_FECINI,ECA_PREP_FECFIN,"
                                      +" ECA_PREP_HORAS_JC,ECA_PREP_HORAS_CONT,ECA_PREP_HORAS_ECA,"
                                      +" ECA_PREP_IMP_SS_JC,ECA_PREP_IMP_SS_JR,ECA_PREP_IMP_SS_ECA,"
                                      +" ECA_PREP_SOLICITUD,"
                                      +" ECA_PREP_TIPOCONTRATO,"
                                      +" ECA_PREP_CODORIGEN,"
                                      +" ECA_PREP_TIPO_SUST)"
                                      +" values ("
                                      +" "+newId
                                      +", "+solPreparadoresCod
                                      +", '"+rsSol.getString("ECA_PREP_NIF")+"'"
                                      +", '"+rsSol.getString("ECA_PREP_NOMBRE")+"'"
                                      +", "+(rsSol.getDate("ECA_PREP_FECINI") != null ? "TO_DATE('"+formatoFecha.format(rsSol.getDate("ECA_PREP_FECINI"))+"', '"+ConstantesMeLanbide44.FORMATO_FECHA+"')" : "null")
                                      +", "+(rsSol.getDate("ECA_PREP_FECFIN") != null ? "TO_DATE('"+formatoFecha.format(rsSol.getDate("ECA_PREP_FECFIN"))+"', '"+ConstantesMeLanbide44.FORMATO_FECHA+"')" : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_HORAS_JC") != null ? rsSol.getBigDecimal("ECA_PREP_HORAS_JC").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_HORAS_CONT") != null ? rsSol.getBigDecimal("ECA_PREP_HORAS_CONT").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_HORAS_ECA") != null ? rsSol.getBigDecimal("ECA_PREP_HORAS_ECA").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_IMP_SS_JC") != null ? rsSol.getBigDecimal("ECA_PREP_IMP_SS_JC").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_IMP_SS_JR") != null ? rsSol.getBigDecimal("ECA_PREP_IMP_SS_JR").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_IMP_SS_ECA") != null ? rsSol.getBigDecimal("ECA_PREP_IMP_SS_ECA").toPlainString() : "null")
                                      +", "+sol.getSolicitudCod()
                                      +", null"
                                      +", "+(codOrigen != null ? mapaIds.get(codOrigen) : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PREP_TIPO_SUST") != null ? rsSol.getBigDecimal("ECA_PREP_TIPO_SUST").toPlainString() : "null")
                                      +")";
                            if(log.isDebugEnabled()) 
                                log.debug("sqlJus = " + queryJus);
                            stJus = con.createStatement();
                            filasInsertadas += stJus.executeUpdate(queryJus);
                        }
                        else
                        {
                            throw new Exception("No se ha podido obtener id de "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES));
                        }
                    }
                    else
                    {
                        throw new Exception("No se ha podido obtener id de "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES));
                    }
                }
            }
            return filasInsertadas;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error copiando Preparadores Justificaci�n", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(rsSol!=null) 
                    rsSol.close();
                if(rsSeq!=null) 
                    rsSeq.close();
                if(stSol!=null) 
                    stSol.close();
                if(stJus!=null) 
                    stJus.close();
                if(stSeq!=null) 
                    stSeq.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public int eliminarTodosProspectoresJus(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        EcaConfiguracionVO conf = null;
        try
        {
            String query = null;
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                    +" where ECA_PROS_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error eliminando Prospectores Justificaci�n", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean hayProspectoresParaCopiar(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "select count(*) as NUM_PROSPECTORES "
                  + " from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                  + " where ECA_PROS_SOLICITUD = "+sol.getSolicitudCod();
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            int numProspectores = 0;
            if(rs.next())
            {
                numProspectores = rs.getInt("NUM_PROSPECTORES");
            }
            return numProspectores > 0;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error comprobando si hay prospectores para copiar", ex);
            throw new Exception(ex);
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
    
//    public int copiarProspectoresFromSolToJus(EcaSolicitudVO sol, Connection con) throws Exception
//    {
//        Statement st = null;
//        EcaConfiguracionVO conf = null;
//        try
//        {
//            String query = null;
//            query = "insert into "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" "+
//                    "(ECA_JUS_PROSPECTORES_COD,"+
//                    "ECA_SOL_PROSPECTORES_COD,"+
//                    "ECA_PROS_NIF,ECA_PROS_NOMBRE,"+
//                    "ECA_PROS_FECINI,ECA_PROS_FECFIN,"+
//                    "ECA_PROS_HORAS_JC,ECA_PROS_HORAS_CONT,ECA_PROS_HORAS_ECA,"+
//                    "ECA_PROS_IMP_SS_JC,ECA_PROS_IMP_SS_JR,ECA_PROS_IMP_SS_ECA,"+
//                    "ECA_PROS_SOLICITUD,"+
//                    "ECA_PROS_TIPOCONTRATO,"+
//                    "ECA_PROS_CODORIGEN,"+
//                    "ECA_PROS_TIPO_SUST) "+
//
//                    "select  "+
//                    ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+".nextval,"+
//                    "ECA_SOL_PROSPECTORES_COD,"+
//                    "ECA_PROS_NIF,ECA_PROS_NOMBRE,"+
//                    "ECA_PROS_FECINI,ECA_PROS_FECFIN,"+
//                    "ECA_PROS_HORAS_JC,ECA_PROS_HORAS_CONT,ECA_PROS_HORAS_ECA,"+
//                    "ECA_PROS_IMP_SS_JC,ECA_PROS_IMP_SS_JR,ECA_PROS_IMP_SS_ECA,"+
//                    "ECA_PROS_SOLICITUD, "+
//                    "null, ECA_PROS_CODORIGEN, ECA_PROS_TIPO_SUST"+
//                    " from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" a "+
//                    " inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" b on a.ECA_PROS_SOLICITUD =b.ECA_SOLICITUD_COD "+
//                    "where "+
//                        "eca_pros_solicitud = "+sol.getSolicitudCod();
//                    
//                    /*"select"
//                    +" "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+".nextval,"
//                    +" p.ECA_SOL_PROSPECTORES_COD,"
//                    +" p.ECA_PROS_NIF, p.ECA_PROS_NOMBRE,"
//                    +" p.ECA_PROS_FECINI, p.ECA_PROS_FECFIN,"
//                    +" p.ECA_PROS_HORAS_JC, p.ECA_PROS_HORAS_CONT, p.ECA_PROS_HORAS_ECA,"
//                    +" p.ECA_PROS_IMP_SS_JC, p.ECA_PROS_IMP_SS_JR, p.ECA_PROS_IMP_SS_ECA,"
//                    +" p.ECA_PROS_SOLICITUD,"
//                    +" null,"
//                    +" p.ECA_PROS_CODORIGEN, p.ECA_PROS_TIPO_SUST"
//                    +" from"
//                    +" ("
//                    +"   select a.* ,"
//                    +"   case when a.ECA_PROS_CODORIGEN IS NULL THEN a.ECA_SOL_PROSPECTORES_COD ELSE a.ECA_PROS_CODORIGEN END AS ORIGEN"
//                    +"   from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" a"
//                    +"   inner join "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" b on a.ECA_PROS_SOLICITUD =b.ECA_SOLICITUD_COD"
//                    +"   where"
//                    +"   eca_pros_solicitud = "+sol.getSolicitudCod()
//                    +"   order by ORIGEN, a.ECA_PROS_CODORIGEN NULLS FIRST, a.ECA_PROS_FECINI, a.ECA_PROS_FECFIN, a.ECA_PROS_NIF"
//                    +" )p";*/
//            
//            if(log.isDebugEnabled()) 
//                log.debug("sql = " + query);
//            st = con.createStatement();
//            return st.executeUpdate(query);
//        }
//        catch(Exception ex)
//        {
//            log.error("Se ha producido un error copiando Preparadores Justificaci�n", ex);
//            throw new Exception(ex);
//        }
//        finally
//        {
//            try
//            {
//                if(log.isDebugEnabled()) 
//                    log.debug("Procedemos a cerrar el statement y el resultset");
//                if(st!=null) 
//                    st.close();
//            }
//            catch(Exception e)
//            {
//                log.error("Se ha producido un error cerrando el statement y el resulset", e);
//                throw new Exception(e);
//            }
//        }
//    }
    
    
    
    public int copiarProspectoresFromSolToJus(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement stSol = null;
        Statement stJus = null;
        Statement stSeq = null;
        ResultSet rsSol = null;
        ResultSet rsSeq = null;
        String queryJus = null;
        Integer newId = null;
        Integer solProspectoresCod = null;
        Integer codOrigen = null;
        HashMap<Integer, Integer> mapaIds = new HashMap<Integer, Integer>();
        SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
        int filasInsertadas = 0;
        try
        {
            String querySeq = "select "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+".nextval NEW_ID from dual";
            
            if(log.isDebugEnabled()) 
                log.debug("sqlSeq = " + querySeq);
            
            String querySol = "select * from "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                             +" where eca_pros_solicitud = "+sol.getSolicitudCod()
                             +" order by ECA_SOL_PROSPECTORES_COD";
            
            if(log.isDebugEnabled()) 
                log.debug("sqlSol = " + querySol);
            
            stSol = con.createStatement();
            rsSol = stSol.executeQuery(querySol);
            
            while(rsSol.next())
            {
                solProspectoresCod = rsSol.getInt("ECA_SOL_PROSPECTORES_COD");
                if(!rsSol.wasNull())
                {
                    stSeq = con.createStatement();
                    rsSeq = stSeq.executeQuery(querySeq);
                    if(rsSeq.next())
                    {
                        newId = rsSeq.getInt("NEW_ID");
                        if(!rsSeq.wasNull())
                        {
                            mapaIds.put(solProspectoresCod, newId);
                            codOrigen = rsSol.getInt("ECA_PROS_CODORIGEN");
                            if(rsSol.wasNull())
                            {
                                codOrigen = null;
                            }
                            queryJus = "insert into "+ ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                                      +" (ECA_JUS_PROSPECTORES_COD,"
                                      +" ECA_SOL_PROSPECTORES_COD,"
                                      +" ECA_PROS_NIF,ECA_PROS_NOMBRE,"
                                      +" ECA_PROS_FECINI,ECA_PROS_FECFIN,"
                                      +" ECA_PROS_HORAS_JC,ECA_PROS_HORAS_CONT,ECA_PROS_HORAS_ECA,"
                                      +" ECA_PROS_IMP_SS_JC,ECA_PROS_IMP_SS_JR,ECA_PROS_IMP_SS_ECA,"
                                      +" ECA_PROS_SOLICITUD,"
                                      +" ECA_PROS_TIPOCONTRATO,"
                                      +" ECA_PROS_CODORIGEN,"
                                      +" ECA_PROS_TIPO_SUST)"
                                      +" values ("
                                      +" "+newId
                                      +", "+solProspectoresCod
                                      +", '"+rsSol.getString("ECA_PROS_NIF")+"'"
                                      +", '"+rsSol.getString("ECA_PROS_NOMBRE")+"'"
                                      +", "+(rsSol.getDate("ECA_PROS_FECINI") != null ? "TO_DATE('"+formatoFecha.format(rsSol.getDate("ECA_PROS_FECINI"))+"', '"+ConstantesMeLanbide44.FORMATO_FECHA+"')" : "null")
                                      +", "+(rsSol.getDate("ECA_PROS_FECFIN") != null ? "TO_DATE('"+formatoFecha.format(rsSol.getDate("ECA_PROS_FECFIN"))+"', '"+ConstantesMeLanbide44.FORMATO_FECHA+"')" : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_HORAS_JC") != null ? rsSol.getBigDecimal("ECA_PROS_HORAS_JC").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_HORAS_CONT") != null ? rsSol.getBigDecimal("ECA_PROS_HORAS_CONT").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_HORAS_ECA") != null ? rsSol.getBigDecimal("ECA_PROS_HORAS_ECA").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_IMP_SS_JC") != null ? rsSol.getBigDecimal("ECA_PROS_IMP_SS_JC").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_IMP_SS_JR") != null ? rsSol.getBigDecimal("ECA_PROS_IMP_SS_JR").toPlainString() : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_IMP_SS_ECA") != null ? rsSol.getBigDecimal("ECA_PROS_IMP_SS_ECA").toPlainString() : "null")
                                      +", "+sol.getSolicitudCod()
                                      +", null"
                                      +", "+(codOrigen != null ? mapaIds.get(codOrigen) : "null")
                                      +", "+(rsSol.getBigDecimal("ECA_PROS_TIPO_SUST") != null ? rsSol.getBigDecimal("ECA_PROS_TIPO_SUST").toPlainString() : "null")
                                    
                                    +")";
                            if(log.isDebugEnabled()) 
                                log.debug("sqlJus = " + queryJus);
                            stJus = con.createStatement();
                            filasInsertadas += stJus.executeUpdate(queryJus);
                        }
                        else
                        {
                            throw new Exception("No se ha podido obtener id de "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES));
                        }
                    }
                    else
                    {
                        throw new Exception("No se ha podido obtener id de "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES));
                    }
                }
            }
            return filasInsertadas;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error copiando Preparadores Justificaci�n", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(rsSol!=null) 
                    rsSol.close();
                if(rsSeq!=null) 
                    rsSeq.close();
                if(stSol!=null) 
                    stSol.close();
                if(stJus!=null) 
                    stJus.close();
                if(stSeq!=null) 
                    stSeq.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public List<EcaJusPreparadoresVO> getPreparadoresJustificacion(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        
        List<EcaJusPreparadoresVO> retList = new ArrayList<EcaJusPreparadoresVO>();
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        +" where ECA_PREP_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            EcaJusPreparadoresVO fila = null;
            while(rs.next())
            {
                fila = (EcaJusPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusPreparadoresVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public List<EcaJusProspectoresVO> getProspectoresJustificacion(EcaSolicitudVO sol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        
        List<EcaJusProspectoresVO> retList = new ArrayList<EcaJusProspectoresVO>();
        try
        {
            String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        +" where ECA_PROS_SOLICITUD = "+sol.getSolicitudCod();
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            EcaJusProspectoresVO fila = null;
            while(rs.next())
            {
                fila = (EcaJusProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusProspectoresVO.class);
                //Si bubiera que mostrar las filas con colores o similares (porque tengan errores) se har�a aqui
                retList.add(fila);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public List<EcaSolPreparadoresVO> getSustitutosPreparadorSolicitud(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaSolPreparadoresVO retPrep = null;
            List<EcaSolPreparadoresVO> retList = new ArrayList<EcaSolPreparadoresVO>();
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PREP_CODORIGEN = "+codPrep ;
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retPrep =  (EcaSolPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolPreparadoresVO.class);
                    retList.add(retPrep);
                }
                return retList;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el susutituto del preparador "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    public List<EcaJusPreparadoresVO> getSustitutosPreparadorJustificacion(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaJusPreparadoresVO retPrep = null;
            List<EcaJusPreparadoresVO> retList = new ArrayList<EcaJusPreparadoresVO>();
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PREP_CODORIGEN = "+codPrep ;
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retPrep =  (EcaJusPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusPreparadoresVO.class);
                    retList.add(retPrep);
                }
                return retList;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el susutituto del preparador "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    public List<EcaSolProspectoresVO> getSustitutosProspectorSolicitud(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaSolProspectoresVO retPros = null;
            List<EcaSolProspectoresVO> retList = new ArrayList<EcaSolProspectoresVO>();
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOL_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PROS_CODORIGEN = "+codPrep ;
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                while(rs.next())
                {
                    retPros =  (EcaSolProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaSolProspectoresVO.class);
                    retList.add(retPros);
                }
                return retList;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el sustituto del prospector "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    public List<EcaJusProspectoresVO> getSustitutosProspectorJustificacion(Integer codPrep, Connection con) throws Exception
    {
        if(codPrep != null )
        {
            Statement st = null;
            ResultSet rs = null;
            EcaJusProspectoresVO retPros = null;
            List<EcaJusProspectoresVO> retList = new ArrayList<EcaJusProspectoresVO>();
            try
            {
                String query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                            + " where ECA_PROS_CODORIGEN = "+codPrep ;
                if(log.isDebugEnabled()) 
                    log.debug("sql = " + query);
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    retPros =  (EcaJusProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaJusProspectoresVO.class);
                    retList.add(retPros);
                }
                return retList;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error al obtener el sustituto del prospector "+codPrep , ex);
                throw new Exception(ex);
            }
            finally
            {
                try
                {
                    if(log.isDebugEnabled()) 
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    if(st!=null) 
                        st.close();
                    if(rs!=null){
                       try{
                           rs.close();
                       }catch(Exception e){log.debug("error al cerra resulset"+e.getMessage());
                       }

                    }
                }
                catch(Exception e)
                {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        else
        {
            return null;
        }
    }
    
    private String[] getDatosTercerosPorExpedienteYRol(String numExp, int rol, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        String nomApe = "";
        String[] result = new String[3];
        try
        {
            String query = null;
            /*query = "select ext_rol, ter_doc from t_ter inner join e_ext on ter_cod = ext_ter"
                    +" where ext_num = '"+numExp+"' and ext_rol in (";*/
            
            query = "select t.ter_nom nom, t.ter_ap1 ap1, t.ter_ap2 ap2,\r\n"
                   +" h.hte_nom nom_his, h.hte_ap1 ap1_his, h.hte_ap2 ap2_his, DNN_PRV, PRV_NOM"
                   +" from e_ext\r\n"
                   +" left join t_ter t on t.ter_cod = ext_ter and ter_nve = ext_nvr\r\n"
                   +" left join t_hte h on h.hte_ter = ext_ter and hte_nvr = ext_nvr\r\n"
                   +" left join T_DOT dot on dot.DOT_TER = t.ter_cod\r\n"
                   +" left join T_DNN dn on dot.DOT_DOM = dn.DNN_DOM\r\n"
                   +" left join FLBGEN.T_PRV p on p.PRV_COD = dn.DNN_PRV\r\n"
                   +" where ext_num = '"+numExp+"' and ext_rol = "+rol;
            
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String nom = null;
            String ape1 = null;
            String ape2 = null;
            if(rs.next())
            {
                if(rs.getString("NOM") != null)
                {
                    nom = rs.getString("NOM");
                    ape1 = rs.getString("AP1");
                    ape2 = rs.getString("AP2");
                }
                else if(rs.getString("NOM_HIS") != null)
                {
                    nom = rs.getString("NOM_HIS");
                    ape1 = rs.getString("AP1_HIS");
                    ape2 = rs.getString("AP2_HIS");
                }
                nomApe = nom != null ? nom : "";
                nomApe += ape1 != null && !ape1.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                nomApe += ape1 != null ? ape1 : "";
                nomApe += ape2 != null && !ape2.equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                nomApe += ape2 != null ? ape2 : "";
                
                result[0] = nomApe;
                
                result[1] = rs.getString("DNN_PRV");
                result[2] = rs.getString("PRV_NOM");
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de terceros por rol", ex);
            throw new Exception(ex);
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
        
        return result;
    }
    
    public List<FilaEcaResProspectoresVO> getListaDetalleProspectoresResumen(String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaEcaResProspectoresVO> retList = new ArrayList<FilaEcaResProspectoresVO>();
        try
        {            
            /*String query = "select r.*, j.ECA_PROS_TIPO_SUST from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RES_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" r"
                          +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" j"
                          +" on r.ECA_JUS_PROSPECTORES_COD = j.ECA_JUS_PROSPECTORES_COD"
                          +" where ECA_NUMEXP = '"+numExp+"'";*/
            
            
            String query = "select r.*, j.ECA_PROS_TIPO_SUST,"
                          +" case when j.ECA_PROS_CODORIGEN IS NULL THEN j.ECA_JUS_PROSPECTORES_COD ELSE j.ECA_PROS_CODORIGEN END AS ORIGEN,"
                          +" j.ECA_PROS_FECINI, j.ECA_PROS_FECFIN, j.ECA_PROS_NIF"
                          +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RES_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" r"
                          +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PROSPECTORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" j on r.ECA_JUS_PROSPECTORES_COD = j.ECA_JUS_PROSPECTORES_COD"
                          +" where ECA_NUMEXP = '"+numExp+"'"
                          +" order by ORIGEN,  j.ECA_PROS_FECINI, j.ECA_PROS_FECFIN, j.ECA_PROS_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaEcaResProspectoresVO pros = null;
            while(rs.next())
            {
                pros = (FilaEcaResProspectoresVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaEcaResProspectoresVO.class);
                retList.add(pros);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public List<FilaEcaResPreparadoresVO> getListaDetallePreparadoresResumen(String numExp, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<FilaEcaResPreparadoresVO> retList = new ArrayList<FilaEcaResPreparadoresVO>();
        try
        {            
            /*String query = "select r.*, j.ECA_PREP_TIPO_SUST from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RES_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" r"
                          +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" j"
                          +" on r.ECA_JUS_PREPARADORES_COD = j.ECA_JUS_PREPARADORES_COD"
                          +" where ECA_NUMEXP = '"+numExp+"'";*/
            
            String query = "select r.*, j.ECA_PREP_TIPO_SUST ,"
                          +" case when j.ECA_PREP_CODORIGEN IS NULL THEN j.ECA_JUS_PREPARADORES_COD ELSE j.ECA_PREP_CODORIGEN END AS ORIGEN,"
                          +" j.ECA_PREP_FECINI, j.ECA_PREP_FECFIN, j.ECA_PREP_NIF, r.IMPORTE_SEGUIMIENTOS_LIM"
                          +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RES_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" r"
                          +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" j on r.ECA_JUS_PREPARADORES_COD = j.ECA_JUS_PREPARADORES_COD"
                          +" where ECA_NUMEXP = '"+numExp+"'"
                          +" order by ORIGEN,  j.ECA_PREP_FECINI, j.ECA_PREP_FECFIN, j.ECA_PREP_NIF";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            FilaEcaResPreparadoresVO prep = null;
            while(rs.next())
            {
                prep = (FilaEcaResPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(rs, FilaEcaResPreparadoresVO.class);
                retList.add(prep);
            }
            return retList;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public EcaResDetalleInsercionesVO cargarDetalleInsercionesPrep(String ejercicio, EcaJusPreparadoresVO prep, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        EcaResDetalleInsercionesVO ret = new EcaResDetalleInsercionesVO();
        try
        {
            String query = "select\r\n"
                          +" COLECTIVO, SUM(ECA_SEG_PORCJORNADA)/100 NUMERO, SUM(IMPORTE_INSERCIONES) IMPORTE FROM (\r\n"
                          +" SELECT ECA_SEG_PORCJORNADA,\r\n"
                          +"     case\r\n"
                          +"       when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   'c1h'\r\n"
                          +"       when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   'c1m'\r\n"
                          +"       when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   'c2h'\r\n"
                          +"       when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   'c2m'\r\n"
                          +"       when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 THEN    'c3h'\r\n"
                          +"       when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then   'c3m'\r\n"
                          +"       when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   'c4h'\r\n"
                          +"       when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   'c4m'\r\n"
                          +"       ELSE 'sin colectivo'\r\n"
                          +"     END  COLECTIVO,\r\n"
                          +"     case\r\n"
                          +"       when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   IM_C1_H*ECA_SEG_PORCJORNADA/100--> c1h,\r\n"
                          +"       when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   IM_C1_M*ECA_SEG_PORCJORNADA/100--> c1m,\r\n"
                          +"       when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C2_H*ECA_SEG_PORCJORNADA/100--> c2h,\r\n"
                          +"       when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C2_M*ECA_SEG_PORCJORNADA/100--> c2m,\r\n"
                          +"       when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 THEN    IM_C3_H*ECA_SEG_PORCJORNADA/100--> c3h,\r\n"
                          +"       when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then   IM_C3_M*ECA_SEG_PORCJORNADA/100--> c3m,\r\n"
                          +"       when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C4_H*ECA_SEG_PORCJORNADA/100--> c4h,\r\n"
                          +"       when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C4_M*ECA_SEG_PORCJORNADA/100--> c4m,\r\n"
                          +"       ELSE 0\r\n"
                          +"     END  IMPORTE_INSERCIONES\r\n"
                          +"   from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SOLICITUD, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" dd inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_JUS_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" cc on cc.ECA_PREP_SOLICITUD=dd.ECA_SOLICITUD_COD\r\n"
                          +"   inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_SEG_PREPARADORES, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+" aa on cc.ECA_JUS_PREPARADORES_COD=aa.ECA_JUS_PREPARADORES_COD\r\n"
                          +"   inner join\r\n"
                          +"   (select IM_SEGUIMIENTO,IM_C1_H,IM_C1_M,IM_C2_H,IM_C2_M,IM_C3_H,IM_C3_M,IM_C4_H,IM_C4_M from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_CONFIGURACION, ConstantesMeLanbide44.FICHERO_PROPIEDADES)+"\r\n"
                          +"   where ano="+ejercicio+"\r\n"
                          +"   ) importe on 1=1\r\n"
                          +"   where\r\n"
                          +"     ECA_SEG_CORRECTO ='S'\r\n"
                          +"   and\r\n"
                          +"   cc.ECA_JUS_PREPARADORES_COD="+prep.getJusPreparadoresCod().toString()+"\r\n"
                          +"   ) GROUP BY COLECTIVO";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String colectivo = null;
            BigDecimal importe = null;
            BigDecimal numero = null;
            
            BigDecimal totalH = new BigDecimal("0.0");
            BigDecimal totalM = new BigDecimal("0.0");
            
            BigDecimal total = new BigDecimal("0.0");
            
            boolean esHombre = false;
            boolean hayTotalH = false;
            boolean hayTotalM = false;
            while(rs.next())
            {
                colectivo = rs.getString("COLECTIVO");
                importe = rs.getBigDecimal("IMPORTE");
                numero = rs.getBigDecimal("NUMERO");
                if(colectivo != null && !colectivo.equals(""))
                {
                    if(colectivo.equalsIgnoreCase("C1H"))
                    {
                        ret.setC1h_importe(importe);
                        ret.setC1h_numero(numero);
                        
                        esHombre = true;
                        hayTotalH = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C1M"))
                    {
                        ret.setC1m_importe(importe);
                        ret.setC1m_numero(numero);
                        
                        hayTotalM = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C2H"))
                    {
                        ret.setC2h_importe(importe);
                        ret.setC2h_numero(numero);
                        
                        esHombre = true;
                        hayTotalH = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C2M"))
                    {
                        ret.setC2m_importe(importe);
                        ret.setC2m_numero(numero);
                        
                        hayTotalM = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C3H"))
                    {
                        ret.setC3h_importe(importe);
                        ret.setC3h_numero(numero);
                        
                        esHombre = true;
                        hayTotalH = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C3M"))
                    {
                        ret.setC3m_importe(importe);
                        ret.setC3m_numero(numero);
                        
                        hayTotalM = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C4H"))
                    {
                        ret.setC4h_importe(importe);
                        ret.setC4h_numero(numero);
                        
                        esHombre = true;
                        hayTotalH = true;
                    }
                    else if(colectivo.equalsIgnoreCase("C4M"))
                    {
                        ret.setC4m_importe(importe);
                        ret.setC4m_numero(numero);
                        
                        hayTotalM = true;
                    }
                }
                
                if(importe != null)
                {
                    if(esHombre)
                    {
                        totalH = totalH.add(importe);
                    }
                    else
                    {
                        totalM = totalM.add(importe);
                    }
                    
                    total = total.add(importe);
                }
                
                esHombre = false;
            }
            
            if(hayTotalH || hayTotalM)
            {
                ret.setTotal(total);
                if(hayTotalH)
                {
                    ret.setTotalH(totalH);
                }
                else
                {
                    ret.setTotalH(null);
                }
                if(hayTotalM)
                {
                    ret.setTotalM(totalM);
                }
                else
                {
                    ret.setTotalM(null);
                }
            }
            else
            {
                ret.setTotal(null);
            }
            return ret;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public String obtieneSeguimientosCorrectos(String numExp, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call obtiene_seguim_correctos14(?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, numExp);
            st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            String result = st.getString(2);
            return result;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public String obtieneVisitasCorrectas(String numExp, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call obtiene_visitas_correctas14(?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, numExp);
            st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            String result = st.getString(2);
            return result;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    /*public String cargaResumenECA(String numExp, Connection con) throws Exception
    {
        //CallableStatement st = null;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            String result = null;
            
            query = "select CARGA_RESUMEN_ECA("+numExp+") from dual";
            st = con.createStatement();
            rs = st.executeQuery(query);
            if(rs.next())
            {
                result = rs.getString(1);
            }
            return result;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    }*/
    
    public String cargaResumenECA(String numExp, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = null;
            query = "call CARGA_RESUMEN_ECA14(?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, numExp);
            st.registerOutParameter(2, java.sql.Types.VARCHAR);
            int i = st.executeUpdate();
            String result = st.getString(2);
            return result;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
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
    
    public EcaResumenVO getResumenSolicitud(EcaSolicitudVO solicitud, Connection con) throws Exception
    {
        if(solicitud != null && solicitud.getSolicitudCod() != null)
        {
            //CallableStatement st = null;
            Statement st = null;
            ResultSet rs = null;
            EcaResumenVO resumen = null;
            try
            {
                String query = null;

                query = "select * from "+ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESUMEN, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                       +" where ECA_RES_SOLICITUD = "+solicitud.getSolicitudCod();
                st = con.createStatement();
                rs = st.executeQuery(query);
                if(rs.next())
                {
                    resumen = (EcaResumenVO)MeLanbide44MappingUtils.getInstance().map(rs, EcaResumenVO.class);
                }
                return resumen;
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando las áreas", ex);
                throw new Exception(ex);
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
        else
        {
            return null;
        }
    }
    
    public EcaResumenVO guardarResumenSolicitud(EcaResumenVO resumen, Connection con) throws Exception
    {
        Statement st = null;
        try
        {
            String query = null;
            if(resumen.getEcaCodResumen() == null)
            {
                resumen.setEcaCodResumen(this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide44.SEQ_ECA_RESUMEN, ConstantesMeLanbide44.FICHERO_PROPIEDADES), con));
                //Es una solicitud nueva
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESUMEN, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + "(ECA_COD_RESUMEN, ECA_RES_SOLICITUD, ECA_RES_SUB_PRIV, ECA_RES_SUB_PUB, ECA_RES_TOT_SUBV, ECA_RES_SUB_GASTOSGEN)"
                        + " values("+resumen.getEcaCodResumen()
                        + ", "+resumen.getSolicitud()
                        + ", "+(resumen.getEcaResSubPriv() != null ? resumen.getEcaResSubPriv().toString() : "null")
                        + ", "+(resumen.getEcaResSubPub() != null ? resumen.getEcaResSubPub().toString() : "null")
                        + ", "+(resumen.getEcaResTotSubv() != null ? resumen.getEcaResTotSubv().toString() : "null")
                        + ", "+(resumen.getGastosGenerales_pag() != null ? resumen.getGastosGenerales_pag().toString() : "null")
                        + ")";
            }
            else
            {
                //Es una solicitud que ya existe
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide44.TABLA_ECA_RESUMEN, ConstantesMeLanbide44.FICHERO_PROPIEDADES)
                        + " set"
                        + " ECA_RES_SOLICITUD = '"+resumen.getSolicitud()+"'"
                        + ", ECA_RES_SUB_PRIV = "+(resumen.getEcaResSubPriv() != null ? resumen.getEcaResSubPriv().toString() : "null")
                        + ", ECA_RES_SUB_PUB = "+(resumen.getEcaResSubPub() != null ? resumen.getEcaResSubPub().toString() : "null")
                        + ", ECA_RES_TOT_SUBV = "+(resumen.getEcaResTotSubv() != null ? resumen.getEcaResTotSubv().toString() : "null")
                        + ", ECA_RES_SUB_GASTOSGEN = "+(resumen.getGastosGenerales_pag() != null ? resumen.getGastosGenerales_pag().toString() : "null")
                        
                        + " where ECA_COD_RESUMEN = "+resumen.getEcaCodResumen();
            }
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.createStatement();
            int res = st.executeUpdate(query);
            if(res > 0)
            {
                return resumen;
            }
            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
}