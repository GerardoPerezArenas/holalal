/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.util;

/**
 *
 * @author santiagoc
 */
public class ConstantesMeLanbide67 {

    //Código del procedimiento
    public static final String COD_PROCEDIMIENTO = "CPE";

    //Fichero de propiedades del módulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE67";

    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    // pipe
    public static final String PIPE = "|";
    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";

    //Nombres de las tablas en el fichero de propiedades.  
    public static final String TABLA_TIPO_DATO_NUMERICO = "TABLA_TIPO_DATO_NUMERICO";
    public static final String TABLA_TIPO_DATO_TEXTO = "TABLA_TIPO_DATO_TEXTO";
    public static final String TABLA_TIPO_DATO_TEXTO_LARGO = "TABLA_TIPO_DATO_TEXTO_LARGO";
    public static final String TABLA_TIPO_DATO_DESPLEGABLE = "TABLA_TIPO_DATO_DESPLEGABLE";
    public static final String TABLA_TIPO_DATO_FECHA = "TABLA_TIPO_DATO_FECHA";

    public static final String TABLA_TIPO_DATO_NUMERICO_TRAMITE = "TABLA_TIPO_DATO_NUMERICO_TRAMITE";
    public static final String TABLA_TIPO_DATO_TEXTO_TRAMITE = "TABLA_TIPO_DATO_TEXTO_TRAMITE";
    public static final String TABLA_TIPO_DATO_TEXTO_LARGO_TRAMITE = "TABLA_TIPO_DATO_TEXTO_LARGO_TRAMITE";
    public static final String TABLA_TIPO_DATO_DESPLEGABLE_TRAMITE = "TABLA_TIPO_DATO_DESPLEGABLE_TRAMITE";
    public static final String TABLA_TIPO_DATO_FECHA_TRAMITE = "TABLA_TIPO_DATO_FECHA_TRAMITE";
    /*public static final String TABLA_PAISES = "TABLA_PAISES";
    public static final String TABLA_PROVINCIAS = "TABLA_PROVINCIAS";*/
 /*public static final String TABLA_CPE_PUESTO = "TABLA_CPE_PUESTO";
    public static final String TABLA_CPE_OFERTA = "TABLA_CPE_OFERTA";
    public static final String TABLA_CPE_JUSTIFICACION = "TABLA_CPE_JUSTIFICACION";
     */
    public static final String TABLA_SUBVENCIONES = "TABLA_SUBVENCIONES";
    public static final String TABLA_E_DES_VAL = "TABLA_E_DES_VAL";
    //public static final String TABLA_CPE_SALARIOS = "TABLA_CPE_SALARIOS";
    public static final String TABLA_TIPO_DOCUMENTO = "TABLA_TIPO_DOCUMENTO";
    public static final String TABLA_INTEROP_VIDALABORAL = "TABLA_INTEROP_VIDALABORAL";
    public static final String VISTA_PROVINCIA = "VISTA_PROVINCIA";
    /*public static final String TABLA_REL_TERCERO_DOMICILIO = "TABLA_REL_TERCERO_DOMICILIO";
    public static final String TABLA_TERCEROS = "TABLA_TERCEROS";
    public static final String TABLA_E_EXP = "TABLA_E_EXP";
    public static final String TABLA_E_CRO = "TABLA_E_CRO";
    public static final String TABLA_TRAMITES = "TABLA_TRAMITES";
    //public static final String TABLA_CPE_HIST_PUESTO = "TABLA_CPE_HIST_PUESTO";
    //public static final String TABLA_CPE_HIST_OFERTA = "TABLA_CPE_HIST_OFERTA";
    //public static final String TABLA_CPE_HIST_JUSTIFICACION = "TABLA_CPE_HIST_JUSTIFICACION";
    public static final String TABLA_GEN_TITULACIONES ="TABLA_GEN_TITULACIONES";
    
    //Nombres de las secuencias en el fichero de propiedades.  
    //public static final String SEQ_CPE_OFERTA = "SEQ_CPE_OFERTA";
    
    //Nombres de los campos suplementarios en el fichero de propiedades
    /*public static final String CAMPO_SUPL_GESTOR_TRAMITADOR = "CAMPO_SUPL_GESTOR_TRAMITADOR";
    public static final String CAMPO_SUPL_EMPRESA = "CAMPO_SUPL_EMPRESA";
    public static final String CAMPO_SUPL_IMP_SOLICITADO = "CAMPO_SUPL_IMP_SOLICITADO";
    public static final String CAMPO_SUPL_IMP_CONVOCATORIA = "CAMPO_SUPL_IMP_CONVOCATORIA";
    public static final String CAMPO_SUPL_OTRAS_AYUDAS_SOLIC = "CAMPO_SUPL_OTRAS_AYUDAS_SOLIC";
    public static final String CAMPO_SUPL_OTRAS_AYUDAS_CONCE = "CAMPO_SUPL_OTRAS_AYUDAS_CONCE";
    public static final String CAMPO_SUPL_MINIMIS_SOLIC = "CAMPO_SUPL_MINIMIS_SOLIC";
    public static final String CAMPO_SUPL_MINIMIS_CONCE = "CAMPO_SUPL_MINIMIS_CONCE";
    public static final String CAMPO_SUPL_IMP_PREV_CON = "CAMPO_SUPL_IMP_PREV_CON";
    public static final String CAMPO_SUPL_IMP_CON = "CAMPO_SUPL_IMP_CON";
    public static final String CAMPO_SUPL_IMP_JUS = "CAMPO_SUPL_IMP_JUS";
    public static final String CAMPO_SUPL_IMP_JUS2 = "CAMPO_SUPL_IMP_JUS2";
    public static final String CAMPO_SUPL_IMP_NO_JUS = "CAMPO_SUPL_IMP_NO_JUS";
    public static final String CAMPO_SUPL_IMP_REN = "CAMPO_SUPL_IMP_REN";
    public static final String CAMPO_SUPL_IMP_REN_RES = "CAMPO_SUPL_IMP_REN_RES";
    public static final String CAMPO_SUPL_IMP_PAG = "CAMPO_SUPL_IMP_PAG";
    public static final String CAMPO_SUPL_IMP_PAG_RES = "CAMPO_SUPL_IMP_PAG_RES";
    public static final String CAMPO_SUPL_IMP_PAG_2 = "CAMPO_SUPL_IMP_PAG_2";
    public static final String CAMPO_SUPL_IMP_REI = "CAMPO_SUPL_IMP_REI";
    public static final String CAMPO_SUPL_IMP_DESP = "CAMPO_SUPL_IMP_DESP";
    public static final String CAMPO_SUPL_IMP_BAJA = "CAMPO_SUPL_IMP_BAJA";
    public static final String CAMPO_SUPL_CNAE = "CAMPO_SUPL_CNAE";
    public static final String CAMPO_SUPL_CAC = "CAMPO_SUPL_CAC";
    public static final String CAMPO_SUPL_TIPO_ENTIDAD = "CAMPO_SUPL_TIPO_ENTIDAD";
    public static final String CAMPO_SUPL_TER_HIS = "CAMPO_SUPL_TER_HIS";
    public static final String CAMPO_SUPL_IMP_BONIF = "CAMPO_SUPL_IMP_BONIF";
    
    public static final String CAMPO_SUPL_PUESTOS_SOLICITADOS = "CAMPO_SUPL_PUESTOS_SOLICITADOS";
    public static final String CAMPO_SUPL_PUESTOS_DENEGADOS = "CAMPO_SUPL_PUESTOS_DENEGADOS";
    public static final String CAMPO_SUPL_PUESTOS_CONTRATADOS = "CAMPO_SUPL_PUESTOS_CONTRATADOS";
    public static final String CAMPO_SUPL_PERSONAS_CONTRATADAS = "CAMPO_SUPL_PERSONAS_CONTRATADAS";
    public static final String CAMPO_SUPL_PUESTOS_DESPIDO = "CAMPO_SUPL_PUESTOS_DESPIDO";
    public static final String CAMPO_SUPL_PERSONAS_DESPIDO = "CAMPO_SUPL_PERSONAS_DESPIDO";
    public static final String CAMPO_SUPL_PUESTOS_BAJA = "CAMPO_SUPL_PUESTOS_BAJA";
    public static final String CAMPO_SUPL_PERSONAS_BAJA = "CAMPO_SUPL_PERSONAS_BAJA";
    public static final String CAMPO_SUPL_PUESTOS_RENUNCIADOS = "CAMPO_SUPL_PUESTOS_RENUNCIADOS";
    //public static final String CAMPO_SUPL_TOTAL_PUESTOS = "CAMPO_SUPL_TOTAL_PUESTOS";
     */
    public static final String CAMPO_SUPL_FEC_RES = "CAMPO_SUPL_FEC_RES";
    public static final String CAMPO_SUPL_FEC_PRE = "CAMPO_SUPL_FEC_PRE";
    public static final String CAMPO_SUPL_EDA_CON = "CAMPO_SUPL_EDA_CON";
    public static final String CAMPO_SUPL_COS_CON = "CAMPO_SUPL_COS_CON";
    public static final String CAMPO_SUPL_FEC_CON = "CAMPO_SUPL_FEC_CON";
    /*public static final String CAMPO_SUPL_RES_NOT_RES = "CAMPO_SUPL_RES_NOT_RES";
    public static final String CAMPO_SUPL_RES_PUESTOS_CONC = "CAMPO_SUPL_RES_PUESTOS_CONC";
    public static final String CAMPO_SUPL_RES_IMP_CONC = "CAMPO_SUPL_RES_IMP_CONC";
    public static final String CAMPO_SUPL_RES_IMP_PRIMER_PAG = "CAMPO_SUPL_RES_IMP_PRIMER_PAG";
    public static final String CAMPO_SUPL_RES_IMP_SEG_PAGO = "CAMPO_SUPL_RES_IMP_SEG_PAGO";
    public static final String CAMPO_SUPL_RES_FEC_RESOL = "CAMPO_SUPL_RES_FEC_RESOL";
    public static final String CAMPO_SUPL_RES_NUM_PUESTO = "CAMPO_SUPL_RES_NUM_PUESTO";
    public static final String CAMPO_SUPL_RES_IMP_RENUN = "CAMPO_SUPL_RES_IMP_RENUN";
    public static final String CAMPO_SUPL_RES_MOD_FEC_RES = "CAMPO_SUPL_RES_MOD_FEC_RES";
    public static final String CAMPO_SUPL_RES_MOD_NOT_RES = "CAMPO_SUPL_RES_MOD_NOT_RES";
    public static final String CAMPO_SUPL_RES_MOD_PUES_CONC = "CAMPO_SUPL_RES_MOD_PUES_CONC";
    public static final String CAMPO_SUPL_RES_MOD_IMP_CONC = "CAMPO_SUPL_RES_MOD_IMP_CONC";
    public static final String CAMPO_SUPL_RES_MOD_IMP_PRI_PAG = "CAMPO_SUPL_RES_MOD_IMP_PRI_PAG";
    public static final String CAMPO_SUPL_RES_MOD_SEG_PAG = "CAMPO_SUPL_RES_MOD_SEG_PAG";
    public static final String CAMPO_SUPL_RES_MOD_FEC_RENUN = "CAMPO_SUPL_RES_MOD_FEC_RENUN";
    public static final String CAMPO_SUPL_RES_MOD_PUES_REN = "CAMPO_SUPL_RES_MOD_PUES_REN";
    public static final String CAMPO_SUPL_RES_MOD_IMP_REN = "CAMPO_SUPL_RES_MOD_IMP_REN";
    public static final String CAMPO_SUPL_FE_AC_REN_RS = "CAMPO_SUPL_FE_AC_REN_RS";
    
     */

