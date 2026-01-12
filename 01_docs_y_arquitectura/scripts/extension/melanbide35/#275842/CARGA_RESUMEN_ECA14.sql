create or replace
PROCEDURE CARGA_RESUMEN_ECA14(
    expediente IN VARCHAR2,
    codigo OUT VARCHAR2)
IS
  --CODIGO VARCHAR2(100);
BEGIN
  dbms_output.put_line('INICIO FUNCION CARGA_RESUMEN_ECA14: '||TO_CHAR(SYSDATE,'yyyymmdd hh24:mi:ss'));
  CODIGO:='OK';
  DELETE ECA14_RES_PROSPECTORES WHERE ECA_NUMEXP=expediente;
  DELETE ECA14_RES_PREPARADORES WHERE ECA_NUMEXP=expediente;
  COMMIT;
  -------------------------------------------------------------------------------------------------------------------
  -- carga de ECA14_RES_PROSPECTORES
  -------------------------------------------------------------------------------------------------------------------
  --ojo, hay que completar el tratamiento de sustitutos
  INSERT
  INTO ECA14_RES_PROSPECTORES
    (
      ECA_RES_PROSPECTORES_COD,
      ECA_NUMEXP,
      eca_jus_prospectores_COD,
      NIF,
      NOMBRE,
      GASTOS_SALARIALES_SOLICITADOS,
      GASTOS_SALARIALES_CONCEDIDOS,
      GASTOS_SALARIALES_JUSTIFICADOS,
      VISITAS_CONCEDIDAS,
      IMPORTE_VISITAS_CONC,
      VISITAS_JUSTIFICADAS,
      IMPORTE_VISITAS,
      A_PAGAR,
      FEC_SYSDATE
    )
  SELECT ECA14_RES_PROSPECTORES_SQ.NEXTVAL,
    a.ECA_NUMEXP,
    B.eca_jus_prospectores_COD,
    COALESCE(c.ECA_PROS_NIF,b.ECA_PROS_NIF) nif,
    COALESCE(c.ECA_PROS_NOMBRE,b.ECA_PROS_NOMBRE) nombre,
    C.ECA_PROS_IMP_SS_ECA GASTOS_SALARIALES_SOLICITADOS,
    --Se modifica por nuevo campo ECA_PROS_IMPTE_CONCEDIDO #241430, #241423
    --c.ECA_PROS_IMP_SS_ECA gastos_salariales_concedidos,
    C.ECA_PROS_IMPTE_CONCEDIDO  GASTOS_SALARIALES_CONCEDIDOS,
    --fin #241430, #241423
    b.ECA_PROS_IMP_SS_ECA gastos_salariales_justificados,
    c.ECA_PROS_VISITAS visitas_concedidas,
    c.ECA_PROS_VISITAS*importe.imp_visita IMPORTE_VISITAS_CONC,
    d.visitas visitas_justificadas,
    d.visitas*importe.imp_visita IMPORTE_visitas_just,
      CASE
      WHEN
        CASE
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND c.ECA_PROS_IMP_SS_ECA             <= b.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_VISITAS*importe.imp_visita
          AND c.ECA_PROS_IMP_SS_ECA             <= d.visitas         *importe.imp_visita
          THEN c.ECA_PROS_IMP_SS_ECA
		 WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND b.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_IMP_SS_ECA
          AND b.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_VISITAS*importe.imp_visita
          AND b.ECA_PROS_IMP_SS_ECA             <= d.visitas         *importe.imp_visita
          THEN b.ECA_PROS_IMP_SS_ECA
		 WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND c.ECA_PROS_VISITAS*importe.imp_visita             <= c.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS*importe.imp_visita             <= b.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS*importe.imp_visita             <= d.visitas         *importe.imp_visita
          THEN c.ECA_PROS_VISITAS*importe.imp_visita
		 WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND d.visitas*importe.imp_visita             <= c.ECA_PROS_IMP_SS_ECA
          AND d.visitas*importe.imp_visita             <= b.ECA_PROS_IMP_SS_ECA
          AND d.visitas*importe.imp_visita             <= c.ECA_PROS_VISITAS         *importe.imp_visita



          THEN d.visitas*importe.imp_visita
		 when COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas *importe.imp_visita     >= b.ECA_PROS_IMP_SS_ECA
          THEN b.ECA_PROS_IMP_SS_ECA
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas*importe.imp_visita      < b.ECA_PROS_IMP_SS_ECA
          THEN d.visitas*importe.imp_visita
          ELSE 0
        END > imp_visita*MAX_EMP_VISIT
      THEN imp_visita   *MAX_EMP_VISIT
      ELSE
          CASE
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND c.ECA_PROS_IMP_SS_ECA             <= b.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_VISITAS*importe.imp_visita
          AND c.ECA_PROS_IMP_SS_ECA             <= d.visitas         *importe.imp_visita
          THEN c.ECA_PROS_IMP_SS_ECA
		 WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND b.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_IMP_SS_ECA
          AND b.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_VISITAS*importe.imp_visita
          AND b.ECA_PROS_IMP_SS_ECA             <= d.visitas         *importe.imp_visita
          THEN b.ECA_PROS_IMP_SS_ECA
		 WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND c.ECA_PROS_VISITAS*importe.imp_visita             <= c.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS*importe.imp_visita             <= b.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS*importe.imp_visita             <= d.visitas         *importe.imp_visita
          THEN c.ECA_PROS_VISITAS*importe.imp_visita
		 WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND d.visitas*importe.imp_visita             <= c.ECA_PROS_IMP_SS_ECA
          AND d.visitas*importe.imp_visita             <= b.ECA_PROS_IMP_SS_ECA
          AND d.visitas*importe.imp_visita             <= c.ECA_PROS_VISITAS         *importe.imp_visita



          THEN d.visitas*importe.imp_visita
		 when COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas *importe.imp_visita     >= b.ECA_PROS_IMP_SS_ECA
          THEN b.ECA_PROS_IMP_SS_ECA
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas*importe.imp_visita      < b.ECA_PROS_IMP_SS_ECA
          THEN d.visitas*importe.imp_visita
          ELSE 0
        END
    END A_PAGAR,
    SYSDATE
  FROM eca14_solicitud a
  INNER JOIN eca14_jus_prospectores b
  ON a.eca_solicitud_COD=b.ECA_PROS_SOLICITUD
  INNER JOIN
    (SELECT imp_visita,
      MAX_EMP_VISIT
    FROM eca14_configuracion
    WHERE ano=SUBSTR(expediente,1,4)
    ) importe
  ON 1=1
  LEFT JOIN eca14_sol_prospectores c
  ON a.eca_solicitud_COD        =c.ECA_PROS_SOLICITUD
  AND b.eca_sol_prospectores_COD=c.eca_sol_prospectores_COD
  LEFT JOIN
    (SELECT eca_jus_prospectores_cod,
      COUNT(*) visitas
    FROM eca14_vis_prospectores
    WHERE ECA_VIS_CORRECTO ='S'
      --------------------------------------hay que filtrar por el expediente
    GROUP BY eca_jus_prospectores_cod
    )d
  ON d.eca_jus_prospectores_cod=b.eca_jus_prospectores_cod
  WHERE ECA_NUMEXP             =EXPEDIENTE;
  COMMIT;
  -------------------------------------------------------------------------------------------------------------------
  -- carga de ECA14_RES_PREPARADORES
  -------------------------------------------------------------------------------------------------------------------
  --ojo, hay que completar el tratamiento de sustitutos
  INSERT
  INTO ECA14_RES_PREPARADORES
    (
      ECA_RES_PREPARADORES_COD,
      ECA_NUMEXP,
      ECA_JUS_PREPARADORES_COD,
      NIF,
      NOMBRE,
      GASTOS_SALARIALES_SOLICITADOS,
      GASTOS_SALARIALES_CONCEDIDOS,
      GASTOS_SALARIALES_JUSTIFICADOS,
      IMPORTE_SEGUIMIENTOS,
      IMPORTE_SEGUIMIENTOS_LIM,
      IMPORTE_INS_CONCEDIDO,
      IMPORTE_INS_JUSTIFICADAS,
      IMPORTE_INSERCIONES,
      IMPORTE_SEG_INSERCIONES,
      A_PAGAR,
      FEC_SYSDATE
    )

