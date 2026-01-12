/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author SantiagoC
 */
public class EcaSolProspectoresVO implements Comparable
{
    private Integer solProspectoresCod;
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
    private Integer visitas;
    private BigDecimal visitasImp;
    private BigDecimal coste;
    private BigDecimal impteConcedido;
    private Integer solicitud;
    private String tipoSust;
    private Integer solProspectorOrigen;
    
    //Este se utiliza para la carga desde excel pero no persiste en BD
    private String nifProspectorSustituido;
    
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
    private Integer visitas_Carga;
    private BigDecimal visitasImp_Carga;
    private BigDecimal coste_Carga;
    
    public EcaSolProspectoresVO()
    {
        
    }

    public Integer getSolProspectoresCod() {
        return solProspectoresCod;
    }

    public void setSolProspectoresCod(Integer solProspectoresCod) {
        this.solProspectoresCod = solProspectoresCod;
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

    public Integer getVisitas() {
        return visitas;
    }

    public void setVisitas(Integer visitas) {
        this.visitas = visitas;
    }

    public BigDecimal getVisitasImp() {
        return visitasImp;
    }

    public void setVisitasImp(BigDecimal visitasImp) {
        this.visitasImp = visitasImp;
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

    public Integer getSolProspectorOrigen() {
        return solProspectorOrigen;
    }

    public void setSolProspectorOrigen(Integer solProspectorOrigen) {
        this.solProspectorOrigen = solProspectorOrigen;
    }

    public String getNifProspectorSustituido() {
        return nifProspectorSustituido;
    }

    public void setNifProspectorSustituido(String nifProspectorSustituido) {
        this.nifProspectorSustituido = nifProspectorSustituido;
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

    public Integer getVisitas_Carga() {
        return visitas_Carga;
    }

    public void setVisitas_Carga(Integer visitas_Carga) {
        this.visitas_Carga = visitas_Carga;
    }

    public BigDecimal getVisitasImp_Carga() {
        return visitasImp_Carga;
    }

    public void setVisitasImp_Carga(BigDecimal visitasImp_Carga) {
        this.visitasImp_Carga = visitasImp_Carga;
    }

    public BigDecimal getCoste_Carga() {
        return coste_Carga;
    }

    public void setCoste_Carga(BigDecimal coste_Carga) {
        this.coste_Carga = coste_Carga;
    }
    
    /*//Este se hace para ordenarlos en el proceso de carga desde Excel
    //Se ordenaran por nif, y en caso de ser sustituto primero por nif del sustituido y luego por nif propio
    public int compareTo(Object o)
    {
        if(o instanceof EcaSolProspectoresVO)
        {
            EcaSolProspectoresVO aux = (EcaSolProspectoresVO)o;
            
            System.out.println("COMPARANDO "+this.getNif()+" CON "+aux.getNif());
            if(this.getNifProspectorSustituido() == null && aux.getNifProspectorSustituido() == null)
            {
                //Si ninguno de los dos es sustituto, entonces ordenamos por el nif de cada uno
                if(this.getNif() != null && aux.getNif() != null)
                {
                    System.out.println("DEVUELVE 1");
                    return this.getNif().compareToIgnoreCase(aux.getNif());
                }
                else if(this.getNif() == null && aux.getNif() == null)
                {
                    System.out.println("DEVUELVE 2");
                    return 0;
                }
                else if(this.getNif() != null)
                {
                    System.out.println("DEVUELVE 3");
                    return -1;
                }
                else
                {
                    System.out.println("DEVUELVE 4");
                    return 1;
                }
            }
            else
            {
                if(this.getNifProspectorSustituido() != null && aux.getNifProspectorSustituido() != null)
                {
                    if(this.getNifProspectorSustituido().equalsIgnoreCase(aux.getNifProspectorSustituido()))
                    {
                        //Si los dos sustituyen al mismo, ordenamos por el nif de cada uno
                        if(this.getNif() != null && aux.getNif() != null)
                        {
                            System.out.println("DEVUELVE 5");
                            return this.getNif().compareToIgnoreCase(aux.getNif());
                        }
                        else if(this.getNif() == null && aux.getNif() == null)
                        {
                            System.out.println("DEVUELVE 6");
                            return 0;
                        }
                        else if(this.getNif() != null)
                        {
                            System.out.println("DEVUELVE 7");
                            return -1;
                        }
                        else
                        {
                            System.out.println("DEVUELVE 8");
                            return 1;
                        }
                    }
                    else
                    {
                        System.out.println("DEVUELVE 9");
                        //Si sustituyen a diferentes prospectores, ordenamos por el nif del prospector sustituido
                        return this.getNifProspectorSustituido().compareToIgnoreCase(aux.getNifProspectorSustituido());
                    }
                }
                else
                {
                    if(this.getNifProspectorSustituido() != null)
                    {
                        //Aqui this.nifProspectorSustituido != null y aux.getNifProspectorSustituido = null
                        if(aux.getNif() != null)
                        {
                            System.out.println("DEVUELVE 10");
                            //Si el nif del prospector sustituido va antes que el nif de aux, entonces this ira antes que aux
                            return this.getNifProspectorSustituido().compareToIgnoreCase(aux.getNif());
                        }
                        else
                        {
                            System.out.println("DEVUELVE 11");
                            return -1;
                        }
                    }
                    else
                    {
                        //Aqui this.nifProspectorSustituido = null y aux.getNifProspectorSustituido != null
                        if(this.getNif() != null)
                        {
                            System.out.println("DEVUELVE 12");
                            //Si el nif de this va antes que el nif del prospector sustituido de aux, entonces this ira antes que aux
                            return this.getNif().compareToIgnoreCase(aux.getNifProspectorSustituido());
                        }
                        else
                        {
                            System.out.println("DEVUELVE 13");
                            return 1;
                        }
                    }
                }
            }
        }
        else
        {
            System.out.println("DEVUELVE 14");
            return -1;
        }
    }*/
    
    
    
    
    //Este se hace para ordenarlos en el proceso de carga desde Excel
    //Se ordenaran por nif, y en caso de ser sustituto primero por nif del sustituido y luego por nif propio
    public int compareTo(Object o)
    {
        if(o instanceof EcaSolProspectoresVO)
        {
            EcaSolProspectoresVO aux = (EcaSolProspectoresVO)o;
            if(this.getNifProspectorSustituido() != null && aux.getNifProspectorSustituido() != null)
            {
                if(this.getNifProspectorSustituido().equalsIgnoreCase(aux.getNifProspectorSustituido()))
                {
                    //Si los dos sustituyen al mismo, ordenamos por el nif de cada uno
                    return compararPorNif(aux);
                }
                else
                {
                    //Si sustituyen a diferentes prospectores, ordenamos por el nif del prospector sustituido
                    return this.getNifProspectorSustituido().compareToIgnoreCase(aux.getNifProspectorSustituido());
                }
            }
            else if(this.getNifProspectorSustituido() == null && aux.getNifProspectorSustituido() == null)
            {
                return compararPorNif(aux);
            }
            else if(this.getNifProspectorSustituido() != null)
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
    
    private int compararPorNif(EcaSolProspectoresVO aux)
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
