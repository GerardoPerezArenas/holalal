package es.altia.flexia.integracion.moduloexterno.melanbide15.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide15.dao.MeLanbide15DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.MeLanbide15Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.IdentidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.FormacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.OrientacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide15Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide15Manager.class);
    private final MeLanbide15DAO m15DAO = new MeLanbide15DAO();
    private static final MeLanbide15Utils m15Utils = new MeLanbide15Utils();

    //Instancia
    private static MeLanbide15Manager instance = null;

    public static MeLanbide15Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide15Manager.class) {
                instance = new MeLanbide15Manager();
            }
        }
        return instance;
    }

    /**
     *
     * @param des_cod
     * @param adapt
     * @return
     * @throws Exception
     */
    public List<DesplegableVO> getValoresDesplegables(String des_cod, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return m15DAO.getValoresE_Des(des_cod, con);
        } catch (BDException e) {
            log.error("Error en BBDD recuperando valores del desplegable: " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error inesperado recuperando valores del desplegable: " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            cerrarConexion(adapt, con, "getValoresDesplegables");
        }
    }

    public List<IdentidadVO> getListaIdentidad(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<IdentidadVO> lista = new ArrayList<IdentidadVO>();
        Connection con = null;

        try {
            // Obtener conexión
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            lista = meLanbide15DAO.getListaIdentidad(numExp, codOrganizacion, con);
            List<DesplegableVO> listaDniNie = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaSexo = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_SEXO, ConstantesMeLanbide15.FICHERO_PROPIEDADES), adapt);
            List<DesplegableVO> listaSustituto = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_BOOL, ConstantesMeLanbide15.FICHERO_PROPIEDADES), adapt);

            for (IdentidadVO identidad : lista) {
                for (DesplegableVO desp : listaDniNie) {
                    if (desp.getDes_val_cod().equals(identidad.getDniNie())) {
                        identidad.setDescDniNie(desp.getDes_nom());
                        break;
                    }
                }
                for (DesplegableVO desp : listaSexo) {
                    if (desp.getDes_val_cod().equals(identidad.getSexo())) {
                        identidad.setDescSexo(desp.getDes_nom());
                        break;
                    }
                }

                for (DesplegableVO desp : listaSustituto) {
                    if (desp.getDes_val_cod().equals(identidad.getSustituto())) {
                        identidad.setDescSustituto(desp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de Identidad ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public IdentidadVO getIdentidadPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            return meLanbide15DAO.getIdentidadPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una identidad:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una identidad:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarIdentidad(int id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            // Llamar al DAO y convertir el resultado booleano a int
            boolean eliminado = meLanbide15DAO.eliminarIdentidad(id, numExp, con);
            return eliminado ? 1 : 0; // Convertir booleano a entero
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una identidad: ID=" + id + ", NUM_EXP=" + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción al eliminar una identidad: ID=" + id + ", NUM_EXP=" + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaIdentidad(IdentidadVO nuevaIdentidad, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            insertOK = meLanbide15DAO.crearNuevaIdentidad(nuevaIdentidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una identidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una identidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarIdentidad(IdentidadVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;
        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            insertOK = meLanbide15DAO.modificarIdentidad(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una identidad : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una identidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public List<FormacionVO> getListaFormacion(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<FormacionVO> lista = new ArrayList<FormacionVO>();
        Connection con = null;

        try {
            // Establecer conexión
            con = adapt.getConnection();

            // Obtener instancia del DAO
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            // Obtener la lista de formaciones
            lista = meLanbide15DAO.getListaFormacion(numExp, codOrganizacion, con);

            // Obtener el desplegable de DNI/NIE y mapear por código
            List<DesplegableVO> listaDniNie = getValoresDesplegables(
                    ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES),
                    adapt
            );

            // Crear un mapa para mejorar el rendimiento de las búsquedas
            Map<String, String> mapaDesplegable = new HashMap<String, String>();
            for (DesplegableVO desp : listaDniNie) {
                mapaDesplegable.put(desp.getDes_val_cod(), desp.getDes_nom());
            }

            // Asignar la descripción de DNI/NIE a cada formación
            for (FormacionVO formacion : lista) {
                if (mapaDesplegable.containsKey(formacion.getDniFor())) {
                    formacion.setDescDniFor(mapaDesplegable.get(formacion.getDniFor()));
                }
            }

            return lista;
        } catch (BDException e) {
            log.error("Error recuperando la lista de Formacion", e);
            throw new Exception(e); // Relanzar la excepción envuelta
        } finally {
            // Devolver la conexión al pool
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD", e);
            }
        }
    }

    public FormacionVO getFormacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            // Obtener conexión del adaptador
            con = adapt.getConnection();

            // Obtener instancia del DAO
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            // Recuperar formación por ID
            return meLanbide15DAO.getFormacionPorID(id, con);

        } catch (BDException e) {
            log.error("Error recuperando datos sobre una formación: " + id, e);
            throw new Exception("Error al recuperar la formación con ID: " + id, e);
        } finally {
            // Asegurar cierre de conexión
            if (con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception e) {
                    log.error("Error al devolver la conexión a la BBDD", e);
                }
            }
        }
    }

    public boolean crearNuevaFormacion(FormacionVO nuevaFormacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK = false;

        try {
            // Obtener conexión del adaptador
            con = adapt.getConnection();

            // Obtener instancia del DAO
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            // Intentar crear la nueva formación
            insertOK = meLanbide15DAO.crearNuevaFormacion(nuevaFormacion, con);

        } catch (BDException e) {
            log.error("Error creando una formación: " + nuevaFormacion.getId(), e);
            throw new Exception("Error al intentar crear la formación con ID: " + nuevaFormacion.getId(), e);
        } finally {
            // Asegurar cierre de conexión
            if (con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception e) {
                    log.error("Error al devolver la conexión a la BBDD", e);
                }
            }
        }

        return insertOK;
    }

    public boolean modificarFormacion(FormacionVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean updateOK = false;

        try {
            // Obtener conexión del adaptador
            con = adapt.getConnection();

            // Obtener instancia del DAO
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            // Intentar actualizar la formación
            updateOK = meLanbide15DAO.modificarFormacion(datModif, con);

        } catch (BDException e) {
            log.error("Error actualizando la formación con ID: " + datModif.getId() + ", Mensaje: " + e.getMessage(), e);
            throw new Exception("Error al intentar actualizar la formación con ID: " + datModif.getId(), e);
        } finally {
            // Asegurar cierre de conexión
            if (con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception e) {
                    log.error("Error al devolver la conexión a la BBDD", e);
                }
            }
        }

        return updateOK;
    }

    public int eliminarFormacion(int id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            // Obtener la conexión
            con = adapt.getConnection();

            // Instanciar el DAO
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            // Ejecutar eliminación
            boolean eliminado = meLanbide15DAO.eliminarFormacion(id, numExp, con);

            // Log y retorno del resultado
            if (eliminado) {
                log.info("Formación con ID: " + id + " y número de expediente: " + numExp + " eliminada correctamente.");
                return 1;
            } else {
                log.warn("No se pudo eliminar la formación con ID: " + id + " y número de expediente: " + numExp);
                return 0;
            }

        } catch (Exception e) {
            // Log del error
            log.error("Error al intentar eliminar la formación con ID: " + id + " y número de expediente: " + numExp, e);
            throw new Exception("Error eliminando formación: " + e.getMessage(), e);

        } finally {
            // Asegurar cierre de la conexión
            if (con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (Exception e) {
                    log.error("Error al devolver la conexión a la BBDD", e);
                }
            }
        }
    }

    public List<OrientacionVO> getListaOrientacion(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<OrientacionVO> lista = new ArrayList<OrientacionVO>();
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            lista = meLanbide15DAO.getListaOrientacion(numExp, codOrganizacion, con);

            List<DesplegableVO> listaDniNie = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), adapt);

            for (OrientacionVO orientacion : lista) {
                for (DesplegableVO desp : listaDniNie) {
                    if (desp.getDes_val_cod().equals(orientacion.getDniOri())) {
                        orientacion.setDescDniOri(desp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de orientación ", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public OrientacionVO getOrientacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            return meLanbide15DAO.getOrientacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una orientacion:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una orientacion:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaOrientacion(OrientacionVO nuevaOrientacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK = false;

        try {
            // Validación del objeto nuevaOrientacion
            if (nuevaOrientacion == null) {
                log.error("El objeto nuevaOrientacion es nulo");
                throw new IllegalArgumentException("El objeto nuevaOrientacion no puede ser nulo");
            }

            log.debug("Validando parámetros de nuevaOrientacion:");
            log.debug("ID: " + nuevaOrientacion.getId());
            log.debug("NUM_EXP: " + nuevaOrientacion.getNumExp());
            log.debug("DNI_ORI: " + nuevaOrientacion.getDniOri());
            log.debug("NUM_IDEN_ORI: " + nuevaOrientacion.getNumIdenOri());
            log.debug("HORAS_ORI: " + nuevaOrientacion.getHorasOri());
            log.debug("SUBVENCION_ORI: " + nuevaOrientacion.getSubvencionOri());

            // Obtener conexión a la base de datos
            con = adapt.getConnection();
            if (con == null) {
                log.error("La conexión a la base de datos es nula");
                throw new Exception("No se pudo obtener la conexión a la base de datos");
            }

            // Llamar al DAO para insertar los datos
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            insertOK = meLanbide15DAO.crearNuevaOrientacion(nuevaOrientacion, con);

            if (insertOK) {
                log.info("La orientación se ha insertado correctamente en la base de datos");
            } else {
                log.error("No se pudo insertar la orientación en la base de datos");
            }

        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una orientación: " + e.getMessage(), e);
            throw new Exception("Error en la base de datos: ", e);

        } catch (Exception ex) {
            log.error("Se ha producido una excepción creando una orientación: " + ex.getMessage(), ex);
            throw new Exception("Error general al crear orientación: ", ex);

        } finally {
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                    log.debug("Conexión devuelta correctamente");
                }
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage(), e);
            }
        }

        return insertOK;
    }

    public boolean modificarOrientacion(OrientacionVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            insertOK = meLanbide15DAO.modificarOrientacion(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una orientación : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una identidad : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public int eliminarOrientacion(int id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            boolean eliminado = meLanbide15DAO.eliminarOrientacion(id, numExp, con);
            return eliminado ? 1 : 0;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una orientacion: ID=" + id + ", NUM_EXP=" + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción al eliminar una orientacion: ID=" + id + ", NUM_EXP=" + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<ContratacionVO> getListaContratacion(String numExp, int codOrganizacion, AdaptadorSQLBD adapt) throws Exception {
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            lista = meLanbide15DAO.getListaContratacion(numExp, codOrganizacion, con);

            List<DesplegableVO> listaDniNie = getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), adapt);

            for (ContratacionVO contratacion : lista) {
                for (DesplegableVO desp : listaDniNie) {
                    if (desp.getDes_val_cod().equals(contratacion.getDniCon())) {
                        contratacion.setDescDniCon(desp.getDes_nom());
                        break;
                    }
                }
            }
            return lista;
        } catch (Exception e) {
            log.error("Error recuperando la lista de Contratacion", e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD", e);
            }
        }
    }

    public ContratacionVO getContratacionPorID(String id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            return meLanbide15DAO.getContratacionPorID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una Contratacion:  " + id, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos sobre una Contratacion:  " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean crearNuevaContratacion(ContratacionVO nuevaContratacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean insertOK;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            insertOK = meLanbide15DAO.crearNuevaContratacion(nuevaContratacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD creando una Contratacion : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD creando una Contratacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }

    public boolean modificarContratacion(ContratacionVO datModif, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean updateOK;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();
            updateOK = meLanbide15DAO.modificarContratacion(datModif, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD actualizando una Contratacion : " + e.getMessage(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD actualizando una Contratacion : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return updateOK;
    }

    public int eliminarContratacion(int id, String numExp, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;

        try {
            con = adapt.getConnection();
            MeLanbide15DAO meLanbide15DAO = MeLanbide15DAO.getInstance();

            boolean eliminado = meLanbide15DAO.eliminarContratacion(id, numExp, con);
            return eliminado ? 1 : 0;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al eliminar una Contratacion: ID=" + id + ", NUM_EXP=" + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción al eliminar una Contratacion: ID=" + id + ", NUM_EXP=" + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

// Método auxiliar para cerrar conexiones y manejar excepciones
    private void cerrarConexion(AdaptadorSQLBD adapt, Connection con, String metodo) {
        try {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        } catch (Exception e) {
            log.error("Error al cerrar conexión en el método " + metodo + ": " + e.getMessage());
        }
    }

}
