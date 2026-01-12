/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.util;

import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42FilaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42ModuloVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42TablaVO;
import java.util.ArrayList;

/**
 *
 * @author mikel
 */
public class MELanbide42ExpElecHTMLGenerator {
    
    private MELanbide42ExpElecHTMLGenerator(){
        // No instanciable
    }
    
    public static String getHTML(ArrayList<MELanbide42ModuloVO> modulos){
        StringBuilder buff = new StringBuilder();
        
        // Un div por cada módulo
        for (MELanbide42ModuloVO m : modulos){
            buff.append("<div class='me_modulo'>");
            buff.append("<div class='me_modulo_titulo'>").append(m.getCodigo()).append("</div>");
            
            // Un table por cada tabla
            for (MELanbide42TablaVO t : m.getTablas()){
                buff.append("<table class='me_modulo_datos'>");
                buff.append("<caption>").append(t.getNombre()).append("</caption>");
                
                // Cabecera de la tabla
                buff.append("<thead><tr>");
                // Cojo la primera fila para conocer los campos. Deberían ser igual en todas las filas
                MELanbide42FilaVO fila = t.getFilas().get(0);
                for (String c : fila.getCampos().keySet()){
                    buff.append("<th>").append(c).append("</th>");
                }
                buff.append("</tr></thead>");
                
                // Cuerpo de la tabla
                buff.append("<tbody>");
                // Un <tr> por cada fila
                for (MELanbide42FilaVO f : t.getFilas()){
                    buff.append("<tr>");
                    for (String c : f.getCampos().keySet()){
                        buff.append("<td>").append(f.getCampos().get(c)).append("</td>");
                    }
                    buff.append("</tr>");
                }
                buff.append("</tbody>");
                
                buff.append("</table>");
            }

            buff.append("</div>");
            buff.append("</div>");
        }
        
        return buff.toString();
    }
}
