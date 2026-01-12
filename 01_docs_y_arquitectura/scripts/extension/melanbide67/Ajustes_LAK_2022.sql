 -- Tabla
 CREATE TABLE "MELANBIDE67_SUBSOLIC" 
   (	"ID" NUMBER, 
	"NUM_EXP" VARCHAR2(20 BYTE) NOT null ENABLE, 
	"ESTADO" VARCHAR2(1 BYTE), 
	"ORGANISMO" VARCHAR2(200 BYTE), 
	"OBJETO" VARCHAR2(200 BYTE), 
	"IMPORTE" NUMBER(8,2), 
	"FECHA" DATE
   ) ;

   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."ID" IS 'ID secuencial';
   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."NUM_EXP" IS 'Número de Expediente';
   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."ESTADO" IS 'Estado de la Ayuda';
   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."ORGANISMO" IS 'Organismo Concedente';
   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."OBJETO" IS 'Descripción Objeto de la Ayuda';
   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."IMPORTE" IS 'Importe de la Ayuda';
   COMMENT ON COLUMN "MELANBIDE67_SUBSOLIC"."FECHA" IS 'Fecha de Solicitud o Concesión';
   
   ALTER TABLE "MELANBIDE67_SUBSOLIC" ADD PRIMARY KEY ("ID");
   
-- Secuencia
   CREATE SEQUENCE  "SEQ_MELANBIDE67_SUBSOLIC"  MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE ;

-- Agrupaciones
INSERT INTO e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) VALUES ('00011','PROCEDIMIENTO DE REINTEGRO',13,'LAK','SI');
INSERT INTO e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) VALUES ('00012','OTRAS AYUDAS',14,'LAK','SI');
INSERT INTO e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) VALUES ('00013','OPOSICIÓN INTEROPERABILIDAD',15,'LAK','SI');


-- Suplementarios
UPDATE e_pca SET pca_des='1. FORMACIÓN DUAL', pca_rot='1. FORMACIÓN DUAL' WHERE pca_pro='LAK' AND pca_cod='FORMACIONDUAL' AND pca_group='00007';
UPDATE e_pca SET pca_des='2. PROGRAMA DE BECAS DEL GOBIERNO VASCO PARA LA FORMACIÓN EN LA EMPRESA', pca_rot='2. PROGRAMA DE BECAS DEL GOBIERNO VASCO PARA LA FORMACIÓN EN LA EMPRESA' WHERE pca_pro='LAK' AND pca_cod='BECASGV' AND pca_group='00007';
UPDATE e_pca SET pca_des='2.1 PREVIAMENTE A LA OBTENCIÓN DE LA TITULACIÓN DE FORMACIÓN PROFESIONAL', pca_rot='2.1 PREVIAMENTE A LA OBTENCIÓN DE LA TITULACIÓN DE FORMACIÓN PROFESIONAL' WHERE pca_pro='LAK' AND pca_cod='PREVTITULACION' AND pca_group='00007';	
UPDATE e_pca SET pca_des='2.2 COMO TRANSICIÓN DEL ÁMBITO UNIVERSITARIO AL LABORAL', pca_rot='2.2 COMO TRANSICIÓN DEL ÁMBITO UNIVERSITARIO AL LABORAL' WHERE pca_pro='LAK' AND pca_cod='TRANSITO' AND pca_group='00007';
UPDATE e_pca SET pca_des='NÚMERO DE OFERTA', pca_rot='NÚMERO DE OFERTA' WHERE pca_pro='LAK' AND pca_cod='NUMEROTIPO2' AND pca_group='00007';

INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','IAEELAK','IAE','10','10','10',null,'0','102','IAE','SI','IAE','NO','NO',null,null,'00001',15,54);
INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','INSCRREG','¿INSCRITA EN REG. REPRESENTANTES?','6','6','1',null,'0','103','¿INSCRITA EN REG. REPRESENTANTES?','SI','BOOL','NO','NO',null,null,'00003',13,16);

INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','DNINIECONT','DNI/NIE','1','2','15',null,'0','104','DNI/NIE','SI',null,'NO','NO',null,null,'00004',11,55);

INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','REINTAYUDA','NOMBRE AYUDA A REINTEGRAR','1','2','250',null,'0','105','AYUDA A REINTEGRAR','SI',null,'NO','NO',null,null,'00011',13,13);
INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','REINTENT','ENTIDAD A LA QUE DEBE REINTEGRAR LA AYUDA','1','2','150',null,'0','106','ENTIDAD A LA QUE DEBE REINTEGRAR LA AYUDA','SI',null,'NO','NO',null,null,'00011',13,59);

INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','IMPOTRAAYUDA','CANTIDAD CONCEDIDA OTRA AYUDA (EN EUROS)','2','1','11', null,'0','107','CANTIDAD CONCEDIDA OTRA AYUDA (EN EUROS)','SI',null,'NO','NO',null,null,'00012',13,12);
INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','CONCOTRAYUDA','ENTIDAD QUE HA CONCEDIDO OTRA AYUDA','1','2','150',null,'0','108','ENTIDAD QUE HA CONCEDIDO OTRA AYUDA','SI',null,'NO','NO',null,null,'00012',13,59);
INSERT INTO e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','SOLOTRAYUDA','ENTIDAD A LA QUE HA SOLICITADO OTRA AYUDA','1','2','150',null,'0','109','ENTIDAD A LA QUE HA SOLICITADO OTRA AYUDA','SI',null,'NO','NO',null,null,'00012',13,100);
				
INSERT INTO  e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','OPOSVERIAUTOHHFF ','OPOSICION CERTIFICADO OBLIGACIONES TRIBUTARIAS','6','6','1',null,'0','110','OPOSICION CERTIFICADO OBLIGACIONES TRIBUTARIAS','SI','SN01','NO','NO',null,null,'00013',13,11);
INSERT INTO  e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','OPOSVERIAUTOTGSS ','OPOSICION CERTIFICADO PAGO S.S.','6','6','1',null,'0','111','OPOSICION CERTIFICADO PAGO S.S.','SI','SN01','NO','NO',null,null,'00013',500,11);
INSERT INTO  e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','OPOSVERIAUTOTIFE ','OPOSICION TARJETA DE IDENTIFICACIÓN FISCAL DE LA EMPRESA','6','6','1',null,'0','112','OPOSICION TARJETA DE IDENTIFICACIÓN FISCAL DE LA EMPRESA','SI','SN01','NO','NO',null,null,'00013',13,41);
INSERT INTO  e_pca (pca_mun,pca_pro,pca_cod,pca_des,pca_plt,pca_tda,pca_tam,pca_mas,pca_obl,pca_nor,pca_rot,pca_activo,pca_desplegable,pca_oculto,pca_bloq,plazo_aviso,periodo_plazo,pca_group,pca_pos_x,pca_pos_y) VALUES ('1','LAK','OPOSVERIIAE ','OPOSICION CERTIFICADO ALTA IAE','6','6','1',null,'0','113','OPOSICION CERTIFICADO ALTA IAE','SI','SN01','NO','NO',null,null,'00013',500,41);
									
