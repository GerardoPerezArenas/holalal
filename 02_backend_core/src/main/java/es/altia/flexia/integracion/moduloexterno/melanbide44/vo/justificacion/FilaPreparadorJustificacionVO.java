/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion;

import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author SantiagoC
 */
public class FilaPreparadorJustificacionVO 
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
        private String c5h;
    private String c5m;
    private String c5Total;
        private String c6h;
    private String c6m;
    private String c6Total;
    private String inserciones;
    private String seguimientosInserciones;
    private String costesSalarialesSS;
    private Integer codTipoContrato;
    private String tipoContrato;
    private Boolean esSustituto;
    private String tipoSust;
    private Integer jusPreparadorOrigen;
    
    private List<String> errores = new ArrayList<String>(); 
    private String[] erroresCampos = null;
    
    
    public static int NUM_CAMPOS_FILA = 29;
    public static int POS_CAMPO_NIF = 1;
    public static int POS_CAMPO_NOMBREAPEL = 2;
    public static int POS_CAMPO_FECHA_INICIO = 3;
    public static int POS_CAMPO_FECHA_FIN = 4;
    public static int POS_CAMPO_TIPO_CONTRATO = 5;
    public static int POS_CAMPO_HORAS_ANUALES = 6;
    public static int POS_CAMPO_HORAS_CONTRATO = 7;
    public static int POS_CAMPO_HORAS_DEDICACION_ECA = 8;
    public static int POS_CAMPO_COSTES_SALARIALES_SS_JOR = 9;
    public static int POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR = 10;
    public static int POS_CAMPO_COSTES_SALARIALES_SS_ECA = 11;
    public static int POS_CAMPO_NUM_SEG_ANTERIORES = 12;
    public static int POS_CAMPO_IMPORTE = 13;
    public static int POS_CAMPO_C1H = 14;
    public static int POS_CAMPO_C1M = 15;
    public static int POS_CAMPO_C1TOTAL = 16;
    public static int POS_CAMPO_C2H = 17;
    public static int POS_CAMPO_C2M = 18;
    public static int POS_CAMPO_C2TOTAL = 19;
    public static int POS_CAMPO_C3H = 20;
    public static int POS_CAMPO_C3M = 21;
    public static int POS_CAMPO_C3TOTAL = 22;
    public static int POS_CAMPO_C4H = 23;
    public static int POS_CAMPO_C4M = 24;
    public static int POS_CAMPO_C4TOTAL = 25;
        public static int POS_CAMPO_C5H = 26;
    public static int POS_CAMPO_C5M = 27;
    public static int POS_CAMPO_C5TOTAL = 28;
        public static int POS_CAMPO_C6H = 29;
    public static int POS_CAMPO_C6M = 30;
    public static int POS_CAMPO_C6TOTAL = 31;
    public static int POS_CAMPO_INSERCIONES = 32;
    public static int POS_CAMPO_SEGUIMIENTOS_INSERCIONES = 33;
    public static int POS_CAMPO_COSTES_SALARIALES_SS = 34;
    
   
    
    
    public FilaPreparadorJustificacionVO()
    {
        erroresCampos = new String[NUM_CAMPOS_FILA];
        for(int i = 0; i < erroresCampos.length; i++)
        {
            erroresCampos[i] = ConstantesMeLanbide44.FALSO;
        }
    }

    public String getC5h() {
        return c5h;
    }

    public void setC5h(String c5h) {
        this.c5h = c5h;
    }

    public String getC5m() {
        return c5m;
    }

    public void setC5m(String c5m) {
        this.c5m = c5m;
    }

    public String getC5Total() {
        return c5Total;
    }

    public void setC5Total(String c5Total) {
        this.c5Total = c5Total;
    }

    public String getC6h() {
        return c6h;
    }

    public void setC6h(String c6h) {
        this.c6h = c6h;
    }

    public String getC6m() {
        return c6m;
    }

    public void setC6m(String c6m) {
        this.c6m = c6m;
    }

    public String getC6Total() {
        return c6Total;
    }

    public void setC6Total(String c6Total) {
        this.c6Total = c6Total;
    }

    public String[] getErroresCampos() {
        return erroresCampos;
    }

    public void setErroresCampos(String[] erroresCampos) {
        this.erroresCampos = erroresCampos;
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

    public Integer getCodTipoContrato() {
        return codTipoContrato;
    }

    public void setCodTipoContrato(Integer codTipoContrato) {
        this.codTipoContrato = codTipoContrato;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }
    
     public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    public Boolean esSustituto() {
        return esSustituto;
    }

    public void setEsSustituto(Boolean esSustituto) {
        this.esSustituto = esSustituto;
    }

    public String getTipoSust() {
        return tipoSust;
    }

    public void setTipoSust(String tipoSust) {
        this.tipoSust = tipoSust;
    }

    public Integer getJusPreparadorOrigen() {
        return jusPreparadorOrigen;
    }

    public void setJusPreparadorOrigen(Integer jusPreparadorOrigen) {
        this.jusPreparadorOrigen = jusPreparadorOrigen;
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
