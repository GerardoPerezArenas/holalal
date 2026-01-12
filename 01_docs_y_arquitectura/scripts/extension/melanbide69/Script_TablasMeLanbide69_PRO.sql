--------------------------------------------------------
-- Archivo creado  - lunes-septiembre-09-2019   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table MELANBIDE69_COLECTIVO
--------------------------------------------------------

  CREATE TABLE "MELANBIDE69_COLECTIVO" 
   (	"COL_ID" NUMBER(8,0), 
	"COL_PRO" VARCHAR2(5 BYTE), 
	"COL_EDADI" NUMBER(3,0), 
	"COL_EDADF" NUMBER(3,0), 
	"COL_EJE" NUMBER(4,0)
   ) ;

   COMMENT ON COLUMN "MELANBIDE69_COLECTIVO"."COL_EDADI" IS 'Edad de inicio del colectivo (se incluye la misma)';
   COMMENT ON COLUMN "MELANBIDE69_COLECTIVO"."COL_EDADF" IS 'Edad fin del colectivo (se incluye la misma)';
   COMMENT ON COLUMN "MELANBIDE69_COLECTIVO"."COL_EJE" IS '(Año de la) convocatoria';
REM INSERTING into MELANBIDE69_COLECTIVO
SET DEFINE OFF;
Insert into MELANBIDE69_COLECTIVO (COL_ID,COL_PRO,COL_EDADI,COL_EDADF,COL_EJE) values ('1','APES','18','29','2017');
Insert into MELANBIDE69_COLECTIVO (COL_ID,COL_PRO,COL_EDADI,COL_EDADF,COL_EJE) values ('2','APES','30',null,'2017');
Insert into MELANBIDE69_COLECTIVO (COL_ID,COL_PRO,COL_EDADI,COL_EDADF,COL_EJE) values ('3','APES','18','29','2018');
Insert into MELANBIDE69_COLECTIVO (COL_ID,COL_PRO,COL_EDADI,COL_EDADF,COL_EJE) values ('4','APES','30',null,'2018');
Insert into MELANBIDE69_COLECTIVO (COL_ID,COL_PRO,COL_EDADI,COL_EDADF,COL_EJE) values ('5','APES','18','29','2019');
Insert into MELANBIDE69_COLECTIVO (COL_ID,COL_PRO,COL_EDADI,COL_EDADF,COL_EJE) values ('6','APES','30',null,'2019');
--------------------------------------------------------
--  DDL for Index MELANBIDE69_COLECTIVO_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MELANBIDE69_COLECTIVO_PK" ON "MELANBIDE69_COLECTIVO" ("COL_ID", "COL_PRO", "COL_EJE") 
  ;
--------------------------------------------------------
--  Constraints for Table MELANBIDE69_COLECTIVO
--------------------------------------------------------

  ALTER TABLE "MELANBIDE69_COLECTIVO" ADD CONSTRAINT "MELANBIDE69_COLECTIVO_PK" PRIMARY KEY ("COL_ID", "COL_PRO", "COL_EJE") ENABLE;
  ALTER TABLE "MELANBIDE69_COLECTIVO" MODIFY ("COL_ID" NOT NULL ENABLE);

--------------------------------------------------------
--  DDL for Table MELANBIDE69_SUBVENCION
--------------------------------------------------------

  CREATE TABLE "MELANBIDE69_SUBVENCION" 
   (	"SUB_ID" NUMBER(8,0), 
	"SUB_PRO" VARCHAR2(5 BYTE), 
	"SUB_EJE" NUMBER(4,0), 
	"SUB_IMPORTE" NUMBER(8,0)
   ) ;

   COMMENT ON COLUMN "MELANBIDE69_SUBVENCION"."SUB_EJE" IS '(Año de la) convocatoria';
   COMMENT ON COLUMN "MELANBIDE69_SUBVENCION"."SUB_IMPORTE" IS 'Importe de la subvención';
REM INSERTING into MELANBIDE69_SUBVENCION
SET DEFINE OFF;
Insert into MELANBIDE69_SUBVENCION (SUB_ID,SUB_PRO,SUB_EJE,SUB_IMPORTE) values ('1','APES','2017','3500');
Insert into MELANBIDE69_SUBVENCION (SUB_ID,SUB_PRO,SUB_EJE,SUB_IMPORTE) values ('2','APES','2017','4035');
Insert into MELANBIDE69_SUBVENCION (SUB_ID,SUB_PRO,SUB_EJE,SUB_IMPORTE) values ('3','APES','2017','4550');
Insert into MELANBIDE69_SUBVENCION (SUB_ID,SUB_PRO,SUB_EJE,SUB_IMPORTE) values ('4','APES','2019','3500');
Insert into MELANBIDE69_SUBVENCION (SUB_ID,SUB_PRO,SUB_EJE,SUB_IMPORTE) values ('5','APES','2019','4025');
Insert into MELANBIDE69_SUBVENCION (SUB_ID,SUB_PRO,SUB_EJE,SUB_IMPORTE) values ('6','APES','2019','4550');
--------------------------------------------------------
--  DDL for Index MELANBIDE69_SUBVENCION_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MELANBIDE69_SUBVENCION_PK" ON "MELANBIDE69_SUBVENCION" ("SUB_ID", "SUB_PRO", "SUB_EJE") 
  ;
--------------------------------------------------------
--  Constraints for Table MELANBIDE69_SUBVENCION
--------------------------------------------------------

  ALTER TABLE "MELANBIDE69_SUBVENCION" ADD CONSTRAINT "MELANBIDE69_SUBVENCION_PK" PRIMARY KEY ("SUB_ID", "SUB_PRO", "SUB_EJE") ENABLE;
  ALTER TABLE "MELANBIDE69_SUBVENCION" MODIFY ("SUB_ID" NOT NULL ENABLE);


