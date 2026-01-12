package es.altia.flexia.integracion.moduloexterno.melanbide12.vo;

import java.sql.Date;

/*
CREATE SEQUENCE  SEQ_M12_EMPR_EXT_LIN1  MINVALUE 0 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

   CREATE TABLE MELANBIDE12_EMPR_EXT_LIN1
   (	ID NUMBER(8,0),
	NUM_EXP VARCHAR2(20 BYTE),
	CIF VARCHAR2(15 BYTE),
	DENOM_EMPR VARCHAR2(200),
	N_FACTURA VARCHAR2(50),
    FEC_EMIS DATE,
    FEC_PAGO DATE,
	IMP_BASE NUMBER(8,2),
	IMP_IVA NUMBER(8,2),
	IMP_TOTAL NUMBER(8,2),
	PERSONAS NUMBER(6,2),
	IMP_PERSONA_FACT NUMBER(8,2),
	IMP_SOLIC NUMBER(8,2)
   );

   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.ID IS 'Identificador Secuencial';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.NUM_EXP IS 'Número de Expediente';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.CIF IS 'CIF empresa externa';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.DENOM_EMPR IS 'Denominación empresa externa (asesoría, gestoría, ...)';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.N_FACTURA IS 'Número de factura';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.FEC_EMIS IS 'Fecha emisión';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.FEC_PAGO IS 'Fecha pago';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.IMP_BASE IS 'Importe base';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.IMP_IVA IS 'Importe IVA';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.IMP_TOTAL IS 'Total importe';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.PERSONAS IS 'Número de personas gestiones';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.IMP_PERSONA_FACT IS 'Importe facturado por persona';
   COMMENT ON COLUMN MELANBIDE12_EMPR_EXT_LIN1.IMP_SOLIC IS 'Importe solicitado';

   ALTER TABLE MELANBIDE12_EMPR_EXT_LIN1 ADD PRIMARY KEY (ID);
*/
public class FilaL1EmpresaExternaVO {
    private Integer id;
    private String numExp;

    private String cif;
    private String denomEmpr;
    private String nFactura;
    private Date fecEmis;
    private String fecEmisStr;
    private Date fecPago;
    private String fecPagoStr;
    private Double impBase;
    private Double impIva;
    private Double impTotal;
    private Double personas;
    private Double impPersonaFact;
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

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getDenomEmpr() {
        return denomEmpr;
    }

    public void setDenomEmpr(String denomEmpr) {
        this.denomEmpr = denomEmpr;
    }

    public String getnFactura() {
        return nFactura;
    }

    public void setnFactura(String nFactura) {
        this.nFactura = nFactura;
    }

    public Date getFecEmis() {
        return fecEmis;
    }

    public void setFecEmis(Date fecEmis) {
        this.fecEmis = fecEmis;
    }

    public String getFecEmisStr() {
        return fecEmisStr;
    }

    public void setFecEmisStr(String fecEmisStr) {
        this.fecEmisStr = fecEmisStr;
    }

    public Date getFecPago() {
        return fecPago;
    }

    public void setFecPago(Date fecPago) {
        this.fecPago = fecPago;
    }

    public String getFecPagoStr() {
        return fecPagoStr;
    }

    public void setFecPagoStr(String fecPagoStr) {
        this.fecPagoStr = fecPagoStr;
    }

    public Double getImpBase() {
        return impBase;
    }

    public void setImpBase(Double impBase) {
        this.impBase = impBase;
    }

    public Double getImpIva() {
        return impIva;
    }

    public void setImpIva(Double impIva) {
        this.impIva = impIva;
    }

    public Double getImpTotal() {
        return impTotal;
    }

    public void setImpTotal(Double impTotal) {
        this.impTotal = impTotal;
    }

    public Double getPersonas() {
        return personas;
    }

    public void setPersonas(Double personas) {
        this.personas = personas;
    }

    public Double getImpPersonaFact() {
        return impPersonaFact;
    }

    public void setImpPersonaFact(Double impPersonaFact) {
        this.impPersonaFact = impPersonaFact;
    }

    public Double getImpSolic() {
        return impSolic;
    }

    public void setImpSolic(Double impSolic) {
        this.impSolic = impSolic;
    }
}
