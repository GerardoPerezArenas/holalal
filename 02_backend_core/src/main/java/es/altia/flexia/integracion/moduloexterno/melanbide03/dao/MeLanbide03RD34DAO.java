package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide03.manager.MeLanbide03Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MeLanbide03RD34DAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03RD34DAO.class);
    
    private static MeLanbide03RD34DAO instance = null;
    
    public static MeLanbide03RD34DAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized (MeLanbide03RD34DAO.class){
                if(instance == null){
                    instance = new MeLanbide03RD34DAO();
                }//if(instance == null)
            }//synchronized (MeLanbide03ReportDAO.class)
        }//if(instance == null) 
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    

    public ArrayList<CertRd34VO> getCertificadosRD34(Connection con) throws SQLException, Exception {
        if (log.isDebugEnabled()) {log.debug("getCertificadosRD34() : BEGIN");}

        Statement st = null;
        ResultSet rs = null;
        ArrayList<CertRd34VO> listaCertificados = new ArrayList();
        String tablaMaxCert = ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_MAX_NUM_CARGA, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        String vistaLanF = ConfigurationParameter.getParameter(ConstantesMeLanbide03.VW_REGEXLAN_CPS_FECHA_OBTENCION, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        String codTramEspExpedicion = ConfigurationParameter.getParameter(ConstantesMeLanbide03.COD_TRAMITE_ESPERA_EXPEDICION, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        String codTramValoracionCP = ConfigurationParameter.getParameter(ConstantesMeLanbide03.COD_TRAMITE_VALORACION_CP_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        try {
            String sql = "select NUM_EXPEDIENTE,\n" +
                    "       nvl(NIF_GEN_PER_NUM_DOC_INTERMEDIACION,nvl(NIF_GEN_PER_NUM_DOC_LANF,NIF_REGEXLAN)) NIF,\n" +
                    "       NOMBRE,\n" +
                    "       APELLIDO1,\n" +
                    "       APELLIDO2,\n" +
                    "       FEC_NACIMIENTO,\n" +
                    "       SEXO,\n" +
                    "       NACION,\n" +
                    "       CCAA,\n" +
                    "       PROVINCIA,\n" +
                    "       LOCALIDAD,\n" +
                    "       DOMICILIO,\n" +
                    "       CP,\n" +
                    "       TELEFONO_FIJO,\n" +
                    "       MOVIL,\n" +
                    "       MAIL,\n" +
                    "       PROVINCIA_REGISTRO,\n" +
                    "       ANIO_REGISTRO,\n" +
                    "       ORGANISMO,\n" +
                    "       PROVINCIA_OTORGAMIENTO,\n" +
                    "       FECHA_OTORGAMIENTO,\n" +
                    "       MODO_GESTION,\n" +
                    "       CODIGO_CERTIFICADO,\n" +
                    "       FECHA_REGISTRO,\n" +
                    "       ITINERARIO,\n" +
                    "       VIA_ACCESO,\n" +
                    "       FECHA_ACCESO,\n" +
                    "       FECHA_MODULO\n" +
                    "from (\n" +
                    "select   \n" +
                    "    et.ext_num NUM_EXPEDIENTE,\n" +
                    "    trim(t.hte_doc) NIF_REGEXLAN,\n" +
                    "    nvl(mp.gen_per_num_doc,'') NIF_GEN_PER_NUM_DOC_LANF,\n" +
                    "    gpf.gen_per_num_doc NIF_GEN_PER_NUM_DOC_INTERMEDIACION,\n" +
                    "    substr(t.hte_nom,1,50) NOMBRE,\n" +
                    "    substr(nvl(t.hte_pa1,'') ||  nvl('','') ||  nvl(t.hte_ap1,''),1,100) APELLIDO1,\n" +
                    "    substr(nvl(t.hte_pa2,'') ||  nvl('','') ||  nvl(t.hte_ap2,''),1,100) APELLIDO2,\n" +
                    "    to_char(fnac.valor,'yyyy-mm-dd') FEC_NACIMIENTO,\n" +
                    "    case when sexo.valor = '1' then 'H' when sexo.valor = '2' then 'M' when sexo.valor = '3' then 'O' else '' end SEXO,\n" +
                    "    nvl((select iso3 from pai_map where cod_naci=nac.valor),'XXX') NACION,\n" +
                    "    '16' CCAA,\n" +
                    "    case when lpad(t_dnn.dnn_prv,2,'0') in ('01','20','48') then lpad(t_dnn.dnn_prv,2,'0') else '01' end PROVINCIA,\n" +
                    "    case when lpad(t_dnn.dnn_prv,2,'0') in ('01','20','48') then mun_nom else 'VITORIA-GASTEIZ' end LOCALIDAD,\n" +
                    "    case when lpad(t_dnn.dnn_prv,2,'0') in ('01','20','48') then \n" +
                    "    substr(nvl(replace(trim (upper (\n" +
                    "\t\t\t   (case when t_tvi.tvi_des like '%SIN TIPO VIA%' then '' else t_tvi.tvi_des || ' '  end) || \n" +
                    "\t\t\t   t_via.via_nom || ' ' || \n" +
                    "\t\t\t   (case when t_dnn.dnn_dmc like '%NO CONSTA%' then '' else t_dnn.dnn_dmc || ' '  end) || \n" +
                    "\t\t\t   t_dnn.dnn_nud || ' ' || \n" +
                    "\t\t\t   t_dnn.dnn_led || ' ' || \n" +
                    "\t\t\t   t_dnn.dnn_nuh || \n" +
                    "\t\t\t   t_dnn.dnn_leh || \n" +
                    "\t\t\t   (case when t_dnn.dnn_blq is not null and t_dnn.dnn_blq <> ' ' then ' Bl. ' else '' end) || \n" +
                    "\t\t\t   t_dnn.dnn_blq || \n" +
                    "\t\t\t   (case when t_dnn.dnn_por is not null and t_dnn.dnn_por <> ' ' then ' Portal. ' else '' end) || \n" +
                    "\t\t\t   t_dnn.dnn_por || \n" +
                    "\t\t\t   (case when t_dnn.dnn_esc is not null and t_dnn.dnn_esc <> ' ' then ' Esc. ' else '' end) || \n" +
                    "\t\t\t   t_dnn.dnn_esc || ' ' || \n" +
                    "\t\t\t   t_dnn.dnn_plt || \n" +
                    "\t\t\t   (case when t_dnn.dnn_plt is not null and t_dnn.dnn_plt <> ' ' then 'º ' else '' end) || \n" +
                    "\t\t\t   t_dnn.dnn_pta)),'   ',' '),'SIN DEFINIR'),1,200) \n" +
                    "    else 'CALLE JOSE ATXOTEGI  1' end DOMICILIO,\n" +
                    "    case when lpad(t_dnn.dnn_prv,2,'0') in ('01','20','48') then nvl(lpad(dnn_cpo,5,'0'),'00000') else '01009' end CP,\n" +
                    "    case when substr(t.hte_tlf,1,1)='9' then substr(replace(t.hte_tlf,' ',''),1,9) else '' end TELEFONO_FIJO,\n" +
                    "    case when (substr(t.hte_tlf,1,1)='6' or substr(t.hte_tlf,1,1)='7') then substr(replace(t.hte_tlf,' ',''),1,9) else '' end MOVIL,\n" +
                    "    t.hte_dce MAIL,\n" +
                    "    case when lpad(t_dnn.dnn_prv,2,'0') in ('01','20','48') then lpad(t_dnn.dnn_prv,2,'0') else '01' end PROVINCIA_REGISTRO,\n" +
                    "    to_char(tfe_valor,'yyyy') ANIO_REGISTRO,\n" +
                    "    '16' ORGANISMO,\n" +
                    "    case when lpad(t_dnn.dnn_prv,2,'0') in ('01','20','48') then lpad(t_dnn.dnn_prv,2,'0') else '01' end PROVINCIA_OTORGAMIENTO,\n" +
                    "    to_char(tfe_valor,'yyyy-mm-dd') FECHA_OTORGAMIENTO,\n" +
                    "    'C' MODO_GESTION,\n" +
                    "    SC1.COD_CERTIFICADO CODIGO_CERTIFICADO,\n" +
                    "    --'' CONVOCATORIA,\n" +
                    "    --'' \"NUM-REGISTRO\",\n" +
                    "    to_char(tfe_valor,'yyyy-mm-dd') FECHA_REGISTRO,\n" +
                    "    '1' ITINERARIO,\n" +
                    "    nvl(mp.via_acceso_rd34,'') VIA_ACCESO,\n" +
                    "    nvl(to_char(mp.fecha_acceso_rd34,'yyyy-mm-dd'),'') FECHA_ACCESO,\n" +
                    "    --'' NOMBRE_EMPRESA,\n" +
                    "    --'' CODIGO_EF,\n" +
                    "    --'' CODIGO_AF,\n" +
                    "    --'' FECHA_INICIO,\n" +
                    "    --'' FECHA_FINAL,\n" +
                    "    --'' UC,\n" +
                    "    --'' MODALIDAD_IMPARTICION,\n" +
                    "    --'' MODULO,\n" +
                    "    nvl(to_char(mp.fecha_obtencion_mp,'yyyy-mm-dd'),'') FECHA_MODULO\n" +
                    "    --'A' \"TIPO-REGISTRO\"\n" +
                    "from e_ext et                        \n" +
                    "    inner join e_exp e on e.exp_mun=et.ext_mun and e.exp_eje=et.ext_eje and e.exp_pro=et.ext_pro and e.exp_num=et.ext_num and e.exp_pro='CEPAP' and et.ext_rol=1\n" +
                    "    left join t_hte t on et.ext_ter=t.hte_ter and et.ext_nvr=t.hte_nvr \n" +
                    "    left join  t_campos_fecha fnac on fnac.cod_campo='TFECNACIMIENTO' and fnac.cod_tercero=t.hte_ter\n" +
                    "    left join  t_campos_desplegable sexo on sexo.cod_campo='TSEXOTERCERO' and sexo.cod_tercero=t.hte_ter\n" +
                    "    left join  t_campos_desplegable nac on nac.cod_campo='TNACIONTERCERO' and nac.cod_tercero=t.hte_ter\n" +
                    "        inner join t_dnn on t_dnn.dnn_dom=et.ext_dot\n" +
                    "        left join t_tvi on t_tvi.tvi_cod=t_dnn.dnn_tvi\n" +
                    "        inner join t_prv on t_prv.prv_cod=t_dnn.dnn_prv\n" +
                    "        inner join t_mun on t_mun.mun_cod=t_dnn.dnn_mun and t_mun.mun_prv=t_dnn.dnn_prv\n" +
                    "        left join t_via on t_via.via_cod=t_dnn.dnn_via and t_via.via_mun=t_dnn.dnn_mun and t_via.via_prv=t_dnn.dnn_prv and t_via.via_pai=t_dnn.dnn_pai\n" +
                    "    inner join  E_TDE on (E_TDE.TDE_MUN=et.ext_mun and E_TDE.TDE_EJE=et.ext_eje and E_TDE.TDE_NUM=et.ext_num and E_TDE.TDE_COD='TIPOACREDITACION' and E_TDE.tde_valor='CP')              \n" +
                    "    left join E_TFE on (TFE_MUN=et.ext_mun and TFE_EJE=et.ext_eje and TFE_NUM=et.ext_num and TFE_COD = 'FECHAPRESENTACION')\n" +
                    "    inner join MELANBIDE03_CERTIFICADO SC1 on SC1.COD_ORGANIZACION=et.ext_mun and SC1.COD_PROCEDIMIENTO=et.ext_pro and SC1.NUM_EXPEDIENTE=et.ext_num \n" +
                    "    -- tramite ESPERA EXPEDICION CP 7=DESA; 5=PRE y PRO; \n" +
                    "    inner join E_CRO on et.ext_mun=CRO_MUN and et.ext_pro=CRO_PRO and et.ext_eje=CRO_EJE and CRO_PRO='CEPAP' and et.ext_num=CRO_NUM and CRO_OCU=(SELECT MAX(cro_ocu) FROM E_CRO C1 WHERE C1.cro_tra = " + codTramEspExpedicion + " and C1.CRO_NUM=et.ext_num) and CRO_TRA=" + codTramEspExpedicion + "\n" +
                    "    -- TRAMITE VALORACION CP APA 3=DESA; 2=PRE y PRO\n" +
                    "    inner join E_TDET on (TDET_MUN=et.ext_mun and TDET_PRO=et.ext_pro and TDET_EJE=et.ext_eje and TDET_NUM=et.ext_num and TDET_TRA=" + codTramValoracionCP + " and TDET_COD='VALORACION2' and TDET_VALOR='T')\n" +
                    "    --MODULO-PRACTICAS\n" +
                    "    left join (SELECT num_expdte_regexlan,fecha_obtencion_mp,via_acceso_rd34,fecha_acceso_rd34,gen_per_corr,gen_per_num_doc FROM " + vistaLanF + ") mp on mp.num_expdte_regexlan=et.ext_num\n" +
                    "    --gen_persona_fisica INTERMEDIACION\n" +
                    "    left join gen_persona_fisica gpf\n" +
                    "    --on gpf.gen_per_num_doc=t.hte_doc\n" +
                    "    on gpf.gen_per_corr=mp.gen_per_corr\n" +
                    "    --check campo CARGADORD34, sólo se recogen los que no han sido subidos al RD34\n" +
                    "    left join E_TDE ck on (ck.TDE_MUN=et.ext_mun and ck.TDE_EJE=et.ext_eje and ck.TDE_NUM=et.ext_num and ck.TDE_COD='CARGADORD34')\n" +
                    "--no anulados\n" +
                    "where e.exp_est<>1\n" +
                    "and e.exp_eje >= 2020\n" +
                    "and (ck.tde_valor is null or ck.tde_valor='N')\n" +
                    "and trim(t.hte_doc) is not null\n" +
                    "and substr(t.hte_nom,1,50) is not null\n" +
                    "and substr(nvl(t.hte_pa1,'') ||  nvl('','') ||  nvl(t.hte_ap1,''),1,100) is not null\n" +
                    "and to_char(fnac.valor,'yyyy-mm-dd') is not null\n" +
                    "and (case when sexo.valor = '1' then 'H' when sexo.valor = '2' then 'M' when sexo.valor = '3' then 'O' else '' end) is not null\n" +
                    "and to_char(tfe_valor,'yyyy') is not null\n" +
                    "and to_char(tfe_valor,'dd/mm/yyyy') is not null\n" +
                    "and SC1.COD_CERTIFICADO is not null\n" +
                    "--and trim(t.hte_doc) = '00000013J'\n" +
                    "order by e.exp_num\n" +
                    ") where ROWNUM <= (select max_num from " + tablaMaxCert + ") and (VIA_ACCESO is not null and FECHA_ACCESO is not null)";
            //if (log.isDebugEnabled()) {log.debug("sql = " + sql);}
            st = con.createStatement();
            rs = st.executeQuery(sql);
            log.info("Query recuperada asignamos a la lista los resultados");
            while (rs.next()) {
                CertRd34VO certificado = new CertRd34VO();
                certificado.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                certificado.setNif(rs.getString("NIF"));
                certificado.setNombre(rs.getString("NOMBRE"));
                certificado.setApellido1(rs.getString("APELLIDO1"));
                certificado.setApellido2(rs.getString("APELLIDO2"));
                certificado.setFec_nacimiento(rs.getString("FEC_NACIMIENTO"));
                certificado.setSexo(rs.getString("SEXO"));
                certificado.setNacion(rs.getString("NACION"));
                certificado.setCcaa(rs.getString("CCAA"));
                certificado.setProvincia(rs.getString("PROVINCIA"));
                certificado.setLocalidad(rs.getString("LOCALIDAD"));
                certificado.setDomicilio(rs.getString("DOMICILIO"));
                certificado.setCp(rs.getString("CP"));
                certificado.setTelefono_fijo(rs.getString("TELEFONO_FIJO"));
                certificado.setMovil(rs.getString("MOVIL"));
                certificado.setMail(rs.getString("MAIL"));
                certificado.setProvincia_registro(rs.getString("PROVINCIA_REGISTRO"));
                certificado.setAnio_registro(rs.getString("ANIO_REGISTRO"));
                certificado.setOrganismo(rs.getString("ORGANISMO"));
                certificado.setProvincia_otorgamiento(rs.getString("PROVINCIA_OTORGAMIENTO"));
                certificado.setFecha_otorgamiento(rs.getString("FECHA_OTORGAMIENTO"));
                certificado.setModo_gestion(rs.getString("MODO_GESTION"));
                certificado.setCodigo_certificado(rs.getString("CODIGO_CERTIFICADO"));
                certificado.setFecha_registro(rs.getString("FECHA_REGISTRO"));
                certificado.setItinerario(rs.getString("ITINERARIO"));
                certificado.setVia_acceso(rs.getString("VIA_ACCESO"));
                certificado.setFecha_acceso(rs.getString("FECHA_ACCESO"));
                certificado.setFecha_modulo(rs.getString("FECHA_MODULO"));

                listaCertificados.add(certificado);
            }
        }
        catch (SQLException e) {
            log.error("Se ha producido un error recuperando la lista de certificados", e);
            throw e;
        }
        catch (Exception e) {
            log.error("Se ha producido un error recuperando la lista de certificados", e);
            throw e;
        }
        finally {
            if (log.isDebugEnabled()) {log.debug("Procedemos a cerrar el statement y el resultset");}
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        if (log.isDebugEnabled()) {log.debug("getCertificadosRD34() : END");}
        return listaCertificados;
    }

    public String checkNumExpediente(ArrayList<CertRd34RDetalleUCVO> unidadesCompetencia, Connection con) throws SQLException, Exception {
        if (log.isDebugEnabled()) {log.debug("checkNumExpediente() : BEGIN");}

        Statement st = null;
        ResultSet rs = null;
        String codigoCertificado = "";
        try {
            String sql = "select distinct codcertificado from\n" +
                    "    (select rel.codcertificado,mf.unidadcompetencia,rel.codmodulo from \n" +
                    "    s_cer_rel_mod_cert rel\n" +
                    "    inner join s_cer_modulos_formativos mf on mf.codmodulo=rel.codmodulo\n" +
                    "    where mf.unidadcompetencia='";
            sql = sql + unidadesCompetencia.get(0).getUc() + "' \n" +
                    "    order by codcertificado,unidadcompetencia)";
            if (unidadesCompetencia.size() > 1) {
                sql = sql + " ct\n" +
                        "where ct.codcertificado in \n" +
                        "(\n" +
                        "    select rel.codcertificado from \n" +
                        "    s_cer_rel_mod_cert rel\n" +
                        "    inner join s_cer_modulos_formativos mf on mf.codmodulo=rel.codmodulo\n" +
                        "    where mf.unidadcompetencia='" + unidadesCompetencia.get(1).getUc() + "' \n" +
                        ")";
            }
            if (unidadesCompetencia.size() > 2) {
                for (int i=2; i<unidadesCompetencia.size(); i++) {
                    sql = sql + "and ct.codcertificado in \n" +
                            "(\n" +
                            "    select rel.codcertificado from \n" +
                            "    s_cer_rel_mod_cert rel\n" +
                            "    inner join s_cer_modulos_formativos mf on mf.codmodulo=rel.codmodulo\n" +
                            "    where mf.unidadcompetencia='" + unidadesCompetencia.get(i).getUc() + "' \n" +
                            ")";
                }
            }

            //if (log.isDebugEnabled()) {log.debug("sql = " + sql);}
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                codigoCertificado = (rs.getString("codcertificado"));
            }
        }
        catch (SQLException e) {
            log.error("Se ha producido un error recuperando el código de certificado", e);
            throw e;
        }
        catch (Exception e) {
            log.error("Se ha producido un error recuperando el código de certificado", e);
            throw e;
        }
        finally {
            if (log.isDebugEnabled()) {log.debug("Procedemos a cerrar el statement y el resultset");}
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        if (log.isDebugEnabled()) {log.debug("checkNumExpediente() : END");}
        return codigoCertificado;
    }

    public CertRd34RCabeceraVO insertarDatosLlamadaRD34(int total_certificados, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarDatosLlamadaRD34() : BEGIN");

        CertRd34RCabeceraVO datosLlamada = new CertRd34RCabeceraVO();

        PreparedStatement ps = null;
        int contBD = 1;
        String sql = "";
        try {
            //nos comentan que el idEnvio será correlativo por fecha, pero como la fechaEnvio es la actual, será siempre idEnvio=1
            int id = 1;

            java.util.Date fechaHoraActual = new Date();//Mon Dec 02 20:39:01 CET 2024
            //2024-10-07T08:47:00.000+02:00
            //DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");//a partir de java 7
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");
            String fechaHoraActualString = fmt.format(fechaHoraActual);//2024-12-03T15:00:01.104+0100
            fechaHoraActualString = fechaHoraActualString.substring(0,26) + ":00";

            sql = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_CARGA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " (ID_ENVIO, FECHA_ENVIO, TOTAL_CERTIFICADOS)"
                    + " values (?,?,?)";

            log.debug("sql = " + sql);

            ps = con.prepareStatement(sql);

            ps.setInt(contBD++, id);
            ps.setString(contBD++,fechaHoraActualString);
            ps.setInt(contBD,total_certificados);

            datosLlamada.setIdEnvio(id);
            datosLlamada.setFechaEnvio(fechaHoraActualString);
            datosLlamada.setTotal_certificados(total_certificados);

            if (ps.execute()) {
                log.info("Fila insertada insertarDatosLlamadaRD34");
            } else {
                log.error("Se ha producido un error en insertarDatosLlamadaRD34");
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error insertando datos llamada RD34 en la tabla RD34_CARGA", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido un error insertando datos llamada RD34 en la tabla RD34_CARGA", e);
            throw e;
        } finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(ps!=null) ps.close();

        }
        if(log.isDebugEnabled()) log.debug("insertarDatosLlamadaRD34() : END");

        return datosLlamada;
    }

    public boolean updateDatosCabeceraRD34(CertRd34RCabeceraVO datosLlamada, CertRd34RCabeceraVO datosRespuesta, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("updateDatosCabeceraRD34() : BEGIN");

        boolean exito;
        PreparedStatement ps = null;
        int contBD = 1;
        String sql = "";

        try {

            //if (datosLlamada.getFechaEnvio().equals(datosRespuesta.getFechaEnvio()) && datosLlamada.getIdEnvio() == datosRespuesta.getIdEnvio()) {
                sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_CARGA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                        + " SET FECHA_RESPUESTA=?, TOTAL_GRABADOS=?, TOTAL_ERRORES=?"
                        + " WHERE FECHA_ENVIO = ? AND ID_ENVIO = ?";

                //log.debug("sql = " + sql);

                ps = con.prepareStatement(sql);

                ps.setString(contBD++,datosRespuesta.getFechaRespuesta());
                ps.setInt(contBD++,Integer.valueOf(datosRespuesta.getTotal_grabados()));
                ps.setInt(contBD++,Integer.valueOf(datosRespuesta.getTotal_errores()));

                ps.setString(contBD++,datosLlamada.getFechaEnvio());
                ps.setInt(contBD, datosRespuesta.getIdEnvio());

                if (ps.executeUpdate() > 0) {
                    exito = true;
                } else {
                    log.error("Se ha producido un error en updateDatosCabeceraRD34");
                    exito = false;
                }
            /*} else {
                exito = false;
                log.error("Se ha producido un error en updateDatosCabeceraRD34 : no coinciden fechaEnvio " + datosLlamada.getFechaEnvio() + " / " + datosRespuesta.getFechaEnvio()
                        + " y/o idEnvio " + datosLlamada.getIdEnvio() + " / " + datosRespuesta.getIdEnvio() + " en llamada y en respuesta");
            }*/

        } catch (SQLException e) {
            log.error("Se ha producido un error actualizando  respuesta RD34 en la tabla RD34_CARGA", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido un error actualizando respuesta RD34 en la tabla RD34_CARGA", e);
            throw e;
        } finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(ps!=null) ps.close();

        }
        if(log.isDebugEnabled()) log.debug("updateDatosCabeceraRD34() : END");

        return exito;
    }

    public boolean insertarDatosDetalleRD34(CertRd34RCabeceraVO datosLlamada, CertRd34RCabeceraVO datosRespuesta, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarDatosDetalleRD34() : BEGIN");

        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide03.COD_ORG, ConstantesMeLanbide03.FICHERO_PROPIEDADES));

        boolean exito = false;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;

        String sql = "";
        String sqlCargadorD34 = "";
        ArrayList<CertRd34RDetalleVO> listaCertificados;;

        try {
            listaCertificados = datosRespuesta.getLista_de_certificados();

            sql = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_CARGA_EXP, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " (FECHA_ENVIO, ID_ENVIO, NUM_EXP, NIF, NUM_REGISTRO, RESULTADO, CCAA, FECHA_REGISTRO, UC1)"
                    + " values (?,?,?,?,?,?,?,?,?)";

            sqlCargadorD34 = "MERGE INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_E_TDE, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " USING (SELECT 1 FROM DUAL) ON (TDE_NUM = ? AND TDE_COD = 'CARGADORD34') WHEN MATCHED THEN UPDATE SET TDE_VALOR = ? " +
                    "WHEN NOT MATCHED THEN INSERT (TDE_MUN, TDE_EJE, TDE_NUM, TDE_COD, TDE_VALOR) VALUES (?, ?, ?, 'CARGADORD34', ?)";
            //log.debug("sql = " + sql);
            //log.debug("sqlCargadorD34 = " + sqlCargadorD34);

            ps = con.prepareStatement(sql);
            ps1 = con.prepareStatement(sqlCargadorD34);

            String codErrorCPSubido = ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_COD_ERROR_CP_SUBIDO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            String codErrorAPASubido = ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_COD_ERROR_APA_SUBIDO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);

            for (int i=0; i<listaCertificados.size(); i++) {
                int contBD = 1;
                int contBD1 = 1;
                String resultado = listaCertificados.get(i).getResultado();
                ps.setString(contBD++,datosLlamada.getFechaEnvio());
                ps.setInt(contBD++,datosRespuesta.getIdEnvio());
                ps.setString(contBD++,listaCertificados.get(i).getNumExpediente());
                ps.setString(contBD++,listaCertificados.get(i).getNif());
                ps.setString(contBD++,listaCertificados.get(i).getNumRegistro());

                if (!resultado.equals("OK")) {
                    resultado = "";
                    for (int j=0; j<listaCertificados.get(i).getListado_errores().size(); j++) {
                        if (listaCertificados.get(i).getListado_errores().get(j).getCodigo().equals(codErrorCPSubido)) {
                            resultado = codErrorCPSubido;
                            break;
                        } else if (listaCertificados.get(i).getListado_errores().get(j).getCodigo().equals(codErrorAPASubido)) {
                            resultado = codErrorAPASubido;
                            break;
                        }
                        resultado = resultado + listaCertificados.get(i).getListado_errores().get(j).getCodigo() + "_" + listaCertificados.get(i).getListado_errores().get(j).getDescripcion() + "_";
                    }
                    if (!resultado.equals(codErrorCPSubido) && !resultado.equals(codErrorAPASubido)) {
                        resultado = resultado.substring(0,resultado.length()-1);
                    }
                }

                ps.setString(contBD++,resultado);
                ps.setString(contBD++,listaCertificados.get(i).getCcaa());
                ps.setString(contBD++,listaCertificados.get(i).getFecha_registro());
                ps.setString(contBD,listaCertificados.get(i).getUnidades_competencia().get(0).getUc());

                ps.execute();


                //sólo se graba en Regexlan el desplegable BOOL : CARGADORD34 - CARGADO EN RD34, si el resultado ha sido OK ó errores " ICA3000 - El solicitante ya tiene un certificado completo para esta especialidad ", "ICA3002 -certificado.alta.ucs.repetidas "
                if (resultado.equals("OK") || resultado.equals(codErrorCPSubido) || resultado.equals(codErrorAPASubido)) {
                    ps1.setString(contBD1++,listaCertificados.get(i).getNumExpediente());
                    ps1.setString(contBD1++,"S");
                    ps1.setInt(contBD1++,codOrg);
                    ps1.setInt(contBD1++,Integer.parseInt(listaCertificados.get(i).getNumExpediente().substring(0,4)));
                    ps1.setString(contBD1++,listaCertificados.get(i).getNumExpediente());
                    ps1.setString(contBD1,"S");

                    ps1.execute();
                }

            }

            exito = true;

        } catch (SQLException e) {
            log.error("Se ha producido un error insertando detalles respuesta RD34 en la tabla RD34_CARGA_EXP", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido un error insertando detalles respuesta RD34 en la tabla RD34_CARGA_EXP", e);
            throw e;
        } finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(ps!=null) ps.close();
            if(ps1!=null) ps1.close();
            log.info ("Cerrada conexión en insertarDatosDetalleRD34");
        }
        if(log.isDebugEnabled()) log.debug("insertarDatosDetalleRD34() : END");

        return exito;
    }

    public boolean updateErrorDatosCabeceraRD34(CertRd34RCabeceraVO datosLlamada, String error, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("updateErrorDatosCabeceraRD34() : BEGIN");

        boolean exito;
        PreparedStatement ps = null;
        int contBD = 1;
        String sql = "";

        try {
            sql = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.RD34_CARGA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " SET ERROR_WS=?"
                    + " WHERE FECHA_ENVIO = ? AND ID_ENVIO = ?";

            log.debug("sql = " + sql);

            ps = con.prepareStatement(sql);

            ps.setString(contBD++,error);

            ps.setString(contBD++,datosLlamada.getFechaEnvio());
            ps.setInt(contBD, datosLlamada.getIdEnvio());

            if (ps.executeUpdate() > 0) {
                exito = true;
            } else {
                log.error("Se ha producido un error en updateErrorDatosCabeceraRD34");
                exito = false;
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error actualizando  error RD34 en la tabla RD34_CARGA", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido un error actualizando error RD34 en la tabla RD34_CARGA", e);
            throw e;
        } finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(ps!=null) ps.close();

        }
        if(log.isDebugEnabled()) log.debug("updateErrorDatosCabeceraRD34() : END");

        return exito;
    }


}//Class
