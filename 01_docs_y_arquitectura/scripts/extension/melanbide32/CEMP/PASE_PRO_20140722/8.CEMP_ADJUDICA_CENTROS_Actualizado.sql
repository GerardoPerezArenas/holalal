create or replace PROCEDURE        CEMP_adjudica_centros (ano in number, mensaje out varchar2, exp_estado_incorrecto out varchar2) iS

AMBITO NUMBER;
NUM_CENTROS NUMBER;
NUM_CENTROSesp NUMBER;
CENTRO number;
CENTRO2 number;
CENTRO3 NUMBER;    
SEGUIR NUMBER;
AMB_DISTR number;
orden number;
 


lines_out number(8);

-----------------------------------------------------------------------------
--              Declaraci?n de variables y Cursores                        --
-----------------------------------------------------------------------------

    CURSOR AMBITOS IS
        SELECT ORI_AMB_COD,ORI_AMB_CE,ORI_AMB_CE_ESPECIAL,ORI_AMB_DISTR FROM ORI_AMBITOS_CE WHERE ORI_AMB_ANOCONV=ano; 
     
    CURSOR CENTROS1 (AMB IN NUMBER) IS 
         select   A.ORI_CE_UBIC_COD ubicacion
        -- ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL,ORI_NUM_PERS_ATENDIDAS
            FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            lefT join ORI_TMP_ADJUDICACION_CENTROS E ON E.ORI_CE_UBIC_COD=A.ORI_CE_UBIC_COD
            left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3               AND 
            ORI_CE_ESPECIAL='N'
            AND (ORI_ENT_ADMLOCAL_VAL ='S' or ORI_ENT_SUPRAMUN_VAL='S') AND A.ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
            AND E.ORI_CE_UBIC_COD IS NULL
            ORDER BY ORI_ENT_SUPRAMUN_VAL desc,d.tray desc , 
            ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC,
            ORI_NUM_PERS_ATENDIDAS DESC ;         
    
    CURSOR CENTROS2 (AMB IN NUMBER) IS
        select  A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL,ORI_NUM_PERS_ATENDIDAS
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
         lefT join ORI_TMP_ADJUDICACION_CENTROS E ON E.ORI_CE_UBIC_COD=A.ORI_CE_UBIC_COD
          left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3           AND 
            ORI_CE_ESPECIAL='N'
            AND (ORI_EXP_COD_VAL IS NOT NULL OR ORI_ENT_OTROS_VAL IS NOT NULL) AND ORI_ENT_ADMLOCAL_VAL='N' AND ORI_ENT_SUPRAMUN_VAL='N'  AND A.ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
            AND E.ORI_CE_UBIC_COD IS NULL
        ORDER BY 
        case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,d.tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          

    CURSOR CENTROS3 (AMB IN NUMBER) IS
        select  A.ORI_CE_UBIC_COD UBICACION
       -- ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL,ORI_NUM_PERS_ATENDIDAS
        from ori_ce_ubic a inner join ori_entidad b on a.ORI_ENT_COD=b.ORI_ENT_COD
        lefT join ORI_TMP_ADJUDICACION_CENTROS E ON E.ORI_CE_UBIC_COD=A.ORI_CE_UBIC_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
                   left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='N'
            AND ORI_EXP_COD_VAL IS NULL AND ORI_ENT_OTROS_VAL IS NULL AND ORI_ENT_ADMLOCAL_VAL='N'  AND ORI_ENT_SUPRAMUN_VAL='N' AND A.ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
            AND E.ORI_CE_UBIC_COD IS NULL
        order by case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,d.tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          

        
    CURSOR CENTROS1CE (AMB IN NUMBER) IS 
         select  A.ORI_CE_UBIC_COD ubicacion
         ---,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL,ORI_NUM_PERS_ATENDIDAS
            from ori_ce_ubic a inner join ori_entidad b on a.ORI_ENT_COD=b.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            lefT join ORI_TMP_ADJUDICACION_CENTROS E ON E.ORI_CE_UBIC_COD=A.ORI_CE_UBIC_COD
                   left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD            
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
            AND (ORI_ENT_ADMLOCAL_VAL ='S' or ORI_ENT_SUPRAMUN_VAL='S') AND A.ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
            AND E.ORI_CE_UBIC_COD IS NULL
            ORDER BY ORI_ENT_SUPRAMUN_VAL desc,d.tray desc, ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC, ORI_NUM_PERS_ATENDIDAS DESC;       
    
    CURSOR CENTROS2CE (AMB IN NUMBER) IS
        select  A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL,ORI_NUM_PERS_ATENDIDAS
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
         lefT join ORI_TMP_ADJUDICACION_CENTROS E ON E.ORI_CE_UBIC_COD=A.ORI_CE_UBIC_COD
                            left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
             AND (ORI_EXP_COD_VAL IS NOT NULL OR ORI_ENT_OTROS_VAL IS NOT NULL) AND ORI_ENT_ADMLOCAL_VAL='N' AND ORI_ENT_SUPRAMUN_VAL='N'  AND A.ORI_AMB_COD=AMB
             and b.ext_num like '%/CEMP/%'
             AND E.ORI_CE_UBIC_COD IS NULL
       order by case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,d.tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          


    CURSOR CENTROS3CE (AMB IN NUMBER) IS
        select  A.ORI_CE_UBIC_COD UBICACION
        --,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL,ORI_NUM_PERS_ATENDIDAS
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
         lefT join ORI_TMP_ADJUDICACION_CENTROS E ON E.ORI_CE_UBIC_COD=A.ORI_CE_UBIC_COD
                            left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
            AND ORI_EXP_COD_VAL IS NULL AND ORI_ENT_OTROS_VAL IS NULL AND ORI_ENT_ADMLOCAL_VAL='N'  AND ORI_ENT_SUPRAMUN_VAL='N' AND A.ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
            AND E.ORI_CE_UBIC_COD IS NULL
      order by  case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,d.tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          

