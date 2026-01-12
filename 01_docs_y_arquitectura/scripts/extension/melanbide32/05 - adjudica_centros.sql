create or replace 
PROCEDURE        adjudica_centros (ano in number, mensaje out varchar2, exp_estado_incorrecto out varchar2) iS

AMBITO NUMBER;
NUM_CENTROS NUMBER;
NUM_CENTROSesp NUMBER;
CENTRO number;
CENTRO2 number;
CENTRO3 NUMBER;    
SEGUIR NUMBER;
 


lines_out number(8);

-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------

    CURSOR AMBITOS IS
        SELECT ORI_AMB_COD,ORI_AMB_CE,ORI_AMB_CE_ESPECIAL FROM ORI_AMBITOS_CE WHERE ORI_AMB_ANOCONV=ano; 
--centros de prioridad 1        
    CURSOR CENTROS1 (AMB IN NUMBER) IS 
         select A.ORI_CE_UBIC_COD ubicacion
         --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL, ORI_ENT_SUPRAMUN, ORI_EXP_COD, ORI_ENT_OTROS, ORI_ENT_ACOLOCACION, ORI_ENT_NUMTRAB, ORI_ENT_NUMTRAB_DISC
            FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            WHERE CRO_TRA=3               AND 
            ORI_CE_ESPECIAL='N'
            AND (ORI_ENT_ADMLOCAL ='S' or ORI_ENT_SUPRAMUN='S') AND ORI_AMB_COD=AMB
            ORDER BY ORI_ENT_SUPRAMUN desc,ORI_ENT_ACOLOCACION desc, ORI_ENT_NUMTRAB_DISC/case when ORI_ENT_NUMTRAB is null or ori_ent_numtrab =0 then 1 else ori_ent_numtrab end DESC, ORI_CE_UBIC_COD ;         
    
    CURSOR CENTROS2 (AMB IN NUMBER) IS
        select A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL, ORI_ENT_SUPRAMUN, ORI_EXP_COD, ORI_ENT_OTROS, ORI_ENT_ACOLOCACION, ORI_ENT_NUMTRAB, ORI_ENT_NUMTRAB_DISC
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            WHERE CRO_TRA=3           AND 
            ORI_CE_ESPECIAL='N'
            AND (ORI_EXP_COD IS NOT NULL OR ORI_ENT_OTROS IS NOT NULL) AND ORI_ENT_ADMLOCAL='N' AND ORI_ENT_SUPRAMUN='N'  AND ORI_AMB_COD=AMB
        ORDER BY ORI_ENT_ACOLOCACION desc, ORI_ENT_NUMTRAB_DISC/case when ORI_ENT_NUMTRAB is null or ori_ent_numtrab =0 then 1 else ori_ent_numtrab end DESC , ORI_CE_UBIC_COD;          

    CURSOR CENTROS3 (AMB IN NUMBER) IS
        select A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL, ORI_ENT_SUPRAMUN, ORI_EXP_COD, ORI_ENT_OTROS, ORI_ENT_ACOLOCACION, ORI_ENT_NUMTRAB, ORI_ENT_NUMTRAB_DISC
        from ori_ce_ubic a inner join ori_entidad b on a.ORI_ENT_COD=b.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='N'
            AND ORI_EXP_COD IS NULL AND ORI_ENT_OTROS IS NULL AND ORI_ENT_ADMLOCAL='N'  AND ORI_ENT_SUPRAMUN='N' AND ORI_AMB_COD=AMB
        ORDER BY ORI_ENT_ACOLOCACION desc, ORI_ENT_NUMTRAB_DISC/case when ORI_ENT_NUMTRAB is null or ori_ent_numtrab =0 then 1 else ori_ent_numtrab end DESC , ORI_CE_UBIC_COD;          
        
    CURSOR CENTROS1CE (AMB IN NUMBER) IS 
         select A.ORI_CE_UBIC_COD ubicacion
         --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL, ORI_ENT_SUPRAMUN, ORI_EXP_COD, ORI_ENT_OTROS, ORI_ENT_ACOLOCACION, ORI_ENT_NUMTRAB, ORI_ENT_NUMTRAB_DISC
            from ori_ce_ubic a inner join ori_entidad b on a.ORI_ENT_COD=b.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            WHERE CRO_TRA=3
            AND ORI_CE_ESPECIAL='S'
            AND (ORI_ENT_ADMLOCAL ='S' or ORI_ENT_SUPRAMUN='S') AND ORI_AMB_COD=AMB
            ORDER BY ORI_ENT_SUPRAMUN desc,ORI_ENT_ACOLOCACION desc, ORI_ENT_NUMTRAB_DISC/case when ORI_ENT_NUMTRAB is null or ori_ent_numtrab =0 then 1 else ori_ent_numtrab end DESC, ORI_CE_UBIC_COD ;         
    
    CURSOR CENTROS2CE (AMB IN NUMBER) IS
        select A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL, ORI_ENT_SUPRAMUN, ORI_EXP_COD, ORI_ENT_OTROS, ORI_ENT_ACOLOCACION, ORI_ENT_NUMTRAB, ORI_ENT_NUMTRAB_DISC
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
             AND (ORI_EXP_COD IS NOT NULL OR ORI_ENT_OTROS IS NOT NULL) AND ORI_ENT_ADMLOCAL='N' AND ORI_ENT_SUPRAMUN='N'  AND ORI_AMB_COD=AMB
        ORDER BY ORI_ENT_ACOLOCACION desc, ORI_ENT_NUMTRAB_DISC/case when ORI_ENT_NUMTRAB is null or ori_ent_numtrab =0 then 1 else ori_ent_numtrab end DESC , ORI_CE_UBIC_COD;          

    CURSOR CENTROS3CE (AMB IN NUMBER) IS
        select A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL, ORI_ENT_SUPRAMUN, ORI_EXP_COD, ORI_ENT_OTROS, ORI_ENT_ACOLOCACION, ORI_ENT_NUMTRAB, ORI_ENT_NUMTRAB_DISC
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
            AND ORI_EXP_COD IS NULL AND ORI_ENT_OTROS IS NULL AND ORI_ENT_ADMLOCAL='N'  AND ORI_ENT_SUPRAMUN='N' AND ORI_AMB_COD=AMB
        ORDER BY ORI_ENT_ACOLOCACION DESC, ORI_ENT_NUMTRAB_DISC/case when ORI_ENT_NUMTRAB is null or ori_ent_numtrab =0 then 1 else ori_ent_numtrab end DESC , ORI_CE_UBIC_COD;          
            
    
    
