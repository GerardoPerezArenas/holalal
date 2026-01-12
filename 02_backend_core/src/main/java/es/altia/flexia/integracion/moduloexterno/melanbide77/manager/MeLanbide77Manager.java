/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide77.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide77.dao.MeLanbide77DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroAerteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import java.sql.Connection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide77Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide77Manager.class);

    //Instancia
    private static MeLanbide77Manager instance = null;

    public static MeLanbide77Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide77Manager.class) {
                instance = new MeLanbide77Manager();
            }
        }
        return instance;
    }

    public InteresadoExpedienteVO getDatosInteresado(int codOrganizacion, int ejercicio, RegistroAerteVO registro, Connection con) throws Exception {
        try {
            return MeLanbide77DAO.getDatosInteresado(codOrganizacion, ejercicio, registro, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del tercero del registro " + registro.getResEje() + "/" + registro.getResNum(), ex);
            throw new Exception(ex);
        }
    }

}
