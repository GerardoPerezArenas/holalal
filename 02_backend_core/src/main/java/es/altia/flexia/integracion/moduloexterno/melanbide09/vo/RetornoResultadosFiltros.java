
package es.altia.flexia.integracion.moduloexterno.melanbide09.vo;

import java.util.List;

/**
 *
 * @author alexandrep
 */
public class RetornoResultadosFiltros {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<ResultadoConsultaVO> lstRegistros;
    
    
    
    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<ResultadoConsultaVO> getLstRegistros() {
        return lstRegistros;
    }

    public void setLstRegistros(List<ResultadoConsultaVO> lstRegistros) {
        this.lstRegistros = lstRegistros;
    }
    
    
}
