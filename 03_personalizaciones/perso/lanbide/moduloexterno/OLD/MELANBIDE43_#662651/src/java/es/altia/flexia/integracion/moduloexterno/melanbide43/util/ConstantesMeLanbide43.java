/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.util;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0 Historial de cambios:
 * <ol>
 * <li>david.caamano * 16-08-2012 * #86969 Edici�n inicial</li>
 * </ol>
 */
public class ConstantesMeLanbide43 {

    //Fichero de propiedades del modulo.
    public static String FICHERO_PROPIEDADES = "MELANBIDE43";
    public static String FICHERO_ADAPTADORES = "AdaptadoresPlateaLan6";

    //Barra separadora
    public static String BARRA_SEPARADORA = "/";

    //Separador nombre archivos
    public static String SEPARADOR_NOMBRE_ARCHIVOS = "-";

    //Constante para la clave del fichero de propiedades que indica el nombre del modulo.
    public static String NOMBRE_MODULO = "NOMBRE_MODULO";

    public static String MELANBIDE43_CERTIFICADO = "TABLA_MELANBIDE43_CERTIFICADO";
    public static String MELANBIDE43_CERT_CENTRO = "TABLA_MELANBIDE43_CERT_CENTRO";
    public static String CERT_CENTROS = "TABLA_CER_CENTROS";
    public static String MELANBIDE43_S_CERTIFICADOS = "TABLA_MELANBIDE43_S_CERTIFICADOS";

    public static String TABLA_EXPEDIENTES = "TABLA_EXPEDIENTES";
    public static String TABLA_TERCEROS = "TABLA_TERCEROS";
    public static String TABLA_EXPTERCEROS = "TABLA_EXPTERCEROS";
    public static String TABLA_REGISTRO = "TABLA_REGISTRO";
    public static String TABLA_EXR = "TABLA_EXR";

    public static final String TABLA_TIPO_DATO_NUMERICO = "TABLA_TIPO_DATO_NUMERICO";
    public static final String TABLA_TIPO_DATO_TEXTO = "TABLA_TIPO_DATO_TEXTO";
    public static final String TABLA_CONTADORES = "TABLA_CONTADORES";
    public static final String TABLA_VALORES_DESPLEGABLE = "TABLA_VALORES_DESPLEGABLE";

    //Constantes para acceder a los ficheros de propiedades
    public static final String CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_CP = "CLAVEREGISTRALCP";
    public static final String CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_APA = "CLAVEREGISTRALAPA";

    //Pares de claves para indicar si un centro esta acreditado o no.
    public static Integer CENTRO_ACREDITADO_SI = 0;
    public static Integer CENTRO_ACREDITADO_NO = 1;

    public static final String FORMATO_FECHA = "dd/MM/yyyy";

    //cei
    public static String TRAM_CEI_SEGUNDO_PAGO = "TRAM_CEI_SEGUNDO_PAGO";
    public static String TRAM_CEI_TERCER_PAGO = "TRAM_CEI_TERCER_PAGO";
    public static String ID_INSTANCIA_PAGO2_LAN68_CEI = "ID_INSTANCIA_PAGO2_LAN68_CEI";
    public static String ID_INSTANCIA_PAGO3_LAN68_CEI = "ID_INSTANCIA_PAGO3_LAN68_CEI";

    //sei
    public static String TRAM_SEI_PRIMER_PAGO = "TRAM_SEI_PRIMER_PAGO";
    public static String TRAM_SEI_1SEM_2AN = "TRAM_SEI_1SEM_2AN";
    public static String TRAM_SEI_2SEM_2AN = "TRAM_SEI_2SEM_2AN";
    public static String TRAM_SEI_1SEM_3AN = "TRAM_SEI_1SEM_3AN";
    public static String TRAM_SEI_2SEM_3AN = "TRAM_SEI_2SEM_3AN";
    public static String TRAM_SEI_JUS_FINAL = "TRAM_SEI_JUS_FINAL";
    public static String TRAM_SEI_1SEM_4AN = "TRAM_SEI_1SEM_4AN";
    public static String TRAM_SEI_2SEM_4AN = "TRAM_SEI_2SEM_4AN";
    public static String TRAM_SEI_JUS_FIN_4AN = "TRAM_SEI_JUS_FIN_4AN";
    public static String TRAM_SEI_1PAGO_FOR = "TRAM_SEI_1PAGO_FOR";
    public static String TRAM_UAAP_DOCU_AVAL = "TRAM_UAAP_DOCU_AVAL";
    public static String TRAM_UAAP_DOCU_LIQUIDA = "TRAM_UAAP_DOCU_LIQUIDA";

