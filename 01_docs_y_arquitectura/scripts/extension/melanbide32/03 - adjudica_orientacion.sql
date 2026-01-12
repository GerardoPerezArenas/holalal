create or replace 
PROCEDURE        adjudica_orientacion (ano in number, mensaje out varchar2, exp_estado_incorrecto out number) iS

HORAS_AMBITO Number(8,2);
HORAS_AMBITO_INI Number(8,2);
ambito Number(8);
ENTIDAD number(4);
HORAS_SOLICITADAS Number(8,2);
HORAS_TOT_SOLICITADAS Number(8,2);
HORAS_X_PUNTO NUMBER (8,2);
VALORACION number(6,2);
VALORACION_TOT number(6,2);
HORAS_ASIGNADAS number(8,2);
PO_PUNTUACION NUMBER(8,2);
PO_HORAS NUMBER(8,2);
PO_CONSUMIDO NUMBER(8,2);
HORAS_CONSUMIDAS NUMBER(8,2);
HORAS_TEMP NUMBER(8,2);
FIN NUMBER;
CAMBIO_AMBITO NUMBER;
loops number;
actualizado number;
ULTIMA NUMBER;
seguir number;

lines_out number(8);

-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------
    CURSOR EXPEDIENTES(AMB IN number) IS
        SELECT ORI_ORIENT_UBIC_COD,ORI_ORIENT_HORASSOLICITADAS,ORI_ORIENT_PUNTUACION  
        FROM ORI_ORI_UBIC B 
        INNER JOIN ORI_ENTIDAD A ON A.ORI_ENT_COD=B.ORI_ENT_COD 
        INNER JOIN E_CRO C ON C.CRO_NUM=A.EXT_NUM
        WHERE CRO_TRA=3        AND 
        ORI_AMB_COD=AMB AND ORI_ORIENT_ANO=ANO
        and ORI_ORIENT_PUNTUACION<>0;
        
    CURSOR EXPEDIENTES2(AMB IN number) IS
        select A.ORI_ORIENT_UBIC_COD,ORI_ORIENT_HORASSOLICITADAS,ORI_ORIENT_PUNTUACION  FROM ORI_ORI_UBIC A INNER JOIN  ORI_TMP_ADJUDICACION B
         ON B.ORI_ORIENT_UBIC_COD=A.ORI_ORIENT_UBIC_COD 
        WHERE A.ORI_AMB_COD=AMB  AND IND_FINALIZA=0 and ORI_ORIENT_ANO=ano;        
 
    CURSOR AMBITOS IS
        select ORI_AMB_COD,ORI_AMB_HORASTOT  FROM ORI_AMBITOS_HORAS where ORI_AMB_ANOCONV=ano;            
    
