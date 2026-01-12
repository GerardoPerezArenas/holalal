--------------------------------------------------------
--  DDL for Table ECA_RESUMEN
--------------------------------------------------------

CREATE TABLE ECA_RESUMEN 
(
  ECA_COD_RESUMEN NUMBER(8, 0) NOT NULL 
, ECA_RES_SOLICITUD NUMBER(8, 0) 
, ECA_RES_SUB_PRIV NUMBER(10, 2) 
, ECA_RES_SUB_PUB NUMBER(10, 2) 
, ECA_RES_TOT_SUBV NUMBER(10, 2) 
, CONSTRAINT ECA_RESUMEN_PK PRIMARY KEY 
  (
    ECA_COD_RESUMEN 
  )
  ENABLE 
);

--------------------------------------------------------
--  DDL for Table ECA_RES_PREPARADORES
--------------------------------------------------------

  CREATE TABLE ECA_RES_PREPARADORES
   (	ECA_RES_PREPARADORES_COD NUMBER, 
	ECA_NUMEXP VARCHAR2(30 BYTE), 
	ECA_JUS_PREPARADORES_COD NUMBER, 
	NIF VARCHAR2(10 BYTE), 
	NOMBRE VARCHAR2(200 BYTE), 
	GASTOS_SALARIALES_SOLICITADOS NUMBER(8,2), 
	GASTOS_SALARIALES_CONCEDIDOS NUMBER(8,2), 
	GASTOS_SALARIALES_JUSTIFICADOS NUMBER(8,2), 
	IMPORTE_SEGUIMIENTOS NUMBER(8,2), 
	IMPORTE_INS_CONCEDIDO NUMBER(8,2), 
	IMPORTE_INS_JUSTIFICADAS NUMBER(8,2), 
	IMPORTE_INSERCIONES NUMBER(8,2), 
	IMPORTE_SEG_INSERCIONES NUMBER(8,2), 
	A_PAGAR NUMBER(8,2), 
	FEC_SYSDATE DATE
   );
   
--------------------------------------------------------
--  DDL for Table ECA_RES_PROSPECTORES
--------------------------------------------------------

  CREATE TABLE ECA_RES_PROSPECTORES
   (	ECA_RES_PROSPECTORES_COD NUMBER, 
	ECA_NUMEXP VARCHAR2(30 BYTE), 
	ECA_JUS_PROSPECTORES_COD NUMBER, 
	NIF VARCHAR2(10 BYTE), 
	NOMBRE VARCHAR2(200 BYTE), 
	GASTOS_SALARIALES_SOLICITADOS NUMBER(8,2), 
	GASTOS_SALARIALES_CONCEDIDOS NUMBER(8,2), 
	GASTOS_SALARIALES_JUSTIFICADOS NUMBER(8,2), 
	VISITAS_CONCEDIDAS NUMBER, 
	VISITAS_JUSTIFICADAS NUMBER, 
	IMPORTE_VISITAS NUMBER(8,2), 
	A_PAGAR NUMBER(8,2), 
	FEC_SYSDATE DATE
   );
   
   
   
--------------------------------------
-------- ECA_SEG_PREPARADORES --------
--------------------------------------

ALTER TABLE ECA_SEG_PREPARADORES ADD (ECA_SEG_CORRECTO VARCHAR2(1 BYTE) );

COMMENT ON COLUMN ECA_SEG_PREPARADORES.ECA_SEG_CORRECTO IS 'Correcto (S/N)';

--------------------------------------
-------- ECA_VIS_PROSPECTORES --------
--------------------------------------

ALTER TABLE ECA_VIS_PROSPECTORES ADD (ECA_VIS_CORRECTO VARCHAR2(1 BYTE) );

COMMENT ON COLUMN ECA_VIS_PROSPECTORES.ECA_VIS_CORRECTO IS 'Correcto (S/N)';



--------------------------------------------------------
--  DDL for Sequence SEQ_ECA_RESUMEN
--------------------------------------------------------

CREATE SEQUENCE  "SEQ_ECA_RESUMEN"  MINVALUE 0 MAXVALUE 99999999 INCREMENT BY 1 START WITH 0;

--------------------------------------------------------
--  DDL for Sequence ECA_RES_PREPARADORES_SQ
--------------------------------------------------------

CREATE SEQUENCE  "ECA_RES_PREPARADORES_SQ"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0;

--------------------------------------------------------
--  DDL for Sequence ECA_RES_PROSPECTORES_SQ
--------------------------------------------------------

