<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide73.i18n.MeLanbide73I18n" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>

<%
    int idiomaUsuario = 1;
    int apl = 5; // Pendiente de mirar
    int codOrganizacion = 0;
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null)
        {
            if (usuario != null)
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuario.getAppCod();
                idiomaUsuario = usuario.getIdioma();
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex){}
    MeLanbide73I18n meLanbide73I18n = MeLanbide73I18n.getInstance();
    final List<GeneralComboVO> listaUsers = (List<GeneralComboVO>) request.getAttribute("listaUsuarios");
    final List<GeneralComboVO> listaUORs = (List<GeneralComboVO>) request.getAttribute("listaUOR");
    final List<GeneralComboVO> listaGrupos = (List<GeneralComboVO>) request.getAttribute("listaGrupos");

    //Combos
    Gson gson = new Gson();
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    gsonB.serializeNulls();

    gson = gsonB.create();

    final String listUsersGA = gson.toJson(listaUsers);
    final String listUORsGA = gson.toJson(listaUORs);
    final String listGruposGA = gson.toJson(listaGrupos);
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide73/melanbide73.css"/>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.2.1/dist/css/bootstrap.min.css" integrity="sha384-GJzZqFGwb1QTTN6wy59ffF1BuGJpLSa9DkKMp0DgiMDm4iYMj70gZWKYbI706tWS" crossorigin="anonymous">
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide73/ecaUtils.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/xtree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/WebFXImgRadioButtonTreeItem.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/uor.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide73/gruposuor/gestionGruposUOR.js"></script>
<script type="text/javascript" src="<html:rewrite page='/scripts/sir/barraPaginacionBuscador.js'/>?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript">
    function getXMLHttpRequest() {
        var aVersions = ["MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp", "Microsoft.XMLHttp"
        ];

        if (window.XMLHttpRequest) {
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                } catch (error) {
                    //no necesitamos hacer nada especial
                }
            }
        } else {
            return null;
        }
    }

    function inicializar() {
        window.focus();
    }
