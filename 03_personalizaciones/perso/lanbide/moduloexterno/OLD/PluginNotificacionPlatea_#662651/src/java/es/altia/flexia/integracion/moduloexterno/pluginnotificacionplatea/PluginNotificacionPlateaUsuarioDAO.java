package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORsDAO;
import es.altia.agora.business.escritorio.UsuarioEscritorioValueObject;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.escritorio.UserPreferences;
import es.altia.agora.business.escritorio.exception.UsuarioNoEncontradoException;
import es.altia.agora.business.escritorio.exception.UsuarioNoValidadoException;
import es.altia.agora.business.util.*;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import es.altia.util.EncriptacionContrasenha;
import es.altia.util.commons.DateOperations;
import es.altia.util.exceptions.InternalErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import es.altia.util.conexion.*;
import es.altia.util.jdbc.GeneralOperations;
import java.util.StringTokenizer;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import java.util.Vector;

public class PluginNotificacionPlateaUsuarioDAO {

	protected static Config m_ConfigTechnical;
	protected static Config m_ConfigError;
	protected static Log m_Log = LogFactory.getLog(PluginNotificacionPlateaUsuarioDAO.class.getName());

    private static PluginNotificacionPlateaUsuarioDAO instance = null;

    protected static String usu_cod;
	protected static String usu_idi;
	protected static String usu_nom;
	protected static String usu_log;
	protected static String usu_pas;
	protected static String usu_blq;

	protected static String apl_cod;
	protected static String apl_nom;
	protected static String apl_exe;
    protected static String apl_ico;

    protected static String aau_apl;
	protected static String aau_usu;

	protected static String org_cod;
	protected static String org_des;
	protected static String org_ico;
	protected static String org_ine;

	protected static String uae_org;
	protected static String uae_ent;
	protected static String uae_apl;
	protected static String uae_usu;

	protected static String ent_cod;
	protected static String ent_nom;
	protected static String ent_org;
	protected static String ent_dtr;

	protected static String uou_uor;
	protected static String uou_usu;
	protected static String uou_org;
	protected static String uou_ent;

	protected static String uor_nom;
        protected static String uor_rex_general;
	protected static String uor_cod;
	protected static String uor_tip;
	protected static String uor_novisreg;
        
	protected static String dep_nom_defecto;
	protected static String dep_cod_defecto;

	protected static String aid_tex;
	protected static String aid_idi;
	protected static String aid_apl;

	protected static String rpu_usu;
	protected static String rpu_org;
	protected static String rpu_ent;
	protected static String rpu_apl;
	protected static String rpu_pro;
	protected static String rpu_tip;

	protected static String ous_usu;
	protected static String ous_org;

	protected static String aae_org;
	protected static String aae_ent;
	protected static String aae_apl;

	protected static String ugo_gru;
	protected static String ugo_usu;
	protected static String ugo_org;
	protected static String ugo_ent;
	protected static String ugo_apl;

	protected static String rpg_org;
	protected static String rpg_ent;
	protected static String rpg_apl;
	protected static String rpg_tip;
	protected static String rpg_gru;
	protected static String rpg_pro;

	protected static String pro_apl;
    protected static String pro_cod;
    protected static String pro_frm;

    protected static String pro_frm_expedientes;
    protected static String pro_frm_padron;

    protected static String organizacion;
	protected static String entidad;
	protected static String aplicacion;
	protected static String ejercicio;
	protected static String gestor;
	protected static String driver;
	protected static String url;
	protected static String usuario;
	protected static String password;
	protected static String fichlog;
	protected static String jndi;

	protected static String gestorD;
	protected static String driverD;
	protected static String urlD;
	protected static String usuarioD;
	protected static String passwordD;
	protected static String fichLogD;
	protected static String jndiD;

    public static String usu_nif;


