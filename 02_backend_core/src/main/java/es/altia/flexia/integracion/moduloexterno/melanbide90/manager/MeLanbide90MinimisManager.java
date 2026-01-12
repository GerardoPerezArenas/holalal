package es.altia.flexia.integracion.moduloexterno.melanbide90.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConstantesMeLanbide90;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class MeLanbide90MinimisManager {

    private static final Logger log = Logger.getLogger(MeLanbide90MinimisManager.class);
    private final IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

    public Map<String,String> gestionValidarValorMinimisAplicarExpediente(int codOrganizacion, String numExpediente, double valorMinimis){
        Map<String,String> respuesta = new HashMap<String, String>();
        try {
            boolean numeroExpedienteOk = numExpediente != null && !numExpediente.isEmpty() && numExpediente.split("/").length == 3;
            String ejercicioExpediente = numeroExpedienteOk ? numExpediente.split("/")[0] : "";
            String codProcedimiento = numeroExpedienteOk ? numExpediente.split("/")[1] :"";
            SalidaIntegracionVO campoMinimis = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion)
                    , ejercicioExpediente, numExpediente, codProcedimiento
                    , ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_MINIMIS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    , ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            // Redondear a dos decimales para que no falle al insertar en flexia
            valorMinimis = (double) Math.round(valorMinimis * 100) / 100;
            if (campoMinimis != null && campoMinimis.getStatus() == 0) {
                double minimis = Double.parseDouble(campoMinimis.getCampoSuplementario().getValorNumero());

                log.info("Minimis-BD: " + minimis);
                log.info("      -----------");
                log.info("Minims-Calculado: " + (valorMinimis));
                // el mismo que el del campo SUBVENCIÓN TOTAL si la suma de los MINIMIS A APLICAR y la SUBVENCIÓN TOTAL no supera los 300.000 euros. Si los superara sería la diferencia entre 300.000 ¤ y los MINIMIS a aplicar
                if (minimis != valorMinimis) {
                    log.info("Valor en BD diferente al calculado, actualizamos.");
                    campoMinimis.getCampoSuplementario().setEjercicio(ejercicioExpediente);
                    campoMinimis.getCampoSuplementario().setValorNumero(String.valueOf(valorMinimis));
                    SalidaIntegracionVO salida = el.grabarCampoSuplementario(campoMinimis.getCampoSuplementario());
                    log.info("Salida Actualizar campo: " + salida != null ? salida.getStatus() + ":  " + salida.getDescStatus() : "null");
                    respuesta.put("minimisAplicar","Valor en BD diferente al calculado, actualizamos: " + salida.getStatus() + "=" + salida.getDescStatus() );
                } else {
                    log.info("Valor en BD igual al calculado.");
                    respuesta.put("minimisAplicar","Valor en BD igual al calculado.");
                }
            }else{
                // No existe y hay un valor calculado: creamos
                if(valorMinimis != 0.0){
                    try {
                        CampoSuplementarioModuloIntegracionVO campoNumerico = new CampoSuplementarioModuloIntegracionVO();
                        campoNumerico.setCodOrganizacion(String.valueOf(codOrganizacion));
                        campoNumerico.setCodProcedimiento(codProcedimiento);
                        campoNumerico.setEjercicio(ejercicioExpediente);
                        campoNumerico.setNumExpediente(numExpediente);
                        campoNumerico.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        campoNumerico.setCodigoCampo(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_MINIMIS, ConstantesMeLanbide90.FICHERO_PROPIEDADES));
                        campoNumerico.setValorNumero(String.valueOf(valorMinimis));
                        SalidaIntegracionVO salida = el.grabarCampoSuplementario(campoNumerico);
                        log.info("Salida grabar campo: " + salida != null ? salida.getStatus() + ":  " + salida.getDescStatus() : "null");
                        respuesta.put("minimisAplicar","No existia campo suple. Creamos: " + salida.getStatus() + "=" + salida.getDescStatus() );
                    } catch (Exception e) {
                        log.error("Error al tratar de inserta el valor del campo suplementario " +ConstantesMeLanbide90.COD_CAMPO_MINIMIS +": " + e.getMessage() ,e);
                    }
                }else{
                    log.info("No existe en BD y el valor calculado es 0, no realizamos ninguna operacion.");
                    respuesta.put("minimisAplicar","No existe en BD y el valor calculado es 0, no realizamos ninguna operacion. - "
                            + (campoMinimis != null ? campoMinimis.getStatus() + "=" + campoMinimis.getDescStatus() : ""));
                }
            }
        } catch (Exception e) {
            respuesta.put("minimisAplicar","Errro al vaidar el minimis a aplicar: " + e.getMessage());
            log.error("Error al validar el Minimis a aplicar en el el expediente: " + numExpediente  + ": " + e.getMessage(),e);
        }
        return respuesta;
    }

    public Double getValorCSEMinimisAplicarExpediente(int codOrganizacion, String numExpediente){
        Double respuesta = null;
        try {
            boolean numeroExpedienteOk = numExpediente != null && !numExpediente.isEmpty() && numExpediente.split("/").length == 3;
            String ejercicioExpediente = numeroExpedienteOk ? numExpediente.split("/")[0] : "";
            String codProcedimiento = numeroExpedienteOk ? numExpediente.split("/")[1] :"";
            SalidaIntegracionVO campoMinimis = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion)
                    , ejercicioExpediente, numExpediente, codProcedimiento
                    , ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_MINIMIS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    , ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            log.info("Consulta campo: " + (campoMinimis != null ? campoMinimis.getStatus() + " - " + campoMinimis.getDescStatus():""));
            if (campoMinimis != null && campoMinimis.getStatus() == 0) {
                respuesta = Double.parseDouble(campoMinimis.getCampoSuplementario().getValorNumero());
            }
        } catch (Exception e) {
            log.error("Error al obtener el valor del campo suplementario MINIMIS -" + numExpediente);
        }
        return respuesta;
    }

    public boolean isSubvencionAplicarMinimisExpediente(FilaMinimisVO lm){
        return lm != null
                //&& lm.getOrganismo() != null && !lm.getOrganismo().isEmpty() && !lm.getOrganismo().toLowerCase().contains("lanbide")
                && lm.getEstado() != null && !lm.getEstado().isEmpty() && lm.getEstado().equalsIgnoreCase("C")
                ;
    }

}