    public static String ID_INSTANCIA_PAGO1_LAN68_SEI = "ID_INSTANCIA_PAGO1_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_SEM1_ANU2_LAN68_SEI = "ID_INSTANCIA_PAGO_SEM1_ANU2_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_SEM2_ANU2_LAN68_SEI = "ID_INSTANCIA_PAGO_SEM2_ANU2_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_SEM1_ANU3_LAN68_SEI = "ID_INSTANCIA_PAGO_SEM1_ANU3_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_SEM2_ANU3_LAN68_SEI = "ID_INSTANCIA_PAGO_SEM2_ANU3_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_FINAL_ANU3_LAN68_SEI = "ID_INSTANCIA_PAGO_FINAL_ANU3_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_SEM1_ANU4_LAN68_SEI = "ID_INSTANCIA_PAGO_SEM1_ANU4_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_SEM2_ANU4_LAN68_SEI = "ID_INSTANCIA_PAGO_SEM2_ANU4_LAN68_SEI";
    public static String ID_INSTANCIA_PAGO_FINAL_ANU4_LAN68_SEI = "ID_INSTANCIA_PAGO_FINAL_ANU4_LAN68_SEI";
    public static String ID_INSTANCIA_DOC_AVAL_LAN62_UAAP = "ID_INSTANCIA_DOC_AVAL_LAN62_UAAP";
    public static String ID_INSTANCIA_DOC_LIQ_LAN62_UAAP = "ID_INSTANCIA_DOC_LIQ_LAN62_UAAP";

    //REPLE
    public static String TRAM_REPLE_RECEPDOC_PAGO2 = "TRAM_REPLE_RECEPDOC_PAGO2";
    public static String ID_INSTANCIA_PAGO2_LAN62_REPLE = "ID_INSTANCIA_PAGO2_LAN62_REPLE";
    public static String TRAM_REPLE_REQDOC_PAGO2 = "TRAM_REPLE_REQDOC_PAGO2";
    public static String TRAM_REPLE_REQSUB_PAGO2 = "TRAM_REPLE_REQSUB_PAGO2";
    public static String TRAM_REPLE_RECEP_PLANTILLA = "TRAM_REPLE_RECEP_PLANTILLA";
    public static String ID_INSTANCIA_PLANTILLA_LAN62_REPLE = "ID_INSTANCIA_PLANTILLA_LAN62_REPLE";
    public static String TRAM_REPLE_COMUNICA_PLANTILLA = "TRAM_REPLE_COMUNICA_PLANTILLA";
    public static String TRAM_REPLE_REQSUB_PLANTILLA = "TRAM_REPLE_REQSUB_PLANTILLA";

    //CUOTS
    public static String TRAM_CUOTS_PAGO1 = "TRAM_CUOTS_PAGO1";

    //ORI14
    public static String TRAM_ORI14_ESP_RECEP_DOC_SUBS_SOL = "TRAM_ORI14_ESP_RECEP_DOC_SUBS_SOL";
    public static String TRAM_ORI14_ESP_APER_DOC_SOL_ALTA = "TRAM_ORI14_ESP_APER_DOC_SOL_ALTA";
    public static String TRAM_ORI14_ESP_APER_DOC_JUSTIF = "TRAM_ORI14_ESP_APER_DOC_JUSTIF";
    public static String TRAM_ORI14_ESP_RECEP_DOC_INI_ACT = "TRAM_ORI14_ESP_RECEP_DOC_INI_ACT";
    public static String TRAM_ORI14_ESP_RECEP_SOL_DEC_INI_ACT = "TRAM_ORI14_ESP_RECEP_SOL_DEC_INI_ACT";
    public static String TRAM_ORI_RESOLUCION_DEFINITIVA_COD_VISIBLE = "TRAM_ORI_RESOLUCION_DEFINITIVA_COD_VISIBLE";
    public static String TRAM_ORI_ESPERA_ACUSE_DESIS_COD_VISIBLE = "TRAM_ORI_ESPERA_ACUSE_DESIS_COD_VISIBLE";
    public static String TRAM_ORI_ESP_RECEP_DOC_ALEG_RES_PROV = "TRAM_ORI_ESP_RECEP_DOC_ALEG_RES_PROV";
    public static String TRAM_ORI_ESPERA_PRESENTACION_RECURSOS = "TRAM_ORI_ESPERA_PRESENTACION_RECURSOS";

