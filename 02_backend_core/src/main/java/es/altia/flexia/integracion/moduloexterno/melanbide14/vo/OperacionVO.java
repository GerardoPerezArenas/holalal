package es.altia.flexia.integracion.moduloexterno.melanbide14.vo;

import java.sql.Date;

public class OperacionVO {

    private Integer id;
    private String numExp;
    private Integer numOper;
    private String nombreOper;
    private String prio;
    private String desPrio;
    private String lin1;
    private String desLin1;
    private String lin2;
    private String desLin2;
    private String lin3;
    private String desLin3;
    private Double impOper;

    public OperacionVO() {
    }

    public OperacionVO(Integer id, String numExp, Integer numOper, String nombreOper, String prio, String lin1,
            String lin2, String lin3, Double impOper) {
        this.id = id;
        this.numExp = numExp;
        this.numOper = numOper;
        this.nombreOper = nombreOper;
        this.prio = prio;
        this.lin1 = lin1;
        this.lin2 = lin2;
        this.lin3 = lin3;
        this.impOper = impOper;
    }
    

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public Integer getNumOper() {
        return numOper;
    }

    public void setNumOper(Integer numOper) {
        this.numOper = numOper;
    }

    public String getNombreOper() {
        return nombreOper;
    }

    public void setNombreOper(String nombreOper) {
        this.nombreOper = nombreOper;
    }

    public String getPrio() {
        return prio;
    }

    public void setPrio(String prio) {
        this.prio = prio;
    }

    public String getLin1() {
        return lin1;
    }

    public void setLin1(String lin1) {
        this.lin1 = lin1;
    }

    public String getLin2() {
        return lin2;
    }

    public void setLin2(String lin2) {
        this.lin2 = lin2;
    }

    public String getLin3() {
        return lin3;
    }

    public void setLin3(String lin3) {
        this.lin3 = lin3;
    }
    
    public Double getImpOper() {
        return impOper;
    }

    public void setImpOper(Double impOper) {
        this.impOper = impOper;
    }

    public String getDesPrio() {
        return desPrio;
    }

    public void setDesPrio(String desPrio) {
        this.desPrio = desPrio;
    }

    public String getDesLin1() {
        return desLin1;
    }

    public void setDesLin1(String desLin1) {
        this.desLin1 = desLin1;
    }

    public String getDesLin2() {
        return desLin2;
    }

    public void setDesLin2(String desLin2) {
        this.desLin2 = desLin2;
    }

    public String getDesLin3() {
        return desLin3;
    }

    public void setDesLin3(String desLin3) {
        this.desLin3 = desLin3;
    }
    
}
