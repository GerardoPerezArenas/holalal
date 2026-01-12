package es.altia.flexia.integracion.moduloexterno.melanbide84.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConstantesMeLanbide84;
import es.altia.flexia.integracion.moduloexterno.melanbide84.util.MeLanbide84MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.PersonaVO;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide84DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide84DAO.class);

    //Instancia
    private static MeLanbide84DAO instance = null;

    // Constructor
    private MeLanbide84DAO() {

    }

    public static MeLanbide84DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide84DAO.class) {
                instance = new MeLanbide84DAO();
            }
        }
        return instance;
    }

    /**
     *
     * @param numExp
     * @param codOrganizacion
     * @param con
     * @return
     * @throws Exception
     */
    public List<PersonaVO> getListaPersonas(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        PersonaVO persona = new PersonaVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.MELANBIDE84_PERSONAS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (PersonaVO) MeLanbide84MappingUtils.getInstance().map(rs, PersonaVO.class);
                lista.add(persona);
                persona = new PersonaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Personas ");
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public PersonaVO getPersonaPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        PersonaVO persona = new PersonaVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.MELANBIDE84_PERSONAS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (PersonaVO) MeLanbide84MappingUtils.getInstance().map(rs, PersonaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una persona : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return persona;
    }

    /**
     *
     * @param nuevaPersona
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevaPersona(PersonaVO nuevaPersona, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide84.SEQ_MELANBIDE84_PERSONAS, ConstantesMeLanbide84.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.MELANBIDE84_PERSONAS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, DNI, PROGRAMA, NOMBRE, APEL1, APEL2, CALLE, NUMERO, LETRA, PLANTA, PUERTA, CP, LOCALIDAD, NIVELACADEMICO, FECHANAC, FECHAINICIO, FECHASOLICITUD, SEXO)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevaPersona.getExpEntap());
            ps.setString(contBD++, nuevaPersona.getDni());
            ps.setString(contBD++, nuevaPersona.getPrograma().toUpperCase());
            ps.setString(contBD++, nuevaPersona.getNombre());
            ps.setString(contBD++, nuevaPersona.getApel1());
            if (nuevaPersona.getApel2() != null) {
                ps.setString(contBD++, nuevaPersona.getApel2());
            } else {
                ps.setNull(contBD++, java.sql.Types.VARCHAR);
            }
            ps.setString(contBD++, nuevaPersona.getCalle());
            ps.setInt(contBD++, nuevaPersona.getNumero());
            ps.setString(contBD++, nuevaPersona.getLetra());
            ps.setString(contBD++, nuevaPersona.getPlanta());
            ps.setString(contBD++, nuevaPersona.getPuerta());
            ps.setInt(contBD++, nuevaPersona.getCodPost());
            ps.setString(contBD++, nuevaPersona.getLocalidad());
            ps.setString(contBD++, nuevaPersona.getNivelAcademico());
            ps.setDate(contBD++, nuevaPersona.getFechaNacimiento());
            ps.setDate(contBD++, nuevaPersona.getFechaInicio());
            ps.setDate(contBD++, nuevaPersona.getFechaSolicitud());
            ps.setInt(contBD++, nuevaPersona.getSexo());
            if (ps.executeUpdate() > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva Persona ");
                return false;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error al Insertar una nueva Persona " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param datModif
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarPersona(PersonaVO datModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.MELANBIDE84_PERSONAS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " SET DNI = ?, PROGRAMA=?, NOMBRE = ?, APEL1=?, APEL2=?, CALLE=?, NUMERO=?, LETRA=?, PLANTA=?, PUERTA=?, CP=?, LOCALIDAD=?,"
                    + " NIVELACADEMICO=?, FECHANAC=?, FECHAINICIO=?, FECHASOLICITUD=?, SEXO=? "
                    + " WHERE NUM_EXP = ? AND ID = ?";

            ps = con.prepareStatement(query);
            ps.setString(contBD++, datModif.getDni());
            ps.setString(contBD++, datModif.getPrograma().toUpperCase());
            ps.setString(contBD++, datModif.getNombre());
            ps.setString(contBD++, datModif.getApel1());
            ps.setString(contBD++, datModif.getApel2());
            ps.setString(contBD++, datModif.getCalle());
            ps.setInt(contBD++, datModif.getNumero());
            ps.setString(contBD++, datModif.getLetra());
            ps.setString(contBD++, datModif.getPlanta());
            ps.setString(contBD++, datModif.getPuerta());
            ps.setInt(contBD++, datModif.getCodPost());
            ps.setString(contBD++, datModif.getLocalidad());
            ps.setString(contBD++, datModif.getNivelAcademico());
            ps.setDate(contBD++, datModif.getFechaNacimiento());
            ps.setDate(contBD++, datModif.getFechaInicio());
            ps.setDate(contBD++, datModif.getFechaSolicitud());
            ps.setInt(contBD++, datModif.getSexo());
            ps.setString(contBD++, datModif.getExpEntap());
            ps.setInt(contBD++, datModif.getId());

            log.debug("sql = " + query);
            if (ps.executeUpdate() > 0) {
                return true;
            } else {
                log.error("Se ha producido un error al Modificar una persona - " + datModif.getId() + " - " + datModif.getExpEntap());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una persona - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarPersona(String id, Connection con) throws Exception {
        String query = null;
        Statement st = null;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.MELANBIDE84_PERSONAS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Persona ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     * Método que comprueba si el expediente se encuentra en el tramite
     * GENERACION EXPEDIENTES APEI/APEA
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return true/false
     * @throws Exception
     */
    public boolean estaEnTramite110(int codOrg, String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = "SELECT COUNT(1) EXISTE"
                    + " FROM E_CRO "
                    + " WHERE "
                    + " CRO_MUN=? "
                    + " AND CRO_NUM=? "
                    + " AND CRO_TRA=? "
                    + " AND CRO_FEF IS NULL ";
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, numExp);
            ps.setString(3, ConfigurationParameter.getParameter(ConstantesMeLanbide84.CODIGO_TRAMITE110, ConstantesMeLanbide84.FICHERO_PROPIEDADES));
            rs = ps.executeQuery();
            if (rs.next()) {
                existe = (rs.getInt("EXISTE") > 0);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar el trámite - " + e);
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

    /**
     *
     * @param sequence
     * @param con
     * @return
     * @throws Exception
     */
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ");
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

    /**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegabe = new DesplegableAdmonLocalVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide84MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegabe);
                valoresDesplegabe = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un valores de un desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public ExpedienteVO getDatosExpediente(int codMunicipio, String numExp, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        ExpedienteVO exp = null;
        try {
            String query = "select distinct exp.EXP_PRO PRO, exp.EXP_EJE EJE, exp.EXP_NUM NUM, exp.EXP_MUN MUN, exp.EXP_UOR UOR, exp.EXP_OBS OBS, exp.EXP_ASU ASU,exp.EXP_USU USU,"
                    + " exr.EXR_DEP DEP,"
                    + " res.RES_TDO TDO, res.RES_NTR NTR, res.RES_AUT AUT,ecro.cro_utr utr"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_E_EXP, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " exp"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_E_EXR, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " exr"
                    + " on exp.exp_pro = exr.exr_pro and exp.exp_eje = exr.exr_eje and exp.exp_num = exr.exr_num"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_R_RES, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " res"
                    + " on exr.exr_nre = res.res_num and exr.exr_eje = res.res_eje"
                    + " left join E_cro ecro on ecro.CRO_NUM=exp.exp_num and  exp.EXP_EJE=ecro.CRO_EJE"
                    + " where exp.EXP_MUN = ? and exp.EXP_NUM = ?";
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setString(2, numExp);
            rs = st.executeQuery();
            if (rs.next()) {
                exp = (ExpedienteVO) MeLanbide84MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return exp;
    }

    public IdExpedienteVO getDatosIdExpediente(String numExpediente, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = null;
        IdExpedienteVO idexpediente = null;
        try {
            query = "SELECT EXP_EJE,EXP_PRO,EXP_NUM,EXP_TRA FROM E_EXP WHERE EXP_NUM='" + numExpediente + "'";
            log.debug("sql = " + query);
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                idexpediente = (IdExpedienteVO) MeLanbide84MappingUtils.getInstance().map(rs, IdExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando getDatosIdExpediente el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return idexpediente;
    }

    public int getcodigoUOR(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        int respuesta = 0;
        try {
            query = "select CRO_UTR from E_CRO where cro_pro='" + procedimientoPadre + "' AND CRO_NUM='" + numExpediente + "' and CRO_TRA=" + codTramite;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                respuesta = rs.getInt("CRO_UTR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la UNIDAD del expediente:  ");
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return respuesta;
    }

    /**
     *
     * @param codOrganizacion
     * @param numExpediente
     * @param procedimientoPadre
     * @param codTramite
     * @param con
     * @return
     * @throws Exception
     */
    public int getUsuarioExpediente(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        int respuesta = 0;
        try {
            query = "select CRO_USU from E_CRO where cro_pro='" + procedimientoPadre + "' AND CRO_NUM='" + numExpediente + "' and CRO_TRA=" + codTramite;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                respuesta = rs.getInt("CRO_USU");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el USUARIO del expediente ");
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return respuesta;
    }

    /**
     * Método que comprueba si una persona está registrada en T_TER
     *
     * @param ter
     * @param con
     * @return El codigo del tercero, 0 si no existe
     * @throws Exception
     */
    public int existeTercero(TerceroVO ter, Connection con) throws Exception {
        //TerceroVO(String ap1, String ap2, String doc, DireccionVO domicilio, String email, String nombre, String telefono, String tipoDoc)
        log.info("EXISTE  TERCERO");
        int cod = 0;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "select TER_COD, TER_DOC, TER_NOM, TER_AP1, TER_AP2, TER_DOM "
                    + " from T_TER "
                    + "where 1=1";

            if (ter != null) {
                if (ter.getDoc() != null && !ter.getDoc().equalsIgnoreCase("")) {
                    sql += " and upper(TER_DOC) like '" + ter.getDoc().toUpperCase() + "%'";
                }
//                if (ter.getNombre() != null && !ter.getNombre().equalsIgnoreCase("")) {
//                    sql += " and upper(TER_NOM) like '%" + ter.getNombre().toUpperCase() + "%'";
//                }
//                if (ter.getAp1() != null && !ter.getAp1().equalsIgnoreCase("")) {
//                    sql += " and upper(TER_AP1) like '%" + ter.getAp1().toUpperCase() + "%'";
//                }
//                if (ter.getAp2() != null && !ter.getAp2().equalsIgnoreCase("")) {
//                    sql += " and upper(TER_AP2) like '%" + ter.getAp2().toUpperCase() + "%'";
//                }
            }
            sql += " order by ter_cod ";
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cod = rs.getInt("TER_COD");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando EXISTE TERCERO ");
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

    public List<InteresadoExpedienteVO> getDatosInteresado(int codOrganizacion, Integer ejercicio, String numExpediente, Connection con) throws SQLException, Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = null;
        TerceroVO tercero = null;
        InteresadoExpedienteVO interesado = null;
        List<InteresadoExpedienteVO> interesados = new ArrayList<InteresadoExpedienteVO>();

        try {
            query = "SELECT EXT_ROL FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE EXT_NUM='" + numExpediente + "' AND EXT_ROL=1";
            log.debug("sql = " + query);
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                interesado = (InteresadoExpedienteVO) MeLanbide84MappingUtils.getInstance().map(rs, InteresadoExpedienteVO.class);

                if (interesado != null) {
                    try {
                        tercero = getTercerosExpediente(codOrganizacion, ejercicio, numExpediente, con, rs.getString("EXT_ROL"));
                        interesado.setTercero(tercero);
                        interesados.add(interesado);
                    } catch (Exception ex) {
                        log.error("Se ha producido un error al recuperar el tercero  para el expediente " + numExpediente, ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return interesados;
    }

    public TerceroVO getTercerosExpediente(int codMunicipio, Integer ejercicio, String numExp, Connection con, String rol) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int historico = 0;
        String codTercero = null;
        int version = 0;
        TerceroVO tercero = null;
        DireccionVO domicilio = null;

        try {
            query = "SELECT h.hte_ter codigo,h.hte_doc documento, h.hte_nvr nvr, h.hte_tlf tlf, h.hte_dce dce, 1 as HISTORICO"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " rel"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCEROS_HIST, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " h on h.hte_ter = rel.ext_ter and h.hte_nvr = rel.ext_nvr"
                    + " where  ext_mun = ? and ext_eje = ? and ext_num = ? and ext_rol = ?";

            log.debug("sql = " + query);
            log.debug("VALORES DE getTercerosExpediente = " + codMunicipio + " " + ejercicio + " " + numExp + " " + rol);
            ps = con.prepareStatement(query);
            ps.setInt(1, codMunicipio);
            ps.setInt(2, ejercicio);
            ps.setString(3, numExp);
            ps.setString(4, rol);
            rs = ps.executeQuery();

            while (rs.next()) {
                codTercero = rs.getString("codigo");
                historico = rs.getInt("HISTORICO");
                version = rs.getInt("nvr");

                tercero = getTerceroHistorico(codMunicipio, ejercicio, numExp, codTercero, version, con);
                if (tercero != null) {
                    try {
                        domicilio = getDomicilioTerceroExp(tercero, codTercero, version, con);
                        tercero.setDomicilio(domicilio);
                    } catch (Exception ex) {
                        log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getDomicilio() + " para el expediente " + numExp, ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tercero;
    }

    public TerceroVO getTercero(int codMunicipio, String dni, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = null;
        String codTercero = null;
        int version = 0;
        TerceroVO tercero = null;
        DireccionVO domicilio = null;

        try {
            query = "SELECT * "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCEROS, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " ter"
                    + " where  ter_doc = ? and TER_FBJ is null order by TER_FAL desc, TER_COD, TER_NVE";

            log.debug("sql = " + query);
            st = con.prepareStatement(query);
            st.setString(1, dni);
            rs = st.executeQuery();

            if (rs.next()) {
                codTercero = rs.getString("TER_COD");
                version = rs.getInt("TER_NVE");

                tercero = getTerceroHist(codMunicipio, codTercero, version, con);
                if (tercero != null) {
                    try {
                        domicilio = getDomicilioTerceroExp(tercero, codTercero, version, con);
                        tercero.setDomicilio(domicilio);
                    } catch (Exception ex) {
                        log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getDomicilio(), ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros ");
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tercero;
    }

    private TerceroVO getTerceroHist(int codMunicipio, String codTercero, int version, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        TerceroVO ter = null;
        String query = null;
        try {
            query = "select "
                    + " HTE_TER COD, HTE_NVR VER, HTE_TID TID, HTE_DOC DOC, HTE_NOM NOM,"
                    + " HTE_AP1 AP1, HTE_PA1 PA1, HTE_AP2 AP2, HTE_PA2 PA2, HTE_NOC NOC, HTE_NML NML,"
                    + " HTE_TLF TLF, HTE_DCE DCE, HTE_APL APL, EXTERNAL_CODE EXTERNAL_CODE,"
                    + " null SIT, null FAL, null UAL, null FBJ, null UBJ, null DOM "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCEROS_HIST, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " h "
                    + " where h.hte_ter = ? and h.hte_nvr = ?";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);

            ps.setString(1, codTercero);
            log.info("codTercero: " + codTercero);
            ps.setInt(2, version);
            log.info("version: " + version);
            rs = ps.executeQuery();
            if (rs.next()) {
                ter = (TerceroVO) MeLanbide84MappingUtils.getInstance().map(rs, TerceroVO.class);
            }
            return ter;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando tercero " + codTercero, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private TerceroVO getTerceroHistorico(int codMunicipio, Integer ejercicio, String numExpediente, String codTercero, int version, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        TerceroVO ter = null;
        String query = null;
        try {
            query = "select "
                    + " HTE_TER COD, HTE_NVR VER, HTE_TID TID, HTE_DOC DOC, HTE_NOM NOM,"
                    + " HTE_AP1 AP1, HTE_PA1 PA1, HTE_AP2 AP2, HTE_PA2 PA2, HTE_NOC NOC, HTE_NML NML,"
                    + " HTE_TLF TLF, HTE_DCE DCE, HTE_APL APL, EXTERNAL_CODE EXTERNAL_CODE,"
                    + " null SIT, null FAL, null UAL, null FBJ, null UBJ, null DOM, EXT_ROL ROL"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCEROS_HIST, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " h"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " r"
                    + " on h.hte_ter = r.ext_ter and h.hte_nvr = r.ext_nvr"
                    + " where r.ext_mun = ? and r.ext_eje = ? and r.ext_num = ? and r.ext_ter = ? and r.ext_nvr = ?";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codMunicipio);
            ps.setInt(2, ejercicio);
            ps.setString(3, numExpediente);
            ps.setString(4, codTercero);
            log.info("codTercero: " + codTercero);
            ps.setInt(5, version);
            log.info("version: " + version);
            rs = ps.executeQuery();
            if (rs.next()) {
                ter = (TerceroVO) MeLanbide84MappingUtils.getInstance().map(rs, TerceroVO.class);
            }
            return ter;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando tercero " + codTercero, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Método que recupera los datos del domicilio que figura en la tabla de
     * terceros T_TER
     *
     * @param ter
     * @param codTercero
     * @param codDomicilio
     * @param con
     * @return DireccionVO: domicilio
     * @throws Exception
     */
    public DireccionVO getDomicilioTercero(TerceroVO ter, int codTercero, int codDomicilio, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DireccionVO dom = null;
        String query = null;
        try {
            query = "select dom.*,via.VIA_NOM "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCEROS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " left join  " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_DOMICILIO, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " dom on dnn_dom=ter_dom"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_T_VIA, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " via on dom.DNN_VIA = via.VIA_COD  and dom.DNN_PRV=via.VIA_PRV and dom.DNN_MUN=via.VIA_MUN "
                    + " where ter_cod=? "
                    + " and ter_dom=?";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);

            ps.setInt(1, codTercero);
            ps.setInt(2, codDomicilio);
            log.info("codigo tercero: " + codTercero);
            log.info("codigo Domicilio: " + codDomicilio);
            rs = ps.executeQuery();
            if (rs.next()) {
                dom = (DireccionVO) MeLanbide84MappingUtils.getInstance().map(rs, DireccionVO.class);
            }
            return dom;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando domicilio tercero ");
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Método que recupera los datos del domicilio que figura en E_EXT
     *
     * @param ter
     * @param codTercero
     * @param version
     * @param con
     * @return DireccionVO: domicilio
     * @throws Exception
     */
    private DireccionVO getDomicilioTerceroExp(TerceroVO ter, String codTercero, int version, Connection con) throws Exception {
        if (ter != null && codTercero != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            DireccionVO dom = null;
            String query = null;
            try {
                query = "select dom.*,via.VIA_NOM "
                        + "from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                        + " left join  " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_DOMICILIO, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " dom on dnn_dom=ext_dot"
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_T_VIA, ConstantesMeLanbide84.FICHERO_PROPIEDADES) + " via on dom.DNN_VIA = via.VIA_COD  and dom.DNN_PRV=via.VIA_PRV and dom.DNN_MUN=via.VIA_MUN "
                        + " where ext_ter=? and ext_nvr=?";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);

                ps.setString(1, codTercero);
                ps.setInt(2, version);
                log.info("codigo tercero: " + codTercero + " v." + version);
                rs = ps.executeQuery();
                if (rs.next()) {
                    dom = (DireccionVO) MeLanbide84MappingUtils.getInstance().map(rs, DireccionVO.class);
                }
                return dom;
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando domicilio tercero ");
                throw new Exception(ex);
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Método que comprueba si el tercero tiene un domicilio en T_TER
     *
     * @param codigoTerceroAPE
     * @param con
     * @return el codigo del domicilio del tercero
     * @throws Exception
     */
    public int existeDomicilioTer(int codigoTerceroAPE, Connection con) throws Exception {
        log.debug("EXISTE DOMICILIO TERCERO");
        int cod = 0;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "select TER_DOM "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCEROS, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where TER_COD=" + codigoTerceroAPE;
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                cod = rs.getInt("TER_DOM");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando EXISTE DOMICILIO TERCERO ");
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

    /**
     * Método que recupera los codigos del pais, provincia y municipio
     *
     * @param localidad
     * @param con
     * @return int[] con los 3 codigos
     * @throws Exception
     */
    public int[] getCodigosMunicipio(String localidad, Connection con) throws Exception {
        log.debug("getCodigosMunicipio");
        int codigos[] = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "select MUN_PAI, MUN_PRV, MUN_COD from flbgen.T_MUN where upper(MUN_NOM) like '%" + localidad.toUpperCase() + "%'";
            log.debug(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                codigos = new int[3];
                codigos[0] = rs.getInt("MUN_PAI");
                codigos[1] = rs.getInt("MUN_PRV");
                codigos[2] = rs.getInt("MUN_COD");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los códigos de Pais, Provincia y Municipio  ");
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return codigos;
    }

    /**
     * Método que recupera el codigo de Tipo de Via
     *
     * @param codPais
     * @param codProvincia
     * @param codMunicipio
     * @param nombreVia
     * @param con
     * @return int Tipo Via
     * @throws Exception
     */
    public int getTipoVia(int codPais, int codProvincia, int codMunicipio, String nombreVia, Connection con) throws Exception {
        log.debug("getTipoVia");
        int cod = 0;
        int contBBDD = 1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select VIA_TVI "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_T_VIA, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where VIA_PAI=? and VIA_PRV=? and VIA_MUN=? and upper(VIA_NOM) like ?";
            log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setInt(contBBDD++, codPais);
            ps.setInt(contBBDD++, codProvincia);
            ps.setInt(contBBDD++, codMunicipio);
            ps.setString(contBBDD++, "%" + nombreVia.toUpperCase() + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                cod = rs.getInt("VIA_TVI");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el tipo de Vía ");
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

    /**
     * Método que recupera el codigo del tercero del expediente con el rol
     * indicado
     *
     * @param codOrg
     * @param numExp
     * @param rol
     * @param con
     * @return
     * @throws Exception
     */
    public int getCodTerceroExp(int codOrg, String numExp, int rol, Connection con) throws Exception {
        log.debug("getCodTerceroExp " + numExp + " - Rol: " + rol);
        int cod = 0;
        int contBBDD = 1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select EXT_TER "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where EXT_MUN=? and EXT_EJE=? and EXT_NUM=? AND EXT_ROL=?";
            log.debug(sql);
            ps = con.prepareStatement(sql);
            ps.setInt(contBBDD++, codOrg);
            ps.setInt(contBBDD++, Integer.valueOf(numExp.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBBDD++, numExp);
            ps.setInt(contBBDD++, rol);
            rs = ps.executeQuery();

            if (rs.next()) {
                cod = rs.getInt("EXT_TER");
            }
            return cod;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código del 3º de E_EXT ");
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Método que comprueba si existe el suplementario de un tercero en la tabla
     * indicada
     *
     * @param codOrg
     * @param tabla
     * @param campo
     * @param codTercero
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeSuplementarioTercero(int codOrg, String tabla, String campo, int codTercero, Connection con) throws Exception {
        log.debug("existeSuplementarioTercero " + campo + " - " + codTercero);
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = "SELECT COUNT(1) EXISTE FROM  " + tabla
                    + " WHERE COD_MUNICIPIO=?  AND COD_CAMPO=?  AND COD_TERCERO=? ";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, campo);
            ps.setInt(3, codTercero);
            rs = ps.executeQuery();
            if (rs.next()) {
                existe = (rs.getInt("EXISTE") > 0);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar el trámite - " + e);
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

    /**
     * Método que graba el suplementario de un tercero en la tabla indicada
     *
     * @param codOrg
     * @param tabla
     * @param campo
     * @param codTercero
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarSuplementarioTercero(int codOrg, String tabla, String campo, int codTercero, String valor, Connection con) throws Exception {
        log.debug("grabarSuplementarioTercero " + campo + " - " + valor + " - " + codTercero);
        PreparedStatement ps = null;
        String query = "";
        int insert = 0;
        int contBD = 1;
        try {
            query = "INSERT INTO " + tabla
                    + " (COD_CAMPO, COD_MUNICIPIO, COD_TERCERO, VALOR) VALUES (?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, campo);
            ps.setInt(contBD++, codOrg);
            ps.setInt(contBD++, codTercero);
            ps.setString(contBD++, valor);
            insert = ps.executeUpdate();
            if (insert > 0) {
                return true;
            } else {
                log.error("Se ha producido un error al grabar " + campo);
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una tercero -  - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Método que actualiza el suplementario de un tercero en la tabla indicada
     *
     * @param codOrg
     * @param tabla
     * @param campo
     * @param codTercero
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public boolean actualizarSuplementarioTercero(int codOrg, String tabla, String campo, int codTercero, String valor, Connection con) throws Exception {
        log.debug("actualizarSuplementarioTercero " + campo);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + tabla
                    + " SET VALOR=? WHERE COD_CAMPO=? AND COD_MUNICIPIO=? AND COD_TERCERO=? ";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, valor);
            ps.setString(contBD++, campo);
            ps.setInt(contBD++, codOrg);
            ps.setInt(contBD++, codTercero);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una tercero -  - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Método que comprueba si existe el suplementario desplegable de Expediente
     *
     * @param codOrg
     * @param campo
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeSuplementarioDesplegable(int codOrg, String campo, String numExp, Connection con) throws Exception {
        log.debug("existeSuplementarioTercero " + campo);
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        int contBD = 1;
        try {
            String query = "SELECT COUNT(1) EXISTE FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE TDE_MUN=?  AND TDE_EJE=?  AND TDE_NUM=? AND TDE_COD=?";
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrg);
            ps.setInt(contBD++, Integer.valueOf(numExp.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBD++, numExp);
            ps.setString(contBD++, campo);
            rs = ps.executeQuery();
            if (rs.next()) {
                existe = (rs.getInt("EXISTE") > 0);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar el trámite - " + e);
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

    /**
     * Método que comprueba si existe el suplementario Fecha de Expediente
     *
     * @param codOrg
     * @param campo
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeSuplementarioFecha(int codOrg, String campo, String numExp, Connection con) throws Exception {
        log.debug("existeSuplementarioFecha " + campo);
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        int contBD = 1;
        try {
            String query = "SELECT COUNT(1) EXISTE FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_FECHAS_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " WHERE TFE_MUN=?  AND TFE_EJE=?  AND TFE_NUM=? AND TFE_COD=?";
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrg);
            ps.setInt(contBD++, Integer.valueOf(numExp.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBD++, numExp);
            ps.setString(contBD++, campo);
            rs = ps.executeQuery();
            if (rs.next()) {
                existe = (rs.getInt("EXISTE") > 0);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si existe el suplementario - " + campo, e);
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

    /**
     * Metodo que graba un suplementario en E_TFE
     *
     * @param codOrg
     * @param campo
     * @param numExp
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarSuplementarioFecha(int codOrg, String campo, String numExp, Date valor, Connection con) throws Exception {
        log.debug("grabarSuplementarioDesplegable " + campo + " - " + valor);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_FECHAS_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR) VALUES (?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrg);
            ps.setInt(contBD++, Integer.valueOf(numExp.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBD++, numExp);
            ps.setString(contBD++, campo);
            ps.setDate(contBD++, valor);
            int insert = ps.executeUpdate();
            if (insert > 0) {
                return true;
            } else {
                log.error("Se ha producido un error al grabar " + campo);
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un tercero - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Método que graba un suplementario desplegable de expediente
     *
     * @param codOrg
     * @param campo
     * @param numExp
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarSuplementarioDesplegable(int codOrg, String campo, String numExp, String valor, Connection con) throws Exception {
        log.debug("grabarSuplementarioDesplegable " + campo + " - " + valor);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " (TDE_MUN, TDE_EJE, TDE_NUM, TDE_COD, TDE_VALOR) VALUES (?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrg);
            ps.setInt(contBD++, Integer.valueOf(numExp.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBD++, numExp);
            ps.setString(contBD++, campo);
            ps.setString(contBD++, valor);
            int insert = ps.executeUpdate();
            if (insert > 0) {
                return true;
            } else {
                log.error("Se ha producido un error al grabar " + campo);
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un tercero - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Método recoge los expedientes del programa indicado en los que la entidad
     * sea representante
     *
     * @param codOrganizacion
     * @param codEntidad
     * @param programa
     * @param con
     * @return
     * @throws Exception
     */
    public List<String> getExpedientesEntidad(int codOrganizacion, int codEntidad, String programa, Connection con) throws Exception {
        log.debug("getExpedientesEntidad " + codEntidad + " - " + programa);
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> lista = new ArrayList<String>();
        String expediente = null;
        String query = null;
        int contBD = 1;
        try {
            query = "SELECT EXT_NUM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where EXT_MUN=? AND EXT_EJE=? AND EXT_PRO=? AND EXT_TER=? AND EXT_ROL=2";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrganizacion);
            ps.setInt(contBD++, Integer.valueOf(expediente.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBD++, programa);
            ps.setInt(contBD++, codEntidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                expediente = rs.getString("EXT_NUM");
                lista.add(expediente);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al buscar expedientes de un tercero - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     * Método que busca en un expediente del procedimiento indicado en el que
     * como interesado figura la persona
     *
     * @param codOrganizacion
     * @param codigoTerceroAPE
     * @param expediente
     * @param programa
     * @param con
     * @return
     * @throws Exception
     */
    public boolean getExpedientesInteresado(int codOrganizacion, int codigoTerceroAPE, String expediente, String programa, Connection con) throws Exception {
        log.debug("getExpedientesInteresado " + codigoTerceroAPE + " - " + expediente + " - " + programa);
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean existe = false;
        String query = null;
        int contBD = 1;
        try {
            query = "SELECT count(*) EXISTE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where EXT_MUN=? AND EXT_EJE=? AND EXT_NUM=? AND EXT_PRO=? AND EXT_TER=? AND EXT_ROL=1";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrganizacion);
            ps.setInt(contBD++, Integer.valueOf(expediente.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[0]));
            ps.setString(contBD++, expediente);
            ps.setString(contBD++, programa);
            ps.setInt(contBD++, codigoTerceroAPE);
            rs = ps.executeQuery();
            if (rs.next()) {
                existe = (rs.getInt("EXISTE") > 0);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al buscar expedientes de un tercero - " + ex);
            throw new Exception(ex);
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

    /**
     * Método recoge los expedientes del procedimiento indicado en el que la
     * Persona figure como interesado
     *
     * @param codOrganizacion
     * @param codigoTerceroAPE
     * @param eje
     * @param programa
     * @param con
     * @return
     * @throws Exception
     */
    public List<String> getExpedientesPersona(int codOrganizacion, int codigoTerceroAPE, String eje, String programa, Connection con) throws Exception {
        log.debug("getExpedientesPersona " + codigoTerceroAPE + " - " + eje + " - " + programa);
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> lista = new ArrayList<String>();
        String expediente = null;
        String query = null;
        int contBD = 1;
        try {
            query = "SELECT EXT_NUM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where EXT_MUN=? AND EXT_EJE=? AND EXT_PRO=? AND EXT_TER=? AND EXT_ROL=1";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, codOrganizacion);
            ps.setString(contBD++, eje);
            ps.setString(contBD++, programa);
            ps.setInt(contBD++, codigoTerceroAPE);
            rs = ps.executeQuery();
            while (rs.next()) {
                expediente = rs.getString("EXT_NUM");
                lista.add(expediente);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al buscar expedientes de un tercero - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     * Metodo que recoge de E_DES_VAL el codigo correspondiente a la Entidad
     *
     * @param codOrg
     * @param documento
     * @param con
     * @return
     * @throws Exception
     */
    public String getCodEntidad(int codOrg, String documento, Connection con) throws Exception {
        log.debug("getCodEntidad " + documento);
        String cod = "";
        int contBBDD = 1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select des_val_cod "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide84.FICHERO_PROPIEDADES)
                    + " where des_nom like ?  AND des_cod=?";
            log.debug(sql);
            ps = con.prepareStatement(sql);

            ps.setString(contBBDD++, documento + "-%");
            ps.setString(contBBDD++, ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_DES_ENTI, ConstantesMeLanbide84.FICHERO_PROPIEDADES));
            rs = ps.executeQuery();

            if (rs.next()) {
                cod = rs.getString("des_val_cod");
            }
            return cod;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código del desplegable de la entidad ");
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }
}