    public static String ID_INSTANCIA_APOR_ALTA_PER_ORI14 = "ID_INSTANCIA_APOR_ALTA_PER_ORI14";
    public static String ID_INSTANCIA_APOR_DOC_TRAM_ORI14 = "ID_INSTANCIA_APOR_DOC_TRAM_ORI14";
    public static String ID_INSTANCIA_APOR_INI_ACTIVIDAD_ORI14 = "ID_INSTANCIA_APOR_INI_ACTIVIDAD_ORI14";
    public static String ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_ORI14 = "ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_ORI14";
    public static String ID_INSTANCIA_APOR_DOC_ALEG_ORI = "ID_INSTANCIA_APOR_DOC_ALEG_ORI";

    //AperturaEsperaNuevoPersonalJob:generarMisGestionesAvance()
    public static String TRAM_ORI14_ENV_ORDEN_PRIMER_PAGO = "TRAM_ORI14_ENV_ORDEN_PRIMER_PAGO";

    //CEMP
    public static String TRAM_CEMP_ESP_RECEP_DOC_SUBS_SOL = "TRAM_CEMP_ESP_RECEP_DOC_SUBS_SOL";
    public static String TRAM_CEMP_ESP_APER_DOC_SOL_ALTA = "TRAM_CEMP_ESP_APER_DOC_SOL_ALTA";
    public static String TRAM_CEMP_ESP_APER_DOC_JUSTIF = "TRAM_CEMP_ESP_APER_DOC_JUSTIF";
    public static String TRAM_CEMP_ESP_RECEP_DOC_INI_ACT = "TRAM_CEMP_ESP_RECEP_DOC_INI_ACT";
    public static String TRAM_CEMP_ESP_RECEP_SOL_DEC_INI_ACT = "TRAM_CEMP_ESP_RECEP_SOL_DEC_INI_ACT";
    public static String TRAM_CEMP_RESOLUCION_DEFINITIVA_COD_VISIBLE = "TRAM_CEMP_RESOLUCION_DEFINITIVA_COD_VISIBLE";
    public static String TRAM_CEMP_ESPERA_ACUSE_DESIS_COD_VISIBLE = "TRAM_CEMP_ESPERA_ACUSE_DESIS_COD_VISIBLE";
    public static String TRAM_CEMP_ESP_RECEP_DOC_ALEG_RES_PROV = "TRAM_CEMP_ESP_RECEP_DOC_ALEG_RES_PROV";
    public static String TRAM_CEMP_ESPERA_PRESENTACION_RECURSOS = "TRAM_CEMP_ESPERA_PRESENTACION_RECURSOS";

    public static String ID_INSTANCIA_APOR_ALTA_PER_CEMP = "ID_INSTANCIA_APOR_ALTA_PER_CEMP";
    public static String ID_INSTANCIA_APOR_DOC_TRAM_CEMP = "ID_INSTANCIA_APOR_DOC_TRAM_CEMP";
    public static String ID_INSTANCIA_APOR_INI_ACTIVIDAD_CEMP = "ID_INSTANCIA_APOR_INI_ACTIVIDAD_CEMP";
    public static String ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_CEMP = "ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_CEMP";
    public static String ID_INSTANCIA_APOR_DOC_ALEG_CEMP = "ID_INSTANCIA_APOR_DOC_ALEG_CEMP";
    //AperturaEsperaNuevoPersonalJob:generarMisGestionesAvance()
    public static String TRAM_CEMP_ENV_ORDEN_PRIMER_PAGO = "TRAM_CEMP_ENV_ORDEN_PRIMER_PAGO";

