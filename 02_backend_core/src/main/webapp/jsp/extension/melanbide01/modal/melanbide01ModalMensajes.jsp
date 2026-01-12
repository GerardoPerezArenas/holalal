<%-- 
    Document   : retramitarDocsCambioProcSelectOwner
    Created on : 19-Jan-2021, 15:23:00
    Author     : INGDGC
--%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!--<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Melanbide01 - Modal Mensajes</title>-->

        <jsp:useBean id="descriptorModalM01" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptorModalM01"  property="idi_cod" param="idiomaUsuarioModal" /> 
        <jsp:setProperty name="descriptorModalM01"  property="apl_cod" param="aplModal" />
        <!--
        <link rel="StyleSheet" media="screen" type="text/css" href="< %=request.getContextPath()+request.getParameter("cssModal")%>" />
        <link rel="stylesheet" type="text/css" href="< %=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
        <link rel="stylesheet" type="text/css" href="< %=request.getContextPath()%>/css/estilo.css"/>
        <script type="text/javascript" src="< %=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="< %=request.getContextPath()%>/scripts/popup.js"></script>
        -->
<!--    </head>
    <body>-->
        <!--Modal-->
        <div class="modal fade" id="modalGestionMensajesM01" tabindex="-1" role="dialog" aria-labelledby="modalMensajeLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalMensajeLabelM01"><%=descriptorModalM01.getDescripcion("msgLabelModuloInteGestionMens")%></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <h5 id="textoBodyModal"></h5>
                        <div  class="container align-self-center" style="width: auto; margin-top:30px;">
                            <div class="alert alert-danger alert-dismissible fade show" id="textoMensajeM01" role="alert"></div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal"><%=descriptorModalM01.getDescripcion("gbAceptar")%></button>
                    </div>
                </div>
            </div>
        </div>
<!--    </body>
</html>-->