/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide07.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide07.dao.MeLanbide07DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author
 */
public class MeLanbide07Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide07Manager.class);

    //Instancia
    private static MeLanbide07Manager instance = null;

    private MeLanbide07Manager() {

    }

    public static MeLanbide07Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide07Manager.class) {
                instance = new MeLanbide07Manager();
            }
        }
        return instance;
    }

    public Calendar getValorCampoFechaExpediente(int codOrg, String numExp, String ejercicio, String codCampo, String codProc, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getValorCampoFechaExpediente(codOrg, numExp, ejercicio, codCampo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD creando entidad ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando entidad ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getValorCampoTextoExpediente(int codOrg, String numExp, String ejercicio, String codCampo, String codProc, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getValorCampoTextoExpediente(codOrg, numExp, ejercicio, codCampo, codProc, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando IdDeuda ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getEstadoDeudaZORKU(String idDeuda, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getEstadoDeudaZORKU(idDeuda, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD creando entidad ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean compruebaFechaNotifFechaResolREINT(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.info("compruebaFechaNotifFechaResolREINT (  numExpediente = " + numExp + " ) : BEGIN");

        Connection con = adapt.getConnection();
        MeLanbide07DAO meLanbide07DAO = MeLanbide07DAO.getInstance();
        String[] datos = numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA);

        String codigoInternoTramite = meLanbide07DAO.obtenerCodigoInternoTramite(datos[1], ConstantesMeLanbide07.TRAMITE_4, con).toString();
        Date fechaResolucion = meLanbide07DAO.getValorFechaTramREINT(numExp, ConstantesMeLanbide07.TABLA_E_TFET, codigoInternoTramite, ConstantesMeLanbide07.CAMPO_FECRESOLUCION, con);

        codigoInternoTramite = meLanbide07DAO.obtenerCodigoInternoTramite(datos[1], ConstantesMeLanbide07.TRAMITE_5, con).toString();
        Date fechaNotifResolucion = meLanbide07DAO.getValorFechaTramREINT(numExp, ConstantesMeLanbide07.TABLA_E_TFET, codigoInternoTramite, ConstantesMeLanbide07.CAMPO_FECACUSENOTIFRESOL, con);
        adapt.devolverConexion(con);
        log.info("compruebaFechaNotifFechaResolREINT() : END");
        return fechaResolucion.before(fechaNotifResolucion) || fechaResolucion.equals(fechaNotifResolucion);
    }

    public boolean compruebaFechaNotifFechaLimPagoREINT(String numExp, AdaptadorSQLBD adapt) throws Exception {
        // esFechaLimitePagoMayorQueFechaNotificacion 
        log.info("compruebaFechaNotifFechaLimPagoREINT ( numExpediente = " + numExp + " ) : BEGIN");
        Connection con = adapt.getConnection();
        MeLanbide07DAO meLanbide07DAO = MeLanbide07DAO.getInstance();
        String[] datos = numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA);

        String codigoInternoTramite = meLanbide07DAO.obtenerCodigoInternoTramite(datos[1], ConstantesMeLanbide07.TRAMITE_5, con).toString();
        Date fechaNotifResolucion = meLanbide07DAO.getValorFechaTramREINT(numExp, ConstantesMeLanbide07.TABLA_E_TFET, codigoInternoTramite, ConstantesMeLanbide07.CAMPO_FECACUSENOTIFRESOL, con);

        Date fechaLimitePago = meLanbide07DAO.getValorFechaExpREINT(numExp, ConstantesMeLanbide07.CAMPO_FECLIMITEPAGO, con);
        adapt.devolverConexion(con);
        log.info("compruebaFechaNotifFechaLimPagoREINT() : END");
        return fechaNotifResolucion.before(fechaLimitePago);
    }

    public boolean compruebaFechaHoyFechaLimPagoREINT(String numExp, AdaptadorSQLBD adapt) throws Exception {
        // esHoyMayorQueFechaLimitePagoMas14Dias
        log.info("compruebaFechaHoyFechaLimPagoREINT ( numExpediente = " + numExp + " ) : BEGIN");
        Connection con = adapt.getConnection();
        MeLanbide07DAO meLanbide07DAO = MeLanbide07DAO.getInstance();

        Date fechaLimitePago = meLanbide07DAO.getValorFechaExpREINT(numExp, ConstantesMeLanbide07.CAMPO_FECLIMITEPAGO, con);
        Calendar fechaLimite = Calendar.getInstance();
        fechaLimite.setTime(fechaLimitePago);
        fechaLimite.add(Calendar.DATE, 14);
        Calendar hoy = Calendar.getInstance();
        adapt.devolverConexion(con);
        log.info("compruebaFechaHoyFechaLimPagoREINT() : END");
        return fechaLimite.before(hoy);
    }

    public String getCampoMotivoAnulacionDesplegable(int codOrg, String numExp, int ocurrencia, AdaptadorSQLBD adapt) throws BDException, Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getCampoMotivoAnulacionDesplegable(numExp, ocurrencia, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public String dameCodigoTipoVia(int codDomicilio, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().dameCodigoViaDomicilio(codDomicilio, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recogiendo Codigo Tipo Via ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean coincideCodigo(String codigoVia, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().coincideCodigo(codigoVia, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD comparando Codigo Tipo Via ", e);
            throw new Exception(e);
        }
    }

    public int getUsuarioExpediente(int codOrg, String numExp, String codProcedimiento, int codTramite, int ocurrenciaTramite, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getUsuarioExpediente(codOrg, numExp, codProcedimiento, codTramite, ocurrenciaTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el Usuario del  expediente " + numExp + " ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getNombreUsuario(int usuario, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getNombreUsuario(usuario, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el nombre del Usuario del  expediente ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getNombreFicheroExpediente(int codOrg, String codProcedimiento, String ejercicio, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getNombreFicheroExpediente(codOrg, codProcedimiento, ejercicio, numExp, codCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando el nombre de la carta de pago del  expediente " + numExp + " ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getImportePendienteZORKU(String numExp, String idDeuda, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getImportePendienteZORKU(numExp, idDeuda, con);
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando el importe pendiente de pago del expediente " + numExp + " ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int guardarValorCampoNumericoTramite(int codOrg, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en guardarValorCampoNumericoTramite Manager");
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().guardarValorCampoNumericoTramite(codOrg, procedimiento, ejercicio, numExp, tramite, ocurrencia, codigoCampo, valor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TNUT ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int guardarValorCampoTextoTramite(int codOrg, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en guardarValorCampoTextoTramite Manager");
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().guardarValorCampoTextoTramite(codOrg, procedimiento, ejercicio, numExp, tramite, ocurrencia, codigoCampo, valor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TXTT ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public BigDecimal getValorCampoNumericoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en getValorCampoNumericoTramite Manager");
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getValorCampoNumericoTramite(codOrg, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TNUT ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getValorCampoTextoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en getValorCampoDesplegableExternoTramite Manager");
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getValorCampoDesplegableExternoTramite(codOrg, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getValorDesplegableExternoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en getValorCampoDesplegableExternoTramite Manager");
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getValorCampoDesplegableExternoTramite(codOrg, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }

    }

    public int getMaxOcurrenciaTramitexCodigo(int codOrg, String procedimiento, String ejercicio, String numExp, int tram, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en getMaxOcurrenciaTramitexCodigo Manager");
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getMaxOcurrenciaTramitexCodigo(codOrg, procedimiento, ejercicio, numExp, tram, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getMaxOcurrenciaTramitexCodigo ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean deudaAbonada(int codOrg, int ejercicio, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            log.debug("==> entra en deudaAbonada Manager");
            return MeLanbide07DAO.getInstance().deudaAbonada(codOrg, ejercicio, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD deudaAbonada ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean desplegableMarcadoConX(int codOrg, String numExp, String codDesplegable, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().desplegableMarcadoConX(codOrg, numExp, codDesplegable, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarTextoExpediente(String numExp, String codDeudaABorrar, String iddeuda, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().borrarTextoExpediente(numExp, codDeudaABorrar, iddeuda, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarFechaExpediente(String numExp, String codFechaABorrar, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().borrarFechaExpediente(numExp, codFechaABorrar, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarDesplegableExpediente(int codOrg, String numExp, String codDesplegable, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().borrarDesplegableExpediente(codOrg, numExp, codDesplegable, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarFicheroExpediente(String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().borrarFicheroExpediente(numExp, codCampo, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarNumericoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().borrarNumericoTramite(codOrg, numExp, tramite, ocurrencia, codCampo, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean existeDocumentoTFI(String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().existeDocumentoTFI(numExp, codCampo, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean existeFechaExpediente(String numExp, String tabla, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().existeFechaExpREINT(numExp, tabla, codCampo, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getCodigoSubvencionEika(String ejerSubvencion, String codProcedimiento, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getCodigoSubvencionEika(ejerSubvencion, codProcedimiento, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean grabarCartaDokusi(int codOrg, String numExp, String codCampo, String nombreFichero, String oid, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().grabarCartaDokusi(codOrg, numExp, codCampo, nombreFichero, oid, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarRelacionDokusiCSE(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().borrarRelacionDokusiCSE(codOrg, numExp, codCampo, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepcion en la BBDD un fichero de expediente " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }
        public String getDocumentoInteresado(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide07DAO.getInstance().getDocumentoInteresado(codOrg, numExp, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el nº documento del interesado ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }
}
