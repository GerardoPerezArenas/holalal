/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author SantiagoC
 */
public class EcaSolPreparadoresVO implements Comparable
{
    private Integer solPreparadoresCod;
    private String nif;
    private String nombre;
    private Date fecIni;
    private Date fecFin;
    private BigDecimal horasJC;
    private BigDecimal horasCont;
    private BigDecimal horasEca;
    private BigDecimal impSSJC;
    private BigDecimal impSSJR;
    private BigDecimal impSSECA;
    private Integer segAnt;
    private BigDecimal impSegAnt;//TODO: revisar tipo dato Integer-BigDecimal
    private BigDecimal insC1H;
    private BigDecimal insC1M;
    private BigDecimal insC1;
    private BigDecimal insC2H;
    private BigDecimal insC2M;
    private BigDecimal insC2;
    private BigDecimal insC3H;
    private BigDecimal insC3M;
    private BigDecimal insC3;
    private BigDecimal insC4H;
    private BigDecimal insC4M;
    private BigDecimal insC4;
        private BigDecimal insC5H;
    private BigDecimal insC5M;
    private BigDecimal insC5;
        private BigDecimal insC6H;
    private BigDecimal insC6M;
    private BigDecimal insC6;
    private BigDecimal insImporte;
    private BigDecimal insSegImporte;
    private BigDecimal coste;
    private BigDecimal impteConcedido;
    private Integer solicitud;
    private String tipoSust;
    private Integer solPreparadorOrigen;
    
    //Este se utiliza para la carga desde excel pero no persiste en BD
    private String nifPreparadorSustituido;
    
    private String nif_Carga;
    private String nombre_Carga;
    private Date fecIni_Carga;
    private Date fecFin_Carga;
    private BigDecimal horasJC_Carga;
    private BigDecimal horasCont_Carga;
    private BigDecimal horasEca_Carga;
    private BigDecimal impSSJC_Carga;
    private BigDecimal impSSJR_Carga;
    private BigDecimal impSSECA_Carga;
    private Integer segAnt_Carga;
    private BigDecimal impSegAnt_Carga;
    private BigDecimal insC1H_Carga;
    private BigDecimal insC1M_Carga;
    private BigDecimal insC1_Carga;
    private BigDecimal insC2H_Carga;
    private BigDecimal insC2M_Carga;
    private BigDecimal insC2_Carga;
    private BigDecimal insC3H_Carga;
    private BigDecimal insC3M_Carga;
    private BigDecimal insC3_Carga;
    private BigDecimal insC4H_Carga;
    private BigDecimal insC4M_Carga;
    private BigDecimal insC4_Carga;
    private BigDecimal insC5H_Carga;
    private BigDecimal insC5M_Carga;
    private BigDecimal insC5_Carga;
    private BigDecimal insC6H_Carga;
    private BigDecimal insC6M_Carga;
    private BigDecimal insC6_Carga;
    private BigDecimal insImporte_Carga;
    private BigDecimal insSegImporte_Carga;
    private BigDecimal coste_Carga;
    
    public EcaSolPreparadoresVO()
    {
        
    }

    public BigDecimal getInsC5H() {
        return insC5H;
    }

    public void setInsC5H(BigDecimal insC5H) {
        this.insC5H = insC5H;
    }

    public BigDecimal getInsC5M() {
        return insC5M;
    }

    public void setInsC5M(BigDecimal insC5M) {
        this.insC5M = insC5M;
    }

    public BigDecimal getInsC5() {
        return insC5;
    }

    public void setInsC5(BigDecimal insC5) {
        this.insC5 = insC5;
    }

    public BigDecimal getInsC6H() {
        return insC6H;
    }

    public void setInsC6H(BigDecimal insC6H) {
        this.insC6H = insC6H;
    }

    public BigDecimal getInsC6M() {
        return insC6M;
    }

    public void setInsC6M(BigDecimal insC6M) {
        this.insC6M = insC6M;
    }

    public BigDecimal getInsC6() {
        return insC6;
    }

    public void setInsC6(BigDecimal insC6) {
        this.insC6 = insC6;
    }

    public BigDecimal getInsC5H_Carga() {
        return insC5H_Carga;
    }

    public void setInsC5H_Carga(BigDecimal insC5H_Carga) {
        this.insC5H_Carga = insC5H_Carga;
    }

    public BigDecimal getInsC5M_Carga() {
        return insC5M_Carga;
    }

    public void setInsC5M_Carga(BigDecimal insC5M_Carga) {
        this.insC5M_Carga = insC5M_Carga;
    }

