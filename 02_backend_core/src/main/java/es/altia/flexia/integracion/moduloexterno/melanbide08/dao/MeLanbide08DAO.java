package es.altia.flexia.integracion.moduloexterno.melanbide08.dao;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide08.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide08.util.ConstantesMeLanbide08;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ResultadoConsultaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.TramitacionExpedientesValueObject;
import es.altia.flexia.portafirmasexternocliente.factoria.PluginPortafirmasExternoClienteFactoria;
import es.altia.flexia.portafirmasexternocliente.plugin.PluginPortafirmasExternoCliente;
import es.altia.flexia.portafirmasexternocliente.plugin.lanbide.servicios.LanbideEstadoPortafirmasManager;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author alexandrep
 */
public class MeLanbide08DAO {
    
     protected static Config m_ConfigTechnical; 
      protected static String crd_mun;
    protected static String crd_pro;
    protected static String crd_eje;
    protected static String crd_num;
    protected static String crd_tra;
    protected static String crd_ocu;
    protected static String crd_nud;
    protected static String crd_fal;
    protected static String crd_fmo;
    protected static String crd_usc;
    protected static String crd_usm;
    protected static String crd_fil;
    protected static String crd_des;
    protected static String crd_dot;
    protected static String crd_fir_est;
    protected static String crd_exp_fd;
    protected static String crd_doc_fd;
    protected static String crd_fir_fd;
    protected static String crd_exp;
    protected static String crd_cod_pf_ext;
    protected static String crd_id_pf_ext;
    
      protected static String usu_cod;
    protected static String usu_nom;

    protected static String dot_mun;
    protected static String dot_pro;
    protected static String dot_tra;
    protected static String dot_cod;
    protected static String dot_plt;
    protected static String dot_activo;
   

