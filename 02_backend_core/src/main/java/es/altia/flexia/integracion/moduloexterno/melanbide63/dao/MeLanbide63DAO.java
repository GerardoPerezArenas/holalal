package es.altia.flexia.integracion.moduloexterno.melanbide63.dao;

import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.flexia.integracion.moduloexterno.melanbide63.exception.CopiarDocumentosTramitacionException;
import es.altia.flexia.integracion.moduloexterno.melanbide63.vo.DocumentoTramitacionVO;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide63DAO {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide63DAO.class);

    //Instancia
    private static MeLanbide63DAO instance = null;
    
    private MeLanbide63DAO()
    {
        
    }
    
    public static MeLanbide63DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide63DAO.class)
            {
                instance = new MeLanbide63DAO();
            }
        }
        return instance;
    }
    
    public ArrayList<DocumentoTramitacionVO> recuperarDocumentosTramitacion(int codOrganizacion, int codTramite, int ocurrencia, String[] codsPlantillas, int ejercicio, String codProcedimiento, String numExpediente, Connection con) throws CopiarDocumentosTramitacionException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        ArrayList<DocumentoTramitacionVO> lista = null;
               log.debug("recuperarDocumentosTramitacion();");
             log.debug("TRAMITE DESTINO "+codTramite);
          log.debug("recuperarDocumentosTramitacion() "+numExpediente);
          log.debug("ejercicio1 "+ejercicio);
        try {
            query = "SELECT CRD_NUD, CRD_DES, CRD_USC, CRD_USM, CRD_DOT, CRD_FIR_EST, CRD_EXP_FD, CRD_DOC_FD, CRD_FIR_FD, CRD_FIL "
                    + "FROM E_CRD WHERE CRD_MUN=? AND CRD_EJE=? AND CRD_PRO=? AND CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=?";
            if(codsPlantillas!=null) {
                query += " AND CRD_DOT IN(";
                String parteIn = "";
                for(int i=0; i<codsPlantillas.length; i++){
                    parteIn += "'' || ? || ''";
                    if(i<codsPlantillas.length-1) parteIn +=",";
                }
                query += parteIn + ")";
            }
            log.debug("sql: "+query);
            String textoParamBD = "Parametros de la query: "+codOrganizacion+"-"+ejercicio+"-"+codProcedimiento+"-"+numExpediente+"-"+codTramite+"-"+ocurrencia;
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrganizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProcedimiento);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocurrencia);
            if(codsPlantillas!=null){
                for(int i=0; i<codsPlantillas.length; i++){
                    ps.setInt(contbd++, Integer.parseInt(codsPlantillas[i]));
                    textoParamBD += "-" + codsPlantillas[i];
                }
            }
            
            rs = ps.executeQuery();
            log.debug(textoParamBD);
            lista = new ArrayList<DocumentoTramitacionVO>();
            while(rs.next()){
                DocumentoTramitacionVO doc = new DocumentoTramitacionVO();
                doc.setNombreDocumento(rs.getString("CRD_DES"));
                doc.setNumeroDocumento(rs.getInt("CRD_NUD"));
                doc.setCodUsuarioCreacion(rs.getInt("CRD_USC"));
                doc.setCodUsuarioModif(rs.getInt("CRD_USM"));
                doc.setCodPlantillaOrigen(rs.getInt("CRD_DOT"));
                doc.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                doc.setCrdExpFD(rs.getString("CRD_EXP_FD"));
                doc.setCrdDocFD(rs.getString("CRD_DOC_FD"));
                int intVal = rs.getInt("CRD_FIR_FD");
                if (rs.wasNull()) {
                    doc.setCrdFirFD(-1);
                } else
                    doc.setCrdFirFD(intVal);
                
                //OBTENEMOS EL CONTENIDO (byte[]) DEL DOCUMENTO
                //Se cambia el orden del if para que primero chequee si está en Dokusi (tiene oid)
                  //Esto se ha hecho porque con el portafirmas el campo CRD_FIL no es null a pesar de que tenga oid en Dokusi
                InputStream stream = rs.getBinaryStream("CRD_FIL");
                DocumentoTramitacionVO docAux = recuperarDocumentoTramitacionDokusi(rs.getInt("CRD_NUD"), codOrganizacion, codTramite, ocurrencia, ejercicio, codProcedimiento, numExpediente, con);
                if(docAux!=null){
                    doc.setCodGestorDokusi(docAux.getCodGestorDokusi());
                    doc.setPreparadoNotificacion(docAux.getPreparadoNotificacion());
                    
                    doc.setAlmacen(1);
                } else {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    int c;
                    while ((c = stream.read())!= -1){
                        output.write(c);
                    }
                    output.flush();
                    doc.setContenido(output.toByteArray());
                    output.close();
                    stream.close();
                    
                    doc.setAlmacen(0);
                }
                
                lista.add(doc);
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener documentos de un trámite.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(5,"Ha ocurrido un error al recuperar documentos de un trámite.");
        } catch (IOException ex) {
            log.error("Ha ocurrido un error al recuperar el contenido del documento.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(6,"Ha ocurrido un error de E/S al recuperar el contenido de un documento.");
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return lista;
    }
    
    
     public ArrayList<DocumentoTramitacionVO> recuperarDocumentosREINT(int codOrganizacion, int codTramite, int ocurrencia, String[] codsPlantillas, int ejercicio, String codProcedimiento, String numExpediente, Connection con) throws CopiarDocumentosTramitacionException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        ArrayList<DocumentoTramitacionVO> lista = null;
               log.debug("recuperarDocumentosREINT();");
             log.debug("TRAMITE DESTINO"+codTramite);
             log.debug("procedimiento dentro de recuperarDocumentosREINT"+codTramite);
          log.debug("rrecuperarDocumentosREINT()"+numExpediente);
          log.debug("ejercicio1"+ejercicio);
        try {
            query = "SELECT CRD_NUD, CRD_DES, CRD_USC, CRD_USM, CRD_DOT, CRD_FIR_EST, CRD_EXP_FD, CRD_DOC_FD, CRD_FIR_FD, CRD_FIL "
                    + "FROM E_CRD WHERE CRD_MUN=? AND CRD_EJE=? AND CRD_PRO=? AND CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=?";
            if(codsPlantillas!=null) {
                query += " AND CRD_DOT IN(";
                String parteIn = "";
                for(int i=0; i<codsPlantillas.length; i++){
                    parteIn += "'' || ? || ''";
                    if(i<codsPlantillas.length-1) parteIn +=",";
                }
                query += parteIn + ")";
            }
            log.debug("sql: "+query);
            String textoParamBD = "Parametros de la query: "+codOrganizacion+"-"+ejercicio+"-"+codProcedimiento+"-"+numExpediente+"-"+codTramite+"-"+ocurrencia;
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrganizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProcedimiento);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocurrencia);
            if(codsPlantillas!=null){
                for(int i=0; i<codsPlantillas.length; i++){
                    ps.setInt(contbd++, Integer.parseInt(codsPlantillas[i]));
                    textoParamBD += "-" + codsPlantillas[i];
                }
            }
            
            rs = ps.executeQuery();
            log.debug(textoParamBD);
            lista = new ArrayList<DocumentoTramitacionVO>();
            while(rs.next()){
                DocumentoTramitacionVO doc = new DocumentoTramitacionVO();
                doc.setNombreDocumento(rs.getString("CRD_DES"));
                doc.setNumeroDocumento(rs.getInt("CRD_NUD"));
                doc.setCodUsuarioCreacion(rs.getInt("CRD_USC"));
                doc.setCodUsuarioModif(rs.getInt("CRD_USM"));
                doc.setCodPlantillaOrigen(rs.getInt("CRD_DOT"));
                doc.setEstadoFirma(rs.getString("CRD_FIR_EST"));
                doc.setCrdExpFD(rs.getString("CRD_EXP_FD"));
                doc.setCrdDocFD(rs.getString("CRD_DOC_FD"));
                int intVal = rs.getInt("CRD_FIR_FD");
                if (rs.wasNull()) {
                    doc.setCrdFirFD(-1);
                } else
                    doc.setCrdFirFD(intVal);
                
                //OBTENEMOS EL CONTENIDO (byte[]) DEL DOCUMENTO
                //Se cambia el orden del if para que primero chequee si está en Dokusi (tiene oid)
                  //Esto se ha hecho porque con el portafirmas el campo CRD_FIL no es null a pesar de que tenga oid en Dokusi
                InputStream stream = rs.getBinaryStream("CRD_FIL");
                DocumentoTramitacionVO docAux = recuperarDocumentoTramitacionDokusi(rs.getInt("CRD_NUD"), codOrganizacion, codTramite, ocurrencia, ejercicio, codProcedimiento, numExpediente, con);
                if(docAux!=null){
                    doc.setCodGestorDokusi(docAux.getCodGestorDokusi());
                    doc.setPreparadoNotificacion(docAux.getPreparadoNotificacion());
                    
                    doc.setAlmacen(1);
                } else {
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    int c;
                    while ((c = stream.read())!= -1){
                        output.write(c);
                    }
                    output.flush();
                    doc.setContenido(output.toByteArray());
                    output.close();
                    stream.close();
                    
                    doc.setAlmacen(0);
                }
                
                lista.add(doc);
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener documentos de un trámite.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(5,"Ha ocurrido un error al recuperar documentos de un trámite.");
        } catch (IOException ex) {
            log.error("Ha ocurrido un error al recuperar el contenido del documento.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(6,"Ha ocurrido un error de E/S al recuperar el contenido de un documento.");
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return lista;
    }
    
    private DocumentoTramitacionVO recuperarDocumentoTramitacionDokusi(int numDoc, int codOrganizacion, int codTramite, int ocurrencia, int ejercicio, String codProcedimiento, String numExpediente, Connection con) throws CopiarDocumentosTramitacionException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String bindVar = "";
        int numDocs = 0;
        DocumentoTramitacionVO documento = new DocumentoTramitacionVO();
            
        try {
            numDocs = contarDocumentosTramitacionDokusi(numDoc, codOrganizacion, codTramite, ocurrencia, ejercicio, codProcedimiento, numExpediente, con);
            if(numDocs>0){
                String maxOid = null;
                if(numDocs>1){
                    maxOid = obtenerMaxIdDokusiTramite(numDoc, codOrganizacion, codTramite, ocurrencia, ejercicio, codProcedimiento, numExpediente, con);
                }
                
                query = "SELECT RELDOC_OID, RELDOC_PREPNOTIF FROM MELANBIDE_DOKUSI_RELDOC_TRAMIT "
                        + "WHERE RELDOC_MUN=? AND RELDOC_EJE=? AND RELDOC_PRO=? AND RELDOC_NUM=? AND RELDOC_TRA=? AND RELDOC_OCU=? AND RELDOC_NUD=?";
                bindVar += codOrganizacion+"-"+ejercicio+"-"+codProcedimiento+"-"+numExpediente+"-"+codTramite+"-"+ocurrencia+"-"+numDoc;
                if(maxOid!=null) {
                    query += " AND RELDOC_OID=?";
                    bindVar += "-"+maxOid;
                }
                log.debug("sql: "+query);
                log.debug("Parametros de la query: "+bindVar);

                ps = con.prepareStatement(query);
                int contbd = 1;
                ps.setInt(contbd++, codOrganizacion);
                ps.setInt(contbd++, ejercicio);
                ps.setString(contbd++, codProcedimiento);
                ps.setString(contbd++, numExpediente);
                ps.setInt(contbd++, codTramite);
                ps.setInt(contbd++, ocurrencia);
                ps.setInt(contbd++, numDoc);
                if(maxOid!=null) ps.setString(contbd++, maxOid);

                rs = ps.executeQuery();
                if(rs.next()){
                    documento.setNumeroDocumento(numDoc);
                    documento.setCodGestorDokusi(rs.getString("RELDOC_OID"));
                    documento.setPreparadoNotificacion(rs.getString("RELDOC_PREPNOTIF"));
                } else {
                    throw new CopiarDocumentosTramitacionException(11,"Se intenta recuperar un documento que no existe en BBDD ni en DOKUSI.");
                }
            } else throw new CopiarDocumentosTramitacionException(11,"Se intenta recuperar un documento que no existe en BBDD ni en DOKUSI.");
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener documentos de un trámite.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(5,"Ha ocurrido un error al recuperar documentos de un trámite.");
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return documento;
    }
    
    private int contarDocumentosTramitacionDokusi(int numDoc, int codOrganizacion, int codTramite, int ocurrencia, int ejercicio, String codProcedimiento, String numExpediente, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int numDocs = 0;
            
        try {
            String bindVar = "";
            query = "SELECT COUNT(*) AS NUM FROM MELANBIDE_DOKUSI_RELDOC_TRAMIT "
                    + "WHERE RELDOC_MUN=? AND RELDOC_EJE=? AND RELDOC_PRO=? AND RELDOC_NUM=? AND RELDOC_TRA=? AND RELDOC_OCU=?";
            bindVar += codOrganizacion+"-"+ejercicio+"-"+codProcedimiento+"-"+numExpediente+"-"+codTramite+"-"+ocurrencia;
            if(numDoc!=-1) {
                query += " AND RELDOC_NUD=?";
                bindVar += "-"+numDoc;
            }
            log.debug("sql: "+query);
            log.debug("Parametros de la query: "+bindVar);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrganizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProcedimiento);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocurrencia);
            if(numDoc!=-1) ps.setInt(contbd++, numDoc);
            
            rs = ps.executeQuery();
            while(rs.next()){
                numDocs = rs.getInt("NUM");
            } 
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener el nï¿½mero de documentos de un trámite en Dokusi.");
            ex.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return numDocs;
    }
    
    private String obtenerMaxIdDokusiTramite(int numDoc, int codOrganizacion, int codTramite, int ocurrencia, int ejercicio, String codProcedimiento, String numExpediente, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String maxId = null;
            
        try {
            String bindVar = "";
            query = "SELECT MAX(RELDOC_OID) AS MAXID FROM MELANBIDE_DOKUSI_RELDOC_TRAMIT "
                    + "WHERE RELDOC_MUN=? AND RELDOC_EJE=? AND RELDOC_PRO=? AND RELDOC_NUM=? AND RELDOC_TRA=? AND RELDOC_OCU=?";
            bindVar += codOrganizacion+"-"+ejercicio+"-"+codProcedimiento+"-"+numExpediente+"-"+codTramite+"-"+ocurrencia;
            if(numDoc!=-1) {
                query += " AND RELDOC_NUD=?";
                bindVar += "-"+numDoc;
            }
            log.debug("sql: "+query);
            log.debug("Parametros de la query: "+bindVar);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrganizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProcedimiento);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocurrencia);
            if(numDoc!=-1) ps.setInt(contbd++, numDoc);
            
            rs = ps.executeQuery();
            while(rs.next()){
                maxId = rs.getString("MAXID");
            } 
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener el mï¿½ximo RELDOC_OID.");
            ex.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return maxId;
    }
    
    public int insertarDocumentoTramitacion(DocumentoTramitacionVO documento, int codOrganizacion, int codTramite, int ocurrencia, HashMap<String, String> mapeoPlts, int ejercicio, String codProcDestino, String numExpediente, boolean copiaDokusi, String codProcOrigen, Connection con, AdaptadorSQLBD abd) throws CopiarDocumentosTramitacionException {
        PreparedStatement ps = null;
        String query;
        int insertado = 0;
        log.debug("insertarDocumentoTramitacion");
        log.error("Parametros que recibimos");
        log.error("ExpedienteInicio " + numExpediente);
        log.error("codProcedimiento " + codProcDestino);
        log.error("codTramite" + codTramite);
        log.error("insertarDocumentoTramitacion fin");

        try {
            //obtenemos el ultimo numero de documento del tramite y la ocurrencia indicados
            int numeroDoc = this.obtenerUltimoDocumentoTramite(codOrganizacion, ejercicio, codProcDestino, numExpediente, codTramite, ocurrencia, con);
            numeroDoc += 1;

            query = "INSERT INTO E_CRD(CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO,CRD_FINF,"
                    + "CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FIR_EST,CRD_EXP_FD,CRD_DOC_FD,CRD_FIR_FD) VALUES "
                    + "(?,?,?,?,?,?,?," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ","
                    + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + "," + abd.funcionFecha(AdaptadorSQL.FUNCIONFECHA_SYSDATE, null) + ",?,?,?,?,?,?,?,?,?)";
            log.debug("sql: " + query);
            log.debug("parametros de la insert: " + codOrganizacion + " " + codProcDestino + " " + ejercicio + " " + numExpediente + " " + codTramite + " " + ocurrencia + " " + numeroDoc + " " + documento.getCodUsuarioCreacion() + " " + documento.getCodUsuarioModif() + " " + documento.getNombreDocumento());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrganizacion);
            ps.setString(contbd++, codProcDestino);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codTramite);
            ps.setInt(contbd++, ocurrencia);
            ps.setInt(contbd++, numeroDoc);
            ps.setInt(contbd++, documento.getCodUsuarioCreacion());
            ps.setInt(contbd++, documento.getCodUsuarioModif());
            ps.setNull(contbd++, java.sql.Types.VARCHAR);
            ps.setString(contbd++, documento.getNombreDocumento());
            if (mapeoPlts != null) {
                String codPltDest = mapeoPlts.get(String.valueOf(documento.getCodPlantillaOrigen()));
                documento.setCodPlantillaDestino(Integer.parseInt(codPltDest));
            } else {
                documento.setCodPlantillaDestino(documento.getCodPlantillaOrigen());
            }
            ps.setInt(contbd++, documento.getCodPlantillaDestino());
            if (documento.getEstadoFirma() != null) {
                ps.setString(contbd++, documento.getEstadoFirma());
            } else {
                ps.setNull(contbd++, java.sql.Types.VARCHAR);
            }
            if (documento.getCrdExpFD() != null) {
                ps.setString(contbd++, documento.getCrdExpFD());
            } else {
                ps.setNull(contbd++, java.sql.Types.VARCHAR);
            }
            if (documento.getCrdDocFD() != null) {
                ps.setString(contbd++, documento.getCrdDocFD());
            } else {
                ps.setNull(contbd++, java.sql.Types.VARCHAR);
            }
            if (documento.getCrdFirFD() != -1) {
                ps.setInt(contbd++, documento.getCrdFirFD());
            } else {
                ps.setNull(contbd++, java.sql.Types.INTEGER);
            }

            insertado = ps.executeUpdate();

            if (insertado > 0) {
                insertado = almacenarContenidoDocumento(documento, numeroDoc, codOrganizacion, codTramite, ocurrencia, ejercicio, codProcDestino, numExpediente, copiaDokusi, codProcOrigen, con);
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al insertar documentos en un trámite.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(7, "Ha ocurrido un error al insertar documentos en un trámite.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }

        return insertado;
    }

    public int obtenerUltimaOcurrenciaTramite(int organizacion, int ejercicio, String codProc, String numExp, int codTram,Connection con) throws CopiarDocumentosTramitacionException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int ocurrencia = -1;
          log.debug("obtenerUltimaOcurrenciaTramite : "+codProc);  
        try {
            query = "SELECT MAX(CRO_OCU) AS OCU FROM E_CRO WHERE CRO_MUN=? AND CRO_EJE=? AND CRO_PRO=? AND CRO_NUM=? AND CRO_TRA=?";
            log.debug("sql: "+query);
            log.debug("Parametros de la query: "+organizacion+"-"+ejercicio+"-"+codProc+"-"+numExp+"-"+codTram);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, organizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            
            rs = ps.executeQuery();
            if(rs.next()){
                ocurrencia = rs.getInt("OCU");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener el numero de última ocurrencia del trámite "+codTram+" del expediente "+numExp);
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(4,"Ha ocurrido un error al recuperar la última ocurrencia de un trámite.");
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return ocurrencia;
    }
    
    
    public int obtenerUltimaOcurrenciaTramiteReint(int organizacion, int ejercicio, String codProc, String numExp, int codTram,Connection con) throws CopiarDocumentosTramitacionException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int ocurrencia = -1;
          log.debug("obtenerUltimaOcurrenciaTramiteReint : "+codProc);  
        try {
            query = "SELECT MAX(CRO_OCU) AS OCU FROM E_CRO WHERE CRO_MUN=? AND CRO_EJE=? AND CRO_PRO=? AND CRO_NUM=? AND CRO_TRA=?";
            log.debug("sql: "+query);
            log.debug("Parametros de la query: "+organizacion+"-"+ejercicio+"-"+codProc+"-"+numExp+"-"+codTram);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, organizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            
            rs = ps.executeQuery();
            if(rs.next()){
                ocurrencia = rs.getInt("OCU");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener el numero de última ocurrencia del trámite "+codTram+" del expediente "+numExp);
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(4,"Ha ocurrido un error al recuperar la última ocurrencia de un trámite.");
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return ocurrencia;
    }
    
    
    
    private int almacenarContenidoDocumento(DocumentoTramitacionVO doc, int numDoc, int codOrganizacion, int codTramite, int ocurrencia, int ejercicio, String codProcDestino, String numExpediente, boolean copia, String codProcOrigen, Connection con) throws CopiarDocumentosTramitacionException {
        PreparedStatement ps = null;
        String query;
        int resultado = 0;

        try {
            if (doc.getAlmacen() == 0 || doc.getAlmacen() == 1) {
                int contbd = 1;
                if (doc.getAlmacen() == 0) {
                    query = "UPDATE E_CRD SET CRD_FIL=? "
                            + "WHERE CRD_MUN=? AND CRD_EJE=? AND CRD_PRO=? AND CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=? AND CRD_NUD=?";
                    log.debug("sql: " + query);

                    ps = con.prepareStatement(query);
                    InputStream stream = new ByteArrayInputStream(doc.getContenido());
                    ps.setBinaryStream(contbd++, stream, doc.getContenido().length);

                } else {
                    query = "INSERT INTO MELANBIDE_DOKUSI_RELDOC_TRAMIT(RELDOC_OID,RELDOC_PREPNOTIF,RELDOC_MUN,RELDOC_EJE,"
                            + "RELDOC_PRO,RELDOC_NUM,RELDOC_TRA,RELDOC_OCU,RELDOC_NUD) VALUES (?,?,?,?,?,?,?,?,?)";
                    log.debug("sql: " + query);

                    if (copia) {
                        String nuevoOid = obtenerIdCopiaDocumentoDokusi(doc.getCodGestorDokusi(), codProcOrigen);
                        doc.setCodGestorDokusi(nuevoOid);
                    }

                    ps = con.prepareStatement(query);
                    ps.setString(contbd++, doc.getCodGestorDokusi());
                    ps.setString(contbd++, doc.getPreparadoNotificacion());
                }

                ps.setInt(contbd++, codOrganizacion);
                ps.setInt(contbd++, ejercicio);
                ps.setString(contbd++, codProcDestino);
                ps.setString(contbd++, numExpediente);
                ps.setInt(contbd++, codTramite);
                ps.setInt(contbd++, ocurrencia);
                ps.setInt(contbd++, numDoc);

                resultado = ps.executeUpdate();
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al almacenar el documento.");
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(12, "Ha ocurrido un error al almacenar el documento.");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }

        return resultado;
    }
  
    private int obtenerUltimoDocumentoTramite(int organizacion, int ejercicio, String codProc, String numExp, int codTram, int  ocurrencia, Connection con) throws CopiarDocumentosTramitacionException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        int maximo = -1;
            
        try {
            query = "SELECT MAX(CRD_NUD) AS NUD FROM E_CRD WHERE CRD_MUN=? AND CRD_EJE=? AND CRD_PRO=? AND CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=?";
            log.debug("sql: "+query);
            log.debug("Parametros de la query: "+organizacion+"-"+ejercicio+"-"+codProc+"-"+numExp+"-"+codTram+"-"+ocurrencia);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, organizacion);
            ps.setInt(contbd++, ejercicio);
            ps.setString(contbd++, codProc);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, codTram);
            ps.setInt(contbd++, ocurrencia);
            
            rs = ps.executeQuery();
            if(rs.next()){
                maximo = rs.getInt("NUD");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener el numero de última documento del trámite "+codTram+" y ocurrencia "+ocurrencia);
            ex.printStackTrace();
            throw new CopiarDocumentosTramitacionException(8,"Ha ocurrido un error al recuperar lel numero de ultimo documento de un trámite.");
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return maximo;
    }
    
    private String obtenerIdCopiaDocumentoDokusi(String idDocOrigen, String codProcOrigen) throws CopiarDocumentosTramitacionException {
        String idCopiaDoc = null;
        Lan6DokusiServicios servicios = null;
        Lan6Documento lan6Documento = null;
        try {
            servicios = new Lan6DokusiServicios(codProcOrigen); // 2.2
            lan6Documento = new Lan6Documento();
            lan6Documento.setIdDocumento(idDocOrigen);
            lan6Documento.setConvertirAPdf(true);
            idCopiaDoc = servicios.copiarDocumento(lan6Documento);
        } catch (Lan6Excepcion e) {
            log.error("Ha ocurrido un error al obtener un id de copia del documento en Dokusi");
            throw new CopiarDocumentosTramitacionException(13, "Ha ocurrido un error al obtener un id de copia del documento en Dokusi");
        }
        return idCopiaDoc;
    }

    public String insertarDocumentoTramitacion(String numExpediente,Connection con) throws CopiarDocumentosTramitacionException, Exception {
       PreparedStatement st = null;
        ResultSet rs = null;
        String expediente="";
        String query = null;
        int historico = 0;
       
        try
        {
            query = "select REX_NUMR from E_REX WHERE REX_NUM ='"+numExpediente+"' AND ROWNUM =1"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next())
            {
               expediente=rs.getString("REX_NUMR");
           }
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getDatosIdExpediente el expediente "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return expediente; 
    }

    public String insertarDocumentoTramitacionProcedimiento(String numExpediente, Connection con) throws SQLException, Exception {
      PreparedStatement st = null;
        ResultSet rs = null;
        String procedimiento="";
        String query = null;
        int historico = 0;
       
        try
        {
            query = "select exp_eje from e_exp where exp_num='"+numExpediente+"'"; 
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next())
            {
               procedimiento=rs.getString("exp_eje");
           }
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getDatosIdExpediente el expediente "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return procedimiento; 
    }


    public ArrayList<String> obtenercodigoPlantillaOrigneReint(int codOrganizacion, int codTramite, int ocurrenciaTramite, int ejercicio, String codProcedimiento, String numExpediente, Connection con) throws Exception {
       PreparedStatement st = null;
        ResultSet rs = null;
        String codigoPlantilla = null;
        String query = null;
        int historico = 0;
        ArrayList<String> pltsOrig = new ArrayList();
        try
        {
            query = "SELECT DOT_PLT FROM E_CRD INNER JOIN E_DOT ON(CRD_DOT=DOT_COD) where E_CRD.CRD_PRO='"+codProcedimiento+"' AND CRD_NUM='"+numExpediente+"'"; 
            if(log.isDebugEnabled()) 
                log.debug("sql obtenercodigoPlantillaOrigneReint() = " + query);
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next())
            {
              pltsOrig.add(rs.getString("DOT_PLT"));
               
           }
       
            
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getDatosIdExpediente el expediente "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            if(log.isDebugEnabled()) 
                log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return pltsOrig;  
    }

    public Boolean existeExpediente(String numExp, Connection con) throws Exception {
        log.error("existeExpediente - Begin() DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            String query = "SELECT COUNT(1) AS EXISTE FROM E_EXP WHERE EXP_NUM=?";
            if (log.isDebugEnabled()) {
                log.error("sql existeExpediente() = " + query);
                log.error("sql existeExpediente() - Param = " + numExp);
            }
            st = con.prepareStatement(query);
            st.setString(1,numExp);
            rs = st.executeQuery();
            while (rs.next()) {
                Integer res = rs.getInt("EXISTE");
                if(res==0)
                    result=false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si existe el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    public boolean existeProcedimiento(String codProc, Connection con) throws Exception {
        log.error("existeProcedimiento - Begin() DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            
            String query = "SELECT COUNT(1) AS EXISTE FROM E_PRO WHERE PRO_COD=?";
            if (log.isDebugEnabled()) {
                log.error("sql existeProcedimiento() = " + query);
                log.error("sql existeProcedimiento() - Param = " + codProc);
            }
            st = con.prepareStatement(query);
            st.setString(1, codProc);
            rs = st.executeQuery();
            while (rs.next()) {
                Integer res = rs.getInt("EXISTE");
                if (res == 0) {
                    result = false;
                }
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si existe el procedimiento " + codProc, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

    public boolean existeCodTramiteInternoProcedimiento(String codigoProcIndicadoInterfaz, String codigoInteTramIndicaInterf, Connection con) throws Exception {
        log.error("existeCodTramiteInternoProcedimiento - Begin()  DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            String query = "SELECT COUNT(1) AS EXISTE FROM E_TRA WHERE TRA_PRO=? AND TRA_COD=?";
            if (log.isDebugEnabled()) {
                log.error("sql existeCodTramiteInternoProcedimiento() = " + query);
                log.error("sql existeCodTramiteInternoProcedimiento() - ParamS = " + codigoProcIndicadoInterfaz+","+codigoInteTramIndicaInterf);
            }
            st = con.prepareStatement(query);
            st.setString(1, codigoProcIndicadoInterfaz);
            Integer codigoInteTramIndicaInterfInt = codigoInteTramIndicaInterf!=null && !codigoInteTramIndicaInterf.equalsIgnoreCase("")?Integer.parseInt(codigoInteTramIndicaInterf):0;
            st.setInt(2,codigoInteTramIndicaInterfInt);
            rs = st.executeQuery();
            while (rs.next()) {
                Integer res = rs.getInt("EXISTE");
                if (res == 0) {
                    result = false;
                }
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si existe codigo interno de tramite en  el procedimiento " + codigoProcIndicadoInterfaz+"-"+codigoInteTramIndicaInterf, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
    public boolean existeTramiteEnExpediente(String numExp, String codigoInternoTramite, Connection con) throws Exception {
        log.error("existeTramiteEnExpediente - Begin()  DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            String query = "SELECT COUNT(1) AS EXISTE FROM E_CRO WHERE CRO_NUM=? AND CRO_TRA=?";
            if (log.isDebugEnabled()) {
                log.error("sql existeTramiteEnExpediente() = " + query);
                log.error("sql existeTramiteEnExpediente() - ParamS = " + numExp+","+codigoInternoTramite);
            }
            st = con.prepareStatement(query);
            st.setString(1, numExp);
            Integer codigoInteTramIndicaInterfInt = codigoInternoTramite!=null && !codigoInternoTramite.equalsIgnoreCase("")?Integer.parseInt(codigoInternoTramite):0;
            st.setInt(2,codigoInteTramIndicaInterfInt);
            rs = st.executeQuery();
            while (rs.next()) {
                Integer res = rs.getInt("EXISTE");
                if (res == 0) {
                    result = false;
                }
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si existe tramite para el expediente " + numExp+"-"+codigoInternoTramite, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    public Integer obtenerMaxOcurrenciaTramiteEnExpediente(String numExp, String codigoInternoTramite, Connection con) throws Exception {
        log.error("obtenerMaxOcurrenciaTramiteEnExpediente - Begin()  DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Integer result = 0;
        try {
            String query = "SELECT MAX(CRO_OCU) MAX_OCURRENCIA FROM E_CRO WHERE CRO_NUM=? AND CRO_TRA=?";
            if (log.isDebugEnabled()) {
                log.error("sql obtenerMaxOcurrenciaTramiteEnExpediente() = " + query);
                log.error("sql obtenerMaxOcurrenciaTramiteEnExpediente() - ParamS = " + numExp+","+codigoInternoTramite);
            }
            st = con.prepareStatement(query);
            st.setString(1, numExp);
            Integer codigoInteTramIndicaInterfInt = codigoInternoTramite!=null && !codigoInternoTramite.equalsIgnoreCase("")?Integer.parseInt(codigoInternoTramite):0;
            st.setInt(2,codigoInteTramIndicaInterfInt);
            rs = st.executeQuery();
            while (rs.next()) {
                result = rs.getInt("MAX_OCURRENCIA");
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error obteniendo la maxima ocurrencia del tramite para el expediente " + numExp+"-"+codigoInternoTramite, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    public Integer obtenerIDNumDocumentoTramiteExpediente(String numExp, Integer codigoInternoTramite,Integer ocurrenciaTramite, Connection con) throws Exception {
        log.error("obtenerIDNumDocumentoTramiteExpediente - Begin()  DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Integer result = 0;
        try {
            String query = "SELECT MAX(CRD_NUD) MAX_OCURRENCIA FROM E_CRD WHERE CRD_NUM=? AND CRD_TRA=? AND CRD_OCU=?";
            if (log.isDebugEnabled()) {
                log.error("sql obtenerIDNumDocumentoTramiteExpediente() = " + query);
                log.error("sql obtenerIDNumDocumentoTramiteExpediente() - ParamS = " + numExp+","+codigoInternoTramite+","+ocurrenciaTramite);
            }
            st = con.prepareStatement(query);
            st.setString(1, numExp);
            st.setInt(2,codigoInternoTramite);
            st.setInt(3,ocurrenciaTramite);
            rs = st.executeQuery();
            while (rs.next()) {
                result = rs.getInt("MAX_OCURRENCIA") + 1;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error obteniendo el id para el nuevo documento del tramite para el expediente " + numExp+"-"+codigoInternoTramite, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.error("resultado id documento : " + result);
        return result;
    }

    public boolean existeCodPlantillaDocumentoTramiteProcedimiento(String codigoProcIndicadoInterfaz, String codigoInteTramIndicaInterf, String codigoPlantilla, Connection con) throws Exception {
        log.error("existeCodPlantillaDocumentoTramiteProcedimiento - Begin()  DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            String query = "SELECT COUNT(1) AS EXISTE  FROM E_DOT WHERE DOT_PRO=? AND DOT_TRA=? AND DOT_COD=?";
            if (log.isDebugEnabled()) {
                log.error("sql existeCodPlantillaDocumentoTramiteProcedimiento() = " + query);
                log.error("sql existeCodPlantillaDocumentoTramiteProcedimiento() - ParamS = " + codigoProcIndicadoInterfaz + "," + codigoInteTramIndicaInterf+","+codigoPlantilla);
            }
            st = con.prepareStatement(query);
            st.setString(1, codigoProcIndicadoInterfaz);
            Integer codigoInteTramIndicaInterfInt = codigoInteTramIndicaInterf != null && !codigoInteTramIndicaInterf.equalsIgnoreCase("") ? Integer.parseInt(codigoInteTramIndicaInterf) : 0;
            st.setInt(2, codigoInteTramIndicaInterfInt);
            Integer codigoPlantillaInt = codigoPlantilla != null && !codigoPlantilla.equalsIgnoreCase("") ? Integer.parseInt(codigoPlantilla) : 0;
            st.setInt(3, codigoPlantillaInt);
            rs = st.executeQuery();
            while (rs.next()) {
                Integer res = rs.getInt("EXISTE");
                if (res == 0) {
                    result = false;
                }
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si existe la plantillla en el tramite para el procedimiento " + codigoProcIndicadoInterfaz + "-" + codigoInteTramIndicaInterf + "-"+ codigoPlantilla, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    public String cargarDocumentoResolucionTramiteBBDD(DocumentoGestor documentoGestor, Connection con) throws Exception {
        log.error("cargarDocumentoResolucionTramiteBBDD - Begin()");
        String salida = null;
        Integer salidaInt = null;
        PreparedStatement pstmt = null;
        try {

            String query = "INSERT INTO E_CRD (CRD_MUN,CRD_PRO,CRD_EJE,CRD_NUM,CRD_TRA,CRD_OCU,CRD_NUD,CRD_FAL,CRD_FMO,CRD_USC,CRD_USM,CRD_FIL,CRD_DES,CRD_DOT,CRD_FINF) "
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            pstmt = con.prepareStatement(query);
            //Ponemos los parametros
            log.error("Params: " + documentoGestor.getCodMunicipio()
                    + "," + documentoGestor.getCodProcedimiento()
                    + "," + documentoGestor.getEjercicio()
                    + "," + documentoGestor.getNumeroExpediente()
                    + "," + documentoGestor.getCodTramite()
                    + "," + documentoGestor.getOcurrenciaTramite()
                    + "," + documentoGestor.getNumeroDocumento()
                    + "," + documentoGestor.getFechaDocumento()
                    + "," + documentoGestor.getFechaDocumento()
                    + "," + documentoGestor.getCodUsuario()
                    + "," + documentoGestor.getCodUsuario()
                    + "," + (documentoGestor.getFichero() != null && documentoGestor.getFichero().length > 0 ? "Existe contenido y length es > 0. No pintamos, para no cargar trazas." : "No Existe contenido o length es = 0.")
                    + "," + documentoGestor.getNombreDocumento()
                    + "," + documentoGestor.getCodDocumento()
                    + "," + documentoGestor.getFechaDocumento()
            );

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date fdocmento = formatter.parse(documentoGestor.getFechaDocumento());
            log.error("Se ha hecho la conversion de Hora String/Date, para luego pasar a java.sql.Date"
                    + " Parametro a poner en SQL : " + (new java.sql.Date(fdocmento.getTime())));
            pstmt.setInt(1, documentoGestor.getCodMunicipio());
            pstmt.setString(2, documentoGestor.getCodProcedimiento());
            pstmt.setInt(3, documentoGestor.getEjercicio());
            pstmt.setString(4, documentoGestor.getNumeroExpediente());
            pstmt.setInt(5, documentoGestor.getCodTramite());
            pstmt.setInt(6, documentoGestor.getOcurrenciaTramite());
            pstmt.setInt(7, Integer.valueOf(documentoGestor.getNumeroDocumento()));
            pstmt.setDate(8, new java.sql.Date(fdocmento.getTime()));
            pstmt.setDate(9, new java.sql.Date(fdocmento.getTime()));
            pstmt.setInt(10, documentoGestor.getCodUsuario());
            pstmt.setInt(11, documentoGestor.getCodUsuario());
            pstmt.setBytes(12, documentoGestor.getFichero());
            pstmt.setString(13, documentoGestor.getNombreDocumento());
            pstmt.setInt(14, documentoGestor.getCodDocumento());
            pstmt.setDate(15, new java.sql.Date(fdocmento.getTime()));
            log.error("Parametros Asigamos al PreparedStatement -");

            salidaInt = pstmt.executeUpdate();
            if (salidaInt != null) {
                salida = String.valueOf(salidaInt);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error cargando en BBDD documento de Resolucion Carga Masiva", ex);
            throw new Exception(ex);
        } finally {
            log.error("cargarDocumentoResolucionTramiteBBDD - End()");
        }
        return salida;
    }
    
    public boolean esDokusiActivoProcedimiento(String codProcedimiento, Connection con) throws Exception {
        log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Begin");
        Statement st = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            String query = "SELECT COUNT(*) AS ACTIVO " 
                    + " FROM PLUGIN_DOC_PROCEDIMIENTO "
                    + " PLUGIN_PRO "
                    + " INNER JOIN ALMACEN_DOC_DISPONIBLES " 
                    + " ALMACEN ON ALMACEN.ID = PLUGIN_PRO.ID_ALMACEN "
                    + " WHERE UPPER(ALMACEN.NOMBRE)='DOKUSI' "
                    + " AND PLUGIN_PRO.COD_PROCEDIMIENTO='" + codProcedimiento + "'";
            if (log.isDebugEnabled()) {
                log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                int noDocumento = rs.getInt("ACTIVO");
                log.error("esDokusiActivoProcedimiento - Resultado SQL : " + noDocumento);
                if (noDocumento > 0) {
                    log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Procedimiento " + codProcedimiento + " Activo para plugin de DOKUSI.");
                    result = true;
                }
            }
        } catch (Exception ex) {
            log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Se ha producido un error al verificando si procedimiento tiene activo el plugin de dokusi " + codProcedimiento, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - End");
        return result;
    }

    public List<DocumentoGestor> obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc(Integer codOrganizacion,Integer ejercicio,String codProcedimiento,String numExp, Integer tramite, Integer ocurrencia, Integer documentoPlantilla, String Nombre, Connection con) throws Exception {
        log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - Begin");
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<DocumentoGestor> result = new ArrayList<DocumentoGestor>();
        DocumentoGestor documentoGestor = new DocumentoGestor();
        try {
            String query = "SELECT CRD_MUN, CRD_EJE, CRD_PRO, CRD_NUM, CRD_TRA, CRD_OCU, CRD_NUD, CRD_DOT " +
                            "FROM E_CRD " +
                            "WHERE CRD_MUN=? AND CRD_EJE=? AND CRD_PRO=? " +
                            "AND CRD_NUM=? " +
                            "AND CRD_TRA=? " +
                            "AND CRD_OCU=? " +
                            "AND CRD_DOT=? " +
                            "AND UPPER(CRD_DES) LIKE '%'||'"+(Nombre!=null?Nombre.toUpperCase():"")+"'||'%' ";
            if (log.isDebugEnabled()) {
                log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc sql = " + query);
            }
            pst = con.prepareStatement(query);
            
            //Ponemos los parametros
            log.error("Params: " + codOrganizacion
                    + "," + ejercicio
                    + "," + codProcedimiento
                    + "," + numExp
                    + "," + tramite
                    + "," + ocurrencia
                    + "," + documentoPlantilla
            );
            
            pst.setInt(1, codOrganizacion);
            pst.setInt(2, ejercicio);
            pst.setString(3, codProcedimiento);
            pst.setString(4, numExp);
            pst.setInt(5, tramite);
            pst.setInt(6, ocurrencia);
            pst.setInt(7, documentoPlantilla);
            
            rs = pst.executeQuery();
            while (rs.next()) {
                documentoGestor.setCodMunicipio(rs.getInt("CRD_MUN"));
                documentoGestor.setEjercicio(rs.getInt("CRD_EJE"));
                documentoGestor.setCodProcedimiento(rs.getString("CRD_PRO"));
                documentoGestor.setNumeroExpediente(rs.getString("CRD_NUM"));
                documentoGestor.setCodTramite(rs.getInt("CRD_TRA"));
                documentoGestor.setOcurrenciaTramite(rs.getInt("CRD_OCU"));
                documentoGestor.setNumeroDocumento(String.valueOf(rs.getInt("CRD_NUD")));
                documentoGestor.setCodDocumento(rs.getInt("CRD_DOT"));
                result.add(documentoGestor);
                documentoGestor = new DocumentoGestor();
            }
        } catch (Exception ex) {
            log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - Se ha producido un error al verificando si procedimiento tiene activo el plugin de dokusi " + codProcedimiento, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - Procedemos a cerrar el statement y el resultset");
                }
                if (pst != null) {
                    pst.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - End");
        return result;
    }
    
    public ArrayList<String> recuperarExpedientesOcurrencia(int numTarea, Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        ArrayList<String> lista = null;
        log.debug("recuperarOIDDocumentos();");
        try {
            query = "SELECT CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CEPAP_MIGRACION_EXCEL.NUM_EXP,'_'),\n" +
"                    CEPAP_MIGRACION_EXCEL.NIVEL_CUALIFICACION),'_'),CEPAP_MIGRACION_EXCEL.DOCUMENTO),'_'),CEPAP_MIGRACION_EXCEL.VALORACION) AS NUM_EXP_OCU_NUD_PDF\n" +
"                    FROM CEPAP_MIGRACION_EXCEL\n" +
"                    WHERE NUM_TAREA=?";
            
            log.debug("sql: "+query);
            
            ps = con.prepareStatement(query);
            ps.setInt(1, numTarea);
            
            rs = ps.executeQuery();
            
            lista = new ArrayList<String>();
            while(rs.next()){
                String exp = rs.getString("NUM_EXP_OCU_NUD_PDF");
                lista.add(exp);
            }
        } catch (Exception ex) {
            log.error("Ha ocurrido un error al obtener los nº expedientes y ocurrencia.");
            ex.printStackTrace();
        } finally {
            try {
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            } catch (SQLException ex){
                log.error("Ha ocurrido un error al liberar recursos de BBDD");
            }
        }
        
        return lista;
    }
    
    public boolean procesoLanzado(Connection con) throws Exception {
        log.info("procesoLanzado - Begin() DAO");
        PreparedStatement st = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            String query = "SELECT ESTADO FROM CTRL_PROCESOS_MASIVOS WHERE PROCESO='CARGA_RESOLUCIONES'";
            if (log.isDebugEnabled()) {
                log.info("sql procesoLanzado() = " + query);
            }
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                Integer res = rs.getInt("ESTADO");
                if (res == 0) {
                    result = false;
                }
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error comprobando si el proceso está lanzado ", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }
    
    public int lanzarProceso(Connection con) throws Exception {
        log.info("lanzarProceso - Begin() DAO");
        PreparedStatement st = null;
        int resultado = 0;
        try {
            String query = "UPDATE CTRL_PROCESOS_MASIVOS SET ESTADO=1, FECHA_ULTIMO=SYSDATE WHERE PROCESO='CARGA_RESOLUCIONES'";
            if (log.isDebugEnabled()) {
                log.info("sql lanzarProceso() = " + query);
            }
            st = con.prepareStatement(query);
            
            resultado= st.executeUpdate();

        } catch (Exception ex) {
            log.error("Se ha producido un error al intentar registrar que el proceso está lanzado ", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement");
            }
            if (st != null) {
                st.close();
            }
        }
        return resultado;
    }
    
    public int liberarProceso(Connection con) throws Exception {
        log.info("liberarProceso - Begin() DAO");
        PreparedStatement st = null;
        int resultado = 0;
        try {
            String query = "UPDATE CTRL_PROCESOS_MASIVOS SET ESTADO=0 WHERE PROCESO='CARGA_RESOLUCIONES'";
            if (log.isDebugEnabled()) {
                log.info("sql liberarProceso() = " + query);
            }
            st = con.prepareStatement(query);
            
            resultado= st.executeUpdate();

        } catch (Exception ex) {
            log.error("Se ha producido un error al intentar registrar que el proceso ha terminado ", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement");
            }
            if (st != null) {
                st.close();
            }
        }
        return resultado;
    }
    
    
}
