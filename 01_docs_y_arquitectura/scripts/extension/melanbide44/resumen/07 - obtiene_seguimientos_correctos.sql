create or replace 
PROCEDURE   obtiene_seguimientos_correctos (expediente in varchar2, codigo out varchar2) iS


  TYPE lista IS TABLE OF VARCHAR2 (100);
  colectivos   lista := lista ();

  correcto BOOLEAN;
  
  nif_preparador VARCHAR2(10);
  
  ano_expediente NUMBER(4);
  ano_inicio_contrato NUMBER(4);
  
  cont_select NUMBER;
  
  encontrado BOOLEAN;
  comb_colectivo VARCHAR2(2);
  col VARCHAR2(2);
  cont_colectivos NUMBER;
  
  yearDiff NUMBER;
  monthDiff NUMBER;
  
  flag VARCHAR2(1);

CURSOR seguimientos IS
       select seg.* from eca_jus_preparadores prep
       inner join eca_seg_preparadores seg on prep.eca_jus_preparadores_cod = seg.eca_jus_preparadores_cod
       inner join eca_solicitud sol on prep.eca_prep_solicitud = sol.eca_solicitud_cod
       where sol.eca_numexp = expediente
       --and seg.eca_seg_tipo = tipo
       ;
       
begin
  codigo := '0';
  correcto := TRUE;
      FOR seg IN seguimientos LOOP
        BEGIN
          correcto := validarNif(seg.ECA_SEG_NIF);
          --DBMS_OUTPUT.PUT_LINE('CORRECTO 1');
          IF correcto THEN
          --DBMS_OUTPUT.PUT_LINE('CORRECTO 2');
            select ECA_PREP_NIF into nif_preparador from eca_jus_preparadores p where p.eca_jus_preparadores_cod = seg.eca_jus_preparadores_cod;
            correcto := validarNif(nif_preparador);
            IF correcto THEN
            --DBMS_OUTPUT.PUT_LINE('CORRECTO 3');
              IF seg.ECA_SEG_FECINI IS NOT NULL AND seg.ECA_SEG_FECFIN IS NOT NULL THEN
                correcto := seg.ECA_SEG_FECINI <= seg.ECA_SEG_FECFIN;
              END IF;
              IF correcto THEN
              --DBMS_OUTPUT.PUT_LINE('CORRECTO 4');
                ano_expediente := TO_NUMBER(SUBSTR(expediente, 1, 4));
                ano_inicio_contrato := TO_NUMBER(SUBSTR(TO_CHAR(seg.ECA_SEG_FECINI, 'yyyy/mm/dd'), 1, 4));
                --DBMS_OUTPUT.PUT_LINE('ANO EXPEDIENTE: '||ano_expediente);
                --DBMS_OUTPUT.PUT_LINE('ANO INICIO CONTRATO: '||ano_inicio_contrato);
                --DBMS_OUTPUT.PUT_LINE('TIPO: '||seg.ECA_SEG_TIPO);
                IF seg.ECA_SEG_TIPO = 0 THEN
                  IF ano_inicio_contrato >= ano_expediente THEN
                    correcto := FALSE;
                  END IF;
                ELSE
                  IF ano_inicio_contrato <> ano_expediente THEN
                    correcto := FALSE;
                  END IF;  
                END IF;
                IF correcto THEN
                --DBMS_OUTPUT.PUT_LINE('CORRECTO 5');
                  IF seg.ECA_SEG_FECNACIMIENTO IS NULL THEN
                    correcto := FALSE;
                  ELSIF seg.ECA_SEG_HORAS IS NULL THEN
                    correcto := FALSE;
                  END IF;
                  IF correcto THEN
                  --DBMS_OUTPUT.PUT_LINE('CORRECTO 6');
                    IF seg.ECA_SEG_PORCJORNADA IS NULL THEN
                      correcto := FALSE;
                    ELSIF seg.ECA_SEG_PORCJORNADA > 100 THEN
                      correcto := FALSE;
                    ELSIF seg.ECA_SEG_TIPO = 1 AND seg.ECA_SEG_PORCJORNADA < 35 THEN
                      correcto := FALSE;
                    END IF;
                    IF correcto THEN
                    --DBMS_OUTPUT.PUT_LINE('CORRECTO 7');
                      IF seg.ECA_SEG_TIPODISCAPACIDAD IS NULL THEN
                        correcto := FALSE;
                      ELSE
                        select count(*) into cont_select from ECA_TIPODISCAPACIDAD p where p.COD_TDISCAPACIDAD = seg.ECA_SEG_TIPODISCAPACIDAD;
                        IF cont_select = 0 THEN
                          correcto := FALSE;
                        END IF;  
                      END IF;  
                      
                      IF correcto THEN
                      --DBMS_OUTPUT.PUT_LINE('CORRECTO 8');
                        IF seg.ECA_SEG_DISCGRAVEDAD IS NULL THEN
                          correcto := FALSE;
                        ELSE
                          select count(*) into cont_select from ECA_DISCAPGRAVEDAD p where p.COD_GRAVEDAD = seg.ECA_SEG_DISCGRAVEDAD;
                          IF cont_select = 0 THEN
                            correcto := FALSE;
                          END IF;  
                        END IF;
                        
                        IF correcto THEN
                        --DBMS_OUTPUT.PUT_LINE('CORRECTO 9');
                          IF seg.ECA_SEG_TIPOCONTRATO IS NULL THEN
                            correcto := FALSE;
                          ELSE
                            select count(*) into cont_select from ECA_TIPOCONTRATO p where p.COD_TIPOCONTRATO = seg.ECA_SEG_TIPOCONTRATO;
                            IF cont_select = 0 THEN
                              correcto := FALSE;
                            END IF;  
                          END IF;
                          
                          IF correcto THEN
                          --DBMS_OUTPUT.PUT_LINE('CORRECTO 10');
                            IF seg.ECA_SEG_SEXO IS NOT NULL THEN
                              IF seg.ECA_SEG_SEXO <> 0 AND seg.ECA_SEG_SEXO <> 1 THEN
                                correcto := FALSE;
                              END IF;
                            END IF;
                            
                            IF correcto THEN
                            --DBMS_OUTPUT.PUT_LINE('CORRECTO 11');
                              IF seg.ECA_SEG_EMPRESA IS NULL THEN
                                correcto := FALSE;
                              END IF;
                              
                              IF correcto THEN
                              --DBMS_OUTPUT.PUT_LINE('CORRECTO 12');
                                comb_colectivo := seg.ECA_SEG_TIPODISCAPACIDAD || seg.ECA_SEG_DISCGRAVEDAD;
                                cont_colectivos := 1;
                                encontrado := FALSE;
                                WHILE cont_colectivos < colectivos.COUNT AND NOT encontrado LOOP
                                  col := colectivos(cont_colectivos);
                                  IF comb_colectivo = col THEN
                                    encontrado := TRUE;
                                  ELSE 
                                    cont_colectivos := cont_colectivos + 1;
                                  END IF;  
                                END LOOP;
                                IF encontrado THEN
                                  yearDiff := TO_NUMBER(SUBSTR(TO_CHAR(seg.ECA_SEG_FECFIN, 'yyyy/mm/dd'), 1, 4)) - TO_NUMBER(SUBSTR(TO_CHAR(seg.ECA_SEG_FECINI, 'yyyy/mm/dd'), 1, 4));
                                  monthDiff := (yearDiff * 12) + (TO_NUMBER(SUBSTR(TO_CHAR(seg.ECA_SEG_FECFIN, 'yyyy/mm/dd'), 6, 2)) - TO_NUMBER(SUBSTR(TO_CHAR(seg.ECA_SEG_FECINI, 'yyyy/mm/dd'), 6, 2)));
                                  
                                  IF monthDiff < 6 THEN
                                    correcto := FALSE;
                                  END IF;  
                                END IF;
                              END IF;
                            END IF;
                          END IF;  
                        END IF;  
                      END IF;
                    END IF;
                  END IF;
                END IF;
              END IF;
            END IF;
          END IF;  
          
          IF correcto THEN
            --DBMS_OUTPUT.PUT_LINE('PREPARADOR: '||seg.ECA_JUS_PREPARADORES_COD||' SEGUIMIENTO: '||seg.ECA_SEG_PREPARADORES_COD||' CORRECTO: SI');
            flag := 'S';
          ELSE
            --DBMS_OUTPUT.PUT_LINE('PREPARADOR: '||seg.ECA_JUS_PREPARADORES_COD||' SEGUIMIENTO: '||seg.ECA_SEG_PREPARADORES_COD||' CORRECTO: NO');
			--EXIT;
            flag := 'N';
          END IF;
          update ECA_SEG_PREPARADORES set ECA_SEG_CORRECTO = flag where ECA_JUS_PREPARADORES_COD = seg.ECA_JUS_PREPARADORES_COD and ECA_SEG_PREPARADORES_COD = seg.ECA_SEG_PREPARADORES_COD;
          
          exception
          when others then
            correcto := FALSE;
            codigo := '1';
        END;  
        
      END LOOP;
      
      commit;
      
      exception
        when others then
          codigo := '2';
END obtiene_seguimientos_correctos;      