    public BigDecimal getInsC5_Carga() {
        return insC5_Carga;
    }

    public void setInsC5_Carga(BigDecimal insC5_Carga) {
        this.insC5_Carga = insC5_Carga;
    }

    public BigDecimal getInsC6H_Carga() {
        return insC6H_Carga;
    }

    public void setInsC6H_Carga(BigDecimal insC6H_Carga) {
        this.insC6H_Carga = insC6H_Carga;
    }

    public BigDecimal getInsC6M_Carga() {
        return insC6M_Carga;
    }

    public void setInsC6M_Carga(BigDecimal insC6M_Carga) {
        this.insC6M_Carga = insC6M_Carga;
    }

    public BigDecimal getInsC6_Carga() {
        return insC6_Carga;
    }

    public void setInsC6_Carga(BigDecimal insC6_Carga) {
        this.insC6_Carga = insC6_Carga;
    }

    
    
    
    
    public Integer getSolPreparadoresCod() {
        return solPreparadoresCod;
    }

    public void setSolPreparadoresCod(Integer solPreparadoresCod) {
        this.solPreparadoresCod = solPreparadoresCod;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecIni() {
        return fecIni;
    }

    public void setFecIni(Date fecIni) {
        this.fecIni = fecIni;
    }

    public Date getFecFin() {
        return fecFin;
    }

    public void setFecFin(Date fecFin) {
        this.fecFin = fecFin;
    }

    public BigDecimal getHorasJC() {
        return horasJC;
    }

    public void setHorasJC(BigDecimal horasJC) {
        this.horasJC = horasJC;
    }

    public BigDecimal getHorasCont() {
        return horasCont;
    }

    public void setHorasCont(BigDecimal horasCont) {
        this.horasCont = horasCont;
    }

    public BigDecimal getHorasEca() {
        return horasEca;
    }

    public void setHorasEca(BigDecimal horasEca) {
        this.horasEca = horasEca;
    }

    public BigDecimal getImpSSJC() {
        return impSSJC;
    }

    public void setImpSSJC(BigDecimal impSSJC) {
        this.impSSJC = impSSJC;
    }

    public BigDecimal getImpSSJR() {
        return impSSJR;
    }

    public void setImpSSJR(BigDecimal impSSJR) {
        this.impSSJR = impSSJR;
    }

    public BigDecimal getImpSSECA() {
        return impSSECA;
    }

    public void setImpSSECA(BigDecimal impSSECA) {
        this.impSSECA = impSSECA;
    }

    public Integer getSegAnt() {
        return segAnt;
    }

    public void setSegAnt(Integer segAnt) {
        this.segAnt = segAnt;
    }

    public BigDecimal getImpSegAnt() {
        return impSegAnt;
    }

    public void setImpSegAnt(BigDecimal impSegAnt) {
        this.impSegAnt = impSegAnt;
    }

    public BigDecimal getInsC1H() {
        return insC1H;
    }

    public void setInsC1H(BigDecimal insC1H) {
        this.insC1H = insC1H;
    }

    public BigDecimal getInsC1M() {
        return insC1M;
    }

    public void setInsC1M(BigDecimal insC1M) {
        this.insC1M = insC1M;
    }

    public BigDecimal getInsC1() {
        return insC1;
    }

    public void setInsC1(BigDecimal insC1) {
        this.insC1 = insC1;
    }

    public BigDecimal getInsC2H() {
        return insC2H;
    }

    public void setInsC2H(BigDecimal insC2H) {
        this.insC2H = insC2H;
    }

    public BigDecimal getInsC2M() {
        return insC2M;
    }

    public void setInsC2M(BigDecimal insC2M) {
        this.insC2M = insC2M;
    }

    public BigDecimal getInsC2() {
        return insC2;
    }

    public void setInsC2(BigDecimal insC2) {
        this.insC2 = insC2;
    }

    public BigDecimal getInsC3H() {
        return insC3H;
    }

    public void setInsC3H(BigDecimal insC3H) {
        this.insC3H = insC3H;
    }

    public BigDecimal getInsC3M() {
        return insC3M;
    }

    public void setInsC3M(BigDecimal insC3M) {
        this.insC3M = insC3M;
    }

    public BigDecimal getInsC3() {
        return insC3;
    }

    public void setInsC3(BigDecimal insC3) {
        this.insC3 = insC3;
    }

    public BigDecimal getInsC4H() {
        return insC4H;
    }

    public void setInsC4H(BigDecimal insC4H) {
        this.insC4H = insC4H;
    }

    public BigDecimal getInsC4M() {
        return insC4M;
    }

    public void setInsC4M(BigDecimal insC4M) {
        this.insC4M = insC4M;
    }

    public BigDecimal getInsC4() {
        return insC4;
    }

    public void setInsC4(BigDecimal insC4) {
        this.insC4 = insC4;
    }

    public BigDecimal getInsImporte() {
        return insImporte;
    }

    public void setInsImporte(BigDecimal insImporte) {
        this.insImporte = insImporte;
    }

    public BigDecimal getInsSegImporte() {
        return insSegImporte;
    }

    public void setInsSegImporte(BigDecimal insSegImporte) {
        this.insSegImporte = insSegImporte;
    }

    public BigDecimal getCoste() {
        return coste;
    }

    public void setCoste(BigDecimal coste) {
        this.coste = coste;
    }

    public BigDecimal getImpteConcedido() {
        return impteConcedido;
    }

    public void setImpteConcedido(BigDecimal impteConcedido) {
        this.impteConcedido = impteConcedido;
    }
    
    public Integer getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Integer solicitud) {
        this.solicitud = solicitud;
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

    public String getNif_Carga() {
        return nif_Carga;
    }

    public void setNif_Carga(String nif_Carga) {
        this.nif_Carga = nif_Carga;
    }

    public String getNombre_Carga() {
        return nombre_Carga;
    }

    public void setNombre_Carga(String nombre_Carga) {
        this.nombre_Carga = nombre_Carga;
    }

    public Date getFecIni_Carga() {
        return fecIni_Carga;
    }

    public void setFecIni_Carga(Date fecIni_Carga) {
        this.fecIni_Carga = fecIni_Carga;
    }

    public Date getFecFin_Carga() {
        return fecFin_Carga;
    }

    public void setFecFin_Carga(Date fecFin_Carga) {
        this.fecFin_Carga = fecFin_Carga;
    }

    public BigDecimal getHorasJC_Carga() {
        return horasJC_Carga;
    }

    public void setHorasJC_Carga(BigDecimal horasJC_Carga) {
        this.horasJC_Carga = horasJC_Carga;
    }

    public BigDecimal getHorasCont_Carga() {
        return horasCont_Carga;
    }

    public void setHorasCont_Carga(BigDecimal horasCont_Carga) {
        this.horasCont_Carga = horasCont_Carga;
    }

    public BigDecimal getHorasEca_Carga() {
        return horasEca_Carga;
    }

    public void setHorasEca_Carga(BigDecimal horasEca_Carga) {
        this.horasEca_Carga = horasEca_Carga;
    }

    public BigDecimal getImpSSJC_Carga() {
        return impSSJC_Carga;
    }

    public void setImpSSJC_Carga(BigDecimal impSSJC_Carga) {
        this.impSSJC_Carga = impSSJC_Carga;
    }

    public BigDecimal getImpSSJR_Carga() {
        return impSSJR_Carga;
    }

    public void setImpSSJR_Carga(BigDecimal impSSJR_Carga) {
        this.impSSJR_Carga = impSSJR_Carga;
    }

    public BigDecimal getImpSSECA_Carga() {
        return impSSECA_Carga;
    }

    public void setImpSSECA_Carga(BigDecimal impSSECA_Carga) {
        this.impSSECA_Carga = impSSECA_Carga;
    }

    public Integer getSegAnt_Carga() {
        return segAnt_Carga;
    }

    public void setSegAnt_Carga(Integer segAnt_Carga) {
        this.segAnt_Carga = segAnt_Carga;
    }

    public BigDecimal getImpSegAnt_Carga() {
        return impSegAnt_Carga;
    }

    public void setImpSegAnt_Carga(BigDecimal impSegAnt_Carga) {
        this.impSegAnt_Carga = impSegAnt_Carga;
    }

    public BigDecimal getInsC1H_Carga() {
        return insC1H_Carga;
    }

    public void setInsC1H_Carga(BigDecimal insC1H_Carga) {
        this.insC1H_Carga = insC1H_Carga;
    }

    public BigDecimal getInsC1M_Carga() {
        return insC1M_Carga;
    }

    public void setInsC1M_Carga(BigDecimal insC1M_Carga) {
        this.insC1M_Carga = insC1M_Carga;
    }

    public BigDecimal getInsC1_Carga() {
        return insC1_Carga;
    }

    public void setInsC1_Carga(BigDecimal insC1_Carga) {
        this.insC1_Carga = insC1_Carga;
    }

    public BigDecimal getInsC2H_Carga() {
        return insC2H_Carga;
    }

    public void setInsC2H_Carga(BigDecimal insC2H_Carga) {
        this.insC2H_Carga = insC2H_Carga;
    }

    public BigDecimal getInsC2M_Carga() {
        return insC2M_Carga;
    }

    public void setInsC2M_Carga(BigDecimal insC2M_Carga) {
        this.insC2M_Carga = insC2M_Carga;
    }

    public BigDecimal getInsC2_Carga() {
        return insC2_Carga;
    }

    public void setInsC2_Carga(BigDecimal insC2_Carga) {
        this.insC2_Carga = insC2_Carga;
    }

    public BigDecimal getInsC3H_Carga() {
        return insC3H_Carga;
    }

    public void setInsC3H_Carga(BigDecimal insC3H_Carga) {
        this.insC3H_Carga = insC3H_Carga;
    }

    public BigDecimal getInsC3M_Carga() {
        return insC3M_Carga;
    }

    public void setInsC3M_Carga(BigDecimal insC3M_Carga) {
        this.insC3M_Carga = insC3M_Carga;
    }

    public BigDecimal getInsC3_Carga() {
        return insC3_Carga;
    }

    public void setInsC3_Carga(BigDecimal insC3_Carga) {
        this.insC3_Carga = insC3_Carga;
    }

    public BigDecimal getInsC4H_Carga() {
        return insC4H_Carga;
    }

    public void setInsC4H_Carga(BigDecimal insC4H_Carga) {
        this.insC4H_Carga = insC4H_Carga;
    }

    public BigDecimal getInsC4M_Carga() {
        return insC4M_Carga;
    }

    public void setInsC4M_Carga(BigDecimal insC4M_Carga) {
        this.insC4M_Carga = insC4M_Carga;
    }

    public BigDecimal getInsC4_Carga() {
        return insC4_Carga;
    }

    public void setInsC4_Carga(BigDecimal insC4_Carga) {
        this.insC4_Carga = insC4_Carga;
    }

    public BigDecimal getInsImporte_Carga() {
        return insImporte_Carga;
    }

    public void setInsImporte_Carga(BigDecimal insImporte_Carga) {
        this.insImporte_Carga = insImporte_Carga;
    }

    public BigDecimal getInsSegImporte_Carga() {
        return insSegImporte_Carga;
    }

    public void setInsSegImporte_Carga(BigDecimal insSegImporte_Carga) {
        this.insSegImporte_Carga = insSegImporte_Carga;
    }

    public BigDecimal getCoste_Carga() {
        return coste_Carga;
    }

    public void setCoste_Carga(BigDecimal coste_Carga) {
        this.coste_Carga = coste_Carga;
    }

    public String getNifPreparadorSustituido() {
        return nifPreparadorSustituido;
    }

    public void setNifPreparadorSustituido(String nifPreparadorSustituido) {
        this.nifPreparadorSustituido = nifPreparadorSustituido;
    }
    
    
    
    
    //Este se hace para ordenarlos en el proceso de carga desde Excel
    //Se ordenaran por nif, y en caso de ser sustituto primero por nif del sustituido y luego por nif propio
    public int compareTo(Object o)
    {
        if(o instanceof EcaSolPreparadoresVO)
        {
            EcaSolPreparadoresVO aux = (EcaSolPreparadoresVO)o;
            if(this.getNifPreparadorSustituido() != null && aux.getNifPreparadorSustituido() != null)
            {
                if(this.getNifPreparadorSustituido().equalsIgnoreCase(aux.getNifPreparadorSustituido()))
                {
                    //Si los dos sustituyen al mismo, ordenamos por el nif de cada uno
                    return compararPorNif(aux);
                }
                else
                {
                    //Si sustituyen a diferentes prospectores, ordenamos por el nif del prospector sustituido
                    return this.getNifPreparadorSustituido().compareToIgnoreCase(aux.getNifPreparadorSustituido());
                }
            }
            else if(this.getNifPreparadorSustituido() == null && aux.getNifPreparadorSustituido() == null)
            {
                return compararPorNif(aux);
            }
            else if(this.getNifPreparadorSustituido() != null)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }
    
    private int compararPorNif(EcaSolPreparadoresVO aux)
    {
        if(this.getNif() != null && aux.getNif() != null)
        {
            return this.getNif().compareToIgnoreCase(aux.getNif());
        }
        else if(this.getNif() == null && aux.getNif() == null)
        {
            return 0;
        }
        else if(this.getNif() != null)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }
}
