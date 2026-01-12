CREATE OR REPLACE FUNCTION ES_TRAMITE_ABIERTO(num_expediente IN VARCHAR2, cod_procedimiento IN VARCHAR2, num_tramite IN VARCHAR2)
RETURN NUMBER IS
    v_count NUMBER := 0;
    v_return NUMBER := 0;
BEGIN
    SELECT COUNT(*) INTO v_count 
        FROM e_cro INNER JOIN E_TRA on CRO_PRO=TRA_PRO AND TRA_COD=CRO_TRA AND tra_fba is null
        WHERE 
            cro_num = num_expediente AND
            cro_pro = cod_procedimiento AND
            tra_cou = num_tramite AND
            cro_fef IS NULL;
            
    IF v_count = 1 THEN
        v_return := 1;
    END IF;
    RETURN v_return;
end ES_TRAMITE_ABIERTO;