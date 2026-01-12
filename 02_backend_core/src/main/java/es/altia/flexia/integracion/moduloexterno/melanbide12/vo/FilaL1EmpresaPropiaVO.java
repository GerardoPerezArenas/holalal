package es.altia.flexia.integracion.moduloexterno.melanbide12.vo;

/*
 CREATE SEQUENCE  SEQ_M12_EMPR_PROP_LIN1  MINVALUE 0 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

   CREATE TABLE MELANBIDE12_EMPR_PROP_LIN1
   (	ID NUMBER(8,0),
	NUM_EXP VARCHAR2(20 BYTE),
	DNI VARCHAR2(15 BYTE),
	NOMBRE VARCHAR2(150),
	APE1 VARCHAR2(150),
	APE2 VARCHAR2(150),
	RETR_ANUAL_BRUTA NUMBER(8,2),
	CC_COT_SS NUMBER(8,2),
	HORAS_LAB_ANUAL NUMBER(6,2),
	HORAS_IMPUT NUMBER(6,2),
	IMP_GEST NUMBER(8,2),
	PERSON_PRACT NUMBER(6,2),
	IMP_SOLIC NUMBER(8,2)
   );

   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.ID IS 'Identificador Secuencial';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.NUM_EXP IS 'Número de Expediente';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.DNI IS 'DNI trabajador';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.NOMBRE IS 'Nombre trabajador';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.APE1 IS 'Apellido1 trabajador';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.APE2 IS 'Apellido2 trabajador';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.RETR_ANUAL_BRUTA IS 'Retribución anual bruta';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.CC_COT_SS IS 'Coste cotización Seguridad Solcial';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.HORAS_LAB_ANUAL IS 'Horas jornada laboral anual';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.HORAS_IMPUT IS 'Horas imputadas gestiones';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.IMP_GEST IS 'Importe económico gestiones';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.PERSON_PRACT IS 'Número personas en prácticas';
   COMMENT ON COLUMN MELANBIDE12_EMPR_PROP_LIN1.IMP_SOLIC IS 'Importe solicitado';

   ALTER TABLE MELANBIDE12_EMPR_PROP_LIN1 ADD PRIMARY KEY (ID);
*/
public class FilaL1EmpresaPropiaVO {
    private Integer id;
    private String numExp;

    private String dni;
    private String nombre;
    private String ape1;
    private String ape2;
    private Double retrAnualBruta;
    private Double ccCotSs;
    private Double horasLabAnual;
    private Double horasImput;
    private Double impGest;
    private Double personPract;
    private Double impSolic;

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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApe1() {
        return ape1;
    }

    public void setApe1(String ape1) {
        this.ape1 = ape1;
    }

    public String getApe2() {
        return ape2;
    }

    public void setApe2(String ape2) {
        this.ape2 = ape2;
    }

    public Double getRetrAnualBruta() {
        return retrAnualBruta;
    }

    public void setRetrAnualBruta(Double retrAnualBruta) {
        this.retrAnualBruta = retrAnualBruta;
    }

    public Double getCcCotSs() {
        return ccCotSs;
    }

    public void setCcCotSs(Double ccCotSs) {
        this.ccCotSs = ccCotSs;
    }

    public Double getHorasLabAnual() {
        return horasLabAnual;
    }

    public void setHorasLabAnual(Double horasLabAnual) {
        this.horasLabAnual = horasLabAnual;
    }

    public Double getHorasImput() {
        return horasImput;
    }

    public void setHorasImput(Double horasImput) {
        this.horasImput = horasImput;
    }

    public Double getImpGest() {
        return impGest;
    }

    public void setImpGest(Double impGest) {
        this.impGest = impGest;
    }

    public Double getPersonPract() {
        return personPract;
    }

    public void setPersonPract(Double personPract) {
        this.personPract = personPract;
    }

    public Double getImpSolic() {
        return impSolic;
    }

    public void setImpSolic(Double impSolic) {
        this.impSolic = impSolic;
    }

}
