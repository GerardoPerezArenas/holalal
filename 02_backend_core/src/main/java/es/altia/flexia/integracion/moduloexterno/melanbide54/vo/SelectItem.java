/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide54.vo;

/**
 *
 * @author santiagoc
 */
public class SelectItem implements Comparable
{
    private Object id;
    private String label;
    private Integer codPrv;
    
    public SelectItem()
    {
        
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getCodPrv() {
        return codPrv;
    }

    public void setCodPrv(Integer codPrv) {
        this.codPrv = codPrv;
    }

    public int compareTo(Object o) 
    {
        if(o instanceof SelectItem)
        {
            SelectItem aux = (SelectItem)o;
            if(this.getLabel() != null && aux.getLabel() != null)
            {
                return this.getLabel().compareToIgnoreCase(aux.getLabel());
            }
            else if(this.getLabel() == null && aux.getLabel() == null)
            {
                return 0;
            }
            else if(this.getLabel() != null)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
        return -1;
    }
}
