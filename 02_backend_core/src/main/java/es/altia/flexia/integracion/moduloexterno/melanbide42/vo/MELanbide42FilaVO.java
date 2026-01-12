/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.vo;

import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42Exception;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author mikel
 */
public class MELanbide42FilaVO {
    // Debe ser un Map que garantice el orden de las iteraciones
    private final Map<String, String> campos = new LinkedHashMap<String, String>();
    
    private MELanbide42TablaVO tabla = null;
    
    public MELanbide42FilaVO (MELanbide42TablaVO tabla){
        if (tabla == null){
            throw new NullPointerException("La tabla padre no puede ser nula");
        }
        this.tabla = tabla;
    }
    
    public void addCampo(String key, String value){
        campos.put(key, value);
    }
    
    public void removeCampo(String key){
        campos.remove(key);
    }
    
    public Map<String, String> getCampos(){
        return campos;
    }
    
    public MELanbide42TablaVO getTabla() {return tabla;}
    
    /**
     * 
     * @return
     * @throws MELanbide42Exception 
     * @deprecated MELanbide42TablaVO.generateVALUESSyntax
     */
    public String generarSQLValores() throws MELanbide42Exception{
        if (campos.isEmpty()) {
            throw new MELanbide42Exception("La fila no tiene ningún campo definido");
        }
        
        StringBuilder buff = new StringBuilder();
        
        boolean isFirst = true;
        for (String key : campos.keySet()){
            if (!isFirst){
                buff.append(", '").append(StringEscapeUtils.escapeSql(campos.get(key))).append("'");
            } else {
                if (tabla.getPkFieldName() != null && tabla.getPkSeqName() != null){
                    buff.append("(").append(tabla.getPkSeqName()).append(".NEXTVAL");
                    buff.append(",'").append(StringEscapeUtils.escapeSql(campos.get(key))).append("'");
                } else {
                    buff.append("('").append(StringEscapeUtils.escapeSql(campos.get(key))).append("'");
                }
                isFirst = false;
            }
        }
        buff.append(")");
        return buff.toString();
    }
}
