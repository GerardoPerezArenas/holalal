create or replace 
PROCEDURE        consolida_horas(mensaje out varchar2)  iS

UBICACION  NUMBER(8);
HORAS  NUMBER(8,2);

w_mensaje  varchar2(100);
lines_out number(8);

-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------


    CURSOR EXPEDIENTES IS
        SELECT ORI_ORIENT_UBIC_COD,HORAS_ASIGNADAS  FROM ORI_TMP_ADJUDICACION;
        
Begin
    Dbms_Output.Put_Line('INICIO DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));


    
      
    
    FOR REG_AMBITO IN EXPEDIENTES LOOP
        UBICACION:=reg_ambito.ORI_ORIENT_UBIC_COD;
        HORAS:=reg_ambito.HORAS_ASIGNADAS;
        
        UPDATE ORI_ORI_UBIC
        SET ORI_ORIENT_HORASADJ=HORAS
        WHERE ORI_ORIENT_UBIC_COD=UBICACION;
    
        COMMIT;    
        
    END LOOP;
    

    
    Dbms_Output.Put_Line('FIN DEL PROCESO' ||TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));    
    
    mensaje := 'OK';
    
    exception
      when others then
        mensaje := 'ERROR';  
END consolida_horas; 
/