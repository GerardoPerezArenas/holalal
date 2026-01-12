package es.altia.flexia.integracion.moduloexterno.melanbide65.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide65.exception.MeLanbide65Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.ConstantesMeLanbide65;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.MeLanbide65MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.ExpUAAPCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.ExpedienteUAAPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.PersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.TrabajadorVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide65DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide65DAO.class);
    //Instancia
    private static MeLanbide65DAO instance = null;

    // Constructor
    private MeLanbide65DAO() {

    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegabe = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide65.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide65.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide65MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
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

    //Devolvemos una �nica instancia de la clase a trav�s de este m�todo ya que el constructor es privado
    public static MeLanbide65DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide65DAO.class) {
                instance = new MeLanbide65DAO();
            }
        }
        return instance;
    }

    public ArrayList<PersonaVO> obtenerDatos(String tabla, String numExpediente, Connection con) throws MeLanbide65Exception, SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query;
        String selectComun;
        String restoQuery;
        Integer valorIntAux;
        String valorStringAux;
        Double valorDoubleAux;
        ArrayList<PersonaVO> lista = new ArrayList<PersonaVO>();

        try {
            selectComun = "SELECT ID, NUM_EXP, APELLIDO1, APELLIDO2, NOMBRE, DNI, ";
            restoQuery = " FROM " + tabla + " WHERE NUM_EXP=? ORDER BY ID";

            if (tabla.contains(ConstantesMeLanbide65.getSUF_TRABAJADOR())) {
                query = new StringBuilder(selectComun).append("TIPDISCPSI, TIPDISCFIS,TIPDISCSEN, TIPCONTRATO, TIPJORNADA, JORPARCPORC, SEXO, PENSIONISTA, TIPPENSIONISTA").append(restoQuery);
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                ps.setString(1, numExpediente);
                rs = ps.executeQuery();
                while (rs.next()) {
                    TrabajadorVO trabaj = new TrabajadorVO();
                    //Guardo en el objeto TrabajadorVO datos comunes a cualquier empleado
                    obtenerDatosComunes(trabaj, rs);
                    //Guardo el resto de datos, propios de TRABAJADOR 

                    valorStringAux = rs.getString("TIPCONTRATO");
                    if (rs.wasNull()) {
                        trabaj.setTipoContrato(null);
                    } else {
                        trabaj.setTipoContrato(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPJORNADA");
                    if (rs.wasNull()) {
                        trabaj.setTipoJornada(null);
                    } else {
                        trabaj.setTipoJornada(valorStringAux);
                    }

                    valorDoubleAux = rs.getDouble("JORPARCPORC");
                    if (rs.wasNull()) {
                        trabaj.setJorParcPorc(-1);
                    } else {
                        trabaj.setJorParcPorc(valorDoubleAux);
                    }

                    valorIntAux = rs.getInt("TIPDISCPSI");
                    if (rs.wasNull()) {
                        trabaj.setTipoDiscPsiquica(-1);
                    } else {
                        trabaj.setTipoDiscPsiquica(valorIntAux);
                    }

                    valorIntAux = rs.getInt("TIPDISCFIS");
                    if (rs.wasNull()) {
                        trabaj.setTipoDiscFisica(-1);
                    } else {
                        trabaj.setTipoDiscFisica(valorIntAux);
                    }

                    valorIntAux = rs.getInt("TIPDISCSEN");
                    if (rs.wasNull()) {
                        trabaj.setTipoDiscSensorial(-1);
                    } else {
                        trabaj.setTipoDiscSensorial(valorIntAux);
                    }

                    trabaj.setSexo(Integer.parseInt(rs.getString("SEXO")));

                    valorStringAux = rs.getString("PENSIONISTA");
                    if (rs.wasNull()) {
                        trabaj.setPensionista(null);
                    } else {
                        trabaj.setPensionista(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPPENSIONISTA");
                    if (rs.wasNull()) {
                        trabaj.setTipoPensionista(null);
                    } else {
                        trabaj.setTipoPensionista(valorStringAux);
                    }

                    lista.add(trabaj);
                }
            } else {
                query = new StringBuilder(selectComun).append("FECALTACONTINDEF,FECALTACONTINDEFTEMP,FECBAJACONTINDEFTEMP,JORPARCPORC, TIPCONTRATO, TIPJORNADA,PENSIONISTA, TIPPENSIONISTA").append(restoQuery);
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                ps.setString(1, numExpediente);
                rs = ps.executeQuery();
                while (rs.next()) {
                    EncargadoVO encarg = new EncargadoVO();
                    //Guardo en el objeto TecnicoVO datos comunes a cualquier empleado
                    obtenerDatosComunes(encarg, rs);
                    //Guardo el resto de datos, propios de TECNICO
                    encarg.setFecAltaContrIndef(rs.getDate("FECALTACONTINDEF"));

                    encarg.setFecAltaContrTemp(rs.getDate("FECALTACONTINDEFTEMP"));

                    encarg.setFecBajaContrTemp(rs.getDate("FECBAJACONTINDEFTEMP"));

                    valorDoubleAux = rs.getDouble("JORPARCPORC");
                    if (rs.wasNull()) {
                        encarg.setJornadaParcialPor(-1);
                    } else {
                        encarg.setJornadaParcialPor(valorDoubleAux);
                    }

                    valorStringAux = rs.getString("TIPCONTRATO");
                    if (rs.wasNull()) {
                        encarg.setTipoContrato(null);
                    } else {
                        encarg.setTipoContrato(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPJORNADA");
                    if (rs.wasNull()) {
                        encarg.setTipoJornada(null);
                    } else {
                        encarg.setTipoJornada(valorStringAux);
                    }

                    valorStringAux = rs.getString("PENSIONISTA");
                    if (rs.wasNull()) {
                        encarg.setPensionista(null);
                    } else {
                        encarg.setPensionista(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPPENSIONISTA");
                    if (rs.wasNull()) {
                        encarg.setTipoPensionista(null);
                    } else {
                        encarg.setTipoPensionista(valorStringAux);
                    }

                    lista.add(encarg);
                }
            }

        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al obtener la relaci�n de registros de " + tabla + " para el expediente " + numExpediente);
            throw new MeLanbide65Exception(3, "Ha ocurrido un error al recuperar registros");
        } catch (NumberFormatException nfe) {
            log.error("Error al obtener el sexo del trabajador");
            throw new MeLanbide65Exception(2, "Error al parsear datos de un registro de la tabla " + tabla);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return lista;
    }

    public boolean insertar(PersonaVO persona, String tabla, String numExpediente, Connection con) throws MeLanbide65Exception, SQLException {
        PreparedStatement ps = null;
        int insertado = 0;
        String insertComun;
        String valoresComun;
        StringBuilder query;
        int contbd = 1;
        TrabajadorVO trabaj = null;
        EncargadoVO encarg = null;

        try {
            insertComun = "INSERT INTO " + tabla + "(ID,NUM_EXP,APELLIDO1,APELLIDO2,NOMBRE,DNI,";
            valoresComun = " VALUES (" + obtenerSecuencia(tabla) + ".nextval,?,?,?,?,?,";

            if (tabla.contains(ConstantesMeLanbide65.getSUF_TRABAJADOR())) {
                query = new StringBuilder(insertComun)
                        .append("TIPDISCPSI,TIPDISCFIS,TIPDISCSEN,TIPCONTRATO,TIPJORNADA,JORPARCPORC,SEXO,PENSIONISTA, TIPPENSIONISTA)")
                        .append(valoresComun).append("?,?,?,?,?,?,?,?,?)");
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                contbd = establecerDatosComunes(persona, numExpediente, ps);
                trabaj = (TrabajadorVO) persona;

                if (trabaj.getTipoDiscPsiquica() == -1) {
                    ps.setNull(contbd++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(contbd++, trabaj.getTipoDiscPsiquica());
                }

                if (trabaj.getTipoDiscFisica() == -1) {
                    ps.setNull(contbd++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(contbd++, trabaj.getTipoDiscFisica());
                }

                if (trabaj.getTipoDiscSensorial() == -1) {
                    ps.setNull(contbd++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(contbd++, trabaj.getTipoDiscSensorial());
                }

                if (trabaj.getTipoContrato() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getTipoContrato());
                }

                if (trabaj.getTipoJornada() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getTipoJornada());
                }

                if (trabaj.getJorParcPorc() == -1.0) {
                    ps.setNull(contbd++, java.sql.Types.DOUBLE);
                } else {
                    ps.setDouble(contbd++, trabaj.getJorParcPorc());
                }

                ps.setString(contbd++, String.valueOf(trabaj.getSexo()));

                if (trabaj.getPensionista() == null || trabaj.getPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getPensionista());
                }

                if (trabaj.getTipoPensionista() == null || trabaj.getTipoPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getTipoPensionista());
                }

                insertado = ps.executeUpdate();
            } else {
                query = new StringBuilder(insertComun).append("FECALTACONTINDEF,FECALTACONTINDEFTEMP,FECBAJACONTINDEFTEMP,JORPARCPORC,TIPCONTRATO,TIPJORNADA,PENSIONISTA, TIPPENSIONISTA)")
                        .append(valoresComun).append("?,?,?,?,?,?,?,?)");
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                contbd = establecerDatosComunes(persona, numExpediente, ps);
                encarg = (EncargadoVO) persona;

                if (encarg.getFecAltaContrIndef() == null) {
                    ps.setNull(contbd++, java.sql.Types.DATE);
                } else {
                    ps.setDate(contbd++, new java.sql.Date(encarg.getFecAltaContrIndef().getTime()));
                }

                if (encarg.getFecAltaContrTemp() == null) {
                    ps.setNull(contbd++, java.sql.Types.DATE);
                } else {
                    ps.setDate(contbd++, new java.sql.Date(encarg.getFecAltaContrTemp().getTime()));
                }

                if (encarg.getFecBajaContrTemp() == null) {
                    ps.setNull(contbd++, java.sql.Types.DATE);
                } else {
                    ps.setDate(contbd++, new java.sql.Date(encarg.getFecBajaContrTemp().getTime()));
                }

                if (encarg.getJornadaParcialPor() == -1.0) {
                    ps.setNull(contbd++, java.sql.Types.DOUBLE);
                } else {
                    ps.setDouble(contbd++, encarg.getJornadaParcialPor());
                }

                if (encarg.getTipoContrato() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getTipoContrato());
                }

                if (encarg.getTipoJornada() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getTipoJornada());
                }

                if (encarg.getPensionista() == null || encarg.getPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getPensionista());
                }

                if (encarg.getTipoPensionista() == null || encarg.getTipoPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getTipoPensionista());
                }

                insertado = ps.executeUpdate();

            }
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al dar de alta un registro en " + tabla);
            throw new MeLanbide65Exception(4, "Ha ocurrido un error en la operaci�n de inserci�n en BBDD");
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return insertado == 1;
    }

    public PersonaVO obtenerRegPorId(String tabla, int id, Connection con) throws MeLanbide65Exception, SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query;
        String selectComun;
        String restoQuery;
        PersonaVO persona = null;
        Integer valorIntAux;
        String valorStringAux;
        Double valorDoubleAux;

        try {
            selectComun = "SELECT ID, NUM_EXP, APELLIDO1, APELLIDO2, NOMBRE, DNI, ";
            restoQuery = " FROM " + tabla + " WHERE ID=?";

            if (tabla.contains(ConstantesMeLanbide65.getSUF_TRABAJADOR())) {
                TrabajadorVO trabaj = new TrabajadorVO();

                query = new StringBuilder(selectComun).append("TIPDISCPSI, TIPDISCFIS,TIPDISCSEN,TIPCONTRATO, TIPJORNADA, JORPARCPORC , SEXO , PENSIONISTA, TIPPENSIONISTA").append(restoQuery);
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                ps.setInt(1, id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    //Guardo en el objeto TrabajadorVO datos comunes a cualquier empleado
                    obtenerDatosComunes(trabaj, rs);
                    //Guardo el resto de datos, propios de TRABAJADOR

                    valorStringAux = rs.getString("TIPCONTRATO");
                    if (rs.wasNull()) {
                        trabaj.setTipoContrato("0");
                    } else {
                        trabaj.setTipoContrato(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPJORNADA");
                    if (rs.wasNull()) {
                        trabaj.setTipoJornada("0");
                    } else {
                        trabaj.setTipoJornada(valorStringAux);
                    }

                    valorDoubleAux = rs.getDouble("JORPARCPORC");
                    if (rs.wasNull()) {
                        trabaj.setJorParcPorc(-1);
                    } else {
                        trabaj.setJorParcPorc(valorDoubleAux);
                    }

                    valorIntAux = rs.getInt("TIPDISCPSI");
                    if (rs.wasNull()) {
                        trabaj.setTipoDiscPsiquica(-1);
                    } else {
                        trabaj.setTipoDiscPsiquica(valorIntAux);
                    }

                    valorIntAux = rs.getInt("TIPDISCFIS");
                    if (rs.wasNull()) {
                        trabaj.setTipoDiscFisica(-1);
                    } else {
                        trabaj.setTipoDiscFisica(valorIntAux);
                    }

                    valorIntAux = rs.getInt("TIPDISCSEN");
                    if (rs.wasNull()) {
                        trabaj.setTipoDiscSensorial(-1);
                    } else {
                        trabaj.setTipoDiscSensorial(valorIntAux);
                    }

                    trabaj.setSexo(Integer.parseInt(rs.getString("SEXO")));

                    valorStringAux = rs.getString("PENSIONISTA");
                    if (rs.wasNull()) {
                        trabaj.setPensionista("0");
                    } else {
                        trabaj.setPensionista(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPPENSIONISTA");
                    if (rs.wasNull()) {
                        trabaj.setTipoPensionista("0");
                    } else {
                        trabaj.setTipoPensionista(valorStringAux);
                    }
                }
                persona = trabaj;
            } else {
                EncargadoVO encarg = new EncargadoVO();

                query = new StringBuilder(selectComun).append("FECALTACONTINDEF,FECALTACONTINDEFTEMP,FECBAJACONTINDEFTEMP,JORPARCPORC,TIPCONTRATO, TIPJORNADA, PENSIONISTA, TIPPENSIONISTA").append(restoQuery);
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                ps.setInt(1, id);
                rs = ps.executeQuery();
                while (rs.next()) {
                    //Guardo en el objeto EncargadoVO datos comunes a cualquier empleado
                    obtenerDatosComunes(encarg, rs);
                    //Guardo el resto de datos, propios de TRABAJADOR
                    encarg.setFecAltaContrIndef(rs.getDate("FECALTACONTINDEF"));

                    encarg.setFecAltaContrTemp(rs.getDate("FECALTACONTINDEFTEMP"));

                    encarg.setFecBajaContrTemp(rs.getDate("FECBAJACONTINDEFTEMP"));

                    valorDoubleAux = rs.getDouble("JORPARCPORC");
                    if (rs.wasNull()) {
                        encarg.setJornadaParcialPor(-1);
                    } else {
                        encarg.setJornadaParcialPor(valorDoubleAux);
                    }

                    valorStringAux = rs.getString("TIPCONTRATO");
                    if (rs.wasNull()) {
                        encarg.setTipoContrato("0");
                    } else {
                        encarg.setTipoContrato(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPJORNADA");
                    if (rs.wasNull()) {
                        encarg.setTipoJornada("0");
                    } else {
                        encarg.setTipoJornada(valorStringAux);
                    }

                    valorStringAux = rs.getString("PENSIONISTA");
                    if (rs.wasNull()) {
                        encarg.setPensionista("0");
                    } else {
                        encarg.setPensionista(valorStringAux);
                    }

                    valorStringAux = rs.getString("TIPPENSIONISTA");
                    if (rs.wasNull()) {
                        encarg.setTipoPensionista("0");
                    } else {
                        encarg.setTipoPensionista(valorStringAux);
                    }
                }
                persona = encarg;
            }

        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al obtener el registro con id " + id + " de la tabla " + tabla);
            throw new MeLanbide65Exception(5, "Ha ocurrido un error al recuperar un registro de BBDD");
        } catch (NumberFormatException nfe) {
            log.error("Error al obtener el sexo del trabajador");
            throw new MeLanbide65Exception(2, "Error al mapear datos de un registro de la tabla " + tabla);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return persona;
    }

    public boolean actualizar(PersonaVO persona, int ident, String tabla, String numExpediente, Connection con) throws MeLanbide65Exception {
        PreparedStatement ps = null;
        int actualizado = 0;
        String updateComun;
        String whereComun;
        StringBuilder query;
        int contbd = 1;
        TrabajadorVO trabaj = null;
        EncargadoVO encarg = null;

        try {
            updateComun = "UPDATE " + tabla + " SET NUM_EXP=?, APELLIDO1=?, APELLIDO2=?, NOMBRE=?, DNI=?,";
            whereComun = " WHERE ID=?";

            if (tabla.contains(ConstantesMeLanbide65.getSUF_TRABAJADOR())) {
                query = new StringBuilder(updateComun).append(" TIPDISCPSI=?, TIPDISCFIS=?,TIPDISCSEN=?, SEXO=?,")
                        .append(" TIPCONTRATO=?, TIPJORNADA=?,JORPARCPORC=?, PENSIONISTA=?, TIPPENSIONISTA=?")
                        .append(whereComun);
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                contbd = establecerDatosComunes(persona, numExpediente, ps);
                trabaj = (TrabajadorVO) persona;

                if (trabaj.getTipoDiscPsiquica() == -1) {
                    ps.setNull(contbd++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(contbd++, trabaj.getTipoDiscPsiquica());
                }

                if (trabaj.getTipoDiscFisica() == -1) {
                    ps.setNull(contbd++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(contbd++, trabaj.getTipoDiscFisica());
                }

                if (trabaj.getTipoDiscSensorial() == -1) {
                    ps.setNull(contbd++, java.sql.Types.INTEGER);
                } else {
                    ps.setInt(contbd++, trabaj.getTipoDiscSensorial());
                }

                ps.setString(contbd++, String.valueOf(trabaj.getSexo()));

                if (trabaj.getTipoContrato() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getTipoContrato());
                }

                if (trabaj.getTipoJornada() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getTipoJornada());
                }

                if (trabaj.getJorParcPorc() == -1) {
                    ps.setNull(contbd++, java.sql.Types.DOUBLE);
                } else {
                    ps.setDouble(contbd++, trabaj.getJorParcPorc());
                }

                if (trabaj.getPensionista() == null || trabaj.getPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getPensionista());
                }

                if (trabaj.getTipoPensionista() == null || trabaj.getTipoPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, trabaj.getTipoPensionista());
                }

            } else {
                query = new StringBuilder(updateComun).append(" FECALTACONTINDEF=?,FECALTACONTINDEFTEMP=?,FECBAJACONTINDEFTEMP=?, JORPARCPORC=?, TIPJORNADA=?, TIPCONTRATO=?, PENSIONISTA=?, TIPPENSIONISTA=?").append(whereComun);
                log.debug("sql: " + query);

                ps = con.prepareStatement(query.toString());
                contbd = establecerDatosComunes(persona, numExpediente, ps);
                encarg = (EncargadoVO) persona;

                if (encarg.getFecAltaContrIndef() == null) {
                    ps.setNull(contbd++, java.sql.Types.DATE);
                } else {
                    ps.setDate(contbd++, new java.sql.Date(encarg.getFecAltaContrIndef().getTime()));
                }

                if (encarg.getFecAltaContrTemp() == null) {
                    ps.setNull(contbd++, java.sql.Types.DATE);
                } else {
                    ps.setDate(contbd++, new java.sql.Date(encarg.getFecAltaContrTemp().getTime()));
                }

                if (encarg.getFecBajaContrTemp() == null) {
                    ps.setNull(contbd++, java.sql.Types.DATE);
                } else {
                    ps.setDate(contbd++, new java.sql.Date(encarg.getFecBajaContrTemp().getTime()));
                }

                if (encarg.getJornadaParcialPor() == -1.0) {
                    ps.setNull(contbd++, java.sql.Types.DOUBLE);
                } else {
                    ps.setDouble(contbd++, encarg.getJornadaParcialPor());
                }

                if (encarg.getTipoJornada() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getTipoJornada());
                }

                if (encarg.getTipoContrato() == null) {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getTipoContrato());
                }

                if (encarg.getPensionista() == null || encarg.getPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getPensionista());
                }

                if (encarg.getTipoPensionista() == null || encarg.getTipoPensionista() == "") {
                    ps.setNull(contbd++, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(contbd++, encarg.getTipoPensionista());
                }

            }
            ps.setInt(contbd++, ident);
            actualizado = ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al modificar los datos del registro con id: " + ident + " de " + tabla);
            throw new MeLanbide65Exception(6, "Ha ocurrido un error en la operacion de actualizaci�n de la BBDD");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }

        return actualizado == 1;
    }

    public boolean eliminar(int ident, String tabla, Connection con) throws MeLanbide65Exception {
        PreparedStatement ps = null;
        int borrado = 0;
        String query;
        int contbd = 1;

        try {
            query = "DELETE FROM " + tabla + " WHERE ID=?";
            log.debug("sql: " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contbd++, ident);

            borrado = ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al eliminar el registro " + ident + " de " + tabla);
            throw new MeLanbide65Exception(7, "Ha ocurrido un error al borrar la fila de BBDD");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Error al liberar recursos de BBDD");
            }
        }

        return borrado == 1;
    }

    private void obtenerDatosComunes(PersonaVO empleado, ResultSet rs) throws SQLException, NumberFormatException {
        empleado.setIdentificador(rs.getInt("ID"));
        empleado.setApellido1(rs.getString("APELLIDO1"));
        if (rs.getString("APELLIDO2") == null) {
            empleado.setApellido2("");
        } else {
            empleado.setApellido2(rs.getString("APELLIDO2"));
        }

        empleado.setNombre(rs.getString("NOMBRE"));
        empleado.setDni(rs.getString("DNI"));
    }

    private int establecerDatosComunes(PersonaVO persona, String numExp, PreparedStatement ps) throws SQLException {
        int cont = 1;
        ps.setString(cont++, numExp);
        ps.setString(cont++, persona.getApellido1());
        ps.setString(cont++, persona.getApellido2());
        ps.setString(cont++, persona.getNombre());
        ps.setString(cont++, persona.getDni());

        return cont;
    }

    private String obtenerSecuencia(String tabla) {
        if (tabla.contains(ConstantesMeLanbide65.getSUF_TRABAJADOR())) {
            return ConstantesMeLanbide65.getNOMBRE_SEQ_TRABAJADOR();
        } else if (tabla.contains(ConstantesMeLanbide65.getSUF_TECNICO())) {
            return ConstantesMeLanbide65.getNOMBRE_SEQ_TECNICO();
        } else if (tabla.contains(ConstantesMeLanbide65.getSUF_ENCARGADO())) {
            return ConstantesMeLanbide65.getNOMBRE_SEQ_ENCARGADO();
        }

        return "";
    }

    public String recalcularSubvencion(String numExp, Connection con) throws Exception {
        CallableStatement st = null;
        try {
            String query = null;
            query = "{?=call recalculoSubvUAAP(?,?)}";	//p_numExp
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareCall(query);
            st.registerOutParameter(1, Types.VARCHAR);
            st.setString(2, numExp);
            st.setString(3, numExp.substring(0, 4));

            st.executeQuery();

            return st.getString(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error recalculando la Subvenci�n", ex);
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

    public boolean insertarSubvencion(int codOrganizacion, String numExpediente, Double importeSub, String nombreTabla, Connection con) throws MeLanbide65Exception, SQLException {
        PreparedStatement ps = null;
        int insertado = 0;
        String sql = "";
        StringBuilder query;
        try {
            if (existeValorSubvencion(numExpediente, nombreTabla, con)) {
                sql = "UPDATE " + nombreTabla + " SET TNU_VALOR = " + importeSub
                        + " WHERE TNU_NUM = '" + numExpediente + "' AND TNU_COD = '" + ConstantesMeLanbide65.IMPTOTCONCESION + "'";
            } else {
                sql = "INSERT INTO " + nombreTabla
                        + " VALUES (" + codOrganizacion + ",'" + numExpediente.substring(0, 4) + "','" + numExpediente + "','" + ConstantesMeLanbide65.IMPTOTCONCESION + "'," + importeSub + ")";
            }

            query = new StringBuilder(sql);
            log.debug("sql: " + query);
            ps = con.prepareStatement(query.toString());
            insertado = ps.executeUpdate();

        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al dar de alta un registro en " + nombreTabla + sqle);
            throw new MeLanbide65Exception(4, "Ha ocurrido un error en la operaci�n de inserci�n en BBDD");
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

        return insertado == 1;
    }

    private boolean existeValorSubvencion(String numExpediente, String nombreTabla, Connection con) throws SQLException {
        String sql = "SELECT TNU_VALOR FROM " + nombreTabla + " WHERE TNU_NUM = '" + numExpediente + "' AND TNU_COD = '" + ConstantesMeLanbide65.IMPTOTCONCESION + "'";
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

    public boolean marcadoNoCalculo(int codOrg, int ejercicio, String numExpediente, Connection con) throws Exception {
        log.info(" ==> Entra en marcadoNoCalculo - DAO - " + numExpediente);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        boolean marcado = false;
        int contbd = 1;

        try {
            query = "select tde_valor from e_tde where tde_mun=? and tde_eje=? and tde_num=? and tde_cod = 'CALCULAR'";
            log.debug("sql: " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contbd++, codOrg);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExpediente);
            rs = ps.executeQuery();
            while (rs.next()) {
                log.debug("marca");
                if (rs.getString("TDE_VALOR").equalsIgnoreCase("X")) {
                    log.debug("X");
                    marcado = true;
                }
            }
        } catch (SQLException e) {
            log.error("Ha ocurrido un error al recuperar el valor de CALCULAR de E_TDE. " + e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return marcado;
    }

    /*
    public List<ExpedienteUAAPVO> busquedaExpedientesRecalculo(ExpUAAPCriteriosFiltroVO _criterioBusqueda, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String expedientesNuevos = "";
        String expedientesIniciales = "";
        List<ExpedienteUAAPVO> listExpedientes = new ArrayList<ExpedienteUAAPVO>();
        ExpedienteUAAPVO expediente = new ExpedienteUAAPVO();
        try {
            if (_criterioBusqueda.getTipoProcedimiento().equals("AEXCE")) {
                if (_criterioBusqueda.getPeriodoSubvencionable().equals("SI")) {
                    expedientesNuevos = " INNER JOIN e_tde td ON td.tde_num=e.ext_num and td.tde_cod = 'AMPPERSUBV' AND td.tde_valor='X'\n";
                } else {
                    expedientesIniciales = " AND (e.ext_num not in (SELECT TDE_NUM FROM E_TDE WHERE TDE_COD='AMPPERSUBV'))";
                }
            }
            query = "SELECT  e.EXT_NUM AS NUMEROEXPEDIENTE,\n"
                    + "        t.hte_nom || ' ' ||t.hte_pa1 || '' || t.hte_ap1 || (CASE WHEN t.hte_pa1 IS NOT NULL OR t.hte_ap1  IS NOT NULL THEN ' ' ELSE '' END) || t.hte_pa2 || '' || t.hte_ap2 AS NOMBREINTERESADO,\n"
                    + "        t_dnn.dnn_cpo AS CPINTERESADO,\n"
                    + "        NVL(g.tnu_valor,0) AS IMPCONINI, \n"
                    + "        NVL(u.txt_valor,0) AS IMPTOTRECAL \n"
                    + "FROM  e_ext e\n"
                    + "       LEFT JOIN t_hte t  ON e.ext_ter = t.hte_ter and e.ext_nvr=t.hte_nvr\n"
                    + "       LEFT JOIN t_dnn ON e.ext_dot = t_dnn.dnn_dom\n"
                    + "       LEFT JOIN e_tnu g ON e.ext_num = g.tnu_num AND e.ext_mun = g.tnu_mun AND e.ext_eje = g.tnu_eje AND g.tnu_cod = 'IMPCONINI'\n"
                    + "       LEFT JOIN e_txt u ON e.ext_num = u.txt_num AND e.ext_mun = u.txt_mun AND e.ext_eje = u.txt_eje AND u.txt_cod = 'IMPTOTRECAL'\n"
                    + expedientesNuevos
                    + " WHERE   (e.ext_pro = '" + _criterioBusqueda.getTipoProcedimiento() + "')"
                    + " and (e.ext_rol = 1) AND  e.ext_eje=" + _criterioBusqueda.getEjercicio()
                    + expedientesIniciales
                    + "        ORDER BY EXT_NUM";
            log.info("query: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                expediente = (ExpedienteUAAPVO) MeLanbide65MappingUtils.getInstance().mapVers2(rs, ExpedienteUAAPVO.class, true);
                listExpedientes.add(expediente);
                expediente = new ExpedienteUAAPVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en lista de expedientes al filtrar - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listExpedientes;
    }
     */
    public List<ExpedienteUAAPVO> busquedaFiltrandoListaExpedientes(ExpUAAPCriteriosFiltroVO _criterioBusqueda, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String expedientesNuevos = "";
        String expedientesIniciales = "";
        List<ExpedienteUAAPVO> listExpedientes = new ArrayList<ExpedienteUAAPVO>();

        try {

            log.info("TipoProcedimiento = " + _criterioBusqueda.getTipoProcedimiento());
            log.info("PeriodoSubvencionable = " + _criterioBusqueda.getPeriodoSubvencionable());
            if (_criterioBusqueda.getTipoProcedimiento().equals("AEXCE")) {
                if (_criterioBusqueda.getPeriodoSubvencionable().equals("SI")) {
                    expedientesNuevos = " INNER JOIN e_tde td ON td.tde_num=e.ext_num and td.tde_cod = 'AMPPERSUBV' AND td.tde_valor='X'\n";
                    log.info("expedientesNuevos = " + expedientesNuevos);
                } else {
                    expedientesIniciales = " AND (e.ext_num not in (SELECT TDE_NUM FROM E_TDE WHERE TDE_COD='AMPPERSUBV'))";
                    log.info("expedientesIniciales = " + expedientesIniciales);
                }
            }
            String where1 = preparaClausulaWhere(_criterioBusqueda);

            query = "SELECT * FROM (\n"
                    + "    SELECT ROWNUM AS NUMREG ,COUNT(0) OVER (PARTITION BY 0) AS NUMTOTALREGISTROS, DATOS.* FROM (\n"
                    + "        SELECT * FROM (SELECT  e.EXT_NUM AS NUMEROEXPEDIENTE,\n"
                    + "        t.hte_nom\n"
                    + "        || ' '\n"
                    + "        ||t.hte_pa1\n"
                    + "        || ''\n"
                    + "        || t.hte_ap1\n"
                    + "        || (\n"
                    + "        CASE\n"
                    + "          WHEN t.hte_pa1 IS NOT NULL\n"
                    + "          OR t.hte_ap1   IS NOT NULL\n"
                    + "          THEN ' '\n"
                    + "          ELSE ''\n"
                    + "        END)\n"
                    + "        || t.hte_pa2\n"
                    + "        || ''\n"
                    + "        || t.hte_ap2 AS NOMBREINTERESADO,\n"
                    + "        t_dnn.dnn_cpo AS CPINTERESADO,\n"
                    + "        NVL(g.tnu_valor,0) AS IMPCONINI, \n"
                    + "        NVL(u.txt_valor,0) AS IMPTOTRECAL \n"
                    + "FROM  e_ext e\n"
                    + "       LEFT JOIN t_hte t  ON e.ext_ter = t.hte_ter and e.ext_nvr=t.hte_nvr\n"
                    + "       LEFT JOIN t_dnn ON e.ext_dot = t_dnn.dnn_dom\n"
                    + "       LEFT JOIN e_tnu g  ON e.ext_num  = g.tnu_num  AND g.tnu_cod = 'IMPCONINI'\n"
                    + "       LEFT JOIN e_txt u  ON e.ext_num  = u.txt_num  AND u.txt_cod = 'IMPTOTRECAL'\n"
                    +  "      LEFT JOIN e_exp y ON e.ext_num = y.exp_num ";
            query = query + expedientesNuevos;
            query = query + " WHERE  "
                    + " " + where1 + " " + expedientesIniciales + " \n"
                    + "        ORDER BY EXT_NUM) WHERE ROWNUM <= 100\n"
                    + "    ) DATOS\n"
                    + ")";

            log.info("DAO2: " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            BigDecimal importeConini = new BigDecimal("0.00");
            String importeTotRecal = "";
            while (rs.next()) {
                ExpedienteUAAPVO expediente = new ExpedienteUAAPVO();
                expediente.setNoRegEnLaConsulta(rs.getInt("NUMREG"));
                expediente.setNoTotalRegConsulta(rs.getInt("NUMTOTALREGISTROS"));
                expediente.setNumeroExpediente(rs.getString("NUMEROEXPEDIENTE"));
                expediente.setNombreInteresado(rs.getString("NOMBREINTERESADO"));
                expediente.setCP(rs.getString("CPINTERESADO"));
                importeConini = BigDecimal.valueOf(rs.getDouble("IMPCONINI"));
                expediente.setImporteConini(importeConini);
                importeTotRecal = rs.getString("IMPTOTRECAL");
                expediente.setImporteTotalRecal(importeTotRecal);
                listExpedientes.add(expediente);
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error en lista de expedientes al filtrar - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listExpedientes;
    }

    private String preparaClausulaWhere(ExpUAAPCriteriosFiltroVO _criterioBusqueda) throws Exception {
        String where = "";
        int i = 0;
        // #31073 - Utilizamos la variable 'expedientesAnulados' para eliminar de la consulta los expedientes anulados.
        String expedientesAnulados = " y.exp_est != 1 ";
        String and = " AND ";
        try {
            if (_criterioBusqueda.getTipoProcedimiento() != null && !_criterioBusqueda.getTipoProcedimiento().isEmpty()) {
                where += "(e.ext_pro = '" + _criterioBusqueda.getTipoProcedimiento() + "')";
                where += " and (e.ext_rol = 1)";
                i++;
            }
            if (_criterioBusqueda.getEjercicio() != null && !_criterioBusqueda.getEjercicio().isEmpty()) {
                where += " AND  (e.ext_eje=" + _criterioBusqueda.getEjercicio() + ")";
                i++;
            }
            if (i > 0) {
                expedientesAnulados += and;
            }
            where = expedientesAnulados + where;
        } catch (Exception ex) {
            log.error("Se ha producido un error preparando la clausula where de la sentencia de consulta con filtros para la lista de expedientes", ex);
            throw new Exception(ex);
        }
        return where;
    }

    public int marcarRecalculoImportes(ExpUAAPCriteriosFiltroVO _criterioBusqueda, int codOrganizacion, String importeMaximo, String[] listaExpedientesMarcadosStr, Connection con) throws Exception {
        Statement st = null;
        ResultSet result = null;
        int resultado = 0;
        String query = "";
        String listaExpedientes = "(";
        try {

            for (int i = 0; i < listaExpedientesMarcadosStr.length; i++) {
                String numExpediente = listaExpedientesMarcadosStr[i];
                if ("(".equals(listaExpedientes)) {
                    listaExpedientes = listaExpedientes + "'" + numExpediente + "'";
                } else {
                    listaExpedientes = listaExpedientes + "," + "'" + numExpediente + "'";
                }

            }
            listaExpedientes = listaExpedientes + ")";

            String tipoProcedimiento = "";
            if (_criterioBusqueda.getTipoProcedimiento() != null && !_criterioBusqueda.getTipoProcedimiento().equals("")) {
                tipoProcedimiento = _criterioBusqueda.getTipoProcedimiento();
            }
            log.info("codOrganizacion++: " + codOrganizacion);
            query = "SELECT  sum(g.tnu_valor) TOTCONINI "
                    + "FROM    E_EXP a "
                    + "LEFT JOIN e_tnu g\n "
                    + "  ON a.exp_num  = g.tnu_num\n "
                    + "  AND g.tnu_cod = 'IMPCONINI' "
                    + " WHERE   EXP_PRO = '" + tipoProcedimiento + "' AND EXP_NUM IN " + listaExpedientes;

            log.info("sql = " + query);
            st = con.createStatement();
            result = st.executeQuery(query);
            if (result.next()) {
                log.info("TOTCONINI: " + result.getDouble("TOTCONINI"));
                if (Double.parseDouble(importeMaximo) < result.getDouble("TOTCONINI")) {

                    BigDecimal importeMax = new BigDecimal(importeMaximo);
                    log.info("importeMax++: " + importeMax);
                    BigDecimal sumaConini = new BigDecimal(result.getDouble("TOTCONINI"));
                    log.info("sumaConini++: " + sumaConini);
                    BigDecimal numReduccion = importeMax.divide(sumaConini, 10, RoundingMode.FLOOR);
                    //BigDecimal numReduccion = importeMax.divide(sumaConini);
                    log.info("numReduccion++: " + numReduccion);

                    //DecimalFormat twoDForm = new DecimalFormat("#.##");
                    //numReduccion = Double.valueOf(twoDForm.format(numReduccion));
                    // Hago el UPDATE
                    BigDecimal conini = new BigDecimal("0.0000");
                    BigDecimal rdo = new BigDecimal("0.0000");
                    String numExpediente = "";
                    for (int i = 0; i < listaExpedientesMarcadosStr.length; i++) {

                        numExpediente = listaExpedientesMarcadosStr[i];
                        log.info("BUCLE NUMEROEXPEDIENTE++: " + numExpediente);
                        query = "SELECT  g.tnu_valor TOTCONINI"
                                + " FROM E_EXP a LEFT JOIN e_tnu g"
                                + " ON a.exp_num  = g.tnu_num AND g.tnu_cod = 'IMPCONINI'"
                                + " WHERE EXP_PRO = '" + tipoProcedimiento + "' AND EXP_NUM = '" + numExpediente + "'";

                        log.info("sql1 = " + query);
                        result = st.executeQuery(query);
                        result.next();
                        //log.debug("g.tnu_valor: " + df.format(result.getLong("TOTCONINI")*numReduccion));
                        //conini = BigDecimal.valueOf(result.getDouble("TOTCONINI"));
                        log.info("ANTES DE CONINI: " + result.getDouble("TOTCONINI"));
                        conini = BigDecimal.valueOf(result.getDouble("TOTCONINI"));
                        log.info("conini++: " + conini);
                        log.info("numReduccion++STRING: " + NumberFormat.getCurrencyInstance().format(numReduccion));
                        rdo = numReduccion.multiply(conini);
                        NumberFormat formatter = NumberFormat.getInstance();
                        formatter.setMaximumFractionDigits(4);
                        log.info("rdo++: " + rdo);

                        query = "SELECT  g.txt_valor IMPTOTRECAL"
                                + " FROM e_txt g "
                                + " WHERE TXT_NUM ='" + numExpediente + "' AND TXT_COD = 'IMPTOTRECAL'";

                        log.info("sql2 = " + query);

                        result = st.executeQuery(query);
                        if (!result.next()) {
                            //query  = "INSERT INTO e_txt g VALUES (0," + numExpediente.substring(0, 4) + ",'" + numExpediente + "','IMPTOTRECAL','"+ d.format(rdo).toString()  + "')";
                            query = "INSERT INTO e_txt g (TXT_MUN,TXT_EJE,TXT_NUM,TXT_COD,TXT_VALOR) VALUES (" + codOrganizacion + "," + numExpediente.substring(0, 4) + ",'" + numExpediente + "','IMPTOTRECAL','" + formatter.format(rdo) + "')";                            log.info("QUERY INSERT ANTES++: " + query);
                            log.info("QUERY INSERT ANTES++: " + query);
                            resultado = st.executeUpdate(query);
                        } else {
                            query = "UPDATE e_txt g SET g.txt_valor='" + formatter.format(rdo)
                                    + "' WHERE   TXT_NUM ='" + numExpediente + "' AND TXT_COD = 'IMPTOTRECAL'";

                            resultado = st.executeUpdate(query);
                            log.info("QUERY UPDATE++: " + query);
                        }
                    }
                } else {
                    query = "UPDATE e_txt g SET g.txt_valor='0' WHERE   TXT_NUM IN " + listaExpedientes + " AND TXT_COD = 'IMPTOTRECAL'";
                    log.info("query INICIALIZACION++: " + query);
                    resultado = st.executeUpdate(query);
                }
            }

        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error en lista de expedientes al recalcular importes - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en lista de expedientes al recalcular importes - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (result != null) {
                result.close();
            }
        }
        return resultado;

    }

    public String getNIFPersonaContratada(String numeroExpediente, Connection con) throws Exception {
        log.info("INICIO getNIFPersonaContratada DAO");
        String nifPersonaContratada = "";

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT  TXT_VALOR ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numeroExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'DNIPERCON' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                nifPersonaContratada = rs.getString("TXT_VALOR");
            }

        } catch (Exception ex) {
            log.error("Excepcion - getNIFPersonaContratada : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getNIFPersonaContratada - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getNIFPersonaContratada - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return nifPersonaContratada;
    }

    public String getNumeroOferta(String numeroExpediente, Connection con) throws Exception {
        log.info("INICIO getNumeroOferta DAO");
        String numeroOferta = "";

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT  TXT_VALOR ");
            query.append("FROM E_TXT ");
            query.append("WHERE TXT_NUM = '");
            query.append(numeroExpediente);
            query.append("' ");
            query.append("AND TXT_COD = 'NUMEROTIPO2' ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {
                numeroOferta = rs.getString("TXT_VALOR");
            }
        } catch (Exception ex) {
            log.error("Excepcion - getNumeroOferta : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getNumeroOferta - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getNumeroOferta - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        return numeroOferta;
    }

    public PersonaContratadaVO getPersonaContratada(String nifPersonaContratada, String numeroOferta, Connection con) throws Exception {
        log.info("INICIO getPersonaContratada DAO");
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();

        Statement st = null;
        ResultSet rs = null;
        try {
            StringBuffer query = new StringBuffer();
            query.append("SELECT pc.GEN_PER_TIPO_DOC, ");
            query.append("pc.GEN_PER_NUM_DOC, "); //dni
            query.append("pc.GEN_PER_NOMBRE, "); //nombre
            query.append("pc.GEN_PER_APELLIDO1, "); //apellido1
            query.append("pc.GEN_PER_APELLIDO2, "); //apellido2
            query.append("pc.GEN_PER_FECHA_NAC, "); //fechaNacimiento
            query.append("pc.EDAD, "); //edad
            query.append("pc.SEXO, "); //sexo
            query.append("pc.ID_OFERTA, ");
            query.append("pc.CONTRAT_INCENT_LEHENAUK, "); //contratoIncentivadoLAK
            query.append("pc.DEM_DEM_FEC_REV_DEM, "); //fechaGestionDemanda
            query.append("pc.DEM_TIT_COD_TITU, "); //formacionCV
            query.append("pc.DEM_TIT_FEC_TITU, "); //anioObtencionTitulacion
            query.append("pc.DEM_DIA_CONTRATACION, "); //desempleadoDiaContratacion
            query.append("pc.ALTA_SIST_NAC_GARANTIA_JUVENIL, "); //altaSistemaGJ
            query.append("pc.EDICION_ANTERIOR_LEHEN_AUKERA, "); //participadoEdicionesAnterioresLAK
            query.append("pc.GEN_PER_REMUNI, "); //municipioDeResidencia
            query.append("o.OFE_FEC_REG, "); //fechaRegistro
            query.append("o.OFERTA_ASOC_LEHEN_AUKERA_2020, "); //ofertaLimitadaLAK2020
            query.append("o.NOMBRE_CENTRO_REGISTRO, "); //centroCaptacionRegistro
            query.append("o.NOMBRE_CENTRO_GESTION "); //centroGestion
            query.append("FROM S_VW_DATOS_PERSONA_CONTRATADA pc, ");
            query.append("S_VW_DATOS_OFERTA o ");
            query.append("WHERE pc.GEN_PER_NUM_DOC = '" + nifPersonaContratada + "' ");
            query.append("AND pc.ID_OFERTA = '" + numeroOferta + "' ");
            query.append("AND pc.ID_OFERTA = o.OFE_ID ");

            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while (rs.next()) {

                personaContratada.setDni(rs.getString("GEN_PER_NUM_DOC"));
                personaContratada.setNombre(rs.getString("GEN_PER_NOMBRE"));
                personaContratada.setApellido1(rs.getString("GEN_PER_APELLIDO1"));
                personaContratada.setApellido2(rs.getString("GEN_PER_APELLIDO2"));
                personaContratada.setFechaNacimiento(rs.getDate("GEN_PER_FECHA_NAC"));
                personaContratada.setEdad(rs.getBigDecimal("EDAD"));
                personaContratada.setSexo(rs.getString("SEXO"));
                personaContratada.setContratoIncentivadoLAK(rs.getString("CONTRAT_INCENT_LEHENAUK"));
                personaContratada.setFechaGestionDemanda(rs.getDate("DEM_DEM_FEC_REV_DEM"));
                personaContratada.setFormacionCV(rs.getString("DEM_TIT_COD_TITU"));
                if (null != rs.getString("DEM_TIT_FEC_TITU")) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(rs.getDate("DEM_TIT_FEC_TITU"));
                    String anio = new Integer(cal.get(Calendar.YEAR)).toString();
                    personaContratada.setAnioObtencionTitulacion(anio);
                }
                personaContratada.setDesempleadoDiaContratacion(rs.getString("DEM_DIA_CONTRATACION"));
                personaContratada.setAltaSistemaGJ(rs.getString("ALTA_SIST_NAC_GARANTIA_JUVENIL"));
                personaContratada.setParticipadoEdicionesAnterioresLAK(rs.getString("EDICION_ANTERIOR_LEHEN_AUKERA"));
                personaContratada.setFechaRegistro(rs.getDate("OFE_FEC_REG"));
                personaContratada.setOfertaLimitadaLAK2020(rs.getString("OFERTA_ASOC_LEHEN_AUKERA_2020"));
                personaContratada.setCentroCaptacionRegistro(rs.getString("NOMBRE_CENTRO_REGISTRO"));
                personaContratada.setCentroGestion(rs.getString("NOMBRE_CENTRO_GESTION"));
                personaContratada.setMunicipioDeResidencia(rs.getString("GEN_PER_REMUNI"));
            }
        } catch (Exception ex) {
            log.error("Excepcion - getPersonaContratada : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("getPersonaContratada - Procedemos a cerrar el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(" getPersonaContratada - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("FIN getPersonaContratada DAO");
        return personaContratada;
    }    
}
