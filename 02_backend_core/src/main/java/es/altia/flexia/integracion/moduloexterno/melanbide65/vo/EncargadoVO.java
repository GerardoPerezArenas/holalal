package es.altia.flexia.integracion.moduloexterno.melanbide65.vo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase con propiedades comunes a TecnicoVO, por eso este hereda de EncargadoVO. Puede haber instancias de EncargadoVO
 * @author Mila
 */
public class EncargadoVO extends PersonaVO{
    private Date fecAltaContrIndef;
    private double jornadaParcialPor;
    private String tipoContrato;
    private String tipoJornada;
    private Date fecAltaContrTemp;
    private Date fecBajaContrTemp;
   private String descTipoContrato;
    private String descTipoJornada;
        private String pensionista;
    private String tipoPensionista;
        private String descPensionista;
    private String descTipoPensionista;

    public String getDescPensionista() {
        return descPensionista;
    }

    public void setDescPensionista(String descPensionista) {
        this.descPensionista = descPensionista;
    }

    public String getDescTipoPensionista() {
        return descTipoPensionista;
    }

    public void setDescTipoPensionista(String descTipoPensionista) {
        this.descTipoPensionista = descTipoPensionista;
    }
    
    

    public String getPensionista() {
        return pensionista;
    }

    public void setPensionista(String pensionista) {
        this.pensionista = pensionista;
    }

    public String getTipoPensionista() {
        return tipoPensionista;
    }

    public void setTipoPensionista(String tipoPensionista) {
        this.tipoPensionista = tipoPensionista;
    }
    
    
       /**
     * @return the fecAltaContrTemp
     */
    
    
    public Date getFecAltaContrTemp() {
        return fecAltaContrTemp;
    }
    
    /**
     * @param fecAltaContrTemp the fecAltaContrTemp to set
     */

    public void setFecAltaContrTemp(Date fecAltaContrTemp) {
        this.fecAltaContrTemp = fecAltaContrTemp;
    }
    
      /**
     * Devuelve fecha como String  con formato dd/MM/yyyy
     */
    public String getFecAltaContrTempAsStr(){
        try{
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecAltaContrTemp);
        }catch(NullPointerException e){
            return "";
        }
    }

    
       /**
     * @return the fecBajaContrTemp
     */
    
    
    public Date getFecBajaContrTemp() {
        return fecBajaContrTemp;
    }

   /**
     * @param fecBajaContrTemp the fecBajaContrTemp to set
     */
    
    public void setFecBajaContrTemp(Date fecBajaContrTemp) {
        this.fecBajaContrTemp = fecBajaContrTemp;
    }
    
      /**
     * Devuelve fecha como String  con formato dd/MM/yyyy
     */
    public String getFecBajaContrTempAsStr(){
        try{
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecBajaContrTemp);
          }catch(NullPointerException e){
            return "";
        }
    }
    
    /**
     * @return the fecAltaContrIndef
     */
    public Date getFecAltaContrIndef() {
        return fecAltaContrIndef;
    }
    
    /**
     * Devuelve fecha como String  con formato dd/MM/yyyy
     */
    public String getFecAltaAsStr(){
        try{
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecAltaContrIndef);
        }catch(NullPointerException e){
            return "";
        }
    }

    /**
     * @param fecAltaContrIndef the fecAltaContrIndef to set
     */
    public void setFecAltaContrIndef(Date fecAltaContrIndef) {
        this.fecAltaContrIndef = fecAltaContrIndef;
    }


    /**
     * @return the jornadaParcialPor
     */
    public double getJornadaParcialPor() {
        return jornadaParcialPor;
    }

    /**
     * @param jornadaParcialPor the jornadaParcialPor to set
     */
    public void setJornadaParcialPor(double jornadaParcialPor) {
        this.jornadaParcialPor = jornadaParcialPor;
    }


     /**
     * @return the tipoJornada
     */
    
    public String getTipoJornada() {
        return tipoJornada;
    }

    
       /**
     * @param tipoJornada the tipoJornada to set
     */
    
    public void setTipoJornada(String tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

    
    
      /**
     * @return the tipoContrato
     */
    
    public String getTipoContrato() {
        return tipoContrato;
    }
    
    /**
     * @param tipoContrato the tipoContrato to set
     */

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

       public String getDescTipoContrato() {
        return descTipoContrato;
    }

    public void setDescTipoContrato(String descTipoContrato) {
        this.descTipoContrato = descTipoContrato;
    }

    public String getDescTipoJornada() {
        return descTipoJornada;
    }

    public void setDescTipoJornada(String descTipoJornada) {
        this.descTipoJornada = descTipoJornada;
    }
    
}
