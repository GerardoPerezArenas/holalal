create or replace 
PROCEDURE   elimina_consolidacion_CENTROS (ano in number, mensaje out varchar2) iS


AMBITO NUMBER(8);
--Mensaje  varchar2(100);


----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------
  
    CURSOR AMBITOS IS
        SELECT ORI_AMB_COD  FROM ORI_AMBITOS_CE WHERE ORI_AMB_ANOCONV=ANO;     
          
Begin
    DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||' ---- INICIO DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));
    FOR REG_AMBITO IN AMBITOS LOOP
        ambito:=reg_ambito.ORI_AMB_COD;
        
        UPDATE ORI_CE_UBIC
        SET ORI_CE_ADJUDICADA=null 
        WHERE ORI_AMB_COD=ambito;
        
       
        Dbms_Output.Put_Line('ELIIMANADO EL VALOR DE CENTROS ADJUDICADOS EN EL AMBITO: ' || ambito );
    END LOOP;
            
    DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||' ---- FIN DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));     
    
    mensaje := 'OK';
    
    exception
      when others then
        mensaje := 'ERROR';   
END elimina_consolidacion_CENTROS;
/