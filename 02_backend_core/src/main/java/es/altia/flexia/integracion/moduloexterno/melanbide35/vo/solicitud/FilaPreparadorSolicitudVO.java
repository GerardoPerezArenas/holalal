/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud;

import es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SantiagoC
 */
public class FilaPreparadorSolicitudVO 
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
    private String numSegAnteriores;
    private String importe;
    private String c1h;
    private String c1m;
    private String c1Total;
    private String c2h;
    private String c2m;
    private String c2Total;
    private String c3h;
    private String c3m;
    private String c3Total;
    private String c4h;
    private String c4m;
    private String c4Total;
    private String inserciones;
    private String seguimientosInserciones;
    private String costesSalarialesSS;
    private String importeConcedido;
    private String tipoSust;
    private Integer solPreparadorOrigen;
    
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
    public static int POS_CAMPO_NUM_SEG_ANTERIORES = 11;
    public static int POS_CAMPO_IMPORTE = 12;
    public static int POS_CAMPO_C1H = 13;
    public static int POS_CAMPO_C1M = 14;
    public static int POS_CAMPO_C1TOTAL = 15;
    public static int POS_CAMPO_C2H = 16;
    public static int POS_CAMPO_C2M = 17;
    public static int POS_CAMPO_C2TOTAL = 18;
    public static int POS_CAMPO_C3H = 19;
    public static int POS_CAMPO_C3M = 20;
    public static int POS_CAMPO_C3TOTAL = 21;
    public static int POS_CAMPO_C4H = 22;
    public static int POS_CAMPO_C4M = 23;
    public static int POS_CAMPO_C4TOTAL = 24;
    public static int POS_CAMPO_INSERCIONES = 25;
    public static int POS_CAMPO_SEGUIMIENTOS_INSERCIONES = 26;
    public static int POS_CAMPO_COSTES_SALARIALES_SS = 27;
    public static int POS_CAMPO_IMPORTE_CONCEDIDO = 28;
    
    public static int NUM_CAMPOS_FILA = 28;
    
    
    public FilaPreparadorSolicitudVO()
    {
        erroresCampos = new String[NUM_CAMPOS_FILA];
        for(int i = 0; i < erroresCampos.length; i++)
        {
            erroresCampos[i] = ConstantesMeLanbide35.FALSO;
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

    public String getNumSegAnteriores() {
        return numSegAnteriores;
    }

    public void setNumSegAnteriores(String numSegAnteriores) {
        this.numSegAnteriores = numSegAnteriores;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getC1h() {
        return c1h;
    }

    public void setC1h(String c1h) {
        this.c1h = c1h;
    }

    public String getC1m() {
        return c1m;
    }

    public void setC1m(String c1m) {
        this.c1m = c1m;
    }

    public String getC1Total() {
        return c1Total;
    }

    public void setC1Total(String c1Total) {
        this.c1Total = c1Total;
    }

    public String getC2h() {
        return c2h;
    }

    public void setC2h(String c2h) {
        this.c2h = c2h;
    }

    public String getC2m() {
        return c2m;
    }

    public void setC2m(String c2m) {
        this.c2m = c2m;
    }

    public String getC2Total() {
        return c2Total;
    }

    public void setC2Total(String c2Total) {
        this.c2Total = c2Total;
    }

    public String getC3h() {
        return c3h;
    }

    public void setC3h(String c3h) {
        this.c3h = c3h;
    }

    public String getC3m() {
        return c3m;
    }

    public void setC3m(String c3m) {
        this.c3m = c3m;
    }

    public String getC3Total() {
        return c3Total;
    }

    public void setC3Total(String c3Total) {
        this.c3Total = c3Total;
    }

    public String getC4h() {
        return c4h;
    }

    public void setC4h(String c4h) {
        this.c4h = c4h;
    }

    public String getC4m() {
        return c4m;
    }

    public void setC4m(String c4m) {
        this.c4m = c4m;
    }

    public String getC4Total() {
        return c4Total;
    }

    public void setC4Total(String c4Total) {
        this.c4Total = c4Total;
    }

    public String getInserciones() {
        return inserciones;
    }

    public void setInserciones(String inserciones) {
        this.inserciones = inserciones;
    }

    public String getSeguimientosInserciones() {
        return seguimientosInserciones;
    }

    public void setSeguimientosInserciones(String seguimientosInserciones) {
        this.seguimientosInserciones = seguimientosInserciones;
    }

    public String getCostesSalarialesSS() {
        return costesSalarialesSS;
    }

    public void setCostesSalarialesSS(String costesSalarialesSS) {
        this.costesSalarialesSS = costesSalarialesSS;
    }

    public String getTipoSust() {
        return tipoSust;
    }

    public void setTipoSust(String tipoSust) {
        this.tipoSust = tipoSust;
    }

    public Integer getSolPreparadorOrigen() {
        return solPreparadorOrigen;
    }

    public void setSolPreparadorOrigen(Integer solPreparadorOrigen) {
        this.solPreparadorOrigen = solPreparadorOrigen;
    }

    public String getImporteConcedido() {
        return importeConcedido;
    }

    public void setImporteConcedido(String importeConcedido) {
        this.importeConcedido = importeConcedido;
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
                return ConstantesMeLanbide35.FALSO;
            }
        }
        return ConstantesMeLanbide35.FALSO;
    }
    
    public void setErrorCampo(int posCampo, String errorCampo)
    {
        if(posCampo >= 0 && posCampo < erroresCampos.length)
        {
            if(errorCampo != null && (errorCampo.equalsIgnoreCase(ConstantesMeLanbide35.CIERTO) || errorCampo.equalsIgnoreCase(ConstantesMeLanbide35.FALSO)))
            {
                erroresCampos[posCampo] = errorCampo;
            }
        }
    }
}