Begin
    DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||' ---- INICIO DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));
    
    SEGUIR:=0;
    
        SELECT 
        COUNT(
        A.EXT_NUM )into seguir
        FROM ORI_ORI_UBIC B 
        INNER JOIN ORI_ENTIDAD A ON A.ORI_ENT_COD=B.ORI_ENT_COD 
        INNER JOIN E_CRO C ON C.CRO_NUM=A.EXT_NUM
        WHERE  CRO_FEF is null
        AND CRO_TRA NOT IN (3,12,999) --RESOLUCION PROVISIONAL, RESOLUCION DENEGATORIA, CIERRE EXPEDIENTE.
        AND ORI_ORIENT_ANO=ANO;
        
    
    IF SEGUIR =0 THEN
    exp_estado_incorrecto:=0;
    ELSE
      MENSAJE := 'EXPEDIENTES EN ESTADOS NO PERMITIDOS';
      exp_estado_incorrecto:=1;
    END IF;
    
    
        DELETE FROM ORI_TMP_ADJUDICACION;
        FOR REG_AMBITO IN AMBITOS LOOP
            ambito:=reg_ambito.ORI_AMB_COD;
            HORAS_AMBITO:=reg_ambito.ORI_AMB_HORASTOT;
            HORAS_AMBITO_INI:=Reg_ambito.ORI_AMB_HORASTOT;
            
            select nvl(SUM(ORI_ORIENT_PUNTUACION),0) INTO VALORACION_TOT FROM ORI_ORI_UBIC 
            WHERE ORI_AMB_COD=AMBITO AND ORI_ORIENT_ANO=ano;
            
            if VALORACION_TOT<>0 then 
            HORAS_X_PUNTO:=HORAS_AMBITO/VALORACION_TOT;  
            DBMS_OUTPUT.PUT_LINE('AMBITO: ' || AMBITO ||' ;NUMERO HORAS:'||HORAS_AMBITO||' ;HORAS_X_PUNTO:'||HORAS_X_PUNTO );
            ELSE 
            DBMS_OUTPUT.PUT_LINE('AMBITO: ' || AMBITO ||'; SIN UBICACIONES ' );
            end if;
    
            
            FOR REG_EXP IN EXPEDIENTES (AMBITO) LOOP
                ENTIDAD:=REG_EXP.ORI_ORIENT_UBIC_COD;
                HORAS_SOLICITADAS:=REG_EXP.ORI_ORIENT_HORASSOLICITADAS;
                VALORACION:=REG_EXP.ORI_ORIENT_PUNTUACION;
                
                HORAS_ASIGNADAS:=   HORAS_X_PUNTO* VALORACION;  
                PO_PUNTUACION:=VALORACION/ VALORACION_TOT; 
                PO_HORAS:=HORAS_SOLICITADAS/HORAS_AMBITO;
                
                IF PO_HORAS>PO_PUNTUACION THEN
                    PO_CONSUMIDO:=0;
                    HORAS_CONSUMIDAS:=0;
                    FIN:=0;
                ELSE 
                    PO_CONSUMIDO:=PO_HORAS;
                    HORAS_CONSUMIDAS:=HORAS_SOLICITADAS;
                    FIN:=1;            
                END IF;
                Dbms_Output.Put_Line('NUMERO HORAS EXPEDIENTE-ENTIDAD: ' || ENTIDAD||' --- HORAS SOL:'||HORAS_SOLICITADAS ||' ---VALORACION:'||VALORACION ||' ---HORAS_AMBITO'||HORAS_AMBITO || ' ---HORAS_ASIGN: '||HORAS_ASIGNADAS || ' ---PORC_VALOR: '||PO_PUNTUACION|| ' ---PORC_HORAS_SOL: '||PO_HORAS ||' ---PO_CONSUMIDO: '||PO_CONSUMIDO||' ---HORAS_CONSUMIDAS: '||HORAS_CONSUMIDAS||' ---fin: '||FIN );
               INSERT INTO ORI_TMP_ADJUDICACION(
                           ORI_ORIENT_UBIC_COD, 
                           VALORACION, 
                           HORAS_SOLICITADAS, 
                           HORAS_ASIGNADAS_TEMP, 
                           PORC_TEMP, 
                           PORC_SOLICITADO, 
                           PORC_CONSUMIDO, 
                           HORAS_ASIGNADAS, 
                           IND_FINALIZA,
                           FECHA_CREACION,COD_AMBITO)
               VALUES (ENTIDAD,VALORACION,HORAS_SOLICITADAS,HORAS_ASIGNADAS,PO_PUNTUACION,PO_HORAS, PO_CONSUMIDO,HORAS_CONSUMIDAS,FIN,SYSDATE,AMBITO);
                        
            END LOOP;
            
            SELECT COUNT(*) INTO CAMBIO_AMBITO FROM ORI_TMP_ADJUDICACION A INNER JOIN ORI_ORI_UBIC B ON A.ORI_ORIENT_UBIC_COD=B.ORI_ORIENT_UBIC_COD 
            WHERE ORI_AMB_COD=ambito AND IND_FINALIZA=0;
    loops:=0;
    actualizado:=0;
    ultima:=1;
            WHILE (CAMBIO_AMBITO <>0  or ultima=0)and loops <=40 LOOP
            
                loops:=loops+1;
                DBMS_OUTPUT.PUT_LINE('CAMBIO_AMBITO: ' || CAMBIO_AMBITO ||' ultima:'||ULTIMA||' loops:'||LOOPS);
                SELECT SUM(HORAS_ASIGNADAS) INTO HORAS_TEMP FROM ORI_TMP_ADJUDICACION WHERE IND_FINALIZA=1 and COD_AMBITO=ambito; 
                
                --Dbms_Output.Put_Line('HORAS_AMBITO_INI: ' || HORAS_AMBITO_INI);  
                Dbms_Output.Put_Line('HORAS_TEMP: ' || HORAS_TEMP);     
                IF HORAS_TEMP IS NULL OR HORAS_TEMP=0 THEN 
                  HORAS_TEMP:=0;
                Dbms_Output.Put_Line('SE ACTUALIZA HORAS_TEMP: ' || HORAS_TEMP);     
                END IF;
                HORAS_AMBITO:=HORAS_AMBITO_INI-HORAS_TEMP;
                DBMS_OUTPUT.PUT_LINE('HORAS_AMBITO: ' || HORAS_AMBITO );
                Dbms_Output.Put_Line('VALORACION_TOT: ' || VALORACION_TOT );
                select sum(valoracion) into VALORACION_TOT FROM ORI_TMP_ADJUDICACION WHERE  IND_FINALIZA=0 and COD_AMBITO=ambito;
            
                if VALORACION_TOT<>0 then 
                  HORAS_X_PUNTO:=HORAS_AMBITO/VALORACION_TOT;  
                  DBMS_OUTPUT.PUT_LINE('AMBITO: ' || AMBITO ||' ;NUMERO HORAS:'||HORAS_AMBITO||' ;HORAS_X_PUNTO:'||HORAS_X_PUNTO );
                ELSE 
                  HORAS_X_PUNTO:=0;
                  DBMS_OUTPUT.PUT_LINE('AMBITO: ' || AMBITO ||'; SIN UBICACIONES ' );
                end if;
                      
                            
             
                    
                  FOR REG_EXP2 IN EXPEDIENTES2 (AMBITO) LOOP
                    ENTIDAD:=REG_EXP2.ORI_ORIENT_UBIC_COD;
                    HORAS_SOLICITADAS:=REG_EXP2.ORI_ORIENT_HORASSOLICITADAS;
                    VALORACION:=REG_EXP2.ORI_ORIENT_PUNTUACION;
    
                    HORAS_ASIGNADAS:=   HORAS_X_PUNTO* VALORACION;  
                    PO_PUNTUACION:=VALORACION/ VALORACION_TOT; 
                    PO_HORAS:=HORAS_SOLICITADAS/HORAS_AMBITO;
                 
                    IF PO_HORAS>PO_PUNTUACION and ultima<>0 THEN
                        PO_CONSUMIDO:=0;
                        HORAS_CONSUMIDAS:=0;
                        FIN:=0;
                        --Dbms_Output.Put_Line('if: PO_HORAS>PO_PUNTUACION and ultima<>0'||PO_HORAS||' '|| PO_PUNTUACION ||' '||ultima);
                    ELSE 
                        if ultima<>0 then
                            PO_CONSUMIDO:=PO_HORAS;
                            HORAS_CONSUMIDAS:=HORAS_SOLICITADAS;
                            FIN:=1;  
                            actualizado:=actualizado+1;
                        else
                            PO_CONSUMIDO:=PO_PUNTUACION;
                            HORAS_CONSUMIDAS:=HORAS_ASIGNADAS;
                            FIN:=1;  
                            actualizado:=actualizado+1;
                        end if;
                        --Dbms_Output.Put_Line('else: PO_HORAS>PO_PUNTUACION and ultima<>0'||PO_HORAS||' '|| PO_PUNTUACION ||' '||ultima);          
                    END IF;
                    Dbms_Output.Put_Line('NUMERO HORAS EXPEDIENTE-ENTIDAD: ' || ENTIDAD||' --- HORAS SOL:'||HORAS_SOLICITADAS ||' ---VALORACION:'||VALORACION ||' ---HORAS_AMBITO'||HORAS_AMBITO || ' ---HORAS_ASIGN: '||HORAS_ASIGNADAS || ' ---PORC_VALOR: '||PO_PUNTUACION|| ' ---PORC_HORAS_SOL: '||PO_HORAS ||' ---PO_CONSUMIDO: '||PO_CONSUMIDO||' ---HORAS_CONSUMIDAS: '||HORAS_CONSUMIDAS||' ---fin: '||FIN );
                    UPDATE ORI_TMP_ADJUDICACION
                    SET VALORACION=VALORACION,
                    HORAS_SOLICITADAS=HORAS_SOLICITADAS, 
                    HORAS_ASIGNADAS_TEMP=HORAS_ASIGNADAS, 
                    PORC_TEMP=PO_PUNTUACION, 
                    PORC_SOLICITADO=PO_HORAS, 
                    PORC_CONSUMIDO=PO_CONSUMIDO, 
                    HORAS_ASIGNADAS=HORAS_CONSUMIDAS, 
                    IND_FINALIZA=FIN,
                    FECHA_CREACION=SYSDATE
                    WHERE ORI_ORIENT_UBIC_COD=ENTIDAD;
                        
    
    
                END LOOP;
                
                --Dbms_Output.Put_Line('CAMBIO_AMBITO_ant: ' || CAMBIO_AMBITO);
                --Dbms_Output.Put_Line('actualizado: ' || actualizado);
                if cambio_ambito=actualizado or actualizado=0 or ultima=0 then
                    if actualizado=0 then
                        cambio_ambito:=0; --para no entrar en bucle
                        ultima:=0;
                    else
                    ultima:=1;
                    end if;
                else 
                    SELECT COUNT(*) INTO CAMBIO_AMBITO FROM ORI_TMP_ADJUDICACION A INNER JOIN ORI_ORI_UBIC B ON A.ORI_ORIENT_UBIC_COD=B.ORI_ORIENT_UBIC_COD 
                    WHERE ORI_AMB_COD=ambito AND IND_FINALIZA=0 AND ORI_ORIENT_ANO=ano;
                    actualizado:=0;
                end if;
                
                --Dbms_Output.Put_Line('CAMBIO_AMBITO: ' || CAMBIO_AMBITO);
            --CAMBIO_AMBITO:=0;
            
            END LOOP;
        
            
        END LOOP;
                
        MENSAJE := 'OK';

    
     Dbms_Output.Put_Line('FIN DEL PROCESO' ||TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));   
    exception
      when others then
        MENSAJE := 'ERROR';   
END adjudica_orientacion;
/