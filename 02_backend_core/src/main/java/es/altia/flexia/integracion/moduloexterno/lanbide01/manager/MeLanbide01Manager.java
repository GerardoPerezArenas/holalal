package es.altia.flexia.integracion.moduloexterno.lanbide01.manager;
import es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.exception.MeLanbide01Exception;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.MeLanbide01DatosCalculoDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.MeLanbidePeriodosDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.Melanbide01Dao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.Melanbide01DecretoDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.Melanbide01DepenPerSutDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.Melanbide01HistoSubvDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConnectionUtils;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01Constantes;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosCalculoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01DepenPerSut;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01HistoSubv;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 26/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 26-10-2012 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide01Manager {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide01Manager.class);
    
    //Instancia
    private static MeLanbide01Manager instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide01Manager, si no existe la crea.
     * @return 
     */
    public static MeLanbide01Manager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide01Manager.class){
                instance = new MeLanbide01Manager();
            }//synchronized(MeLanbide01Manager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    private static Melanbide01HistoSubvDao melanbide01HistoSubvDao = new Melanbide01HistoSubvDao();
    private static Melanbide01DepenPerSutDao melanbide01DepenPerSutDao = new Melanbide01DepenPerSutDao();
    private static Melanbide01DecretoDao melanbide01DecretoDao = new Melanbide01DecretoDao();
    private static Melanbide01Dao melanbide01Dao = new Melanbide01Dao();
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public void insertarDatosCalculo (DatosCalculoVO datosCalculo, String nombreCampoSuplementarioFinContrato, 
           String nombreCampoSuplementarioTotalSubvencionado, String nombreCampoSuplementarioInicioContrato, 
           AdaptadorSQLBD adaptador) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("insertarDatosCalculo() : BEGIN");
        Connection con = null;
        try{
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            con = adaptador.getConnection();
            con.setAutoCommit(false);
            
            MeLanbide01DatosCalculoDao datosCalculoDao = MeLanbide01DatosCalculoDao.getInstance();
            Boolean existenCalculos = false;
            if(log.isDebugEnabled()) log.debug("Comprobamos si existen previamente datos");
            existenCalculos = datosCalculoDao.existenDatosCalculo(datosCalculo.getNumExpediente(), datosCalculo.getCodMunicipio(), con);
            if(existenCalculos){
                if(log.isDebugEnabled()) log.debug("Existen calculos asi que los borramos previamente");
                datosCalculoDao.borrarDatosCalculo(datosCalculo.getNumExpediente(), datosCalculo.getCodMunicipio(), con);
            }//if(existenCalculos)
            
            if(log.isDebugEnabled()) log.debug("Creamos un registro con los datos");
            datosCalculoDao.insertarDatosCalculo(datosCalculo, con);
            
            //Si todo sale correctamente cogemos la fecha de inicio del primer periodo y actualizamos la fecha de inicio de contrato con ella
            //para mantener una coherencia con los datos de los periodos.
            CampoSuplementarioModuloIntegracionVO campoSuplementarioInicioContrato = new CampoSuplementarioModuloIntegracionVO();
            campoSuplementarioInicioContrato.setCodOrganizacion(datosCalculo.getCodMunicipio());
                campoSuplementarioInicioContrato.setCodProcedimiento(datosCalculo.getCodProcedimiento());
                campoSuplementarioInicioContrato.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                campoSuplementarioInicioContrato.setTramite(false);
                campoSuplementarioInicioContrato.setNumExpediente(datosCalculo.getNumExpediente());
                campoSuplementarioInicioContrato.setEjercicio(datosCalculo.getEjercicio());
                campoSuplementarioInicioContrato.setCodigoCampo(nombreCampoSuplementarioInicioContrato);
                Calendar fechaInicio = datosCalculo.getPeriodos().get(0).getFechaInicio();
                campoSuplementarioInicioContrato.setValorFecha(fechaInicio);
            el.grabarCampoSuplementario(campoSuplementarioInicioContrato);
            
            //Si todo sale correctamente cogemos la fecha de fin del ultimo periodo y actualizamos la fecha de fin de contrato con ella
            //para mantener una coherencia con los datos de los periodos.
            CampoSuplementarioModuloIntegracionVO campoSuplementarioFinContrato = new CampoSuplementarioModuloIntegracionVO();
                campoSuplementarioFinContrato.setCodOrganizacion(datosCalculo.getCodMunicipio());
                campoSuplementarioFinContrato.setCodProcedimiento(datosCalculo.getCodProcedimiento());
                campoSuplementarioFinContrato.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                campoSuplementarioFinContrato.setTramite(false);
                campoSuplementarioFinContrato.setNumExpediente(datosCalculo.getNumExpediente());
                campoSuplementarioFinContrato.setEjercicio(datosCalculo.getEjercicio());
                campoSuplementarioFinContrato.setCodigoCampo(nombreCampoSuplementarioFinContrato);
                Calendar fechaFinal = datosCalculo.getPeriodos().get(datosCalculo.getPeriodos().size()-1).getFechaFin();
                campoSuplementarioFinContrato.setValorFecha(fechaFinal);
            el.grabarCampoSuplementario(campoSuplementarioFinContrato);
            
            //Actualizamos el campo suplementario con el total subvencionado
            CampoSuplementarioModuloIntegracionVO campoSuplementarioTotalSubvencion = new CampoSuplementarioModuloIntegracionVO();
            campoSuplementarioTotalSubvencion.setCodOrganizacion(datosCalculo.getCodMunicipio());
            campoSuplementarioTotalSubvencion.setCodProcedimiento(datosCalculo.getCodProcedimiento());
            campoSuplementarioTotalSubvencion.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            campoSuplementarioTotalSubvencion.setTramite(false);
            campoSuplementarioTotalSubvencion.setNumExpediente(datosCalculo.getNumExpediente());
            campoSuplementarioTotalSubvencion.setEjercicio(datosCalculo.getEjercicio());
            campoSuplementarioTotalSubvencion.setCodigoCampo(nombreCampoSuplementarioTotalSubvencionado);
            //campoSuplementarioTotalSubvencion.setValorNumero(datosCalculo.getImporteSubvencionado());
            campoSuplementarioTotalSubvencion.setValorNumero(Double.toString(datosCalculo.getTotalConDescuento()));
            el.grabarCampoSuplementario(campoSuplementarioTotalSubvencion);
                
            con.commit();
        }catch(BDException e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del calculo en el expediente " + datosCalculo.getNumExpediente(), e);            
            throw new MeLanbide01Exception("Se ha producido una excepción en la BBDD insertando los datos del calculo en el expediente " + datosCalculo.getNumExpediente(), e);
        }catch(SQLException ex){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback al insertar/actualizar el certificado: " + f.getMessage());
            }
            log.error("Se ha producido una excepción insertando los datos del calculo en el expediente =  " + datosCalculo.getNumExpediente(),ex);            
            throw new MeLanbide01Exception("Se ha producido una excepción insertando los datos del calculo en el expediente =  " + datosCalculo.getNumExpediente(),ex);
        }catch(Exception e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del calculo en el expediente " + datosCalculo.getNumExpediente(), e);            
            throw new MeLanbide01Exception("Se ha producido una excepción en la BBDD insertando los datos del calculo en el expediente " + datosCalculo.getNumExpediente(), e);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarDatosCalculo() : END");
    }//insertarDatosCalculo
    
    public DatosCalculoVO getDatosCalculo (String numExpediente, String codOrganizacion, AdaptadorSQLBD adaptador) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("getDatosCalculo() : BEGIN");
        Connection con = null;
        DatosCalculoVO datosCalculo = new DatosCalculoVO();
        try{
            con = adaptador.getConnection();
            //MeLanbide01DatosCalculoDao datosCalculoDao = MeLanbide01DatosCalculoDao..getInstance();
            MeLanbide01DatosCalculoDao datosCalculoDAO = MeLanbide01DatosCalculoDao.getInstance();
            MeLanbidePeriodosDao periodosDao = MeLanbidePeriodosDao.getInstance();
            datosCalculo = datosCalculoDAO.getDatosCalculo(numExpediente, codOrganizacion, con);
            
            //Si el numero de expediente de datos calculo es distinto de null, recuperamos los periodos
            if(datosCalculo.getNumExpediente() != null && !datosCalculo.getNumExpediente().equalsIgnoreCase("")){
                datosCalculo.setPeriodos(periodosDao.getPeriodos(datosCalculo.getId(), codOrganizacion, con));
            }//if(datosCalculo.getNumExpediente() != null && !datosCalculo.getNumExpediente().equalsIgnoreCase(""))
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los datos del cálculo ", e);
            throw new MeLanbide01Exception("Se ha producido una excepción en la BBDD recuperando los datos del cálculo ",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los datos del cálculo ",ex);
            throw new MeLanbide01Exception("Se ha producido una excepción recuperando los datos del cálculo ",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDatosCalculo() : END");
        return datosCalculo;
    }//getDatosCalculo
    
        /**
     * Metodo que recupera el numero total de dias de los periodos de un expediente
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return Integer
     * @throws MeLanbide01Exception 
     */
    public Integer numDiasTotalExpediente (String numExpediente, String codOrganizacion, AdaptadorSQLBD adaptador) throws MeLanbide01Exception{
        if(log.isDebugEnabled()) log.debug("numDiasTotalExpediente() : BEGIN");
        Connection con = null;
        Integer numTotalDiasExpediente = new Integer(0);
        try{
            con = adaptador.getConnection();
            MeLanbide01DatosCalculoDao datosCalculoDao = MeLanbide01DatosCalculoDao.getInstance();
            numTotalDiasExpediente = datosCalculoDao.numDiasTotalExpediente(numExpediente, codOrganizacion, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el numero total de dias del expediente ", e);
            throw new MeLanbide01Exception("Se ha producido una excepción en la BBDD recuperando el numero total de dias del expediente ",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el numero total de dias del expediente ",ex);
            throw new MeLanbide01Exception("Se ha producido una excepción recuperando el numero total de dias del expediente ",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("numDiasTotalExpediente() : END");
        return numTotalDiasExpediente;
    }//numDiasTotalExpediente

    public void insertarDatosCampoSuplementarioNroTotalDiasConcedidos(int codOrganizacion, Integer ejercicio, String numExpediente, String numTotalDiasConcedidos, AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.error("insertarDatosCampoSuplementarioNroTotalDiasConcedidos() - Manager - : BEGIN " + numExpediente);
        Connection con = null;
        Integer numTotalDias = new Integer(0);
        numTotalDias = numTotalDiasConcedidos != null && !numTotalDiasConcedidos.isEmpty() ? Integer.valueOf(numTotalDiasConcedidos):0;
        try {
            con = adaptador.getConnection();
            MeLanbide01DatosCalculoDao datosCalculoDao = MeLanbide01DatosCalculoDao.getInstance();
            String CodigoCampoSuplementario = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.CODIGO_CAMPOSUPLEMENTARIO_NUMEROTOTALDIASCONCEDEDIDOS,
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            boolean existeCampo = datosCalculoDao.getExisteCampoSuplemenarioNumeroDiasConcedidos(codOrganizacion, ejercicio, numExpediente,CodigoCampoSuplementario,con);
            // volvemos a obtener la conexio pq la hemos cerrado al comprobar si existes datos del campo suplementario
            con=adaptador.getConnection();
            datosCalculoDao.insertarDatosCampoSuplementarioNroTotalDiasConcedidos(codOrganizacion, ejercicio, numExpediente,numTotalDias,CodigoCampoSuplementario,existeCampo,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al insertar el valor del campo suplementarios Total dias concedidos - al guardar datos calculo ", e);
            throw new MeLanbide01Exception("Se ha producido una excepción en la BBDD al insertar el valor del campo suplementarios Total dias concedidos - al guardar datos calculo ", e);
        } catch (SQLException e) {
            log.error("Se ha producido una SQLException insertando el valor del campo suplementario numero de dias concedidos al guardar datos del cálculo", e);
            throw new MeLanbide01Exception("Se ha producido una SQLException insertando el valor del campo suplementario numero de dias concedidos al guardar datos del cálculo", e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al insertar el valor del campo suplementarios Total dias concedidos - al guardar datos calculo ", ex);
            throw new MeLanbide01Exception("Se ha producido una excepción general al insertar el valor del campo suplementarios Total dias concedidos - al guardar datos calculo ", ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: insertarDatosCampoSuplementarioNroTotalDiasConcedidos - " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.error("insertarDatosCampoSuplementarioNroTotalDiasConcedidos() - Manager - : END " + numExpediente);
    }
    
    public List<Melanbide01HistoSubv> getTodoHitorialSubvencionExpediente(String numExpediente,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getTodoHitorialSubvencionExpediente() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01HistoSubvDao.getTodoHitorialSubvencionExpediente(numExpediente, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos Historial Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos del Historial de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getTodoHitorialSubvencionExpediente() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getTodoHitorialSubvencionExpediente - " + e.getMessage());
            }
        }
    }
    
    public Melanbide01HistoSubv getLineaHistorialSubvencionById(Long id,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getLineaHistorialSubvencionById() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + id );
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01HistoSubvDao.getLineaHistorialSubvencionById(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos Historial Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos del Historial de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getLineaHistorialSubvencionById() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getLineaHistorialSubvencionById - " + e.getMessage());
            }
        }
    }
    
    public void agregarNuevaLineaHistorialSubvencion(Melanbide01HistoSubv dato,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("agregarNuevaLineaHistorialSubvencion() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            melanbide01HistoSubvDao.agregarNuevaLineaHistorialSubvencion(dato, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al crear datos Historial Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al crear Datos del Historial de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("agregarNuevaLineaHistorialSubvencion() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: agregarNuevaLineaHistorialSubvencion - " + e.getMessage());
            }
        }
    }
    
    public void actualizarDatosLineaHistorialSubvencion(Melanbide01HistoSubv dato,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("actualizarDatosLineaHistorialSubvencion() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            melanbide01HistoSubvDao.actualizarDatosLineaHistorialSubvencion(dato, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al actualizar datos Historial Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al actualizar Datos del Historial de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("actualizarDatosLineaHistorialSubvencion() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: actualizarDatosLineaHistorialSubvencion - " + e.getMessage());
            }
        }
    }
    
    public void eliminarFilaHistorialSubvencion(Long dato,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("eliminarFilaHistorialSubvencion() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            melanbide01HistoSubvDao.eliminarFilaHistorialSubvencion(dato, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al eliminar datos Historial Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al eliminar Datos del Historial de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("eliminarFilaHistorialSubvencion() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: eliminarFilaHistorialSubvencion - " + e.getMessage());
            }
        }
    }
    
    public List<Melanbide01DepenPerSut> getTodosCausantesSubvencion(String numExpediente,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getTodosCausantesSubvencion() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + numExpediente );
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01DepenPerSutDao.getTodosCausantesSubvencion(numExpediente, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos causantes Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos del causantes de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getTodosCausantesSubvencion() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getTodosCausantesSubvencion - " + e.getMessage());
            }
        }
    }
    
    public Melanbide01DepenPerSut getCausantesSubvencionById(Long id,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getCausantesSubvencionById() - Manager - : BEGIN "+ formatFechaLog.format(new Date()) + id );
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01DepenPerSutDao.getCausantesSubvencionById(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos causantes Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos del causantes de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getCausantesSubvencionById() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getCausantesSubvencionById - " + e.getMessage());
            }
        }
    }
    
    public void agregarNuevoCausante(Melanbide01DepenPerSut dato,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("agregarNuevoCausante() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            melanbide01DepenPerSutDao.agregarNuevoCausante(dato, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al crear datos causante Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al crear Datos del causante de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("agregarNuevoCausante() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: agregarNuevoCausante - " + e.getMessage());
            }
        }
    }
    
    public void actualizarDatosCausante(Melanbide01DepenPerSut dato,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("actualizarDatosCausante() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            melanbide01DepenPerSutDao.actualizarDatosCausante(dato, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al actualizar datos causante Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al actualizar Datos del causante de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("actualizarDatosCausante() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: actualizarDatosCausante - " + e.getMessage());
            }
        }
    }
    
    public void eliminarCausante(Long dato,AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("eliminarCausante() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            melanbide01DepenPerSutDao.eliminarCausante(dato, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al eliminar datos causante Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al eliminar Datos del causante de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("eliminarCausante() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: eliminarCausante - " + e.getMessage());
            }
        }
    }
    
    public ArrayList<TipoDocumentoVO> getTiposDocumento(AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("eliminarCausante() - Manager - : BEGIN "+ formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01Dao.getTiposDocumento(con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al eliminar datos causante Snvencion " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al eliminar Datos del causante de Subvencion. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("eliminarCausante() - Manager - End  "+ formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: eliminarCausante - " + e.getMessage());
            }
        }
    }
    
    public List<Melanbide01Decreto> getDecretoTodos(AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getDecretoTodos() - Manager - : BEGIN " + formatFechaLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01DecretoDao.getDecretoTodos(con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos Decretos " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos de Decretos Aplicables a CONCM " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDecretoTodos() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDecretoTodos - " + e.getMessage());
            }
        }
    }
    
    public Melanbide01Decreto getDecretoById(int id, AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getDecretoById() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " - " + id);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01DecretoDao.getDecretoById(id, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos del DECRETO " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos de DECRETO." + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDecretoById() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDecretoById - " + e.getMessage());
            }
        }
    }
    
    public Melanbide01Decreto getDecretoAplicableExpediente(Date fechaReferenciaExpediente, AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getDecretoAplicableExpediente() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + fechaReferenciaExpediente);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01DecretoDao.getDecretoAplicableExpediente(fechaReferenciaExpediente, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos del DECRETO " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos de DECRETO. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDecretoAplicableExpediente() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDecretoAplicableExpediente - " + e.getMessage());
            }
        }
    }
    
    /**
     * Devuelve una lista de los expedientes que De una misma Empresa y una misma persona contratada pero distinta Persona sutituida
     * Para determinar si una persona esta sustiyendo a mas de una persona en la misma empresa
     * @param numeroExpediente
     * @param datosDocuRolesExpte
     * @param adaptador
     * @return
     * @throws MeLanbide01Exception 
     */
    public List<String> getExptsMismaEmpresaPerContratadaDifePerSust(String numeroExpediente,Map<String,String> datosDocuRolesExpte, AdaptadorSQLBD adaptador) throws MeLanbide01Exception, Exception {
        log.info("getExptsMismaEmpresaPerContratadaDifePerSust() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + numeroExpediente);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01Dao.getExptsMismaEmpresaPerContratadaDifePerSust(numeroExpediente,datosDocuRolesExpte, con);
        } catch (SQLException e) {
            log.error("SQLException  getExptsMismaEmpresaPerContratadaDifePerSust " + e.getErrorCode() + " - " + e.getSQLState() + " - " +e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getExptsMismaEmpresaPerContratadaDifePerSust " + e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al getExptsMismaEmpresaPerContratadaDifePerSust " + ex.getMessage(), ex);
            throw  ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getExptsMismaEmpresaPerContratadaDifePerSust() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getExptsMismaEmpresaPerContratadaDifePerSust - " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene un mapa con los datos de el documento y roles de un expediente
     * @param numeroExpediente
     * @param adaptador
     * @return  : Mapa: <CodigoRol,Documento>
     * @throws SQLException
     * @throws Exception 
     */
    public Map<String,String> getDatosDocumentoRolExpediente(String numeroExpediente,AdaptadorSQLBD adaptador) throws SQLException, Exception {
        log.info("getDatosDocumentoRolExpediente() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + numeroExpediente);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01Dao.getDatosDocumentoRolExpediente(numeroExpediente,con);
        } catch (SQLException e) {
            log.error("SQLException  getDatosDocumentoRolExpediente " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getDatosDocumentoRolExpediente " + e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al getDatosDocumentoRolExpediente " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDatosDocumentoRolExpediente() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDatosDocumentoRolExpediente - " + e.getMessage());
            }
        }
    }    
    
    public List<Map<String,String>> getExpedientesRelacionados(String numeroExpediente, AdaptadorSQLBD adaptador) throws MeLanbide01Exception, Exception {
        log.info("getExpedientesRelacionados() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + numeroExpediente);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01Dao.getExpedientesRelacionados(numeroExpediente, con);
        } catch (SQLException e) {
            log.error("SQLException  getExpedientesRelacionados " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getExpedientesRelacionados " + e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al getExpedientesRelacionados " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getExptsMismaEmpresaPerContratadaDifePerSust() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getExptsMismaEmpresaPerContratadaDifePerSust - " + e.getMessage());
            }
        }
    }

    public Map<String, Integer> getDatosCalculoNumMaxDias_Dec164_2019(Integer codigoOrganizacion,String codProcedimiento, String numeroExpediente, String codigoRolPSustituida, String codigoRolPContratada) throws SQLException, Exception {
        AdaptadorSQLBD adaptador=null;
        Connection con = null;
        try {
            adaptador=ConnectionUtils.getAdaptSQLBD(String.valueOf(codigoOrganizacion));
            con=adaptador.getConnection();
            return melanbide01Dao.getDatosCalculoNumMaxDias_Dec164_2019(codigoOrganizacion,codProcedimiento,numeroExpediente,codigoRolPSustituida,codigoRolPContratada, con);
        } catch (SQLException e) {
            log.error("SQLException  getDatosCalculoNumMaxDias_Dec164_2019 " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getDatosCalculoNumMaxDias_Dec164_2019 " + e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al getDatosCalculoNumMaxDias_Dec164_2019 " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDatosCalculoNumMaxDias_Dec164_2019() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDatosCalculoNumMaxDias_Dec164_2019 - " + e.getMessage());
            }
        }
    }

    public Melanbide01Decreto getDecretoByCodigoDecreto(String decretoCodigo, AdaptadorSQLBD adaptador) throws MeLanbide01Exception {
        log.info("getDecretoByCodigoDecreto() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " - " + decretoCodigo);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return melanbide01DecretoDao.getDecretoByCodigoDecreto(decretoCodigo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos del DECRETO " + ex.getMessage(), ex);
            throw new MeLanbide01Exception("Error al leer Datos de DECRETO." + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDecretoByCodigoDecreto() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDecretoByCodigoDecreto - " + e.getMessage());
            }
        }
    }
    
    public double getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios(Integer codigoOrganizacion, String codProcedimiento, String numeroExpediente) throws SQLException, Exception {
        AdaptadorSQLBD adaptador = null;
        Connection con = null;
        try {
            adaptador = ConnectionUtils.getAdaptSQLBD(String.valueOf(codigoOrganizacion));
            con = adaptador.getConnection();
            return melanbide01Dao.getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios(codigoOrganizacion, codProcedimiento, numeroExpediente,con);
        } catch (SQLException e) {
            log.error("SQLException  getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios " + e.getErrorCode() + " - " + e.getSQLState() + " - " + e.getMessage(), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("SQLException  getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios " + e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios - " + e.getMessage());
            }
        }
    }
            
}//MeLanbide01Manager