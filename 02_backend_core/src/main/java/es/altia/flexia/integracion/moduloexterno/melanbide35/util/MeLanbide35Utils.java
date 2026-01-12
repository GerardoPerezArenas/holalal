/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.util;

import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.DatoExcelNoValidoException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.ExcelRowMappingException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaValorListaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorSolicitudVO;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.plaf.synth.Region;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 *
 * @author SantiagoC
 */
public class MeLanbide35Utils 
{
    
    
    private static Logger log = LogManager.getLogger(MeLanbide35Utils.class);
    public static String getValorCelda(HSSFCell celda, String tipoRequerido)
    {
        log.debug("tiporequerido:"+tipoRequerido+", celda: "+celda);
        Object valor = null;
        try{        
        if(celda != null)
        {
            log.debug("celda.getCellType():"+celda.getCellType());
            switch(celda.getCellType())
            {
                case HSSFCell.CELL_TYPE_BLANK:
                    valor = null;
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN:
                    valor = celda.getBooleanCellValue();
                    break;
                case HSSFCell.CELL_TYPE_ERROR:
                    valor = celda.getErrorCellValue();
                    break;
                case HSSFCell.CELL_TYPE_FORMULA:
                    valor = celda.getNumericCellValue();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                        valor = celda.getNumericCellValue();
                        //log.debug("valor:"+valor);
                        try{                                                
                        if (HSSFDateUtil.isCellDateFormatted(celda)) //poi 3.0                            
                        {
                            //es fecha
                            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                            valor = format.format(HSSFDateUtil.getJavaDate((Double)celda.getNumericCellValue()));                            
                            log.debug("celda es fecha:"+valor);
                        }
                        else
                        {
                            //es numero 
                            //#250993 EN LOS NÚMEROS DE TELÉFONO NO SE GUARDAN LOS CEROS DEL FINAL                          
                            //valor = ((Double)valor).doubleValue();
                            valor=new BigDecimal(celda.getNumericCellValue());
                            log.debug("celda es numero:"+valor);
                        }                        
                        }catch(Exception hexc){
                           log.error("iscelldateformatted error--", hexc);
                        }
                    break;
                case HSSFCell.CELL_TYPE_STRING:
                    valor = celda.getStringCellValue();
                    break;
            }
        }
        
        //Esto se hace porque libreoffice no formatea bien algunos tipos de datos y se han jodido las plantillas
        //Apaño para solucionarlo
        if(tipoRequerido != null && valor != null)
        {
            if(tipoRequerido.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.FECHA))
            {
                //Si es una fecha lo mas seguro es q venga como numero. Hay que mirar porque puede venir con parte decimal.
                //Si viene con parte decimal hay que quitarsela si esta es 0
                //Si la parte decimal no es 0, se devuelve el valor con decimales
                //Si no es un numero puede que venga bien formateado, o no --> capturo excepcion y devuelvo lo q tengo
                try
                {
                    String texto = valor.toString();
                    if(texto.contains(".") || texto.contains(","))
                    {
                        //O puede que venga con decimales. En este caso basta con mirar si la parte decimal es 0
                        //Si es asi, se la quito. Si no devuelvo el numero con decimales
                        texto = texto.replaceAll("\\.", ",");
                        String[] partes = texto.split(",");
                        if(partes.length == 2 && Integer.parseInt(partes[1]) == 0)
                        {
                            texto = texto.substring(0, texto.indexOf(","));
                            valor = texto;
                        }
                    }
                    

                    if(texto.contains("/"))
                    {
                       valor = texto;
                    }else{   
                        log.debug("fecha sacar numdias de "+valor);            
                        int numDias = Integer.parseInt(valor.toString());
                        SimpleDateFormat sdf = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);     

                        GregorianCalendar calPartida = new GregorianCalendar();
                        calPartida.setTime(sdf.parse(ConstantesMeLanbide35.FECHA_PARTIDA_EXCEL));

                        calPartida.add(Calendar.DAY_OF_MONTH, numDias-1);
                        Date d = calPartida.getTime();
                        valor = sdf.format(d);
                    }
                }
                catch(Exception ex)
                {
                    log.error("exception", ex);
                }
            }
            else if(tipoRequerido.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.NUMERICO))
            {
                //Si es un numerico, puede que venga con exponente o con decimales.
                try
                {
                    String texto = valor.toString();
                    if(texto.contains("E"))
                    {
                        //Puede que venga con exponente. Hay que mirar si el numero sin exponente tiene decimales o no
                        //Si no tiene decimales, se devuelve el numero sin exponente
                        //Si tiene decimales, hay que mirar que la parte decimal sea 0
                        //Si es 0, se devuelve el numero sin exponente y sin decimales
                        //Si no, se devuelve el numero tal cual
                        
                        String[] partes = texto.toUpperCase().split("E");
        
                        if(partes.length == 2)
                        {
                            int numExp = Integer.parseInt(partes[1]);

                            String numStr = partes[0];

                            if(numStr.contains(".") || numStr.contains(","))
                            {
                                numStr = numStr.replaceAll("\\.", ",");
                                String[] partesStr = numStr.split(",");
                                if(partesStr.length == 2)
                                {
                                    if(partesStr[1].length() <= numExp)
                                    {
                                        numStr = numStr.replace(",", "");
                                        valor = numStr;
                                    }
                                    else
                                    {
                                        int parteDecimal = Integer.parseInt(partesStr[1]);
                                        if(parteDecimal == 0)
                                        {
                                            numStr = partesStr[1];
                                            valor = numStr;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if(texto.contains(".") || texto.contains(","))
                    {
                        //O puede que venga con decimales. En este caso basta con mirar si la parte decimal es 0
                        //Si es asi, se la quito. Si no devuelvo el numero con decimales
                        texto = texto.replaceAll("\\.", ",");
                        String[] partes = texto.split(",");
                        if(partes.length == 2 && Integer.parseInt(partes[1]) == 0)
                        {
                            texto = texto.substring(0, texto.indexOf(","));
                            valor = texto;
                        }
                    }
                }
                catch(Exception ex)
                {
                     log.error("exception", ex);
                }
            }
            else if(tipoRequerido.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING_NIF) || tipoRequerido.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING_CIF))
            {
                String str = valor.toString();
                str = str.replaceAll("-", "").replaceAll(" ", "");
                valor = str;
            }
            else if(tipoRequerido.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING))
            {
                valor = reemplazarCaracteresEspeciales(valor.toString());
            }
        }
        }catch(Exception e){
        log.error("Error",e);
        }
        
        return valor != null ? valor.toString() : null;
    }
    
