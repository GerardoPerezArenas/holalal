create or replace 
PROCEDURE CARGA_RESUMEN_ECA (expediente in varchar2, codigo out varchar2) IS

  --CODIGO VARCHAR2(100);
 
BEGIN      
  dbms_output.put_line('INICIO FUNCION CARGA_RESUMEN_ECA: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss')); 
  CODIGO:='OK';
  
  delete ECA_RES_PROSPECTORES where ECA_NUMEXP=expediente;
  delete ECA_RES_PREPARADORES where ECA_NUMEXP=expediente;
  commit;
  
-------------------------------------------------------------------------------------------------------------------
-- carga de ECA_RES_PROSPECTORES
-------------------------------------------------------------------------------------------------------------------  
    
  --ojo, hay que completar el tratamiento de sustitutos
insert into ECA_RES_PROSPECTORES (ECA_RES_PROSPECTORES_COD,ECA_NUMEXP,ECA_JUS_PROSPECTORES_COD,NIF,NOMBRE,GASTOS_SALARIALES_SOLICITADOS,GASTOS_SALARIALES_CONCEDIDOS,
GASTOS_SALARIALES_JUSTIFICADOS,VISITAS_CONCEDIDAS,VISITAS_JUSTIFICADAS,IMPORTE_VISITAS,A_PAGAR,FEC_SYSDATE
)
select
  ECA_RES_PROSPECTORES_SQ.NEXTVAL, a.ECA_NUMEXP,
  B.ECA_JUS_PROSPECTORES_COD,
  coalesce(c.ECA_PROS_NIF,b.ECA_PROS_NIF) nif,
  coalesce(c.ECA_PROS_NOMBRE,b.ECA_PROS_NOMBRE) nombre,
  c.ECA_PROS_IMP_SS_ECA gastos_salariales_solicitados,
  c.ECA_PROS_IMP_SS_ECA gastos_salariales_concedidos,
  b.ECA_PROS_IMP_SS_ECA gastos_salariales_justificados,
  c.ECA_PROS_VISITAS visitas_concedidas,
  d.visitas visitas_justificadas,
   CASE 
    WHEN c.ECA_PROS_VISITAS*importe.imp_visita <= d.visitas*importe.imp_visita
       then c.ECA_PROS_VISITAS*importe.imp_visita               
    ELSE d.visitas*importe.imp_visita
  END IMPORTE_VISITAS,
      CASE 
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0 AND b.ECA_PROS_IMP_SS_ECA >= C.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita >= C.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita  >= C.ECA_PROS_IMP_SS_ECA 
    then c.ECA_PROS_IMP_SS_ECA
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0 AND b.ECA_PROS_IMP_SS_ECA <= C.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita >= b.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita  >= b.ECA_PROS_IMP_SS_ECA 
    then  b.ECA_PROS_IMP_SS_ECA   
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0 AND c.ECA_PROS_VISITAS*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita <= b.ECA_PROS_IMP_SS_ECA 
        AND c.ECA_PROS_VISITAS*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
    then c.ECA_PROS_VISITAS*importe.imp_visita               
    WHEN (b.ECA_PROS_TIPO_SUST is null or b.ECA_PROS_TIPO_SUST<>2) and d.visitas*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita <= b.ECA_PROS_IMP_SS_ECA 
        AND d.visitas*importe.imp_visita <= C.ECA_PROS_IMP_SS_ECA 
    then d.visitas*importe.imp_visita 
    when b.ECA_PROS_TIPO_SUST=2 and d.visitas*importe.imp_visita <= b.ECA_PROS_IMP_SS_ECA then d.visitas*importe.imp_visita
    when b.ECA_PROS_TIPO_SUST=2 and d.visitas*importe.imp_visita > b.ECA_PROS_IMP_SS_ECA then b.ECA_PROS_IMP_SS_ECA
  ELSE 0
  END A_PAGAR,SYSDATE
  
