/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide81.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.DesplegableVO;
import es.altia.technical.PortableContext;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide81Utils {

    private static final Logger log = LogManager.getLogger(MeLanbide81Utils.class);

    /**
     * Operacion que recupera los datos de conexion a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;

        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
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
                log.error(e.getStackTrace());
            } catch (TechnicalException ex) {
                java.util.logging.Logger.getLogger(MeLanbide81Utils.class.getName()).log(Level.SEVERE, null, ex);
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
                    log.error(e.getStackTrace());
                }//try-catch
            }// finally

        }// synchronized
        return adapt;
    }//getConnection

    public static String getCodProc(String numExpediente) {
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
     * Decodifica una cadena de texto a un encoding especifico
     *
     * @param textoInput
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String decodeText(String textoInput, String encoding) throws IOException {
        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(textoInput.getBytes(encoding)
                        )
                )
        ).readLine();
    }

    /**
     * Método que extrae la descripcion de los desplegables en el idioma del
     * usuario, en BBDD estan en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    public List<DesplegableVO> traducirDesplegable(HttpServletRequest request, List<DesplegableVO> desplegable) {
        for (DesplegableVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    /**
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    public String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraSeparadoraDobleIdiomaDesple);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide81.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }
            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
    }

    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    public int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }

    /**
     * Devuelve la extension de fichero correspondiente al tipo MIME
     * pasado.Utilizamos la clase de flexia para no reescribir funciones
     *
     * @param extension
     * @return
     */
    public String getMimeTypeFromExtension(String extension) {
        return MimeTypes.guessMimeTypeFromExtension(extension);
    }

    /**
     * @param fechaIn
     * @param fechaFinal
     * @return
     */
    public int restarFechas(Date fechaIn, Date fechaFinal) {
        GregorianCalendar fechaInicio = new GregorianCalendar();
        fechaInicio.setTime(fechaIn);
        GregorianCalendar fechaFin = new GregorianCalendar();
        fechaFin.setTime(fechaFinal);
        int dias = 0;
        if (fechaFin.get(Calendar.YEAR) == fechaInicio.get(Calendar.YEAR)) {

            dias = (fechaFin.get(Calendar.DAY_OF_YEAR) - fechaInicio.get(Calendar.DAY_OF_YEAR)) + 1;
        } else {
            int rangoAnyos = fechaFin.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);

            for (int i = 0; i <= rangoAnyos; i++) {
                int diasAnio = fechaInicio.isLeapYear(fechaInicio.get(Calendar.YEAR)) ? 366 : 365;
                if (i == 0) {
                    dias = 1 + dias + (diasAnio - fechaInicio.get(Calendar.DAY_OF_YEAR));
                } else if (i == rangoAnyos) {
                    dias = dias + fechaFin.get(Calendar.DAY_OF_YEAR);
                } else {
                    dias = dias + diasAnio;
                }
            }
        }
        return dias;
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    public void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * @param request
     * @param response
     * @param respuesta
     */
    public static void parsearRespuestasEnviarJSON(HttpServletRequest request, HttpServletResponse response, Object respuesta) {
        GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("dd/MM/yyyy");
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(respuesta);
        log.info("respuestaJsonString : " + respuestaJsonString);
        try {
            PrintWriter out = response.getWriter();
            // Codificamos con UTF-8 encoding de la request para los caracteres especiales o tildes
            if (respuestaJsonString != null && !respuestaJsonString.isEmpty()) {
                respuestaJsonString = decodeText(respuestaJsonString, request.getCharacterEncoding());
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
        }
    }
}
