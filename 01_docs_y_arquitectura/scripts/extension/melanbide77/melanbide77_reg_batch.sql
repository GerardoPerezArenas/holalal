--------------------------------------------------------
-- Archivo creado  - viernes-agosto-21-2020   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table MELANBIDE77_REG_BATCH
--------------------------------------------------------

  CREATE TABLE "MELANBIDE77_REG_BATCH" 
   (	"ID" NUMBER(30,0), 
	"FEC_REGISTRO" DATE, 
	"EJER_REGISTRO" NUMBER(4,0), 
	"NUM_REGISTRO" NUMBER(6,0), 
	"NUM_EXPEDIENTE" VARCHAR2(30 BYTE),
	"NUM_SOLICITUD" NUMBER(6,0), 
	"COD_TRA" NUMBER(4,0), 
	"OPERACION" VARCHAR2(50 BYTE), 
	"RESULTADO" VARCHAR2(2 BYTE), 
	"COD_OPERACION" NUMBER(3,0), 
	"RELANZAR" NUMBER(1,0) DEFAULT NULL, 
	"OBSERVACIONES" VARCHAR2(500 BYTE)
   ) ;

   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."ID" IS 'Identificador del registro';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."FEC_REGISTRO" IS 'Fecha y hora en la que se produce el registro';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."EJER_REGISTRO" IS 'Año del registro';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."NUM_REGISTRO" IS 'Número del registro';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."NUM_EXPEDIENTE" IS 'Número de Expediente';
   comment on column "MELANBIDE77_REG_BATCH"."NUM_SOLICITUD" is 'Número de solicitud AERTE asociada al registro';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."COD_TRA" IS 'Código de Trámite';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."OPERACION" IS 'Operación realizada';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."RESULTADO" IS 'Resultado de la operación';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."COD_OPERACION" IS 'Código de operación';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."RELANZAR" IS '0-Sin relanzar;1-Pendiente de relanzar;2-Relanzado';
   COMMENT ON COLUMN "MELANBIDE77_REG_BATCH"."OBSERVACIONES" IS 'Observaciones';
--------------------------------------------------------
--  DDL for Index MELANBIDE77_REGISTROS_BATC_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MELANBIDE77_REGISTROS_BATC_PK" ON "MELANBIDE77_REG_BATCH" ("ID") 
  ;
--------------------------------------------------------
--  Constraints for Table MELANBIDE77_REG_BATCH
--------------------------------------------------------

  ALTER TABLE "MELANBIDE77_REG_BATCH" ADD CONSTRAINT "MELANBIDE77_REGISTROS_BATC_PK" PRIMARY KEY ("ID")
  USING INDEX  ENABLE;
  ALTER TABLE "MELANBIDE77_REG_BATCH" MODIFY ("FEC_REGISTRO" NOT NULL ENABLE);
  ALTER TABLE "MELANBIDE77_REG_BATCH" MODIFY ("ID" NOT NULL ENABLE);
  
  --------------------------------------------------------
--  DDL for Sequence SEQ_MELANBIDE77_REG_BATCH
--------------------------------------------------------

   CREATE SEQUENCE  "SEQ_MELANBIDE77_REG_BATCH"  MINVALUE 0 MAXVALUE 999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  ORDER  NOCYCLE ;
