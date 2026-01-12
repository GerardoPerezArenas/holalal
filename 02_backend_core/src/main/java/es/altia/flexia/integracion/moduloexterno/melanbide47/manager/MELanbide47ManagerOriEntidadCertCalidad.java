package es.altia.flexia.integracion.moduloexterno.melanbide47.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.OriEntidadCertCalidadDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriEntidadCertCalidad;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.List;

public class MELanbide47ManagerOriEntidadCertCalidad {

    private static final Logger log = LogManager.getLogger(MELanbide47ManagerOriEntidadCertCalidad.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    private static OriEntidadCertCalidadDAO oriEntidadCertCalidadDAO = new OriEntidadCertCalidadDAO();

    public OriEntidadCertCalidad getOriEntidadCertCalidadById(Integer id, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriEntidadCertCalidadById - Begin - " + id);
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.getOriEntidadCertCalidadById(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriEntidadCertCalidadById ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriEntidadCertCalidadById ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriEntidadCertCalidadById - End -");
        }
    }

    public List<OriEntidadCertCalidad> getOriEntidadCertCalidadByNumExp(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriEntidadCertCalidadByNumExp - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.getOriEntidadCertCalidadByNumExp(numExpediente,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriEntidadCertCalidadByNumExp ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriEntidadCertCalidadByNumExp ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriEntidadCertCalidadByNumExp - End -");
        }
    }

    public List<OriEntidadCertCalidad> getListaOriEntidadCertCalidadByCodEntidad(Integer codEntidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaOriEntidadCertCalidadByCodEntidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.getListaOriEntidadCertCalidadByCodEntidad(codEntidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getListaOriEntidadCertCalidadByCodEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getListaOriEntidadCertCalidadByCodEntidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getListaOriEntidadCertCalidadByCodEntidad - End -");
        }
    }

    public OriEntidadCertCalidad getOriEntidadCertCalidadByIdEntidadAndIdCertificado(Integer idEntidad, String idCertificado, AdaptadorSQLBD adapt) throws Exception {
        log.info("getOriEntidadCertCalidadByIdEntidadAndIdCertificado - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.getOriEntidadCertCalidadByIdEntidadAndIdCertificado(idEntidad,idCertificado,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getOriEntidadCertCalidadByIdEntidadAndIdCertificado ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getOriEntidadCertCalidadByIdEntidadAndIdCertificado ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("getOriEntidadCertCalidadByIdEntidadAndIdCertificado - End -");
        }
    }

    public boolean insertOriEntidadCertCalidad(OriEntidadCertCalidad oriEntidadCertCalidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("insertOriEntidadCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.insertOriEntidadCertCalidad(oriEntidadCertCalidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException insertOriEntidadCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion insertOriEntidadCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("insertOriEntidadCertCalidad - End -");
        }
    }

    public boolean updateOriEntidadCertCalidad(OriEntidadCertCalidad oriEntidadCertCalidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("updateOriEntidadCertCalidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.updateOriEntidadCertCalidad(oriEntidadCertCalidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException updateOriEntidadCertCalidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion updateOriEntidadCertCalidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("updateOriEntidadCertCalidad - End -");
        }
    }

    public boolean deleteOriEntidadCertCalidadByIdEntidad(Integer idEntidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("deleteOriEntidadCertCalidadByIdEntidad - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return deleteOriEntidadCertCalidadByIdEntidad(idEntidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteOriEntidadCertCalidadByIdEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteOriEntidadCertCalidadByIdEntidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("deleteOriEntidadCertCalidadByIdEntidad - End -");
        }
    }

    /**
     * Elimina datos de certificados de una entidad - recibe solo la conexion, consumidor debe gestionar cierre de conexion y commit si es transaccional
     * @param idEntidad
     * @param con
     * @return
     * @throws Exception
     */
    public boolean deleteOriEntidadCertCalidadByIdEntidad(Integer idEntidad, Connection con) throws Exception {
        try {
            return oriEntidadCertCalidadDAO.deleteOriEntidadCertCalidadByIdEntidad(idEntidad,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteOriEntidadCertCalidadByIdEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteOriEntidadCertCalidadByIdEntidad ", ex);
            throw ex;
        }
    }

    public boolean deleteOriEntidadCertCalidadById(Integer id, AdaptadorSQLBD adapt) throws Exception {
        log.info("deleteOriEntidadCertCalidadById - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.deleteOriEntidadCertCalidadById(id,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteOriEntidadCertCalidadById ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteOriEntidadCertCalidadById ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("deleteOriEntidadCertCalidadById - End -");
        }
    }

    public boolean deleteOriEntidadCertCalidadByNumExp(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.info("deleteOriEntidadCertCalidadByNumExp - Begin - " );
        Connection con = null;
        try {
            con = adapt.getConnection();
            return oriEntidadCertCalidadDAO.deleteOriEntidadCertCalidadByNumExp(numExp,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException deleteOriEntidadCertCalidadByNumExp ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion deleteOriEntidadCertCalidadByNumExp ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
            log.info("deleteOriEntidadCertCalidadByNumExp - End -");
        }
    }


    public void guardarDatosCertificadosCalidadSolicitudValidados(EntidadVO entidad, String certificadoCalidadLista, String certificadoCalidadListaValidado, AdaptadorSQLBD adaptador) {
        // Guardar datos de Certificados calidad
        Connection con = null;
        try {
            // Tratamos los datos de Cetificados de Calidad Solictus/Validados en transanccion. Estan en la misma tabla
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            // Datos Solicitud - Paso 1
            // Limpiamos lo que hay en BD
            log.info(
                    "Eliminados datos certificados calidad entidad  " + entidad.getOriEntCod()
                            + oriEntidadCertCalidadDAO.deleteOriEntidadCertCalidadByIdEntidad(entidad.getOriEntCod().intValue(),con)
            );
            if(certificadoCalidadLista != null && !certificadoCalidadLista.equalsIgnoreCase("null") && !certificadoCalidadLista.isEmpty()){
                String[] datosCertCalidad = certificadoCalidadLista.split(",");
                if(datosCertCalidad.length>0){
                    // Esta lista viene solo con los que se han marcado.
                    for (int i=0; i<datosCertCalidad.length;i++){
                        OriEntidadCertCalidad oriEntidadCertCalidadBD = oriEntidadCertCalidadDAO.getOriEntidadCertCalidadByIdEntidadAndIdCertificado(entidad.getOriEntCod().intValue(),datosCertCalidad[i],con);
                        if(oriEntidadCertCalidadBD != null && oriEntidadCertCalidadBD.getValorSNSolicitud()==1){
                            log.info("No se inserta, ya estaba marcado previamente en BD");
                        }else{
                            if(oriEntidadCertCalidadBD ==null){
                                // Nuevo no existia, se crea
                                oriEntidadCertCalidadBD = new OriEntidadCertCalidad();
                                oriEntidadCertCalidadBD.setIdEntidad(entidad.getOriEntCod().intValue());
                                oriEntidadCertCalidadBD.setIdCertificado(datosCertCalidad[i]);
                                oriEntidadCertCalidadBD.setNumExp(entidad.getExtNum());
                                oriEntidadCertCalidadBD.setValorSNSolicitud(1);
                                // Inicialmente lo cargamos con null, se ahce update en el siguiente paso si se ha validado
                                //oriEntidadCertCalidadBD.setValorSNValidado(0);
                                //oriEntidadCertCalidadBD.setId(0);
                                log.info("Guardamos datos? = "
                                        + oriEntidadCertCalidadDAO.insertOriEntidadCertCalidad(oriEntidadCertCalidadBD,con)
                                );
                            }else{
                                // Existia con un valor difetente,  actualizamos datos solicitud a 1. Validacion en el siguiente paso
                                oriEntidadCertCalidadBD.setValorSNSolicitud(1);
                                log.info("Actualizamos datos? = "
                                        + oriEntidadCertCalidadDAO.updateOriEntidadCertCalidad(oriEntidadCertCalidadBD,con)
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
                            OriEntidadCertCalidad oriEntidadCertCalidadBD = oriEntidadCertCalidadDAO.getOriEntidadCertCalidadByIdEntidadAndIdCertificado(entidad.getOriEntCod().intValue(),datosCertCalidadValidados1[0],con);
                            if(oriEntidadCertCalidadBD ==null){
                                // No existe datos para ese idCertificado, creamos la linea y solo marcamos la validacion a 1 (La solicitud la dejamos a null)
                                oriEntidadCertCalidadBD = new OriEntidadCertCalidad();
                                oriEntidadCertCalidadBD.setIdEntidad(entidad.getOriEntCod().intValue());
                                oriEntidadCertCalidadBD.setIdCertificado(datosCertCalidadValidados1[0]);
                                oriEntidadCertCalidadBD.setNumExp(entidad.getExtNum());
                                // Dejamos a null ya que no existia dato previo en BD
                                //oriEntidadCertCalidadBD.setValorSNSolicitud(1);
                                oriEntidadCertCalidadBD.setValorSNValidado(validadoSN_10);
                                //oriEntidadCertCalidadBD.setId(0);
                                log.info("Guardamos datos? = "
                                        + oriEntidadCertCalidadDAO.insertOriEntidadCertCalidad(oriEntidadCertCalidadBD,con)
                                );
                            }else{
                                // Existe en BD, verificamos si es necesario actualizar.
                                if(oriEntidadCertCalidadBD.getValorSNValidado()==validadoSN_10){
                                    log.info("Informacion en BD igual a la recibida desde Interfaz, no se procesa");
                                }else{
                                    // Procedemos a actualizar los datos
                                    oriEntidadCertCalidadBD.setValorSNValidado(validadoSN_10);
                                    log.info("Actualizamos datos? = "
                                            + oriEntidadCertCalidadDAO.updateOriEntidadCertCalidad(oriEntidadCertCalidadBD,con)
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
