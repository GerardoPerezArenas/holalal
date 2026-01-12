/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.clientws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.request.DatosEspecificos;
import es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.request.Persona;
import es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.request.Request;
import es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.request.Tramitador;
import es.altia.flexia.integracion.moduloexterno.melanbide67.ws.client.vidalaboralws.response.Response;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;

/**
 *
 * @author pablo.bugia
 */
public class ClientWSVidaLaboral {

    private static final Logger log = Logger.getLogger(ClientWSVidaLaboral.class);

    public static Response getVidaLaboral(final Persona persona, final String fechaDesde,
            final String fechaHasta) {
        try {
            final URL url = new URL(ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_VIDALABORAL_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // 
            conn.setDoOutput(true);

            // Crear el cuerpo de la petición
            Request req = new Request(new DatosEspecificos(fechaDesde, fechaHasta),
                    persona,
                    new Tramitador(ConfigurationParameter.getParameter(ConstantesMeLanbide67.COD_PROCEDIMIENTO_TRAMITADOR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                            ConfigurationParameter.getParameter(ConstantesMeLanbide67.NOMBRE_PROCEDIMIENTO_TRAMITADOR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                            ConfigurationParameter.getParameter(ConstantesMeLanbide67.FINALIDAD_PROCEDIMIENTO_TRAMITADOR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                            ConfigurationParameter.getParameter(ConstantesMeLanbide67.CONSENTIMIENTO_FIRMADO, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                            ConfigurationParameter.getParameter(ConstantesMeLanbide67.NIF_USUARIO_TRAMITADOR, ConstantesMeLanbide67.FICHERO_PROPIEDADES),
                            ConfigurationParameter.getParameter(ConstantesMeLanbide67.USUARIO_TRAMITADOR, ConstantesMeLanbide67.FICHERO_PROPIEDADES)));

            final Gson gson = new Gson();
            String jsonString = gson.toJson(req);
            log.info("ClientWSVidaLaboral getVidaLaboral json a enviar: " + jsonString);

            final OutputStream os = conn.getOutputStream();
            byte[] input = jsonString.getBytes("utf-8");
            os.write(input, 0, input.length);

            // Leer la respuesta
            final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            final StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            log.info("ClientWSVidaLaboral getVidaLaboral respuesta en json: " + response.toString());
            final Response respuesta = new Gson().fromJson(response.toString(), Response.class);
            log.info("ClientWSVidaLaboral getVidaLaboral respuesta en objeto: " + respuesta);
            return respuesta;
        } catch (MalformedURLException ex) {
            log.info("ClientWSVidaLaboral getVidaLaboral: " + ex.getMessage());
            return null;
        } catch (IOException ex) {
            log.info("ClientWSVidaLaboral getVidaLaboral: " + ex.getMessage());
            return null;
        } catch (RuntimeException ex) {
            log.info("ClientWSVidaLaboral getVidaLaboral: " + ex.getMessage());
            return null;
        } catch (Exception ex) {
            log.info("ClientWSVidaLaboral getVidaLaboral: " + ex.getMessage());
            return null;
        }
    }
}
