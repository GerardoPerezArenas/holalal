<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerCertificadoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerUnidadeCompetencialVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerModuloFormativoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03"%>
<%@page import="java.util.ArrayList" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");
    String cargarDatos = (String) request.getAttribute("cargarDatos");
    boolean btnExp = (Boolean)request.getAttribute("botonExpedientes");
    
    //solo para avanzar expediente espedicion
    String esquemaBBDD = (String) request.getAttribute("esquemaBBDD");
    

    String codCertificado = "";
    String area = "";
    String familia = "";
    String decreto = "";
    String nivel = "";
    String moduloAcreditado = "";
    String motivoModuloAcreditado = "";
    ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
    
    if(cargarDatos.equalsIgnoreCase("S")){
        CerCertificadoVO certificado = (CerCertificadoVO) request.getAttribute("certificado");
        CerModuloFormativoVO moduloFormativo = (CerModuloFormativoVO) request.getAttribute("modulo");
        codCertificado = certificado.getCodCertificado();
        area = certificado.getArea().getDescAreaE();
        familia = certificado.getArea().getFamilia().getDescFamiliaE();        
        decreto = certificado.getDecreto();
        nivel = String.valueOf(certificado.getNivel());
        if(moduloFormativo.getModuloAcreditado() != null){
            moduloAcreditado = String.valueOf(moduloFormativo.getModuloAcreditado());
        }//if(moduloFormativo.getModuloAcreditado() != null)
        if(moduloFormativo.getCodMotivoAcreditacion() != null){
           motivoModuloAcreditado = moduloFormativo.getCodMotivoAcreditacion(); 
        }//if(moduloFormativo.getCodMotivoAcreditacion() != null)
        
    }//if(cargarDatos.equalsIgnoreCase("S"))
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide03/melanbide03.css'/>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<!--nuevas-->
<%
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idioma = 1;
    int apl = 5;
    String css = "";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idioma = usuarioVO.getIdioma();
        css = usuarioVO.getCss();
}%>
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<!--script type="text/javascript" src="<//%=request.getContextPath()%>/scripts/general.js"></script-->
<!--<script type="text/javascript"  src="<//%=request.getContextPath()%>/scripts/domlay.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listas.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script><script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script><link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide03/TablaNueva.js?v=2"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript">
    var nombreModuloLicitacion = "<%=nombreModulo%>";
    var numExpLicitacion = "<%=numExpediente%>";
    var codOrganizacion = "<%=codOrganizacion%>";
    //Solo para avanzar expediente
    var esquemaBBDD = "<%=esquemaBBDD%>";
    var codProcedimiento = "<%=codProcedimiento%>";
    var cargarDatos = "<%=cargarDatos%>";

    var comboListaCertificados;
    var comboAcreditadoModFormacion;
    var comboMotivoAcreditadoModFormacion;
    var comboMotivoNoAcreditadoModFormacion;
    var tablaUnidadesCompetenciales;

    var listaCodigosCertificados = new Array();
    var listaDescripcionesCertificados = new Array();

    var listaCodigosCentros = new Array();
    var listaDescripcionesCentros = new Array();

    var listaCodigosCentrosLanF = new Array();
    var listaDescripcionesCentrosLanF = new Array();

    var codigosUnidadesDocumentales = new Array();

    var listaCodigosOrigenAcreditacion = new Array();
    var listaDescripcionesOrigenAcreditacion = new Array();

    var listaCodigosMotivoNoAcreditado = new Array();
    var listaDescripcionesMotivoNoAcreditado = new Array();

    var listaCodigosMotivosAcreditadoModForm = new Array();
    var listaDescripcionesMotivosAcreditadoModForm = new Array();

    var listaCodigosMotivosNoAcreditadoModForm = new Array();
    var listaDescripcionesMotivosNoAcreditadoModForm = new Array();

    var unidadCompetencialBloqueada = new Array();

    var listaSiNo = new Array();
    var listaCodigosSiNo = new Array();

    listaCodigosSiNo[0] = 0;
    listaCodigosSiNo[1] = 1;

    listaSiNo[0] = '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.SI")%>';
    listaSiNo[1] = '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.NO")%>';

    <% if(cargarDatos.equalsIgnoreCase("S")){ %>
    function cargarDatosCert() {
        var codCertificado = "<%= codCertificado%>";
        var moduloAcreditado = "<%=moduloAcreditado%>";
        var motivoModuloAcreditado = "<%=motivoModuloAcreditado%>";

        buscaCodigoCertificado(codCertificado);

        var codigosUnidades = new Array();
        var codigosCentros = new Array();
        var codigosCentrosLanF = new Array();
        var centrosAcreditados = new Array();
        var descCentrosAcreditados = new Array();
        var descCentrosLanF = new Array();
        var codigosOrigenAcreditacion = new Array();
        var descOrigenAcreditacion = new Array();
        var codigosMotivoNoAcreditado = new Array();
        var descMotivoNoAcreditado = new Array();
        var bloquearFilaUnidadCompetencial = new Array();
        var cont = 0;

        var codModulo = new Array();
        var descModulo = new Array();
        var duracionModulo = new Array();
        var fechaModulo = new Array();
        var arrayClaveRegistral = new Array();

        var cont = 0;
    <logic:iterate id="unidad" name="unidades" scope="request">
        codigosUnidades[cont] = ['<bean:write name="unidad" property="codUnidad" />'];
        codigosCentros[cont] = ['<bean:write name="unidad" property="codCentro" />'];
        codigosCentrosLanF[cont] = ['<bean:write name="unidad" property="codCentroLanF" />'];
        centrosAcreditados[cont] = ['<bean:write name="unidad" property="centroAcreditado" />'];
        descCentrosAcreditados[cont] = ['<bean:write name="unidad" property="descCentro" />'];
        descCentrosLanF[cont] = ['<bean:write name="unidad" property="descCentroLanF" />'];
        codigosOrigenAcreditacion[cont] = ['<bean:write name="unidad" property="codOrigenAcred" />'];
        codigosMotivoNoAcreditado[cont] = ['<bean:write name="unidad" property="codMotNoAcreditado" />'];
        bloquearFilaUnidadCompetencial[cont] = ['<bean:write name="unidad" property="bloquearEnPantalla" />'];
        arrayClaveRegistral[cont] = ['<bean:write name="unidad" property="claveRegistral" />'];
        cont++;
    </logic:iterate>

        for (c = 0; codigosUnidades != null && c < codigosUnidades.length; c++) {

            if (document.getElementById("cod" + codigosUnidades[c]) != null) {
                document.getElementById("cod" + codigosUnidades[c]).value = codigosCentros[c];
            }//if(document.getElementById("cod"+codigosUnidades[c]) != null)
            if (document.getElementById("desc" + codigosUnidades[c]) != null) {
                document.getElementById("desc" + codigosUnidades[c]).value = descCentrosAcreditados[c];
            }//if(document.getElementById("desc"+codigosUnidades[c]) != null)
            if (document.getElementById("codCentroLanF" + codigosUnidades[c]) != null) {
                document.getElementById("codCentroLanF" + codigosUnidades[c]).value = codigosCentrosLanF[c];
            }
            if (document.getElementById("descCentroLanF" + codigosUnidades[c]) != null) {
                document.getElementById("descCentroLanF" + codigosUnidades[c]).value = descCentrosLanF[c];
            }
            if (document.getElementById("codAcreditado" + codigosUnidades[c]) != null) {
                document.getElementById("codAcreditado" + codigosUnidades[c]).value = centrosAcreditados [c];
            }//if(document.getElementById("codAcreditado"+codigosUnidades[c]) != null)

            var acreditado = "";
            if (centrosAcreditados[c] == "0") {
                acreditado = "Si";
            } else {
                acreditado = "No";
            }//if(centrosAcreditados[c] == "0")

            if (document.getElementById("descAcreditado" + codigosUnidades[c]) != null) {
                document.getElementById("descAcreditado" + codigosUnidades[c]).value = acreditado;
            }//if(document.getElementById("descAcreditado"+codigosUnidades[c]) != null)

            //Buscamos en los array de codigosOrigenAcreditacion y codigosMotivoNoAcreditado para extraer las descripciones de sus campos
            var codOrigenAcreditacionUnidad = "" + codigosOrigenAcreditacion[c];

            if (codOrigenAcreditacionUnidad != null && codOrigenAcreditacionUnidad != undefined && codOrigenAcreditacionUnidad != "") {

                for (x = 0; x < listaCodigosOrigenAcreditacion.length; x++) {
                    var codigoAuxiliar = new String(listaCodigosOrigenAcreditacion[x]);
                    if (codOrigenAcreditacionUnidad == codigoAuxiliar) {

                        if (document.getElementById("codOrigenAcreditacion" + codigosUnidades[c]) != null) {
                            document.getElementById("codOrigenAcreditacion" + codigosUnidades[c]).value = codOrigenAcreditacionUnidad;
                        }

                        if (document.getElementById("descOrigenAcreditacion" + codigosUnidades[c]) != null) {
                            document.getElementById("descOrigenAcreditacion" + codigosUnidades[c]).value = listaDescripcionesOrigenAcreditacion[x];
                        }
                    }
                }
            }// if

            //Buscamos en los array de codigosOrigenAcreditacion y codigosMotivoNoAcreditado para extraer las descripciones de sus campos
            var codigoMotivoNoAcreditado = codigosMotivoNoAcreditado[c];
            for (x = 0; x < listaCodigosMotivoNoAcreditado.length; x++) {
                var codigoMotivoNoAcred = listaCodigosMotivoNoAcreditado[x];
                if (parseInt(codigoMotivoNoAcreditado) == parseInt(codigoMotivoNoAcred)) {
                    if (document.getElementById("codMotivoNoAcreditado" + codigosUnidades[c]) != null) {
                        document.getElementById("codMotivoNoAcreditado" + codigosUnidades[c]).value = codigoMotivoNoAcreditado;
                    }//if(document.getElementById("codMotivoNoAcreditado"+codigosUnidades[c]) != null)
                    if (document.getElementById("descMotivoNoAcreditado" + codigosUnidades[c]) != null) {
                        document.getElementById("descMotivoNoAcreditado" + codigosUnidades[c]).value = listaDescripcionesMotivoNoAcreditado [x];
                    }//if(document.getElementById("descMotivoNoAcreditado"+codigosUnidades[c]) != null)
                }//if(codigoAcreditacion == codigoOrigenAcreditacion)
            }//for(x =0; x<listaCodigosOrigenAcreditacion.length; x++)

            //Buscamos en el array de claves registrales para extraerlas
            var clave = arrayClaveRegistral[c];
            if (document.getElementById("claveRegistral" + codigosUnidades[c]) != null) {
                document.getElementById("claveRegistral" + codigosUnidades[c]).value = clave;
            }//if(document.getElementById("claveRegistral"+codigosUnidades[c]) != null)

        }//for(c=0; codigosUnidades != null && c<codigosUnidades.length; c++)

        //Cargamos la info de los modulos formativos
        comboAcreditadoModFormacion.buscaCodigo(moduloAcreditado);
        if (moduloAcreditado == "0") {
            comboMotivoAcreditadoModFormacion.buscaCodigo(motivoModuloAcreditado);
        } else if (moduloAcreditado == "1") {
            comboMotivoNoAcreditadoModFormacion.buscaCodigo(motivoModuloAcreditado);
        }//if moduloAcreditado

        seleccionAcreditado();
    }//cargarDatos

    function buscaCodigoCertificado(codCertificado) {
        comboListaCertificados.buscaCodigo(codCertificado);
    }//buscaCodigo
    <%}//if(cargarDatos == "S") %>


    var lineasTabla;

    function cargarDatosCertificado() {
        codigosUnidadesDocumentales = new Array();
        var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var listaUnidadesTabla = new Array();
        var listaModulosTabla = new Array();
        var CONTEXT_PATH = '<%=request.getContextPath()%>';

        var bloqueosPantalla = new Array();
        var codigosCentro = new Array();
        var descripcionesCentro = new Array();
        var codigosCentroLanF = new Array();
        var descripcionesCentroLanF = new Array();
        var origenAcreditacion = new Array();
        var claveRegistral = new Array();

        limpiarPantalla();
        bloquearModulosPracticas();

        if (ajax != null && codCertificadoSeleccionado != null && codCertificadoSeleccionado != "") {
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=rellenarPorCertificado" + "&modulo=" + nombreModuloLicitacion + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpLicitacion + "&tipo=0&codCertificado=" + codCertificadoSeleccionado + "&idioma=" +<%=idiomaUsuario%>;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            try {
                if (ajax.readyState == 4 && ajax.status == 200) {
                    var xmlDoc = null;
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async = "false";
                        xmlDoc.loadXML(text);
                    } else {
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                }//for(j=0;hijos!=null && j<hijos.length;j++)

                if (codigoOperacion == "70") {
                    tablaUnidadesCompetenciales.lineas = new Array();
                    tablaUnidadesCompetenciales.displayTabla();

                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "AREA") {
                            var descArea = "";
                            if (hijos[j].childNodes[0] != null)
                                descArea = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("descArea") != null) {
                                document.getElementById("descArea").value = descArea;
                            }//if(document.getElementById("descArea") != null)                    
                        } else if (hijos[j].nodeName == "FAMILIA") {
                            var descFamilia = "";
                            if (hijos[j].childNodes[0] != null)
                                descFamilia = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("descFamilia") != null) {
                                document.getElementById("descFamilia").value = descFamilia;
                            }//if(document.getElementById("descFamilia") != null)

                        } else if (hijos[j].nodeName == "DECRETO") {
                            var descDecreto = "";
                            if (hijos[j].childNodes[0] != null)
                                descDecreto = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("descDecreto") != null) {
                                document.getElementById("descDecreto").value = descDecreto;
                            }//if(document.getElementById("descDecreto") != null)

                        } else if (hijos[j].nodeName == "NIVEL") {
                            var descNivel = "";
                            if (hijos[j].childNodes[0] != null)
                                descNivel = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("descNivel") != null) {
                                document.getElementById("descNivel").value = descNivel;
                            }//if(document.getElementById("descNivel") != null)

                        } else if (hijos[j].nodeName == "TIPO_CP") {
                            var tipoCP = "";
                            if (hijos[j].childNodes[0] != null)
                                tipoCP = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("tipoCP") != null) {
                                if (tipoCP == "0") {
                                    document.getElementById("tipoCP").value = "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.tipoCPNuevo")%>";
                                } else if (tipoCP == "1") {
                                    document.getElementById("tipoCP").value = "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.tipoCPAntiguo")%>";
                                }//if(tipoCP)
                            }//if(document.getElementById("tipoCP") != null)

                        } else if (hijos[j].nodeName == "RD_MODIF") {
                            var rdModif = "";
                            if (hijos[j].childNodes[0] != null)
                                rdModif = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("rdModif") != null) {
                                document.getElementById("rdModif").value = rdModif;
                            }//if(document.getElementById("rdModif") != null)                            

                        } else if (hijos[j].nodeName == "RD_DEROGA") {
                            var rdDeroga = "";
                            if (hijos[j].childNodes[0] != null)
                                rdDeroga = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("rdDeroga") != null) {
                                document.getElementById("rdDeroga").value = rdDeroga;
                            }//if(document.getElementById("rdDeroga") != null)

                        } else if (hijos[j].nodeName == "FECHA_RD") {
                            var fechaRD = "";
                            if (hijos[j].childNodes[0] != null)
                                fechaRD = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("fechaRD") != null) {
                                document.getElementById("fechaRD").value = fechaRD;
                            }//if(document.getElementById("fechaRD") != null)

                        } else if (hijos[j].nodeName == "FECHA_RD_MODIF") {
                            var fechaRDModif = "";
                            if (hijos[j].childNodes[0] != null)
                                fechaRDModif = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("fechaRDModif") != null) {
                                document.getElementById("fechaRDModif").value = fechaRDModif;
                            }//if(document.getElementById("fechaRDModif") != null)

                        } else if (hijos[j].nodeName == "FECHA_RD_DEROGA") {
                            var fechaRDDeroga = "";
                            if (hijos[j].childNodes[0] != null)
                                fechaRDDeroga = hijos[j].childNodes[0].nodeValue;
                            if (document.getElementById("fechaRDDeroga") != null) {
                                document.getElementById("fechaRDDeroga").value = fechaRDDeroga;
                            }//if(document.getElementById("fechaRDDeroga") != null)

                        } else if (hijos[j].nodeName == "UNIDADES_COMPETENCIALES") {
                            var listaUnidades = xmlDoc.getElementsByTagName("UNIDADES_COMPETENCIALES");
                            var nodosUnidades = listaUnidades[0];
                            var hijosUnidades = nodosUnidades.childNodes;

                            for (x = 0; hijosUnidades != null && x < hijosUnidades.length; x++) {
                                var codigoUnidad = "";
                                var descripcionUnidad = "";
                                var nivel = "";
                                var bloqueoPantalla = "";
                                if (hijosUnidades[x].nodeName == "UNIDAD_COMPETENCIAL") {
                                    var listaUnidades = xmlDoc.getElementsByTagName("UNIDAD_COMPETENCIAL");
                                    for (x = 0; listaUnidades != null && x < listaUnidades.length; x++) {
                                        var unidad = listaUnidades[x].childNodes;
                                        for (y = 0; unidad != null && y < unidad.length; y++) {
                                            if (unidad[y].nodeName == "CODIGO") {
                                                codigoUnidad = unidad[y].childNodes[0].nodeValue;

                                            } else if (unidad[y].nodeName == "DESCRIPCION") {
                                                descripcionUnidad = unidad[y].childNodes[0].nodeValue;

                                            } else if (unidad[y].nodeName == "NIVEL") {
                                                if (unidad[y].childNodes[0].nodeValue != null) {
                                                    nivel = unidad[y].childNodes[0].nodeValue;
                                                }

                                            } else if (unidad[y].nodeName == "BLOQUEO_PANTALLA") {

                                                bloqueosPantalla[x] = unidad[y].childNodes[0].nodeValue;
                                                unidadCompetencialBloqueada[x] = unidad[y].childNodes[0].nodeValue;
                                                bloqueoPantalla = unidad[y].childNodes[0].nodeValue;

                                            } else if (unidad[y].nodeName == "COD_CENTRO") {
                                                if (bloqueoPantalla == "S") {
                                                    if (unidad[y].childNodes[0].nodeValue != null &&
                                                            unidad[y].childNodes[0].nodeValue != "null") {
                                                        codigosCentro[x] = unidad[y].childNodes[0].nodeValue;
                                                    } else {
                                                        codigosCentro[x] = " ";
                                                    }/*if(unidad[y].childNodes[0].nodeValue != null &&
                                                     unidad[y].childNodes[0].nodeValue != "null")*/
                                                } else {
                                                    codigosCentro[x] = "";
                                                }//if(bloqueoPantalla == "S")

                                            } else if (unidad[y].nodeName == "DESC_CENTRO") {
                                                if (bloqueoPantalla == "S") {
                                                    if (unidad[y].childNodes[0].nodeValue != null &&
                                                            unidad[y].childNodes[0].nodeValue != "null") {
                                                        descripcionesCentro[x] = unidad[y].childNodes[0].nodeValue;
                                                    } else {
                                                        descripcionesCentro[x] = " ";
                                                    }/*if(unidad[y].childNodes[0].nodeValue != null &&
                                                     unidad[y].childNodes[0].nodeValue != "null")*/
                                                } else {
                                                    descripcionesCentro[x] = "";
                                                }//if(bloqueoPantalla == "S")

                                            } else if (unidad[y].nodeName == "COD_CENTROLANF") {
                                                if (bloqueoPantalla == "S") {
                                                    if (unidad[y].childNodes[0].nodeValue != null &&
                                                            unidad[y].childNodes[0].nodeValue != "null") {
                                                        codigosCentroLanF[x] = unidad[y].childNodes[0].nodeValue;
                                                    } else {
                                                        codigosCentroLanF[x] = " ";
                                                    }

                                                } else {
                                                    codigosCentro[x] = "";
                                                }//if(bloqueoPantalla == "S")

                                            } else if (unidad[y].nodeName == "DESC_CENTROLANF") {
                                                if (bloqueoPantalla == "S") {
                                                    if (unidad[y].childNodes[0].nodeValue != null &&
                                                            unidad[y].childNodes[0].nodeValue != "null") {
                                                        descripcionesCentroLanF[x] = unidad[y].childNodes[0].nodeValue;
                                                    } else {
                                                        descripcionesCentroLanF[x] = " ";
                                                    }

                                                } else {
                                                    descripcionesCentroLanF[x] = "";
                                                }//if(bloqueoPantalla == "S")

                                            } else if (unidad[y].nodeName == "ORIGEN_ACREDITACION") {
                                                if (bloqueoPantalla == "S") {
                                                    if (unidad[y].childNodes[0].nodeValue != null) {
                                                        origenAcreditacion[x] = unidad[y].childNodes[0].nodeValue;
                                                    } else {
                                                        origenAcreditacion[x] = "";
                                                    }
                                                } else {
                                                    origenAcreditacion[x] = "";
                                                }//if(bloqueoPantalla == "S")

                                            } else if (unidad[y].nodeName == "CLAVE_REGISTRAL") {
                                                try {
                                                    if (bloqueoPantalla == "S") {
                                                        if (unidad[y].childNodes[0] != null && unidad[y].childNodes[0].nodeValue != null) {
                                                            claveRegistral[x] = unidad[y].childNodes[0].nodeValue;

                                                        } else {
                                                            claveRegistral[x] = "";
                                                        }
                                                    } else {
                                                        claveRegistral[x] = "";
                                                    }//if(bloqueoPantalla == "S")
                                                } catch (error) {
                                                    alert("error CLAVE_REGISTRAL: " + error.description);
                                                }
                                            }//if
                                        }//for(x=0;listaUnidades!=null && x<listaUnidades.length;x++)
                                        listaUnidadesTabla[x] = [codigoUnidad, descripcionUnidad, nivel, comboCentro(codigoUnidad), comboAcreditado(codigoUnidad), comboMotivoNoAcreditado(codigoUnidad), comboOrigenAcreditacion(codigoUnidad), textClaveRegistral(codigoUnidad), comboCentroLanF(codigoUnidad)];
                                        codigosUnidadesDocumentales[x] = codigoUnidad;
                                    }//for(x=0;listaUnidades!=null && x<listaUnidades.length;x++)
                                }//if(hijosUnidades[x].nodeName == "UNIDAD_COMPETENCIAL")

                            }//for(x=0;hijos!=null && x<hijos.length;x++)
                        } else if (hijos[j].nodeName == "MODULOS_FORMATIVOS") {
                            var listaModulos = xmlDoc.getElementsByTagName("MODULOS_FORMATIVOS");
                            var nodosModulos = listaModulos[0];
                            var hijosModulos = nodosModulos.childNodes;
                            for (x = 0; hijosModulos != null && x < hijosModulos.length; x++) {
                                var codModulo = "";
                                var desModulo = "";
                                var duracionModulo = "";
                                var fechaModulo = "";
                                var nivel = "";
                                if (hijosModulos[x].nodeName == "MODULO_FORMATIVO") {
                                    var listaModulos = xmlDoc.getElementsByTagName("MODULO_FORMATIVO");
                                    for (x = 0; listaModulos != null && x < listaModulos.length; x++) {
                                        var modulo = listaModulos[x].childNodes;
                                        for (y = 0; modulo != null && y < modulo.length; y++) {
                                            if (modulo[y].nodeName == "CODMODULO") {
                                                codModulo = modulo[y].childNodes[0].nodeValue;
                                                if (document.getElementById("codModForm") != null) {
                                                    document.getElementById("codModForm").value = codModulo;
                                                }//if(document.getElementById("codModForm") != null)
                                            } else if (modulo[y].nodeName == "DESMODULO_E") {
                                                desModulo = modulo[y].childNodes[0].nodeValue;
                                                if (document.getElementById("desModulo") != null) {
                                                    document.getElementById("desModulo").value = desModulo;
                                                }//if(document.getElementById("desModulo") != null)
                                            } else if (modulo[y].nodeName == "NIVEL") {
                                                if (modulo[y].childNodes[0] != null && modulo[y].childNodes[0].nodeValue != null) {
                                                    nivel = modulo[y].childNodes[0].nodeValue;
                                                } else {
                                                    nivel = " ";
                                                }//if(modulo[y].childNodes[0] != null && modulo[y].childNodes[0].nodeValue != null )
                                                /*
                                                 if(document.getElementById("nivelModForm") != null){
                                                 document.getElementById("nivelModForm").value = nivel;
                                                 }//if(document.getElementById("nivelModForm") != null)
                                                 */
                                            } else if (modulo[y].nodeName == "DURACION") {
                                                duracionModulo = modulo[y].childNodes[0].nodeValue;
                                                if (document.getElementById("duracionModForm") != null) {
                                                    document.getElementById("duracionModForm").value = duracionModulo;
                                                }//document.getElementById("duracionModForm")
                                            } else if (modulo[y].nodeName == "FEC_PRACTICAS") {
                                                if (modulo[y].childNodes[0] != null && modulo[y].childNodes[0].nodeValue != null) {
                                                    fechaModulo = modulo[y].childNodes[0].nodeValue;
                                                    document.getElementById("fecPracticas").value = fechaModulo;
                                                } else {
                                                    fechaModulo = " ";
                                                }
                                            }//if
                                        }//for(y=0;modulo!=null && y<modulo.length;y++)
                                        listaModulosTabla[x] = [codModulo, desModulo, nivel, duracionModulo, fechaModulo];
                                    }//for(x=0;listaModulos!=null && x<listaModulos.length;x++)
                                }//if(hijosUnidades[x].nodeName == "MODULO_FORMATIVO")
                            }//for(x=0;hijosModulos!=null && x<hijosModulos.length;x++)
                            habilitarModulosPracticas();
                        }//if
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    tablaUnidadesCompetenciales.lineas = listaUnidadesTabla;
                    lineasTabla = listaUnidadesTabla.length;
                    //document.getElementById('cabecera8').style.display="none";

                    //var celda;
                    //for (i=0; i<lineasTabla;i++){
                    //  celda="celda"+i+"8";            
                    //var element = document.getElementById(celda);   
                    //element.style.display = "none";            
                    //}
                    tablaUnidadesCompetenciales.displayTabla();

                    //Iniciamos los combos si hay valores en la tabla
                    if (listaUnidadesTabla.length > 0) {
                        for (var c = 0; codigosUnidadesDocumentales != null && c < codigosUnidadesDocumentales.length; c++) {
                            var codigo = codigosUnidadesDocumentales[c];
                            var combo = new Combo(codigo);
                            combo.addItems(listaCodigosCentros, listaDescripcionesCentros);
                            var comboLanF = new Combo("CentroLanF" + codigo);
                            comboLanF.addItems(listaCodigosCentrosLanF, listaDescripcionesCentrosLanF);
                            var listaCodigosAcreditacion = new Array(2);
                            listaCodigosAcreditacion[0] = [0];
                            listaCodigosAcreditacion[1] = [1];

                            var listaDescripcionesAcreditacion = new Array(2);
                            listaDescripcionesAcreditacion[0] = ['<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.SI")%>'];
                            listaDescripcionesAcreditacion[1] = ['<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.NO")%>'];

                            var combo2 = new Combo("Acreditado" + codigo);
                            combo2.addItems(listaCodigosAcreditacion, listaDescripcionesAcreditacion);
                            combo2.change = seleccionAcreditado;
                            /** COMBO MOTIVO NO ACREDITADO **/
                            var combo3 = new Combo("MotivoNoAcreditado" + codigo);
                            combo3.addItems(listaCodigosMotivoNoAcreditado, listaDescripcionesMotivoNoAcreditado);
                            /** COMBO MOTIVO NO ACREDITADO **/

                            /** COMBO ORIGEN DE ACREDITACI�N **/
                            var combo4 = new Combo("OrigenAcreditacion" + codigo);
                            combo4.addItems(listaCodigosOrigenAcreditacion, listaDescripcionesOrigenAcreditacion);
                            /** COMBO ORIGEN DE ACREDITACI�N **/

                        }//for(c=0; codigosUnidadesDocumentales != null && c<codigosUnidadesDocumentales.length; c++)

                        for (y = 0; bloqueosPantalla != null && y < bloqueosPantalla.length; y++) {
                            if (bloqueosPantalla[y] == "S") {
                                document.getElementById("cod" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("desc" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchor" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codCentroLanF" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descCentroLanF" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorCentroLanF" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorAcreditado" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codOrigenAcreditacion" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descOrigenAcreditacion" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorOrigenAcreditacion" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorMotivoNoAcreditado" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("cod" + codigosUnidadesDocumentales[y]).value = codigosCentro[y];
                                document.getElementById("desc" + codigosUnidadesDocumentales[y]).value = descripcionesCentro[y];

                                document.getElementById("codCentroLanF" + codigosUnidadesDocumentales[y]).value = codigosCentroLanF[y];
                                document.getElementById("descCentroLanF" + codigosUnidadesDocumentales[y]).value = descripcionesCentroLanF[y];

                                document.getElementById("codAcreditado" + codigosUnidadesDocumentales[y]).value = "0";
                                document.getElementById("descAcreditado" + codigosUnidadesDocumentales[y]).value = "SI";

                                //Buscamos en los array de codigosOrigenAcreditacion y codigosMotivoNoAcreditado para extraer las descripciones de sus campos
                                var codigoAcreditacion = origenAcreditacion[y];
                                if (origenAcreditacion != "") {
                                    for (x = 0; x < listaCodigosOrigenAcreditacion.length; x++) {
                                        var codigoOrigenAcreditacion = listaCodigosOrigenAcreditacion[x];
                                        if (codigoAcreditacion == codigoOrigenAcreditacion) {
                                            document.getElementById("codOrigenAcreditacion" + codigosUnidadesDocumentales[y]).value = codigoAcreditacion;
                                            document.getElementById("descOrigenAcreditacion" + codigosUnidadesDocumentales[y]).value = listaDescripcionesOrigenAcreditacion [x];
                                            habilitarModulosPracticas();
                                        }//if(codigoAcreditacion == codigoOrigenAcreditacion)
                                    }//for(x =0; x<listaCodigosOrigenAcreditacion.length; x++)
                                }//if(origenAcreditacion != "")

                                //Rellenamos el campo con la claveRegistral
                                if (document.getElementById("claveRegistral" + codigosUnidadesDocumentales[y]) != null) {
                                    document.getElementById("claveRegistral" + codigosUnidadesDocumentales[y]).value = claveRegistral[y];
                                    document.getElementById("claveRegistral" + codigosUnidadesDocumentales[y]).disabled = true;
                                }//if(document.getElementById("claveRegistral"+codigosUnidadesDocumentales[y]) != null)

                            }//if(bloqueosPantalla[c] == "S")
                        }//for(c=0; bloqueosPantalla != null && c<bloqueosPantalla.length; c++)
                    }//if(listaUnidadesTabla.length > 0)
                } else if (codigoOperacion == "71") {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoDatosCertificado")%>');
                } else if (codigoOperacion == "72") {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoAreaCertificado")%>');
                } else if (codigoOperacion == "73") {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoFamiliaCertificado")%>');
                } else if (codigoOperacion == "73") {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoUnidadesCertificado")%>');
                }//if
            } catch (Err) {
                alert("cargarDatosCertificado Error.descripcion: " + Err.description);
            }//try-catch
        } else {
            // si no se ha seleccionado ning�n certificado y hay datos:            
            document.getElementById("descArea").value = "";
            document.getElementById("descFamilia").value = "";
            //document.getElementById("descTieneModulo").value = "";
            //document.getElementById("descEstado").value = "";
            document.getElementById("descDecreto").value = "";

            tablaUnidadesCompetenciales.lineas = new Array();
            tablaUnidadesCompetenciales.displayTabla();
        }//if(ajax!=null)
    }//cargarDatosCertificado

    function actualizarTabla() {
        var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var listaUnidadesTabla = new Array();
        var CONTEXT_PATH = '<%=request.getContextPath()%>';

        var bloqueosPantalla = new Array();
        var codigosCentro = new Array();
        var descripcionesCentro = new Array();
        var codigosCentroLanF = new Array();
        var descripcionesCentroLanF = new Array();
        var origenAcreditacion = new Array();
        var codAcreditacion = new Array();
        var claveRegistral = new Array();

        if (ajax != null && codCertificadoSeleccionado != null && codCertificadoSeleccionado != "") {
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=actualizarTabla" + "&modulo=" + nombreModuloLicitacion + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpLicitacion + "&tipo=0&codCertificado=" + codCertificadoSeleccionado;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            try {
                if (ajax.readyState == 4 && ajax.status == 200) {
                    var xmlDoc = null;
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async = "false";
                        xmlDoc.loadXML(text);
                    } else {
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;

                codigoOperacion = hijos[0].childNodes[0].nodeValue;

                if (codigoOperacion == "70") {
                    if (hijos[1].nodeName == "UNIDADES_COMPETENCIALES") {
                        var listaUnidades = xmlDoc.getElementsByTagName("UNIDADES_COMPETENCIALES");
                        var nodosUnidades = listaUnidades[0];
                        var hijosUnidades = nodosUnidades.childNodes;
                        for (x = 0; hijosUnidades != null && x < hijosUnidades.length; x++) {
                            var codigoUnidad = "";
                            var descripcionUnidad = "";
                            var nivel = "";
                            var bloqueoPantalla = "";
                            if (hijosUnidades[x].nodeName == "UNIDAD_COMPETENCIAL") {
                                var listaUnidades = xmlDoc.getElementsByTagName("UNIDAD_COMPETENCIAL");
                                //for(x=0;listaUnidades!=null && x<listaUnidades.length;x++){
                                for (var z = 0; listaUnidades != null && z < listaUnidades.length; z++) {
                                    //var unidad = listaUnidades[x].childNodes;
                                    var unidad = listaUnidades[z].childNodes;
                                    for (y = 0; unidad != null && y < unidad.length; y++) {
                                        if (unidad[y].nodeName == "CODIGO") {
                                            codigoUnidad = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "DESCRIPCION") {
                                            descripcionUnidad = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "BLOQUEO_PANTALLA") {
                                            bloqueosPantalla[z] = unidad[y].childNodes[0].nodeValue;
                                            unidadCompetencialBloqueada[z] = unidad[y].childNodes[0].nodeValue;
                                            bloqueoPantalla = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "COD_ACREDITACION") {
                                            codAcreditacion[z] = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "COD_CENTRO") {
                                            codigosCentro[z] = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "DESC_CENTRO") {
                                            descripcionesCentro[z] = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "COD_CENTROLANF") {
                                            if (unidad[y].childNodes[0] != null) {
                                                codigosCentroLanF[z] = unidad[y].childNodes[0].nodeValue;
                                            } else {
                                                codigosCentroLanF[z] = "";
                                            }
                                        } else if (unidad[y].nodeName == "DESC_CENTROLANF") {
                                            if (unidad[y].childNodes[0] != null) {
                                                descripcionesCentroLanF[z] = unidad[y].childNodes[0].nodeValue;
                                            } else {
                                                descripcionesCentroLanF[z] = "";
                                            }
                                        } else if (unidad[y].nodeName == "ORIGEN_ACREDITACION") {
                                            origenAcreditacion[z] = unidad[y].childNodes[0].nodeValue;
                                        } else if (unidad[y].nodeName == "CLAVE_REGISTRAL") {//puede venir vac�a si es centro LANF
                                            try {
                                                if (unidad[y].childNodes[0] != null) {
                                                    var valorClaveRegistral = unidad[y].childNodes[0].nodeValue;
                                                    if (valorClaveRegistral == "VACIO")
                                                        claveRegistral[z] = "";
                                                    else
                                                        claveRegistral[z] = valorClaveRegistral;
                                                } else {
                                                    claveRegistral[z] = "";
                                                }
                                            } catch (error) {
                                                alert("error clave registral: " + error.description);
                                            }
                                        }//if
                                    }//for(x=0;listaUnidades!=null && x<listaUnidades.length;x++)
                                }//for(x=0;listaUnidades!=null && x<listaUnidades.length;x++)
                            }//if(hijosUnidades[x].nodeName == "UNIDAD_COMPETENCIAL")
                        }//for(x=0;hijos!=null && x<hijos.length;x++){
                    }//if


                    for (y = 0; bloqueosPantalla != null && y < bloqueosPantalla.length; y++) {
                        if (bloqueosPantalla[y] == "S") {
                            if (document.getElementById("cod" + codigosUnidadesDocumentales[y]) != null) {
                                document.getElementById("cod" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("desc" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchor" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorAcreditado" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codOrigenAcreditacion" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descOrigenAcreditacion" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorOrigenAcreditacion" + codigosUnidadesDocumentales[y]).style.display = "none";

                                document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[y]).disabled = true;
                                document.getElementById("anchorMotivoNoAcreditado" + codigosUnidadesDocumentales[y]).style.display = "none";
                            }//if(document.getElementById("cod"+codigosUnidadesDocumentales[y]) != null)
                        }//if(bloqueosPantalla[c] == "S")


                        if (document.getElementById("cod" + codigosUnidadesDocumentales[y]) != null) {
                            if (codigosCentro[y] != "null") {
                                document.getElementById("cod" + codigosUnidadesDocumentales[y]).value = codigosCentro[y];
                                document.getElementById("desc" + codigosUnidadesDocumentales[y]).value = descripcionesCentro[y];

                                if (codAcreditacion[y] == "0") {
                                    document.getElementById("codAcreditado" + codigosUnidadesDocumentales[y]).value = "0";
                                    document.getElementById("descAcreditado" + codigosUnidadesDocumentales[y]).value = "SI";
                                } else {
                                    document.getElementById("codAcreditado" + codigosUnidadesDocumentales[y]).value = "1";
                                    document.getElementById("descAcreditado" + codigosUnidadesDocumentales[y]).value = "NO";
                                }//if(codAcreditacion[y] == "0")
                            }//if(codigosCentro[y] != "null")
                        }//if(document.getElementById("cod"+codigosUnidadesDocumentales[y]) != null)


                        //jess �?�?�?
                        if (document.getElementById("codCentroLanF" + codigosUnidadesDocumentales[y]) != null) {
                            if (codigosCentroLanF[y] != "null") {
                                document.getElementById("codCentroLanF" + codigosUnidadesDocumentales[y]).value = codigosCentroLanF[y];
                                document.getElementById("descCentroLanF" + codigosUnidadesDocumentales[y]).value = descripcionesCentroLanF[y];
                            }
                        }

                        //Buscamos en los array de codigosOrigenAcreditacion y codigosMotivoNoAcreditado para extraer las descripciones de sus campos
                        var codigoAcreditacion = origenAcreditacion[y];
                        if (origenAcreditacion != "") {
                            for (x = 0; x < listaCodigosOrigenAcreditacion.length; x++) {
                                var codigoOrigenAcreditacion = listaCodigosOrigenAcreditacion[x];
                                if (codigoAcreditacion == codigoOrigenAcreditacion) {
                                    document.getElementById("codOrigenAcreditacion" + codigosUnidadesDocumentales[y]).value = codigoAcreditacion;
                                    document.getElementById("descOrigenAcreditacion" + codigosUnidadesDocumentales[y]).value = listaDescripcionesOrigenAcreditacion [x];
                                }//if(codigoAcreditacion == codigoOrigenAcreditacion)
                            }//for(x =0; x<listaCodigosOrigenAcreditacion.length; x++)
                        }//if(origenAcreditacion != "")


                        //Rellenamos el campo con la claveRegistral
                        if (document.getElementById("claveRegistral" + codigosUnidadesDocumentales[y]) != null) {
                            document.getElementById("claveRegistral" + codigosUnidadesDocumentales[y]).value = claveRegistral[y];
                            //document.getElementById("claveRegistral"+codigosUnidadesDocumentales[y]).disabled = true;
                        }
                    }//for(c=0; bloqueosPantalla != null && c<bloqueosPantalla.length; c++)
                    seleccionAcreditado();
                }//if(codigoOperacion=="70")
            } catch (Err) {
                alert("actualizar Error.descripcion: " + Err.description);
            }//try-catch
        }//if(ajax!=null && codCertificadoSeleccionado!=null && codCertificadoSeleccionado!="")
    }//actualizarTabla

    function comboCentro(codUnidadCompetencial) {
        var fila = '';
        fila += '<input type="text" name="cod' + codUnidadCompetencial + '" id="cod' + codUnidadCompetencial + '" size="8" class="inputTextoCombos03" value="" onkeyup="xAMayusculas(this);"/>';
        fila += '&nbsp;';
        fila += '<input type="text" name="desc' + codUnidadCompetencial + '" id="desc' + codUnidadCompetencial + '" size="25" class="inputTextoCombos03" readonly="true" value=""/>';
        fila += '&nbsp;';
        fila += '<a href="" id="anchor' + codUnidadCompetencial + '" name="anchor' + codUnidadCompetencial + '">';
        //fila += '<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">';
        fila += '<span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>';
        fila += '</a>';
        return fila;
    }//comboCentro

    function comboCentroLanF(codUnidadCompetencial) {
        var fila = '';
        fila += '<input type="text" name="codCentroLanF' + codUnidadCompetencial + '" id="codCentroLanF' + codUnidadCompetencial + '" size="8" class="inputTextoCombos03" value="" onkeyup="xAMayusculas(this);"/>';
        fila += '&nbsp;';
        fila += '<input type="text" name="descCentroLanF' + codUnidadCompetencial + '" id="descCentroLanF' + codUnidadCompetencial + '" size="25" class="inputTextoCombos03" readonly="true" value=""/>';
        fila += '&nbsp;';
        fila += '<a href="" id="anchorCentroLanF' + codUnidadCompetencial + '" name="anchorCentroLanF' + codUnidadCompetencial + '">';
        //fila += '<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">';
        fila += '<span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>';
        fila += '</a>';
        return fila;
    }//comboCentroLanF

    function comboAcreditado(codUnidadCompetencial) {
        var fila = '';
        fila += '<input type="text" name="codAcreditado' + codUnidadCompetencial + '" id="codAcreditado' + codUnidadCompetencial + '" size="1" class="inputTextoCombos03" value="" onkeyup="xAMayusculas(this);" style="visibility:none;"/>';
        fila += '&nbsp;';
        fila += '<input type="text" name="descAcreditado' + codUnidadCompetencial + '" id="descAcreditado' + codUnidadCompetencial + '" size="4" class="inputTextoCombos03" readonly="true" value=""/>';
        fila += '&nbsp;';
        fila += '<a href="" id="anchorAcreditado' + codUnidadCompetencial + '" name="anchorAcreditado' + codUnidadCompetencial + '">';
        //fila += '<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">';
        fila += '<span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>';
        fila += '</a>';
        return fila;
    }//comboAcreditado

    function comboOrigenAcreditacion(codUnidadCompetencial) {
        var fila = '';
        fila += '<input type="text" name="codOrigenAcreditacion' + codUnidadCompetencial + '" id="codOrigenAcreditacion' + codUnidadCompetencial + '" size="2" class="inputTextoCombos03" value="" onkeyup="xAMayusculas(this);"/>';
        fila += '&nbsp;';
        fila += '<input type="text" name="descOrigenAcreditacion' + codUnidadCompetencial + '" id="descOrigenAcreditacion' + codUnidadCompetencial + '" size="10" class="inputTextoCombos03" readonly="true" value=""/>';
        fila += '&nbsp;';
        fila += '<a href="" id="anchorOrigenAcreditacion' + codUnidadCompetencial + '" name="anchorOrigenAcreditacion' + codUnidadCompetencial + '">';
        //fila += '<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">';
        fila += '<span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>';
        fila += '</a>';
        return fila;
    }//comboMotivoNoAcreditado

    function comboMotivoNoAcreditado(codUnidadCompetencial) {
        var fila = '';
        fila += '<input type="text" name="codMotivoNoAcreditado' + codUnidadCompetencial + '" id="codMotivoNoAcreditado' + codUnidadCompetencial + '" size="2" class="inputTextoCombos03" disabled="true" value="" onkeyup="xAMayusculas(this);"/>';
        fila += '&nbsp;';
        fila += '<input type="text" name="descMotivoNoAcreditado' + codUnidadCompetencial + '" id="descMotivoNoAcreditado' + codUnidadCompetencial + '" size="10" class="inputTextoCombos03" disabled="true" readonly="true" value=""/>';
        fila += '&nbsp;';
        fila += '<a href="" style="display:none" id="anchorMotivoNoAcreditado' + codUnidadCompetencial + '" name="anchorMotivoNoAcreditado' + codUnidadCompetencial + '">';
        //fila += '<img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">';
        fila += '<span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>';
        fila += '</a>';
        return fila;
    }//comboMotivoNoAcreditado

    function textClaveRegistral(codUnidadCompetencial) {
        var fila = '';
        fila += '<input type="text" name="claveRegistral' + codUnidadCompetencial + '" id="claveRegistral' + codUnidadCompetencial + '" size="15" maxlength="26" class="inputTextoCombos03" value="" onkeyup="xAMayusculas(this);"/>';
        return fila;
    }//textClaveRegistral

    function guardarPantalla() {
        if (comprobarDatos()) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;

            if (ajax != null) {
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "&tarea=procesar&operacion=insertarCertificadoCentroExpediente" + "&modulo=" + nombreModuloLicitacion + "&codOrganizacion=" + codOrganizacion
                        + "&numero=" + numExpLicitacion + "&tipo=0&codCertificado=" + codCertificadoSeleccionado;

                //Procesamos las selecciones de las unidades competenciales
                if (codigosUnidadesDocumentales.length > 0) {
                    parametros += "&unidadesDocumentales=";
                    var primera = true;
                    for (c = 0; codigosUnidadesDocumentales != null && c < codigosUnidadesDocumentales.length; c++) {
                        if (unidadCompetencialBloqueada[c] != "S") {
                            var string = "";
                            var string2 = "";
                            var string3 = "";
                            var codigoUnidad = codigosUnidadesDocumentales[c];
                            var codigoCentro = "";
                            var codigoCentroLanF = " ";
                            var codigoAcreditacion = " ";
                            var codMotNoAcreditado = " ";
                            var codOrigenAcred = " ";
                            var claveRegistral = " ";

                            if (document.getElementById("codCentroLanF" + codigoUnidad).value != '') {
                                codigoCentroLanF = document.getElementById("codCentroLanF" + codigoUnidad).value;
                            }
                            if (document.getElementById("codAcreditado" + codigoUnidad).value != '') {

                                codigoCentro = document.getElementById("cod" + codigoUnidad).value;
                                codigoAcreditacion = document.getElementById("codAcreditado" + codigoUnidad).value;
                                if (document.getElementById("codMotivoNoAcreditado" + codigoUnidad).value != "") {
                                    codMotNoAcreditado = document.getElementById("codMotivoNoAcreditado" + codigoUnidad).value;
                                }/*else{
                                 codMotNoAcreditado = " ";
                                 }*///if(document.getElementById("codMotivoNoAcreditado"+codigoUnidad).value != "")

                                if (document.getElementById("codOrigenAcreditacion" + codigoUnidad).value != "") {
                                    codOrigenAcred = document.getElementById("codOrigenAcreditacion" + codigoUnidad).value;
                                }/*else{
                                 codOrigenAcred = " ";
                                 }*///if(document.getElementById("codOrigenAcreditacion"+codigoUnidad).value != "")

                                if (document.getElementById("claveRegistral" + codigoUnidad).value != null) {
                                    claveRegistral = document.getElementById("claveRegistral" + codigoUnidad).value;
                                }/*else{
                                 claveRegistral = " ";
                                 }*///if(document.getElementById("claveRegistral"+codigoUnidad).value != null)
                            }
                            if (codigoCentroLanF != ' ' || codigoCentro != '') {
                                if (primera == false) {
                                    string += "@#@";
                                }//if(primera == true)
                                primera = false;

                                if (claveRegistral == "")
                                    claveRegistral = "-";
                                string += codigoUnidad + "#@#" + codigoCentro + "#@#" + codigoAcreditacion + "#@#" + codMotNoAcreditado
                                        + "#@#" + codOrigenAcred + "#@#" + claveRegistral + "#@#" + codigoCentroLanF;
                                parametros += string;
                            }

                        }//if(unidadCompetencialBloqueada[c] != "S")
                    }//for(c=0; codigosUnidadesDocumentales != null && c<codigosUnidadesDocumentales.length; c++)
                }//if(codigosUnidadesDocumentales.length > 0)

                if (document.getElementById("codModForm").value != null && document.getElementById("codModForm").value != "") {
                    //Recuperamos si esta el modulo acreditado o no para saber si elegimos el motivo de acreditado o de no acreditado
                    var acreditado = document.getElementById("codAcreditadoModForm").value;
                    var valorMotivoAcreditado = "";
                    if (acreditado == "0") {
                        valorMotivoAcreditado = document.getElementById("codMotivoAcreditadoModForm").value;
                    } else if (acreditado == "1") {
                        valorMotivoAcreditado = document.getElementById("codMotivoNoAcreditadoModForm").value;
                    }//if acreditado

                    parametros += "&modForm=";
                    parametros += document.getElementById("codModForm").value;
                    parametros += "@#@" + document.getElementById("codAcreditadoModForm").value;
                    parametros += "@#@" + valorMotivoAcreditado;
                }//if(document.getElementById("codModForm").value != null && document.getElementById("codModForm").value != "")

                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);

                try {
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var xmlDoc = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            // En IE el XML viene en responseText y no en la propiedad responseXML
                            var text = ajax.responseText;
                            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                            xmlDoc.async = "false";
                            xmlDoc.loadXML(text);
                        } else {
                            // En el resto de navegadores el XML se recupera de la propiedad responseXML
                            xmlDoc = ajax.responseXML;
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)

                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var segundoExp = "";
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }
                        if (hijos[j].nodeName == "SEGUNDOEXP") {
                            segundoExp = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    if (codigoOperacion == "70") {
                        jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.todoCorrecto")%>');
                        actualizarTabla();
                    } else if (codigoOperacion == "71") {
                        jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorRecuperandoDatosCertificado")%>');
                    } else if (codigoOperacion == "72") {
                        jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorInsertandoDatosCertificado")%>');
                    } else if (codigoOperacion == "20") {
                        jsp_alerta("A", 'Ya existe un expediente tipo CP valorado positivamente para este solicitante: expediente ' + segundoExp);
                    }
                } catch (Err) {
                    alert("Error.descripcion: " + Err.description);
                }//try-catch
            }//if(ajax!=null)
        }//if(comprobarDatos())
    }//guardarPantalla

    function unidadesCompetencialesActivas() {
        var unidadesActivas = false;
        for (c = 0; c < unidadCompetencialBloqueada.length; c++) {
            if (unidadCompetencialBloqueada[c] == "S") {
                unidadesActivas = true;
            }//if(unidadCompetencialBloqueada[c] == "S")
        }//for(c=0; c< unidadCompetencialBloqueada.length; c++)
        return unidadesActivas;
    }//unidadesCompetencialesActivas

    function comprobarDatos() {
        //Primero comprobamos que haya un certificado instalado.
        var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
        if (codCertificadoSeleccionado == null || codCertificadoSeleccionado == "") {
            jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorNoCertificadoSeleccionado")%>');
            return false;
        } else {
            //jess: Santi dice que de momento no se haga ninguna comprobaci�n con el centroLanF
            //Comprobamos que si alguna unidad competencial tiene seleccionado un centro este tenga un valor en el campo acreditaci�n
            if (codigosUnidadesDocumentales != null && codigosUnidadesDocumentales.length > 0) {
                for (c = 0; codigosUnidadesDocumentales != null && c < codigosUnidadesDocumentales.length; c++) {
                    var codigoUnidad = codigosUnidadesDocumentales[c];
                    var codigoCentro = document.getElementById("cod" + codigoUnidad).value;
                    var codigoCentroLanF = document.getElementById("codCentroLanF" + codigoUnidad).value;
                    var motivoNoAcred = document.getElementById("codMotivoNoAcreditado" + codigoUnidad).value;
                    var origenAcred = document.getElementById('codOrigenAcreditacion' + codigoUnidad).value;
                    if (codigoCentro != null && codigoCentro != "") {
                        var acreditado = document.getElementById("codAcreditado" + codigoUnidad).value;
                        if (acreditado == null || acreditado == "") {
                            jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorNoAcreditacionSeleccionada")%>');
                            return false;
                        }//if(acreditado == null && acreditado == "")
                    }//if(codigoCentro != null && codigoCentro != "")                    
                    var acreditado = document.getElementById("codAcreditado" + codigoUnidad).value;
                    if (acreditado != null && acreditado != "" && (codigoCentro == null || codigoCentro == "")) {
                        //jsp_alerta("A","<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorAcreditacionSinCentro")%>");
                        //return false;                        
                    }//if(acreditado!=null && acreditado!="" && (codigoCentro==null || codigoCentro==""))

                    if (acreditado == "1") {
                        if (motivoNoAcred == "") {
                            jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorSinMotivoNoAcreditado")%>");
                            return false;
                        }//if(motivoNoAcred == "")
                        else
                        if (codigoCentro == "") {
                            jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorUnidadSinCentro")%> " + codigoUnidad + " <%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorUnidadNoAcreditadaSinCentro")%>");
                            return false;
                        }
                    } else if (acreditado == "0") {
                        if (origenAcred == "" && (codigoCentro != "<%=ConfigurationParameter.getParameter(ConstantesMeLanbide03.CODCENTRO_LANF, ConstantesMeLanbide03.FICHERO_PROPIEDADES)%>" || codigoCentroLanF == null || codigoCentroLanF == "")) {
                            jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorSinOrigenAcreditacion")%>");
                            return false;
                        }//if(origenAcred == "")
                        else
                        if (codigoCentro == "") {
                            jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorUnidadSinCentro")%> " + codigoUnidad + " <%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorUnidadAcreditadaSinCentro")%>");
                            return false;
                        }
                    }//if
                }//for(c=0; codigosUnidadesDocumentales != null && c<codigosUnidadesDocumentales.length; c++)
            }//if(codigosUnidadesDocumentales.length > 0)
            //Comprobamos que si para el m�dulo formativo se selecciona un valor para el combo acreditado se seleccione un valor
            //para el combo de motivo acreditado.
            if (document.getElementById("codAcreditadoModForm").value != "") {
                if (document.getElementById("codAcreditadoModForm").value == "0") {
                    if (document.getElementById("codMotivoAcreditadoModForm").value == "") {
                        jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorSinMotivoNoAcreditadoModForm")%>");
                        return false;
                    }//if(document.getElementById("codMotivoAcreditadoModForm").value == "")
                } else if (document.getElementById("codAcreditadoModForm").value == "1") {
                    if (document.getElementById("codMotivoNoAcreditadoModForm").value == "") {
                        jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorSinMotivoNoAcreditadoModForm")%>");
                        return false;
                    }//if(document.getElementById("codMotivoAcreditadoModForm").value == "")
                }//if(document.getElementById("codAcreditadoModForm").value == "0")
            }//if(document.getElementById("codAcreditadoModForm").value != "")
        }//if(codCertificadoSeleccionado == null || codCertificadoSeleccionado == "")
        return true;
    }//comprobarDatos

    function borrarPantalla() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
        var codCertificadoSeleccionado = document.getElementById("codListaCertificados").value;
        if (ajax != null) {
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=borrarCertificadoExpediente" + "&modulo=" + nombreModuloLicitacion + "&codOrganizacion=" + codOrganizacion
                    + "&numero=" + numExpLicitacion + "&tipo=0&codCertificado=" + codCertificadoSeleccionado;

            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            try {
                if (ajax.readyState == 4 && ajax.status == 200) {
                    var xmlDoc = null;
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async = "false";
                        xmlDoc.loadXML(text);
                    } else {
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)

                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                }//for(j=0;hijos!=null && j<hijos.length;j++)

                if (codigoOperacion == "70") {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.eliminarCorrecto")%>');
                    //Limpiamos los datos de la pantalla.
                    document.getElementById("descArea").value = "";
                    document.getElementById("descFamilia").value = "";
                    document.getElementById("tipoCP").value = "";
                    document.getElementById("rdDeroga").value = "";
                    document.getElementById("rdModif").value = "";
                    document.getElementById("fechaRD").value = "";
                    document.getElementById("fechaRDModif").value = "";
                    document.getElementById("fechaRDDeroga").value = "";
                    document.getElementById("descDecreto").value = "";
                    document.getElementById("descNivel").value = "";
                    document.getElementById("codListaCertificados").value = "";
                    document.getElementById("descListaCertificados").value = "";
                    tablaUnidadesCompetenciales.lineas = new Array();
                    tablaUnidadesCompetenciales.displayTabla();

                    //Borramos el m�dulo formativo                    

                    document.getElementById("codModForm").value = "";
                    document.getElementById("desModulo").value = "";
                    //document.getElementById("nivelModForm").value = "";
                    document.getElementById("duracionModForm").value = "";
                    document.getElementById("codAcreditadoModForm").value = "";
                    document.getElementById("descAcreditadoModForm").value = "";
                    document.getElementById("codMotivoAcreditadoModForm").value = "";
                    document.getElementById("descMotivoAcreditadoModForm").value = "";

                    bloquearModulosPracticas();
                } else if (codigoOperacion == "71") {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"alert.errorEliminandoDatosCertificado")%>');
                }//if(
            } catch (Err) {
                alert("Error.descripcion: " + Err.description);
            }//try-catch
        }//if(ajax!=null)
    }//borrarPantalla

    function limpiarPantalla() {
        //Limpiamos los datos de la pantalla.
        document.getElementById("descArea").value = "";
        document.getElementById("descFamilia").value = "";
        document.getElementById("tipoCP").value = "";
        document.getElementById("rdDeroga").value = "";
        document.getElementById("rdModif").value = "";
        document.getElementById("fechaRD").value = "";
        document.getElementById("fechaRDModif").value = "";
        document.getElementById("fechaRDDeroga").value = "";
        document.getElementById("descDecreto").value = "";
        document.getElementById("descNivel").value = "";

        tablaUnidadesCompetenciales.lineas = new Array();
        tablaUnidadesCompetenciales.displayTabla();

        //Borramos el m�dulo formativo
        document.getElementById("codModForm").value = "";
        document.getElementById("desModulo").value = "";
        //document.getElementById("nivelModForm").value = "";
        document.getElementById("duracionModForm").value = "";
        document.getElementById("codAcreditadoModForm").value = "";
        document.getElementById("descAcreditadoModForm").value = "";
        document.getElementById("codMotivoAcreditadoModForm").value = "";
        document.getElementById("descMotivoAcreditadoModForm").value = "";
    }//limpiarPantalla

    function seleccionAcreditado() {
        for (x = 0; x < codigosUnidadesDocumentales.length; x++) {
            if (document.getElementById("codAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                if (document.getElementById("codAcreditado" + codigosUnidadesDocumentales[x]).value == "1") {
                    if (document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).disabled = false;
                    }//if(document.getElementById("codMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                    if (document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).disabled = false;
                    }//if(document.getElementById("descMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                    if (document.getElementById("anchorMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("anchorMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).style.display = "";
                    }//if(document.getElementById("anchorMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                } else if (document.getElementById("codAcreditado" + codigosUnidadesDocumentales[x]).value != "1") {
                    if (document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).disabled = true;
                    }//if(document.getElementById("codMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                    if (document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).disabled = true;
                    }//if(document.getElementById("descMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                    if (document.getElementById("anchorMotivoNoAcreditado" + codigosUnidadesDocumentales[x])) {
                        document.getElementById("anchorMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).style.display = "none";
                    }//if(document.getElementById("anchorMotivoNoAcreditado"+codigosUnidadesDocumentales[x]))
                    if (document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("codMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).value = "";
                    }//if(document.getElementById("codMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                    if (document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[x]) != null) {
                        document.getElementById("descMotivoNoAcreditado" + codigosUnidadesDocumentales[x]).value = "";
                    }//if(document.getElementById("descMotivoNoAcreditado"+codigosUnidadesDocumentales[x]) != null)
                }//if
            }//if(document.getElementById("codAcreditado"+codigosUnidadesDocumentales[x]) != null)
        }//for(x=0; x<codigosUnidadesDocumentales.length; x++)
    }//seleccionAcreditado

    function bloquearModulosPracticas() {
        document.getElementById("codAcreditadoModForm").value = "";
        document.getElementById("codAcreditadoModForm").disabled = true;
        document.getElementById("descAcreditadoModForm").value = "";
        document.getElementById("descAcreditadoModForm").disabled = true;
        document.getElementById("anchorAcreditadoModForm").style.display = "none";

        document.getElementById("codMotivoAcreditadoModForm").value = "";
        document.getElementById("codMotivoAcreditadoModForm").disabled = true;
        document.getElementById("descMotivoAcreditadoModForm").value = "";
        document.getElementById("descMotivoAcreditadoModForm").disabled = true;
        document.getElementById("anchorMotivoAcreditadoModForm").style.display = "none";

        document.getElementById("codMotivoNoAcreditadoModForm").value = "";
        document.getElementById("codMotivoNoAcreditadoModForm").disabled = true;
        document.getElementById("descMotivoNoAcreditadoModForm").value = "";
        document.getElementById("descMotivoNoAcreditadoModForm").disabled = true;
        document.getElementById("anchorMotivoNoAcreditadoModForm").style.display = "none";
    }//bloquearModulosPracticas

    function habilitarModulosPracticas() {
        document.getElementById("codAcreditadoModForm").disabled = false;
        document.getElementById("descAcreditadoModForm").disabled = false;
        document.getElementById("anchorAcreditadoModForm").style.display = "";
    }//habilitarModulosPracticas

    function habilitarMotivoAcreditadoModuloPracticas() {
        document.getElementById("codMotivoAcreditadoModForm").disabled = false;
        document.getElementById("descMotivoAcreditadoModForm").disabled = false;
        document.getElementById("anchorMotivoAcreditadoModForm").style.display = "";

        document.getElementById("codMotivoNoAcreditadoModForm").value = "";
        document.getElementById("codMotivoNoAcreditadoModForm").disabled = true;
        document.getElementById("descMotivoNoAcreditadoModForm").value = "";
        document.getElementById("descMotivoNoAcreditadoModForm").disabled = true;
        document.getElementById("anchorMotivoNoAcreditadoModForm").style.display = "none";
    }//habilitarMotivoAcreditadoModuloPracticas

    function habilitarMotivoNoAcreditadoModuloPracticas() {
        document.getElementById("codMotivoNoAcreditadoModForm").disabled = false;
        document.getElementById("descMotivoNoAcreditadoModForm").disabled = false;
        document.getElementById("anchorMotivoNoAcreditadoModForm").style.display = "";

        document.getElementById("codMotivoAcreditadoModForm").value = "";
        document.getElementById("codMotivoAcreditadoModForm").disabled = true;
        document.getElementById("descMotivoAcreditadoModForm").value = "";
        document.getElementById("descMotivoAcreditadoModForm").disabled = true;
        document.getElementById("anchorMotivoAcreditadoModForm").style.display = "none";
    }//habilitarMotivoNoAcreditadoModuloPracticas

    function bloquearMotivosAcreditadosModuloPracticas() {
        document.getElementById("codMotivoNoAcreditadoModForm").value = "";
        document.getElementById("codMotivoNoAcreditadoModForm").disabled = true;
        document.getElementById("descMotivoNoAcreditadoModForm").value = "";
        document.getElementById("descMotivoNoAcreditadoModForm").disabled = true;
        document.getElementById("anchorMotivoNoAcreditadoModForm").style.display = "none";

        document.getElementById("codMotivoAcreditadoModForm").value = "";
        document.getElementById("codMotivoAcreditadoModForm").disabled = true;
        document.getElementById("descMotivoAcreditadoModForm").value = "";
        document.getElementById("descMotivoAcreditadoModForm").disabled = true;
        document.getElementById("anchorMotivoAcreditadoModForm").style.display = "none";
    }//bloquearMotivosAcreditadosModuloPracticas

    function acreditacionModForm() {
        //Recuperamos el valor seleccionado en el combo.
        if (document.getElementById("codAcreditadoModForm") != null) {
            var codigo = document.getElementById("codAcreditadoModForm").value;
            if (codigo == "0") {
                habilitarMotivoAcreditadoModuloPracticas();
            } else if (codigo == "1") {
                habilitarMotivoNoAcreditadoModuloPracticas();
            } else {
                bloquearMotivosAcreditadosModuloPracticas();
            }//if codigo
        }//if(document.getElementById("codAcreditadoModForm") != null)
    }//acreditacionModForm

    function pulsarVerListaExp() {
        var control = new Date();
        var result = null;
        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&operacion=recogeDatosExp&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime(), 400, 820, 'no', 'no');
        } else {
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&operacion=recogeDatosExp&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime(), 400, 850, 'no', 'no');
        }
        if (result != undefined) {
            if (result[0] == '0') {
                recargarTablaUbicacionesCE(result);
            }
        }
    }

    function copiarDatos() {
        if (codigosUnidadesDocumentales.length > 0) {
            var primera = true;
            var codigoCentro = "";
            var desCentro = "";
            var codigoCentroLanF = "";
            var desCentroLanF = "";
            var codigoAcreditacion = "";
            var desAcreditacion = "";
            var codOrigenAcred = "";
            var desOrigenAcred = "";
            for (c = 0; codigosUnidadesDocumentales != null && c < codigosUnidadesDocumentales.length; c++) {
                if (unidadCompetencialBloqueada[c] != "S") {
                    var codigoUnidad = codigosUnidadesDocumentales[c];

                    if (document.getElementById("codAcreditado" + codigoUnidad).value != '' && primera == true) {
                        codigoCentro = document.getElementById("cod" + codigoUnidad).value;
                        desCentro = document.getElementById("desc" + codigoUnidad).value;
                        codigoCentroLanF = document.getElementById("codCentroLanF" + codigoUnidad).value;
                        desCentroLanF = document.getElementById("descCentroLanF" + codigoUnidad).value;
                        codigoAcreditacion = document.getElementById("codAcreditado" + codigoUnidad).value;
                        desAcreditacion = document.getElementById("descAcreditado" + codigoUnidad).value;
                        codOrigenAcred = document.getElementById("codOrigenAcreditacion" + codigoUnidad).value;
                        desOrigenAcred = document.getElementById("descOrigenAcreditacion" + codigoUnidad).value;

                        primera = false;
                    }
                    if (primera == false) {
                        document.getElementById("cod" + codigoUnidad).value = codigoCentro;
                        document.getElementById("desc" + codigoUnidad).value = desCentro;
                        document.getElementById("codCentroLanF" + codigoUnidad).value = codigoCentroLanF;
                        document.getElementById("descCentroLanF" + codigoUnidad).value = desCentroLanF;
                        document.getElementById("codAcreditado" + codigoUnidad).value = codigoAcreditacion;
                        document.getElementById("descAcreditado" + codigoUnidad).value = desAcreditacion;
                        document.getElementById("codOrigenAcreditacion" + codigoUnidad).value = codOrigenAcred;
                        document.getElementById("descOrigenAcreditacion" + codigoUnidad).value = desOrigenAcred;
                        if (codigoAcreditacion == "1") {
                            document.getElementById("anchorMotivoNoAcreditado" + codigoUnidad).style.display = "";
                        }
                    }
                }
            }
        }
    }

    function avanzarToExperaExpedicion() {
        if (comprobarDatosAvanzar()) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            if (ajax != null) {
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "&tarea=procesar&operacion=avanzarExpedienteToEsperaExpedicion" + "&modulo=" + nombreModuloLicitacion + "&codOrganizacion=" + esquemaBBDD
                        + "&numero=" + numExpLicitacion + "&tipo=0";
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);
                try {
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var xmlDoc = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            // En IE el XML viene en responseText y no en la propiedad responseXML
                            var text = ajax.responseText;
                            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                            xmlDoc.async = "false";
                            xmlDoc.loadXML(text);
                        } else {
                            // En el resto de navegadores el XML se recupera de la propiedad responseXML
                            xmlDoc = ajax.responseXML;
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var mensajeOperacion = "";
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }
                        if (hijos[j].nodeName == "MENSAJE_OPERACION") {
                            mensajeOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    jsp_alerta("A", mensajeOperacion);
                    if ("0" === codigoOperacion) {
                        recargarDatosExpediente();
                    }
                } catch (Err) {
                    alert("Error.descripcion: " + Err.description);
                }//try-catch
            }//if(ajax!=null)
        }//if(comprobarDatosAvanzar())
    }//avanzarToExperaExpedicion

    function comprobarDatosAvanzar() {
        //Primero realizamos las mismas comprobaciones que al guardar, asi que llamamos al mismo metodo
        var validado = comprobarDatos();
        if (validado) {
            // validamos en BD que estes guardados los datos y acreditas las UC/MP para CP y al menos una UC para los APA
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            if (ajax != null) {
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "&tarea=procesar&operacion=comprobarDatosAvanzarToEsperaExped" + "&modulo=" + nombreModuloLicitacion + "&codOrganizacion=" + esquemaBBDD
                        + "&numero=" + numExpLicitacion + "&tipo=0";
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);

                try {
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var xmlDoc = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            // En IE el XML viene en responseText y no en la propiedad responseXML
                            var text = ajax.responseText;
                            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                            xmlDoc.async = "false";
                            xmlDoc.loadXML(text);
                        } else {
                            // En el resto de navegadores el XML se recupera de la propiedad responseXML
                            xmlDoc = ajax.responseXML;
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)

                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var mensajeOperacion = "";
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }
                        if (hijos[j].nodeName == "MENSAJE_OPERACION") {
                            mensajeOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                       
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if ("0" !== codigoOperacion) {
                        jsp_alerta("A", mensajeOperacion);
                        validado = false;
                    } else
                        validado = true;
                } catch (Err) {
                    alert("Error General : " + Err.description);
                }//try-catch
            }//if(ajax!=null)
        }
        return validado;

    }//comprobarDatosAvanzar

    /**
     * Funcino copiada de la jsp tramitacionExpedientes -- Carga tramites, 
     * boton volver, que carga los datos de los tramites de nuevo en el expediente
     * pulsarVolver()
     * quito este complemento en l aurl del action + "?expHistorico= menorque bean:write name='TramitacionExpedientesForm' property='expHistorico' scope='session'/ mayorque";
     * @returns {undefined}     */
    function recargarDatosExpediente() {
        pleaseWait('on');
        document.forms[0].opcion.value = "cargarPestTram";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
    }
    cargarDesplegableEdades();
    function cargarDesplegableEdades() {
        $(document).ajaxComplete(function () {
            //alert("Fecha que recibimos  del campo superior:"+ document.getElementById("FECHAPRESENTACION").value);
        });
    }


