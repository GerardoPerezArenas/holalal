package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteETraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43GestionTramitesExpedienteRespuesta;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.MeLanbide43JobsTareaEje;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tram9099CerrarVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide43Tramite9099DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide43Tramite9099DAO.class);
    private static final SimpleDateFormat dateFormatddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");

    //Instancia
    private static MeLanbide43Tramite9099DAO instance = null;

    public static MeLanbide43Tramite9099DAO getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : BEGIN");
        }
        if (instance == null) {
            synchronized (MeLanbide43Tramite9099DAO.class) {
                if (instance == null) {
                    instance = new MeLanbide43Tramite9099DAO();
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : END");
        }
        return instance;
    }

    public List<Tram9099CerrarVO> getExpedientesFallidos9099(Connection con) throws Exception {

        List<Tram9099CerrarVO> tramite9099VOList = new ArrayList<Tram9099CerrarVO>();
        Statement st = null;
        ResultSet rs = null;

        try {
            //prueba DESA 2025/ATASE/000002
            /*
            String query = "select exp_num numexpediente,\n" +
                    "(select tra_cod from e_tra where tra_cou=9099 and tra_fba is null\n" +
                    "and tra_pro=substr(exp_num,6,length(exp_num)-12)) cod_tra,\n" +
                    "(select max(cro_ocu) from e_cro where cro_num=exp_num and cro_tra=\n" +
                    "(\n" +
                    "select tra_cod from e_tra where tra_cou=9099 and tra_fba is null\n" +
                    "and tra_pro=substr(exp_num,6,length(exp_num)-12)\n" +
                    ")\n" +
                    ") ocu_tra\n" +
                    "from e_exp\n" +
                    "where exp_num='2025/ATASE/000002'";
             */
            String query = "select distinct numexpediente,\n" +
                    "(select tra_cod from e_tra where tra_cou=9099 and tra_fba is null\n" +
                    "and tra_pro=substr(exp_num,6,length(exp_num)-12)) cod_tra,\n" +
                    "nvl(\n" +
                    "(select max(cro_ocu) from e_cro where cro_num=exp_num and cro_tra=\n" +
                    "(\n" +
                    "select tra_cod from e_tra where tra_cou=9099 and tra_fba is null\n" +
                    "and tra_pro=substr(exp_num,6,length(exp_num)-12)\n" +
                    ")\n" +
                    ")\n" +
                    ",1)\n" +
                    " ocu_tra\n" +
                    "from melanbide43_tramite9099_job mc\n" +
                    "inner join e_exp e on e.exp_num=mc.numexpediente and e.exp_est=0\n" +
                    "where numexpediente not in (\n" +
                    "    select distinct e.exp_num from e_exp e\n" +
                    "    inner join e_cro c on c.cro_mun=e.exp_mun and c.cro_pro=e.exp_pro and c.cro_eje=e.exp_eje and c.cro_num=e.exp_num\n" +
                    "    inner join (\n" +
                    "        select pr.pro_cod pro,tr.tra_cod cod_9099_interno\n" +
                    "        from e_tra tr \n" +
                    "        inner join e_pro pr on pr.pro_cod=tr.tra_pro \n" +
                    "        where TR.TRA_COU=9099\n" +
                    "        and tr.tra_fba is null\n" +
                    "    ) ct\n" +
                    "     on ct.pro=e.exp_pro and ct.cod_9099_interno=c.cro_tra\n" +
                    "    where e.exp_est=0 and c.cro_fef is null\n" +
                    ")\n" +
                    "--el expediente está en MiCarpeta\n" +
                    "and numexpediente in (\n" +
                    "select exp_num from melanbide43_integmisgest where tipo_operacion='I' and resultado_proceso=1\n" +
                    ")\n" +
                    "--que existe el trámite\n" +
                    "and (select tra_cod from e_tra where tra_cou=9099 and tra_fba is null\n" +
                    "and tra_pro=substr(exp_num,6,length(exp_num)-12)) is not null\n" +
                    "order by numexpediente";

            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Tram9099CerrarVO tramite9099VO = new Tram9099CerrarVO();
                tramite9099VO.setNumExpediente(rs.getString("numexpediente"));
                tramite9099VO.setCodTramite(rs.getInt("cod_tra"));
                tramite9099VO.setOcuTramite(rs.getInt("ocu_tra"));
                tramite9099VOList.add(tramite9099VO);
            }
        } catch (Exception ex) {
            log.error("Error en getExpedientesFallidos9099 " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getExpedientesFallidos9099 END: " + tramite9099VOList.size());
        return tramite9099VOList;
    }

    public List<Tram9099CerrarVO> getExpedientesCerrar9099(Connection con) throws Exception {

        List<Tram9099CerrarVO> tramite9099VOList = new ArrayList<Tram9099CerrarVO>();
        Statement st = null;
        ResultSet rs = null;

        try {
            String query = "--select definitiva con expedientes a avanzar en Regexlan y cerrar la espera en MiCarpeta si corresponde\n" +
                    "select distinct tr.cro_num exp,tr.cro_pro proc,tr.cro_eje eje,tr.cro_utr utr,\n" +
                    "--lt.cod_tra_origen tra_anterior,lt.ocu_tra_origen ocu_anterior,\n" +
                    "dt.tra_cod tra_espera,\n" +
                    "(select tml_valor from e_tml where tml_mun=dt.tra_mun and tml_pro=dt.tra_pro and tml_tra=dt.tra_cod) des_espera,\n" +
                    "tr.cro_ocu ocu_espera,case when mci.resultado_proceso=1 then 'S' else 'N' end en_micarpeta,tra_sal.tra_salida,\n" +
                    "(select tra_cou from e_tra where tra_fba is null and tra_mun=dt.tra_mun and tra_pro=dt.tra_pro and tra_cod=tra_sal.tra_salida) cou_tra_salida,\n" +
                    "(select tml_valor from e_tml where tml_mun=dt.tra_mun and tml_pro=dt.tra_pro and tml_tra=tra_sal.tra_salida) des_salida,\n" +
                    "                case when nvl((select max(cro_ocu) from e_cro where cro_mun=e.exp_mun and cro_pro=e.exp_pro and cro_eje=e.exp_eje and cro_num=e.exp_num and cro_tra=tra_sal.tra_salida and cro_fef is null),0) > 0 then 0\n" +
                    "                   else nvl((select max(cro_ocu) from e_cro where cro_mun=e.exp_mun and cro_pro=e.exp_pro and cro_eje=e.exp_eje and cro_num=e.exp_num and cro_tra=tra_sal.tra_salida and cro_fef is not null),0)+1 end ocu_salida   \n" +
                    "                --0 si ya está abierto el trámite\n" +
                    "from e_tra dt\n" +
                    "inner join e_cro tr\n" +
                    " on tr.cro_mun=dt.tra_mun and tr.cro_pro=dt.tra_pro and tr.cro_tra=dt.tra_cod\n" +
                    "left join melanbide43_integmisgest mci\n" +
                    " on mci.exp_num=tr.cro_num and mci.tipo_operacion='I' and mci.resultado_proceso=1\n" +
                    "inner join e_exp e\n" +
                    " on e.exp_mun=tr.cro_mun and e.exp_eje=tr.cro_eje and e.exp_pro=tr.cro_pro and e.exp_num=tr.cro_num\n" +
                    "inner join (\n" +
                    "    select distinct s1.fls_mun,s1.fls_pro,s1.fls_tra esp_recurso,s1.fls_cts tra_salida from e_fls s1\n" +
                    "    inner join (\n" +
                    "    select s.fls_mun,s.fls_pro,s.fls_tra,max(s.fls_nuc) cond from e_fls s\n" +
                    "    inner join e_tra dt\n" +
                    "     on dt.tra_mun=s.fls_mun and dt.tra_pro=s.fls_pro and dt.tra_cod=s.fls_tra and dt.tra_cou=9099 and dt.tra_fba is null\n" +
                    "    group by s.fls_mun,s.fls_pro,s.fls_tra\n" +
                    "    ) sal\n" +
                    "     on sal.fls_mun=s1.fls_mun and sal.fls_pro=s1.fls_pro and sal.fls_tra=s1.fls_tra and sal.cond=s1.fls_nuc\n" +
                    ") tra_sal\n" +
                    " on tra_sal.fls_mun=e.exp_mun and tra_sal.fls_pro=e.exp_pro and tra_sal.esp_recurso=dt.tra_cod\n" +
                    "--de momento se quita, a veces no está bien la tabla list_tram_orig y seguramente no haga falta el dato del trámite que abrió la espera\n" +
                    "/*\n" +
                    "left join list_tram_orig lt \n" +
                    " on lt.cod_mun=e.exp_mun and lt.ejercicio=e.exp_eje and lt.cod_pro=e.exp_pro and lt.num_exp=e.exp_num and lt.cod_tra_destino=dt.tra_cod and lt.ocu_tra_destino=tr.cro_ocu\n" +
                    " */\n" +
                    "where dt.tra_cou=9099 and dt.tra_fba is null\n" +
                    "and tr.cro_fef is null\n" +
                    "and trunc(sysdate)-trunc(cro_fei) > (case when dt.tra_plz is null then 30 when dt.tra_und='M' then 30*dt.tra_plz when dt.tra_und='N' or dt.tra_und='H' then dt.tra_plz end)\n" +
                    "--expedientes pendientes en Regexlan (ni anulados ni finalizados)\n" +
                    "and e.exp_est=0\n" +
                    "order by tr.cro_num";

            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                Tram9099CerrarVO tramite9099VO = new Tram9099CerrarVO();
                tramite9099VO.setEjercicio(rs.getInt("eje"));
                tramite9099VO.setCodProcedimiento(rs.getString("proc"));
                tramite9099VO.setNumExpediente(rs.getString("exp"));
                tramite9099VO.setCodTramite(rs.getInt("tra_espera"));
                tramite9099VO.setDesTramite(rs.getString("des_espera"));
                tramite9099VO.setOcuTramite(rs.getInt("ocu_espera"));
                tramite9099VO.setEnMiCarpeta(rs.getString("en_micarpeta"));
                tramite9099VO.setCodTramiteSal(rs.getInt("tra_salida"));
                tramite9099VO.setCodTramiteSalVis(rs.getInt("cou_tra_salida"));
                tramite9099VO.setDesTramiteSal(rs.getString("des_salida"));
                tramite9099VO.setOcuTramiteSal(rs.getInt("ocu_salida"));
                tramite9099VO.setUor(rs.getInt("utr"));
                tramite9099VOList.add(tramite9099VO);
            }
        } catch (Exception ex) {
            log.error("Error en getExpedientesCerrar9099 " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getExpedientesCerrar9099 END: " + tramite9099VOList.size());
        return tramite9099VOList;
    }

    public int insertarLineasLogJob(Connection con, String numeroExpediente, String estado, String mensaje, String enMiCarpeta, int traEspera, int ocuEspera, int traSalida, int ocuSalida) throws Exception {
        log.info("insertarLineasLogJob MELANBIDE_43_TRAMITE9099_JOB - Begin () ");
        int id = 0;
        CallableStatement pt = null;
        ResultSet rs = null;

        try {
            String query = "INSERT INTO MELANBIDE43_TRAMITE9099_JOB"
                    + " ( NUMEXPEDIENTE, ESTADOLOG, DESCRIPCION, EN_MICARPETA, TRA_ESPERA, OCU_ESPERA, TRA_SALIDA, OCU_SALIDA) "
                    + " VALUES (?,?,?,?,?,?,?,?)";
            log.debug("query: " + query);
            log.debug("----Fin aplicar condiciones----");

            pt = con.prepareCall(query);
            pt.setString(1, numeroExpediente);
            pt.setString(2, estado);
            pt.setString(3, mensaje);
            pt.setString(4, enMiCarpeta);
            pt.setInt(5,traEspera);
            pt.setInt(6,ocuEspera);
            pt.setInt(7,traSalida);
            pt.setInt(8,ocuSalida);

            log.debug("sql = " + query);
            pt.executeUpdate();

        } catch (Exception ex) {
            log.error("Se ha producido un error al registrar la linea en el log del job CierreTramiteEspera9099Job ", ex);
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

        log.debug("insertarLineasLogJob MELANBIDE_43_TRAMITE9099_JOB - End () ");
        return id;
    }

                }
