/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author gerardo.perez
 */
package es.altia.flexia.integracion.moduloexterno.melanbide91.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.DesplegableVO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author gerardo.perez
 */
public class MeLanbide91Utils {
    
    static final Logger log = LogManager.getLogger(MeLanbide91Utils.class);
    
    public static String getCodigoProcedimientoFromNumExpediente(String numExpediente) {
        String retorno = "";
        try {
            if (numExpediente != null) {
                String[] arrayDatos = numExpediente.split("/");
                if (arrayDatos != null && arrayDatos.length == 3) {
                    retorno = arrayDatos[1];
                }
            }
        } catch (Exception e) {
            retorno = "";
            log.error("Error al recuperar el cod procedimiento desde el numero de expediente - retornamos vacio. ", e);
        }
        return retorno;
    }
       /**
     * Método que extrae la descripción de los desplegables en el idioma del
     * usuario, en BBDD están en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    public List<DesplegableVO> traducirDesplegable(HttpServletRequest request,int idioma, List<DesplegableVO> desplegable) {
        for (DesplegableVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request,d.getDes_nom()));
            }
        }
        return desplegable;
    }
    
    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
      public int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide91.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            idioma = ConstantesMeLanbide91.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }
    /**
     * Método que retorna el valor de un desplegable en el idioma del usuario
     *
     * @param request
     * @param descripcion
     * @return String con el valor
     */
    public String getDescripcionDesplegable(HttpServletRequest request, String descripcion ) {
       log.info("getDescripcionDesplegable : descripcion " + descripcion );
        String barraIdioma = "|" ;
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraIdioma);
                if (descripcionDobleIdioma != null ) {
                    if (getIdioma(request) == ConstantesMeLanbide91.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                } else{
                    log.info("El tamano del no es valido " + descripcion);
                }
            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
    }
}
    
    

