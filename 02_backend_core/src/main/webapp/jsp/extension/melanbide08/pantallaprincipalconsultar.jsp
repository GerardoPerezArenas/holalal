<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide08.i18n.MeLanbide08I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ComboVO" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%
    int idiomaUsuario = 0;
    int codOrganizacion = 0;
    int idUsuario=0;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                codOrganizacion  = usuario.getOrgCod();
                idUsuario=usuario.getIdUsuario();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide08I18n meLanbide08I18n = MeLanbide08I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>



<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="all">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="all">

<!--<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide08/gestionResultados.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<div class="contenidoPantalla">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>
    <div style="padding-left: 15px; text-align: center;" class="txttitblanco"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.titulo.principal")%></div>
    <div  class="container align-self-center" style="width: 40%; margin-top:30px;">
            <!--EJERCICIO-->
            <div class="form-group">
                <label for="ejercicio"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.ejercicio")%></label>
                <input type="text" maxlength="4" max="4" width="6em" class="form-control" id="ejercicio" value="" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.ejercicio")%>"  placeholder="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.ejercicio.placeholder")%>" 
                       onkeypress="return numeroEntero(event);">
            </div>
                                   <script>document.getElementById("ejercicio").value =new Date().getFullYear()</script>           

            <!--PROCEDIMIENTO-->
            <div class="form-group">
                <label for="listaProcedimiento"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.procedimiento")%></label>
                <select class="selectpicker form-control" data-actions-box="true" name="listaProcedimiento" id="listaProcedimiento" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.procedimiento")%>">
                    <option value=""><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.default")%></option>
                    <c:forEach items="${listaProcedimiento}" var="elementLista" varStatus="contador">
                        <option value="<c:out value="${elementLista.id}"/>" title="<c:out value="${elementLista.valor}"/>"><c:out value="${elementLista.id}"/></option>
                    </c:forEach>
                </select>
            </div>
          
              <!--NumeroExpediente-->
            <div class="form-group">
                <label style="width: 140px;" for="numeroExpediente"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.numeroexpediente")%></label>
                <input type="text" size="45" class="form-control" id="numeroExpediente" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.numeroexpediente")%>"  placeholder="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.numeroexpediente.placeholder")%>" >
            </div>
              <!--TIPODOCUMENTO
            <div class="form-group">
                <label style="width: 140px;" for="tipoDocumento"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.tipodocumento")%></label>
                <input type="text" size="45" class="form-control" id="tipoDocumento" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.tipodocumento")%>"  placeholder="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.tipodocumento.placeholder")%>" >
            </div>-->
            <!--FECHAHORACREACIONPETICION-->
            <div class="form-group">
                <label style="width: 200px;" for="fechahoraenviopeticion"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticiondocumento")%></label>
                 <!--<input type="text" maxlength="10" size="45" class="form-control" id="fechahoraenviopeticion" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticion")%>"  placeholder="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticion.placeholder")%>" >-->
                <form>
                      <div style=" display: flex;">
                <input type="text" witdh="90%" maxlength="10" size="45" class="form-control" id="fechahoraenviopeticiondesde" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticiondesde")%>"  placeholder="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticion.placeholderdesde")%>" >
                
                <a witdh="10%" onclick="showCalendar('forms[0]','fechahoraenviopeticiondesde',null,null,null,'','calFECSOLAPL','',null,null,null,null,null,null,null,null,event);return false;" onblur="javascript: document.getElementById("fechahoraenviopeticiondesde").focus();ocultarcalendario();="" return="" false;"="" style="TEXT-DECORATION: none; "><span class="fa fa-calendar" aria-hidden="true" id="cal" name="cal" alt=""></span> </a>
                 
                    </div>
                <BR>
                  <div style=" display: flex;">
                <input witdh="90%" type="text"  maxlength="10" size="45" class="form-control" id="fechahoraenviopeticionhasta" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticionhasta")%>"  placeholder="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.fechaenviopeticion.placeholderhasta")%>" >
                </form>
                <a witdh="10%" onclick="showCalendar('forms[0]','fechahoraenviopeticionhasta',null,null,null,'','calFECSOLAPL','',null,null,null,null,null,null,null,null,event);return false;" onblur="javascript: document.getElementById("fechahoraenviopeticiondesde").focus();ocultarcalendario();="" return="" false;"="" style="TEXT-DECORATION: none; "><span class="fa fa-calendar" aria-hidden="true" id="cal" name="cal" alt=""></span> </a>
                 <BR> 
                </div>
            
            </div>
            <!--ESTADOFIRMA-->
            <div class="form-group">
                <label style="width: 140px;" for="estadoFirma"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.estadofirma")%></label>
                <select class="selectpicker form-control" data-actions-box="true" name="estadoFirma" id="estadoFirma" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.estadofirma")%>">
                    <option value=""><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.default")%></option>
                     <!--<option value="XX" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.xx")%>"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.xx")%></option>-->
                    <option value="U" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.u")%>"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.u")%></option>
                    <option value="V" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.v")%>"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.v")%></option>
                    <option value="O" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.o")%>"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.o")%></option>
                    <option value="F" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.f")%>"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.f")%></option>
                    <option value="R" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.r")%>"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.r")%></option>
                 </select>
            </div>
              <!--DOCUMENTONOTIFICADO-->
            <div class="form-group">
                <label style="width: 140px;" for="documentoNotificado"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.documentonotificado")%></label>
                <select class="selectpicker form-control" data-actions-box="true" name="documentoNotificado" id="documentoNotificado" title="<%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.label.documentonotificado")%>">
                    <option value=""><%=meLanbide08I18n.getMensaje(idiomaUsuario, "label.select.option.default")%></option>
                    <option value="1" title="SÍ">SÍ</option>
                    <option value="0" title="NO">NO</option>
                 </select>
            </div>
            
            <!--BOTON FILTRAR-->
            <button type="button" id="filtrar" class="btn btn-primary" onclick="lanzarProcesoFiltroTablaLog();"><%=meLanbide08I18n.getMensaje(idiomaUsuario, "resultado.button.filtrar")%></button>
            </div>         
    <div>
        <!--TABLA -->
        <div id="tablaLog" style="margin-top: 30px; margin-bottom: 100px; margin-left: 30px; margin-right: 30px; height: 250px;">
            <table class="xTabla compact tablaDatos" id="tableLog">
                <thead>
                    <tr role="row">
                        <th field="ejercicio" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.ejercicio")%></th>
                        <th field="procedimiento" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.procedimiento")%></th>
                        <th field="numExpediente" datatype="String"  class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.numero")%></th>
                        <th field="fechaRegistrado" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.fecharegistro")%></th>
                        <th field="fechaFirmado" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.fechafirmado")%></th>
                        <th field="descDocumento" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.documento")%></th>
                        <th field="estado" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.estado")%></th>
                        <th field="descEstado" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.estadodes")%></th>
                        <th field="docNotificado" datatype="String" class="selectedUp" rowspan="1" colspna="1" ><%=meLanbide08I18n.getMensaje(idiomaUsuario, "tabla.titulo.notificado")%></th>
                        </tr>
                </thead>
                <tbody>
                    <c:forEach items="${lstInteropLlamadas}" var="elementLista" varStatus="contador">
                        <tr value="<c:out value="${elementLista.id}"/>">
                            <td align="left" valign="middle"><c:out value="${elementLista.ejercicio}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.procedimiento}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.numExpediente}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.fechaRegistrado}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.fechaFirmado}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.descDocumento}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.estado}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.descEstado}" /></td>
                            <td align="left" valign="middle"><c:out value="${elementLista.docNotificado}" /></td>
                           </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <!-- Campos Ocultos con texto por idioma -->
        <input type="hidden" name="texto-buscar" id="texto-buscar" value="<%=descriptor.getDescripcion("buscar")%>">
        <input type="hidden" name="texto-siguiente" id="texto-siguiente" value="<%=descriptor.getDescripcion("siguiente")%>">
        <input type="hidden" name="texto-mosFilasPag" id="texto-mosFilasPag" value="<%=descriptor.getDescripcion("mosFilasPag")%>">
        <input type="hidden" name="texto-msgNoResultBusq" id="texto-msgNoResultBusq" value="<%=descriptor.getDescripcion("msgNoResultBusq")%>">
        <input type="hidden" name="texto-mosPagDePags" id="texto-mosPagDePags" value="<%=descriptor.getDescripcion("mosPagDePags")%>">
        <input type="hidden" name="texto-noRegDisp" id="texto-noRegDisp" value="<%=descriptor.getDescripcion("noRegDisp")%>">
        <input type="hidden" name="texto-filtrDeTotal" id="texto-filtrDeTotal" value="<%=descriptor.getDescripcion("filtrDeTotal")%>">
    </div>
</div>

