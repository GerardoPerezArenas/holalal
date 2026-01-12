package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author INGDGC
 */
public class OriCECriteriosEvaDTO extends OriCECriteriosEva {
    
    private List<OriCECriteriosEvaOpcion> listaOpciones = new  ArrayList<OriCECriteriosEvaOpcion>();

    private int puntuacionCentro;

    public OriCECriteriosEvaDTO(long id, String codigo, String codigoOrden, String descripcion_ES, String descripcion_EU, int ejercicioConvocatoria) {
        super(id, codigo, codigoOrden, descripcion_ES, descripcion_EU,ejercicioConvocatoria);
    }
    
    public OriCECriteriosEvaDTO(OriCECriteriosEva oriCECriteriosEva,List<OriCECriteriosEvaOpcion> listaOpcionesC) {
        super(oriCECriteriosEva.getId(), oriCECriteriosEva.getCodigo(), oriCECriteriosEva.getCodigoOrden(), oriCECriteriosEva.getDescripcion_ES(), oriCECriteriosEva.getDescripcion_EU(), oriCECriteriosEva.getEjercicioConvocatoria());
        listaOpciones=(listaOpcionesC!=null?listaOpcionesC:new ArrayList<OriCECriteriosEvaOpcion>());
    }

    public OriCECriteriosEvaDTO(OriCECriteriosEva oriCECriteriosEva, List<OriCECriteriosEvaOpcion> listaOpcionesC, int puntuacion) {
        super(oriCECriteriosEva.getId(), oriCECriteriosEva.getCodigo(), oriCECriteriosEva.getCodigoOrden(), oriCECriteriosEva.getDescripcion_ES(), oriCECriteriosEva.getDescripcion_EU(), oriCECriteriosEva.getEjercicioConvocatoria());
        listaOpciones=(listaOpcionesC!=null?listaOpcionesC:new ArrayList<OriCECriteriosEvaOpcion>());
        puntuacionCentro=puntuacion;
    }


    public List<OriCECriteriosEvaOpcion> getListaOpciones() {
        return listaOpciones;
    }

    public void setListaOpciones(List<OriCECriteriosEvaOpcion> listaOpciones) {
        this.listaOpciones = listaOpciones;
    }

    public Long getIDOpcionListaOpciones(String codigoOrdenOpcion){
        Long respuesta=null;
        if(listaOpciones!=null && !listaOpciones.isEmpty()){
            for (OriCECriteriosEvaOpcion listaOpcion : listaOpciones) {
                if(listaOpcion.getCodigoOrden().equalsIgnoreCase(codigoOrdenOpcion))
                    return listaOpcion.getId();
            }
        }
        return respuesta;
    }

    public int getPuntuacionCentro() {
        return puntuacionCentro;
    }

    public void setPuntuacionCentro(int puntuacionCentro) {
        this.puntuacionCentro = puntuacionCentro;
    }

    @Override
    public String toString() {
        return "OriCECriteriosEvaDTO{" 
                + "OriCECriteriosEva=" + super.toString() + ","
                + "listaOpciones=[" + (listaOpciones!=null?Arrays.toString(listaOpciones.toArray()):"") + "]}"
                + "puntuacionCentro=" + puntuacionCentro
                ;
    }
           
}
