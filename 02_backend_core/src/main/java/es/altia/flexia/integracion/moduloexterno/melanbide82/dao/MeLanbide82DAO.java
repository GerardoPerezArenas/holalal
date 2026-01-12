package es.altia.flexia.integracion.moduloexterno.melanbide82.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConstantesMeLanbide82;
import es.altia.flexia.integracion.moduloexterno.melanbide82.util.MeLanbide82MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.FilaContratacionVO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
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
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.PersonaContratadaVO;
import java.sql.Date;

public class MeLanbide82DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide82DAO.class);
    //Instancia
    private static MeLanbide82DAO instance = null;

    // Constructor
    private MeLanbide82DAO() {

    }

    public static MeLanbide82DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide82DAO.class) {
                instance = new MeLanbide82DAO();
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
        FilaContratacionVO filaContratacion = new FilaContratacionVO();
        try {
            String query = null;
            // El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " B"
                    + " ON A.NUM_EXP = B.NUM_EXP AND A.PRIORIDAD = B.NUM_PUESTO"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " C"
                    + " ON A.NUM_EXP = C.NUM_EXP AND A.PRIORIDAD = C.NUM_PUESTO"
                    + " WHERE A.NUM_EXP ='" + numExp + "'"
                    + " ORDER BY A.ID";

            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                filaContratacion = (FilaContratacionVO) MeLanbide82MappingUtils.getInstance().map(rs, FilaContratacionVO.class);
                lista.add(filaContratacion);
                filaContratacion = new FilaContratacionVO();
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
        FilaContratacionVO filaContratacion = new FilaContratacionVO();
        String query = null;
        try {
            // El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " B"
                    + " ON A.NUM_EXP = B.NUM_EXP AND A.PRIORIDAD = B.NUM_PUESTO"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " C"
                    + " ON A.NUM_EXP = C.NUM_EXP AND A.PRIORIDAD = C.NUM_PUESTO"
                    + " WHERE A.ID=" + (id != null && !id.isEmpty() ? id : "null");

            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                filaContratacion = (FilaContratacionVO) MeLanbide82MappingUtils.getInstance().map(rs, FilaContratacionVO.class);
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
        return filaContratacion;
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
            //CONTRATACION
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide82.SEQ_MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,PRIORIDAD,DENOMPUESTO,NIVELCUALIFICACION,MODCONTRATO,DURCONTRATO,"
                    + "GRUPOCOTIZ,COSTESALARIAL,SUBVSOLICITADA)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?)";

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevaContratacion.getNumExp());
            ps.setInt(contBD++, nuevaContratacion.getPrioridad());
            ps.setString(contBD++, nuevaContratacion.getDenomPuesto());
            ps.setString(contBD++, nuevaContratacion.getNivelCualificacion());
            ps.setString(contBD++, nuevaContratacion.getModContrato());
            ps.setInt(contBD++, nuevaContratacion.getDurContrato());
            ps.setString(contBD++, nuevaContratacion.getGrupoCotiz());
            ps.setDouble(contBD++, nuevaContratacion.getCostesalarial());
            ps.setDouble(contBD++, nuevaContratacion.getSubvsolicitada());

            log.debug("sql = " + query);
            log.debug("PARÁMETROS: " + id + " - " + nuevaContratacion.getPrioridad() + " - " + nuevaContratacion.getDenomPuesto() + " - " + nuevaContratacion.getNivelCualificacion() + " - " + nuevaContratacion.getModContrato() + " - " + nuevaContratacion.getDurContrato() + " - " + nuevaContratacion.getGrupoCotiz() + " - " + nuevaContratacion.getCostesalarial() + " - " + nuevaContratacion.getSubvsolicitada());
            if (ps.executeUpdate() > 0) {
                resultado = true;
                contBD = 1;
                if (tablas != 1) {
                    //CONTRATACION INICIO  
                    id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide82.SEQ_MELANBIDE82_CONTRATA_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES), con);
                    query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                            + "(ID, NUM_EXP, NUM_PUESTO, MUNICIPIO2, NOMBRE2, APELLIDO12, APELLIDO22, DNINIE2, FECHANACIMIENTO2, SEXO2,"
                            + " NIVELCUALIFICACION2, PUESTOTRABAJO2, NOFERTA2, GRUPOCOTIZ2, DURACION2, FECHAINICIO2, EDAD2, RETRIBUCIONBRUTA2) "
                            + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    ps = con.prepareStatement(query);

                    ps.setInt(contBD++, id);
                    ps.setString(contBD++, nuevaContratacion.getNumExp());
                    ps.setInt(contBD++, nuevaContratacion.getPrioridad());// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
                    ps.setString(contBD++, nuevaContratacion.getMunicipioInicio());
                    ps.setString(contBD++, nuevaContratacion.getNombreInicio());
                    ps.setString(contBD++, nuevaContratacion.getApellido1Inicio());
                    ps.setString(contBD++, nuevaContratacion.getApellido2Inicio());
                    ps.setString(contBD++, nuevaContratacion.getDninieInicio());
                    ps.setDate(contBD++, nuevaContratacion.getFechanacimientoInicio());
                    ps.setString(contBD++, nuevaContratacion.getSexoInicio());
                    ps.setString(contBD++, nuevaContratacion.getNivelcualificacionInicio());
                    ps.setString(contBD++, nuevaContratacion.getPuestotrabajoInicio());
                    ps.setString(contBD++, nuevaContratacion.getNofertaInicio());
                    ps.setString(contBD++, nuevaContratacion.getGrupocotizInicio());
                    ps.setInt(contBD++, (nuevaContratacion.getDurcontratoInicio() != null && !nuevaContratacion.getDurcontratoInicio().toString().isEmpty() ? nuevaContratacion.getDurcontratoInicio() : 0));
                    ps.setDate(contBD++, nuevaContratacion.getFechainicioInicio());
                    ps.setInt(contBD++, (nuevaContratacion.getEdadInicio() != null && !nuevaContratacion.getEdadInicio().toString().isEmpty() ? nuevaContratacion.getEdadInicio() : 0));
                    ps.setDouble(contBD++, (nuevaContratacion.getRetribucionbrutaInicio() != null ? nuevaContratacion.getRetribucionbrutaInicio() : 0.0));

                    log.debug("sql = " + query);
                    log.debug("PARÁMETROS: " + id + " - " + nuevaContratacion.getPrioridad() + " - " + nuevaContratacion.getMunicipioInicio() + " - " + nuevaContratacion.getNombreInicio() + " " + nuevaContratacion.getApellido1Inicio() + " " + nuevaContratacion.getApellido2Inicio() + " - " + nuevaContratacion.getDninieInicio() + " - " + nuevaContratacion.getFechanacimientoInicio() + " - " + nuevaContratacion.getSexoInicio()
                            + " - " + nuevaContratacion.getNivelcualificacionInicio() + " - " + nuevaContratacion.getPuestotrabajoInicio() + " - " + nuevaContratacion.getNofertaInicio() + " - " + nuevaContratacion.getGrupocotizInicio() + " - " + nuevaContratacion.getDurcontratoInicio() + " - " + nuevaContratacion.getFechainicioInicio() + " - " + nuevaContratacion.getEdadInicio() + " - " + nuevaContratacion.getRetribucionbrutaInicio());
                    if (ps.executeUpdate() > 0) {
                        resultado = true;
                        if (tablas == 3) {
                            //CONTRATACION FIN
                            contBD = 1;
                            id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide82.SEQ_MELANBIDE82_CONTRATA_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES), con);
                            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                                    + "(ID, NUM_EXP, NUM_PUESTO, MUNICIPIO3, NOMBRE3, APELLIDO13, APELLIDO23, DNINIE3, SEXO3, GRUPOCOTIZ3,"
                                    + " DURACION3, FECHAINICIO3, FECHAFIN3, RETRIBUCIONBRUTA3, COSTESALARIAL3, COSTESSS3, INDEMFINCONTRATO3, COSTETOTALREAL3, SUBVCONCEDIDALAN3) "
                                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                            ps = con.prepareStatement(query);

                            ps.setInt(contBD++, id);
                            ps.setString(contBD++, nuevaContratacion.getNumExp());
                            ps.setInt(contBD++, nuevaContratacion.getPrioridad());// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
                            ps.setString(contBD++, nuevaContratacion.getMunicipioFin());
                            ps.setString(contBD++, nuevaContratacion.getNombreFin());
                            ps.setString(contBD++, nuevaContratacion.getApellido1Fin());
                            ps.setString(contBD++, nuevaContratacion.getApellido2Fin());
                            ps.setString(contBD++, nuevaContratacion.getDninieFin());
                            ps.setString(contBD++, nuevaContratacion.getSexoFin());
                            ps.setString(contBD++, nuevaContratacion.getGrupocotizFin());
                            ps.setInt(contBD++, (nuevaContratacion.getDurcontratoFin() != null && !nuevaContratacion.getDurcontratoFin().toString().isEmpty() ? nuevaContratacion.getDurcontratoFin() : 0));
                            ps.setDate(contBD++, nuevaContratacion.getFechainicioFin());
                            ps.setDate(contBD++, nuevaContratacion.getFechafinFin());
                            ps.setDouble(contBD++, (nuevaContratacion.getRetribucionbrutaFin() != null ? nuevaContratacion.getRetribucionbrutaFin() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getCostesalarialFin() != null ? nuevaContratacion.getCostesalarialFin() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getCostesssFin() != null ? nuevaContratacion.getCostesssFin() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getIndemfincontratoFin() != null ? nuevaContratacion.getIndemfincontratoFin() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getCostetotalrealFin() != null ? nuevaContratacion.getCostetotalrealFin() : 0.0));
                            ps.setDouble(contBD++, (nuevaContratacion.getSubvconcedidalanFin() != null ? nuevaContratacion.getSubvconcedidalanFin() : 0.0));

                            log.debug("sql = " + query);
                            log.debug("PARÁMETROS: " + id + " - " + nuevaContratacion.getPrioridad() + " - " + nuevaContratacion.getMunicipioFin() + " - " + nuevaContratacion.getNombreFin() + " " + nuevaContratacion.getApellido1Fin() + " " + nuevaContratacion.getApellido2Fin() + " - " + nuevaContratacion.getDninieFin() + " - " + nuevaContratacion.getSexoInicio() + " - " + nuevaContratacion.getGrupocotizFin()
                                    + " - " + nuevaContratacion.getDurcontratoFin() + " - " + nuevaContratacion.getFechainicioFin() + " - " + nuevaContratacion.getFechafinFin() + " - " + nuevaContratacion.getRetribucionbrutaFin() + " - " + nuevaContratacion.getCostesalarialFin() + " - " + nuevaContratacion.getCostesssFin() + " - " + nuevaContratacion.getIndemfincontratoFin() + " - " + nuevaContratacion.getCostetotalrealFin() + " - " + nuevaContratacion.getSubvconcedidalanFin());

                            if (ps.executeUpdate() > 0) {
                                resultado = true;
                            } else {
                                log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES));
                                resultado = false;
                            }
                            log.info("El expediente " + nuevaContratacion.getNumExp() + " tiene datos en las 3 tablas");
                        } else {
                            log.info("El expediente " + nuevaContratacion.getNumExp() + " solo tiene datos en las tabla CONTRATACIONES y CONTRATACIONES_INI");
                        }
                    } else {
                        log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES));
                        resultado = false;
                    }
                } else {
                    log.info("El expediente " + nuevaContratacion.getNumExp() + " solo tiene datos en la tabla CONTRATACIONES");
                }
            } else {
                log.error("No Se ha podido guardar una nueva Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva Contratación " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    public boolean modificarContratacion(FilaContratacionVO contrataModif, int tablas, Connection con) throws Exception {
        log.info("Entramos en modificarContratacion DAO - " + contrataModif.getNumExp() + " Tablas: " + tablas);
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        int id = 0;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " SET PRIORIDAD=?, DENOMPUESTO = ?, NIVELCUALIFICACION=?, MODCONTRATO=?, DURCONTRATO=?,  GRUPOCOTIZ=?, COSTESALARIAL=?,  SUBVSOLICITADA=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, contrataModif.getPrioridad());
            ps.setString(contBD++, contrataModif.getDenomPuesto());
            ps.setString(contBD++, contrataModif.getNivelCualificacion());
            ps.setString(contBD++, contrataModif.getModContrato());
            ps.setInt(contBD++, contrataModif.getDurContrato());
            ps.setString(contBD++, contrataModif.getGrupoCotiz());
            ps.setDouble(contBD++, contrataModif.getCostesalarial());
            ps.setDouble(contBD++, contrataModif.getSubvsolicitada());
            ps.setString(contBD++, contrataModif.getNumExp());
            ps.setInt(contBD++, contrataModif.getId());
            log.debug("PARÁMETROS: " + contrataModif.getPrioridad() + " - " + contrataModif.getDenomPuesto() + " - " + contrataModif.getNivelCualificacion() + " - " + contrataModif.getModContrato() + " - " + contrataModif.getDurContrato() + " - " + contrataModif.getGrupoCotiz() + " - " + contrataModif.getCostesalarial() + " - " + contrataModif.getSubvsolicitada());
            log.debug("ID: " + contrataModif.getId());
            if (ps.executeUpdate() > 0) {
                resultado = true;
                if (tablas != 1) {
                    contBD = 1;
                    if (existeContratacion(contrataModif.getNumExp(), contrataModif.getPrioridad(), 2, con)) {
                        query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                                + " SET MUNICIPIO2 = ?, NOMBRE2 = ?, APELLIDO12=?, APELLIDO22=?, DNINIE2=?,  FECHANACIMIENTO2=?, SEXO2=?,  NIVELCUALIFICACION2=?, PUESTOTRABAJO2 =?, NOFERTA2 =?, "
                                + " GRUPOCOTIZ2 =?, DURACION2 =?, FECHAINICIO2 =?, EDAD2 =?, RETRIBUCIONBRUTA2 =?"
                                + " WHERE NUM_EXP = ? AND NUM_PUESTO = ?";
                        ps = con.prepareStatement(query);

                        ps.setString(contBD++, contrataModif.getMunicipioInicio());
                        ps.setString(contBD++, contrataModif.getNombreInicio());
                        ps.setString(contBD++, contrataModif.getApellido1Inicio());
                        ps.setString(contBD++, contrataModif.getApellido2Inicio());
                        ps.setString(contBD++, contrataModif.getDninieInicio());
                        ps.setDate(contBD++, contrataModif.getFechanacimientoInicio());
                        ps.setString(contBD++, contrataModif.getSexoInicio());
                        ps.setString(contBD++, contrataModif.getNivelcualificacionInicio());
                        ps.setString(contBD++, contrataModif.getPuestotrabajoInicio());
                        ps.setString(contBD++, contrataModif.getNofertaInicio());
                        ps.setString(contBD++, contrataModif.getGrupocotizInicio());
                        ps.setInt(contBD++, (contrataModif.getDurcontratoInicio() != null && !contrataModif.getDurcontratoInicio().toString().isEmpty() ? contrataModif.getDurcontratoInicio() : 0));
                        ps.setDate(contBD++, contrataModif.getFechainicioInicio());
                        ps.setInt(contBD++, (contrataModif.getEdadInicio() != null && !contrataModif.getEdadInicio().toString().isEmpty() ? contrataModif.getEdadInicio() : 0));
                        ps.setDouble(contBD++, (contrataModif.getRetribucionbrutaInicio() != null ? contrataModif.getRetribucionbrutaInicio() : 0.0));

                        ps.setString(contBD++, contrataModif.getNumExp());
                        ps.setInt(contBD++, contrataModif.getPrioridad());// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
                        log.debug("PARÁMETROS:  Mun: " + contrataModif.getMunicipioInicio()
                                + " Nombre: " + contrataModif.getNombreInicio() + " " + contrataModif.getApellido1Inicio() + " " + contrataModif.getApellido2Inicio() + " - " + contrataModif.getDninieInicio()
                                + " - " + contrataModif.getFechanacimientoInicio() + " - " + contrataModif.getSexoInicio() + " Nivel: " + contrataModif.getNivelcualificacionInicio() + " Puesto: " + contrataModif.getPuestotrabajoInicio()
                                + " Oferta: " + contrataModif.getNofertaInicio() + " Grupo: " + contrataModif.getGrupocotizInicio() + " Dura: " + contrataModif.getDurcontratoInicio()
                                + " F Ini: " + contrataModif.getFechainicioInicio() + " Edad: " + contrataModif.getEdadInicio() + " Retr: " + contrataModif.getRetribucionbrutaInicio());
                        log.debug("Nº puesto " + contrataModif.getPrioridad());
                    } else {
                        id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide82.SEQ_MELANBIDE82_CONTRATA_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES), con);
                        query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                                + "(ID, NUM_EXP, NUM_PUESTO, MUNICIPIO2, NOMBRE2, APELLIDO12, APELLIDO22, DNINIE2, FECHANACIMIENTO2, SEXO2, "
                                + "NIVELCUALIFICACION2, PUESTOTRABAJO2, NOFERTA2, GRUPOCOTIZ2, DURACION2, FECHAINICIO2, EDAD2, RETRIBUCIONBRUTA2) "
                                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        ps = con.prepareStatement(query);
                        ps.setInt(contBD++, id);
                        ps.setString(contBD++, contrataModif.getNumExp());
                        ps.setInt(contBD++, contrataModif.getPrioridad());// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
                        ps.setString(contBD++, contrataModif.getMunicipioInicio());
                        ps.setString(contBD++, contrataModif.getNombreInicio());
                        ps.setString(contBD++, contrataModif.getApellido1Inicio());
                        ps.setString(contBD++, contrataModif.getApellido2Inicio());
                        ps.setString(contBD++, contrataModif.getDninieInicio());
                        ps.setDate(contBD++, contrataModif.getFechanacimientoInicio());
                        ps.setString(contBD++, contrataModif.getSexoInicio());
                        ps.setString(contBD++, contrataModif.getNivelcualificacionInicio());
                        ps.setString(contBD++, contrataModif.getPuestotrabajoInicio());
                        ps.setString(contBD++, contrataModif.getNofertaInicio());
                        ps.setString(contBD++, contrataModif.getGrupocotizInicio());
                        ps.setInt(contBD++, (contrataModif.getDurcontratoInicio() != null && !contrataModif.getDurcontratoInicio().toString().isEmpty() ? contrataModif.getDurcontratoInicio() : 0));
                        ps.setDate(contBD++, contrataModif.getFechainicioInicio());
                        ps.setInt(contBD++, (contrataModif.getEdadInicio() != null && !contrataModif.getEdadInicio().toString().isEmpty() ? contrataModif.getEdadInicio() : 0));
                        ps.setDouble(contBD++, (contrataModif.getRetribucionbrutaInicio() != null ? contrataModif.getRetribucionbrutaInicio() : 0.0));
                        log.debug("PARÁMETROS: id: " + id + " Prior:  " + contrataModif.getPrioridad() + " Mun: " + contrataModif.getMunicipioInicio()
                                + " Nombre: " + contrataModif.getNombreInicio() + " " + contrataModif.getApellido1Inicio() + " " + contrataModif.getApellido2Inicio() + " - " + contrataModif.getDninieInicio()
                                + " - " + contrataModif.getFechanacimientoInicio() + " - " + contrataModif.getSexoInicio() + " Nivel: " + contrataModif.getNivelcualificacionInicio() + " Puesto: " + contrataModif.getPuestotrabajoInicio()
                                + " Oferta: " + contrataModif.getNofertaInicio() + " Grupo: " + contrataModif.getGrupocotizInicio() + " Dura: " + contrataModif.getDurcontratoInicio()
                                + " F Ini: " + contrataModif.getFechainicioInicio() + " Edad: " + contrataModif.getEdadInicio() + " Retr: " + contrataModif.getRetribucionbrutaInicio());

                    }
                    log.debug("sql = " + query);

                    if (ps.executeUpdate() > 0) {
                        resultado = true;
                        if (tablas == 3) {
                            contBD = 1;
                            if (existeContratacion(contrataModif.getNumExp(), contrataModif.getPrioridad(), 3, con)) {
                                query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                                        + " SET MUNICIPIO3 = ?,  NOMBRE3=?, APELLIDO13=?, APELLIDO23=?,  DNINIE3=?, SEXO3=?, GRUPOCOTIZ3 =?, DURACION3 =?, FECHAINICIO3 =?"
                                        + ", FECHAFIN3=?,  RETRIBUCIONBRUTA3=?, COSTESALARIAL3=?,  COSTESSS3=?, INDEMFINCONTRATO3 =?, COSTETOTALREAL3 =?, SUBVCONCEDIDALAN3 =?"
                                        + " WHERE NUM_EXP = ? AND NUM_PUESTO = ?";
                                ps = con.prepareStatement(query);

                                ps.setString(contBD++, contrataModif.getMunicipioFin());
                                ps.setString(contBD++, contrataModif.getNombreFin());
                                ps.setString(contBD++, contrataModif.getApellido1Fin());
                                ps.setString(contBD++, contrataModif.getApellido2Fin());
                                ps.setString(contBD++, contrataModif.getDninieFin());
                                ps.setString(contBD++, contrataModif.getSexoFin());
                                ps.setString(contBD++, contrataModif.getGrupocotizFin());
                                ps.setInt(contBD++, (contrataModif.getDurcontratoFin() != null && !contrataModif.getDurcontratoFin().toString().isEmpty() ? contrataModif.getDurcontratoFin() : 0));
                                ps.setDate(contBD++, contrataModif.getFechainicioFin());
                                ps.setDate(contBD++, contrataModif.getFechafinFin());
                                ps.setDouble(contBD++, (contrataModif.getRetribucionbrutaFin() != null ? contrataModif.getRetribucionbrutaFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostesalarialFin() != null ? contrataModif.getCostesalarialFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostesssFin() != null ? contrataModif.getCostesssFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getIndemfincontratoFin() != null ? contrataModif.getIndemfincontratoFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostetotalrealFin() != null ? contrataModif.getCostetotalrealFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getSubvconcedidalanFin() != null ? contrataModif.getSubvconcedidalanFin() : 0.0));

                                ps.setString(contBD++, contrataModif.getNumExp());
                                ps.setInt(contBD++, contrataModif.getPrioridad());// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
                                log.debug("PARÁMETROS:  Mun: " + contrataModif.getMunicipioFin()
                                        + " Nombre: " + contrataModif.getNombreFin() + " " + contrataModif.getApellido1Fin() + " " + contrataModif.getApellido2Fin() + " - " + contrataModif.getDninieFin()
                                        + " - " + contrataModif.getSexoFin() + " Grupo: " + contrataModif.getGrupocotizFin() + " Dura: " + contrataModif.getDurcontratoFin()
                                        + " F Ini: " + contrataModif.getFechainicioFin() + " F Fin: " + contrataModif.getFechafinFin() + " Retr: " + contrataModif.getRetribucionbrutaFin());
                                log.debug("Nº puesto " + contrataModif.getPrioridad());
                            } else {
                                id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide82.SEQ_MELANBIDE82_CONTRATA_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES), con);
                                query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                                        + "(ID, NUM_EXP, NUM_PUESTO, MUNICIPIO3, NOMBRE3, APELLIDO13, APELLIDO23, DNINIE3, SEXO3, GRUPOCOTIZ3,"
                                        + " DURACION3, FECHAINICIO3, FECHAFIN3, RETRIBUCIONBRUTA3, COSTESALARIAL3, COSTESSS3, INDEMFINCONTRATO3, COSTETOTALREAL3, SUBVCONCEDIDALAN3) "
                                        + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                                ps = con.prepareStatement(query);

                                ps.setInt(contBD++, id);
                                ps.setString(contBD++, contrataModif.getNumExp());
                                ps.setInt(contBD++, contrataModif.getPrioridad());// El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
                                ps.setString(contBD++, contrataModif.getMunicipioFin());
                                ps.setString(contBD++, contrataModif.getNombreFin());
                                ps.setString(contBD++, contrataModif.getApellido1Fin());
                                ps.setString(contBD++, contrataModif.getApellido2Fin());
                                ps.setString(contBD++, contrataModif.getDninieFin());
                                ps.setString(contBD++, contrataModif.getSexoFin());
                                ps.setString(contBD++, contrataModif.getGrupocotizFin());
                                ps.setInt(contBD++, (contrataModif.getDurcontratoFin() != null && !contrataModif.getDurcontratoFin().toString().isEmpty() ? contrataModif.getDurcontratoFin() : 0));
                                ps.setDate(contBD++, contrataModif.getFechainicioFin());
                                ps.setDate(contBD++, contrataModif.getFechafinFin());
                                ps.setDouble(contBD++, (contrataModif.getRetribucionbrutaFin() != null ? contrataModif.getRetribucionbrutaFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostesalarialFin() != null ? contrataModif.getCostesalarialFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostesssFin() != null ? contrataModif.getCostesssFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getIndemfincontratoFin() != null ? contrataModif.getIndemfincontratoFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getCostetotalrealFin() != null ? contrataModif.getCostetotalrealFin() : 0.0));
                                ps.setDouble(contBD++, (contrataModif.getSubvconcedidalanFin() != null ? contrataModif.getSubvconcedidalanFin() : 0.0));

                                log.debug("sql = " + query);
                                log.debug("PARÁMETROS: " + id + " - " + contrataModif.getPrioridad() + " - " + contrataModif.getMunicipioFin()
                                        + " Nombre: " + contrataModif.getNombreFin() + " " + contrataModif.getApellido1Fin() + " " + contrataModif.getApellido2Fin() + " - " + contrataModif.getDninieFin()
                                        + " - " + contrataModif.getSexoFin() + " Grupo: " + contrataModif.getGrupocotizFin() + " Dura: " + contrataModif.getDurcontratoFin()
                                        + " F Ini: " + contrataModif.getFechainicioFin() + " F Fin: " + contrataModif.getFechafinFin() + " Retr: " + contrataModif.getRetribucionbrutaFin() + " - " + contrataModif.getCostesalarialFin()
                                        + " - " + contrataModif.getCostesssFin() + " - " + contrataModif.getIndemfincontratoFin() + " - " + contrataModif.getCostetotalrealFin() + " - " + contrataModif.getSubvconcedidalanFin());
                            }
                            if (ps.executeUpdate() > 0) {
                                resultado = true;
                            } else {
                                log.error("Se ha producido un error al Modificar una Contratación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES));
                                resultado = false;
                            }
                            log.info("El expediente " + contrataModif.getNumExp() + " tiene datos en las 3 tablas");
                        } else {
                            log.info("El expediente " + contrataModif.getNumExp() + " solo tiene datos para insertar en las tabla CONTRATACIONES y CONTRATACIONES_INI");
                        }
                    } else {
                        log.error("No Se ha podido modificar una  Contratación INICIO en " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES));
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
        Statement st = null;
        String query = null;
        int numPuesto = 0;
        int resultado = 0;
        try {
            numPuesto = getNumPuesto(numExp, id, con);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND NUM_PUESTO=" + numPuesto;
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND NUM_PUESTO=" + numPuesto;
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado += st.executeUpdate(query);

            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
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
        // El nº de puesto se corresponde con PRIORIDAD de la tabla contrataciones
        Statement st = null;
        ResultSet rs = null;
        int numPuesto = 0;
        try {
            String query = "SELECT PRIORIDAD FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "' AND ID=" + id;
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numPuesto = rs.getInt("PRIORIDAD");
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

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "' order by DES_NOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide82MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
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
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_INI, ConstantesMeLanbide82.FICHERO_PROPIEDADES);
            } else if (fase == 3) {
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES);
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

    public boolean existePrioridad(String numExp, int prioridad, Connection con) throws Exception {
        log.info("Entramos en existePrioridad DAO - " + numExp + " Puesto: " + prioridad);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "";
        boolean existe = false;
        try {
            query = "SELECT COUNT(*) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.MELANBIDE82_CONTRATACION, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP=? AND PRIORIDAD=?";

            log.debug("SQL " + query);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, prioridad);
            rs = ps.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                existe = true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si existe la prioridad " + prioridad + " del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return existe;
    }
    
    public void setFicheroCVoDemandaIntermediacion(String numExpediente, File valor, String campo, BigDecimal txt_mun, BigDecimal txt_eje, Connection con) throws Exception {
        log.info("INICIO setFicheroCVoDemandaIntermediacion DAO");

        Statement st = null;
        ResultSet rs = null;
        try {
            // Comprobamos si ya existe información del campo en BBDD
            StringBuffer query = new StringBuffer();
            query.append("SELECT TFI_VALOR ");
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " ");
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
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " ");
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
            String sql = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " "
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
            String sql = "UPDATE E_TFI " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
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
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " ");
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
            String sql = "UPDATE E_TFI " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
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
            String sql = "UPDATE E_TFE " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide82.FICHERO_PROPIEDADES)
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
    
    public BigDecimal getMUNDatosIntermediacionExterno(String numExpediente, Connection con) throws Exception {
        log.info("INICIO getMUNDatosIntermediacionExterno DAO");
        BigDecimal txt_mun = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT  TXT_MUN ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                txt_mun = rs.getBigDecimal("TXT_MUN");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getMUNDatosIntermediacionExterno : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getMUNDatosIntermediacionExterno - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getMUNDatosIntermediacionExterno - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getMUNDatosIntermediacionExterno DAO");
        return txt_mun;
    }   
    
    public PersonaContratadaVO getPersonaContratadaExterno(String nifPersonaContratada, Connection con) throws Exception {
        log.info("INICIO getPersonaContratadaExterno DAO");
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();
        personaContratada.setDni(nifPersonaContratada);

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT * ");
            query.append("FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide82.VISTA_SIST_GARANTIA_JUVE, ConstantesMeLanbide82.FICHERO_PROPIEDADES) + " pc ");
            query.append("WHERE pc.GEN_PER_NUM_DOC = '" + nifPersonaContratada + "'");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            if (rs.next()) {
                personaContratada.setAltaSistemaGJ("S");
            } else {
                personaContratada.setAltaSistemaGJ("N");
            }
        } catch (Exception ex) {
            log.error("Excepcion - getPersonaContratadaExterno : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getPersonaContratadaExterno - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getPersonaContratadaExterno - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getPersonaContratadaExterno DAO");
        return personaContratada;
    }

    public BigDecimal getEJEDatosIntermediacionExterno(String numExpediente, Connection con) throws Exception {
        log.info("INICIO getEJEDatosIntermediacionExterno DAO");
        BigDecimal txt_eje = null;

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();

            query.append("SELECT  TXT_EJE ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numExpediente);
            query.append("' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

            if (rs.next()) {
                txt_eje = rs.getBigDecimal("TXT_EJE");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getEJEDatosIntermediacionExterno : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getEJEDatosIntermediacionExterno - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("getEJEDatosIntermediacionExterno - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getEJEDatosIntermediacionExterno DAO");
        return txt_eje;
    }   
    
    public boolean setEstadoSistemaGJ(PersonaContratadaVO personaContratada, 
            final String tabla, final Integer id, final String nombreCampo, Connection con) throws Exception {
        PreparedStatement st = null;
        String query = "";
        
        try {
            int i = 1;
            query = "UPDATE " + tabla
                    + " SET " + nombreCampo + "=?  WHERE ID=?";
            st = con.prepareStatement(query);
            st.setString(i++, personaContratada.getAltaSistemaGJ());
            st.setInt(i++, id);
            
            log.debug("sql = " + query);

            return st.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.debug("Se ha producido un error en setEstadoSistemaGJ - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }    
}
