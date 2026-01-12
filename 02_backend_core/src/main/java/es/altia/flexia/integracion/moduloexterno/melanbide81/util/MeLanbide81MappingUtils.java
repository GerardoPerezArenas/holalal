package es.altia.flexia.integracion.moduloexterno.melanbide81.util;

import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ProyectoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.PuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo2VO;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide81MappingUtils {

    private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    public ProyectoVO mapearProyecto(ResultSet rs) throws SQLException {
        ProyectoVO objeto = new ProyectoVO();
        objeto.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            objeto.setId(null);
        }
        objeto.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        objeto.setPrioridad(rs.getInt("PRIORIDAD") != 0 ? rs.getInt("PRIORIDAD") : null);
        objeto.setDenominacion(rs.getString("DENOM") != null && !rs.getString("DENOM").isEmpty() ? rs.getString("DENOM") : "");
        objeto.setEntidad(rs.getString("ENTIDAD") != null && !rs.getString("ENTIDAD").isEmpty() ? rs.getString("ENTIDAD") : "");
        objeto.setTipoProyecto(rs.getString("TIPO") != null && !rs.getString("TIPO").isEmpty() ? rs.getString("TIPO") : "");
        objeto.setFases(rs.getInt("NUMFASES") != 0 ? rs.getInt("NUMFASES") : null);
        return objeto;
    }

    public PuestoVO mapearPuesto(ResultSet rs) throws SQLException {
        PuestoVO objeto = new PuestoVO();
        objeto.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            objeto.setId(null);
        }
        objeto.setIdProyecto(rs.getInt("ID_PROYECTO") != 0 ? rs.getInt("ID_PROYECTO") : null);
        objeto.setIdPrioridadProyecto(rs.getInt("ID_PRIORIDAD_PROYECTO") != 0 ? rs.getInt("ID_PRIORIDAD_PROYECTO") : null);
        objeto.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        objeto.setDenominacion(rs.getString("DENOMPUESTO") != null && !rs.getString("DENOMPUESTO").isEmpty() ? rs.getString("DENOMPUESTO") : "");
        objeto.setDuracion(rs.getDouble("DURESTIM") != 0 ? rs.getDouble("DURESTIM") : null);
        objeto.setPorcJorn(rs.getDouble("PORCJORN") != 0 ? rs.getDouble("PORCJORN") : null);
        objeto.setNumContratos(rs.getInt("NUMCONTRATOS") != 0 ? rs.getInt("NUMCONTRATOS") : null);
        objeto.setCoste(rs.getDouble("COSTEESTIMADO") != 0 ? rs.getDouble("COSTEESTIMADO") : null);
        objeto.setSubvencion(rs.getDouble("SUBVENSOLIC") != 0 ? rs.getDouble("SUBVENSOLIC") : null);
        return objeto;
    }

    public ContratacionVO mapearContratacion(ResultSet rs) throws SQLException {
        ContratacionVO objeto = new ContratacionVO();
        objeto.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            objeto.setId(null);
        }
        objeto.setIdProyecto(rs.getInt("ID_PROYECTO") != 0 ? rs.getInt("ID_PROYECTO") : null);
        objeto.setIdPrioridadProyecto(rs.getInt("ID_PRIORIDAD_PROYECTO") != 0 ? rs.getInt("ID_PRIORIDAD_PROYECTO") : null);
        objeto.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        objeto.setTipoDesempleado(rs.getString("TIPOPERSDESEM") != null && !rs.getString("TIPOPERSDESEM").isEmpty() ? rs.getString("TIPOPERSDESEM") : "");
        objeto.setDuracion(rs.getDouble("DURACESTIMADAA") != 0 ? rs.getDouble("DURACESTIMADAA") : null);
        objeto.setSexo(rs.getString("SEXO") != null && !rs.getString("SEXO").isEmpty() ? rs.getString("SEXO") : "");
        objeto.setPorcJorn(rs.getDouble("PORCJORN") != 0 ? rs.getDouble("PORCJORN") : null);
        objeto.setNumContratos(rs.getInt("NUMCONTRPREV") != 0 ? rs.getInt("NUMCONTRPREV") : null);
        objeto.setSubvencion(rs.getDouble("SUBVENSOLIC") != 0 ? rs.getDouble("SUBVENSOLIC") : null);
        return objeto;
    }

    public Tipo1VO mapearTipo1(ResultSet rs) throws SQLException {
        Tipo1VO objeto = new Tipo1VO();
        objeto.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            objeto.setId(null);
        }
        objeto.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        objeto.setEntbene(rs.getString("ENTBENE") != null && !rs.getString("ENTBENE").isEmpty() ? rs.getString("ENTBENE") : "");
        objeto.setEntcontra(rs.getString("ENTCONTRA") != null && !rs.getString("ENTCONTRA").isEmpty() ? rs.getString("ENTCONTRA") : "");
        objeto.setCif(rs.getString("CIF") != null && !rs.getString("CIF").isEmpty() ? rs.getString("CIF") : "");
        objeto.setCcc(rs.getString("CCC") != null && !rs.getString("CCC").isEmpty() ? rs.getString("CCC") : "");
        objeto.setDenomproy(rs.getString("DENOMPROY") != null && !rs.getString("DENOMPROY").isEmpty() ? rs.getString("DENOMPROY") : "");
        objeto.setDenompuesto(rs.getString("DENOMPUESTO") != null && !rs.getString("DENOMPUESTO").isEmpty() ? rs.getString("DENOMPUESTO") : "");
        objeto.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").isEmpty() ? rs.getString("NOMBRE") : "");
        objeto.setApellido1(rs.getString("APELLIDO1") != null && !rs.getString("APELLIDO1").isEmpty() ? rs.getString("APELLIDO1") : "");
        objeto.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        objeto.setDni(rs.getString("DNI") != null && !rs.getString("DNI").isEmpty() ? rs.getString("DNI") : "");
        objeto.setNaf(rs.getString("NAF") != null && !rs.getString("NAF").isEmpty() ? rs.getString("NAF") : "");
        objeto.setFecnacimiento(rs.getDate("FECNACIMIENTO"));
        if (objeto.getFecnacimiento() != null) {
            objeto.setFecnacimientoStr(formatoFecha.format(objeto.getFecnacimiento()));
        }
        objeto.setFecnacimiento(rs.getDate("FECNACIMIENTO"));

        objeto.setSexo(rs.getString("SEXO") != null && !rs.getString("SEXO").isEmpty() ? rs.getString("SEXO") : "");
        objeto.setGrupocot(rs.getString("GRUPOCOT") != null && !rs.getString("GRUPOCOT").isEmpty() ? rs.getString("GRUPOCOT") : "");
        objeto.setFecinicio(rs.getDate("FECINICIO"));
        if (objeto.getFecinicio() != null) {
            objeto.setFecIniStr(formatoFecha.format(objeto.getFecinicio()));
        }
        objeto.setFecinicio(rs.getDate("FECINICIO"));
        objeto.setFecfin(rs.getDate("FECFIN"));
        if (objeto.getFecfin() != null) {
            objeto.setFecfinStr(formatoFecha.format(objeto.getFecfin()));
        }
        objeto.setFecfin(rs.getDate("FECFIN"));
        objeto.setPorcJorn(rs.getDouble("PORCJORN") != 0 ? rs.getDouble("PORCJORN") : null);
        objeto.setDurcontrato(rs.getString("DURCONTRATO") != null && !rs.getString("DURCONTRATO").isEmpty() ? rs.getString("DURCONTRATO") : "");
     //   objeto.setDurcontrato(rs.getString("DURCONTRATO")!= null && !rs.getString("DURCONTRATO") );
        objeto.setEdad(rs.getInt("EDAD") != 0 ? rs.getInt("EDAD") : null);
        objeto.setMunicipio(rs.getString("MUNICIPIO") != null && !rs.getString("MUNICIPIO").isEmpty() ? rs.getString("MUNICIPIO") : "");
        objeto.setCostesal(rs.getDouble("COSTESAL") != 0 ? rs.getDouble("COSTESAL") : null);
        objeto.setCostess(rs.getDouble("COSTESS") != 0 ? rs.getDouble("COSTESS") : null);
        objeto.setCostetotal(rs.getDouble("COSTETOTAL") != 0 ? rs.getDouble("COSTETOTAL") : null);
        objeto.setInscrita(rs.getString("INSCRITA") != null && !rs.getString("INSCRITA").isEmpty() ? rs.getString("INSCRITA") : "");
        objeto.setCertinter(rs.getString("CERTINTER") != null && !rs.getString("CERTINTER").isEmpty() ? rs.getString("CERTINTER") : "");
        objeto.setSubconcedida(rs.getDouble("SUBCONCEDIDA") != 0 ? rs.getDouble("SUBCONCEDIDA") : null);
        objeto.setPago1(rs.getDouble("PAGO1") != 0 ? rs.getDouble("PAGO1") : null);
        objeto.setSubliquidada(rs.getDouble("SUBLIQUIDADA") != 0 ? rs.getDouble("SUBLIQUIDADA") : null);
        objeto.setPago2(rs.getDouble("PAGO2") != 0 ? rs.getDouble("PAGO2") : null);
        objeto.setObservaciones(rs.getString("OBSERVACIONES") != null && !rs.getString("OBSERVACIONES").isEmpty() ? rs.getString("OBSERVACIONES") : "");
        return objeto;
    }

    public Tipo2VO mapearTipo2(ResultSet rs) throws SQLException {
        Tipo2VO objeto = new Tipo2VO();
        objeto.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            objeto.setId(null);
        }
        objeto.setNumExp(rs.getString("NUM_EXP") != null && !rs.getString("NUM_EXP").isEmpty() ? rs.getString("NUM_EXP") : "");
        objeto.setEntbene(rs.getString("ENTBENE") != null && !rs.getString("ENTBENE").isEmpty() ? rs.getString("ENTBENE") : "");
        objeto.setEmpcontra(rs.getString("EMPCONTRA") != null && !rs.getString("EMPCONTRA").isEmpty() ? rs.getString("EMPCONTRA") : "");
        objeto.setCif(rs.getString("CIF") != null && !rs.getString("CIF").isEmpty() ? rs.getString("CIF") : "");
        objeto.setCcc(rs.getString("CCC") != null && !rs.getString("CCC").isEmpty() ? rs.getString("CCC") : "");
        objeto.setNombre(rs.getString("NOMBRE") != null && !rs.getString("NOMBRE").isEmpty() ? rs.getString("NOMBRE") : "");
        objeto.setApellido1(rs.getString("APELLIDO1") != null && !rs.getString("APELLIDO1").isEmpty() ? rs.getString("APELLIDO1") : "");
        objeto.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        objeto.setDni(rs.getString("DNI") != null && !rs.getString("DNI").isEmpty() ? rs.getString("DNI") : "");
        objeto.setNaf(rs.getString("NAF") != null && !rs.getString("NAF").isEmpty() ? rs.getString("NAF") : "");
        objeto.setFecnacimiento(rs.getDate("FECNACIMIENTO"));
        if (objeto.getFecnacimiento() != null) {
            objeto.setFecnacimientoStr(formatoFecha.format(objeto.getFecnacimiento()));
        }
        objeto.setFecnacimiento(rs.getDate("FECNACIMIENTO"));
        objeto.setSexo(rs.getString("SEXO") != null && !rs.getString("SEXO").isEmpty() ? rs.getString("SEXO") : "");
        objeto.setGrupocot(rs.getString("GRUPOCOT") != null && !rs.getString("GRUPOCOT").isEmpty() ? rs.getString("GRUPOCOT") : "");
        objeto.setFecinicio(rs.getDate("FECINICIO"));
        if (objeto.getFecinicio() != null) {
            objeto.setFecIniStr(formatoFecha.format(objeto.getFecinicio()));
        }
        objeto.setFecinicio(rs.getDate("FECINICIO"));                   
        
        objeto.setFecfin(rs.getDate("FECFIN"));
        if (objeto.getFecfin() != null) {
            objeto.setFecfinStr(formatoFecha.format(objeto.getFecfin()));
        }
        objeto.setFecfin(rs.getDate("FECFIN"));

        objeto.setPorcJorn(rs.getDouble("PORCJORN") != 0 ? rs.getDouble("PORCJORN") : null);
        objeto.setDurcontrato(rs.getString("DURCONTRATO") != null && !rs.getString("DURCONTRATO").isEmpty() ? rs.getString("DURCONTRATO") : "");
        objeto.setEdad(rs.getInt("EDAD") != 0 ? rs.getInt("EDAD") : null);
        objeto.setMunicipio(rs.getString("MUNICIPIO") != null && !rs.getString("MUNICIPIO").isEmpty() ? rs.getString("MUNICIPIO") : "");
        objeto.setCostesal(rs.getDouble("COSTESAL") != 0 ? rs.getDouble("COSTESAL") : null);
        objeto.setCostess(rs.getDouble("COSTESS") != 0 ? rs.getDouble("COSTESS") : null);
        objeto.setCostetotal(rs.getDouble("COSTETOTAL") != 0 ? rs.getDouble("COSTETOTAL") : null);
        objeto.setInscrita(rs.getString("INSCRITA") != null && !rs.getString("INSCRITA").isEmpty() ? rs.getString("INSCRITA") : "");
        objeto.setCertinter(rs.getString("CERTINTER") != null && !rs.getString("CERTINTER").isEmpty() ? rs.getString("CERTINTER") : "");
        objeto.setSubconcedida(rs.getDouble("SUBCONCEDIDA") != 0 ? rs.getDouble("SUBCONCEDIDA") : null);
        objeto.setPago1(rs.getDouble("PAGO1") != 0 ? rs.getDouble("PAGO1") : null);
        objeto.setSubliquidada(rs.getDouble("SUBLIQUIDADA") != 0 ? rs.getDouble("SUBLIQUIDADA") : null);
        objeto.setPago2(rs.getDouble("PAGO2") != 0 ? rs.getDouble("PAGO2") : null);
        objeto.setObservaciones(rs.getString("OBSERVACIONES") != null && !rs.getString("OBSERVACIONES").isEmpty() ? rs.getString("OBSERVACIONES") : "");
        objeto.setTipocontrato(rs.getString("TIPOCONTRATO") != null && !rs.getString("TIPOCONTRATO").isEmpty() ? rs.getString("TIPOCONTRATO") : "");
        objeto.setColectivo(rs.getString("COLECTIVO") != null && !rs.getString("COLECTIVO").isEmpty() ? rs.getString("COLECTIVO") : "");
        return objeto;
    }

    public DesplegableVO mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableVO valoresDesplegable = new DesplegableVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }
}
