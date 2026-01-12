package es.altia.flexia.expedientes.relacionados.historico.plugin;

import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.TramitacionManager;
import es.altia.flexia.expedientes.relacionados.historico.util.ConstantesExpedientesRelacionadosHistorico;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesAsociadosVO;
import es.altia.flexia.expedientes.relacionados.historico.vo.ExpedientesRelacionadosHistoricoVO;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author david.caamano
 */
public class PluginBusquedaExpedientesRelacionadosHistoricoLanbide extends PluginBusquedaExpedientesRelacionadosHistorico {
    
    //Logger
    private static Logger log = LogManager.getLogger(PluginBusquedaExpedientesRelacionadosHistoricoLanbide.class);
    
    //Fichero de configuracion
    private static ResourceBundle bundleRegistro = ResourceBundle.getBundle("Registro");
    
    //Constantes
    private static String BLOQUEAR_PANTALLA = "BLOQUEAR_PANTALLA_DESASOCIAR";
    private static String ESTADO_ANULADO = "1";
    
    @Override
    public Boolean bloquearPantalla(String codOrganizacion) {
        if(log.isDebugEnabled()) log.debug("bloquearPantalla() : BEGIN");
        Boolean bloquearPantalla = false;
        try{
            String valor = bundleRegistro.getString(codOrganizacion + ConstantesExpedientesRelacionadosHistorico.BARRA + BLOQUEAR_PANTALLA);
            if("SI".equalsIgnoreCase(valor)){
                bloquearPantalla = true;
            }//if("SI".equalsIgnoreCase(valor))
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la propiedad que indica si debemos de bloquear la pantalla de expedientes asociados"
                    + "por defecto devolvera el valor false " + ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("bloquearPantalla = " + String.valueOf(bloquearPantalla));
        if(log.isDebugEnabled()) log.debug("bloquearPantalla() : END");
        return bloquearPantalla;
    }//bloquearPantalla
    
    @Override
    public ExpedientesAsociadosVO getComprobarDesasociacion(String departamento, String ejercicio, String tipo_reg,
        String[] params) throws TramitacionException{
        if(log.isDebugEnabled()) log.debug("getNumeroExpedientesAsociados() : BEGIN");
        ArrayList<ExpedientesRelacionadosHistoricoVO> expedientes = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        ExpedientesAsociadosVO expedientesAsociados = new ExpedientesAsociadosVO();
        try{
            //Recuperamos la lista de expedientes con su estado
            //Para el cliente de lanbide tenemos 3 posibles casos
            // * Si no existen expedientes asociados damos permiso para desasociar
            // * Si todos los expedientes asociados tienen el estado anulado damos permiso para desasociar
            // * Si alguno de los expedientes asociados NO tiene estado anulado NO damos permiso para desasociar
            expedientes = TramitacionManager.getInstance().getExpedientesAsociados(departamento, ejercicio, tipo_reg, params);
            
            if(expedientes != null && expedientes.size() > 0){
                if(log.isDebugEnabled()) log.debug("Recorremos los expedientes asociados para saber si estan todos anulados");
                Boolean permiso = true;
                for(ExpedientesRelacionadosHistoricoVO expediente : expedientes){
                    if(!ESTADO_ANULADO.equalsIgnoreCase(expediente.getEstado())){
                        permiso = false;
                    }//if(ESTADO_ANULADO.equalsIgnoreCase(expediente.getEstado()))
                }//for(ExpedientesRelacionadosHistoricoVO expediente : expedientes)
                if(log.isDebugEnabled()) log.debug("Seteamos el valor que indica si permitimos desasociar los expedientes");
                if(log.isDebugEnabled()) log.debug("permiso = " + String.valueOf(permiso) );
                expedientesAsociados.setPermitimosDesasociar(permiso);
                expedientesAsociados.setNumExpedientesAsociados(String.valueOf(expedientes.size()));
            }else{
                if(log.isDebugEnabled()) log.debug("No existen expedientes asociados a la entrada de registro");
                expedientesAsociados.setPermitimosDesasociar(true);
                expedientesAsociados.setNumExpedientesAsociados("0");
            }//if(expedientes != null && expedientes.size() > 0)
        }catch(TramitacionException trEx){
            log.error("Se ha producido un error recuperando el numero de expedientes asociados " + trEx.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getNumeroExpedientesAsociados() : END");
        return expedientesAsociados;
    }//getNumeroExpedientesAsociados

    @Override
    public ExpedientesAsociadosVO getComprobarDesasociacion(String departamento, String ejercicio, String tipo_reg, String cod_uni, String[] params) throws TramitacionException {
        // Existe esta implementación porque así lo requiere la clase abstracta de la que hereda, pero en Lanbide no es necesaria
        return null;
    }

    @Override
    public ArrayList<String> listaExpedientes(String departamento, String ejercicio, String tipo_reg, String[] params)
        throws TramitacionException{
        if(log.isDebugEnabled()) log.debug("listaExpedientes() : BEGIN");
        ArrayList<ExpedientesRelacionadosHistoricoVO> expedientes = new ArrayList<ExpedientesRelacionadosHistoricoVO>();
        ArrayList<String> listaExpedientes = new ArrayList<String>();
        try{
            expedientes = TramitacionManager.getInstance().getExpedientesAsociados(departamento, ejercicio, tipo_reg, params);
            for(ExpedientesRelacionadosHistoricoVO expRel : expedientes){
                listaExpedientes.add(expRel.getExpediente());
            }//for(ExpedientesRelacionadosHistoricoVO expRel : expedientes)
        }catch(TramitacionException trEx){
            log.error("Se ha producido un error recuperando los expedientes asociados " + trEx.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("listaExpedientes() : END");
        return listaExpedientes;
    }//listaExpedientes

    @Override
    public ArrayList<String> listaExpedientes(String departamento, String ejercicio, String tipo_reg, String unidadReg, String[] params) throws TramitacionException {
        // Existe esta implementación porque así lo requiere la clase abstracta de la que hereda, pero en Lanbide no es necesaria
        return null;
    }

}//class