from eca_solicitud a
inner join eca_jus_prospectores b on a.ECA_SOLICITUD_COD=b.ECA_PROS_SOLICITUD 
inner join (select imp_visita from eca_configuracion 
where ano=substr(expediente,1,4)
) importe on 1=1
left join eca_sol_prospectores c on a.ECA_SOLICITUD_COD=c.ECA_PROS_SOLICITUD and b.ECA_SOL_PROSPECTORES_COD=c.ECA_SOL_PROSPECTORES_COD
left join 
(
  select eca_jus_prospectores_cod, count(*)  visitas
  from eca_vis_prospectores 
  where 
  ECA_VIS_CORRECTO ='S'
  group by eca_jus_prospectores_cod
)d on d.eca_jus_prospectores_cod=b.eca_jus_prospectores_cod
where ECA_NUMEXP=EXPEDIENTE;
commit;




-------------------------------------------------------------------------------------------------------------------
-- carga de ECA_RES_PREPARADORES
-------------------------------------------------------------------------------------------------------------------
--ojo, hay que completar el tratamiento de sustitutos

INSERT INTO ECA_RES_PREPARADORES (ECA_RES_PREPARADORES_COD,ECA_NUMEXP,ECA_JUS_PREPARADORES_COD,NIF,NOMBRE,GASTOS_SALARIALES_SOLICITADOS,GASTOS_SALARIALES_CONCEDIDOS,
GASTOS_SALARIALES_JUSTIFICADOS,IMPORTE_SEGUIMIENTOS,IMPORTE_INS_CONCEDIDO,IMPORTE_INS_JUSTIFICADAS,IMPORTE_INSERCIONES,
IMPORTE_SEG_INSERCIONES,A_PAGAR,FEC_SYSDATE)
SELECT 
  ECA_RES_PREPARADORES_SQ.NEXTVAL ECA_RES_PREPARADORES_COD , a.ECA_NUMEXP,
  b.ECA_JUS_PREPARADORES_COD,
  coalesce(c.ECA_PREP_NIF,b.ECA_PREP_NIF) nif,
  coalesce(c.ECA_PREP_NOMBRE,b.ECA_PREP_NOMBRE) nombre,
  c.ECA_PREP_IMP_SS_ECA gastos_salariales_solicitados,
  c.ECA_PREP_IMP_SS_ECA gastos_salariales_concedidos,
  b.ECA_PREP_IMP_SS_ECA gastos_salariales_justificados,
  IM_SEGUIMIENTO* nvl(d.seg,0) importe_seguimientos,
  nvl(eca_prep_ins_importe,0) importe_ins_concedido,
  nvl(E.IMPORTE_INSERCIONES,0) IMPORTE_INS_justificadas,
  case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
  else nvl(E.IMPORTE_INSERCIONES,0) end IMPORTE_INSERCIONES,
  IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
  else nvl(E.IMPORTE_INSERCIONES,0) end IMPORTE_SEG_INSERCIONES,
  CASE

    WHEN   COALESCE (c.ECA_PREP_IMP_SS_ECA,0) <> 0 AND COALESCE (c.ECA_PREP_IMP_SS_ECA,0) <=  b.ECA_PREP_IMP_SS_ECA AND 
        COALESCE (c.ECA_PREP_IMP_SS_ECA,0) <=  IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
        else nvl(E.IMPORTE_INSERCIONES,0) end THEN COALESCE (c.ECA_PREP_IMP_SS_ECA,0) 
    WHEN   
        b.ECA_PREP_IMP_SS_ECA <=  IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
        else nvl(E.IMPORTE_INSERCIONES,0) end THEN b.ECA_PREP_IMP_SS_ECA
    else   IM_SEGUIMIENTO* nvl(d.seg,0)+case when nvl(eca_prep_ins_importe,0)<=nvl(E.IMPORTE_INSERCIONES,0)then nvl(eca_prep_ins_importe,0)
  else nvl(E.IMPORTE_INSERCIONES,0) end 
        
  END A_PAGAR, sysdate FEC_SYSDATE 
  
  