    //COLEC
    public static String TRAM_COLEC_ESP_RECEP_DOC_SUBS_SOL = "TRAM_COLEC_ESP_RECEP_DOC_SUBS_SOL";
    public static String TRAM_COLEC_ESP_APER_DOC_SOL_ALTA = "TRAM_COLEC_ESP_APER_DOC_SOL_ALTA";
    public static String TRAM_COLEC_ESP_APER_DOC_JUSTIF = "TRAM_COLEC_ESP_APER_DOC_JUSTIF";
    public static String TRAM_COLEC_ESP_RECEP_DOC_INI_ACT = "TRAM_COLEC_ESP_RECEP_DOC_INI_ACT";
    public static String TRAM_COLEC_ESP_RECEP_SOL_DEC_INI_ACT = "TRAM_COLEC_ESP_RECEP_SOL_DEC_INI_ACT";
    public static String TRAM_COLEC_RESOLUCION_DEFINITIVA_COD_VISIBLE = "TRAM_COLEC_RESOLUCION_DEFINITIVA_COD_VISIBLE";
    public static String TRAM_COLEC_ESPERA_ACUSE_DESIS_COD_VISIBLE = "TRAM_COLEC_ESPERA_ACUSE_DESIS_COD_VISIBLE";
    public static String TRAM_COLEC_ESP_RECEP_DOC_ALEG_RES_PROV = "TRAM_COLEC_ESP_RECEP_DOC_ALEG_RES_PROV";
    public static String TRAM_COLEC_ESPERA_PRESENTACION_RECURSOS = "TRAM_COLEC_ESPERA_PRESENTACION_RECURSOS";

    public static String ID_INSTANCIA_APOR_ALTA_PER_COLEC = "ID_INSTANCIA_APOR_ALTA_PER_COLEC";
    public static String ID_INSTANCIA_APOR_DOC_TRAM_COLEC = "ID_INSTANCIA_APOR_DOC_TRAM_COLEC";
    public static String ID_INSTANCIA_APOR_INI_ACTIVIDAD_COLEC = "ID_INSTANCIA_APOR_INI_ACTIVIDAD_COLEC";
    public static String ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_COLEC = "ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_COLEC";
    public static String ID_INSTANCIA_APOR_DOC_ALEG_COLEC = "ID_INSTANCIA_APOR_DOC_ALEG_COLEC";
    //AperturaEsperaNuevoPersonalJob:generarMisGestionesAvance()
    public static String TRAM_COLEC_ENV_ORDEN_PRIMER_PAGO = "TRAM_COLEC_ENV_ORDEN_PRIMER_PAGO";

    //CONCM
    public static String TRAM_CONCM_RESOLUCION = "TRAM_CONCM_RESOLUCION";
    public static String TRAM_CONCM_EST_TECNICO = "TRAM_CONCM_EST_TECNICO";

    //DECEX
    public static String TRAM_DECEX_RECEP_MODIF = "TRAM_DECEX_RECEP_MODIF";
    public static String TRAM_DECEX_RECEP_REG_ENT_3M = "TRAM_DECEX_RECEP_REG_ENT_3M";
    public static String TRAM_DECEX_RECEP_REG_ENT_A1 = "TRAM_DECEX_RECEP_REG_ENT_A1";
    public static String TRAM_DECEX_RECEP_REG_ENT_A2 = "TRAM_DECEX_RECEP_REG_ENT_A2";
    public static String TRAM_DECEX_RECEP_REG_ENT_A3 = "TRAM_DECEX_RECEP_REG_ENT_A3";

    public static String ID_INSTANCIA_APOR_MOD_LAN62_DECEX = "ID_INSTANCIA_APOR_MOD_LAN62_DECEX";
    public static String ID_INSTANCIA_APOR_3MESES_LAN62_DECEX = "ID_INSTANCIA_APOR_3MESES_LAN62_DECEX";
    public static String ID_INSTANCIA_APOR_ANU1_LAN62_DECEX = "ID_INSTANCIA_APOR_ANU1_LAN62_DECEX";