--------------------------------------------------------
--  DDL for Table MELANBIDE69_SUBV_COLEC_SEX
--------------------------------------------------------

  CREATE TABLE "MELANBIDE69_SUBV_COLEC_SEX" 
   (	"SCS_PRO" VARCHAR2(5 BYTE), 
	"SCS_EJE" NUMBER(4,0), 
	"SUB_ID" NUMBER(8,0), 
	"COL_ID" NUMBER(8,0), 
	"SCS_SEXO" NUMBER(1,0)
   ) ;

   COMMENT ON COLUMN "MELANBIDE69_SUBV_COLEC_SEX"."SUB_ID" IS 'Importe subvención';
   COMMENT ON COLUMN "MELANBIDE69_SUBV_COLEC_SEX"."COL_ID" IS 'Colectivo (según edad)';
   COMMENT ON COLUMN "MELANBIDE69_SUBV_COLEC_SEX"."SCS_SEXO" IS 'Sexo';
REM INSERTING into MELANBIDE69_SUBV_COLEC_SEX
SET DEFINE OFF;
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2017','1','2','1');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2017','2','1','1');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2017','2','2','2');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2017','3','1','2');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2019','4','6','1');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2019','5','5','1');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2019','5','6','2');
Insert into MELANBIDE69_SUBV_COLEC_SEX (SCS_PRO,SCS_EJE,SUB_ID,COL_ID,SCS_SEXO) values ('APES','2019','6','5','2');
--------------------------------------------------------
--  DDL for Index MELANBIDE69_SUBV_COLEC_SEX_UQ
--------------------------------------------------------

  CREATE UNIQUE INDEX "MELANBIDE69_SUBV_COLEC_SEX_UQ" ON "MELANBIDE69_SUBV_COLEC_SEX" ("SCS_PRO", "SCS_EJE", "SUB_ID", "COL_ID", "SCS_SEXO") 
  ;
--------------------------------------------------------
--  Constraints for Table MELANBIDE69_SUBV_COLEC_SEX
--------------------------------------------------------

  ALTER TABLE "MELANBIDE69_SUBV_COLEC_SEX" ADD CONSTRAINT "MELANBIDE69_SUBV_COLEC_SEX_UQ" UNIQUE ("SCS_PRO", "SCS_EJE", "SUB_ID", "COL_ID", "SCS_SEXO") ENABLE;


--------------------------------------------------------
--  DDL for Table MELANBIDE69_DATOSPLANTILLAS
--------------------------------------------------------

  CREATE TABLE "MELANBIDE69_DATOSPLANTILLAS" 
   (	"PLA_ID" NUMBER(8,0), 
	"PLA_PRO" VARCHAR2(5 BYTE), 
	"PLA_EJE" NUMBER(4,0), 
	"PLA_PORCENTAJE" NUMBER(4,2), 
	"PLA_FECHA_ACUERDO" DATE, 
	"PLA_FECHA_BOPV" DATE,
	CONSTRAINT "MELANBIDE69_DATOSPLANTILLAS_PK" PRIMARY KEY ("PLA_ID")
   ) ;

   COMMENT ON COLUMN "MELANBIDE69_DATOSPLANTILLAS"."PLA_EJE" IS '(Año de la) convocatoria';
   COMMENT ON COLUMN "MELANBIDE69_DATOSPLANTILLAS"."PLA_PORCENTAJE" IS 'Porcentaje del primer pago';


--------------------------------------------------------
--  DDL for Table MELANBIDE69_FACTURAS
--------------------------------------------------------

  CREATE TABLE "MELANBIDE69_FACTURAS" 
   (	"ID" NUMBER(8,0), 
	"NUM_EXP" VARCHAR2(30 BYTE), 
	"ESTADO" VARCHAR2(1 BYTE), 
	"FECHA_FACTURA" DATE, 
	"CODIGO_CONCEPTO" VARCHAR2(3 BYTE), 
	"CODIGO_SUBCONCEPTO" VARCHAR2(5 BYTE), 
	"IMPORTE" NUMBER(14,2), 
	"ENTREGADA_FACT" VARCHAR2(1 BYTE), 
	"ENTREGADO_JUSTIF" VARCHAR2(1 BYTE), 
	"OBSERVACIONES" CLOB, 
	"IDENTIFICACION" VARCHAR2(100 BYTE),
	CONSTRAINT "MELANBIDE69_FACTURAS_PK" PRIMARY KEY ("ID")
   ) ;

   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."ID" IS 'Índice secuencial y clave primaria de la tabla';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."NUM_EXP" IS 'Número de expediente';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."ESTADO" IS 'Estado de la factura. A-Aceptada;N-No aceptada';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."FECHA_FACTURA" IS 'Fecha de la factura';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."CODIGO_CONCEPTO" IS 'Identificador de concepto.';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."CODIGO_SUBCONCEPTO" IS 'Identificador de subconcepto';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."IMPORTE" IS 'Importe de la factura';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."ENTREGADA_FACT" IS 'Indica si se ha entregado la factura. S-Si;N-No';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."ENTREGADO_JUSTIF" IS 'Indica si se ha entregado el justificante de pago. S-Si;N-No';
   COMMENT ON COLUMN "MELANBIDE69_FACTURAS"."OBSERVACIONES" IS 'Comentarios sobre la factura';
   COMMENT ON TABLE "MELANBIDE69_FACTURAS"  IS 'Relación de facturas de justificación del procedimiento APES';

	CREATE SEQUENCE SEQ_MELANBIDE69_FACTURAS MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1;
