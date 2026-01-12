<%-- 
    Document   : criteriosEvaluacionCentroEmpleoUbic_Modal.jsp
    Author     : INGDGC
--%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html" pageEncoding="ISO-8859-15"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    try
    {
        if (session != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");            
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    }catch(Exception ex){
        ex.printStackTrace();
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

        <jsp:useBean id="descriptorModal" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptorModal"  property="idi_cod" param="idiomaUsuarioModal" /> 
        <jsp:setProperty name="descriptorModal"  property="apl_cod" param="aplModal" />

        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()+request.getParameter("cssModal")%>" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        
        <title>Criterios Seleccion/Hautatzeko irizpideak</title>
    </head>
    <body>
        <!-- Variables comunes -->
        <input type="hidden" name="idiomaUsuarioModal" id="idiomaUsuarioModal" value="<%=request.getParameter("idiomaUsuarioModal")%>"/>
        <input type="hidden" name="mensajeSeleccionar" id="mensajeSeleccionar" value="<%=descriptorModal.getDescripcion("gbSeleccionar")%>"/>
        <input type="hidden" name="numeroExpediente" id="numeroExpediente" value="<%=request.getParameter("numeroExpediente")%>"/>
        <input type="hidden" name="idBDCentroEmpleo" id="idBDCentroEmpleo" value="<%=request.getParameter("idBDCentroEmpleo")%>"/>
        <!--Modales-->
        <div class="modal fade" id="modalCriteriosSeleccionCentroEmpleo" tabindex="-1" role="dialog" aria-labelledby="modalMensajeLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalMensajeLabel"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.modal.criterios.evaluacion.titulo")%></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <h5 id="textoBodyModal"></h5>
                        <div  class="container align-self-center">
                            <div class="form-group-lg">
                                <div class="row">
                                    <div id="tablaCriterios">
                                    </div> 
                                </div>
                            </div>
                            <hr class="mt-2 mb-3">        
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal"><%=descriptorModal.getDescripcion("gbVolver")%></button>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>