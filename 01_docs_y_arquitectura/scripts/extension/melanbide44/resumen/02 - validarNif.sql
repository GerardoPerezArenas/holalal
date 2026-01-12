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