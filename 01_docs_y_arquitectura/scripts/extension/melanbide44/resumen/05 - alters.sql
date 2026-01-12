--------------------------------------
-------- ECA_SEG_PREPARADORES --------
--------------------------------------

ALTER TABLE ECA14_SEG_PREPARADORES ADD (ECA_SEG_CORRECTO VARCHAR2(1 BYTE) );

COMMENT ON COLUMN ECA14_SEG_PREPARADORES.ECA_SEG_CORRECTO IS 'Correcto (S/N)';

--------------------------------------
-------- ECA_VIS_PROSPECTORES --------
--------------------------------------

ALTER TABLE ECA14_VIS_PROSPECTORES ADD (ECA_VIS_CORRECTO VARCHAR2(1 BYTE) );

COMMENT ON COLUMN ECA14_VIS_PROSPECTORES.ECA_VIS_CORRECTO IS 'Correcto (S/N)';