    public static List<String> validarFilaPreparadorSolicitud(FilaPreparadorSolicitudVO fila, EcaSolPreparadoresVO origen, List<EcaSolPreparadoresVO> sustitutos, EcaConfiguracionVO config, Integer ejercicio, int idioma)
    {
        try
        {
            EcaSolPreparadoresVO prep = MeLanbide35MappingUtils.getInstance().fromFilaPreparadorVOToPreparadorVO(fila);
            return validarEcaSolPreparadoresVO(prep, origen, sustitutos, config, ejercicio, idioma, false, true);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage());
            return list;
        }
    }
    
    public static List<String> validarEcaSolPreparadoresVO(EcaSolPreparadoresVO prep, EcaSolPreparadoresVO origen, List<EcaSolPreparadoresVO> sustitutos, EcaConfiguracionVO config, Integer ejercicio,  int idioma, boolean soloFormato, boolean desdeFila)
    {
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        
        //validar nif
        if(prep.getNif() != null && !prep.getNif().equals(""))
        {
            if(validarPatronNif(prep.getNif()))
            {
                if(!soloFormato)
                {
                    try
                    {
                        String nif = prep.getNif();
                        String numero = nif.substring(0, 8);
                        String letra = nif.substring(8, nif.length());
                        String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                        if(letra != null && letraCalculada != null)
                        {
                            if(!letra.equalsIgnoreCase(letraCalculada))
                            {
                                mensaje = traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrectoLetra");
                                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            else
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Fecha de inicio anterior a fecha de fin
        if(!soloFormato)
        {
            try
            {
                Date feInicio = prep.getFecIni();
                Date feFin = prep.getFecFin();
                if(feInicio != null && feFin != null && feInicio.after(feFin))
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechaIniFechaFin");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    Calendar cal = null;
                    int ano = -1;
                    //Fecha de inicio y fin en el ano del expediente
                    if(feInicio != null)
                    {
                        cal = new GregorianCalendar();
                        cal.setTime(feInicio);
                        ano = cal.get(Calendar.YEAR);
                        if(ejercicio != null && ano != ejercicio)
                        {
                            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechasAno"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }

                    if(feFin != null)
                    {
                        cal.setTime(feFin);
                        ano = cal.get(Calendar.YEAR);
                        if(ejercicio != null && ano != ejercicio)
                        {
                            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechasAno"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicacion ECA
        if(!soloFormato)
        {
            try
            {
                Double hajc = prep.getHorasJC() != null ? prep.getHorasJC().doubleValue() : 0.0;
                Double hc = prep.getHorasCont() != null ? prep.getHorasCont().doubleValue() : 0.0;
                Double hdECA = prep.getHorasEca() != null ? prep.getHorasEca().doubleValue() : 0.0;
                if(hajc < hc)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasJCHorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(hc < hdECA)
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasContratoHorasEca");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionHoras");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
         
        
        //Horas Dedicación ECA >= 50% Horas Contrato
       /* if(!soloFormato)
        {
            try
            {
                Double hc = prep.getHorasCont() != null ? prep.getHorasCont().doubleValue() : 0.0;
                hc *= 0.5;
                Double hdECA = prep.getHorasEca() != null ? prep.getHorasEca().doubleValue() : 0.0;
                if(hdECA < hc)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasDedicacionEca50HorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasDedicacionEca50HorasContrato");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }*/
        
        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicaciÃ³n ECA
        if(!soloFormato)
        {
            try
            {
                Double cssJC = prep.getImpSSJC() != null ? prep.getImpSSJC().doubleValue() : 0.0;
                Double cssPorJor = prep.getImpSSJR() != null ? prep.getImpSSJR().doubleValue() : 0.0;
                Double cssECA = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : 0.0;

                if(cssJC < cssPorJor)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesJCCostesPJ");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(cssPorJor < cssECA)
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesPJCostesECA");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionCostes");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Importe Seguimientos: si es mayor al 25% de los costes salariales + seg social ECA, limitar a dicho valor
        if(!soloFormato)
        {
            try
            {
                Double importe = prep.getImpSegAnt() != null ? prep.getImpSegAnt().doubleValue() : 0.0;
                Double cssECA = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : 0.0;
                double poMax = config.getPoMaxSeguimientos() != null ? config.getPoMaxSeguimientos().doubleValue() : 1.0;
                Double por = (cssECA * poMax);
                BigDecimal porBD = new BigDecimal(por.toString());
                por = Double.parseDouble(redondearDecimalesString(porBD, 2));
                if(importe < por)
                {
                    //Hay que comprobar que corresponda a numSeg * config.getImSeguimiento()
                    if(prep.getImpSegAnt() != null)
                    {
                        BigDecimal imSeg = config.getImSeguimiento();
                        if(imSeg != null)
                        {
                            Integer segAnt = prep.getSegAnt();
                            if(segAnt != null)
                            {
                                double res = imSeg.doubleValue() * segAnt;
                                if(res != prep.getImpSegAnt().doubleValue())
                                {
                                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.importeSegAntIncorrecto"), ejercicio);
                                    if(!errores.contains(mensaje))
                                        errores.add(mensaje);
                                }
                            }
                        }
                    }
                }
                else if(importe > por)
                {
                    if(!desdeFila)
                    {
                        prep.setImpSegAnt(new BigDecimal(por.toString()));
                    }
                    else
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.importeSegAntSup25"), ejercicio);
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionImporteSegAnt"), ejercicio);
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Numero de inserciones, importes totales: Valores numÃ©ricos, sin decimales
        /*     
        double num;
        long iPart;
        double fPart;
        
        try
        {
            if(prep.getInsImporte() != null)
            {
                num = prep.getInsImporte().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insImporteConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    prep.setInsImporte(new BigDecimal(""+iPart));
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsImporte");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
      try
        {
            if(prep.getInsC1() != null)
            {
                num = prep.getInsC1().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC1ConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    prep.setInsC1(new BigDecimal(""+iPart));
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsC1");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        try
        {
            if(prep.getInsC2() != null)
            {
                num = prep.getInsC2().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC2ConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    prep.setInsC2(new BigDecimal(""+iPart));
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsC2");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
            
        try
        {
            if(prep.getInsC3() != null)
            {
                num = prep.getInsC3().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC3ConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    prep.setInsC3(new BigDecimal(""+iPart));
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsC3");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        try
        {
            if(prep.getInsC4() != null)
            {
                num = prep.getInsC4().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC4ConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    prep.setInsC4(new BigDecimal(""+iPart));
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsC4");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Los totales de cada colectivo = H + M
        if(!soloFormato)
        {
            if(prep.getInsC1() != null)
            {
                if(prep.getInsC1H() != null || prep.getInsC1M() != null)
                {
                    int suma = 0;
                    suma += prep.getInsC1H() != null ? prep.getInsC1H().intValue() : 0;
                    suma += prep.getInsC1M() != null ? prep.getInsC1M().intValue() : 0;
                    if(suma != prep.getInsC1().intValue())
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC1Incorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }

            if(prep.getInsC2() != null)
            {
                if(prep.getInsC2H() != null || prep.getInsC2M() != null)
                {
                    int suma = 0;
                    suma += prep.getInsC2H() != null ? prep.getInsC2H().intValue() : 0;
                    suma += prep.getInsC2M() != null ? prep.getInsC2M().intValue() : 0;
                    if(suma != prep.getInsC2().intValue())
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC2Incorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }

            if(prep.getInsC3() != null)
            {
                if(prep.getInsC3H() != null || prep.getInsC3M() != null)
                {
                    int suma = 0;
                    suma += prep.getInsC3H() != null ? prep.getInsC3H().intValue() : 0;
                    suma += prep.getInsC3M() != null ? prep.getInsC3M().intValue() : 0;
                    if(suma != prep.getInsC3().intValue())
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC3Incorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }

            if(prep.getInsC4() != null)
            {
                if(prep.getInsC4H() != null || prep.getInsC4M() != null)
                {
                    int suma = 0;
                    suma += prep.getInsC4H() != null ? prep.getInsC4H().intValue() : 0;
                    suma += prep.getInsC4M() != null ? prep.getInsC4M().intValue() : 0;
                    if(suma != prep.getInsC4().intValue())
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insC4Incorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
        }
        */
        
        //Importe de las inserciones. Cada Colectivo tiene un precio
        if(!soloFormato)
        {
            if(prep.getInsImporte() != null)
            {
                double importe = prep.getInsImporte().doubleValue();
                double importeCalculado = 0.0;

                if(prep.getInsC1H() != null)
                {
                    importeCalculado += prep.getInsC1H().doubleValue() * (config.getImC1h() != null ? config.getImC1h().doubleValue() : 1.0);
                }

                if(prep.getInsC1M() != null)
                {
                    importeCalculado += prep.getInsC1M().doubleValue() * (config.getImC1m() != null ? config.getImC1m().doubleValue() : 1.0);
                }

                if(prep.getInsC2H() != null)
                {
                    importeCalculado += prep.getInsC2H().doubleValue() * (config.getImC2h() != null ? config.getImC2h().doubleValue() : 1.0);
                }

                if(prep.getInsC2M() != null)
                {
                    importeCalculado += prep.getInsC2M().doubleValue() * (config.getImC2m() != null ? config.getImC2m().doubleValue() : 1.0);
                }

                if(prep.getInsC3H() != null)
                {
                    importeCalculado += prep.getInsC3H().doubleValue() * (config.getImC3h() != null ? config.getImC3h().doubleValue() : 1.0);
                }

                if(prep.getInsC3M() != null)
                {
                    importeCalculado += prep.getInsC3M().doubleValue() * (config.getImC3m() != null ? config.getImC3m().doubleValue() : 1.0);
                }

                if(prep.getInsC4H() != null)
                {
                    importeCalculado += prep.getInsC4H().doubleValue() * (config.getImC4h() != null ? config.getImC4h().doubleValue() : 1.0);
                }

                if(prep.getInsC4M() != null)
                {
                    importeCalculado += prep.getInsC4M().doubleValue() * (config.getImC4m() != null ? config.getImC4m().doubleValue() : 1.0);
                }

                if(importeCalculado != importe)
                {
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insImporteIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
        }
        
        boolean csSSCalculado = false;
        //Seguimientos + Inserciones: controlar que es la suma Importe + Inserciones
        if(!soloFormato)
        {
            if(prep.getInsSegImporte() != null)
            {
                if(prep.getImpSegAnt() != null || prep.getInsImporte() != null)
                {
                    double suma = 0.0;
                    suma += prep.getImpSegAnt() != null ? prep.getImpSegAnt().doubleValue() : 0.0;
                    suma += prep.getInsImporte() != null ? prep.getInsImporte().doubleValue() : 0.0;
                    suma= Double.parseDouble( redondearDecimalesString(new BigDecimal(suma),2));
                    if(prep.getInsSegImporte().doubleValue() != suma)
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.segInsNoSumaImpIns");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                    
                    csSSCalculado = true;
                    
                    

                    //Comprobar coste total solicitado prospector
                    if(prep.getCoste() != null)
                    {
                        Double imSSEca = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : null;
                        double minimo = 0.0;
                        if(imSSEca != null)
                        {
                            minimo = Math.min(prep.getInsSegImporte().doubleValue(), imSSEca);
                        }
                        else
                        {
                            minimo = prep.getInsSegImporte().doubleValue();
                        }

                        if(prep.getCoste().doubleValue() > minimo)
                        {
                            mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTE+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }    
            }
            
            if(!csSSCalculado)
            {
                if(prep.getCoste() != null)
                {
                    if(prep.getImpSSECA() != null)
                    {
                        Double coste = prep.getCoste().doubleValue();
                        Double minimo = prep.getImpSSECA().doubleValue();

                        if(coste > minimo)
                        {
                            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesSalarialesSSIncorrecto");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                    else
                    {
                        mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesSalarialesSSIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
        }
        
        //Fechas sustituto-origen solapadas
        if(!soloFormato && desdeFila)
        {
            Date dFinOrigen = null;
            long timedFinorigen = 0;
            Date dIni = null;
            long n2 = 0;
            long resultFcontrato = 0;
            if(origen != null && origen.getFecFin() != null && prep.getFecIni() != null)
            {
                dFinOrigen = origen.getFecFin();
                timedFinorigen = dFinOrigen.getTime();
                
                dIni = prep.getFecIni();
                n2 = dIni.getTime();
                
                resultFcontrato = n2 - timedFinorigen ;
                if (resultFcontrato <= 0){
                    mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechaIniAnteriorAOrigen");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            
            if(sustitutos != null && sustitutos.size() > 0)
            {
                EcaSolPreparadoresVO act = null;
                int posAct = 0;
                boolean encontrado = false;
                while(posAct < sustitutos.size() && !encontrado)
                {
                    act = sustitutos.get(posAct);
                    if(act.getFecIni() != null && prep.getFecFin() != null)
                    {
                        dFinOrigen = prep.getFecFin();
                        timedFinorigen = dFinOrigen.getTime();

                        dIni = act.getFecIni();
                        n2 = dIni.getTime();

                        resultFcontrato = n2 - timedFinorigen ;

                        if (resultFcontrato <= 0){
                            encontrado = true;
                            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechaIniAnteriorAOrigen");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                    posAct++;
                }
            }
        }
        
        return errores;
    }
    
    //Estas validaciones son las mismas que las que se hacen en validarEcaSolProspectoresVO
    //Cualquier cambio que se realice en este mÃ©todo, deberÃ¡ replicarse en validarEcaSolProspectoresVO
    public static List<String> validarFilaProspectorSolicitud(FilaProspectorSolicitudVO fila, EcaSolProspectoresVO origen, List<EcaSolProspectoresVO> sustitutos, EcaConfiguracionVO config, Integer ejercicio, int idioma)
    {
        try
        {
            EcaSolProspectoresVO pros = MeLanbide35MappingUtils.getInstance().fromFilaProspectorVOToProspectorVO(fila);
            return validarEcaSolProspectoresVO(pros, origen, sustitutos, config, ejercicio, idioma, false, true);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage());
            return list;
        }
    }
    
    //Estas validaciones son las mismas que las que se hacen en validarFilaProspectorSolicitud
    //Cualquier cambio que se realice en este mÃ©todo, deberÃ¡ replicarse en validarFilaProspectorSolicitud
    public static List<String> validarEcaSolProspectoresVO(EcaSolProspectoresVO pros, EcaSolProspectoresVO origen, List<EcaSolProspectoresVO> sustitutos, EcaConfiguracionVO config, Integer ejercicio, int idioma, boolean soloFormato, boolean desdeFila)
    {
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        //validar nif
        if(pros.getNif() != null && !pros.getNif().equals(""))
        {
            if(validarPatronNif(pros.getNif()))
            {
                if(!soloFormato)
                {
                    try
                    {
                        String nif = pros.getNif();
                        String numero = nif.substring(0, 8);
                        String letra = nif.substring(8, nif.length());
                        String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                        if(letra != null && letraCalculada != null)
                        {
                            if(!letra.equalsIgnoreCase(letraCalculada))
                            {
                                mensaje = traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrectoLetra");
                                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        mensaje = FilaProspectorSolicitudVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.nifIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            else
            {
                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Fecha de inicio anterior a fecha de fin
        if(!soloFormato)
        {
            try
            {
                Date feInicio = pros.getFecIni();
                Date feFin = pros.getFecFin();
                if(feInicio != null && feFin != null && feInicio.after(feFin))
                {
                    mensaje = FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.fechaIniFechaFin");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    Calendar cal = null;
                    int ano = -1;
                    //Fecha de inicio y fin en el ano del expediente
                    if(feInicio != null)
                    {
                        cal = new GregorianCalendar();
                        cal.setTime(feInicio);
                        ano = cal.get(Calendar.YEAR);
                        if(ejercicio != null && ano != ejercicio)
                        {
                            mensaje = FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.fechasAno"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }

                    if(feFin != null)
                    {
                        cal.setTime(feFin);
                        ano = cal.get(Calendar.YEAR);
                        if(ejercicio != null && ano != ejercicio)
                        {
                            mensaje = FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.fechasAno"), ejercicio);
                            if(!errores.contains(mensaje))
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.errorValidacionFechas");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicacion ECA
        if(!soloFormato)
        {
            try
            {
                Double hajc = pros.getHorasJC() != null ? pros.getHorasJC().doubleValue() : 0.0;
                Double hc = pros.getHorasCont() != null ? pros.getHorasCont().doubleValue() : 0.0;
                Double hdECA = pros.getHorasEca() != null ? pros.getHorasEca().doubleValue() : 0.0;
                if(hajc >= 0.0)
                {
                    if(hc >= 0.0)
                    {
                        if(hdECA >= 0.0)
                        {
                            if(hajc < hc)
                            {
                                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.horasJCHorasContrato");
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                            else
                            {
                                if(hc < hdECA)
                                {
                                    mensaje = FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.horasContratoHorasEca");
                                    if(!errores.contains(mensaje))
                                        errores.add(mensaje);
                                }
                            }
                        }
                        else
                        {
                            //mensaje
                        }
                    }
                    else
                    {
                        //mensaje
                    }
                }
                else
                {
                    //Mensaje
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.errorValidacionHoras");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
         
        
        //Horas Dedicación ECA >= 50% Horas Contrato
        /*if(!soloFormato)
        {
            try
            {
                Double hc = pros.getHorasCont() != null ? pros.getHorasCont().doubleValue() : 0.0;
                hc *= 0.5;
                Double hdECA = pros.getHorasEca() != null ? pros.getHorasEca().doubleValue() : 0.0;
                if(hdECA < hc)
                {
                    mensaje = FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.horasDedicacionEca50HorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.horasDedicacionEca50HorasContrato");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }*/
        
        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicaciÃ³n ECA
        if(!soloFormato)
        {
            try
            {
                Double cssJC = pros.getImpSSJC() != null ? pros.getImpSSJC().doubleValue() : 0.0;
                Double cssPorJor = pros.getImpSSJR() != null ? pros.getImpSSJR().doubleValue() : 0.0;
                Double cssECA = pros.getImpSSECA() != null ? pros.getImpSSECA().doubleValue() : 0.0;

                if(cssJC < cssPorJor)
                {
                    mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.costesJCCostesPJ");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(cssPorJor < cssECA)
                    {
                        mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.costesPJCostesECA");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.errorValidacionCostes");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Numero visitas: Controlar minimo 100, maximo 200
        if(!soloFormato)
        {
            if(pros.getVisitas() != null)
            {
                Double porJornada = 1.0;
                BigDecimal validaVisitaHorasECA = BigDecimal.ZERO;
                int totalVisitas = 0;
                if (origen != null) {
                    if (origen.getVisitas() != null) {
                        totalVisitas += origen.getVisitas();
                    }
                    if (origen.getHorasEca() != null) {
                        validaVisitaHorasECA = validaVisitaHorasECA.add(origen.getHorasEca());
                    }
                }

                if (sustitutos != null && sustitutos.size() > 0) {
                    for (EcaSolProspectoresVO p : sustitutos) {
                        if (p.getVisitas() != null) {
                            totalVisitas += p.getVisitas();
                        }
                        if (p.getHorasEca() != null) {
                            validaVisitaHorasECA = validaVisitaHorasECA.add(p.getHorasEca());
                        }
                    }
                }
                if (pros.getVisitas() != null) {
                    totalVisitas += pros.getVisitas();
                }
                if (pros.getHorasEca() != null) {
                    validaVisitaHorasECA = validaVisitaHorasECA.add(pros.getHorasEca());
                }
                log.info(" totalVisitas : " + pros.getNif() + " - " + pros.getNombre());
                log.info(" totalVisitas : " + totalVisitas);
                log.info(" validaVisitaHorasECA : " + validaVisitaHorasECA);
                if(pros.getHorasJC() != null) //getHorasCont  2018/10 Comparar con horas dedicacion pros.getHorasCont() != null && 
                {
                    try
                    {
                        BigDecimal temp = validaVisitaHorasECA.divide(pros.getHorasJC(),2, BigDecimal.ROUND_HALF_UP);
                        temp = new BigDecimal(redondearDecimalesString(temp, 2));
                        porJornada = temp.doubleValue();
                        log.info(" Porcentaje Jornada Calulado :" + porJornada);
                    }
                    catch(Exception ex)
                    {
                        
                    }
                }
                int minVisitas = 100;
                int maxVisitas = 200;
                if(config.getMinEmpVisit() != null)
                {
                    minVisitas = config.getMinEmpVisit();
                    Double d = minVisitas * porJornada;
                    String str = redondearDecimalesString(new BigDecimal(d.toString()), 0);
                    minVisitas = Integer.parseInt(str);
                }
                if(config.getMaxEmpVisit() != null)
                {
                    maxVisitas = config.getMaxEmpVisit();
                    Double d = maxVisitas * porJornada;
                    String str = redondearDecimalesString(new BigDecimal(d.toString()), 0);
                    maxVisitas = Integer.parseInt(str);
                }
                log.info(" minVisitas :" + minVisitas);
                log.info(" maxVisitas :" + maxVisitas);
                if(totalVisitas < minVisitas)
                {
                    mensaje = traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.visitasFueraRango");
                    mensaje = String.format(mensaje, minVisitas, maxVisitas);
                    mensaje = FilaProspectorSolicitudVO.POS_CAMPO_VISITAS+ConstantesMeLanbide35.BARRA_SEPARADORA+mensaje;
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else if(config.getMaxEmpVisit() != null)
                {
                    if(totalVisitas > maxVisitas)
                    {
                        mensaje = traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.visitasFueraRango");
                        mensaje = String.format(mensaje, minVisitas, maxVisitas);
                        mensaje = FilaProspectorSolicitudVO.POS_CAMPO_VISITAS+ConstantesMeLanbide35.BARRA_SEPARADORA+mensaje;
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }

                //Total importe visitas   
                if(pros.getVisitasImp() != null)
                {
                    double importeCalculado = pros.getVisitas() * (config.getImpVisita() != null ? config.getImpVisita().doubleValue() : 0.0);
                    if(pros.getVisitasImp().doubleValue() != importeCalculado)
                    {
                        mensaje = FilaProspectorSolicitudVO.POS_CAMPO_VISITAS_IMP+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.importeTotalVisitasIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }

                    //Comprobar coste total solicitado prospector
                    if(pros.getCoste() != null)
                    {
                        Double imSSEca = pros.getImpSSECA() != null ? pros.getImpSSECA().doubleValue() : null;
                        double minimo = 0.0;
                        if(imSSEca != null)
                        {
                            minimo = Math.min(importeCalculado, imSSEca);
                        }
                        else
                        {
                            minimo = importeCalculado;
                        }

                        if(pros.getCoste().doubleValue() > minimo)
                        {
                            mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTE+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                else
                {

                    if(pros.getCoste() != null)
                    {
                        if(pros.getImpSSECA() != null)
                        {
                            double minimo = pros.getImpSSECA().doubleValue();

                            if(pros.getCoste().doubleValue() > minimo)
                            {
                                mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTE+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto");
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                        else
                        {
                            mensaje = FilaProspectorSolicitudVO.POS_CAMPO_COSTE+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
            }
        }
        
        //Fechas sustituto-origen solapadas
        if(!soloFormato && desdeFila)
        {
            Date dFinOrigen = null;
            long timedFinorigen = 0;
            Date dIni = null;
            long n2 = 0;
            long resultFcontrato = 0;
            
            if(origen != null && origen.getFecFin() != null && pros.getFecIni() != null)
            {
                dFinOrigen = origen.getFecFin();
                timedFinorigen = dFinOrigen.getTime();
                
                dIni = pros.getFecIni();
                n2 = dIni.getTime();
                
                resultFcontrato = n2 - timedFinorigen ;
                if (resultFcontrato <= 0){
                    mensaje = FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.fechaIniAnteriorAOrigen");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            
            if(sustitutos != null && sustitutos.size() > 0)
            {
                EcaSolProspectoresVO act = null;
                int posAct = 0;
                boolean encontrado = false;
                while(posAct < sustitutos.size() && !encontrado)
                {
                    act = sustitutos.get(posAct);
                    if(pros.getFecFin() != null && act.getFecIni() != null)
                    {
                        dFinOrigen = pros.getFecFin();
                        timedFinorigen = dFinOrigen.getTime();

                        dIni = act.getFecIni();
                        n2 = dIni.getTime();

                        resultFcontrato = n2 - timedFinorigen ;

                        if (resultFcontrato <= 0){
                            encontrado = true;
                            mensaje = FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.prospectores.fechaIniAnteriorAOrigen");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                    posAct++;
                }
            }
        }
        return errores;
    }
    
    
    public static boolean validarDatoExcel(String valor, String tipo, int longMax, int pEntera, int pDecimal, boolean sePermiteVacio) throws DatoExcelNoValidoException
    {
        try
        {
            if(tipo != null)
            {
                if(valor != null && !valor.equals(""))
                {
                    if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.FECHA))
                    {
                        if ((valor.length()==10)){
                            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                            format.parse(valor);
                            return true;
                        }else return false;
                    }
                    else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.NUMERICO))
                    {
                        Double d = Double.parseDouble(valor);
                        Integer i = d.intValue();
                        if(i >= 0)
                        {
                            //valor = i.toString();
                            if(valor.contains(".") || valor.contains(","))
                            {
                                valor = valor.replaceAll("\\.", ",");
                                String[] partes = valor.split(",");
                                if(partes.length == 2 && Integer.parseInt(partes[1]) == 0)
                                {
                                    valor = valor.substring(0, valor.indexOf(","));
                                }
                                else
                                {
                                    return false;
                                }
                            }
                            if(valor.length() <= pEntera)
                            {
                                /*Long.parseLong(valor);
                                Integer i = new BigDecimal(valor).intValue();
                                String str = i.toString();
                                if(str.length() <= pEntera)
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }*/
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL))
                    {
                        valor = valor.replaceAll(",", "\\.");
                        Double d = Double.parseDouble(valor);
                        if(d >= 0.0)
                        {
                            String[] partes = valor.split("\\.");
                            if(partes.length == 1)
                            {
                                String parte1 = partes[0];
                                if(parte1.length() <= pEntera)
                                {
                                    Long.parseLong(parte1);
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                            else if(partes.length == 2)
                            {
                                String parte1 = partes[0];
                                String parte2 = partes[1];
                                pEntera = pEntera - pDecimal;
                                if(parte1.length() <= pEntera && parte2.length() <= pDecimal)
                                {
                                    Long.parseLong(parte1);
                                    Long.parseLong(parte2);
                                    BigDecimal bd = new BigDecimal(valor);
                                    if(bd.doubleValue() >= 0)
                                    {
                                        return true;
                                    }
                                    else
                                    {
                                        return false;
                                    }
                                }
                                else
                                {
                                    return false;
                                }
                            }
                            else
                            {
                                return false;
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING))
                    {
                        if(valor.length() <= longMax)
                        {
                            if(!tieneCaracteresEspeciales(valor))
                            {
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING_NIF))
                    {
                        
                            if(!tieneCaracteresEspeciales(valor))
                            {
                                return validarPatronNif(valor);
                            }
                            else
                            {
                                return false;
                            }
                    }
                    else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING_CIF))
                    {
                        if(!tieneCaracteresEspeciales(valor))
                        {
                            return isCifValido(valor);
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    if(sePermiteVacio)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            else
            {
                return false;
            }
        }
        catch(Exception ex)
        {
            throw new DatoExcelNoValidoException();
        }
    }
    
    public static String crearMensajeDatoExcelNoValido(ExcelRowMappingException erme, int fila, int codIdioma)
    {
        String mensaje = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.filaExcelDatosNoValidos");
        
        mensaje = String.format(mensaje, fromColNumberToExcelColName(Integer.parseInt(erme.getCampo())),""+fila);
        if(erme != null && erme.getTipo() != null)
        {
            String tipo = erme.getTipo();
            String mensajeTipo = "";
            if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.FECHA))
            {
                mensajeTipo = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "expectedType.date");
                mensajeTipo = String.format(mensajeTipo, ConstantesMeLanbide35.FORMATO_FECHA);
            }
            else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.NUMERICO))
            {
                if(erme.getpEntera() != null)
                {
                    mensajeTipo = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "expectedType.number");
                    mensajeTipo = String.format(mensajeTipo, erme.getpEntera());
                }
            }
            else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL))
            {
                if(erme.getpEntera() != null && erme.getpDecimal() != null)
                {
                    mensajeTipo = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "expectedType.decimalNumber");
                    mensajeTipo = String.format(mensajeTipo, erme.getpEntera()-erme.getpDecimal(), erme.getpDecimal());
                }
            }
            else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING))
            {
                if(erme.getLongMax() != null)
                {
                    mensajeTipo = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "expectedType.string");
                    mensajeTipo = String.format(mensajeTipo, erme.getLongMax());
                }
            }
            else if(tipo.equalsIgnoreCase(ConstantesMeLanbide35.TiposRequeridos.STRING_NIF))
            {
                if(erme.getLongMax() != null)
                {
                    mensajeTipo = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "expectedType.string_nif");
                }
            }
            mensaje += " ";
            mensaje += mensajeTipo;
        }
        return mensaje;
    }

    public static boolean validarPatronNif(String dni) 
    {
        boolean correcto=true;
        if (dni.length() <= 10)
        {
            
            Pattern patron = Pattern.compile("[0-9]{8,8}[a-zA-z]{1}");
            Matcher encaja = patron.matcher(dni);
            if(encaja.matches())
            {
                return true;
            }
            else
            {
                Pattern patronNie = Pattern.compile("[k-mK-Mx-zX-Z]{1}[0-9]{7,7}[a-zA-z]{1}");
                Matcher encajaNie = patronNie.matcher(dni);
                return encajaNie.matches();
            }
        }
        else 
        {
          correcto = false;
        }
        return correcto;
    }
    
    public static String fromColNumberToExcelColName(int col)
    {
        String retStr = "";
        try
        {
            int res;
            if(col > 25)
            {
                res = (col / 25) - 1;
            }
            else
            {
                res = col - 1;
            }
            switch(res)
            {
                case 0:
                    retStr += "A";
                    break;
                case 1:
                    retStr += "B";
                    break;
                case 2:
                    retStr += "C";
                    break;
                case 3:
                    retStr += "D";
                    break;
                case 4:
                    retStr += "E";
                    break;
                case 5:
                    retStr += "F";
                    break;
                case 6:
                    retStr += "G";
                    break;
                case 7:
                    retStr += "H";
                    break;
                case 8:
                    retStr += "I";
                    break;
                case 9:
                    retStr += "J";
                    break;
                case 10:
                    retStr += "K";
                    break;
                case 11:
                    retStr += "L";
                    break;
                case 12:
                    retStr += "M";
                    break;
                case 13:
                    retStr += "N";
                    break;
                case 14:
                    retStr += "O";
                    break;
                case 15:
                    retStr += "P";
                    break;
                case 16:
                    retStr += "Q";
                    break;
                case 17:
                    retStr += "R";
                    break;
                case 18:
                    retStr += "S";
                    break;
                case 19:
                    retStr += "T";
                    break;
                case 20:
                    retStr += "U";
                    break;
                case 21:
                    retStr += "V";
                    break;
                case 22:
                    retStr += "W";
                    break;
                case 23:
                    retStr += "X";
                    break;
                case 24:
                    retStr += "Y";
                    break;
                case 25:
                    retStr += "Z";
                    break;
                default:
                    retStr += "";
                    break;
            }
            if(col > 25)
            {
                int mod = (col % 25) - 1;
                switch(mod)
                {
                    case 0:
                        retStr += "A";
                        break;
                    case 1:
                        retStr += "B";
                        break;
                    case 2:
                        retStr += "C";
                        break;
                    case 3:
                        retStr += "D";
                        break;
                    case 4:
                        retStr += "E";
                        break;
                    case 5:
                        retStr += "F";
                        break;
                    case 6:
                        retStr += "G";
                        break;
                    case 7:
                        retStr += "H";
                        break;
                    case 8:
                        retStr += "I";
                        break;
                    case 9:
                        retStr += "J";
                        break;
                    case 10:
                        retStr += "K";
                        break;
                    case 11:
                        retStr += "L";
                        break;
                    case 12:
                        retStr += "M";
                        break;
                    case 13:
                        retStr += "N";
                        break;
                    case 14:
                        retStr += "O";
                        break;
                    case 15:
                        retStr += "P";
                        break;
                    case 16:
                        retStr += "Q";
                        break;
                    case 17:
                        retStr += "R";
                        break;
                    case 18:
                        retStr += "S";
                        break;
                    case 19:
                        retStr += "T";
                        break;
                    case 20:
                        retStr += "U";
                        break;
                    case 21:
                        retStr += "V";
                        break;
                    case 22:
                        retStr += "W";
                        break;
                    case 23:
                        retStr += "X";
                        break;
                    case 24:
                        retStr += "Y";
                        break;
                    case 25:
                        retStr += "Z";
                        break;
                    default:
                        retStr += "";
                        break;
                }
            }
        }
        catch(Exception ex)
        {
            retStr = "";
        }
        return retStr;
    }
    
    public static boolean esFilaVacia(int numCols, HSSFRow fila)
    {
        int i = 0;
        boolean valorEncontrado = false;
        HSSFCell cell = null;
        String valor = "";
        while(i < numCols && !valorEncontrado)
        {
            cell = fila.getCell(i);
            valor = getValorCelda(cell, null);
            if(valor != null && !valor.equals(""))
            {
                valorEncontrado = true;
            }
            i++;
        }
        return !valorEncontrado;
    }
    
    public static String calcularLetraNif(int numero)
    {
        try
        {
            int posicion = numero % 23;
            return ""+ConstantesMeLanbide35.LETRAS_NIF.charAt(posicion);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    /**
         * Realiza la validacion si la cadena representa un CIF
         * 
         * @param strCadena
         *            la cadena a comprobar
         * @return true si la cadena representa un CIF del tipo indicado
         */
        public static boolean isCifValido(String cif) {

        boolean resultado = false;

        try {
            String vCif = cif.trim();
            
            //Estos tres CIF en principio son incorrectos, pero deben pasar la validacion
            
            if(vCif.equalsIgnoreCase("B48561003") || vCif.equalsIgnoreCase("K8903850") || vCif.equalsIgnoreCase("K16052710") || vCif.equalsIgnoreCase("5092637T") || vCif.equalsIgnoreCase("A20066781"))
            {
                resultado = true;
            }
            else
            {
                int suma = 0;
                int contador = 0;
                int temporal = 0;
                int codigoControl = 0;
                String cadenaTemporal = null;
                String valoresCif = "ABCDEFGHJKLMNPQRSUVW";
                String letraControlCIF = "0123456789";
                String letraSociedadNumerica = "KLMNPQRSW";
                String primeraLetra = null;
                String ultimaLetra = null;

                // Comprueba la longitud correcta del CIF.
                if (!(vCif.length() == 9))
                    return false;

                // Si encuentra algï¿½n caracter que no sea una letra o un nï¿½mero, el cif
                // no es valido.
                if (vCif.matches("[^A-Za-z0-9]"))
                    return false;

                // Convierte a mayï¿½sculas la cadena.
                vCif = vCif.toUpperCase();

                // Obtiene la primera letra (letra de la sociedad) y la ï¿½ltima letra del
                // CIF (letra de control).
                primeraLetra = vCif.substring(0, 1);

                // Obtiene la ï¿½ltima letra del CIF, para comprobar si es vï¿½lida.
                ultimaLetra = vCif.substring(8, 9);

                // Comprueba si la primera letra es vï¿½lida.
                if (valoresCif.indexOf(primeraLetra) < 0)
                    return false;

                // Obtiene el cï¿½digo de control.
                // Sumamos las cifras pares
                suma = suma + Integer.parseInt(vCif.substring(2, 3)) + Integer.parseInt(vCif.substring(4, 5))
                        + Integer.parseInt(vCif.substring(6, 7));

                // Ahora cada cifra impar la multiplicamos por dos y sumamos las cifras
                // del resultado.
                for (contador = 1; contador < 8; contador = contador + 2) {
                    // Multiplica por 2
                    temporal = (Integer.parseInt(vCif.substring(contador, contador + 1)) * 2);

                    // Suma los digitos.
                    // Diferencia si tiene una cifra, por ejemplo: 8 = 8
                    // o si tiene varias, por ejemplo: 16 -> 6 + 1 = 7
                    if (temporal < 10)
                        suma = suma + temporal;
                    else {
                        cadenaTemporal = String.valueOf(temporal);
                        suma = suma + (Integer.parseInt(cadenaTemporal.substring(0, 1)))
                                + (Integer.parseInt(cadenaTemporal.substring(1, 2)));
                    }
                }

                // Obtiene las unidades de la suma y se las resta a 10, para obtener el
                // dï¿½gito de control.
                codigoControl = ((10 - (suma % 10)) % 10);

                // Si la letra es K, L, M, N, P, Q ï¿½ S entonces al codigo de control le
                // suma 64 y
                // obtengo su ASCII para ver si coincide con la ultima letra del cif.
                if (letraSociedadNumerica.indexOf(primeraLetra) >= 0) {
                    byte[] ascii = new byte[1];

                    // Obtiene el cï¿½digo ASCII asociado, al sumar 64 al cï¿½digo de
                    // control.
                    if (codigoControl == 0)
                        codigoControl = 10;
                    codigoControl = codigoControl + 64;
                    ascii[0] = (Integer.valueOf(codigoControl)).byteValue();

                    // El ï¿½ltimo dï¿½gito tiene que coincidir con el dï¿½gito de control
                    // obtenido
                    resultado = (ultimaLetra.equals(new String(ascii)));
                } else {
                    // Para el resto de letras de comienzo de CIF el ï¿½ltimo dï¿½gito debe ser
                    // numï¿½rico,
                    // y coincidir con el cï¿½digo de control.
                    resultado = (codigoControl == letraControlCIF.indexOf(ultimaLetra));
                }
            }
        } catch (Exception e) {
            // Si ha habido algï¿½n error es porque hay algï¿½n parseo que tira bien.
            resultado = false;
        }
        return resultado;
    }
    
    
    public static Integer getEjercicioDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide35.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        }
        catch(Exception ex)
        {
            return null;
        }
    }
    
    public static void formatearFilaPreparadorSolicitudVOError(FilaPreparadorSolicitudVO fila)
    {
        fila.setC1Total(formatearMensajeColor(fila.getC1Total(), ConstantesMeLanbide35.ROJO));
        fila.setC1h(formatearMensajeColor(fila.getC1h(), ConstantesMeLanbide35.ROJO));
        fila.setC1m(formatearMensajeColor(fila.getC1m(), ConstantesMeLanbide35.ROJO));
        fila.setC2Total(formatearMensajeColor(fila.getC2Total(), ConstantesMeLanbide35.ROJO));
        fila.setC2h(formatearMensajeColor(fila.getC2h(), ConstantesMeLanbide35.ROJO));
        fila.setC2m(formatearMensajeColor(fila.getC2m(), ConstantesMeLanbide35.ROJO));
        fila.setC3Total(formatearMensajeColor(fila.getC3Total(), ConstantesMeLanbide35.ROJO));
        fila.setC3h(formatearMensajeColor(fila.getC3h(), ConstantesMeLanbide35.ROJO));
        fila.setC3m(formatearMensajeColor(fila.getC3m(), ConstantesMeLanbide35.ROJO));
        fila.setC4Total(formatearMensajeColor(fila.getC4Total(), ConstantesMeLanbide35.ROJO));
        fila.setC4h(formatearMensajeColor(fila.getC4h(), ConstantesMeLanbide35.ROJO));
        fila.setC4m(formatearMensajeColor(fila.getC4m(), ConstantesMeLanbide35.ROJO));
        fila.setCostesSalarialesSS(formatearMensajeColor(fila.getCostesSalarialesSS(), ConstantesMeLanbide35.ROJO));
        fila.setCostesSalarialesSSEca(formatearMensajeColor(fila.getCostesSalarialesSSEca(), ConstantesMeLanbide35.ROJO));
        fila.setCostesSalarialesSSJor(formatearMensajeColor(fila.getCostesSalarialesSSJor(), ConstantesMeLanbide35.ROJO));
        fila.setCostesSalarialesSSPorJor(formatearMensajeColor(fila.getCostesSalarialesSSPorJor(), ConstantesMeLanbide35.ROJO));
        fila.setFechaFin(formatearMensajeColor(fila.getFechaFin(), ConstantesMeLanbide35.ROJO));
        fila.setFechaInicio(formatearMensajeColor(fila.getFechaInicio(), ConstantesMeLanbide35.ROJO));
        fila.setHorasAnuales(formatearMensajeColor(fila.getHorasAnuales(), ConstantesMeLanbide35.ROJO));
        fila.setHorasContrato(formatearMensajeColor(fila.getHorasContrato(), ConstantesMeLanbide35.ROJO));
        fila.setHorasDedicacionECA(formatearMensajeColor(fila.getHorasDedicacionECA(), ConstantesMeLanbide35.ROJO));
        fila.setImporte(formatearMensajeColor(fila.getImporte(), ConstantesMeLanbide35.ROJO));
        fila.setInserciones(formatearMensajeColor(fila.getInserciones(), ConstantesMeLanbide35.ROJO));
        fila.setNif(formatearMensajeColor(fila.getNif(), ConstantesMeLanbide35.ROJO));
        fila.setNombreApel(formatearMensajeColor(fila.getNombreApel(), ConstantesMeLanbide35.ROJO));
        fila.setNumSegAnteriores(formatearMensajeColor(fila.getNumSegAnteriores(), ConstantesMeLanbide35.ROJO));
        fila.setSeguimientosInserciones(formatearMensajeColor(fila.getSeguimientosInserciones(), ConstantesMeLanbide35.ROJO));
    }
    
    public static String formatearMensajeColor(String mensaje, String color)
    {
       return "<font color=\""+color+"\">"+mensaje+"</font>";
    }
    
        
    /*public static String validarEcaJusPreparadoresVO(EcaJusPreparadoresVO prep, boolean soloFormato)
    {
        //Fecha de inicio anterior a fecha de fin
       if (!soloFormato){
        try
        {
            Date feInicio = prep.getFecIni();
            Date feFin = prep.getFecFin();
            if(feInicio != null && feFin != null && feInicio.after(feFin))
            {
                return ConstantesMeLanbide35.ERROR;
            }
        }
        catch(Exception ex)
        {
            return ConstantesMeLanbide35.ERROR;
        }
       }
        //TODO: Fecha de inicio o fin en el ano del expediente
        
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicaciÃ³n ECA
        try
        {
            Double hajc = prep.getHorasJC() != null ? prep.getHorasJC().doubleValue() : 0.0;
            Double hc = prep.getHorasCont() != null ? prep.getHorasCont().doubleValue() : 0.0;
            Double hdECA = prep.getHorasEca() != null ? prep.getHorasEca().doubleValue() : 0.0;
            if(hajc < hc)
            {
                return ConstantesMeLanbide35.ERROR;
            }
            else
            {
                if(hc < hdECA)
                {
                    return ConstantesMeLanbide35.ERROR;
                }
            }
        }
        catch(Exception ex)
        {
            return ConstantesMeLanbide35.ERROR;
        }
        
        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicaciÃ³n ECA
        try
        {
            Double cssJC = prep.getImpSSJC() != null ? prep.getImpSSJC().doubleValue() : 0.0;
            Double cssPorJor = prep.getImpSSJR() != null ? prep.getImpSSJR().doubleValue() : 0.0;
            Double cssECA = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : 0.0;

            if(cssJC < cssPorJor)
            {
                return ConstantesMeLanbide35.ERROR;
            }
            else
            {
                if(cssPorJor < cssECA)
                {
                    return ConstantesMeLanbide35.ERROR;
                }
            }
        }
        catch(Exception ex)
        {
            return ConstantesMeLanbide35.ERROR;
        }
        
        //Importe Seguimientos: si es mayor al 25% de los costes salariales + seg social ECA, limitar a dicho valor
        try
        {
            Double importe = prep.getImpSegAnt() != null ? prep.getImpSegAnt().doubleValue() : 0.0;
            Double cssECA = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : 0.0;
            Double por = (cssECA * 25) / 100;
            if(importe > por)
            {
                prep.setImpSegAnt(new BigDecimal(por.toString()));
            }
        }
        catch(Exception ex)
        {
            return ConstantesMeLanbide35.ERROR;
        }
        
        //Numero de inserciones, importes totales: Valores numÃ©ricos, sin decimales
        try
        {
            
            double num;
            long iPart;
            double fPart;
            
            if(prep.getInsImporte() != null)
            {
                num = prep.getInsImporte().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    return ConstantesMeLanbide35.ERROR;
                }
                else
                {
                    prep.setInsImporte(new BigDecimal(""+iPart));
                }
            }
            
        }
        catch(Exception ex)
        {
            return ConstantesMeLanbide35.ERROR;
        }
        
        //TODO: Los totales finales: Â¿Calculados o insertados?
        
        return ConstantesMeLanbide35.OK;
    }*/
            
    public static String obtenerNombreProceso(int codProceso, int codIdioma)
    {
        String desc = null;
        MeLanbide35I18n mm = MeLanbide35I18n.getInstance();
        try
        {
            switch(codProceso)
            {
                case ConstantesMeLanbide35.COD_PROC_RESOLUCION_PROV:
                    desc = mm.getMensaje(codIdioma, "proc.label.RPSE");
                    break;
                case ConstantesMeLanbide35.COD_PROC_DOC_RESOLUCION:
                    desc = mm.getMensaje(codIdioma, "proc.label.DRSE");
                    break;
                case ConstantesMeLanbide35.COD_PROC_CONSOLIDAR:
                    desc = mm.getMensaje(codIdioma, "proc.label.CSE");
                    break;
                case ConstantesMeLanbide35.COD_PROC_DESHACER_CONSOLIDACION:
                    desc = mm.getMensaje(codIdioma, "proc.label.DCSE");
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
    
    
    public static List<String> validarFilaPreparadorJustificacion(FilaPreparadorJustificacionVO fila, EcaJusPreparadoresVO origen, List<EcaJusPreparadoresVO> sustitutos, List<Integer> listaTipoContrato, EcaConfiguracionVO config, Integer ejercicio, int idioma, boolean soloFormato)
    {
        try
        {
            EcaJusPreparadoresVO prep = MeLanbide35MappingUtils.getInstance().fromFilaPrepJusVOToPrepJusVO(fila);
            return validarEcaJusPreparadoresVO(prep, origen, sustitutos, listaTipoContrato, config, ejercicio, idioma, soloFormato, true);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage() != null ? ex.getMessage() : "");
            return list;
        }
    }
    
    public static List<String> validarEcaJusPreparadoresVO(EcaJusPreparadoresVO prep, EcaJusPreparadoresVO origen, List<EcaJusPreparadoresVO> sustitutos, List<Integer> listaTipoContrato, EcaConfiguracionVO config, Integer ejercicio, int idioma, boolean soloFormato, boolean desdeFila)
    {
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        
        //validar nif
        if(prep.getNif() != null && !prep.getNif().equals(""))
        {
            if(validarPatronNif(prep.getNif()))
            {
                if(!soloFormato)
                {
                    try
                    {
                        String nif = prep.getNif();
                        String numero = nif.substring(0, 8);
                        String letra = nif.substring(8, nif.length());
                        String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                        if(letra != null && letraCalculada != null)
                        {
                            if(!letra.equalsIgnoreCase(letraCalculada))
                            {
                                mensaje = traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrectoLetra");
                                mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                    }
                    catch(Exception ex)
                    {
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            else
            {
                mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Fecha de inicio anterior a fecha de fin     
        if(!soloFormato)
        {
            try
            {
                Date feInicio = prep.getFecIni();
                Date feFin = prep.getFecFin();
                if(feInicio != null && feFin != null && feInicio.after(feFin))
                {
                    mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechaIniFechaFin");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    Calendar cal = null;
                    int ano = -1;
                    //Fecha de inicio y fin en el ano del expediente
                    if(feInicio != null)
                    {
                        cal = new GregorianCalendar();
                        cal.setTime(feInicio);
                        ano = cal.get(Calendar.YEAR);
                        if(ejercicio != null && ano != ejercicio)
                        {
                            mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechasAno"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }

                    if(feFin != null)
                    {
                        cal.setTime(feFin);
                        ano = cal.get(Calendar.YEAR);
                        if(ejercicio != null && ano != ejercicio)
                        {
                            mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechasAno"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //tipo contrato
        if(!soloFormato)
        {
            try
           {
               if(prep.getTipoContrato() != null && !prep.getTipoContrato().equals(""))
               {
                   Integer codtipocontrato = prep.getTipoContrato();
                   if (!listaTipoContrato.contains(codtipocontrato)) {                    
                       mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoContratoIncorrecto");
                       if(!errores.contains(mensaje))
                           errores.add(mensaje);

                   }
               }else {
                mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoContratoIncorrecto");
                       if(!errores.contains(mensaje))
                           errores.add(mensaje);
               }
           }catch(Exception ex)
           {
               mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionTipoContrato");
               if(!errores.contains(mensaje))
                   errores.add(mensaje);
           }
        }
         
        
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicacion ECA
        if(!soloFormato)
        {
            try
            {
                Double hajc = prep.getHorasJC() != null ? prep.getHorasJC().doubleValue() : 0.0;
                Double hc = prep.getHorasCont() != null ? prep.getHorasCont().doubleValue() : 0.0;
                Double hdECA = prep.getHorasEca() != null ? prep.getHorasEca().doubleValue() : 0.0;
                if(hajc < hc)
                {
                    mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasJCHorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(hc < hdECA)
                    {
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasContratoHorasEca");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionHoras");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
         
        
        //Horas Dedicación ECA >= 50% Horas Contrato
        /*if(!soloFormato)
        {
            try
            {
                Double hc = prep.getHorasCont() != null ? prep.getHorasCont().doubleValue() : 0.0;
                hc *= 0.5;
                Double hdECA = prep.getHorasEca() != null ? prep.getHorasEca().doubleValue() : 0.0;
                if(hdECA < hc)
                {
                    mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.horasDedicacionEca50HorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.horasDedicacionEca50HorasContrato");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }*/
        
        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicaciÃ³n ECA
        if(!soloFormato)
        {
            try
            {
                Double cssJC = prep.getImpSSJC() != null ? prep.getImpSSJC().doubleValue() : 0.0;
                Double cssPorJor = prep.getImpSSJR() != null ? prep.getImpSSJR().doubleValue() : 0.0;
                Double cssECA = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : 0.0;

                if(cssJC < cssPorJor)
                {
                    mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesJCCostesPJ");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(cssPorJor < cssECA)
                    {
                        mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesPJCostesECA");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionCostes");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        
        //Importe Seguimientos: si es mayor al 25% de los costes salariales + seg social ECA, limitar a dicho valor
        /*try
        {
            Double importe = prep.getImpSegAnt() != null ? prep.getImpSegAnt().doubleValue() : 0.0;
            Double cssECA = prep.getImpSSECA() != null ? prep.getImpSSECA().doubleValue() : 0.0;
            double poMax = config.getPoMaxSeguimientos() != null ? config.getPoMaxSeguimientos().doubleValue() : 1.0;
            Double por = (cssECA * poMax);
            if(importe < por)
            {
                //Hay que comprobar que corresponda a numSeg * config.getImSeguimiento()
                if(prep.getImpSegAnt() != null)
                {
                    BigDecimal imSeg = config.getImSeguimiento();
                    if(imSeg != null)
                    {
                        Integer segAnt = prep.getSegAnt();
                        if(segAnt != null)
                        {
                            double res = imSeg.doubleValue() * segAnt;
                            if(res != prep.getImpSegAnt().doubleValue())
                            {
                                mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.importeSegAntIncorrecto"), ejercicio);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                    }
                }
            }
            else
            {
                prep.setImpSegAnt(new BigDecimal(por.toString()));
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionImporteSegAnt"), ejercicio);
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }*/
        
        
        //Numero de inserciones, importes totales: Valores numÃ©ricos, con decimales
            
        double num;
        long iPart;
        double fPart;
        
        try
        {
            if(prep.getInsImporte() != null)
            {
                num = prep.getInsImporte().doubleValue();
               
                prep.setInsImporte(new BigDecimal(""+num));
                
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsImporte");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Fechas sustituto-origen solapadas
        if(!soloFormato && desdeFila)
        {
            Date dFinOrigen = null;
            long timedFinorigen = 0;
            Date dIni = null;
            long n2 = 0;
            long resultFcontrato = 0;
            if(origen != null && origen.getFecFin() != null && prep.getFecIni() != null)
            {
                dFinOrigen = origen.getFecFin();
                timedFinorigen = dFinOrigen.getTime();
                
                dIni = prep.getFecIni();
                n2 = dIni.getTime();
                
                resultFcontrato = n2 - timedFinorigen ;
                if (resultFcontrato <= 0){
                    mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaIniAnteriorAOrigen");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            
            if(sustitutos != null && sustitutos.size() > 0)
            {
                EcaJusPreparadoresVO act = null;
                int posAct = 0;
                boolean encontrado = false;
                while(posAct < sustitutos.size() && !encontrado)
                {
                    act = sustitutos.get(posAct);
                    if(act.getFecIni() != null && prep.getFecFin() != null)
                    {
                        dFinOrigen = prep.getFecFin();
                        timedFinorigen = dFinOrigen.getTime();

                        dIni = act.getFecIni();
                        n2 = dIni.getTime();

                        resultFcontrato = n2 - timedFinorigen ;

                        if (resultFcontrato <= 0){
                            encontrado = true;
                            mensaje = FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaIniAnteriorAOrigen");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                    posAct++;
                }
            }
        }
        
        return errores;
    }
    
    public static List<String> validarFilaSeguimientoPrep(String numexp, FilaSegPreparadoresVO fila, EcaConfiguracionVO config, int idioma, List<Integer> listaTipoDisc, List<Integer> listaGravedad, List<Integer> listaTipoContrato)
    {
        try
        {
            EcaSegPreparadoresVO seg = MeLanbide35MappingUtils.getInstance().fromFilaSegPrepVOToSegPrepVO(fila);
            return validarEcaSegPreparadoresVO(numexp,seg, config, idioma, listaTipoDisc, listaGravedad, listaTipoContrato, false,true);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage());
            return list;
        }
    }
    
    public static List<String> validarEcaSegPreparadoresVO(String numexp, EcaSegPreparadoresVO seg, EcaConfiguracionVO config, int idioma, List<Integer> listaTipoDisc, List<Integer> listaGravedad, List<Integer> listaTipoContrato, boolean soloFormato, boolean errorColectivo)
    {
        
        Integer ejercicio = MeLanbide35Utils.getEjercicioDeExpediente(numexp);
    
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        
               
        //validar nif
        if(seg.getNif() != null && !seg.getNif().equals(""))
        {
            if(validarPatronNif(seg.getNif()))
            {
                if (!soloFormato){
                try
                {
                    String nif = seg.getNif();
                    String numero="";             
                    //NIE
                    String primercaracter = nif.toUpperCase().substring(0, 1);
                    
                    if ( primercaracter.equals("X") || primercaracter.equals("Y") ||  primercaracter.equals("Z")  ){
                        //NIE
                        numero = nif.substring(1, 8);  
                        if (primercaracter.equals("X")) numero= '0'+numero;
                        if (primercaracter.equals("Y")) numero= '1'+numero;
                        if (primercaracter.equals("Z")) numero= '2'+numero;
                    }else {
                         numero = nif.substring(0, 8);                         
                    }
                    String letra=nif.substring(8, nif.length());
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoLetra");
                            mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                }
            }
            else
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }
        
        //validar nif preparador
        if(seg.getNifPreparador() != null && !seg.getNifPreparador().equals(""))
        {
            if(validarPatronNif(seg.getNifPreparador()))
            {
                if (!soloFormato){
                try
                {
                    String nif = seg.getNifPreparador();
                    String numero = nif.substring(0, 8);
                    String letra = nif.substring(8, nif.length());
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoLetraPrep");
                            mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoPrep");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                }
            }
            else
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoPrep");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //fecha de seguimiento en aÃ±o del expediente o posterior (SOLO INSERCIONES)
        /*if (seg.getTipo()==0){
        try
        {
            Date feSeg = seg.getFecSeguimiento();           
            if (!soloFormato){
                if(feSeg != null )
                {
                    Calendar cal = null;
                    int ano = -1;
                    cal = new GregorianCalendar();
                    cal.setTime(feSeg);
                    ano = cal.get(Calendar.YEAR);
                    if(ano < ejercicio){
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaSgto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }else{
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaSgtoObligatorio");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                }
            }
        }catch(Exception ex)
        {
            mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        }*/
        
        //Fecha de inicio anterior a fecha de fin  de contrato   
        try
        {
            Date feInicio = seg.getFecIni();
            Date feFin = seg.getFecFin();
            if (!soloFormato){
                if(feInicio != null && feFin != null && feInicio.after(feFin))
                {
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaIniFechaFin");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    Calendar cal = null;
                    int ano = -1;
                    //Fecha de inicio de contrato en  aÃ±o anterior al expediente
                    if(feInicio != null)
                    {
                        cal = new GregorianCalendar();
                        cal.setTime(feInicio);
                        ano = cal.get(Calendar.YEAR);
                        if (seg.getTipo()==0){
                            if(ano >= ejercicio)
                            {
                                mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechasAnoIni"), ejercicio);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                        else {
                            if(ano != ejercicio)
                            {
                                mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechasAnoIni"), ejercicio);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                    }

                    /*if(feFin != null)
                    {
                        cal.setTime(feFin);
                        ano = cal.get(Calendar.YEAR);
                        if(ano < ejercicio)
                        {
                            mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechasAnoFin"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }*/
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //FECHA NACIMIENTO
         try
        {
            Date feNacimiento = seg.getFecNacimiento();
            if (!soloFormato){
                if(feNacimiento == null ){
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaNacimientoOblig");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }   
         
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicaciÃ³n ECA
        try
        {
            Double horas = seg.getHorasCont() != null ? seg.getHorasCont().doubleValue() : 0.0;
            if (!soloFormato){
                if (seg.getHorasCont() == null){               
                 mensaje = FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.horasOblig");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                }
            }
            
        }
        catch(Exception ex)
        {
            mensaje = FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionHoras");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Porcentaje jornada
        try
        {
            if (!soloFormato)
            {    
                if (seg.getPorcJornada() == null)
                {
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_PORCJORN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.porcentajeOblig");
                    if(!errores.contains(mensaje))
                    {
                        errores.add(mensaje);
                    }
                }
                else
                {
                    Double porc = seg.getPorcJornada() != null ? seg.getPorcJornada().doubleValue() : 0.0;
                    if (porc>100)
                    {
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_PORCJORN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.porcentajeFueraRango");
                        if(!errores.contains(mensaje))
                        {
                            errores.add(mensaje);
                        }
                    }
                    else if(seg.getTipo() != null && seg.getTipo().equals(1))
                    {
                        if(porc < 35)
                        {
                            log.info("Jornada menor de 35% en una insercion, comprobamos si esta validad para subvencionar - Boton Fin Contrato/Despido" + seg.getFinContratoDespido());
                            if (seg.getFinContratoDespido() != null && seg.getFinContratoDespido().equals(1)) {
                                log.info("Jornada menor de 35% en una insercion, pero se ha validado la finalizacion del contrato : " + seg.getFinContratoDespido());
                            } else {
                                log.info("Jornada menor de 35% en una insercion, NO validado la finalizacion del contrato - MARCAMOS COMO ERROR : " + seg.getFinContratoDespido());
                                mensaje = FilaSegPreparadoresVO.POS_CAMPO_PORCJORN + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.porcentajeFueraRango");
                                if (!errores.contains(mensaje)) {
                                    errores.add(mensaje);
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaSegPreparadoresVO.POS_CAMPO_PORCJORN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionPorcentaje");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Tipo discapacidad
        if(seg.getTipoDiscapacidad() != null && !seg.getTipoDiscapacidad().equals(""))
        {
            try
            {
                Integer tipodisc = seg.getTipoDiscapacidad();
                if (!soloFormato){
                    if (!listaTipoDisc.contains(tipodisc)) {                    
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoDiscIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionTipoDisc");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
             mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoDiscIncorrecto");
             if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Gravedad discapacidad
        if(seg.getGravedad() != null && !seg.getGravedad().equals(""))
        {
            try
            {
                Integer codgravedad = seg.getGravedad();
                if (!soloFormato){
                    if (!listaTipoDisc.contains(codgravedad)) {                    
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.gravedadIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionGravedad");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
            mensaje = FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.gravedadIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
        }
        
        //Tipo contrato
        if(seg.getTipoContrato() != null && !seg.getTipoContrato().equals(""))
        {
            try
            {
                Integer codtipocontrato = seg.getTipoContrato();
                if (!soloFormato){           
                    if (!listaTipoContrato.contains(codtipocontrato)) {                    
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoContratoIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionTipoContrato");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
             mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoContratoIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
        }
        
        
        //sexo
        if(seg.getSexo() != null && !seg.getSexo().equals(""))
        {
            try
            {
                Integer sexo = seg.getSexo();
                if (!soloFormato){
                    if ((sexo!=0)&& (sexo!=1)) {                    
                        mensaje = FilaSegPreparadoresVO.POS_CAMPO_SEXO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.sexoIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_SEXO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionSexo");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
            
        }
        
        //empresa
        if (!soloFormato){
            if(seg.getEmpresa() == null || seg.getEmpresa().equals(""))
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_EMPRESA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.empresaOblig");
                if(!errores.contains(mensaje))
                errores.add(mensaje);                   
            }
        }
        
        
        //colectivos
        if (!soloFormato && errorColectivo){
            String[] colectivos = ConstantesMeLanbide35.COMBINACIONES_COLECTIVO;
        
            boolean encontrado = false;
            String combColectivo = ""+seg.getTipoDiscapacidad()+seg.getGravedad();
            for(int i = 0; i < colectivos.length; i++){
                if(combColectivo.equals(colectivos[i])){
                    encontrado = true;
                }
            }
            if(!encontrado){
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "msg.justificacion.preparadores.colectivoNoDefinido");
                    if(!errores.contains(mensaje))
                    errores.add(mensaje);   
            } 
            
            //if (seg.getFecIni())
            //fechas contrato en colectivo
            try { 
                log.info("--" + seg.getNif() + " " + seg.getNombre());
                Calendar cal1 = Calendar.getInstance(); 
                Calendar cal2 = Calendar.getInstance(); 
                Calendar cal1Plus6Months = Calendar.getInstance(); 
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                
                if(seg.getFecIni()!=null)
                cal1.setTime(seg.getFecIni()); 
                else
                    log.info("seg.getFecIni() Viene a null ");
                    
                if(seg.getFecFin()!=null)
                cal2.setTime(seg.getFecFin()); 
                else
                    log.info("seg.getFecFin() Viene a null ");
                
                // Comparamos teniendo en cuenta los dias tambien sumamos 6 Meses a la fecha incial y comparamos la fehca fin si anterior
                log.info("Fechas recibidas : getFecIni - getFecFin :" + (seg.getFecIni()!=null?format.format(seg.getFecIni()):"") +" - "+(seg.getFecFin()!=null?format.format(seg.getFecFin()):""));
                cal1Plus6Months=cal1;
                boolean contratoMas6Meses = true;
                if(cal1Plus6Months!=null){
                    cal1Plus6Months.add(Calendar.MONTH,6);
                    log.info("Fechas recibidas : getFecIni + 6 Meses " + (format.format(cal1Plus6Months.getTime())));
                    // Para hacer inclusivo el ultima dia del contrato restamos un dia de la fecha fin mas 6 meses 
                    // Ejemplo 01/01/2017 a 30/06/2017 Son 6 meses pero la validacion por dias dice que no, porque 01/07/2017 es la fecha ini + 6 meses y es superior a fecha fin 30/06/2017
                    cal1Plus6Months.add(Calendar.DAY_OF_MONTH, -1);
                    log.info("Fechas recibidas : getFecIni + 6 Meses - 1 Dia : " + (format.format(cal1Plus6Months.getTime())));
                }
                else
                    log.info("cal1Plus6Months - getFecIni + 6 Meses : Es null");
                
                // Si la fecha de fin es null, no se valida se asume que el contrato esta vigente
                if(seg.getFecFin()!=null && cal2.before(cal1Plus6Months))
                    contratoMas6Meses=false;
                log.info("contratoMas6Meses " + contratoMas6Meses);
                
                int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR); 
                int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH); 
                log.info("Formula Vieja : yearDiff -  monthDiff " + yearDiff + " - " + monthDiff);
                //if (monthDiff < 6){
                if (!contratoMas6Meses){
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "msg.justificacion.preparadores.colectivoNoDefinidoFechas");
                    if(!errores.contains(mensaje))
                    errores.add(mensaje);  
                }
            } 
            catch (Exception pe) 
            { 
                log.error("Error al validar Seguimientos Preparadores : " + pe.getMessage(), pe);
            } 
            
            
        }
        
        return errores;
    }
    
    
    public static List<String> validarFilaInsercionPrep(String numexp, FilaInsPreparadoresVO fila, EcaConfiguracionVO config, int idioma, List<Integer> listaTipoDisc, List<Integer> listaGravedad, List<Integer> listaTipoContrato)
    {
        try
        {
            EcaInsPreparadoresVO seg = MeLanbide35MappingUtils.getInstance().fromFilaInsPrepVOToInsPrepVO(fila);
            return validarEcaInsPreparadoresVO(numexp,seg, config, idioma, listaTipoDisc, listaGravedad, listaTipoContrato, false,true);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage());
            return list;
        }
    }
    
    public static List<String> validarEcaInsPreparadoresVO(String numexp, EcaInsPreparadoresVO seg, EcaConfiguracionVO config, int idioma, List<Integer> listaTipoDisc, List<Integer> listaGravedad, List<Integer> listaTipoContrato, boolean soloFormato, boolean errorColectivo)
    {
        Integer ejercicio = MeLanbide35Utils.getEjercicioDeExpediente(numexp);
    
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        
               
        //validar nif
        if(seg.getNif() != null && !seg.getNif().equals(""))
        {
            if(validarPatronNif(seg.getNif()))
            {
                if (!soloFormato){
                try
                {
                    String nif = seg.getNif();                    
                     String numero="";             
                    //NIE
                    String primercaracter = nif.toUpperCase().substring(0, 1);                    
                    if ( primercaracter.equals("X") || primercaracter.equals("Y") ||  primercaracter.equals("Z")  ){
                        //NIE
                        numero = nif.substring(1, 8);   
                        if (primercaracter.equals("X")) numero= '0'+numero;
                        if (primercaracter.equals("Y")) numero= '1'+numero;
                        if (primercaracter.equals("Z")) numero= '2'+numero;
                    }else {
                         numero = nif.substring(0, 8);                         
                    }
                    String letra=nif.substring(8, nif.length());                    
                    
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoLetra");
                            mensaje = FilaInsPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaInsPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                }
            }
            else
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }
        
        //validar nif preparador
        if(seg.getNifPreparador() != null && !seg.getNifPreparador().equals(""))
        {
            if(validarPatronNif(seg.getNifPreparador()))
            {
                if (!soloFormato){
                try
                {
                    String nif = seg.getNifPreparador();
                    String numero = nif.substring(0, 8);
                    String letra = nif.substring(8, nif.length());
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoLetraPrep");
                            mensaje = FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoPrep");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                }
            }
            else
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoPrep");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        
        
        //Fecha de inicio anterior a fecha de fin  de contrato   
        try
        {
            Date feInicio = seg.getFecIni();
            Date feFin = seg.getFecFin();
            if (!soloFormato){
                if(feInicio != null && feFin != null && feInicio.after(feFin))
                {
                    mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaIniFechaFin");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    Calendar cal = null;
                    int ano = -1;
                    //Fecha de inicio de contrato en  aÃ±o de expediente
                    if(feInicio != null)
                    {
                        cal = new GregorianCalendar();
                        cal.setTime(feInicio);
                        ano = cal.get(Calendar.YEAR);
                        if (seg.getTipo()==0){
                            if(ano >= ejercicio)
                            {
                                mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechasAnoIni"), ejercicio);
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                        else {
                            if(ano != ejercicio)
                            {
                                mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "msg.justificacion.preparadores.fechaIniAnoIncorrectoIns");
                                if(!errores.contains(mensaje))
                                    errores.add(mensaje);
                            }
                        }
                    }

                    /*if(feFin != null)
                    {
                        cal.setTime(feFin);
                        ano = cal.get(Calendar.YEAR);
                        if(ano < ejercicio)
                        {
                            mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechasAnoFin"), ejercicio);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }*/
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //FECHA NACIMIENTO
         try
        {
            Date feNacimiento = seg.getFecNacimiento();
            if (!soloFormato){
                if(feNacimiento == null ){
                    mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.fechaNacimientoOblig");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }   
         
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicaciÃ³n ECA
        try
        {
            Double horas = seg.getHorasCont() != null ? seg.getHorasCont().doubleValue() : 0.0;
            if (!soloFormato){
                if (seg.getHorasCont() == null){               
                 mensaje = FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.horasOblig");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                }
            }
            
        }
        catch(Exception ex)
        {
            mensaje = FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionHoras");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Porcentaje jornada
        try
        {
            if (!soloFormato)
            {  
                if (seg.getPorcJornada() == null)
                {               
                    mensaje = FilaInsPreparadoresVO.POS_CAMPO_PORCJORN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.porcentajeOblig");
                    if(!errores.contains(mensaje))
                    {
                        errores.add(mensaje);
                    }
                }
                else
                {
                    Double porc = seg.getPorcJornada().doubleValue();
                    if (porc > 100)
                    {
                        mensaje = FilaInsPreparadoresVO.POS_CAMPO_PORCJORN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.porcentajeFueraRango");
                        if(!errores.contains(mensaje))
                        {
                            errores.add(mensaje);
                        }
                    }
                    else if(porc < 35)
                    {
                        log.info("Jornada menor de 35% en una insercion, comprobamos si esta validad para subvencionar - Boton Fin Contrato/Despido" + seg.getFinContratoDespido());
                        if (seg.getFinContratoDespido() != null && seg.getFinContratoDespido().equals(1)) {
                            log.info("Jornada menor de 35% en una insercion, pero se ha validado la finalizacion del contrato : " + seg.getFinContratoDespido());
                        } else {
                            log.info("Jornada menor de 35% en una insercion, NO validado la finalizacion del contrato - MARCAMOS COMO ERROR : " + seg.getFinContratoDespido());
                            mensaje = FilaInsPreparadoresVO.POS_CAMPO_PORCJORN + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.porcentajeFueraRango");
                            if (!errores.contains(mensaje)) {
                                errores.add(mensaje);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaInsPreparadoresVO.POS_CAMPO_PORCJORN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionPorcentaje");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Tipo discapacidad
        if(seg.getTipoDiscapacidad() != null && !seg.getTipoDiscapacidad().equals(""))
        {
            try
            {
                Integer tipodisc = seg.getTipoDiscapacidad();
                if (!soloFormato){
                    if (!listaTipoDisc.contains(tipodisc)) {                    
                        mensaje = FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoDiscIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionTipoDisc");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
             mensaje = FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoDiscIncorrecto");
             if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //Gravedad discapacidad
        if(seg.getGravedad() != null && !seg.getGravedad().equals(""))
        {
            try
            {
                Integer codgravedad = seg.getGravedad();
                if (!soloFormato){
                    if (!listaTipoDisc.contains(codgravedad)) {                    
                        mensaje = FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.gravedadIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionGravedad");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
            mensaje = FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.gravedadIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
        }
        
        //Tipo contrato
        if(seg.getTipoContrato() != null && !seg.getTipoContrato().equals(""))
        {
            try
            {
                Integer codtipocontrato = seg.getTipoContrato();
                if (!soloFormato){           
                    if (!listaTipoContrato.contains(codtipocontrato)) {                    
                        mensaje = FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoContratoIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionTipoContrato");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
             mensaje = FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.tipoContratoIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
        }
        
        
        //sexo
        if(seg.getSexo() != null && !seg.getSexo().equals(""))
        {
            try
            {
                Integer sexo = seg.getSexo();
                if (!soloFormato){
                    if ((sexo!=0)&& (sexo!=1)) {                    
                        mensaje = FilaInsPreparadoresVO.POS_CAMPO_SEXO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.sexoIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_SEXO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.errorValidacionSexo");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
            
        }
        
        //empresa
        if (!soloFormato){
            if(seg.getEmpresa() == null || seg.getEmpresa().equals(""))
            {
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_EMPRESA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.empresaOblig");
                if(!errores.contains(mensaje))
                errores.add(mensaje);                   
            }
        }
        
                //colectivos
        if (!soloFormato && errorColectivo){
            String[] colectivos = ConstantesMeLanbide35.COMBINACIONES_COLECTIVO;
        
            boolean encontrado = false;
            String combColectivo = ""+seg.getTipoDiscapacidad()+seg.getGravedad();
            for(int i = 0; i < colectivos.length; i++){
                if(combColectivo.equals(colectivos[i])){
                    encontrado = true;
                }
            }
            if(!encontrado){
                mensaje = FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "msg.justificacion.preparadores.colectivoNoDefinido");
                    if(!errores.contains(mensaje))
                    errores.add(mensaje);   
            } 
            
            //if (seg.getFecIni())
            //fechas contrato en colectivo
            try { 
                Calendar cal1 = Calendar.getInstance(); 
                Calendar cal2 = Calendar.getInstance(); 
                
                cal1.setTime(seg.getFecIni()); 
                cal2.setTime(seg.getFecFin()); 
                
                int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR); 
                int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH); 
                
                if (monthDiff < 6){
                    mensaje = FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "msg.justificacion.preparadores.colectivoNoDefinidoFechas");
                    if(!errores.contains(mensaje))
                    errores.add(mensaje);  
                }
            } 
            catch (Exception pe) 
            { 
            pe.printStackTrace(); 
            } 
            
            
        }
        
        
        return errores;
    }
    
    public static List<String> validarFilaProspectorJustificacion(FilaProspectorJustificacionVO fila, EcaJusProspectoresVO origen, List<EcaJusProspectoresVO> sustitutos, EcaConfiguracionVO config, Integer ejercicio, int idioma)
    {
        try
        {
            EcaJusProspectoresVO pros = MeLanbide35MappingUtils.getInstance().fromFilaProsJusVOToProsJusVO(fila);
            return validarEcaJusProspectoresVO(pros, origen, sustitutos, config, ejercicio, idioma, false, true);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage());
            return list;
        }
    }
    
    public static List<String> validarEcaJusProspectoresVO(EcaJusProspectoresVO pros, EcaJusProspectoresVO origen, List<EcaJusProspectoresVO> sustitutos, EcaConfiguracionVO config, Integer ejercicio, int idioma, boolean soloFormato, boolean desdeFila)
    {
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        
        //validar nif
        if(pros.getNif() != null && !pros.getNif().equals(""))
        {
            if(validarPatronNif(pros.getNif()))
            {
                if(!soloFormato)
                {
                try
                {
                    String nif = pros.getNif();
                    String numero = nif.substring(0, 8);
                    String letra = nif.substring(8, nif.length());
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrectoLetra");
                            mensaje = FilaProspectorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                }
            }
            else
            {
                mensaje = FilaProspectorJustificacionVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //Fecha de inicio anterior a fecha de fin        
        if(!soloFormato)
        {
        try
        {
            Date feInicio = pros.getFecIni();
            Date feFin = pros.getFecFin();
            if(feInicio != null && feFin != null && feInicio.after(feFin))
            {
                mensaje = FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechaIniFechaFin");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
            else
            {
                Calendar cal = null;
                int ano = -1;
                //Fecha de inicio y fin en el ano del expediente
                if(feInicio != null)
                {
                    cal = new GregorianCalendar();
                    cal.setTime(feInicio);
                    ano = cal.get(Calendar.YEAR);
                    if(ejercicio != null && ano != ejercicio)
                    {
                        mensaje = FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechasAno"), ejercicio);
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }

                if(feFin != null)
                {
                    cal.setTime(feFin);
                    ano = cal.get(Calendar.YEAR);
                    if(ejercicio != null && ano != ejercicio)
                    {
                        mensaje = FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.fechasAno"), ejercicio);
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        }
        
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicacion ECA
        if(!soloFormato)
        {
            try
            {
                Double hajc = pros.getHorasJC() != null ? pros.getHorasJC().doubleValue() : 0.0;
                Double hc = pros.getHorasCont() != null ? pros.getHorasCont().doubleValue() : 0.0;
                Double hdECA = pros.getHorasEca() != null ? pros.getHorasEca().doubleValue() : 0.0;
                if(hajc < hc)
                {
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasJCHorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(hc < hdECA)
                    {
                        mensaje = FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.horasContratoHorasEca");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionHoras");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
         
        
        //Horas Dedicación ECA >= 50% Horas Contrato
        // 2018/Jun Se cambia validacion. Tener en cuenta la suma de los prospectores con sustitutos y comparar contra JC Anual. 
        if(!soloFormato)
        {
            try
            {
                Double hajc = pros.getHorasJC() != null ? pros.getHorasJC().doubleValue() : 0.0;
                Double hajcMinimo = hajc * 0.5;
                Double totalhorasDECA = 0.0; // Debe incluirse aqui la suma de las horas eca de los prospectores y sus sustitutos.
                totalhorasDECA = pros.getHorasEca() != null ? pros.getHorasEca().doubleValue() : 0.0;
                if(origen!=null && origen.getHorasEca()!=null)
                    totalhorasDECA += origen.getHorasEca().doubleValue();
                for (EcaJusProspectoresVO sustituto : sustitutos) {
                    totalhorasDECA += sustituto.getHorasEca() != null ? sustituto.getHorasEca().doubleValue() : 0.0;
                }
                if (totalhorasDECA < hajcMinimo) {
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA + ConstantesMeLanbide35.BARRA_SEPARADORA + traductor.getMensaje(idioma, "validaciones.justificacion.prospectores.horasDedicacionEca50HorasJCAnual");
                    if (!errores.contains(mensaje)) {
                        errores.add(mensaje);
                    }
                }
                /*
                Double hc = pros.getHorasCont() != null ? pros.getHorasCont().doubleValue() : 0.0;
                hc *= 0.5;
                Double hdECA = pros.getHorasEca() != null ? pros.getHorasEca().doubleValue() : 0.0;
                if(hdECA < hc)
                {
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.prospectores.horasDedicacionEca50HorasContrato");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                */
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.prospectores.horasDedicacionEca50HorasContrato");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicaciÃ³n ECA
        if(!soloFormato)
        {
            try
            {
                Double cssJC = pros.getImpSSJC() != null ? pros.getImpSSJC().doubleValue() : 0.0;
                Double cssPorJor = pros.getImpSSJR() != null ? pros.getImpSSJR().doubleValue() : 0.0;
                Double cssECA = pros.getImpSSECA() != null ? pros.getImpSSECA().doubleValue() : 0.0;

                if(cssJC < cssPorJor)
                {
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesJCCostesPJ");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    if(cssPorJor < cssECA)
                    {
                        mensaje = FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.costesPJCostesECA");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);
                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionCostes");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }
        
        //IMPORTE de visitas: Valores numÃ©ricos, sin decimales            
       /* double num;
        long iPart;
        double fPart;
        
        try
        {
            if(pros.getImpVisitas() != null)
            {
                num = pros.getImpVisitas().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.insImporteConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                else
                {
                    pros.setImpVisitas(new BigDecimal(""+iPart));
                }
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionInsImporte");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        */
        
        //Fechas sustituto-origen solapadas
        if(!soloFormato && desdeFila)
        {
            Date dFinOrigen = null;
            long timedFinorigen = 0;
            Date dIni = null;
            long n2 = 0;
            long resultFcontrato = 0;
            if(origen != null && origen.getFecFin() != null && pros.getFecIni() != null)
            {
                dFinOrigen = origen.getFecFin();
                timedFinorigen = dFinOrigen.getTime();
                
                dIni = pros.getFecIni();
                n2 = dIni.getTime();
                
                resultFcontrato = n2 - timedFinorigen ;
                if (resultFcontrato <= 0){
                    mensaje = FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.prospectores.fechaIniAnteriorAOrigen");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            
            if(sustitutos != null && sustitutos.size() > 0)
            {
                EcaJusProspectoresVO act = null;
                int posAct = 0;
                boolean encontrado = false;
                while(posAct < sustitutos.size() && !encontrado)
                {
                    act = sustitutos.get(posAct);
                    if(pros.getFecFin() != null && act.getFecIni() != null)
                    {
                        dFinOrigen = pros.getFecFin();
                        timedFinorigen = dFinOrigen.getTime();

                        dIni = act.getFecIni();
                        n2 = dIni.getTime();

                        resultFcontrato = n2 - timedFinorigen ;

                        if (resultFcontrato <= 0){
                            encontrado = true;
                            mensaje = FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO+ConstantesMeLanbide35.SEPARADOR_VALORES_CONF+FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.prospectores.fechaIniAnteriorAOrigen");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                    posAct++;
                }
            }
        }
        return errores;
    }
    
    
    public static int calcularEdad(Date fecNacimiento)
    {
        try
        {
            Calendar calHoy = new GregorianCalendar();
            calHoy.setTime(new Date());
            Calendar calNac = new GregorianCalendar();
            calNac.setTime(fecNacimiento);
            int anoHoy = calHoy.get(Calendar.YEAR);
            int anoNac = calNac.get(Calendar.YEAR);
            int diaHoy = calHoy.get(Calendar.DAY_OF_YEAR);
            int diaNac = calNac.get(Calendar.DAY_OF_YEAR);
            int anos = anoHoy - anoNac;
            if(diaHoy < diaNac)
            {
                anos -= 1;
            }
            return anos;
        }
        catch(Exception ex)
        {
            return -1;
        }
    }
    
    
    public static List<String> validarFilaVisitaPros(String numexp, FilaVisProspectoresVO fila, List<Integer> listaSector, List<Integer> listaProvincia, List<Integer> listaCumple, List<Integer> listaResultado, EcaConfiguracionVO config, int idioma)
    {
        try
        {
            EcaVisProspectoresVO vis = MeLanbide35MappingUtils.getInstance().fromFilaVisProsVOToVisProsVO(fila);
            return validarEcaVisProspectoresVO(numexp,vis, listaSector, listaProvincia, listaCumple, listaResultado, config, idioma, false);
        }
        catch(Exception ex)
        {
            List<String> list = new ArrayList<String>();
            list.add(ex.getMessage());
            return list;
        }
    }
    
    public static List<String> validarEcaVisProspectoresVO(String numexp, EcaVisProspectoresVO vis, List<Integer> listaSector, List<Integer> listaProvincia, List<Integer> listaCumple, List<Integer> listaResultado, EcaConfiguracionVO config, int idioma,  boolean soloFormato)
    {
        if(vis.getEmpresa() != null && vis.getEmpresa().equalsIgnoreCase("TALLERES GALSET"))
            System.out.println();
        Integer ejercicio = MeLanbide35Utils.getEjercicioDeExpediente(numexp);
    
        List<String> errores = new ArrayList<String>();
        MeLanbide35I18n traductor = MeLanbide35I18n.getInstance();
        String mensaje = "";
        
        //validar cif
        if(vis.getCif() != null && !vis.getCif().equals(""))
        {
            if (!soloFormato){
                if (!isCifValido(vis.getCif())){
                    if(validarPatronNif(vis.getCif()))
                    {
                        try
                        {
                            String nif = vis.getCif();
                            String numero="";             
                            //NIE
                            String primercaracter = nif.toUpperCase().substring(0, 1);

                            if ( primercaracter.equals("X") || primercaracter.equals("Y") ||  primercaracter.equals("Z")  ){
                                //NIE
                                numero = nif.substring(1, 8);  
                                if (primercaracter.equals("X")) numero= '0'+numero;
                                if (primercaracter.equals("Y")) numero= '1'+numero;
                                if (primercaracter.equals("Z")) numero= '2'+numero;
                            }else {
                                 numero = nif.substring(0, 8);                         
                            }
                            String letra=nif.substring(8, nif.length());
                            String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                            if(letra != null && letraCalculada != null)
                            {
                                if(!letra.equalsIgnoreCase(letraCalculada))
                                {
                                    
                                    mensaje = FilaVisProspectoresVO.POS_CAMPO_CIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.cifIncorrecto");
                                    if(!errores.contains(mensaje))
                                        errores.add(mensaje);
                                }
                            }
                        }
                        catch(Exception ex)
                        {
                            mensaje = FilaVisProspectoresVO.POS_CAMPO_CIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.cifIncorrecto");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                    else
                    {
                        mensaje = FilaVisProspectoresVO.POS_CAMPO_CIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.cifIncorrecto");
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                    }
                }
            }
        }
        
       /* //validar nif
        if(seg.getNif() != null && !seg.getNif().equals(""))
        {
            if(validarPatronNif(seg.getNif()))
            {
                try
                {
                    String nif = seg.getNif();
                    String numero = nif.substring(0, 8);
                    String letra = nif.substring(8, nif.length());
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrectoLetra");
                            mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            else
            {
                mensaje = FilaSegPreparadoresVO.POS_CAMPO_NIF+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.nifIncorrecto");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }*/
        
        //validar nif preparador
        if(vis.getNifProspector() != null && !vis.getNifProspector().equals(""))
        {
            if(validarPatronNif(vis.getNifProspector()))
            {
                try
                {
                    String nif = vis.getNifProspector();
                    String numero = nif.substring(0, 8);
                    String letra = nif.substring(8, nif.length());
                    String letraCalculada = calcularLetraNif(Integer.parseInt(numero));
                    if(letra != null && letraCalculada != null)
                    {
                        if(!letra.equalsIgnoreCase(letraCalculada))
                        {
                            mensaje = traductor.getMensaje(idioma, "validaciones.justificacion.visita.nifIncorrectoLetraPros");
                            mensaje = FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR+ConstantesMeLanbide35.BARRA_SEPARADORA+String.format(mensaje, letraCalculada);
                            if(!errores.contains(mensaje))
                                errores.add(mensaje);
                        }
                    }
                }
                catch(Exception ex)
                {
                    mensaje = FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.nifIncorrectoPros");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }
            else
            {
                mensaje = FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.nifIncorrectoPros");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }
        }else {            
                mensaje = FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.nifIncorrectoProsObligatorio");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
        }
        
        //fecha de visita en aÃ±o del expediente
        try
        {
            Date feVisita = vis.getFecVisita();           
            if(feVisita != null )
            {
                Calendar cal = null;
                int ano = -1;
                cal = new GregorianCalendar();
                cal.setTime(feVisita);
                ano = cal.get(Calendar.YEAR);
                if(ano != ejercicio){
                    mensaje = FilaVisProspectoresVO.POS_CAMPO_FECVISITA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.fechaVisita");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
            }else{
                    mensaje = FilaVisProspectoresVO.POS_CAMPO_FECVISITA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.fechaVisitaObligatorio");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
            }
        }catch(Exception ex)
        {
            mensaje = FilaVisProspectoresVO.POS_CAMPO_FECVISITA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.solicitud.preparadores.errorValidacionFechas");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }

        //Provincia
        if(vis.getProvincia() != null)
        {
            try
            {
                Integer codprovincia = vis.getProvincia();
                if (!soloFormato){                         
                   if (!listaProvincia.contains(codprovincia)) {                
                        mensaje = FilaVisProspectoresVO.POS_CAMPO_PROVINCIA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.provinciaIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaVisProspectoresVO.POS_CAMPO_PROVINCIA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.errorValidacionProvincia");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }
        
        //SEctor
        if(vis.getSector() != null)
        {
            try
            {
                Integer codsector = vis.getSector();
                if (!soloFormato){                               
                    if (!listaSector.contains(codsector)) {                    
                        mensaje = FilaVisProspectoresVO.POS_CAMPO_SECTORACT+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.sectorIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaVisProspectoresVO.POS_CAMPO_SECTORACT+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.errorValidacionSector");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }
        
       //cumple LISMI
        if(vis.getCumpleLismi() != null)
        {
            try
            {
                Integer codCumple = vis.getCumpleLismi();
                if (!soloFormato){                               
                    if (!listaCumple.contains(codCumple)) {                    
                        mensaje = FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.cumpleLismiIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.errorValidacioncumpleLismi");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }
        
        
        //Resultado final
        if(vis.getResultadoFinal() != null)
        {
            try
            {
                Integer codResultado = vis.getResultadoFinal();
                if (!soloFormato){                               
                    if (!listaResultado.contains(codResultado)) {                    
                        mensaje = FilaVisProspectoresVO.POS_CAMPO_RESULTADO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.resultadoIncorrecto");
                        if(!errores.contains(mensaje))
                            errores.add(mensaje);

                    }
                }
            }
            catch(Exception ex)
            {
                mensaje = FilaVisProspectoresVO.POS_CAMPO_RESULTADO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.errorValidacionResultado");
                if(!errores.contains(mensaje))
                    errores.add(mensaje);
            }            
        }else {
             mensaje = FilaVisProspectoresVO.POS_CAMPO_RESULTADO+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.resultadoIncorrecto");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
        }
        
        
        //num trabajadores: Valores numÃ©ricos, sin decimales            
        double num;
        long iPart;
        double fPart;        
        try
        {
            if(vis.getNumTrab() != null)
            {
                num = vis.getNumTrab().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaVisProspectoresVO.POS_CAMPO_NUMTRAB+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.numTrabConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaVisProspectoresVO.POS_CAMPO_NUMTRAB+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita..errorValidacionNumTrab");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //num trabajadores: Valores numÃ©ricos, sin decimales                         
        try
        {
            if(vis.getNumTrab() != null)
            {
                num = vis.getNumTrabDisc().doubleValue();
                iPart = (long)num;
                fPart = num - iPart;
                if(fPart != 0.0)
                {
                    mensaje = FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita.numTrabDiscConDecimales");
                    if(!errores.contains(mensaje))
                        errores.add(mensaje);
                }
                
            }
        }
        catch(Exception ex)
        {
            mensaje = FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.visita..errorValidacionNumTrab");
            if(!errores.contains(mensaje))
                errores.add(mensaje);
        }
        
        //empresa
        if(vis.getEmpresa() == null || vis.getEmpresa().equals(""))
        {
            mensaje = FilaVisProspectoresVO.POS_CAMPO_EMPRESA+ConstantesMeLanbide35.BARRA_SEPARADORA+traductor.getMensaje(idioma, "validaciones.justificacion.preparadores.empresaOblig");
            if(!errores.contains(mensaje))
            errores.add(mensaje);                   
        }
        
        return errores;
    }
    
    public static String redondearDecimalesString(BigDecimal num, int numDecimales)
    {
        if(num == null)
        {
            return null;
        }
        else
        {
            try
            {
                String strCero  = "0";
                if(numDecimales > 0)
                {
                    strCero += ".";
                    for(int i = 0; i < numDecimales; i++)
                    {
                        strCero += "0";
                    }
                }
                BigDecimal cero = new BigDecimal(strCero);
                num = num.stripTrailingZeros();
                num = num.add(cero);
                num = num.setScale(numDecimales, RoundingMode.HALF_UP);
                return num.toPlainString();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            return "";
        }
    }
    
//    public static String restarSeguimientoPorErrores(String str1, String str2, String tipo)
//    {
//        if(str1 != null && !str1.equals("") && str2 != null && !str2.equals(""))
//        {
//            BigDecimal sumando = new BigDecimal("1");
//            String[] partes = str1.split("-");
//            String parte1 = partes[1].replaceAll(",", "\\.").trim();
//            BigDecimal total = new BigDecimal(parte1);
//            BigDecimal porc = new BigDecimal(str2.replaceAll(",", "\\."));
//            porc = porc.divide(new BigDecimal("100.00"), 2, RoundingMode.DOWN);
//            total = total.subtract(porc);
//            total = total.add(sumando);
//            total = total.stripTrailingZeros();
//            total = total.subtract(sumando);
//            return partes[0].trim()+"-"+total.toPlainString().replaceAll("\\.", ",");
//        }
//        else
//        {
//            return str1;
//        }
//    }
    
    public static String restarSeguimientoPorErrores(String str1, String str2, boolean decimal)
    {
        if(str1 != null && !str1.equals("") && str2 != null && !str2.equals(""))
        {
            BigDecimal sumando = new BigDecimal("1");
            String[] partes = str1.split("-");
            String parte1 = partes[1].replaceAll(",", "\\.").trim();
            BigDecimal total = new BigDecimal(parte1);
            BigDecimal porc = new BigDecimal(str2.replaceAll(",", "\\."));
            porc = porc.divide(new BigDecimal("100.00"), 2, RoundingMode.DOWN);
            total = total.subtract(porc);
            total = total.add(sumando);
            total = total.stripTrailingZeros();
            total = total.subtract(sumando);
            if(total.compareTo(new BigDecimal("0.00")) < 0)
                total = new BigDecimal("0.00");
            if(decimal)
            {
                return partes[0].trim()+"-"+total.toPlainString().replaceAll("\\.", ",");
            }
            else
            {
                return partes[0].trim()+"-"+total.intValue();
            }
        }
        else
        {
            return str1;
        }
    }
    
    public static boolean tieneCaracteresEspeciales(String valor)
    {
        if(valor != null && !valor.trim().equals(""))
        {
            valor = normalizarTexto(valor);
            //String patron = "/^[a-zA-Z0-9,.ÂºÂª_@#!()=Â¿?Â¡*\\[\\]-]*$/";

            Pattern patron = Pattern.compile(ConstantesMeLanbide35.CARACTERES_ESPECIALES);
            Matcher encaja = patron.matcher(valor);
            boolean tiene = encaja.matches();
            return !encaja.matches();
        }
        else
        {
            return true;
        }
    } 

    public static String normalizarTexto(String text)
    {
        String acentos = "ÃÀÁÄÂÈÉËÊÌÍÏÎÒÓÖÔÙÚÜÛãàáäâèéëêìíïîòóöôùúüûÑñÇç-[]";
        String original = "AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuuNncc_()";
        text = text.toUpperCase();
        for (int i=0; i<acentos.length(); i++) 
        {
            text = text.replace(acentos.charAt(i), original.charAt(i));
        }
        text = text.replaceAll("\\r\\n", "");
        text = text.replaceAll("\\n", "");
        text = text.replaceAll("\\r", "");
        text = text.replaceAll("\\s","_");
        return text;
    }

    
    public static BigDecimal fromFormateadoToDecimal(String numformateado){
        BigDecimal numero=BigDecimal.ZERO;
        numero= new BigDecimal(numformateado.replace(".", "").replace(",", "."));
        return numero;
    }
    
    public static String reemplazarCaracteresEspeciales(String texto)
    {
        if(texto != null && !texto.equals(""))
        {
            String caracteresEspeciales = ConstantesMeLanbide35.CARACTERES_ESPECIALES;
            Pattern patron = Pattern.compile(caracteresEspeciales);
            Matcher encaja = null;
            for (int i=0; i<texto.length(); i++) 
            {
                char c = texto.charAt(i);
                encaja = patron.matcher(""+c);
                if(!encaja.matches())
                {
                    texto = texto.replace(texto.charAt(i), ' ');
                }
            }
            normalizarTexto(texto);
        }
        return texto;
    }
}
