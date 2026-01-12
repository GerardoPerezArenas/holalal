package es.altia.flexia.integracion.moduloexterno.melanbide03.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Enumerado para los tipos de formato de los reports.
 * 
 * @author david.caamano
 * @version 11/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 11-10-2012 * </li>
 * </ol> 
 */
public enum MeLanbide03ReportFormatEnum {
    /** PDF */
    PDF("PDF"),
    /** RTF */
    RTF("RTF"),
    /** ODT */
    ODT("ODT");

    private String code;
    private static List<MeLanbide03ReportFormatEnum> list;
    private static HashMap<String, MeLanbide03ReportFormatEnum> hash;

    static {
        list = new ArrayList<MeLanbide03ReportFormatEnum>();
        hash = new HashMap<String, MeLanbide03ReportFormatEnum>();
        for (MeLanbide03ReportFormatEnum exportFormatEnum : values()) {
                list.add(exportFormatEnum);
                hash.put(exportFormatEnum.getCode(), exportFormatEnum);
        }//for (ReportFormatEnum exportFormatEnum : values()) 
    }//static

    private MeLanbide03ReportFormatEnum(String value) {
            code = value;
    }//ReportFormatEnum

    public String getCode() {
            return code;
    }//getCode

    public static List<MeLanbide03ReportFormatEnum> getEnums() {
            return list;
    }//getEnums

    public static MeLanbide03ReportFormatEnum getEnum(String value) {
            return hash.get(value);
    }//getEnum

    public static MeLanbide03ReportFormatEnum getDefault() {
            return RTF;
    }//getDefault
    
}