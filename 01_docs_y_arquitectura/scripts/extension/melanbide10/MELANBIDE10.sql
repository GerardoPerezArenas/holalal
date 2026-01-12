
--desplegables
select * from e_des where des_cod='TCDS';
insert into e_des (des_cod,des_nom) values ('TCDS','TIPO CONTRATO DISCP');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('TCDS','I','INDEFINIDO|MUGAGABEA','A');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('TCDS','C','CONVERSIÓN EN INDEFINIDO|MUGAGABE BIHURTZEA','A');

select * from e_des where des_cod='CODS';
insert into e_des (des_cod,des_nom) values ('CODS','CONTRATO ORIGEN DISCP');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('CODS','E','FOMENTO EMPLEO|ENPLEGU-SUSTAPENA','A');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('CODS','P','PRÁCTICAS|PRAKTIKAK','A');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('CODS','A','FORMACIÓN Y APRENDIZAJE|PRESTAKUNTZA ETA IKASKUNTZA','A');

select * from e_des where des_cod='SEDS';
insert into e_des (des_cod,des_nom) values ('SEDS','SITUACIÓN EMPLEO ANTERIOR DISCP');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('SEDS','D','DESEMPLEADA E INSCRITA COMO DEMANDANTE DE EMPLEO|LANGABETUA ETA ENPLEGU-ESKATZAILE GISA INSKRIBATUA','A');
insert into e_des_val (des_cod,des_val_cod,des_nom,des_val_estado) values ('SEDS','C','CONTRATADA EN UN CENTRO ESPECIAL DE EMPLEO|ENPLEGU-ZENTRO BEREZI BATEAN KONTRATATUA','A');




select * from e_pca_group where pca_pro='DISCP' order by pca_order_group;
--grupos según Mapeo campos DISCP - V2.xlsx
--IDENTIFICACION EMPRESA SOLICITANTE (A - DATOS COMPLEMENTARIOS DE LA ENTIDAD SOLICITANTE)(orden 2)
--PERSONA REPRESENTANTE
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('PR','PERSONA REPRESENTANTE','4','DISCP','SI');
--PERSONA CONTACTO
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('PC','PERSONA CONTACTO','5','DISCP','SI');
--NOTIFICAICON (DAN - DATOS DE AVISO/NOTIFICACION)(orden 1)
--IMPORTE SUBVENCION SOLICITADA
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('IS','IMPORTE SUBVENCION SOLICITADA','6','DISCP','SI');
--CONTRATO TRABAJO
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('CT','CONTRATO TRABAJO','7','DISCP','SI');
--PERSONA CONTRATADA (D - DATOS COMPLEMENTARIOS DEL TRABAJADOR)
update e_pca_group set pca_order_group='8' where pca_pro='DISCP' and pca_id_group='D' and pca_order_group='4';
--DECLARACION RESPONSABLE (C - DECLARACION EMPRESA EMPLEADO)
update e_pca_group set pca_order_group='9' where pca_pro='DISCP' and pca_id_group='C' and pca_order_group='5';
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('DR','DECLARACION RESPONSABLE','10','DISCP','SI');
--SUBVENCIONES SOLICITADAS MINIMIS (pestaña MELANBIDE10)
--OPOSICION INTEROPERABILIDAD
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('OI','OPOSICION INTEROPERABILIDAD','11','DISCP','SI');
--CAMPOS CONVOCATORIAS ANTERIORES
insert into e_pca_group (pca_id_group,pca_desc_group,pca_order_group,pca_pro,pca_active) values ('CA','CONVOCATORIAS ANTERIORES','12','DISCP','SI');
--después de modificaciones:
/*DAN	DATOS DE AVISO/NOTIFICACION	1
A	DATOS COMPLEMENTARIOS DE LA ENTIDAD SOLICITANTE	2
B	DATOS CUENTA DE COTIZACIÓN	3
PR	PERSONA REPRESENTANTE	4
PC	PERSONA CONTACTO	5
IS	IMPORTE SUBVENCION SOLICITADA	6
CT	CONTRATO TRABAJO	7
D	DATOS COMPLEMENTARIOS DEL TRABAJADOR	8
C	DECLARACION EMPRESA EMPLEADO	9
DR	DECLARACION RESPONSABLE	10
OI	OPOSICION INTEROPERABILIDAD	11
CA  CONVOCATORIAS ANTERIORES 12*/


