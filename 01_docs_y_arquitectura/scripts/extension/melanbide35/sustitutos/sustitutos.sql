----------------------------
--- ECA_JUS_PREPARADORES ---
----------------------------

ALTER TABLE ECA_JUS_PREPARADORES ADD (ECA_PREP_TIPO_SUST NUMBER(1, 0) );

COMMENT ON COLUMN ECA_JUS_PREPARADORES.ECA_PREP_TIPO_SUST IS 'Tipo de sustituto (1: Solicitud; 2: Justificación)';

----------------------------
--- ECA_JUS_PROSPECTORES ---
----------------------------

ALTER TABLE ECA_JUS_PROSPECTORES ADD (ECA_PROS_TIPO_SUST NUMBER(1, 0) );

COMMENT ON COLUMN ECA_JUS_PROSPECTORES.ECA_PROS_TIPO_SUST IS 'Tipo de sustituto (1: Solicitud; 2: Justificación)';


----------------------------
--- ECA_SOL_PREPARADORES ---
----------------------------

ALTER TABLE ECA_SOL_PREPARADORES ADD (ECA_PREP_TIPO_SUST NUMBER(1, 0) );

COMMENT ON COLUMN ECA_SOL_PREPARADORES.ECA_PREP_TIPO_SUST IS 'Tipo de sustituto (1: Solcitud; 2: Justificacion)';

ALTER TABLE ECA_SOL_PREPARADORES ADD (ECA_PREP_CODORIGEN NUMBER(8, 0) );

COMMENT ON COLUMN ECA_SOL_PREPARADORES.ECA_PREP_CODORIGEN IS 'Codigo del preparador al que sustituye';


----------------------------
--- ECA_SOL_PROSPECTORES ---
----------------------------

ALTER TABLE ECA_SOL_PROSPECTORES ADD (ECA_PROS_TIPO_SUST NUMBER(1, 0) );

COMMENT ON COLUMN ECA_SOL_PROSPECTORES.ECA_PROS_TIPO_SUST IS 'Tipo de sustituto (1: Solcitud; 2: Justificacion)';

ALTER TABLE ECA_SOL_PROSPECTORES ADD (ECA_PROS_CODORIGEN NUMBER(8, 0) );

COMMENT ON COLUMN ECA_SOL_PROSPECTORES.ECA_PROS_CODORIGEN IS 'Codigo del prospector al que sustituye';


----------------------------
------ ECA_SOLICITUD -------
----------------------------

update eca_solicitud set eca_exp_eje = substr(eca_numexp, 1, 4);

----------------------------
--- ECA_JUS_PREPARADORES ---
----------------------------

update ECA_JUS_PREPARADORES set ECA_PREP_TIPO_SUST = 2 where eca_prep_codorigen is not null;

----------------------------
--- ECA_JUS_PROSPECTORES ---
----------------------------

update ECA_JUS_PROSPECTORES set ECA_PROS_TIPO_SUST = 2 where eca_pros_codorigen is not null;