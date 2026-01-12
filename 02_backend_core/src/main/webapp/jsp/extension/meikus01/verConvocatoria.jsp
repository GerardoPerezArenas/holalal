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

<TITLE>::: Ver convocatoria:::</TITLE>
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


var descPlurianualidades=new Array();
var codJustificacion= new Array();
var descJustificacion=new Array();
var argumentos = window.dialogArguments;


function inicializar() {
    
    if((argumentos!=null)){
        document.getElementById("convocatoria").value = argumentos[2];
        document.getElementById("descPlurianualidades").value = argumentos[3];
        document.getElementById("numAnos").value = argumentos[4];
        document.getElementById("descJustificacion").value = argumentos[5];
        document.getElementById("numPagos").value = argumentos[6];
          


    }else{
        document.getElementById("convocatoria").value = "";
        document.getElementById("descPlurianualidades").value = "";
        document.getElementById("numAnos").value = "";
        document.getElementById("descJustificacion").value = "";
        document.getElementById("numPagos").value = "";
    }

}
function pulsarSalir() {
  top.close();
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
                                <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco">Ver convocatoria</td>
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
                                                        <input type="text" class="inputTextoObligatorio" id="convocatoria" name="convocatoria" style="width: 150px" size="25" maxlength="20" readonly="true" disabled/>
                                                        </td>
                                                </tr>
                                                <tr>
                                                        <td class="etiqueta">Plurianualidades:</td>
                                                        <td class="columnP">
                                                        <input styleId="obligatorio" class="inputTextoObligatorio" name="descPlurianualidades"  id="descPlurianualidades" size="2" class="inputTexto" readonly="true" disabled/>
                                                    <a href="" id="anchorPlurianualidades" name="anchorPlurianualidades">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable2.gif" id="botonAplicacion" name="botonAplicacion" style="border: 0">
                                                    </a>
                                                    </td>
                                                 </tr>
                                                 <tr>
                                                        <td style="width: 100px" class="etiqueta">Nº. de años:</td>
                                                        <td colspan="2">
                                                        <input type="text" class="inputTextoObligatorio" id="numAnos" name="numAnos" style="width: 150px" size="25" maxlength="20"
                                                                       readonly="true" disabled/>
                                                        </td>
                                                  </tr>
                                                  <tr>
                                                        <td class="etiqueta">Justificación:</td>
                                                        <td class="columnP">
                                                        <input styleId="obligatorio" class="inputTextoObligatorio" name="descJustificacion"  id="descJustificacion" size="2" class="inputTexto" readonly="true" disabled/>
                                                    <a href="" id="anchorCodigo" name="anchorJustificacion">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable2.gif" id="botonAplicacion" name="botonAplicacion" style="border: 0">
                                                    </a>
                                                    </td>
                                                    </tr>
                                                    <tr>
                                                        <td style="width: 100px" class="etiqueta">Nº de pagos:</td>
                                                        <td colspan="2">
                                                        <input type="text" class="inputTextoObligatorio" id="numPagos" name="numPagos" style="width: 150px" size="25" maxlength="20"
                                                                       readonly="true" disabled/>
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