select * from e_pca_group g
right join e_pca s on g.pca_pro=s.pca_pro and g.pca_id_group=s.pca_group 
where s.pca_pro='DISCP' order by g.pca_order_group,s.pca_pos_y,s.pca_pos_x;

--CONVOCATORIAS ANTERIORES
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='1',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='CODCNAE';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='35',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='CCREGIMEN';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='70',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='CCTERHIST';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='105',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='CCNUMERO';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='140',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='CCDIGCONTROL';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='175',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='TIPOCONTRATO';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='210',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='TTHHTRAB';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='245',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='MUNITRABTXT';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='280',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='MUNICENTROTXT';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='315',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='PERMCOPIACIF';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='350',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='PERMJUSTPAGHAC';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='385',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='PERMJUSTPAGSS';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='420',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='OPOSJUSTPAGHAC';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='455',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='OPOSJUSTPAGSS';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='490',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='OPOSCOPIACIF';
update e_pca set pca_bloq='SI',pca_group='CA',pca_pos_y='525',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='OPOSEPIGIAE';

update e_pca set pca_pos_y='1',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='RESULESTTEC';
update e_pca set pca_pos_y='35',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='OBSOLTELEM';

update e_pca set pca_pos_y='13',pca_pos_x='13' where pca_pro='DISCP' and pca_cod='AVISOIDCTAINTETIT';
update e_pca set pca_pos_y='13',pca_pos_x='524' where pca_pro='DISCP' and pca_cod='AVISOIDCTAINTE';
update e_pca set pca_pos_y='84',pca_pos_x='12' where pca_pro='DISCP' and pca_cod='AVISOEMAILTIT';
update e_pca set pca_pos_y='150',pca_pos_x='14' where pca_pro='DISCP' and pca_cod='AVISOEMAIL';
update e_pca set pca_pos_y='223',pca_pos_x='13' where pca_pro='DISCP' and pca_cod='AVISOSMSTIT';
update e_pca set pca_pos_y='223',pca_pos_x='524' where pca_pro='DISCP' and pca_cod='AVISOSMS';
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','IDIOMAAVISOSTIT','AVISO - IDIOMA | TITULAR','1','2','30',null,'0','7','AVISO - IDIOMA | TITULAR','SI',null,'NO','SI',null,null,'DAN','15','305');
update e_pca set pca_pos_y='306',pca_pos_x='525',pca_des='AVISO - IDIOMA | REPRESENTANTE',pca_rot='AVISO - IDIOMA | REPRESENTANTE' where pca_pro='DISCP' and pca_cod='IDIOMAAVISOS';
update e_pca set pca_pos_y='309',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='DOCACREDRDAVAL';

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','CCC','CODIGO CUENTA COTIZACION','1','2','20',null,'0','7','CODIGO CUENTA COTIZACION','SI',null,'NO','NO',null,null,'A','1','1');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','CNAE','CNAE','10','10','0',null,'0','14','CNAE','SI','CNAE','NO','NO',null,null,'A','1','35');

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','REAPCAE','INSCRITA EN REGISTRO DE REPRESENTANTES ADMON PUBLICA CAE','6','6','6',null,'0','7','INSCRITA EN REGISTRO DE REPRESENTANTES ADMON PUBLICA CAE','SI','BOOL','NO','NO',null,null,'PR','1','1');

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NOMBREPC','NOMBRE','1','2','100',null,'0','7','NOMBRE','SI',null,'NO','NO',null,null,'PC','1','1');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','APELLIDO1PC','APELLIDO 1','1','2','100',null,'0','7','APELLIDO 1','SI',null,'NO','NO',null,null,'PC','1','35');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','APELLIDO2PC','APELLIDO 2','1','2','100',null,'0','7','APELLIDO 2','SI',null,'NO','NO',null,null,'PC','1','70');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','DNIPC','DNI','1','2','15',null,'0','7','DNI','SI',null,'NO','NO',null,null,'PC','1','105');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','TFNOPC','TFNO','1','2','15',null,'0','7','TFNO','SI',null,'NO','NO',null,null,'PC','1','140');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','EMAILPC','EMAIL','1','2','50',null,'0','7','EMAIL','SI',null,'NO','NO',null,null,'PC','1','175');

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','CANTSUBV','CANTIDAD SOLICITADA (EUROS)','2','1','11',null,'0','7','CANTIDAD SOLICITADA (EUROS)','SI',null,'NO','NO',null,null,'IS','1','1');

