create or replace
PROCEDURE   obtiene_visitas_correctas14 (expediente in varchar2, codigo out varchar2) iS


  TYPE lista IS TABLE OF VARCHAR2 (100);
  colectivos   lista := lista ();

  correcto BOOLEAN;
  
  nif_prospector VARCHAR2(10);
  fecIniPros DATE;
  fecFinPros DATE;  
  
  ano_expediente NUMBER(4);
  ano_visita NUMBER(4);
  
  cont_select NUMBER;
  
  encontrado BOOLEAN;
  comb_colectivo VARCHAR2(2);
  col VARCHAR2(2);
  cont_colectivos NUMBER;
  
  yearDiff NUMBER;
  monthDiff NUMBER;
  
  flag VARCHAR2(1);

CURSOR SEGUIMIENTOS IS
      SELECT VIS.* from eca14_vis_prospectores vis  
      inner join eca14_jus_prospectores pros  ON pros.eca_jus_prospectores_cod = vis.eca_jus_prospectores_cod  
      inner join eca14_solicitud sol on pros.eca_pros_solicitud = sol.eca_solicitud_cod
      where sol.eca_numexp = expediente;
       
begin

  correcto := TRUE;
  codigo := '0';
      FOR VIS IN SEGUIMIENTOS LOOP
        BEGIN
          CORRECTO := VALIDARCIF(TRIM(REPLACE(VIS.ECA_VIS_CIF, CHR(9), ''))) OR VALIDARNIF(TRIM(REPLACE(VIS.ECA_VIS_CIF, CHR(9), '')));
         IF NOT CORRECTO THEN 
         DBMS_OUTPUT.PUT_LINE('INCORRECTO'||VIS.ECA_VIS_CIF);
         END IF;
          IF correcto THEN
          --DBMS_OUTPUT.PUT_LINE('CORRECTO 2');
            select ECA_PROS_NIF,ECA_PROS_FECINI,ECA_PROS_FECFIN into nif_prospector, fecIniPros,fecFinPros from eca14_jus_prospectores p where p.eca_jus_prospectores_cod = vis.eca_jus_prospectores_cod;
            correcto := validarNif(nif_prospector);
            IF correcto THEN
            --DBMS_OUTPUT.PUT_LINE('CORRECTO 3');
              IF vis.ECA_VIS_FECVISITA IS NOT NULL THEN
                ano_expediente := TO_NUMBER(SUBSTR(expediente, 1, 4));
                ano_visita := TO_NUMBER(SUBSTR(TO_CHAR(vis.ECA_VIS_FECVISITA, 'yyyy/mm/dd'), 1, 4));
                
                --DBMS_OUTPUT.PUT_LINE('ANO_EXPEDIENTE'||ano_expediente);
                --DBMS_OUTPUT.PUT_LINE('ANO_VISITA'||ano_visita);
                
                IF ano_visita <> ano_expediente THEN
                  correcto := FALSE;
                END IF;
               
               --NUEVO COMPROBAR FECHA CON FECHAS DE PROSPECTOR
                IF FECINIPROS IS NOT NULL AND FECFINPROS IS NOT NULL THEN
                  IF vis.ECA_VIS_FECVISITA < fecIniPros OR vis.ECA_VIS_FECVISITA > fecFinPros THEN 
                     CORRECTO := FALSE;
                  END IF;
                END IF; 
                --FIN  COMPROBAR FECHA CON FECHAS DE PROSPECTOR
                
                IF correcto THEN
                --DBMS_OUTPUT.PUT_LINE('CORRECTO 4');
                --DBMS_OUTPUT.PUT_LINE('ECA_VIS_PROVINCIA: '||vis.ECA_VIS_PROVINCIA);
                  IF vis.ECA_VIS_PROVINCIA IS NOT NULL THEN
                    select count(*) into cont_select from (select COD from (select rownum as COD, UPPER(PRV_NOM) as NOMBRE  from 
                        FLBGEN.T_PRV P 
                        WHERE PRV_PAI=108  and prv_aut=14 order by PRV_COD) where COD = vis.ECA_VIS_PROVINCIA);
                    IF cont_select = 0 THEN
                      correcto := FALSE;
                    END IF;  
                  END IF;
                  
                  --DBMS_OUTPUT.PUT_LINE('CORRECTO 5');
                  
                  IF correcto THEN
                    --DBMS_OUTPUT.PUT_LINE('CORRECTO 6');
                    IF vis.ECA_VIS_SECTOR IS NOT NULL THEN
                      select count(*) into cont_select from ECA14_SECTORACT WHERE COD_SECTOR = vis.ECA_VIS_SECTOR;
                      IF cont_select = 0 THEN
                        correcto := FALSE;
                      END IF;
                    END IF;
                    
                    IF correcto THEN
                      --DBMS_OUTPUT.PUT_LINE('CORRECTO 7');
                      IF vis.ECA_VIS_CUMPLELISMI IS NOT NULL THEN
                        select count(*) into cont_select from ECA14_CUMPLELISMI WHERE COD_CUMPLE = vis.ECA_VIS_CUMPLELISMI;
                        IF cont_select = 0 THEN
                          correcto := FALSE;
                        END IF;  
                      END IF;
                      
                      IF correcto THEN
                        --DBMS_OUTPUT.PUT_LINE('CORRECTO 8');
                        IF vis.ECA_VIS_RESULTADOFINAL IS NOT NULL THEN
                          select count(*) into cont_select from ECA14_RESULTADO WHERE COD_RESULTADO = vis.ECA_VIS_RESULTADOFINAL;
                          IF cont_select = 0 THEN
                            correcto := FALSE;
                          END IF;  
                        END IF;
                        
                        IF correcto THEN
                        --DBMS_OUTPUT.PUT_LINE('CORRECTO 9');
                          IF vis.ECA_VIS_NUMTRAB IS NOT NULL THEN
                            IF NOT esEntero(vis.ECA_VIS_NUMTRAB) THEN
                            correcto := FALSE;
                            END IF;
                          END IF;
                          
                          IF correcto THEN
                          --DBMS_OUTPUT.PUT_LINE('CORRECTO 10');
                            IF vis.ECA_VIS_NUMTRABDISC IS NOT NULL THEN
                              IF NOT esEntero(vis.ECA_VIS_NUMTRABDISC) THEN
                                correcto := FALSE;
                              END IF;
                            END IF;
                            
                            IF correcto THEN
                            --DBMS_OUTPUT.PUT_LINE('CORRECTO 11');
                              IF vis.ECA_VIS_EMPRESA IS NULL THEN
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
          
          IF correcto THEN
            --DBMS_OUTPUT.PUT_LINE('PROSPECTOR: '||vis.ECA_JUS_PROSPECTORES_COD||' VISITA: '||vis.ECA_VIS_PROSPECTORES_COD||' CORRECTO: SI');
            flag := 'S';
          ELSE
            --DBMS_OUTPUT.PUT_LINE('PROSPECTOR: '||vis.ECA_JUS_PROSPECTORES_COD||' VISITA: '||vis.ECA_VIS_PROSPECTORES_COD||' CORRECTO: NO');
            --EXIT;
            flag := 'N';
          END IF;
          
          update ECA14_VIS_PROSPECTORES set ECA_VIS_CORRECTO = flag where ECA_JUS_PROSPECTORES_COD = vis.ECA_JUS_PROSPECTORES_COD and ECA_VIS_PROSPECTORES_COD = vis.ECA_VIS_PROSPECTORES_COD;
          commit;
          
          
          
          exception
          when others then
            correcto := FALSE;
            codigo := '1';
        END;
      END LOOP;
      
      exception
      when others then
        correcto := FALSE;
        codigo := '2';
END obtiene_visitas_correctas14;