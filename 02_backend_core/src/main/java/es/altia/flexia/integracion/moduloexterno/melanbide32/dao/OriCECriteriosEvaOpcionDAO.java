package es.altia.flexia.integracion.moduloexterno.melanbide32.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEvaOpcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
public class OriCECriteriosEvaOpcionDAO {
    
    private static final Logger log = LogManager.getLogger(OriCECriteriosEvaOpcionDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final MappingUtils mappingUtils = MappingUtils.getInstance();
    
    public OriCECriteriosEvaOpcion getOriCECriteriosEvaOpcionById(long id, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaOpcionById - Begin () " + formatDate.format(new Date()));
        OriCECriteriosEvaOpcion retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM ORI_CE_CRITERIOS_EVA_OPCION "
                    + " WHERE id=? ";
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setLong(1, id);
            log.info("Param ? : " + id
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = mappingUtils.getOriCECriteriosEvaOpcion(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEvaOpcion ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaOpcionById - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public OriCECriteriosEvaOpcion getOriCECriteriosEvaOpcionByIdCriterioFKCodigoOrden(long idCriterioFK,String codigoOrden, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaOpcionByIdCriterioFKCodigoOrden - Begin () " + formatDate.format(new Date()));
        OriCECriteriosEvaOpcion retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Eva_Opcion "
                    + " WHERE idCriterioFK=? "
                    + " and codigoOrden=? "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setLong(contParam++, idCriterioFK);
            pt.setString(contParam, codigoOrden);
            log.info("Param ? : " + idCriterioFK
                    + "," + codigoOrden
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = mappingUtils.getOriCECriteriosEvaOpcion(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEvaOpcion ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaOpcionByIdCriterioFKCodigoOrden - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosEvaOpcion> getOriCECriteriosEvaOpcionByIdCriterioFK(long idCriterioFK, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaOpcionByIdCriterioFK - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosEvaOpcion> retorno = new ArrayList<OriCECriteriosEvaOpcion>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Eva_Opcion "
                    + " WHERE idCriterioFK=? "
                    + " order by  idcriteriofk, regexp_substr(codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(codigoOrden, '\\d+')) "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setLong(contParam++, idCriterioFK);
            log.info("Param ? : " + idCriterioFK
            );
            rs = pt.executeQuery();
            while(rs.next()) {
                retorno.add(mappingUtils.getOriCECriteriosEvaOpcion(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEvaOpcion ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaOpcionByIdCriterioFK - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
}