CREATE SEQUENCE  "ECA_RES_PROSPECTORES_SQ"  MINVALUE 0 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 0;


----------------------------
-------- VALIDARCIF --------
----------------------------

create or replace 
FUNCTION validarCif(cif IN VARCHAR2) RETURN BOOLEAN  IS
    
  correcto BOOLEAN;
  
  upper_cif VARCHAR2(10);
  
  suma NUMBER := 0;
  contador NUMBER := 2;
  temporal NUMBER := 0;
  codigoControl NUMBER := 0;
  cadenaTemporal VARCHAR2(8);
  valoresCif VARCHAR2(20) := 'ABCDEFGHJKLMNPQRSUVW';
  letraControlCIF VARCHAR2(10) := '0123456789';
  letraSociedadNumerica VARCHAR2(10) := 'KLMNPQRSW0';
  primeraLetra VARCHAR2(1);
  ultimaLetra VARCHAR2(1);
  letraControl VARCHAR2(1);
  posLetraControlCif NUMBER;
  
  patron_letras_numeros VARCHAR2(12) := '[^A-Za-z0-9]';
    
BEGIN
  correcto := TRUE;
  upper_cif := UPPER(cif);
  
  DBMS_OUTPUT.PUT_LINE(upper_cif);
  
  IF upper_cif = 'B48561003' OR upper_cif = 'K8903850' OR upper_cif = 'K16052710' OR upper_cif = '5092637T' OR upper_cif = 'A20066781' THEN
    correcto := TRUE;
  ELSE
    IF LENGTH(upper_cif) <> 9 THEN
      correcto := FALSE;
    END IF;
    
    IF correcto THEN
      DBMS_OUTPUT.PUT_LINE('CIF 1');
      IF REGEXP_LIKE(upper_cif, patron_letras_numeros) THEN
        correcto := FALSE;
      END IF;  
      DBMS_OUTPUT.PUT_LINE('CIF 2');
      
      IF correcto THEN
        primeraLetra := SUBSTR(upper_cif, 1, 1);
        
        ultimaLetra := SUBSTR(upper_cif, 9, 1);
        
        DBMS_OUTPUT.PUT_LINE('PRIMERA LETRA = '||primeraLetra);
        DBMS_OUTPUT.PUT_LINE('ULTIMA LETRA = '||ultimaLetra);
        
        IF INSTR(valoresCif, primeraLetra, 1) = 0 THEN
          correcto := FALSE;
        END IF;
        
        suma := suma + TO_NUMBER(SUBSTR(upper_cif, 3, 1)) + TO_NUMBER(SUBSTR(upper_cif, 5, 1)) + TO_NUMBER(SUBSTR(upper_cif, 7, 1));
    
        
        WHILE contador <= 8 LOOP
          temporal := TO_NUMBER(SUBSTR(upper_cif, contador, 1)) * 2;
          
          IF TEMPORAL < 10 THEN
            suma := suma + temporal;
          ELSE
            cadenaTemporal := TO_CHAR(temporal);
            suma := suma + TO_NUMBER(SUBSTR(cadenaTemporal, 1, 1)) + TO_NUMBER(SUBSTR(cadenaTemporal, 2, 1));
          END IF;
          contador := contador + 2;
        END LOOP;
        
        codigoControl := MOD((10 - MOD(suma, 10)), 10);
        
        IF INSTR(letraSociedadNumerica, primeraLetra, 1) > 0 THEN
          IF codigoControl = 0 THEN
            codigoControl := 10;
          END IF;
          
          codigoControl := codigoControl + 64;
          
          letraControl := CHR(codigoControl);
          
          If ultimaLetra <> letraControl THEN
            correcto := FALSE;
          END IF;
        ELSE
          posLetraControlCif := INSTR(letraControlCIF, ultimaLetra, 1) - 1;
          IF codigoControl <> posLetraControlCif THEN
            correcto := FALSE;
          END IF;  
        END IF;
      END IF;  
    END IF;  
  END IF;
  
  return correcto;
  
END;  
/



----------------------------
-------- VALIDARNIF --------
----------------------------

create or replace 
FUNCTION validarNif(nif IN VARCHAR2) RETURN BOOLEAN  IS
    
    upper_nif VARCHAR2(15);
    correcto BOOLEAN;
    patron_nif VARCHAR2(21) := '[0-9]{8,8}[a-zA-z]{1}';
    patron_nie VARCHAR2(38) := '[k-mK-Mx-zX-Z]{1}[0-9]{7,7}[a-zA-z]{1}';
    letras_nif VARCHAR2(24) := 'TRWAGMYFPDXBNJZSQVHLCKET';
    
    numero VARCHAR2(8);
    primer_caracter VARCHAR2(1);
    letra VARCHAR2(1);
    letra_calculada VARCHAR2(1);
    posicion NUMBER(2);
    