--CURSORES DE AMBITOS DISTRIBUIDOS
    CURSOR CENTROS1_AD (AMB IN NUMBER) IS 
        SELECT  UBICACION FROM (select 
         --ORI_AMB_COD,
         ROW_NUMBER() OVER (PARTITION BY ORI_AMB_COD,B.ORI_ENT_COD ORDER BY ORI_AMB_COD,B.ORI_ENT_COD,A.ORI_CE_UBIC_COD) ORDEN,
         A.ORI_CE_UBIC_COD ubicacion
         ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL, ORI_ENT_PORTRAB_DISC_VAL, ORI_NUM_PERS_ATENDIDAS,
         D.TRAY
            FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
            left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3               AND 
            ORI_CE_ESPECIAL='N'
            AND (ORI_ENT_ADMLOCAL_VAL ='S' or ORI_ENT_SUPRAMUN_VAL='S') AND ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
             ) WHERE ORDEN=1
             ORDER BY ORI_ENT_SUPRAMUN_VAL desc,Tray desc , 
            ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC,
            ORI_NUM_PERS_ATENDIDAS DESC;  
            
            
    
    CURSOR CENTROS2_AD (AMB IN NUMBER) IS
        SELECT  UBICACION FROM (select 
        ROW_NUMBER() OVER (PARTITION BY ORI_AMB_COD,B.ORI_ENT_COD ORDER BY ORI_AMB_COD,B.ORI_ENT_COD,A.ORI_CE_UBIC_COD) ORDEN,
        A.ORI_CE_UBIC_COD UBICACION
        ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL, ORI_ENT_PORTRAB_DISC_VAL, ORI_NUM_PERS_ATENDIDAS
        ,D.TRAY
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
          left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3           AND 
            ORI_CE_ESPECIAL='N'
            AND (ORI_EXP_COD_VAL IS NOT NULL OR ORI_ENT_OTROS_VAL IS NOT NULL) AND ORI_ENT_ADMLOCAL_VAL='N' AND ORI_ENT_SUPRAMUN_VAL='N'  AND ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
        )WHERE ORDEN=1 
        ORDER BY 
        case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          

    CURSOR CENTROS3_AD (AMB IN NUMBER) IS
        SELECT  UBICACION FROM (select 
        ROW_NUMBER() OVER (PARTITION BY ORI_AMB_COD,B.ORI_ENT_COD ORDER BY ORI_AMB_COD,B.ORI_ENT_COD,A.ORI_CE_UBIC_COD) ORDEN,
        A.ORI_CE_UBIC_COD UBICACION
        ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL, ORI_ENT_PORTRAB_DISC_VAL, ORI_NUM_PERS_ATENDIDAS
        ,D.TRAY
        from ori_ce_ubic a inner join ori_entidad b on a.ORI_ENT_COD=b.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
                   left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='N'
            AND ORI_EXP_COD_VAL IS NULL AND ORI_ENT_OTROS_VAL IS NULL AND ORI_ENT_ADMLOCAL_VAL='N'  AND ORI_ENT_SUPRAMUN_VAL='N' AND ORI_AMB_COD=AMB
        and b.ext_num like '%/CEMP/%'
        )WHERE ORDEN=1
        order by case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          

        
    CURSOR CENTROS1CE_AD (AMB IN NUMBER) IS 
    SELECT  UBICACION FROM (select 
        ROW_NUMBER() OVER (PARTITION BY ORI_AMB_COD,B.ORI_ENT_COD ORDER BY ORI_AMB_COD,B.ORI_ENT_COD,A.ORI_CE_UBIC_COD) ORDEN,
         A.ORI_CE_UBIC_COD ubicacion
         ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL, ORI_ENT_PORTRAB_DISC_VAL, ORI_NUM_PERS_ATENDIDAS,
         D.TRAY
            from ori_ce_ubic a inner join ori_entidad b on a.ORI_ENT_COD=b.ORI_ENT_COD
            INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
                   left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD            
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
            AND (ORI_ENT_ADMLOCAL_VAL ='S' or ORI_ENT_SUPRAMUN_VAL='S') AND ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
        )WHERE ORDEN=1 
            ORDER BY ORI_ENT_SUPRAMUN_VAL desc,tray desc, ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC, ORI_NUM_PERS_ATENDIDAS DESC;       
    
    CURSOR CENTROS2CE_AD (AMB IN NUMBER) IS
    SELECT  UBICACION FROM (select 
        ROW_NUMBER() OVER (PARTITION BY ORI_AMB_COD,B.ORI_ENT_COD ORDER BY ORI_AMB_COD,B.ORI_ENT_COD,A.ORI_CE_UBIC_COD) ORDEN,
        A.ORI_CE_UBIC_COD UBICACION
        ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL, ORI_ENT_PORTRAB_DISC_VAL, ORI_NUM_PERS_ATENDIDAS
        ,D.TRAY
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
                            left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
             AND (ORI_EXP_COD_VAL IS NOT NULL OR ORI_ENT_OTROS_VAL IS NOT NULL) AND ORI_ENT_ADMLOCAL_VAL='N' AND ORI_ENT_SUPRAMUN_VAL='N'  AND ORI_AMB_COD=AMB
             and b.ext_num like '%/CEMP/%'
       )WHERE ORDEN=1
       order by case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          


    CURSOR CENTROS3CE_AD (AMB IN NUMBER) IS
        SELECT  UBICACION FROM (select 
        ROW_NUMBER() OVER (PARTITION BY ORI_AMB_COD,B.ORI_ENT_COD ORDER BY ORI_AMB_COD,B.ORI_ENT_COD,A.ORI_CE_UBIC_COD) ORDEN,
        A.ORI_CE_UBIC_COD UBICACION
        ,B.ORI_ENT_COD,B.ORI_ENT_ADMLOCAL_VAL, ORI_ENT_SUPRAMUN_VAL, ORI_EXP_COD_VAL, ORI_ENT_OTROS_VAL, ORI_ENT_ACOLOCACION_VAL, ORI_ENT_NUMTRAB_VAL, ORI_ENT_NUMTRAB_DISC_VAL, ORI_ENT_PORTRAB_DISC_VAL, ORI_NUM_PERS_ATENDIDAS,
        D.TRAY
        FROM ORI_CE_UBIC A INNER JOIN ORI_ENTIDAD B ON A.ORI_ENT_COD=B.ORI_ENT_COD
         INNER JOIN E_CRO C ON C.CRO_NUM=B.EXT_NUM
                            left join 
            (
            select ori_ent_cod, nvl(sum(ORI_CETRA_DURACION),0)tray from 
            ori_ce_trayectoria
            group by ori_ent_cod
            )d on d.ori_ent_cod=b.ORI_ENT_COD
            WHERE CRO_TRA=3            AND 
            ORI_CE_ESPECIAL='S'
            AND ORI_EXP_COD_VAL IS NULL AND ORI_ENT_OTROS_VAL IS NULL AND ORI_ENT_ADMLOCAL_VAL='N'  AND ORI_ENT_SUPRAMUN_VAL='N' AND ORI_AMB_COD=AMB
            and b.ext_num like '%/CEMP/%'
        )WHERE ORDEN=1
      order by  case when ORI_EXP_COD_VAL in (1,2) then 1 else ORI_EXP_COD_VAL end,
        ORI_ENT_ACOLOCACION_VAL desc, ORI_ENT_PORTRAB_DISC_VAL DESC ,tray desc, ORI_NUM_PERS_ATENDIDAS DESC;          

            
    
    
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
    
    DELETE FROM ORI_TMP_ADJUDICACION_CENTROS;
    
    FOR REG_AMBITO IN AMBITOS LOOP
        AMBITO:=REG_AMBITO.ORI_AMB_COD;
        NUM_CENTROS:=REG_AMBITO.ORI_AMB_CE;
        NUM_CENTROSesp:=REG_AMBITO.ORI_AMB_CE_ESPECIAL;
        AMB_DISTR:=REG_AMBITO.ORI_AMB_DISTR;
        
        orden:=1;
          
          Dbms_Output.Put_Line('AMBITO : '||AMBITO||' ---- NUM_CENTROS: '|| NUM_CENTROS);
          FOR REG_CENTROS1 IN CENTROS1_AD(ambito) LOOP
              CENTRO:=REG_CENTROS1.UBICACION;
              
                     
              
              Dbms_Output.Put_Line('    CENTRO: '||CENTRO);
              if num_centros <>0 then 
                  
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'ADJUDICADO',AMBITO,0);
                    commit;
                    NUM_CENTROS:=NUM_CENTROS-1;
                    Dbms_Output.Put_Line('        ADJUDICADO');
              else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;
              end if;
              
              
                         
          end loop;
          
          FOR REG_CENTROS2 IN CENTROS2_AD(ambito) LOOP
              CENTRO2:=REG_CENTROS2.ubicacion;
              
              if num_centros <>0 then 
                  INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'ADJUDICADO',AMBITO,0);
                  commit;
                  num_centros:=num_centros-1;
              else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;
              end if;
          end loop;
          
          FOR REG_CENTROS3 IN CENTROS3_AD(ambito) LOOP
              CENTRO3:=REG_CENTROS3.ubicacion;
              
              if num_centros <>0 then 
                  
                  INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'ADJUDICADO',AMBITO,0);
                  commit;
                  num_centros:=num_centros-1;
              else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;
              end if;
          END LOOP;
          
          FOR REG_CENTROS1CE IN CENTROS1CE_AD(AMBITO) LOOP
            CENTRO:=REG_CENTROS1ce.UBICACION;
            Dbms_Output.Put_Line('CENTRO: '||CENTRO);
            if NUM_CENTROSesp <>0 then 
                
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'ADJUDICADO',AMBITO,0);
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
                Dbms_Output.Put_Line('ADJUDICADO');
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;
            end if;
        end loop;
        
          FOR REG_CENTROS2CE IN CENTROS2ce_ad(AMBITO) LOOP
            CENTRO2:=REG_CENTROS2ce.ubicacion;
            
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'ADJUDICADO',AMBITO,0);
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;
            end if;
        end loop;
        
          FOR REG_CENTROS3CE IN CENTROS3ce_ad(AMBITO) LOOP
            CENTRO3:=REG_CENTROS3ce.ubicacion;
            
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'ADJUDICADO',AMBITO,0);
                
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;                
            END IF;
        end loop;
                
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE');
	
	        FOR REG_CENTROS1 IN CENTROS1(ambito) LOOP
              CENTRO:=REG_CENTROS1.UBICACION;
              
               Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.CENTROS1');      
              
              Dbms_Output.Put_Line('    CENTRO: '||CENTRO);
              if num_centros <>0 then 
                  
                    
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'ADJUDICADO',AMBITO,0);
                    commit;
                    NUM_CENTROS:=NUM_CENTROS-1;
                    Dbms_Output.Put_Line('        ADJUDICADO');
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;                      
              end if;
              
              
                         
          end loop;
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.2');          
          FOR REG_CENTROS2 IN CENTROS2(ambito) LOOP
              CENTRO2:=REG_CENTROS2.ubicacion;
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.CENTROS2');                   
              if num_centros <>0 then 
                  INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'ADJUDICADO',AMBITO,0);
                  
                  commit;
                  num_centros:=num_centros-1;
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;                    
              end if;
          end loop;
                
          FOR REG_CENTROS3 IN CENTROS3(ambito) LOOP
              CENTRO3:=REG_CENTROS3.ubicacion;
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.CENTROS3');                  
              if num_centros <>0 then 
                  INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'ADJUDICADO',AMBITO,0);
                  
                  commit;
                  num_centros:=num_centros-1;
              else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1; 
              end if;
          END LOOP;
        
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.2.1');                  
          FOR REG_CENTROS1CE IN CENTROS1CE(AMBITO) LOOP
            CENTRO:=REG_CENTROS1ce.UBICACION;
            Dbms_Output.Put_Line('CENTRO: '||CENTRO);
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'ADJUDICADO',AMBITO,0);
                
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
                Dbms_Output.Put_Line('ADJUDICADO');
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1; 
            end if;
        end loop;
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.2.2');                          
          FOR REG_CENTROS2CE IN CENTROS2ce(AMBITO) LOOP
            CENTRO2:=REG_CENTROS2ce.ubicacion;
            
            if NUM_CENTROSesp <>0 then 
                
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'ADJUDICADO',AMBITO,0);
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro2,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;   
            end if;
        end loop;