update e_pca set pca_group='CT',pca_pos_y='1',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='FECALTATRAB';
update e_pca set pca_group='CT',pca_pos_y='35',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='TTHHCENTRO';
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','CPCT','CODIGO POSTAL CENTRO TRABAJO','1','2','5',null,'0','7','CODIGO POSTAL CENTRO TRABAJO','SI',null,'NO','NO',null,null,'CT','1','70');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','MUNICT','MUNICIPIO CENTRO TRABAJO','10','10','0',null,'0','14','MUNICIPIO CENTRO TRABAJO','SI','MUNI','NO','NO',null,null,'CT','1','105');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','TCTR','TIPO CONTRATO','6','6','6',null,'0','7','TIPO CONTRATO','SI','TCDS','NO','NO',null,null,'CT','1','140');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','TCTROR','CONTRATO ORIGEN','6','6','6',null,'0','7','CONTRATO ORIGEN','SI','CODS','NO','NO',null,null,'CT','1','175');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','TJORN','JORNADA','6','6','6',null,'0','7','JORNADA','SI','JORN','NO','NO',null,null,'CT','1','210');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','PORJORN','PROPORCION JORNADA','2','1','11',null,'0','7','PROPORCION JORNADA','SI',null,'NO','NO',null,null,'CT','1','245');

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','SEAC','SITUACION EMPLEO ANTERIOR A CONTRATACION','6','6','6',null,'0','7','SITUACION EMPLEO ANTERIOR A CONTRATACION','SI','SEDS','NO','NO',null,null,'D','1','1');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','ENLB','ENCLAVE LABORAL','6','6','6',null,'0','7','ENCLAVE LABORAL','SI','BOOL','NO','NO',null,null,'D','1','35');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NCEE','NOMBRE_RAZON SOCIAL CENTRO ESPECIAL EMPLEO','1','2','150',null,'0','7','NOMBRE_RAZON SOCIAL CENTRO ESPECIAL EMPLEO','SI',null,'NO','NO',null,null,'D','1','70');
update e_pca set pca_group='D',pca_pos_y='105',pca_pos_x='1' where pca_pro='DISCP' and pca_cod='NIVELFORMA';

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','SANCADM','1_ENTIDAD NO SANCIONADA CON PERDIDA DE OBTENCION DE SUBVENCIONES','6','6','6',null,'0','7','1_ENTIDAD NO SANCIONADA CON PERDIDA DE OBTENCION DE SUBVENCIONES','SI','BOOL','NO','NO',null,null,'DR','1','1');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','PAGREINT','2_ENTIDAD NO OBLIGACIONES DE REINTEGRO POR SUBVENCIONES','6','6','6',null,'0','7','2_ENTIDAD NO OBLIGACIONES DE REINTEGRO POR SUBVENCIONES','SI','BOOL','NO','NO',null,null,'DR','1','35');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','RESTCIRC','3_ENTIDAD NO INCURSA EN APDOS 2_3 ART 13 LEY 38_2003 SUBVENCIONES','6','6','6',null,'0','7','3_ENTIDAD NO INCURSA EN APDOS 2_3 ART 13 LEY 38_2003 SUBVENCIONES','SI','BOOL','NO','NO',null,null,'DR','1','70');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','REINTSANC','4_ENTIDAD NO INCURSA PROC SANCIONADOR SUBVENCION MISMA NATURALEZA','6','6','6',null,'0','7','4_ENTIDAD NO INCURSA PROC SANCIONADOR SUBVENCION MISMA NATURALEZA','SI','BOOL','NO','NO',null,null,'DR','1','105');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','REINTAYUDA','NOMBRE_AYUDA A REINTEGRAR','1','2','250',null,'0','7','NOMBRE_AYUDA A REINTEGRAR','SI',null,'NO','NO',null,null,'DR','1','140');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','REINTENT','ENTIDAD A LA QUE REINTEGRAR LA AYUDA','1','2','150',null,'0','7','ENTIDAD A LA QUE REINTEGRAR LA AYUDA','SI',null,'NO','NO',null,null,'DR','1','175');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NOAYUD','5_ENTIDAD NO PERCIBIENDO AYUDA MISMO OBJETO','6','6','6',null,'0','7','ENTIDAD NO SANCIONADA CON PERDIDA DE OBTENCION DE SUBVENCIONES','SI','BOOL','NO','NO',null,null,'DR','1','210');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','IMPAYUCON1','IMPORTE CONCEDIDO A ENTIDAD SOLICITANTE','2','1','11',null,'0','7','IMPORTE CONCEDIDO A ENTIDAD SOLICITANTE','SI',null,'NO','NO',null,null,'DR','1','245');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','ADMINCONC','ADMON O ENTIDAD QUE HA CONCEDIDO AYUDA','1','2','100',null,'0','7','ADMON O ENTIDAD QUE HA CONCEDIDO AYUDA','SI',null,'NO','NO',null,null,'DR','1','320');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','AYUDPDRESOL','5_3 HA SOLICITADO AYUDA PTE RESOLUCION','6','6','6',null,'0','7','5_3 HA SOLICITADO AYUDA PTE RESOLUCION','SI','BOOL','NO','NO',null,null,'DR','1','355');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','ADYENTPUPRI','ADMON O ENTIDAD CON AYUDA PTE RESOLUCION','1','2','100',null,'0','7','ADMON O ENTIDAD CON AYUDA PTE RESOLUCION','SI',null,'NO','NO',null,null,'DR','1','390');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NOMINIMIS','6_NO HA RECIBIDO AYUDAS MINIMIS','6','6','6',null,'0','7','6_NO HA RECIBIDO AYUDAS MINIMIS','SI','BOOL','NO','NO',null,null,'DR','1','425');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NCIRCA','7_NO CIRCUNSTANCIAS ART 9_5 A DECRETO 168_2019','6','6','6',null,'0','7','7_NO CIRCUNSTANCIAS ART 9_5 A DECRETO 168_2019','SI','BOOL','NO','NO',null,null,'DR','1','460');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NCIRCC','8_NO CIRCUNSTANCIAS ART 9_5 C DECRETO 168_2019','6','6','6',null,'0','7','8_NO CIRCUNSTANCIAS ART 9_5 C DECRETO 168_2019','SI','BOOL','NO','NO',null,null,'DR','1','495');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','NCIRCD','9_NO CIRCUNSTANCIAS ART 9_5 D DECRETO 168_2019','6','6','6',null,'0','7','9_NO CIRCUNSTANCIAS ART 9_5 D DECRETO 168_2019','SI','BOOL','NO','NO',null,null,'DR','1','530');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','DATOSVER','10_DATOS VERACES','6','6','6',null,'0','7','10_DATOS VERACES','SI','BOOL','NO','NO',null,null,'DR','1','565');

Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','OPOSVERIAUTOHHFF','OPOSICION A OBTENER CERTIFICADO CORRIENTE OBLIGACIONES TRIBUTARIAS','1','2','1',null,'0','7','OPOSICION A OBTENER CERTIFICADO CORRIENTE OBLIGACIONES TRIBUTARIAS','SI',null,'NO','SI',null,null,'OI','1','1');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','OPOSVERIAUTOTGSS','OPOSICION A OBTENER CERTIFICADO ACREDITATIVO CORRIENTE OBLIGACIONES TGSS','1','2','1',null,'0','7','OPOSICION A OBTENER CERTIFICADO ACREDITATIVO CORRIENTE OBLIGACIONES TGSS','SI',null,'NO','SI',null,null,'OI','1','35');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','OPOSIDFISC','OPOSICION A OBTENER TARJETA DE IDENTIFICACION FISCAL DE EMPRESA','1','2','1',null,'0','7','OPOSICION A OBTENER TARJETA DE IDENTIFICACION FISCAL DE EMPRESA','SI',null,'NO','SI',null,null,'OI','1','70');
Insert into E_PCA (PCA_MUN,PCA_PRO,PCA_COD,PCA_DES,PCA_PLT,PCA_TDA,PCA_TAM,PCA_MAS,PCA_OBL,PCA_NOR,PCA_ROT,PCA_ACTIVO,PCA_DESPLEGABLE,PCA_OCULTO,PCA_BLOQ,PLAZO_AVISO,PERIODO_PLAZO,PCA_GROUP,PCA_POS_X,PCA_POS_Y) 
values ('1','DISCP','OPOSVERIIAE','OPOSICION A CONSULTA EPIGRAFE IAE','1','2','1',null,'0','7','OPOSICION A CONSULTA EPIGRAFE IAE','SI',null,'NO','SI',null,null,'OI','1','105');