BEGIN
  upper_nif := UPPER(nif);
  --DBMS_OUTPUT.PUT_LINE(upper_nif);
  correcto := FALSE;
  IF REGEXP_LIKE(upper_nif, patron_nif) THEN
    correcto := TRUE;
  ELSIF REGEXP_LIKE(upper_nif, patron_nie) THEN
    correcto := TRUE;
  ELSE
    correcto := FALSE;
  END IF;
  
  IF correcto THEN
    primer_caracter := SUBSTR(upper_nif, 1, 1);
    
    IF primer_caracter = 'X' OR primer_caracter = 'Y' OR primer_caracter = 'Z' THEN
      --NIE
      
      numero := SUBSTR(upper_nif, 2, 7);
      
      IF primer_caracter = 'X' THEN
        numero := '0' || numero;
      ELSIF primer_caracter = 'Y' THEN
        numero := '1' || numero;
      ELSE
        numero := '2' || numero;
      END IF;  
    ELSE
      numero := SUBSTR(upper_nif, 1, 8);
    END IF;
    letra := SUBSTR(upper_nif, 9, LENGTH(upper_nif));
    --Calculamos la letra correcta
    posicion := MOD(numero, 23);
    --DBMS_OUTPUT.PUT_LINE(posicion);
    letra_calculada := SUBSTR(letras_nif, posicion+1, 1);
        --DBMS_OUTPUT.PUT_LINE(letra_calculada||'/'||letra);
    
    IF letra = letra_calculada THEN
      correcto := TRUE;
    ELSE  
      correcto := FALSE;
    END IF;
  END IF;
  
  return correcto;
  
END;  
/


create or replace 
FUNCTION esEntero(numero IN NUMBER) RETURN BOOLEAN  IS
    
  correcto BOOLEAN := TRUE;
  
  patron_numeros VARCHAR2(12) := '[0-9]';
    
BEGIN
    
    IF NOT REGEXP_LIKE(TO_CHAR(numero), patron_numeros) THEN
      correcto := FALSE;
    END IF;  
  
  return correcto;
  
END;  
/


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
/


create or replace 
PROCEDURE   obtiene_visitas_correctas (expediente in varchar2, codigo out varchar2) iS


  TYPE lista IS TABLE OF VARCHAR2 (100);
  colectivos   lista := lista ();

  correcto BOOLEAN;
  
  nif_prospector VARCHAR2(10);
  
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

CURSOR seguimientos IS
      select vis.* from eca_vis_prospectores vis  
      inner join eca_jus_prospectores pros  ON pros.eca_jus_prospectores_cod = vis.eca_jus_prospectores_cod  
      inner join eca_solicitud sol on pros.eca_pros_solicitud = sol.eca_solicitud_cod
      where sol.eca_numexp = expediente;
       
begin

  correcto := TRUE;
  codigo := '0';
      FOR vis IN seguimientos LOOP
        BEGIN
          correcto := validarCif(vis.ECA_VIS_CIF) OR validarNif(vis.ECA_VIS_CIF);
          --DBMS_OUTPUT.PUT_LINE('CORRECTO 1');
          IF correcto THEN
          --DBMS_OUTPUT.PUT_LINE('CORRECTO 2');
            select ECA_PROS_NIF into nif_prospector from eca_jus_prospectores p where p.eca_jus_prospectores_cod = vis.eca_jus_prospectores_cod;
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
                      select count(*) into cont_select from ECA_SECTORACT WHERE COD_SECTOR = vis.ECA_VIS_SECTOR;
                      IF cont_select = 0 THEN
                        correcto := FALSE;
                      END IF;
                    END IF;
                    
                    IF correcto THEN
                      --DBMS_OUTPUT.PUT_LINE('CORRECTO 7');
                      IF vis.ECA_VIS_CUMPLELISMI IS NOT NULL THEN
                        select count(*) into cont_select from ECA_CUMPLELISMI WHERE COD_CUMPLE = vis.ECA_VIS_CUMPLELISMI;
                        IF cont_select = 0 THEN
                          correcto := FALSE;
                        END IF;  
                      END IF;
                      
                      IF correcto THEN
                        --DBMS_OUTPUT.PUT_LINE('CORRECTO 8');
                        IF vis.ECA_VIS_RESULTADOFINAL IS NOT NULL THEN
                          select count(*) into cont_select from ECA_RESULTADO WHERE COD_RESULTADO = vis.ECA_VIS_RESULTADOFINAL;
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
          
          update ECA_VIS_PROSPECTORES set ECA_VIS_CORRECTO = flag where ECA_JUS_PROSPECTORES_COD = vis.ECA_JUS_PROSPECTORES_COD and ECA_VIS_PROSPECTORES_COD = vis.ECA_VIS_PROSPECTORES_COD;
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
END obtiene_visitas_correctas;     
/ 