    //Nombres de los campos suplementarios desplegables en el fichero de propiedades
    //public static final String CAMPO_SUPL_TITULACION = "CAMPO_SUPL_TITULACION";
    /*public static final String CAMPO_SUPL_IDIOMA = "CAMPO_SUPL_IDIOMA";
    public static final String CAMPO_SUPL_NIVEL_IDIOMA = "CAMPO_SUPL_NIVEL_IDIOMA";
    public static final String CAMPO_SUPL_NIVEL_FORMATIVO = "CAMPO_SUPL_NIVEL_FORMATIVO";
     */
    public static final String CAMPO_MODALIDAD = "CAMPO_MODALIDAD";
    public static final String CAMPO_TITULACION = "CAMPO_TITULACION";
    public static final String CAMPO_SUPL_FEC_NAC = "CAMPO_SUPL_FEC_NAC";


    /*public static final String CAMPO_SUPL_RESULTADO = "CAMPO_SUPL_RESULTADO";
    public static final String CAMPO_SUPL_MOTIVO = "CAMPO_SUPL_MOTIVO";
    public static final String CAMPO_SUPL_OFICINAS_LANBIDE = "CAMPO_SUPL_OFICINAS_LANBIDE";
    public static final String CAMPO_SUPL_CAUSA_BAJA = "CAMPO_SUPL_CAUSA_BAJA";
    public static final String CAMPO_SUPL_ESTADO_JUSTIF = "CAMPO_SUPL_ESTADO_JUSTIF";
    public static final String CAMPO_SUPL_CAUSA_RENUNCIA = "CAMPO_SUPL_CAUSA_RENUNCIA";
    public static final String CAMPO_SUPL_CAUSA_RENUNCIA_PRES_OFERTA = "CAMPO_SUPL_CAUSA_RENUNCIA_PRES_OFERTA";*/
    //Tipos de datos campos suplementarios
    public static final int TIPO_DATO_NUMERICO = 1;
    public static final int TIPO_DATO_TEXTO = 2;
    public static final int TIPO_DATO_FECHA = 3;
    public static final int TIPO_DATO_DESPLEGABLE = 6;

