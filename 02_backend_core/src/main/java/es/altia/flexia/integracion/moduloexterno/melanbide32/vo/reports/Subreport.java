/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.vo.reports;

import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author SantiagoC
 */
public class Subreport 
{
    private JasperReport subreport;

    private List datasource;


    private Map subreportParams;

    public Subreport(JasperReport subreport, List dataSource, Map subreportParams)
    {
        this.datasource = dataSource;
        this.subreport = subreport;
        this.subreportParams = subreportParams;
    }

    public JasperReport getSubreport() {
        return subreport;
    }

    public void setSubreport(JasperReport subreport) {
        this.subreport = subreport;
    }

    public List getDatasource() {
        return datasource;
    }

    public void setDatasource(List dataSource) {
        this.datasource = dataSource;
    }

    public Map getSubreportParams() {
        return subreportParams;
    }

    public void setSubreportParams(Map subreportParams) {
        this.subreportParams = subreportParams;
    }    
}