create or replace 
FUNCTION CARGA_RESUMEN_ECA_sust_PREP (EXPEDIENTE VARCHAR2 ) RETURN VARCHAR2
IS
  CODIGO VARCHAR2(100) := 'OK';
  PREP_JUST NUMBER;
  PREP_JUST_ANT NUMBER;
  PREP_SUST_origen NUMBER;
  PREP_SUST NUMBER;
  A_PAGAR_SUST NUMBER(8,2);
  A_PAGAR_PTE NUMBER(8,2);
  GSALARIAL NUMBER (8,2);
  IMP_INS_SEG NUMBER (8,2);
  A_PAGAR_PREP NUMBER (8,2);
  


  
-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------

    CURSOR PREPARADORES_JUST IS
      select 
        a.ECA_JUS_PREPARADORES_COD,
        CASE 
          WHEN GASTOS_SALARIALES_CONCEDIDOS <= GASTOS_SALARIALES_JUSTIFICADOS THEN GASTOS_SALARIALES_CONCEDIDOS
          ELSE GASTOS_SALARIALES_JUSTIFICADOS
        END GASTOS_SALARIALES,
        IMPORTE_SEG_INSERCIONES,
        A_PAGAR       
      from eca_res_preparadores a 
      inner join eca_jus_preparadores b on a.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
      where 
        (ECA_PREP_TIPO_SUST not in (2) or ECA_PREP_TIPO_SUST is null)
      and  eca_numexp=EXPEDIENTE
      ORDER BY A.ECA_JUS_PREPARADORES_COD;
      
      CURSOR PREPARADORES_SUST (PREP number) IS
      select 
        a.ECA_JUS_PREPARADORES_COD,
        b.eca_prep_codorigen,
        A.A_PAGAR
      from eca_res_preparadores a 
      inner join eca_jus_preparadores b on a.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
      where 
        ECA_PREP_TIPO_SUST=2
      and  b.eca_prep_codorigen=PREP
      ORDER BY A.ECA_JUS_PREPARADORES_COD;
      
        
BEGIN      
dbms_output.put_line('INICIO FUNCION CARGA_RESUMEN_ECA_sust_PREP: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss')); 
--CAMBIO:=0;

FOR REG_PREPARADORES_JUST IN PREPARADORES_JUST LOOP
  PREP_JUST:=REG_PREPARADORES_JUST.ECA_JUS_PREPARADORES_COD;
  GSALARIAL:=REG_PREPARADORES_JUST.GASTOS_SALARIALES;
  IMP_INS_SEG:=REG_PREPARADORES_JUST.IMPORTE_SEG_INSERCIONES;
  A_PAGAR_PREP:=REG_PREPARADORES_JUST.A_PAGAR;
  
  IF (GSALARIAL-A_PAGAR_PREP >0) THEN 
    A_PAGAR_PTE:=GSALARIAL-A_PAGAR_PREP;
  ELSE 
    A_PAGAR_PTE:=0;
  END IF;
 
  dbms_output.put_line('PREPARADOR TITULAR: '||PREP_JUST); 
  
  
  FOR REG_PREPARADORES_SUST IN PREPARADORES_SUST (PREP_JUST)LOOP
    PREP_SUST:=REG_PREPARADORES_SUST.ECA_JUS_PREPARADORES_COD;
    PREP_SUST_origen:=REG_PREPARADORES_SUST.eca_prep_codorigen;
    A_PAGAR_SUST:=REG_PREPARADORES_SUST.A_PAGAR;

    dbms_output.put_line('                    '); 
    dbms_output.put_line('    PREPARADOR SUSTITUTO: '||PREP_SUST); 
    dbms_output.put_line('    A_PAGAR TITULAR : '||A_PAGAR_PREP); 
    dbms_output.put_line('    A_PAGAR SUSTITUTO: '||A_PAGAR_SUST); 
    dbms_output.put_line('    DISPONIBLE: '||A_PAGAR_PTE); 
    dbms_output.put_line('                    '); 
    
    IF (A_PAGAR_PTE>0) THEN  
    -- EL SUSTITUTO PUEDE JUSTIFICAR
      IF (A_PAGAR_PTE-A_PAGAR_SUST >0)THEN
        A_PAGAR_PTE:=A_PAGAR_PTE-A_PAGAR_SUST;
        
        UPDATE eca_res_preparadores
        SET A_PAGAR=A_PAGAR_SUST
        WHERE ECA_JUS_PREPARADORES_COD=PREP_SUST;
        dbms_output.put_line('              ACTUALIZADO A_PAGAR A : '||A_PAGAR_SUST); 
        dbms_output.put_line('              DISPONIBLE : '||A_PAGAR_PTE); 
        
      ELSE 
        UPDATE eca_res_preparadores
        SET A_PAGAR=A_PAGAR_PTE
        WHERE ECA_JUS_PREPARADORES_COD=PREP_SUST;
        

        dbms_output.put_line('              ACTUALIZADO SUELDO A : '||A_PAGAR_PTE);     
        A_PAGAR_PTE:=0;
        dbms_output.put_line('              DISPONIBLE : '||A_PAGAR_PTE); 
      END IF;
    

    END IF;
  END LOOP;