    //AERTE
    public static String TRAM_AERTE_ESPERA_PRESENTACION_RECURSOS = "TRAM_AERTE_ESPERA_PRESENTACION_RECURSOS";
    public static String TRAM_AERTE_ESPERA_PRESENTACION_RECURSOS_COD_VISIBLE = "TRAM_AERTE_ESPERA_PRESENTACION_RECURSOS_COD_VISIBLE";
    public static String TRAM_AERTE_APERTURA_RENUNCIA = "TRAM_AERTE_APERTURA_RENUNCIA";
    public static String TRAM_AERTE_APERTURA_RENUNCIA_COD_VISIBLE = "TRAM_AERTE_APERTURA_RENUNCIA_COD_VISIBLE";
    public static String TRAM_AERTE_CIERRE_DESISTIMIENTO = "TRAM_AERTE_CIERRE_DESISTIMIENTO";
    public static String TRAM_AERTE_NOTIFICACION_RESOLUCION = "TRAM_AERTE_NOTIFICACION_RESOLUCION";
    public static String TRAM_AERTE_NOTIFICACION_RESOLUCION_COD_VISIBLE = "TRAM_AERTE_NOTIFICACION_RESOLUCION_COD_VISIBLE";
    public static String TRAM_AERTE_ACUSE_NOTIFICACION_RESOLUCION = "TRAM_AERTE_ACUSE_NOTIFICACION_RESOLUCION";
    public static String TRAM_AERTE_ESPERA_ACUSE_DESIS_COD_VISIBLE = "TRAM_AERTE_ESPERA_ACUSE_DESIS_COD_VISIBLE";
    public static String CAMPO_AERTE_FECACUSENOT = "CAMPO_AERTE_FECACUSENOT";

    //ATASE
    public static String TRAM_ATASE_ESPERA_PRESENTACION_RECURSOS = "TRAM_ATASE_ESPERA_PRESENTACION_RECURSOS";
    public static String TRAM_ATASE_ESPERA_PRESENTACION_RECURSOS_COD_VISIBLE = "TRAM_ATASE_ESPERA_PRESENTACION_RECURSOS_COD_VISIBLE";

