<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="java.text.MessageFormat"%>

<html:html>

<head><jsp:include page="/jsp/sge/tpls/app-constants.jsp" />

<TITLE>::: Nueva convocatoria:::</TITLE>
<%@ include file="/jsp/plantillas/Metas.jsp" %>

<!-- Estilos -->

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%><c:out value="${sessionScope.usuario.css}"/>">


<%
  int idioma=1;
  int apl=1;
  int munic = 0;
    if (session!=null){
      UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
        if (usuario!=null) {
          idioma = usuario.getIdioma();
          apl = usuario.getAppCod();
          munic = usuario.getOrgCod();
        }
  }
  String municipio = Integer.toString(munic);
  String aplicacion = Integer.toString(apl);
  String modoInicio = "";
  if (session.getAttribute("modoInicio") != null) {
    modoInicio = (String) session.getAttribute("modoInicio");
    session.removeAttribute("modoInicio");
  }
  String lectura = "";
  if (session.getAttribute("lectura") != null) {
    lectura = (String) session.getAttribute("lectura");
    session.removeAttribute("lectura");
  }
  


%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />



<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>

<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>



<SCRIPT type="text/javascript">

var codPlurianualidades= new Array();
var descPlurianualidades=new Array();
var codJustificacion= new Array();
var descJustificacion=new Array();
var argumentos = window.dialogArguments;


function inicializar() {
//combo Plurianualidades
  codPlurianualidades[0]=0;
  codPlurianualidades[1]=1;
  descPlurianualidades[0]='NO';
  descPlurianualidades[1]='SI';
  comboPlurianualidades.addItems(codPlurianualidades, descPlurianualidades);

  //combo Plurianualidades
  codJustificacion[0]=0;
  codJustificacion[1]=1;
  descJustificacion[0]='NO';
  descJustificacion[1]='SI';
  comboJustificacion.addItems(descJustificacion, descJustificacion);

  

}
function pulsarSalir() {
  top.close();
}
function getXMLHttpRequest(){
    var aVersions = [ "MSXML2.XMLHttp.5.0",
        "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
        "MSXML2.XMLHttp","Microsoft.XMLHttp"
      ];

    if (window.XMLHttpRequest){
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
    }else if (window.ActiveXObject){
        // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
        for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                }catch (error) {
                //no necesitamos hacer nada especial
                }
         }
    }
}

function pulsarAceptar () {
    var  codOrganizacion = null;
    var  codProcedimiento = null;
    var argumentos = window.dialogArguments;
	
    
    if((argumentos[0]!=null) && (argumentos[1]!=null)){
        codProcedimiento = argumentos[0];
        codOrganizacion = argumentos[1];
    }else{
        codProcedimiento = "";
        codOrganizacion = "";
    }
    
    var o = new Object();
    var convocatoria     = document.getElementById("convocatoria").value;
    o.convocatoria = convocatoria;
    var plurianualidades = document.getElementById("descPlurianualidades").value;
    o.plurianualidades = plurianualidades;
    var numAnos          = document.getElementById("numAnos").value;
    o.numAnos = numAnos;
    var justificacion    = document.getElementById("descJustificacion").value;
    o.justificacion = justificacion;
    var numPagos         = document.getElementById("numPagos").value;
    o.numPagos = numPagos;
    //mensajes de error personalizados
    o.convocatoriaExiste = '';
    o.camposConvocatoriaVacios = '';
    o.errorInsertarConvocatoria = '';
    
    if ((convocatoria == '')|| (plurianualidades == '')|| (numAnos == '')|| (justificacion == '')|| (numPagos == '')){
        jsp_alerta('A','<%=descriptor.getDescripcion("msjObligTodos")%>');
    }else{
        window.returnValue = o;

        var ajax = getXMLHttpRequest();
        if(ajax!=null){
            var url = "<%= request.getContextPath() %>" + "/PeticionModuloIntegracion.do";
            var parametros = "&tarea=procesar&operacion=procesarAltaConvocatoria&modulo=MEIKUS01&tipo=2&codOrganizacion=" + codOrganizacion + "&codProcedimiento="+ codProcedimiento + "&convocatoria=" + convocatoria + "&plurianualidades=" + plurianualidades + "&numAnos=" + numAnos + "&justificacion=" + justificacion + "&numPagos=" + numPagos;

            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded;  charset=ISO-8859-1");
            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
            ajax.send(parametros);

            try{

                if (ajax.readyState==4){
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        // En IE el XML viene en responseText y no en la propiedad responseXML

                        var text = ajax.responseText;
                        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async="false";
                        xmlDoc.loadXML(text);

                    }else{
                        // En el resto de navegadores el XML se recupera
                        // de la propiedad  responseXML
                        xmlDoc = ajax.responseXML;
                    }

                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                if (nodos[0]!=null){
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoError = null;
                    var errorInsertaConv = null;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_ERROR"){
                            codigoError = hijos[j].childNodes[0].nodeValue;
                            if (codigoError==1){
                                    o.convocatoria = '';
                                    o.plurianualidades = '';
                                    o.numAnos = "";
                                    o.justificacion = '';
                                    o.numPagos = '';
                                    o.convocatoriaExiste = 1;
                            }
                        }//if(hijos[j].nodeName=="CODIGO_ERROR")
                        if(hijos[j].nodeName=="ERROR_INSERTA_CONV"){
                            errorInsertaConv = hijos[j].childNodes[0].nodeValue;
                            if (errorInsertaConv==1){
                                    o.convocatoria = '';
                                    o.plurianualidades = '';
                                    o.numAnos = "";
                                    o.justificacion = '';
                                    o.numPagos = '';
                                    o.errorInsertarConvocatoria = 1;
                            }
                        }//if(hijos[j].nodeName=="ERROR_INSERTA_CONV")
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                }
               window.returnValue = o;

        }
    } catch(err){
     //   alert("descripción: " + err.description);
    }
    top.close();
    }//cierre else

}//cierre if

}


