package es.altia.flexia.integracion.moduloexterno.melanbide32.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MeLanbide32Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class OriCECriteriosCentroDAO {
    
    private static final Logger log = LogManager.getLogger(OriCECriteriosCentroDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final MappingUtils mappingUtils = MappingUtils.getInstance();
    private final OriCECriteriosEvaOpcionDAO oriCECriteriosEvaOpcionDAO = new OriCECriteriosEvaOpcionDAO();
    
    
    public OriCECriteriosCentro getOriCECriteriosCentroById(long id, Connection con) throws Exception {
        log.info("getOriCECriteriosCentroById - Begin () " + formatDate.format(new Date()));
        OriCECriteriosCentro retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Centro "
                    + " WHERE id=? ";
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setLong(1, id);
            log.info("Param ? : " + id
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = mappingUtils.getOriCECriteriosCentro(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosCentro ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosCentroById - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosCentro> getOriCECriteriosCentroByIdCentro(long idCentro, Connection con) throws Exception {
        log.info("getOriCECriteriosCentroByIdCentro - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosCentro> retorno = new ArrayList<OriCECriteriosCentro>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM ORI_CE_CRITERIOS_CENTRO "
                    + " WHERE IDCENTRO=? "
                    + " order by IDCRITERIO,IDCRITERIOOPCION "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setLong(contParam++, idCentro);
            log.info("Param ? : " + idCentro
            );
            rs = pt.executeQuery();
            while (rs.next()) {
                retorno.add(mappingUtils.getOriCECriteriosCentro(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosCentro ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosCentroByIdCentro - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosCentroDTO> getOriCECriteriosCentroDTOByIdCentro(long idCentro, Connection con) throws Exception {
        log.info("getOriCECriteriosCentroByIdCentro - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosCentroDTO> retorno = new ArrayList<OriCECriteriosCentroDTO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT "
                    + " ORI_CE_CRITERIOS_CENTRO.* "
                    + " ,occe.codigo||'_'||occe.codigoorden||'_'||occeo.codigoorden  idElementoHTML "
                    + " FROM ORI_CE_CRITERIOS_CENTRO "
                    + " left join ori_ce_criterios_eva occe on occe.id=ori_ce_criterios_centro.idcriterio "
                    + " left join ori_ce_criterios_eva_opcion occeo on occeo.idcriteriofk=ori_ce_criterios_centro.idcriterio and occeo.id=ori_ce_criterios_centro.idcriterioopcion "
                    + " WHERE IDCENTRO=? "
                    + " order by numeroExpediente desc,idCentro,occe.codigo, regexp_substr(occe.codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(occe.codigoOrden, '\\d+')) nulls first ,regexp_substr(occeo.codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(occeo.codigoOrden, '\\d+')) "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setLong(contParam++, idCentro);
            log.info("Param ? : " + idCentro
            );
            rs = pt.executeQuery();
            while (rs.next()) {
                retorno.add(mappingUtils.getOriCECriteriosCentroDTO(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosCentro ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosCentroDTOByIdCentro - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosCentroDTO> getOriCECriteriosCentroDTOByNumeroExpediente(String numeroExpediente, Connection con) throws Exception {
        log.info("getOriCECriteriosCentroDTOByNumeroExpediente - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosCentroDTO> retorno = new ArrayList<OriCECriteriosCentroDTO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT "
                    + " ORI_CE_CRITERIOS_CENTRO.* "
                    + " occe.codigo||'_'||occe.codigoorden||'_'||occeo.codigoorden  idElementoHTML "
                    + " FROM ORI_CE_CRITERIOS_CENTRO "
                    + " left join ori_ce_criterios_eva occe on occe.id=ori_ce_criterios_centro.idcriterio "
                    + " left join ori_ce_criterios_eva_opcion occeo on occeo.idcriteriofk=ori_ce_criterios_centro.idcriterio and occeo.id=ori_ce_criterios_centro.idcriterioopcion "
                    + " WHERE NUMEROEXPEDIENTE=? "
                    + " order by order by numeroExpediente desc,idCentro,occe.codigo, regexp_substr(occe.codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(occe.codigoOrden, '\\d+')) nulls first ,regexp_substr(occeo.codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(occeo.codigoOrden, '\\d+')) "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setString(contParam++, numeroExpediente);
            log.info("Param ? : " + numeroExpediente
            );
            rs = pt.executeQuery();
            while (rs.next()) {
                retorno.add(mappingUtils.getOriCECriteriosCentroDTO(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosCentro ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosCentroDTOByNumeroExpediente - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosCentro> getOriCECriteriosCentroByNumeroExpediente(String numeroExpediente, Connection con) throws Exception {
        log.info("getOriCECriteriosCentroByNumeroExpediente - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosCentro> retorno = new ArrayList<OriCECriteriosCentro>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM ORI_CE_CRITERIOS_CENTRO "
                    + " WHERE NUMEROEXPEDIENTE=? "
                    + " order by IDCRITERIO,IDCRITERIOOPCION "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setString(contParam++, numeroExpediente);
            log.info("Param ? : " + numeroExpediente
            );
            rs = pt.executeQuery();
            while (rs.next()) {
                retorno.add(mappingUtils.getOriCECriteriosCentro(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosCentro ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosCentroByNumeroExpediente - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public boolean crearOriCECriteriosCentro(OriCECriteriosCentro oriCECriteriosCentro, Connection con) throws Exception {
        log.info("crearOriCECriteriosCentro - Begin () " + formatDate.format(new Date()));
        boolean retorno = true;
        PreparedStatement pt = null;
        try {
            if(oriCECriteriosCentro!=null){
                String query = " INSERT INTO "
                        + " Ori_CE_Criterios_Centro "
                        + " ( ID, IDCENTRO, IDCRITERIO, IDCRITERIOOPCION, CENTROSELECCIONOPCION,CENTROSELECCIONOPCIONVAL, NUMEROEXPEDIENTE, EJERCICIOCONVOCATORIA)"
                        + " VALUES (SEQ_ORI_CE_CRITERIOS_CENTRO.nextval,?,?,?,?,?,?,?) "
                        ;
                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParam = 1;
                pt.setLong(contParam++, oriCECriteriosCentro.getIdCentro());
                pt.setLong(contParam++, oriCECriteriosCentro.getIdCriterio());
                pt.setLong(contParam++, oriCECriteriosCentro.getIdCriterioOpcion());
                pt.setLong(contParam++, oriCECriteriosCentro.getCentroSeleccionOpcion());
                pt.setLong(contParam++, oriCECriteriosCentro.getCentroSeleccionOpcionVAL());
                pt.setString(contParam++, oriCECriteriosCentro.getNumeroExpediente());
                pt.setInt(contParam++, oriCECriteriosCentro.getEjercicioConvocatoria());
                log.info("Param ? : " + oriCECriteriosCentro.toString()
                );
                retorno = pt.executeUpdate()>0;
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error crearOriCECriteriosCentro ... " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("crearOriCECriteriosCentro - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public boolean actualizarOriCECriteriosCentro(OriCECriteriosCentro oriCECriteriosCentro, Connection con) throws Exception {
        log.info("actualizarOriCECriteriosCentro - Begin () " + formatDate.format(new Date()));
        boolean retorno = true;
        PreparedStatement pt = null;
        try {
            if(oriCECriteriosCentro!=null){
                String query = " UPDATE "
                        + " Ori_CE_Criterios_Centro "
                        + " SET CENTROSELECCIONOPCION=?, centroSeleccionOpcionVal=?)"
                        + " WHERE "
                        + " id=? "
                        ;
                log.info("sql = " + query);
                pt = con.prepareStatement(query);
                int contParam = 1;
                pt.setLong(contParam++, oriCECriteriosCentro.getCentroSeleccionOpcion());
                pt.setLong(contParam++, oriCECriteriosCentro.getCentroSeleccionOpcionVAL());
                pt.setLong(contParam++, oriCECriteriosCentro.getId());
                log.info("Param ? : " + oriCECriteriosCentro.toString()
                );
                retorno = pt.executeUpdate()>0;
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error actualizarOriCECriteriosCentro ... " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("actualizarOriCECriteriosCentro - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public boolean eliminarOriCECriteriosCentro(long id, Connection con) throws Exception {
        log.info("eliminarOriCECriteriosCentro - Begin () " + formatDate.format(new Date()));
        boolean retorno = true;
        PreparedStatement pt = null;
        try {
            String query = " delete from "
                    + " Ori_CE_Criterios_Centro "
                    + " WHERE "
                    + " id=? "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setLong(contParam++, id);
            log.info("Param ? : " + id
            );
            retorno = pt.executeUpdate()>0;
        } catch (Exception ex) {
            log.info("Se ha producido un error eliminarOriCECriteriosCentro ... " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("eliminarOriCECriteriosCentro - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public boolean eliminarOriCECriteriosCentroByIdCentro(long idCentro, Connection con) throws Exception {
        log.info("eliminarOriCECriteriosCentroByIdCentro - Begin () " + formatDate.format(new Date()));
        boolean retorno = true;
        PreparedStatement pt = null;
        try {
            String query = " delete from "
                    + " Ori_CE_Criterios_Centro "
                    + " WHERE "
                    + " idcentro=? "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setLong(contParam++, idCentro);
            log.info("Param ? : " + idCentro
            );
            int filasEliminadas = pt.executeUpdate();
            log.info("Se han eliminado " + filasEliminadas + " Asociadas al centro : " + idCentro);
            retorno = filasEliminadas > 0;
        } catch (Exception ex) {
            log.info("Se ha producido un error eliminarOriCECriteriosCentroByIdCentro ... " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("eliminarOriCECriteriosCentroByIdCentro - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosEvaDTO> getCriteriosPuntuacionXUbicacionCE(CeUbicacionVO ubicacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<OriCECriteriosEvaDTO> respuesta = new ArrayList<OriCECriteriosEvaDTO>();
        try {
            String query = "select  "+(ubicacion!=null ? ubicacion.getOriCeUbicCod() : "null")+" as ori_ce_ubic_cod, oce.*, nvl(cup.puntuacion,0) puntuacion  " +
                    "from ori_ce_criterios_eva oce  " +
                    "left join ( select  ub.ori_ce_ubic_cod, crc.idcriterio, crc.puntuacion  " +
                    " from ori_ce_ubic ub   " +
                    " left join (  " +
                    "        WITH opciones_filtradas AS (  " +
                    "            SELECT cc.idcentro,cc.idcriterio,NVL(op.codigosubgrupo, 0) AS codigosubgrupo,op.puntuacion  " +
                    "            FROM ori_ce_criterios_centro cc  " +
                    "            JOIN ori_ce_criterios_eva_opcion op ON cc.idcriterio = op.idcriteriofk AND cc.idcriterioopcion = op.id  " +
                    "            WHERE cc.CENTROSELECCIONOPCION = 1 AND cc.CENTROSELECCIONOPCIONVAL = 1  " +
                    "                AND cc.ejercicioConvocatoria = "+ MeLanbide32Utils.getEjercicioDeExpediente(ubicacion.getNumeroExpediente())+" AND op.ejercicioConvocatoria =  " + MeLanbide32Utils.getEjercicioDeExpediente(ubicacion.getNumeroExpediente()) +
                    "        ),  " +
                    "        subgrupos_agrupados AS (  " +
                    "            SELECT idcentro,idcriterio,codigosubgrupo,  " +
                    "                CASE  " +
                    "                    WHEN codigosubgrupo = 0 or codigosubgrupo = 1 THEN MAX(puntuacion)  " +
                    "                    WHEN codigosubgrupo = 4 THEN   " +
                    "                        CASE   " +
                    "                            WHEN SUM(puntuacion) > ( SELECT MAXIMAPUNTUACIONSUBGRUPO FROM ori_ce_criterio_eva_subgrup_opc WHERE id = 4) THEN 2  " +
                    "                            ELSE SUM(puntuacion)  " +
                    "                        END  " +
                    "                    ELSE SUM(puntuacion)  " +
                    "                END AS puntuacion_normalizada  " +
                    "            FROM opciones_filtradas  " +
                    "            GROUP BY idcentro, idcriterio, codigosubgrupo  " +
                    "        ),  " +
                    "        final_puntuacion AS (  " +
                    "            SELECT idcentro,idcriterio,SUM(puntuacion_normalizada) AS puntuacion  " +
                    "            FROM subgrupos_agrupados  " +
                    "            GROUP BY idcentro, idcriterio  " +
                    "        )  " +
                    "        SELECT *   " +
                    "        FROM final_puntuacion  " +
                    "        ORDER BY idcentro, idcriterio  " +
                    "    )crc on crc.idcentro=ub.ori_ce_ubic_cod    " +
                    " where ub.ori_ce_ubic_cod= " +(ubicacion!=null ? ubicacion.getOriCeUbicCod() : "null")+
                    " ) cup on cup.idcriterio=oce.id  " +
                    " where oce.ejercicioConvocatoria = " + MeLanbide32Utils.getEjercicioDeExpediente(ubicacion.getNumeroExpediente()) +
                    " order by to_number(CODIGOORDEN)"
                    ;
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                OriCECriteriosEva criterioTemp = mappingUtils.getOriCECriteriosEva(rs);
                respuesta.add(new OriCECriteriosEvaDTO(criterioTemp
                        ,oriCECriteriosEvaOpcionDAO.getOriCECriteriosEvaOpcionByIdCriterioFK(criterioTemp.getId(), con), rs.getInt("puntuacion"))
                );
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los años de trayectoria validada CE para la entidad " + (ubicacion != null ? ubicacion.getOriCeUbicCod(): "(Ubicacion = null)"), ex);
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
        return respuesta;
    }
    
}
