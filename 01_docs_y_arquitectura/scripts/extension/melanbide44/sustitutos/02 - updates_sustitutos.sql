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