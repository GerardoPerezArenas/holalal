package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43JobKontuDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.exception.GestionAutomaticaKONTUException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.KONTU_JobUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexiaWS.tramitacion.bd.datos.ExpedienteVO;
import es.altia.flexiaWS.tramitacion.bd.datos.InfoConexionVO;
import es.altia.flexiaWS.tramitacion.bd.datos.RespuestasTramitacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.util.*;


public class GestionAutomaticaKONTU implements Job {
    private Logger log = LogManager.getLogger(GestionAutomaticaKONTU.class);
    private static final String PROCEDIMIENTO = "KONTU";
    private static final int CODUSU = 5;
    private static final String LOGUSU = "ADMIN";

    // Gestion de errores a traves de lan6gestionerrores
    private ErrorBean errorB;
    private String prefIdenErr = "JOB_GESTAUTOKONTU_ERR_";
    private String prefErrMen = "Error al ejecutar el job GestionAutomaticaKONTU: ";
    private String idError;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();

            log.debug("jec.getRefireCount(): " + contador);

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            log.debug("servidor: " + servidor);
            log.debug("weblogic.Name: " + System.getProperty("weblogic.Name"));
            if(servidor.equals(System.getProperty("weblogic.Name")))  {//PARA LOCAL QUITAR

                synchronized (jec) {
                    Connection con = null;
                    AdaptadorSQLBD adaptador = null;
                    RegistroValueObject datosBusqueda = null;

                    MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                    MeLanbide43JobKontuDAO meLanbide43JobKontuDAO = MeLanbide43JobKontuDAO.getInstance();

                    try {
                        log.info("Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                        con = adaptador.getConnection();

                        log.debug("JOB LANZADO -> " + jec.getJobDetail().getName());

                        // 1. Buscar registros pendientes del procedimiento KONTU
                        datosBusqueda = new RegistroValueObject();
                        datosBusqueda.setTipoReg("E");
                        datosBusqueda.setIdentDepart(ConstantesDatos.REG_COD_DEP_DEFECTO);
                        datosBusqueda.setUnidadOrgan(0);
                        datosBusqueda.setEstAnotacion(0);
                        datosBusqueda.setCodProcedimiento(PROCEDIMIENTO);

                        List<RegistroValueObject> listaAnotaciones = meLanbide43JobKontuDAO.getAnotacionesPendientes(datosBusqueda, con);

                        if(listaAnotaciones.isEmpty()){
                            log.debug("No existen entradas pendientes del procedimiento " + PROCEDIMIENTO);
                            meLanbide43JobKontuDAO.insertarLineasLog(-1, -1L, "", "OK", "No se ha procesado ninguna anotación.", con);
                        }

                        for (RegistroValueObject registroVO : listaAnotaciones) {
                            try{
                                log.debug(String.format("Se va a procesar la anotación %d/%d", registroVO.getAnoReg(), registroVO.getNumReg()));

                                String numExpedienteAsociado = procesarAnotacion(registroVO, codOrg, con);
                                if(numExpedienteAsociado!=null){
                                    log.debug(String.format("Anotación %d/%d procesada correctamente", registroVO.getAnoReg(), registroVO.getNumReg()));
                                    meLanbide43JobKontuDAO.insertarLineasLog(registroVO.getAnoReg(), registroVO.getNumReg(), numExpedienteAsociado, "OK", null, con);
                                }
                            } catch(GestionAutomaticaKONTUException e){
                                String mensaje = String.format("%sHa ocurrido un error procesando la anotación %d/%d: %s",
                                        prefErrMen, registroVO.getAnoReg(), registroVO.getNumReg(), e.getDescripcionError());
                                log.error(mensaje, e);

                                errorB = new ErrorBean();
                                errorB.setSituacion("es.altia.flexia.integracion.moduloexterno.melanbide43.job.GestionAutomaticaKONTU.procesarAnotacion()");
                                if(e.getCodigo()==8){
                                    errorB.setIdError("FLEX_PLUG_DOK_GETDOC_REG");
                                } else {
                                    idError = "00" + e.getCodigo();
                                    errorB.setIdError(prefIdenErr + idError);
                                }
                                String numExpedienteErr = (e.getExpedienteDeError()!=null ? e.getExpedienteDeError() : "");
                                MeLanbide43Manager.grabarError(errorB, mensaje, e.toString(), numExpedienteErr);
                                meLanbide43JobKontuDAO.insertarLineasLog(registroVO.getAnoReg(), registroVO.getNumReg(), numExpedienteErr, "KO", mensaje, con);
                                //Este error ya ha sido tratado, para evitar que se vuelva a tratar en el finally del try-catch externo eliminamos el codigo de error
                                idError = null;
                            }

                        }
                    } catch (BDException e) {
                        String mensaje = String.format("%sNo se ha podido obtener una conexión de base de datos: %s", prefErrMen, e.getMessage());
                        log.error(mensaje, e);

                        errorB = new ErrorBean();
                        errorB.setCausa(e.toString());
                        idError = "001";
                    } catch (GestionAutomaticaKONTUException e) {
                        String mensaje = String.format("%s%s", prefErrMen, e.getDescripcionError());
                        log.error(mensaje, e);

                        errorB = new ErrorBean();
                        errorB.setCausa(e.toString());
                        idError = "001";
                    } catch (Exception e) {
                        String mensaje = String.format("%s%s", prefErrMen, e.getMessage());
                        log.error(mensaje, e);

                        errorB = new ErrorBean();
                        errorB.setCausa(e.toString());
                        idError = "001";
                    } finally {
                        if(idError != null) {
                            String mensaje = "Ha ocurrido un error al inicio de la ejecución del Job: no se ha podido procesar ninguna anotación.";
                            //Hay un error que grabar en base de datos y que informar por email
                            errorB.setSituacion("es.altia.flexia.integracion.moduloexterno.melanbide43.job.GestionAutomaticaKONTU.execute()");
                            errorB.setIdError(prefIdenErr+idError);
                            MeLanbide43Manager.grabarError(errorB, mensaje, errorB.getCausa(), "");
                            meLanbide43JobKontuDAO.insertarLineasLog(-1, -1L,"", "KO", mensaje, con);
                        }

                        if (con != null) {
                            con.close();
                        }
                    }
                }
            }//para local quitar
        } catch (Exception ex) {
            log.error("No se ha podido ejecutar el job " + jec.getJobDetail().getName()+ ". Error: " + ex, ex);
        }
    }

    /**
     * Realiza todas las operaciones de la gestión automática requeridas: inicia expediente, asocia expediente y anotación y tramita el expediente
     * @param registroVO
     * @param codOrg
     * @param con
     * @return Es num del expediente asociado si el proceso completo se ha ejecutado correctamente, o null en caso contrario
     * @throws GestionAutomaticaKONTUException
     * @throws AlmacenDocumentoTramitacionException
     */
    private String procesarAnotacion(RegistroValueObject registroVO, int codOrg, Connection con) throws GestionAutomaticaKONTUException, AlmacenDocumentoTramitacionException {
        String numExpedienteAsociado = null;
        KONTU_JobUtils jobUtils = new KONTU_JobUtils();

        ExpedienteVO expedienteVO = jobUtils.crearExpedienteVO(CODUSU, String.valueOf(codOrg), registroVO, con);
        InfoConexionVO infoConexionVO = jobUtils.crearInfoConexionVO(codOrg);

        //2. Crear el nuevo expediente KONTU (en REGEXLAN, a traves de WSTramitacionFlexia, y en mi carpeta)
        RespuestasTramitacionVO respuestaTramitacionVO = jobUtils.iniciarExpediente(LOGUSU, expedienteVO, infoConexionVO, registroVO.getListaDocsAsignados(), con);
        if (respuestaTramitacionVO!=null && respuestaTramitacionVO.getSalida().getCodigoError().equals(0)) {
            //3. Asociar el registro al nuevo expediente (a traves de WSTramitacion)
            boolean exitoAsociar = jobUtils.asociarExpedienteRegistro(expedienteVO, registroVO, infoConexionVO);

            // El siguiente paso depende del exito de este, si no se socia entonces se notifica el error
            if (exitoAsociar) {
                //4. Realizar la tramitacion del expediente
                boolean exitoAvanzar = jobUtils.tramitarExpediente(expedienteVO, infoConexionVO, con);
                if (!exitoAvanzar) {
                    String mensaje = String.format("No se ha podido realizar la tramitación del expediente %s correctamente.", expedienteVO.getNumero());
                    log.debug(mensaje);
                    throw new GestionAutomaticaKONTUException(8, mensaje, expedienteVO.getNumero());
                } else {
                    numExpedienteAsociado = expedienteVO.getNumero();
                }
            } else {
                log.debug("No se ha podido realizar la asociación entre expediente y anotación");
                throw new GestionAutomaticaKONTUException(7,"No se ha podido realizar la asociación entre expediente y anotación", expedienteVO.getNumero());
            }
        } else{
            String mensaje = String.format("Ha ocurrido un error en iniciarExpediente(): ", expedienteVO.getNumero());
            log.debug(mensaje);
            throw new GestionAutomaticaKONTUException(5, mensaje, expedienteVO.getNumero());
        }
        return numExpedienteAsociado;
    }

}