Begin
    DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||' ---- INICIO DEL PROCESO: '|| TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));
    
      SEGUIR:=0;
    
      
                 SELECT COUNT(EXT_NUM )INTO SEGUIR
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            inner join e_tra d on c.cro_tra=d.tra_cod and tra_pro=cro_pro
            WHERE CRO_FEF is null
            AND TRA_cou NOT IN (3,12,999);
            
    
    IF SEGUIR =0 THEN
    exp_estado_incorrecto:=0;
    ELSE
      MENSAJE := 'EXPEDIENTES EN ESTADOS NO PERMITIDOS';
      exp_estado_incorrecto:=1;
    END IF;
    
    
        DBMS_OUTPUT.PUT_LINE('ANO: '||ANO||'Se continua');
      DELETE FROM ORI_TMP_ADJUDICACION_CENTROS;
    FOR REG_AMBITO IN AMBITOS LOOP
        AMBITO:=REG_AMBITO.ORI_AMB_COD;
        NUM_CENTROS:=REG_AMBITO.ORI_AMB_CE;
        NUM_CENTROSesp:=REG_AMBITO.ORI_AMB_CE_ESPECIAL;
        
        Dbms_Output.Put_Line('AMBITO: '||AMBITO||' ---- NUM_CENTROS: '|| NUM_CENTROS);
        FOR REG_CENTROS1 IN CENTROS1(ambito) LOOP
            CENTRO:=REG_CENTROS1.UBICACION;
            Dbms_Output.Put_Line('CENTRO: '||CENTRO);
            if num_centros <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD) VALUES (centro,'S',AMBITO);
                commit;
                NUM_CENTROS:=NUM_CENTROS-1;
                Dbms_Output.Put_Line('ADJUDICADO');
            end if;
        end loop;
        
        FOR REG_CENTROS2 IN CENTROS2(ambito) LOOP
            CENTRO2:=REG_CENTROS2.ubicacion;
            
            if num_centros <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD) VALUES (centro2,'S',AMBITO);
                commit;
                num_centros:=num_centros-1;
            end if;
        end loop;
        
        FOR REG_CENTROS3 IN CENTROS3(ambito) LOOP
            CENTRO3:=REG_CENTROS3.ubicacion;
            
            if num_centros <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD) VALUES (centro3,'S',AMBITO);
                commit;
                num_centros:=num_centros-1;
            end if;
        END LOOP;
        
         FOR REG_CENTROS1CE IN CENTROS1CE(AMBITO) LOOP
            CENTRO:=REG_CENTROS1ce.UBICACION;
            Dbms_Output.Put_Line('CENTRO: '||CENTRO);
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD) VALUES (centro,'S',AMBITO);
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
                Dbms_Output.Put_Line('ADJUDICADO');
            end if;
        end loop;
        
        FOR REG_CENTROS2CE IN CENTROS2ce(AMBITO) LOOP
            CENTRO2:=REG_CENTROS2ce.ubicacion;
            
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD) VALUES (centro2,'S',AMBITO);
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
            end if;
        end loop;
        
        FOR REG_CENTROS3CE IN CENTROS3ce(AMBITO) LOOP
            CENTRO3:=REG_CENTROS3ce.ubicacion;
            
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD) VALUES (centro3,'S',AMBITO);
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
            END IF;
        end loop;
                
    end loop;
    MENSAJE := 'OK';

        
     Dbms_Output.Put_Line('FIN DEL PROCESO' ||TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));   

    
    exception
      when others then
        MENSAJE := 'ERROR';  
END ADJUDICA_CENTROS;
/