/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicCTValoracion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class ColecUbicCTValoracionDAO {
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private final Logger log = LogManager.getLogger(MeLanbideConvocatoriasDAO.class);
    
    public ColecUbicCTValoracion getColecUbicCTValoracionByID(int id,Connection con) throws SQLException, Exception{
        log.info(" getColecUbicCTValoracionByID - Begin " + id + formatFechaLog.format(new Date()));
        ColecUbicCTValoracion resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_UBIC_CT_VALORACION "
                    + " where "
                    + " id=? ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, id);
            log.info("params = " + id);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecUbicCTValoracion)MeLanbide48MappingUtils.getInstance().map(rs, ColecUbicCTValoracion.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getColecUbicCTValoracionByID - End " + id + formatFechaLog.format(new Date()));
        return resultado;       
    }
    
    
    /**
     * Guarda los datos de validacion y valoracion de una ubicacion. Si no se recibe un idbD se inserta, si vienen aguno se actualiza
     * @param colecUbicCTValoracion
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public ColecUbicCTValoracion guardarColecUbicCTValoracion(ColecUbicCTValoracion colecUbicCTValoracion,Connection con) throws SQLException, Exception{
        log.info(" guardarColecUbicCTValoracion - Begin " +  formatFechaLog.format(new Date()));
        ColecUbicCTValoracion resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if(colecUbicCTValoracion!=null){
                String query = "";
                int contadorParam=1;
                if(colecUbicCTValoracion.getId()!=null){
                    
                    query=" update COLEC_UBIC_CT_VALORACION "
                            + " set "
                            + " numeroExpediente=? "
                            + " ,idFkUbicacion=? "
                            + " ,validarSegundosLocales =? "
                            + " ,validarPlanIgualdad =? "
                            + " ,validarCertificadoCalidad =? "
                            + " ,validarEspacioComplem =? "
                            + " ,validarCentroEspEmpleo =? "
                            + " ,validarEmpresaInsercion =? "
                            + " ,validarPromoEmpInsercion =? "
                            + " ,puntuacionTrayectoriaEntidad =? "
                            + " ,puntuacionUbicacionCT =? "
                            + " ,puntuacionSegundosLocales =? "
                            + " ,puntuacionPlanIgualdad =? "
                            + " ,puntuacionCertificadoCalidad =? "
                            + " ,puntuacionEspacioComplem =? "
                            + " ,puntuacionCentroEspEmpleo =? "
                            + " ,puntuacionEmpOpromEmpInsercion =? "
                            + " ,puntuacionObservaciones =? "
                            + " where "
                            + " id=? "
                        ;
                    ps = con.prepareStatement(query);
                    ps.setString(contadorParam++,colecUbicCTValoracion.getNumeroExpediente());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getIdFkUbicacion());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarSegundosLocales());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarPlanIgualdad());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarCertificadoCalidad());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarEspacioComplem());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarCentroEspEmpleo());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarEmpresaInsercion());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getValidarPromoEmpInsercion());
                    if (colecUbicCTValoracion.getPuntuacionTrayectoriaEntidad() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionTrayectoriaEntidad());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionUbicacionCT() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionUbicacionCT());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionSegundosLocales() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionSegundosLocales());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionPlanIgualdad() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionPlanIgualdad());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionCertificadoCalidad() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionCertificadoCalidad());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionEspacioComplem() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionEspacioComplem());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionCentroEspEmpleo() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionCentroEspEmpleo());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    if (colecUbicCTValoracion.getPuntuacionEmpOpromEmpInsercion() != null) {
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionEmpOpromEmpInsercion());
                    } else {
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    }
                    ps.setString(contadorParam++,colecUbicCTValoracion.getPuntuacionObservaciones());
                    ps.setInt(contadorParam++,colecUbicCTValoracion.getId());
                }else{
                    query = " insert into COLEC_UBIC_CT_VALORACION "
                            + "("
                            + " id "
                            + " ,numeroExpediente "
                            + " ,idFkUbicacion "
                            + " ,validarSegundosLocales  "
                            + " ,validarPlanIgualdad  "
                            + " ,validarCertificadoCalidad  "
                            + " ,validarEspacioComplem  "
                            + " ,validarCentroEspEmpleo  "
                            + " ,validarEmpresaInsercion  "
                            + " ,validarPromoEmpInsercion  "
                            + " ,puntuacionTrayectoriaEntidad  "
                            + " ,puntuacionUbicacionCT  "
                            + " ,puntuacionSegundosLocales  "
                            + " ,puntuacionPlanIgualdad  "
                            + " ,puntuacionCertificadoCalidad  "
                            + " ,puntuacionEspacioComplem  "
                            + " ,puntuacionCentroEspEmpleo  "
                            + " ,puntuacionEmpOpromEmpInsercion  "
                            + " ,puntuacionObservaciones  "
                            + ")"
                            + " values"
                            + "("
                            + " SEQ_COLEC_UBIC_CT_VALORACION.nextval "
                            + ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? "
                            + ") "
                            ;
                    ps = con.prepareStatement(query);
                    ps.setString(contadorParam++, colecUbicCTValoracion.getNumeroExpediente());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getIdFkUbicacion());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarSegundosLocales());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarPlanIgualdad());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarCertificadoCalidad());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarEspacioComplem());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarCentroEspEmpleo());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarEmpresaInsercion());
                    ps.setInt(contadorParam++, colecUbicCTValoracion.getValidarPromoEmpInsercion());
                    if(colecUbicCTValoracion.getPuntuacionTrayectoriaEntidad()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionTrayectoriaEntidad());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionUbicacionCT()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionUbicacionCT());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionSegundosLocales()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionSegundosLocales());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionPlanIgualdad()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionPlanIgualdad());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionCertificadoCalidad()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionCertificadoCalidad());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionEspacioComplem()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionEspacioComplem());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionCentroEspEmpleo()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionCentroEspEmpleo());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    if(colecUbicCTValoracion.getPuntuacionEmpOpromEmpInsercion()!=null)
                        ps.setDouble(contadorParam++, colecUbicCTValoracion.getPuntuacionEmpOpromEmpInsercion());
                    else
                        ps.setNull(contadorParam++, java.sql.Types.NULL);
                    ps.setString(contadorParam++, colecUbicCTValoracion.getPuntuacionObservaciones());
                }                 
                log.info("sql = " + query);
                log.info("params = " + colecUbicCTValoracion.toString());
                 if(ps.executeUpdate()>0){
                     log.info("Ejecucion Correcta...");
                     resultado=getColecUbicCTValoracionByidFkUbicacion(colecUbicCTValoracion.getIdFkUbicacion(), con);
                 }else
                     log.error("Datos no insertado o actualizados....!!!");
            }else
                log.info("Objeto  a insertar/modificar recibido a null, no se procesa, respuesta devuelta : null");
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" guardarColecUbicCTValoracion - End "  + formatFechaLog.format(new Date()));
        return resultado;       
    }
    
    public ColecUbicCTValoracion getColecUbicCTValoracionByidFkUbicacion(int idFkUbicacion, Connection con) throws SQLException, Exception {
        log.info(" getColecUbicCTValoracionByidFkUbicacion - Begin " + idFkUbicacion + formatFechaLog.format(new Date()));
        ColecUbicCTValoracion resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select * "
                    + " from COLEC_UBIC_CT_VALORACION "
                    + " where "
                    + " idFkUbicacion=? "
                    + " order by id "
                    ;
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setLong(1, idFkUbicacion);
            log.info("params = " + idFkUbicacion);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = (ColecUbicCTValoracion) MeLanbide48MappingUtils.getInstance().map(rs, ColecUbicCTValoracion.class);
            }
        } catch (SQLException e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido recuperando datos Valoracion Ubicacion ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getColecUbicCTValoracionByidFkUbicacion - End " + idFkUbicacion + formatFechaLog.format(new Date()));
        return resultado;
    }
}
