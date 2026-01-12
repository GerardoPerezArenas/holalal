create or replace PROCEDURE CARGA_RESUMEN_ECA(
    expediente IN VARCHAR2,
    codigo OUT VARCHAR2)
IS
  --CODIGO VARCHAR2(100);
BEGIN
  dbms_output.put_line('INICIO FUNCION CARGA_RESUMEN_ECA: '||TO_CHAR(SYSDATE,'yyyymmdd hh24:mi:ss'));
  CODIGO:='OK';
  DELETE ECA_RES_PROSPECTORES WHERE ECA_NUMEXP=expediente;
  DELETE ECA_RES_PREPARADORES WHERE ECA_NUMEXP=expediente;
  COMMIT;
  -------------------------------------------------------------------------------------------------------------------
  -- carga de ECA_RES_PROSPECTORES
  -------------------------------------------------------------------------------------------------------------------
  --ojo, hay que completar el tratamiento de sustitutos
  INSERT
  INTO ECA_RES_PROSPECTORES
    (
      ECA_RES_PROSPECTORES_COD,
      ECA_NUMEXP,
      ECA_JUS_PROSPECTORES_COD,
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
  SELECT ECA_RES_PROSPECTORES_SQ.NEXTVAL,
    a.ECA_NUMEXP,
    B.ECA_JUS_PROSPECTORES_COD,
    COALESCE(c.ECA_PROS_NIF,b.ECA_PROS_NIF) nif,
    COALESCE(c.ECA_PROS_NOMBRE,b.ECA_PROS_NOMBRE) nombre,
    c.ECA_PROS_IMP_SS_ECA gastos_salariales_solicitados,
    c.ECA_PROS_IMP_SS_ECA gastos_salariales_concedidos,
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
          AND c.ECA_PROS_IMP_SS_ECA              < b.ECA_PROS_IMP_SS_ECA
          AND B.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_VISITAS*importe.imp_visita
          AND B.ECA_PROS_IMP_SS_ECA             <=d.visitas          *importe.imp_visita
          THEN B.ECA_PROS_IMP_SS_ECA
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)    <>0
          AND c.ECA_PROS_VISITAS *importe.imp_visita  < c.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS *importe.imp_visita  < B.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS *importe.imp_visita <= d.visitas*importe.imp_visita
          THEN c.ECA_PROS_VISITAS*importe.imp_visita
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND d.visitas         *importe.imp_visita       < c.ECA_PROS_IMP_SS_ECA
          AND d.visitas         *importe.imp_visita       < B.ECA_PROS_IMP_SS_ECA
          AND C.ECA_PROS_VISITAS*importe.imp_visita       > d.visitas*importe.imp_visita
          THEN d.visitas        *importe.imp_visita
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas *importe.imp_visita     >= b.ECA_PROS_IMP_SS_ECA
          THEN d.visitas*importe.imp_visita
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas*importe.imp_visita      > b.ECA_PROS_IMP_SS_ECA
          THEN b.ECA_PROS_IMP_SS_ECA
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
          AND c.ECA_PROS_IMP_SS_ECA              < b.ECA_PROS_IMP_SS_ECA
          AND B.ECA_PROS_IMP_SS_ECA             <= c.ECA_PROS_VISITAS*importe.imp_visita
          AND B.ECA_PROS_IMP_SS_ECA             <= d.visitas         *importe.imp_visita
          THEN B.ECA_PROS_IMP_SS_ECA
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)    <>0
          AND c.ECA_PROS_VISITAS *importe.imp_visita  < c.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS *importe.imp_visita  < B.ECA_PROS_IMP_SS_ECA
          AND c.ECA_PROS_VISITAS *importe.imp_visita <= d.visitas*importe.imp_visita
          THEN c.ECA_PROS_VISITAS*importe.imp_visita
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)<>0
          AND d.visitas         *importe.imp_visita       < c.ECA_PROS_IMP_SS_ECA
          AND d.visitas         *importe.imp_visita       < B.ECA_PROS_IMP_SS_ECA
          AND C.ECA_PROS_VISITAS*importe.imp_visita       > d.visitas*importe.imp_visita
          THEN d.visitas        *importe.imp_visita
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas *importe.imp_visita     >= b.ECA_PROS_IMP_SS_ECA
          THEN d.visitas*importe.imp_visita
          WHEN COALESCE(c.ECA_PROS_IMP_SS_ECA,0)=0
          AND d.visitas*importe.imp_visita      > b.ECA_PROS_IMP_SS_ECA
          THEN b.ECA_PROS_IMP_SS_ECA
          ELSE 0
        END
    END A_PAGAR,
    SYSDATE
  FROM eca_solicitud a
  INNER JOIN eca_jus_prospectores b
  ON a.ECA_SOLICITUD_COD=b.ECA_PROS_SOLICITUD
  INNER JOIN
    (SELECT imp_visita,
      MAX_EMP_VISIT
    FROM eca_configuracion
    WHERE ano=SUBSTR(expediente,1,4)
    ) importe
  ON 1=1
  LEFT JOIN eca_sol_prospectores c
  ON a.ECA_SOLICITUD_COD        =c.ECA_PROS_SOLICITUD
  AND b.ECA_SOL_PROSPECTORES_COD=c.ECA_SOL_PROSPECTORES_COD
  LEFT JOIN
    (SELECT eca_jus_prospectores_cod,
      COUNT(*) visitas
    FROM eca_vis_prospectores
    WHERE ECA_VIS_CORRECTO ='S'
      --------------------------------------hay que filtrar por el expediente
    GROUP BY eca_jus_prospectores_cod
    )d
  ON d.eca_jus_prospectores_cod=b.eca_jus_prospectores_cod
  WHERE ECA_NUMEXP             =EXPEDIENTE;
  COMMIT;
  -------------------------------------------------------------------------------------------------------------------
  -- carga de ECA_RES_PREPARADORES
  -------------------------------------------------------------------------------------------------------------------
  --ojo, hay que completar el tratamiento de sustitutos
  INSERT
  INTO ECA_RES_PREPARADORES
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
  SELECT ECA_RES_PREPARADORES_SQ.NEXTVAL ECA_RES_PREPARADORES_COD ,
    a.ECA_NUMEXP,
    b.ECA_JUS_PREPARADORES_COD,
    COALESCE(c.ECA_PREP_NIF,b.ECA_PREP_NIF) nif,
    COALESCE(c.ECA_PREP_NOMBRE,b.ECA_PREP_NOMBRE) nombre,
    c.ECA_PREP_IMP_SS_ECA gastos_salariales_solicitados,
    c.ECA_PREP_INS_SEG_IMPORTE gastos_salariales_concedidos,
    b.ECA_PREP_IMP_SS_ECA gastos_salariales_justificados,
    IM_SEGUIMIENTO* NVL(d.seg,0) importe_seguimientos,
    CASE
      WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA
        AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS
      THEN c.ECA_PREP_INS_SEG_IMPORTE
      WHEN B.ECA_PREP_IMP_SS_ECA< C.ECA_PREP_INS_SEG_IMPORTE
        AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS
      THEN B.ECA_PREP_IMP_SS_ECA
      ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS
    END importe_seguimientos_LIM,
    NVL(eca_prep_ins_importe,0) importe_ins_concedido,
    NVL(E.IMPORTE_INSERCIONES,0) IMPORTE_INS_justificadas,
    CASE
      WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)
      THEN NVL(eca_prep_ins_importe,0)
      ELSE NVL(E.IMPORTE_INSERCIONES,0)
    END IMPORTE_INSERCIONES,
    CASE
      WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA
        AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS
      THEN c.ECA_PREP_INS_SEG_IMPORTE
      WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE
        AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS
      THEN B.ECA_PREP_IMP_SS_ECA
      ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS
    END +
    CASE
      WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0)
      THEN NVL(eca_prep_ins_importe,0)
      ELSE NVL(E.IMPORTE_INSERCIONES,0)
    END IMPORTE_SEG_INSERCIONES,
 CASE
      WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA
        AND c.ECA_PREP_INS_SEG_IMPORTE <= (CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN c.ECA_PREP_INS_SEG_IMPORTE WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN B.ECA_PREP_IMP_SS_ECA ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS END + CASE WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0) THEN NVL(eca_prep_ins_importe,0) ELSE NVL(E.IMPORTE_INSERCIONES,0) END 	+ 	 CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN c.ECA_PREP_INS_SEG_IMPORTE WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN B.ECA_PREP_IMP_SS_ECA ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS END)
      THEN c.ECA_PREP_INS_SEG_IMPORTE
      WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE
        AND B.ECA_PREP_IMP_SS_ECA <= (CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN c.ECA_PREP_INS_SEG_IMPORTE WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN B.ECA_PREP_IMP_SS_ECA ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS END + CASE WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0) THEN NVL(eca_prep_ins_importe,0) ELSE NVL(E.IMPORTE_INSERCIONES,0) END 	+ 	 CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN c.ECA_PREP_INS_SEG_IMPORTE WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN B.ECA_PREP_IMP_SS_ECA ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS END)
      THEN B.ECA_PREP_IMP_SS_ECA
      ELSE (CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN c.ECA_PREP_INS_SEG_IMPORTE WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN B.ECA_PREP_IMP_SS_ECA ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS END + CASE WHEN NVL(eca_prep_ins_importe,0)<=NVL(E.IMPORTE_INSERCIONES,0) THEN NVL(eca_prep_ins_importe,0) ELSE NVL(E.IMPORTE_INSERCIONES,0) END 	+ 	 CASE WHEN c.ECA_PREP_INS_SEG_IMPORTE<= B.ECA_PREP_IMP_SS_ECA AND c.ECA_PREP_INS_SEG_IMPORTE <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN c.ECA_PREP_INS_SEG_IMPORTE WHEN B.ECA_PREP_IMP_SS_ECA< c.ECA_PREP_INS_SEG_IMPORTE AND B.ECA_PREP_IMP_SS_ECA <= IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS THEN B.ECA_PREP_IMP_SS_ECA ELSE IM_SEGUIMIENTO* NVL(d.seg,0)*PO_MAX_SEGUIMIENTOS END)
    END A_PAGAR,
    sysdate FEC_SYSDATE
  FROM eca_solicitud a
  INNER JOIN eca_jus_PREPARADORES b
  ON a.ECA_SOLICITUD_COD=b.ECA_PREP_SOLICITUD
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
    FROM eca_configuracion
    WHERE ano=SUBSTR(expediente,1,4)
    ) importe
  ON 1=1
  LEFT JOIN eca_sol_prEPARADORES c
  ON a.ECA_SOLICITUD_COD        =c.ECA_PREP_SOLICITUD
  AND b.ECA_SOL_PREPARADORES_COD=c.ECA_SOL_PREPARADORES_COD
  LEFT JOIN
    (SELECT ECA_JUS_PREPARADORES_COD,
      COUNT(*) seg
    FROM ECA_SEG_PREPARADORES
    WHERE ECA_SEG_TIPO   =0
    AND ECA_SEG_CORRECTO ='S'
      --------------------------------------hay que filtrar por el expediente
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
    FROM eca_seg_preparadores aa
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
      FROM eca_configuracion
      WHERE ano=SUBSTR(expediente,1,4)
      ) importe
    ON 1                   =1
    WHERE ECA_SEG_CORRECTO ='S'
    GROUP BY ECA_JUS_PREPARADORES_COD
    )E ON E.ECA_JUS_PREPARADORES_COD=b.ECA_JUS_PREPARADORES_COD
  WHERE ECA_NUMEXP                  =expediente;
  COMMIT;
  CODIGO:=CARGA_RESUMEN_ECA_sust_PREP(expediente);
  CODIGO:=CARGA_RESUMEN_ECA_sust_PROS(expediente);
  dbms_output.put_line('FIN PROCESO CARGA_RESUMEN_ECA: '||TO_CHAR(SYSDATE,'yyyymmdd hh24:mi:ss'));
  dbms_output.put_line(CODIGO);
  --RETURN CODIGO;
EXCEPTION -- exception handlers begin
WHEN OTHERS THEN
  CODIGO := 'ERROR EN CONSULTA BBDD';
END CARGA_RESUMEN_ECA;