	protected PluginNotificacionPlateaUsuarioDAO() {
		super();
		//Queremos usar el fichero de	configuracion techserver
		m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
		//Queremos tener acceso	a los	mensajes de	error	localizados
		m_ConfigError = ConfigServiceHelper.getConfig("error");

        usu_cod = m_ConfigTechnical.getString("SQL.A_USU.codigo");
		usu_idi = m_ConfigTechnical.getString("SQL.A_USU.idioma");
		usu_nom = m_ConfigTechnical.getString("SQL.A_USU.nombre");
		usu_log = m_ConfigTechnical.getString("SQL.A_USU.login");
		usu_pas = m_ConfigTechnical.getString("SQL.A_USU.password");
		usu_blq = m_ConfigTechnical.getString("SQL.A_USU.estado");

                apl_cod = m_ConfigTechnical.getString("SQL.A_APL.codigo");
		apl_nom = m_ConfigTechnical.getString("SQL.A_APL.nombre");
		apl_exe = m_ConfigTechnical.getString("SQL.A_APL.url");
		apl_ico = m_ConfigTechnical.getString("SQL.A_APL.icono");

        aau_apl = m_ConfigTechnical.getString("SQL.A_AAU.aplicacion");
		aau_usu = m_ConfigTechnical.getString("SQL.A_AAU.usuario");

        org_cod = m_ConfigTechnical.getString("SQL.A_ORG.codigo");
		org_des = m_ConfigTechnical.getString("SQL.A_ORG.descripcion");
		org_ico = m_ConfigTechnical.getString("SQL.A_ORG.icono");
		org_ine = m_ConfigTechnical.getString("SQL.A_ORG.codINE");

		uae_org = m_ConfigTechnical.getString("SQL.A_UAE.organizacion");
		uae_ent = m_ConfigTechnical.getString("SQL.A_UAE.entidad");
		uae_apl = m_ConfigTechnical.getString("SQL.A_UAE.aplicacion");
		uae_usu = m_ConfigTechnical.getString("SQL.A_UAE.usuario");

		ent_cod = m_ConfigTechnical.getString("SQL.A_ENT.codigo");
		ent_nom = m_ConfigTechnical.getString("SQL.A_ENT.nombre");
		ent_org = m_ConfigTechnical.getString("SQL.A_ENT.organizacion");
		ent_dtr = m_ConfigTechnical.getString("SQL.A_ENT.dirTrabajo");

		uou_uor = m_ConfigTechnical.getString("SQL.A_UOU.unidadOrg");
		uou_usu = m_ConfigTechnical.getString("SQL.A_UOU.usuario");
		uou_org = m_ConfigTechnical.getString("SQL.A_UOU.organizacion");
		uou_ent = m_ConfigTechnical.getString("SQL.A_UOU.entidad");

		uor_nom = m_ConfigTechnical.getString("SQL.A_UOR.nombre");
                uor_rex_general= m_ConfigTechnical.getString("SQL.A_UOR.rex_xeral");
		uor_cod = m_ConfigTechnical.getString("SQL.A_UOR.codigo");
		uor_tip = m_ConfigTechnical.getString("SQL.A_UOR.tipo");
		uor_novisreg = m_ConfigTechnical.getString("SQL.A_UOR.noVisRegistro");

		dep_nom_defecto = "DEPARTAMENTO1";
		dep_cod_defecto = "1";

		aid_tex = m_ConfigTechnical.getString("SQL.A_AID.texto");
		aid_idi = m_ConfigTechnical.getString("SQL.A_AID.idioma");
		aid_apl = m_ConfigTechnical.getString("SQL.A_AID.aplicacion");

		rpu_usu = m_ConfigTechnical.getString("SQL.A_RPU.usuario");
		rpu_org = m_ConfigTechnical.getString("SQL.A_RPU.organizacion");
		rpu_ent = m_ConfigTechnical.getString("SQL.A_RPU.entidad");
		rpu_apl = m_ConfigTechnical.getString("SQL.A_RPU.aplicacion");
		rpu_pro = m_ConfigTechnical.getString("SQL.A_RPU.proceso");
		rpu_tip = m_ConfigTechnical.getString("SQL.A_RPU.tipo");

		ous_usu = m_ConfigTechnical.getString("SQL.A_OUS.usuario");
		ous_org = m_ConfigTechnical.getString("SQL.A_OUS.organizacion");

		aae_org = m_ConfigTechnical.getString("SQL.A_AAE.organizacion");
		aae_ent = m_ConfigTechnical.getString("SQL.A_AAE.entidad");
		aae_apl = m_ConfigTechnical.getString("SQL.A_AAE.aplicacion");

		ugo_gru = m_ConfigTechnical.getString("SQL.A_UGO.grupo");
		ugo_usu = m_ConfigTechnical.getString("SQL.A_UGO.usuario");
		ugo_org = m_ConfigTechnical.getString("SQL.A_UGO.organizacion");
		ugo_ent = m_ConfigTechnical.getString("SQL.A_UGO.entidad");
		ugo_apl = m_ConfigTechnical.getString("SQL.A_UGO.aplicacion");

		rpg_org = m_ConfigTechnical.getString("SQL.A_RPG.organizacion");
		rpg_ent = m_ConfigTechnical.getString("SQL.A_RPG.entidad");
		rpg_apl = m_ConfigTechnical.getString("SQL.A_RPG.aplicacion");
		rpg_tip = m_ConfigTechnical.getString("SQL.A_RPG.tipo");
		rpg_gru = m_ConfigTechnical.getString("SQL.A_RPG.grupo");
		rpg_pro = m_ConfigTechnical.getString("SQL.A_RPG.proceso");

		pro_apl = m_ConfigTechnical.getString("SQL.A_PRO.aplicacion");
        pro_cod = m_ConfigTechnical.getString("SQL.A_PRO.codigo");
        pro_frm = m_ConfigTechnical.getString("SQL.A_PRO.formulario");

        pro_frm_expedientes = m_ConfigTechnical.getString("SQL.A_PRO.formulario.tramitacion");
        pro_frm_padron = m_ConfigTechnical.getString("SQL.A_PRO.formulario.gestion");

        organizacion = m_ConfigTechnical.getString("SQL.A_EEA.organizacion");
		entidad = m_ConfigTechnical.getString("SQL.A_EEA.entidad");
		aplicacion = m_ConfigTechnical.getString("SQL.A_EEA.aplicacion");
		ejercicio = m_ConfigTechnical.getString("SQL.A_EEA.ejercicio");
		gestor = m_ConfigTechnical.getString("SQL.A_EEA.gestor");
		driver = m_ConfigTechnical.getString("SQL.A_EEA.driver");
		url = m_ConfigTechnical.getString("SQL.A_EEA.url");
		usuario = m_ConfigTechnical.getString("SQL.A_EEA.usuario");
		password = m_ConfigTechnical.getString("SQL.A_EEA.password");
		fichlog = m_ConfigTechnical.getString("SQL.A_EEA.fichlog");
		jndi = m_ConfigTechnical.getString("SQL.A_EEA.jndi");

        jndiD = m_ConfigTechnical.getString("CON.jndi");
        gestorD = m_ConfigTechnical.getString("CON.gestor");

		driverD = m_ConfigTechnical.getString("CON.driver");
		urlD = m_ConfigTechnical.getString("CON.url");
		usuarioD = m_ConfigTechnical.getString("CON.usuario");
		passwordD = m_ConfigTechnical.getString("CON.password");
		fichLogD = m_ConfigTechnical.getString("CON.fichlog");

        usu_nif = m_ConfigTechnical.getString("SQL.A_USU.nif");
	}
        
        
        /**
	* Factory method para el<code>Singelton</code>.
	 * @return La unica instancia de UsuarioDAO.The	only CustomerDAO instance.
	*/
	public static PluginNotificacionPlateaUsuarioDAO getInstance() {
		if (instance == null) {
			synchronized (PluginNotificacionPlateaUsuarioDAO.class) {
				if (instance == null) instance = new PluginNotificacionPlateaUsuarioDAO();
			}
		}
		return instance;
	}
        
        
           /**
     * Recupera el nombre completo de un usuario
     * @param codUsuario: Código del usuario
     * @param con
     * @return [0]: nombre, [1]: nif
     * @throws es.altia.common.exception.TechnicalException
     */
     public String[] getNombreNifUsuario(int codUsuario, String[] params)
            throws TechnicalException {

        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String[] result = new String[2];
        String sql = "";

	try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT " + usu_nom + ", " + usu_nif
                + " FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE "
                + usu_cod + " ='" + codUsuario + "'";

            if (m_Log.isDebugEnabled()) m_Log.debug("existeUsuarioCodigo: " + sql);
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                result[0] = rs.getString(usu_nom);
                result[1] = rs.getString(usu_nif);
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al recuperar nombre del usuario: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return result;
        }

        
    }      
    
}//class
