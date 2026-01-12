-- PRIORIDAD
Insert into E_DES (DES_COD,DES_NOM) values ('PROP','PRIORIDAD OPERACIÓN PRESENTADA - PFSE');

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('PROP','1','Empleo|Enplegua','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('PROP','2','Inclusion social|Gizarteratzea','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('PROP','5','Empleo Juvenil|Gazteen enplegua','A');


-- OBJETIVO
Insert into E_DES (DES_COD,DES_NOM) values ('OBJO','OBJETIVO ESPECÍFICO - PFSE');

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('OBJO','A','Mejorar el acceso al empleo y las medidas de activación para el empleo de los demandantes de empleo, en particular de los jóvenes|Enplegurako sarbidea hobetzea, eta enplegu-eskatzaileak, bereziki gazteak, enplegurako aktibatzeko neurriak hartzea.','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('OBJO','H','Fomentar la inclusión activa al objeto de promover la igualdad de oportunidades, la no discriminación y la participación activa|Gizarteratze aktiboa sustatzea, aukera-berdintasuna, diskriminaziorik eza eta parte-hartze aktiboa sustatzeko.','A');

-- TIPOLOGIA
Insert into E_DES (DES_COD,DES_NOM) values ('TIOB','TIPOLOGÍA DE OPERACIÓN - PFSE');

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('TIOB','02','Itinerarios Integrados|Ibilbide integratuak','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('TIOB','04','Becas y ayudas para la educación y la formación|Hezkuntza eta prestakuntzarako bekak eta laguntzak','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('TIOB','06','Apoyo a los emprendedores y a la creación de empresas|Ekintzaileentzako eta enpresak sortzeko laguntza','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('TIOB','1A','Formación a lo largo de la vida laboral|Lan-bizitzan zeharreko prestakuntza','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('TIOB','5A','Ayudas para el fomento de la contratación|Kontratazioa sustatzeko laguntzak','A');

-- ENTIDAD
Insert into E_DES (DES_COD,DES_NOM) values ('ENPS','ENTIDAD - PFSE');

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','ASL','ASLE|ASLE','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','BEK','BILBAO EKINTZA|BILBAO EKINTZA','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','EDE','EDE FUNDAZIOA|EDE FUNDAZIOA','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','ELB','EHLABE|LANTEGI BATUAK','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','EMG','EMAUS|EMAUS','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','EPO','ESC. PROF. OTXARKOAGA|ESC. PROF. OTXARKOAGA','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','FMH','FUND.F. MAQ.HERRAMI|FUND.F. MAQ.HERRAMI','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','FNS','NOVIA SALCEDO|NOVIA SALCEDO','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','FSS','FOMENTO SAN SEBASTIAN|FOMENTO SAN SEBASTIAN','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','GAR','GARAPEN|GARAPEN','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) values ('ENPS','SEN','SENDOTU ALDIBEREAN|SENDOTU ALDIBEREAN','A');



-- tabla OPERACIONES PRESENTADAS

  CREATE TABLE "FLB"."MELANBIDE14_OPE_PRESENTADAS" 
   (	"ID" NUMBER(8,0) NOT NULL ENABLE, 
	"NUM_EXP" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"EJEOPERACION" NUMBER(4,0) NOT NULL ENABLE, 
	"TIPOPERACION" VARCHAR2(200 BYTE), 
	"NUMOPEPRE" NUMBER(4,0) NOT NULL ENABLE, 
	"PRIORIDAD" VARCHAR2(1 BYTE) NOT NULL ENABLE, 
	"OBJETIVO" VARCHAR2(1 BYTE), 
	"TIPOLOGIA" VARCHAR2(2 BYTE), 
	"FECINICIO" DATE, 
	"FECFIN" DATE, 
	"ENTIDAD" VARCHAR2(3 BYTE), 
	"ORGANISMO" VARCHAR2(250 BYTE), 
	 CONSTRAINT "MELANBIDE14_OPE_PRESENTADAS_PK" PRIMARY KEY ("ID")
  USING INDEX  ENABLE
   ) ;

   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."ID" IS 'ID secuencial';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."NUM_EXP" IS 'Número de Expediente';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."EJEOPERACION" IS 'Ejercicio de la Operación';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."TIPOPERACION" IS 'Tipo de Operación';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."NUMOPEPRE" IS 'Número de Operación presentada';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."PRIORIDAD" IS 'Prioridad';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."OBJETIVO" IS 'Objetivo Específico';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."TIPOLOGIA" IS 'Tipología de la Operación';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."FECINICIO" IS 'Fecha de Inicio';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."FECFIN" IS 'Fecha Fin';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."ENTIDAD" IS 'Entidad';
   COMMENT ON COLUMN "FLB"."MELANBIDE14_OPE_PRESENTADAS"."ORGANISMO" IS 'Organismo Beneficiario';
   COMMENT ON TABLE "FLB"."MELANBIDE14_OPE_PRESENTADAS"  IS 'Operaciones Presentadas';



create sequence SEQ_M14_OPE_PRESENTADAS minvalue 1 maxvalue 9999999999999999999999999999 increment by 1 start with 1 nocache  noorder  nocycle  nokeep  noscale  global ;