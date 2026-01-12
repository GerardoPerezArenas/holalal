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