END LOOP;  
 
 
 

dbms_output.put_line('FIN PROCESO CARGA_RESUMEN_ECA_sust_PREP: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss'));   
    RETURN CODIGO;
EXCEPTION  -- exception handlers begin

   WHEN OTHERS THEN  
      RETURN 'ERROR EN CONSULTA BBDD';

END CARGA_RESUMEN_ECA_sust_PREP;
/

create or replace 
FUNCTION CARGA_RESUMEN_ECA_sust_PROS (EXPEDIENTE VARCHAR2 ) RETURN VARCHAR2
IS
  CODIGO VARCHAR2(100);
  PROS_JUST NUMBER;
  PROS_JUST_ANT NUMBER;
  PROS_SUST_origen NUMBER;
  PROS_SUST NUMBER;
  A_PAGAR_SUST NUMBER(8,2);
  A_PAGAR_PTE NUMBER(8,2);
  GSALARIAL NUMBER (8,2);
  visitas NUMBER (8,2);
  A_PAGAR_PROS NUMBER (8,2);
  


  
-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------

    CURSOR PROSPECTORES_JUST IS
      select 
        a.ECA_JUS_PROSPECTORES_COD,
        CASE 
          WHEN GASTOS_SALARIALES_CONCEDIDOS <= GASTOS_SALARIALES_JUSTIFICADOS THEN GASTOS_SALARIALES_CONCEDIDOS
          ELSE GASTOS_SALARIALES_JUSTIFICADOS
        END GASTOS_SALARIALES,
        CASE 
          WHEN VISITAS_CONCEDIDAS<=VISITAS_JUSTIFICADAS THEN VISITAS_CONCEDIDAS
          ELSE VISITAS_JUSTIFICADAS
        END visitas,
        A_PAGAR 
      from eca_res_PROSPECTORES a 
      inner join eca_jus_PROSPECTORES b on a.ECA_JUS_PROSPECTORES_COD=b.ECA_JUS_PROSPECTORES_COD
      where 
        (ECA_PROS_TIPO_SUST not in (2) or ECA_PROS_TIPO_SUST is null)
      and  eca_numexp=EXPEDIENTE
      ORDER BY A.ECA_JUS_PROSPECTORES_COD;
      
      CURSOR PROSPECTORES_SUST (PROS number) IS
      select 
        a.ECA_JUS_PROSPECTORES_COD,
        b.eca_PROS_codorigen,
        A.A_PAGAR
      from eca_res_PROSPECTORES a 
      inner join eca_jus_PROSPECTORES b on a.ECA_JUS_PROSPECTORES_COD=b.ECA_JUS_PROSPECTORES_COD
      where 
        ECA_PROS_TIPO_SUST=2
      and  b.eca_PROS_codorigen=PROS
      ORDER BY A.ECA_JUS_PROSPECTORES_COD;
      
        
BEGIN      
dbms_output.put_line('INICIO FUNCION CARGA_RESUMEN_ECA_sust_PROS: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss')); 
--CAMBIO:=0;

