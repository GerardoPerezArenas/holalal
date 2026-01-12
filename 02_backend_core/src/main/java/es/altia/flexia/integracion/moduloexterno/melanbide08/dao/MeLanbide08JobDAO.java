package es.altia.flexia.integracion.moduloexterno.melanbide08.dao;

import es.altia.common.service.config.Config;
import es.altia.flexia.integracion.moduloexterno.melanbide08.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide08.util.ConstantesMeLanbide08;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ResultadoConsultaVO;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;


/**
 *
 * @author alexandrep
 */
public class MeLanbide08JobDAO {
    
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
   

    private static final Logger log = LogManager.getLogger(MeLanbide08JobDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //Instancia
    private static MeLanbide08JobDAO instance = null;

    // Constructor
    private MeLanbide08JobDAO() {
        
        }

    
    public static MeLanbide08JobDAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide08JobDAO.class) {
                instance = new MeLanbide08JobDAO();
            }
        }
        return instance;
    }
    
  

    public int insertarLineasLogJob(Connection con, ResultadoConsultaVO resultadoConsulta) throws Exception {
        log.info("insertarLineasLogJob - Begin () " + formatDate.format(new Date()));
        int id = 0;
       CallableStatement  pt = null;
        ResultSet rs = null;

        try {
            String query = "BEGIN INSERT INTO "+ ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)
                    + " ( EJERCICIO, DESCRIPCION, CODIGOMUNICIPIO, CODTRAMITE, PROCEDIMIENTO, NUMEXPEDIENTE, ESTADOLOG, MENSAJEERROR,FECHAENVIADO,CRDNUD) "
                    + " VALUES (?,?,?,?,?,?,'PENDIENTE','',?,?) RETURNING IDLOG INTO ?; END;";
                log.info("query: " +query);
                log.info("----Fin aplicar condiciones----");
            
          

            pt = con.prepareCall(query);
            pt.setString(1, resultadoConsulta.getEjercicio());
            pt.setString(2, resultadoConsulta.getDescDocumento());
            pt.setString(3, resultadoConsulta.getCodigoMunicipio());
            pt.setString(4, resultadoConsulta.getCodTramite());
            pt.setString(5, resultadoConsulta.getProcedimiento());
            pt.setString(6, resultadoConsulta.getNumExpediente());
            pt.setTimestamp(7, new Timestamp(resultadoConsulta.getFechaEnviadoJob().getTime()));
            pt.setString(8, resultadoConsulta.getCodigoDocumentoAnterior());
            pt.registerOutParameter(9, Types.NUMERIC);
            
            pt.executeUpdate();
            log.info("sql = " + query);
            
           
                id = pt.getInt(9);
            
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

        log.info("insertarLineasLogJob - End () " + formatDate.format(new Date()));
        return id;
    }

    /**
     *
     * @param con
     * @param idLog
     * @param estado
     * @param mensaje
     * @return
     * @throws Exception
     */
    public Boolean actualizarLineasLogJob(Connection con, int idLog, String estado, String mensaje) throws Exception {
 log.info("actualizarLineasLogJob - Begin () " + formatDate.format(new Date()));
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            String query = " UPDATE "+ ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES) 
                    + " SET ";
            if (mensaje!=null){
             query = query +"MENSAJEERROR = '"+mensaje+"',";   
            }
             query = query +" ESTADOLOG = '"+estado+"',";
             query = query +" FECHATRAMITADO = ?"
                     + " WHERE IDLOG ="+idLog;   
               log.info("query: " +query);
                log.info("----Fin aplicar condiciones----");
            
            pt = con.prepareStatement(query);
            pt.setTimestamp(1, new Timestamp(new Date().getTime()));
            rs = pt.executeQuery();
            log.info("sql = " + query);

            
        } catch (Exception ex) {
            log.info("Se ha producido un error al updatear la linea en el log del job ", ex);
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

        log.info("actualizarLineasLogJob - End () " + formatDate.format(new Date()));
        return true;
    }
    
    
    public List<ResultadoConsultaVO> getLogJob(int codOrganizacion, Connection con, FiltrosVO filtros, boolean exportar, List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getLogJob - Begin () " + formatDate.format(new Date()));
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
                        + " SELECT * "
                        + " FROM " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES)+" log ";
                        
                        
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
                    log.info("aplicando condición --> Enviados a portafirmas estado");
                    query += " WHERE log.ESTADOLOG ='"+ filtros.getEstadoFirma() + "'";
                } else {
                    query += " WHERE log.ESTADOLOG IS NOT NULL ";
                }

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND log.ejercicio = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND log.procedimiento = '" + filtros.getProcedimiento() + "'";

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
                    
                     query += " AND log.procedimiento IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND log.numexpediente = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && !"XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> getEstadoFirma");

                    query += " AND log.estadolog = '" + filtros.getEstadoFirma() + "'";

                }

                if (filtros.getFechaEnvioPeticion() != null && !filtros.getFechaEnvioPeticion().isEmpty()) {
                    log.info("aplicando condición --> fechaEnvioPeticion");

                    query += " AND to_char(log.fechatramitado,'dd/mm/yyyy')='" + filtros.getFechaEnvioPeticion() + "'";

                }

                if (filtros.getFechaEnvioPeticionDesde() != null && !filtros.getFechaEnvioPeticionDesde().isEmpty()) {
                    log.info("aplicando condición --> FechaEnvioPeticionDesde");

                    query += " AND to_char(log.fechatramitado,'dd/mm/yyyy')>='" + filtros.getFechaEnvioPeticionDesde() + "'";

                }

                if (filtros.getFechaEnvioPeticionHasta() != null && !filtros.getFechaEnvioPeticionHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioPeticionHasta");

                    query += " AND to_char(log.fechatramitado,'dd/mm/yyyy')<='" + filtros.getFechaEnvioPeticionHasta() + "'";

                }

              
                log.info("----Fin aplicar condiciones----");
             } 
                        
                        
                        
                        
                    query +=     "  ORDER BY IDLOG DESC ) I "
                        + " WHERE  ROWNUM <= " + filtros.getFinish() + ") "
                        + "WHERE rnum > " + filtros.getStart();       

            }
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaVO elementoListaRetorno = new ResultadoConsultaVO();

                elementoListaRetorno.setIdLog(rs.getInt("IDLOG"));
                elementoListaRetorno.setEjercicio(rs.getString("EJERCICIO"));
                elementoListaRetorno.setProcedimiento(rs.getString("PROCEDIMIENTO"));
                elementoListaRetorno.setNumExpediente(rs.getString("NUMEXPEDIENTE"));
                elementoListaRetorno.setDescDocumento(rs.getString("DESCRIPCION"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("FECHAENVIADO"));
                elementoListaRetorno.setFechaFirmado(rs.getString("FECHATRAMITADO"));
                elementoListaRetorno.setEstado(rs.getString("ESTADOLOG"));
                elementoListaRetorno.setMensajeErrorJob(rs.getString("MENSAJEERROR") != null ? rs.getString("MENSAJEERROR") : "");
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

    public int getNumRegistrosLogJob(int codOrganizacion, Connection con, FiltrosVO filtros, List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getNumRegistrosLogJob - Begin () " + formatDate.format(new Date()));
        int retorno = 0;
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            String query = " SELECT COUNT(*) "
                        + " FROM " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide08.TABLA_LOG_JOB,
                                ConstantesMeLanbide08.FICHERO_PROPIEDADES) +" log ";
            
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
                    log.info("aplicando condición --> Enviados a portafirmas estado");
                    query += " WHERE log.ESTADOLOG ='"+ filtros.getEstadoFirma() + "'";
                } else {
                    query += " WHERE log.ESTADOLOG IS NOT NULL ";
                }

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND log.ejercicio = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND log.procedimiento = '" + filtros.getProcedimiento() + "'";

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
                    
                     query += " AND log.procedimiento IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND log.numexpediente = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getEstadoFirma() != null && !filtros.getEstadoFirma().isEmpty() && !"XX".equalsIgnoreCase(filtros.getEstadoFirma())) {
                    log.info("aplicando condición --> getEstadoFirma");

                    query += " AND log.estadolog = '" + filtros.getEstadoFirma() + "'";

                }

                if (filtros.getFechaEnvioPeticion() != null && !filtros.getFechaEnvioPeticion().isEmpty()) {
                    log.info("aplicando condición --> fechaEnvioPeticion");

                    query += " AND to_char(log.fechatramitado,'dd/mm/yyyy')='" + filtros.getFechaEnvioPeticion() + "'";

                }

                if (filtros.getFechaEnvioPeticionDesde() != null && !filtros.getFechaEnvioPeticionDesde().isEmpty()) {
                    log.info("aplicando condición --> FechaEnvioPeticionDesde");

                    query += " AND to_char(log.fechatramitado,'dd/mm/yyyy')>='" + filtros.getFechaEnvioPeticionDesde() + "'";

                }

                if (filtros.getFechaEnvioPeticionHasta() != null && !filtros.getFechaEnvioPeticionHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioPeticionHasta");

                    query += " AND to_char(log.fechatramitado,'dd/mm/yyyy')<='" + filtros.getFechaEnvioPeticionHasta() + "'";

                }

              
                log.info("----Fin aplicar condiciones----");
             }

            pt = con.prepareStatement(query);
            log.info("sql = " + query);
            rs = pt.executeQuery();
           

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

        log.info("getNumRegistrosLogJob - End () " + formatDate.format(new Date()));
        return retorno;
    }
     
}
