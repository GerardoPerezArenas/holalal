package es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago;

import es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoTerceroPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoResolucionPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoReservaPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BResultadoReservaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.*;
import java.util.ArrayList;

/**
 * @author david.caamano
 * @version 03/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 03/12/2012 * Edición inicial</li>
 * </ol> 
 */
public interface IPasarelaPago {
    
    /**
     * Firma que debera implementar el metodo para inicializar las posibles peculiaridades de la pasarela de pagos para un expediente
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception 
     */
    public String iniciarExpedientePasarela (String numExpediente, String codProcedimiento , String ejercicio, Integer codOrganizacion) 
            throws MeIkus01Exception;
      
    /**
     * Firma que debera implementar el metodo para realizar reservas en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param reservaVO
     * @return ResultadoReservaVO
     */
    public ResultadoReservaPasarelaPagosVO reserva(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;

    /**
     * Firma que debera implementar el metodo para realizar reservas en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param reservaVO
     * @return ResultadoReservaVO
     */
    public ResultadoReservaPasarelaPagosVO reservaSinExt(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;    
    
    /**
     * Firma que debera implementar el metodo para eliminar reservas en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param reservaVO
     * @return ResultadoReservaVO
     */
    public ResultadoReservaPasarelaPagosVO eliminarReserva(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;
    
    /**
     * Firma que debera implementar el metodo para grabar resoluciones en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param resolucionVO
     * @return 
     */
    public ResultadoResolucionPasarelaPagosVO grabarResolucion(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;

    /**
     * Firma que debera implementar el metodo para grabar resoluciones en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param resolucionVO
     * @return 
     */
    public ResultadoResolucionPasarelaPagosVO grabarResolucionSinExt(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;    

    /**
     * Firma que debera implementar el metodo para grabar pagos en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param resolucionVO
     * @return 
     */
    public ResultadoResolucionPasarelaPagosVO grabarPago(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;
    
    /**
     * Firma que debera implementar el metodo para grabar pagos en la pasarela de pagos del modulo.
     * @param seguridadVO
     * @param resolucionVO
     * @return 
     */
    public ResultadoResolucionPasarelaPagosVO grabarPagoSinExt(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion)
            throws MeIkus01Exception;    
        
    /**
     * Lista con los codigos de error para la pasarela cargados del fichero de propiedades
     * @param codigosError
     * @return 
     */
    public void setCodigosError(ArrayList<String> codigosError)
            throws MeIkus01Exception;
    
}//IPasarelaPago
