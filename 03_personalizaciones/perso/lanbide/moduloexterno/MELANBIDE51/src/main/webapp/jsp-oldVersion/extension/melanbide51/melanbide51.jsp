<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.i18n.MeLanbide51I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.vo.ControlAccesoVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide51.vo.DesplegableAdmonLocalVO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null)
    {
        try
        {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }

    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide51I18n meLanbide51I18n = MeLanbide51I18n.getInstance();
    
    //String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    //String nombreModulo     = request.getParameter("nombreModulo");
    
%>
<script type="text/javascript">
    var comboListaMotivoVisita_busq;
    var listaCodigosMotivoVisita_busq = new Array();
    var listaDescripcionesMotivoVisita_busq = new Array();
    
    function buscaCodigoMotivoVisita_busq(codMotivoVisita) {
        comboListaMotivoVisita_busq.buscaCodigo(codMotivoVisita);
    }

    function cargarDatosMotivoVisita_busq() {
        var codMotivoVisitaSeleccionado = document.getElementById("codListaMotivoVisita_busq").value;
        buscaCodigoMotivoVisita_busq(codMotivoVisitaSeleccionado);
    }
    

</script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide51/melanbide51.css"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/lanbide.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/JavaScriptUtil.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/Parsers.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide51/InputMask.js"></script>
<!-- Eventos onKeyPress compatibilidad firefox  -->
<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>

<body class="bandaBody" onload="javascript:{if (window.top.principal.frames[0]&&window.top.principal.frames['menu'].Go)
        window.top.principal.frames['menu'].Go();}">        <!--inicializar();-->
    <br/>
    <form>
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
    <div>
        <div style="clear: both;">
            <div id="div_label" class="sub3titulo" style=" text-align: center;">
                <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide51I18n.getMensaje(idiomaUsuario, "legend.titulo.busqueda")%></label>
            </div>
            <br/>
            <div>
                <table id="tablaBusqueda">
                    <tbody>
                        <tr>
                            <td style="width: 10%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.dni_nif")%>
                                </label>                            
                            </td>
                            <td>
                                <input id="dni_nif_busq" name="dni_nif_busq" type="text" class="inputTexto" size="15" maxlength="15">
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                                </label>                            
                            </td>
                            <td>
                                <input id="nombre_busq" name="nombre_busq" type="text" class="inputTexto" size="30" maxlength="50">
                            <!--</td>
                            <td>-->
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                                </label>
                                <input id="apellido1_busq" name="apellido1_busq" type="text" class="inputTexto" size="30" maxlength="50">
                            <!--</td>
                            <td>-->
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                                </label>
                                <input id="apellido2_busq" name="apellido2_busq" type="text" class="inputTexto" size="30" maxlength="50">
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.entradas")%>
                                </label>                            
                            </td>
                            <td  style="width: 25%">
                                <table style="width: 100%">
                                    <tbody>
                                        <tr>
                                            <td style="width: 50px">
                                            <label class="etiqueta" style="float: left;">
                                                <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.meLanbide51Fecha")%>
                                            </label>
                                            </td>
                                            <td>
                                            <div style="float: left;">
                                                <input type="text" class="inputTxtFecha" 
                                                       id="meLanbide51Fecha_busqE" name="meLanbide51Fecha_busqE"
                                                       maxlength="10"  size="10"
                                                       value=""
                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                       onfocus="javascript:this.select();"/>
                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaE(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide51Fecha_busqE" name="calMeLanbide51Fecha_busqE" border="0" 
                                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                </A>
                                            </div>
                                            <label class="etiqueta" style="float: left; margin-left: 5px">
                                                <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.hora")%>
                                            </label>
                                            <input id="hora_entrada_busq" name="hora_entrada_busq" type="text" class="inputTexto" size="10" maxlength="8" 
                                                   onkeypress="formatearHora(this,event)" onblur="comprobarMaskaraHora(this)" 
                                                   style="float: left; margin-left: 5px">
                                        <!--</td>
                                        <td>-->
                                            <label class="etiqueta" style="float: left; margin-left: 30px; margin-right: 30px ">
                                                <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                            </label>
                                            <input id="hora_entrada_busqF" name="hora_entrada_busqF" type="text" class="inputTexto" size="10" maxlength="8"   
                                                   onkeypress="formatearHora(this,event)" onblur="comprobarMaskaraHora(this)" 
                                                   style="float: left; margin-left: 5px">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.salidas")%>
                                </label>                            
                            </td>
                            <td style="width: 15%">
                                <table style="width: 100%">
                                    <tbody>
                                        <tr>
                                            <td style="width: 50px">
                                            <label class="etiqueta" style="float: left;">
                                                <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.meLanbide51Fecha")%>
                                            </label>
                                            </td>
                                            <td>
                                            <div style="float: left;">
                                                <input type="text" class="inputTxtFecha" 
                                                       id="meLanbide51Fecha_busqS" name="meLanbide51Fecha_busqS"
                                                       maxlength="10"  size="10"
                                                       value=""
                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                       onfocus="javascript:this.select();"/>
                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaS(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide51Fecha_busqS" name="calMeLanbide51Fecha_busqS" border="0" 
                                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                </A>
                                            </div>
                                            <label class="etiqueta" style="float: left; margin-left: 5px">
                                                <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.hora")%>
                                            </label>    
                                            <input id="hora_salida_busq" name="hora_salida_busq" type="text" class="inputTexto" size="10" maxlength="8"   
                                                   onkeypress="formatearHora(this,event)" onblur="comprobarMaskaraHora(this)" 
                                                   style="float: left; margin-left: 5px">
                                        <!--</td>
                                        <td>-->
                                            <label class="etiqueta" style="float: left; margin-left: 30px; margin-right: 30px">
                                                <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                            </label>
                                            <input id="hora_salida_busqF" name="hora_salida_busqF" type="text" class="inputTexto" size="10" maxlength="8"   
                                                   onkeypress="formatearHora(this,event)" onblur="comprobarMaskaraHora(this)" 
                                                   style="float: left; margin-left: 5px">
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado")%>
                                </label>
                            </td>                 
                            <td>
                                <label class="etiqueta"><%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado.dentro")%></label>
                                <input id="option_dentro" name="option_estado" type="radio" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado.dentro")%>">
                                <label class="etiqueta"><%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado.fuera")%></label>
                                <input id="option_fuera" name="option_estado" type="radio" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado.fuera")%>">
                                <label class="etiqueta"><%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado.todos")%></label>
                                <input id="option_todos" name="option_estado" type="radio" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.busqueda.estado.todos")%>">
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.empresa_entidad")%>
                                </label>
                            </td>                 
                            <td>
                                <input id="empresa_entidad_busq" name="empresa_entidad_busq" type="text" class="inputTexto" size="50" maxlength="150">
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 15%">
                                <label class="etiqueta">
                                    <%=meLanbide51I18n.getMensaje(idiomaUsuario,"label.motivovisita")%>
                                </label>
                            </td>                 
                            <td style="width: 80%">
                                <input type="text" name="codListaMotivoVisita_busq" id="codListaMotivoVisita_busq" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaMotivoVisita_busq"  id="descListaMotivoVisita_busq" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaMotivoVisita_busq" name="anchorListaMotivoVisita_busq">
                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                         name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand; border: 0px;">
                                </a>
                                <!--   Celda de la Imagen Busca      -->
                                <IMG  id="botonBusqueda"  name="botonBusqueda"  style="CURSOR: hand; BORDER-TOP: 0px; BORDER-RIGHT: 0px; BORDER-BOTTOM: 0px; BORDER-LEFT: 0px; margin-left: 100px; " 
                                      src="<%=request.getContextPath()%>/images/prismaticos.gif" onclick="pulsarBusquedaFiltrando()">
                            </td>
                        </tr>
                    </tbody>                
                </table>                
            </div>
        </div>
        <div>
            <div id="div_label" class="sub3titulo" style=" text-align: center;">
                <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide51I18n.getMensaje(idiomaUsuario, "legend.titulo.tabla")%></label>
            </div>    
            <br/>
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 290px;">     <!--onscroll="deshabilitarRadios();"-->
                <div id="listaAccesos" style="padding: 5px; width:990px; height: 280px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
            </div>
            <br/><br>
            <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoAcceso" name="btnNuevoAcceso" class="botonGeneral"  value="<%=meLanbide51I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoAcceso();">
                    <input type="button" id="btnModificarAcceso" name="btnModificarAcceso" class="botonGeneral" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAcceso();">
                    <input type="button" id="btnEliminarAcceso" name="btnEliminarAcceso"   class="botonGeneral" value="<%=meLanbide51I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAcceso();">
            </div>
        </div>
    </div>
                
    <!--Script Ejecucion Elementos Pagina   legendAzul-->
    <script type="text/javascript">
        // rellenamos la lista desplegables de motivos para busquedas
        listaCodigosMotivoVisita_busq[0] = "";
        listaDescripcionesMotivoVisita_busq[0] = "";

        /* Lista con los Vlaores de desplegable Motivos  Visita recuperados En BBDD */
        var contador = 0;
        
        <logic:iterate id="motivoVisita_busq" name="listaMotivoVisita_busq" scope="request">
        listaCodigosMotivoVisita_busq[contador] = ['<bean:write name="motivoVisita_busq" property="des_val_cod" />'];
        listaDescripcionesMotivoVisita_busq[contador] = ['<bean:write name="motivoVisita_busq" property="des_nom" />'];
        contador++;
        </logic:iterate>
            
        comboListaMotivoVisita_busq = new Combo("ListaMotivoVisita_busq");
        comboListaMotivoVisita_busq.addItems(listaCodigosMotivoVisita_busq, listaDescripcionesMotivoVisita_busq);
        comboListaMotivoVisita_busq.change = cargarDatosMotivoVisita_busq;               
        
        
        //Tabla Accesos
        var tabaAccesos;
        var listaAccesos = new Array();
        var listaAccesosTabla = new Array();
        
        //right - left - center
        tabaAccesos = new Tabla(document.getElementById('listaAccesos'), 2755);
        tabaAccesos.addColumna('150','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col1")%>");
        tabaAccesos.addColumna('150','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col2")%>");
        tabaAccesos.addColumna('150','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col3")%>");
        tabaAccesos.addColumna('150','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col4")%>");
        tabaAccesos.addColumna('250','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col5")%>");
        tabaAccesos.addColumna('100','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col6")%>");
        tabaAccesos.addColumna('250','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col7")%>");
        tabaAccesos.addColumna('250','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col8")%>");
        tabaAccesos.addColumna('250','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col9")%>");
        tabaAccesos.addColumna('450','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col10")%>");
        tabaAccesos.addColumna('50','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col11")%>");
        tabaAccesos.addColumna('50','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col12")%>");
        tabaAccesos.addColumna('500','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col13")%>");

        tabaAccesos.displayCabecera=true;
        tabaAccesos.height = 300;

        <%  		
            ControlAccesoVO objectVO = null;
            List<ControlAccesoVO> List = null;
            if(request.getAttribute("listaAccesos")!=null){
                List = (List<ControlAccesoVO>)request.getAttribute("listaAccesos");
            }													
            if (List!= null && List.size() >0){
                for (int indice=0;indice<List.size();indice++)
                {
                    objectVO = List.get(indice);

        %>
            listaAccesos[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getFecha()%>','<%=objectVO.getNo_tarjeta()%>','<%=objectVO.getNif_Dni()%>','<%=objectVO.getNombre()%>','<%=objectVO.getApellido1()%>','<%=objectVO.getApellido2()%>','<%=objectVO.getTelefono()%>','<%=objectVO.getEmpresa_entidad()%>','<%=objectVO.getServicio_visitado()%>','<%=objectVO.getPersona_contacto()%>','<%=objectVO.getCod_mot_visita()%>','<%=objectVO.getHora_entrada()%>','<%=objectVO.getHora_salida()%>','<%=objectVO.getObservaciones()%>'];
            listaAccesosTabla[<%=indice%>] = ['<%=objectVO.getFecha()%>','<%=objectVO.getNo_tarjeta()%>','<%=objectVO.getNif_Dni()%>','<%=objectVO.getNombre()%>',('<%=objectVO.getApellido1()%>'=='null'?"":'<%=objectVO.getApellido1()%>')+ " " +('<%=objectVO.getApellido2()%>'=='null'?"":'<%=objectVO.getApellido2()%>'),('<%=objectVO.getTelefono()%>'=='null'?"":'<%=objectVO.getTelefono()%>'),('<%=objectVO.getEmpresa_entidad()%>'=='null'?"":'<%=objectVO.getEmpresa_entidad()%>'),('<%=objectVO.getServicio_visitado()%>'=='null'?"":'<%=objectVO.getServicio_visitado()%>')
                ,('<%=objectVO.getPersona_contacto()%>'=='null'?"":'<%=objectVO.getPersona_contacto()%>'),('<%=objectVO.getDes_mot_visita()%>'=='null'?"":'<%=objectVO.getDes_mot_visita()%>'),('<%=objectVO.getHora_entrada()%>'=='null'?"":'<%=objectVO.getHora_entrada()%>'),('<%=objectVO.getHora_salida()%>'=='null'?"":'<%=objectVO.getHora_salida()%>'),('<%=objectVO.getObservaciones()%>'=='null'?"":'<%=objectVO.getObservaciones()%>')];
        <%
                }// for
            }// if
        %>

        tabaAccesos.lineas=listaAccesosTabla;
        tabaAccesos.displayTabla();
        //document.getElementById('listaAccesos').children[0].children[1].children[0].children[0].ondblclick = function(event){
        //            pulsarModificarAcceso(event);
        if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
            try{
                var div = document.getElementById('listaAccesos');
                //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                }else{
                    div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].style.width = '100%';
                }
            }
            catch (err) {

            }
        }
    </script>
    <!-- Script Con Funciones-->
    <script type="text/javascript">
    
        var mensajeValidacion="";
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

        function pulsarNuevoAcceso() {
            var control = new Date();
            var result = null;
            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE51&operacion=cargarNuevoAcceso&tipo=0&nuevo=1&control='+control.getTime(), 650, 1000, 'no', 'no');
            } else {
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE51&operacion=cargarNuevoAcceso&tipo=0&nuevo=1&control='+control.getTime(), 650, 1000, 'no', 'no');
            }
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaAccesos(result);
                }
            }
        }

        function pulsarModificarAcceso() {
            if (tabaAccesos.selectedIndex != -1) {
                var control = new Date();
                var result = null;
                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE51&operacion=cargarModificarAcceso&tipo=0&nuevo=0&id=' + listaAccesos[tabaAccesos.selectedIndex][0] + '&control=' + control.getTime(), 650, 1000, 'no', 'no');
                } else {
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE51&operacion=cargarModificarAcceso&tipo=0&nuevo=0&id=' + listaAccesos[tabaAccesos.selectedIndex][0] + '&control=' + control.getTime(), 650, 1000, 'no', 'no');
                }
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaAccesos(result);
                    }
                }
            }
            else {
                jsp_alerta('A', '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarEliminarAcceso() {
            if (tabaAccesos.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {

                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE51&operacion=eliminarAcceso&tipo=0&id=' + listaAccesos[tabaAccesos.selectedIndex][0] + '&control=' + control.getTime();
                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                        //var formData = new FormData(document.getElementById('formContrato'));
                        ajax.send(parametros);
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
                        var listaNueva = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaNueva[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if (hijos[j].nodeName == "FILA") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[0] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_FECHA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[1] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_NUMTAR") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[2] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_DNI_CIF") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[3] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_NOMBRE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[4] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_APE1") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[5] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_APE2") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[6] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_TEL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[7] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_EMPENT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[8] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_SERVVIS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[9] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_PERSCONT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[10] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_MOTVIS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[11] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_FECHAIV") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[12] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_FECHAFV") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[13] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_HORENT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[14] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_HORSAL") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[15] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "M51CA_OBSER") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[16] = '-';
                                        }
                                    }
                                    else if (hijosFila[cont].nodeName == "DESC_MOTVIS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[17] = '-';
                                        }
                                    }
                                }
                                listaNueva[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            recargarTablaAccesos(listaNueva);
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch (Err) {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch
                }
            }
            else {
                jsp_alerta('A', '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function recargarTablaAccesos(result) {
            var fila;
            listaAccesos = new Array();
            listaAccesosTabla = new Array();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaAccesos[i - 1] = fila;
                listaAccesosTabla[i - 1] = [fila[1],fila[2],fila[3],fila[4],(fila[5]=='null'?"":fila[5])+ ' ' + (fila[6]=='null'?"":fila[6]),(fila[7]=='null'?"":fila[7]),(fila[8]=='null'?"":fila[8]),(fila[9]=='null'?"":fila[9]),(fila[10]=='null'?"":fila[10]),(fila[11]=='null'?"":fila[11]),(fila[14]=='null'?"":fila[14]),(fila[15]=='null'?"":fila[15]),(fila[16]=='null'?"":fila[16])];
            }
            tabaAccesos = new Tabla(document.getElementById('listaAccesos'), 2755);
            tabaAccesos.addColumna('150','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col1")%>");
            tabaAccesos.addColumna('150','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col2")%>");
            tabaAccesos.addColumna('150','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col3")%>");
            tabaAccesos.addColumna('150','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col4")%>");
            tabaAccesos.addColumna('250','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col5")%>");
            tabaAccesos.addColumna('100','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col6")%>");
            tabaAccesos.addColumna('250','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col7")%>");
            tabaAccesos.addColumna('250','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col8")%>");
            tabaAccesos.addColumna('250','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col9")%>");
            tabaAccesos.addColumna('450','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col10")%>");
            tabaAccesos.addColumna('50','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col11")%>");
            tabaAccesos.addColumna('50','center',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col12")%>");
            tabaAccesos.addColumna('500','left',"<%=meLanbide51I18n.getMensaje(idiomaUsuario,"controlAcceso.tablaAccesos.col13")%>");

            tabaAccesos.displayCabecera=true;
            tabaAccesos.height = 300;
            tabaAccesos.lineas = listaAccesosTabla;
            tabaAccesos.displayTabla();
            //document.getElementById('listaAccesos').children[0].children[1].children[0].children[0].ondblclick = function(event){
            //        pulsarModificarAcceso(event);
            if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
                try{
                    var div = document.getElementById('listaAccesos');
                    //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                        div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                    }else{
                        div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                        div.children[0].children[1].children[0].children[0].style.width = '100%';
                    }
                }
                catch (err) {

                }
            }
        }
        
        function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value)!='') {
                  var D = ValidarFechaConFormatoLanbide(inputFecha.value,formato);
                  if (!D[0]){
                    jsp_alerta("A","<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                    document.getElementById(inputFecha.name).focus();
                    document.getElementById(inputFecha.name).select();
                    return false;
                  } else {
                    inputFecha.value = D[1];
                    return true;
                  }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaLanbide
            
        function mostrarCalFechaE(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide51Fecha_busqE").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide51Fecha_busqE',null,null,null,'','calMeLanbide51Fecha_busqE','',null,null,null,null,null,null,null, null,evento);        
        }//mostrarCalFechaAdqui
        
        function mostrarCalFechaS(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide51Fecha_busqS").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide51Fecha_busqS',null,null,null,'','calMeLanbide51Fecha_busqS','',null,null,null,null,null,null,null, null,evento);        
        }//mostrarCalFechaAdqui
        
         function formatearHora(control,evento){  
            evento = (evento) ? evento : window.event;  
            var charCode = (evento.which) ? evento.which : evento.keyCode;  
            
            var ignore = evento.altKey || evento.ctrlKey || inArray(charCode, JST_IGNORED_KEY_CODES);
            if (!ignore) {
                var range = getInputSelectionRange(control);
                if (range != null && range[0] != range[1]) {
                    replaceSelection(this, "");
                }
            }
            
            if (!ignore) {  //charCode >= 48 && charCode <= 56 && control.value.length < 8
                var i = control.value.length;  
                var texto = "";  
                if (i == 2 || i == 5) {
                    texto = control.value+":";  
                    control.value = texto;  
                }  
                return true;  
            }  
            return false;  
        }
        
        function comprobarMaskaraHora(control){
            if(control!=null){
                if(control.value==""){
                    return true;
                }
                var texto = control.value.split(':');
                if(texto.length!=3){
                    jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                    control.focus();
                    control.select();
                    return false;
                }
                if(!(texto[0]>=0 && texto[0]<=24)){
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                        control.focus();
                        control.select();
                        return false;
                }else{
                    if(texto[0].length<2){
                        texto[0]= '0'+texto[0];
                        control.value=texto[0]+':'+texto[1]+':'+texto[2];
                    }
                }
                if(!(texto[1]>=0 && texto[1]<=59) || !(texto[2]>=0 && texto[2]<=59)){
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                        control.focus();
                        control.select();
                        return false;
                }else{
                    if(texto[1].length<2){
                        texto[1]= '0'+texto[1];
                        control.value=texto[0]+':'+texto[1]+':'+texto[2];
                    }
                    if(texto[2].length<2){
                        texto[2]= '0'+texto[2];
                        control.value=texto[0]+':'+texto[1]+':'+texto[2];
                    }
                     
                }
            }
            else{
                jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
            }
        }
        
        function pulsarBusquedaFiltrando(){
             if (validarFiltrosBusqueda()) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var estado = prepararFiltroEstado();
                parametros = "tarea=preparar&modulo=MELANBIDE51&operacion=busquedaFiltrandoListaAcceso&tipo=0&control=" + control.getTime()
                                + "&dni_nif_busq="+ document.getElementById('dni_nif_busq').value
                                + "&nombre_busq="+ document.getElementById('nombre_busq').value
                                + "&apellido1_busq="+ document.getElementById('apellido1_busq').value
                                + "&apellido2_busq="+ document.getElementById('apellido2_busq').value
                                + "&meLanbide51Fecha_busqE="+ document.getElementById('meLanbide51Fecha_busqE').value
                                + "&hora_entrada_busq="+ document.getElementById('hora_entrada_busq').value
                                + "&hora_entrada_busqF="+ document.getElementById('hora_entrada_busqF').value
                                + "&meLanbide51Fecha_busqS="+ document.getElementById('meLanbide51Fecha_busqS').value
                                + "&hora_salida_busq="+ document.getElementById('hora_salida_busq').value
                                + "&hora_salida_busqF="+ document.getElementById('hora_salida_busqF').value
                                + "&estado="+ estado
                                + "&empresa_entidad_busq="+ document.getElementById('empresa_entidad_busq').value
                                + "&codListaMotivoVisita_busq="+ document.getElementById('codListaMotivoVisita_busq').value
                ;
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    //var formData = new FormData(document.getElementById('formContrato'));
                    ajax.send(parametros);
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
                    var listaNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaNueva[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[0] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_FECHA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[1] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_NUMTAR") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[2] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_DNI_CIF") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[3] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_NOMBRE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[4] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_APE1") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[5] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_APE2") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[6] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_TEL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[7] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_EMPENT") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[8] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_SERVVIS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[9] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_PERSCONT") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[10] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_MOTVIS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[11] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_FECHAIV") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[12] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_FECHAFV") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[13] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_HORENT") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[14] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_HORSAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[15] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "M51CA_OBSER") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[16] = '-';
                                    }
                                }
                                else if (hijosFila[cont].nodeName == "DESC_MOTVIS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[17] = '-';
                                    }
                                }
                            }
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaAccesos(listaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch (Err) {
                    jsp_alerta("A", '<%=meLanbide51I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
            else {
                jsp_alerta('A',mensajeValidacion);
            }
        }
        
        function prepararFiltroEstado(){
            /*var optionDentro = document.getElementsByName("option_dentro");
            var optionFuera = document.getElementsByName("option_fuera");
            var optionTodos = document.getElementsByName("option_todos");*/
            if(document.getElementById("option_dentro").checked){
                return "D";            
            }else if(document.getElementById("option_fuera").checked){
                return "F";
            }else if (document.getElementById("option_todos").checked){
                return "T";
            }
            else
                return "";
        }
        
        function validarFiltrosBusqueda(){
            if(document.getElementById("dni_nif_busq").value =="" 
                && document.getElementById("nombre_busq").value=="" 
                && document.getElementById("apellido1_busq").value=="" 
                && document.getElementById("apellido2_busq").value=="" 
                && document.getElementById("meLanbide51Fecha_busqE").value=="" 
                && document.getElementById("hora_entrada_busq").value=="" 
                && document.getElementById("hora_entrada_busqF").value=="" 
                && document.getElementById("meLanbide51Fecha_busqS").value=="" 
                && document.getElementById("hora_salida_busq").value=="" 
                && document.getElementById("hora_salida_busqF").value=="" 
                &&!document.getElementById("option_dentro").checked 
                && !document.getElementById("option_fuera").checked 
                && !document.getElementById("option_todos").checked 
                && document.getElementById("empresa_entidad_busq").value=="" 
                && document.getElementById("codListaMotivoVisita_busq").value=="" ){
                mensajeValidacion='<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.busqueda.filtrosoblig")%>';
                return false;
            }
            if(document.getElementById("meLanbide51Fecha_busqE").value!="" && document.getElementById("meLanbide51Fecha_busqS").value!=""){
                var fechaentrada = document.getElementById("meLanbide51Fecha_busqE").value.split("/");
                var fechasalida = document.getElementById("meLanbide51Fecha_busqS").value.split("/");
                fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                if(fechaentrada>fechasalida){
                    mensajeValidacion='<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechas")%>';
                    document.getElementById("meLanbide51Fecha_busqE").select();
                    return false;
                }
                
            }
            if(document.getElementById("hora_entrada_busq").value!="" && document.getElementById("hora_entrada_busqF").value!=""){
                var horaentrada = document.getElementById("hora_entrada_busq").value;
                var horaentradaF = document.getElementById("hora_entrada_busqF").value;
                if(horaentrada>horaentradaF){
                    mensajeValidacion='<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervaloshoraentrada")%>';
                    document.getElementById("hora_entrada_busq").select();
                    return false;
                }
                
            }
            if(document.getElementById("hora_salida_busq").value!="" && document.getElementById("hora_salida_busqF").value!=""){
                var horasalida = document.getElementById("hora_salida_busq").value;
                var horasalidaF = document.getElementById("hora_salida_busqF").value;
                if(horasalida>horasalidaF){
                    mensajeValidacion='<%=meLanbide51I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervaloshorasalida")%>';
                    document.getElementById("hora_salida_busq").select();
                    return false;
                }
            }
            return true;
        }
        
    </script>
    </form>
    <div id="popupcalendar" class="text"></div>                
</body>

