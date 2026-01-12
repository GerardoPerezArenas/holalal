package es.altia.flexia.integracion.moduloexterno.meikus.utilidades;

import es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioTerceroModuloIntegracionVO;
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
public class MeIkus01Utils {
    
    private final static Logger log = LogManager.getLogger(MeIkus01Utils.class);
    
    public static String getValorCampoSuplementario (CampoSuplementarioModuloIntegracionVO campo) throws MeIkus01Exception{
        if(log.isDebugEnabled()) {
            log.debug("getValorCampoSuplementario() : BEGIN");
        }
        String valor = "";
        try{
            Integer tipoCampo = campo.getTipoCampo();
            switch(tipoCampo){
                case(1):
                    valor = campo.getValorNumero();
                    break;
                case(2):
                    valor = campo.getValorTexto();
                    break;
                case(3):
                    valor = campo.getValorFechaAsString();
                    break;
                case(4):
                    valor = campo.getValorTexto();
                    break;
                case(6):
                    valor = campo.getValorDesplegable();
                    break;
                case(7):
                    valor = campo.getValorDesplegable();
                    break;
                default:
                    log.error("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioModuloIntegracionVO, el codigo de campo"
                            + "no es valido");
                    throw new MeIkus01Exception("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioModuloIntegracionVO, el codigo de campo"
                            + "no es valido");
            }//switch(tipoCampo)
        }catch(MeIkus01Exception ex){
            log.error("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioModuloIntegracionVO " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioModuloIntegracionVO", ex);
        }//try-catch
        if(log.isDebugEnabled()) {
            log.debug("getValorCampoSuplementario() : BEGIN");
        }
        return valor;
    }//getValorCampoSuplementario
    
    public static String getValorCampoSuplementarioTercero (CampoSuplementarioTerceroModuloIntegracionVO campo) throws MeIkus01Exception{
        if(log.isDebugEnabled()) {
            log.debug("getValorCampoSuplementarioTercero() : BEGIN");
        }
        String valor = "";
        try{
            Integer tipoCampo = campo.getTipoCampo();
            log.debug("tipoCampo : " +tipoCampo);
            switch(tipoCampo){
                case(1):
                    valor = campo.getValorNumero();
                    break;
                case(2):
                    valor = campo.getValorTexto();
                    break;
                case(3):
                    valor = campo.getValorFechaAsString();
                    break;
                case(4):
                    valor = campo.getValorTexto();
                    break;
                case(6):
                    valor = campo.getValorDesplegable();
                    break;
                case(7):
                    valor = campo.getValorDesplegable();
                    break;
                default:
                    log.error("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioTerceroModuloIntegracionVO, el codigo de campo"
                            + "no es valido");
                    throw new MeIkus01Exception("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioTerceroModuloIntegracionVO, el codigo de campo"
                            + "no es valido");
            }//switch(tipoCampo)
        }catch(MeIkus01Exception ex){
            log.error("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioTerceroModuloIntegracionVO " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error extrayendo el valor de un objeto CampoSuplementarioTerceroModuloIntegracionVO", ex);
        }//try-catch
        if(log.isDebugEnabled()) {
            log.debug("getValorCampoSuplementarioTercero() : END");
        }
        return valor;
    }//getValorCampoSuplementario
    
}//class
