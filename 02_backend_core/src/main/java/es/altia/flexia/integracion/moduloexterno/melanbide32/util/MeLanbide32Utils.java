/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.util;

import es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.DomicilioVO;

/**
 *
 * @author santiagoc
 */
public class MeLanbide32Utils 
{
    public static String obtenerNombreProceso(int codProceso, int codIdioma)
    {
        String desc = null;
        MeLanbide32I18n mm = MeLanbide32I18n.getInstance();
        try
        {
            switch(codProceso)
            {
                case ConstantesMeLanbide32.COD_PROC_ADJUDICA_CENTROS:
                    desc = mm.getMensaje(codIdioma, "proc.label.RPC");
                    break;
                case ConstantesMeLanbide32.COD_PROC_ADJUDICA_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.RPH");
                    break;
                case ConstantesMeLanbide32.COD_PROC_CONSOLIDA_CENTROS:
                    desc = mm.getMensaje(codIdioma, "proc.label.CC");
                    break;
                case ConstantesMeLanbide32.COD_PROC_CONSOLIDA_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.CH");
                    break;
                case ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_CENTROS:
                    desc = mm.getMensaje(codIdioma, "proc.label.DCC");
                    break;
                case ConstantesMeLanbide32.COD_PROC_DESHACER_CONSOLIDACION_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.DCH");
                    break;
                case ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_CENTROS:
                    desc = mm.getMensaje(codIdioma, "proc.label.DRC");
                    break;
                case ConstantesMeLanbide32.COD_PROC_DOCUMENTACION_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.DRH");
                    break;
                default:
                    desc = null;
                    break;
            }
        }
        catch(Exception ex)
        {
            
        }
        return desc;
    }
    
    public static Integer getEjercicioDeExpediente(String numExpediente) {
        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getCodProcedimientoDeExpediente(String numExpediente) {
        try {
            String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
            return datos[1];
        } catch (Exception ex) {
            return null;
        }
    }
}
