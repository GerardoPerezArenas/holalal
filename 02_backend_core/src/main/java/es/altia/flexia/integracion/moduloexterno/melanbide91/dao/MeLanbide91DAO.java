package es.altia.flexia.integracion.moduloexterno.melanbide91.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide91.manager.MeLanbide91Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConstantesMeLanbide91;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.MeLanbide91MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.ContrGenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.SubvenSolicVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide91DAO {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide91DAO.class);
    //Instancia
    private static MeLanbide91DAO instance = null;

    // Constructor
    private MeLanbide91DAO() {

    }

    public static MeLanbide91DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide91DAO.class) {
                instance = new MeLanbide91DAO();
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
    public List<ContrGenVO> getListaContrGen(String numExp, int codOrganizacion, int idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ContrGenVO> lista = new ArrayList<ContrGenVO>();
        ContrGenVO acceso = new ContrGenVO();
        try {
            String query = null;
            query = "select ctr.*, TJOR.DES_NOM DES_NOM_TJOR, SEXO.DES_NOM DES_NOM_SEXO "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES) + " ctr"
                    + " LEFT JOIN e_des_val TJOR ON TJOR.des_cod = 'TJOR' AND TJOR.des_val_cod = ctr.jornada "
                    + " left join E_DES_VAL SEXO on SEXO.des_cod='SEXO' and SEXO.des_val_cod=ctr.sexo "                    
                    + " where ctr.num_exp ='" + numExp + "'"
                    + " order by ctr.id";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            log.debug("Antes del rs");
            while (rs.next()) {
                acceso = (ContrGenVO) MeLanbide91MappingUtils.getInstance().map(rs, ContrGenVO.class);

                acceso.setDesSexo(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma,rs.getString("DES_NOM_SEXO")));
                acceso.setDesJorn(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma, rs.getString("DES_NOM_TJOR")));

                acceso.setDesSexo(rs.getString("DES_NOM_SEXO"));
                acceso.setDesJorn(rs.getString("DES_NOM_TJOR"));

                log.debug("getListaContrGen -> numExp: " + acceso.getNumExp());
                log.debug("getListaContrGen 2 -> numExp: " + acceso.getNumExp());
                lista.add(acceso);
                acceso = new ContrGenVO();
            }
            log.debug("Despues del rs 222222");
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        log.debug("SALGO");
        return lista;
    }
 	
 	/**
     *
     * @param numExp
     * @param codOrganizacion
     * @param con
     * @return
     * @throws Exception
     */
    public List<SubvenSolicVO> getListaSubvencion(String numExp, int codOrganizacion, int idioma, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<SubvenSolicVO> lista = new ArrayList<SubvenSolicVO>();
        SubvenSolicVO acceso = new SubvenSolicVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES) + " ctr"
                    + " left join E_DES_VAL E_DES_VAL on e_des_val.des_cod='TDAT' and e_des_val.des_val_cod=ctr.tipodatos "
                    + " where ctr.num_exp ='" + numExp + "'"
                    + " order by ctr.id";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            log.debug("Antes del rs");

            while (rs.next()) {
                log.debug("dentro del while");
                acceso = (SubvenSolicVO) MeLanbide91MappingUtils.getInstance().map(rs, SubvenSolicVO.class);

                acceso.setDesTipoDatos(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma, rs.getString("DES_NOM")));


                acceso.setDesTipoDatos(rs.getString("DES_NOM"));

                log.debug("getListaSubvencion -> id: " + acceso.getId());
                log.debug("getListaSubvencion -> numExp: " + acceso.getNumExp());
                lista.add(acceso);
                acceso = new SubvenSolicVO();
            }
            log.debug("Despues del rs 333333");
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("SALGO");
        return lista;
    }

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public ContrGenVO getContrGenPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ContrGenVO acceso = new ContrGenVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES) + " ctr"
                    + " WHERE ctr.ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                acceso = (ContrGenVO) MeLanbide91MappingUtils.getInstance().map(rs, ContrGenVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una acceso : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return acceso;
    }

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public SubvenSolicVO getSubvencionPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        SubvenSolicVO acceso = new SubvenSolicVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES) + " ctr"
                    + " WHERE ctr.ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                acceso = (SubvenSolicVO) MeLanbide91MappingUtils.getInstance().map(rs, SubvenSolicVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una acceso : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return acceso;
    }

    /**
     *
     * @param nuevoSubvenSolic
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearSubvenSolic(SubvenSolicVO nuevoSubvenSolic, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide91.SEQ_MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + "( ID, NUM_EXP, TIPODATOS, TIPO, FECHA, DESTINO, COSTE)"
                    + " VALUES(?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
              log.debug("sql id = " + id);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevoSubvenSolic.getNumExp());
            ps.setString(contBD++, nuevoSubvenSolic.getTipoDatos());
            ps.setString(contBD++, nuevoSubvenSolic.getTipo());
            if (nuevoSubvenSolic.getFecha() != null) {
                ps.setDate(contBD++, new java.sql.Date(nuevoSubvenSolic.getFecha().getTime()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DATE);
            }

            ps.setString(contBD++, nuevoSubvenSolic.getDestino());
            ps.setDouble(contBD++, nuevoSubvenSolic.getCoste());
            

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido guardar un nuevo acceso en " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar un nuevo acceso" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param nuevoSubvenSolic
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarSubvenSolic(SubvenSolicVO nuevoSubvenSolic, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " SET TIPODATOS=?, "
                    + "TIPO=?, "
                    + "FECHA=?,"
                    + "DESTINO=?,"
                    + "COSTE=?,"
                    + " WHERE NUM_EXP = ? AND ID = ?";

            log.debug("sql = " + query);

            ps = con.prepareStatement(query);

            ps.setString(contBD++, nuevoSubvenSolic.getTipoDatos());
            ps.setString(contBD++, nuevoSubvenSolic.getTipo());
            if (nuevoSubvenSolic.getFecha() != null) {
                ps.setDate(contBD++, new java.sql.Date(nuevoSubvenSolic.getFecha().getTime()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DATE);
            }

            ps.setString(contBD++, nuevoSubvenSolic.getDestino());
            ps.setDouble(contBD++, nuevoSubvenSolic.getCoste());
            ps.setString(contBD++, nuevoSubvenSolic.getNumExp());
            ps.setInt(contBD++, nuevoSubvenSolic.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("Se ha producido un error al Modificar SubvenSolic - " + nuevoSubvenSolic.getId() + " - " + nuevoSubvenSolic.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar SubvenSolic - " + nuevoSubvenSolic.getId() + " - " + nuevoSubvenSolic.getNumExp() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param nuevoContrGen
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearContrGen(ContrGenVO nuevoContrGen, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide91.SEQ_MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + "( ID, NUM_EXP, NOMBRE, APELLIDO1, APELLIDO2, SEXO, DNI, PSIQUICA, FISICA, SENSORIAL, FECINI, JORNADA, PORCPARCIAL)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevoContrGen.getNumExp());
            ps.setString(contBD++, nuevoContrGen.getNombre());
            ps.setString(contBD++, nuevoContrGen.getApellido1());
            ps.setString(contBD++, nuevoContrGen.getApellido2());
            ps.setString(contBD++, nuevoContrGen.getSexo());
            ps.setString(contBD++, nuevoContrGen.getDni());
            
            if (nuevoContrGen.getPsiquica() != null) {
                ps.setDouble(contBD++, nuevoContrGen.getPsiquica());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }                      
            
             if (nuevoContrGen.getFisica() != null) {
                ps.setDouble(contBD++, (nuevoContrGen.getFisica()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }
             
            if (nuevoContrGen.getSensorial() != null) {
                ps.setDouble(contBD++, (nuevoContrGen.getSensorial()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }
            
             if (nuevoContrGen.getFecIni() != null) {
                ps.setDate(contBD++, new java.sql.Date(nuevoContrGen.getFecIni().getTime()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DATE);
            }
             
            ps.setString(contBD++, nuevoContrGen.getJornada());
            
            if (nuevoContrGen.getPorcParcial() != null) {
            ps.setDouble(contBD++, (nuevoContrGen.getPorcParcial()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }
           
            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido guardar un nuevo acceso en " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar un nuevo acceso" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param nuevoContrGen
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarContrGen(ContrGenVO nuevoContrGen, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " SET NOMBRE=?, "
                    + "APELLIDO1=?, "
                    + "APELLIDO2=?, "
                    + "SEXO=?, "
                    + "DNI=?,"
                    + "PSIQUICA=?,"
                    + "FISICA=?,"
                    + "SENSORIAL=?,"
                    + "FECINI=?,"
                    + "JORNADA=?,"
                    + "PORCPARCIAL=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";

            log.debug("sql = " + query);

            ps = con.prepareStatement(query);

            ps.setString(contBD++, nuevoContrGen.getNombre());
            ps.setString(contBD++, nuevoContrGen.getApellido1());
            ps.setString(contBD++, nuevoContrGen.getApellido2());
            ps.setString(contBD++, nuevoContrGen.getSexo());
            ps.setString(contBD++, nuevoContrGen.getDni());
            
            if (nuevoContrGen.getPsiquica() != null) {
                ps.setDouble(contBD++, nuevoContrGen.getPsiquica());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }                      
            
            if (nuevoContrGen.getFisica() != null) {
                ps.setDouble(contBD++, (nuevoContrGen.getFisica()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }
             
            if (nuevoContrGen.getSensorial() != null) {
                ps.setDouble(contBD++, (nuevoContrGen.getSensorial()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }           
            
            if(nuevoContrGen.getFecIni()!=null){
                 ps.setDate(contBD++, new java.sql.Date(nuevoContrGen.getFecIni().getTime())); 
            }else{
                ps.setNull(contBD++,java.sql.Types.DATE);
            }
            ps.setString(contBD++, nuevoContrGen.getJornada());
            
             if (nuevoContrGen.getPorcParcial() != null) {
                ps.setDouble(contBD++, (nuevoContrGen.getPorcParcial()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }    
                        
            ps.setString(contBD++, nuevoContrGen.getNumExp());
            ps.setInt(contBD++, nuevoContrGen.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("Se ha producido un error al Modificar ContrGen - " + nuevoContrGen.getId() + " - " + nuevoContrGen.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar ContrGen - " + nuevoContrGen.getId() + " - " + nuevoContrGen.getNumExp() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    public int maxId(Connection con) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        int maxId = 0;
        log.debug("dentro de sql MAXID .");
        try {
            String query = null;
            query = "SELECT MAX(ID) AS PROXID FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES) + " ctr";
            if (log.isDebugEnabled()) {
                log.debug("sql MAXID = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            log.debug("Antes del rs");
            while (rs.next()) {
                log.debug("maximo id: " + rs.getInt("PROXID"));
                maxId = rs.getInt("PROXID");
            }
            log.debug("Despues del rs 222222");
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        log.debug("SALGO");
        return maxId;

    }

    /**
     *
     * @param id
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarContrGen(String id, String numExp, Connection con) throws Exception {
        String query = null;
        Statement st = null;
        int resultado = 0;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_CONTR_GEN, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado = st.executeUpdate(query);

        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Contratación ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param id
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarSubvenSolicVO(String id, String numExp, Connection con) throws Exception {
        String query = null;
        Statement st = null;
        int resultado = 0;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado = st.executeUpdate(query);

        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Contratación ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param nuevoContrGen
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearnuevoSubvenSolic(SubvenSolicVO nuevonSubvenSolic, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide91.SEQ_MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + "( ID, NUM_EXP, TIPODATOS, TIPO, FECHA, DESTINO, COSTE )"
                    + " VALUES(?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevonSubvenSolic.getNumExp());
            ps.setString(contBD++, nuevonSubvenSolic.getTipoDatos());
            ps.setString(contBD++, nuevonSubvenSolic.getTipo());
            
            if (nuevonSubvenSolic.getFecha() != null) {
                ps.setDate(contBD++, new java.sql.Date(nuevonSubvenSolic.getFecha().getTime()));
            } else {
                ps.setNull(contBD++, java.sql.Types.DATE);
            }
            
            ps.setString(contBD++, nuevonSubvenSolic.getDestino());
            
            if (nuevonSubvenSolic.getCoste() != null) {
                ps.setDouble(contBD++, nuevonSubvenSolic.getCoste());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }      
           
            
            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido guardar un nuevo acceso en " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar un nuevo acceso" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param nuevoContrGen
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarSubvencion(SubvenSolicVO nuevoSubvenSolic, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " SET NUM_EXP=?, "
                    + "TIPODATOS=?, "
                    + "TIPO=?, "
                    + "FECHA=?,"
                    + "DESTINO=?,"
                    + "COSTE=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";

            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
        
            ps.setString(contBD++, nuevoSubvenSolic.getNumExp());
            ps.setString(contBD++, nuevoSubvenSolic.getTipoDatos());
            ps.setString(contBD++, nuevoSubvenSolic.getTipo());
            if(nuevoSubvenSolic.getFecha()!=null){
                 ps.setDate(contBD++, new java.sql.Date(nuevoSubvenSolic.getFecha().getTime())); 
            }else{
                ps.setNull(contBD++,java.sql.Types.DATE);
            }
           
            ps.setString(contBD++, nuevoSubvenSolic.getDestino());
            if (nuevoSubvenSolic.getCoste() != null) {
                ps.setDouble(contBD++, nuevoSubvenSolic.getCoste());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }
            
            ps.setString(contBD++, nuevoSubvenSolic.getNumExp());
            ps.setInt(contBD++, nuevoSubvenSolic.getId());
            
            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("Se ha producido un error al Modificar la subvencion - " + nuevoSubvenSolic.getId() + " - " + nuevoSubvenSolic.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar la subvencion - " + nuevoSubvenSolic.getId() + " - " + nuevoSubvenSolic.getNumExp() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param id
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarSubvencion(String id, String numExp, Connection con) throws Exception {
        String query = null;
        Statement st = null;
        int resultado = 0;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide91.MELANBIDE91_SUBVEN_SOLIC, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado = st.executeUpdate(query);

        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Contratación ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return resultado;
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
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el número de ID para inserción al llamar la secuencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
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
    public List<DesplegableVO> getValoresDesplegables(String des_cod, Connection con) throws Exception {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<DesplegableVO> lista = new ArrayList<DesplegableVO>();
        DesplegableVO valoresDesplegable = new DesplegableVO();
        try {
            String query = null;
            query = "SELECT * FROM " + es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter.getParameter(ConstantesMeLanbide91.TABLA_E_DES_VAL, ConstantesMeLanbide91.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD=? order by DES_NOM";
            pst = con.prepareStatement(query);
            log.debug("sql = " + query);
            pst.setString(1, des_cod);
            log.debug("PARÁMETROS: " + des_cod);
            rs = pst.executeQuery();
            while (rs.next()) {
                valoresDesplegable = (DesplegableVO) MeLanbide91MappingUtils.getInstance().map(rs, DesplegableVO.class);
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

}