    //codigos de los procedimientos
    public static String ID_PROC_QUEJA =    "PROCEDIMIENTO_ID_LAN61_QUEJA";
    public static String ID_PROC_CONC =     "PROCEDIMIENTO_ID_LAN62_CONC";
    public static String ID_PROC_REGC =     "PROCEDIMIENTO_ID_LAN63_REGC";
    public static String ID_PROC_REGE =     "PROCEDIMIENTO_ID_LAN63_REGE";
    public static String ID_PROC_RECUR =    "PROCEDIMIENTO_ID_LAN64_RECUR";
    public static String ID_PROC_AACC =     "PROCEDIMIENTO_ID_LAN65_AACC";
    public static String ID_PROC_AACCB =    "PROCEDIMIENTO_ID_LAN65_AACCB";
    public static String ID_PROC_AACCR =    "PROCEDIMIENTO_ID_LAN65_AACCR";
    public static String ID_PROC_CEI =      "PROCEDIMIENTO_ID_LAN68_CEI";
    public static String ID_PROC_SEI =      "PROCEDIMIENTO_ID_LAN68_SEI";
    public static String ID_PROC_LEI =      "PROCEDIMIENTO_ID_LAN67_LEI";
    public static String ID_PROC_CUOTA =    "PROCEDIMIENTO_ID_LAN66_CUOTA";
    public static String ID_PROC_CEECS =    "PROCEDIMIENTO_ID_LAN62_CEECS";
    public static String ID_PROC_DISCP =    "PROCEDIMIENTO_ID_LAN62_DISCA";
    public static String ID_PROC_SUBAF =    "PROCEDIMIENTO_ID_LAN11_SUBAF";
    public static String ID_PROC_REPLE =    "PROCEDIMIENTO_ID_LAN62_REPLE";
    public static String ID_PROC_SUENC =    "PROCEDIMIENTO_ID_LAN11_SUENC";
    public static String ID_PROC_UAAP =     "PROCEDIMIENTO_ID_LAN62_UAAP";
    public static String ID_PROC_ORI14 =    "PROCEDIMIENTO_ID_LAN62_ORI14";
    public static String ID_PROC_COLEC =    "PROCEDIMIENTO_ID_LAN62_COLEC";
    public static String ID_PROC_CEMP =     "PROCEDIMIENTO_ID_LAN62_CEMP";
    public static String ID_PROC_LEAUK =    "PROCEDIMIENTO_ID_LAN62_LEAUK";
    public static String ID_PROC_DECEX =    "PROCEDIMIENTO_ID_LAN62_DECEX";
    public static String ID_PROC_COCUR =    "PROCEDIMIENTO_ID_LAN62_COCUR";
    public static String ID_PROC_REJUV =    "PROCEDIMIENTO_ID_LAN62_REJUV";
    public static String ID_PROC_DLDUR =    "PROCEDIMIENTO_ID_LAN62_DLDUR";
    public static String ID_PROC_ECA =      "PROCEDIMIENTO_ID_LAN62_ECA";
    public static String ID_PROC_CCEE =     "PROCEDIMIENTO_ID_LAN62_CCEE";
    public static String ID_PROC_LAKCC =    "PROCEDIMIENTO_ID_LAN62_LAKCC";
    public static String ID_PROC_ATASE =    "PROCEDIMIENTO_ID_LAN62_ATASE";
    public static String ID_PROC_APEC =     "PROCEDIMIENTO_ID_LAN62_APEC";
    public static String ID_PROC_APES =     "PROCEDIMIENTO_ID_LAN62_APES";
    public static String ID_PROC_DISCT =    "PROCEDIMIENTO_ID_LAN62_DISCT";
    public static String ID_PROC_ACASE =    "PROCEDIMIENTO_ID_LAN62_ACASE";
    public static String ID_PROC_LPEEL =    "PROCEDIMIENTO_ID_LAN62_LPEEL";
    public static String ID_PROC_LPEPE =    "PROCEDIMIENTO_ID_LAN62_LPEPE";
    public static String ID_PROC_LPEAL =    "PROCEDIMIENTO_ID_LAN62_LPEAL";
    public static String ID_PROC_GEL =      "PROCEDIMIENTO_ID_LAN62_GEL";
    public static String ID_PROC_AEXCE =    "PROCEDIMIENTO_ID_LAN62_AEXCE";
    public static String ID_PROC_AERTE =    "PROCEDIMIENTO_ID_LAN62_AERTE";
    public static String ID_PROC_LAKOF =    "PROCEDIMIENTO_ID_LAN62_LAKOF";
    public static String ID_PROC_ENTAP =    "PROCEDIMIENTO_ID_LAN62_ENTAP";
    public static String ID_PROC_IGCEE =    "PROCEDIMIENTO_ID_LAN62_IGCEE";
    public static String ID_PROC_SPROS =    "PROCEDIMIENTO_ID_LAN11_SPROS";
    public static String ID_PROC_SAFCC =    "PROCEDIMIENTO_ID_LAN11_SAFCC";
    public static String ID_PROC_SMUJF =    "PROCEDIMIENTO_ID_LAN11_SMUJF";
    public static String ID_PROC_SMUJC =    "PROCEDIMIENTO_ID_LAN11_SMUJC";
    public static String ID_PROC_HEZFE =    "PROCEDIMIENTO_ID_LAN11_HEZFE";
    public static String ID_PROC_HEZFC =    "PROCEDIMIENTO_ID_LAN11_HEZFC";
    public static String ID_PROC_EHABE =    "PROCEDIMIENTO_ID_LAN11_EHABE";
    public static String ID_PROC_SFJBC =    "PROCEDIMIENTO_ID_LAN11_SFJBC";
    public static String ID_PROC_PEX =      "PROCEDIMIENTO_ID_LAN62_PEX";
    public static String ID_PROC_IKER =     "PROCEDIMIENTO_ID_LAN62_IKER";
    public static String ID_PROC_COLVU =    "PROCEDIMIENTO_ID_LAN62_COLVU";
    public static String ID_PROC_TRECO =    "PROCEDIMIENTO_ID_LAN62_TRECO";
    public static String ID_PROC_MCD =      "PROCEDIMIENTO_ID_LAN11_MCD";
    public static String ID_PROC_NNE =      "PROCEDIMIENTO_ID_LAN11_NNE";
    public static String ID_PROC_MRU =      "PROCEDIMIENTO_ID_LAN11_MRU";
    public static String ID_PROC_APEA =     "PROCEDIMIENTO_ID_LAN62_APEA";
    public static String ID_PROC_APEI =     "PROCEDIMIENTO_ID_LAN62_APEI";
    public static String ID_PROC_RYU =      "PROCEDIMIENTO_ID_LAN11_RYU";
    public static String ID_PROC_CEPAP =    "PROCEDIMIENTO_ID_LAN11_CEPAP";
    public static String ID_PROC_GO =       "PROCEDIMIENTO_ID_LAN62_GO";
    public static String ID_PROC_SUOPO =    "PROCEDIMIENTO_ID_LAN11_SUOPO";
    public static String ID_PROC_ALDEM =    "PROCEDIMIENTO_ID_LAN11_ALDEM";
    public static String ID_PROC_ININ =     "PROCEDIMIENTO_ID_LAN11_ININ";
    public static String ID_PROC_OPDE =     "PROCEDIMIENTO_ID_LAN11_OPDE";
    public static String ID_PROC_IPACR =    "PROCEDIMIENTO_ID_LAN11_IPACR";
    public static String ID_PROC_AGRED =    "PROCEDIMIENTO_ID_LAN11_AGRED";
    public static String ID_PROC_TF =       "PROCEDIMIENTO_ID_LAN11_TF";
    public static String ID_PROC_PREST =    "PROCEDIMIENTO_ID_LAN62_PREST";
    public static String ID_PROC_IMV =      "PROCEDIMIENTO_ID_LAN33_IMV";
    public static String ID_PROC_RGI =      "PROCEDIMIENTO_ID_LAN31_RGI";
    public static String ID_PROC_EADN =     "PROCEDIMIENTO_ID_LAN11_EADN";
    public static String ID_PROC_DLD50 =     "PROCEDIMIENTO_ID_LAN62_DLD50";
    public static String ID_PROC_AAEEF =     "PROCEDIMIENTO_ID_LAN11_AAEEF";
    public static String ID_PROC_PRMEM =     "PROCEDIMIENTO_ID_LAN62_PRMEM";

