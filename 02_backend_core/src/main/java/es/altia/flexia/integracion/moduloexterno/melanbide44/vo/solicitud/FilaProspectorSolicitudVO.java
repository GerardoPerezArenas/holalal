/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud;

import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author santiagoc
 */
public class FilaProspectorSolicitudVO 
{
    private String id;
    private String nif;
    private String nombreApel;
    private String fechaInicio;
    private String fechaFin;
    private String horasAnuales;
    private String horasContrato;
    private String horasDedicacionECA;
    private String costesSalarialesSSJor;
    private String costesSalarialesSSPorJor;
    private String costesSalarialesSSEca;
    private String visitas;
    private String visitasImp;
    private String coste;
    private String importeConcedido;
    private String tipoSust;
    private Integer solProspectorOrigen;
    
    private List<String> errores = new ArrayList<String>();
    
    private String[] erroresCampos = null;
    
    public static int POS_CAMPO_NIF = 1;
    public static int POS_CAMPO_NOMBREAPEL = 2;
    public static int POS_CAMPO_FECHA_INICIO = 3;
    public static int POS_CAMPO_FECHA_FIN = 4;
    public static int POS_CAMPO_HORAS_ANUALES = 5;
    public static int POS_CAMPO_HORAS_CONTRATO = 6;
    public static int POS_CAMPO_HORAS_DEDICACION_ECA = 7;
    public static int POS_CAMPO_COSTES_SALARIALES_SS_JOR = 8;
    public static int POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR = 9;
    public static int POS_CAMPO_COSTES_SALARIALES_SS_ECA = 10;
    public static int POS_CAMPO_VISITAS = 11;
    public static int POS_CAMPO_VISITAS_IMP = 12;
    public static int POS_CAMPO_COSTE = 13;
    public static int POS_CAMPO_IMPORTE_CONCEDIDO = 14;
    
    public static int NUM_CAMPOS_FILA = 14;
    
    public FilaProspectorSolicitudVO()
    {
        erroresCampos = new String[NUM_CAMPOS_FILA];
        for(int i = 0; i < erroresCampos.length; i++)
        {
            erroresCampos[i] = ConstantesMeLanbide44.FALSO;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombreApel() {
        return nombreApel;
    }

    public void setNombreApel(String nombreApel) {
        this.nombreApel = nombreApel;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getHorasAnuales() {
        return horasAnuales;
    }

    public void setHorasAnuales(String horasAnuales) {
        this.horasAnuales = horasAnuales;
    }

    public String getHorasContrato() {
        return horasContrato;
    }

    public void setHorasContrato(String horasContrato) {
        this.horasContrato = horasContrato;
    }

    public String getHorasDedicacionECA() {
        return horasDedicacionECA;
    }

    public void setHorasDedicacionECA(String horasDedicacionECA) {
        this.horasDedicacionECA = horasDedicacionECA;
    }

    public String getCostesSalarialesSSJor() {
        return costesSalarialesSSJor;
    }

    public void setCostesSalarialesSSJor(String costesSalarialesSSJor) {
        this.costesSalarialesSSJor = costesSalarialesSSJor;
    }

    public String getCostesSalarialesSSPorJor() {
        return costesSalarialesSSPorJor;
    }

    public void setCostesSalarialesSSPorJor(String costesSalarialesSSPorJor) {
        this.costesSalarialesSSPorJor = costesSalarialesSSPorJor;
    }

    public String getCostesSalarialesSSEca() {
        return costesSalarialesSSEca;
    }

    public void setCostesSalarialesSSEca(String costesSalarialesSSEca) {
        this.costesSalarialesSSEca = costesSalarialesSSEca;
    }

    public String getVisitas() {
        return visitas;
    }

    public void setVisitas(String visitas) {
        this.visitas = visitas;
    }

    public String getVisitasImp() {
        return visitasImp;
    }

    public void setVisitasImp(String visitasImp) {
        this.visitasImp = visitasImp;
    }

    public String getCoste() {
        return coste;
    }

    public void setCoste(String coste) {
        this.coste = coste;
    }

    public String getImporteConcedido() {
        return importeConcedido;
    }

    public void setImporteConcedido(String importeConcedido) {
        this.importeConcedido = importeConcedido;
    }

    public String getTipoSust() {
        return tipoSust;
    }

    public void setTipoSust(String tipoSust) {
        this.tipoSust = tipoSust;
    }

    public Integer getSolProspectorOrigen() {
        return solProspectorOrigen;
    }

    public void setSolProspectorOrigen(Integer solProspectorOrigen) {
        this.solProspectorOrigen = solProspectorOrigen;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    public String getErrorCampo(int posCampo)
    {
        if(posCampo >= 0 && posCampo < erroresCampos.length)
        {
            try
            {
                return erroresCampos[posCampo];
            }
            catch(Exception ex)
            {
                return ConstantesMeLanbide44.FALSO;
            }
        }
        return ConstantesMeLanbide44.FALSO;
    }
    
    public void setErrorCampo(int posCampo, String errorCampo)
    {
        if(posCampo >= 0 && posCampo < erroresCampos.length)
        {
            if(errorCampo != null && (errorCampo.equalsIgnoreCase(ConstantesMeLanbide44.CIERTO) || errorCampo.equalsIgnoreCase(ConstantesMeLanbide44.FALSO)))
            {
                erroresCampos[posCampo] = errorCampo;
            }
        }
    }
}