function SoloCaracterValidos(objeto){
   var numeros='1234567890';
   xAMayusculas(objeto);
   var valido = false;

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<4;i++){
            if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1) {
                valido = true;
            }else{
               valido = false;
            }
            if(valido) salida = salida + original.charAt(i);
        }
        objeto.value=salida.toUpperCase();
    }
}

function SoloCaracterValidosSinCero(objeto){
   var numeros='1234567890';
   var numerosNoCero='123456789';
   xAMayusculas(objeto);
   var valido = false;

    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<4;i++){
            if(i==0){
            if(numerosNoCero.indexOf(original.charAt(i).toUpperCase())!=-1) {
                valido = true;
            }else{
               valido = false;
            }
            }else{
              if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1) {
                valido = true;
            }else{
               valido = false;
            }
            }
            if(valido) salida = salida + original.charAt(i);
        }
        objeto.value=salida.toUpperCase();
    }
}
//años
function SoloCaracterValidosParaAnos(objeto){
   var numeros='1234567890';
   var numerosNoCero='123456789';
   var plurianualidades = document.getElementById("descPlurianualidades").value;
   xAMayusculas(objeto);
   var valido = false;
    if (objeto){
        var original = objeto.value;
        var salida = "";
        for(i=0;i<4;i++){
            if((i==0)&&(plurianualidades == 'SI')){
            if(numerosNoCero.indexOf(original.charAt(i).toUpperCase())!=-1) {
                valido = true;
            }else{
               valido = false;
            }
            }else{
              if(numeros.indexOf(original.charAt(i).toUpperCase())!=-1) {
                valido = true;
            }else{
               valido = false;
            }
            }
            if(valido) salida = salida + original.charAt(i);
        }
        objeto.value=salida.toUpperCase();
    }
}


</SCRIPT>

</HEAD>

<BODY class="bandaBody" onload="javascript:{ inicializar()}">



    <div id="hidepage" style="position: absolute; top:150px; z-index:10; visibility: hidden;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span><%=descriptor.getDescripcion("msjCargDatos")%></span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:5%;height:20%;"></td>
                                        <td class="imagenHide"></td>
                                        <td style="width:5%;height:20%;"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" style="height:10%" ></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>


<form action="/PeticionModuloIntegracion.do" method="POST">