FOR REG_PROSPECTORES_JUST IN PROSPECTORES_JUST LOOP
  PROS_JUST:=REG_PROSPECTORES_JUST.ECA_JUS_PROSPECTORES_COD;
  GSALARIAL:=REG_PROSPECTORES_JUST.GASTOS_SALARIALES;
  visitas:=REG_PROSPECTORES_JUST.visitas;
  A_PAGAR_PROS:=REG_PROSPECTORES_JUST.A_PAGAR;
  
  IF (GSALARIAL-A_PAGAR_PROS >0) THEN 
    A_PAGAR_PTE:=GSALARIAL-A_PAGAR_PROS;
  ELSE 
    A_PAGAR_PTE:=0;
  END IF;
 
  dbms_output.put_line('PROSPECTOR TITULAR: '||PROS_JUST); 
  
  
  FOR REG_PROSPECTORES_SUST IN PROSPECTORES_SUST (PROS_JUST)LOOP
    PROS_SUST:=REG_PROSPECTORES_SUST.ECA_JUS_PROSPECTORES_COD;
    PROS_SUST_origen:=REG_PROSPECTORES_SUST.eca_PROS_codorigen;
    A_PAGAR_SUST:=REG_PROSPECTORES_SUST.A_PAGAR;

    dbms_output.put_line('                    '); 
    dbms_output.put_line('    PROSPECTOR SUSTITUTO: '||PROS_SUST); 
    dbms_output.put_line('    A_PAGAR TITULAR : '||A_PAGAR_PROS); 
    dbms_output.put_line('    A_PAGAR SUSTITUTO: '||A_PAGAR_SUST); 
    dbms_output.put_line('    DISPONIBLE: '||A_PAGAR_PTE); 
    dbms_output.put_line('                    '); 
    
    IF (A_PAGAR_PTE>0) THEN  
    -- EL SUSTITUTO PUEDE JUSTIFICAR
      IF (A_PAGAR_PTE-A_PAGAR_SUST >0)THEN
        A_PAGAR_PTE:=A_PAGAR_PTE-A_PAGAR_SUST;
        
        UPDATE eca_res_PROSPECTORES
        SET A_PAGAR=A_PAGAR_SUST
        WHERE ECA_JUS_PROSPECTORES_COD=PROS_SUST;
        dbms_output.put_line('              ACTUALIZADO A_PAGAR A : '||A_PAGAR_SUST); 
        dbms_output.put_line('              DISPONIBLE : '||A_PAGAR_PTE); 
        
      ELSE 
        UPDATE eca_res_PROSPECTORES
        SET A_PAGAR=A_PAGAR_PTE
        WHERE ECA_JUS_PROSPECTORES_COD=PROS_SUST;
        

        dbms_output.put_line('              ACTUALIZADO SUELDO A : '||A_PAGAR_PTE);     
        A_PAGAR_PTE:=0;
        dbms_output.put_line('              DISPONIBLE : '||A_PAGAR_PTE); 
      END IF;
    

    END IF;
  END LOOP;

END LOOP;  
 
 
 

dbms_output.put_line('FIN PROCESO CARGA_RESUMEN_ECA_sust_PROS: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss'));   
    RETURN CODIGO;
EXCEPTION  -- exception handlers begin

   WHEN OTHERS THEN  
      RETURN 'ERROR EN CONSULTA BBDD';

END CARGA_RESUMEN_ECA_sust_PROS;
/

create or replace 
PROCEDURE CARGA_RESUMEN_ECA (expediente in varchar2, codigo out varchar2) IS

  --CODIGO VARCHAR2(100);
 
BEGIN      
  dbms_output.put_line('INICIO FUNCION CARGA_RESUMEN_ECA: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss')); 
  CODIGO:='OK';
  
  delete ECA_RES_PROSPECTORES where ECA_NUMEXP=expediente;
  delete ECA_RES_PREPARADORES where ECA_NUMEXP=expediente;
  commit;
  
-------------------------------------------------------------------------------------------------------------------
-- carga de ECA_RES_PROSPECTORES
-------------------------------------------------------------------------------------------------------------------  
    
  --ojo, hay que completar el tratamiento de sustitutos