</script>
<body class="bandaBody" onload="javascript:{
            /*if (window.top.principal.frames[0]&&window.top.principal.frames['menu'].Go)
             window.top.principal.frames['menu'].Go();*/inicializar();
        }">

    <input type="hidden" id="listaGrupos" value=""/>
    <script>document.getElementById("listaGrupos").value = JSON.stringify(<%=listGruposGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    <input type="hidden" id="listaUsuarios" value=""/>
    <script>document.getElementById("listaUsuarios").value = JSON.stringify(<%=listUsersGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    <input type="hidden" id="listaUOR" value=""/>
    <script>document.getElementById("listaUOR").value = JSON.stringify(<%=listUORsGA%>, function (key, value) {
            return value == null ? "" : value;
        });</script>
    <input type="hidden" id="errorAnadirGrupoUOR" name="errorAnadirGrupoUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirGrupoUOR")%>" />
    <input type="hidden" id="errorModificarNombreGrupoUOR" name="errorModificarNombreGrupoUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorModificarNombreGrupoUOR")%>" />
    <input type="hidden" id="errorEliminarGrupoUOR" name="errorEliminarGrupoUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorEliminarGrupoUOR")%>" />
    <input type="hidden" id="errorAnadirUORaGrupo" name="errorAnadirUORaGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirUORaGrupo")%>" />
    <input type="hidden" id="errorEliminarUORdeGrupo" name="errorEliminarUORdeGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorEliminarUORdeGrupo")%>" />
    <input type="hidden" id="errorAnadirUsuarioaGrupo" name="errorAnadirUsuarioaGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirUsuarioaGrupo")%>" />
    <input type="hidden" id="errorEliminarUsuarioDeGrupo" name="errorEliminarUsuarioDeGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorEliminarUsuarioDeGrupo")%>" />
    <input type="hidden" id="errorAnadirDuplicadoGrupoUOR" name="errorAnadirDuplicadoGrupoUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirDuplicadoGrupoUOR")%>" />
    <input type="hidden" id="errorEliminarGrupoNoVacioUOR" name="errorEliminarGrupoNoVacioUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorEliminarGrupoNoVacioUOR")%>" />
    <input type="hidden" id="errorAnadirUORExistenteaGrupo" name="errorAnadirUORExistenteaGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirUORExistenteaGrupo")%>" />
    <input type="hidden" id="errorEliminarUORInexistenteEnGrupo" name="errorEliminarUORInexistenteEnGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorEliminarUORInexistenteEnGrupo")%>" />
    <input type="hidden" id="errorEliminarUsuarioInexistenteDeGrupo" name="errorEliminarUsuarioInexistenteDeGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorEliminarUsuarioInexistenteDeGrupo")%>" />
    <input type="hidden" id="errorAnadirUsuarioExistenteaGrupo" name="errorAnadirUsuarioExistenteaGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirUsuarioExistenteaGrupo")%>" />
    <input type="hidden" id="errorAnadirUsuarioaGrupoConOUInactiva" name="errorAnadirUsuarioaGrupoConOUInactiva" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirUsuarioaGrupoConOUInactiva")%>" />
    <input type="hidden" id="errorAnadirUsuarioaGrupoSinUOR" name="errorAnadirUsuarioaGrupoSinUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorAnadirUsuarioaGrupoSinUOR")%>" />
    <input type="hidden" id="errorNombreGrupoUORDemasiadoLargo" name="errorNombreGrupoUORDemasiadoLargo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.errorNombreGrupoUORDemasiadoLargo")%>" />
    <input type="hidden" id="advertenciaUor" name="advertenciaUor" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.advertenciaUor")%>" />
    <input type="hidden" id="advertenciaGrupo" name="advertenciaGrupo" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.advertenciaGrupo")%>" />
    <input type="hidden" id="respuestaSiNO" name="respuestaSiNO" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.respuestaSiNO")%>" />
    <input type="hidden" id="advertenciaGrupoAnadirEliminarUOR" name="advertenciaGrupoAnadirEliminarUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.advertenciaGrupoAnadirEliminarUOR")%>" />
    <input type="hidden" id="conjuncion" name="conjuncion" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.conjuncion")%>" />
    <input type="hidden" id="advertenciaQuitarUsuarioGrupoUOR1" name="advertenciaQuitarUsuarioGrupoUOR1" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.advertenciaQuitarUsuarioGrupoUOR1")%>" />
    <input type="hidden" id="advertenciaQuitarUsuarioGrupoUOR2" name="advertenciaQuitarUsuarioGrupoUOR2" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.advertenciaQuitarUsuarioGrupoUOR2")%>" />
    <input type="hidden" id="advertenciaEliminarUOR" name="advertenciaEliminarUOR" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "msg.advertenciaEliminarUOR")%>" />

    <form id="formGruposUOR">
        <div id="titulo" class="txttitblanco"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.gruposUORusuarios")%></div>
        <div class="contenidoPantalla">
            <table width="100%" border="1">
                <tr>
                    <td class="etiqueta" style="width:10%;"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.grupoUOR")%></td>
                    <td class="etiqueta" style="width:20%;">
                        <input type="text" name="codGrupos" id="codGrupos" size="5" class="inputTextoObligatorio" value="">
                        <input type="text" name="descGrupos"  id="descGrupos" size="65" class="inputTextoObligatorio" style="width:200px" readonly="true" value="">
                        <A href="" id="anchorGrupos" name="anchorGrupos">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupos"
                                  name="botonGrupos" style="cursor:hand;"></span>
                        </A>
                    </td>
                    <td rowspan="5" align="justify"  valign="top">
                        <h4 class="etiqueta"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.listaUORGrupo")%></h4>
                        <div id="divUORS" class="webfx-tree-div" style="height:300px;width:100%;overflow:auto;"></div>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" style="width:10%;"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.uORs")%></td>
                    <td class="etiqueta" style="width:20%;">
                        <input type="text" name="codUOR" id="codUOR" size="5" class="inputTextoObligatorio" value="">
                        <input type="text" name="descUOR"  id="descUOR" size="65" class="inputTextoObligatorio" style="width:200px" readonly="true" value="">
                        <A href="" name="anchorUOR" id="anchorUOR">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUOR"
                                  name="botonUOR" style="cursor:hand;"></span>
                        </A>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="etiqueta">
                        <div id="formUnidadesOrganicas">
                            <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.anadirAlGrupo")%>"
                                   name="cmdAltaUORGrupo" id="cmdAltaUORGrupo" onclick="pulsarAnadirUOR();" accesskey="A">
                            <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.quitarDelGrupo")%>"
                                   name="cmdEliminarUORGrupo" id="cmdEliminarUORGrupo" onclick="pulsarEliminarUOR();" accesskey="E">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td class="etiqueta" style="width:10%;"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.grupoUOR")%></td>
                    <td class="etiqueta" style="width:20%;">
                        <input type="text" name="nombreGrupoUOR"  id="nombreGrupoUOR" size="65" class="inputTextoObligatorio" style="width:380px" value="">
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="etiqueta">
                        <div id="tablaBotonesUOR" class="botoneraPrincipal">
                            <form action="" name="botones" id="botones">
                                <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.alta")%>"
                                       name="cmdAltaGrupo" id="cmdAltaGrupo" onclick="pulsarAlta();" accesskey="A">
                                <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.modificar")%>"
                                       name="cmdModificarGrupo" id="cmdModificarGrupo" onclick="pulsarModificar();" accesskey="M">
                                <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.eliminar")%>"
                                       name="cmdEliminarGrupo" id="cmdEliminarGrupo" onclick="pulsarEliminar();" accesskey="E">
                                <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.limpiar")%>"
                                       name="cmdLimpiarGrupo" id="cmdLimpiarGrupo" onclick="pulsarLimpiar();" accesskey="L">
                            </form>
                        </div>
                    </td>
                </tr>
                <tr><td colspan="3"></td></tr>
                <tr>
                    <td class="etiqueta" style="width:10%;"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.usuarios")%></td>
                    <td class="etiqueta" style="width:20%;">
                        <input type="text" name="codUsuarios" id="codUsuarios" size="5" class="inputTextoObligatorio" value="">
                        <input type="text" name="descUsuarios"  id="descUsuarios" size="65" class="inputTextoObligatorio" style="width:200px" readonly="true" value="">
                        <A href="" id="anchorUsuarios" name="anchorUsuarios">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonUsuarios"
                                  name="botonUsuarios" style="cursor:hand;"></span>
                        </A>
                    </td>
                    <td rowspan="2" align="justify"  valign="top">
                        <h4 class="etiqueta"><%=meLanbide73I18n.getMensaje(idiomaUsuario, "label.listaUsuariosGrupo")%></h4>
                        <div id="divUsuarios" class="webfx-tree-div" style="height:300px;width:100%;overflow:auto;"></div>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" class="etiqueta">
                        <div id="tablaBotonesUser" class="botoneraPrincipal">
                            <form action="" name="botones" id="botones">
                                <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.alta")%>"
                                       name="cmdAltaUser" id="cmdAltaUser" onclick="pulsarAltaUsuario();" accesskey="A">
                                <input type= "button" class="botonGeneral" value="<%=meLanbide73I18n.getMensaje(idiomaUsuario, "boton.eliminar")%>"
                                       name="cmdEliminarUser" id="cmdEliminarUser" onclick="pulsarQuitarUsuario();" accesskey="E">
                            </form>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</body>