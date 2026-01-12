package es.altia.flexia.integracion.moduloexterno.melanbide69.dao;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide69.util.ConstantesMeLanbide69;
import es.altia.flexia.integracion.moduloexterno.melanbide69.util.MeLanbide69MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosSuplementariosExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ElementoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InformeApesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InformeApes_No_AceptadasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.SocioVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide69DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide69DAO.class);
    //Instancia
    private static MeLanbide69DAO instance = null;

    // Constructor
    private MeLanbide69DAO() {

    }

    //Devolvemos una ï¿½nica instancia de la clase a travï¿½s de este mï¿½todo ya que el constructor es privado
    public static MeLanbide69DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide69DAO.class) {
                instance = new MeLanbide69DAO();
            }
        }
        return instance;
    }

    public InfoDesplegableVO obtenerInfoDesplegablePorCodigo(String codDesplegable, String codConcepto, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        InfoDesplegableVO datos = new InfoDesplegableVO();
        ArrayList<String> codigos = new ArrayList<String>();
        ArrayList<String> valores = new ArrayList<String>();
        ArrayList<ElementoDesplegableVO> elementos = new ArrayList<ElementoDesplegableVO>();

        try {
            query = "SELECT E_DES.DES_NOM AS NOMBRE,E_DES_VAL.DES_VAL_COD,E_DES_VAL.DES_NOM AS VALOR FROM E_DES_VAL "
                    + "JOIN E_DES ON E_DES.DES_COD = E_DES_VAL.DES_COD "
                    + "WHERE E_DES_VAL.DES_COD=? ORDER BY DES_VAL_COD";
            log.debug("query = " + query);
            log.debug("parametros de la query - codigo del desplegable: " + codDesplegable);

            ps = con.prepareStatement(query);
            ps.setString(1, codDesplegable);
            rs = ps.executeQuery();

            datos.setCodDesplegable(codDesplegable);
            boolean primero = true;
            while (rs.next()) {
                if (primero) {
                    datos.setDescDesplegable(rs.getString("NOMBRE"));
                    primero = false;
                }
                String cod = rs.getString("DES_VAL_COD");
                String val = rs.getString("VALOR");
                if (codConcepto == null || cod.startsWith(codConcepto)) {
                    ElementoDesplegableVO elem = new ElementoDesplegableVO(cod, val);
                    codigos.add(rs.getString("DES_VAL_COD"));
                    valores.add(rs.getString("VALOR"));
                    elementos.add(elem);
                }
            }
            datos.setCodigos(codigos);
            datos.setValores(valores);
            datos.setParesCodVal(elementos);
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener los datos del desplegable con codigo: " + codDesplegable);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return datos;
    }

    public ArrayList<FacturaVO> obtenerFacturas(String numExpediente, String tabla, HashMap<String, String> desplegables, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<FacturaVO> lista = new ArrayList<FacturaVO>();
        String query;

        try {
            /*query = "SELECT ID,ESTADO,FECHA_FACTURA,CODIGO_CONCEPTO,CODIGO_SUBCONCEPTO,IMPORTE,ENTREGADA_FACT,ENTREGADO_JUSTIF,OBSERVACIONES,"
                    + "IDENTIFICACION FROM " + tabla + " WHERE NUM_EXP=? ORDER BY ID DESC";*/
            query = "SELECT ID,ESTADO, CASE WHEN ESTADO='A' THEN 'ACEPTADA' WHEN ESTADO='N' THEN 'NO ACEPTADA' ELSE '-' END AS DESCESTADO, "
                    + "FECHA_FACTURA,CODIGO_CONCEPTO,CODIGO_SUBCONCEPTO,IMPORTE,ENTREGADA_FACT,ENTREGADO_JUSTIF,OBSERVACIONES,"
                    + "IDENTIFICACION FROM " + tabla + " WHERE NUM_EXP=? ORDER BY ID DESC";
            log.debug("query = " + query);

            ps = con.prepareStatement(query);
            ps.setString(1, numExpediente);

            rs = ps.executeQuery();
            while (rs.next()) {
                FacturaVO factura = new FacturaVO();
                String codEstado = rs.getString("ESTADO");
                String estado = rs.getString("DESCESTADO");
                /*if("A".equals(codEstado))
                    estado = "ACEPTADA";
                else if ("N".equals(codEstado)) estado = "NO ACEPTADA";
                else estado ="";*/
                String codConcepto = rs.getString("CODIGO_CONCEPTO");
                String descConcepto = obtenerValorDesplegablePorCodigo(desplegables.get(ConstantesMeLanbide69.getCOD_DESPL_CONCEPTO()), codConcepto, con);
                String codSubcpto = rs.getString("CODIGO_SUBCONCEPTO");
                String descSubcpto = obtenerValorDesplegablePorCodigo(desplegables.get(ConstantesMeLanbide69.getCOD_DESPL_DESGLOSE_CPTO()), codSubcpto, con);

                factura.setCodIdent(rs.getInt("ID"));
                factura.setNumExpediente(numExpediente);
                factura.setCodEstado(codEstado);
                factura.setDescEstado(estado);
                factura.setFecha(rs.getDate("FECHA_FACTURA"));
                factura.setCodConcepto(codConcepto);
                factura.setDescConcepto(descConcepto);
                factura.setCodDesgloseCpto(codSubcpto);
                factura.setDescDesgloseCpto(descSubcpto);
                factura.setImporte(rs.getDouble("IMPORTE"));
                factura.setCodEntregaFact(rs.getString("ENTREGADA_FACT"));
                factura.setCodEntregaJustif(rs.getString("ENTREGADO_JUSTIF"));
                factura.setObservaciones(rs.getString("OBSERVACIONES"));
                factura.setIdentFactura(rs.getString("IDENTIFICACION"));
                // Seteamos propiedades generadas a partir de otras
                factura.setFechaStr();
                factura.setImporteStr();
                factura.setDescEntregaFact();
                factura.setDescEntregaJustif();

                lista.add(factura);
            }
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al recuperar la relacion de facturas de la base de datos");
            throw sqle;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return lista;
    }

    public boolean altaFactura(FacturaVO factura, String tabla, String secuencia, Connection con) throws SQLException {
        PreparedStatement ps = null;
        int insertado = 0;
        String query;

        try {
            query = "INSERT INTO " + tabla + "(ID,NUM_EXP,ESTADO,FECHA_FACTURA,CODIGO_CONCEPTO,CODIGO_SUBCONCEPTO,IMPORTE,ENTREGADA_FACT,ENTREGADO_JUSTIF,"
                    + "IDENTIFICACION,OBSERVACIONES) VALUES (" + secuencia + ".NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query = " + factura.getNumExpediente() + "-" + factura.getCodEstado() + "-" + factura.getFecha() + "-"
                    + factura.getCodConcepto() + "-" + factura.getCodDesgloseCpto() + "-" + factura.getImporte() + "-" + factura.getCodEntregaFact() + "-"
                    + factura.getCodEntregaJustif() + "-" + factura.getIdentFactura());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, factura.getNumExpediente());
            ps.setString(contbd++, factura.getCodEstado());
            ps.setDate(contbd++, new java.sql.Date(factura.getFecha().getTime()));
            ps.setString(contbd++, factura.getCodConcepto());
            ps.setString(contbd++, factura.getCodDesgloseCpto());
            ps.setDouble(contbd++, factura.getImporte());
            ps.setString(contbd++, factura.getCodEntregaFact());
            ps.setString(contbd++, factura.getCodEntregaJustif());
            ps.setString(contbd++, factura.getIdentFactura());
            String observ = factura.getObservaciones();
            java.io.StringReader reader = new java.io.StringReader(observ);
            ps.setCharacterStream(contbd++, reader, observ.length());

            insertado = ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al guardar la factura en base de datos");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return insertado > 0;
    }

    public boolean actualizarFactura(FacturaVO factura, String tabla, Connection con) throws SQLException {
        PreparedStatement ps = null;
        int modificado = 0;
        String query;

        try {
            query = "UPDATE " + tabla + " SET ESTADO=?, FECHA_FACTURA=?, CODIGO_CONCEPTO=?, CODIGO_SUBCONCEPTO=?, IMPORTE=?, ENTREGADA_FACT=?, "
                    + "ENTREGADO_JUSTIF=?, IDENTIFICACION=?, OBSERVACIONES=? WHERE ID=? AND NUM_EXP=?";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query = " + factura.getCodEstado() + "-" + factura.getFecha() + "-" + factura.getCodConcepto() + "-"
                    + factura.getCodDesgloseCpto() + "-" + factura.getImporte() + "-" + factura.getCodEntregaFact() + "-" + factura.getCodEntregaJustif() + "-"
                    + factura.getIdentFactura() + "-" + factura.getCodIdent() + "-" + factura.getNumExpediente());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, factura.getCodEstado());
            ps.setDate(contbd++, new java.sql.Date(factura.getFecha().getTime()));
            ps.setString(contbd++, factura.getCodConcepto());
            ps.setString(contbd++, factura.getCodDesgloseCpto());
            ps.setDouble(contbd++, factura.getImporte());
            ps.setString(contbd++, factura.getCodEntregaFact());
            ps.setString(contbd++, factura.getCodEntregaJustif());
            ps.setString(contbd++, factura.getIdentFactura());
            String observ = factura.getObservaciones();
            java.io.StringReader reader = new java.io.StringReader(observ);
            ps.setCharacterStream(contbd++, reader, observ.length());
            ps.setInt(contbd++, factura.getCodIdent());
            ps.setString(contbd++, factura.getNumExpediente());

            modificado = ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al guardar la factura en base de datos");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return modificado > 0;
    }

    public boolean borrarFactura(FacturaVO factura, String tabla, Connection con) throws SQLException {
        PreparedStatement ps = null;
        int borrado = 0;
        String query;

        try {
            query = "DELETE FROM " + tabla + " WHERE ID=? AND NUM_EXP=?";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query = " + factura.getCodIdent() + "-" + factura.getNumExpediente());

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, factura.getCodIdent());
            ps.setString(contbd++, factura.getNumExpediente());

            borrado = ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al guardar la factura en base de datos");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return borrado > 0;
    }

    /**
     * Recupera el valor que tiene el desplegable indicado para el elemento con
     * el código pasado
     *
     * @param codDesplegable
     * @param codigo
     * @param con
     * @return
     * @throws SQLException
     */
    private String obtenerValorDesplegablePorCodigo(String codDesplegable, String codigo, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String valor = null;

        try {
            query = "SELECT DES_NOM AS VALOR FROM E_DES_VAL WHERE E_DES_VAL.DES_COD=? AND DES_VAL_COD=?";
            log.debug("query = " + query);
            log.debug("parametros de la query - codigo del desplegable: " + codDesplegable + " - codigo buscado: " + codigo);

            ps = con.prepareStatement(query);
            ps.setString(1, codDesplegable);
            ps.setString(2, codigo);
            rs = ps.executeQuery();

            if (rs.next()) {
                valor = rs.getString("VALOR");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al obtener el valor para el código " + codigo + " del desplegable con codigo: " + codDesplegable);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return valor;
    }

    public GeneralValueObject getDatosSuplementariosExpediente(ArrayList<String> codsCampo, int codOrg, int ejerc, String codProc, String numExp, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        GeneralValueObject datos = new GeneralValueObject();

        try {
            for (String codCampo : codsCampo) {
                query = "SELECT PCA_TDA FROM E_EXP JOIN E_PCA ON EXP_MUN=PCA_MUN AND EXP_PRO=PCA_PRO "
                        + "WHERE EXP_MUN=? AND EXP_EJE=? AND EXP_PRO=? AND EXP_NUM=? AND PCA_COD=?";

                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetros pasados a la query: " + codOrg + "-" + ejerc + "-" + codProc + "-" + numExp + "-" + codCampo);
                }

                int contbd = 1;
                ps = con.prepareStatement(query);
                ps.setString(contbd++, String.valueOf(codOrg));
                ps.setInt(contbd++, ejerc);
                ps.setString(contbd++, codProc);
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, codCampo);

                rs = ps.executeQuery();
                if (rs.next()) {
                    String tipoDato = rs.getString("PCA_TDA");
                    Object valor = getValorCampoSup(codCampo, tipoDato, codOrg, ejerc, numExp, con);
                    datos.setAtributo(codCampo, valor);
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return datos;
    }

    public int grabarValorCampoSup(String codCampo, Object valorCS, String codOrg, String codProc, int ejer, String numExp, Connection con) throws SQLException {
        PreparedStatement ps = null;
        StringBuilder query = new StringBuilder("");
        String tipoDato;
        String parteTabla;
        int contbd = 1;
        int actualizado = 0;

        try {
            tipoDato = recuperarTipoCS(codCampo, Integer.parseInt(codOrg), codProc, con);
            parteTabla = recuperarTablaTipo(tipoDato, con);
            if (parteTabla.isEmpty()) {
                parteTabla = "TXT";
            }

            query = query.append("DELETE FROM E_").append(parteTabla).append(" WHERE ").append(parteTabla).append("_MUN=? AND ")
                    .append(parteTabla).append("_EJE=? AND ").append(parteTabla).append("_NUM=? AND ").append(parteTabla).append("_COD=?");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetros pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo);
            }
            ps = con.prepareStatement(query.toString());
            ps.setInt(contbd++, Integer.parseInt(codOrg));
            ps.setInt(contbd++, ejer);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, codCampo);
            ps.executeUpdate();

            if (valorCS != null) {
                query.delete(0, query.length());
                query.insert(0, "INSERT INTO E_").append(parteTabla).append(" (").append(parteTabla).append("_MUN,").append(parteTabla)
                        .append("_EJE,").append(parteTabla).append("_NUM,").append(parteTabla).append("_COD,").append(parteTabla)
                        .append("_VALOR) VALUES (?,?,?,?,?)");

                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetros pasados a la query: " + codOrg + "-" + ejer + "-" + numExp + "-" + codCampo + "-" + valorCS);
                }

                ps = con.prepareStatement(query.toString());
                contbd = 1;
                ps.setInt(contbd++, Integer.parseInt(codOrg));
                ps.setInt(contbd++, ejer);
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, codCampo);
                if (valorCS instanceof String) {
                    String valor = (String) valorCS;
                    ps.setString(contbd++, valor);
                } else if (valorCS instanceof Double) {
                    Double valor = (Double) valorCS;
                    ps.setDouble(contbd++, valor);
                } else if (valorCS instanceof Date) {
                    Date valor = (Date) valorCS;
                    ps.setDate(contbd++, new java.sql.Date(valor.getTime()));
                }

                actualizado = ps.executeUpdate();
            } else {
                actualizado = 1;
            }
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error al grabar el valor de campo suplementario", ex);
            throw new SQLException(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al grabar el valor de campo suplementario", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return actualizado;
    }

    public String getDesCodCampoSuplementario(String codCampo, int codOrg, String codProc, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String desCod = null;

        try {
            query = "SELECT PCA_DESPLEGABLE FROM E_PCA WHERE PCA_TDA=6 AND PCA_MUN=? AND PCA_PRO=? AND PCA_COD=?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetros pasados a la query: " + codOrg + "-" + codProc + "-" + codCampo);
            }

            int contbd = 1;
            ps = con.prepareStatement(query);
            ps.setString(contbd++, String.valueOf(codOrg));
            ps.setString(contbd++, codProc);
            ps.setString(contbd++, codCampo);

            rs = ps.executeQuery();
            while (rs.next()) {
                desCod = rs.getString("PCA_DESPLEGABLE");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return desCod;
    }

    public Object getValorCampoSup(String codCampo, String tipoDato, int codOrg, int ejerc, String numExp, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query = new StringBuilder("");
        String partTabla;
        Object valor = null;

        try {
            partTabla = recuperarTablaTipo(tipoDato, con);
            if (partTabla.isEmpty()) {
                partTabla = "TXT";
            }

            query = query.append("SELECT ").append(partTabla).append("_VALOR FROM E_").append(partTabla).append(" WHERE ")
                    .append(partTabla).append("_MUN=? AND ").append(partTabla).append("_EJE=? AND ").append(partTabla).append("_NUM=? AND ")
                    .append(partTabla).append("_COD=?");

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetros pasados a la query: " + codOrg + "-" + ejerc + "-" + numExp + "-" + codCampo);
            }

            int contbd = 1;
            ps = con.prepareStatement(query.toString());
            ps.setString(contbd++, String.valueOf(codOrg));
            ps.setInt(contbd++, ejerc);
            ps.setString(contbd++, numExp);
            ps.setString(contbd++, codCampo);

            rs = ps.executeQuery();
            while (rs.next()) {
                if (tipoDato.equals("1") || tipoDato.equals("8")) //Numï¿½rico o numï¿½rico calculado
                {
                    valor = String.valueOf(rs.getInt(partTabla + "_VALOR"));
                }
                if (tipoDato.equals("3") || tipoDato.equals("9")) //Fecha o fecha calculada
                {
                    valor = rs.getDate(partTabla + "_VALOR");
                } else {
                    valor = rs.getString(partTabla + "_VALOR");
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return valor;
    }

    private String recuperarTablaTipo(String tipoDato, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String idTabla = "";

        try {
            query = "SELECT TDA_TAB FROM E_TDA WHERE TDA_COD=?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetros pasados a la query: " + tipoDato);
            }

            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(tipoDato));

            rs = ps.executeQuery();
            if (rs.next()) {
                idTabla = rs.getString("TDA_TAB").substring(2);
            }
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error al recuperar el nombre de tabla en la que buscar el valor de cs segï¿½n el tipo de dato", ex);
            throw new SQLException(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar el nombre de tabla en la que buscar el valor de cs segï¿½n el tipo de dato", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return idTabla;
    }

    private String recuperarTipoCS(String codCampo, int codOrg, String codProc, Connection con) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        String tipoDato = null;

        try {
            query = "SELECT PCA_TDA AS TIPO FROM E_PCA WHERE PCA_MUN=? AND PCA_PRO=? AND PCA_COD=?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Parámetros pasados a la query: " + codOrg + "-" + codProc + "-" + codCampo);
            }

            int contbd = 1;
            ps = con.prepareStatement(query);
            ps.setString(contbd++, String.valueOf(codOrg));
            ps.setString(contbd++, codProc);
            ps.setString(contbd++, codCampo);

            rs = ps.executeQuery();
            if (rs.next()) {
                tipoDato = rs.getString("TIPO");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando valores de datos suplementarios", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tipoDato;
    }

    public List<InformeApesVO> getListaApes(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<InformeApesVO> listTramites = new ArrayList<InformeApesVO>();
        InformeApesVO datosInformeInterno;

        log.info("FECHA HASTA " + numExpediente);
        try {
            String query = "select IDENTIFICACION,ENTREGADA_FACT,ENTREGADO_JUSTIF,CODIGO_SUBCONCEPTO,IMPORTE from MELANBIDE69_FACTURAS where NUM_EXP='" + numExpediente + "'and ESTADO='A'";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                log.info("fila informe");
                datosInformeInterno = (InformeApesVO) MeLanbide69MappingUtils.getInstance().map(rs, InformeApesVO.class);
                listTramites.add(datosInformeInterno);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
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

        log.error("FECHA HASTA" + numExpediente);
        try {
            String query = "SELECT t_hte.hte_nom,t_hte.hte_ap1 FROM t_hte,e_ext WHERE hte_ter = ext_ter AND hte_nvr = ext_nvr AND EXT_ROL=1 AND ext_num = '" + numExpediente + "'";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                nombre = rs.getString("hte_nom");
                apellido = rs.getString("hte_ap1");
            }
            nombreInteresado = nombre + " " + apellido;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return nombreInteresado;
    }

    public List<InformeApes_No_AceptadasVO> getListaApesNoPagadas(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<InformeApes_No_AceptadasVO> listTramites = new ArrayList<InformeApes_No_AceptadasVO>();
        InformeApes_No_AceptadasVO datosInformeInterno;

        log.error("FECHA HASTA" + numExpediente);
        try {
            String query = "select IDENTIFICACION,ENTREGADA_FACT,ENTREGADO_JUSTIF,CODIGO_SUBCONCEPTO,IMPORTE,OBSERVACIONES from MELANBIDE69_FACTURAS where NUM_EXP='" + numExpediente + "'and ESTADO='N'";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                log.error("fila informe");
                rs.getString("IMPORTE");
                log.error("fila informe" + rs.getString("IMPORTE"));
                datosInformeInterno = (InformeApes_No_AceptadasVO) MeLanbide69MappingUtils.getInstance().map(rs, InformeApes_No_AceptadasVO.class);
                listTramites.add(datosInformeInterno);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listTramites;
    }

    public DatosSuplementariosExpedienteVO getDatosSuplementariosInforme(int codOrg, String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String procedimiento = numExpediente.split("/")[1];
            StringBuilder query = new StringBuilder("SELECT subv.TNU_VALOR IMPSUBVENCIONG,subvcb.TNU_VALOR IMPSUBVENCBSC,primerpago.TNU_VALOR IMPPRIMERPAGOG,primerpagocb.TNU_VALOR IMPPRIMERPAGOCBSC,segundopago.TNU_VALOR IMPSEGUNDOPAGO2,reintegro.TNU_VALOR CANTREINTEGRAR,fecha.TFE_VALOR FECPRIMERPAGO, resolucion.tde_valor RESOLUCION FROM e_EXP LEFT JOIN (SELECT * FROM E_TNU WHERE TNU_COD = '");
            query.append(ConstantesMeLanbide69.getPropVal_CS_SUBV(String.valueOf(codOrg), procedimiento));
            query.append("')subv ON e_EXP.EXP_NUM =subv.tnu_num AND e_EXP.EXP_mUn=subv.tnu_mun AND e_EXP.EXP_EJE=subv.tnu_eje LEFT JOIN (SELECT * FROM E_TNU WHERE TNU_COD = '");
            query.append(ConstantesMeLanbide69.getPropVal_CS_SUBVCB(String.valueOf(codOrg), procedimiento));
            query.append("')subvcb ON e_EXP.EXP_NUM =subvcb.tnu_num AND e_EXP.EXP_mUn=subvcb.tnu_mun AND e_EXP.EXP_EJE=subvcb.tnu_eje LEFT JOIN   (SELECT * FROM E_TNU WHERE TNU_COD = '");
            query.append(ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGO(String.valueOf(codOrg), procedimiento));
            query.append("')primerpago ON e_EXP.EXP_NUM =primerpago.tnu_num AND e_EXP.EXP_mUn=primerpago.tnu_mun AND e_EXP.EXP_EJE=primerpago.tnu_eje LEFT JOIN   (SELECT * FROM E_TNU WHERE TNU_COD = '");
            query.append(ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGOCB(String.valueOf(codOrg), procedimiento));
            query.append("')primerpagocb ON e_EXP.EXP_NUM =primerpagocb.tnu_num AND e_EXP.EXP_mUn=primerpagocb.tnu_mun AND e_EXP.EXP_EJE=primerpagocb.tnu_eje  LEFT JOIN (SELECT * FROM E_TNU WHERE TNU_COD = '");
            query.append(ConstantesMeLanbide69.getPropVal_CS_SEGPAGO2(String.valueOf(codOrg), procedimiento));
            query.append("')segundopago ON e_EXP.EXP_NUM =segundopago.tnu_num  AND e_EXP.EXP_mUn=segundopago.tnu_mun AND e_EXP.EXP_EJE=segundopago.tnu_eje LEFT JOIN (SELECT * FROM E_TNU WHERE TNU_COD = '");
            query.append(ConstantesMeLanbide69.getPropVal_CS_IMPREINTEGRO(String.valueOf(codOrg), procedimiento));
            query.append("' )reintegro ON e_EXP.EXP_NUM =reintegro.tnu_num AND e_EXP.EXP_mUn=reintegro.tnu_mun AND e_EXP.EXP_EJE=reintegro.tnu_eje LEFT JOIN (SELECT * FROM E_TFE WHERE TFE_COD = 'FECPRIMERPAGO'"
                    + "  )fecha ON e_EXP.EXP_NUM   =fecha.tFE_num AND e_EXP.EXP_mUn  =fecha.tFE_mun AND e_EXP.EXP_EJE  =fecha.tFE_eje ");
            query.append(" LEFT JOIN (SELECT * FROM E_TDE WHERE TDE_COD = 'RESFAV'  )resolucion ");
            query.append(" ON e_EXP.EXP_NUM   =resolucion.tdE_num AND e_EXP.EXP_mUn  =resolucion.tdE_mun AND e_EXP.EXP_EJE  =resolucion.tdE_eje  ");

            query.append(" WHERE e_exp.exp_num='");
            query.append(numExpediente);
            query.append("'");

            log.debug("SQL getImportesSubv1Pago = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            if (rs.next()) {
                return (DatosSuplementariosExpedienteVO) MeLanbide69MappingUtils.getInstance().map(rs, DatosSuplementariosExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return null;
    }

//    public String getGastosJustificados(int codOrg, String numExpediente, Connection con) throws Exception {
//        Statement st = null;	
//        ResultSet rs = null;
//        String Gastos = null;
//        String GastosJustificados = null;
//
//        log.error("FECHA HASTA"+ numExpediente);
//        try{
//            String query = "SELECT TDE_VALOR  FROM E_TDE,E_PCA, E_DES_VAL  WHERE TDE_NUM='"+numExpediente+"'  AND TDE_MUN = PCA_MUN AND TDE_COD = PCA_COD  AND PCA_DESPLEGABLE = DES_COD AND TDE_VALOR = DES_VAL_COD  AND TDE_COD='"+ConstantesMeLanbide69.getPropVal_CS_PRIMERPAGO(String.valueOf(codOrg), numExpediente.split("/")[1])+"'";
//            log.debug("SQL CREACION  EXCEL = " + query);
//            st = con.createStatement();
//            rs = st.executeQuery(query);
//            while(rs.next())
//            {
//              Gastos=rs.getString("TDE_VALOR");
//              
//            }
//            Gastos=GastosJustificados;
//        }
//        catch(Exception ex)
//        {
//            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
//            throw new Exception(ex);
//        }
//        finally {
//            try {
//                if(log.isDebugEnabled()) 
//                    log.debug("Procedemos a cerrar el statement y el resultset");
//                if(st!=null) 
//                    st.close();
//                if(rs!=null) 
//                    rs.close();
//            }
//            catch(Exception e)
//            {
//                log.error("Se ha producido un error cerrando el statement y el resulset", e);
//                throw new Exception(e);
//            }
//        }
//        return GastosJustificados;
//    }
    public String getACobrar(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String nombre = null;
        String apellido = null;
        String nombreInteresado;

        log.error("FECHA HASTA" + numExpediente);
        try {
            String query = "SELECT t_hte.hte_nom,t_hte.hte_ap1 FROM t_hte,e_ext WHERE hte_ter = ext_ter AND hte_nvr = ext_nvr AND ext_num = '" + numExpediente + "'";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                nombre = rs.getString("hte_nom");
                apellido = rs.getString("hte_ap1");
            }
            nombreInteresado = nombre + " " + apellido;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return nombreInteresado;
    }

    public String getADevolver(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String nombre = null;
        String apellido = null;
        String nombreInteresado;

        log.error("FECHA HASTA" + numExpediente);
        try {
            String query = "SELECT t_hte.hte_nom,t_hte.hte_ap1 FROM t_hte,e_ext WHERE hte_ter = ext_ter AND hte_nvr = ext_nvr AND ext_num = '" + numExpediente + "'";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                nombre = rs.getString("hte_nom");
                apellido = rs.getString("hte_ap1");
            }
            nombreInteresado = nombre + " " + apellido;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return nombreInteresado;
    }

    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query;
        try {
            query = new StringBuilder("SELECT T_CAMPOS_DESPLEGABLE.VALOR as sexo, T_CAMPOS_FECHA.VALOR as fecnacimiento, TFE_VALOR as fecPresentacion, TXT_VALOR AS cifCBSC, TDE_VALOR as esCBSC"
                    + " FROM e_exp"
                    + " INNER JOIN e_ext on e_exp.EXP_MUN=e_ext.EXT_MUN and e_exp.EXP_EJE=e_ext.EXT_EJE and e_exp.EXP_NUM=e_ext.EXT_NUM and e_exp.EXP_PRO=e_ext.EXT_PRO"
                    + " left join T_CAMPOS_DESPLEGABLE on e_ext.EXT_TER=T_CAMPOS_DESPLEGABLE.COD_TERCERO and e_ext.EXT_MUN=T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO"
                    + " left join T_CAMPOS_FECHA on  e_ext.EXT_TER=T_CAMPOS_FECHA.COD_TERCERO and e_ext.EXT_MUN=T_CAMPOS_FECHA.COD_MUNICIPIO"
                    + " left join (select TFE_EJE,TFE_NUM,TFE_MUN,TFE_VALOR from e_tfe where TFE_COD='FECHAPRESENTACION') e_tfe on e_exp.EXP_EJE=e_tfe.TFE_EJE and e_exp.EXP_NUM=e_tfe.TFE_NUM and e_exp.EXP_MUN=e_tfe.TFE_MUN"
                    + " LEFT JOIN (SELECT TXT_EJE, TXT_NUM, TXT_MUN, TXT_VALOR FROM e_txt WHERE TXT_COD='CIFCBSC') e_txt ON e_exp.EXP_EJE=e_txt.TXT_EJE AND e_exp.EXP_NUM=e_txt.TXT_NUM AND e_exp.EXP_MUN=e_txt.TXT_MUN"
                    + " left join (select TDE_EJE, TDE_NUM, TDE_MUN, TDE_VALOR from E_TDE where TDE_COD='SOCOMBIESC') E_TDE on e_exp.EXP_EJE=e_tde.TDE_EJE AND e_exp.EXP_NUM=e_tde.TDE_NUM AND e_exp.EXP_MUN=e_tde.TDE_MUN"
                    + " WHERE e_exp.EXP_NUM=? and e_exp.EXP_MUN=? and e_ext.EXT_ROL=? and T_CAMPOS_DESPLEGABLE.COD_CAMPO='TSEXOTERCERO' and T_CAMPOS_FECHA.COD_CAMPO='TFECNACIMIENTO'");
            log.debug("query = " + query);
            log.debug("parametros de la query - EXP_NUM: " + numExpediente);
            log.debug("parametros de la query - EXT_ROL: " + ConstantesMeLanbide69.ROLES.INTERESADO);

            ps = con.prepareStatement(query.toString());
            ps.setString(1, numExpediente);
            ps.setString(2, String.valueOf(codOrg));
            ps.setString(3, String.valueOf(ConstantesMeLanbide69.ROLES.INTERESADO));
            rs = ps.executeQuery();
            if (rs.next()) {
                return (ExpedienteVO) MeLanbide69MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Ha ocurrido un error al obtener los datos del tercero del expediente: " + numExpediente);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return null;
    }

    public DatosEconomicosExpVO getImporteSubvencion(Integer edad, String sexo, String pro, String eje, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        try {
            query = "SELECT SUB_IMPORTE "
                    + "FROM MELANBIDE69_SUBV_COLEC_SEX "
                    + "INNER JOIN MELANBIDE69_COLECTIVO "
                    + "ON MELANBIDE69_COLECTIVO.COL_ID=MELANBIDE69_SUBV_COLEC_SEX.COL_ID "
                    + "AND MELANBIDE69_COLECTIVO.COL_PRO=MELANBIDE69_SUBV_COLEC_SEX.SCS_PRO "
                    + "AND MELANBIDE69_COLECTIVO.COL_EJE=MELANBIDE69_SUBV_COLEC_SEX.SCS_EJE "
                    + "inner join MELANBIDE69_SUBVENCION "
                    + "on MELANBIDE69_SUBVENCION.SUB_ID=MELANBIDE69_SUBV_COLEC_SEX.SUB_ID "
                    + "AND MELANBIDE69_SUBVENCION.SUB_PRO=MELANBIDE69_SUBV_COLEC_SEX.SCS_PRO "
                    + "AND MELANBIDE69_SUBVENCION.SUB_EJE=MELANBIDE69_SUBV_COLEC_SEX.SCS_EJE "
                    + "WHERE SCS_PRO=? AND SCS_EJE=? AND SCS_SEXO=? "
                    + "AND MELANBIDE69_COLECTIVO.COL_EDADI<=? "
                    + "AND (MELANBIDE69_COLECTIVO.COL_EDADF IS NULL "
                    + "OR MELANBIDE69_COLECTIVO.COL_EDADF>=?)";
            log.debug("query = " + query);
            log.debug("parametros de la query - edad: " + edad);
            log.debug("parametros de la query - sexo: " + sexo);

            ps = con.prepareStatement(query);
            ps.setString(1, pro);
            ps.setString(2, eje);
            ps.setString(3, sexo);
            ps.setInt(4, edad);
            ps.setInt(5, edad);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (DatosEconomicosExpVO) MeLanbide69MappingUtils.getInstance().map(rs, DatosEconomicosExpVO.class);
            }
        } catch (Exception ex) {
            log.error("Ha ocurrido un error al obtener el importe de la subvencion: " + ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return null;
    }

    public void insertSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query;
        try {
            query = "INSERT INTO E_TNU (TNU_VALOR,TNU_NUM,TNU_MUN,TNU_COD,TNU_EJE) VALUES(?,?,?,?,?)";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query TNU_VALOR= " + valor);
            log.debug("parametros pasados a la query TNU_COD= " + nombreCampo);

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, valor);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            ps.setString(contbd++, numExpediente.substring(0, 4));

            ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al guardar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void deleteSuplNumerico(int codOrg, String numExpediente, String nombreCampo, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query;
        try {
            query = "DELETE FROM E_TNU where TNU_NUM=? and TNU_MUN=? and TNU_COD=?";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query TNU_COD= " + nombreCampo);

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);

            ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al borrar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void deleteSuplDespl(int codOrg, String numExpediente, String nombreCampo, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query;
        try {
            query = "DELETE FROM E_TDE where TDE_NUM=? and TDE_MUN=? and TDE_COD=?";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query TDE_COD= " + nombreCampo);

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);

            ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al borrar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void insertSuplDespl(int codOrg, String numExpediente, String valor, String nombreCampo, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query;
        try {
            query = "INSERT INTO E_TDE (TDE_VALOR,TDE_NUM,TDE_MUN,TDE_COD,TDE_EJE) VALUES(?,?,?,?,?)";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query TDE_VALOR= " + valor);
            log.debug("parametros pasados a la query TDE_COD= " + nombreCampo);

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, valor);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            ps.setString(contbd++, numExpediente.substring(0, 4));

            ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al guardar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<SocioVO> getListaSocios(String numExpediente, int codOrganizacion, Connection con) throws Exception {
        log.info("Entramos en getListaSocios DAO - " + numExpediente);
        Statement st = null;
        ResultSet rs = null;
        List<SocioVO> listaSocios = new ArrayList<SocioVO>();
        SocioVO socio = new SocioVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConstantesMeLanbide69.getNOMBRE_TABLA_SOCIOS()
                    + " WHERE NUM_EXP='" + numExpediente + "'"
                    + " ORDER BY ID";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                socio = (SocioVO) MeLanbide69MappingUtils.getInstance().map(rs, SocioVO.class);
                listaSocios.add(socio);
                socio = new SocioVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Socios ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaSocios;
    }

    public SocioVO getSocioPorID(String id, Connection con) throws Exception {
        log.info("Entramos en getSocioPorID DAO - " + id);
        Statement st = null;
        ResultSet rs = null;
        SocioVO socio = new SocioVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConstantesMeLanbide69.getNOMBRE_TABLA_SOCIOS()
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                socio = (SocioVO) MeLanbide69MappingUtils.getInstance().map(rs, SocioVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un SOCIO : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return socio;
    }

    public boolean crearSocio(SocioVO nuevoSocio, Connection con) throws Exception {
        log.info("Entramos en crearSocio DAO - " + nuevoSocio.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {
            query = "INSERT INTO " + ConstantesMeLanbide69.getNOMBRE_TABLA_SOCIOS()
                    + " (ID, NUM_EXP, NOMBRE, APELLIDO1, APELLIDO2, DNINIE) VALUES (" + ConstantesMeLanbide69.getNOMBRE_SEQ_SOCIOS() + ".NEXTVAL,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setString(contBD++, nuevoSocio.getNumExp());
            ps.setString(contBD++, nuevoSocio.getNombre());
            ps.setString(contBD++, nuevoSocio.getApellido1());
            ps.setString(contBD++, nuevoSocio.getApellido2());
            ps.setString(contBD++, nuevoSocio.getDni());
            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido guardar un nuevO socio ");
                resultado = false;
            }
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al insertar un nuevo SOCIO" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    public boolean modificarSocio(SocioVO socioModif, Connection con) throws Exception {
        log.info("Entramos en modificarSocio DAO - " + socioModif.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {
            query = "UPDATE " + ConstantesMeLanbide69.getNOMBRE_TABLA_SOCIOS()
                    + " SET NOMBRE=?, APELLIDO1=?, APELLIDO2=?, DNINIE=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setString(contBD++, socioModif.getNombre());
            ps.setString(contBD++, socioModif.getApellido1());
            ps.setString(contBD++, socioModif.getApellido2());
            ps.setString(contBD++, socioModif.getDni());
            ps.setString(contBD++, socioModif.getNumExp());
            ps.setInt(contBD++, socioModif.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido modificar un socio ");
                resultado = false;
            }
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al modificar un SOCIO" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    public int eliminarSocio(String id, Connection con) throws Exception {
        log.info("Entramos en eliminarSocio DAO -  Id: " + id);
        String query = null;
        Statement st = null;
        try {
            query = "DELETE FROM " + ConstantesMeLanbide69.getNOMBRE_TABLA_SOCIOS()
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando SOCIO - ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public Date getFechaFirmaResolucion(int codOrg, String codProc, String numExp, Connection con) throws Exception {
        log.info("getFechaFirmaResolucion - Begin " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        Date fecha = null;
        int tramite = Integer.parseInt(ConstantesMeLanbide69.getPropVal_COD_TRAMITE_NOTIF());
        int ocurrencia = 1;
        try {
            ocurrencia = getMaxOcurrenciaTramite(codOrg, numExp, tramite, con);
            query = "SELECT FX_FIRMA FROM " + ConstantesMeLanbide69.getPropVal_TABLADOCFIR() + " F"
                    + " INNER JOIN " + ConstantesMeLanbide69.getPropVal_TABLADOCS() + " D"
                    + " ON D.crd_pro= F.crd_pro AND D.crd_eje = F.crd_eje  AND D.crd_mun = F.crd_mun AND D.crd_num = F.crd_num AND D.crd_tra = F.crd_tra AND D.crd_ocu = F.crd_ocu AND D.crd_nud = F.crd_nud AND D.crd_fir_est = F.fir_est AND D.crd_des LIKE 'Resol%.pdf' "
                    + " WHERE F.crd_mun = ? AND F.crd_eje = ? AND F.crd_pro = ? AND F.crd_num = ? AND F.crd_tra = ? AND F.crd_ocu = ? AND F.fir_est = 'F'";

            log.debug("sql " + query);
            log.debug("parametros de la query - COD_TRAMITE:  " + tramite);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrg);
            ps.setInt(contbd++, Integer.parseInt(numExp.split("/")[0]));
            ps.setString(contbd++, numExp.split("/")[1]);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, tramite);
            ps.setInt(contbd++, ocurrencia);
            rs = ps.executeQuery();
            if (rs.next()) {
                fecha = rs.getDate("FX_FIRMA");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido una excepcion getFechaFirmaResolucion : " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return fecha;
    }

    
    public Date getFechaEnvioNotificacion(int codOrg, String codProc, String numExp, Connection con) throws Exception {
        log.info("getFechaEnvioNotificacion - Begin " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        Date fecha = null;
        int tramite = Integer.parseInt(ConstantesMeLanbide69.getPropVal_COD_TRAMITE_NOTIF());
        try {
            query = "SELECT fecha_envio FROM " + ConstantesMeLanbide69.getPropVal_TABLANOTIF()
                    + " WHERE cod_municipio=? AND cod_procedimiento=? AND ejercicio=?  AND num_expediente=? AND cod_tramite=? AND ocu_tramite=1";

            log.debug("sql: " + query);
            log.debug("parametros de la query - COD_TRAMITE:  " + tramite);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, numExp.split("/")[1]);
            ps.setInt(contbd++, Integer.parseInt(numExp.split("/")[0]));
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, tramite);
            rs = ps.executeQuery();
            if (rs.next()) {
                fecha = rs.getDate("fecha_envio");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido una excepcion getFechaNotificacion : " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return fecha;
    }

    public Date getFechaAcuse(int codOrg, String codProc, String numExp, Connection con) throws Exception {
        log.info("getFechaAcuse - Begin " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        Date fecha = null;
        int tramite = Integer.parseInt(ConstantesMeLanbide69.getPropVal_COD_TRAMITE_ACUSE());
        try {
            query = "SELECT tfet_valor FROM " + ConstantesMeLanbide69.getPropVal_TABLAFET()
                    + " WHERE tfet_mun=? AND tfet_pro=? AND tfet_eje=?  AND tfet_num=? AND tfet_tra=? AND tfet_ocu=1 AND tfet_cod='FECACUSENOT'";

            log.debug("sql: " + query);
            log.debug("parametros de la query - COD_TRAMITE:  " + tramite);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, numExp.split("/")[1]);
            ps.setInt(contbd++, Integer.parseInt(numExp.split("/")[0]));
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, tramite);
            rs = ps.executeQuery();
            if (rs.next()) {
                fecha = rs.getDate("tfet_valor");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido una excepcion getFechaAcuse : " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return fecha;
    }

    public Date[] getFechasNotificacion(int codOrg, String codProc, String numExp, Connection con) throws Exception {
        log.info("getFechasNotificacion - Begin " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        Date[] fechas = new Date[2];
        int tramite = Integer.parseInt(ConstantesMeLanbide69.getPropVal_COD_TRAMITE_NOTIF());
        int ocurrencia = 1;
        try {
            ocurrencia = getMaxOcurrenciaTramite(codOrg, numExp, tramite, con);
            query = "SELECT FECHA_ENVIO, FECHA_ACUSE FROM " + ConstantesMeLanbide69.getPropVal_TABLANOTIF()
                    + " WHERE cod_municipio=? AND ejercicio=?  AND cod_procedimiento=? AND num_expediente=? AND cod_tramite=? AND ocu_tramite=?";

            log.debug("sql: " + query);
            log.debug("parametros de la query - COD_TRAMITE:  " + tramite);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrg);
            ps.setInt(contbd++, Integer.parseInt(numExp.split("/")[0]));
            ps.setString(contbd++, numExp.split("/")[1]);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, tramite);
            ps.setInt(contbd++, ocurrencia);
            rs = ps.executeQuery();
            if (rs.next()) {
                fechas[0] = rs.getDate("FECHA_ENVIO");
                fechas[1] = rs.getDate("FECHA_ACUSE");
            }
        } catch (SQLException ex) {
            log.error("Ha ocurrido una excepcion getFechasNotificacion : " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return fechas;
    }

    public int getMaxOcurrenciaTramite(int codOrg, String numExp, int tramite, Connection con) throws Exception {
        log.info("getMaxOcurrenciaTramite - ini()");
        Statement st = null;
        String query = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int ocurrencia = 1;
        try {
            query = "SELECT MAX(CRO_OCU) ocurrencia  FROM " + ConstantesMeLanbide69.getPropVal_TABLAOCU()
                    + " WHERE CRO_MUN=? AND CRO_EJE=? AND CRO_PRO=? AND CRO_NUM=? AND CRO_TRA=?";
            log.info("getMaxOcurrenciaTramitexCodigoInterno - sql = " + query);
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, codOrg);
            ps.setInt(contbd++, Integer.parseInt(numExp.split("/")[0]));
            ps.setString(contbd++, numExp.split("/")[1]);
            ps.setString(contbd++, numExp);
            ps.setInt(contbd++, tramite);

            rs = ps.executeQuery();

            while (rs.next()) {
                ocurrencia = rs.getInt("ocurrencia");
            }
        } catch (SQLException ex) {
            log.error("Excepcion - getMaxOcurrenciaTramite : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" getMaxOcurrenciaTramite - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getMaxOcurrenciaTramite - end() - OCU: " + ocurrencia);
        return ocurrencia;
    }

}
