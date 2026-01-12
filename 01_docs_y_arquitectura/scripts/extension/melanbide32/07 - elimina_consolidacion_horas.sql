create or replace 
PROCEDURE   elimina_consolidacion_HORAS (ano in number, mensaje out varchar2) iS


AMBITO NUMBER(8);
--Mensaje  varchar2(100);


----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------
  
    CURSOR AMBITOS IS
        select ORI_AMB_COD  FROM ORI_AMBITOS_HORAS where ORI_AMB_ANOCONV=ano;            
    
Begin
    DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||' ---- INICIO DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));
    FOR REG_AMBITO IN AMBITOS LOOP
        ambito:=reg_ambito.ORI_AMB_COD;
        
        UPDATE ORI_ORI_UBIC
        SET ori_orient_horasadj=null 
        WHERE ORI_AMB_COD=ambito and ORI_ORIENT_ANO=ano;
        
       
        Dbms_Output.Put_Line('ELIIMANADO EL VALOR DE HORAS ADJUDICADAS EN EL AMBITO: ' || ambito );
    END LOOP;
    
    DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||' ---- FIN DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));            
    
    mensaje := 'OK';
    
    exception
      when others then
        mensaje := 'ERROR';   
END elimina_consolidacion_HORAS;
/