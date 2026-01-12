-------------------------------
-------- ORI_AUDITORIA --------
-------------------------------

update ORI_AUDITORIA set COD_PROCEDIMIENTO = 'AYORI';
commit;

------------------------------
------- ORI_AMBITOS_CE -------
------------------------------

update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='34';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='37';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='39';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='42';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='45';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='46';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='47';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='49';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='61';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=1 where ORI_AMB_COD='62';
update ORI_AMBITOS_CE set ORI_AMB_DISTR=0 where ORI_AMB_COD>='33' and ORI_AMB_DISTR is null;