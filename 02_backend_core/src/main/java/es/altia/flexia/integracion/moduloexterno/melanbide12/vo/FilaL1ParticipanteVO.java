package es.altia.flexia.integracion.moduloexterno.melanbide12.vo;

import java.sql.Date;

/*
   CREATE SEQUENCE  SEQ_M12_PERS_PRACT_LIN1  MINVALUE 0 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

   CREATE TABLE MELANBIDE12_PERS_PRACT_LIN1
   (	ID NUMBER(8,0),
	NUM_EXP VARCHAR2(20 BYTE),
	TIPO_DOC VARCHAR2(1),
	DOC VARCHAR2(15 BYTE),
	NOMBRE VARCHAR2(150),
	APE1 VARCHAR2(150),
	APE2 VARCHAR2(150),
	NSS VARCHAR2(50),
	COD_ACT_FORM VARCHAR2(50),
    FEC_INI_PRACT DATE,
    FEC_FIN_PRACT DATE,
	CC_COT VARCHAR2(50),
	DIAS_COT NUMBER(6,2),
	IMP_SOLIC NUMBER(8,2)
   );

   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.ID IS 'Identificador Secuencial';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.NUM_EXP IS 'Número de Expediente';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.TIPO_DOC IS 'Tipo de identificación - DESPLEGABLE TDOC: valores D: DNI, N: NIE';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.DOC IS 'Número de documento de la persona en prácticas';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.NOMBRE IS 'Nombre de la persona en prácticas';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.APE1 IS 'Apellido1 de la persona en prácticas';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.APE2 IS 'Apellido2 de la persona en prácticas';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.NSS IS 'Número Seguridad Social';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.COD_ACT_FORM IS 'Código acción formativa';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.FEC_INI_PRACT IS 'Fecha inicio prácticas';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.FEC_FIN_PRACT IS 'Fecha fin prácticas';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.CC_COT IS 'Cuenta cotización empresa-entidad';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.DIAS_COT IS 'Días cotizados';
   COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN1.IMP_SOLIC IS 'Importe solicitado';

   ALTER TABLE MELANBIDE12_PERS_PRACT_LIN1 ADD PRIMARY KEY (ID);
*/
public class FilaL1ParticipanteVO {
    private Integer id;
    private String numExp;

    private String tipoDoc;
    private String desTipoDoc;
    private String doc;
    private String nombre;
    private String ape1;
    private String ape2;
    private String nss;
    private String codActForm;
    private Date fecIniPract;
    private String fecIniPractStr;
    private Date fecFinPract;
    private String fecFinPractStr;
    private String ccCot;
    private Double diasCot;
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

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public String getDesTipoDoc() {
        return desTipoDoc;
    }

    public void setDesTipoDoc(String desTipoDoc) {
        this.desTipoDoc = desTipoDoc;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
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

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getCodActForm() {
        return codActForm;
    }

    public void setCodActForm(String codActForm) {
        this.codActForm = codActForm;
    }

    public Date getFecIniPract() {
        return fecIniPract;
    }

    public void setFecIniPract(Date fecIniPract) {
        this.fecIniPract = fecIniPract;
    }

    public String getFecIniPractStr() {
        return fecIniPractStr;
    }

    public void setFecIniPractStr(String fecIniPractStr) {
        this.fecIniPractStr = fecIniPractStr;
    }

    public Date getFecFinPract() {
        return fecFinPract;
    }

    public void setFecFinPract(Date fecFinPract) {
        this.fecFinPract = fecFinPract;
    }

    public String getFecFinPractStr() {
        return fecFinPractStr;
    }

    public void setFecFinPractStr(String fecFinPractStr) {
        this.fecFinPractStr = fecFinPractStr;
    }

    public String getCcCot() {
        return ccCot;
    }

    public void setCcCot(String ccCot) {
        this.ccCot = ccCot;
    }

    public Double getDiasCot() {
        return diasCot;
    }

    public void setDiasCot(Double diasCot) {
        this.diasCot = diasCot;
    }

    public Double getImpSolic() {
        return impSolic;
    }

    public void setImpSolic(Double impSolic) {
        this.impSolic = impSolic;
    }
}
