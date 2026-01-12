--DESPLEGABLES--
--PRIO
select * from e_des where des_cod='PRIO';

Insert into E_DES (DES_COD,DES_NOM) values ('PRIO','PRIORIDAD Y OBJETIVO ESPECÍFICO');

select * from e_des_val where des_cod='PRIO';

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('PRIO','1','P1 OEA.|P1 OEA.','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('PRIO','2','P2 OEH.|P2 OEH.','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('PRIO','5','P5 OEA.|P5 OEA.','A');

--LIN1
select * from e_des where des_cod='LIN1';

Insert into E_DES (DES_COD,DES_NOM) values ('LIN1','LÍNEA DE ACTUACIÓN P1 OEA');

select * from e_des_val where des_cod='LIN1';

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('LIN1','A02','Itinerarios de inserción laboral para personas desempleadas.|Laneratzeko ibilbideak langabeentzat.','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('LIN1','A05','Medidas para la retención y/o atracción del talento.|Talentua atxikitzeko eta/edo erakartzeko neurriak.','A');
Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('LIN1','A06','Apoyo inicial a la consolidación del autoempleo y la creación de empresas.|Autoenplegua sendotzeko eta enpresak sortzeko hasierako laguntza.','A');

--LIN2
select * from e_des where des_cod='LIN2';

Insert into E_DES (DES_COD,DES_NOM) values ('LIN2','LÍNEA DE ACTUACIÓN P2 OEH');

select * from e_des_val where des_cod='LIN2';

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('LIN2','2H02','2.H.02. Itinerarios de inserción socio-laboral para personas en riesgo de exclusión social.|2.H.02. Gizartetik baztertuta geratzeko arriskuan dauden pertsonentzako gizarteratze- eta laneratze-ibilbideak.','A');

--LIN3
select * from e_des where des_cod='LIN3';

Insert into E_DES (DES_COD,DES_NOM) values ('LIN3','LÍNEA DE ACTUACIÓN P5 OEA');

select * from e_des_val where des_cod='LIN3';

Insert into E_DES_VAL (DES_COD,DES_VAL_COD,DES_NOM,DES_VAL_ESTADO) 
values ('LIN3','5A03','5.A.03. Itinerarios de inserción para jóvenes muy alejados del mercado laboral.|5.A.03. Lan-merkatutik oso urrun dauden gazteak gizarteratzeko ibilbideak.','A');

--SUPLEMENTARIOS


--TABLA SEGÚN MAPEO 'Mapeo campos - PFSE - V0.xlsx'

CREATE SEQUENCE  "FLB"."SEQ_MELANBIDE14_OPERACIONES"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 61 NOCACHE  NOORDER  NOCYCLE  NOKEEP  NOSCALE  GLOBAL ;


 CREATE TABLE "FLB"."MELANBIDE14_OPERACIONES" 
   (	
   "ID" NUMBER(8,0) NOT NULL ENABLE, 
	"NUM_EXP" VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	"NUMOPER" NUMBER(4,0) NOT NULL ENABLE, 
	"NOMBREOPER" VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	"PRIOROBJ" VARCHAR2(2 BYTE), 
	"LINACT1" VARCHAR2(3 BYTE), 
	"LINACT2" VARCHAR2(4 BYTE), 
	"LINACT3" VARCHAR2(4 BYTE), 
	"IMPOPER" NUMBER(10,2) NOT NULL ENABLE
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "FLB_DBS1" ;