    public static String USU_TRAMI = "USU_TRAMI";

    public final class CODIGOS_DESPLEGABLE {

        public static final String TIPO_ACREDITACION = "CPAP";
        public static final String VALORACION = "VPAP";

    }

    public static String RECHAZADA = "R";
    public static String ACEPTADA = "A";

    public final class ROLES {

        public static final int INTERESADO = 1;
    }

    //JOB
    public static String CAMPO_SERVIDOR = "SERVIDOR";
    public static String COD_ORG = "COD_ORG";
    public static String DOS_ENTORNOS = "DOS_ENTORNOS";

    //codigo del procedimiento Desarrollo 0
    public static String ID_PROC_DESARROLLO_CERO = "PROCEDIMIENTO_ID_DESARROLLO_CERO";

    /**
     * Indica el numero maximo de linea a imprimir en el excel que se genera al
     * imprimir los resultados de la pantalla LLamadas a MIs Gestiones
     */
    public static final String MIS_GESTIONES_LIMITE_LINEAS_HOJA_EXCEL = "MIS_GESTIONES_LIMITE_LINEAS_HOJA_EXCEL";

    // Lista de procedimientos que inician expedientes en Mi Carpeta sin tener Registro de Entrada      
    public static String PROC_SIN_SOLICITUD = "PROC_SIN_SOLICITUD";

    // Campos Suplementario FECHA APORTACION/ENTREGA DE DOCUMENTACION
    public static String COD_CAMPO_FECHA_ENTREGA_DOC_FECENTDOC = "COD_CAMPO_FECHA_ENTREGA_DOC_FECENTDOC";

}//class
