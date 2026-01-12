package es.altia.flexia.integracion.moduloexterno.melanbide86.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConstantesMeLanbide86;
import es.altia.flexia.integracion.moduloexterno.melanbide86.util.MeLanbide86MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaContratacionVO;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaMinimisVO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide86DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide86DAO.class);
    //Instancia
    private static MeLanbide86DAO instance = null;

    // Constructor
    private MeLanbide86DAO() {

    }

    public static MeLanbide86DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide86DAO.class) {
                instance = new MeLanbide86DAO();
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
        FilaContratacionVO filaContratacionVO = new FilaContratacionVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " B"
                    + " ON A.NUM_EXP = B.NUM_EXP AND A.NUM_PUESTO = B.NUM_PUESTO"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " C"
                    + " ON A.NUM_EXP = C.NUM_EXP AND A.NUM_PUESTO = C.NUM_PUESTO"
                    + " WHERE A.NUM_EXP ='" + numExp + "'"
                    + " ORDER BY A.ID";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                filaContratacionVO = (FilaContratacionVO) MeLanbide86MappingUtils.getInstance().map(rs, FilaContratacionVO.class);
                lista.add(filaContratacionVO);
                filaContratacionVO = new FilaContratacionVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Contrataciones ");
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
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " B"
                    + " ON A.NUM_EXP = B.NUM_EXP AND A.NUM_PUESTO = B.NUM_PUESTO"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " C"
                    + " ON A.NUM_EXP = C.NUM_EXP AND A.NUM_PUESTO = C.NUM_PUESTO"
                    + " WHERE A.ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                contratacion = (FilaContratacionVO) MeLanbide86MappingUtils.getInstance().map(rs, FilaContratacionVO.class);
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
    public boolean crearContratación(FilaContratacionVO nuevaContratacion, int tablas, Connection con) throws Exception {
        log.info("Entramos en crearContratación DAO - " + nuevaContratacion.getNumExp() + " Tablas: " + tablas);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide86.SEQ_MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, NUM_PUESTO, DENOMPUESTO1, ACTDES1, TITULACION1, TIPCONT1, DURCONTRATO1, GRUPOCOTIZ1, COSTESALARIAL1, SUBVSOLICITADA1, CAINVINN1)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevaContratacion.getNumExp());
            ps.setInt(contBD++, nuevaContratacion.getNumPuesto());
            ps.setString(contBD++, nuevaContratacion.getDenomPuesto());
            ps.setString(contBD++, nuevaContratacion.getActDes1());
            ps.setString(contBD++, nuevaContratacion.getTitulacion1());
            ps.setString(contBD++, nuevaContratacion.getTipoCont1());
            ps.setInt(contBD++, nuevaContratacion.getDurContrato1());
            ps.setString(contBD++, nuevaContratacion.getGrupoCotiz1());
            ps.setDouble(contBD++, nuevaContratacion.getCosteSalarial1());
            ps.setDouble(contBD++, nuevaContratacion.getSubvSolicitada1());
            ps.setString(contBD++, nuevaContratacion.getCainVinn1());
            log.debug("sql = " + query);
            log.debug("PARÁMETROS: " + id + " - " + nuevaContratacion.getNumPuesto() + " - " + nuevaContratacion.getDenomPuesto() + " - " + nuevaContratacion.getActDes1() + " - " + nuevaContratacion.getTitulacion1() + " - " + nuevaContratacion.getTipoCont1() + " - " + nuevaContratacion.getDurContrato1() + " - " + nuevaContratacion.getGrupoCotiz1() + " - " + nuevaContratacion.getCosteSalarial1() + " - " + nuevaContratacion.getSubvSolicitada1());

            if (ps.executeUpdate() > 0) {
                resultado = true;
                if (tablas != 1) {
                    contBD = 1;
                    id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide86.SEQ_MELANBIDE86_CONTRATA_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES), con);
                    query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                            + "(ID, NUM_EXP, NUM_PUESTO, NOFERTA2, NOMBRE2, APELLIDO12, APELLIDO22, DNINIE2, SEXO2, DENOMPUESTO2, ACTDES2, TITULACION2, TIPCONT2, DURCONTRATO2, GRUPOCOTIZ2, "
                            + "FECHANACIMIENTO2, FECHAINICIO2, EDAD2, RETRIBUCIONBRUTA2, CAINVINN2)"
                            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    ps = con.prepareStatement(query);
                    ps.setInt(contBD++, id);
                    ps.setString(contBD++, nuevaContratacion.getNumExp2());
                    ps.setInt(contBD++, nuevaContratacion.getNumPuesto2());
                    ps.setString(contBD++, nuevaContratacion.getnOferta2());
                    ps.setString(contBD++, nuevaContratacion.getNombre2());
                    ps.setString(contBD++, nuevaContratacion.getApellido12());
                    ps.setString(contBD++, nuevaContratacion.getApellido22());
                    ps.setString(contBD++, nuevaContratacion.getDniNie2());
                    ps.setString(contBD++, nuevaContratacion.getSexo2());
                    ps.setString(contBD++, nuevaContratacion.getDenomPuesto2());
                    ps.setString(contBD++, nuevaContratacion.getActDes2());
                    ps.setString(contBD++, nuevaContratacion.getTitulacion2());
                    ps.setString(contBD++, nuevaContratacion.getTipoCont2());
                    ps.setInt(contBD++, (nuevaContratacion.getDurContrato2() != null && !nuevaContratacion.getDurContrato2().toString().equals("") ? nuevaContratacion.getDurContrato2() : 0));
                    ps.setString(contBD++, nuevaContratacion.getGrupoCotiz2());
                    ps.setDate(contBD++, nuevaContratacion.getFechaNacimiento2());
                    ps.setDate(contBD++, nuevaContratacion.getFechaInicio2());
                    ps.setInt(contBD++, nuevaContratacion.getEdad2());
                    ps.setDouble(contBD++, nuevaContratacion.getRetribucionBruta2());
                    ps.setString(contBD++, nuevaContratacion.getCainVinn2());
                    log.debug("sql = " + query);
                    log.debug("PARÁMETROS: " + id + " - " + nuevaContratacion.getnOferta2() + " - " + nuevaContratacion.getNombre2() + " - " + nuevaContratacion.getApellido12() + " - " + nuevaContratacion.getApellido22() + " - " + nuevaContratacion.getDniNie2() + " - " + nuevaContratacion.getSexo2() + " - " + nuevaContratacion.getDenomPuesto2() + " - " + nuevaContratacion.getActDes2() + " - " + nuevaContratacion.getTitulacion2()
                            + " - " + nuevaContratacion.getTipoCont2() + " - " + nuevaContratacion.getDurContrato2() + " - " + nuevaContratacion.getGrupoCotiz2() + " - " + nuevaContratacion.getFechaNacimiento2() + " - " + nuevaContratacion.getFechaInicio2() + " - " + nuevaContratacion.getEdad2() + " - " + nuevaContratacion.getRetribucionBruta2() + " - " + nuevaContratacion.getCainVinn2());

                    if (ps.executeUpdate() > 0) {
                        resultado = true;
                        if (tablas == 3) {
                            contBD = 1;
                            id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide86.SEQ_MELANBIDE86_CONTRATA_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES), con);
                            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                                    + "(ID, NUM_EXP, NUM_PUESTO, NOMBRE3, APELLIDO13, APELLIDO23, DNINIE3, DURCONTRATO3, FECHAINICIO3, FECHAFIN3, COSTESALARIAL3, COSTESSS3, COSTETOTALREAL3, SUBVCONCEDIDALAN3)"
                                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                            ps = con.prepareStatement(query);
                            ps.setInt(contBD++, id);
                            ps.setString(contBD++, nuevaContratacion.getNumExp3());
                            ps.setInt(contBD++, nuevaContratacion.getNumPuesto3());
                            ps.setString(contBD++, nuevaContratacion.getNombre3());
                            ps.setString(contBD++, nuevaContratacion.getApellido13());
                            ps.setString(contBD++, nuevaContratacion.getApellido23());
                            ps.setString(contBD++, nuevaContratacion.getDniNie3());
                            ps.setInt(contBD++, (nuevaContratacion.getDurContrato3() != null && !nuevaContratacion.getDurContrato3().toString().equals("") ? nuevaContratacion.getDurContrato3() : 0));
                            ps.setDate(contBD++, nuevaContratacion.getFechaInicio3());
                            ps.setDate(contBD++, nuevaContratacion.getFechaFin3());
                            ps.setDouble(contBD++, nuevaContratacion.getCosteSalarial3());
                            ps.setDouble(contBD++, nuevaContratacion.getCostesSS3());
                            ps.setDouble(contBD++, nuevaContratacion.getCosteTotalReal());
                            ps.setDouble(contBD++, nuevaContratacion.getSubvConcedidaLan3());
                            log.debug("sql = " + query);
                            log.debug("PARÁMETROS: " + id + " - " + nuevaContratacion.getNombre3() + " - " + nuevaContratacion.getApellido13() + " - " + nuevaContratacion.getApellido23() + " - " + nuevaContratacion.getDniNie3() + " - " + nuevaContratacion.getDurContrato3() + " - "
                                    + nuevaContratacion.getFechaInicio3() + " - " + nuevaContratacion.getFechaFin3() + " - " + nuevaContratacion.getCosteSalarial3() + " - " + nuevaContratacion.getCostesSS3() + " - " + nuevaContratacion.getCosteTotalReal() + " - " + nuevaContratacion.getSubvConcedidaLan3());

                            if (ps.executeUpdate() > 0) {
                                resultado = true;
                            } else {
                                log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES));
                                resultado = false;
                            }
                            log.info("El expediente " + nuevaContratacion.getNumExp() + " tiene datos en las 3 tablas");
                        } else {
                            log.info("El expediente " + nuevaContratacion.getNumExp() + " solo tiene datos en las tabla CONTRATACIONES y CONTRATACIONES_INI");
                        }
                    } else {
                        log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES));
                        resultado = false;
                    }
                } else {
                    log.info("El expediente " + nuevaContratacion.getNumExp() + " solo tiene datos en la tabla CONTRATACIONES");
                }
            } else {
                log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al Insertar una nueva Contratación" + ex.getMessage());
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
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " SET NUM_PUESTO = ?, DENOMPUESTO1=?, ACTDES1 = ?, TITULACION1=?, TIPCONT1=?, DURCONTRATO1=?,  GRUPOCOTIZ1=?, COSTESALARIAL1=?,  SUBVSOLICITADA1=?,CAINVINN1 =?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, contrataModif.getNumPuesto());
            ps.setString(contBD++, contrataModif.getDenomPuesto());
            ps.setString(contBD++, contrataModif.getActDes1());
            ps.setString(contBD++, contrataModif.getTitulacion1());
            ps.setString(contBD++, contrataModif.getTipoCont1());
            ps.setInt(contBD++, contrataModif.getDurContrato1());
            ps.setString(contBD++, contrataModif.getGrupoCotiz1());
            ps.setDouble(contBD++, getValueField(contrataModif.getCosteSalarial1()));
            ps.setDouble(contBD++, getValueField(contrataModif.getSubvSolicitada1()));
            ps.setString(contBD++, contrataModif.getCainVinn1());

            ps.setString(contBD++, contrataModif.getNumExp());
            ps.setInt(contBD++, contrataModif.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
                if (tablas != 1) {
                    contBD = 1;
                    if (existeContratacion(contrataModif.getNumExp(), contrataModif.getNumPuesto(), 2, con)) {
                        query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                                + " SET NOFERTA2 = ?, NOMBRE2=?, APELLIDO12 = ?, APELLIDO22=?, DNINIE2=?, SEXO2=?,  DENOMPUESTO2=?, ACTDES2=?,  TITULACION2=?, TIPCONT2 =?, DURCONTRATO2 =?, GRUPOCOTIZ2 =?,"
                                + " FECHANACIMIENTO2 =?, FECHAINICIO2 =?, EDAD2 =?, RETRIBUCIONBRUTA2 =?, CAINVINN2 =?"
                                + " WHERE NUM_EXP = ? AND NUM_PUESTO = ?";

                        ps = con.prepareStatement(query);

                        ps.setString(contBD++, contrataModif.getnOferta2());
                        ps.setString(contBD++, contrataModif.getNombre2());
                        ps.setString(contBD++, contrataModif.getApellido12());
                        ps.setString(contBD++, contrataModif.getApellido22());
                        ps.setString(contBD++, contrataModif.getDniNie2());
                        ps.setString(contBD++, contrataModif.getSexo2());
                        ps.setString(contBD++, contrataModif.getDenomPuesto2());
                        ps.setString(contBD++, contrataModif.getActDes2());
                        ps.setString(contBD++, contrataModif.getTitulacion2());
                        ps.setString(contBD++, contrataModif.getTipoCont2());
                        ps.setInt(contBD++, getValueField(contrataModif.getDurContrato2()));
                        ps.setString(contBD++, contrataModif.getGrupoCotiz2());
                        ps.setDate(contBD++, contrataModif.getFechaNacimiento2());
                        ps.setDate(contBD++, contrataModif.getFechaInicio2());

                        ps.setInt(contBD++, getValueField(contrataModif.getEdad2()));

                        ps.setDouble(contBD++, getValueField(contrataModif.getRetribucionBruta2()));

                        ps.setString(contBD++, contrataModif.getCainVinn2());

                        ps.setString(contBD++, contrataModif.getNumExp2());

                        ps.setInt(contBD++, getValueField(contrataModif.getNumPuesto2()));

                    } else {
                        id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide86.SEQ_MELANBIDE86_CONTRATA_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES), con);

                        query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                                + "(ID, NUM_EXP, NUM_PUESTO, NOFERTA2, NOMBRE2, APELLIDO12, APELLIDO22, DNINIE2, SEXO2, DENOMPUESTO2, ACTDES2, TITULACION2, TIPCONT2, DURCONTRATO2, GRUPOCOTIZ2, "
                                + "FECHANACIMIENTO2, FECHAINICIO2, EDAD2, RETRIBUCIONBRUTA2, CAINVINN2)"
                                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        ps = con.prepareStatement(query);
                        ps.setInt(contBD++, id);
                        ps.setString(contBD++, contrataModif.getNumExp2());
                        ps.setInt(contBD++, contrataModif.getNumPuesto2());
                        ps.setString(contBD++, contrataModif.getnOferta2());
                        ps.setString(contBD++, contrataModif.getNombre2());
                        ps.setString(contBD++, contrataModif.getApellido12());
                        ps.setString(contBD++, contrataModif.getApellido22());
                        ps.setString(contBD++, contrataModif.getDniNie2());
                        ps.setString(contBD++, contrataModif.getSexo2());
                        ps.setString(contBD++, contrataModif.getDenomPuesto2());
                        ps.setString(contBD++, contrataModif.getActDes2());
                        ps.setString(contBD++, contrataModif.getTitulacion2());
                        ps.setString(contBD++, contrataModif.getTipoCont2());
                        ps.setInt(contBD++, getValueField(contrataModif.getDurContrato2()));
                        ps.setString(contBD++, contrataModif.getGrupoCotiz2());
                        ps.setDate(contBD++, contrataModif.getFechaNacimiento2());
                        ps.setDate(contBD++, contrataModif.getFechaInicio2());
                        ps.setInt(contBD++, getValueField(contrataModif.getEdad2()));
                        ps.setDouble(contBD++, getValueField(contrataModif.getRetribucionBruta2()));
                        ps.setString(contBD++, contrataModif.getCainVinn2());
                    }
                    log.debug("sql = " + query);

                    if (ps.executeUpdate() > 0) {
                        resultado = true;
                        if (tablas == 3) {
                            contBD = 1;
                            if (existeContratacion(contrataModif.getNumExp(), contrataModif.getNumPuesto(), 3, con)) {
                                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                                        + " SET NOMBRE3 = ?, APELLIDO13 = ?, APELLIDO23=?, DNINIE3=?, DURCONTRATO3=?,  FECHAINICIO3=?, FECHAFIN3=?,  COSTESALARIAL3=?, COSTESSS3 =?, COSTETOTALREAL3 =?, SUBVCONCEDIDALAN3 =?"
                                        + " WHERE NUM_EXP = ? AND NUM_PUESTO = ?";

                                ps = con.prepareStatement(query);

                                ps.setString(contBD++, contrataModif.getNombre3());
                                ps.setString(contBD++, contrataModif.getApellido13());
                                ps.setString(contBD++, contrataModif.getApellido23());
                                ps.setString(contBD++, contrataModif.getDniNie3());
                                ps.setInt(contBD++, getValueField(contrataModif.getDurContrato3()));
                                ps.setDate(contBD++, contrataModif.getFechaInicio3());
                                ps.setDate(contBD++, contrataModif.getFechaFin3());
                                ps.setDouble(contBD++, getValueField(contrataModif.getCosteSalarial3()));
                                ps.setDouble(contBD++, getValueField(contrataModif.getCostesSS3()));
                                ps.setDouble(contBD++, getValueField(contrataModif.getCosteTotalReal()));
                                ps.setDouble(contBD++, getValueField(contrataModif.getSubvConcedidaLan3()));
                                ps.setString(contBD++, contrataModif.getNumExp3());
                                ps.setInt(contBD++, getValueField(contrataModif.getNumPuesto3()));
                            } else {
                                id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide86.SEQ_MELANBIDE86_CONTRATA_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES), con);
                                query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                                        + "(ID, NUM_EXP, NUM_PUESTO, NOMBRE3, APELLIDO13, APELLIDO23, DNINIE3, DURCONTRATO3, FECHAINICIO3, FECHAFIN3, COSTESALARIAL3, COSTESSS3, COSTETOTALREAL3, SUBVCONCEDIDALAN3)"
                                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                ps = con.prepareStatement(query);
                                ps.setInt(contBD++, id);
                                ps.setString(contBD++, contrataModif.getNumExp3());
                                ps.setInt(contBD++, getValueField(contrataModif.getNumPuesto3()));
                                ps.setString(contBD++, contrataModif.getNombre3());
                                ps.setString(contBD++, contrataModif.getApellido13());
                                ps.setString(contBD++, contrataModif.getApellido23());
                                ps.setString(contBD++, contrataModif.getDniNie3());
                                ps.setInt(contBD++, getValueField(contrataModif.getDurContrato3()));
                                ps.setDate(contBD++, contrataModif.getFechaInicio3());
                                ps.setDate(contBD++, contrataModif.getFechaFin3());
                                ps.setDouble(contBD++, getValueField(contrataModif.getCosteSalarial3()));
                                ps.setDouble(contBD++, getValueField(contrataModif.getCostesSS3()));
                                ps.setDouble(contBD++, getValueField(contrataModif.getCosteTotalReal()));
                                ps.setDouble(contBD++, getValueField(contrataModif.getSubvConcedidaLan3()));
                            }
                            log.debug("sql = " + query);

                            if (ps.executeUpdate() > 0) {
                                resultado = true;
                            } else {
                                log.error("Se ha producido un error al Modificar una Contratación FIN en " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES));
                                resultado = false;
                            }
                            log.info("El expediente " + contrataModif.getNumExp() + " tiene datos en las 3 tablas");
                        } else {
                            log.info("El expediente " + contrataModif.getNumExp() + " solo tiene datos para insertar en las tabla CONTRATACIONES y CONTRATACIONES_INI");
                        }
                    } else {
                        log.error("No Se ha podido modificar una  Contratación INICIO en " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES));
                        resultado = false;
                    }
                } else {
                    log.info("El expediente " + contrataModif.getNumExp() + " solo tiene datos  para insertar en la tabla CONTRATACIONES");
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

    private int getValueField(Integer value) {
        if (value != null && !value.toString().equals("")) {
            return value.intValue();
        } else {
            return 0;
        }
    }

    private double getValueField(Double value) {
        if (value != null && !value.toString().equals("")) {
            return value.doubleValue();
        } else {
            return 0.0;
        }
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

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND NUM_PUESTO=" + numPuesto;
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND NUM_PUESTO=" + numPuesto;
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);
        } catch (SQLException ex) {
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
            String query = "SELECT NUM_PUESTO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND ID=" + id;
            log.debug("sql = " + query);

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
        DesplegableAdmonLocalVO valoresDesplegabe = new DesplegableAdmonLocalVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide86MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
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

    private boolean existeContratacion(String numExp, int numPuesto, int fase, Connection con) throws Exception {
        log.info("Entramos en existeContratacion DAO - " + numExp + " Puesto: " + numPuesto + " Fase: " + fase);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        String tabla = null;
        try {
            if (fase == 2) {
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_INI, ConstantesMeLanbide86.FICHERO_PROPIEDADES);
            } else if (fase == 3) {
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_CONTRATACION_FIN, ConstantesMeLanbide86.FICHERO_PROPIEDADES);
            } else {
                log.error("No se ha recibido el código de la fase válido");
                return false;
            }
            query = "SELECT COUNT(*) FROM " + tabla
                    + " WHERE NUM_EXP=? AND NUM_PUESTO=?";
            log.debug("Query" + query);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, numPuesto);
            rs = ps.executeQuery();
            rs.next();
            log.debug("Devuelve " + rs.getInt(1));
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar el puesto" + numPuesto + " del expediente " + numExp, ex);
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
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " ");
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

    public int deleteFicheroCVoDemandaIntermediacion(String numExpediente, String codCampo, Connection con) throws Exception {
        log.info("INICIO deleteFicheroCVoDemandaIntermediacion DAO");

        Statement st = null;
        int result = 0;
        try {
            StringBuffer query = new StringBuffer();
            query.append("DELETE ");
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " ");
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

    private void insertarFicheroCVoDemandaIntermediacion(String numExpediente, File valor, String campo,
            BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarFicheroCVoDemandaIntermediacion DAO");

        PreparedStatement st = null;
        int rows = 0;
        try {
            String sql = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " "
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
            String sql = "UPDATE E_TFI " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
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

    private void insertarFechaDatosIntermediacion(String numExpediente, Date valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO insertarFechaDatosIntermediacion DAO");
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide86.FORMATO_FECHA);

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
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide86.FORMATO_FECHA);

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
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " ");
            query.append("WHERE TFI_NUM = '");
            query.append(numExpediente);
            query.append("' AND TFI_COD = '");
            query.append(campo);
            query.append("'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            
            if (rs.next()) {
                result = true;
            } 
            else {
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
            String sql = "UPDATE E_TFI " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " SET TFI_COD = ?"
                    + " WHERE TFI_NUM = '"
                    + numExpediente + "' AND TFI_COD = '"
                    + codActual + "'";
            st = con.prepareStatement(sql);

            st.setString(1, codNuevo);

            if (st.executeUpdate() > 0) {
                result = true;
            }
            else {
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
            String sql = "UPDATE E_TFE " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " SET TFE_COD = ?"
                    + " WHERE TFE_NUM = '"
                    + numExpediente + "' AND TFE_COD = '"
                    + codActual + "'";
            st = con.prepareStatement(sql);

            st.setString(1, codNuevo);

            if (st.executeUpdate() > 0) {
                result = true;
            }
            else {
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

    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide86MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(minimis);
                minimis = new FilaMinimisVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Minimis ", ex);
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
        return lista;
    }

    public List<FilaMinimisVO> getMinimis(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "Select * From " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES) + " A "
                    + "Where A.Num_Exp= '" + numExp + "' Order By A.Id";
            if (log.isDebugEnabled()) {
                log.debug("sql getMinimis= " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide86MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(minimis);
                minimis = new FilaMinimisVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Minimis ", ex);
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
        return lista;
    }

    public FilaMinimisVO getMinimisPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide86MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Minimis : " + id, ex);
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
        return minimis;
    }

    public int eliminarMinimis(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Minimis ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (nuevaMinimis != null && nuevaMinimis.getFecha()!= null && !nuevaMinimis.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(nuevaMinimis.getFecha());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide86.SEQ_MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (" + id
                    + ", '" + nuevaMinimis.getNumExp()
                    + "', '" + nuevaMinimis.getEstado()
                    + "', '" + nuevaMinimis.getOrganismo()
                    + "', '" + nuevaMinimis.getObjeto()
                    + "', " + nuevaMinimis.getImporte()
                    + " , TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar una nueva Minimis ");
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al insertar una nueva Minimis" + ex.getMessage());
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (datModif != null && datModif.getFecha()!= null && !datModif.getFecha().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(datModif.getFecha());
        }
        
        
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide86.MELANBIDE86_SUBSOLIC, ConstantesMeLanbide86.FICHERO_PROPIEDADES)
                    + " SET ESTADO='" + datModif.getEstado()+ "'"
                    + ", ORGANISMO='" + datModif.getOrganismo()+ "'"
                    + ", OBJETO='" + datModif.getObjeto()+ "'"
                    + ", IMPORTE=" + datModif.getImporte()
                    + ", FECHA=TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al modificar una Minimis - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement");
            }
            if (st != null) {
                st.close();
            }
        }
    }

  
}