insert into ECA_RES_PROSPECTORES (ECA_RES_PROSPECTORES_COD,ECA_NUMEXP,ECA_JUS_PROSPECTORES_COD,NIF,NOMBRE,GASTOS_SALARIALES_SOLICITADOS,GASTOS_SALARIALES_CONCEDIDOS,
GASTOS_SALARIALES_JUSTIFICADOS,VISITAS_CONCEDIDAS,VISITAS_JUSTIFICADAS,IMPORTE_VISITAS,A_PAGAR,FEC_SYSDATE
)
select
  ECA_RES_PROSPECTORES_SQ.NEXTVAL, a.ECA_NUMEXP,
  B.ECA_JUS_PROSPECTORES_COD,
  coalesce(c.ECA_PROS_NIF,b.ECA_PROS_NIF) nif,
  coalesce(c.ECA_PROS_NOMBRE,b.ECA_PROS_NOMBRE) nombre,
  c.ECA_PROS_IMP_SS_ECA gastos_salariales_solicitados,
  c.ECA_PROS_IMP_SS_ECA gastos_salariales_concedidos,
  b.ECA_PROS_IMP_SS_ECA gastos_salariales_justificados,
  c.ECA_PROS_VISITAS visitas_concedidas,
  d.visitas visitas_justificadas,
   CASE 
    WHEN c.ECA_PROS_VISITAS*importe.imp_visita <= d.visitas*importe.imp_visita
       then c.ECA_PROS_VISITAS*importe.imp_visita               
    ELSE d.visitas*importe.imp_visita
  END IMPORTE_VISITAS,
      CASE 
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0 AND b.ECA_PROS_IMP_SS_ECA >= C.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita >= C.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita  >= C.ECA_PROS_IMP_SS_ECA 
    then c.ECA_PROS_IMP_SS_ECA
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0 AND b.ECA_PROS_IMP_SS_ECA <= C.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita >= b.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita  >= b.ECA_PROS_IMP_SS_ECA 
    then  b.ECA_PROS_IMP_SS_ECA   
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0 AND c.ECA_PROS_VISITAS*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita <= b.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
    then c.ECA_PROS_VISITAS*importe.imp_visita               
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and d.visitas*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita <= b.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
    then d.visitas*importe.imp_visita 
    when b.ECA_PROS_TIPO_SUST=2 and d.visitas*importe.imp_visita <= b.ECA_PROS_IMP_SS_ECA then d.visitas*importe.imp_visita
    when b.ECA_PROS_TIPO_SUST=2 and d.visitas*importe.imp_visita > b.ECA_PROS_IMP_SS_ECA then b.ECA_PROS_IMP_SS_ECA
  ELSE 0
  END A_PAGAR,SYSDATE
  
from eca_solicitud a
inner join eca_jus_prospectores b on a.ECA_SOLICITUD_COD=b.ECA_PROS_SOLICITUD 
inner join (select imp_visita from eca_configuracion 
where ano=substr(expediente,1,4)
) importe on 1=1
left join eca_sol_prospectores c on a.ECA_SOLICITUD_COD=c.ECA_PROS_SOLICITUD and b.ECA_SOL_PROSPECTORES_COD=c.ECA_SOL_PROSPECTORES_COD
left join 
(
  select eca_jus_prospectores_cod, count(*)  visitas
  from eca_vis_prospectores 
  where 
  ECA_VIS_CORRECTO ='S'
  group by eca_jus_prospectores_cod
)d on d.eca_jus_prospectores_cod=b.eca_jus_prospectores_cod
where ECA_NUMEXP=EXPEDIENTE;
commit;




-------------------------------------------------------------------------------------------------------------------
-- carga de ECA_RES_PREPARADORES
-------------------------------------------------------------------------------------------------------------------
--ojo, hay que completar el tratamiento de sustitutos

