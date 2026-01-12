package es.altia.flexia.integracion.moduloexterno.melanbide85.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConstantesMeLanbide85;
import es.altia.flexia.integracion.moduloexterno.melanbide85.util.MeLanbide85MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.FilaContratacionVO;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import java.sql.Blob;
import java.util.HashMap;

public class MeLanbide85DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide85DAO.class);
    //Instancia
    private static MeLanbide85DAO instance = null;

    // Constructor
    private MeLanbide85DAO() {

    }

    public static MeLanbide85DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide85DAO.class) {
                instance = new MeLanbide85DAO();
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
    public List<FilaContratacionVO> getListaContrataciones(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("Entramos en getListaContrataciones DAO - " + numExp);
        Statement st = null;
        ResultSet rs = null;
        List<FilaContratacionVO> lista = new ArrayList<FilaContratacionVO>();
        FilaContratacionVO contratacion = new FilaContratacionVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ctr"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ini"
                    + " on ini.num_exp=ctr.num_exp and ini.num_puesto=ctr.num_puesto"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " fin"
                    + " on fin.num_exp=ctr.num_exp and fin.num_puesto=ctr.num_puesto"
                    + " where ctr.num_exp ='" + numExp + "'"
                    + " order by ctr.id";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                contratacion = (FilaContratacionVO) MeLanbide85MappingUtils.getInstance().map(rs, FilaContratacionVO.class);
                lista.add(contratacion);
                contratacion = new FilaContratacionVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Contrataciones ", ex);
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
    public FilaContratacionVO getContratacionPorID(String id, Connection con) throws Exception {
        log.info("Entramos en getContratacionPorID DAO - " + id);
        Statement st = null;
        ResultSet rs = null;
        FilaContratacionVO contratacion = new FilaContratacionVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ctr"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ini"
                    + " on ini.num_exp=ctr.num_exp and ini.num_puesto=ctr.num_puesto"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " fin"
                    + " on fin.num_exp=ctr.num_exp and fin.num_puesto=ctr.num_puesto"
                    + " WHERE ctr.ID=" + (id != null && !id.isEmpty() ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                contratacion = (FilaContratacionVO) MeLanbide85MappingUtils.getInstance().map(rs, FilaContratacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Contratación : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return contratacion;
    }

    /**
     *
     * @param nuevaContratacion
     * @param tablas
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearContratacion(FilaContratacionVO nuevaContratacion, int tablas, Connection con) throws Exception {
        log.info("Entramos en crearContratación DAO - " + nuevaContratacion.getNumExp() + " Tablas: " + tablas);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide85.SEQ_MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, NUM_PUESTO, DENOMPUESTO1, TITULACION1, MUNICIPIOCT1, DURCONTRATO1, GRUPOCOTIZ1, COSTESALARIAL1, COSTEEPIS1, SUBVSOLICITADA1, EMPVERDE1, EMPDIGIT1, EMPGEN1)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevaContratacion.getNumExp());
            ps.setInt(contBD++, nuevaContratacion.getNumpuesto());
            ps.setString(contBD++, nuevaContratacion.getDenompuesto1());
            ps.setString(contBD++, nuevaContratacion.getTitulacion1());
            ps.setString(contBD++, nuevaContratacion.getMunicipioct1());
            ps.setString(contBD++, nuevaContratacion.getDurcontrato1());
            ps.setString(contBD++, nuevaContratacion.getGrupocotiz1());
            ps.setDouble(contBD++, (nuevaContratacion.getCostesalarial1() != null ? nuevaContratacion.getCostesalarial1() : 0.0));
            ps.setDouble(contBD++, (nuevaContratacion.getCosteepis1() != null ? nuevaContratacion.getCosteepis1() : 0.0));
            ps.setDouble(contBD++, (nuevaContratacion.getSubvsolicitada1() != null ? nuevaContratacion.getSubvsolicitada1() : 0.0));
            ps.setString(contBD++, nuevaContratacion.getEmpverde1());
            ps.setString(contBD++, nuevaContratacion.getEmpdigit1());
            ps.setString(contBD++, nuevaContratacion.getEmpgen1());

            if (ps.executeUpdate() > 0) {
                resultado = true;
                if (tablas != 1) {
                    contBD = 1;
                    id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide85.SEQ_MELANBIDE85_CONTRATA_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES), con);
                    query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                            + "(ID, NUM_EXP, NUM_PUESTO, NOFERTA2, NOMBRE2, APELLIDO12, APELLIDO22, DNINIE2, SEXO2, TITULACION2, DENOMPUESTO2, MUNICIPIOCT2, GRUPOCOTIZ2, DURCONTRATO2, "
                            + "FECHANACIMIENTO2, FECHAINICIO2, EDAD, RETRIBUCIONBRUTA2, EMPVERDE2, EMPDIGIT2, EMPGEN2)"
                            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    log.debug("sql = " + query);
                    ps = con.prepareStatement(query);
                    ps.setInt(contBD++, id);
                    ps.setString(contBD++, nuevaContratacion.getNumExp());
                    ps.setInt(contBD++, nuevaContratacion.getNumpuesto());
                    ps.setString(contBD++, nuevaContratacion.getNoferta2());
                    ps.setString(contBD++, nuevaContratacion.getNombre2());
                    ps.setString(contBD++, nuevaContratacion.getApellido12());
                    ps.setString(contBD++, nuevaContratacion.getApellido22());
                    ps.setString(contBD++, nuevaContratacion.getDninie2());
                    ps.setString(contBD++, nuevaContratacion.getSexo2());
                    ps.setString(contBD++, nuevaContratacion.getTitulacion2());
                    ps.setString(contBD++, nuevaContratacion.getDenompuesto2());
                    ps.setString(contBD++, nuevaContratacion.getMunicipioct2());
                    ps.setString(contBD++, nuevaContratacion.getGrupocotiz2());
                    ps.setString(contBD++, nuevaContratacion.getDurcontrato2());
                    ps.setDate(contBD++, nuevaContratacion.getFechanacimiento2());
                    ps.setDate(contBD++, nuevaContratacion.getFechainicio2());
                    ps.setInt(contBD++, (nuevaContratacion.getEdad() != null && !nuevaContratacion.getEdad().toString().isEmpty() ? nuevaContratacion.getEdad() : 0));
                    ps.setDouble(contBD++, (nuevaContratacion.getRetribucionbruta2() != null ? nuevaContratacion.getRetribucionbruta2() : 0.0));
                    ps.setString(contBD++, nuevaContratacion.getEmpverde2());
                    ps.setString(contBD++, nuevaContratacion.getEmpdigit2());
                    ps.setString(contBD++, nuevaContratacion.getEmpgen2());

                    if (ps.executeUpdate() > 0) {
                        resultado = true;
                        if (tablas == 3) {
                            contBD = 1;
                            id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide85.SEQ_MELANBIDE85_CONTRATA_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES), con);
                            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                                    + "(ID, NUM_EXP, NUM_PUESTO, NOMBRE3, APELLIDO13, APELLIDO23, DNINIE3, DURCONTRATO3, FECHAINICIO3, FECHAFIN3, COSTESALARIAL3, COSTESSS3, COSTEEPIS3, COSTETOTALREAL3, SUBVCONCEDIDALAN3)"
                                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            log.debug("sql = " + query);
                            ps = con.prepareStatement(query);
                            ps.setInt(contBD++, id);
                            ps.setString(contBD++, nuevaContratacion.getNumExp());
                            ps.setInt(contBD++, nuevaContratacion.getNumpuesto());
                            ps.setString(contBD++, nuevaContratacion.getNombre3());
                            ps.setString(contBD++, nuevaContratacion.getApellido13());
                            ps.setString(contBD++, nuevaContratacion.getApellido23());
                            ps.setString(contBD++, nuevaContratacion.getDninie3());
                            ps.setString(contBD++, nuevaContratacion.getDurcontrato3());
                            ps.setDate(contBD++, nuevaContratacion.getFechainicio3());
                            ps.setDate(contBD++, nuevaContratacion.getFechafin3());
                            ps.setDouble(contBD++, (nuevaContratacion.getCostesalarial3() != null ? nuevaContratacion.getCostesalarial3() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getCostesss3() != null ? nuevaContratacion.getCostesss3() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getCosteepis3() != null ? nuevaContratacion.getCosteepis3() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getCostetotalreal3() != null ? nuevaContratacion.getCostetotalreal3() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getSubvconcedidalan3() != null ? nuevaContratacion.getSubvconcedidalan3() : 0.0));

                            if (ps.executeUpdate() > 0) {
                                resultado = true;
                            } else {
                                log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES));
                                resultado = false;
                            }
                            log.info("El expediente " + nuevaContratacion.getNumExp() + " tiene datos en las 3 tablas");
                        } else {
                            log.info("El expediente " + nuevaContratacion.getNumExp() + " solo tiene datos en las tabla CONTRATACIONES y CONTRATACIONES_INI");
                        }
                    } else {
                        log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES));
                        resultado = false;
                    }
                } else {
                    log.info("El expediente " + nuevaContratacion.getNumExp() + " solo tiene datos en la tabla CONTRATACIONES");
                }
            } else {
                log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar una nueva Contratación" + ex.getMessage());
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
     * @param contrataModif
     * @param tablas
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarContratacion(FilaContratacionVO contrataModif, int tablas, Connection con) throws Exception {
        log.info("Entramos en modificarContratacion DAO - " + contrataModif.getNumExp() + " Tablas: " + tablas);
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        int id = 0;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " SET NUM_PUESTO=?, DENOMPUESTO1=?, TITULACION1=?, MUNICIPIOCT1=?, DURCONTRATO1=?, GRUPOCOTIZ1=?, COSTESALARIAL1=?, COSTEEPIS1=?, SUBVSOLICITADA1=?, EMPVERDE1=?, EMPDIGIT1=?, EMPGEN1=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, contrataModif.getNumpuesto());
            ps.setString(contBD++, contrataModif.getDenompuesto1());
            ps.setString(contBD++, contrataModif.getTitulacion1());
            ps.setString(contBD++, contrataModif.getMunicipioct1());
            ps.setString(contBD++, contrataModif.getDurcontrato1());
            ps.setString(contBD++, contrataModif.getGrupocotiz1());
            ps.setDouble(contBD++, contrataModif.getCostesalarial1());
            ps.setDouble(contBD++, contrataModif.getCosteepis1());
            ps.setDouble(contBD++, contrataModif.getSubvsolicitada1());
            ps.setString(contBD++, contrataModif.getEmpverde1());
            ps.setString(contBD++, contrataModif.getEmpdigit1());
            ps.setString(contBD++, contrataModif.getEmpgen1());

            ps.setString(contBD++, contrataModif.getNumExp());
            ps.setInt(contBD++, contrataModif.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
                if (tablas != 1) {
                    contBD = 1;
                    if (existeContratacion(contrataModif.getNumExp(), contrataModif.getNumpuesto(), 2, con)) {
                        query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                                + " SET NOFERTA2=?, NOMBRE2=?, APELLIDO12=?, APELLIDO22=?, DNINIE2=?, SEXO2=?, TITULACION2=?, DENOMPUESTO2=?, MUNICIPIOCT2=?, GRUPOCOTIZ2=?, DURCONTRATO2=?,"
                                + " FECHANACIMIENTO2=?, FECHAINICIO2=?, EDAD=?, RETRIBUCIONBRUTA2=?, EMPVERDE2=?, EMPDIGIT2=?, EMPGEN2=?"
                                + " WHERE NUM_EXP=? AND NUM_PUESTO=?";

                        ps = con.prepareStatement(query);
                        ps.setString(contBD++, contrataModif.getNoferta2());
                        ps.setString(contBD++, contrataModif.getNombre2());
                        ps.setString(contBD++, contrataModif.getApellido12());
                        ps.setString(contBD++, contrataModif.getApellido22());
                        ps.setString(contBD++, contrataModif.getDninie2());
                        ps.setString(contBD++, contrataModif.getSexo2());
                        ps.setString(contBD++, contrataModif.getTitulacion2());
                        ps.setString(contBD++, contrataModif.getDenompuesto2());
                        ps.setString(contBD++, contrataModif.getMunicipioct2());
                        ps.setString(contBD++, contrataModif.getGrupocotiz2());
                        ps.setString(contBD++, contrataModif.getDurcontrato2());
                        ps.setDate(contBD++, contrataModif.getFechanacimiento2());
                        ps.setDate(contBD++, contrataModif.getFechainicio2());
                        ps.setInt(contBD++, (contrataModif.getEdad() != null && !contrataModif.getEdad().toString().isEmpty() ? contrataModif.getEdad() : 0));
                        ps.setDouble(contBD++, (contrataModif.getRetribucionbruta2() != null ? contrataModif.getRetribucionbruta2() : 0.0));
                        ps.setString(contBD++, contrataModif.getEmpverde2());
                        ps.setString(contBD++, contrataModif.getEmpdigit2());
                        ps.setString(contBD++, contrataModif.getEmpgen2());

                        ps.setString(contBD++, contrataModif.getNumExp2());
                        ps.setInt(contBD++, contrataModif.getNumpuesto());
                    } else {
                        id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide85.SEQ_MELANBIDE85_CONTRATA_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES), con);
                        query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                                + "(ID, NUM_EXP, NUM_PUESTO, NOFERTA2, NOMBRE2, APELLIDO12, APELLIDO22, DNINIE2, SEXO2, TITULACION2, DENOMPUESTO2, MUNICIPIOCT2, GRUPOCOTIZ2, DURCONTRATO2, "
                                + "FECHANACIMIENTO2, FECHAINICIO2, EDAD, RETRIBUCIONBRUTA2, EMPVERDE2, EMPDIGIT2, EMPGEN2)"
                                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = con.prepareStatement(query);
                        ps.setInt(contBD++, id);
                        ps.setString(contBD++, contrataModif.getNumExp());
                        ps.setInt(contBD++, contrataModif.getNumpuesto());
                        ps.setString(contBD++, contrataModif.getNoferta2());
                        ps.setString(contBD++, contrataModif.getNombre2());
                        ps.setString(contBD++, contrataModif.getApellido12());
                        ps.setString(contBD++, contrataModif.getApellido22());
                        ps.setString(contBD++, contrataModif.getDninie2());
                        ps.setString(contBD++, contrataModif.getSexo2());
                        ps.setString(contBD++, contrataModif.getTitulacion2());
                        ps.setString(contBD++, contrataModif.getDenompuesto2());
                        ps.setString(contBD++, contrataModif.getMunicipioct2());
                        ps.setString(contBD++, contrataModif.getGrupocotiz2());
                        ps.setString(contBD++, contrataModif.getDurcontrato2());
                        ps.setDate(contBD++, contrataModif.getFechanacimiento2());
                        ps.setDate(contBD++, contrataModif.getFechainicio2());
                        ps.setInt(contBD++, (contrataModif.getEdad() != null && !contrataModif.getEdad().toString().isEmpty() ? contrataModif.getEdad() : 0));
                        ps.setDouble(contBD++, (contrataModif.getRetribucionbruta2() != null ? contrataModif.getRetribucionbruta2() : 0.0));
                        ps.setString(contBD++, contrataModif.getEmpverde2());
                        ps.setString(contBD++, contrataModif.getEmpdigit2());
                        ps.setString(contBD++, contrataModif.getEmpgen2());
                    }
                    log.debug("Nº puesto " + contrataModif.getNumpuesto());
                    log.debug("PARÁMETROS:  oferta: " + contrataModif.getNoferta2() + " Nombre: " + contrataModif.getNombre2() + " " + contrataModif.getApellido12() + " " + contrataModif.getApellido22()
                            + " - " + contrataModif.getDninie2() + " - " + contrataModif.getSexo2() + " - " + contrataModif.getFechanacimiento2() + " Titulación: " + contrataModif.getTitulacion2()
                            + " Puesto: " + contrataModif.getDenompuesto2() + " Municipio: " + contrataModif.getMunicipioct2() + " Grupo: " + contrataModif.getGrupocotiz2() + " Dura: " + contrataModif.getDurcontrato2()
                            + " F Ini: " + contrataModif.getFechainicio2() + " Edad: " + contrataModif.getEdad() + " Retr: " + contrataModif.getRetribucionbruta2() + " Verde: " + contrataModif.getEmpverde2() + " Digit: " + contrataModif.getEmpdigit2() + "Gen" + contrataModif.getEmpgen2());
                    log.debug("sql = " + query);

                    if (ps.executeUpdate() > 0) {
                        resultado = true;
                        if (tablas == 3) {
                            contBD = 1;
                            if (existeContratacion(contrataModif.getNumExp(), contrataModif.getNumpuesto(), 3, con)) {
                                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                                        + " SET NOMBRE3=?, APELLIDO13=?, APELLIDO23=?, DNINIE3=?, DURCONTRATO3=?, FECHAINICIO3=?, FECHAFIN3=?, COSTESALARIAL3=?, COSTESSS3=?, COSTEEPIS3=?, COSTETOTALREAL3=?, SUBVCONCEDIDALAN3=?"
                                        + " WHERE NUM_EXP=? AND NUM_PUESTO=?";
                                ps = con.prepareStatement(query);

                                ps.setString(contBD++, contrataModif.getNombre3());
                                ps.setString(contBD++, contrataModif.getApellido13());
                                ps.setString(contBD++, contrataModif.getApellido23());
                                ps.setString(contBD++, contrataModif.getDninie3());
                                ps.setString(contBD++, contrataModif.getDurcontrato3());
                                ps.setDate(contBD++, contrataModif.getFechainicio3());
                                ps.setDate(contBD++, contrataModif.getFechafin3());
                                ps.setDouble(contBD++, (contrataModif.getCostesalarial3() != null ? contrataModif.getCostesalarial3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostesss3() != null ? contrataModif.getCostesss3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCosteepis3() != null ? contrataModif.getCosteepis3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostetotalreal3() != null ? contrataModif.getCostetotalreal3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getSubvconcedidalan3() != null ? contrataModif.getSubvconcedidalan3() : 0.0));

                                ps.setString(contBD++, contrataModif.getNumExp());
                                ps.setInt(contBD++, contrataModif.getNumpuesto());

                            } else {
                                id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide85.SEQ_MELANBIDE85_CONTRATA_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES), con);
                                query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                                        + "(ID, NUM_EXP, NUM_PUESTO, NOMBRE3, APELLIDO13, APELLIDO23, DNINIE3, DURCONTRATO3, FECHAINICIO3, FECHAFIN3, COSTESALARIAL3, COSTESSS3, COSTEEPIS3, COSTETOTALREAL3, SUBVCONCEDIDALAN3)"
                                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                ps = con.prepareStatement(query);
                                ps.setInt(contBD++, id);
                                ps.setString(contBD++, contrataModif.getNumExp());
                                ps.setInt(contBD++, contrataModif.getNumpuesto());
                                ps.setString(contBD++, contrataModif.getNombre3());
                                ps.setString(contBD++, contrataModif.getApellido13());
                                ps.setString(contBD++, contrataModif.getApellido23());
                                ps.setString(contBD++, contrataModif.getDninie3());
                                ps.setString(contBD++, contrataModif.getDurcontrato3());
                                ps.setDate(contBD++, contrataModif.getFechainicio3());
                                ps.setDate(contBD++, contrataModif.getFechafin3());
                                ps.setDouble(contBD++, (contrataModif.getCostesalarial3() != null ? contrataModif.getCostesalarial3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostesss3() != null ? contrataModif.getCostesss3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCosteepis3() != null ? contrataModif.getCosteepis3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostetotalreal3() != null ? contrataModif.getCostetotalreal3() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getSubvconcedidalan3() != null ? contrataModif.getSubvconcedidalan3() : 0.0));
                            }
                            log.debug("PARÁMETROS:  Nombre: " + contrataModif.getNombre3() + " " + contrataModif.getApellido13() + " " + contrataModif.getApellido23() + " - " + contrataModif.getDninie3()
                                    + " - Dura: " + contrataModif.getDurcontrato3() + " F Ini: " + contrataModif.getFechainicio3() + " F Fin: " + contrataModif.getFechafin3()
                                    + " - Coste " + contrataModif.getCostesalarial3() + " - SS " + contrataModif.getCostesss3() + " - EPIs " + contrataModif.getCosteepis3()
                                    + " - " + contrataModif.getCostetotalreal3() + " - " + contrataModif.getSubvconcedidalan3());

                            log.debug("sql = " + query);

                            if (ps.executeUpdate() > 0) {
                                resultado = true;
                            } else {
                                log.error("Se ha producido un error al Modificar una Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES));
                                resultado = false;
                            }
                            log.info("El expediente " + contrataModif.getNumExp() + " tiene datos en las 3 tablas");
                        } else {
                            log.info("El expediente " + contrataModif.getNumExp() + " solo tiene datos en las tabla CONTRATACIONES y CONTRATACIONES_INI");
                        }
                    } else {
                        log.error("No Se ha podido modificar una  Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES));
                        resultado = false;
                    }
                } else {
                    log.info("El expediente " + contrataModif.getNumExp() + " solo tiene datos en la tabla CONTRATACIONES");
                }
            } else {
                log.error("Se ha producido un error al Modificar una Contratación - " + contrataModif.getId() + " - " + contrataModif.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una Contratación - " + contrataModif.getId() + " - " + contrataModif.getNumExp() + " - " + ex);
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
    public int eliminarContratacion(String id, String numExp, Connection con) throws Exception {
        log.info("Entramos en eliminarContratacion DAO - " + numExp + " Id: " + id);
        String query = null;
        Statement st = null;
        int numPuesto = 0;
        int resultado = 0;
        try {
            numPuesto = getNumPuesto(numExp, id, con);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND NUM_PUESTO=" + numPuesto;
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND NUM_PUESTO=" + numPuesto;
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

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
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el número de ID para inserción al llamar la secuencia " + sequence + " : ", ex);
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
     * @param numExp
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    private int getNumPuesto(String numExp, String id, Connection con) throws Exception {
        log.info("Entramos en getNumPuesto DAO - " + numExp + " Id: " + id);
        Statement st = null;
        ResultSet rs = null;
        int numPuesto = 0;
        try {
            String query = "SELECT NUM_PUESTO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND ID=" + id;
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numPuesto = rs.getInt("NUM_PUESTO");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando El numero de Puesto ");
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numPuesto;
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
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "' order by DES_NOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide85MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
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

    private boolean existeContratacion(String numExp, int numPuesto, int fase, Connection con) throws Exception {
        log.info("Entramos en existeContratacion DAO - " + numExp + " Puesto: " + numPuesto + " Fase: " + fase);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        String tabla = null;
        try {
            if (fase == 2) {
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_INI, ConstantesMeLanbide85.FICHERO_PROPIEDADES);
            } else if (fase == 3) {
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide85.MELANBIDE85_CONTRATACION_FIN, ConstantesMeLanbide85.FICHERO_PROPIEDADES);
            } else {
                log.error("No se ha recibido el código de la fase válido");
                return false;
            }
            query = "SELECT COUNT(*) FROM " + tabla
                    + " WHERE NUM_EXP=? AND NUM_PUESTO=?";
            log.debug("SQL " + query);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, numPuesto);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si existen las tablas en el puesto" + numPuesto + " del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void setFicheroCVoDemandaIntermediacion(String numExpediente, File valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setFicheroCVoDemandaIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TFI_VALOR ");
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ");
            query.append("WHERE TFI_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFI_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarFicheroCVoDemandaIntermediacion(numExpediente, valor, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarFicheroCVoDemandaIntermediacion(numExpediente, valor, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setFicheroCVoDemandaIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setFicheroCVoDemandaIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setFicheroCVoDemandaIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setFicheroCVoDemandaIntermediacion DAO");
    }

    public CamposFormulario getValoresFichero(GeneralValueObject gVO, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            String codMunicipio = (String) gVO.getAtributo("codMunicipio");
            String ejercicio = (String) gVO.getAtributo("ejercicio");
            String numero = (String) gVO.getAtributo("numero");

            String sql = "SELECT tfi_cod, tfi_nomfich, tfi_valor, tfi_mime FROM E_TFI WHERE tfi_mun =? AND tfi_eje =? AND tfi_num =?";

            if (log.isDebugEnabled()) {
                log.debug(sql);
            }

            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, Integer.parseInt(codMunicipio));
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setString(i++, numero);
            rs = ps.executeQuery();

            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfi_cod");
                String valorCampo = rs.getString("tfi_nomfich");

                final Blob fichero = rs.getBlob("tfi_valor");

                campos.put(codCampo, fichero);

                String mime = rs.getString("tfi_mime");
                campos.put(codCampo + "_TIPO", mime);
                campos.put(codCampo + "_NOMBRE", valorCampo);

                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            e.printStackTrace();
            log.error("Excepcion capturada en: " + getClass().getName());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Excepcion capturada en: " + getClass().getName());
            }
        }
        return cF;
    }

    private void insertarFicheroCVoDemandaIntermediacion(String numExpediente, File valor, String campo,
            BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarFicheroCVoDemandaIntermediacion DAO");

        PreparedStatement st = null;
        int rows = 0;
        try {
            String sql = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " "
                    + "(TFI_MUN, TFI_EJE, TFI_NUM, TFI_COD, TFI_VALOR, TFI_MIME, TFI_NOMFICH,"
                    + " TFI_TAMANHO) VALUES (" //?, ?, ?, ?, ?, ?, ?, ?)";
                    + txt_mun + ", " + txt_eje + ", '" + numExpediente + "', '" + campo + "', ?"
                    + ", 'application/pdf', '" + valor.getName() + "'"
                    + ", " + valor.length() + ")";
            log.info("insertarFicheroCVoDemandaIntermediacion sql = " + sql);
            st = con.prepareStatement(sql);

            final byte[] buffer = getInputStream(valor);
            st.setBinaryStream(1, new ByteArrayInputStream(buffer), buffer.length);

            rows = st.executeUpdate();
        } catch (Exception ex) {
            log.error("Excepcion - insertarFicheroCVoDemandaIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarFicheroCVoDemandaIntermediacion - Procedemos a cerrar el statement y el resultset: " + rows);
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("insertarFicheroCVoDemandaIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarFicheroCVoDemandaIntermediacion DAO");
    }

    private void actualizarFicheroCVoDemandaIntermediacion(String numExpediente, File valor, String campo,
            Connection con) throws Exception {
        log.info("INICIO actualizarFicheroCVoDemandaIntermediacion DAO");

        PreparedStatement st = null;

        try {
            String sql = "UPDATE E_TFI " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " SET TFI_VALOR = ?"
                    + " WHERE TFI_NUM = '"
                    + numExpediente + "' AND TFI_COD = '"
                    + campo + "'";
            st = con.prepareStatement(sql);

            final byte[] buffer = getInputStream(valor);
            st.setBinaryStream(1, new ByteArrayInputStream(buffer), buffer.length);

            st.executeUpdate();

        } catch (Exception ex) {
            log.error("Excepcion - actualizarFicheroCVoDemandaIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarFicheroCVoDemandaIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("actualizarFicheroCVoDemandaIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN actualizarFicheroCVoDemandaIntermediacion DAO");
    }

    public BigDecimal getMUNDatosIntermediacion(String numExpediente, Connection con) throws Exception {
        log.info("INICIO getMUNDatosIntermediacion DAO");
        BigDecimal txt_mun = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT TXT_MUN ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                txt_mun = rs.getBigDecimal("TXT_MUN");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getMUNDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getMUNDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getMUNDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getMUNDatosIntermediacion DAO");
        return txt_mun;
    }

    public BigDecimal getEJEDatosIntermediacion(String numExpediente, Connection con) throws Exception {
        log.info("INICIO getEJEDatosIntermediacion DAO");
        BigDecimal txt_eje = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT TXT_EJE ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                txt_eje = rs.getBigDecimal("TXT_EJE");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getEJEDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getEJEDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getEJEDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getEJEDatosIntermediacion DAO");
        return txt_eje;
    }

    public int deleteFechaDatosIntermediacion(String numExpediente, String codCampo, Connection con) throws Exception {
        log.info("INICIO deleteFechaDatosIntermediacion DAO");

        Statement st = null;
        int result = 0;
        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE ");
            query.append("FROM E_TFE ");
            query.append("WHERE TFE_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFE_COD = '");
            query.append(codCampo);
            query.append("'");

            st = con.createStatement();
            result = st.executeUpdate(query.toString());
        } catch (Exception ex) {
            log.error("Excepcion - deleteFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("deleteFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("deleteFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN deleteFechaDatosIntermediacion DAO result = " + result);
        return result;
    }

    public int deleteFicheroCVoDemandaIntermediacion(String numExpediente, String codCampo, Connection con) throws Exception {
        log.info("INICIO deleteFicheroCVoDemandaIntermediacion DAO");

        Statement st = null;
        int result = 0;
        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE ");
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ");
            query.append("WHERE TFI_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFI_COD = '");
            query.append(codCampo);
            query.append("'");

            st = con.createStatement();
            result = st.executeUpdate(query.toString());
        } catch (Exception ex) {
            log.error("Excepcion - deleteFicheroCVoDemandaIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("deleteFicheroCVoDemandaIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("setFicheroCVoDemandaIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN setFicheroCVoDemandaIntermediacion DAO result = " + result);
        return result;
    }

    public void setFechaDatosIntermediacion(String numExpediente, Date valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setFechaDatosIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TFE_VALOR ");
            query.append("FROM E_TFE ");
            query.append("WHERE TFE_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFE_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            // Si YA existe se ACTUALIZA
            if (rs.next()) {
                actualizarFechaDatosIntermediacion(numExpediente, valor, campo, con);
            } // Si NO existe se INSERTA
            else {
                insertarFechaDatosIntermediacion(numExpediente, valor, campo, txt_mun, txt_eje, con);
            }

        } catch (Exception ex) {
            log.error("Excepcion - setFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("setFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("setFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN setFechaDatosIntermediacion DAO");
    }

    private void insertarFechaDatosIntermediacion(String numExpediente, Date valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarFechaDatosIntermediacion DAO");
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("INSERT INTO E_TFE (TFE_MUN, TFE_EJE, TFE_NUM, TFE_COD, TFE_VALOR, TFE_FEC_VENCIMIENTO, PLAZO_ACTIVADO) VALUES (");
            query.append(txt_mun);
            query.append(", ");
            query.append(txt_eje);
            query.append(", '");
            query.append(numExpediente);
            query.append("', '");
            query.append(campo);
            query.append("', ");
            query.append("TO_DATE('" + format.format(valor) + "', 'dd/mm/yyyy')");
            query.append(", ");
            query.append("null, ");
            query.append("null ");
            query.append(") ");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - insertarFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("insertarFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("insertarFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN insertarFechaDatosIntermediacion DAO");
    }

    private void actualizarFechaDatosIntermediacion(String numExpediente, Date valor, String campo, Connection con) throws Exception {
        log.info("INICIO actualizarFechaDatosIntermediacion DAO");
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("UPDATE E_TFE SET TFE_VALOR = ");
            query.append("TO_DATE('" + format.format(valor) + "', 'dd/mm/yyyy')");
            query.append(" ");
            query.append("WHERE TFE_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFE_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            st.executeUpdate(query.toString());

        } catch (Exception ex) {
            log.error("Excepcion - actualizarFechaDatosIntermediacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizarFechaDatosIntermediacion - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("actualizarFechaDatosIntermediacion - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN actualizarFechaDatosIntermediacion DAO");
    }

    public boolean existeCodigoFichero(String numExpediente, String campo, Connection con) throws Exception {
        log.info("INICIO existeCodigoFichero DAO");

        Statement st = null;
        ResultSet rs = null;
        boolean result;

        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TFI_MUN ");
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES) + " ");
            query.append("WHERE TFI_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFI_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                result = true;
            } else {
                result = false;
            }

        } catch (Exception ex) {
            log.error("Excepcion - existeCodigoFichero : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("existeCodigoFichero - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("existeCodigoFichero - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN existeCodigoFichero DAO");
        return result;
    }

    public boolean actualizaCodigoFichero(String numExpediente, String codActual, String codNuevo,
            Connection con) throws Exception {
        log.info("INICIO actualizaCampoFichero DAO");

        PreparedStatement st = null;
        boolean result = false;
        try {
            String sql = "UPDATE E_TFI " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " SET TFI_COD = ?"
                    + " WHERE TFI_NUM = '"
                    + numExpediente + "' AND TFI_COD = '"
                    + codActual + "'";
            st = con.prepareStatement(sql);

            st.setString(1, codNuevo);

            if (st.executeUpdate() > 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (Exception ex) {
            log.error("Excepcion - actualizaCampoFichero : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizaCampoFichero - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("actualizaCampoFichero - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN actualizaCampoFichero DAO");
        return result;
    }

    public boolean actualizaCodigoFecha(String numExpediente, String codActual, String codNuevo,
            Connection con) throws Exception {
        log.info("INICIO actualizaCodigoFecha DAO");

        PreparedStatement st = null;
        boolean result = false;
        try {
            String sql = "UPDATE E_TFE " + ConfigurationParameter.getParameter(ConstantesMeLanbide85.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide85.FICHERO_PROPIEDADES)
                    + " SET TFE_COD = ?"
                    + " WHERE TFE_NUM = '"
                    + numExpediente + "' AND TFE_COD = '"
                    + codActual + "'";
            st = con.prepareStatement(sql);

            st.setString(1, codNuevo);

            if (st.executeUpdate() > 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (Exception ex) {
            log.error("Excepcion - actualizaCodigoFecha : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("actualizaCodigoFecha - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
            } catch (Exception e) {
                log.error("actualizaCodigoFecha - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("FIN actualizaCodigoFecha DAO");
        return result;
    }

    private byte[] getInputStream(final File valor) throws Exception {
        if (valor.length() > Integer.MAX_VALUE) {
            throw new Exception("Erro: El fichero es demasiado grande para tratarlo");
        }
        final InputStream inputStream = new FileInputStream(valor);
        byte dataBuffer[] = new byte[(int) valor.length()];
        inputStream.read(dataBuffer, 0, (int) valor.length());
        inputStream.close();

        return dataBuffer;
    }

}
