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