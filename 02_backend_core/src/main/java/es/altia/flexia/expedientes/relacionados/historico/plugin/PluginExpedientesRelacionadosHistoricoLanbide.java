/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.expedientes.relacionados.historico.plugin;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.historico.util.ResultadoPluginExpRelHist;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesAsociadosVO;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import net.lanbide.rgi.services.type.ResultadoVO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import rgi.lanbide.net.RGIServices;
import rgi.lanbide.net.RGIServicesServiceLocator;

/**
 *
 * @author david.vidal
 */
public class PluginExpedientesRelacionadosHistoricoLanbide extends PluginExpedientesRelacionadosHistorico {
    
    //Logger
    private static Logger log = LogManager.getLogger(PluginExpedientesRelacionadosHistoricoLanbide.class);
    
    private String convertCalendarToString(Calendar fecha){
        String salida = null;
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
        salida = sf.format(fecha.getTime());
        return salida;
    }//convertCalendarToString
    
    @Override
    public ResultadoPluginExpRelHist recuperarPermisosDeshacerRelacion(ExpedientesRelacionadosHistoricoVO registro) {      
        if(log.isDebugEnabled()) log.debug("recuperarPermisosDeshacerRelacion() : BEGIN");
        String salida = null;
        RGIServicesServiceLocator locator = null;        
        ResourceBundle config = ResourceBundle.getBundle("RGILanbide");        
        String url = config.getString("LANBIDE/URL");
        ResultadoPluginExpRelHist devolucionServicio = new ResultadoPluginExpRelHist();
        int codIdioma = registro.getCodigoIdiomaUsuario();
        
        try{
            URL oUrl = new URL(url);
            locator = new RGIServicesServiceLocator();
            RGIServices servicio = locator.getRGIServicesPort(oUrl);
            String numRegistro = registro.getNum_entrada();
            String ejercicio = registro.getEjercicio();
            Calendar cFechaDesasociacion = registro.getFecha_desasociada();
            Calendar cFechaPresentacion  = registro.getFecha_presentacion();
            String expedienteRelacionado = registro.getExpediente();
            String login = registro.getUsuario();
            
            log.debug("recuperarPermisosDeshacerRelacion: Invocando servicio: "+url);
            log.debug(" ==========> Código de idioma del usuario: " + codIdioma);            
            log.debug(" ==========> Parametros a pasar a la operación desasociarEntradaRegistro de RGI <====================");
            log.debug(" ==========> Param 1 numRegistro: " + numRegistro);
            log.debug(" ==========> Param 2 ejercicio: " + ejercicio);
            log.debug(" ==========> Param 3 fechaDesasociacion: " + convertCalendarToString(cFechaDesasociacion));
            log.debug(" ==========> Param 4 cFechaPresentacion: " + convertCalendarToString(cFechaPresentacion));
            log.debug(" ==========> Param 5 expedienteRelacionado: " + expedienteRelacionado);
            log.debug(" ==========> Param 6 login: " + login);
            
            ResultadoVO salidaServicio = servicio.desasociarEntradaRegistro(numRegistro, ejercicio,convertCalendarToString(cFechaDesasociacion), convertCalendarToString(cFechaPresentacion), expedienteRelacionado, login);
            log.debug("SalidaServicio codigoError: " + salidaServicio.getCodigoError());
            log.debug("SalidaServicio mensajeError: " + salidaServicio.getMensajeError());            
            log.debug("SalidaServicio status: " + salidaServicio.getStatus());
                         
            Integer status = salidaServicio.getStatus();
            
            if(status.equals(0)){
                devolucionServicio.setCodigoError(salidaServicio.getCodigoError());                
                devolucionServicio.setMensajeError(null);
                devolucionServicio.setStatus(salidaServicio.getStatus());
            }else if(status.equals(1)){
                if(salidaServicio.getCodigoError()!=null && "0".equals(salidaServicio.getCodigoError())){
                    devolucionServicio.setMensajeError(config.getString("ERR_PARAMETROS_INCORRECTOS_" + codIdioma));                    
                }else if(salidaServicio.getCodigoError()!=null && "1".equals(salidaServicio.getCodigoError())){
                    devolucionServicio.setMensajeError(config.getString("ERR_ACCESO_BASEDATOS_" + codIdioma));                    
                }else if(salidaServicio.getCodigoError()!=null && "2".equals(salidaServicio.getCodigoError())){
                    devolucionServicio.setMensajeError(config.getString("ERR_EXPEDIENTE_REINTEGRO_" + codIdioma));                    
                }//
                devolucionServicio.setCodigoError(salidaServicio.getCodigoError());                
                devolucionServicio.setStatus(salidaServicio.getStatus());                
            }//if status                             
        }catch(java.rmi.RemoteException e){
            log.error("Error al conectarse al servicio web: "+url+":"+e.getMessage());
            e.printStackTrace();
            // Error al conectarse al servicio web
            devolucionServicio.setCodigoError("3");                
            devolucionServicio.setStatus(1);                
            devolucionServicio.setMensajeError(config.getString("ERR_CONEXION_WS_" + codIdioma));
        }catch(javax.xml.rpc.ServiceException e){
            log.error("Error al conectarse al servicio web: "+url+":"+e.getMessage());
            e.printStackTrace();
            // Error al conectarse al servicio web
            devolucionServicio.setCodigoError("3");                
            devolucionServicio.setStatus(1);                
            devolucionServicio.setMensajeError(config.getString("ERR_CONEXION_WS_" + codIdioma));
        }catch(java.net.MalformedURLException e){
            log.error("La URL de acceso al servicio web está mal formada: "+url+":"+e.getMessage());
            e.printStackTrace();
            // Error al conectarse al servicio web
            devolucionServicio.setCodigoError("4");                
            devolucionServicio.setStatus(1);                
            devolucionServicio.setMensajeError(config.getString("ERR_CONEXION_WS_" + codIdioma));
        }//try-catch
        if(log.isDebugEnabled()) log.debug("recuperarPermisosDeshacerRelacion() : END");
        return devolucionServicio;
    }//recuperarPermisosDeshacerRelacion  
    
}//class
