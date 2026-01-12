
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud;

import java.math.BigDecimal;

/**
 *
 * @author alexandrep
 */
public class FilaPreparadorECA23VO {
    
    private Integer id;
    private String numeroExpediente;
    private String nombre;
    private String dni;
    private BigDecimal horasECA;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public BigDecimal getHorasECA() {
        return horasECA;
    }

    public void setHorasECA(BigDecimal horasECA) {
        this.horasECA = horasECA;
    }
    

    
    
}
