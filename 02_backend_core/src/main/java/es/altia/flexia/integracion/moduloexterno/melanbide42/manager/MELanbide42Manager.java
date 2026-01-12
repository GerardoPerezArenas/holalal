/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.manager;

import com.aeat.valida.Validador;
import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide42.dao.MELanbide42DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.ConstantesMeLanbide42;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42CalculosTablas;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42GeneralUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42Properties;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42XMLParser;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42FilaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42ModuloVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42TablaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42XMLAltaExpediente;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author mikel
 */
public class MELanbide42Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MELanbide42Manager.class);
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    public MELanbide42XMLAltaExpediente parsearExpedienteElectronico(String xml) throws MELanbide42Exception {
        // Parsea el XML
        MELanbide42XMLParser parser = new MELanbide42XMLParser();
        return parser.parseXML(xml);
    }

    public void insertarExpedienteElectronico(MELanbide42XMLAltaExpediente expediente, AdaptadorSQLBD adaptSQL, String numExp, String codOrg) throws MELanbide42Exception, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("insertarExpedienteElectronico() : BEGIN");
        }
        try {
            boolean borrar = true;
            ArrayList<MELanbide42ModuloVO> modulos = expediente.getListaModulos();
            ArrayList<MELanbide42Exception> erroresInsercion = new ArrayList<MELanbide42Exception>();

            for (MELanbide42ModuloVO m : modulos) {
                for (MELanbide42TablaVO t : m.getTablas()) {
                    // Añade la generación de PK+SEQ a la tabla
                    if (!t.getNombre().equals("MELANBIDE01_PERIODO")
                            && !t.getNombre().equals("MELANBIDE01_DATOS_CALCULO")//&&
                            /*!t.getNombre().equals("S75PUESTOS")*/) {
                        this.anadirPK(t);

                        //si viene numexp en xml comparar numexp del xml con el que viene por parametro
                        for (MELanbide42FilaVO f : t.getFilas()) {
                            Set<String> keySet = f.getCampos().keySet();
                            // for del keySet para garantizar orden con respecto a generateINSERTSyntax
                            // Saco todos los valore+ de los campos
                            boolean existeNumExp = false;
                            boolean coincideExp = false;
                            for (String k : keySet) {
                                if (("NUM_EXP").equals(k)) {
                                    existeNumExp = true;
                                    log.debug("num_exp xml:" + f.getCampos().get(k));
                                    if (numExp.equals(f.getCampos().get(k))) {
                                        coincideExp = true;
                                    }
                                }
                            }
                            if (!existeNumExp || (existeNumExp && coincideExp)) {
                                this.anadirNumExp(t, numExp);
                            } else {
                                //abortar proceso
                                log.error("ERRRORRRRRR>>>>>>El expedienete no coincide");
                                throw new MELanbide42Exception("Error al comprobar numexp " + numExp + ". No coincide.");

                            }
                        }
                        // Hay que hacer una excepcion en este punto. 
                        /* Para ORI14. Cuando no es una asociacion, no se puede borrar los datos de la tabla ORI14_ASOCIACION
                         Porque primero se guardan alguno datos de la entidad principal en esta tabla y luego la lista de las entidades que la conforman
                         eso quiere decir que hay dos inserciones en la misma tabla dentro del mismo modulo
                         Si no hay entidaddes en la lista  viene el nodo vacio  <Tabla name="ORI14_ASOCIACION"/>
                         pero entra en esta ciclo, no hace inserciones al no tener filas pero borra lo guardado anteriormente en la parte de la entidad principal
                         
                         Creamos variable por defecto que borre, si es este caso que no.
                         */
                        // Si es esta tabla y no tiene filas que no borre los datos de la entidad principal guardados anteriormente.
                        if (t.getNombre().equals("ORI14_ASOCIACION") && t.getFilas().size() < 1) {
                            borrar = false;
                        }
                        /* #802119 SEI - Sustituciones OTRA EXCEPCION
                        Al ser una aportación de datos que se va a realizar varias veces en cada expediente y se deben conservar los datos anteriores hay que añadir una excepción en el método para no borrar los datos de S75_SUSTITUCIONES
                         */
                        if (t.getNombre().equals("S75SUSTITUCIONES")) {
                            log.info("Es una sustitución de SEI. No se borra la tabla " + t.getNombre());
                            borrar = false;
                        } else if (t.getNombre().equals("MELANBIDE14_OPE_PRESENTADAS")) { // #803703 PFSE - Financiacion Anual
                            log.info("Es una aportación de OPERACIONES de PFSE. No se borra la tabla " + t.getNombre());
                            borrar = false;
                        } else if (t.getNombre().equals("MELANBIDE15_IDENTIDAD") || t.getNombre().equals("MELANBIDE15_FORMACION") || t.getNombre().equals("MELANBIDE15_ORIENTACION") || t.getNombre().equals("MELANBIDE15_CONTRATACION")) {
                            log.info("Es una aportación de CATP. No se borra la tabla " + t.getNombre());
                            borrar = false;
                        } else if (t.getNombre().equals("ECA23_JUS_INSERCIONES") || t.getNombre().equals("ECA23_JUS_PREPARADORES") || t.getNombre().equals("ECA23_JUS_SEGUIMIENTOS")) { //#854473 - justificacion ECA 23
                            log.info("Es una justificación de ECA. No se borra la tabla " + t.getNombre());
                            borrar = false;
                        }
                        if (borrar) {
                            borrarDatosTabla(t.getModulo().getCodigo(), t.getNombre(), numExp, codOrg);
                        }
                        borrar = true; // Dejamos valor por defecto en caso de que los haya cambiado anteriormente 

                        /*int resultado = this.anadirNumExp(t, numExp);   
                        if (resultado<0)                            
                         throw new MELanbide42Exception("Error al al comprobar numexp");*/
                    }
                    // Realiza la insert en BD
                    String[] sentencias = null;
                    try {
                        sentencias = t.generarSQLInsercion(adaptSQL);
                    } catch (MELanbide42Exception mle) {
                        log.error("Error al generar las SQL de inserción", mle);
                        erroresInsercion.add(new MELanbide42Exception("Error al generar las SQL de inserción", mle));
                    }
                    for (String s : sentencias) {
                        try {
                            this.realizarInsertSQL(s, adaptSQL);
                        } catch (MELanbide42Exception me) {
                            log.error("Error al grabar tabla " + t.getNombre() + " del módulo " + m.getCodigo(), me);
                            erroresInsercion.add(new MELanbide42Exception("Error al grabar tabla " + t.getNombre() + " del módulo " + m.getCodigo(), me));
                            throw me;
                        }
                    }
                }
            }
        } catch (SQLException mle) {
            if (!mle.getMessage().contains("Error al comprobar numexp")) {
                // TODO GESTION ERRORES
                log.error("Error en insertarExpedienteElectronico", mle);
                String error = "Error en la funcion insertarExpedienteElectronico: " + mle.toString();
                ErrorBean errorB = new ErrorBean();
                errorB.setIdError("TELEMATICO_03");
                errorB.setMensajeError("Error en la funcion insertarExpedienteElectronico" + mle.getMessage() + mle.getErrorCode() + " - " + mle.getSQLState());
                errorB.setSituacion("insertarExpedienteElectronico");

                MELanbide42Manager.grabarError(errorB, error, error, numExp);
            }
            throw mle;
        }
        if (log.isDebugEnabled()) {
            log.debug("insertarExpedienteElectronico() : END");
        }
    }

    private void anadirPK(MELanbide42TablaVO tabla) {
        if (log.isDebugEnabled()) {
            log.debug("anadirPK() : BEGIN");
        }
        // Añade la clave primaria que falta en la tabla:
        String aux = tabla.getModulo().getCodigo() + ConstantesDatos.BARRA + tabla.getNombre() + "/pkname";
        String pk = MELanbide42Properties.getProperty(aux);
        if (log.isDebugEnabled()) {
            log.debug("Propiedad/PK: " + aux + ConstantesDatos.BARRA + pk);
        }

        // Añade la secuencia que da valor a la PK
        aux = tabla.getModulo().getCodigo() + ConstantesDatos.BARRA + tabla.getNombre() + "/seqname";
        String seq = MELanbide42Properties.getProperty(aux);
        if (log.isDebugEnabled()) {
            log.debug("Propiedad/SEQ: " + aux + ConstantesDatos.BARRA + seq);
        }

        if (pk != null && seq != null) {
            tabla.setPkFieldName(pk);
            tabla.setPkSeqName(seq);
        }
        if (log.isDebugEnabled()) {
            log.debug("anadirPK() : END");
        }
    }

    private void anadirPK_S75PUESTOS(MELanbide42TablaVO tabla) {
        if (log.isDebugEnabled()) {
            log.debug("anadirPK_S75PUESTOS() : BEGIN");
        }
        // Añade la clave primaria que falta en la tabla:
        String aux = tabla.getModulo().getCodigo() + ConstantesDatos.BARRA + tabla.getNombre() + "/pkname";
        String pk = MELanbide42Properties.getProperty(aux);
        if (log.isDebugEnabled()) {
            log.debug("Propiedad/PK: " + aux + ConstantesDatos.BARRA + pk);
        }

        // Añade la secuencia que da valor a la PK
        aux = tabla.getModulo().getCodigo() + ConstantesDatos.BARRA + tabla.getNombre() + "/seqname";
        String seq = MELanbide42Properties.getProperty(aux);
        if (log.isDebugEnabled()) {
            log.debug("Propiedad/SEQ: " + aux + ConstantesDatos.BARRA + seq);
        }

        if (pk != null && seq != null) {
            tabla.setPkFieldName(pk);
            tabla.setPkSeqName(seq);
        }
        if (log.isDebugEnabled()) {
            log.debug("anadirPK_S75PUESTOS() : END");
        }
    }

    private void anadirNumExp(MELanbide42TablaVO tabla, String numExp) {
        if (log.isDebugEnabled()) {
            log.debug("anadirNumExp() : BEGIN");
        }
        // Añade la clave primaria que falta en la tabla:
        String aux = tabla.getModulo().getCodigo() + ConstantesDatos.BARRA + tabla.getNombre() + "/numexpfield";
        String numExpField = MELanbide42Properties.getProperty(aux);

        // Añade el numero del expediente a todas las filas de la tabla
        if (aux != null) {
            for (MELanbide42FilaVO f : tabla.getFilas()) {
                f.addCampo(numExpField, numExp);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("anadirNumExp() : END");
        }
    }

    public void anadirCamposMelanbide33(MELanbide42XMLAltaExpediente expediente, String numExp, String codOrg) {
        Connection con = null;
        try {
            con = getAdaptSQLBD(codOrg).getConnection();
            ArrayList<MELanbide42ModuloVO> modulos = expediente.getListaModulos();
            ArrayList<MELanbide42Exception> erroresInsercion = new ArrayList<MELanbide42Exception>();
            Map<String, String[]> mapFecSubvencionPorceJorXNroContrato = new HashMap<String, String[]>();

            for (MELanbide42ModuloVO m : modulos) {
                for (MELanbide42TablaVO t : m.getTablas()) {
                    if (log.isDebugEnabled()) {
                        log.debug("anadirCamposMelanbide33 : BEGIN");
                    }
                    String ejercicio = "";
                    String proc = "";
                    String[] num = numExp.split(ConstantesDatos.BARRA);
                    ejercicio = num[0];
                    int cod = 12;
                    boolean primera = true;
                    proc = num[1];
                    //S75PUESTOS
                    if (t.getNombre().equals("S75PUESTOS")) {
                        log.info("S75PUESTOS");
                        for (MELanbide42FilaVO f : t.getFilas()) {
                            f.addCampo("PST_MUN", codOrg);
                            f.addCampo("PST_PRO", proc);
                            f.addCampo("PST_EJE", ejercicio);
                            String valor = f.getCampos().get("NUM_ORDEN_INSERCION");
                            f.removeCampo("NUM_ORDEN_INSERCION");
                            log.debug("valor: " + valor);
                            log.debug("codOrg: " + codOrg);
//                            if (primera){
//                                String id = MELanbide42DAO.getInstance().recogerId_S75PUESTOS(numExp, valor, getAdaptSQLBD(codOrg).getConnection());
//                                cod = Integer.parseInt(id);
//                                primera = false;
//                            }else cod ++;
                            f.addCampo("PST_NUMCON", valor);
                            log.info("Creamos Map con fecha subvencion / porcentaje Jornada");
                            try {
                                String[] datosFecSubvPorcJor = new String[]{f.getCampos().get("PST_FCONTR"), f.getCampos().get("PST_PORJOR")};
                                mapFecSubvencionPorceJorXNroContrato.put(f.getCampos().get("PST_NUMCON"), datosFecSubvPorcJor);
                            } catch (Exception e) {
                                log.error("No se ha podido cargar el map con datos de fecha subvencion/porcentaje joranda para cargar S75JORNPST Si viene sin datos. - El proceso de carga continua. MesnajeError " + e.getMessage(), e);
                            }
                            // Calculamos la fehca fin subvencion
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            //           Date fehcaFinSubvencio = cacula3AnosMenos1DiaFechaFinSubvSEI(f.getCampos().get("PST_FCONTR"));
                            Date fechaFinSubvencion = calcularFechaFinSubvSEI(formato.parse(f.getCampos().get("PST_FCONTR")));

                            if (fechaFinSubvencion != null) {
                                f.addCampo("PST_FBAJA", formato.format(fechaFinSubvencion));
                            } else {
                                log.info("No se ha podido calcular la fecha fin subvención " + numExp);
                            }
                            valor = f.getCampos().get("PST_SEXO");
                            if (valor != null && !valor.isEmpty()) {
                                valor = f.getCampos().get("PST_SEXO");
                                if (valor.equals("1")) {
                                    f.addCampo("PST_SEXO", "H");
                                } else if (valor.equals("2")) {
                                    f.addCampo("PST_SEXO", "M");
                                }
                            }
                            String valorDoc = f.getCampos().get("PST_DNICIF");
                            if (valorDoc != null && !valorDoc.isEmpty()) {
                                Validador validador = new Validador();
                                int result = validador.checkNif(valorDoc);
                                int tipoDoc = 5;
                                if (result == Validador.NIF_OK) {
                                    tipoDoc = 1;
                                } else if (result == Validador.CIF_OK) {
                                    tipoDoc = 4;
                                } else if (result == Validador.NIF_EXTRANJEROS) {
                                    tipoDoc = 2;
                                }
                                f.addCampo("PST_TIPDNICIF", String.valueOf(tipoDoc));
                            }
                            valor = f.getCampos().get("PST_FECNAC");
                            if (valorDoc != null && !valorDoc.isEmpty()) {
                                log.info("No se ha informado la fecha de nacimiento en el XML para Documento " + valorDoc);
                                // Recuperamos los datos del terceropara ver si tiene fecha de nacimiento registrada en Regexlan
                                String fechaNacimientoxDocumento = MELanbide42DAO.getInstance().getTerceroFechaNacimientoDAO(valorDoc, con);
                                f.addCampo("PST_FECNAC", fechaNacimientoxDocumento);
                                log.info("Agregamos fecha de nacimiento en el XML : " + fechaNacimientoxDocumento);
                            }
                        }
                    } //S75CONCEPTOS
                    else if (t.getNombre().equals("S75CONCEPTOS")) {
                        log.info("S75CONCEPTOS");
                        primera = true;
                        for (MELanbide42FilaVO f : t.getFilas()) {
                            f.addCampo("CNP_MUN", codOrg);
                            f.addCampo("CNP_PRO", proc);
                            f.addCampo("CNP_EJE", ejercicio);
                            String valor = f.getCampos().get("CNP_CONCEP");

                            //f.removeCampo("NUM_ORDEN_INSERCION");
                            log.debug("valor: " + valor);
                            log.debug("codOrg: " + codOrg);
                            if (primera) {
                                String id = MELanbide42DAO.getInstance().recogerId_S75CONCEPTOS(numExp, valor, con);
                                cod = Integer.parseInt(id);
                                primera = false;
                            } else {
                                cod++;
                            }
                            f.addCampo("CNP_NUMCON", String.valueOf(cod));
                            // 2019 Campos pasan a ser no obligatorios. Mientras controlamos null en S75 JBossSeam 
                            // En formacion cuatia de Desplazamiento
                            String valorDesplazamiento = f.getCampos().get("CNP_CUANDES");
                            log.info("valorConcepto/valorDesplazamiento :" + valor + ConstantesDatos.BARRA + valorDesplazamiento);
                            if (valorDesplazamiento == null || "".equals(valorDesplazamiento)) {
                                // Asignamos Valor 0
                                f.addCampo("CNP_CUANDES", "0");
                            }
                            String valorCac = f.getCampos().get("CNP_CAC");
                            if (valorCac == null || valorCac.isEmpty() || valorCac.equalsIgnoreCase("null")) {
                                // Sino lo envian, lo calculamos en funcion de tipo de concepto. Mismo criterios que s75.properties3
                                if (valor.equalsIgnoreCase("2")) { // 2 ACTIVOS FIJOS 
                                    valorCac = MELanbide42Properties.getProperty("CAC_752");
                                } else {
                                    valorCac = MELanbide42Properties.getProperty("CAC_452");
                                }
                                f.addCampo("CNP_CAC", valorCac);
                            }
                            log.info("valorCac : " + valorCac);
                        }
                    } //S75JORNPST
                    else if (t.getNombre().equals("S75JORNPST")) {
                        log.info("S75JORNPST");
                        for (MELanbide42FilaVO f : t.getFilas()) {
                            f.addCampo("JRN_MUN", codOrg);
                            f.addCampo("JRN_PRO", proc);
                            f.addCampo("JRN_EJE", ejercicio);
                            f.addCampo("JRN_JORCOR", "1");
                            String valor = f.getCampos().get("NUM_ORDEN_INSERCION");
                            f.removeCampo("NUM_ORDEN_INSERCION");
                            String fechaD = f.getCampos().get("JRN_FDESDE");
                            String valorCodigoConcepto = f.getCampos().get("JRN_CONCEP");
                            String fechaContratoS75_PUESTOS = "";
                            String porcJornadaContraS75_PUESTOS = "";
                            try {
                                String[] datosPuesto = mapFecSubvencionPorceJorXNroContrato.get(valor);
                                if (datosPuesto != null && datosPuesto.length > 1) { // Aseguramos que vengan los dos datos
                                    fechaContratoS75_PUESTOS = datosPuesto[0];
                                    log.info("fechaContratoS75_PUESTOS : " + fechaContratoS75_PUESTOS);
                                    log.info("fechaD (XML de S75JORNPST) : " + fechaD);
                                    if (valorCodigoConcepto.equalsIgnoreCase("3")) {
                                        fechaD = fechaContratoS75_PUESTOS;
                                        log.info("Asiganamos Fecha Fin Subvencion Para calculo en la tabla S75JORNPST Asignamos lo que viene en S75PUESTOS");
                                    } else if (fechaD == null || "".equals(fechaD)) {
                                        fechaD = fechaContratoS75_PUESTOS;
                                        log.info("No viene fecha Desde Para calculo en la tabla S75JORNPST Asignamos lo que viene en S75PUESTOS");
                                    }
                                    //Actualizamos si es necesario en XML
                                    f.removeCampo("JRN_FDESDE");
                                    f.addCampo("JRN_FDESDE", fechaD);
                                    log.info("f.addCampo(JRN_FDESDE, " + fechaD + ");");
                                    //}
                                    porcJornadaContraS75_PUESTOS = datosPuesto[1];
                                    log.info("porcJornadaContraS75_PUESTOS : " + porcJornadaContraS75_PUESTOS);
                                }
                            } catch (Exception e) {
                                log.error("Error al leer los datos de FechaSubvencion/PorcentJornada de la tabla S75_PUESTOS en el wile de la tabla S75_JORNPST", e);
                            }
                            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                            Date fdesde = null;
                            Date fhasta = null;
                            if (fechaD != null && !"".equals(fechaD)) {

                                fhasta = calcularFechaFinSubvSEI(formato.parse(fechaD));
                                Long diferenciaDias = DiferenciaEntreFechas(formato.parse(fechaD), fhasta);

                                String fHas = formato.format(fhasta);
                                f.addCampo("JRN_FHASTA", fHas);

                                log.debug("fhasta: " + fHas);
                                //NUMDIAS
                                log.debug("dias: " + String.valueOf(diferenciaDias));
                                f.addCampo("JRN_NUMDIA", String.valueOf(diferenciaDias));
                            } else {
                                log.error("Fecha desde no Viene en el XML - No rellenamos fechas no numero de dias");
                            }
                            log.debug("valor: " + valor);
                            f.addCampo("JRN_NUMCON", valor);
                            // 2019 Campos pasan a ser no obligatorios. Mientras controlamos null en S75 JBossSeam 
                            // JRN_PORJOR Ralacion tecnicos con puestos de insercion
                            String valorPorceJor = f.getCampos().get("JRN_PORJOR");
                            log.info("JRN_NUMCON/valorPorceJor :" + valor + ConstantesDatos.BARRA + valorPorceJor);
                            if (valorCodigoConcepto.equalsIgnoreCase("3") // Reemplazamos solo en caso de puestos de insercion
                                    || valorPorceJor == null || "".equals(valorPorceJor)) {
                                // ASignamos los ue viene en  lo que viene en S75PUESTOS
                                if (porcJornadaContraS75_PUESTOS != null && !"".equals(porcJornadaContraS75_PUESTOS)) {
                                    valorPorceJor = porcJornadaContraS75_PUESTOS;
                                } else // Asignamos Valor 0
                                {
                                    valorPorceJor = "0";
                                }
                                f.removeCampo("JRN_PORJOR");
                                f.addCampo("JRN_PORJOR", valorPorceJor);
                                log.info("f.addCampo(JRN_PORJOR, valorPorceJor); " + valorPorceJor);
                            }
                        }
                    }
                }
                // Una vez haya leido las tablas, creamos los datos de la convocaria, numero de trabajadores totales de la empresa 
                // cargado desde el campos suplementario NUMTRAB
                // Debe insertarse en la tabla s75relconvoca en la columna RCV_NUMTRABORD
                log.info(" Vamos a agregar los datos de numero de trabajadores " + numExp);
                MELanbide42TablaVO mELanbide42TablaVO = new MELanbide42TablaVO("S75RELCONVOCA", m);
                MELanbide42FilaVO mELanbide42FilaVO = new MELanbide42FilaVO(mELanbide42TablaVO);
                // RCV_MUN, RCV_PRO, RCV_EJE, RCV_NUM, RCV_EJE_1, RCV_NUM_1, RCV_EJE_2, RCV_NUM_2, RCV_NUMTRABORD, RCV_NUMPSTACTUAL, RCV_NUMPSTANTES
                mELanbide42FilaVO.addCampo("RCV_MUN", codOrg);
                mELanbide42FilaVO.addCampo("RCV_PRO", MELanbide42GeneralUtils.getCodigoProcFromNumExpediente(numExp));
                mELanbide42FilaVO.addCampo("RCV_EJE", MELanbide42GeneralUtils.getAnioFromNumExpediente(numExp));
                mELanbide42FilaVO.addCampo("RCV_NUM", MELanbide42GeneralUtils.getNumeroConsecutivoExpedienteFromNumExpediente(numExp));
                String valorCCampoSupleNUMTRAB = "";
                valorCCampoSupleNUMTRAB = MELanbide42DAO.getInstance().getCampoSuplementarioTipoNumerico("NUMTRAB", numExp, con);
                mELanbide42FilaVO.addCampo("RCV_NUMTRABORD", valorCCampoSupleNUMTRAB);
                mELanbide42TablaVO.addFila(mELanbide42FilaVO);
                m.addTabla(mELanbide42TablaVO);
                log.info(" FILAS : " + mELanbide42TablaVO.getFilas().size());
                log.info(" Datos de numero de trabajadores Agregado correctamente" + numExp + ConstantesDatos.BARRA + valorCCampoSupleNUMTRAB);
            }
            if (log.isDebugEnabled()) {
                log.debug("anadirCamposMelanbide33 : END");
            }
        } catch (Exception ex) {
            log.error("Error en anadirCamposMelanbide33: " + ex);
            String error = "Error en la funcion anadirCamposMelanbide33: " + ex.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_04");
            errorB.setMensajeError("Error en la funcion anadirCamposMelanbide33");
            errorB.setSituacion("anadirCamposMelanbide33");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        } finally {
            try {
                getAdaptSQLBD(codOrg).devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

    }

    /**
     * Operación que calcula la fecha Fin de la subvención sumando 3 años y
     * restando un día
     *
     * @param fechaInicio
     * @return la fecha fecFinPeriodo como Date calculada
     */
    private Date calcularFechaFinSubvSEI(Date fechaInicio) {
        log.info("calcularFechaFinSubvSEI - Begin " + fechaInicio.toString());
        Date fecFinPeriodo = null;
        Calendar valFechaFin;
        try {
            Calendar valFechaIni = Calendar.getInstance();;
            valFechaIni.setTime(fechaInicio);
            valFechaFin = Calendar.getInstance();
            valFechaFin.setTime(valFechaIni.getTime());
            valFechaFin.add(Calendar.DATE, -1);
            valFechaFin.add(Calendar.YEAR, 3);
            fecFinPeriodo = valFechaFin.getTime();
        } catch (Exception e) {
            log.error("Error al calcular el periodo " + e);
        }
        return fecFinPeriodo;
    }

    /*    private Date cacula3AnosMenos1DiaFechaFinSubvSEI(String fechaDesdeString) {
        log.info("cacula3AnosMenos1DiaFechaFinSubvSEI - Begin " + fechaDesdeString);
        Date fechaHata3AnosMenos1Dia = null;
        Date fechaDesdeDate = null;
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaDesdeString != null && !"".equals(fechaDesdeString)) {
            try {
                fechaDesdeDate = formato.parse(fechaDesdeString);
                fechaHata3AnosMenos1Dia = new Date(fechaDesdeDate.getTime());
                fechaHata3AnosMenos1Dia.setDate(fechaDesdeDate.getDate() - 1);
                fechaHata3AnosMenos1Dia.setYear(fechaDesdeDate.getYear() + 3);
            } catch (ParseException ex) {
                log.error("Error al calcular la fecha fin Subvencion " + ex.getMessage(), ex);
                fechaHata3AnosMenos1Dia = null;
            }
        }
        log.info("cacula3AnosMenos1DiaFechaFinSubvSEI - End " + (fechaHata3AnosMenos1Dia != null ? fechaHata3AnosMenos1Dia.toString() : "null"));
        return fechaHata3AnosMenos1Dia;
    }*/
    private void realizarInsertSQL(String sql, AdaptadorSQLBD adaptador) throws MELanbide42Exception {
        if (log.isDebugEnabled()) {
            log.debug("realizarInsertSQL() : BEGIN");
        }

        Connection con = null;
        if (log.isDebugEnabled()) {
            log.debug("TEST ============" + sql);
        }
        try {
            //if (5 == 6){
            con = adaptador.getConnection();
            MELanbide42DAO meLanbide42DAO = MELanbide42DAO.getInstance();
            meLanbide42DAO.insertarSQL(sql, con);
            //}

        } catch (BDException e) {
            log.error("Error BD insertando expediente electrónico para query " + sql, e);
            throw new MELanbide42Exception("Error BDException BD insertando expediente electrónico para query " + sql
                    + " - " + e.getMensaje() + " => " + e.getErrorCode() + " " + e.getDescripcion(), e);
        } catch (SQLException e) {
            log.error("Error BD insertando expediente electrónico para query " + sql, e);
            throw new MELanbide42Exception("Error SQLException BD insertando expediente electrónico para query " + sql
                    + " - " + e.getMessage() + " => " + e.getErrorCode() + " " + e.getSQLState(), e);
        } catch (MELanbide42Exception e) {
            log.error("Error BD insertando expediente electrónico para query " + sql + " => " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error BD insertando expediente electrónico para query " + sql + " => " + e.getMessage(), e);
            throw new MELanbide42Exception("Error-Generico- insertando expediente electrónico para query " + sql
                    + " - " + e.getMessage(), e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("realizarInsertSQL() : END");
        }
    }

    /**
     * CONCM
     *
     * @param expediente
     * @param codOrganizacion
     * @throws MELanbide42Exception
     */
    public void realizarCalculosMELANBIDE01(MELanbide42XMLAltaExpediente expediente, AdaptadorSQLBD adaptSQL, String numExp, int codOrganizacion) throws Exception {
        // Cálculo: PORC_SUBVENC = ((REDUCPERSSUST) * (JORNPERSSUST)) / (JORNPERSCONT)
        try {
            for (MELanbide42ModuloVO m : expediente.getListaModulos()) {
                if (m.getCodigo().equalsIgnoreCase("MELANBIDE01")) {
                    // Es el módulo adecuado
                    MELanbide42TablaVO tablaCal = m.getTableByName("MELANBIDE01_DATOS_CALCULO");
                    MELanbide42TablaVO tablaPer = m.getTableByName("MELANBIDE01_PERIODO");
                    try {
                        // Solo hacemos el calculo e isnertamos la tabla relacionada padre si,en el modulo esta la tabla periodo
                        if (tablaPer != null) {
                            MELanbide42CalculosTablas.calculo_MELANBIDE01_PERIODO(expediente, tablaCal, tablaPer, adaptSQL, numExp, codOrganizacion);
                        } else {
                            log.info("Modulo " + m.getCodigo() + " - " + " No se invoca MELanbide42CalculosTablas.calculo_MELANBIDE01_PERIODO - no esta en el modulo la tabla MELANBIDE01_PERIODO");
                        }
                    } catch (ParseException pe) {
                        log.error("Error al parsear los datos variables del expediente", pe);
                        throw new MELanbide42Exception("Error al parsear los datos variables del expediente", pe);
                    }

                }
            }
        } catch (Exception ex) {
            log.error("Error en realizarCalculosMELANBIDE01 ", ex);
            String error = "Error en la funcion realizarCalculosMELANBIDE01: " + ex.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_05");
            errorB.setMensajeError("Error en la funcion realizarCalculosMELANBIDE01");
            errorB.setSituacion("realizarCalculosMELANBIDE01");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw ex;
        }
    }

    /**
     * REGCF
     *
     * @param expediente
     * @param adaptSQL
     * @throws MELanbide42Exception
     * @throws java.sql.SQLException
     */
    public void realizarCalculosMELANBIDE41(MELanbide42XMLAltaExpediente expediente,
            AdaptadorSQLBD adaptSQL) throws MELanbide42Exception, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("realizarCalculosMELANBIDE41() : BEGIN");
        }

        // MELANBIDE41_DISPONRECURSOS << Necesita FKs de la tabla MELANBIDE41_ESPECIALIDADES
        for (MELanbide42ModuloVO m : expediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE41")) {
                // Es el módulo adecuado
                MELanbide42TablaVO tablaEsp = m.getTableByName("MELANBIDE41_ESPECIALIDADES");
                MELanbide42TablaVO tablaDis = m.getTableByName("MELANBIDE41_DISPONRECURSOS");
                MELanbide42TablaVO tablaCap = m.getTableByName("MELANBIDE41_CAPACIDADINST");
                MELanbide42TablaVO tablaDot = m.getTableByName("MELANBIDE41_DOTACION");
                MELanbide42TablaVO tablaMat = m.getTableByName("MELANBIDE41_MATERIALCONSU");
                MELanbide42TablaVO tablaIde = m.getTableByName("MELANBIDE41_IDENTIFICESP");
                try {
                    MELanbide42CalculosTablas.calculo_MELANBIDE41_ESPECIALIDADES(expediente, tablaEsp, tablaDis,
                            tablaCap, tablaDot, tablaMat, tablaIde, adaptSQL);
                } catch (ParseException pe) {
                    log.error("Error al parsear los datos del expediente", pe);
                    throw new MELanbide42Exception("Error al parsear los datos del expediente", pe);
                } catch (BDException bde) {
                    log.error("Error de base de datos al insertar los datos del expediente", bde);
                    throw new MELanbide42Exception("Error de base de datos al insertar los datos del expediente", bde);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("realizarCalculosMELANBIDE41() : END");
        }
    }

    /**
     *
     * @param expediente
     * @param adaptSQL
     * @throws MELanbide42Exception
     */
    public void realizarCalculosMELANBIDE50(MELanbide42XMLAltaExpediente expediente,
            AdaptadorSQLBD adaptSQL) throws MELanbide42Exception {
        if (log.isDebugEnabled()) {
            log.debug("realizarCalculosMELANBIDE50() : BEGIN");
        }

        // MELANBIDE50_DISPONRECURSOS << Necesita FKs de la tabla MELANBIDE50_ESPECIALIDADES
        for (MELanbide42ModuloVO m : expediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE50")) {
                // Es el módulo adecuado
                MELanbide42TablaVO tablaEsp = m.getTableByName("MELANBIDE50_ESPECIALIDADES");
                MELanbide42TablaVO tablaDis = m.getTableByName("MELANBIDE50_DISPONRECURSOS");
                MELanbide42TablaVO tablaCap = m.getTableByName("MELANBIDE50_CAPACIDADINST");
                MELanbide42TablaVO tablaDot = m.getTableByName("MELANBIDE50_DOTACION");
                MELanbide42TablaVO tablaMat = m.getTableByName("MELANBIDE50_MATERIALCONSU");
                MELanbide42TablaVO tablaIde = m.getTableByName("MELANBIDE50_IDENTIFICESP");
                MELanbide42TablaVO tablaEspac = m.getTableByName("MELANBIDE50_ESPACIOS");
                try {
                    MELanbide42CalculosTablas.calculo_MELANBIDE50_ESPECIALIDADES(expediente, tablaEsp, tablaDis,
                            tablaCap, tablaDot, tablaMat, tablaIde, tablaEspac, adaptSQL);
                } catch (ParseException pe) {
                    log.error("Error al parsear los datos del expediente", pe);
                    throw new MELanbide42Exception("Error al parsear los datos del expediente", pe);
                } catch (BDException bde) {
                    log.error("Error de base de datos al insertar los datos del expediente", bde);
                    throw new MELanbide42Exception("Error de base de datos al insertar los datos del expediente", bde);
                }
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("realizarCalculosMELANBIDE50() : END");
        }
    }

    public void anadirCamposMelanbide61Pexco(MELanbide42XMLAltaExpediente expediente, String numExp, String codOrg) {
        try {
            ArrayList<MELanbide42ModuloVO> modulos = expediente.getListaModulos();
            ArrayList<MELanbide42Exception> erroresInsercion = new ArrayList<MELanbide42Exception>();

            for (MELanbide42ModuloVO m : modulos) {
                for (MELanbide42TablaVO t : m.getTablas()) {
                    if (log.isDebugEnabled()) {
                        log.debug("anadirCamposMelanbide61Pexco : BEGIN");
                    }
                    String ejercicio = "";
                    String proc = "";
                    String[] num = numExp.split(ConstantesDatos.BARRA);
                    ejercicio = num[0];
                    int cod = 12;
                    boolean primera = true;
                    proc = num[1];
                    //REPLE_PEXCO
                    if (t.getNombre().equals("REPLE_PEXCO")) {
                        log.debug("REPLE_PEXCO");
                        primera = true;
                        for (MELanbide42FilaVO f : t.getFilas()) {
                            f.addCampo("PEXCO_MUN", codOrg);
                            f.addCampo("PEXCO_PRO", proc);
                            f.addCampo("PEXCO_EJE", ejercicio);
                            cod = 1;
                            f.addCampo("PEXCO_NCON", String.valueOf(cod));
                        }
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("anadirCamposMelanbide61Pexco : END");
            }
        } catch (Exception ex) {
            log.error("Error en anadirCamposMelanbide61: " + ex);
            String error = "Error en la funcion anadirCamposMelanbide33: " + ex.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_04");
            errorB.setMensajeError("Error en la funcion anadirCamposMelanbide61");
            errorB.setSituacion("anadirCamposMelanbide61Pexco");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }

    }

    public void anadirCampoPuestoGEL(MELanbide42XMLAltaExpediente expediente, String numExp, String codOrg) {
        log.info("anadirCampoPuestoGEL : BEGIN");
        try {
            ArrayList<MELanbide42ModuloVO> modulos = expediente.getListaModulos();
            for (MELanbide42ModuloVO m : modulos) {
                for (MELanbide42TablaVO t : m.getTablas()) {
                    if (t.getNombre().equals("MELANBIDE82_CONTRATACION")) {
                        for (MELanbide42FilaVO f : t.getFilas()) {
                            String valor = f.getCampos().get("NUM_PUESTO");
                            f.addCampo("PRIORIDAD", valor);
                            f.removeCampo("NUM_PUESTO");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error en anadirCampoPuestoGEL: " + ex);
            String error = "Error en la funcion anadirCampoPuestoGEL: " + ex.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_04");
            errorB.setMensajeError("Error en la funcion anadirCampoPuestoGEL");
            errorB.setSituacion("anadirCampoPuestoGEL");
            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    public static long DiferenciaEntreFechas(Date fechaini, Date fechafin) {
        //java.util.Date fechaAnterior, fechaPosterior;
        final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000; //Milisegundos al día 
        long diferencia = (fechafin.getTime() - fechaini.getTime()) / MILLSECS_PER_DAY;

        System.out.println("FINI:" + fechaini + " Ffin:" + fechafin + " diferencia de dias:" + diferencia);
        return diferencia;
    }

    public static void grabarError(ErrorBean error, String excepError, String traza, String numExp) {
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError);
            error.setTraza(excepError);
            error.setCausa(traza);
            log.error("causa: " + traza);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERRMISGEST/000000";
            }
            String valor[] = numExp.split(ConstantesDatos.BARRA);
            String idProcedimiento = "";
            if (valor.length > 1) {
                idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(valor[1]); //convierteProcedimiento(codProcedimiento);
            }
            log.error("procedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setIdClave("");
            // Para el jar que se usa en WSRegexlan cambiar estas 2 lineas
            error.setSistemaOrigen("REGEXLAN");
            error.setErrorLog("flexia_debug");
//            error.setSistemaOrigen("WSRegexlan");
//            error.setErrorLog("WSRegexlan.log");
            error.setIdFlexia(numExp);
            log.error("Vamos a registrar el error");

            RegistroErrores.registroError(error);

            //ErroresDAO.getInstance().insertaRegistroError(error, con);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    public String recalcularSMI(MELanbide42XMLAltaExpediente expediente, String numExp, String codOrg) throws BDException {
        if (log.isDebugEnabled()) {
            log.debug("recalcularSMI : BEGIN");
        }
        MELanbide42DAO meLanbide42DAO = MELanbide42DAO.getInstance();
        Connection con = null;
        String anio = "";
        String rdo = "";
        ErrorBean errorB = new ErrorBean();
        try {
            con = getAdaptSQLBD(codOrg).getConnection();
            anio = meLanbide42DAO.obtenerAnio(numExp, con);
            /*if(anio.isEmpty()){
                rdo=ConstantesMeLanbide42.ANIO_VACIO;
                String error = "Error año solicitado no dado de alta";
                errorB.setIdError(rdo);
                errorB.setMensajeError("No se puede recalcular la subvención porque el Salario Mínimo Interprofesional para el año solicitado no está dado de alta en el sistema");
                errorB.setSituacion("recalcularSMI");
                MELanbide42Manager.grabarError(errorB, error, error, numExp);
            }else{*/
            rdo = meLanbide42DAO.recalcularSMI(numExp, anio, con);

            if (rdo.equalsIgnoreCase(ConstantesMeLanbide42.OK)) {
                log.debug("El recálculo ha finalizado con éxito.");
            } else if (rdo.equalsIgnoreCase(ConstantesMeLanbide42.NOT_FOUND_SMI)) {
                String error = "Error al obtener SMI";
                errorB.setIdError(rdo);
                errorB.setMensajeError("La tabla MELANBIDE58_VALOR_SMI no contiene la información solicitada (No encuentra el SMI)");
                errorB.setSituacion("recalcularSMI");
                MELanbide42Manager.grabarError(errorB, error, error, numExp);
            }

            if (log.isDebugEnabled()) {
                log.debug("recalcularSMI : END");
            }
            //}

        } catch (SQLException e) {
            log.error("Error Oracle en recálculo de subvención: " + e);
            rdo = ConstantesMeLanbide42.ERROR_RECALCULO;
            String error = "Error Oracle en recálculo de subvención: " + e.toString();

            errorB.setIdError(rdo);
            errorB.setMensajeError("Error Oracle realizando el recálculo de la subvención para el expediente de Subvención de Centros Especiales de Empleo");
            errorB.setSituacion("recalcularSMI");
            MELanbide42Manager.grabarError(errorB, error, error, numExp);

            e.printStackTrace();
        } catch (Exception ex) {
            log.error("Error en la funcion recalcularSMI: " + ex);
            String error = "Error en la funcion recalcularSMI: " + ex.toString();

            errorB.setIdError(rdo);
            errorB.setMensajeError("Error en la funcion recalcularSMI");
            errorB.setSituacion("recalcularSMI");

           grabarError(errorB, error, error, numExp);
        } finally {
            try {
                getAdaptSQLBD(codOrg).devolverConexion(con);
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return rdo;

    }

    public String borrarDatosTabla(String modulo, String tabla, String numExp, String codOrg) {
        if (log.isDebugEnabled()) {
            log.debug("borrarDatosTabla : BEGIN");
        }
        MELanbide42DAO meLanbide42DAO = MELanbide42DAO.getInstance();
        Connection con = null;
        String rdo = "";
        ErrorBean errorB = new ErrorBean();
        try {
            con = getAdaptSQLBD(codOrg).getConnection();
            String aux = modulo + ConstantesDatos.BARRA + tabla + "/numexpfield";
            String numexpfield = MELanbide42Properties.getProperty(aux);
            if (log.isDebugEnabled()) {
                log.debug("Propiedad/numexpfield: " + aux + ConstantesDatos.BARRA + numexpfield);
            }
            rdo = meLanbide42DAO.borrarDatosTabla(tabla, numexpfield, numExp, con);
        } catch (Exception ex) {
            log.error("Error en la funcion borrarDatosTabla: " + ex);
            String error = "Error en la funcion borrarDatosTabla: " + ex.toString();

            errorB.setIdError(rdo);
            errorB.setMensajeError("Error en la funcion borrarDatosTabla");
            errorB.setSituacion("borrarDatosTabla");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                log.error("Ha ocurrido un error al cerrar la conexión con la BBDD");
            }
        }
        return rdo;
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }

        //TRUCO DEL ALMENDRUCO --> CONEXION LOCAL
        /*if (5 == 5) {
            log.error("EERRRORRRRRRRRRRRR getAdaptSQLBD trucado !!!!!!!");
            return null;
        }*/
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genÃ©rico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    public void prepararDatosORI14Mod47(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("prepararDatosORI14Mod47() : BEGIN");
        }
        /*
        Necesitamos : 
        PK ENTIDAD para ASOCIACION y UBICACIONES.
        PK ASOCIACION para TRAYECTORIA
        PK ENTIDAD y COD_AMBITO(Recuperarlo de BBDD este datos se precarga) para UBICACIONES
        
         */
        for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE47")) {
                // Es el módulo adecuado
                MELanbide42TablaVO tablaEntidad = m.getTableByName("ORI14_ENTIDAD");
                //MELanbide42TablaVO tablaAsociacionDatosEntidad = m.getTableByName("ORI14_ASOCIACION_ENT");
                MELanbide42TablaVO tablaAsociacionListaEnts = m.getTableByName("ORI14_ASOCIACION");
                MELanbide42TablaVO tablaTrayectoria = m.getTableByName("ORI_TRAYECTORIA_ENTIDAD");
                MELanbide42TablaVO tablaUbicaciones = m.getTableByName("ORI14_ORI_UBIC");
                MELanbide42TablaVO tablaAmbitosSoli = m.getTableByName("ORI14_AMBITOS_SOLICITADOS");
                MELanbide42TablaVO tablaOtrosProgramas = m.getTableByName("ORI_OTROS_PROGRAMAS");
                MELanbide42TablaVO tablaActividades = m.getTableByName("ORI_ACTIVIDADES");
                MELanbide42TablaVO tablaCertificadosCalidad = m.getTableByName("ORI_ENTIDAD_CERT_CALIDAD");
                try {
                    MELanbide42CalculosTablas.prepararDatosORI14Mod47(xmlExpediente, tablaEntidad //, tablaAsociacionDatosEntidad
                            ,
                             tablaAsociacionListaEnts,
                            tablaTrayectoria, tablaUbicaciones, tablaAmbitosSoli,
                            tablaOtrosProgramas, tablaActividades, tablaCertificadosCalidad,
                            m,
                            adapt, numExp, codOrg);
                } catch (ParseException pe) {
                    log.error("Error al parsear los datos del expediente", pe);
                    throw new MELanbide42Exception("Error al parsear los datos del expediente", pe);
                } catch (BDException bde) {
                    log.error("Error de base de datos al insertar los datos del expediente", bde);
                    throw new MELanbide42Exception("Error de base de datos al insertar los datos del expediente", bde);
                } catch (Exception ex) {
                    log.error("prepararDatosORI14Mod47 - Error general al tratar los datos de ORI14 ", ex);
                    throw new MELanbide42Exception("Error general al tratar los datos de ORI14." + ex.getMessage(), ex);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("prepararDatosORI14Mod47() : END");
        }
    }

    public Map<String, String> getTerceroxNumExpxRol(String numExp, int rol, Connection conn) throws Exception {
        log.debug("getTerceroxNumExpxRol - BEGIN()");
        Map<String, String> datosTercero = new HashMap<String, String>();
        MELanbide42DAO meLanbide42DAO = MELanbide42DAO.getInstance();
        try {
            datosTercero = meLanbide42DAO.getTerceroxNumExpxRolDAO(numExp, rol, conn);
        } catch (Exception e) {
            log.error("getTerceroxNumExpxRol - Error al recuperar losd atos del tercero : ", e);
            throw new MELanbide42Exception("Error recuperando los datos de un tercero.", e);
        }
        log.debug("getTerceroxNumExpxRol - END()");
        return datosTercero;
    }

    public String getTerceroFechaNacimiento(String documento, Connection conn) throws Exception {
        log.info("getTerceroFechaNacimiento - BEGIN()");
        String datosTercero = "";
        MELanbide42DAO meLanbide42DAO = MELanbide42DAO.getInstance();
        try {
            datosTercero = meLanbide42DAO.getTerceroFechaNacimientoDAO(documento, conn);
        } catch (Exception e) {
            log.error("getTerceroxNumExpxRol - Error al getTerceroFechaNacimiento : " + e.getMessage(), e);
            throw new MELanbide42Exception("Error recuperando los datos  tercerogetTerceroFechaNacimiento.", e);
        }
        log.info("getTerceroFechaNacimiento - END()");
        return datosTercero;
    }

    public String getCodAmbitoxNombrexAnioConvocxCodPro(String nombreAmbito, String ejercicio, String codPro, Connection connection) throws Exception {
        log.debug("getCodAmbitoxNombrexAnioConvoc - BEGIN()");
        String codAmbito = "";
        MELanbide42DAO meLanbide42DAO = MELanbide42DAO.getInstance();
        try {
            codAmbito = meLanbide42DAO.getCodAmbitoxNombrexAnioConvocxCodPro(nombreAmbito, ejercicio, codPro, connection);
        } catch (Exception e) {
            log.error("getCodAmbitoxNombrexAnioConvoc - Error al recuperar los datos de codigo de ambito : ", e);
            throw new MELanbide42Exception("Error recuperando el codigo de ámbito: : " + e.getMessage(), e);
        }
        log.debug("getCodAmbitoxNombrexAnioConvoc - codAmbito recuperado " + codAmbito);
        log.debug("getCodAmbitoxNombrexAnioConvoc - END()");
        return codAmbito;
    }

    public MELanbide42XMLAltaExpediente ordenarNodosXML_ORI14(MELanbide42XMLAltaExpediente xmlExpediente) throws MELanbide42Exception {
        if (log.isDebugEnabled()) {
            log.debug("ordenarNodosXML_ORI14() : BEGIN");
        }
        MELanbide42XMLAltaExpediente xmlExpedienteOrdenadoORI14 = xmlExpediente;
        try {
            for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
                if (m.getCodigo().equalsIgnoreCase("MELANBIDE47")) {
                    // Es el módulo ORI14. 
                    // Limpio de la copia el modulo para añadirlo ordenado
                    xmlExpedienteOrdenadoORI14.getListaModulos().remove(m);
                    MELanbide42ModuloVO moduloOr = new MELanbide42ModuloVO("MELANBIDE47");

                    // Obtenemos las tablas ya completas con datos del XML original y las pasamos en el orden que necesitamos al xml copia
                    MELanbide42TablaVO tablaEntidad = m.getTableByName("ORI14_ENTIDAD");
                    // Si NO es una asociacion debemos guardar los datos de la entidad en la tabla Asociacion
                    // Leemos la tabla temporal creada en La preparcion del modulo para estos datos, 
                    // Si no viene es porque es una agrupacionpor tanto no agregamos esos datos.
                    MELanbide42TablaVO tablaAsociacionDatosEntidad = m.getTableByName("ORI14_ASOCIACION_ENT");
                    MELanbide42TablaVO tablaAsociacionDatosEntidad_0 = new MELanbide42TablaVO("ORI14_ASOCIACION", moduloOr);
                    boolean addDatosEntTablaAsociacion = false;
                    if (tablaAsociacionDatosEntidad != null && tablaAsociacionDatosEntidad.getFilas() != null && tablaAsociacionDatosEntidad.getFilas().size() > 0) {
                        tablaAsociacionDatosEntidad_0.setPkFieldName(tablaAsociacionDatosEntidad.getPkFieldName());
                        tablaAsociacionDatosEntidad_0.setPkSeqName(tablaAsociacionDatosEntidad.getPkSeqName());
                        tablaAsociacionDatosEntidad_0.getFilas().addAll(tablaAsociacionDatosEntidad.getFilas());
                        addDatosEntTablaAsociacion = true;
                    }

                    MELanbide42TablaVO tablaAsociacionListaEnts = m.getTableByName("ORI14_ASOCIACION");
                    MELanbide42TablaVO tablaTrayectoria = m.getTableByName("ORI_TRAYECTORIA_ENTIDAD");
                    MELanbide42TablaVO tablaUbicaciones = m.getTableByName("ORI14_ORI_UBIC");
                    MELanbide42TablaVO tablaAmbitosSoli = m.getTableByName("ORI14_AMBITOS_SOLICITADOS");
                    MELanbide42TablaVO tablaOtrosProgrammas = m.getTableByName("ORI_OTROS_PROGRAMAS");
                    MELanbide42TablaVO tablaActividades = m.getTableByName("ORI_ACTIVIDADES");
                    MELanbide42TablaVO tablaCertificadosCalidad = m.getTableByName("ORI_ENTIDAD_CERT_CALIDAD");

                    // Añadimos a la copia:
                    if (tablaEntidad != null) {
                        moduloOr.addTabla(tablaEntidad);
                    }
                    if (addDatosEntTablaAsociacion) {
                        moduloOr.addTabla(tablaAsociacionDatosEntidad_0);
                        log.error(" addDatosEntTablaAsociacion es " + addDatosEntTablaAsociacion + " Agregamos en el XML Ordenado los datos de la entidad principal para tabla asociacion ");
                    } else {
                        log.error(" addDatosEntTablaAsociacion " + addDatosEntTablaAsociacion + "  No la agregamos en el XML Ordenado");
                    }
                    if (tablaAsociacionListaEnts != null) {      // Sino viene en el XML se queda a null y da error al recorrer la preparacion de las insert
                        moduloOr.addTabla(tablaAsociacionListaEnts);
                        log.error(" tablaAsociacionListaEnts Viene a null no la agregamos en el XML Ordenado");
                    } else {
                        log.error(" tablaAsociacionListaEnts Viene a null no la agregamos en el XML Ordenado");
                    }
                    if (tablaTrayectoria != null) {
                        moduloOr.addTabla(tablaTrayectoria);
                    }
                    if (tablaUbicaciones != null) {
                        moduloOr.addTabla(tablaUbicaciones);
                    }
                    if (tablaAmbitosSoli != null) {
                        moduloOr.addTabla(tablaAmbitosSoli);
                    }
                    if (tablaOtrosProgrammas != null) {
                        moduloOr.addTabla(tablaOtrosProgrammas);
                    }
                    if (tablaActividades != null) {
                        moduloOr.addTabla(tablaActividades);
                    }
                    if (tablaCertificadosCalidad != null) {
                        moduloOr.addTabla(tablaCertificadosCalidad);
                    }

                    xmlExpedienteOrdenadoORI14.getListaModulos().add(moduloOr);
                }
            }
        } catch (Exception ex) {
            log.error("ordenarNodosXML_ORI14 - Error general al ordenar los nodos del XML FLX para carga telematicas de ORI14 ", ex);
            throw new MELanbide42Exception("EError general al ordenar los nodos del XML FLX para carga telematicas de ORI14. " + ex.getMessage(), ex);
        }

        if (log.isDebugEnabled()) {
            log.debug("ordenarNodosXML_ORI14() : END");
        }
        return xmlExpedienteOrdenadoORI14;
    }

    public void prepararDatosCEMPMod32(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws MELanbide42Exception {
        if (log.isDebugEnabled()) {
            log.debug("prepararDatosCEMPMod32() : BEGIN");
        }
        /*
         Necesitamos : 
         PK ENTIDAD para TRAYECTORIA
         PK ENTIDAD y COD_AMBITO(Recuperarlo de BBDD este datos se precarga) para UBICACIONES
         */
        for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE32")) {
                // Es el módulo adecuado
                MELanbide42TablaVO tablaEntidad = m.getTableByName("ORI_ENTIDAD");
                MELanbide42TablaVO tablaTrayectoria = m.getTableByName("ORI_CE_TRAYECTORIA");
                MELanbide42TablaVO tablaUbicaciones = m.getTableByName("ORI_CE_UBIC");
                MELanbide42TablaVO tablaAmbitosSoli = m.getTableByName("ORI_AMBITOS_CE_SOLICITADOS");
                MELanbide42TablaVO tablaCriteriosCentro = m.getTableByName("ORI_CE_CRITERIOS_CENTRO");
                try {
                    MELanbide42CalculosTablas.prepararDatosCEMPMod32(xmlExpediente, tablaEntidad,
                            tablaTrayectoria, tablaUbicaciones, tablaAmbitosSoli, tablaCriteriosCentro, adapt, numExp, codOrg);
                } catch (MELanbide42Exception pe) {
                    log.error("Error al procesar la carga telematica del Modulo M32 " + pe.getMessage(), pe);
                    throw pe;
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("prepararDatosCEMPMod32() : END");
        }
    }

    public void prepararDatosCOLECMod48(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws MELanbide42Exception, Exception {
        if (log.isDebugEnabled()) {
            log.info("prepararDatosCOLECMod48() : BEGIN");
        }
        /*
         PREPARAR LAS PK DE CADA TABLA A INSERTAR
         */
        for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE48")) {
                // Es el módulo adecuado
                MELanbide42TablaVO tablaSolicitud = m.getTableByName("COLEC_SOLICITUD");
                MELanbide42TablaVO tablaEntidad = m.getTableByName("COLEC_ENTIDAD");
                MELanbide42TablaVO tablaEntidadListAgrup = m.getTableByName("COLEC_ENTIDAD_AGRUP_LIST");
                MELanbide42TablaVO tablaTrayectoriaEsp = m.getTableByName("COLEC_TRAY_ESP");
                MELanbide42TablaVO tablaTrayectoria = m.getTableByName("COLEC_TRAYECTORIA_ENTIDAD");
                MELanbide42TablaVO tablaOtrosProgramas = m.getTableByName("COLEC_OTROS_PROGRAMAS");
                MELanbide42TablaVO tablaActividades = m.getTableByName("COLEC_ACTIVIDADES");
                MELanbide42TablaVO tablaUbicaciones = m.getTableByName("COLEC_UBICACIONES_CT");
                MELanbide42TablaVO tablaPorcxColecxTHH = m.getTableByName("COLEC_COMPREAL_XCOLTH");
                MELanbide42TablaVO tablaCertificadosCalidad = m.getTableByName("COLEC_ENTIDAD_CERT_CALIDAD");
                try {
                    MELanbide42CalculosTablas.prepararDatosCOLECMod48(xmlExpediente, tablaSolicitud, tablaEntidad,
                            tablaEntidadListAgrup, tablaTrayectoriaEsp, tablaTrayectoria, tablaOtrosProgramas, tablaActividades, tablaUbicaciones,
                            tablaPorcxColecxTHH, tablaCertificadosCalidad,
                            adapt, numExp, codOrg);
                } catch (ParseException pe) {
                    log.error("Error al parsear los datos del expediente - M48", pe);
                    throw new MELanbide42Exception("Error al parsear los datos del expediente - M48", pe);
                } catch (BDException bde) {
                    log.error("Error de base de datos al insertar los datos del expediente - M48", bde);
                    throw new MELanbide42Exception("Error de base de datos al insertar los datos del expediente - M48", bde);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("prepararDatosCOLECMod48() : END");
        }
    }

    public void prepararDatosTRECOMod88(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws MELanbide42Exception, Exception {
        if (log.isDebugEnabled()) {
            log.info("prepararDatosTRECOMod88() : BEGIN");
        }

        for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE88")) {
                MELanbide42TablaVO tablaSocios = m.getTableByName("MELANBIDE88_SOCIOS");
                MELanbide42TablaVO tablaInversiones = m.getTableByName("MELANBIDE88_INVERSIONES");
                MELanbide42TablaVO tablaSubvenciones = m.getTableByName("MELANBIDE88_SUBSOLIC");
                try {
                    MELanbide42CalculosTablas.prepararDatosTRECOMod88(xmlExpediente, tablaInversiones, tablaSubvenciones,
                            adapt, numExp, codOrg);
                } catch (Exception bde) {
                    log.error("Error al tratar datos del Modulo - M88", bde);
                    throw new MELanbide42Exception("Error al tratar/formatear/parsear datos del Modulo  del expediente - M88", bde);
                }
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("prepararDatosTRECOMod88() : END");
        }
    }

    public void prepararDatosLPELLMod81(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws MELanbide42Exception, Exception {
        ArrayList<MELanbide42ModuloVO> modulos = xmlExpediente.getListaModulos();
        for (MELanbide42ModuloVO m : modulos) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE81")) {
                // Es el módulo adecuado
                MELanbide42TablaVO tablaProyectos = m.getTableByName("MELANBIDE81_PROYECTOS");
                MELanbide42TablaVO tablaPuestos = m.getTableByName("MELANBIDE81_PUESTOS");
                MELanbide42TablaVO tablaContrataciones = m.getTableByName("MELANBIDE81_CONTRATACIONES");
                MELanbide42TablaVO tablaTipo1 = m.getTableByName("MELANBIDE81_TIPO1");
                MELanbide42TablaVO tablaTipo2 = m.getTableByName("MELANBIDE81_TIPO2");
                if (tablaTipo1 == null && tablaTipo2 == null) {
                    try {
                        MELanbide42CalculosTablas.prepararDatosLPEEL(xmlExpediente, tablaProyectos, tablaPuestos, tablaContrataciones, adapt, numExp, codOrg);
                    } catch (Exception ex) {
                        log.error("Error en prepararDatosLPELLMod81: " + ex);
                        String error = "Error en la funcion prepararDatosLPELLMod81: " + ex.toString();

                        ErrorBean errorB = new ErrorBean();
                        errorB.setIdError("TELEMATICO_04");
                        errorB.setMensajeError("Error en la funcion prepararDatosLPELLMod81");
                        errorB.setSituacion("prepararDatosLPELLMod81");
                        MELanbide42Manager.grabarError(errorB, error, error, numExp);
                    }
                }
            }
        }
    }

    public MELanbide42XMLAltaExpediente ordenarNodosXML_CEMP(MELanbide42XMLAltaExpediente xmlExpediente) throws MELanbide42Exception {
        log.info("ordenarNodosXML_CEMP() : BEGIN");
        MELanbide42XMLAltaExpediente xmlExpedienteOrdenadoCEMP = xmlExpediente;
        try {
            for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
                if (m.getCodigo().equalsIgnoreCase("MELANBIDE32")) {
                    // Es el módulo CEMP. 
                    // Limpio de la copia el modulo para añadirlo ordenado
                    xmlExpedienteOrdenadoCEMP.getListaModulos().remove(m);
                    MELanbide42ModuloVO moduloOr = new MELanbide42ModuloVO("MELANBIDE32");

                    // Obtenemos las tablas ya completas con datos del XML original y las pasamos en el orden que necesitamos al xml copia
                    // Es el módulo adecuado
                    MELanbide42TablaVO tablaEntidad = m.getTableByName("ORI_ENTIDAD");
                    MELanbide42TablaVO tablaTrayectoria = m.getTableByName("ORI_CE_TRAYECTORIA");
                    MELanbide42TablaVO tablaUbicaciones = m.getTableByName("ORI_CE_UBIC");
                    MELanbide42TablaVO tablaAmbitosSoli = m.getTableByName("ORI_AMBITOS_CE_SOLICITADOS");
                    MELanbide42TablaVO tablaCriteriosCentro = m.getTableByName("ORI_CE_CRITERIOS_CENTRO");

                    // Añadimos a la copia:
                    if (tablaEntidad != null) {
                        moduloOr.addTabla(tablaEntidad);
                    }
                    if (tablaTrayectoria != null) {
                        moduloOr.addTabla(tablaTrayectoria);
                    }
                    if (tablaUbicaciones != null) {
                        moduloOr.addTabla(tablaUbicaciones);
                    }
                    if (tablaAmbitosSoli != null) {
                        moduloOr.addTabla(tablaAmbitosSoli);
                    }
                    if (tablaCriteriosCentro != null) {
                        moduloOr.addTabla(tablaCriteriosCentro);
                    }
                    xmlExpedienteOrdenadoCEMP.getListaModulos().add(moduloOr);
                }
            }
        } catch (Exception ex) {
            log.error("ordenarNodosXML_CEMP - Error general al ordenar los nodos del XML FLX para carga telematicas de CEMP ", ex);
            throw new MELanbide42Exception("Error general al ordenar los nodos del XML FLX para carga telematicas de CEMP. " + ex.getMessage(), ex);
        }
        log.debug("ordenarNodosXML_CEMP() : END");
        return xmlExpedienteOrdenadoCEMP;
    }

    public void prepararDatosININMod90(MELanbide42XMLAltaExpediente xmlExpediente) throws MELanbide42Exception, Exception {
        log.info("prepararDatosININMod90() : BEGIN");
        for (MELanbide42ModuloVO m : xmlExpediente.getListaModulos()) {
            if (m.getCodigo().equalsIgnoreCase("MELANBIDE90")) {
                MELanbide42TablaVO tablaFacturas = m.getTableByName("MELANBIDE90_FACTURAS");
                try {
                    MELanbide42CalculosTablas.prepararDatosININMod90(tablaFacturas);
                } catch (Exception bde) {
                    log.error("Error al tratar datos del Modulo - M90", bde);
                    throw new MELanbide42Exception("Error al tratar/formatear/parsear datos del Modulo  del expediente - M90: " + bde.getMessage(), bde);
                }
            }
        }
        log.debug("prepararDatosININMod90() : END");
    }

}
