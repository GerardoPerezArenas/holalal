/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes;

import es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35;

/**
 *
 * @author santiagoc
 */
public class FilaInformeDesglose implements Comparable
{
    private String numExp;
    
    private String codProvincia;
    private String descProvincia;
    
    private String entidadPromotora;
    
    private String empresa;
   
    private Integer hInd_psi_65;
    private Integer mInd_psi_65;
    private Integer hTemp_psi_65;
    private Integer mTemp_psi_65;
    private Integer hInd_psi_33_65;
    private Integer mInd_psi_33_65;
    private Integer hTemp_psi_33_65;
    private Integer mTemp_psi_33_65;
   
    private Integer hInd_fis_65;
    private Integer mInd_fis_65;
    private Integer hTemp_fis_65;
    private Integer mTemp_fis_65;
    
    private Integer hInd_sens_mas_33;
    private Integer mInd_sens_mas_33;
    private Integer hTemp_sens_mas_33;
    private Integer mTemp_sens_mas_33;
    
    public FilaInformeDesglose()
    {
        numExp = "";
        
        empresa = "-";

        hInd_psi_65 = 0;
        mInd_psi_65 = 0;
        hTemp_psi_65 = 0;
        mTemp_psi_65 = 0;
        hInd_psi_33_65 = 0;
        mInd_psi_33_65 = 0;
        hTemp_psi_33_65 = 0;
        mTemp_psi_33_65 = 0;

        hInd_fis_65 = 0;
        mInd_fis_65 = 0;
        hTemp_fis_65 = 0;
        mTemp_fis_65 = 0;
    
        hInd_sens_mas_33 = 0;
        mInd_sens_mas_33 = 0;
        hTemp_sens_mas_33 = 0;
        mTemp_sens_mas_33 = 0;
    }

    public String getNumExp() {
        return numExp;
    }

