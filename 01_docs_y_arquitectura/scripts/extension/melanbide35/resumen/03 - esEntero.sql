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