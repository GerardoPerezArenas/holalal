package es.altia.flexia.integracion.moduloexterno.meikus.manager;

import es.altia.agora.business.sge.persistence.manual.DatosSuplementariosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.EstructuraCampo;
import es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.PagoPasarelaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao.ConvocatoriaDAO;
import es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao.DatosPasikusDAO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.DatosPasikusVO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao.PagosPasarelaDao;
import es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao.RespuestasOperacionesDAO;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeMeikus01ConnectionUtils;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.ConvocatoriaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.RespuestaOperacionVO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.TerceroPasarela;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 29/12/2012 1.0 Historial de cambios:
 * <ol>
 * <li>david.caamano * 29/12/2012 * Edición inicial</li>
 * </ol>
 */
public class MeIkus01Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeIkus01Manager.class);

    //Instance
    private static MeIkus01Manager instance = null;

    /**
     * Método que devuelve una unica instancia (Singleton) de la clase
     * MeIkus01Manager.
     *
     * @return MeIkus01Manager
     */
    public static MeIkus01Manager getInstance() {
        if (instance == null) {
            synchronized (MeIkus01Manager.class) {
                if (instance == null) {
                    log.debug("Generamos una nueva instancia de MeIkus01Manager");
                    instance = new MeIkus01Manager();
                }//if(instance == null)
            }//synchronized(MeIkus01Manager.class)
        }//if(instace == null)
        return instance;
    }//getInstance

    /**
     * Recupera una lista de convocatorias para un codigo de procedimiento
     *
     * @param codProcedimiento
     * @param codOrganizacion
     * @return Vector <ConvocatoriaVO>
     * @throws MeIkus01Exception
     */
    public Vector<ConvocatoriaVO> getConvocatorias(String codProcedimiento, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("getConvocatorias() : BEGIN");
        Vector<ConvocatoriaVO> listaConvocatorias = new Vector<ConvocatoriaVO>();
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            ConvocatoriaDAO convocatoriaDao = ConvocatoriaDAO.getInstance();
            listaConvocatorias = convocatoriaDao.getConvocatorias(codProcedimiento, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando las convocatorias " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error recuperando las convocatorias " + e.getMessage(), e);
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las convocatorias " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error recuperando las convocatorias " + ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando las convocatorias " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error recuperando las convocatorias " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("getConvocatorias() : END");
        return listaConvocatorias;
    }//getConvocatorias

    /**
     * Comprueba si existen convocatorias para un ejercicio y codigo de
     * procedimiento dado
     *
     * @param ejercicio
     * @param codProcedimiento
     * @param codOrganizacion
     * @return Boolean
     * @throws MeIkus01Exception
     */
    public boolean existeConvocatoria(int ejercicio, String codProcedimiento, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("existeConvocatoria() : BEGIN");
        Boolean retorno = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            ConvocatoriaDAO convocatoriaDao = ConvocatoriaDAO.getInstance();
            retorno = convocatoriaDao.existeConvocatoria(ejercicio, codProcedimiento, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido un error comprobando si existe la convocatoria " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error comprobando si existe la convocatoria " + e.getMessage(), e);
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si existe la convocatoria " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error comprobando si existe la convocatoria " + ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Se ha producido un error comprobando si existe la convocatoria " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error comprobando si existe la convocatoria " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("existeConvocatoria() : END");
        return retorno;
    }//existeConvocatoria

    /**
     * Inserta una nueva convocatoria en la BBDD
     *
     * @param convocatoriaVO
     * @param codOrganizacion
     * @return boolean
     * @throws MeIkus01Exception
     */
    public boolean insertarConvocatoria(ConvocatoriaVO convocatoriaVO) throws MeIkus01Exception {
        log.debug("insertarConvocatoria() : BEGIN");
        Boolean retorno = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(convocatoriaVO.getCodOrganizacion());
            con = adapt.getConnection();
            con.setAutoCommit(false);
            ConvocatoriaDAO convocatoriaDao = ConvocatoriaDAO.getInstance();
            retorno = convocatoriaDao.insertarConvocatoria(convocatoriaVO, con);
            log.debug("Realizamos un commit de la operacion");
            con.commit();
        } catch (BDException e) {
            try {
                log.error("Realizamos un rollback de la operacion");
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al insertar la convocatoria " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error insertando la convocatoria " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error insertando la convocatoria " + e.getMessage(), e);
        } catch (SQLException ex) {
            try {
                log.error("Realizamos un rollback de la operacion");
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al insertar la convocatoria " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error insertando la convocatoria " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error insertando la convocatoria " + ex.getMessage(), ex);
        } catch (Exception e) {
            try {
                log.error("Realizamos un rollback de la operacion");
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al insertar la convocatoria " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error insertando la convocatoria " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error insertando la convocatoria " + e.getMessage(), e);
        } finally {
            try {
                log.error("Devolvemos la conexion");
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("insertarConvocatoria() : END");
        return retorno;
    }//insertarConvocatoria

    /**
     * ELimina una convocatoria de la lista
     *
     * @param convocatoriaVO
     * @param codOrganizacion
     * @return boolean
     * @throws MeIkus01Exception
     */
    public boolean eliminarConvocatoria(ConvocatoriaVO convocatoriaVO, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("eliminarConvocatoria() : BEGIN");
        Boolean retorno = false;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            con.setAutoCommit(false);
            ConvocatoriaDAO convocatoriaDao = ConvocatoriaDAO.getInstance();
            retorno = convocatoriaDao.eliminarConvocatoria(convocatoriaVO, con);
            con.commit();
        } catch (BDException e) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al eliminar la convocatoria: " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error al eliminar la convocatoria " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un al eliminar la convocatoria " + e.getMessage(), e);
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al eliminar la convocatoria " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error al eliminar la convocatoria " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error al eliminar la convocatoria " + ex.getMessage(), ex);
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al eliminar la convocatoria " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error al eliminar la convocatoria " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error al eliminar la convocatoria " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("eliminarConvocatoria() : END");
        return retorno;
    }//eliminarConvocatoria

    /**
     * Graba en la BBDD los datos de un objeto RespuestaOperacionVO
     *
     * @param respuesta
     * @throws MeIkus01Exception
     */
    public void grabarRespuestaOperacion(RespuestaOperacionVO respuesta) throws MeIkus01Exception {
        log.debug("grabarRespuestaOperacion() : BEGIN");
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(respuesta.getCodOrganizacion());
            con = adapt.getConnection();
            con.setAutoCommit(false);
            RespuestasOperacionesDAO respuestasOperacionesDao = RespuestasOperacionesDAO.getInstance();
            respuestasOperacionesDao.grabarRespuestaOperacion(respuesta, con);
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al grabar la respuesta de la operacion " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error grabando la respuesta de la operacion " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error grabando la respuesta de la operacion " + ex.getMessage(), ex);
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al grabar la respuesta de la operacion " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error grabando la respuesta de la operacion " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error grabando la respuesta de la operacion " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("grabarRespuestaOperacion() : END");
    }//grabarRespuestaOperacion

    /**
     * Da de alta o modifica los datos de pago de un expediente
     *
     * @param pago
     * @throws MeIkus01Exception
     */
    public void altaExpedientePago(PagoPasarelaVO pago) throws MeIkus01Exception {
        log.debug("altaExpedientePago() : BEGIN");
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(pago.getCodOrganizacion());
            con = adapt.getConnection();
            con.setAutoCommit(false);
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            pagosPasarelaDao.altaExpediente(pago, con);
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al insertar/actualizar el pago del expediente: " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error en el metodo altaExpedientePago " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error en el metodo altaExpedientePago " + ex.getMessage(), ex);
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException f) {
                log.error("Error al realizar un rollback al insertar/actualizar el pago del expediente: " + f.getMessage());
            }//try-catch
            log.error("Se ha producido un error en el metodo altaExpedientePago " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo altaExpedientePago " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("altaExpedientePago() : END");
    }//altaExpedientePago

    /**
     * Devuelve los datos de pago de un expediente
     *
     * @param numExpediente
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    public PagoPasarelaVO getPagoExpediente(String numExpediente, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("getPagoExpediente() : BEGIN");
        PagoPasarelaVO pagoExpediente = new PagoPasarelaVO();
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            pagoExpediente = pagosPasarelaDao.getExpediente(numExpediente, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en el metodo getPagoExpediente " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getPagoExpediente " + ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("getPagoExpediente() : END");
        return pagoExpediente;
    }//getPagoExpediente

    /**
     * Anhade el id del expediente generado por la pasarela de pago al registro
     * de pago
     *
     * @param pago
     * @throws MeIkus01Exception
     */
    public void anhadirIdExpedientePasarela(PagoPasarelaVO pago) throws MeIkus01Exception {
        log.debug("anhadirIdExpedientePasarela() : BEGIN");
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(pago.getCodOrganizacion());
            con = adapt.getConnection();
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            pagosPasarelaDao.anhadirIdExpedientePasarela(pago, con);
        } catch (BDException e) {
            log.error("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en el metodo getPagoExpediente " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getPagoExpediente " + ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getPagoExpediente " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("anhadirIdExpedientePasarela() : END");
    }//anhadirIdExpedientePasarela

    /**
     * Recupera la ultima ocurrencia de un tramite en un expediente
     *
     * @param numExpediente
     * @param codTramite
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    public Integer getOcurrencia(String numExpediente, Integer codTramite, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("getOcurrencia() : BEGIN");
        Integer ocurrencia;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            ocurrencia = pagosPasarelaDao.getOcurrencia(numExpediente, codTramite, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en el metodo getOcurrencia " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getOcurrencia " + ex.getMessage(), ex);
        } catch (Exception e) {
            log.error("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.debug("getOcurrencia() : END");
        return ocurrencia;
    }//getPagoExpediente

    /**
     * Recupera el código de la procincia del domicilio del interesado que
     * consta en el expediente
     *
     * @param codTercero
     * @param numExpediente
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    public String getCodigoProvincia(String codTercero, String numExpediente, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("getCodigoProvincia() : BEGIN");
        String codProv;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            codProv = pagosPasarelaDao.getCodigoProvincia(codTercero, numExpediente, codOrganizacion, con);
        } catch (Exception e) {
            log.error("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }
        log.info("getCodigoProvincia() :  " + codProv);

        return codProv;
    }

        public String getCodigoPostal(String codTercero, String numExpediente, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("getCodigoPostal() : BEGIN");
        String codPostal;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            codPostal = pagosPasarelaDao.getCodigoPostal(codTercero, numExpediente, codOrganizacion, con);
        } catch (Exception e) {
            log.error("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo getOcurrencia " + e.getMessage(), e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + e.getMessage());
            }//try-catch
        }
        log.info("getCodigoPostal() :  " + codPostal);

        return codPostal;
    }
    public void grabarIdTercero(TerceroPasarela datosTercero, String codProcedimiento, Integer codOrganizacion) throws MeIkus01Exception {
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            PagosPasarelaDao pagosPasarelaDao = PagosPasarelaDao.getInstance();
            pagosPasarelaDao.grabarIdTerceroPasikus(datosTercero, codProcedimiento, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido un error en el metodo grabarIdTercero " + e.getMessage(), e);
            throw new MeIkus01Exception("Se ha producido un error en el metodo grabarIdTercero " + e.getMessage(), e);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en el metodo grabarIdTercero " + ex.getMessage(), ex);
            throw new MeIkus01Exception("Se ha producido un error en el metodo grabarIdTercero " + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Error al cerrar conexión a la BBDD: " + ex.getMessage());
                throw new MeIkus01Exception("Se ha producido un error al devolver la conexion de BBDD " + ex.getMessage());
            }
        }
    }

    public ConvocatoriaVO getConvocatoriaId(String codProcedimiento, String id, int codOrganizacion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean ningunCampoNumericoEsNulo(final List<String> campos, int codOrganizacion, String numExp) {
        for (final String codCampo : campos) {
            try {
                final String valorCampo = getSuplementarioNumerico(codOrganizacion, numExp, numExp.split("/")[0], codCampo);
                if (valorCampo == null) {
                    return false;
                }
            } catch (final Exception ex) {
                return false;
            }
        }
        return true;
    }

    public String getSuplementarioNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo) throws Exception {
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        String valor = null;
        DatosPasikusDAO datosPasikusDAO = DatosPasikusDAO.getInstance();
        BigDecimal numero = null;
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            numero = datosPasikusDAO.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Numerico." + e.getMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (numero != null) {
            try {
                valor = numero.toPlainString();
            } catch (Exception e) {
                log.error("Fallo al convertir BigDecimal: " + e);
            }
        }
        return valor;
    }
    
    public boolean ningunCampoTextoEsNuloOVacio(final List<String> campos, int codOrganizacion, String numExp) {
        for (final String codCampo : campos) {
            try {
                final String valorCampo = getSuplementarioTexto(codOrganizacion, numExp, numExp.split("/")[0], codCampo);
                if (valorCampo == null || valorCampo.isEmpty()) {
                    return false;
                }
            } catch (final Exception ex) {
                return false;
            }
        }
        return true;
    }

    public String getSuplementarioTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo) throws Exception {
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        String valor = null;
        DatosPasikusDAO datosPasikusDAO = DatosPasikusDAO.getInstance();
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            valor = datosPasikusDAO.getValorCampoTexto(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo Numerico", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.debug("Error al obtener Campo Numerico." + e.getMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return valor;
    }

    public String getSuplementarioDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo) throws Exception {
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        String valor = null;
        DatosPasikusDAO datosPasikusDAO = DatosPasikusDAO.getInstance();
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();
            valor = datosPasikusDAO.getValorCampoDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando campo desplegable", e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Error al obtener Campo Desplegable." + e.getMessage());
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        return valor;
    }

    public int guardarDatosPasikus(int codOrganizacion, final DatosExpedienteVO datosExpediente,
                                   final String codMunicipio, final DatosPasikusVO datosPasikusVO) throws Exception {
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        int[] result = new int[14];
        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();

            final EstructuraCampo eC = new EstructuraCampo();
            eC.setCodCampo("ENVIOEIKA");
            eC.setCodTipoDato("6");
            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", codMunicipio);
            gVO.setAtributo("ejercicio", new Integer(datosExpediente.getEjercicio()).toString());
            gVO.setAtributo("numero", datosExpediente.getNumExpediente());
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getEnvioEika());
            result[0] = setDatoDesplegable(adapt, eC, gVO);

            eC.setCodCampo("IMPRESERVA");
            eC.setCodTipoDato("1");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getImpReserva());
            result[1] = setDatoNumerico(adapt, eC, gVO);

            eC.setCodCampo("IDRESERVA");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getIdReserva());
            result[2] = setDatoTexto(adapt, eC, gVO);

            eC.setCodCampo("IMPANIO1");
            eC.setCodTipoDato("1");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getImpanio1());
            result[3] = setDatoNumerico(adapt, eC, gVO);

            eC.setCodCampo("IMPANIO2");
            eC.setCodTipoDato("1");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getImpanio2());
            result[4] = setDatoNumerico(adapt, eC, gVO);

            eC.setCodCampo("IDIKUS");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getIdIkus());
            result[5] = setDatoTexto(adapt, eC, gVO);

            eC.setCodCampo("EXPEIKAD");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getExpeIkad());
            result[6] = setDatoTexto(adapt, eC, gVO);

            eC.setCodCampo("IMPORTECONCEDIDO");
            eC.setCodTipoDato("1");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getImporteConcedido());
            result[7] = setDatoNumerico(adapt, eC, gVO);

            eC.setCodCampo("PAGO1");
            eC.setCodTipoDato("1");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getPago1());
            result[8] = setDatoNumerico(adapt, eC, gVO);

            eC.setCodCampo("PAGO2");
            eC.setCodTipoDato("1");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getPago2());
            result[9] = setDatoNumerico(adapt, eC, gVO);

            eC.setCodCampo("IDPAGO1");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getIdPago1());
            result[10] = setDatoTexto(adapt, eC, gVO);

            eC.setCodCampo("IDPAGO2");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getIdPago2());
            result[11] = setDatoTexto(adapt, eC, gVO);

            eC.setCodCampo("EXPEIKAO1");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getExpeIkao1());
            result[12] = setDatoTexto(adapt, eC, gVO);

            eC.setCodCampo("EXPEIKAO2");
            eC.setCodTipoDato("2");
            gVO.setAtributo(eC.getCodCampo(), datosPasikusVO.getExpeIkao2());
            result[13] = setDatoTexto(adapt, eC, gVO);

            for (int i = 0 ; i < result.length ; i++) {
                if (result[i] == 0) {
                    return 0;
                }
            }
            return 1;
        } catch (BDException e) {
            log.error("Se ha producido un error grabando el campo suplementario valor " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario valor : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexi?n a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoDesplegable(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoDesplegable(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoNumerico(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoNumerico(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private int setDatoTexto(AdaptadorSQLBD adaptador, EstructuraCampo eC, GeneralValueObject gVO) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            DatosSuplementariosDAO datosSuplementariosDAO = DatosSuplementariosDAO.getInstance();

            return datosSuplementariosDAO.setDatoTexto(adaptador, con, eC, gVO);
        } catch (BDException e) {
            log.error("Se ha producido un error de base de datos para el expediente " + (String) gVO.getAtributo("numero"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci?n para el expediente " + (String) gVO.getAtributo("numero"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int comprobarAutorizado(int codOrganizacion, final DatosExpedienteVO datosExpediente,
                                   final String codMunicipio, final String impReserva, final String impAnio1, final String impAnio2) throws Exception {
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        int[] result = {0, 0, 0};

        try {
            adapt = MeMeikus01ConnectionUtils.getAdapt(codOrganizacion);
            con = adapt.getConnection();

            final EstructuraCampo eC = new EstructuraCampo();
            final GeneralValueObject gVO = new GeneralValueObject();
            gVO.setAtributo("codMunicipio", codMunicipio);
            gVO.setAtributo("ejercicio", new Integer(datosExpediente.getEjercicio()).toString());
            gVO.setAtributo("numero", datosExpediente.getNumExpediente());

            if (impReserva == null || impReserva.equals("")) {
                final String impTot = getSuplementarioNumerico(codOrganizacion, datosExpediente.getNumExpediente(),
                        datosExpediente.getNumExpediente().split("/")[0], "IMPTOT");
                if (impTot != null && !impTot.equals("")) {
                    eC.setCodCampo("IMPRESERVA");
                    eC.setCodTipoDato("1");
                    gVO.setAtributo(eC.getCodCampo(), impTot);
                    result[0] = setDatoNumerico(adapt, eC, gVO);
                }
            }
            if (impAnio1 == null || impAnio1.equals("")) {
                final String impPago1 = getSuplementarioNumerico(codOrganizacion, datosExpediente.getNumExpediente(),
                        datosExpediente.getNumExpediente().split("/")[0], "IMPPAGO1");
                if (impPago1 != null && !impPago1.equals("")) {
                    eC.setCodCampo("IMPANIO1");
                    eC.setCodTipoDato("1");
                    gVO.setAtributo(eC.getCodCampo(), impPago1);
                    result[1] = setDatoNumerico(adapt, eC, gVO);
                }
            }
            if (impAnio2 == null || impAnio2.equals("")) {
                final String impPago2 = getSuplementarioNumerico(codOrganizacion, datosExpediente.getNumExpediente(),
                        datosExpediente.getNumExpediente().split("/")[0], "IMPPAGO2");
                if (impPago2 != null && !impPago2.equals("")) {
                    eC.setCodCampo("IMPANIO2");
                    eC.setCodTipoDato("1");
                    gVO.setAtributo(eC.getCodCampo(), impPago2);
                    result[2] = setDatoNumerico(adapt, eC, gVO);
                }
            }


            for (int i = 0; i < result.length; i++) {
                if (result[i] != 0) {
                    return 1;
                }
            }
            return 0;
        } catch (BDException e) {
            log.error("Se ha producido un error grabando el campo suplementario valor " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario valor : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

}//class
