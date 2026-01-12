/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.dao;

import es.altia.agora.webservice.tramitacion.servicios.tvg.bd.datos.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConstantesMeLanbide61;
import es.altia.flexia.integracion.moduloexterno.melanbide61.util.MeLanbide61MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ExpedienteREPLE;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ExpedienteUAAP;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.PersonaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.SubSolicVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.TrabajadorCAPVValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.ValorCampoDesplegableEstadoVO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide61DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide61DAO.class);

    //Instancia
    private static MeLanbide61DAO instance = null;

    private MeLanbide61DAO() {

    }

    public static MeLanbide61DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide61DAO.class) {
                instance = new MeLanbide61DAO();
            }
        }
        return instance;
    }

    public List<FilaContratoRenovacionPlantillaVO> getContratosExpediente(String numExp, Connection con) throws Exception {
        List<FilaContratoRenovacionPlantillaVO> retList = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        Statement st = null;
        ResultSet rs = null;
        Statement st2 = null;
        ResultSet rs2 = null;
        try {
            String sql = "select distinct (PEXCO_NCON) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + " where PEXCO_NUM like '" + numExp + "' order by PEXCO_NCON ASC";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            FilaContratoRenovacionPlantillaVO fila = null;
            String num = null;
            String query = null;
            boolean nuevoContrato = true;
            String nom = "";
            String ape1 = "";
            String ape2 = "";
            String dni = "";
            String nomApe = "";
            int tipoPers = -1;
            while (rs.next()) {
                num = rs.getString("PEXCO_NCON");
                query = "select PEXCO_NCON, PEXCO_NUM, PEXCO_CTPE, PEXCO_AP1, PEXCO_AP2, PEXCO_NOM, PEXCO_DOC from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                        + " where PEXCO_NCON like '" + num + "' and PEXCO_NUM like '" + numExp + "'";
                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st2 = con.createStatement();
                rs2 = st2.executeQuery(query);
                while (rs2.next()) {
                    if (nuevoContrato) {
                        fila = new FilaContratoRenovacionPlantillaVO();
                        fila.setNumContrato(rs2.getInt("PEXCO_NCON"));
                        if (rs2.wasNull()) {
                            fila.setNumContrato(null);
                        }
                        fila.setNumExpediente(rs2.getString("PEXCO_NUM"));
                        retList.add(fila);
                        nuevoContrato = false;
                    }

                    ape1 = rs2.getString("PEXCO_AP1");
                    ape2 = rs2.getString("PEXCO_AP2");
                    nom = rs2.getString("PEXCO_NOM");
                    dni = rs2.getString("PEXCO_DOC");
                    nomApe = nom != null ? nom : "";
                    nomApe += ape1 != null && !ape1.isEmpty() ? (!nomApe.isEmpty() ? " " : "") : "";
                    nomApe += ape1 != null ? ape1 : "";
                    nomApe += ape2 != null && !ape2.isEmpty() ? (!nomApe.isEmpty() ? " " : "") : "";
                    nomApe += ape2 != null ? ape2 : "";
                    try {
                        tipoPers = rs2.getInt("PEXCO_CTPE");
                        if (!rs2.wasNull()) {
                            switch (tipoPers) {
                                case 1:
                                    fila.setDni1(dni);
                                    fila.setNomApe1(nomApe);
                                    break;
                                case 2:
                                    fila.setDni2(dni);
                                    fila.setNomApe2(nomApe);
                                    break;
                                case 3:
                                    fila.setDni3(dni);
                                    fila.setNomApe3(nomApe);
                                    break;
                            }
                        }
                    } catch (SQLException ex) {
                        log.error("Se ha producido un error al procesar la persona del contrato con dni: " + dni, ex);
                    }
                }
                nuevoContrato = true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (st2 != null) {
                st2.close();
            }
            if (rs2 != null) {
                rs2.close();
            }
        }
        return retList;
    }

    public ContratoRenovacionPlantillaVO getContratoExpedientePorNumContrato(String numExp, Integer numContrato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ContratoRenovacionPlantillaVO contrato = null;
        try {
            PersonaContratoRenovacionPlantillaVO persona = null;
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " where PEXCO_NUM like '" + numExp + "' and PEXCO_NCON = '" + numContrato + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            boolean nuevoContrato = true;
            while (rs.next()) {
                if (nuevoContrato) {
                    contrato = new ContratoRenovacionPlantillaVO();
                    contrato.setEjercicio(rs.getInt("PEXCO_EJE"));
                    if (rs.wasNull()) {
                        contrato.setEjercicio(null);
                    }
                    contrato.setEntorno(rs.getInt("PEXCO_MUN"));
                    if (rs.wasNull()) {
                        contrato.setEntorno(null);
                    }
                    contrato.setNumContrato(rs.getInt("PEXCO_NCON"));
                    if (rs.wasNull()) {
                        contrato.setNumContrato(null);
                    }
                    contrato.setNumExpediente(rs.getString("PEXCO_NUM"));
                    contrato.setProcedimiento(rs.getString("PEXCO_PRO"));
                    nuevoContrato = false;
                }

                persona = new PersonaContratoRenovacionPlantillaVO();
                persona.setCodTercero(rs.getLong("PEXCO_TER_COD"));
                persona.setApellido1(rs.getString("PEXCO_AP1"));
                persona.setApellido2(rs.getString("PEXCO_AP2"));
                persona.setCnoe(rs.getString("PEXCO_CNOE"));
                persona.setCodTercero(rs.getLong("PEXCO_TER_COD"));
                persona.setCodTipoPersona(rs.getInt("PEXCO_CTPE"));
                if (rs.wasNull()) {
                    persona.setCodTipoPersona(null);
                }
                persona.setColectivo(rs.getInt("PEXCO_COL"));
                if (rs.wasNull()) {
                    persona.setColectivo(null);
                }
                persona.setDuracionContrato(rs.getInt("PEXCO_DCO"));
                if (rs.wasNull()) {
                    persona.setDuracionContrato(null);
                }
                persona.setFeAlta(rs.getDate("PEXCO_FAC"));
                persona.setFeBaja(rs.getDate("PEXCO_FBE"));
                persona.setFeFinContrato(rs.getDate("PEXCO_FFC"));
                persona.setFeNac(rs.getDate("PEXCO_FNA"));
                persona.setFlInmigrante(rs.getString("PEXCO_INM"));
                persona.setFlMinusvalido(rs.getString("PEXCO_MIN"));
                persona.setFlOtr(rs.getString("PEXCO_OTR"));
                persona.setFlPld(rs.getString("PEXCO_PLD"));
                persona.setFlRml(rs.getString("PEXCO_RML"));
                persona.setImpSubvencion(rs.getBigDecimal("PEXCO_IMP"));
                persona.setMesesDesempleo(rs.getInt("PEXCO_MDE"));
                if (rs.wasNull()) {
                    persona.setMesesDesempleo(null);
                }
                persona.setNivelEstudios(rs.getInt("PEXCO_NES"));
                if (rs.wasNull()) {
                    persona.setNivelEstudios(null);
                }
                persona.setNombre(rs.getString("PEXCO_NOM"));
                persona.setNumDoc(rs.getString("PEXCO_DOC"));
                persona.setPorReduJor(rs.getBigDecimal("PEXCO_PRJ"));
                persona.setRetSalarial(rs.getBigDecimal("PEXCO_SAL"));
                persona.setSexo(rs.getInt("PEXCO_SEX"));
                if (rs.wasNull()) {
                    persona.setSexo(null);
                }
                persona.setTipoContrato(rs.getString("PEXCO_TCO"));
                if (rs.wasNull()) {
                    persona.setTipoContrato("IN");
                }

                persona.setTipoJornada(rs.getString("PEXCO_TJO"));
                persona.setFecInConPre(rs.getDate("PEXCO_FINCPRE"));
                persona.setFecFinConPre(rs.getDate("PEXCO_FFINCPRE"));
                persona.setFecInConPre2(rs.getDate("PEXCO_FINCPRE2"));
                persona.setFecFinConPre2(rs.getDate("PEXCO_FFINCPRE2"));
                persona.setFecInConAd(rs.getDate("PEXCO_FINCAD"));
                persona.setFecFinConAd(rs.getDate("PEXCO_FFINCAD"));
                persona.setConvenio(rs.getString("PEXCO_CONVENIO"));
                persona.setDias(rs.getString("PEXCO_DIAS"));
                persona.setDiasF(rs.getString("PEXCO_DIASF"));
                persona.setDiasI(rs.getString("PEXCO_DIASI"));
                persona.setDiasContrato(rs.getString("PEXCO_DIASCON"));
                persona.setFechaPublica(rs.getDate("PEXCO_FEPUBLICA"));
                persona.setFechaIniContrato(rs.getDate("PEXCO_FEINICON"));
                persona.setFechaFinContrato(rs.getDate("PEXCO_FEFINCON"));
                persona.setFondo(rs.getString("PEXCO_FONDO"));
                persona.setSitPrevia(rs.getString("PEXCO_SITPREVIA"));
                contrato.addPersona(persona);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return contrato;
    }

    public String getMaxIdContrato(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String id = "0";
        try {
            String query = null;
            query = "select MAX(PEXCO_NCON) AS IDCON from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " where PEXCO_NUM like '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("IDCON") != null) {
                    id = rs.getString("IDCON");
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }

    public boolean guardarContrato(ContratoRenovacionPlantillaVO contrato, Connection con) throws Exception {
        Statement st = null;
        int rows = 0;
        try {
            con.setAutoCommit(false);
            List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
            PersonaContratoRenovacionPlantillaVO p = null;
            String query = "";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            boolean persRelevadaExistia = false;
            boolean persRelevistaExistia = false;
            boolean persAdicionalExistia = false;
            boolean persRelevadaExisteAhora = false;
            boolean persRelevistaExisteAhora = false;
            boolean persAdicionalExisteAhora = false;
            ContratoRenovacionPlantillaVO contratoAntiguo = getContratoExpedientePorNumContrato(contrato.getNumExpediente(), contrato.getNumContrato(), con);
            List<PersonaContratoRenovacionPlantillaVO> personasContAntiguo = contratoAntiguo.getPersonas();
            for (int i = 0; i < personasContAntiguo.size(); i++) {
                if (personasContAntiguo.get(i).getCodTipoPersona() == 1) {
                    persAdicionalExistia = true;
                }
                if (personasContAntiguo.get(i).getCodTipoPersona() == 2) {
                    persRelevistaExistia = true;
                }
                if (personasContAntiguo.get(i).getCodTipoPersona() == 3) {
                    persRelevadaExistia = true;
                }
            }

            for (int i = 0; i < personas.size(); i++) {
                p = personas.get(i);

                if (p.getCodTipoPersona() != null) {
                    switch (p.getCodTipoPersona()) {
                        case 1:
                            if (p.getNumDoc().isEmpty()) {
                                persAdicionalExisteAhora = false;
                            } else {
                                persAdicionalExisteAhora = true;
                                ////comprobamos si existe persona de ese tipo guardada
                                if (persAdicionalExistia) {
                                    query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                            + " set PEXCO_NOM = '" + p.getNombre() + "', PEXCO_AP1 = '" + p.getApellido1() + "', PEXCO_AP2 = '" + p.getApellido2() + "',"
                                            + " PEXCO_DOC='" + p.getNumDoc() + "',"
                                            + (p.getFeNac() != null ? " PEXCO_FNA = TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : " PEXCO_FNA = null,")
                                            + " PEXCO_SEX = " + p.getSexo() + ", PEXCO_COL = " + p.getColectivo() + ", "
                                            + (p.getFecInConAd() != null ? " PEXCO_FINCAD = TO_DATE('" + format.format(p.getFecInConAd()) + "', 'dd/mm/yyyy')," : " PEXCO_FINCAD = null,")
                                            + (p.getFecFinConAd() != null ? " PEXCO_FFINCAD = TO_DATE('" + format.format(p.getFecFinConAd()) + "', 'dd/mm/yyyy')," : " PEXCO_FFINCAD = null,")
                                            + " PEXCO_CNOE = '" + p.getCnoe() + "',"
                                            //+" PEXCO_DIAS = "+p.getDias()+", PEXCO_DIASF = "+p.getDiasF()+"',"
                                            + " PEXCO_DIASI = " + p.getDiasI() + ",  PEXCO_DIASCON = " + p.getDiasContrato()
                                            + ", PEXCO_NES = " + p.getNivelEstudios() + ", "
                                            //+ (p.getFechaPublica()!= null ? " PEXCO_FEPUBLICA = TO_DATE('"+format.format(p.getFechaPublica())+"', 'dd/mm/yyyy')," : " PEXCO_FEPUBLICA = null,")
                                            // + (p.getFechaIniContrato()!= null ? " PEXCO_FEINICON = TO_DATE('"+format.format(p.getFechaIniContrato())+"', 'dd/mm/yyyy')," : " PEXCO_FEINICON = null,")
                                            // + (p.getFechaFinContrato()!= null ? " PEXCO_FEFINCON = TO_DATE('"+format.format(p.getFechaFinContrato())+"', 'dd/mm/yyyy')," : " PEXCO_FEFINCON = null,")
                                            + " PEXCO_SITPREVIA = '" + p.getSitPrevia() + "' "
                                            //+ ", PEXCO_CONVENIO ="+(p.getConvenio()!= null ? "'"+p.getConvenio() +"' " : "null")
                                            + " where PEXCO_NUM = '" + contrato.getNumExpediente() + "' and PEXCO_NCON = '" + contrato.getNumContrato()
                                            //+"' and PEXCO_DOC = '"+p.getNumDoc()+"'";
                                            + "' and PEXCO_CTPE = '" + p.getCodTipoPersona() + "'";
                                } else {
                                    //insert
                                    query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                            + "(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, "
                                            + "PEXCO_FNA, PEXCO_SEX,PEXCO_COL, PEXCO_FINCAD, PEXCO_FFINCAD, PEXCO_CONVENIO, PEXCO_FEPUBLICA, PEXCO_FEINICON, PEXCO_FEFINCON, "
                                            + "PEXCO_DIAS, PEXCO_DIASF, PEXCO_DIASI, PEXCO_DIASCON, PEXCO_NES, PEXCO_SITPREVIA)"
                                            + " values("
                                            + contrato.getEntorno() + ", " + contrato.getEjercicio() + ", '" + contrato.getProcedimiento() + "', '" + contrato.getNumExpediente() + "', " + contrato.getNumContrato() + ","
                                            + " " + p.getCodTipoPersona() + ", '" + p.getNumDoc() + "', " + p.getCodTercero() + ", '" + p.getNombre() + "', '" + p.getApellido1() + "', '" + p.getApellido2() + "',"
                                            + (p.getFeNac() != null ? " TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : " null,")
                                            + " " + p.getSexo() + ", " + p.getColectivo() + ", "
                                            + (p.getFecInConAd() != null ? " TO_DATE('" + format.format(p.getFecInConAd()) + "', 'dd/mm/yyyy')," : " null,")
                                            + (p.getFecFinConAd() != null ? " TO_DATE('" + format.format(p.getFecFinConAd()) + "', 'dd/mm/yyyy')," : " null,")
                                            + (p.getConvenio() != null ? "'" + p.getConvenio() + "', " : "null, ")
                                            + (p.getFechaPublica() != null ? " TO_DATE('" + format.format(p.getFechaPublica()) + "', 'dd/mm/yyyy')," : " null,")
                                            + (p.getFechaIniContrato() != null ? " TO_DATE('" + format.format(p.getFechaIniContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                            + (p.getFechaFinContrato() != null ? " TO_DATE('" + format.format(p.getFechaFinContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                            + p.getDias() + ", " + p.getDiasF() + ", " + p.getDiasI() + ", " + p.getDiasContrato() + ", " + p.getNivelEstudios()
                                            + ", '" + p.getSitPrevia() + "' "
                                            + ")";
                                }
                            }
                            break;
                        case 2:
                            persRelevistaExisteAhora = true;
                            if (persRelevistaExistia) {
                                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                        + " set PEXCO_NOM = '" + p.getNombre() + "', PEXCO_AP1 = '" + p.getApellido1() + "', PEXCO_AP2 = '" + p.getApellido2() + "',"
                                        + " PEXCO_DOC='" + p.getNumDoc() + "',"
                                        + (p.getFeNac() != null ? " PEXCO_FNA = TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : " PEXCO_FNA = null,")
                                        + " PEXCO_MIN = '" + p.getFlMinusvalido() + "', PEXCO_INM = '" + p.getFlInmigrante() + "', PEXCO_NES = " + p.getNivelEstudios() + ","
                                        + " PEXCO_COL = " + p.getColectivo() + ", PEXCO_MDE = " + p.getMesesDesempleo() + ", PEXCO_PLD = '" + p.getFlPld() + "', PEXCO_RML = '" + p.getFlRml() + "', PEXCO_OTR = '" + p.getFlOtr() + "', PEXCO_TCO = '" + p.getTipoContrato() + "',"
                                        + (p.getFeAlta() != null ? " PEXCO_FAC = TO_DATE('" + format.format(p.getFeAlta()) + "', 'dd/mm/yyyy')," : " PEXCO_FAC = null,")
                                        + " PEXCO_TJO = '" + p.getTipoJornada() + "', PEXCO_CNOE = '" + p.getCnoe() + "', PEXCO_DCO = " + p.getDuracionContrato() + ", PEXCO_SAL= " + p.getRetSalarial()
                                        + ", PEXCO_FONDO = " + (p.getFondo() != null ? "'" + p.getFondo() + "'" : "null") + ", " + (p.getSitPrevia() != null ? "PEXCO_SITPREVIA = '" + p.getSitPrevia() + "' " : "PEXCO_SITPREVIA = null ")
                                        + ", PEXCO_CONVENIO =" + (p.getConvenio() != null ? "'" + p.getConvenio() + "' " : "null")
                                        + (p.getFechaPublica() != null ? ", PEXCO_FEPUBLICA = TO_DATE('" + format.format(p.getFechaPublica()) + "', 'dd/mm/yyyy')," : ", PEXCO_FEPUBLICA = null,")
                                        + (p.getFechaIniContrato() != null ? " PEXCO_FEINICON = TO_DATE('" + format.format(p.getFechaIniContrato()) + "', 'dd/mm/yyyy')," : " PEXCO_FEINICON = null,")
                                        + (p.getFechaFinContrato() != null ? " PEXCO_FEFINCON = TO_DATE('" + format.format(p.getFechaFinContrato()) + "', 'dd/mm/yyyy')," : " PEXCO_FEFINCON = null, ")
                                        + " PEXCO_DIAS = " + p.getDias() + ",  PEXCO_DIASF = " + p.getDiasF()
                                        + " ,PEXCO_SEX = " + p.getSexo()
                                        + " where PEXCO_NUM = '" + contrato.getNumExpediente() + "' and PEXCO_NCON = '" + contrato.getNumContrato()
                                        //+"' and PEXCO_DOC = '"+p.getNumDoc()+"'";
                                        + "' and PEXCO_CTPE = '" + p.getCodTipoPersona() + "'";
                            } else {
                                //insert
                                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                        + "(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_MIN, PEXCO_INM, PEXCO_PLD, PEXCO_RML, PEXCO_OTR, PEXCO_COL, PEXCO_MDE, PEXCO_NES, PEXCO_TCO, PEXCO_FAC, "
                                        + " PEXCO_TJO, PEXCO_FFC, PEXCO_DCO, PEXCO_CNOE, PEXCO_SAL, PEXCO_IMP, PEXCO_SITPREVIA, PEXCO_FONDO,"
                                        + " PEXCO_FEPUBLICA, PEXCO_CONVENIO, PEXCO_FEINICON, PEXCO_FEFINCON,PEXCO_DIAS, PEXCO_DIASF)"
                                        + " values("
                                        + contrato.getEntorno() + ", " + contrato.getEjercicio() + ", '" + contrato.getProcedimiento() + "', '" + contrato.getNumExpediente() + "', " + contrato.getNumContrato() + ","
                                        + " " + p.getCodTipoPersona() + ", '" + p.getNumDoc() + "', " + p.getCodTercero() + ", '" + p.getNombre() + "', '" + p.getApellido1() + "', '" + p.getApellido2() + "',"
                                        + (p.getFeNac() != null ? " TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : "null,")
                                        + " " + p.getSexo() + ", '" + p.getFlMinusvalido() + "', '" + p.getFlInmigrante() + "', '" + p.getFlPld() + "', '" + p.getFlRml() + "', '" + p.getFlOtr() + "',"
                                        + " " + p.getColectivo() + ", " + p.getMesesDesempleo() + ", " + p.getNivelEstudios() + ", '" + p.getTipoContrato() + "',"
                                        + (p.getFeAlta() != null ? " TO_DATE('" + format.format(p.getFeAlta()) + "', 'dd/mm/yyyy')," : "null,")
                                        + " '" + p.getTipoJornada() + "',"
                                        + (p.getFeFinContrato() != null ? " TO_DATE('" + format.format(p.getFeFinContrato()) + "', 'dd/mm/yyyy')," : "null,")
                                        + " " + p.getDuracionContrato() + ", '" + p.getCnoe() + "', " + (p.getRetSalarial() != null ? p.getRetSalarial().toString() : "null") + ", " + (p.getImpSubvencion() != null ? p.getImpSubvencion().toString() : "null")
                                        + ", '" + p.getSitPrevia() + "', " + (p.getFondo() != null ? "'" + p.getFondo() + "'" : "null") + ", "
                                        + (p.getFechaPublica() != null ? " TO_DATE('" + format.format(p.getFechaPublica()) + "', 'dd/mm/yyyy')," : " null,")
                                        + (p.getConvenio() != null ? "'" + p.getConvenio() + "', " : "null, ")
                                        + (p.getFechaIniContrato() != null ? " TO_DATE('" + format.format(p.getFechaIniContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                        + (p.getFechaFinContrato() != null ? " TO_DATE('" + format.format(p.getFechaFinContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                        + p.getDias() + ", " + p.getDiasF() + " "
                                        + ")";
                            }
                            break;
                        case 3:
                            persRelevadaExisteAhora = true;
                            if (persRelevadaExistia) {
                                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                        + " set PEXCO_NOM = '" + p.getNombre() + "', PEXCO_AP1 = '" + p.getApellido1() + "', PEXCO_AP2 = '" + p.getApellido2() + "',"
                                        + " PEXCO_DOC='" + p.getNumDoc() + "',"
                                        + (p.getFeNac() != null ? " PEXCO_FNA = TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : " PEXCO_FNA = null,")
                                        + " PEXCO_MIN = '" + p.getFlMinusvalido() + "', PEXCO_INM = '" + p.getFlInmigrante() + "', PEXCO_NES = " + p.getNivelEstudios() + ","
                                        + " PEXCO_COL = " + p.getColectivo() + ", PEXCO_MDE = " + p.getMesesDesempleo() + ", PEXCO_TCO = '" + p.getTipoContrato() + "',"
                                        + " PEXCO_PRJ = " + (p.getPorReduJor() != null ? p.getPorReduJor().intValue() : "null") + ","
                                        + (p.getFeBaja() != null ? " PEXCO_FBE = TO_DATE('" + format.format(p.getFeBaja()) + "', 'dd/mm/yyyy')," : "PEXCO_FBE = null,")
                                        + (p.getFeAlta() != null ? " PEXCO_FAC = TO_DATE('" + format.format(p.getFeAlta()) + "', 'dd/mm/yyyy')," : " PEXCO_FAC = null,")
                                        + (p.getFecInConPre() != null ? " PEXCO_FINCPRE = TO_DATE('" + format.format(p.getFecInConPre()) + "', 'dd/mm/yyyy')," : " PEXCO_FINCPRE = null,")
                                        + (p.getFecFinConPre() != null ? " PEXCO_FFINCPRE = TO_DATE('" + format.format(p.getFecFinConPre()) + "', 'dd/mm/yyyy')," : " PEXCO_FFINCPRE = null,")
                                        + (p.getFecInConPre2() != null ? " PEXCO_FINCPRE2 = TO_DATE('" + format.format(p.getFecInConPre2()) + "', 'dd/mm/yyyy')," : " PEXCO_FINCPRE2 = null,")
                                        + (p.getFecFinConPre2() != null ? " PEXCO_FFINCPRE2 = TO_DATE('" + format.format(p.getFecFinConPre2()) + "', 'dd/mm/yyyy')," : " PEXCO_FFINCPRE2 = null,")
                                        + " PEXCO_TJO = '" + p.getTipoJornada() + "', PEXCO_PLD = '" + p.getFlPld() + "', PEXCO_RML = '" + p.getFlRml() + "', PEXCO_OTR = '" + p.getFlOtr() + "',"
                                        + " PEXCO_CNOE = '" + p.getCnoe() + "'"
                                        + " ,PEXCO_SEX = " + p.getSexo()
                                        + " where PEXCO_NUM = '" + contrato.getNumExpediente() + "' and PEXCO_NCON = '" + contrato.getNumContrato()
                                        //+"' and PEXCO_DOC = '"+p.getNumDoc()+"'"
                                        + "' and PEXCO_CTPE = '" + p.getCodTipoPersona() + "'"
                                        + "";
                            } else {
                                //insert
                                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_MIN, PEXCO_INM, PEXCO_PLD, PEXCO_RML, PEXCO_OTR, PEXCO_COL, PEXCO_MDE, PEXCO_NES, PEXCO_TCO, PEXCO_FAC, PEXCO_TJO, PEXCO_CNOE, PEXCO_SAL,  PEXCO_FBE, PEXCO_PRJ,"
                                        + "PEXCO_FINCPRE,PEXCO_FFINCPRE, PEXCO_FINCPRE2,PEXCO_FFINCPRE2)"
                                        + " values("
                                        + contrato.getEntorno() + ", " + contrato.getEjercicio() + ", '" + contrato.getProcedimiento() + "', '" + contrato.getNumExpediente() + "', " + contrato.getNumContrato() + ","
                                        + " " + p.getCodTipoPersona() + ", '" + p.getNumDoc() + "', " + p.getCodTercero() + ", '" + p.getNombre() + "', '" + p.getApellido1() + "', '" + p.getApellido2() + "',"
                                        + (p.getFeNac() != null ? " TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : "null,")
                                        + " " + p.getSexo() + ", '" + p.getFlMinusvalido() + "', '" + p.getFlInmigrante() + "', '" + p.getFlPld() + "', '" + p.getFlRml() + "', '" + p.getFlOtr() + "',"
                                        + " " + p.getColectivo() + ", " + p.getMesesDesempleo() + ", " + p.getNivelEstudios() + ", " + p.getTipoContrato() + ","
                                        + (p.getFeAlta() != null ? " TO_DATE('" + format.format(p.getFeAlta()) + "', 'dd/mm/yyyy')," : "null,")
                                        + " '" + p.getTipoJornada() + "', '" + p.getCnoe() + "', " + (p.getRetSalarial() != null ? p.getRetSalarial().toString() : "null") + ", "
                                        + (p.getFeBaja() != null ? " TO_DATE('" + format.format(p.getFeBaja()) + "', 'dd/mm/yyyy')," : "null,")
                                        + " " + (p.getPorReduJor() != null ? p.getPorReduJor().toString() : "null") + ", "
                                        + (p.getFecInConPre() != null ? " TO_DATE('" + format.format(p.getFecInConPre()) + "', 'dd/mm/yyyy')," : "null,")
                                        + (p.getFecFinConPre() != null ? " TO_DATE('" + format.format(p.getFecFinConPre()) + "', 'dd/mm/yyyy')," : "null,")
                                        + (p.getFecInConPre2() != null ? " TO_DATE('" + format.format(p.getFecInConPre2()) + "', 'dd/mm/yyyy')," : "null,")
                                        + (p.getFecFinConPre2() != null ? " TO_DATE('" + format.format(p.getFecFinConPre2()) + "', 'dd/mm/yyyy')" : "null")
                                        + ")";
                            }
                            break;

                    }
                    if (!query.isEmpty()) {
                        if (log.isDebugEnabled()) {
                            log.debug("query = " + query);
                        }
                        st = con.createStatement();
                        rows = st.executeUpdate(query);
                        log.debug("actualiza = " + rows);
                    }
                }
            }
            //comprobamos si hay que eliminar algún registro
            if (!persAdicionalExisteAhora) {
                eliminarPersona(contrato.getNumExpediente(), contrato.getNumContrato(), 1, con);
                eliminarCamposFechaPreviaAdicional(contrato.getNumExpediente(), contrato.getNumContrato(), con);
            }
            if (!persRelevistaExisteAhora) {
                eliminarPersona(contrato.getNumExpediente(), contrato.getNumContrato(), 2, con);
            }
            if (!persRelevadaExisteAhora) {
                eliminarPersona(contrato.getNumExpediente(), contrato.getNumContrato(), 3, con);
            }
            con.commit();
        } catch (Exception ex) {
            con.rollback();
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return rows > 0;
    }

    public List<Tercero> busquedaTerceros(Tercero ter, Connection con) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("BUSQUEDA TERCEROS");
        }
        Statement st = null;
        ResultSet rs = null;
        List<Tercero> retList = new ArrayList<Tercero>();
        try {
            String sql = "select TER_COD, TER_DOC, TER_NOM, TER_AP1, TER_AP2 "
                    + ", TO_CHAR(F.VALOR, 'dd/MM/yyyy') as fNaci,\n"
                    + " CASE WHEN S.VALOR = 1 THEN 'H' ELSE 'M' END AS SEXO"
                    + " from T_TER "
                    + " INNER JOIN T_CAMPOS_DESPLEGABLE S ON S.COD_TERCERO = TER_COD AND S.COD_CAMPO='TSEXOTERCERO'\n"
                    + " INNER JOIN T_CAMPOS_FECHA F ON F.COD_TERCERO = TER_COD AND F.COD_CAMPO = 'TFECNACIMIENTO'"
                    + "where 1=1";

            if (ter != null) {
                if (ter.getDni() != null && !ter.getDni().isEmpty()) {
                    sql += " and upper(TER_DOC) like '%" + ter.getDni().toUpperCase() + "%'";
                }
                if (ter.getNombre() != null && !ter.getNombre().isEmpty()) {
                    sql += " and upper(TER_NOM) like '%" + ter.getNombre().toUpperCase() + "%'";
                }
                if (ter.getApellido1() != null && !ter.getApellido1().isEmpty()) {
                    sql += " and upper(TER_AP1) like '%" + ter.getApellido1().toUpperCase() + "%'";
                }
                if (ter.getApellido2() != null && !ter.getApellido2().isEmpty()) {
                    sql += " and upper(TER_AP2) like '%" + ter.getApellido2().toUpperCase() + "%'";
                }
            }
            sql += " order by TER_NOM ASC, TER_AP1 ASC, TER_AP2 ASC, TER_DOC ASC";
            if (log.isDebugEnabled()) {
                log.debug("SQL BUSQUEDA TERCEROS = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            Tercero t = null;
            while (rs.next()) {
                t = new Tercero();
                t.setApellido1(rs.getString("TER_AP1"));
                t.setApellido2(rs.getString("TER_AP2"));
                t.setDni(rs.getString("TER_DOC"));
                t.setId(rs.getLong("TER_COD"));
                t.setNombre(rs.getString("TER_NOM"));
                t.setSexo(rs.getString("SEXO"));
                t.setFNaci(rs.getString("fNaci"));
                retList.add(t);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("FIN BUSQUEDA TERCEROS: " + retList.size());
        }
        return retList;
    }

    public List<Tercero> busquedaTercerosIntermediacion(Tercero ter, Connection con) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("BUSQUEDA TERCEROS INTERMEDIACION");
        }
        Statement st = null;
        ResultSet rs = null;
        List<Tercero> retList = new ArrayList<Tercero>();
        try {
            String sql = "select gen_per_tipo_doc, gen_per_num_doc, gen_per_nombre, gen_per_apellido1, gen_per_apellido2, "
                    + " case when gen_per_sexo=1 then 'H' when gen_per_sexo=2 then 'M' else gen_per_sexo end as sexo, "
                    + " TO_CHAR(gen_per_fecha_nac, 'dd/MM/yyyy') as fNaci "
                    + " from VISTA_GEN_PERSONA_FISICA p  where 1=1 ";

            st = con.createStatement();
            if (ter != null) {
                if (ter.getDni() != null && !ter.getDni().isEmpty()) {
                    sql += " and gen_per_num_doc like '" + ter.getDni().toUpperCase() + "%' ";
                }
                if (ter.getNombre() != null && !ter.getNombre().isEmpty()) {
                    sql += " and gen_per_nombre like '" + ter.getNombre().toUpperCase() + "%'";
                }
                if (ter.getApellido1() != null && !ter.getApellido1().isEmpty()) {
                    sql += " and gen_per_apellido1 like '" + ter.getApellido1().toUpperCase() + "%'";
                }
                if (ter.getApellido2() != null && !ter.getApellido2().isEmpty()) {
                    sql += " and gen_per_apellido2 like '" + ter.getApellido2().toUpperCase() + "%'";
                }
            }
            sql += " order by gen_per_nombre ASC, gen_per_apellido1 ASC, gen_per_apellido2 ASC, gen_per_num_doc ASC";
            if (log.isDebugEnabled()) {
                log.debug("SQL BUSQUEDA TERCEROS = " + sql);
            }
            rs = st.executeQuery(sql);
            Tercero t = null;
            while (rs.next()) {
                t = new Tercero();
                t.setApellido1(rs.getString("gen_per_apellido1"));
                t.setApellido2(rs.getString("gen_per_apellido2"));
                t.setDni(rs.getString("gen_per_num_doc"));
                // t.setId(rs.getLong("TER_COD"));
                t.setNombre(rs.getString("gen_per_nombre"));
                t.setSexo(rs.getString("sexo"));
                t.setFNaci(rs.getString("fNaci"));
                retList.add(t);
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las áreas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("FIN BUSQUEDA TERCEROS: " + retList.size());
        }
        return retList;
    }

    public int getNuevoNumContrato(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "select max(distinct(pexco_ncon)) + 1 as num from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                return rs.getInt("NUM");
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el último número de contrato", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean crearContrato(ContratoRenovacionPlantillaVO contrato, Connection con) throws Exception {
        Statement st = null;
        int rows = 0;
        try {
            con.setAutoCommit(false);
            List<PersonaContratoRenovacionPlantillaVO> personas = contrato.getPersonas();
            PersonaContratoRenovacionPlantillaVO p = null;
            String query = "";
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            for (int i = 0; i < personas.size(); i++) {
                p = personas.get(i);

                if (p.getCodTipoPersona() != null) {
                    switch (p.getCodTipoPersona()) {
                        case 1:
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                    + "(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, "
                                    + "PEXCO_FNA, PEXCO_SEX,PEXCO_COL, PEXCO_FINCAD, PEXCO_FFINCAD, PEXCO_CONVENIO, PEXCO_FEPUBLICA, PEXCO_FEINICON, PEXCO_FEFINCON, "
                                    + "PEXCO_DIAS, PEXCO_DIASF, PEXCO_DIASI, PEXCO_DIASCON, PEXCO_NES, PEXCO_SITPREVIA)"//, PEXCO_FONDO
                                    + " values("
                                    + contrato.getEntorno() + ", " + contrato.getEjercicio() + ", '" + contrato.getProcedimiento() + "', '" + contrato.getNumExpediente() + "', " + contrato.getNumContrato() + ","
                                    + " " + p.getCodTipoPersona() + ", '" + p.getNumDoc() + "', " + p.getCodTercero() + ", '" + p.getNombre() + "', '" + p.getApellido1() + "', '" + p.getApellido2() + "',"
                                    + (p.getFeNac() != null ? " TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : " null,")
                                    + " " + p.getSexo() + ", " + p.getColectivo() + ", "
                                    + (p.getFecInConAd() != null ? " TO_DATE('" + format.format(p.getFecInConAd()) + "', 'dd/mm/yyyy')," : " null,")
                                    + (p.getFecFinConAd() != null ? " TO_DATE('" + format.format(p.getFecFinConAd()) + "', 'dd/mm/yyyy')," : " null,")
                                    + (p.getConvenio() != null ? "'" + p.getConvenio() + "', " : "null, ")
                                    + (p.getFechaPublica() != null ? " TO_DATE('" + format.format(p.getFechaPublica()) + "', 'dd/mm/yyyy')," : " null,")
                                    + (p.getFechaIniContrato() != null ? " TO_DATE('" + format.format(p.getFechaIniContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                    + (p.getFechaFinContrato() != null ? " TO_DATE('" + format.format(p.getFechaFinContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                    + p.getDias() + ", " + p.getDiasF() + ", " + p.getDiasI() + ", " + p.getDiasContrato() + ", " + p.getNivelEstudios()
                                    + ", '" + p.getSitPrevia() + "'"//+" , '" + p.getFondo() + "'"
                                    + ")";
                            break;
                        case 2:
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                                    + "(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_MIN, PEXCO_INM, PEXCO_PLD, PEXCO_RML, PEXCO_OTR, PEXCO_COL, PEXCO_MDE, PEXCO_NES, PEXCO_TCO, PEXCO_FAC, "
                                    + " PEXCO_TJO, PEXCO_FFC, PEXCO_DCO, PEXCO_CNOE, PEXCO_SAL, PEXCO_IMP, PEXCO_SITPREVIA, "
                                    + " PEXCO_FEPUBLICA, PEXCO_CONVENIO, PEXCO_FEINICON, PEXCO_FEFINCON,PEXCO_DIAS, PEXCO_DIASF, PEXCO_FONDO)"
                                    + " values("
                                    + contrato.getEntorno() + ", " + contrato.getEjercicio() + ", '" + contrato.getProcedimiento() + "', '" + contrato.getNumExpediente() + "', " + contrato.getNumContrato() + ","
                                    + " " + p.getCodTipoPersona() + ", '" + p.getNumDoc() + "', " + p.getCodTercero() + ", '" + p.getNombre() + "', '" + p.getApellido1() + "', '" + p.getApellido2() + "',"
                                    + (p.getFeNac() != null ? " TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : "null,")
                                    + " " + p.getSexo() + ", '" + p.getFlMinusvalido() + "', '" + p.getFlInmigrante() + "', '" + p.getFlPld() + "', '" + p.getFlRml() + "', '" + p.getFlOtr() + "',"
                                    + " " + p.getColectivo() + ", " + p.getMesesDesempleo() + ", " + p.getNivelEstudios() + ", '" + p.getTipoContrato() + "',"
                                    + (p.getFeAlta() != null ? " TO_DATE('" + format.format(p.getFeAlta()) + "', 'dd/mm/yyyy')," : "null,")
                                    + " '" + p.getTipoJornada() + "',"
                                    + (p.getFeFinContrato() != null ? " TO_DATE('" + format.format(p.getFeFinContrato()) + "', 'dd/mm/yyyy')," : "null,")
                                    + " " + p.getDuracionContrato() + ", '" + p.getCnoe() + "', " + (p.getRetSalarial() != null ? p.getRetSalarial().toString() : "null") + ", " + (p.getImpSubvencion() != null ? p.getImpSubvencion().toString() : "null")
                                    + ", '" + p.getSitPrevia() + "', "
                                    + (p.getFechaPublica() != null ? " TO_DATE('" + format.format(p.getFechaPublica()) + "', 'dd/mm/yyyy')," : " null,")
                                    + (p.getConvenio() != null ? "'" + p.getConvenio() + "', " : "null, ")
                                    + (p.getFechaIniContrato() != null ? " TO_DATE('" + format.format(p.getFechaIniContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                    + (p.getFechaFinContrato() != null ? " TO_DATE('" + format.format(p.getFechaFinContrato()) + "', 'dd/mm/yyyy')," : " null,")
                                    + p.getDias() + ", " + p.getDiasF() + " " + " , " + (p.getFondo() != null ? "'" + p.getFondo() + "'" : "null")
                                    + ")";
                            break;
                        case 3:
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "(PEXCO_MUN, PEXCO_EJE, PEXCO_PRO, PEXCO_NUM, PEXCO_NCON, PEXCO_CTPE, PEXCO_DOC, PEXCO_TER_COD, PEXCO_NOM, PEXCO_AP1, PEXCO_AP2, PEXCO_FNA, PEXCO_SEX, PEXCO_MIN, PEXCO_INM, PEXCO_PLD, PEXCO_RML, PEXCO_OTR, PEXCO_COL, PEXCO_MDE, PEXCO_NES, PEXCO_TCO, PEXCO_FAC, PEXCO_TJO, PEXCO_CNOE, PEXCO_SAL,  PEXCO_FBE, PEXCO_PRJ,"
                                    + "PEXCO_FINCPRE,PEXCO_FFINCPRE, PEXCO_FINCPRE2,PEXCO_FFINCPRE2)"
                                    + " values("
                                    + contrato.getEntorno() + ", " + contrato.getEjercicio() + ", '" + contrato.getProcedimiento() + "', '" + contrato.getNumExpediente() + "', " + contrato.getNumContrato() + ","
                                    + " " + p.getCodTipoPersona() + ", '" + p.getNumDoc() + "', " + p.getCodTercero() + ", '" + p.getNombre() + "', '" + p.getApellido1() + "', '" + p.getApellido2() + "',"
                                    + (p.getFeNac() != null ? " TO_DATE('" + format.format(p.getFeNac()) + "', 'dd/mm/yyyy')," : "null,")
                                    + " " + p.getSexo() + ", '" + p.getFlMinusvalido() + "', '" + p.getFlInmigrante() + "', '" + p.getFlPld() + "', '" + p.getFlRml() + "', '" + p.getFlOtr() + "',"
                                    + " " + p.getColectivo() + ", " + p.getMesesDesempleo() + ", " + p.getNivelEstudios() + ", " + p.getTipoContrato() + ","
                                    + (p.getFeAlta() != null ? " TO_DATE('" + format.format(p.getFeAlta()) + "', 'dd/mm/yyyy')," : "null,")
                                    + " '" + p.getTipoJornada() + "', '" + p.getCnoe() + "', " + (p.getRetSalarial() != null ? p.getRetSalarial().toString() : "null") + ", "
                                    + (p.getFeBaja() != null ? " TO_DATE('" + format.format(p.getFeBaja()) + "', 'dd/mm/yyyy')," : "null,")
                                    + " " + (p.getPorReduJor() != null ? p.getPorReduJor().toString() : "null") + ", "
                                    + (p.getFecInConPre() != null ? " TO_DATE('" + format.format(p.getFecInConPre()) + "', 'dd/mm/yyyy')," : "null,")
                                    + (p.getFecFinConPre() != null ? " TO_DATE('" + format.format(p.getFecFinConPre()) + "', 'dd/mm/yyyy')," : "null,")
                                    + (p.getFecInConPre2() != null ? " TO_DATE('" + format.format(p.getFecInConPre2()) + "', 'dd/mm/yyyy')," : "null,")
                                    + (p.getFecFinConPre2() != null ? " TO_DATE('" + format.format(p.getFecFinConPre2()) + "', 'dd/mm/yyyy')" : "null")
                                    + ")";
                            break;
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("query = " + query);
                    }
                    st = con.createStatement();
                    rows = st.executeUpdate(query);
                }
            }
            con.commit();
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando un contrato", ex);
            con.rollback();
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return rows > 0;
    }

    public boolean eliminarContrato(String numExp, Integer numContrato, Connection con) throws Exception {
        String sql = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                + " where PEXCO_NUM = '" + numExp + "' and PEXCO_NCON = " + numContrato;
        if (log.isDebugEnabled()) {
            log.debug("SQL ELIMINAR CONTRATO = " + sql);
        }
        int rows = 0;
        Statement st = null;
        try {
            con.setAutoCommit(false);
            st = con.createStatement();
            rows = st.executeUpdate(sql);
            con.commit();
        } catch (SQLException ex) {
            log.error("Se ha producido un error al eliminar un contrato. ", ex);
            con.rollback();
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return rows > 0;
    }

    public List<FilaDocumentoContableVO> getListaDocumentosContablesExpediente(String numExp, Connection con) throws Exception {
        String sql = "SELECT\r\n"
                + " TO_CHAR(ANOEJ_V1,'0000')||'.'||TO_CHAR(ANORI_V1,'0000')||'.'||TO_CHAR(NUMA_V1,'000000')||'.'||TO_CHAR(NUMD_V1,'0000') AS REFERENCIA,\r\n"
                + " TIPDOCU_V1 AS TIPO_DOC,\r\n"
                + " TO_CHAR(EIMPORTE_V1,'9G999G999G990D00', 'NLS_NUMERIC_CHARACTERS = '',.''') AS IMPORTE,\r\n"
                + " FDOCU_V1 AS FECHA_DOCUMENTO,\r\n"
                + " FTRAMI_V1 AS FECHA_TRAMITACION,\r\n"
                + " FECMAQ_V1 AS FECHA_MAQUETA,\r\n"
                + " NUMMAQ_V1 AS NUMERO_MAQUETA,\r\n"
                + " FCONTA_V1 AS FECHA_CONTAB,\r\n"
                + " APUNTE_V1 AS NUMERO_APUNTE\r\n"
                + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_J1201V00, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "\r\n"
                + " WHERE CODAPLIC_V1='" + ConstantesMeLanbide61.TIPO_AYUDA_RPLEM + "'\r\n"
                + " --and REFEXP_V1 = (SELECT nvl(substr(txt_valor, 5), '') FROM E_TXT WHERE TXT_COD='" + ConstantesMeLanbide61.CAMPO_NUMEXP_P29 + "' and txt_num = '" + numExp + "')\r\n"
                + " and REFEXP_V1 = (SELECT nvl(txt_valor, '') FROM E_TXT WHERE TXT_COD='" + ConstantesMeLanbide61.CAMPO_NUMEXP_P29 + "' and txt_num = '" + numExp + "')\r\n"
                + " ORDER BY REFERENCIA, TIPDOCU_V1";
        if (log.isDebugEnabled()) {
            log.debug("sql = " + sql);
        }
        List<FilaDocumentoContableVO> retList = new ArrayList<FilaDocumentoContableVO>();
        Statement st = null;
        ResultSet rs = null;
        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                retList.add((FilaDocumentoContableVO) MeLanbide61MappingUtils.getInstance().map(rs, FilaDocumentoContableVO.class));
            }
        } catch (Exception ex) {
            retList.clear();
            log.error("Se ha producido un error recuperando lista documentos contables para expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return retList;
    }

    public ArrayList<ValorCampoDesplegableEstadoVO> getListaDesplegable(Connection con, String codigo, Integer idioma) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<ValorCampoDesplegableEstadoVO> retList = new ArrayList<ValorCampoDesplegableEstadoVO>();
        try {
            String query = "select DES_VAL_COD, DES_NOM, DES_VAL_ESTADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_DES_VAL, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " where DES_COD ='" + codigo + "' order by DES_NOM";

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                ValorCampoDesplegableEstadoVO desplegable = new ValorCampoDesplegableEstadoVO();
                desplegable.setCodigo(rs.getString("DES_VAL_COD"));

                String descripcion = rs.getString("DES_NOM");
                String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide61.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (idioma == ConstantesMeLanbide61.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }
//                log.debug("Valor descripcion despleagble asignado: " + descripcion);
                desplegable.setDescripcion(descripcion);

                desplegable.setEstado(rs.getString("DES_VAL_ESTADO"));

                if (!rs.wasNull()) {
                    retList.add(desplegable);
                }
            }
            return retList;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al obtener los valores del desplegable '" + codigo + "'", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean eliminarPersona(String numExp, Integer numContrato, Integer tipoPersona, Connection con) throws Exception {
        String sql = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                + " where PEXCO_NUM = '" + numExp + "' and PEXCO_NCON = " + numContrato + " and PEXCO_CTPE = " + tipoPersona;
        if (log.isDebugEnabled()) {
            log.debug("SQL ELIMINAR persona tipo(" + tipoPersona + ") = " + sql);
        }
        int rows = 0;
        Statement st = null;
        try {
            con.setAutoCommit(false);
            st = con.createStatement();
            rows = st.executeUpdate(sql);
            con.commit();
        } catch (SQLException ex) {
            log.error("Se ha producido un error al eliminar una persona. ", ex);
            con.rollback();
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return rows > 0;
    }

    public boolean eliminarCamposFechaPreviaAdicional(String numExp, Integer numContrato, Connection con) throws Exception {
        String sql = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                + " set PEXCO_FINCPRE2=NULL, PEXCO_FFINCPRE2=NULL "
                + " where PEXCO_NUM = '" + numExp + "' and PEXCO_NCON = " + numContrato + " and PEXCO_CTPE = 3 ";
        if (log.isDebugEnabled()) {
            log.debug("SQL eliminarCamposFechaPreviaAdicional = " + sql);
        }
        int rows = 0;
        Statement st = null;
        try {
            con.setAutoCommit(false);
            st = con.createStatement();
            rows = st.executeUpdate(sql);
            con.commit();
        } catch (SQLException ex) {
            log.error("Se ha producido un error al eliminarCamposFechaPreviaAdicional. ", ex);
            con.rollback();
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return rows > 0;
    }

    /**
     * Recupera el valor del campo suplementario a nivel de tramite de un
     * expediente
     *
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return String con el valor o null sino se ha podido recuperar
     * @throws Exception
     */
    public String getValorFechaAcuseExpediente(String numExpediente, String codCampo, Connection con) throws Exception {
        String campoSuple = null;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "select TFET_VALOR from E_TFET "
                    + "WHERE TFET_NUM='" + numExpediente + "' AND TFET_COD= '" + codCampo.trim() + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql getValorFechaAcuseExpediente= " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                campoSuple = rs.getString("TFET_VALOR");
                log.debug("campoSuple " + campoSuple);
            }

        } catch (SQLException ex) {
            log.error("Error al recuperar el campo suplementario " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return campoSuple;

    }

    /**
     * Metodos que inserta el dato general CRO_FIP en tabla E_CRO.
     *
     * @param numExp
     * @param fechaAcuse
     * @param con
     * @return boolean
     * @throws java.lang.Exception
     *
     */
    public boolean insertDatoGeneral(String numExp, String fechaAcuse, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "update E_CRO"
                    + " set CRO_FIP= TO_DATE('" + fechaAcuse + "', 'yyyy/MM/dd')"
                    + " where CRO_NUM = '" + numExp + "'"
                    + " AND CRO_TRA=37";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            con.commit();

            return insert > 0;

        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Modificar Modulo Formativo" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public List<ExpedienteVO> getExpedientesPorDNI(String numExp, String dni, Connection con) throws Exception {
        List<ExpedienteVO> retList = new ArrayList<ExpedienteVO>();
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "select DISTINCT PEXCO_NUM from " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " where pexco_doc like '" + dni + "' AND PEXCO_NUM NOT LIKE '" + numExp + "' and pexco_ctpe in (2,3)";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            ExpedienteVO exp = null;
            while (rs.next()) {
                exp = new ExpedienteVO();
                exp.setNumero(rs.getString("PEXCO_NUM"));
                retList.add(exp);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getExpedientesPorDNI", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return retList;
    }

    public List<TrabajadorCAPVValueObject> getTrabajadoresCAPV(String numExp, Connection con) throws SQLException {
        List<TrabajadorCAPVValueObject> relacion = new ArrayList<TrabajadorCAPVValueObject>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;

        try {
            String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_CCCCAPV, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
            query = "SELECT ID,NUMCCC,NUMTRABFIJ FROM " + tabla + " WHERE NUM_EXP=? ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();

            while (rs.next()) {
                TrabajadorCAPVValueObject trabVO = new TrabajadorCAPVValueObject();
                trabVO.setIdTrabajador(rs.getLong("ID"));
                trabVO.setNumCCC(rs.getString("NUMCCC"));
                trabVO.setNumTrabajadorFijo(rs.getInt("NUMTRABFIJ"));

                relacion.add(trabVO);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getTrabajadoresCAPV", ex);
            throw new SQLException(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return relacion;
    }

    public boolean insertarTrabajadorCAPV(TrabajadorCAPVValueObject objeto, String numExp, Connection con) throws SQLException {
        int insertado = 0;
        PreparedStatement ps = null;
        String query;
        int contbd = 1;

        try {
            String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_CCCCAPV, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
            String seq = ConfigurationParameter.getParameter(ConstantesMeLanbide61.SEQ_CCCCAPV, ConstantesMeLanbide61.FICHERO_PROPIEDADES);

            query = "INSERT INTO " + tabla + " (ID,NUM_EXP,NUMCCC,NUMTRABFIJ) VALUES (" + seq + ".nextval,?,?,?)";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            ps = con.prepareStatement(query);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, objeto.getNumCCC());
            ps.setInt(contbd++, objeto.getNumTrabajadorFijo());
            insertado = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en insertarTrabajadorCAPV", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return insertado == 1;
    }

    public boolean actualizarTrabajadorCAPV(long id, TrabajadorCAPVValueObject objeto, String numExp, Connection con) throws SQLException {
        int modificado = 0;
        PreparedStatement ps = null;
        String query;
        int contbd = 1;
        try {
            String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_CCCCAPV, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
            query = "UPDATE " + tabla + " SET NUM_EXP=?,NUMCCC=?,NUMTRABFIJ=? WHERE ID=?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            ps = con.prepareStatement(query);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, objeto.getNumCCC());
            ps.setInt(contbd++, objeto.getNumTrabajadorFijo());
            ps.setLong(contbd++, id);

            modificado = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en actualizarTrabajadorCAPV", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return modificado == 1;
    }

    public boolean eliminarTrabajadorCAPV(long id, Connection con) throws SQLException {
        int borrado = 0;
        PreparedStatement ps = null;
        String query;

        try {
            String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_CCCCAPV, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
            query = "DELETE FROM " + tabla + " WHERE ID=?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            ps = con.prepareStatement(query);
            ps.setLong(1, id);

            borrado = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en eliminarTrabajadorCAPV", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return borrado == 1;
    }

    //EXPEDIENTES TELEMÁTICOS
    public List<String> getExpedientesTelematicos(String proc, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<String> listExp = new ArrayList<String>();
        String numExp = new String();
        try {
            String query = null;
            query = "SELECT EXP_NUM FROM E_EXP "
                    + "INNER JOIN E_EXR On Exr_Num = Exp_Num And Exr_Mun = Exp_Mun And Exp_Pro = Exr_Pro "
                    + "INNER JOIN R_RES ON RES_UOR=EXR_UOR AND RES_TIP=EXR_TIP AND RES_NUM=EXR_NRE AND RES_EJE=EXR_EJR AND REGISTRO_TELEMATICO =1 "
                    + "WHERE EXR_PRO='" + proc + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numExp = rs.getString("EXP_NUM");
                listExp.add(numExp);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listExp;
    }

    public List<ExpedienteREPLE> getExpedientesTelematicosREPLE(Connection con) throws Exception {
        log.debug(" BEGIN getExpedientesTelematicosREPLE  ");
        Statement st = null;
        ResultSet rs = null;
        List<ExpedienteREPLE> listExp = new ArrayList<ExpedienteREPLE>();
        ExpedienteREPLE expediente;
        String numExp = new String();
        Date fecReso;
        Date fecContratoRel;
        try {
            String query = null;

            query = "SELECT EXP_NUM, TFE_VALOR AS FECRESOL , PEXCO_FAC AS FECCONTRATOREL "
                    + "FROM E_EXP "
                    + "Inner Join E_Exr On Exr_Num = Exp_Num And Exr_Mun = Exp_Mun And Exp_Pro = Exr_Pro "
                    + "INNER JOIN R_RES ON RES_UOR=EXR_UOR AND RES_TIP=EXR_TIP AND RES_NUM=EXR_NRE AND RES_EJE=EXR_EJR AND REGISTRO_TELEMATICO =1 "
                    + "LEFT JOIN E_TFE ON TFE_NUM=EXR_NUM AND TFE_COD='FECRESOL' "
                    + "LEFT JOIN REPLE_PEXCO ON PEXCO_NUM=EXR_NUM AND PEXCO_CTPE = 2 "
                    + //"WHERE EXR_PRO='REPLE' and (PEXCO_FEINICON is not null or TFE_VALOR is not null)" +
                    "INNER JOIN E_TDE ON TDE_NUM=EXR_NUM AND TDE_COD='RESULESTTEC' AND TDE_VALOR='S' "
                    + "WHERE EXR_PRO='REPLE' and (PEXCO_FAC is not null or TFE_VALOR is not null) "
                    + "ORDER BY EXR_NUM ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numExp = rs.getString("EXP_NUM");
                fecReso = rs.getDate("FECRESOL");
                fecContratoRel = rs.getDate("FECCONTRATOREL");
                expediente = new ExpedienteREPLE();
                expediente.setNumExpediente(numExp);
                expediente.setFecResolucion(fecReso);
                expediente.setFecContratoRelevo(fecContratoRel);
                listExp.add(expediente);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getExpedientesTelematicosREPLE", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getExpedientesTelematicosREPLE  ");
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listExp;
    }

    public List<ExpedienteUAAP> getExpedientesTelematicosUAAP(Connection con) throws Exception {
        log.debug(" BEGIN getExpedientesTelematicosUAAP  ");
        Statement st = null;
        ResultSet rs = null;
        List<ExpedienteUAAP> listExp = new ArrayList<ExpedienteUAAP>();
        ExpedienteUAAP expediente;
        String numExp = new String();
        Date fecReg;
        int anyo;
        try {
            String query = null;

            query = "SELECT EXP_NUM, RES_FEC, EXTRACT(YEAR FROM RES_FEC) AS ANYO "
                    + "FROM E_EXP "
                    + "Inner Join E_Exr On Exr_Num = Exp_Num And Exr_Mun = Exp_Mun And Exp_Pro = Exr_Pro "
                    + "INNER JOIN R_RES ON RES_UOR=EXR_UOR AND RES_TIP=EXR_TIP AND RES_NUM=EXR_NRE AND RES_EJE=EXR_EJR AND REGISTRO_TELEMATICO =1 "
                    + "WHERE EXR_PRO='UAAP' AND EXTRACT(YEAR FROM RES_FEC)= EXTRACT(YEAR FROM SYSDATE)-1 "
                    + "ORDER BY EXR_NUM ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numExp = rs.getString("EXP_NUM");
                fecReg = rs.getDate("RES_FEC");
                anyo = rs.getInt("ANYO");
                expediente = new ExpedienteUAAP();
                expediente.setNumExpediente(numExp);
                expediente.setFecRegistro(fecReg);
                expediente.setAnyo(anyo);
                listExp.add(expediente);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getExpedientesTelematicosUAAP", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getExpedientesTelematicosREPLE  ");
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listExp;
    }

    public boolean existeTramite(String numExp, String codTramite, Connection con) throws Exception {
        boolean existe = false;
        ResultSet rs = null;
        PreparedStatement ps = null;

        try {
            String query = null;
            query = "SELECT CRO_TRA FROM E_CRO "
                    + " INNER JOIN E_TRA on CRO_PRO=TRA_PRO AND TRA_COD=CRO_TRA AND tra_fba is null"
                    + " WHERE CRO_NUM=?  AND TRA_COU=? "
                    + " ORDER BY TRA_COU";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setString(2, codTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                existe = true;
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error en existeTramite ", e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return existe;
    }

    public boolean crearTramite(int codOrg, String numExp, Long codTramite, String unidad, Connection con) throws Exception {
        int rows = 0;
        PreparedStatement ps = null;

        try {
            String[] datos = numExp.split("/");
            String query = null;
            query = "INSERT INTO E_CRO (CRO_PRO, CRO_MUN, CRO_EJE, CRO_NUM, CRO_TRA,CRO_OCU, CRO_FEI,CRO_USU,CRO_UTR) "
                    + "values (?,?,?,?,?,1,SYSDATE,5,?)";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            ps = con.prepareStatement(query);
            log.debug("param 1 = " + datos[1]);
            ps.setString(1, datos[1]);
            log.debug("param 2 = " + codOrg);
            ps.setLong(2, codOrg);
            log.debug("param 3 = " + datos[0]);
            ps.setString(3, datos[0]);
            log.debug("param 4 = " + numExp);
            ps.setString(4, numExp);
            log.debug("param 5 = " + codTramite);
            ps.setLong(5, codTramite);
            log.debug("param 6 = " + Integer.valueOf(unidad));
            ps.setLong(6, Long.parseLong(unidad));

            rows = ps.executeUpdate();

        } catch (NumberFormatException e) {
            log.error("Se ha producido un error creando trámite", e);
            throw new Exception(e);
        } catch (SQLException e) {
            log.error("Se ha producido un error creando trámite", e);
            throw new Exception(e);
        }
        return rows > 0;
    }

    public Long obtenerCodigoInternoTramite(Integer entorno, String proc, String codTramite, Connection con) throws Exception {
        Long cod = 0L;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "select TRA_COD from E_TRA "
                    + " where TRA_MUN = " + entorno
                    + " and TRA_PRO = '" + proc + "'"
                    + " and TRA_COU = " + codTramite;

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                cod = rs.getLong("TRA_COD");
                if (rs.wasNull()) {
                    cod = 0L;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código interno del trámite : " + codTramite, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cod;
    }

    public String obtenerUnidadExpediente(String numExp, Connection con) throws Exception {
        String cod = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select EXP_UOR from E_EXP where EXP_NUM = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }

            ps = con.prepareStatement(sql);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            if (rs.next()) {
                cod = rs.getString("EXP_UOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la unidad organizativa : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cod;
    }

    public Date obtenerFechaIniRelevo(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Date resultado = null;
        try {
            String sql = "select pexco_fac from reple_pexco "
                    + " where pexco_num='" + numExpediente + "' and pexco_ncon=("
                    + "select min(pexco_ncon) from reple_pexco "
                    + "where pexco_num='" + numExpediente + "') and PEXCO_CTPE =2";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs != null && rs.next()) {
                resultado = rs.getDate(1);
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error obteniendo la fecha de inicio relevo del expediente : " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return resultado;
    }

    public boolean insertarFecha20Relevo(int codOrganizacion, String numExpediente, Date fecha20Relevo, Connection con) throws SQLException {
        PreparedStatement ps = null;
        int insertado = 0;
        String sql = "";
        StringBuilder query;
        try {
            if (existeValorFecha20(numExpediente, con)) {
                sql = "UPDATE E_TFE SET TFE_VALOR = ?"
                        + " WHERE TFE_NUM = '" + numExpediente + "' AND TFE_COD = '" + ConstantesMeLanbide61.FEC20MESESREL + "'";
            } else {
                sql = "INSERT INTO E_TFE"
                        + " VALUES (" + codOrganizacion + ",'" + numExpediente.substring(0, 4) + "','" + numExpediente + "','" + ConstantesMeLanbide61.FEC20MESESREL + "',?,null,null)";
            }
            query = new StringBuilder(sql);
            log.debug("sql: " + query);
            ps = con.prepareStatement(query.toString());
            ps.setDate(1, new java.sql.Date(fecha20Relevo.getTime()));
            insertado = ps.executeUpdate();

        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al dar de alta un registro en la tabla E_TFE" + sqle);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return insertado == 1;
    }

    private boolean existeValorFecha20(String numExpediente, Connection con) throws SQLException {
        String sql = "SELECT TFE_VALOR FROM E_TFE WHERE TFE_NUM = '" + numExpediente + "' AND TFE_COD = '" + ConstantesMeLanbide61.FEC20MESESREL + "'";
        StringBuilder query = new StringBuilder(sql);
        log.debug("sql: " + query);
        PreparedStatement ps = con.prepareStatement(query.toString());
        ResultSet rs = ps.executeQuery(sql);
        boolean resp = rs != null && rs.next();
        if (rs != null) {
            rs.close();
        }
        ps.close();
        return resp;
    }

    public boolean eliminarFecha20Relevo(String numExpediente, Connection con) throws SQLException {
        PreparedStatement ps = null;
        int eliminado = 0;
        String sql = "";

        StringBuilder query;
        try {
            if (existeValorFecha20(numExpediente, con)) {
                sql = "DELETE FROM E_TFE "
                        + "WHERE TFE_NUM = '" + numExpediente + "' AND TFE_COD = '" + ConstantesMeLanbide61.FEC20MESESREL + "'";

                query = new StringBuilder(sql);
                log.debug("sql: " + query);
                ps = con.prepareStatement(query.toString());

                eliminado = ps.executeUpdate();
            } else {
                eliminado = 1;
            }
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al eliminar un registro en la tabla E_TFE" + sqle);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return eliminado == 1;
    }

    public DatosEconomicosExpVO getImporteSubvencionYPorc(String numExpediente, Connection con) throws Exception {
        log.info(this.getClass().getSimpleName() + " >>>>>>>>>  entra en  getImporteSubvencionYPorc.DAO");
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String tablaPexco = ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
        String tablaImportes = ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_IMPORTES, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
        String tablaPorcentajes = ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_PORCENTAJE_PAGO, ConstantesMeLanbide61.FICHERO_PROPIEDADES);
        String[] datosExp = numExpediente.split("/");
        try {
            query = "SELECT importe,PORC_PRIMER AS porcentaje"
                    + " FROM " + tablaPexco
                    + " INNER JOIN (SELECT MAX(pexco_ncon) AS fila, pexco_num"
                    + " FROM " + tablaPexco + "  GROUP BY pexco_num) a"
                    + " ON a.pexco_num = " + tablaPexco + ".pexco_num"
                    + " AND pexco_ncon = a.fila"
                    + " AND pexco_ctpe = 2"
                    + " INNER JOIN " + tablaImportes
                    + " ON " + tablaImportes + ".sexo = " + tablaPexco + ".pexco_sex"
                    + " AND " + tablaImportes + ".ejercicio = " + tablaPexco + ".pexco_eje"
                    + " INNER JOIN " + tablaPorcentajes
                    + " ON " + tablaPorcentajes + ".porc_eje=" + tablaPexco + ".pexco_eje"
                    + " AND " + tablaPorcentajes + ".porc_pro='" + datosExp[1] + "'"
                    + " WHERE " + tablaPexco + ".pexco_num = '" + numExpediente + "'";
            log.debug("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs != null && rs.next()) {
                return (DatosEconomicosExpVO) MeLanbide61MappingUtils.getInstance().map(rs, DatosEconomicosExpVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error obteniendo el importe total de la subvención y el porcentaje del primer pago del expediente : " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return null;
    }

    public boolean haySustituto(String numExpediente, Connection con) throws SQLException {
        log.info(this.getClass().getSimpleName() + " >>>>>>>>>  entra en  haySustituto.DAO");
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        boolean existe = false;
        try {
            query = "SELECT MAX(pexco_ncon) "
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.RPLEM_PEXCO, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " WHERE pexco_num='" + numExpediente + "'"
                    + " AND pexco_ctpe = 2";
            log.debug("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si existe persona sustituta del expediente : " + numExpediente, ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return existe;
    }

    public int guardarImportes(int codOrganizacion, String numExpediente, DatosEconomicosExpVO datosEcon, Connection con) throws SQLException {
        log.info(this.getClass().getSimpleName() + " >>>>>>>>>  entra en  guardarImportes.DAO");
        Statement st = null;
        String query = null;
        String[] datosExp = numExpediente.split("/");
        int insertado = 0;
        try {
            // total
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + "(TNU_MUN,TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) "
                    + " VALUES("
                    + codOrganizacion
                    + ", " + datosExp[0]
                    + ", '" + numExpediente + "'"
                    + ", '" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_TOTAL, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'"
                    + ", " + datosEcon.getImporteSubvencion() + ")";

            log.debug("sql: " + query);
            st = con.createStatement();
            insertado = insertado + st.executeUpdate(query);

            // primer pago
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + "(TNU_MUN,TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) "
                    + " VALUES("
                    + codOrganizacion
                    + ", " + datosExp[0]
                    + ", '" + numExpediente + "'"
                    + ", '" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_PRIMERO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'"
                    + ", " + datosEcon.getImportePrimerPago() + ")";

            log.debug("sql: " + query);
            st = con.createStatement();
            insertado = insertado + st.executeUpdate(query);

            // segundo pago
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + "(TNU_MUN,TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) "
                    + " VALUES("
                    + codOrganizacion
                    + ", " + datosExp[0]
                    + ", '" + numExpediente + "'"
                    + ", '" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_SEGUNDO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'"
                    + ", " + datosEcon.getImporteSegundoPago() + ")";
            log.debug("sql: " + query);
            st = con.createStatement();
            insertado = insertado + st.executeUpdate(query);

        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando el importe total de la subvención y el porcentaje del primer pago del expediente : " + numExpediente, ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return insertado;
    }

    public boolean existeValorImporte(String numExpediente, Connection con) throws SQLException {
        log.info(this.getClass().getSimpleName() + " >>>>>>>>>  entra en  existeValorImporte.DAO");
        boolean existe = false;
        ResultSet rs = null;
        Statement st = null;
        String query = null;
        try {
            query = "SELECT TNU_VALOR FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)// total 
                    + " WHERE TNU_NUM = '" + numExpediente
                    + "' AND TNU_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_TOTAL, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'";
            log.debug("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                existe = true;
            }

            query = "SELECT TNU_VALOR FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)// total 
                    + " WHERE TNU_NUM = '" + numExpediente
                    + "' AND TNU_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_PRIMERO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'";
            log.debug("sql: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            existe = rs.next();
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si existe Importe Subvencion del expediente : " + numExpediente, ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return existe;
    }

    public int actualizarImportes(int codOrganizacion, String numExpediente, DatosEconomicosExpVO datosEcon, Connection con) throws SQLException {
        log.info(this.getClass().getSimpleName() + " >>>>>>>>>  entra en  actualizarImportes.DAO");
        Statement st = null;
        String query = null;
        String[] datosExp = numExpediente.split("/");
        int insertado = 0;
        try {
            // total
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " SET TNU_VALOR=" + datosEcon.getImporteSubvencion()
                    + " WHERE TNU_MUN=" + codOrganizacion
                    + " AND TNU_NUM='" + numExpediente + "'"
                    + " AND TNU_EJE=" + datosExp[0]
                    + " AND TNU_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_TOTAL, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'";

            log.debug("sql: " + query);
            st = con.createStatement();
            insertado = insertado + st.executeUpdate(query);

            // primer pago
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " SET TNU_VALOR=" + datosEcon.getImportePrimerPago()
                    + " WHERE TNU_MUN=" + codOrganizacion
                    + " AND TNU_NUM='" + numExpediente + "'"
                    + " AND TNU_EJE=" + datosExp[0]
                    + " AND TNU_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_PRIMERO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'";

            log.debug("sql: " + query);
            st = con.createStatement();
            insertado = insertado + st.executeUpdate(query);

            // segundo pago
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_TNU, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " SET TNU_VALOR=" + datosEcon.getImporteSegundoPago()
                    + " WHERE TNU_MUN=" + codOrganizacion
                    + " AND TNU_NUM='" + numExpediente + "'"
                    + " AND TNU_EJE=" + datosExp[0]
                    + " AND TNU_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide61.CAMPO_IMPORTE_SEGUNDO, ConstantesMeLanbide61.FICHERO_PROPIEDADES) + "'";

            log.debug("sql: " + query);
            st = con.createStatement();
            insertado = insertado + st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error actualizando el importe total de la subvención y el porcentaje del primer pago del expediente : " + numExpediente, ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return insertado;
    }
    private Integer getNextId(String seqName, Connection con) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numSec = rs.getInt(1);
                if (rs.wasNull()) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
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
        return numSec;
    }

   
    
    /**
     * Recupera los datos de SubSolic del expediente
     *
     * @param numExp
     * @param codOrganizacion
     * @param con
     * @return
     * @throws Exception
     */
    public List<SubSolicVO> getDatosSubSolic(String numExp, int codOrganizacion, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        SubSolicVO subSolic = new SubSolicVO();
        try {
            String query = null;
            query = "SELECT * FROM " + es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_SUBSOLIC, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP=? ORDER BY ID";
            log.debug("sql = " + query);
            pst = con.prepareStatement(query);
            pst.setString(1, numExp);
            log.debug("PARÁMETROS: " + numExp);
            rs = pst.executeQuery();
            while (rs.next()) {
                subSolic = (SubSolicVO) MeLanbide61MappingUtils.getInstance().map(rs, SubSolicVO.class);
                lista.add(subSolic);
                subSolic = new SubSolicVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando SubSolic ", ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     * Recupera una fila de SubSolic por ID
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public SubSolicVO getSubSolicPorID(String id, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        SubSolicVO subSolic = new SubSolicVO();
        try {
            String query = null;
            query = "SELECT * FROM " + es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_SUBSOLIC, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " WHERE ID=?";
            log.debug("sql = " + query);
            pst = con.prepareStatement(query);
            pst.setString(1, id);
            log.debug("PARÁMETROS: " + id);
            rs = pst.executeQuery();
            while (rs.next()) {
                subSolic = (SubSolicVO) MeLanbide61MappingUtils.getInstance().map(rs, SubSolicVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una SubSolic : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return subSolic;
    }

    /**
     * Operacion que elimina un registro
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarSubSolic(String id, Connection con) throws Exception {
        PreparedStatement pst = null;
        String query = null;
        try {
            query = "DELETE FROM " + es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_SUBSOLIC, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " WHERE ID=?";
            log.debug("sql = " + query);
            pst = con.prepareStatement(query);
            pst.setString(1, id);
            log.debug("PARÁMETROS: " + id);
            return pst.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando SubSolic ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     * Operacion que crea un nuevo registro
     *
     * @param nuevoSubSolic
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevoSubSolic(SubSolicVO nuevoSubSolic, Connection con) throws Exception {
        PreparedStatement pst = null;
        String query = "";
        try {
            int i = 1;
            int id = getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide61.SEQ_SUBSOLIC, ConstantesMeLanbide61.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_SUBSOLIC, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (?,?,?,?,?,?,?)";
            pst = con.prepareStatement(query);
            pst.setInt(i++, id);
            pst.setString(i++, nuevoSubSolic.getNumExp());
            pst.setString(i++, nuevoSubSolic.getEstado());
            pst.setString(i++, nuevoSubSolic.getOrganismo());
            pst.setString(i++, nuevoSubSolic.getObjeto());
            pst.setDouble(i++, nuevoSubSolic.getImporte());
            pst.setDate(i++, nuevoSubSolic.getFecha());
            log.debug("sql = " + query);
            log.debug("PARÁMETROS: Id- " + nuevoSubSolic.getId() + " NumExp- " + nuevoSubSolic.getNumExp() + " Estado- " + nuevoSubSolic.getEstado() + " Organismo- " + nuevoSubSolic.getOrganismo() + " Objeto- " + nuevoSubSolic.getObjeto() + " Importe- " + nuevoSubSolic.getImporte() + " Fecha- " + nuevoSubSolic.getFecha());
            return pst.executeUpdate() > 0;
        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar una nueva SubSolic" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     * Operacion que modifica registro
     *
     * @param datModif
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarSubSolic(SubSolicVO datModif, Connection con) throws Exception {
        PreparedStatement pst = null;
        String query = "";
        try {
            int i = 1;
            query = "UPDATE " + es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_SUBSOLIC, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " SET ESTADO=?, ORGANISMO=?, OBJETO=?, IMPORTE=?, FECHA=? WHERE ID=? AND NUM_EXP=?";
            pst = con.prepareStatement(query);
            pst.setString(i++, datModif.getEstado());
            pst.setString(i++, datModif.getOrganismo());
            pst.setString(i++, datModif.getObjeto());
            pst.setDouble(i++, datModif.getImporte());
            pst.setDate(i++, datModif.getFecha());
            pst.setInt(i++, datModif.getId());
            pst.setString(i++, datModif.getNumExp());
            log.debug("sql = " + query);
            log.debug("PARÁMETROS: estado- " + datModif.getEstado() + " Organismo- " + datModif.getOrganismo() + " Objeto- " + datModif.getObjeto() + " Importe- " + datModif.getImporte() + " Fecha- " + datModif.getFecha() + " Id- " + datModif.getId() + " NumExp- " + datModif.getNumExp());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al modificar una SubSolic - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
    public List<DesplegableVO> getValoresDesplegables(String des_cod, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<DesplegableVO> lista = new ArrayList<DesplegableVO>();
        DesplegableVO valoresDesplegable = new DesplegableVO();
        try {
            String query = null;
            query = "SELECT * FROM " + es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter.getParameter(ConstantesMeLanbide61.TABLA_E_DES_VAL, ConstantesMeLanbide61.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD=? order by DES_NOM";
            pst = con.prepareStatement(query);
            log.debug("sql = " + query);
            pst.setString(1, des_cod);
            log.debug("PARÁMETROS: " + des_cod);
            rs = pst.executeQuery();
            while (rs.next()) {
                valoresDesplegable = (DesplegableVO) MeLanbide61MappingUtils.getInstance().map(rs, DesplegableVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }
    public boolean esTramiteAbierto(String numExpediente, String codTramite, String codProcedimiento, Connection conexion) throws Exception {
        CallableStatement cstmt = null;
        int resultado = -1;
        boolean esAbierto = false;
        try {
            log.debug("Llamada a la funcion SQL ES_TRAMITE_ABIERTO " + codProcedimiento + " tramite " + codTramite + " expediente " + numExpediente);
            cstmt = conexion.prepareCall("CALL ES_TRAMITE_ABIERTO(?,?,?) INTO ?");
            cstmt.setString(1, numExpediente);
            cstmt.setString(2, codProcedimiento);
            cstmt.setString(3, codTramite);
            cstmt.registerOutParameter(4, Types.INTEGER);
            cstmt.executeUpdate();
            resultado = cstmt.getInt(4);
            log.debug("Resultado de la llamada a la funcion SQL ES_TRAMITE_ABIERTO " + codProcedimiento + " tramite " + codTramite + " Resultado: " + resultado);
        } catch (Exception e) {
            log.error("Se ha producido un error llamando a la funcion SQL ES_TRAMITE_ABIERTO, tramite: " + codTramite, e);
            throw new Exception(e);
        } finally {
            if (cstmt != null) {
                cstmt.close();
            }
            if (resultado == -1) {
                log.error("La funcion SQL ES_TRAMITE_ABIERTO, tramite: " + codTramite + " ha devuelto un resultado invalido.");
            }
        }
        if (resultado == 1) {
            esAbierto = true;
        }
        return esAbierto;
    }
}