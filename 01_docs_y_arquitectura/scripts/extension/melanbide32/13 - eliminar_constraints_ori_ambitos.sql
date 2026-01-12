alter table ori_ori_ubic drop constraint R_3;

alter table ori_ori_ubic add constraint "R_3" foreign key ("ORI_AMB_COD")
	  references "ORI_AMBITOS_HORAS" ("ORI_AMB_COD") on delete set null ENABLE


alter table ori_ce_ubic drop constraint R_7;

alter table ori_ce_ubic add constraint "R_7" foreign key ("ORI_AMB_COD")
references "ORI_AMBITOS_CE" ("ORI_AMB_COD") on delete set null enable