Dbms_Output.Put_Line('ENTRAMOS EN LA SEGUNDA FASE.2.3');                          
          FOR REG_CENTROS3CE IN CENTROS3ce(AMBITO) LOOP
            CENTRO3:=REG_CENTROS3ce.ubicacion;
            
            if NUM_CENTROSesp <>0 then 
                INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'ADJUDICADO',AMBITO,0);
                
                commit;
                NUM_CENTROSesp:=NUM_CENTROSesp-1;
            else
                    INSERT INTO ORI_TMP_ADJUDICACION_CENTROS (ORI_CE_UBIC_COD, ORI_CE_ADJUDICADO, ORI_AMB_COD, ORI_ORDEN) VALUES (centro3,'SUPLENTE No. '|| ORDEN,AMBITO,ORDEN);
                    commit;
                    ORDEN:=ORDEN+1;   
            END IF;
        end loop;
          end loop;
    MENSAJE := 'OK';

        
     Dbms_Output.Put_Line('FIN DEL PROCESO' ||TO_CHAR(SYSDATE,'YYYY-MM-DD HH24:MI:SS'));   

    
    exception
      when others then
        MENSAJE := 'ERROR';  
         Dbms_Output.Put_Line('ERROR');   
END CEMP_ADJUDICA_CENTROS;