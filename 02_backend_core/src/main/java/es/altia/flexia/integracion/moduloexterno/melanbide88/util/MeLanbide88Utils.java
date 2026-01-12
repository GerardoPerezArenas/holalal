/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide88.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
//import es.altia.merlin.licitacion.commons.utils.PortableContext;
import es.altia.technical.PortableContext;

import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author INGDGC
 */
public class MeLanbide88Utils {
    
    
    private static final Logger LOG = LogManager.getLogger(MeLanbide88Utils.class);

    /**
     * Operacion que recupera los datos de conexion a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;

        if (LOG.isDebugEnabled()) {
            LOG.debug("getJndi =========> ");
            LOG.debug("parametro codOrganizacion: " + codOrganizacion);
            LOG.debug("gestor: " + gestor);
            LOG.debug("jndi: " + jndiGenerico);
        }

        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("He cogido el jndi: " + jndiGenerico);
                }
                DataSource ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexion al esquema generico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
                    String[] salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (SQLException e) {
                LOG.error(e.getStackTrace());
            } catch (TechnicalException ex) {
                java.util.logging.Logger.getLogger(MeLanbide88Utils.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                        conGenerico.close();
                    }
                } catch (SQLException e) {
                    LOG.error(e.getStackTrace());
                }//try-catch
            }// finally
            if (LOG.isDebugEnabled()) {
                LOG.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection
    
    public static String getCodProcedimientoDeExpediente(String numExpediente) {
        try {
            String[] datos = numExpediente.split("/");
            return datos[1];
        } catch (Exception ex) {
            return null;
        }
    }
    
    /**
     * Decodifica un array de bite en una cadena e texto
     *
     * @param input
     * @return Cadena de texto resultado de la decodificacion desde el byte[]
     * @throws IOException
     */
    public static String decodeText(byte[] input) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(input)
                )
        ).readLine();
    }

    /**
     * Decodifica una cadena de texto a un endogin especifico.
     *
     * @param textoInput
     * @param encodig
     * @return
     * @throws IOException
     */
    public static String decodeText(String textoInput, String encodig) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(textoInput.getBytes(encodig)
                        )
                )
        ).readLine();
    }
    
    public int getIdiomaUsuarioFromRequest(HttpServletRequest request) {
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            return (usuario != null ? usuario.getIdioma() : 1);
        } catch (Exception ex) {
            LOG.error("Error leyendo Idioma del Usuario: " + ex.getMessage(), ex);
            ex.printStackTrace();
            return 1;
        }
    }
    
    public static void parsearRespuestasEnviarJSON(HttpServletRequest request, HttpServletResponse response, Object respuesta) {
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuesta);
        LOG.info("respuestaJsonString : " + respuestaJsonString);
        try {
            PrintWriter out = response.getWriter();
            // Codificamos con UTF-8 Encodig de la request para los caracteres especiales o tildes
            if (respuestaJsonString != null && !respuestaJsonString.isEmpty()) {
                respuestaJsonString = decodeText(respuestaJsonString, request.getCharacterEncoding());
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            LOG.error("Error preparando respuesta " + e.getMessage(), e);
        }
    }
}
