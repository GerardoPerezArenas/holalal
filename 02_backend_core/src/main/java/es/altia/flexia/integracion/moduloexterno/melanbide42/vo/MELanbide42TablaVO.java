/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.vo;

import es.altia.flexia.integracion.moduloexterno.melanbide42.dao.MELanbide42DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42Exception;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Clase para realizar las inserciones en BD del XML de alta de expediente electr�nico.
 * <strong>Todos las filas que se a�adan deben tener exactamente
 * las mismas claves, ya que se usar� una para generar los nombres de los campos
 * en la INSERT sin comprobar las dem�s</strong>
 *
  * @author mikel
 */
public class MELanbide42TablaVO {
    //Logger
    private static final Logger log = LogManager.getLogger(MELanbide42TablaVO.class);
    
    private static int borrame = 1;
    
    private MELanbide42ModuloVO modulo = null;
    private String nombre = null;
    private String pkFieldName = null;
    private String pkSeqName = null;

    private final ArrayList<MELanbide42FilaVO> filas = new ArrayList<MELanbide42FilaVO>();
    
    public MELanbide42TablaVO (String nombre, MELanbide42ModuloVO modulo){
        this.nombre = nombre;
        this.modulo = modulo;
    }
    
    public ArrayList<MELanbide42FilaVO> getFilas(){
        return this.filas;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    public void addFila(MELanbide42FilaVO fila){
        filas.add(fila);
    }
    public void addFila(int num, MELanbide42FilaVO fila){
        filas.add(num, fila);
    }
    
    public ArrayList<MELanbide42FilaVO> getFilasByFieldValue(String field, String value){
        ArrayList<MELanbide42FilaVO> filas = new ArrayList<MELanbide42FilaVO>();
        for (MELanbide42FilaVO f : this.filas){
            String aux = f.getCampos().get(field);
            if (value.equals(aux)){
                filas.add(f);
            }
        }
        
        return filas;
    }
    
    /**
     * No se puede hacer una insert única de múltiples valores porque habría que asegurarse que los campos rellenos
     * en el XML son exactamente los mismos en todas las filas. Si uno viene vacío se fastidia el orden.
     * @return
     * @throws MELanbide42Exception 
     */
    public String[] generarSQLInsercion(AdaptadorSQLBD adaptSQL) throws MELanbide42Exception, SQLException{
        if(log.isDebugEnabled()) log.debug("generarSQLInsercion() : BEGIN");
        String[] result = new String[this.filas.size()];
        int i = 0;
        
        /* No es condición de error. Pueden venir tablas vacías y el tag se genera de todas formas.
        if (filas.isEmpty()) {
            throw new MELanbide42Exception("La tabla no tiene filas añadidas");
        } */
        
        // Se generan las INSERT por cada fila:
        for (MELanbide42FilaVO fila : filas){
            StringBuilder buff = new StringBuilder();
            
            buff.append(generateINSERTSyntax(fila));
            buff.append(generateVALUESSyntax(fila, adaptSQL));
            result[i++] = buff.toString();
        }
        
        return result;
    }
    
    private String generateINSERTSyntax(MELanbide42FilaVO fila){
        StringBuilder buff = new StringBuilder();
        buff.append("INSERT INTO ").append(this.nombre).append(" (");
        
        // Une todos los campos de la fila campo1, campo2... campoN
        Set<String> keySet = fila.getCampos().keySet();
        String[] keyArr = keySet.toArray(new String[keySet.size()]);
        String aux = StringUtils.join(keyArr, ",");
        
        // Hay que mirar si hay una PK autogenerada. La añade al final.
        if (this.getPkFieldName() != null && this.getPkSeqName() != null){
            // Hay PK autogenerada
            buff.append(this.getPkFieldName()).append(",");
        } 

        return buff.append(aux).append(") ").toString();
    }

    private String generateVALUESSyntax(MELanbide42FilaVO fila, AdaptadorSQLBD adaptSQL) throws SQLException{
        StringBuilder buff = new StringBuilder();
        buff.append(" VALUES (");
        
        Set<String> keySet = fila.getCampos().keySet();
        String[] aux = new String[keySet.size()];
        int i = 0;
        
        // for del keySet para garantizar orden con respecto a generateINSERTSyntax
        // Saco todos los valores de los campos
        for (String k : keySet){
            aux[i++] = "'" + fila.getCampos().get(k) + "'";
        }
        
        // Hay que mirar si hay una PK autogenerada:
        if (this.getPkFieldName() != null && this.getPkSeqName() != null){
            // Hay PK autogenerada
            MELanbide42DAO dao = new MELanbide42DAO();
            String pk = null;
            Connection con = null;
            try {
                try {
//                if (5 == 5){
//                    log.error("SE ESTA PONIENDO NEXT VAL A PELO");
//                    pk = String.valueOf(borrame++);
//                } else {
                    con = adaptSQL.getConnection();
                    pk = dao.getSeqNextValue(this.getPkSeqName(), con);
//                }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(MELanbide42TablaVO.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (MELanbide42Exception me){
                log.error("Error al obtener valor de secuencia " + this.getPkSeqName(), me);
                log.error("Queda valor null para forzar fallo en la INSERT");
            } catch (BDException be){
                log.error("Error en BD al obtener valor de secuencia " + this.getPkSeqName(), be);
                log.error("Queda valor null para forzar fallo en la INSERT");
            }finally{
                try {
                    adaptSQL.devolverConexion(con);
                } catch (Exception ex) {
                    log.error("Error al cerrar la conexion " + ex.getMessage(),ex);
                }
            }
            buff.append(pk).append(",");
        } 
        
        buff.append(StringUtils.join(aux, ","));
        return buff.append(") ").toString();
    }

    public String getPkFieldName() {
        return pkFieldName;
    }

    public void setPkFieldName(String pkFieldName) {
        this.pkFieldName = pkFieldName;
    }

    public String getPkSeqName() {
        return pkSeqName;
    }

    public void setPkSeqName(String pkSeqName) {
        this.pkSeqName = pkSeqName;
    }

    public MELanbide42ModuloVO getModulo(){
        return this.modulo;
    }
}
