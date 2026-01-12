package es.altia.flexia.integracion.moduloexterno.melanbide32.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.MeLanbide32Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEva;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEvaDTO;
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
public class OriCECriteriosEvaDAO {
    
    private static final Logger log = LogManager.getLogger(OriCECriteriosEvaDAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final MappingUtils mappingUtils = MappingUtils.getInstance();
    private final OriCECriteriosEvaOpcionDAO oriCECriteriosEvaOpcionDAO = new OriCECriteriosEvaOpcionDAO();
    
    public OriCECriteriosEva getOriCECriteriosEvaById(long id, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaById - Begin () " + formatDate.format(new Date()));
        OriCECriteriosEva retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Eva "
                    + " WHERE id=? "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setLong(1, id);
            log.info("Param ? : " + id
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = mappingUtils.getOriCECriteriosEva(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEva ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaById - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public OriCECriteriosEva getOriCECriteriosEvaByCodigoCodigoOrden(String codigo,String codigoOrden, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaByCodigoCodigoOrden - Begin () " + formatDate.format(new Date()));
        OriCECriteriosEva retorno = null;
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Eva "
                    + " WHERE codigo=? "
                    + " and codigoOrden=? "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setString(contParam++, codigo);
            pt.setString(contParam, codigoOrden);
            log.info("Param ? : " + codigo
                    + "," + codigoOrden
            );
            rs = pt.executeQuery();
            if (rs.next()) {
                retorno = mappingUtils.getOriCECriteriosEva(rs);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEva ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaByCodigoCodigoOrden - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosEva> getOriCECriteriosEvaByCodigo(String codigo, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaByCodigo - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosEva> retorno = new ArrayList<OriCECriteriosEva>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Eva "
                    + " WHERE codigo=? "
                    + " order by codigo ,regexp_substr(codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(codigoOrden, '\\d+')) "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setString(contParam++, codigo);
            log.info("Param ? : " + codigo
            );
            rs = pt.executeQuery();
            while (rs.next()) {
                retorno.add(mappingUtils.getOriCECriteriosEva(rs));
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEva ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaByCodigo - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public List<OriCECriteriosEvaDTO> getOriCECriteriosEvaDTOByCodigo(String codigo, Integer ejercicioConvocatoria, Connection con) throws Exception {
        log.info("getOriCECriteriosEvaDTOByCodigo - Begin () " + formatDate.format(new Date()));
        List<OriCECriteriosEvaDTO> retorno = new ArrayList<OriCECriteriosEvaDTO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = " SELECT * "
                    + " FROM Ori_CE_Criterios_Eva "
                    + " WHERE codigo=? "
                    + " and EJERCICIOCONVOCATORIA =? "
                    + " order by codigo ,regexp_substr(codigoOrden, '^\\D*') nulls first,to_number(regexp_substr(codigoOrden, '\\d+')) "
                    ;
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            int contParam = 1;
            pt.setString(contParam++, codigo);
            pt.setInt(contParam++, ejercicioConvocatoria);
            log.info("Param ? : " + codigo + " , " + ejercicioConvocatoria
            );
            rs = pt.executeQuery();
            while(rs.next()) {
                OriCECriteriosEva criterioTemp = mappingUtils.getOriCECriteriosEva(rs);
                retorno.add(new OriCECriteriosEvaDTO(criterioTemp
                        ,oriCECriteriosEvaOpcionDAO.getOriCECriteriosEvaOpcionByIdCriterioFK(criterioTemp.getId(), con))
                );
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger entity OriCECriteriosEva ... " + ex.getMessage(), ex);
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
        log.info("getOriCECriteriosEvaDTOByCodigo - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
}
