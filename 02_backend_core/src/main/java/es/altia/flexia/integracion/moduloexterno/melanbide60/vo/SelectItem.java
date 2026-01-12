/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide60.vo;

/**
 *
 * @author santiagoc
 */
public class SelectItem implements Comparable
{
    private String codigo;
    private String descripcion;
    private int modoOrdenacion = ORDENAR_POR_DESCRIPCION;
    
    public static final int ORDENAR_POR_CODIGO = 0;
    public static final int ORDENAR_POR_DESCRIPCION = 1;
    
    public SelectItem()
    {
        
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getModoOrdenacion() {
        return modoOrdenacion;
    }

    public void setModoOrdenacion(int modoOrdenacion) {
        this.modoOrdenacion = modoOrdenacion;
    }

    public int compareTo(Object o) 
    {
        if(o instanceof SelectItem)
        {
            SelectItem aux = (SelectItem)o;
            if(this.getModoOrdenacion() == ORDENAR_POR_DESCRIPCION && aux.getModoOrdenacion() == ORDENAR_POR_DESCRIPCION)
            {
                if(this.getDescripcion() != null && aux.getDescripcion() != null)
                {
                    return this.getDescripcion().compareToIgnoreCase(aux.getDescripcion());
                }
                else if(this.getDescripcion() == null && aux.getDescripcion() == null)
                {
                    return 0;
                }
                else if(this.getDescripcion() != null)
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
            else if(this.getModoOrdenacion() == ORDENAR_POR_CODIGO && aux.getModoOrdenacion() == ORDENAR_POR_CODIGO)
            {
                if(this.getCodigo() != null && aux.getCodigo() != null)
                {
                    boolean sonNumericos = false;
                    Integer i1 = null, i2 = null;
                    try
                    {
                        i1 = Integer.parseInt(this.getCodigo());
                        i2 = Integer.parseInt(aux.getCodigo());
                        sonNumericos = true;
                    }
                    catch(Exception ex)
                    {

                    }
                    if(sonNumericos)
                    {
                        return i1.compareTo(i2);
                    }
                    else
                    {
                        return this.getCodigo().compareToIgnoreCase(aux.getCodigo());
                    }
                }
                else if(this.getCodigo() == null && aux.getCodigo() == null)
                {
                    return 0;
                }
                else if(this.getCodigo() != null)
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
        }
        return -1;
    }
}
