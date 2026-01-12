package es.altia.flexia.integracion.moduloexterno.melanbide12.vo;

/*
 CREATE SEQUENCE  SEQ_M12_PERS_PRACT_LIN2  MINVALUE 0 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

         CREATE TABLE MELANBIDE12_PERS_PRACT_LIN2
         (	ID NUMBER(8,0),
         NUM_EXP VARCHAR2(20 BYTE),
         TIPO_DOC VARCHAR2(1),
         DOC VARCHAR2(15 BYTE),
         NOMBRE VARCHAR2(150),
         APE1 VARCHAR2(150),
         APE2 VARCHAR2(150),
         COD_ACT_FORM VARCHAR2(50),
         HORAS_PRACT NUMBER(6,2),
         IMP_SOLIC NUMBER(8,2)
         );

         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.ID IS 'Identificador Secuencial';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.NUM_EXP IS 'Número de Expediente';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.TIPO_DOC IS 'Tipo de identificación - DESPLEGABLE TDOC: valores D: DNI, N: NIE';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.DOC IS 'Número de documento de la persona en prácticas';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.NOMBRE IS 'Nombre de la persona en prácticas';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.APE1 IS 'Apellido1 de la persona en prácticas';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.APE2 IS 'Apellido2 de la persona en prácticas';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.COD_ACT_FORM IS 'Código acción formativa';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.HORAS_PRACT IS 'Horas prácticas';
         COMMENT ON COLUMN MELANBIDE12_PERS_PRACT_LIN2.IMP_SOLIC IS 'Importe solicitado';

         ALTER TABLE MELANBIDE12_PERS_PRACT_LIN2 ADD PRIMARY KEY (ID);
*/
public class FilaL2ParticipanteVO {
    private Integer id;
    private String numExp;

    private String tipoDoc;
    private String desTipoDoc;
    private String doc;
    private String nombre;
    private String ape1;
    private String ape2;
    private String codActForm;
    private Double horasPract;
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

    public String getCodActForm() {
        return codActForm;
    }

    public void setCodActForm(String codActForm) {
        this.codActForm = codActForm;
    }

    public Double getHorasPract() {
        return horasPract;
    }

    public void setHorasPract(Double horasPract) {
        this.horasPract = horasPract;
    }

    public Double getImpSolic() {
        return impSolic;
    }

    public void setImpSolic(Double impSolic) {
        this.impSolic = impSolic;
    }

}