    /*//Codigos resultados puesto
    public static final String CODIGO_RESULTADO_CONCEDIDO = "001";
    public static final String CODIGO_RESULTADO_RENUNCIA = "002";
    public static final String CODIGO_RESULTADO_DENEGADO = "003";
    public static final String CODIGO_RESULTADO_NO_EVALUADO = "004";
    
    //Codigos causa baja
    public static final String CODIGO_CAUSA_BAJA_CESE_VOLUNTARIO = "001";
    public static final String CODIGO_CAUSA_BAJA_DESPIDO = "002";
    public static final String CODIGO_CAUSA_BAJA_NO_SUPERA_PRUEBA = "003";
    public static final String CODIGO_CAUSA_BAJA_INVALIDEZ = "004";
    public static final String CODIGO_CAUSA_BAJA_FALLECIMIENTO = "005";
    public static final String CODIGO_CAUSA_BAJA_RENUNCIA = "006";
    
    //Codigos estado justificacion
    public static final String CODIGO_ESTADO_JUSTIFICADO = "001";
    public static final String CODIGO_ESTADO_RENUNCIA = "002";
     */
    //Roles
    public static final String CODIGO_ROL_ENTIDAD_SOLICITANTE = "1";

    //OTROS
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String CIERTO = "S";
    public static final String FALSO = "N";
    public static final String CERO_CON_DECIMALES = "0.00";