    private static final Logger log = LogManager.getLogger(MeLanbide08DAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //Instancia
    private static MeLanbide08DAO instance = null;

    // Constructor
    private MeLanbide08DAO() {
        
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
  
        crd_mun = m_ConfigTechnical.getString("SQL.E_CRD.codMunicipio");
        crd_pro = m_ConfigTechnical.getString("SQL.E_CRD.codProcedimiento");
        crd_eje = m_ConfigTechnical.getString("SQL.E_CRD.ejercicio");
        crd_num = m_ConfigTechnical.getString("SQL.E_CRD.numeroExpediente");
        crd_tra = m_ConfigTechnical.getString("SQL.E_CRD.codTramite");
        crd_ocu = m_ConfigTechnical.getString("SQL.E_CRD.ocurrencia");
        crd_nud = m_ConfigTechnical.getString("SQL.E_CRD.numeroDocumento");
        crd_fal = m_ConfigTechnical.getString("SQL.E_CRD.fechaAlta");
        crd_fmo = m_ConfigTechnical.getString("SQL.E_CRD.fechaModificacion");
        crd_usc = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioCreac");
        crd_usm = m_ConfigTechnical.getString("SQL.E_CRD.codUsuarioModif");
        crd_fil = m_ConfigTechnical.getString("SQL.E_CRD.fichero");
        crd_des = m_ConfigTechnical.getString("SQL.E_CRD.descripcion");
        crd_dot = m_ConfigTechnical.getString("SQL.E_CRD.codDocumento");
        crd_fir_est = m_ConfigTechnical.getString("SQL.E_CRD.estadoFirma");
        crd_exp_fd = m_ConfigTechnical.getString("SQL.E_CRD.expedienteFirmaDoc");
        crd_doc_fd = m_ConfigTechnical.getString("SQL.E_CRD.documentoFirmaDoc");
        crd_fir_fd = m_ConfigTechnical.getString("SQL.E_CRD.firmaFirmaDoc");
        crd_exp = m_ConfigTechnical.getString("SQL.E_CRD.expedientes");
  
  
  usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
        usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");

        dot_mun = m_ConfigTechnical.getString("SQL.E_DOT.codMunicipio");
        dot_pro = m_ConfigTechnical.getString("SQL.E_DOT.codProcedimiento");
        dot_tra = m_ConfigTechnical.getString("SQL.E_DOT.codTramite");
        dot_cod = m_ConfigTechnical.getString("SQL.E_DOT.codDocumento");
        dot_plt = m_ConfigTechnical.getString("SQL.E_DOT.codPlantilla");
        dot_activo = m_ConfigTechnical.getString("SQL.E_DOT.activo");
    }

    
    public static MeLanbide08DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide08DAO.class) {
                instance = new MeLanbide08DAO();
            }
        }
        return instance;
    }
    
    public List<ResultadoConsultaVO> getExpedientesFirmadosSinPaginar(int codOrganizacion, Connection con, FiltrosVO filtros, boolean exportar,List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getExpedientesFirmadosSinPaginar - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaVO> retorno = new ArrayList<ResultadoConsultaVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "";
            if (exportar) {
                query = " ";
            } else {
                query = "SELECT crd.*, case when adnot.num_expediente is null then 0 else 1 end as doc_notificado"
                        + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide08.NOMBRE_TABLA_DOCUMENTOS_ENVIADOS_FIRMAR, ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " crd"
                        + " LEFT OUTER JOIN " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.NOMBRE_TABLA_DOCUMENTOS_NOTIFICADOS,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " adnot ON adnot.cod_tramite = crd.crd_tra"
                        + " AND adnot.num_expediente = crd.crd_num"
                        + " AND adnot.ocu_tramite = crd.crd_ocu"
                        + " LEFT OUTER JOIN " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " log ON log.numexpediente = crd.crd_num"
                        + " AND log.crdnud = crd.crd_nud";

            }
            if (filtros != null) {
                log.info("params : " + filtros.getStart() + ", "
                        + filtros.getFinish() + ", "
                        + filtros.getEjercicio() + ", "
                        + filtros.getProcedimiento() + ", "
                        + filtros.getEstadoFirma() + ", "
                        + filtros.getTipoDocumento() + ", "
                        + filtros.getFechaEnvioPeticion() + "."
                );

                //Por si se buscan no enviados al portafirmas
                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && "XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> Enviados a portafirmas");
                    query += " WHERE crd.crd_fir_est IS NULL ";
                } else {
                    query += " WHERE crd.crd_fir_est IS NOT NULL ";
                }

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND crd.CRD_EJE = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND crd.CRD_PRO = '" + filtros.getProcedimiento() + "'";

                }else {
                    //Obtenemos los procedimientos que puede consultar el usuario
                  String  listaProcedimientos ="(";
                for (int i = 0; i < listaProcedimiento.size(); i++) {
                String procedimiento = listaProcedimiento.get(i).getId();
                if (listaProcedimientos == "(") {
                    listaProcedimientos = listaProcedimientos + "'" + procedimiento + "'";
                } else {
                    listaProcedimientos = listaProcedimientos + "," + "'" + procedimiento + "'";
                }

            }
            listaProcedimientos = listaProcedimientos + ")";
                    
                     query += " AND crd.CRD_PRO IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND crd.CRD_NUM = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && !"XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> getEstadoFirma");

                    query += " AND crd.CRD_FIR_EST = '" + filtros.getEstadoFirma() + "'";

                }

                if (filtros.getFechaEnvioPeticion() != null && !filtros.getFechaEnvioPeticion().isEmpty()) {
                    log.info("aplicando condición --> fechaEnvioPeticion");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')='" + filtros.getFechaEnvioPeticion() + "'";

                }

                if (filtros.getFechaEnvioPeticionDesde() != null && !filtros.getFechaEnvioPeticionDesde().isEmpty()) {
                    log.info("aplicando condición --> FechaEnvioPeticionDesde");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')>='" + filtros.getFechaEnvioPeticionDesde() + "'";

                }

                if (filtros.getFechaEnvioPeticionHasta() != null && !filtros.getFechaEnvioPeticionHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioPeticionHasta");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')<='" + filtros.getFechaEnvioPeticionHasta() + "'";

                }

                if (filtros.getTipoDocumento() != null && !filtros.getTipoDocumento().isEmpty()) {
                    log.info("aplicando condición --> tipoDocumento");
                    //Desa va mas rapido dejamos el LIKE, dejamos el substr por si se debe modificar mas adelante
                    // query += " substr(crd.crd_des,-3) ='" + filtros.getTipoDocumento() + "'";
                    query += " AND crd.crd_des LIKE '%" + filtros.getTipoDocumento() + "'";
                }
                
                query += "  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  ) ";

                log.info("----Fin aplicar condiciones----");

            }
            if (exportar) {
                query += " ORDER BY crd.crd_fal";
            } else {
                query += " ORDER BY crd.crd_fal DESC";
            }
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaVO elementoListaRetorno = new ResultadoConsultaVO();

                elementoListaRetorno.setEjercicio(rs.getString("CRD_EJE"));
                elementoListaRetorno.setCodigoMunicipio(rs.getString("CRD_MUN"));
                elementoListaRetorno.setCodigoDocumentoAnterior(rs.getString("CRD_NUD"));
                elementoListaRetorno.setCodTramite(rs.getString("CRD_TRA"));
                elementoListaRetorno.setOcurrenciaTramite(rs.getString("CRD_OCU"));
                elementoListaRetorno.setProcedimiento(rs.getString("CRD_PRO"));
                elementoListaRetorno.setNumExpediente(rs.getString("CRD_NUM"));
                elementoListaRetorno.setDescDocumento(rs.getString("CRD_DES"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("CRD_FAL"));
                elementoListaRetorno.setFechaFirmado(rs.getString("CRD_FMO"));
                elementoListaRetorno.setEstado(rs.getString("CRD_FIR_EST"));
                elementoListaRetorno.setDescEstado(descripcionEstado(rs.getString("CRD_FIR_EST")));
                elementoListaRetorno.setDocNotificado(rs.getInt("doc_notificado") == 1 ? "SÍ" : "NO");
                retorno.add(elementoListaRetorno);
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
        log.info("getExpedientesFirmadosSinPaginar - End () " + formatDate.format(new Date()));
        return retorno;
    }



    public List<ResultadoConsultaVO> getExpedientesFirmados(int codOrganizacion, Connection con, FiltrosVO filtros, boolean exportar,List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getExpedientesFirmados - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaVO> retorno = new ArrayList<ResultadoConsultaVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "";
            if (exportar) {
                query = " ";
            } else {
                query = "SELECT * FROM ("
                        + " SELECT I.*, ROWNUM rnum FROM ("
                        + " SELECT crd.*, case when adnot.num_expediente is null then 0 else 1 end as doc_notificado"
                        + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide08.NOMBRE_TABLA_DOCUMENTOS_ENVIADOS_FIRMAR, ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " crd"
                        + " LEFT OUTER JOIN " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.NOMBRE_TABLA_DOCUMENTOS_NOTIFICADOS,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " adnot ON adnot.cod_tramite = crd.crd_tra"
                        + " AND adnot.num_expediente = crd.crd_num"
                        + " AND adnot.ocu_tramite = crd.crd_ocu"
                         + " LEFT OUTER JOIN " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " log ON log.numexpediente = crd.crd_num"
                        + " AND log.crdnud = crd.crd_nud";

            }
            if (filtros != null) {
                log.info("params : " + filtros.getStart() + ", "
                        + filtros.getFinish() + ", "
                        + filtros.getEjercicio() + ", "
                        + filtros.getProcedimiento() + ", "
                        + filtros.getEstadoFirma() + ", "
                        + filtros.getTipoDocumento() + ", "
                        + filtros.getFechaEnvioPeticion() + "."
                );

                //Por si se buscan no enviados al portafirmas
                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && "XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> Enviados a portafirmas");
                    query += " WHERE crd.crd_fir_est IS NULL ";
                } else {
                    query += " WHERE crd.crd_fir_est IS NOT NULL ";
                }

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND crd.CRD_EJE = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND crd.CRD_PRO = '" + filtros.getProcedimiento() + "'";

                }else {
                    //Obtenemos los procedimientos que puede consultar el usuario
                  String  listaProcedimientos ="(";
                for (int i = 0; i < listaProcedimiento.size(); i++) {
                String procedimiento = listaProcedimiento.get(i).getId();
                if (listaProcedimientos == "(") {
                    listaProcedimientos = listaProcedimientos + "'" + procedimiento + "'";
                } else {
                    listaProcedimientos = listaProcedimientos + "," + "'" + procedimiento + "'";
                }

            }
            listaProcedimientos = listaProcedimientos + ")";
                    
                     query += " AND crd.CRD_PRO IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND crd.CRD_NUM = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && !"XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> getEstadoFirma");

                    query += " AND crd.CRD_FIR_EST = '" + filtros.getEstadoFirma() + "'";

                }

                if (filtros.getFechaEnvioPeticion() != null && !filtros.getFechaEnvioPeticion().isEmpty()) {
                    log.info("aplicando condición --> fechaEnvioPeticion");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')='" + filtros.getFechaEnvioPeticion() + "'";

                }

                if (filtros.getFechaEnvioPeticionDesde() != null && !filtros.getFechaEnvioPeticionDesde().isEmpty()) {
                    log.info("aplicando condición --> FechaEnvioPeticionDesde");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')>='" + filtros.getFechaEnvioPeticionDesde() + "'";

                }

                if (filtros.getFechaEnvioPeticionHasta() != null && !filtros.getFechaEnvioPeticionHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioPeticionHasta");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')<='" + filtros.getFechaEnvioPeticionHasta() + "'";

                }

                if (filtros.getTipoDocumento() != null && !filtros.getTipoDocumento().isEmpty()) {
                    log.info("aplicando condición --> tipoDocumento");
                    //Desa va mas rapido dejamos el LIKE, dejamos el substr por si se debe modificar mas adelante
                    // query += " substr(crd.crd_des,-3) ='" + filtros.getTipoDocumento() + "'";
                    query += " AND crd.crd_des LIKE '%" + filtros.getTipoDocumento() + "'";
                }

                 query += "  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  ) ";
                
                log.info("----Fin aplicar condiciones----");

            }
            if (exportar) {
                query += " ORDER BY crd.crd_fal";
            } else {
                query += " ORDER BY crd.crd_fal DESC) I "
                        + "WHERE";
                if (filtros.getDocumentoNotificado() != null && !filtros.getDocumentoNotificado().isEmpty()) {
                    log.info("aplicando condición --> documentoNotificado");
                    query += " doc_notificado=" + filtros.getDocumentoNotificado() + " AND ";
                }

                query += " ROWNUM <= " + filtros.getFinish() + ") "
                        + "WHERE rnum > " + filtros.getStart();
            }
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaVO elementoListaRetorno = new ResultadoConsultaVO();

                elementoListaRetorno.setEjercicio(rs.getString("CRD_EJE"));
                elementoListaRetorno.setProcedimiento(rs.getString("CRD_PRO"));
                elementoListaRetorno.setNumExpediente(rs.getString("CRD_NUM"));
                elementoListaRetorno.setDescDocumento(rs.getString("CRD_DES"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("CRD_FAL"));
                elementoListaRetorno.setFechaFirmado(rs.getString("CRD_FMO"));
                elementoListaRetorno.setEstado(rs.getString("CRD_FIR_EST"));
                elementoListaRetorno.setDescEstado(descripcionEstado(rs.getString("CRD_FIR_EST")));
                elementoListaRetorno.setDocNotificado(rs.getInt("doc_notificado") == 1 ? "SÍ" : "NO");
                retorno.add(elementoListaRetorno);
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
        log.info("getPeticionesConsultasNISAExExpediente - End () " + formatDate.format(new Date()));
        return retorno;
    }

    public int getNumRegistrosTotalExpedientesFirmados(int codOrganizacion, Connection con, FiltrosVO filtros, List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getNumRegistrosTotalExpedientesFirmados - Begin () " + formatDate.format(new Date()));
        int retorno = 0;
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            String query = " SELECT COUNT(*) FROM ("
                    + " SELECT crd.*, case when adnot.num_expediente is null then 0 else 1 end as doc_notificado"
                    + " FROM " + ConfigurationParameter.getParameter(
                            ConstantesMeLanbide08.NOMBRE_TABLA_DOCUMENTOS_ENVIADOS_FIRMAR,
                            ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                    + " crd"
                    + " LEFT OUTER JOIN " + ConfigurationParameter.getParameter(
                            ConstantesMeLanbide08.NOMBRE_TABLA_DOCUMENTOS_NOTIFICADOS,
                            ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                    + " adnot ON adnot.cod_tramite = crd.crd_tra"
                    + " AND adnot.num_expediente = crd.crd_num"
                    + " AND adnot.ocu_tramite = crd.crd_ocu"
                     + " LEFT OUTER JOIN " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                        + " log ON log.numexpediente = crd.crd_num"
                        + " AND log.crdnud = crd.crd_nud";

            if (filtros != null) {
                log.info("params : " + filtros.getStart() + ", "
                        + filtros.getFinish() + ", "
                        + filtros.getEjercicio() + ", "
                        + filtros.getProcedimiento() + ", "
                        + filtros.getEstadoFirma() + ", "
                        + filtros.getTipoDocumento() + ", "
                        + filtros.getFechaEnvioPeticion() + "."
                );

                //Por si se buscan no enviados al portafirmas
                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && "XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> Enviados a portafirmas");
                    query += " WHERE crd.crd_fir_est IS NULL ";
                } else {
                    query += " WHERE crd.crd_fir_est IS NOT NULL";
                }

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND crd.CRD_EJE = '" + filtros.getEjercicio() + "'";

                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND crd.CRD_PRO = '" + filtros.getProcedimiento() + "'";

                } else {
                    //Obtenemos los procedimientos que puede consultar el usuario
                  String  listaProcedimientos ="(";
                for (int i = 0; i < listaProcedimiento.size(); i++) {
                String procedimiento = listaProcedimiento.get(i).getId();
                if (listaProcedimientos == "(") {
                    listaProcedimientos = listaProcedimientos + "'" + procedimiento + "'";
                } else {
                    listaProcedimientos = listaProcedimientos + "," + "'" + procedimiento + "'";
                }

            }
            listaProcedimientos = listaProcedimientos + ")";
                    
                     query += " AND crd.CRD_PRO IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND crd.CRD_NUM = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && !"XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> getEstadoFirma");

                    query += " AND crd.CRD_FIR_EST = '" + filtros.getEstadoFirma() + "'";

                }
                if (filtros.getFechaEnvioPeticion() != null && !filtros.getFechaEnvioPeticion().isEmpty()) {
                    log.info("aplicando condición --> fechaEnvioPeticion");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')='" + filtros.getFechaEnvioPeticion() + "'";

                }
                if (filtros.getTipoDocumento() != null && !filtros.getTipoDocumento().isEmpty()) {
                    log.info("aplicando condición --> tipoDocumento");
                    //Desa va mas rapido dejamos el LIKE, dejamos el substr por si se debe modificar mas adelante
                    // query += " substr(crd.crd_des,-3) ='" + filtros.getTipoDocumento() + "'";
                    query += " AND crd.crd_des LIKE '%" + filtros.getTipoDocumento() + "'";
                }

                if (filtros.getFechaEnvioPeticionDesde() != null && !filtros.getFechaEnvioPeticionDesde().isEmpty()) {
                    log.info("aplicando condición --> FechaEnvioPeticionDesde");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')>'" + filtros.getFechaEnvioPeticionDesde() + "'";

                }

                if (filtros.getFechaEnvioPeticionHasta() != null && !filtros.getFechaEnvioPeticionHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioPeticionHasta");

                    query += " AND to_char(crd.CRD_FAL,'dd/mm/yyyy')<'" + filtros.getFechaEnvioPeticionHasta() + "'";

                }
                
                
                 query += "  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  ) ";
                 
                 
                 
                if (filtros.getDocumentoNotificado() != null && !filtros.getDocumentoNotificado().isEmpty()) {
                    log.info("aplicando condición --> documentoNotificado");
                    query += ") WHERE doc_notificado=" + filtros.getDocumentoNotificado() + "";
                } else {
                    query += ")";
                }
                
                

                log.info("----Fin aplicar condiciones----");
            }

            pt = con.prepareStatement(query);

            rs = pt.executeQuery();
            log.info("sql = " + query);

            while (rs.next()) {
                retorno = rs.getInt(1);
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

        log.info("getNumRegistrosTotalExpedientesFirmados - END () " + formatDate.format(new Date()));
        return retorno;
    }

    public List<ComboVO> getComboProcedimiento(int codOrganizacion,int codUsuario, Connection con) throws Exception {
        log.info("getComboProcedimiento - Begin () " + formatDate.format(new Date()));
        List<ComboVO> retorno = new ArrayList<ComboVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT DISTINCT PRO_COD AS PROC, pro_des AS PML_VALOR "
                    + "FROM E_PUI, E_PRO "
                    + "WHERE "
                     + " pro_cod != 'REINT' AND "
                    +" (PUI_MUN=PRO_MUN AND PUI_PRO=PRO_COD  "
                    + "    AND (EXISTS (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1 AND UOU_UOR=PUI_COD))  "
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')  AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )  "
                    + "    AND PRO_EST=1 AND PRO_LIBRERIA <> 1  AND pro_cod != 'REINT' )  "
                    + "UNION  "
                    + "SELECT DISTINCT PRO_COD  AS PROC , pro_des AS PML_VALOR "
                    + "FROM E_PRO "
                    + "WHERE (( NOT EXISTS (  SELECT DISTINCT PUI_COD FROM E_PUI  WHERE PUI_PRO=PRO_COD)  AND EXISTS (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1)) "   
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')  AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )  "
                    + "    AND PRO_EST=1 AND PRO_LIBRERIA <> 1  AND pro_cod != 'REINT' )  "
                    + "UNION  "
                    + "SELECT DISTINCT e_tra.tra_pro  AS PROC , pro_des AS PML_VALOR "
                    + "FROM e_tra  "
                    + "INNER JOIN E_PRO ON (E_TRA.tra_pro = pro_cod and E_TRA.tra_mun = pro_mun)  "
                    + "RIGHT JOIN e_tra_utr on (e_tra.tra_cod = e_tra_utr.tra_cod and e_tra.tra_mun = e_tra_utr.tra_mun and e_tra.tra_pro = e_tra_utr.tra_pro )   "
                    + "where e_tra.tra_utr =0 and exists (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1 AND UOU_UOR=tra_utr_cod) "
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')   "
                    + "    AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )   "
                    + "    AND PRO_EST=1 "
                    + "    AND PRO_LIBRERIA <> 1 "
                     + "     AND pro_cod != 'REINT'  "
                    + "UNION "
                    + "SELECT DISTINCT tra_pro AS PROC , pro_des AS PML_VALOR "
                    + "FROM e_tra "
                    + "INNER JOIN E_PRO ON (tra_pro = pro_cod and tra_mun = pro_mun  )  "
                    + "where ("
                    + "        ((tra_utr =1 or tra_uin = -99998 ) and  EXISTS (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1)) "
                    + "        or exists (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=5 AND UOU_ORG=1 AND UOU_ENT=1 AND UOU_UOR=tra_uin) "
                    + "		) "
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')  "
                    + "    AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )  "
                    + "    AND PRO_EST=1 "
                    + "    AND PRO_LIBRERIA<>1 "
                    + "     AND pro_cod != 'REINT'  "
                    + "ORDER BY PROC ASC";

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setInt(1, codUsuario);
            pt.setInt(2, codOrganizacion);
            pt.setInt(3, codUsuario);
            pt.setInt(4, codOrganizacion);
            pt.setInt(5, codUsuario);
            pt.setInt(6, codOrganizacion);
            pt.setInt(7, codUsuario);
            pt.setInt(8, codOrganizacion);
            log.info("Param ? : " + codUsuario);
            rs = pt.executeQuery();
            while (rs.next()) {
                ComboVO elementoListaRetorno = new ComboVO();
                elementoListaRetorno.setId(rs.getString("proc"));
                elementoListaRetorno.setValor(rs.getString("PML_VALOR"));
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger combos ... ", ex);
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
        log.info("getComboProcedimiento - End () " + formatDate.format(new Date()));
        return retorno;
    }

    private String descripcionEstado(String x) {

        if (x == null) {
            return "";
        }
        if (x.equalsIgnoreCase("E")) {
            return "EN EDICIÓN";
        }
        if (x.equalsIgnoreCase("O")) {
            return "ENVIADO A PORTAFIRMAS";
        }
        if (x.equalsIgnoreCase("T")) {
            return "PENDIENTE SU FIRMA";
        }
        if (x.equalsIgnoreCase("L")) {
            return "PENDIENTE DE FIRMA";
        }
        if (x.equalsIgnoreCase("U")) {
            return "PENDIENTE DE FIRMA";
        }
        if (x.equalsIgnoreCase("F")) {
            return "FIRMADO";
        }
        if (x.equalsIgnoreCase("R")) {
            return "RECHAZADO";
        }
        if (x.equalsIgnoreCase("M")) {
            return "DOCUMENTO ORIGINAL";
        }
        if (x.equalsIgnoreCase("V")) {
            return "DOCUMENTO ORIGINAL";
        }

        return "";

    }

 /**
    public Integer cambiarEstadoFirmaDocumentoCRD(TramitacionExpedientesValueObject tEVO, int idUsuario, String portafirmas, Connection con) throws TramitacionException, Exception {
    
        AdaptadorSQLBD oad = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        int resultado = 0;
        String sql;
        
         try {

            if (portafirmas != null && "LAN".equals(portafirmas)) {                      
                    //Se actualiza el numero de documento de la tabla E_CRD_FIR_FIRMANTES para que apunte al documento correcto
                    sql = "UPDATE E_CRD_FIR_FIRMANTES SET COD_DOCUMENTO =  " + tEVO.getCodDocumento() +
                    " WHERE COD_MUNICIPIO =" + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" 
                            + tEVO.getCodProcedimiento() + "' AND  EJERCICIO =" + tEVO.getEjercicio() + 
                            " AND NUM_EXPEDIENTE ='" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite() + 
                            " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodigoDocumentoAnterior() +
                    " AND  ID_USUARIO = (SELECT CRD_USM FROM E_CRD WHERE CRD_MUN =" + tEVO.getCodMunicipio() + " AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND CRD_NUM='" + tEVO.getNumeroExpediente() +
                    "' AND CRD_TRA=" + tEVO.getCodTramite() + " AND CRD_OCU=" +
                    tEVO.getOcurrenciaTramite() + " AND CRD_NUD=" + tEVO.getCodigoDocumentoAnterior()+ ")";
                    if(log.isDebugEnabled()) log.debug(sql);
                    pt = con.prepareStatement(sql);
                    pt.executeUpdate(sql);
                   log.info("resultado de la acualizacion de la tabla E_CRD_FIR_FIRMANTES del expediente " + tEVO.getNumeroExpediente() + " es: " + sql);
                    pt.close();
                    //Se actualiza el registro del documento convertido para renombrarlo correctamente y que apunte al usuario firmante correcto
                    sql = "UPDATE E_CRD SET crd_des = SUBSTR(crd_des,0,instr(crd_des,'.',-1))||'pdf', CRD_USM = (SELECT ID_USUARIO FROM E_CRD_FIR_FIRMANTES WHERE COD_MUNICIPIO =" + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" 
                            + tEVO.getCodProcedimiento() + "' AND  EJERCICIO =" + tEVO.getEjercicio() + 
                            " AND NUM_EXPEDIENTE ='" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite() + 
                            " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodDocumento() +
                    ") WHERE CRD_NUM=" + tEVO.getCodMunicipio() + " AND CRD_PRO ='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" +
                    tEVO.getEjercicio() + " AND CRD_NUM='" + tEVO.getNumeroExpediente() +
                    "' AND CRD_TRA=" + tEVO.getCodTramite() + " AND CRD_OCU=" +
                    tEVO.getOcurrenciaTramite() + " AND CRD_NUD=" + tEVO.getCodDocumento();
                    if(log.isDebugEnabled()) log.debug(sql);
                    pt = con.prepareStatement(sql);
                    pt.executeUpdate(sql);
                    log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: "+ sql);
                    pt.close();
                    
                    //Se actualiza el documento
                    sql = "UPDATE E_CRD SET CRD_USM = " + idUsuario + 
                    " WHERE CRD_MUN=" + tEVO.getCodMunicipio() + " AND CRD_PRO='" + tEVO.getCodProcedimiento() + "' AND CRD_EJE=" +
                    tEVO.getEjercicio() + " AND CRD_NUM='" + tEVO.getNumeroExpediente() +
                    "' AND CRD_TRA=" + tEVO.getCodTramite() + " AND CRD_OCU=" +
                    tEVO.getOcurrenciaTramite() + " AND CRD_NUD=" + tEVO.getCodigoDocumentoAnterior();
                    if(log.isDebugEnabled()) log.debug(sql);
                    pt = con.prepareStatement(sql);
                    pt.executeUpdate(sql);
                    log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: "+ sql);
                    pt.close();
            
            } 
    
    } catch (SQLException e) {
            resultado = -1;
            log.error("Se ha producido un error de tipo SQLException: " + e.getMessage());
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        } catch (BDException bde) {
            log.error("Se ha producido un error de tipo TramitacionException: " + bde.getMessage());
            resultado = -1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        }catch (Exception ex) {
            log.error("Se ha producido un error de tipo Exception: " + ex.getMessage());
            resultado = -1;
            ex.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + ex.getMessage());
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
            }//try-catch
        }//try-catch
        return resultado;
    }
 **/
    
    
    
    
     public int cambiarEstadoFirmaDocumentoCRD(es.altia.flexia.integracion.moduloexterno.melanbide08.vo.TramitacionExpedientesValueObject tEVO, int idUsuario, String portafirmas, String[] params,AdaptadorSQLBD adaptador)
            throws TramitacionException, TechnicalException {
        
        Connection con = null;
        Statement st = null;
        int resultado = 0;
        String sql;

        
        //Recuperamos la propiedad que nos indica si existe un portafirmas externo
        if(log.isDebugEnabled()) log.debug("Comprobamos si existe un portafirmas externo");
        Boolean existePortafirmasExterno = false;
            //    PluginPortafirmasExternoClienteFactoria.getExistePortafirmasExterno(tEVO.getCodOrganizacion());
        if(log.isDebugEnabled()) log.debug("existe un portafirmas externo = " + existePortafirmasExterno);

        //Recuperamos el codigo del cliente del portafirmas
        String clientePortafirmasExterno = new String();
        if(existePortafirmasExterno){
            if(log.isDebugEnabled()) log.debug("Recuperamos el cliente del portafirmas externo");
            String cliente = PluginPortafirmasExternoClienteFactoria.
                getCodClientePortafirmasExterno(tEVO.getCodMunicipio());
            if(cliente != null && !"".equalsIgnoreCase(cliente)){
                clientePortafirmasExterno = cliente;
                if(log.isDebugEnabled()) log.debug("Cliente del portafirmas externo =  " + cliente);
            }//if(clientePortafirmasExterno != null && !"".equalsIgnoreCase(clientePortafirmasExterno))
        }//if(existePortafirmasExterno)

        //Aqui pondriamos el control para ver si tenemos que enviar el documento a un portafirmas externo
        String resultadoPortafirmasExterno = new String("");
        
            
        try {
            
           
            con=adaptador.getConnection();
            st = con.createStatement();
            adaptador.inicioTransaccion(con);
            
            
            if(existePortafirmasExterno){
                if(log.isDebugEnabled()) log.debug("Enviamos el documento al portafirmas externo");
                PluginPortafirmasExternoCliente portafirmasExterno =
                        PluginPortafirmasExternoClienteFactoria.getImplClass(tEVO.getCodOrganizacion());

                //Si se trata de un Portafirmas lanbide, como se han modificado la funcionalidad, se debe renombrar el nombre del documeto cambiando la extension a PDF
                if (portafirmas != null && "LAN".equals(portafirmas)) {
                    
                                        
                    //Se actualiza el numero de documento de la tabla E_CRD_FIR_FIRMANTES para que apunte al documento correcto
                    sql = "UPDATE E_CRD_FIR_FIRMANTES SET COD_DOCUMENTO =  " + tEVO.getCodDocumento() +
                    " WHERE COD_MUNICIPIO =" + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" 
                            + tEVO.getCodProcedimiento() + "' AND  EJERCICIO =" + tEVO.getEjercicio() + 
                            " AND NUM_EXPEDIENTE ='" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite() + 
                            " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodigoDocumentoAnterior() +
                    " AND  ID_USUARIO = (SELECT CRD_USM FROM E_CRD WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodigoDocumentoAnterior()+ ")";
                    if(log.isDebugEnabled()) log.debug(sql);
                    resultado = st.executeUpdate(sql);
                    log.info("resultado de la acualizacion de la tabla E_CRD_FIR_FIRMANTES del expediente " + tEVO.getNumeroExpediente() + " es: " + st.executeUpdate(sql));
                    
                    //Se actualiza el registro del documento convertido para renombrarlo correctamente y que apunte al usuario firmante correcto
                    sql = "UPDATE E_CRD SET crd_des = SUBSTR(crd_des,0,instr(crd_des,'.',-1))||'pdf', CRD_USM = (SELECT ID_USUARIO FROM E_CRD_FIR_FIRMANTES WHERE COD_MUNICIPIO =" + tEVO.getCodMunicipio() + " AND COD_PROCEDIMIENTO = '" 
                            + tEVO.getCodProcedimiento() + "' AND  EJERCICIO =" + tEVO.getEjercicio() + 
                            " AND NUM_EXPEDIENTE ='" + tEVO.getNumeroExpediente() + "' AND COD_TRAMITE = " + tEVO.getCodTramite() + 
                            " AND COD_OCURRENCIA = " + tEVO.getOcurrenciaTramite() + " AND  COD_DOCUMENTO = " + tEVO.getCodDocumento() +
                    ") WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
                    if(log.isDebugEnabled()) log.debug(sql);
                    resultado = st.executeUpdate(sql);
                    log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: " + st.executeUpdate(sql));
                    
                    
                    //Se actualiza el documento
                    sql = "UPDATE E_CRD SET CRD_USM = " + idUsuario + 
                    " WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodigoDocumentoAnterior();
                    if(log.isDebugEnabled()) log.debug(sql);
                    resultado = st.executeUpdate(sql);
                    log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: " + st.executeUpdate(sql));
                } 

                if(portafirmasExterno != null){
                    resultadoPortafirmasExterno = portafirmasExterno.enviarDocumentoTramitacionPortafirmas(tEVO.getCodOrganizacion(),
                            tEVO.getCodProcedimiento(), tEVO.getNumeroExpediente(), tEVO.getCodTramite(), tEVO.getOcurrenciaTramite(), 
                            tEVO.getCodDocumento(), tEVO.getCodigoDocumentoAnterior(), idUsuario, new byte[9],params, portafirmas);
                    if(log.isDebugEnabled()) log.info("Id del documento en el portafirmas externo = " + resultadoPortafirmasExterno);
                }//if(portafirmasExterno != null)
            }//if(existePortafirmasExterno)
        
            if(existePortafirmasExterno && ("".equalsIgnoreCase(resultadoPortafirmasExterno) || 
                PluginPortafirmasExternoCliente.OPERACION_ERROR.equals(resultadoPortafirmasExterno))){
                log.error("Se ha producido un error enviando el documento al portafirmas externo");
                resultado = -1;
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite");
            }

            final String nuevoEstadoFirma = ( (tEVO.getEstadoFirma()!=null)?("'"+tEVO.getEstadoFirma()+"'"):("NULL") );
            sql = "UPDATE E_CRD SET CRD_FIR_EST="+nuevoEstadoFirma+
                    " WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
            if(log.isDebugEnabled()) log.debug(sql);
            resultado = st.executeUpdate(sql);
            log.info("resultado de la acualizacion de la tabla E_CRD del expediente " + tEVO.getNumeroExpediente() + " es: " + st.executeUpdate(sql));
            
            log.info(" tEVO.getNumeroExpediente() vale " + tEVO.getNumeroExpediente());
            //si se trata de un Portafirmas lanbide, como se han modificado la funcionalidad, 
            //se debe cambiar el estado del documento original para que aparezca el reenvio del documento
            if (portafirmas != null && "LAN".equals(portafirmas) && 
                    tEVO.getCodigoDocumentoAnterior() != null && !"".equals(tEVO.getCodigoDocumentoAnterior().trim())) {
                sql = "UPDATE E_CRD SET CRD_FIR_EST = 'V' "+
                    " WHERE " + crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                    crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                    tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                    "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                    tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodigoDocumentoAnterior();
                if(log.isDebugEnabled()) log.debug(sql);
                resultado = st.executeUpdate(sql);
                log.info("resultado de la acualizacion de la tabla E_CRD del documento Original del expediente " + tEVO.getNumeroExpediente() + " es: " + st.executeUpdate(sql));
            }

            if ( (resultado >= 0) && (nuevoEstadoFirma!=null) && (nuevoEstadoFirma.equals("'O'")) ) {
                /* Averiguar código plantilla*/
                sql = "SELECT CRD_DOT FROM E_CRD WHERE "+ crd_mun + "=" + tEVO.getCodMunicipio() + " AND " +
                        crd_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + crd_eje + "=" +
                        tEVO.getEjercicio() + " AND " + crd_num + "='" + tEVO.getNumeroExpediente() +
                        "' AND " + crd_tra + "=" + tEVO.getCodTramite() + " AND " + crd_ocu + "=" +
                        tEVO.getOcurrenciaTramite() + " AND " + crd_nud + "=" + tEVO.getCodDocumento();
                if(log.isDebugEnabled()) log.info(sql);
                ResultSet rs = st.executeQuery(sql);
                if (rs.next()) {
                    final int codigoPlantilla = rs.getInt(1);
                    rs.close();
                    /* Recuperar usuarios que deben firmar el documento */
                    sql = "SELECT USU_COD FROM E_DOT_FIR WHERE "+  dot_mun + "=" + tEVO.getCodMunicipio() +
                            " AND " + dot_pro + "='" + tEVO.getCodProcedimiento() + "' AND " + dot_tra + "=" +
                            tEVO.getCodTramite()+" AND "+ dot_cod + "=" + codigoPlantilla;
                    Vector codigosUsuariosFirmantes = new Vector();
                    if(log.isDebugEnabled()) log.info(sql);
                    rs = st.executeQuery(sql);
                    while (rs.next()) {
                        codigosUsuariosFirmantes.add(new Integer(rs.getInt(1)));
                    }//while
                    rs.close();
                    //Si hay portafirmas externo añadimos el cliente del portafirmas y el id de firma generado.
                    if(existePortafirmasExterno && !"".equalsIgnoreCase(resultadoPortafirmasExterno)){
                        
                       Long idEstadoPortafirmas = null;
                       String OidDocumento = "";
                       if (portafirmas != null && "LAN".equals(portafirmas)) {
                           
                          Map<String,String> mapa = new HashMap<String,String>();
                          String[] resPortafirmasExterno = resultadoPortafirmasExterno.split("\\$");
                          
                          //Si todo ha ocurrido correctamente devuelve los datos necesatios concatenados con $
                          // 1 OID Documento, 2 Extension Documento, 3 Buzon, 4 Buzon Firma
                          OidDocumento = resPortafirmasExterno[0];
                          mapa.put("OidDocumento", OidDocumento);
                          mapa.put("extension", resPortafirmasExterno[1]);
                          mapa.put("buzon", resPortafirmasExterno[2]);
                          mapa.put("firmaImformante",resPortafirmasExterno[3]);
                          
                          //Se inserta en la tabla estado_portafirmas y estado_portafirmas_h la informacion indicando que se ha enviado al portafirmas la documentacion
                          idEstadoPortafirmas = LanbideEstadoPortafirmasManager.getInstance().insertarEstadoInicialPortafirmas(mapa,con);
  
                        }
                        
                        /* Mandar al portafirmas de cada uno de esos usuarios */
                        sql = "INSERT INTO E_CRD_FIR ("+crd_mun+","+crd_pro+","+crd_eje+","+crd_num+","+crd_tra+","+crd_ocu+","+crd_nud+",FIR_EST,"+usu_cod+
                            ",CRD_COD_PF_EXT,CRD_ID_SOL_PF_EXT";
                        if (portafirmas != null && "LAN".equals(portafirmas)) {
                            sql += ", ESTADO_PORTAFIRMAS";
                            
                        }
                        sql += ") VALUES (";
                        sql += tEVO.getCodMunicipio() + ",'" + tEVO.getCodProcedimiento() + "', " + tEVO.getEjercicio() + ", '" + tEVO.getNumeroExpediente() +
                                "', " + tEVO.getCodTramite() + ", " + tEVO.getOcurrenciaTramite() + ", " + tEVO.getCodDocumento()+
                                ",'O',";
                        for (int i = 0; i < codigosUsuariosFirmantes.size(); i++) {
                            Integer firmante = (Integer)codigosUsuariosFirmantes.elementAt(i);
                            sql += firmante +",";
                        }//for
                        
                        if (portafirmas != null && "LAN".equals(portafirmas)) {
                            sql += "'" + clientePortafirmasExterno + "','" + OidDocumento + "','" + idEstadoPortafirmas + "' )";
                        } else {
                            sql += "'" + clientePortafirmasExterno + "','" + resultadoPortafirmasExterno + "')";
                        }
                        
                        if(log.isDebugEnabled()) log.info(sql);
                        st.executeUpdate(sql);
                    }else{
                        /* Mandar al portafirmas de cada uno de esos usuarios */
                        sql = "INSERT INTO E_CRD_FIR ("+crd_mun+","+crd_pro+","+crd_eje+","+crd_num+","+crd_tra+","+crd_ocu+","+crd_nud+",FIR_EST,"+usu_cod+") VALUES (";
                        sql += tEVO.getCodMunicipio() + ",'" + tEVO.getCodProcedimiento() + "', " + tEVO.getEjercicio() + ", '" + tEVO.getNumeroExpediente() +
                                "', " + tEVO.getCodTramite() + ", " + tEVO.getOcurrenciaTramite() + ", " + tEVO.getCodDocumento()+
                                ",'O',";
                        for (int i = 0; i < codigosUsuariosFirmantes.size(); i++) {
                            Integer firmante = (Integer)codigosUsuariosFirmantes.elementAt(i);
                            String queryFinal = sql + firmante +")";
                            if(log.isDebugEnabled()) log.info(queryFinal);
                            st.executeUpdate(queryFinal);
                        }//for
                    }//if(existePortafirmasExterno && !"".equalsIgnoreCase(resultadoPortafirmasExterno)) 
                }//if
            }//if
            st.close();
        } catch (SQLException e) {
            resultado = -1;
            log.error("Se ha producido un error de tipo SQLException: " + e.getMessage());
            e.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + e.getMessage());
        } catch (BDException bde) {
            log.error("Se ha producido un error de tipo TramitacionException: " + bde.getMessage());
            resultado = -1;
            bde.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + bde.getMessage());
        }catch (Exception ex) {
            log.error("Se ha producido un error de tipo Exception: " + ex.getMessage());
            resultado = -1;
            ex.printStackTrace();
            throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite " + ex.getMessage());
        } finally {
            try {
                if (resultado >= 0) {
                   log.debug("resultado es mayor a 0 y se hace commit de los cambios");
                   if (adaptador != null && con != null) {
                       adaptador.finTransaccion(con); 
                   } else {
                       log.debug("AdaptadorSQLBD o Connection son nulos");
                   }
                   
                } else {
                    log.debug("resultado es menor o igual a 0 y se realiza el rollback");
                    adaptador.rollBack(con);
                }
                
                if (adaptador != null && con != null) {
                    adaptador.devolverConexion(con);
                } else {
                    log.debug("AdaptadorSQLBD o Connection son nulos");
                }

            } catch (BDException bde) {
                resultado = -1;
                bde.printStackTrace();
                if(log.isDebugEnabled()) log.error ("TramitacionExpedientesDAO. Exception: " + bde.getMensaje());
                throw new TramitacionException("Error.TramitacionExpedientesDAO.grabarTamite.Fin transaccion y devolver conexion", bde);
            }//try-catch
        }//try-catch
        return resultado;
    }
     
     
}
