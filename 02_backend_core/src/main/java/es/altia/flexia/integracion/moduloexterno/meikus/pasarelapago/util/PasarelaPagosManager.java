package es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.util;

import es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.IPasarelaPago;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * @author david.caamano
 * @version 03/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 03/12/2012 * Edición inicial</li>
 * </ol> 
 */
public class PasarelaPagosManager {
    
    //Logger
    private final static Logger log = LogManager.getLogger(PasarelaPagosManager.class);
    
    //Propiedad del fichero del cual recuperaremos la ruta a la clase que queremos implementar para la pasarela de pagos
    private static String IMPL_CLASS = "PASARELA_PAGOS/IMPLCLASS";
    private static String CODS_ERROR = "CODS_ERROR";
    
    /**
     * 
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     * @throws MeIkus01Exception 
     */
    public static IPasarelaPago getPasarelaPagos(Integer codOrganizacion, String codProcedimiento) throws MeIkus01Exception{
        if(log.isDebugEnabled()) log.debug("getPasarelaPagos() : BEGIN");
        IPasarelaPago pasarela = null;
        if(log.isDebugEnabled()) log.debug("Instanciamos la pasarela de pagos para la organizacion = " + codOrganizacion + " y el procedimiento"
                + " = " + codProcedimiento);
        String clase = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + 
                MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + IMPL_CLASS
                , MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        if(log.isDebugEnabled()) log.debug("Clase a implementar = " + clase);
        try {
            pasarela = (IPasarelaPago) Class.forName(clase).newInstance();
            if(pasarela != null){
                if(log.isDebugEnabled()) log.debug("Procedemos a cargar las propiedades de la pasarela");
                cargarCodigosError(pasarela, codOrganizacion, codProcedimiento);
            }//if(pasarela != null)
        } catch (ClassNotFoundException ex) {
            log.error("Se ha producido un error instanciando la clase de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error instanciando la clase de la pasarela de pagos ", ex);
        } catch (InstantiationException ex) {
            log.error("Se ha producido un error instanciando la clase de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error instanciando la clase de la pasarela de pagos ", ex);
        } catch (IllegalAccessException ex) {
            log.error("Se ha producido un error instanciando la clase de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error instanciando la clase de la pasarela de pagos ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getPasarelaPagos() : END");
        return pasarela;
    }//getPasarelaPagos
    
    private static void cargarCodigosError (IPasarelaPago pasarela, Integer codOrganizacion, String codProcedimiento) throws MeIkus01Exception{
        if(log.isDebugEnabled()) log.debug("cargarCodigosError() : BEGIN");
        ArrayList<String> listaCodigosError = new ArrayList<String>();
        String codigosError = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + 
                MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PASARELA_PAGOS + 
                MeIkus01Constantes.BARRA + CODS_ERROR
                , MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        
        if(codigosError != null && !"".equalsIgnoreCase(codigosError)){
            if(log.isDebugEnabled()) log.debug("Separamos los codigos de error y los metemos en un array");
            String[] listaCodigos = codigosError.split(";");
            for(int i=0; i<listaCodigos.length; i++){
                listaCodigosError.add(listaCodigos[i]);
            }//for(int i=0; i<listaCodigos.length; i++)
            if(log.isDebugEnabled()) log.debug("Anhadimos los codigos de error a la implementacion de la pasarela de pago");
            pasarela.setCodigosError(listaCodigosError);
        }else{
            if(log.isDebugEnabled()) log.debug("No existen codigos de error");
        }//if(codigosError != null && !"".equalsIgnoreCase(codigosError))
        if(log.isDebugEnabled()) log.debug("cargarCodigosError() : END");
    }//cargarCodigosError
    
}//class
