package es.altia.flexia.integracion.moduloexterno.meikus.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author david.caamano
 * @version 07/12/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 07/12/2012 * Edición inicial</li>
 * </ol> 
 */
public enum OperacionesPasarelaEnum {
    //reserva
    RSV("RSV"),
    //eliminar reserva
    DRSV("DRSV"),
    //grabar resolucion
    GRES("GRES"),
    //grabar pago
    GPAG("GPAG");
    
    private String code;
    private static List<OperacionesPasarelaEnum> list;
    private static HashMap<String, OperacionesPasarelaEnum> hash;
    
    static {
        list = new ArrayList<OperacionesPasarelaEnum>();
        hash = new HashMap<String, OperacionesPasarelaEnum>();
        for(OperacionesPasarelaEnum operacionesPasarelaEnum : values()){
            list.add(operacionesPasarelaEnum);
            hash.put(operacionesPasarelaEnum.getCode(), operacionesPasarelaEnum);
        }//for(OperacionesPasarelaEnum operacionesPasarelaEnum : values()) 
    }//static
    
    private OperacionesPasarelaEnum(String value){
        this.code = value;
    }//OperacionesPasarelaEnum
    
    public String getCode() {
            return code;
    }//getCode
    
    public static List<OperacionesPasarelaEnum> getEnums() {
            return list;
    }//getEnums
    
    public static OperacionesPasarelaEnum getEnum(String value) {
            return hash.get(value);
    }//getEnum
            
}//enum