</script>

<div class="tab-page" id="tabPage100" style="height:520px;">
    <h2 class="tab" id="pestana100"><%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.cabecera")%></h2>
    <script type="text/javascript">tp1_p100 = tp1.addTabPage(document.getElementById("tabPage100"));</script>
    <table border="0" width="95%" cellspacing="2" cellpadding="2" align="center" class="contenidoPestanha">
        <tr>
            <td id="tdOcultos">
            </td>
        </tr>
        <%-- Certificado --%>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="102px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.certificado")%>
                        </td>
                        <td class="columnP">
                            <input type="text" name="codListaCertificados" id="codListaCertificados" size="12" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descListaCertificados"  id="descListaCertificados" size="112" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorListaCertificados" name="anchorListaCertificados">                                                
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <%-- Area y familia --%>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="102px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.area")%>:
                        </td>
                        <td class="columnP"  width="350px">
                            <textarea name="descArea" id="descArea" rows="2" cols="50" class="textareaTexto" readonly="true" value=""></textarea>
                        </td>
                        <td class="etiqueta" width="106px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.familia")%>:
                        </td>
                        <td class="columnP">
                            <textarea name="descFamilia" id="descFamilia" rows="2" cols="50" class="textareaTexto" readonly="true" value=""></textarea>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <%-- Decreto y tipoCP --%>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="102px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.decreto")%>:
                        </td>
                        <td class="columnP"  width="350px">
                            <input type="text" name="descDecreto"  id="descDecreto" size="50" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="106px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.tipoCP")%>:
                        </td>
                        <td class="columnP">
                            <input type="text" name="tipoCP"  id="tipoCP" size="50" class="inputTexto" readonly="true" value=""/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <%-- RDModif y RDDeroga --%>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="102px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.rdModif")%>:
                        </td>
                        <td class="columnP"  width="350px">
                            <input type="text" name="rdModif"  id="rdModif" size="50" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="106px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.rdDeroga")%>:
                        </td>
                        <td class="columnP">
                            <input type="text" name="rdDeroga"  id="rdDeroga" size="50" class="inputTexto" readonly="true" value=""/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <%-- Nivel, FechaRD, FechaRDModif y FechaRDDeroga --%>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="102px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.nivel")%>:
                        </td>
                        <td class="columnP" width="138px">
                            <input type="text" name="descNivel"  id="descNivel" size="10" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="102px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.fechaRD")%>:
                        </td>
                        <td class="columnP" width="111px">
                            <input type="text" name="fechaRD"  id="fechaRD" size="10" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="106px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.fechaRDModif")%>:
                        </td>
                        <td class="columnP" width="133px">
                            <input type="text" name="fechaRDModif"  id="fechaRDModif" size="10" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="106px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.fechaRDDeroga")%>:
                        </td>
                        <td class="columnP">
                            <input type="text" name="fechaRDDeroga"  id="fechaRDDeroga" size="10" class="inputTexto" readonly="true" value=""/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td class="sub3titulo">
                <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.unidadesCompetenciales")%>
            </td>
        </tr>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td>
                            <div id="tablaUnidadesCompetenciales" class="text" align="left"></div>
                        </td>
                    </tr>
                </table>

                <input type= "button" align="center" class="botonGeneral" value="Copiar datos" name="cmdCopiar" onclick="copiarDatos();">
            </td>   
        </tr>
        <tr>
            <td class="sub3titulo">
                <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.modulosFormativos")%>
            </td>
        </tr>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="120px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.codigoModFormacion")%>:
                        </td>
                        <td class="columnP" width="110px">
                            <input type="text" name="codModForm"  id="codModForm" size="10" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="90px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.duracion")%>:
                        </td>
                        <td class="columnP" width="110px">
                            <input type="text" name="duracionModForm"  id="duracionModForm" size="5" class="inputTexto" readonly="true" value=""/>
                        </td>
                        <td class="etiqueta" width="130px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.fechaPracticas")%>:
                        </td>
                        <td class="columnP">
                            <input type="text" name="fecPracticas"  id="fecPracticas" size="10" class="inputTexto" readonly="true" value=""/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="120px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.desModulo")%>:
                        </td>
                        <td class="columnP">
                            <textarea name="desModulo" id="desModulo" rows="2" cols="133" class="textareaTexto" readonly="true" value=""></textarea>
                        </td>

                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>					
                <!--table border="1" width="100%" cellpadding="0" cellspacing="0" align="center">
                        <tr>
                                <td class="columnP">
                                        <textarea name="desModulo" id="desModulo" rows="3" cols="143" class="textareaTexto" readonly="true" value=""></textarea>
                                </td>
                        </tr>
                </table-->					
            </td>   
        </tr>
        <tr>
            <td>
                <table border="0" width="100%" cellpadding="0" cellspacing="0" align="center">
                    <tr>
                        <td class="etiqueta" width="80px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.acreditado")%>:
                        </td>
                        <td class="columnP" width="210px">
                            <input type="text" name="codAcreditadoModForm" id="codAcreditadoModForm" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descAcreditadoModForm"  id="descAcreditadoModForm" size="20" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorAcreditadoModForm" name="anchorAcreditadoModForm">                                                
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </td>
                        <td class="etiqueta" width="100px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.motivoAcreditado")%>:
                        </td>
                        <td class="columnP" width="210px">
                            <input type="text" name="codMotivoAcreditadoModForm" id="codMotivoAcreditadoModForm" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descMotivoAcreditadoModForm"  id="descMotivoAcreditadoModForm" size="20" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorMotivoAcreditadoModForm" name="anchorMotivoAcreditadoModForm">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </td>
                        <td class="etiqueta" width="100px">
                            <%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.motivoNoAcreditado")%>:
                        </td>
                        <td class="columnP" >
                            <input type="text" name="codMotivoNoAcreditadoModForm" id="codMotivoNoAcreditadoModForm" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descMotivoNoAcreditadoModForm"  id="descMotivoNoAcreditadoModForm" size="20" class="inputTexto" readonly="true" value=""/>
                            <a href="" id="anchorMotivoNoAcreditadoModForm" name="anchorMotivoNoAcreditadoModForm">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2"> 
                            <input type= "button" class="botonGeneral"  id="btnExpedientes"  value="Exp. Rel" name="btnExpedientes" onclick="pulsarVerListaExp()"  />
                        </td>
                    </tr>
                </table>
            </td>   
        </tr>
        <tr>
            <td>
                &nbsp;
            </td>
        </tr>
        <tr>
            <td>
        <center>
            <input type= "button" class="botonGeneral" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario,"boton.grabar")%>" name="cmdGuardar" onclick="guardarPantalla();">
            <input type= "button" class="botonGeneral" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario,"boton.eliminar")%>" name="cmdBorrar" onclick="borrarPantalla();">
        </center>        
        </td>
        </tr>
        <tr>
            <td>
        <left>
            <input type= "button" class="botonMasLargo" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario,"boton.Avanzar.ToExperaExpedicion")%>" name="cmdAvanzarTramEspExp" onclick="avanzarToExperaExpedicion();">
        </left>
        </td>
        </tr>
    </table>
