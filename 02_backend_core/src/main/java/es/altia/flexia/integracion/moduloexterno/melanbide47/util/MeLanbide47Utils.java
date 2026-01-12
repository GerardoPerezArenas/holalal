/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author santiagoc
 */
public class MeLanbide47Utils 
{
    private static Logger log = LogManager.getLogger(MeLanbide47Utils.class);
    static final SimpleDateFormat formatoddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");

    public static Integer getEjercicioDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide47.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public static String getCodProcedimientoDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide47.BARRA_SEPARADORA);
            return datos[1];
        }
        catch(Exception ex)
        {
            return null;
        }
    }   
    
    public static String obtenerNombreProceso(int codProceso, int codIdioma)
    {
        String desc = null;
        MeLanbide47I18n mm = MeLanbide47I18n.getInstance();
        try
        {
            switch(codProceso)
            {
                case ConstantesMeLanbide47.COD_PROC_ADJUDICA_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.RPH");
                    break;
                case ConstantesMeLanbide47.COD_PROC_CONSOLIDA_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.CH");
                    break;
                case ConstantesMeLanbide47.COD_PROC_DESHACER_CONSOLIDACION_HORAS:
                    desc = mm.getMensaje(codIdioma, "proc.label.DCH");
                    break;
                case ConstantesMeLanbide47.COD_PROC_DOCUMENTACION_HORAS:
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
     public static String escapeNewLineToSimpleSpace(String texto){
        if(texto!=null){
            texto=texto.replaceAll("\n"," ");
        }
        return texto;
    }
     
    /**
     * Decodifica una cadena de texto a un endogin especifico.
     *
     * @param textoInput
     * @param encodig
     * @return
     * @throws IOException
     */
    public static String decodeText(String textoInput, String encodig) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(textoInput.getBytes(encodig)
                        )
                )
        ).readLine();
    }
    
    /**
     * Calcula el numero de Meses etre dos fechas -1 Si son fechas no validas o si Fecha Fin < Fecha Inicio
     * @param inicio
     * @param fin
     * @return Numero de Meses entre dos fechas. -1 Si son fechas no validas (Errores al convertir) o si Fecha Fin < Fecha Inicio
     */
    public int calcularNroMesesEntreDosFechas(Date inicio, Date fin){
        int retorno =-1;
        try {
            if(inicio!=null && fin!=null){
                String inicioH00= formatoddMMyyyy.format(inicio);
                String finH00= formatoddMMyyyy.format(fin);
                if(formatoddMMyyyy.parse(inicioH00).before(formatoddMMyyyy.parse(finH00))){
                    String[] inicioH00Array = inicioH00.split("/");
                    String[] finH00Array = finH00.split("/");
                    LocalDate local1 = new LocalDate(Integer.valueOf(inicioH00Array[2]),Integer.valueOf(inicioH00Array[1]),Integer.valueOf(inicioH00Array[0]));
                    LocalDate local2 = new LocalDate(Integer.valueOf(finH00Array[2]),Integer.valueOf(finH00Array[1]),Integer.valueOf(finH00Array[0]));
                    retorno = Months.monthsBetween(local1, local2.plusDays(1)).getMonths();                   
                }else if(formatoddMMyyyy.parse(inicioH00).after(formatoddMMyyyy.parse(finH00))){
                    retorno=-1;
                }else{
                    retorno=0;
                }
            }
        } catch (Exception e) {
            log.error("calcularNroMesesEntreDosFechas: ", e);
            retorno=-1;
        }
        return retorno;
    }

    public static int getIdiomaUsuarioFromRequest(HttpServletRequest request){
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            return (usuario != null ? usuario.getIdioma() : 1);
        } catch (Exception ex) {
            log.error("Error leyendo Idioma del Usuario: " + ex.getMessage(), ex);
            ex.printStackTrace();
            return 1;
        }
    }
}