    public static final int ESTADO_EXPEDIENTE_CERRADO = 9;
    public static final int ESTADO_EXPEDIENTE_ANULADO = 1;

    //Codigos de los tramites en el fichero de propiedades
    //public static final String CODIGO_TRAMITE_RESOLUCION = "TRAM_RESOLUCION";
    //public static final String CODIGO_TRAMITE_RESOLUCION_MODIF = "TRAM_RESOLUCION_MODIF";
    public static final String MELANBIDE67_EMPRESAS_SQ = "MELANBIDE67_EMPRESAS_SQ";

    public static final String TABLA_SUBSOLIC = "TABLA_SUBSOLIC";
    public static final String SEQ_SUBSOLIC = "SEQ_SUBSOLIC";
    public static final String CAMPO_ESTADO = "CAMPO_ESTADO";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;

    public class CODIGOS_SEXO {

        public static final int HOMBRE = 0;
        public static final int MUJER = 1;
    }

    public static final String TRAMITE_200 = "TRAMITE_200";
    public static final String TRAMITE_260 = "TRAMITE_260";
    public static final String TRAMITE_310 = "TRAMITE_310";
    public static final String TRAMITE_320 = "TRAMITE_320";
    public static final String TABLA_E_CRO = "E_CRO";

    public static final String NOMBRE_PROCEDIMIENTO = "NOMBRE_PROCEDIMIENTO";

    public static final String URL_CV_WS = "URL_CV_WS";

    public static final String TIPO_DNI_CV_WS = "TIPO_DNI_CV_WS";
    public static final String TIPO_NIE_CV_WS = "TIPO_NIE_CV_WS";
    public static final String DOCUMENTO_CV = "DOCUMENTO_CV";
    
    public static final String URL_VIDALABORAL_WS = "URL_VIDALABORAL_WS";
    public static final String COD_PROCEDIMIENTO_TRAMITADOR = "COD_PROCEDIMIENTO_TRAMITADOR";
    public static final String NOMBRE_PROCEDIMIENTO_TRAMITADOR = "NOMBRE_PROCEDIMIENTO_TRAMITADOR";
    public static final String FINALIDAD_PROCEDIMIENTO_TRAMITADOR = "FINALIDAD_PROCEDIMIENTO_TRAMITADOR";
    public static final String CONSENTIMIENTO_FIRMADO = "CONSENTIMIENTO_FIRMADO";
    public static final String NIF_USUARIO_TRAMITADOR = "NIF_USUARIO_TRAMITADOR";
    public static final String USUARIO_TRAMITADOR = "USUARIO_TRAMITADOR";
    public static final String TIPO_DNI_VIDA_LABORAL = "TIPO_DNI_VIDA_LABORAL";
    public static final String TIPO_NIE_VIDA_LABORAL = "TIPO_NIE_VIDA_LABORAL";
    public static final String FORMATO_FECHA_WS_VIDA_LABORAL = "FORMATO_FECHA_WS_VIDA_LABORAL";
}
