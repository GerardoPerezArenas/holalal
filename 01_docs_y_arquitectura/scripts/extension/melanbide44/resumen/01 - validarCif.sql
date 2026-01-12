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