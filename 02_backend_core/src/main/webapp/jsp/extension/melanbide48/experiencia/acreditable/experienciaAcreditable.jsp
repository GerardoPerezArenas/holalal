<%-- 
    Document   : experienciaAcreditable
    Created on : 28-Jun-2021, 10:43:27
    Author     : INGDGC
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionExperienciaAcreCol.js"></script>


<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
            }
        }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }
    
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    
    String urlPestanaDatos_expeAcredColectivo1 = (String)request.getAttribute("urlPestanaDatos_expeAcredColectivo1");
    String urlPestanaDatos_expeAcredColectivo2 = (String)request.getAttribute("urlPestanaDatos_expeAcredColectivo2");
    String urlPestanaDatos_expeAcredColectivo3 = (String)request.getAttribute("urlPestanaDatos_expeAcredColectivo3");
    String urlPestanaDatos_expeAcredColectivo4 = (String)request.getAttribute("urlPestanaDatos_expeAcredColectivo4");
%>

<div>
    <div class="tab-page" id="tabPage48experienciaAcreditable" style="height:100%; width: 100%;">
        <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
            <div class="form-group">
                <label for="idBDEntidad" class="etiqueta"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.entidad")%></label>
                <input type="text" class="inputTexto" disabled="true" readonly="true" size="5" id="idBDEntidad" value="<c:out value="${entidad.codEntidad}"/>">
                <input type="text" class="inputTexto" disabled="true" readonly="true" size="15" id="cifEntidad" value="<c:out value="${entidad.cif}"/>">
                <input type="text" class="inputTexto" disabled="true" readonly="true" size="90" id="nombreEntidad" value="<c:out value="${entidad.nombre}"/>">
            </div>
            <div class="form-group" id="divListaEntidades">
                <label for="listaEntidades" class="etiqueta"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.entidad.datosIntegrantes.Agrupacion")%></label>
                <select class="form-control" id="listaEntidades" style="width: auto;">
                    <option value="" title=""><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%></option>
                    <c:forEach items="${entidades}" var="elementLista" varStatus="contador">
                        <option value="<c:out value="${elementLista.codEntidad}"/>" title="<c:out value="${elementLista.cif}"/>"><c:out value="${elementLista.nombre}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div style="clear: both;">
            <div id="tab-panel-48experienciaAcreditable" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
            <script type="text/javascript"> 
                tp48experienciaAcreditable = new WebFXTabPane(document.getElementById("tab-panel-48experienciaAcreditable"));
                tp48experienciaAcreditable.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPage48experienciaAcreditable_Col1">
                <h2 class="tab" id="pestana48experienciaAcreditable_Col1"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo1.tituloPestana")%></h2>
                <script type="text/javascript">tp48experienciaAcreditable.addTabPage(document.getElementById( "tabPage48experienciaAcreditable_Col1"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_expeAcredColectivo1%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage48experienciaAcreditable_Col2">
                <h2 class="tab" id="pestana48experienciaAcreditable_Col2"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo2.tituloPestana")%></h2>
                <script type="text/javascript">tp48experienciaAcreditable.addTabPage(document.getElementById( "tabPage48experienciaAcreditable_Col2"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_expeAcredColectivo2%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage48experienciaAcreditable_Col3">
                <h2 class="tab" id="pestana48experienciaAcreditable_Col3"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo3.tituloPestana")%></h2>
                <script type="text/javascript">tp48experienciaAcreditable.addTabPage(document.getElementById( "tabPage48experienciaAcreditable_Col3"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_expeAcredColectivo3%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage48experienciaAcreditable_Col4">
                <h2 class="tab" id="pestana48experienciaAcreditable_Col4"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo4.tituloPestana")%></h2>
                <script type="text/javascript">tp48experienciaAcreditable.addTabPage(document.getElementById( "tabPage48experienciaAcreditable_Col4"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_expeAcredColectivo4%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">   
    var tp48experienciaAcreditable;
</script>