<table id="tabla99" style="width: 100%; height: 150px" cellpadding="0px" cellspacing="0px">
    <tr>
        <td>
            <table class="bordeExteriorPantalla" style="width: 844px" cellspacing="0px" cellpadding="1px">
                <tr>
                    <td>
                        <table class="bordeInteriorPantalla" style="width: 841px" cellspacing="1px">
                            <tr>
                                <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco">Nueva convocatoria</td>
                            </tr>
                            <tr>
                                <td class="separadorTituloPantalla"></td>
                            </tr>
                            <tr>
                                <td class="contenidoPantalla" valign="top">
                                    <table cellspacing="0px" class="cuadroFondoBlanco" style="padding-top: 5px; padding-bottom: 5px" cellpadding="5px" align="center">
                                        <TR>
                                            <td style="width:100%;  height:23px;" class="encabezadoRegistro"></td>
                                        </TR>
                                        <TR>
                                            <TD>
                                                <table id ="tablaDatosGral" cellpadding="0px" border="0px">
                                                <tr>
                                                        <td style="width: 100px" class="etiqueta">Convocatoria:</td>
                                                        <td colspan="2">
                                                        <input type="text" class="inputTextoObligatorio" id="convocatoria" name="convocatoria" style="width: 150px" size="25" maxlength="20"
                                                                       onkeyup="return SoloCaracterValidos(this);"/>
                                                        </td>
                                                </tr>
                                                <tr>
                                                        <td class="etiqueta">Plurianualidades:</td>
                                                        <td class="columnP">
                                                        <input styleId="obligatorio" class="inputTextoObligatorio" name="descPlurianualidades"  id="descPlurianualidades" size="2" class="inputTexto" readonly="true" value=""/>
                                                    <a href="" id="anchorCodigo" name="anchorPlurianualidades">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" style="border: 0">
                                                    </a>
                                                    </td>
                                                 </tr>
                                                 <tr>
                                                        <td style="width: 100px" class="etiqueta">Nº. de años:</td>
                                                        <td colspan="2">
                                                        <input type="text" class="inputTextoObligatorio" id="numAnos" name="numAnos" style="width: 150px" size="25" maxlength="20"
                                                                       onkeyup="return SoloCaracterValidosParaAnos(this);"/>
                                                        </td>
                                                  </tr>
                                                  <tr>
                                                        <td class="etiqueta">Justificación:</td>
                                                        <td class="columnP">
                                                        <input styleId="obligatorio" class="inputTextoObligatorio" name="descJustificacion"  id="descJustificacion" size="2" class="inputTexto" readonly="true" value=""/>
                                                    <a href="" id="anchorCodigo" name="anchorJustificacion">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion" name="botonAplicacion" style="border: 0">
                                                    </a>
                                                    </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="width: 100px" class="etiqueta">Nº de pagos:</td>
                                                        <td colspan="2">
                                                        <input type="text" class="inputTextoObligatorio" id="numPagos" name="numPagos" style="width: 150px" size="25" maxlength="20"
                                                                       onkeyup="return SoloCaracterValidosSinCero(this);"/>
                                                        </td>
                                                    </tr>
                                                    
                                                </table>
                                            </TD>
                                        </TR>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td class="sombraLateral">
            <table width="1px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td style="height: 1px" class="parteSuperior"></td>
                </tr>
                <tr>
                    <td style="height: 219px" class="parteInferior"></td>
                </tr>
            </table>
        </td>
        <!-- Fin sombra lateral. -->
    </tr>

    <!-- Sombra inferior. -->
    <tr>
        <td colspan="2" class="sombraInferior">
            <table cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td style="width: 1px" class="parteIzquierda"></td>
                    <td style="width: 843px" class="parteDerecha"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>





<!-- Separador. -->
<table height="2px" cellpadding="0px" cellspacing="0px" border="0px">
  <tr>
    <td></td>
  </tr>
</table>
<!-- Fin separador. -->

<!-------------------------------------- BOTONES. ------------------------------------------>
<DIV id="capaBotones1" name="capaBotones1" STYLE="position:absolute;width:100%;height:0px;visibility:visible; text-align: right">
    <table cellpadding="0px" cellspacing="0px" border="0px" align="right">
        <tr>
            <td>
                <INPUT type= "button" class="botonGeneral" accesskey="A" value="<%=descriptor.getDescripcion("gbAceptar")%>" name="cmdAceptar"  onclick="pulsarAceptar();">
            </td>
            <!-- Fin botón REGISTRAR ALTA. -->
            <td style="width: 2px"></td>
            <!-- Botón CANCELAR ALTA. -->
            <td>
                <INPUT type= "button" class="botonGeneral" accesskey="S" value="<%=descriptor.getDescripcion("gbSalir")%>" name="cmdSalir"  onclick="pulsarSalir();">
            </td>
        </tr>
    </table>
</DIV>
</form>



<script language="JavaScript1.2">
var comboPlurianualidades = new Combo("Plurianualidades");
var comboJustificacion = new Combo("Justificacion");

function mostrarCapasBotones(nombreCapa) {
  document.getElementById('capaBotones1').style.visibility='hidden';
  document.getElementById('capaBotones2').style.visibility='hidden';
  
}

<%String Agent = request.getHeader("user-agent");%>

var coordx=0;
var coordy=0;


<%if(Agent.indexOf("MSIE")==-1) {%> //Que no sea IE
    window.addEventListener('mousemove', function(e) {
        coordx = e.clientX;
        coordy = e.clientY;
    }, true);
<%}%>

document.onmouseup = checkKeys;

function checkKeysLocal(evento,tecla) {
    var teclaAuxiliar = "";
    if(window.event){
        evento = window.event;
        teclaAuxiliar = evento.keyCode;
    }else{
        teclaAuxiliar = evento.which;
    }


	

    keyDel(evento);
}
</script>

</BODY>

</html:html>

