package es.altia.flexia.integracion.moduloexterno.melanbide48.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecEntidadCertCalidadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadCertCalidad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide48ManagerColecEntidadCertCalidad {

    private static final Logger log = LogManager.getLogger(MELanbide48ManagerColecEntidadCertCalidad.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static ColecEntidadCertCalidadDAO colecEntidadCertCalidadDAO = new ColecEntidadCertCalidadDAO();

    public ColecEntidadCertCalidad getColecEntidadCertCalidadById(Integer id, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecEntidadCertCalidadById - Begin - " + id);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.getColecEntidadCertCalidadById(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecEntidadCertCalidadById ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecEntidadCertCalidadById ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecEntidadCertCalidadById - End -");
        }
    }

    public List<ColecEntidadCertCalidad> getColecEntidadCertCalidadByNumExp(String numExpediente,AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecEntidadCertCalidadByNumExp - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.getColecEntidadCertCalidadByNumExp(numExpediente,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecEntidadCertCalidadByNumExp ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecEntidadCertCalidadByNumExp ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecEntidadCertCalidadByNumExp - End -");
        }
    }

    public List<ColecEntidadCertCalidad> getListaColecEntidadCertCalidadByCodEntidad(Integer codEntidad,AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaColecEntidadCertCalidadByCodEntidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.getListaColecEntidadCertCalidadByCodEntidad(codEntidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getListaColecEntidadCertCalidadByCodEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getListaColecEntidadCertCalidadByCodEntidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getListaColecEntidadCertCalidadByCodEntidad - End -");
        }
    }

    public ColecEntidadCertCalidad getColecEntidadCertCalidadByIdEntidadAndIdCertificado(Integer idEntidad, String idCertificado, AdaptadorSQLBD adapt) throws Exception {
        log.info("getColecEntidadCertCalidadByIdEntidadAndIdCertificado - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.getColecEntidadCertCalidadByIdEntidadAndIdCertificado(idEntidad,idCertificado,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecEntidadCertCalidadByIdEntidadAndIdCertificado ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecEntidadCertCalidadByIdEntidadAndIdCertificado ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getColecEntidadCertCalidadByIdEntidadAndIdCertificado - End -");
        }
    }

    public boolean insertColecEntidadCertCalidad(ColecEntidadCertCalidad colecEntidadCertCalidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("insertColecEntidadCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.insertColecEntidadCertCalidad(colecEntidadCertCalidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException insertColecEntidadCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion insertColecEntidadCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("insertColecEntidadCertCalidad - End -");
        }
    }

    public boolean updateColecEntidadCertCalidad(ColecEntidadCertCalidad colecEntidadCertCalidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("updateColecEntidadCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.updateColecEntidadCertCalidad(colecEntidadCertCalidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException updateColecEntidadCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion updateColecEntidadCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("updateColecEntidadCertCalidad - End -");
        }
    }

    public boolean deleteColecEntidadCertCalidadByIdEntidad(Integer idEntidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("deleteColecEntidadCertCalidadByIdEntidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return deleteColecEntidadCertCalidadByIdEntidad(idEntidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteColecEntidadCertCalidadByIdEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteColecEntidadCertCalidadByIdEntidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("deleteColecEntidadCertCalidadByIdEntidad - End -");
        }
    }

    /**
     * Elimina datos de certificados de una entidad - recibe solo la conexion, consumidor debe gestionar cierre de conexion y commit si es transaccional
     * @param idEntidad
     * @param con
     * @return
     * @throws Exception
     */
    public boolean deleteColecEntidadCertCalidadByIdEntidad(Integer idEntidad, Connection con) throws Exception {
        try {
            return colecEntidadCertCalidadDAO.deleteColecEntidadCertCalidadByIdEntidad(idEntidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteColecEntidadCertCalidadByIdEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteColecEntidadCertCalidadByIdEntidad ", ex);
            throw ex;
        }
    }

    public boolean deleteColecEntidadCertCalidadById(Integer id, AdaptadorSQLBD adapt) throws Exception {
        log.info("deleteColecEntidadCertCalidadById - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.deleteColecEntidadCertCalidadById(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteColecEntidadCertCalidadById ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteColecEntidadCertCalidadById ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("deleteColecEntidadCertCalidadById - End -");
        }
    }

    public boolean deleteColecEntidadCertCalidadByNumExp(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.info("deleteColecEntidadCertCalidadByNumExp - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecEntidadCertCalidadDAO.deleteColecEntidadCertCalidadByNumExp(numExp,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteColecEntidadCertCalidadByNumExp ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteColecEntidadCertCalidadByNumExp ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("deleteColecEntidadCertCalidadByNumExp - End -");
        }
    }


    public void guardarDatosCertificadosCalidadSolicitudValidados(ColecEntidadVO entidad, String certificadoCalidadLista, String certificadoCalidadListaValidado, AdaptadorSQLBD adaptador) {
        // Guardar datos de Certificados calidad
        Connection con = null;
        try {
            // Tratamos los datos de Cetificados de Calidad Solictus/Validados en transanccion. Estan en la misma tabla
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            // Datos Solicitud - Paso 1
            // Limpiamos lo que hay en BD
            log.info(
                    "Eliminados datos certificados calidad entidad  " + entidad.getCodEntidad()
                            + colecEntidadCertCalidadDAO.deleteColecEntidadCertCalidadByIdEntidad(entidad.getCodEntidad().intValue(),con)
            );
            if(certificadoCalidadLista != null && !certificadoCalidadLista.equalsIgnoreCase("null") && !certificadoCalidadLista.isEmpty()){
                String[] datosCertCalidad = certificadoCalidadLista.split(",");
                if(datosCertCalidad.length>0){
                    // Esta lista viene solo con los que se han marcado.
                    for (int i=0; i<datosCertCalidad.length;i++){
                        ColecEntidadCertCalidad colecEntidadCertCalidadBD = colecEntidadCertCalidadDAO.getColecEntidadCertCalidadByIdEntidadAndIdCertificado(entidad.getCodEntidad().intValue(),datosCertCalidad[i],con);
                        if(colecEntidadCertCalidadBD != null && colecEntidadCertCalidadBD.getValorSNSolicitud()==1){
                            log.info("No se inserta, ya estaba marcado previamente en BD");
                        }else{
                            if(colecEntidadCertCalidadBD==null){
                                // Nuevo no existia, se crea
                                colecEntidadCertCalidadBD = new ColecEntidadCertCalidad();
                                colecEntidadCertCalidadBD.setIdEntidad(entidad.getCodEntidad().intValue());
                                colecEntidadCertCalidadBD.setIdCertificado(datosCertCalidad[i]);
                                colecEntidadCertCalidadBD.setNumExp(entidad.getNumExp());
                                colecEntidadCertCalidadBD.setValorSNSolicitud(1);
                                // Inicialmente lo cargamos con null, se ahce update en el siguiente paso si se ha validado
                                //colecEntidadCertCalidadBD.setValorSNValidado(0);
                                //colecEntidadCertCalidadBD.setId(0);
                                log.info("Guardamos datos? = "
                                        + colecEntidadCertCalidadDAO.insertColecEntidadCertCalidad(colecEntidadCertCalidadBD,con)
                                );
                            }else{
                                // Existia con un valor difetente,  actualizamos datos solicitud a 1. Validacion en el siguiente paso
                                colecEntidadCertCalidadBD.setValorSNSolicitud(1);
                                log.info("Actualizamos datos? = "
                                        + colecEntidadCertCalidadDAO.updateColecEntidadCertCalidad(colecEntidadCertCalidadBD,con)
                                );
                            }
                        }
                    }
                }
            }
            //Datos Validados Paso 2
            if(certificadoCalidadListaValidado != null && !certificadoCalidadListaValidado.equalsIgnoreCase("null") && !certificadoCalidadListaValidado.isEmpty()){
                String[] datosCertCalidadValidados = certificadoCalidadListaValidado.split(",");
                if(datosCertCalidadValidados!= null && datosCertCalidadValidados.length>0) {
                    // Esta lista viene marcado idCertificado_S/N Hay que convetir S/N => 1/0
                    for (int i = 0; i < datosCertCalidadValidados.length; i++) {
                        String[] datosCertCalidadValidados1  = datosCertCalidadValidados[i].split("_");
                        if(datosCertCalidadValidados1!= null && datosCertCalidadValidados1.length==2){
                            int validadoSN_10 = "S".equalsIgnoreCase(datosCertCalidadValidados1[1]) ? 1 : 0;
                            ColecEntidadCertCalidad colecEntidadCertCalidadBD = colecEntidadCertCalidadDAO.getColecEntidadCertCalidadByIdEntidadAndIdCertificado(entidad.getCodEntidad().intValue(),datosCertCalidadValidados1[0],con);
                            if(colecEntidadCertCalidadBD==null){
                                // No existe datos para ese idCertificado, creamos la linea y solo marcamos la validacion a 1 (La solicitud la dejamos a null)
                                colecEntidadCertCalidadBD = new ColecEntidadCertCalidad();
                                colecEntidadCertCalidadBD.setIdEntidad(entidad.getCodEntidad().intValue());
                                colecEntidadCertCalidadBD.setIdCertificado(datosCertCalidadValidados1[0]);
                                colecEntidadCertCalidadBD.setNumExp(entidad.getNumExp());
                                // Dejamos a null ya que no existia dato previo en BD
                                //colecEntidadCertCalidadBD.setValorSNSolicitud(1);
                                colecEntidadCertCalidadBD.setValorSNValidado(validadoSN_10);
                                //colecEntidadCertCalidadBD.setId(0);
                                log.info("Guardamos datos? = "
                                        + colecEntidadCertCalidadDAO.insertColecEntidadCertCalidad(colecEntidadCertCalidadBD,con)
                                );
                            }else{
                                // Existe en BD, verificamos si es necesario actualizar.
                                if(colecEntidadCertCalidadBD.getValorSNValidado()==validadoSN_10){
                                    log.info("Informacion en BD igual a la recibida desde Interfaz, no se procesa");
                                }else{
                                    // Procedemos a actualizar los datos
                                    colecEntidadCertCalidadBD.setValorSNValidado(validadoSN_10);
                                    log.info("Actualizamos datos? = "
                                            + colecEntidadCertCalidadDAO.updateColecEntidadCertCalidad(colecEntidadCertCalidadBD,con)
                                    );
                                }
                            }
                        }else{
                            log.error("No se procesa " + datosCertCalidadValidados[i] +" formato incorrecto. idCertificado_S/N");
                        }
                    }
                }
            }
        adaptador.finTransaccion(con);
        }catch (Exception ex){
            try {
                adaptador.rollBack(con);
            } catch (BDException e) {
                log.error("Error Realizando el rollback de la transaccion "  + e.getMensaje() + " Detalles: " + e.getErrorCode()+" => " + e.getDescripcion());
            }
            log.error("Error al guardar datos de certificados de calidad - Entidad COLEC ",ex);
        }
    }
}
