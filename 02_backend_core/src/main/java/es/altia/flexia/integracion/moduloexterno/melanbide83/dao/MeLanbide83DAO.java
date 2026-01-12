package es.altia.flexia.integracion.moduloexterno.melanbide83.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.util.ConstantesMeLanbide83;
import es.altia.flexia.integracion.moduloexterno.melanbide83.util.MeLanbide83MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.ElementoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InfoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InformeAtaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InformeAtase_No_AceptadasVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide83DAO {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide83DAO.class);
    //Instancia
    private static MeLanbide83DAO instance = null;
    // Constructor
    private MeLanbide83DAO() {   
        
    }
    
    //Devolvemos una �nica instancia de la clase a trav�s de este m�todo ya que el constructor es privado
    public static MeLanbide83DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide83DAO.class)
            {
                instance = new MeLanbide83DAO();
            }
        }
        return instance;
    }
    
    public InfoDesplegableVO obtenerInfoDesplegablePorCodigo(String codDesplegable, String codConcepto, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        InfoDesplegableVO datos = new InfoDesplegableVO();
        ArrayList<String> codigos = new ArrayList<String>();
        ArrayList<String> valores = new ArrayList<String>();
        ArrayList<ElementoDesplegableVO> elementos = new ArrayList<ElementoDesplegableVO>();
        
        try{
            query = "SELECT E_DES.DES_NOM AS NOMBRE,E_DES_VAL.DES_VAL_COD,E_DES_VAL.DES_NOM AS VALOR FROM E_DES_VAL "
                    + "JOIN E_DES ON E_DES.DES_COD = E_DES_VAL.DES_COD "
                    + "WHERE E_DES_VAL.DES_COD=? ORDER BY DES_VAL_COD";
            log.debug("query = "+query);
            log.debug("parametros de la query - codigo del desplegable: "+codDesplegable);
            
            ps = con.prepareStatement(query);
            ps.setString(1, codDesplegable);
            rs = ps.executeQuery();
            
            datos.setCodDesplegable(codDesplegable);
            boolean primero = true;
            while(rs.next()){
                if(primero){
                    datos.setDescDesplegable(rs.getString("NOMBRE"));
                    primero = false;
                }
                String cod = rs.getString("DES_VAL_COD");
                String val = rs.getString("VALOR");
                if(codConcepto == null || cod.startsWith(codConcepto)){
                    ElementoDesplegableVO elem = new ElementoDesplegableVO(cod, val);
                    codigos.add(rs.getString("DES_VAL_COD"));
                    valores.add(rs.getString("VALOR"));
                    elementos.add(elem);
                }
            }
            datos.setCodigos(codigos);
            datos.setValores(valores);
            datos.setParesCodVal(elementos);
        } catch(SQLException ex){
            log.error("Ha ocurrido un error al obtener los datos del desplegable con codigo: "+codDesplegable);
            ex.printStackTrace();
            throw ex;
        } finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        
        return datos;
    }
    
    public ArrayList<FacturaVO> obtenerFacturas(String numExpediente, String tabla, HashMap<String,String> desplegables, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<FacturaVO> lista = new ArrayList<FacturaVO>();
        String query;
        
        try {
            query = "SELECT ID,ESTADO, CASE WHEN ESTADO='A' THEN 'ACEPTADA' WHEN ESTADO='N' THEN 'NO ACEPTADA' ELSE '-' END AS DESCESTADO, "
                    + "FECHA_FACTURA,CODIGO_CONCEPTO,IMPORTE,ENTREGADO_JUSTIF,IMPUTADA,OBSERVACIONES"
                    + " FROM " + tabla + " WHERE NUM_EXP=? ORDER BY ID DESC";
            log.debug("query = "+query);
            
            ps = con.prepareStatement(query);
            ps.setString(1, numExpediente);
            
            rs = ps.executeQuery();
            while(rs.next()){
                FacturaVO factura = new FacturaVO();
                String codEstado = rs.getString("ESTADO");
                String estado = rs.getString("DESCESTADO");
                String codConcepto = rs.getString("CODIGO_CONCEPTO");
                String descConcepto = obtenerValorDesplegablePorCodigo(desplegables.get(ConstantesMeLanbide83.getCOD_DESPL_CONCEPTO()), codConcepto, con);
                
                factura.setCodIdent(rs.getInt("ID"));
                factura.setNumExpediente(numExpediente);
                factura.setCodEstado(codEstado);
                factura.setDescEstado(estado);
                factura.setFecha(rs.getDate("FECHA_FACTURA"));
                factura.setCodConcepto(codConcepto);
                factura.setDescConcepto(descConcepto);
                factura.setImporte(rs.getDouble("IMPORTE"));
                factura.setCodEntregaJustif(rs.getString("ENTREGADO_JUSTIF"));
                factura.setCodImputada(rs.getString("IMPUTADA"));
                factura.setObservaciones(rs.getString("OBSERVACIONES"));
                // Seteamos propiedades generadas a partir de otras
                factura.setFechaStr();
                factura.setImporteStr();
                factura.setDescEntregaJustif();
                
                lista.add(factura);
            }
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al recuperar la relacion de facturas de la base de datos");
            throw sqle;
        } finally {
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        return lista;
    }
    
    public boolean altaFactura(FacturaVO factura, String tabla, String secuencia, Connection con) throws SQLException{
        PreparedStatement ps = null;
        int insertado = 0;
        String query;
        
        try {
            query = "INSERT INTO " + tabla + "(ID,NUM_EXP,ESTADO,FECHA_FACTURA,CODIGO_CONCEPTO,IMPORTE,ENTREGADO_JUSTIF,IMPUTADA,"
                    + "OBSERVACIONES) VALUES (" + secuencia + ".NEXTVAL,?,?,?,?,?,?,?,?)";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query = "+factura.getNumExpediente()+"-"+factura.getCodEstado()+"-"+factura.getFecha()+"-"+
                    factura.getCodConcepto()+"-"+factura.getImporte()+"-"+
                    factura.getCodEntregaJustif()+"-" + factura.getCodImputada()+"-" + factura.getObservaciones());
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, factura.getNumExpediente());
            ps.setString(contbd++, factura.getCodEstado());
            ps.setDate(contbd++, new java.sql.Date(factura.getFecha().getTime()));
            ps.setString(contbd++, factura.getCodConcepto());
            ps.setDouble(contbd++, factura.getImporte());
            ps.setString(contbd++, factura.getCodEntregaJustif());
            ps.setString(contbd++, factura.getCodImputada());
            String observ = factura.getObservaciones();
            java.io.StringReader reader = new java.io.StringReader(observ);
            ps.setCharacterStream(contbd++, reader, observ.length());
            
            insertado = ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al guardar la factura en base de datos");
            throw sqle;
        } finally {
            if(ps!=null) ps.close();
        }
        return insertado>0;
    }
    
    public boolean actualizarFactura(FacturaVO factura, String tabla, Connection con) throws SQLException{
        PreparedStatement ps = null;
        int modificado = 0;
        String query;
        
        try {
            query = "UPDATE " + tabla + " SET ESTADO=?, FECHA_FACTURA=?, CODIGO_CONCEPTO=?, IMPORTE=?, "
                    + "ENTREGADO_JUSTIF=?, IMPUTADA=?, OBSERVACIONES=? WHERE ID=? AND NUM_EXP=?";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query = "+factura.getCodEstado()+"-"+factura.getFecha()+"-"+factura.getCodConcepto()+"-"+
                    factura.getImporte()+"-"+factura.getCodEntregaJustif()+"-"+
                    factura.getObservaciones()+  " - " +factura.getCodIdent()+"-"+factura.getNumExpediente());
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, factura.getCodEstado());
            ps.setDate(contbd++, new java.sql.Date(factura.getFecha().getTime()));
            ps.setString(contbd++, factura.getCodConcepto());
            ps.setDouble(contbd++, factura.getImporte());
            ps.setString(contbd++, factura.getCodEntregaJustif());
            ps.setString(contbd++, factura.getCodImputada());
            String observ = factura.getObservaciones();
            java.io.StringReader reader = new java.io.StringReader(observ);
            ps.setCharacterStream(contbd++, reader, observ.length());
            ps.setInt(contbd++, factura.getCodIdent());
            ps.setString(contbd++, factura.getNumExpediente());
            
            modificado = ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al guardar la factura en base de datos");
            throw sqle;
        } finally {
            if(ps!=null) ps.close();
        }
        return modificado>0;
    }
    
    public boolean borrarFactura(FacturaVO factura, String tabla, Connection con) throws SQLException{
        PreparedStatement ps = null;
        int borrado = 0;
        String query;
        
        try {
            query = "DELETE FROM " + tabla + " WHERE ID=? AND NUM_EXP=?";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query = "+factura.getCodIdent()+"-"+factura.getNumExpediente());
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, factura.getCodIdent());
            ps.setString(contbd++, factura.getNumExpediente());
            
            borrado = ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al guardar la factura en base de datos");
            throw sqle;
        } finally {
            if(ps!=null) ps.close();
        }
        return borrado>0;
    }
    
    public TerceroVO getDatosTerceroXNroExpediente(String numExpediente, Connection con) throws Exception {
        Statement st = null;	
        ResultSet rs = null;
        TerceroVO terceroVO = new TerceroVO();

        log.info(" getDatosTerceroXNroExpediente - Begin - " + numExpediente);
        try{
            String query = "Select T_HTE.* " +
                        " ,E_EXT.* " +
                        " ,TFECNACIMIENTO.VALOR TFecNacimiento " +
                        " ,TNACIONTERCERO.VALOR TNacionTercero " +
                        " ,TSEXOTERCERO.VALOR TSexoTercero " +
                        " ,trunc(months_between(sysdate,TFECNACIMIENTO.VALOR)/12) Edad " +
                        " from E_EXT " +
                        " INNER JOIN T_HTE ON HTE_NVR=EXT_NVR AND HTE_TER=EXT_TER " +
                        " LEFT JOIN T_CAMPOS_FECHA TFECNACIMIENTO ON TFECNACIMIENTO.COD_MUNICIPIO=EXT_MUN AND TFECNACIMIENTO.COD_TERCERO=EXT_TER AND TFECNACIMIENTO.COD_CAMPO='TFECNACIMIENTO' " +
                        " LEFT JOIN T_CAMPOS_DESPLEGABLE TNACIONTERCERO ON TNACIONTERCERO.COD_MUNICIPIO=EXT_MUN AND TNACIONTERCERO.COD_TERCERO=EXT_TER AND TNACIONTERCERO.COD_CAMPO='TNACIONTERCERO' " +
                        " LEFT JOIN T_CAMPOS_DESPLEGABLE TSEXOTERCERO ON TSEXOTERCERO.COD_MUNICIPIO=EXT_MUN AND TSEXOTERCERO.COD_TERCERO=EXT_TER AND TSEXOTERCERO.COD_CAMPO='TSEXOTERCERO' " +
                        " where ext_num='"+numExpediente+"' and ext_rol=1";
            log.info("getDatosTerceroXNroExpediente = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                terceroVO=(TerceroVO)MeLanbide83MappingUtils.getInstance().map(rs, TerceroVO.class);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando getDatosTerceroXNroExpediente", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info(" getDatosTerceroXNroExpediente - End - ");
        return terceroVO;
    }
    
     /**
      * Recupera el valor que tiene el desplegable indicado para el elemento con el c�digo pasado
      * @param codDesplegable
      * @param codigo
      * @param con
      * @return
      * @throws SQLException 
      */
    private String obtenerValorDesplegablePorCodigo(String codDesplegable, String codigo, Connection con) throws SQLException{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String valor = null;
        
        try{
            query = "SELECT DES_NOM AS VALOR FROM E_DES_VAL WHERE E_DES_VAL.DES_COD=? AND DES_VAL_COD=?";
            log.debug("query = "+query);
            log.debug("parametros de la query - codigo del desplegable: "+codDesplegable+" - codigo buscado: "+codigo);
            
            ps = con.prepareStatement(query);
            ps.setString(1, codDesplegable);
            ps.setString(2, codigo);
            rs = ps.executeQuery();
            
            if(rs.next()){
                valor = rs.getString("VALOR");
            }
        } catch(SQLException ex){
            log.error("Ha ocurrido un error al obtener el valor para el c�digo " + codigo + " del desplegable con codigo: "+codDesplegable);
            ex.printStackTrace();
            throw ex;
        } finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        
        return valor;
    }
    
    public List<InformeAtaseVO> getListaAtase(String numExpediente, Connection con) throws Exception {
          Statement st = null;	
        ResultSet rs = null;
        List<InformeAtaseVO> listTramites  = new ArrayList<InformeAtaseVO>();
        InformeAtaseVO datosInformeInterno;

        log.info("ListaAtase :"+ numExpediente);
        try{
            String query = "select IMPUTADA,ENTREGADO_JUSTIF,CODIGO_CONCEPTO,IMPORTE,OBSERVACIONES from MELANBIDE83_FACTURAS where NUM_EXP='"+numExpediente+"'and ESTADO='A'";
            log.info("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                log.info("fila informe");
                datosInformeInterno = (InformeAtaseVO) MeLanbide83MappingUtils.getInstance().map(rs, InformeAtaseVO.class);
                //log.info("OBSERVACIONES........ = " + datosInformeInterno.getOBSERVACIONES());
                listTramites.add(datosInformeInterno);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listTramites;
    }
    
    public List<InformeAtase_No_AceptadasVO> getListaAtaseNoPagadas(String numExpediente, Connection con) throws Exception {
        Statement st = null;	
        ResultSet rs = null;
        List<InformeAtase_No_AceptadasVO> listTramites  = new ArrayList<InformeAtase_No_AceptadasVO>();
        InformeAtase_No_AceptadasVO datosInformeInterno;

        log.info("ListaAtaseNoPagadas :"+ numExpediente);
        try{
            String query = "select IMPUTADA,ENTREGADO_JUSTIF,CODIGO_CONCEPTO,IMPORTE,OBSERVACIONES from MELANBIDE83_FACTURAS where NUM_EXP='"+numExpediente+"'and ESTADO='N'";
            log.info("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
                log.info("fila informe"+rs.getString("IMPORTE"));
                datosInformeInterno = (InformeAtase_No_AceptadasVO) MeLanbide83MappingUtils.getInstance().map(rs, InformeAtase_No_AceptadasVO.class);
                listTramites.add(datosInformeInterno);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listTramites;
    }
    
    public String getNombreInteresado(String numExpediente, Connection con) throws Exception {
        Statement st = null;	
        ResultSet rs = null;
        String nombre = null;
        String apellido = null;
        String nombreInteresado;

        log.info(" getNombreInteresado - Begin - " + numExpediente);
        try{
            String query = "SELECT t_hte.hte_nom,t_hte.hte_ap1, t_hte.hte_ap2 FROM t_hte,e_ext WHERE hte_ter = ext_ter AND hte_nvr = ext_nvr AND ext_num = '"+numExpediente+"'";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next())
            {
              nombre=rs.getString("hte_nom");
              apellido=rs.getString("hte_ap1") 
                      + (rs.getString("hte_ap2")!=null && !rs.getString("hte_ap2").isEmpty() && !rs.getString("hte_ap2").equalsIgnoreCase("null")  ? (" " + rs.getString("hte_ap1")) :"")
                      ;
            }
            nombreInteresado=nombre 
                    + (apellido!=null && !apellido.isEmpty() && !apellido.equalsIgnoreCase("null")  ? (" " + apellido) :"")
                    ;
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info(" getNombreInteresado - End - " + nombreInteresado);
        return nombreInteresado;
    }
    
    public double getTotalAyuda(String numExpediente, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        double valor = 0.00;
        
        try{
            query = "SELECT TNU_VALOR TOTALAYUDA FROM E_TNU WHERE TNU_COD='IMPCONCEDIDO' AND TNU_NUM='" + numExpediente + "'";
      
            log.debug("query getTotalAyuda = "+query);
            //log.debug("parametros de la query - codigo del desplegable: "+codDesplegable+" - codigo buscado: "+codigo);
            
            ps = con.prepareStatement(query);
            //ps.setString(1, codDesplegable);
            //ps.setString(2, codigo);
            rs = ps.executeQuery();
            
            if(rs.next()){
                valor = rs.getDouble("TOTALAYUDA");
            }
        } catch(SQLException ex){
            log.error("Ha ocurrido un error al obtener el valor de getTotalAyuda");
            ex.printStackTrace();
            throw ex;
        } finally{
            if(rs!=null) rs.close();
            if(ps!=null) ps.close();
        }
        
        return valor;
    }

}
