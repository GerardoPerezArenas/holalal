/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecProcesoAdjudicacionExcelDatosVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class ColecProcesoAdjGestionExcelDAO {
    private static final Logger log = LogManager.getLogger(ColecProcesoAdjGestionExcelDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public ColecProcesoAdjudicacionExcelDatosVO getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion(int idConvocatoria,int idUbicacion, Connection con) throws SQLException, Exception {
        log.info(" getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion - Begin " + idUbicacion +" " + formatFechaLog.format(new Date()));
        ColecProcesoAdjudicacionExcelDatosVO resultado = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select distinct "
                    + "    cah.IDFKCONVOCATORIAACTIVA idConvocatoria, cuct.colec_ubic_ct_tipo idColectivo "
                    + "    ,cuct.COLEC_UBIC_CT_NUMEXP numeroExpediente,cuct.FK_ID_AMBITO_SOLICITADO idAmbito,cuct.COLEC_UBIC_CT_COD idUbicacion "
                    + "    ,ce.COD_ENTIDAD_PADRE idEntidadPadre, ce.CIF_ENTIDAD_PADRE entidadPadreCif, ce.nombre_ENTIDAD_PADRE entidadPadreNombre "
                    + "    ,cuct.COLEC_UBIC_CT_CODENTIDAD idEntidad,ce.COLEC_ENT_CIF entidadCif, ce.COLEC_ENT_NOMBRE entidadNombre "
                    + "    ,cuctv.PUNTUACIONTRAYECTORIAENTIDAD, cuctv.PUNTUACIONUBICACIONCT, cuctv.PUNTUACIONSEGUNDOSLOCALES, cuctv.PUNTUACIONPLANIGUALDAD, cuctv.PUNTUACIONCERTIFICADOCALIDAD, cuctv.PUNTUACIONESPACIOCOMPLEM, cuctv.PUNTUACIONCENTROESPEMPLEO, cuctv.PUNTUACIONEMPOPROMEMPINSERCION "
                    + "    ,nvl(cuctv.PUNTUACIONTRAYECTORIAENTIDAD,0)+nvl(cuctv.PUNTUACIONUBICACIONCT,0)+nvl(cuctv.PUNTUACIONSEGUNDOSLOCALES,0)+nvl(cuctv.PUNTUACIONPLANIGUALDAD,0)+nvl(cuctv.PUNTUACIONCERTIFICADOCALIDAD,0)+nvl(cuctv.PUNTUACIONESPACIOCOMPLEM,0)+nvl(cuctv.PUNTUACIONCENTROESPEMPLEO,0)+nvl(cuctv.PUNTUACIONEMPOPROMEMPINSERCION,0) valoracionTotalUbicacion "
                    + "    ,TO_CHAR(registro.RES_FEC,'dd/MM/yyyy HH24:MI:SSss') fechaRegistroStr "
                    + "    ,bloquesSolicitadosColecAmbito.bloques_horas_solicitados bloquesHorasSolicitados "
                    + "    ,bloquesSolicitadosColecAmbito.numero_ubicaciones numeroUbicaciones "
                    + "    ,(nvl(MAX(cuctv.PUNTUACIONTRAYECTORIAENTIDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONUBICACIONCT) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONSEGUNDOSLOCALES) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONPLANIGUALDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONCERTIFICADOCALIDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONESPACIOCOMPLEM) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONCENTROESPEMPLEO) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONEMPOPROMEMPINSERCION) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "    )valoracionTotalMaxEntidad "
                    + "    ,CASE WHEN CE.ACEPTNUMEROSUPEHORAS=1 THEN 'Si/Bai' ELSE 'No/Ez' end ACEPTNUMEROSUPEHORASStr "
                    + "    ,cad.id idBdAdjudicacion "
                    + "    ,cad.HORAS_ASIGNADAS horasAsignadas "
                    + "    ,ltrim(rtrim(cuct.colec_ubic_ct_direccion ||(' '||COLEC_UBIC_CT_PORTAL_DIR||' '||COLEC_UBIC_CT_PISO_DIR||' '||COLEC_UBIC_CT_LETRA_DIR))) direccion "
                    + "    ,pr.prv_nom territorioHistoricoDes "
                    + "    ,mu.mun_nom municipioDes "
                    + "    ,cuct.COLEC_UBIC_CT_CODPOSTAL codigoPostal "
                    + "    ,CASE WHEN tramiteresolucionprovisional.cro_num is not NULL THEN 'Si/Bai' ELSE 'No/Ez' end expEnTramResolProvisional "
                    + "    ,cad.UBICACION_ADJUDICADA ubicacionAdjudicada "
                    + "    from colec_ubicaciones_ct cuct "
                    + "    INNER JOIN VISTA_COLEC_ORI_CONVO_EXPTS vistaConvoExpts ON CUCT.COLEC_UBIC_CT_NUMEXP = vistaConvoExpts.exp_num AND vistaConvoExpts.IDBD_CONVOCATORIA=? "
                    + "    INNER JOIN COLEC_AMBITOS_bloques_HORAS cah ON cah.IDFKCONVOCATORIAACTIVA=?  "
                    + "        and cah.COLECTIVO=cuct.colec_ubic_ct_tipo and cah.id=cuct.FK_ID_AMBITO_SOLICITADO "
                    + "    LEFT JOIN COLEC_AMBITO_UBIC_IMPRE CAUI ON cah.IDFKCONVOCATORIAACTIVA=CAUI.idFkConvocatoria AND cah.COLECTIVO=caui.idFkColectivo and caui.idFkAmbito=cah.id "
                    + "    INNER JOIN COLEC_UBIC_CT_VALORACION cuctv on cuctv.NUMEROEXPEDIENTE=cuct.COLEC_UBIC_CT_NUMEXP and cuctv.IDFKUBICACION=cuct.COLEC_UBIC_CT_COD "
                    + "    INNER JOIN (SELECT CE.COLEC_NUMEXP,CE.COLEC_ENT_COD COD_ENTIDAD_PADRE,CE.COLEC_ENT_CIF CIF_ENTIDAD_PADRE,CE.COLEC_ENT_NOMBRE NOMBRE_ENTIDAD_PADRE "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_COD ELSE cel.colec_ent_cod END COLEC_ENT_COD "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_CIF ELSE cel.COLEC_ENT_CIF END COLEC_ENT_CIF "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_NOMBRE ELSE cel.COLEC_ENT_NOMBRE END COLEC_ENT_NOMBRE  "
                    + "                ,CE.ACEPTNUMEROSUPEHORAS "
                    + "                FROM  "
                    + "                COLEC_ENTIDAD CE "
                    + "                LEFT JOIN colec_entidad_agrup_list CEL ON cel.colec_ent_agrup_cod=ce.colec_ent_cod "
                    + "        ) ce on ce.COLEC_NUMEXP=cuct.COLEC_UBIC_CT_NUMEXP and  ce.colec_ent_cod=cuct.COLEC_UBIC_CT_CODENTIDAD "
                    + "    LEFT JOIN (SELECT COLEC_NUMEXP,CODIGOCOLECTIVO, AMBITOCOMARCA, SUM(nvl(NUMEROBLOQUESHORAS,0)) bloques_horas_solicitados, SUM(nvl(NUMEROUBICACIONES,0)) numero_ubicaciones "
                    + "        FROM COLEC_SOLICITUD group by COLEC_NUMEXP, CODIGOCOLECTIVO, AMBITOCOMARCA "
                    + "    ) bloquesSolicitadosColecAmbito on bloquesSolicitadosColecAmbito.CODIGOCOLECTIVO=cuct.colec_ubic_ct_tipo and bloquesSolicitadosColecAmbito.AMBITOCOMARCA=cuct.FK_ID_AMBITO_SOLICITADO  and bloquesSolicitadosColecAmbito.COLEC_NUMEXP=cuct.COLEC_UBIC_CT_NUMEXP   "
                    + "    left join (SELECT MIN(R.RES_FEC) RES_FEC,E.EXR_NUM FROM E_EXR E,R_RES R  "
                    + "               WHERE  E.EXR_PRO='COLEC' AND R.RES_TIP='E' AND E.EXR_TOP=0 AND E.EXR_EJE>=2019 "
                    + "                AND E.EXR_NRE=R.RES_NUM AND E.EXR_EJR=R.RES_EJE AND E.EXR_UOR=R.RES_UOR AND E.EXR_DEP=R.RES_DEP AND E.EXR_TIP=R.RES_TIP "
                    + "               GROUP BY E.EXR_NUM "
                    + "            ) registro on registro.EXR_NUM =cuct.COLEC_UBIC_CT_NUMEXP "
                    + "    LEFT JOIN colec_adjudicacion cad on cad.idFkConvocatoria=?  "
                    + "        and cad.numeroExpediente=cuct.COLEC_UBIC_CT_NUMEXP and cad.idFkEntidad=ce.COD_ENTIDAD_PADRE and cad.idFkColectivo=cuct.colec_ubic_ct_tipo and cad.idFkAmbito=cuct.FK_ID_AMBITO_SOLICITADO and cad.idFkUbicacion=cuct.COLEC_UBIC_CT_COD "
                    + "    left join flbgen.t_prv pr on pr.prv_cod=cuct.colec_ubic_ct_territorio "
                    + "    left join flbgen.t_mun mu on mu.mun_prv=cuct.colec_ubic_ct_territorio and mu.mun_cod=cuct.colec_ubic_ct_municipio "
                    + "    LEFT JOIN (select CRO_NUM "
                    + "                from e_tra "
                    + "                LEFT JOIN E_CRO ON CRO_PRO='COLEC' AND CRO_TRA=TRA_COD "
                    + "                where tra_pro='COLEC' AND TRA_COU=3  "
                    + "                AND CRO_FEF IS NULL) tramiteResolucionProvisional "
                    + "        on cuct.COLEC_UBIC_CT_NUMEXP=tramiteresolucionprovisional.cro_num "
                    + "    where "
                    + "        cuct.COLEC_UBIC_CT_COD=? "
                    + "    order by cuct.colec_ubic_ct_tipo asc,cuct.FK_ID_AMBITO_SOLICITADO asc,valoracionTotalMaxEntidad desc, valoracionTotalUbicacion desc, "
                    + "    cuctv.PUNTUACIONTRAYECTORIAENTIDAD desc nulls last, cuctv.PUNTUACIONUBICACIONCT desc nulls last "
                    + "    ,cuctv.PUNTUACIONSEGUNDOSLOCALES desc nulls last "
                    + "    ,cuctv.PUNTUACIONPLANIGUALDAD desc nulls last, cuctv.PUNTUACIONCERTIFICADOCALIDAD desc nulls last, cuctv.PUNTUACIONESPACIOCOMPLEM desc nulls last "
                    + "    ,cuctv.PUNTUACIONCENTROESPEMPLEO desc nulls last, cuctv.PUNTUACIONEMPOPROMEMPINSERCION desc nulls last         "
                    + "    ,fechaRegistroStr ASC ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int params=1;
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idUbicacion);
            log.info("params = " + idConvocatoria
                    + "," + idConvocatoria
                    + "," + idUbicacion
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = new ColecProcesoAdjudicacionExcelDatosVO(
                        rs.getInt("idConvocatoria"),
                        rs.getInt("idColectivo"),
                        rs.getString("numeroExpediente"),
                        rs.getInt("idAmbito"),
                        rs.getInt("idUbicacion"),
                        rs.getInt("idEntidadPadre"),
                        rs.getString("entidadPadreCif"),
                        rs.getString("entidadPadreNombre"),
                        rs.getInt("idEntidad"),
                        rs.getString("entidadCif"),
                        rs.getString("entidadNombre"),
                        rs.getDouble("puntuacionTrayectoriaEntidad"),
                        rs.getDouble("puntuacionUbicacionCT"),
                        rs.getDouble("puntuacionSegundosLocales"),
                        rs.getDouble("puntuacionPlanIgualdad"),
                        rs.getDouble("puntuacionCertificadoCalidad"),
                        rs.getDouble("puntuacionEspacioComplem"),
                        rs.getDouble("puntuacionCentroEspEmpleo"),
                        rs.getDouble("puntuacionEmpoPromEmpInsercion"),
                        rs.getDouble("valoracionTotalUbicacion"),
                        rs.getString("fechaRegistroStr"),
                        rs.getDouble("bloquesHorasSolicitados"),
                        rs.getDouble("numeroUbicaciones"),
                        rs.getDouble("valoracionTotalMaxEntidad"),
                        rs.getString("aceptNumeroSupeHorasStr"),
                        rs.getInt("idBdAdjudicacion"),
                        rs.getDouble("horasAsignadas"),
                        rs.getString("direccion"),
                        rs.getString("territorioHistoricoDes"),
                        rs.getString("municipioDes"),
                        rs.getString("codigoPostal"),
                        rs.getString("expEnTramResolProvisional"),
                        rs.getInt("ubicacionAdjudicada")
                    );
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion - End " + idUbicacion +" "+formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<ColecProcesoAdjudicacionExcelDatosVO> getColecProcesoAdjudicacionExcelDatosVOByIdColectivo(int idConvocatoria,int idColectivo, Connection con) throws SQLException, Exception {
        log.info(" getColecProcesoAdjudicacionExcelDatosVOByIdColectivo - Begin " + idColectivo +" " + formatFechaLog.format(new Date()));
        List<ColecProcesoAdjudicacionExcelDatosVO> resultado = new ArrayList<ColecProcesoAdjudicacionExcelDatosVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select distinct "
                    + "    cah.IDFKCONVOCATORIAACTIVA idConvocatoria, cuct.colec_ubic_ct_tipo idColectivo "
                    + "    ,cuct.COLEC_UBIC_CT_NUMEXP numeroExpediente,cuct.FK_ID_AMBITO_SOLICITADO idAmbito,cuct.COLEC_UBIC_CT_COD idUbicacion "
                    + "    ,ce.COD_ENTIDAD_PADRE idEntidadPadre, ce.CIF_ENTIDAD_PADRE entidadPadreCif, ce.nombre_ENTIDAD_PADRE entidadPadreNombre "
                    + "    ,cuct.COLEC_UBIC_CT_CODENTIDAD idEntidad,ce.COLEC_ENT_CIF entidadCif, ce.COLEC_ENT_NOMBRE entidadNombre "
                    + "    ,cuctv.PUNTUACIONTRAYECTORIAENTIDAD, cuctv.PUNTUACIONUBICACIONCT, cuctv.PUNTUACIONSEGUNDOSLOCALES, cuctv.PUNTUACIONPLANIGUALDAD, cuctv.PUNTUACIONCERTIFICADOCALIDAD, cuctv.PUNTUACIONESPACIOCOMPLEM, cuctv.PUNTUACIONCENTROESPEMPLEO, cuctv.PUNTUACIONEMPOPROMEMPINSERCION "
                    + "    ,nvl(cuctv.PUNTUACIONTRAYECTORIAENTIDAD,0)+nvl(cuctv.PUNTUACIONUBICACIONCT,0)+nvl(cuctv.PUNTUACIONSEGUNDOSLOCALES,0)+nvl(cuctv.PUNTUACIONPLANIGUALDAD,0)+nvl(cuctv.PUNTUACIONCERTIFICADOCALIDAD,0)+nvl(cuctv.PUNTUACIONESPACIOCOMPLEM,0)+nvl(cuctv.PUNTUACIONCENTROESPEMPLEO,0)+nvl(cuctv.PUNTUACIONEMPOPROMEMPINSERCION,0) valoracionTotalUbicacion "
                    + "    ,TO_CHAR(registro.RES_FEC,'dd/MM/yyyy HH24:MI:SSss') fechaRegistroStr "
                    + "    ,bloquesSolicitadosColecAmbito.bloques_horas_solicitados bloquesHorasSolicitados "
                    + "    ,bloquesSolicitadosColecAmbito.numero_ubicaciones numeroUbicaciones "
                    + "    ,(nvl(MAX(cuctv.PUNTUACIONTRAYECTORIAENTIDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONUBICACIONCT) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONSEGUNDOSLOCALES) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONPLANIGUALDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONCERTIFICADOCALIDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONESPACIOCOMPLEM) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONCENTROESPEMPLEO) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONEMPOPROMEMPINSERCION) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "    )valoracionTotalMaxEntidad "
                    + "    ,CASE WHEN CE.ACEPTNUMEROSUPEHORAS=1 THEN 'Si/Bai' ELSE 'No/Ez' end ACEPTNUMEROSUPEHORASStr "
                    + "    ,cad.id idBdAdjudicacion "
                    + "    ,cad.HORAS_ASIGNADAS horasAsignadas "
                    + "    ,ltrim(rtrim(cuct.colec_ubic_ct_direccion ||(' '||COLEC_UBIC_CT_PORTAL_DIR||' '||COLEC_UBIC_CT_PISO_DIR||' '||COLEC_UBIC_CT_LETRA_DIR))) direccion "
                    + "    ,pr.prv_nom territorioHistoricoDes "
                    + "    ,mu.mun_nom municipioDes "
                    + "    ,cuct.COLEC_UBIC_CT_CODPOSTAL codigoPostal "
                    + "    ,CASE WHEN tramiteresolucionprovisional.cro_num is not NULL THEN 'Si/Bai' ELSE 'No/Ez' end expEnTramResolProvisional "
                    + "    ,cad.UBICACION_ADJUDICADA ubicacionAdjudicada "
                    + "    from colec_ubicaciones_ct cuct "
                    + "    INNER JOIN VISTA_COLEC_ORI_CONVO_EXPTS vistaConvoExpts ON CUCT.COLEC_UBIC_CT_NUMEXP = vistaConvoExpts.exp_num AND vistaConvoExpts.IDBD_CONVOCATORIA=? "
                    + "    INNER JOIN COLEC_AMBITOS_bloques_HORAS cah ON cah.IDFKCONVOCATORIAACTIVA=?  "
                    + "        and cah.COLECTIVO=cuct.colec_ubic_ct_tipo and cah.id=cuct.FK_ID_AMBITO_SOLICITADO "
                    + "    LEFT JOIN COLEC_AMBITO_UBIC_IMPRE CAUI ON cah.IDFKCONVOCATORIAACTIVA=CAUI.idFkConvocatoria AND cah.COLECTIVO=caui.idFkColectivo and caui.idFkAmbito=cah.id "
                    + "    INNER JOIN COLEC_UBIC_CT_VALORACION cuctv on cuctv.NUMEROEXPEDIENTE=cuct.COLEC_UBIC_CT_NUMEXP and cuctv.IDFKUBICACION=cuct.COLEC_UBIC_CT_COD "
                    + "    INNER JOIN (SELECT CE.COLEC_NUMEXP,CE.COLEC_ENT_COD COD_ENTIDAD_PADRE,CE.COLEC_ENT_CIF CIF_ENTIDAD_PADRE,CE.COLEC_ENT_NOMBRE NOMBRE_ENTIDAD_PADRE "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_COD ELSE cel.colec_ent_cod END COLEC_ENT_COD "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_CIF ELSE cel.COLEC_ENT_CIF END COLEC_ENT_CIF "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_NOMBRE ELSE cel.COLEC_ENT_NOMBRE END COLEC_ENT_NOMBRE  "
                    + "                ,CE.ACEPTNUMEROSUPEHORAS "
                    + "                FROM  "
                    + "                COLEC_ENTIDAD CE "
                    + "                LEFT JOIN colec_entidad_agrup_list CEL ON cel.colec_ent_agrup_cod=ce.colec_ent_cod "
                    + "        ) ce on ce.COLEC_NUMEXP=cuct.COLEC_UBIC_CT_NUMEXP and  ce.colec_ent_cod=cuct.COLEC_UBIC_CT_CODENTIDAD "
                    + "    LEFT JOIN (SELECT COLEC_NUMEXP,CODIGOCOLECTIVO, AMBITOCOMARCA, SUM(nvl(NUMEROBLOQUESHORAS,0)) bloques_horas_solicitados, SUM(nvl(NUMEROUBICACIONES,0)) numero_ubicaciones "
                    + "        FROM COLEC_SOLICITUD group by COLEC_NUMEXP, CODIGOCOLECTIVO, AMBITOCOMARCA "
                    + "    ) bloquesSolicitadosColecAmbito on bloquesSolicitadosColecAmbito.CODIGOCOLECTIVO=cuct.colec_ubic_ct_tipo and bloquesSolicitadosColecAmbito.AMBITOCOMARCA=cuct.FK_ID_AMBITO_SOLICITADO  and bloquesSolicitadosColecAmbito.COLEC_NUMEXP=cuct.COLEC_UBIC_CT_NUMEXP   "
                    + "    left join (SELECT MIN(R.RES_FEC) RES_FEC,E.EXR_NUM FROM E_EXR E,R_RES R  "
                    + "               WHERE  E.EXR_PRO='COLEC' AND R.RES_TIP='E' AND E.EXR_TOP=0 AND E.EXR_EJE>=2019 "
                    + "                AND E.EXR_NRE=R.RES_NUM AND E.EXR_EJR=R.RES_EJE AND E.EXR_UOR=R.RES_UOR AND E.EXR_DEP=R.RES_DEP AND E.EXR_TIP=R.RES_TIP "
                    + "               GROUP BY E.EXR_NUM "
                    + "            ) registro on registro.EXR_NUM =cuct.COLEC_UBIC_CT_NUMEXP "
                    + "    LEFT JOIN colec_adjudicacion cad on cad.idFkConvocatoria=?  "
                    + "        and cad.numeroExpediente=cuct.COLEC_UBIC_CT_NUMEXP and cad.idFkEntidad=ce.COD_ENTIDAD_PADRE and cad.idFkColectivo=cuct.colec_ubic_ct_tipo and cad.idFkAmbito=cuct.FK_ID_AMBITO_SOLICITADO and cad.idFkUbicacion=cuct.COLEC_UBIC_CT_COD "
                    + "    left join flbgen.t_prv pr on pr.prv_cod=cuct.colec_ubic_ct_territorio "
                    + "    left join flbgen.t_mun mu on mu.mun_prv=cuct.colec_ubic_ct_territorio and mu.mun_cod=cuct.colec_ubic_ct_municipio "
                    + "    LEFT JOIN (select CRO_NUM "
                    + "                from e_tra "
                    + "                LEFT JOIN E_CRO ON CRO_PRO='COLEC' AND CRO_TRA=TRA_COD "
                    + "                where tra_pro='COLEC' AND TRA_COU=3  "
                    + "                AND CRO_FEF IS NULL) tramiteResolucionProvisional "
                    + "        on cuct.COLEC_UBIC_CT_NUMEXP=tramiteresolucionprovisional.cro_num "
                    + "    where "
                    + "        cuct.colec_ubic_ct_tipo=? "
                    + "    order by cuct.colec_ubic_ct_tipo asc,cuct.FK_ID_AMBITO_SOLICITADO asc,valoracionTotalMaxEntidad desc, valoracionTotalUbicacion desc, "
                    + "    cuctv.PUNTUACIONTRAYECTORIAENTIDAD desc nulls last, cuctv.PUNTUACIONUBICACIONCT desc nulls last "
                    + "    ,cuctv.PUNTUACIONSEGUNDOSLOCALES desc nulls last "
                    + "    ,cuctv.PUNTUACIONPLANIGUALDAD desc nulls last, cuctv.PUNTUACIONCERTIFICADOCALIDAD desc nulls last, cuctv.PUNTUACIONESPACIOCOMPLEM desc nulls last "
                    + "    ,cuctv.PUNTUACIONCENTROESPEMPLEO desc nulls last, cuctv.PUNTUACIONEMPOPROMEMPINSERCION desc nulls last         "
                    + "    ,fechaRegistroStr ASC ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int params=1;
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idColectivo);
            log.info("params = " + idConvocatoria
                    + "," + idConvocatoria
                    + "," + idColectivo
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new ColecProcesoAdjudicacionExcelDatosVO(
                        rs.getInt("idConvocatoria"),
                        rs.getInt("idColectivo"),
                        rs.getString("numeroExpediente"),
                        rs.getInt("idAmbito"),
                        rs.getInt("idUbicacion"),
                        rs.getInt("idEntidadPadre"),
                        rs.getString("entidadPadreCif"),
                        rs.getString("entidadPadreNombre"),
                        rs.getInt("idEntidad"),
                        rs.getString("entidadCif"),
                        rs.getString("entidadNombre"),
                        rs.getDouble("puntuacionTrayectoriaEntidad"),
                        rs.getDouble("puntuacionUbicacionCT"),
                        rs.getDouble("puntuacionSegundosLocales"),
                        rs.getDouble("puntuacionPlanIgualdad"),
                        rs.getDouble("puntuacionCertificadoCalidad"),
                        rs.getDouble("puntuacionEspacioComplem"),
                        rs.getDouble("puntuacionCentroEspEmpleo"),
                        rs.getDouble("puntuacionEmpoPromEmpInsercion"),
                        rs.getDouble("valoracionTotalUbicacion"),
                        rs.getString("fechaRegistroStr"),
                        rs.getDouble("bloquesHorasSolicitados"),
                        rs.getDouble("numeroUbicaciones"),
                        rs.getDouble("valoracionTotalMaxEntidad"),
                        rs.getString("aceptNumeroSupeHorasStr"),
                        rs.getInt("idBdAdjudicacion"),
                        rs.getDouble("horasAsignadas"),
                        rs.getString("direccion"),
                        rs.getString("territorioHistoricoDes"),
                        rs.getString("municipioDes"),
                        rs.getString("codigoPostal"),
                        rs.getString("expEnTramResolProvisional"),
                        rs.getInt("ubicacionAdjudicada")
                    )
                );
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getColecProcesoAdjudicacionExcelDatosVOByIdColectivo ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getColecProcesoAdjudicacionExcelDatosVOByIdColectivo ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getColecProcesoAdjudicacionExcelDatosVOByIdColectivo - End " + idColectivo +" "+formatFechaLog.format(new Date()));
        return resultado;
    }
    
    public List<ColecProcesoAdjudicacionExcelDatosVO> getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito(int idConvocatoria,int idColectivo, int idAmbito, Connection con) throws SQLException, Exception {
        log.info(" getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito - Begin " + idColectivo +" " + formatFechaLog.format(new Date()));
        List<ColecProcesoAdjudicacionExcelDatosVO> resultado = new ArrayList<ColecProcesoAdjudicacionExcelDatosVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "select distinct "
                    + "    cah.IDFKCONVOCATORIAACTIVA idConvocatoria, cuct.colec_ubic_ct_tipo idColectivo "
                    + "    ,cuct.COLEC_UBIC_CT_NUMEXP numeroExpediente,cuct.FK_ID_AMBITO_SOLICITADO idAmbito,cuct.COLEC_UBIC_CT_COD idUbicacion "
                    + "    ,ce.COD_ENTIDAD_PADRE idEntidadPadre, ce.CIF_ENTIDAD_PADRE entidadPadreCif, ce.nombre_ENTIDAD_PADRE entidadPadreNombre "
                    + "    ,cuct.COLEC_UBIC_CT_CODENTIDAD idEntidad,ce.COLEC_ENT_CIF entidadCif, ce.COLEC_ENT_NOMBRE entidadNombre "
                    + "    ,cuctv.PUNTUACIONTRAYECTORIAENTIDAD, cuctv.PUNTUACIONUBICACIONCT, cuctv.PUNTUACIONSEGUNDOSLOCALES, cuctv.PUNTUACIONPLANIGUALDAD, cuctv.PUNTUACIONCERTIFICADOCALIDAD, cuctv.PUNTUACIONESPACIOCOMPLEM, cuctv.PUNTUACIONCENTROESPEMPLEO, cuctv.PUNTUACIONEMPOPROMEMPINSERCION "
                    + "    ,nvl(cuctv.PUNTUACIONTRAYECTORIAENTIDAD,0)+nvl(cuctv.PUNTUACIONUBICACIONCT,0)+nvl(cuctv.PUNTUACIONSEGUNDOSLOCALES,0)+nvl(cuctv.PUNTUACIONPLANIGUALDAD,0)+nvl(cuctv.PUNTUACIONCERTIFICADOCALIDAD,0)+nvl(cuctv.PUNTUACIONESPACIOCOMPLEM,0)+nvl(cuctv.PUNTUACIONCENTROESPEMPLEO,0)+nvl(cuctv.PUNTUACIONEMPOPROMEMPINSERCION,0) valoracionTotalUbicacion "
                    + "    ,TO_CHAR(registro.RES_FEC,'dd/MM/yyyy HH24:MI:SSss') fechaRegistroStr "
                    + "    ,bloquesSolicitadosColecAmbito.bloques_horas_solicitados bloquesHorasSolicitados "
                    + "    ,bloquesSolicitadosColecAmbito.numero_ubicaciones numeroUbicaciones "
                    + "    ,(nvl(MAX(cuctv.PUNTUACIONTRAYECTORIAENTIDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONUBICACIONCT) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONSEGUNDOSLOCALES) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONPLANIGUALDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONCERTIFICADOCALIDAD) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONESPACIOCOMPLEM) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONCENTROESPEMPLEO) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "        +nvl(MAX(cuctv.PUNTUACIONEMPOPROMEMPINSERCION) over (partition  by cuct.COLEC_UBIC_CT_NUMEXP, cuct.colec_ubic_ct_tipo, cuct.FK_ID_AMBITO_SOLICITADO, ce.COD_ENTIDAD_PADRE),0) "
                    + "    )valoracionTotalMaxEntidad "
                    + "    ,CASE WHEN CE.ACEPTNUMEROSUPEHORAS=1 THEN 'Si/Bai' ELSE 'No/Ez' end ACEPTNUMEROSUPEHORASStr "
                    + "    ,cad.id idBdAdjudicacion "
                    + "    ,cad.HORAS_ASIGNADAS horasAsignadas "
                    + "    ,ltrim(rtrim(cuct.colec_ubic_ct_direccion ||(' '||COLEC_UBIC_CT_PORTAL_DIR||' '||COLEC_UBIC_CT_PISO_DIR||' '||COLEC_UBIC_CT_LETRA_DIR))) direccion "
                    + "    ,pr.prv_nom territorioHistoricoDes "
                    + "    ,mu.mun_nom municipioDes "
                    + "    ,cuct.COLEC_UBIC_CT_CODPOSTAL codigoPostal "
                    + "    ,CASE WHEN tramiteresolucionprovisional.cro_num is not NULL THEN 'Si/Bai' ELSE 'No/Ez' end expEnTramResolProvisional "
                    + "    ,cad.UBICACION_ADJUDICADA ubicacionAdjudicada "
                    + "    from colec_ubicaciones_ct cuct "
                    + "    INNER JOIN VISTA_COLEC_ORI_CONVO_EXPTS vistaConvoExpts ON CUCT.COLEC_UBIC_CT_NUMEXP = vistaConvoExpts.exp_num AND vistaConvoExpts.IDBD_CONVOCATORIA=? "
                    + "    INNER JOIN COLEC_AMBITOS_bloques_HORAS cah ON cah.IDFKCONVOCATORIAACTIVA=?  "
                    + "        and cah.COLECTIVO=cuct.colec_ubic_ct_tipo and cah.id=cuct.FK_ID_AMBITO_SOLICITADO "
                    + "    LEFT JOIN COLEC_AMBITO_UBIC_IMPRE CAUI ON cah.IDFKCONVOCATORIAACTIVA=CAUI.idFkConvocatoria AND cah.COLECTIVO=caui.idFkColectivo and caui.idFkAmbito=cah.id "
                    + "    INNER JOIN COLEC_UBIC_CT_VALORACION cuctv on cuctv.NUMEROEXPEDIENTE=cuct.COLEC_UBIC_CT_NUMEXP and cuctv.IDFKUBICACION=cuct.COLEC_UBIC_CT_COD "
                    + "    INNER JOIN (SELECT CE.COLEC_NUMEXP,CE.COLEC_ENT_COD COD_ENTIDAD_PADRE,CE.COLEC_ENT_CIF CIF_ENTIDAD_PADRE,CE.COLEC_ENT_NOMBRE NOMBRE_ENTIDAD_PADRE "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_COD ELSE cel.colec_ent_cod END COLEC_ENT_COD "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_CIF ELSE cel.COLEC_ENT_CIF END COLEC_ENT_CIF "
                    + "                ,CASE WHEN cel.colec_ent_cod IS NULL THEN CE.COLEC_ENT_NOMBRE ELSE cel.COLEC_ENT_NOMBRE END COLEC_ENT_NOMBRE  "
                    + "                ,CE.ACEPTNUMEROSUPEHORAS "
                    + "                FROM  "
                    + "                COLEC_ENTIDAD CE "
                    + "                LEFT JOIN colec_entidad_agrup_list CEL ON cel.colec_ent_agrup_cod=ce.colec_ent_cod "
                    + "        ) ce on ce.COLEC_NUMEXP=cuct.COLEC_UBIC_CT_NUMEXP and  ce.colec_ent_cod=cuct.COLEC_UBIC_CT_CODENTIDAD "
                    + "    LEFT JOIN (SELECT COLEC_NUMEXP,CODIGOCOLECTIVO, AMBITOCOMARCA, SUM(nvl(NUMEROBLOQUESHORAS,0)) bloques_horas_solicitados, SUM(nvl(NUMEROUBICACIONES,0)) numero_ubicaciones "
                    + "        FROM COLEC_SOLICITUD group by COLEC_NUMEXP, CODIGOCOLECTIVO, AMBITOCOMARCA "
                    + "    ) bloquesSolicitadosColecAmbito on bloquesSolicitadosColecAmbito.CODIGOCOLECTIVO=cuct.colec_ubic_ct_tipo and bloquesSolicitadosColecAmbito.AMBITOCOMARCA=cuct.FK_ID_AMBITO_SOLICITADO  and bloquesSolicitadosColecAmbito.COLEC_NUMEXP=cuct.COLEC_UBIC_CT_NUMEXP   "
                    + "    left join (SELECT MIN(R.RES_FEC) RES_FEC,E.EXR_NUM FROM E_EXR E,R_RES R  "
                    + "               WHERE  E.EXR_PRO='COLEC' AND R.RES_TIP='E' AND E.EXR_TOP=0 AND E.EXR_EJE>=2019 "
                    + "                AND E.EXR_NRE=R.RES_NUM AND E.EXR_EJR=R.RES_EJE AND E.EXR_UOR=R.RES_UOR AND E.EXR_DEP=R.RES_DEP AND E.EXR_TIP=R.RES_TIP "
                    + "               GROUP BY E.EXR_NUM "
                    + "            ) registro on registro.EXR_NUM =cuct.COLEC_UBIC_CT_NUMEXP "
                    + "    LEFT JOIN colec_adjudicacion cad on cad.idFkConvocatoria=?  "
                    + "        and cad.numeroExpediente=cuct.COLEC_UBIC_CT_NUMEXP and cad.idFkEntidad=ce.COD_ENTIDAD_PADRE and cad.idFkColectivo=cuct.colec_ubic_ct_tipo and cad.idFkAmbito=cuct.FK_ID_AMBITO_SOLICITADO and cad.idFkUbicacion=cuct.COLEC_UBIC_CT_COD "
                    + "    left join flbgen.t_prv pr on pr.prv_cod=cuct.colec_ubic_ct_territorio "
                    + "    left join flbgen.t_mun mu on mu.mun_prv=cuct.colec_ubic_ct_territorio and mu.mun_cod=cuct.colec_ubic_ct_municipio "
                    + "    LEFT JOIN (select CRO_NUM "
                    + "                from e_tra "
                    + "                LEFT JOIN E_CRO ON CRO_PRO='COLEC' AND CRO_TRA=TRA_COD "
                    + "                where tra_pro='COLEC' AND TRA_COU=3  "
                    + "                AND CRO_FEF IS NULL) tramiteResolucionProvisional "
                    + "        on cuct.COLEC_UBIC_CT_NUMEXP=tramiteresolucionprovisional.cro_num "
                    + "    where "
                    + "        cuct.colec_ubic_ct_tipo=? "
                    + "        and cuct.FK_ID_AMBITO_SOLICITADO=? "
                    + "    order by cuct.colec_ubic_ct_tipo asc,cuct.FK_ID_AMBITO_SOLICITADO asc,valoracionTotalMaxEntidad desc, valoracionTotalUbicacion desc, "
                    + "    cuctv.PUNTUACIONTRAYECTORIAENTIDAD desc nulls last, cuctv.PUNTUACIONUBICACIONCT desc nulls last "
                    + "    ,cuctv.PUNTUACIONSEGUNDOSLOCALES desc nulls last "
                    + "    ,cuctv.PUNTUACIONPLANIGUALDAD desc nulls last, cuctv.PUNTUACIONCERTIFICADOCALIDAD desc nulls last, cuctv.PUNTUACIONESPACIOCOMPLEM desc nulls last "
                    + "    ,cuctv.PUNTUACIONCENTROESPEMPLEO desc nulls last, cuctv.PUNTUACIONEMPOPROMEMPINSERCION desc nulls last         "
                    + "    ,fechaRegistroStr ASC ";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            int params=1;
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idConvocatoria);
            ps.setInt(params++, idColectivo);
            ps.setInt(params++, idAmbito);
            log.info("params = " + idConvocatoria
                    + "," + idConvocatoria
                    + "," + idColectivo
                    + "," + idAmbito
            );
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado.add(new ColecProcesoAdjudicacionExcelDatosVO(
                        rs.getInt("idConvocatoria"),
                        rs.getInt("idColectivo"),
                        rs.getString("numeroExpediente"),
                        rs.getInt("idAmbito"),
                        rs.getInt("idUbicacion"),
                        rs.getInt("idEntidadPadre"),
                        rs.getString("entidadPadreCif"),
                        rs.getString("entidadPadreNombre"),
                        rs.getInt("idEntidad"),
                        rs.getString("entidadCif"),
                        rs.getString("entidadNombre"),
                        rs.getDouble("puntuacionTrayectoriaEntidad"),
                        rs.getDouble("puntuacionUbicacionCT"),
                        rs.getDouble("puntuacionSegundosLocales"),
                        rs.getDouble("puntuacionPlanIgualdad"),
                        rs.getDouble("puntuacionCertificadoCalidad"),
                        rs.getDouble("puntuacionEspacioComplem"),
                        rs.getDouble("puntuacionCentroEspEmpleo"),
                        rs.getDouble("puntuacionEmpoPromEmpInsercion"),
                        rs.getDouble("valoracionTotalUbicacion"),
                        rs.getString("fechaRegistroStr"),
                        rs.getDouble("bloquesHorasSolicitados"),
                        rs.getDouble("numeroUbicaciones"),
                        rs.getDouble("valoracionTotalMaxEntidad"),
                        rs.getString("aceptNumeroSupeHorasStr"),
                        rs.getInt("idBdAdjudicacion"),
                        rs.getDouble("horasAsignadas"),
                        rs.getString("direccion"),
                        rs.getString("territorioHistoricoDes"),
                        rs.getString("municipioDes"),
                        rs.getString("codigoPostal"),
                        rs.getString("expEnTramResolProvisional"),
                        rs.getInt("ubicacionAdjudicada")
                    )
                );
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito ", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido Exception getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito ", e);
            throw e;
        } finally {
            log.debug("Procedemos a cerrar el resultset");
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info(" getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito - End " + idColectivo +" "+formatFechaLog.format(new Date()));
        return resultado;
    }
}
