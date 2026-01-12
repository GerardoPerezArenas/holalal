package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import es.altia.agora.business.registro.InteresadoAnotacionVO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.registro.persistence.manual.AnotacionRegistroDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.exception.GestionAutomaticaKONTUException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MeLanbide43JobKontuDAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide43JobKontuDAO.class);

    //Instancia
    private static MeLanbide43JobKontuDAO instance = null;

    public static MeLanbide43JobKontuDAO getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : BEGIN");
        }
        if (instance == null) {
            synchronized (MeLanbide43JobKontuDAO.class) {
                if (instance == null) {
                    instance = new MeLanbide43JobKontuDAO();
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : END");
        }
        return instance;
    }

    /**
     * Recupera de base de datos una lista de anotaciones pendiendientes
     * @param datosBusqueda Datos de departamento, unidad, tipo y procedimiento por los que hacer la búsqueda
     * @param con
     * @return
     * @throws GestionAutomaticaKONTUException
     */
    public List<RegistroValueObject> getAnotacionesPendientes(RegistroValueObject datosBusqueda, Connection con) throws GestionAutomaticaKONTUException {

        List<RegistroValueObject> listado = new ArrayList<RegistroValueObject>();
        RegistroValueObject anotacion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int depart = datosBusqueda.getIdentDepart();
        int unidad = datosBusqueda.getUnidadOrgan();
        String tipo = datosBusqueda.getTipoReg();
        int estado = datosBusqueda.getEstAnotacion();
        String codProc = datosBusqueda.getCodProcedimiento();

        try {
            query = "SELECT RES_EJE, RES_NUM, RES_UOD, RES_ASU, RES_OBS FROM R_RES WHERE RES_DEP = ? AND RES_UOR = ? AND RES_TIP = ? AND RES_EST = ? AND PROCEDIMIENTO = ?";
            query += " ORDER BY RES_NUM";
            log.debug("Query para obtener anotaciones pendientes de KONTU = " + query);
            log.debug(String.format("Parámetros de la query: %d-%d-%s-%d-%s", depart, unidad, tipo, estado, codProc));

            ps = con.prepareStatement(query);
            int  contbd = 1;
            ps.setInt(contbd++, depart);
            ps.setInt(contbd++, unidad);
            ps.setString(contbd++, tipo);
            ps.setInt(contbd++, estado);
            ps.setString(contbd++, codProc);

            rs = ps.executeQuery();
            while (rs.next()) {
                anotacion = new RegistroValueObject();
                anotacion.setIdentDepart(depart);
                anotacion.setUnidadOrgan(unidad);
                anotacion.setTipoReg(tipo);
                anotacion.setAnoReg(rs.getInt("RES_EJE"));
                anotacion.setNumReg(rs.getLong("RES_NUM"));
                anotacion.setEstAnotacion(estado);
                anotacion.setCodProcedimiento(codProc);
                anotacion.setIdUndTramitad(rs.getString("RES_UOD"));
                anotacion.setAsunto(rs.getString("RES_ASU"));
                anotacion.setObservaciones(rs.getString("RES_OBS"));
                anotacion.setListaInteresados(getInteresadosAnotacion(anotacion, con));
                anotacion.setListaDocsAsignados(AnotacionRegistroDAO.getInstance().getListaDocumentos(anotacion, con));


                listado.add(anotacion);
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error en getAnotacionesPendientes " + ex.getMessage(), ex);
            throw new GestionAutomaticaKONTUException(ex.getMessage());
        } catch (Exception ex) {
            log.error("Ha ocurrido un error en getAnotacionesPendientes " + ex.getMessage(), ex);
            throw new GestionAutomaticaKONTUException(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex){
                log.error("Ha ocurrido un error en getAnotacionesPendientes " + ex.getMessage(), ex);
                throw new GestionAutomaticaKONTUException(ex.getMessage());
            }
        }
        log.debug("getAnotacionesPendientes END: " + listado.size());
        return listado;
    }

    /**
     * Inserta en base de datos un registro de resultado por anotación procesada al ejecutar el job GestionAutomaticaKONTU
     * @param anoAnotacion
     * @param numAnotacion
     * @param numExpediente
     * @param estado
     * @param mensaje
     * @param con
     * @return
     * @throws Exception
     */
    public int insertarLineasLog(int anoAnotacion, long numAnotacion, String numExpediente, String estado, String mensaje, Connection con)  {
        log.info("insertarLineasLog() -  Begin () ");
        int numInsertadas = -1;
        PreparedStatement ps = null;
        String query;

        try {
            query = "INSERT INTO melanbide43_kontujob (eje_anotacion, num_anotacion, num_expediente, fecha, resultado, desc_error) VALUES  (?,?,?,SYSDATE,?,?)";
            log.debug("Query: " + query);
            log.debug(String.format("Parámetros de la query: %d-%d-%s-%s", anoAnotacion, numAnotacion, numExpediente, estado));

            int contbd = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contbd++, anoAnotacion);
            ps.setLong(contbd++, numAnotacion);
            ps.setString(contbd++, numExpediente);
            ps.setString(contbd++, estado);
            ps.setString(contbd++, mensaje);

            numInsertadas = ps.executeUpdate();

        } catch (SQLException ex) {
            log.info("Se ha producido un error en insertarLineasLog(), al registrar la linea en el log del job: " + ex.getMessage(), ex);
        } catch (Exception ex) {
            log.info("Se ha producido un error en insertarLineasLog(): " + ex.getMessage(), ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error en insertarLineasLog(): " + e.getMessage(), e);
            }
        }
        return numInsertadas;
    }

    /**
     * Recupera de base de datos la lista de interesados de una anotación de registro
     * @param anotacion
     * @param con
     * @return
     * @throws SQLException
     */
    private List<InteresadoAnotacionVO> getInteresadosAnotacion(RegistroValueObject anotacion, Connection con) throws SQLException {
        List<InteresadoAnotacionVO> lista = new ArrayList<InteresadoAnotacionVO>();
        InteresadoAnotacionVO interesadoAnotacion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;

        try{
            query = "SELECT * FROM R_EXT WHERE EXT_DEP = ? AND EXT_UOR = ? AND EXT_TIP = ? AND EXT_EJE = ? AND EXT_NUM = ?";
            log.debug("Query para recuperar los interesados de una anotación: " + query);
            log.debug(String.format("Parámetros de la query: %d-%d-%s-%d-%d",
                    anotacion.getIdentDepart(), anotacion.getUnidadOrgan(), anotacion.getTipoReg(), anotacion.getAnoReg(), anotacion.getNumReg()));

            ps = con.prepareStatement(query);
            int contbd=1;
            ps.setInt(contbd++, anotacion.getIdentDepart());
            ps.setInt(contbd++, anotacion.getUnidadOrgan());
            ps.setString(contbd++, anotacion.getTipoReg());
            ps.setInt(contbd++, anotacion.getAnoReg());
            ps.setLong(contbd++, anotacion.getNumReg());

            rs = ps.executeQuery();
            while(rs.next()){
                interesadoAnotacion = new InteresadoAnotacionVO();
                interesadoAnotacion.setCodTercero(rs.getInt("EXT_TER"));
                interesadoAnotacion.setNumVersion(rs.getInt("EXT_NVR"));
                interesadoAnotacion.setCodDomicilio(rs.getInt("EXT_DOT"));
                interesadoAnotacion.setCodigoRol(rs.getInt("EXT_ROL"));

                lista.add(interesadoAnotacion);
            }

        } catch (SQLException e){
            log.error("Ha ocurrido un error al recuperar los interesados de una anotación: " + e.getMessage(),e);
            throw e;
        } finally{
            try{
                if(rs != null) rs.close();
                if(ps != null) ps.close();
            } catch (SQLException e){
                log.error("Ha ocurrido un error al devolver los recursos de base de datos");
            }
        }

        return lista;
    }

}
