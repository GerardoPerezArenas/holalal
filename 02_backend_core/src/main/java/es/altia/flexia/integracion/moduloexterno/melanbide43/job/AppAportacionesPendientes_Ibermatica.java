package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Aportacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6AportacionDoc;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Tramite;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * Unit test for simple App.
 */
public class AppAportacionesPendientes_Ibermatica {

    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    private static Calendar obtenerCalendar3(String fecha) {
        // formato de fecha dd/MM/YYYY hh:mm:ss
        Calendar fechaCalendar = Calendar.getInstance();
        try {

            int dia = Integer.parseInt(fecha.trim().substring(0, 2));
            int mes = Integer.parseInt(fecha.trim().substring(3, 5));
            int anio = Integer.parseInt(fecha.trim().substring(6, 10));
            int hora = Integer.parseInt(fecha.trim().substring(11, 13));
            int minuto = Integer.parseInt(fecha.trim().substring(14, 16));
            int segundo = Integer.parseInt(fecha.trim().substring(17, 19));
            fechaCalendar.set(anio, mes - 1, dia, hora, minuto, segundo);

        } catch (Exception e) {

        }
        return fechaCalendar;
    }

    private static void aportacionSolicitante(String proc, String expediente, String numregistroGV, String fechaRegistroGV, String[][] documentosEvento) throws Lan6Excepcion {
        try {
//            String idProcedimiento = lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_" + proc); // 2.2
            String[] datos = expediente.split("/");
            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(datos[1]);

            // Expediente
            Lan6Expediente lan6Expediente = new Lan6Expediente();
            lan6Expediente.setNumero(expediente);
            String ejercicio = "2020";

            if (expediente.indexOf("/") != -1) {

                ejercicio = expediente.substring(0, expediente.indexOf("/"));
            }

            lan6Expediente.setEjercicio(ejercicio);

            List<Lan6Tramite> listaTramites = new ArrayList<Lan6Tramite>();
            Lan6Tramite lan6Tramite = new Lan6Tramite();

//		lan6Tramite.setId(Lan6Constantes.TRAMITE_APORTACION_DOC +"_"+fechaRegistroGV.replace("-", "").trim());
//		lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
//		lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
            lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC") + "_" + fechaRegistroGV.replace("-", "").trim());
            lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_DESC"));
            lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_DESC_EU"));
            lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);

            Calendar cal = obtenerCalendar3(fechaRegistroGV);

            lan6Tramite.setFechaActualizacion(cal);

            listaTramites.add(lan6Tramite);

            lan6Expediente.setListaTramites(listaTramites);

            List<Lan6Aportacion> aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
            List<Lan6AportacionDoc> documentos = new ArrayList<Lan6AportacionDoc>();

            documentos = new ArrayList<Lan6AportacionDoc>();
            // Documento aportado
            for (String[] documento : documentosEvento) {
                Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
                lan6AportacionDoc.setOid(documento[0]);
                lan6AportacionDoc.setNombre(documento[1]);
                lan6AportacionDoc.setDescripcionRol(documento[2]);
                lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
                documentos.add(lan6AportacionDoc);
            }

            // // Aportacion
            Lan6Aportacion lan6Aportacion = new Lan6Aportacion();

            lan6Aportacion.setIdAportacion(numregistroGV);
            lan6Aportacion.setNumRegistro(numregistroGV);
            lan6Aportacion.setFechaRegistro(cal);

            lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_ATTACHMENT);

            lan6Aportacion.setDocumentos(documentos);
            lan6Aportacion.setFechaAportacion(cal);
            aportacionesCiudadano.add(lan6Aportacion);

            lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);

            // Llamada m�todo
            servicios.actualizarAportacionSolicitante(lan6Expediente);
        } catch (Lan6Excepcion e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }

    }

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		ArrayList<String> codes = null;
		ArrayList<String> messages = null;
		
		try {
				
			/*************2020/ATASE/000789****************************/
			
			String[] documento1 ={"0901a0fe84275900","01.FORMULARIO SOLICITUD-Nekane Inchausti.pdf","SOLICITUD SUBS."};
			String[] documento2 ={"0901a0fe84275916","02.ALTA DE TERCERO-Nekane Inchausti.pdf","ALTA TERCEROS"};
			String[] documento3 ={"0901a0fe84275926","03.FORMULARIO OTORGAMIENTO REPRESENTACION-Nekane Inchausti.pdf","REPRESETN.SUBS"};
			String[] documento4 ={"0901a0fe84275957","NEKANE.pdf","DNI"};
			String[] documento5 ={"0901a0fe84275983","Aportación de documentos.html","Solicitud"};
			
			
			String[][] documentos = {documento1,documento2,documento3,documento4,documento5};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/000789", "2020RTE00207513", "23/04/2020 12:20:51", documentos );					
			
		} catch (Lan6Excepcion e) {
		
			e.printStackTrace();
			if(codes!=null) 
				codes.addAll(e.getCodes()	);
			else 
				 codes = e.getCodes();
			
			if(messages!=null) 
				messages.addAll(e.getMessages()	);
			else 
				messages = e.getMessages();
			
		}
			
		try{
	/*************2020/ATASE/000863****************************/
			
			String[] documento1_863 ={"0901a0fe84275ab3","CONTRATO DE ALQUILER.pdf","CONTRATO ARRENDAMIENTO"};
			String[] documento2_863 ={"0901a0fe84275afa","01.FORMULARIO SOLICITUD-Maribel Moreno.pdf","SOLICITUD SUBS."};
			String[] documento3_863 ={"0901a0fe84275aff","02.ALTA DE TERCERO-Maribel Moreno.pdf","ALTA TERCEROS."};
			String[] documento4_863 ={"0901a0fe84275b32","03.FORMULARIO OTORGAMIENTO REPRESENTACION-Maribel Moreno.pdf","REPRESENT.SUBS"};
			String[] documento5_863 ={"0901a0fe84275b1b","DNI.pdf","DNI"};
			String[] documento6_863 ={"0901a0fe84275b67","Aportación de documentos.html","Solicitud"};
			
			
			
			String[][] documentos863 = {documento1_863,documento2_863,documento3_863,documento4_863,documento5_863,documento6_863};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/000863", "2020RTE00207523", "23/04/2020 12:24:41", documentos863 );					
		}
		catch (Lan6Excepcion e) {
			
			e.printStackTrace();
			if(codes!=null) 
				codes.addAll(e.getCodes()	);
			else 
				 codes = e.getCodes();
			
			if(messages!=null) 
				messages.addAll(e.getMessages()	);
			else 
				messages = e.getMessages();
			
		}	
		
		
		try {
/*************2020/ATASE/001130****************************/
			
			String[] documento1_1130 ={"0901a0fe84277fcf","01.FORMULARIO SOLICITUD-Marta Eguez.pdf","SOLICITUD SUBS"};
			String[] documento2_1130 ={"0901a0fe84277fd0","02-ALTA DE TERCERO-Martha Eguez.pdf","ALTA TERCEROS SUBS"};
			String[] documento3_1130 ={"0901a0fe84278010","03.FORMULARIO OTORGAMIENTO REPRESENTACION-Marta Eguez.pdf","REPRESENT.SUBS."};
			String[] documento4_1130 ={"0901a0fe84278012","DNI.pdf","NIE"};
			String[] documento5_1130 ={"0901a0fe84278032","Aportación de documentos.html","Solicitud"};
				
			
			
			String[][] documentos1130 = {documento1_1130 ,documento2_1130 ,documento3_1130 ,documento4_1130 ,documento5_1130};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/001130", "2020RTE00207853", "23/04/2020 14:38:59", documentos1130);	
	} catch (Lan6Excepcion e) {
		
		e.printStackTrace();
		if(codes!=null) 
			codes.addAll(e.getCodes()	);
		else 
			 codes = e.getCodes();
		
		if(messages!=null) 
			messages.addAll(e.getMessages()	);
		else 
			messages = e.getMessages();
		
	}
		
		try {
/*************2020/ATASE/001143****************************/
			
			String[] documento1_1143 ={"0901a0fe842785df","01.FORMULARIO SOLICITUD-Roberto Estrada.pdf","SOLICITUD SUBS"};
			String[] documento2_1143 ={"0901a0fe842785e4","03.FORMULARIO OTORGAMIENTO REPRESENTACION-Roberto Estrada.pdf","REPRESENT"};
			String[] documento3_1143 ={"0901a0fe842785e2","02.ALTA DE TERCERO-Roberto Estrada.pdf","ALTA TERCEROS SUBS"};
			String[] documento4_1143 ={"0901a0fe842785c2","NIE.pdf","NIE"};
			String[] documento5_1143 ={"0901a0fe84278600","Aportación de documentos.html","Solicitud"};
				
			
			
			String[][] documentos1143= {documento1_1143,documento2_1143,documento3_1143,documento4_1143,documento5_1143};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/001143", "2020RTE00207902", "23/04/2020 15:03:32", documentos1143);	
} catch (Lan6Excepcion e) {
	
	e.printStackTrace();
	if(codes!=null) 
		codes.addAll(e.getCodes()	);
	else 
		 codes = e.getCodes();
	
	if(messages!=null) 
		messages.addAll(e.getMessages()	);
	else 
		messages = e.getMessages();
	
}
		
		try {
	
/*************2020/ATASE/001453****************************/
			
			String[] documento1_1453 ={"0901a0fe8427c0c6","AUTORIZACION GESTORIA.pdf","Autorización Gestoría Manuel Ozollo"};
			String[] documento2_1453 ={"0901a0fe8427c0b5","ALTA TERCEROS.pdf","Alta de tercero Manuel Ozollo"};
			String[] documento3_1453 ={"0901a0fe8427c117","CONTRATO.pdf","Contrato alquiler Mnnuel Ozollo"};
			String[] documento4_1453 ={"0901a0fe8427c107","RESOLUCION MUTUA MANUEL OZOLLO.pdf","Resolución Cese actividad"};
			String[] documento5_1453 ={"0901a0fe8427c109","Constitución Comunidad de Bienes.pdf","Constitución Comunidad de Bienes"};
			String[] documento6_1453 ={"0901a0fe8427c12e","Aportación de documentos.html","Solicitud"};
							
			
			String[][] documentos1453= {documento1_1453,documento2_1453,documento3_1453,documento4_1453,documento5_1453,documento6_1453};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/001453", "2020RTE00208481", "23/04/2020 20:10:32", documentos1453);	
		} catch (Lan6Excepcion e) {
			
			e.printStackTrace();
			if(codes!=null) 
				codes.addAll(e.getCodes()	);
			else 
				 codes = e.getCodes();
			
			if(messages!=null) 
				messages.addAll(e.getMessages()	);
			else 
				messages = e.getMessages();
			
		}
			
		try {
		
			/*************2020/ATASE/000575****************************/
			
			String[] documento1_575 ={"0901a0fe84284066","20200424130814.pdf","ESCRITO APORTANDO DATO"};
			String[] documento2_575 ={"0901a0fe842840ab","Aportación de documentos.html","Solicitud"};
				
						
			String[][] documentos575= {documento1_575,documento2_575};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/000575", "2020RTE00209416", "24/04/2020 13:08:44", documentos575);
		} catch (Lan6Excepcion e) {
			
			e.printStackTrace();
			if(codes!=null) 
				codes.addAll(e.getCodes()	);
			else 
				 codes = e.getCodes();
			
			if(messages!=null) 
				messages.addAll(e.getMessages()	);
			else 
				messages = e.getMessages();
			
		}
			
		
		try {
			
           /*************2020/ATASE/002889****************************/
			
			String[] documento1_2889 ={"0901a0fe84298b04","CESE ACTIVIDAD MONICA MEDINA.pdf","CESE ACTIVIDAD"};
			String[] documento2_2889 ={"0901a0fe84298afd","Aportación de documentos.html","Solicitud"};
				
						
			String[][] documentos2889= {documento1_2889,documento2_2889};
			aportacionSolicitante("LAN62_ATASE","2020/ATASE/002889", "2020RTE00211633", "27/04/2020 13:32:32", documentos2889);
		} catch (Lan6Excepcion e) {
			
			e.printStackTrace();
			if(codes!=null) 
				codes.addAll(e.getCodes()	);
			else 
				 codes = e.getCodes();
			
			if(messages!=null) 
				messages.addAll(e.getMessages()	);
			else 
				messages = e.getMessages();
			
		}
			
	

	}

}