INSERT INTO ECA_RES_PREPARADORES (ECA_RES_PREPARADORES_COD,ECA_NUMEXP,ECA_JUS_PREPARADORES_COD,NIF,NOMBRE,GASTOS_SALARIALES_SOLICITADOS,GASTOS_SALARIALES_CONCEDIDOS,
GASTOS_SALARIALES_JUSTIFICADOS,IMPORTE_SEGUIMIENTOS,IMPORTE_INS_CONCEDIDO,IMPORTE_INS_JUSTIFICADAS,IMPORTE_INSERCIONES,
IMPORTE_SEG_INSERCIONES,A_PAGAR,FEC_SYSDATE)
SELECT 
  ECA_RES_PREPARADORES_SQ.NEXTVAL ECA_RES_PREPARADORES_COD , a.ECA_NUMEXP,
  b.ECA_JUS_PREPARADORES_COD,
  coalesce(c.ECA_PREP_NIF,b.ECA_PREP_NIF) nif,
  coalesce(c.ECA_PREP_NOMBRE,b.ECA_PREP_NOMBRE) nombre,
  c.ECA_PREP_IMP_SS_ECA gastos_salariales_solicitados,
  c.ECA_PREP_IMP_SS_ECA gastos_salariales_concedidos,
  b.ECA_PREP_IMP_SS_ECA gastos_salariales_justificados,
  IM_SEGUIMIENTO* nvl(d.seg,0) importe_seguimientos,
  nvl(eca_prep_ins_importe,0) importe_ins_concedido,
  nvl(E.IMPORTE_INSERCIONES,0) IMPORTE_INS_justificadas,
  case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
  else nvl(E.IMPORTE_INSERCIONES,0) end IMPORTE_INSERCIONES,
  IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
  else nvl(E.IMPORTE_INSERCIONES,0) end IMPORTE_SEG_INSERCIONES,
  CASE

    WHEN   COALESCE (c.ECA_PREP_IMP_SS_ECA,0) <> 0 AND COALESCE (c.ECA_PREP_IMP_SS_ECA,0) <=  b.ECA_PREP_IMP_SS_ECA AND 
        COALESCE (c.ECA_PREP_IMP_SS_ECA,0) <=  IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
        else nvl(E.IMPORTE_INSERCIONES,0) end THEN COALESCE (c.ECA_PREP_IMP_SS_ECA,0) 
    WHEN   
        b.ECA_PREP_IMP_SS_ECA <=  IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
        else nvl(E.IMPORTE_INSERCIONES,0) end THEN b.ECA_PREP_IMP_SS_ECA
    else   IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
  else nvl(E.IMPORTE_INSERCIONES,0) end 
        
  END A_PAGAR, sysdate FEC_SYSDATE 
  
  
FROM 
eca_solicitud a
inner join eca_jus_PREPARADORES b on a.ECA_SOLICITUD_COD=b.ECA_PREP_SOLICITUD 
inner join (select IM_SEGUIMIENTO,IM_C1_H,IM_C1_M,IM_C2_H,IM_C2_M,IM_C3_H,IM_C3_M,IM_C4_H,IM_C4_M from eca_configuracion 
where ano=substr(expediente,1,4)
) importe on 1=1
left join eca_sol_prEPARADORES c on a.ECA_SOLICITUD_COD=c.ECA_PREP_SOLICITUD and b.ECA_SOL_PREPARADORES_COD=c.ECA_SOL_PREPARADORES_COD
LEFT JOIN (
SELECT ECA_JUS_PREPARADORES_COD, COUNT(*) seg
FROM ECA_SEG_PREPARADORES 
WHERE ECA_SEG_TIPO=0 
and   ECA_SEG_CORRECTO ='S'
GROUP BY ECA_JUS_PREPARADORES_COD
)D on d.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
LEFT JOIN 
(
select ECA_JUS_PREPARADORES_COD, SUM(
  case 
    when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   IM_C1_H*ECA_SEG_PORCJORNADA/100--> c1h,
    when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   IM_C1_M*ECA_SEG_PORCJORNADA/100--> c1m,
    when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C2_H*ECA_SEG_PORCJORNADA/100--> c2h, 
    when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C2_M*ECA_SEG_PORCJORNADA/100--> c2m,
    when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 THEN    IM_C3_H*ECA_SEG_PORCJORNADA/100--> c3h,
    when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then   IM_C3_M*ECA_SEG_PORCJORNADA/100--> c3m, 
    when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C4_H*ECA_SEG_PORCJORNADA/100--> c4h,
    when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C4_M*ECA_SEG_PORCJORNADA/100--> c4m,
    ELSE 0 
  END ) IMPORTE_INSERCIONES
from eca_seg_preparadores aa inner join 
(select IM_SEGUIMIENTO,IM_C1_H,IM_C1_M,IM_C2_H,IM_C2_M,IM_C3_H,IM_C3_M,IM_C4_H,IM_C4_M from eca_configuracion 
where ano=substr(expediente,1,4)
) importe on 1=1
where 

ECA_SEG_CORRECTO ='S'

GROUP BY ECA_JUS_PREPARADORES_COD
)E ON E.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD

where ECA_NUMEXP=expediente;
COMMIT;

CODIGO:=CARGA_RESUMEN_ECA_sust_PREP(expediente);
CODIGO:=CARGA_RESUMEN_ECA_sust_PROS(expediente);





dbms_output.put_line('FIN PROCESO CARGA_RESUMEN_ECA: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss'));   
dbms_output.put_line(CODIGO);
    --RETURN CODIGO;
EXCEPTION  -- exception handlers begin

   WHEN OTHERS THEN  
      CODIGO := 'ERROR EN CONSULTA BBDD';

END CARGA_RESUMEN_ECA;
/