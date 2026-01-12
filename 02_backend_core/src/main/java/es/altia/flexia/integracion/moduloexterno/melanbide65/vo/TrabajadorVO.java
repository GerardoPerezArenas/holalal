package es.altia.flexia.integracion.moduloexterno.melanbide65.vo;

public class TrabajadorVO extends PersonaVO {
    private int tipoDiscFisica;
    private int tipoDiscPsiquica;
     private int tipoDiscSensorial;
    private String tipoContrato;
    private String descTipoContrato;
    private String tipoJornada;
    private String descTipoJornada;
    private double jorParcPorc;
    private int sexo;   
    private String descSexo;
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
     * @return the tipoDiscSensorial
     */
    public int getTipoDiscSensorial() {
        return tipoDiscSensorial;
    }
    
       /**
     * @param tipoDiscSensorial the tipoDiscSensorial to set
     */

    public void setTipoDiscSensorial(int tipoDiscSensorial) {
        this.tipoDiscSensorial = tipoDiscSensorial;
    }
    
    
    /**
     * @return the sexo
     */
    public int getSexo() {
        return sexo;
    }

    /**
     * @param sexo the sexo to set
     */
    public void setSexo(int sexo) {
        this.sexo = sexo;
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
     * @return the jorParcPorc - Porcentaje de jornada a tiempo parcial
     */
    
    public double getJorParcPorc() {
        return jorParcPorc;
    }

    
       /**
     * @param jorParcPorc the jorParcPorc - Porcentaje de jornada a tiempo parcial to set
     */

    
    public void setJorParcPorc(double jorParcPorc) {
        this.jorParcPorc = jorParcPorc;
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

    /**
     * @return the tipoDiscFisica
     */
    public int getTipoDiscFisica() {
        return tipoDiscFisica;
    }

    /**
     * @param tipoDiscFisica the tipoDiscFisica to set
     */
    public void setTipoDiscFisica(int tipoDiscFisica) {
        this.tipoDiscFisica = tipoDiscFisica;
    }

    /**
     * @return the tipoDiscPsiquica
     */
    public int getTipoDiscPsiquica() {
        return tipoDiscPsiquica;
    }

    /**
     * @param tipoDiscPsiquica the tipoDiscPsiquica to set
     */
    public void setTipoDiscPsiquica(int tipoDiscPsiquica) {
        this.tipoDiscPsiquica = tipoDiscPsiquica;
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

    public String getDescSexo() {
        return descSexo;
    }

    public void setDescSexo(String descSexo) {
        this.descSexo = descSexo;
    }

    
}
