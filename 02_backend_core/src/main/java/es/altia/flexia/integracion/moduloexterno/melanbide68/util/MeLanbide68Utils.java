/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide68.util;

import es.altia.flexia.integracion.moduloexterno.melanbide68.exception.DatoExcelNoValidoException;
import es.altia.flexia.integracion.moduloexterno.melanbide68.exception.ExcelRowMappingException;
import es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 *
 * @author sergiog
 */
public class MeLanbide68Utils {
    
    private static Logger log = LogManager.getLogger(MeLanbide68Utils.class);
    
    public static String getValorCelda(HSSFCell celda, String tipoRequerido)
    {
        log.debug("tiporequerido:"+tipoRequerido+"celda: "+celda);
        Object valor = null;
        boolean numerico = false;
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
                        numerico = true;
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        valor = celda.getStringCellValue();
                        break;
                }
                 //log.debug("celda valor:"+valor);
                if((tipoRequerido != null && tipoRequerido.equalsIgnoreCase(ConstantesMeLanbide68.TiposRequeridos.NUMERICO)) || numerico)
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
                                            int cerosAgregar = numExp - partesStr[1].length(); 
                                            numStr = numStr.replace(",", "");
                                            for (int i= 0; i < cerosAgregar; i++){
                                                numStr =numStr+"0";
                                            }
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
            }        
        
        }catch(Exception e){
        log.error("Error",e);
        }
        
        return valor != null ? valor.toString() : null;
    }
    
    public static boolean esFilaVacia(int numCols, HSSFRow fila)
    {
        log.debug("esFilaVacia BEGIN NUMCOLS="+numCols);
        int i = 0;
        
        boolean valorEncontrado = false;
        HSSFCell cell = null;
        String valor = "";
        try{            
        while(i < numCols && !valorEncontrado)
        {
            cell = fila.getCell((short)i);
            valor = getValorCelda(cell, null);
            log.debug("col "+i+": "+valor);
            if(valor != null && !valor.equals(""))
            {
                valorEncontrado = true;
            }
            i++;
        }
        }catch(Exception e){
            log.error("error en es fila vacÌa",e);
        }
        log.debug("esFilaVacia END  --resultado:"+!valorEncontrado);
        return !valorEncontrado;
    }
    
    public static String validarDatoExcel(String valor, String tipo, int longMax, boolean sePermiteVacio)
    {
        String rdo="0";
        
        if(tipo != null)
        {
            if(valor != null && !valor.equals(""))
            {
               if(tipo.equalsIgnoreCase(ConstantesMeLanbide68.TiposRequeridos.STRING))
                {
                    valor =valor.trim();
                    if(valor.length() > longMax)
                    {
                        rdo="1";
                    }

                }                   

                else
                {
                    rdo="2";
                }
            }
            else
            {
                if(!sePermiteVacio)
                {
                    rdo="3";
                }
            }
        }
        else
        {
            rdo="4";
        }
        return rdo;               
       
    }
    
//     public static boolean tieneCaracteresEspeciales(String valor)
//    {
//        if(valor != null && !valor.trim().equals(""))
//        {
//            valor = normalizarTexto(valor);
//            //String patron = "/^[a-zA-Z0-9,.¬∫¬™_@#!()=¬ø?¬°*\\[\\]-]*$/";
//
//            Pattern patron = Pattern.compile(ConstantesMeLanbide68.CARACTERES_ESPECIALES);
//            Matcher encaja = patron.matcher(valor);
//            boolean tiene = encaja.matches();
//            return !encaja.matches();
//        }
//        else
//        {
//            return true;
//        }
//    } 

//    public static String normalizarTexto(String text)
//    {
//        String acentos = "√¿¡ƒ¬»…À ÃÕœŒ“”÷‘Ÿ⁄‹€„‡·‰‚ËÈÎÍÏÌÔÓÚÛˆÙ˘˙¸˚—Ò«Á-[]";
//        String original = "AAAAAEEEEIIIIOOOOUUUUaaaaaeeeeiiiioooouuuuNncc_()";
//        text = text.toUpperCase();
//        for (int i=0; i<acentos.length(); i++) 
//        {
//            text = text.replace(acentos.charAt(i), original.charAt(i));
//        }
//        text = text.replaceAll("\\r\\n", "");
//        text = text.replaceAll("\\n", "");
//        text = text.replaceAll("\\r", "");
//        text = text.replaceAll("\\s","_");
//        return text;
//    }
    
    public static String crearMensajeDatoExcelNoValido(ExcelRowMappingException erme, int fila, int codIdioma)
    {
        String mensaje = MeLanbide68I18n.getInstance().getMensaje(codIdioma, "error.filaExcelDatosNoValidos");
        
        mensaje = String.format(mensaje, fromColNumberToExcelColName(Integer.parseInt(erme.getCampo())),""+fila);
        if(erme != null && erme.getTipo() != null)
        {
            String tipo = erme.getTipo();
            String mensajeTipo = "";
            if(tipo.equalsIgnoreCase(ConstantesMeLanbide68.TiposRequeridos.NUMERICO))
            {
                if(erme.getpEntera() != null)
                {
                    mensajeTipo = MeLanbide68I18n.getInstance().getMensaje(codIdioma, "expectedType.number");
                    mensajeTipo = String.format(mensajeTipo, erme.getpEntera());
                }
            }            
            else if(tipo.equalsIgnoreCase(ConstantesMeLanbide68.TiposRequeridos.STRING))
            {
                if(erme.getLongMax() != null)
                {
                    mensajeTipo = MeLanbide68I18n.getInstance().getMensaje(codIdioma, "expectedType.string");
                    mensajeTipo = String.format(mensajeTipo, erme.getLongMax());
                }
            }            
            mensaje += " ";
            mensaje += mensajeTipo;
        }
        return mensaje;
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
    
}
