/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide57.dao;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConstantesMelanbide57;
import es.altia.flexia.integracion.moduloexterno.melanbide57.util.MeLanbide57MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeGeneralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeRGIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class Melanbide57DAO {

    private static Melanbide57DAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Logger m_Log = LogManager.getLogger(Melanbide57DAO.class.getName());
    //Constantes de la clase
    private final int codTraGestionAreaAsignada1     = 301;
    private final int codTraGestionAreaAsignada2     = 302;
    private final int codTraGestionAreaAsignada3     = 303;
    private final int codTraGestionAreaAsignada4     = 304;
    private final String nombreProcedimiento     = "QUEJA";

    protected Melanbide57DAO() {
                m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");


	}    
     
    public static Melanbide57DAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (Melanbide57DAO.class) {
            if (instance == null) {
                instance = new Melanbide57DAO();
            }

        }
        return instance;
    }     
    

    public List<TramiteVO> getListaTramitesAreas(int codOrganizacion, int ocurrenciaTramite, String numExpediente, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<TramiteVO> listTramites  = new ArrayList<TramiteVO>();
        TramiteVO tramite;
        try
        {
            StringBuilder query = new StringBuilder("SELECT A_UOR.UOR_NOM,  E_TML.TML_VALOR FROM E_CRO INNER JOIN A_UOR ON E_CRO.CRO_UTR = A_UOR.UOR_COD INNER JOIN E_TRA ON (E_CRO.CRO_TRA=E_TRA.TRA_COD AND E_CRO.CRO_PRO=E_TRA.TRA_PRO AND  E_CRO.CRO_MUN=E_TRA.TRA_MUN) INNER JOIN E_TML ON (E_TRA.TRA_COD=E_TML.TML_TRA AND E_TRA.TRA_PRO=E_TML.TML_PRO AND  E_TRA.TRA_MUN=E_TML.TML_MUN)");
            query.append("WHERE E_CRO.CRO_NUM    = '");
            query.append(numExpediente);
            query.append("'");
//            query.append(" AND E_CRO.CRO_OCU = ");
//            query.append(ocurrenciaTramite);
            query.append(" AND E_CRO.CRO_MUN = ");
            query.append(codOrganizacion);
            query.append(" AND (E_TRA.TRA_COU =");
            query.append(codTraGestionAreaAsignada1);
            query.append(" OR E_TRA.TRA_COU   =");
            query.append(codTraGestionAreaAsignada2);
            query.append(" OR E_TRA.TRA_COU   =");
            query.append(codTraGestionAreaAsignada3);
            query.append(" OR E_TRA.TRA_COU   =");
            query.append(codTraGestionAreaAsignada4);
            query.append(") AND E_TML.TML_LENG=1");
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while(rs.next())
            {
                tramite = (TramiteVO)MeLanbide57MappingUtils.getInstance().map(rs, TramiteVO.class);
                listTramites.add(tramite);
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando tramitesAreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listTramites;
    }  

    public List<InformeInternoVO> getListaDatosInformeInternoDerivadas(int codOrganizacion, int ocurrenciaTramite, String strFechaDesde, String strFechaHasta, String codigoTramiteRevision, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<InformeInternoVO> listTramites  = new ArrayList<InformeInternoVO>();
        InformeInternoVO datosInformeInterno;
        try
        {
            /*
            SELECT A_UOR.UOR_NOM,
                A_UOR.UOR_COD,
                mes,
                anyo,
                NVL(totalExpedientes,0) AS totalExpedientes
              FROM (A_UOR
              INNER JOIN E_TRA_UTR
              ON A_UOR.UOR_COD =E_TRA_UTR.TRA_UTR_COD
              INNER JOIN E_TRA
              ON (E_TRA_UTR.TRA_COD =E_TRA.TRA_COD
              AND E_TRA_UTR.TRA_PRO =E_TRA.TRA_PRO
              AND E_TRA_UTR.TRA_MUN = E_TRA.TRA_MUN))
              LEFT JOIN
                (SELECT E_cro.cro_utr ,
                  EXTRACT(MONTH FROM tfet_valor) AS mes ,
                  EXTRACT(YEAR FROM tfet_valor)  AS anyo ,
                  COUNT(DISTINCT(CRO_NUM))       AS totalExpedientes
                FROM E_cro
                INNER JOIN E_TRA
                ON (E_cro.cro_tra =E_TRA.TRA_COD
                AND E_cro.cro_PRO =E_TRA.TRA_PRO
                AND E_cro.CRO_MUN =E_TRA.TRA_MUN)
                INNER JOIN e_tfet
                ON (E_cro.cro_num    =e_tfet.TFET_NUM
                AND E_cro.CRO_MUN    = e_tfet.TFET_MUN
                AND E_cro.cro_PRO    =e_tfet.TFET_PRO)
                WHERE e_tfet.TFET_TRA=2
                AND e_tfet.tfet_cod  ='FECHAQUEJA'
                AND tfet_valor BETWEEN NVL(TO_DATE('01/01/2016','DD/MM/YYYY'),'') AND NVL(TO_DATE('31/12/2016','DD/MM/YYYY'),'')
                AND E_TRA.TRA_PRO='QUEJA'
              AND (E_TRA.TRA_COU =301
              OR E_TRA.TRA_COU   =302
              OR E_TRA.TRA_COU   =303
              OR E_TRA.TRA_COU   =304)
              AND E_TRA.TRA_MUN  =0
                GROUP BY cro_utr,
                  EXTRACT(MONTH FROM tfet_valor),
                  EXTRACT(YEAR FROM tfet_valor)
                ) ON (UOR_COD    = cro_utr)
              WHERE E_TRA.TRA_PRO='QUEJA'
              AND (E_TRA.TRA_COU =301
              OR E_TRA.TRA_COU   =302
              OR E_TRA.TRA_COU   =303
              OR E_TRA.TRA_COU   =304)
              AND E_TRA.TRA_MUN  =0
              GROUP BY A_UOR.UOR_NOM,
                A_UOR.UOR_COD,
                mes,
                anyo,
                totalExpedientes
              ORDER BY A_UOR.UOR_NOM
            */
            StringBuilder query = new StringBuilder("SELECT A_UOR.UOR_NOM, A_UOR.UOR_COD, mes, anyo, NVL(totalExpedientes,0) as totalExpedientes FROM (A_UOR INNER JOIN E_TRA_UTR ON A_UOR.UOR_COD =E_TRA_UTR.TRA_UTR_COD INNER JOIN E_TRA ON (E_TRA_UTR.TRA_COD=E_TRA.TRA_COD AND E_TRA_UTR.TRA_PRO =E_TRA.TRA_PRO and E_TRA_UTR.TRA_MUN = E_TRA.TRA_MUN)) left join (SELECT E_cro.cro_utr ,EXTRACT(month FROM tfet_valor) as mes ,EXTRACT(year FROM tfet_valor) as anyo ,COUNT(distinct(CRO_NUM)) as totalExpedientes FROM E_cro INNER JOIN E_TRA ON (E_cro.cro_tra   =E_TRA.TRA_COD AND E_cro.cro_PRO   =E_TRA.TRA_PRO and E_cro.CRO_MUN   =E_TRA.TRA_MUN) inner join e_tfet on (E_cro.cro_num   =e_tfet.TFET_NUM and E_cro.CRO_MUN= e_tfet.TFET_MUN and E_cro.cro_PRO   =e_tfet.TFET_PRO) WHERE e_tfet.TFET_TRA=");
            query.append(codigoTramiteRevision);
            query.append(" and e_tfet.tfet_cod='FECHAQUEJA' and tfet_valor BETWEEN  NVL(TO_DATE('");
            query.append(strFechaDesde);
            query.append("','DD/MM/YYYY'),'')  AND  NVL(TO_DATE('");
            query.append(strFechaHasta);
            query.append("','DD/MM/YYYY'),'') AND ");
            query.append(getWhereComunInfInt(codOrganizacion));
            query.append(" GROUP BY cro_utr,EXTRACT(month FROM tfet_valor),EXTRACT(year FROM tfet_valor)) ON (UOR_COD   = cro_utr) WHERE ");
            query.append(getWhereComunInfInt(codOrganizacion));
            query.append(" GROUP BY A_UOR.UOR_NOM, A_UOR.UOR_COD, mes, anyo, totalExpedientes order by A_UOR.UOR_NOM");
           
            
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while(rs.next())
            {
                datosInformeInterno = (InformeInternoVO)MeLanbide57MappingUtils.getInstance().map(rs, InformeInternoVO.class);
                listTramites.add(datosInformeInterno);
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listTramites;
    }

    private String getWhereComunInfInt(int codOrganizacion){
        StringBuilder whereComun = new StringBuilder("E_TRA.TRA_PRO='");
        whereComun.append(nombreProcedimiento);
        whereComun.append("' AND (E_TRA.TRA_COU=");
        whereComun.append(codTraGestionAreaAsignada1);
        whereComun.append(" OR E_TRA.TRA_COU   =");
        whereComun.append(codTraGestionAreaAsignada2);
        whereComun.append(" OR E_TRA.TRA_COU   =");
        whereComun.append(codTraGestionAreaAsignada3);
        whereComun.append(" OR E_TRA.TRA_COU   =");
        whereComun.append(codTraGestionAreaAsignada4);
        whereComun.append(") and E_TRA.TRA_MUN=");
        whereComun.append(codOrganizacion);
        return whereComun.toString();
    }

    public List<InformeInternoVO> getListaDatosInformeInternoResueltas(int codOrganizacion, int ocurrenciaTramite, String strFechaDesde, String strFechaHasta, String codigoTramiteRevision, String codigoDesplegableRespuesta1, String codigoDesplegableRespuesta2, String codigoDesplegableRespuesta3, String codigoDesplegableRespuesta4, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<InformeInternoVO> listTramites  = new ArrayList<InformeInternoVO>();
        InformeInternoVO datosInformeInterno;
        try
        {
            /*
            SELECT A_UOR.UOR_NOM,
                A_UOR.UOR_COD,
                mes,
                anyo,
                NVL(totalExpedientes,0) AS totalExpedientes
              FROM (A_UOR
              INNER JOIN E_TRA_UTR
              ON A_UOR.UOR_COD =E_TRA_UTR.TRA_UTR_COD
              INNER JOIN E_TRA
              ON (E_TRA_UTR.TRA_COD =E_TRA.TRA_COD
              AND E_TRA_UTR.TRA_PRO =E_TRA.TRA_PRO
              AND E_TRA_UTR.TRA_MUN = E_TRA.TRA_MUN))
              LEFT JOIN
                (SELECT E_cro.cro_utr ,
                  EXTRACT(MONTH FROM tfet_valor) AS mes ,
                  EXTRACT(YEAR FROM tfet_valor)  AS anyo ,
                  COUNT(distinct(CRO_NUM))                 AS totalExpedientes
                FROM E_cro
                INNER JOIN E_TRA
                ON (E_cro.cro_tra =E_TRA.TRA_COD
                AND E_cro.cro_PRO =E_TRA.TRA_PRO
                AND E_cro.CRO_MUN =E_TRA.TRA_MUN)
                INNER JOIN e_tfet
                ON (E_cro.cro_num    =e_tfet.TFET_NUM
                AND E_cro.CRO_MUN    = e_tfet.TFET_MUN
                 and E_cro.cro_PRO   =e_tfet.TFET_PRO)
                inner join E_TDET 
                on (E_cro.cro_num   =E_TDET.TDET_NUM
                 and E_cro.CRO_MUN= E_TDET.TDET_MUN
                 and E_cro.cro_PRO   =E_TDET.TDET_PRO)
                WHERE e_tfet.TFET_TRA=2
                AND e_tfet.tfet_cod  ='FECHAQUEJA'
                AND tfet_valor BETWEEN NVL(TO_DATE('01/01/2013','DD/MM/YYYY'),'') AND NVL(TO_DATE('31/12/2013','DD/MM/YYYY'),'')
                AND E_TRA.TRA_PRO='QUEJA'
                  AND (E_TRA.TRA_COU =301
                  OR E_TRA.TRA_COU   =302
                  OR E_TRA.TRA_COU   =303
                  OR E_TRA.TRA_COU   =304)
                  AND E_TRA.TRA_MUN  =0

                and ((TDET_COD='RESUELTA1' and TDET_VALOR='S') or (TDET_COD='RESUELTA2' and TDET_VALOR='S') or (TDET_COD='RESUELTA3' and TDET_VALOR='S') or (TDET_COD='RESUELTA4' and TDET_VALOR='S') )

                GROUP BY cro_utr,
                  EXTRACT(MONTH FROM tfet_valor),
                  EXTRACT(YEAR FROM tfet_valor)
                ) ON (UOR_COD    = cro_utr)
              WHERE E_TRA.TRA_PRO='QUEJA'
              AND (E_TRA.TRA_COU =301
              OR E_TRA.TRA_COU   =302
              OR E_TRA.TRA_COU   =303
              OR E_TRA.TRA_COU   =304)
              AND E_TRA.TRA_MUN  =0
              GROUP BY A_UOR.UOR_NOM,
                A_UOR.UOR_COD,
                mes,
                anyo,
                totalExpedientes
              ORDER BY A_UOR.UOR_NOM;
            */
            
            StringBuilder query = new StringBuilder("SELECT A_UOR.UOR_NOM, A_UOR.UOR_COD, mes, anyo, NVL(totalExpedientes,0) as totalExpedientes FROM (A_UOR INNER JOIN E_TRA_UTR ON A_UOR.UOR_COD =E_TRA_UTR.TRA_UTR_COD INNER JOIN E_TRA ON (E_TRA_UTR.TRA_COD=E_TRA.TRA_COD AND E_TRA_UTR.TRA_PRO =E_TRA.TRA_PRO and E_TRA_UTR.TRA_MUN = E_TRA.TRA_MUN)) left join (SELECT E_cro.cro_utr ,EXTRACT(month FROM tfet_valor) as mes ,EXTRACT(year FROM tfet_valor) as anyo ,COUNT(distinct(CRO_NUM)) as totalExpedientes FROM E_cro INNER JOIN E_TRA ON (E_cro.cro_tra   =E_TRA.TRA_COD AND E_cro.cro_PRO   =E_TRA.TRA_PRO and E_cro.CRO_MUN   =E_TRA.TRA_MUN) inner join e_tfet on (E_cro.cro_num   =e_tfet.TFET_NUM and E_cro.CRO_MUN= e_tfet.TFET_MUN and E_cro.cro_PRO   =e_tfet.TFET_PRO) inner join E_TDET on (E_cro.cro_num   =E_TDET.TDET_NUM and E_cro.CRO_MUN= E_TDET.TDET_MUN and E_cro.cro_PRO   =E_TDET.TDET_PRO) WHERE e_tfet.TFET_TRA=");
            query.append(codigoTramiteRevision);
            query.append(" and e_tfet.tfet_cod='FECHAQUEJA' and tfet_valor BETWEEN  NVL(TO_DATE('");
            query.append(strFechaDesde);
            query.append("','DD/MM/YYYY'),'')  AND  NVL(TO_DATE('");
            query.append(strFechaHasta);
//            query.append("','DD/MM/YYYY'),'') and ((TDET_COD='RESUELTA1' and TDET_VALOR='S') or (TDET_COD='RESUELTA2' and TDET_VALOR='S') or (TDET_COD='RESUELTA3' and TDET_VALOR='S') or (TDET_COD='RESUELTA4' and TDET_VALOR='S') ) GROUP BY cro_utr,EXTRACT(month FROM tfet_valor),EXTRACT(year FROM tfet_valor)) ON (UOR_COD   = cro_utr)");
            query.append("','DD/MM/YYYY'),'')  AND ");
            query.append(getWhereComunInfInt(codOrganizacion));
            query.append(" and ((TDET_COD='");
            query.append(codigoDesplegableRespuesta1);
            query.append("' and TDET_VALOR='S') or (TDET_COD='");
            query.append(codigoDesplegableRespuesta2);
            query.append("' and TDET_VALOR='S') or (TDET_COD='");
            query.append(codigoDesplegableRespuesta3);
            query.append("' and TDET_VALOR='S') or (TDET_COD='");
            query.append(codigoDesplegableRespuesta4);
            query.append("' and TDET_VALOR='S') ) GROUP BY cro_utr,EXTRACT(month FROM tfet_valor),EXTRACT(year FROM tfet_valor)) ON (UOR_COD   = cro_utr) WHERE ");
            query.append(getWhereComunInfInt(codOrganizacion));
            query.append(" GROUP BY A_UOR.UOR_NOM, A_UOR.UOR_COD, mes, anyo, totalExpedientes order by A_UOR.UOR_NOM");

            //Informe Resueltas 
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while(rs.next())
            {
                datosInformeInterno = (InformeInternoVO)MeLanbide57MappingUtils.getInstance().map(rs, InformeInternoVO.class);
                listTramites.add(datosInformeInterno);
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listTramites;
    }  

    public List<InformeRGIVO> getListaDatosInformeRGI(int codOrganizacion, String strFechaDesde, String strFechaHasta, int idUsuario, Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<InformeRGIVO> listExpedientes  = new ArrayList<InformeRGIVO>();
        InformeRGIVO datosInformeRGI;
        try
        {
            /*
            SELECT E_EXP.EXP_NUM,--numExpediente
                T_HTE.HTE_AP1,     --interesado
                T_HTE.HTE_AP2,     --interesado
                T_HTE.HTE_NOM,     --interesado
                T_HTE.HTE_DOC,     --interesado
                E_EXP.EXP_ASU,     --asunto
                E_EXP.EXP_FEI,     --fecha inicio expediente??
                E_EXP.EXP_FEF,     --fecha fin expediente??
                a_usu.USU_NOM,     --usuario inicio??
                e_pro.pro_des       --descripción procedimiento
              FROM E_EXP
              INNER JOIN e_ext
              ON E_EXP.EXP_EJE =e_ext.EXT_EJE
              AND E_EXP.EXP_mun=e_ext.EXT_MUN
              AND E_EXP.EXP_pro=e_ext.EXT_PRO
              AND E_EXP.EXP_num=e_ext.EXT_NUM
              INNER JOIN T_HTE
              ON T_HTE.HTE_NVR =E_EXT.EXT_NVR
              AND T_HTE.HTE_TER=E_EXT.EXT_TER
              INNER JOIN e_cro
              ON E_EXP.EXP_MUN =e_cro.CRO_MUN
              AND E_EXP.EXP_EJE=e_cro.CRO_EJE
              AND E_EXP.EXP_NUM=e_cro.CRO_NUM
              AND E_EXP.EXP_PRO=e_cro.CRO_PRO
              INNER JOIN flbgen.a_uou
              ON e_cro.CRO_UTR    =a_uou.UOU_UOR--que la unidad de inicio del trámite pertenezca a alguna de las unidades del usuario conectado
              inner join flbgen.a_usu on E_EXP.exp_usu=a_usu.usu_cod--a_uou.UOU_USU=a_usu.usu_cod--nombre de usuario de inicio
              inner join e_pro on E_EXP.EXP_PRO=e_pro.PRO_COD and E_EXP.EXP_MUN= e_pro.PRO_MUN --descripción del procedimiento
              WHERE E_EXT.Ext_Rol = 1           --interesado
              AND (E_EXP.EXP_PRO  ='APO'
              OR E_EXP.EXP_PRO    ='EXT'
              OR E_EXP.EXP_PRO    ='INC'
              OR E_EXP.EXP_PRO    ='MOD'
              OR E_EXP.EXP_PRO    ='REA'
              OR E_EXP.EXP_PRO    ='REC'
              OR E_EXP.EXP_PRO    ='REI'
              OR E_EXP.EXP_PRO    ='REN'
              OR E_EXP.EXP_PRO    ='REV'
              OR E_EXP.EXP_PRO    ='RGI'
              OR E_EXP.EXP_PRO    ='RKP'
              OR E_EXP.EXP_PRO    ='SUT')
              AND E_EXP.EXP_FEI BETWEEN NVL(TO_DATE('01/01/2013','DD/MM/YYYY'),'') AND NVL(TO_DATE('31/12/2017','DD/MM/YYYY'),'')
              AND a_uou.UOU_USU=5--usuario conectado
              GROUP BY           -- la inner con los trámites del expediente me puede devolver varios registros y también con las distintas unidades del usuario conectado
                E_EXP.EXP_NUM,     --numExpediente
                T_HTE.HTE_AP1,     --interesado
                T_HTE.HTE_AP2,     --interesado
                T_HTE.HTE_NOM,     --interesado
                T_HTE.HTE_DOC,     --interesado
                E_EXP.EXP_ASU,     --asunto
                E_EXP.EXP_FEI,     --fecha inicio expediente??
                E_EXP.EXP_FEF,     --fecha fin expediente??
                a_usu.USU_NOM,     --usuario inicio??
                e_pro.pro_des
                order by e_exp.exp_num
            */
            String selectGroupBy = "E_EXP.EXP_NUM, T_HTE.HTE_AP1, T_HTE.HTE_AP2, T_HTE.HTE_NOM, T_HTE.HTE_DOC, E_EXP.EXP_ASU, E_EXP.EXP_FEI, E_EXP.EXP_FEF, a_usu.USU_NOM, e_pro.pro_des";
            StringBuilder query = new StringBuilder("SELECT ");
            query.append(selectGroupBy);
            query.append(" FROM E_EXP INNER JOIN e_ext ON E_EXP.EXP_EJE =e_ext.EXT_EJE AND E_EXP.EXP_mun=e_ext.EXT_MUN AND E_EXP.EXP_pro=e_ext.EXT_PRO AND E_EXP.EXP_num=e_ext.EXT_NUM INNER JOIN T_HTE ON T_HTE.HTE_NVR =E_EXT.EXT_NVR AND T_HTE.HTE_TER=E_EXT.EXT_TER INNER JOIN e_cro ON E_EXP.EXP_MUN =e_cro.CRO_MUN AND E_EXP.EXP_EJE=e_cro.CRO_EJE AND E_EXP.EXP_NUM=e_cro.CRO_NUM AND E_EXP.EXP_PRO=e_cro.CRO_PRO INNER JOIN flbgen.a_uou ON e_cro.CRO_UTR    =a_uou.UOU_UOR inner join flbgen.a_usu on E_EXP.exp_usu=a_usu.usu_cod inner join e_pro on E_EXP.EXP_PRO=e_pro.PRO_COD and E_EXP.EXP_MUN= e_pro.PRO_MUN WHERE E_EXT.Ext_Rol = ");
            query.append(ConstantesMelanbide57.ROLES.INTERESADO);
            query.append("AND (E_EXP.EXP_PRO  ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.APO);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.EXT);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.INC);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.MOD);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.REA);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.REC);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.REI);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.REN);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.REV);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.RGI);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.RKP);
            query.append("' OR E_EXP.EXP_PRO    ='");
            query.append(ConstantesMelanbide57.PROCEDIMIENTOSRGI.SUT);
            query.append("') AND E_EXP.EXP_FEI BETWEEN  NVL(TO_DATE('");
            query.append(strFechaDesde);
            query.append("','DD/MM/YYYY'),'')  AND  NVL(TO_DATE('");
            query.append(strFechaHasta);
//            query.append("','DD/MM/YYYY'),'') and ((TDET_COD='RESUELTA1' and TDET_VALOR='S') or (TDET_COD='RESUELTA2' and TDET_VALOR='S') or (TDET_COD='RESUELTA3' and TDET_VALOR='S') or (TDET_COD='RESUELTA4' and TDET_VALOR='S') ) GROUP BY cro_utr,EXTRACT(month FROM tfet_valor),EXTRACT(year FROM tfet_valor)) ON (UOR_COD   = cro_utr)");
            query.append("','DD/MM/YYYY'),'')  AND a_uou.UOU_USU=");
            query.append(idUsuario);
            query.append("  AND E_EXP.EXP_MUN= ");
            query.append(codOrganizacion);
            query.append("  GROUP BY ");
            query.append(selectGroupBy);
            query.append(" order by e_exp.exp_num");
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while(rs.next())
            {
                datosInformeRGI = (InformeRGIVO)MeLanbide57MappingUtils.getInstance().map(rs, InformeRGIVO.class);
                listExpedientes.add(datosInformeRGI);
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando los datos del informe RGI", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listExpedientes;
    }  

    public List<InformeGeneralVO> getListaDatosInformeGeneralDesplegable(String codigoCampo, int codOrganizacion/*, int ocurrenciaTramite*/, String strFechaDesde, String strFechaHasta, String codigoTramite, Connection con, boolean bloqueRespuesta) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<InformeGeneralVO> listTramites  = new ArrayList<InformeGeneralVO>();
        InformeGeneralVO datosInformeInterno;
        try
        {
            /*
            SELECT E_DES_VAL.DES_NOM as descripcion, mes , anyo , totalExp as totalExpedientes
            FROM E_DES_VAL
            inner join E_TCA ON (E_TCA.TCA_DESPLEGABLE=E_DES_VAL.DES_COD)
            left join 
            (select E_TDET.TDET_VALOR, TDET_MUN, TDET_PRO, TDET_TRA, TDET_COD, EXTRACT(MONTH FROM tfet_valor) AS mes ,
            EXTRACT(YEAR FROM tfet_valor)  AS anyo, COUNT(TDET_VALOR) as totalExp
            from E_TDET 
            inner JOIN e_tfet
            ON (e_tfet.TFET_NUM=E_TDET.TDET_NUM AND e_tfet.TFET_MUN=E_TDET.TDET_MUN AND e_tfet.TFET_PRO=E_TDET.TDET_PRO)
            where e_tfet.tfet_cod  ='FECHAQUEJA' AND tfet_valor BETWEEN NVL(TO_DATE('01/01/2013','DD/MM/YYYY'),'') AND NVL(TO_DATE('31/12/2013','DD/MM/YYYY'),'')
            GROUP BY E_TDET.TDET_VALOR, TDET_MUN, TDET_PRO, TDET_TRA, TDET_COD, EXTRACT(MONTH FROM tfet_valor), EXTRACT(YEAR FROM tfet_valor)
            )
            on (TDET_VALOR=E_DES_VAL.DES_VAL_COD and TDET_MUN=E_TCA.TCA_MUN and TDET_PRO=E_TCA.TCA_PRO and TDET_TRA=E_TCA.TCA_TRA AND TDET_COD=E_TCA.TCA_COD)
            WHERE E_TCA.TCA_PRO='QUEJA' AND E_TCA.TCA_COD='TIPOINTERLOCUTOR' AND E_TCA.TCA_TRA=2
            AND E_TCA.TCA_MUN=0
            GROUP BY E_DES_VAL.DES_NOM, mes, anyo, totalExp order by E_DES_VAL.DES_NOM
            */
            /*
            *********** SELECT PARA BLOQUE RESPUESTA******************
            SELECT DECODE (E_DES_VAL.DES_NOM, 'RESPONDIDAS', 'Sí', 'No') as descripcion, mes , anyo , count(totalExp) as totalExpedientes
            FROM E_DES_VAL
            inner join E_TCA ON (E_TCA.TCA_DESPLEGABLE=E_DES_VAL.DES_COD)
            left join 
            (select E_TDET.TDET_VALOR, TDET_MUN, TDET_PRO, TDET_TRA, TDET_COD, EXTRACT(MONTH FROM tfet_valor) AS mes ,
            EXTRACT(YEAR FROM tfet_valor)  AS anyo, COUNT(TDET_VALOR) as totalExp
            from E_TDET 
            inner JOIN e_tfet
            ON (e_tfet.TFET_NUM=E_TDET.TDET_NUM AND e_tfet.TFET_MUN=E_TDET.TDET_MUN AND e_tfet.TFET_PRO=E_TDET.TDET_PRO)
            where e_tfet.tfet_cod  ='FECHAQUEJA' AND tfet_valor BETWEEN NVL(TO_DATE('01/01/2013','DD/MM/YYYY'),'') AND NVL(TO_DATE('31/12/2013','DD/MM/YYYY'),'')
            GROUP BY E_TDET.TDET_VALOR, TDET_MUN, TDET_PRO, TDET_TRA, TDET_COD, EXTRACT(MONTH FROM tfet_valor), EXTRACT(YEAR FROM tfet_valor)
            )
            on (TDET_VALOR=E_DES_VAL.DES_VAL_COD and TDET_MUN=E_TCA.TCA_MUN and TDET_PRO=E_TCA.TCA_PRO and TDET_TRA=E_TCA.TCA_TRA AND TDET_COD=E_TCA.TCA_COD)
            WHERE E_TCA.TCA_PRO='QUEJA' AND E_TCA.TCA_COD='NIVRESPQUEJA1' AND E_TCA.TCA_TRA=8
            AND E_TCA.TCA_MUN=0
            GROUP BY (DECODE (E_DES_VAL.DES_VAL_COD, '3', 'Sí', 'No')), mes, anyo
            */
            StringBuilder query = new StringBuilder("SELECT ");
            if(bloqueRespuesta){
                //TODO: mejorar el decode porque el valor respondidas igual debería estar en el properties?
                query.append("DECODE (E_DES_VAL.DES_VAL_COD, '3', 'Sí', 'No')");
            }else{
                query.append("E_DES_VAL.DES_NOM");
            }
            query.append(" as descripcion, mes , anyo , ");
//            if(bloqueRespuesta){
//                query.append("count(totalExp)");
//            }else{
                query.append("totalExp");
//            }
            query.append(" as totalExpedientes FROM E_DES_VAL inner join E_TCA ON (E_TCA.TCA_DESPLEGABLE=E_DES_VAL.DES_COD) left join (select E_TDET.TDET_VALOR, TDET_MUN, TDET_PRO, TDET_TRA, TDET_COD, EXTRACT(MONTH FROM tfet_valor) AS mes , EXTRACT(YEAR FROM tfet_valor)  AS anyo, COUNT(TDET_VALOR) as totalExp from E_TDET inner JOIN e_tfet ON (e_tfet.TFET_NUM=E_TDET.TDET_NUM AND e_tfet.TFET_MUN=E_TDET.TDET_MUN AND e_tfet.TFET_PRO=E_TDET.TDET_PRO) where e_tfet.tfet_cod  ='FECHAQUEJA' AND tfet_valor BETWEEN NVL(TO_DATE('");
            query.append(strFechaDesde);
            query.append("','DD/MM/YYYY'),'')  AND  NVL(TO_DATE('");
            query.append(strFechaHasta);
            query.append("','DD/MM/YYYY'),'') GROUP BY E_TDET.TDET_VALOR, TDET_MUN, TDET_PRO, TDET_TRA, TDET_COD, EXTRACT(MONTH FROM tfet_valor), EXTRACT(YEAR FROM tfet_valor)) on (TDET_VALOR=E_DES_VAL.DES_VAL_COD and TDET_MUN=E_TCA.TCA_MUN and TDET_PRO=E_TCA.TCA_PRO and TDET_TRA=E_TCA.TCA_TRA AND TDET_COD=E_TCA.TCA_COD) WHERE E_TCA.TCA_PRO='");
            query.append(nombreProcedimiento);
            query.append("' AND E_TCA.TCA_COD='");
            query.append(codigoCampo);
            query.append("' AND E_TCA.TCA_TRA=");
            query.append(codigoTramite);
            query.append(" AND E_TCA.TCA_MUN=");
            query.append(codOrganizacion);
            if(bloqueRespuesta){
//                query.append(" GROUP BY (DECODE (E_DES_VAL.DES_VAL_COD, '3', 'Sí', 'No')), mes, anyo");
                query.append(" GROUP BY (DECODE (E_DES_VAL.DES_VAL_COD, '3', 'Sí', 'No')), mes, anyo, totalExp order by DECODE (E_DES_VAL.DES_VAL_COD, '3', 'Sí', 'No')");
            }else{
                query.append(" GROUP BY E_DES_VAL.DES_NOM, mes, anyo, totalExp order by E_DES_VAL.DES_NOM");
            }
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while(rs.next())
            {
                datosInformeInterno = (InformeGeneralVO)MeLanbide57MappingUtils.getInstance().map(rs, InformeGeneralVO.class);
                listTramites.add(datosInformeInterno);
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listTramites;
    }  
    
    
    public ArrayList<String> recogerExpedientesAFinalizar(AdaptadorSQLBD adapt) throws Exception  {
        Connection con = null;
        //AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        String expediente = "";
        ArrayList<String> expedientes = new ArrayList<String>();
        ResultSet rs = null;
        Statement st = null;
        try{
           
            String sql = "Select distinct EXP_NUM From E_Exp\n " +
                    "inner join E_EXT ON Ext_Num = Exp_Num And Ext_Eje = Exp_Eje " +
                    "Where Exp_Pro = 'QUEJA' And Exp_FEF < (select TO_CHAR(sysdate - interval '1' year,'DD/MM/YYYY') from dual) " + 
                    " And Ext_Num Not In (SELECT ExT_Num FROM E_EXT WHERE EXT_TER = 1)";
            if(m_Log.isDebugEnabled()) m_Log.error("Recogiendo notificaciones pendientes: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            while(rs.next()){ //Puede tener varias notificaciones pero vamos a devolver la ultima
                m_Log.error("expediente recogida: " + rs.getString("EXP_NUM"));
                expediente = rs.getString("EXP_NUM");
                expedientes.add(expediente);
            }
        }
        catch(Exception e){
            m_Log.error("Error en recogerExpedientesAFinalizar: " + e);
            e.printStackTrace();
            throw e;
        }
        finally{
            if(con != null)
                con.close();
            if(st!=null) st.close();
            if(rs!=null) rs.close();                
        }
        return expedientes;
    }
    
    public int actualizaTercero(String expediente, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            
                query = "UPDATE E_EXT SET EXT_TER =1, EXT_NVR =1" 
                    + " WHERE EXT_NUM = '" + expediente + "' AND EXT_ROL = 1";

                if(m_Log.isDebugEnabled()) 
                    m_Log.error("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            m_Log.error("Error en finalizarExpediente: " + ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                m_Log.error("Procedemos a cerrar el statement y el resultset");

            if(con != null)
                con.close();
            if(st!=null) st.close();
        }
        return result;
    }
    
    public int eliminaTercero(String expediente, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            
                query = "DELETE FROM E_EXT WHERE EXT_NUM = '" + expediente + "' ";

                if(m_Log.isDebugEnabled()) 
                    m_Log.error("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            m_Log.error("Error en finalizarExpediente: " + ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                m_Log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }
        return result;
    }
    
    public int insertaTercero(String expediente, int codOrg, String ano, Connection con) throws Exception
    {
        Statement st = null;
        int result = 0;
        boolean nuevo = true;
        try
        {
            String query = null;
            
            
                query = "INSERT INTO E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_ROL, EXT_PRO, MOSTRAR, EXT_DOT)"
                        + "VALUES ("+codOrg+", "+ano+", '"+expediente+"', 1, 1, 1, 'QUEJA', 1, 3)";

                if(m_Log.isDebugEnabled()) 
                    m_Log.error("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
        }
        catch(Exception ex)
        {
            m_Log.error("Error en finalizarExpediente: " + ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                m_Log.error("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }
        return result;
    }
    
    
}