FROM 
eca_solicitud a
inner join eca_jus_PREPARADORES b on a.ECA_SOLICITUD_COD=b.ECA_PREP_SOLICITUD 
inner join (select IM_SEGUIMIENTO,IM_C1_H,IM_C1_M,IM_C2_H,IM_C2_M,IM_C3_H,IM_C3_M,IM_C4_H,IM_C4_M from eca_configuracion 
where ano=substr(expediente,1,4)
) importe on 1=1
left join eca_sol_prEPARADORES c on a.ECA_SOLICITUD_COD=c.ECA_PREP_SOLICITUD and b.ECA_SOL_PREPARADORES_COD=c.ECA_SOL_PREPARADORES_COD
LEFT JOIN (
SELECT ECA_JUS_PREPARADORES_COD, COUNT(*) seg
FROM ECA_SEG_PREPARADORES 
WHERE ECA_SEG_TIPO=0 
and   ECA_SEG_CORRECTO ='S'
GROUP BY ECA_JUS_PREPARADORES_COD
)D on d.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
LEFT JOIN 
(
select ECA_JUS_PREPARADORES_COD, SUM(
  case 
    when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   IM_C1_H*ECA_SEG_PORCJORNADA/100--> c1h,
    when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad=3) and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1  then   IM_C1_M*ECA_SEG_PORCJORNADA/100--> c1m,
    when eca_seg_sexo=0 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C2_H*ECA_SEG_PORCJORNADA/100--> c2h, 
    when eca_seg_sexo=1 and eca_seg_tipodiscapacidad = 1 and (eca_seg_discgravedad = 2 or eca_seg_discgravedad = 3) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6 and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C2_M*ECA_SEG_PORCJORNADA/100--> c2m,
    when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 THEN    IM_C3_H*ECA_SEG_PORCJORNADA/100--> c3h,
    when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) ))  and (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12 or eca_seg_fecfin is null) and eca_seg_tipo = 1 then   IM_C3_M*ECA_SEG_PORCJORNADA/100--> c3m, 
    when eca_seg_sexo=0 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C4_H*ECA_SEG_PORCJORNADA/100--> c4h,
    when eca_seg_sexo=1 and   ((eca_seg_tipodiscapacidad = 2 and eca_seg_discgravedad =3 ) or (eca_seg_tipodiscapacidad = 3 and (eca_seg_discgravedad =2 or eca_seg_discgravedad = 3) )) and months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6  and months_between(eca_seg_fecfin+1,eca_seg_fecini)<12 and eca_seg_tipo = 1 then   IM_C4_M*ECA_SEG_PORCJORNADA/100--> c4m,
    ELSE 0 
  END ) IMPORTE_INSERCIONES
from eca_seg_preparadores aa inner join 
(select IM_SEGUIMIENTO,IM_C1_H,IM_C1_M,IM_C2_H,IM_C2_M,IM_C3_H,IM_C3_M,IM_C4_H,IM_C4_M from eca_configuracion 
where ano=substr(expediente,1,4)
) importe on 1=1
where 

ECA_SEG_CORRECTO ='S'

GROUP BY ECA_JUS_PREPARADORES_COD
)E ON E.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD

where ECA_NUMEXP=expediente;
COMMIT;

CODIGO:=CARGA_RESUMEN_ECA_sust_PREP(expediente);
CODIGO:=CARGA_RESUMEN_ECA_sust_PROS(expediente);





dbms_output.put_line('FIN PROCESO CARGA_RESUMEN_ECA: '||to_char(SYSDATE,'yyyymmdd hh24:mi:ss'));   
dbms_output.put_line(CODIGO);
    --RETURN CODIGO;
EXCEPTION  -- exception handlers begin

   WHEN OTHERS THEN  
      CODIGO := 'ERROR EN CONSULTA BBDD';

END CARGA_RESUMEN_ECA;