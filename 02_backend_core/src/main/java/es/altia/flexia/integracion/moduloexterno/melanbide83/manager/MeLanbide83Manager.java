package es.altia.flexia.integracion.moduloexterno.melanbide83.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.dao.MeLanbide83DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.util.ConstantesMeLanbide83;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InfoDesplegableVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.ElementoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide83Manager {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide83Manager.class);
    
    //Instancia
    private static MeLanbide83Manager instance = null;

    private MeLanbide83Manager() {
    }
    
    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide83Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide83Manager.class)
            {
                instance = new MeLanbide83Manager();
            }
        }
        return instance;
    }
    
    public InfoDesplegableVO obtenerInfoDesplegablePorCodigo(String codigo, String codCpto, AdaptadorSQLBD adapt){
        Connection con = null;
        InfoDesplegableVO datos = null;
        
        try {
            con = adapt.getConnection();
            
            datos = MeLanbide83DAO.getInstance().obtenerInfoDesplegablePorCodigo(codigo, codCpto, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al obtener información del desplegable de código: "+codigo);
            sqle.printStackTrace();
        } finally {
            try {
                if(con!=null) con.close();
            } catch(SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        
        return datos;
    }
    
    public ArrayList<FacturaVO> recuperarListaFacturas(String numExpediente, String tabla, String codOrg, int idioma,String codProc, AdaptadorSQLBD adapt) {
        ArrayList<FacturaVO> lista = null;
        HashMap<String,String> desplegables = new HashMap<String, String>();
        Connection con = null;
        
        try {
            con = adapt.getConnection();
            String[] partes = numExpediente.split("/");
            Integer ejercicio = Integer.parseInt(partes[0]);
            desplegables.put(ConstantesMeLanbide83.getCOD_DESPL_CONCEPTO(), ConstantesMeLanbide83.getPropVal_COD_DESPL_CTAT(codOrg, codProc,ejercicio));
            lista = MeLanbide83DAO.getInstance().obtenerFacturas(numExpediente, tabla, desplegables, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al recuperar la lista de facturas");
            sqle.printStackTrace();
        } finally {
            try {
                if(con!=null) con.close();
            } catch(SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        
        return lista;
    }
    
    public boolean guardarFactura(FacturaVO factura, String tabla, String secuencia, AdaptadorSQLBD adapt) throws BDException, SQLException{
        Connection con = null;
        boolean exito = false;
        
        try {
            con = adapt.getConnection();
            
            if(factura.getCodIdent()==-1)
                exito = MeLanbide83DAO.getInstance().altaFactura(factura, tabla, secuencia, con);
            else if(factura.getCodIdent()>0)
                exito = MeLanbide83DAO.getInstance().actualizarFactura(factura, tabla, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al añadir/modificar una factura a la BBDD");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
            } catch(SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        
        return exito;
    }
    
    public boolean borrarFactura(FacturaVO factura, String tabla, AdaptadorSQLBD adapt) throws BDException, SQLException{
        Connection con = null;
        boolean exito = false;
        
        try {
            con = adapt.getConnection();
            
            exito = MeLanbide83DAO.getInstance().borrarFactura(factura, tabla, con);
        } catch (BDException bde) {
            log.error("Ha ocurrido un error al obtener una conexión a la BBDD");
            bde.printStackTrace();
            throw bde;
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al eliminar una factura a la BBDD");
            sqle.printStackTrace();
            throw sqle;
        } finally {
            try {
                if(con!=null) con.close();
                
            } catch(SQLException ex){
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        
        return exito;
    }
        
    public InfoDesplegableVO recuperarCampoDesplegable(String codOrg,int idioma, String codCombo) throws SQLException{
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        CampoDesplegableModuloIntegracionVO campoDesplegable = null;
        ArrayList<ElementoDesplegableVO> elementosDesplegable = new ArrayList<ElementoDesplegableVO>();
        InfoDesplegableVO desp = null;
        
        salida = el.getCampoDesplegable(codOrg, codCombo);
        if(salida.getStatus() == 0){
            campoDesplegable = salida.getCampoDesplegable();
            if(campoDesplegable != null){
                ArrayList<ValorCampoDesplegableModuloIntegracionVO> valores = campoDesplegable.getValores();

                desp = new InfoDesplegableVO();
                desp.setCodDesplegable(campoDesplegable.getCodigo());
                desp.setDescDesplegable(campoDesplegable.getDescripcion());
                for(ValorCampoDesplegableModuloIntegracionVO valor : valores){
                    String cod = valor.getCodigo();
                    String val = getDescripcionDesplegableByIdioma(idioma,valor.getDescripcion());
                    ElementoDesplegableVO elem = new ElementoDesplegableVO(cod, val);
                    elementosDesplegable.add(elem);
                }
                desp.setParesCodVal(elementosDesplegable);
            }
        }else{
            throw new SQLException(salida.getDescStatus());
        }
        return desp;
    }
    
    public TerceroVO getDatosTerceroXNroExpediente(String numeExpediente, Connection con) throws Exception {
        return MeLanbide83DAO.getInstance().getDatosTerceroXNroExpediente(numeExpediente, con);
    }
    
     public String getDescripcionDesplegableByIdioma(int idioma, String descripcion ) {
       log.info("getDescripcionDesplegable : descripcion " + descripcion + "idioma " + idioma);
        String barraIdioma = "\\|" ;
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraIdioma);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length >1) {
                    if (idioma == 4) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                } else{
                    log.info("El tamano del no es valido " + descripcion);
                }
            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
 
    }
}
