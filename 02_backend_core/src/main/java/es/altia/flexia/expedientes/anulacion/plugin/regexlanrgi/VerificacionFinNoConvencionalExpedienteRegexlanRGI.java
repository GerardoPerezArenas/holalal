package es.altia.flexia.expedientes.anulacion.plugin.regexlanrgi;

import es.altia.flexia.expedientes.anulacion.exception.VerificacionFinNoConvencionalExpedienteException;
import es.altia.flexia.expedientes.anulacion.plugin.VerificacionFinNoConvencionalExpediente;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import net.lanbide.rgi.services.type.ResultadoVO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import rgi.lanbide.net.RGIServicesPortSoapBindingStub;
import rgi.lanbide.net.RGIServicesServiceLocator;

/**
 * Plugin de finalización no convencional de expediente integración REGEXLAN-RGI 
 */
public class VerificacionFinNoConvencionalExpedienteRegexlanRGI extends VerificacionFinNoConvencionalExpediente {
    private Logger log = LogManager.getLogger(VerificacionFinNoConvencionalExpedienteRegexlanRGI.class);
    private final String CONFIG_FILE_NAME = "VerificacionFinNoConvencionalRegexlanRGI";
    private final String PROPERTY_URL_WEBSERVICE= "/URL_WEBSERVICE_VERIFICACION";
    
    public boolean verificarFinalizacionNoConvencional(int codOrganizacion,String codProcedimiento,String numExpediente,int codUsuario,String loginUsuario) throws VerificacionFinNoConvencionalExpedienteException{
        boolean exito = false;
        
        log.debug(VerificacionFinNoConvencionalExpedienteRegexlanRGI.class.toString() + ".verificarFinalizacionNoConvencional codOrganizacion: " + codOrganizacion + ",codProcedimiento: " + codProcedimiento 
                + ",numExpediente: " + numExpediente + ", codUsuario: " + codUsuario + ", loginUsuario: " + loginUsuario);
                
        String url = null;                
        try {

            ResourceBundle config = ResourceBundle.getBundle(CONFIG_FILE_NAME);
            if(config!=null) log.debug("El fichero de configuración es !=null <> locale: " + config.getLocale().toString());
            url = config.getString(codOrganizacion + PROPERTY_URL_WEBSERVICE);

            RGIServicesPortSoapBindingStub binding = (RGIServicesPortSoapBindingStub)new RGIServicesServiceLocator().getRGIServicesPort(new URL(url));
            binding.setTimeout(9000);

            ResultadoVO resultado = binding.finalizarExpediente(numExpediente);
            if(resultado!=null){
                log.debug(VerificacionFinNoConvencionalExpedienteRegexlanRGI.class.toString() + "<> Resultado de la ejecución de la operación finalizarExpediente del WS de RGI - status: " + resultado.getStatus());
                log.debug(VerificacionFinNoConvencionalExpedienteRegexlanRGI.class.toString() + "<> Resultado de la ejecución de la operación finalizarExpediente del WS de RGI - mensajeError: " + resultado.getMensajeError());
                log.debug(VerificacionFinNoConvencionalExpedienteRegexlanRGI.class.toString() + "<> Resultado de la ejecución de la operación finalizarExpediente del WS de RGI - codigoError: " + resultado.getCodigoError());
                if(resultado.getStatus().equals(new Integer("0")))
                    exito = true; // Si devuelve un cero, la operación es correcta y por tanto se puede finalizar el expediente
            }

        } catch(MissingResourceException e){
            log.error("Error al recuperar una propiedad del fichero de configuración: " + e.getMessage());
            throw new VerificacionFinNoConvencionalExpedienteException("Error al recuperar una propiedad del fichero de configuración: " + e.getMessage());                

        } catch (javax.xml.rpc.ServiceException jre) {                
            log.error("Error durante la ejecución de la operación finalizarExpediente del servicio web de RGI: " + jre.getMessage());
            throw new VerificacionFinNoConvencionalExpedienteException("Error durante la ejecución de la operación finalizarExpediente del servicio web de RGI: " + jre.getMessage());

        }catch(java.net.MalformedURLException e){
            log.error("Error al formar la URL del servicio web de RGI para ejecutar la operación finalizarExpediente: " + e.getMessage());
            throw new VerificacionFinNoConvencionalExpedienteException("Error al formar la URL del servicio web de RGI para ejecutar la operación finalizarExpediente: " + e.getMessage());

        }catch(java.rmi.RemoteException e){
            log.error("Error durante la ejecución de la operación finalizarExpediente del servicio web de RGI: " + e.getMessage());
            throw new VerificacionFinNoConvencionalExpedienteException("Error durante la ejecución de la operación finalizarExpediente del servicio web de RGI: " + e.getMessage());

        }catch(Exception e){
            log.error("Error durante la ejecución de la operación finalizarExpediente del servicio web de RGI: " + e.getMessage());
            throw new VerificacionFinNoConvencionalExpedienteException("Error durante la ejecución de la operación finalizarExpediente del servicio web de RGI: " + e.getMessage());
        }
        
        return exito;        
    }
    
    
    
}