SELECT ECA14_RES_PREPARADORES_SQ.NEXTVAL ECA14_RES_PREPARADORES_COD, ECA_NUMEXP,ECA_JUS_PREPARADORES_COD, NIF, NOMBRE, GASTOS_SALARIALES_SOLICITADOS, GASTOS_SALARIALES_CONCEDIDOS,GASTOS_SALARIALES_JUSTIFICADOS,
 IMPORTE_SEGUIMIENTOS, IMPORTE_SEGUIMIENTOS_LIM,IMPORTE_INS_CONCEDIDO,IMPORTE_INS_JUSTIFICADAS,IMPORTE_INSERCIONES,IMPORTE_SEG_INSERCIONES,--A_PAGAR,
 CASE WHEN gastos_salariales_concedidos <= A_PAGAR THEN gastos_salariales_concedidos ELSE A_PAGAR END AS A_PAGAR2,FEC_SYSDATE
 FROM (
	
	
	
  SELECT --ECA14_RES_PREPARADORES_SQ.NEXTVAL ECA14_RES_PREPARADORES_COD ,
    a.ECA_NUMEXP,
    b.ECA_JUS_PREPARADORES_COD,
    COALESCE(c.ECA_PREP_NIF,b.ECA_PREP_NIF) nif,
    COALESCE(c.ECA_PREP_NOMBRE,b.ECA_PREP_NOMBRE) nombre,
    --c.ECA_PREP_IMP_SS_ECA gastos_salariales_solicitados,
    --c.ECA_PREP_INS_SEG_IMPORTE gastos_salariales_concedidos,
   c.ECA_PREP_IMP_SS_ECA gastos_salariales_solicitados,
   --Se modifica por nuevo campo ECA_PROS_IMPTE_CONCEDIDO #241430, #241423
   -- c.ECA_PREP_COSTE gastos_salariales_concedidos,     
     c.ECA_PREP_IMPTE_CONCEDIDO gastos_salariales_concedidos,   
   -- fin  #241423
	b.ECA_PREP_IMP_SS_ECA gastos_salariales_justificados,
    IM_SEGUIMIENTO* NVL(d.seg,0) importe_seguimientos,
       case when 
    (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS <= IM_SEGUIMIENTO* NVL(d.seg,0)then (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS 




    else IM_SEGUIMIENTO* NVL(d.seg,0)
    end importe_seguimientos_LIM,
    NVL(eca_prep_ins_importe,0) importe_ins_concedido,
    NVL(E.IMPORTE_INSERCIONES,0) IMPORTE_INS_justificadas,
    CASE
      WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0) AND (B.ECA_PREP_TIPO_SUST is null or B.ECA_PREP_TIPO_SUST<>2)
      THEN NVL(eca_prep_ins_importe,0)
      ELSE NVL(E.IMPORTE_INSERCIONES,0)
    END IMPORTE_INSERCIONES,
    CASE
      WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)AND (B.ECA_PREP_TIPO_SUST is null or B.ECA_PREP_TIPO_SUST<>2)
      THEN NVL(eca_prep_ins_importe,0)
      ELSE NVL(E.IMPORTE_INSERCIONES,0)
    END
  + 
  case when 
    (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS <= IM_SEGUIMIENTO* NVL(d.seg,0)then (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS 
    else IM_SEGUIMIENTO* NVL(d.seg,0)
    end 
   IMPORTE_SEG_INSERCIONES,
-- SI IMPORTE_SEG_INSERCIONES ES MAYOR AL MINIMO (SS_CONCEDIDO, SS_JUSTIFICADO) LIMITAR AL MINIMO (CONC,JUS)
-- SI ES MENOR IMP_SEG_INSERCIONES.
 /* CASE WHEN ( CASE
      WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)AND B.ECA_PREP_TIPO_SUST<>2
      THEN NVL(eca_prep_ins_importe,0)
      ELSE NVL(E.IMPORTE_INSERCIONES,0)
    END
  + 
  case when 
    (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS <= IM_SEGUIMIENTO* NVL(d.seg,0)then (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS 







    else IM_SEGUIMIENTO* NVL(d.seg,0)
    end ) >= (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end) 






    THEN (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)


    ELSE 
  CASE
      WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)
      THEN NVL(eca_prep_ins_importe,0)
      ELSE NVL(E.IMPORTE_INSERCIONES,0)
    END
  + 
  case when 
    (CASE WHEN c.ECA_PREP_COSTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_COSTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS <= IM_SEGUIMIENTO* NVL(d.seg,0)then (CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA then c.ECA_PREP_INS_SEG_IMPORTE else B.ECA_PREP_IMP_SS_ECA end)*PO_MAX_SEGUIMIENTOS 
    else IM_SEGUIMIENTO* NVL(d.seg,0)
    end 
    END 
 A_pagar,*/
 -- SI IMPORTE_SEG_INSERCIONES ES MAYOR AL MINIMO (SS_CONCEDIDO, SS_JUSTIFICADO, SS_SOLICITUD) LIMITAR AL MINIMO (CONC,JUS, SOL)
-- SI ES MENOR IMP_SEG_INSERCIONES.
 CASE
    WHEN (
      CASE
        WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)
        THEN NVL(eca_prep_ins_importe,0)
        ELSE NVL(E.IMPORTE_INSERCIONES,0)
      END +
      CASE
        WHEN (
          CASE
            WHEN c.ECA_PREP_IMP_SS_ECA<= c.ECA_PREP_INS_SEG_IMPORTE
            AND c.ECA_PREP_IMP_SS_ECA <= B.ECA_PREP_IMP_SS_ECA
            THEN c.ECA_PREP_IMP_SS_ECA
            WHEN c.ECA_PREP_IMP_SS_ECA>B.ECA_PREP_IMP_SS_ECA
            AND B.ECA_PREP_IMP_SS_ECA<=c.ECA_PREP_INS_SEG_IMPORTE
            THEN B.ECA_PREP_IMP_SS_ECA
            ELSE C.ECA_PREP_INS_SEG_IMPORTE
          END) *PO_MAX_SEGUIMIENTOS<= IM_SEGUIMIENTO* NVL(d.seg,0)
        THEN (
          CASE
            WHEN c.ECA_PREP_IMP_SS_ECA<= c.ECA_PREP_INS_SEG_IMPORTE
            AND c.ECA_PREP_IMP_SS_ECA <= B.ECA_PREP_IMP_SS_ECA
            THEN c.ECA_PREP_IMP_SS_ECA
            WHEN c.ECA_PREP_IMP_SS_ECA>B.ECA_PREP_IMP_SS_ECA
            AND B.ECA_PREP_IMP_SS_ECA<=c.ECA_PREP_INS_SEG_IMPORTE
            THEN B.ECA_PREP_IMP_SS_ECA
            ELSE C.ECA_PREP_INS_SEG_IMPORTE
          END)             *PO_MAX_SEGUIMIENTOS
        ELSE IM_SEGUIMIENTO* NVL(d.seg,0)
      END ) >= (
      CASE
        WHEN c.ECA_PREP_IMP_SS_ECA<= c.ECA_PREP_INS_SEG_IMPORTE
        AND c.ECA_PREP_IMP_SS_ECA <= B.ECA_PREP_IMP_SS_ECA
        THEN c.ECA_PREP_IMP_SS_ECA
        WHEN c.ECA_PREP_IMP_SS_ECA>B.ECA_PREP_IMP_SS_ECA
        AND B.ECA_PREP_IMP_SS_ECA<=c.ECA_PREP_INS_SEG_IMPORTE
        THEN B.ECA_PREP_IMP_SS_ECA
        ELSE C.ECA_PREP_INS_SEG_IMPORTE
      END)
    THEN (
      CASE
        WHEN c.ECA_PREP_IMP_SS_ECA<= c.ECA_PREP_INS_SEG_IMPORTE
        AND c.ECA_PREP_IMP_SS_ECA <= B.ECA_PREP_IMP_SS_ECA
        THEN c.ECA_PREP_IMP_SS_ECA
        WHEN c.ECA_PREP_IMP_SS_ECA>B.ECA_PREP_IMP_SS_ECA
        AND B.ECA_PREP_IMP_SS_ECA<=c.ECA_PREP_INS_SEG_IMPORTE
        THEN B.ECA_PREP_IMP_SS_ECA
        ELSE C.ECA_PREP_INS_SEG_IMPORTE
      END)
    ELSE
      CASE
        WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)  and (c.ECA_PREP_TIPO_SUST IS NULL OR c.ECA_PREP_TIPO_SUST<>2 )
        THEN NVL(eca_prep_ins_importe,0)
        ELSE NVL(E.IMPORTE_INSERCIONES,0)
      END +
      CASE
        WHEN (
          CASE
            WHEN c.ECA_PREP_IMP_SS_ECA<= c.ECA_PREP_INS_SEG_IMPORTE
            AND c.ECA_PREP_IMP_SS_ECA <= B.ECA_PREP_IMP_SS_ECA
            THEN c.ECA_PREP_IMP_SS_ECA
            WHEN c.ECA_PREP_IMP_SS_ECA>B.ECA_PREP_IMP_SS_ECA
            AND B.ECA_PREP_IMP_SS_ECA<=c.ECA_PREP_INS_SEG_IMPORTE
            THEN B.ECA_PREP_IMP_SS_ECA
            ELSE C.ECA_PREP_INS_SEG_IMPORTE
          END ) *PO_MAX_SEGUIMIENTOS <= IM_SEGUIMIENTO* NVL(d.seg,0)
        THEN (
          CASE
            WHEN c.ECA_PREP_IMP_SS_ECA<= c.ECA_PREP_INS_SEG_IMPORTE
            AND c.ECA_PREP_IMP_SS_ECA <= B.ECA_PREP_IMP_SS_ECA
            THEN c.ECA_PREP_IMP_SS_ECA
            WHEN c.ECA_PREP_IMP_SS_ECA>B.ECA_PREP_IMP_SS_ECA
            AND B.ECA_PREP_IMP_SS_ECA<=c.ECA_PREP_INS_SEG_IMPORTE
            THEN B.ECA_PREP_IMP_SS_ECA
            ELSE C.ECA_PREP_INS_SEG_IMPORTE
          END )            *PO_MAX_SEGUIMIENTOS
        ELSE IM_SEGUIMIENTO* NVL(d.seg,0)
      END
  END A_PAGAR,
 
    sysdate FEC_SYSDATE
  FROM eca14_solicitud a
  INNER JOIN eca14_jus_PREPARADORES b
  ON a.eca_solicitud_COD=b.ECA_PREP_SOLICITUD
  INNER JOIN
    (SELECT IM_SEGUIMIENTO,
      IM_C1_H,
      IM_C1_M,
      IM_C2_H,
      IM_C2_M,
      IM_C3_H,
      IM_C3_M,
      IM_C4_H,
      IM_C4_M,
      PO_MAX_SEGUIMIENTOS
    FROM eca14_configuracion
    WHERE ano=SUBSTR(EXPEDIENTE,1,4)
    ) importe
  ON 1=1
  LEFT JOIN eca14_sol_prEPARADORES c
  ON a.eca_solicitud_COD        =c.ECA_PREP_SOLICITUD
  AND b.ECA_SOL_PREPARADORES_COD=c.ECA_SOL_PREPARADORES_COD
  LEFT JOIN
    (SELECT ECA_JUS_PREPARADORES_COD,
      COUNT(*) seg
    FROM eca14_seg_preparadores
    WHERE ECA_SEG_TIPO   =0
    AND ECA_SEG_CORRECTO ='S'
      --------------------------------------hay que filtrar por el EXPEDIENTE'
    GROUP BY ECA_JUS_PREPARADORES_COD
    )D
  ON d.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
  LEFT JOIN
    (SELECT ECA_JUS_PREPARADORES_COD,
      SUM(
      CASE
        WHEN eca_seg_sexo                                     =0
        AND eca_seg_tipodiscapacidad                          = 1
        AND (eca_seg_discgravedad                             = 2
        OR eca_seg_discgravedad                               =3)
        AND (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12
        OR eca_seg_fecfin                                    IS NULL)
        AND eca_seg_tipo                                      = 1
        THEN IM_C1_H*ECA_SEG_PORCJORNADA/100--> c1h,
        WHEN eca_seg_sexo                                     =1
        AND eca_seg_tipodiscapacidad                          = 1
        AND (eca_seg_discgravedad                             = 2
        OR eca_seg_discgravedad                               =3)
        AND (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12
        OR eca_seg_fecfin                                    IS NULL)
        AND eca_seg_tipo                                      = 1
        THEN IM_C1_M*ECA_SEG_PORCJORNADA/100--> c1m,
        WHEN eca_seg_sexo                                    =0
        AND eca_seg_tipodiscapacidad                         = 1
        AND (eca_seg_discgravedad                            = 2
        OR eca_seg_discgravedad                              = 3)
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini)  <12
        AND eca_seg_tipo                                     = 1
        THEN IM_C2_H*ECA_SEG_PORCJORNADA/100--> c2h,
        WHEN eca_seg_sexo                                    =1
        AND eca_seg_tipodiscapacidad                         = 1
        AND (eca_seg_discgravedad                            = 2
        OR eca_seg_discgravedad                              = 3)
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini)  <12
        AND eca_seg_tipo                                     = 1
        THEN IM_C2_M*ECA_SEG_PORCJORNADA/100--> c2m,
        WHEN eca_seg_sexo                                     =0
        AND ((eca_seg_tipodiscapacidad                        = 2
        AND eca_seg_discgravedad                              =3 )
        OR (eca_seg_tipodiscapacidad                          = 3
        AND (eca_seg_discgravedad                             =2
        OR eca_seg_discgravedad                               = 3) ))
        AND (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12
        OR eca_seg_fecfin                                    IS NULL)
        AND eca_seg_tipo                                      = 1
        THEN IM_C3_H*ECA_SEG_PORCJORNADA/100--> c3h,
        WHEN eca_seg_sexo                                     =1
        AND ((eca_seg_tipodiscapacidad                        = 2
        AND eca_seg_discgravedad                              =3 )
        OR (eca_seg_tipodiscapacidad                          = 3
        AND (eca_seg_discgravedad                             =2
        OR eca_seg_discgravedad                               = 3) ))
        AND (months_between(eca_seg_fecfin+1,eca_seg_fecini) >=12
        OR eca_seg_fecfin                                    IS NULL)
        AND eca_seg_tipo                                      = 1
        THEN IM_C3_M*ECA_SEG_PORCJORNADA/100--> c3m,
        WHEN eca_seg_sexo                                    =0
        AND ((eca_seg_tipodiscapacidad                       = 2
        AND eca_seg_discgravedad                             =3 )
        OR (eca_seg_tipodiscapacidad                         = 3
        AND (eca_seg_discgravedad                            =2
        OR eca_seg_discgravedad                              = 3) ))
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini)  <12
        AND eca_seg_tipo                                     = 1
        THEN IM_C4_H*ECA_SEG_PORCJORNADA/100--> c4h,
        WHEN eca_seg_sexo                                    =1
        AND ((eca_seg_tipodiscapacidad                       = 2
        AND eca_seg_discgravedad                             =3 )
        OR (eca_seg_tipodiscapacidad                         = 3
        AND (eca_seg_discgravedad                            =2
        OR eca_seg_discgravedad                              = 3) ))
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini) >=6
        AND months_between(eca_seg_fecfin+1,eca_seg_fecini)  <12
        AND eca_seg_tipo                                     = 1
        THEN IM_C4_M*ECA_SEG_PORCJORNADA/100--> c4m,
        ELSE 0
      END ) IMPORTE_INSERCIONES
    FROM eca14_seg_preparadores aa
    INNER JOIN
      (SELECT IM_SEGUIMIENTO,PO_MAX_SEGUIMIENTOS,
        IM_C1_H,
        IM_C1_M,
        IM_C2_H,
        IM_C2_M,
        IM_C3_H,
        IM_C3_M,
        IM_C4_H,
        IM_C4_M
      FROM eca14_configuracion
      WHERE ano=SUBSTR(EXPEDIENTE,1,4)
      ) importe
    ON 1                   =1
    WHERE ECA_SEG_CORRECTO ='S'
    GROUP BY ECA_JUS_PREPARADORES_COD
    )E ON E.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
  WHERE ECA_NUMEXP                  =EXPEDIENTE
  ) PRUEBA
  ;
  COMMIT;
  CODIGO:=CARGA_RESUMEN_ECA14_sust_PREP(expediente);
  CODIGO:=CARGA_RESUMEN_ECA14_sust_PROS(expediente);
  dbms_output.put_line('FIN PROCESO CARGA_RESUMEN_ECA14: '||TO_CHAR(SYSDATE,'yyyymmdd hh24:mi:ss'));
  dbms_output.put_line(CODIGO);
  --RETURN CODIGO;
EXCEPTION -- exception handlers begin
WHEN OTHERS THEN
  CODIGO := 'ERROR EN CONSULTA BBDD';
END CARGA_RESUMEN_ECA14;