--reordenación
update e_pca set pca_nor=1 where pca_pro='DISCP' and pca_cod='RESULESTTEC';
update e_pca set pca_nor=2 where pca_pro='DISCP' and pca_cod='OBSOLTELEM';
update e_pca set pca_nor=3 where pca_pro='DISCP' and pca_cod='AVISOIDCTAINTETIT';
update e_pca set pca_nor=4 where pca_pro='DISCP' and pca_cod='AVISOIDCTAINTE';
update e_pca set pca_nor=5 where pca_pro='DISCP' and pca_cod='AVISOEMAILTIT';
update e_pca set pca_nor=6 where pca_pro='DISCP' and pca_cod='AVISOEMAIL';
update e_pca set pca_nor=7 where pca_pro='DISCP' and pca_cod='AVISOSMSTIT';
update e_pca set pca_nor=8 where pca_pro='DISCP' and pca_cod='AVISOSMS';
update e_pca set pca_nor=9 where pca_pro='DISCP' and pca_cod='IDIOMAAVISOSTIT';
update e_pca set pca_nor=10 where pca_pro='DISCP' and pca_cod='IDIOMAAVISOS';
update e_pca set pca_nor=11 where pca_pro='DISCP' and pca_cod='DOCACREDRDAVAL';
update e_pca set pca_nor=12 where pca_pro='DISCP' and pca_cod='CCC';
update e_pca set pca_nor=13 where pca_pro='DISCP' and pca_cod='CNAE';
update e_pca set pca_nor=14 where pca_pro='DISCP' and pca_cod='REAPCAE';
update e_pca set pca_nor=15 where pca_pro='DISCP' and pca_cod='NOMBREPC';
update e_pca set pca_nor=16 where pca_pro='DISCP' and pca_cod='APELLIDO1PC';
update e_pca set pca_nor=17 where pca_pro='DISCP' and pca_cod='APELLIDO2PC';
update e_pca set pca_nor=18 where pca_pro='DISCP' and pca_cod='DNIPC';
update e_pca set pca_nor=19 where pca_pro='DISCP' and pca_cod='TFNOPC';
update e_pca set pca_nor=20 where pca_pro='DISCP' and pca_cod='EMAILPC';
update e_pca set pca_nor=21 where pca_pro='DISCP' and pca_cod='CANTSUBV';
update e_pca set pca_nor=22 where pca_pro='DISCP' and pca_cod='FECALTATRAB';
update e_pca set pca_nor=23 where pca_pro='DISCP' and pca_cod='TTHHCENTRO';
update e_pca set pca_nor=24 where pca_pro='DISCP' and pca_cod='CPCT';
update e_pca set pca_nor=25 where pca_pro='DISCP' and pca_cod='MUNICT';
update e_pca set pca_nor=26 where pca_pro='DISCP' and pca_cod='TCTR';
update e_pca set pca_nor=27 where pca_pro='DISCP' and pca_cod='TCTROR';
update e_pca set pca_nor=28 where pca_pro='DISCP' and pca_cod='TJORN';
update e_pca set pca_nor=29 where pca_pro='DISCP' and pca_cod='PORJORN';
update e_pca set pca_nor=30 where pca_pro='DISCP' and pca_cod='SEAC';
update e_pca set pca_nor=31 where pca_pro='DISCP' and pca_cod='ENLB';
update e_pca set pca_nor=32 where pca_pro='DISCP' and pca_cod='NCEE';
update e_pca set pca_nor=33 where pca_pro='DISCP' and pca_cod='NIVELFORMA';
update e_pca set pca_nor=34 where pca_pro='DISCP' and pca_cod='EMISCERTDISC';
update e_pca set pca_nor=35 where pca_pro='DISCP' and pca_cod='NIVELDISC';
update e_pca set pca_nor=36 where pca_pro='DISCP' and pca_cod='TIPODISCAP';
update e_pca set pca_nor=37 where pca_pro='DISCP' and pca_cod='IMPCONTPARCIAL';
update e_pca set pca_nor=38 where pca_pro='DISCP' and pca_cod='SANCADM';
update e_pca set pca_nor=39 where pca_pro='DISCP' and pca_cod='PAGREINT';
update e_pca set pca_nor=40 where pca_pro='DISCP' and pca_cod='RESTCIRC';
update e_pca set pca_nor=41 where pca_pro='DISCP' and pca_cod='REINTSANC';
update e_pca set pca_nor=42 where pca_pro='DISCP' and pca_cod='REINTAYUDA';
update e_pca set pca_nor=43 where pca_pro='DISCP' and pca_cod='REINTENT';
update e_pca set pca_nor=44 where pca_pro='DISCP' and pca_cod='NOAYUD';
update e_pca set pca_nor=45 where pca_pro='DISCP' and pca_cod='IMPAYUCON1';
update e_pca set pca_nor=46 where pca_pro='DISCP' and pca_cod='ADMINCONC';
update e_pca set pca_nor=47 where pca_pro='DISCP' and pca_cod='AYUDPDRESOL';
update e_pca set pca_nor=48 where pca_pro='DISCP' and pca_cod='ADYENTPUPRI';
update e_pca set pca_nor=49 where pca_pro='DISCP' and pca_cod='NOMINIMIS';
update e_pca set pca_nor=50 where pca_pro='DISCP' and pca_cod='NCIRCA';
update e_pca set pca_nor=51 where pca_pro='DISCP' and pca_cod='NCIRCC';
update e_pca set pca_nor=52 where pca_pro='DISCP' and pca_cod='NCIRCD';
update e_pca set pca_nor=53 where pca_pro='DISCP' and pca_cod='DATOSVER';
update e_pca set pca_nor=54 where pca_pro='DISCP' and pca_cod='OPOSVERIAUTOHHFF';
update e_pca set pca_nor=55 where pca_pro='DISCP' and pca_cod='OPOSVERIAUTOTGSS';
update e_pca set pca_nor=56 where pca_pro='DISCP' and pca_cod='OPOSIDFISC';
update e_pca set pca_nor=57 where pca_pro='DISCP' and pca_cod='OPOSVERIIAE';
update e_pca set pca_nor=58 where pca_pro='DISCP' and pca_cod='CODCNAE';
update e_pca set pca_nor=59 where pca_pro='DISCP' and pca_cod='CCREGIMEN';
update e_pca set pca_nor=60 where pca_pro='DISCP' and pca_cod='CCTERHIST';
update e_pca set pca_nor=61 where pca_pro='DISCP' and pca_cod='CCNUMERO';
update e_pca set pca_nor=62 where pca_pro='DISCP' and pca_cod='CCDIGCONTROL';
update e_pca set pca_nor=63 where pca_pro='DISCP' and pca_cod='TIPOCONTRATO';
update e_pca set pca_nor=64 where pca_pro='DISCP' and pca_cod='TTHHTRAB';
update e_pca set pca_nor=65 where pca_pro='DISCP' and pca_cod='MUNITRABTXT';
update e_pca set pca_nor=66 where pca_pro='DISCP' and pca_cod='MUNICENTROTXT';
update e_pca set pca_nor=67 where pca_pro='DISCP' and pca_cod='PERMCOPIACIF';
update e_pca set pca_nor=68 where pca_pro='DISCP' and pca_cod='PERMJUSTPAGHAC';
update e_pca set pca_nor=69 where pca_pro='DISCP' and pca_cod='PERMJUSTPAGSS';
update e_pca set pca_nor=70 where pca_pro='DISCP' and pca_cod='OPOSJUSTPAGHAC';
update e_pca set pca_nor=71 where pca_pro='DISCP' and pca_cod='OPOSJUSTPAGSS';
update e_pca set pca_nor=72 where pca_pro='DISCP' and pca_cod='OPOSCOPIACIF';
update e_pca set pca_nor=73 where pca_pro='DISCP' and pca_cod='OPOSEPIGIAE';




CREATE SEQUENCE  SEQ_MELANBIDE10_SUBSOLIC  MINVALUE 0 MAXVALUE 99999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE;

  CREATE TABLE MELANBIDE10_SUBSOLIC 
   (	ID NUMBER(8,0) NOT NULL ENABLE, 
	NUM_EXP VARCHAR2(30 BYTE), 
	ESTADO VARCHAR2(1 BYTE), 
	ORGANISMO VARCHAR2(150 BYTE), 
	OBJETO VARCHAR2(150 BYTE), 
    IMPORTE NUMBER(8,2), 
	FECHA DATE,  
	 CONSTRAINT MELANBIDE10_SUBSOLIC_PK PRIMARY KEY (ID)
 )
 ;

   COMMENT ON COLUMN MELANBIDE10_SUBSOLIC.ESTADO IS 'Estado de la Ayuda';
   COMMENT ON COLUMN MELANBIDE10_SUBSOLIC.ORGANISMO IS 'Organismo concedente';
   COMMENT ON COLUMN MELANBIDE10_SUBSOLIC.OBJETO IS 'Descripción objeto de la ayuda';
   COMMENT ON COLUMN MELANBIDE10_SUBSOLIC.FECHA IS 'Fecha Solicitud o Concesión';