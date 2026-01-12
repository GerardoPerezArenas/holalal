/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecMunVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaBusquedaMunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class ColecMunicipioDAO {
    //Logger
    private static final Logger log = LogManager.getLogger(ColecMunicipioDAO.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    public List<FilaBusquedaMunicipioVO> buscarMunicipios(List<String> codComarcas, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaBusquedaMunicipioVO> retList = new ArrayList<FilaBusquedaMunicipioVO>();
        if (codComarcas != null && !codComarcas.isEmpty()) {
            try {
                String query = null;
                query = "select m.COLEC_COD_MUN COD_MUNICIPIO, c.COLEC_COD_COMARCA COD_COMARCA, m.COLEC_DESC_MUN DESC_MUNICIPIO, c.COLEC_DESC_COMARCA DESC_COMARCA"
                        + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_MUN, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " m"
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_COMARCAS, ConstantesMeLanbide48.FICHERO_PROPIEDADES) + " c on m.COLEC_COD_COMARCA = c.COLEC_COD_COMARCA"
                        + " where m.COLEC_COD_COMARCA in (";

                boolean noEsElPrimero = false;
                String codAct = null;
                for (int i = 0; i < codComarcas.size(); i++) {
                    codAct = codComarcas.get(i);
                    if (noEsElPrimero) {
                        query += ", " + codAct;
                    } else {
                        query += codAct;
                        noEsElPrimero = true;
                    }
                }

                query += ") order by c.COLEC_DESC_COMARCA, DESC_MUNICIPIO";
                if (log.isDebugEnabled()) {
                    log.error("sql = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    retList.add((FilaBusquedaMunicipioVO) MeLanbide48MappingUtils.getInstance().map(rs, FilaBusquedaMunicipioVO.class));
                }
            } catch (Exception ex) {
                log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarca " + (codComarcas != null ? codComarcas.toArray(new String[]{}) : " (codComarcas = null)"), ex);
                throw new Exception(ex);
            } finally {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Procedemos a cerrar el statement y el resultset");
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    log.error("Se ha producido un error cerrando el statement y el resulset", e);
                    throw new Exception(e);
                }
            }
        }
        return retList;
    }
    
    public List<SelectItem> getListaMunicipios(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select COLEC_COD_MUN CODIGO, COLEC_DESC_MUN DESCRIPCION from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_MUN, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " order by DESCRIPCION";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return retList;
    }
    
    public List<SelectItem> getMunicipiosPorComarca(Long codComarca, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select COLEC_COD_MUN CODIGO, COLEC_DESC_MUN DESCRIPCION from " + ConfigurationParameter.getParameter(ConstantesMeLanbide48.TABLA_COLEC_MUN, ConstantesMeLanbide48.FICHERO_PROPIEDADES)
                    + " where COLEC_COD_COMARCA = '" + codComarca + "'"
                    + " order by DESCRIPCION";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarca " + (codComarca != null ? codComarca : " (codComarca = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return retList;
    }

    public List<SelectItem> getMunicipiosPorConvocatoriaColectivoComarca(Integer idConvocatoria, Integer codColectivo, Integer codComarca, Integer idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select distinct "
                    + " cmp.codigoColectivo,cmp.grupoAmbitoorden "
                    + " ,cmp.CODIGOCOMARCA "
                    + " ,cmp.CODIGOPROVINCIA, cmp.codigoMunicipio CODIGO "
                    + " ,case when 4=" + (idioma != null ? idioma : 1) + " then (case when cmp.puntuacion> 0 then (cm.COLEC_DESC_MUN_EU||' ('||cmp.puntuacion||')') elSe cm.COLEC_DESC_MUN end)  "
                    + "    else (case when cmp.puntuacion> 0 then (cm.COLEC_DESC_MUN||' ('||cmp.puntuacion||')') elSe cm.COLEC_DESC_MUN end) end  "
                    + " as DESCRIPCION "
                    + " , cmp.puntuacion  "
                    + " from colec_municipios_puntuacion cmp "
                    + " inner join colec_municipio cm on cm.COLEC_COD_COMARCA=cmp.codigoComarca and cm.COLEC_COD_TTHH=cmp.codigoprovincia and cm.COLEC_COD_MUN=cmp.codigoMunicipio "
                    + " where  "
                    + " cmp.idFkConvocatoria=" + (idConvocatoria != null ? idConvocatoria : "null")
                    + " and cmp.codigoColectivo=" + (codColectivo != null ? codColectivo : "null")
                    + " and cmp.CODIGOCOMARCA=" + (codComarca != null ? codComarca : "null")
                    + " order by cmp.codigoColectivo,cmp.grupoAmbitoorden,(case when cmp.puntuacion>0 then 1 elSe 0 end) desc, cmp.puntuacion desc,DESCRIPCION,cmp.CODIGOCOMARCA";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Excepcion -getMunicipiosPorConvocatoriaColectivoComarca-  "
                    + "IdConvocatoria:" + idConvocatoria + "-"
                    + "codColectivo:" + codColectivo + "-"
                    + "codComarca:!" + codComarca + "-"
                    + "idioma:" + idioma + "-Error:" + ex.getMessage(),
                     ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return retList;
    }
    
    /**
     * COlectivos 1 y 2 con TTHH
     *
     * @param idConvocatoria
     * @param codColectivo
     * @param territorioHistorico
     * @param idioma
     * @param con
     * @return
     * @throws Exception
     */
    public List<SelectItem> getMunicipiosPorConvocatoriaColectivoTH(Integer idioma, Integer idConvocatoria, Integer codColectivo, Integer territorioHistorico,Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select distinct cabh.ambitoDescripcion,cmp.puntuacion, "
                    + " cmp.codigomunicipio CODIGO "
                    + " ,case when 4=" + (idioma != null ? idioma : "null") + " then (case when cmp.puntuacion> 0 then (cm.COLEC_DESC_MUN_EU||' ('||cmp.puntuacion||')') elSe cm.COLEC_DESC_MUN end) "
                    + " else (case when cmp.puntuacion> 0 then (cm.COLEC_DESC_MUN||' ('||cmp.puntuacion||')') elSe cm.COLEC_DESC_MUN end) end "
                    + " as  DESCRIPCION  "
                    + " From COLEC_AMBITOS_BLOQUES_HORAS cabh  "
                    + " LEFT JOIN colec_municipios_puntuacion cmp ON cabh.idfkconvocatoriaactiva=cmp.idfkconvocatoria and  cmp.CODIGOCOLECTIVO=cabh.colectivo AND cmp.GRUPOAMBITOORDEN=cabh.id "
                    + " LEFT JOIN COLEC_MUNICIPIO cm ON cm.COLEC_COD_MUN=cmp.codigomunicipio and cm.COLEC_COD_TTHH=cmp.codigoprovincia  "
                    + " where cabh.idfkconvocatoriaactiva=" + (idConvocatoria != null ? idConvocatoria : "null")
                    + " and cabh.colectivo=" + (codColectivo != null ? codColectivo : "null")
                    + " AND cmp.codigoprovincia=" + (territorioHistorico != null ? territorioHistorico : "null")
                    + " order by (case when cmp.puntuacion>0 then 1 elSe 0 end) desc, cmp.puntuacion desc,DESCRIPCION";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Excepcion -getMunicipiosPorConvocatoriaColectivoTH-  "
                    + "IdConvocatoria:" + idConvocatoria + "-"
                    + "codColectivo:" + codColectivo + "-"
                    + "territorioHist:" + territorioHistorico + "-"
                    + "idioma:" + idioma,
                     ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }
    /**
     * COlectivos 3 y 4 con IdAmbito Solicitado
     *
     * @param idConvocatoria
     * @param codColectivo
     * @param territorioHistorico
     * @param codAmbitoSol
     * @param idioma
     * @param con
     * @return
     * @throws Exception
     */
    public List<SelectItem> getMunicipiosPorConvocatoriaColectivoTHAmbitoSol(Integer idioma, Integer idConvocatoria, Integer codColectivo, Integer territorioHistorico, Integer codAmbitoSol, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SelectItem> retList = new ArrayList<SelectItem>();
        try {
            String query = null;
            query = "select distinct cabh.ambitoDescripcion,cmp.puntuacion, "
                    + " cmp.codigomunicipio CODIGO "
                    + " ,case when 4=" + (idioma != null ? idioma : "null") + " then (case when cmp.puntuacion> 0 then (cm.COLEC_DESC_MUN_EU||' ('||cmp.puntuacion||')') elSe cm.COLEC_DESC_MUN end) "
                    + " else (case when cmp.puntuacion> 0 then (cm.COLEC_DESC_MUN||' ('||cmp.puntuacion||')') elSe cm.COLEC_DESC_MUN end) end "
                    + " as  DESCRIPCION  "
                    + " From COLEC_AMBITOS_BLOQUES_HORAS cabh  "
                    + " LEFT JOIN colec_municipios_puntuacion cmp ON cabh.idfkconvocatoriaactiva=cmp.idfkconvocatoria and  cmp.CODIGOCOLECTIVO=cabh.colectivo AND cmp.GRUPOAMBITOORDEN=cabh.id "
                    + " LEFT JOIN COLEC_MUNICIPIO cm ON cm.COLEC_COD_MUN=cmp.codigomunicipio and cm.COLEC_COD_TTHH=cmp.codigoprovincia  "
                    + " where cabh.idfkconvocatoriaactiva=" + (idConvocatoria != null ? idConvocatoria : "null")
                    + " and cabh.colectivo=" + (codColectivo != null ? codColectivo : "null")
                    + " AND cmp.codigoprovincia=" + (territorioHistorico != null ? territorioHistorico : "null")
                    + " AND cabh.id=" + (codAmbitoSol != null ? codAmbitoSol : "null")
                    + " order by (case when cmp.puntuacion>0 then 1 elSe 0 end) desc, cmp.puntuacion desc,DESCRIPCION";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                retList.add((SelectItem) MeLanbide48MappingUtils.getInstance().map(rs, SelectItem.class));
            }
        } catch (Exception ex) {
            log.error("Excepcion -getMunicipiosPorConvocatoriaColectivoTHAmbitoSol-  "
                    + "IdConvocatoria:" + idConvocatoria + "-"
                    + "codColectivo:" + codColectivo + "-"
                    + "territorioHist:" + territorioHistorico + "-"
                    + "codAmbitoSol:" + codAmbitoSol
                    + "idioma:" + idioma,
                     ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return retList;
    }

    public ColecMunVO getMunicipioPorTHCodMun(Integer territorioHistorico, Integer codMunicipio, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ColecMunVO respuesta = null;
        try {
            String query = "select * from colec_municipio where COLEC_cod_mun="+(codMunicipio!=null?codMunicipio:"null")+" and colec_cod_tthh="+(territorioHistorico!=null?territorioHistorico:"null");
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                respuesta=(ColecMunVO) MeLanbide48MappingUtils.getInstance().map(rs, ColecMunVO.class);
            }
        } catch (Exception ex) {
            log.error("Excepcion -getMunicipioPorTHCodMun-  territorioHist:" + territorioHistorico + "-"
                    + "codMunicipio:" + codMunicipio
                    ,ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("Procedemos a cerrar el statement y el resultset");
                if (st != null) st.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return respuesta;
    }
    
    public Double getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun(Integer idBDConvocattoria, Integer colectivo,Integer territorioHistorico, Integer codMunicipio, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Double respuesta = 0.0D;
        try {
            String query = "select PUNTUACION from colec_municipios_puntuacion "
                    + "where IDFKCONVOCATORIA="+(idBDConvocattoria!=null?idBDConvocattoria:"null")
                    + " and CODIGOCOLECTIVO="+(colectivo!=null?colectivo:"null")
                    + " and CODIGOPROVINCIA="+(territorioHistorico!=null?territorioHistorico:"null")
                    + " and CODIGOMUNICIPIO="+(codMunicipio!=null?codMunicipio:"null")
                    ;
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                respuesta=rs.getDouble("PUNTUACION");
            }
        } catch (Exception ex) {
            log.error("Excepcion -getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun-  territorioHist:" + territorioHistorico + "-"
                    + "codMunicipio:" + codMunicipio
                    + "colectivo:" + colectivo
                    ,ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("Procedemos a cerrar el statement y el resultset");
                if (st != null) st.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return respuesta;
    }    
}
