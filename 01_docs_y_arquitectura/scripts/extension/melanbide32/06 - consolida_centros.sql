create or replace 
PROCEDURE        consolida_CENTROS(mensaje out varchar2)  iS



w_mensaje  varchar2(100);
lines_out number(8);

-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------


        
Begin
    Dbms_Output.Put_Line('INICIO DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));


    UPDATE ORI_CE_UBIC SET ORI_CE_ADJUDICADA='S' WHERE ORI_CE_UBIC_COD IN (SELECT ORI_CE_UBIC_COD FROM ORI_TMP_ADJUDICACION_CENTROS);
      

    
    Dbms_Output.Put_Line('FIN DEL PROCESO' ||TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));    
    mensaje := 'OK';
    exception
      when others then
        mensaje := 'ERROR';  
END consolida_CENTROS; 
/