</div>


<script type="text/javascript">
    <% if(!btnExp) { %>
    document.getElementById("btnExpedientes").style.display = "none";
    <% } else { %>
    document.getElementById("btnExpedientes").style.display = "block";
    <% } %>

    listaCodigosCertificados[0] = "";
    listaDescripcionesCertificados[0] = "";

    listaCodigosMotivoNoAcreditado[0] = "";
    listaDescripcionesMotivoNoAcreditado[0] = "";

    /* Lista con los certificados recuperados de la BBDD */
    var contador = 0;
    <logic:iterate id="certificados" name="listaCertificados" scope="request">
    listaCodigosCertificados[contador] = '<bean:write name="certificados" property="codCertificado" />';
    listaDescripcionesCertificados[contador] = '<bean:write name="certificados" property="descCertificadoC" />';
    contador++;
    </logic:iterate>

    /* Lista con los centros recuperados de la BBDD */
    var contador2 = 0;
    <logic:iterate id="centros" name="listaCentros" scope="request">
    listaCodigosCentros[contador2] = '<bean:write name="centros" property="codigo" />';
    listaDescripcionesCentros[contador2] = '<bean:write name="centros" property="descripcion" />';
    contador2++;
    </logic:iterate>

    var contadorLanF = 0;
    <logic:iterate id="centrosLanF" name="listaCentrosLanF" scope="request">
    listaCodigosCentrosLanF[contadorLanF] = '<bean:write name="centrosLanF" property="codigo" />';
    listaDescripcionesCentrosLanF[contadorLanF] = '<bean:write name="centrosLanF" property="descripcion" />';
    contadorLanF++;
    </logic:iterate>

    var contador3 = 0;
    <logic:iterate id="motivos" name="listaMotivosNoAcreditado" scope="request">
    listaCodigosMotivoNoAcreditado[contador3] = '<bean:write name="motivos" property="codigo" />';
    listaDescripcionesMotivoNoAcreditado[contador3] = '<bean:write name="motivos" property="descripcion" />';
    contador3++;
    </logic:iterate>

    var contador4 = 0;
    <logic:iterate id="motivosAcreditadoModForm" name="listaMotivosAcreditacionModForm" scope="request">
    listaCodigosMotivosAcreditadoModForm[contador4] = '<bean:write name="motivosAcreditadoModForm" property="codigo" />';
    listaDescripcionesMotivosAcreditadoModForm[contador4] = '<bean:write name="motivosAcreditadoModForm" property="descripcion" />';
    contador4++;
    </logic:iterate>

    var contador5 = 0;
    <logic:iterate id="motivosNoAcreditadoModForm" name="listaMotivosNoAcreditacionModForm" scope="request">
    listaCodigosMotivosNoAcreditadoModForm[contador5] = '<bean:write name="motivosNoAcreditadoModForm" property="codigo" />';
    listaDescripcionesMotivosNoAcreditadoModForm[contador5] = '<bean:write name="motivosNoAcreditadoModForm" property="descripcion" />';
    contador5++;
    </logic:iterate>

    var contadorAuxiliar = 0;
    <logic:iterate id="origenAcreditacion" name="VALORES_COMBO_ORIGEN_ACREDITACION" scope="request">
    listaCodigosOrigenAcreditacion[contadorAuxiliar] = '<bean:write name="origenAcreditacion" property="codigo"/>';
    listaDescripcionesOrigenAcreditacion[contadorAuxiliar] = '<bean:write name="origenAcreditacion" property="descripcion" />';
    contadorAuxiliar++;
    </logic:iterate>


    comboListaCertificados = new Combo("ListaCertificados");
    comboListaCertificados.addItems(listaCodigosCertificados, listaDescripcionesCertificados);
    comboListaCertificados.change = cargarDatosCertificado;

    comboAcreditadoModFormacion = new Combo("AcreditadoModForm");
    comboAcreditadoModFormacion.addItems(listaCodigosSiNo, listaSiNo);
    comboAcreditadoModFormacion.change = acreditacionModForm;

    comboMotivoAcreditadoModFormacion = new Combo("MotivoAcreditadoModForm");
    comboMotivoAcreditadoModFormacion.addItems(listaCodigosMotivosAcreditadoModForm, listaDescripcionesMotivosAcreditadoModForm);

    comboMotivoNoAcreditadoModFormacion = new Combo("MotivoNoAcreditadoModForm");
    comboMotivoNoAcreditadoModFormacion.addItems(listaCodigosMotivosNoAcreditadoModForm, listaDescripcionesMotivosNoAcreditadoModForm);

    /* Creamos la tabla */
    // TABLA DE UNIDADES DE UNIDADES COMPETENCIALES
    if (document.all) {
        if (document.all)
            tablaUnidadesCompetenciales = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.all.tablaUnidadesCompetenciales);
        else
            tablaUnidadesCompetenciales = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaUnidadesCompetenciales'));
    } else {
        if (document.all)
            tablaUnidadesCompetenciales = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.all.tablaUnidadesCompetenciales);
        else
            tablaUnidadesCompetenciales = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaUnidadesCompetenciales'));
    }//if(document.all)

    tablaUnidadesCompetenciales.addColumna('58', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.codigoUnidad")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('135', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.descripcion")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('30', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.nivel")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('180', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.centro")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('80', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.acreditado")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('138', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.motivoNoAcreditado")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('100', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.origenAcreditacion")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('145', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.claveRegistral")%>', '', false);
    tablaUnidadesCompetenciales.addColumna('180', 'left', '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"label.centroLanF")%>', '', true);

    tablaUnidadesCompetenciales.height = 165;
    tablaUnidadesCompetenciales.displayCabecera = true;
    var lin = tablaUnidadesCompetenciales.lineas.length;
    tablaUnidadesCompetenciales.lineas = new Array();
    tablaUnidadesCompetenciales.displayTabla();

    //document.getElementById('cabecera8').style.display="none";

    /* var celda;
     for (var i=0; i<4;i++){
     celda='celda'+i+'8';            
     alert(celda);
     document.getElementById(celda).style.display = "none";            
     }
     */
    <% if(cargarDatos.equalsIgnoreCase("S")){ %>
    cargarDatosCert();
    <%}else{ %>
    bloquearModulosPracticas();
    <%}//if(cargarDatos == "S") %>

</script>
