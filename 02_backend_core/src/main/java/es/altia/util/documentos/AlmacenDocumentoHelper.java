package es.altia.util.documentos;

import es.altia.agora.business.administracion.mantenimiento.persistence.manual.OrganizacionesDAO;
import es.altia.agora.business.administracion.mantenimiento.persistence.manual.UORDTO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoTramitacionFactoria;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoTramitacionFactoria;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.cache.CacheDatosFactoria;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.ResourceBundle;

public class AlmacenDocumentoHelper {
    private static final Logger log = LogManager.getLogger(AlmacenDocumentoHelper.class.getName());

    private int codOrganizacion;
    private AlmacenDocumento almacenDocumento;
    private Hashtable<String,Object> datos;

    public AlmacenDocumentoHelper(int codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getCodOrganizacion() {
        return codOrganizacion;
    }

    public AlmacenDocumento getAlmacenDocumento() {
        return almacenDocumento;
    }

    public Hashtable<String, Object> getDatos() {
        return datos;
    }

    /**
     * Recupera el contenido de un documento de registro mediante el plugin de almacén documental
     * @param documento
     * @param params
     * @return
     * @throws AlmacenDocumentoTramitacionException
     */
    public Documento getDocumentoRegistro (RegistroValueObject documento, String[] params) throws AlmacenDocumentoTramitacionException {
        int codUnidadRegistro = documento.getUnidadOrgan();
        almacenDocumento = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(codOrganizacion)).getImplClassRegistro(Integer.toString(codOrganizacion));

        datos = new Hashtable<String,Object>();
        datos.put("identDepart",Integer.valueOf(documento.getDptoUsuarioQRegistra()));
        datos.put("unidadOrgan",new Integer(codUnidadRegistro));
        datos.put("anoReg",new Integer(documento.getAnoReg()));
        datos.put("numReg",new Long(documento.getNumReg()));
        datos.put("tipoReg",documento.getTipoReg());
        datos.put("nombreDocumento",documento.getNombreDoc());
        datos.put("documentoRegistro",new Boolean(true));
        datos.put("params",params);

        int tipoDocumento = -1;
        if(!almacenDocumento.isPluginGestor()) {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            datos.put("codMunicipio",Integer.toString(codOrganizacion));
            datos.put("tipoMime",documento.getTipoDoc());
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(documento.getTipoDoc()));

            if(almacenDocumento.isPluginGestor()){
                //  Si se trata de un plugin de un gestor documental, se pasa la información extra necesaria
                ResourceBundle config = ResourceBundle.getBundle("documentos");
                String carpetaRaiz  = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacenDocumento.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                Connection con = null;
                String descripcionOrganizacion = null;
                String descripcionUnidadRegistro = null;
                AdaptadorSQLBD adapt = null;
                try{
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();

                    descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);
                    UORDTO uorDTO = (UORDTO) CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codUnidadRegistro));
                    if (uorDTO!=null)
                        descripcionUnidadRegistro = uorDTO.getUor_nom();
                }catch(BDException e){
                    if (log.isErrorEnabled()) {
                        log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());}
                }finally{
                    try{
                        adapt.devolverConexion(con);
                    }catch(BDException e){
                        if (log.isErrorEnabled()) {
                            log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());}
                    }
                }

                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codUnidadRegistro + ConstantesDatos.GUION + descripcionUnidadRegistro);

                if(documento.getTipoReg().equalsIgnoreCase("E")) {
                    listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
                } else if(documento.getTipoReg().equalsIgnoreCase("S")) {
                    listaCarpetas.add(ConstantesDatos.DESCRIPCION_SALIDAS_REGISTRO);
                }

                listaCarpetas.add(documento.getAnoReg() + ConstantesDatos.GUION + documento.getNumReg());
                datos.put("listaCarpetas",listaCarpetas);
            }

        }

        Documento doc = null;
        try{
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            doc = almacenDocumento.getDocumentoRegistro(doc);
        }catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;
        }

        return doc;
    }

    //Esta ocurrencia recupera un documento de registro cuyo almacén es base de datos
    /*public Documento getDocumentoRegistro (RegistroValueObject documento, String codProcedimiento, String[] params) throws AlmacenDocumentoTramitacionException {
        int codUnidadRegistro = documento.getUnidadOrgan();
        almacenDocumento = AlmacenDocumentoTramitacionFactoria.getInstance(Integer.toString(codOrganizacion)).getImplClassPluginProcedimiento(String.valueOf(codOrganizacion),codProcedimiento);

        datos = new Hashtable<String,Object>();
        datos.put("identDepart",Integer.valueOf(documento.getDptoUsuarioQRegistra()));
        datos.put("unidadOrgan",new Integer(codUnidadRegistro));
        datos.put("anoReg",new Integer(documento.getAnoReg()));
        datos.put("numReg",new Long(documento.getNumReg()));
        datos.put("tipoReg",documento.getTipoReg());
        datos.put("nombreDocumento",documento.getNombreDoc());
        datos.put("documentoRegistro",new Boolean(true));
        datos.put("params",params);

        int tipoDocumento = -1;
        if(!almacenDocumento.isPluginGestor()) {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_BBDD;
        } else {
            tipoDocumento = DocumentoTramitacionFactoria.TIPO_DOCUMENTO_GESTOR;
            datos.put("codMunicipio",Integer.toString(codOrganizacion));
            datos.put("tipoMime",documento.getTipoDoc());
            datos.put("extension", MimeTypes.guessExtensionFromMimeType(documento.getTipoDoc()));

            if(almacenDocumento.isPluginGestor()){
                //  Si se trata de un plugin de un gestor documental, se pasa la información extra necesaria
                ResourceBundle config = ResourceBundle.getBundle("documentos");
                String carpetaRaiz  = config.getString(ConstantesDatos.PREFIJO_PROPIEDAD_ALMACENAMIENTO + codOrganizacion + ConstantesDatos.BARRA + almacenDocumento.getNombreServicio() + ConstantesDatos.SUFIJO_PLUGIN_GESTOR_CARPETA_RAIZ);

                Connection con = null;
                String descripcionOrganizacion = null;
                String descripcionUnidadRegistro = null;
                AdaptadorSQLBD adapt = null;
                try{
                    adapt = new AdaptadorSQLBD(params);
                    con = adapt.getConnection();

                    descripcionOrganizacion   = OrganizacionesDAO.getInstance().getDescripcionOrganizacion(codOrganizacion, con);
                    UORDTO uorDTO = (UORDTO) CacheDatosFactoria.getImplUnidadesOrganicas().getDatoClaveUnica(params[6],String.valueOf(codUnidadRegistro));
                    if (uorDTO!=null)
                        descripcionUnidadRegistro = uorDTO.getUor_nom();
                }catch(BDException e){
                    if (log.isErrorEnabled()) {
                        log.error("Error al recuperar una conexión a la BBDD: " + e.getMessage());}
                }finally{
                    try{
                        adapt.devolverConexion(con);
                    }catch(BDException e){
                        if (log.isErrorEnabled()) {
                            log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());}
                    }
                }

                ArrayList<String> listaCarpetas = new ArrayList<String>();
                listaCarpetas.add(carpetaRaiz);
                listaCarpetas.add(codOrganizacion + ConstantesDatos.GUION + descripcionOrganizacion);
                listaCarpetas.add(codUnidadRegistro + ConstantesDatos.GUION + descripcionUnidadRegistro);

                if(documento.getTipoReg().equalsIgnoreCase("E")) {
                    listaCarpetas.add(ConstantesDatos.DESCRIPCION_ENTRADAS_REGISTRO);
                } else if(documento.getTipoReg().equalsIgnoreCase("S")) {
                    listaCarpetas.add(ConstantesDatos.DESCRIPCION_SALIDAS_REGISTRO);
                }

                listaCarpetas.add(documento.getAnoReg() + ConstantesDatos.GUION + documento.getNumReg());
                datos.put("listaCarpetas",listaCarpetas);
            }

        }

        Documento doc = null;
        try{
            doc = DocumentoTramitacionFactoria.getInstance().getDocumento(datos, tipoDocumento);
            doc = almacenDocumento.getDocumentoRegistro(doc);
        }catch(AlmacenDocumentoTramitacionException e){
            e.printStackTrace();
            throw e;
        }

        return doc;
    }
     */
}