    public void setNumExp(String numExp) {
        this.numExp = numExp;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Integer gethInd_psi_65() {
        return hInd_psi_65;
    }

    public void sethInd_psi_65(Integer hInd_psi_65) {
        this.hInd_psi_65 = hInd_psi_65;
    }

    public Integer getmInd_psi_65() {
        return mInd_psi_65;
    }

    public void setmInd_psi_65(Integer mInd_psi_65) {
        this.mInd_psi_65 = mInd_psi_65;
    }

    public Integer gethTemp_psi_65() {
        return hTemp_psi_65;
    }

    public void sethTemp_psi_65(Integer hTemp_psi_65) {
        this.hTemp_psi_65 = hTemp_psi_65;
    }

    public Integer getmTemp_psi_65() {
        return mTemp_psi_65;
    }

    public void setmTemp_psi_65(Integer mTemp_psi_65) {
        this.mTemp_psi_65 = mTemp_psi_65;
    }

    public Integer gethInd_psi_33_65() {
        return hInd_psi_33_65;
    }

    public void sethInd_psi_33_65(Integer hInd_psi_33_65) {
        this.hInd_psi_33_65 = hInd_psi_33_65;
    }

    public Integer getmInd_psi_33_65() {
        return mInd_psi_33_65;
    }

    public void setmInd_psi_33_65(Integer mInd_psi_33_65) {
        this.mInd_psi_33_65 = mInd_psi_33_65;
    }

    public Integer gethTemp_psi_33_65() {
        return hTemp_psi_33_65;
    }

    public void sethTemp_psi_33_65(Integer hTemp_psi_33_65) {
        this.hTemp_psi_33_65 = hTemp_psi_33_65;
    }

    public Integer getmTemp_psi_33_65() {
        return mTemp_psi_33_65;
    }

    public void setmTemp_psi_33_65(Integer mTemp_psi_33_65) {
        this.mTemp_psi_33_65 = mTemp_psi_33_65;
    }

    public Integer gethInd_fis_65() {
        return hInd_fis_65;
    }

    public void sethInd_fis_65(Integer hInd_fis_65) {
        this.hInd_fis_65 = hInd_fis_65;
    }

    public Integer getmInd_fis_65() {
        return mInd_fis_65;
    }

    public void setmInd_fis_65(Integer mInd_fis_65) {
        this.mInd_fis_65 = mInd_fis_65;
    }

    public Integer gethTemp_fis_65() {
        return hTemp_fis_65;
    }

    public void sethTemp_fis_65(Integer hTemp_fis_65) {
        this.hTemp_fis_65 = hTemp_fis_65;
    }

    public Integer getmTemp_fis_65() {
        return mTemp_fis_65;
    }

    public void setmTemp_fis_65(Integer mTemp_fis_65) {
        this.mTemp_fis_65 = mTemp_fis_65;
    }

    public String getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(String codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getDescProvincia() {
        return descProvincia;
    }

    public void setDescProvincia(String descProvincia) {
        this.descProvincia = descProvincia;
    }

    public Integer gethInd_sens_mas_33() {
        return hInd_sens_mas_33;
    }

    public void sethInd_sens_mas_33(Integer hInd_sens_mas_33) {
        this.hInd_sens_mas_33 = hInd_sens_mas_33;
    }

    public Integer getmInd_sens_mas_33() {
        return mInd_sens_mas_33;
    }

    public void setmInd_sens_mas_33(Integer mInd_sens_mas_33) {
        this.mInd_sens_mas_33 = mInd_sens_mas_33;
    }

    public Integer gethTemp_sens_mas_33() {
        return hTemp_sens_mas_33;
    }

    public void sethTemp_sens_mas_33(Integer hTemp_sens_mas_33) {
        this.hTemp_sens_mas_33 = hTemp_sens_mas_33;
    }

    public Integer getmTemp_sens_mas_33() {
        return mTemp_sens_mas_33;
    }

    public void setmTemp_sens_mas_33(Integer mTemp_sens_mas_33) {
        this.mTemp_sens_mas_33 = mTemp_sens_mas_33;
    }

    public String getEntidadPromotora() {
        return entidadPromotora;
    }

    public void setEntidadPromotora(String entidadPromotora) {
        this.entidadPromotora = entidadPromotora;
    }

    public int compareTo(Object o) 
    {
        if(o instanceof FilaInformeDesglose)
        {
            FilaInformeDesglose aux = (FilaInformeDesglose)o;
            if(this.getCodProvincia() != null && !this.getCodProvincia().equals("") && aux.getCodProvincia() != null && !aux.getCodProvincia().equals(""))
            {
                if(this.getCodProvincia().compareTo(aux.getCodProvincia()) == 0)
                {
                    if(this.getEntidadPromotora() != null && !this.getEntidadPromotora().equals("") && aux.getEntidadPromotora() != null && !aux.getEntidadPromotora().equals(""))
                    {
                        return this.getEntidadPromotora().compareTo(aux.getEntidadPromotora());
                    }
                    else if((this.getEntidadPromotora() == null || this.getEntidadPromotora().equals("")) && (aux.getEntidadPromotora() == null || aux.getEntidadPromotora().equals("")))
                    {
                        return 0;
                    }
                    else if(this.getEntidadPromotora() != null && !this.getEntidadPromotora().equals(""))
                    {
                        return -1;
                    }
                    else
                    {
                        return 1;
                    }
                }
                else
                {
                    return this.getCodProvincia().compareTo(aux.getCodProvincia());
                }
            }
            else if((this.getCodProvincia() == null || this.getCodProvincia().equals("")) && (aux.getCodProvincia() == null || aux.getCodProvincia().equals("")))
            {
                if(this.getEntidadPromotora() != null && !this.getEntidadPromotora().equals("") && aux.getEntidadPromotora() != null && !aux.getEntidadPromotora().equals(""))
                {
                    return this.getEntidadPromotora().compareTo(aux.getEntidadPromotora());
                }
                else if((this.getEntidadPromotora() == null || this.getEntidadPromotora().equals("")) && (aux.getEntidadPromotora() == null || aux.getEntidadPromotora().equals("")))
                {
                    return 0;
                }
                else if(this.getEntidadPromotora() != null && !this.getEntidadPromotora().equals(""))
                {
                    return -1;
                }
                else
                {
                    return 1;
                }
            }
            else if(this.getCodProvincia() != null && !this.getCodProvincia().equals(""))
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return -1;
        }
    }
}
