package es.altia.flexia.integracion.moduloexterno.melanbide92.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide92.dao.MeLanbide92DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConstantesMeLanbide92;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ProcedimientoLireiVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide92Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide92Manager.class);

    //Instancia
    private static MeLanbide92Manager instance = null;
    private static final MeLanbide92DAO m92DAO = MeLanbide92DAO.getInstance();

    public static MeLanbide92Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide92Manager.class) {
                instance = new MeLanbide92Manager();
            }
        }
        return instance;
    }

    public int getCodigoInternoTramite(int codOrg, String procedimiento, String codExterno, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getCodigoInternoTramite(codOrg, procedimiento, codExterno, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion recuperando el código interno del trámite externo: " + codExterno + " del procedimiento: " + procedimiento, e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int getCodigoExternoTramite(int codOrg, String procedimiento, String codInterno, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getCodigoExternoTramite(codOrg, procedimiento, codInterno, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion recuperando el código externo del trámite interno: " + codInterno + " del procedimiento: " + procedimiento, e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int getMaxOcurrenciaTramitexCodigo(int codOrg, String numExp, int tram, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getMaxOcurrenciaTramitexCodigo(codOrg, numExp, tram, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getMaxOcurrenciaTramitexCodigo ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean tieneTramiteAbierto(int codOrg, String numExp, int codTramite, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
            try {
            con = adapt.getConnection();
            return m92DAO.tieneTramiteAbierto(codOrg, numExp, codTramite, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD cerrando un trámite ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int cerrarTramite(int codOrg, String numExp, int codTramite, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.cerrarTramite(codOrg, numExp, codTramite, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD cerrando un trámite ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int abrirTramite(int codOrg, String numExp, int codTramite, int ocurrencia, String uor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.abrirTramite(codOrg, numExp, codTramite, ocurrencia, uor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD abriendo un trámite ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getUnidadExpediente(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getUnidadExpediente(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getUnidadExpediente ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getValorDesplegableTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getValorDesplegableTramite(codOrg, numExp, tramite, ocurrencia, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el código de un desplegable externo de trámite " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int guardarValorDesplegableTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
            try {
            con = adapt.getConnection();
            return m92DAO.guardarValorDesplegableTramite(codOrg, numExp, tramite, ocurrencia, codCampo, valor, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el código de un desplegable externo de trámite " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getCodigoDespExternoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getCodigoDespExternoTramite(codOrg, numExp, tramite, ocurrencia, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el código de un desplegable externo de trámite " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getDescripcionDespExternoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getDescripcionDespExternoTramite(codOrg, numExp, tramite, ocurrencia, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la descripción de un desplegable externo de trámite " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int getUsuarioTramite(int codOrg, String numExpediente, String codProcedimiento, int codTramite, int ocurrenciaTramite, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            log.debug("==> entra en getUsuarioTramite Manager");
            return m92DAO.getUsuarioTramite(codOrg, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getUsuarioTramite ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getNombreUsuario(int usuario, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            log.debug("==> entra en getUsuarioTramite Manager");
            return m92DAO.getNombreUsuario(usuario, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getUsuarioTramite ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getValorCampoTextoExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getValorCampoTextoExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando el campo Texto de Expediente  " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarTextoExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.borrarTextoExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD borrando un campo Texto de Expediente " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int guardarValorCampoNumericoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            log.debug("==> entra en guardarValorCampoNumericoTramite Manager");
            con = adapt.getConnection();
            return m92DAO.guardarValorCampoNumericoTramite(codOrg, numExp, tramite, ocurrencia, codigoCampo, valor, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD guardando en la tabla E_TNUT ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public int borrarNumericoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.borrarNumericoTramite(codOrg, numExp, tramite, ocurrencia, codCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD borrando en la tabla E_TNUT ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean grabarCartaDokusi(int codOrg, String numExp, String codCampo, String nombreFichero, String oid, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.grabarCartaDokusi(codOrg, numExp, codCampo, nombreFichero, oid, con);
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
            return m92DAO.borrarRelacionDokusiCSE(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD un fichero de expediente " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public int borrarFicheroExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.borrarFicheroExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD un fichero de expediente " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean existeFicheroExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.existeFicheroExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD comprobando si existe el campo " + codCampo, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getNombreFicheroExpediente(int codOrg, String numExpediente, String codCampoCartaPago, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getNombreFicheroExpediente(codOrg, numExpediente, codCampoCartaPago, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD comprobando si existe el campo " + codCampoCartaPago, ex);
            throw new Exception(ex);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getCodigoSubvencionEika(String ejerSubvencion, String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return m92DAO.getCodigoSubvencionEika(ejerSubvencion, codProcedimiento, con);
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            adaptador.devolverConexion(con);
        }
    }

    public String getImportePendienteZORKU(int codOrg, String numExpediente, String idDeuda, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getImportePendienteZORKU(codOrg, numExpediente, idDeuda, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getGER_DE_IMPORTE_PENDIENTE ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public String getEstadoDeudaZORKU(String idDeuda, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getEstadoDeudaZORKU(idDeuda, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD getEstadoDeudaZORKU ", e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public List<ProcedimientoLireiVO> getTramitesProcedimientos(AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getTramitesProcedimientos(con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public List<String> getExpedientesEnEspera(int codOrg, ProcedimientoLireiVO procedimiento, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.getExpedientesEnEspera(codOrg, procedimiento, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean tieneCheckMarcado(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.tieneCheckMarcado(codOrg, numExp, codCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getDocumentoInteresado(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
            try {
            con = adapt.getConnection();
            return m92DAO.getDocumentoInteresado(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
            }
        }

    public boolean tieneMarcaTelematica(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide92DAO.getInstance().tieneMarcaTelematica(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepción en BBDD comprobando si tiene marca telemática ", e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public boolean deudaPagadaZORKU(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.deudaPagadaZORKU(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean deudaEnEjecutivaZORKU(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.deudaEnEjecutivaZORKU(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean tienePagoFraccionadoZORKU(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.tienePagoFraccionadoZORKU(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los trámites por procedimiento:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean deudaAnuladaZORKU(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.deudaAnuladaZORKU(codOrg, numExp, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD :  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean existeDesplegableTramite(int codOrg, String numExp, int codTramite, int ocurrencia, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.existeDesplegableTramite(codOrg, numExp, codTramite, ocurrencia, codCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD comprobando si tiene Tipo Notificacion.:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean existeFechaExp(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.existeFechaExp(codOrg, numExp, codCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD :  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean existeFechaTram(int codOrg, String numExp, int codTramResolucion, int ocuTramRes, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.existeFechaTram(codOrg, numExp, codTramResolucion, ocuTramRes, codCampo, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD :  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public boolean compruebaFecAcuseFecResol(int codOrg, String numExp, int codResolucion, int codAcuse, int ocuResolucion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        Date fecResolucion = null;
        Date fecAcuseRes = null;
        try {
            con = adapt.getConnection();
            fecResolucion = m92DAO.obtenerFechaTram(codOrg, numExp, codResolucion, ocuResolucion, ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
            fecAcuseRes = m92DAO.obtenerFechaTram(codOrg, numExp, codAcuse, ocuResolucion, ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACUSE_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD :  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
        return fecResolucion.before(fecAcuseRes) || fecResolucion.equals(fecAcuseRes);
    }

    public boolean compruebaFechaAcuseFechaLimPago(int codOrg, String numExp, int codAcuse, int ocuResolucion, AdaptadorSQLBD adapt) throws Exception {
        log.info("compruebaFechaAcuseFechaLimPago ( numExpediente = " + numExp + " ) : BEGIN");
        Connection con = null;
        Date fecAcuseRes = null;
        Date fecLimite = null;
        try {
            con = adapt.getConnection();
            fecAcuseRes = m92DAO.obtenerFechaTram(codOrg, numExp, codAcuse, ocuResolucion, ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACUSE_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
            fecLimite = m92DAO.obtenerFechaExp(codOrg, numExp, ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD :  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
        return fecAcuseRes.before(fecLimite);
    }

    public boolean compruebaFechaHoyFechaLimPago(int codOrg, String numExp, AdaptadorSQLBD adapt) throws Exception {
        // esHoyMayorQueFechaLimitePagoMas14Dias
        log.info("compruebaFechaHoyFechaLimPago ( numExpediente = " + numExp + " ) : BEGIN");
        Connection con = null;
        Date fecLimite = null;
        Calendar fechaLimite = null;
        Calendar hoy = Calendar.getInstance();
        try {
            con = adapt.getConnection();
            fecLimite = m92DAO.obtenerFechaExp(codOrg, numExp, ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
            fechaLimite = Calendar.getInstance();
            fechaLimite.setTime(fecLimite);
            fechaLimite.add(Calendar.DATE, 14);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD :  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
        return fechaLimite.before(hoy);
    }

    public int insertarRegistroProcesosProgramadosEjec(Date fechaJob, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m92DAO.insertarRegistroProcesosProgramadosEjec(fechaJob, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD insertando el proceso programado via ejecutiva:  " + e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexon a la BBDD: " + e.getMessage());
            }
        }
    }

    public void modificarRegistroProcesosProgramadosEjec(Integer idSecuencia, Date fechaFinJob, String resultadoJob, int registrosOK, int registrosKO, String mensajeErrorJob, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            m92DAO.modificarRegistroProcesosProgramadosEjec(idSecuencia, fechaFinJob, resultadoJob, registrosOK, registrosKO, mensajeErrorJob, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD insertando el proceso programado via ejecutiva:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }

    public void insertarRegistroProcProgViaEjeLIREI(Integer idSecuenciaProcesoProgramado, Date fecha, String numExp, String idDeuda, String detalleError, int error, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            m92DAO.insertarRegistroProcProgViaEjeLIREI(idSecuenciaProcesoProgramado, fecha, numExp, idDeuda, detalleError, error, con);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD insertando el proceso programado via ejecutiva:  " + e);
            throw new Exception(e);
        } finally {
                adapt.devolverConexion(con);
        }
    }
}
