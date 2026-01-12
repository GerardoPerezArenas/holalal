package es.altia.flexia.integracion.moduloexterno.melanbide65.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.flexia.integracion.moduloexterno.melanbide65.dao.MeLanbide65DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.exception.MeLanbide65Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide65.util.ConstantesMeLanbide65;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.ExpUAAPCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.ExpedienteUAAPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.PersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide65.vo.TrabajadorVO;
//import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide65Manager {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide65Manager.class);
    //private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    //Instancia
    private static MeLanbide65Manager instance = null;

    private MeLanbide65Manager() {
    }

    //Devolvemos una unica instancia de la clase a traves de este metodo ya que el constructor es privado
    public static MeLanbide65Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide65Manager.class) {
                instance = new MeLanbide65Manager();
            }
        }
        return instance;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();
            return meLanbide65DAO.getValoresDesplegablesAdmonLocalxdes_cod(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando los valores de un Desplegable : " + des_cod, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ArrayList<PersonaVO> obtenerDatos(String nombreTabla, String numExpediente, AdaptadorSQLBD adapt, Connection con) throws MeLanbide65Exception {
        ArrayList<PersonaVO> lista = null;
        Connection nuevaCon = null;

        try {
            if (con == null) {
                nuevaCon = adapt.getConnection();
            } else {
                nuevaCon = con;
            }

            lista = MeLanbide65DAO.getInstance().obtenerDatos(nombreTabla, numExpediente, nuevaCon);

            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratos = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO(), adapt);
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), adapt);
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_SEXO(), adapt);
            List<DesplegableAdmonLocalVO> listaPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod("BOOL", adapt);
            List<DesplegableAdmonLocalVO> listaTipoPensionista = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_PENSIONISTA(), adapt);

            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaContratosTec = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_CONTRATO_B(), adapt);
            List<DesplegableAdmonLocalVO> listaJornadaTec = MeLanbide65Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConstantesMeLanbide65.getNOMBRE_LISTA_JORNADA(), adapt);

            for (PersonaVO proy : lista) {
                if (proy instanceof TrabajadorVO) {

                    for (DesplegableAdmonLocalVO desp : listaContratos) {
                        if (desp.getDes_val_cod().equals(((TrabajadorVO) proy).getTipoContrato())) {
                            ((TrabajadorVO) proy).setDescTipoContrato(desp.getDes_nom());
                            break;
                        }
                    }

                    for (DesplegableAdmonLocalVO desp : listaJornada) {
                        if (desp.getDes_val_cod().equals(((TrabajadorVO) proy).getTipoJornada())) {
                            ((TrabajadorVO) proy).setDescTipoJornada(desp.getDes_nom());
                            break;
                        }
                    }

                    for (DesplegableAdmonLocalVO desp : listaSexo) {
                        if (desp.getDes_val_cod().equals(String.valueOf((Object) ((TrabajadorVO) proy).getSexo()))) {
                            ((TrabajadorVO) proy).setDescSexo(desp.getDes_nom());
                            break;
                        }
                    }

                    for (DesplegableAdmonLocalVO desp : listaPensionista) {
                        if (desp.getDes_val_cod().equals(String.valueOf((Object) ((TrabajadorVO) proy).getPensionista()))) {
                            ((TrabajadorVO) proy).setDescPensionista(desp.getDes_nom());
                            break;
                        }
                    }

                    for (DesplegableAdmonLocalVO desp : listaTipoPensionista) {
                        if (desp.getDes_val_cod().equals(String.valueOf((Object) ((TrabajadorVO) proy).getTipoPensionista()))) {
                            ((TrabajadorVO) proy).setDescTipoPensionista(desp.getDes_nom());
                            break;
                        }
                    }

                } else if (proy instanceof EncargadoVO) {
                    for (DesplegableAdmonLocalVO desp : listaContratosTec) {
                        if (desp.getDes_val_cod().equals(((EncargadoVO) proy).getTipoContrato())) {
                            ((EncargadoVO) proy).setDescTipoContrato(desp.getDes_nom());
                            break;
                        }
                    }

                    for (DesplegableAdmonLocalVO desp : listaJornadaTec) {
                        if (desp.getDes_val_cod().equals(((EncargadoVO) proy).getTipoJornada())) {
                            ((EncargadoVO) proy).setDescTipoJornada(desp.getDes_nom());
                            break;
                        }
                    }
                    for (DesplegableAdmonLocalVO desp : listaPensionista) {
                        if (desp.getDes_val_cod().equals(String.valueOf((Object) ((EncargadoVO) proy).getPensionista()))) {
                            ((EncargadoVO) proy).setDescPensionista(desp.getDes_nom());
                            break;
                        }
                    }

                    for (DesplegableAdmonLocalVO desp : listaTipoPensionista) {
                        if (desp.getDes_val_cod().equals(String.valueOf((Object) ((EncargadoVO) proy).getTipoPensionista()))) {
                            ((EncargadoVO) proy).setDescTipoPensionista(desp.getDes_nom());
                            break;
                        }
                    }
                }
            }

        } catch (BDException bde) {
            log.error("Error al obtener la conexión con la BBDD");
            throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
        } catch (MeLanbide65Exception ex) {
            log.error("Ha ocurrido un error al recuperar las filas de la tabla " + nombreTabla + " para el expediente " + numExpediente);
            throw ex;
        } catch (SQLException ex) {
            log.error("Error en SQL");
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MeLanbide65Manager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con == null && nuevaCon != null) try {
                adapt.devolverConexion(nuevaCon);
            } catch (BDException ex) {
                log.error("Error al cerrar la conexión con la BBDD");
            }
        }

        return lista;
    }

    public boolean insertarDatos(PersonaVO persona, String nombreTabla, String numExpediente, AdaptadorSQLBD adapt) throws MeLanbide65Exception {
        boolean exito = false;
        Connection con = null;

        try {
            con = adapt.getConnection();

            exito = MeLanbide65DAO.getInstance().insertar(persona, nombreTabla, numExpediente, con);
        } catch (BDException bde) {
            log.error("Error al obtener la conexión a la BBDD");
            throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
        } catch (MeLanbide65Exception ex) {
            log.error("Ha ocurrido un error al insertar un registro en la tabla: " + nombreTabla);
            throw ex;
        } catch (SQLException ex) {
            log.error("Error en SQL");
        } finally {
            if (con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Erron al cerrar la conexión con la BBDD");
            }
        }

        return exito;
    }

    public PersonaVO obtenerDatosPorId(String nombreTabla, int ident, AdaptadorSQLBD adapt) throws MeLanbide65Exception {
        Connection con = null;
        PersonaVO persona = null;

        try {
            con = adapt.getConnection();
            persona = MeLanbide65DAO.getInstance().obtenerRegPorId(nombreTabla, ident, con);
        } catch (BDException bde) {
            log.error("Error al obtener la conexión a la BBDD");
            throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
        } catch (MeLanbide65Exception e) {
            log.error("Ha ocurrido un error al obtener un registro de la tabla " + nombreTabla);
            throw e;
        } catch (SQLException ex) {
            log.error("Error en SQL");
        } finally {
            if (con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Error al cerrar la conexión con la BBDD");
            }
        }

        return persona;
    }

    public boolean modificarDatos(PersonaVO persona, String nombreTabla, int ident, String numExpediente, AdaptadorSQLBD adapt) throws MeLanbide65Exception {
        Connection con = null;
        boolean exito = false;

        try {
            con = adapt.getConnection();
            exito = MeLanbide65DAO.getInstance().actualizar(persona, ident, nombreTabla, numExpediente, con);
        } catch (BDException bde) {
            log.error("Error al obtener la conexión a la BBDD");
            throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
        } catch (MeLanbide65Exception e) {
            log.error("Ha ocurrido un error al modificar datos de la tabla " + nombreTabla);
            throw e;
        } finally {
            if (con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Error al cerrar la conexión con la BBDD");
            }
        }

        return exito;
    }

    public boolean eliminarFila(int ident, String nombreTabla, AdaptadorSQLBD adapt) throws MeLanbide65Exception {
        Connection con = null;
        boolean exito = false;

        try {
            con = adapt.getConnection();
            exito = MeLanbide65DAO.getInstance().eliminar(ident, nombreTabla, con);
        } catch (BDException bde) {
            log.error("Error al obtener la conexión a la BBDD");
            throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
        } catch (MeLanbide65Exception e) {
            log.error("Ha ocurrido un error al eliminar datos de la tabla " + nombreTabla);
            throw e;
        } finally {
            if (con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Error al cerrar la conexión con la BBDD");
            }
        }

        return exito;
    }

    public String recalcularSubvencion(String numExp, AdaptadorSQLBD adapt) {
        if (log.isDebugEnabled()) {
            log.info("recalcularSubvención : BEGIN");
        }
        MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();
        Connection con = null;
        String rdo = null;
        String subvencion = "0";
        ErrorBean errorB = new ErrorBean();
        try {
            con = adapt.getConnection();

            rdo = meLanbide65DAO.recalcularSubvencion(numExp, con);

            if (esNumeroDecimal(rdo)) {
                subvencion = rdo;
                log.info("El recálculo de la subvención ha finalizado con éxito.");
            } else if (rdo.equalsIgnoreCase(ConstantesMeLanbide65.NOT_FOUND_IMP_UAAP)) {
                String error = "Error al obtener el valor de la subvención";
                errorB.setIdError(rdo);
                errorB.setMensajeError("La tabla MELANBIDE65_SUBVS_UAAP no contiene la información solicitada (No encuentra el valor de la subvención)");
                        errorB.setSituacion("recalcularSubvencion");
                MeLanbide65Manager.grabarError(errorB, error, error, numExp);
            } else if (rdo.equalsIgnoreCase(ConstantesMeLanbide65.NOT_FOUND_REG_UAAP)) {
                String error = "Error al obtener registros del Anexo A";
                errorB.setIdError(rdo);
                errorB.setMensajeError("No se han encontrado registros en el Anexo A (tabla MELANBIDE65_TRABAJADOR) para ese expediente");
                errorB.setSituacion("recalcularSubvencion");
                MeLanbide65Manager.grabarError(errorB, error, error, numExp);
            } else {
                String error = "Error Oracle en recálculo de subvención: " + rdo;
                errorB.setIdError(ConstantesMeLanbide65.ERROR_RECAL_UAAP);
                errorB.setMensajeError("Error Oracle realizando el recálculo de la subvención para el expediente UAAP");
                errorB.setSituacion("recalcularSubvencion");
                MeLanbide65Manager.grabarError(errorB, error, error, numExp);
            }

            if (log.isDebugEnabled()) {
                log.debug("recalcularSubvención : END");
            }

        } catch (SQLException e) {
            log.error("Error Oracle en recálculo de subvención: " + e);
            String error = "Error Oracle en recálculo de subvención: " + rdo;
            errorB.setIdError(ConstantesMeLanbide65.ERROR_RECAL_UAAP);
            errorB.setMensajeError("Error Oracle realizando el recálculo de la subvención para el expediente UAAP");
            errorB.setSituacion("recalcularSubvencion");
            MeLanbide65Manager.grabarError(errorB, error, error, numExp);
        } catch (Exception ex) {
            log.error("Error en la funcion recalcularSubvención: " + ex);
            String error = "Error en la funcion recalcularSubvención: " + ex.toString();

            errorB.setIdError(ConstantesMeLanbide65.ERROR_RECAL_UAAP);
            errorB.setMensajeError("Error en la funcion recalcularSubvención");
            errorB.setSituacion("recalcularSubvención");

                            MeLanbide65Manager.grabarError(errorB, error, error, numExp);
        } finally {
            if (con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Error al cerrar la conexión con la BBDD");
            }
        }

        return subvencion;

    }

    private boolean esNumeroDecimal(String cad) {
        try {
            if (cad.contains(",")) {
                cad = cad.replace(",", ".");
            }
            Double.valueOf(cad);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static void grabarError(ErrorBean error, String excepError, String traza, String numExp) {
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError);
            error.setTraza(excepError);
            error.setCausa(traza);
            log.error("causa: " + traza);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERRMISGEST/000000";
            }

            String idProcedimiento = "1"; // gestionAdaptadoresLan6Config.getCodProcedimientoPlatea("UAAP"); //convierteProcedimiento(codProcedimiento);
            log.error("procedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setIdClave("");
            error.setSistemaOrigen("REGEXLAN");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);
            log.error("Vamos a registrar el error");

            RegistroErrores.registroError(error);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    public boolean insertarSubvencion(int codOrganizacion,  String numExpediente, Double importeSubvencion, String nombreTabla, AdaptadorSQLBD adapt) throws MeLanbide65Exception {
        boolean exito = false;
        Connection con = null;

        try {
            con = adapt.getConnection();

            exito = MeLanbide65DAO.getInstance().insertarSubvencion(codOrganizacion, numExpediente, importeSubvencion, nombreTabla, con);
        } catch (BDException bde) {
            log.error("Error al obtener la conexión a la BBDD");
            throw new MeLanbide65Exception(1, "Problemas para obtener una conexión con la BBDD");
        } catch (MeLanbide65Exception ex) {
            log.error("Ha ocurrido un error al insertar un registro en la tabla: " + nombreTabla);
            throw ex;
        } catch (SQLException ex) {
            log.error("Error en SQL");
        } finally {
            if (con != null) try {
                adapt.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Erron al cerrar la conexión con la BBDD");
            }
        }

        return exito;
    }

    public List<ExpedienteUAAPVO> busquedaFiltrandoListaExpedientes(ExpUAAPCriteriosFiltroVO _criterioBusqueda, AdaptadorSQLBD adapt) throws Exception {
        List<ExpedienteUAAPVO> lista = new ArrayList<ExpedienteUAAPVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();
            lista = meLanbide65DAO.busquedaFiltrandoListaExpedientes(_criterioBusqueda, con);
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando Datos de lista de expedientes UAAP/AEXCE - Recalculo Importes", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos de lista de expedientes UAAP/AEXCE - Recalculo Importes ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error(" UAAP/AEXCE - recalculo Importes  - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int marcarRecalculoImportes(ExpUAAPCriteriosFiltroVO _criterioBusqueda, int codOrganizacion, String[] listaExpedientesMarcadosStr, String importeMaximo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean transactionStarted = false;
        int numExpedientesMarcados = 0;
        try {

            con = adapt.getConnection();
            MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();

            adapt.inicioTransaccion(con);
            transactionStarted = true;
            String listaExpedientes = "(";

            for (int i = 0; i < listaExpedientesMarcadosStr.length; i++) {
                String numExpediente = listaExpedientesMarcadosStr[i];
                if (listaExpedientes == "(") {
                    listaExpedientes = listaExpedientes + "'" + numExpediente + "'";
                } else {
                    listaExpedientes = listaExpedientes + "," + "'" + numExpediente + "'";
                }
                numExpedientesMarcados++;
            }
            listaExpedientes = listaExpedientes + ")";
            meLanbide65DAO.marcarRecalculoImportes(_criterioBusqueda, codOrganizacion, importeMaximo, listaExpedientesMarcadosStr, con);

            adapt.finTransaccion(con);
            log.info("numExpedientesMarcados = " + numExpedientesMarcados);
            return numExpedientesMarcados;
        } catch (BDException e) {
            log.error("Error al calcular el total 1 - Recalculo Importes", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error al calcular el total 2 - Recalculo Importes", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("- Recalculo Importes 3 - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean marcadoNoCalculo(int codOrg, int ejercicio, String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide65DAO m65DAO = MeLanbide65DAO.getInstance();
            return m65DAO.marcadoNoCalculo(codOrg, ejercicio, numExpediente, con);
        } catch (Exception e) {
            log.error("Ha ocurrido un error al recuperar el valor de CALCULAR de E_TDE. " + e);
            throw new Exception(e);
        } finally {
            adapt.devolverConexion(con);
        }
    }

    public String getDatosSuplementariosNIFPersonaContratada(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosSuplementariosNIFPersonaContratada");
        String nifPersonaContratada = "";

        Connection con = null;
        MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();

        try {
            con = adaptador.getConnection();
            nifPersonaContratada = meLanbide65DAO.getNIFPersonaContratada(numeroExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getDatosSuplementariosNIFPersonaContratada() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getDatosSuplementariosNIFPersonaContratada");

        return nifPersonaContratada;
    }

    public String getDatosSuplementariosNumeroOferta(String numeroExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosSuplementariosNumeroOferta");
        String numeroOferta = "";

        Connection con = null;
        MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();

        try {
            con = adaptador.getConnection();
            numeroOferta = meLanbide65DAO.getNumeroOferta(numeroExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getNumeroOferta() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

        log.info("FIN getDatosSuplementariosNumeroOferta");

        return numeroOferta;
    }

    public PersonaContratadaVO getDatosPersonaContratada(String nifPersonaContratada, String numeroOferta, AdaptadorSQLBD adaptador) throws Exception {
        log.info("Inicio getDatosPersonaContratada");
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();

        Connection con = null;
        MeLanbide65DAO meLanbide65DAO = MeLanbide65DAO.getInstance();

        try {
            con = adaptador.getConnection();
            personaContratada = meLanbide65DAO.getPersonaContratada(nifPersonaContratada, numeroOferta, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion recuperando la conexión a BBDD", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en getPersonaContratada() manager" + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.info("FIN getDatosPersonaContratada");
        return personaContratada;
    }
}
