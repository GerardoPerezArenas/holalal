<%-- 
    Document   : experienciaAcreditableResumen
    Created on : 13-Aug-2021, 12:00:27
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

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionExperienciaAcreColResumen.js"></script>


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

%>

<div>
    <div class="tab-page" id="tabPage48experienciaAcreditableResumen" style="height:100%; width: 100%;">
        <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
            <div class="form-group">
                <div class="shadow-none bg-light rounded etiqueta" style="text-align: center;">
                    <hr>
                    <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.resumen.titulo")%></p>
                    <hr>
                </div>
                <div class="shadow p-3 mb-5 bg-light rounded">
                    <div class="form-group">
                        <label for="idBDEntidad" class="etiqueta"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.entidad")%></label>
                        <input type="text" class="inputTexto" disabled="true" readonly="true" size="5" id="idBDEntidad" value="<c:out value="${entidad.codEntidad}"/>">
                        <input type="text" class="inputTexto" disabled="true" readonly="true" size="15" id="cifEntidad" value="<c:out value="${entidad.cif}"/>">
                        <input type="text" class="inputTexto" disabled="true" readonly="true" size="90" id="nombreEntidad" value="<c:out value="${entidad.nombre}"/>">
                    </div>
                </div>
                <div style="clear: both;">
                    <div id="listaExperienciaAcreditableResumen" name="listaExperienciaAcreditableResumen"></div>
                    <div class="lineaFormulario">         
                        <div style="float: right" >
                            <div >
                                <span><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.total.meses.solicitud.sin.solapar")%></span><br/>
                                <div class="input-group mb-3">
<!--                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon1"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo1.tituloPestana")%></span>
                                    </div>-->
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesSolicitudC1" id="numeroTotalMesesSolicitudC1" value="" aria-describedby="basic-addon1" />
                                </div>
                                <div class="input-group mb-3">
<!--                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon2"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo2.tituloPestana")%></span>
                                    </div>-->
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesSolicitudC2" id="numeroTotalMesesSolicitudC2" value="" aria-describedby="basic-addon2" />
                                </div>
                                <div class="input-group mb-3">
<!--                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon3"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo3.tituloPestana")%></span>
                                    </div>-->
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesSolicitudC3" id="numeroTotalMesesSolicitudC3" value="" aria-describedby="basic-addon3" />
                                </div>
                                <div class="input-group mb-3">
<!--                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addon4"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo4.tituloPestana")%></span>
                                    </div>-->
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesSolicitudC4" id="numeroTotalMesesSolicitudC4" value="" aria-describedby="basic-addon4" />
                                </div>
                            </div>
                        </div>
                        <div style="float: right" >
                            <div >
                                <span><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.meses.validar")%></span><br/>
                                <div class="input-group mb-3">
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidados_CalculadosC1" id="numeroTotalMesesValidados_CalculadosC1"/>
                                </div>
                                <div class="input-group mb-3">
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidados_CalculadosC2" id="numeroTotalMesesValidados_CalculadosC2"/>
                                </div>
                                <div class="input-group mb-3">
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidados_CalculadosC3" id="numeroTotalMesesValidados_CalculadosC3"/>
                                </div>
                                <div class="input-group mb-3">
                                    <input type="text" style="text-align: right" readonly="true" disabled="true"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidados_CalculadosC4" id="numeroTotalMesesValidados_CalculadosC4"/>
                                </div>
                            </div>
                        </div>
                        <div style="float: right" >
                            <div style="margin-top: 30%;">
                                <div>
                                    <span style="margin-right: 5px;margin-left: 5px;"><i class="fa fa-hand-o-left" aria-hidden="true"></i></span>
                                    <input type="button" id="botonValidarTotalMesesResumen" name="botonValidarTotalMesesResumen" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.validar")%>" 
                                           onclick="guardarValidarTotalMesesResumen();"/>
                                    <span style="margin-right: 5px;margin-left: 5px;"><i class="fa fa-hand-o-left" aria-hidden="true"></i></span>
                                </div>
                                <br/>
                                <div>
                                    <span style="margin-right: 5px;margin-left: 5px;"><i class="fa fa-hand-o-left" aria-hidden="true"></i></span>
                                    <input type="button" id="botonCopiarMesesValidarToValidados" name="botonCopiarMesesValidarToValidados" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.copiar.meses")%>" 
                                       onclick="copiarNumeroMesesValidarToValidados();"/><span style="margin-right: 5px;margin-left: 5px;"><i class="fa fa-hand-o-left" aria-hidden="true"></i></span>
                                </div>
                            </div>
                            <br/>
                        </div>
                        <div style="float: right" >
                            <div >
                                <br/>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addonV1"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo1.tituloPestana")%></span>
                                    </div>
                                <input type="text" style="text-align: right"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidadosC1" id="numeroTotalMesesValidadosC1" value="" 
                                       onkeypress="return SoloDecimales(event);"/>
                                </div>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addonV2"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo2.tituloPestana")%></span>
                                    </div>
                                <input type="text" style="text-align: right"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidadosC2" id="numeroTotalMesesValidadosC2" value="" 
                                       onkeypress="return SoloDecimales(event);"/>
                                </div>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addonV3"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo3.tituloPestana")%></span>
                                    </div>
                                <input type="text" style="text-align: right"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidadosC3" id="numeroTotalMesesValidadosC3" value="" 
                                       onkeypress="return SoloDecimales(event);"/>
                                </div>
                                <div class="input-group mb-3">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text" id="basic-addonV4"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo4.tituloPestana")%></span>
                                    </div>
                                <input type="text" style="text-align: right"  size="6" class="form-control inputTexto"name="numeroTotalMesesValidadosC4" id="numeroTotalMesesValidadosC4" value="" 
                                       onkeypress="return SoloDecimales(event);"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="text-align: right;">
                        <span id="textoMensajeMesesValidadosNoGuardados"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.datos.validacion.noGuardados")%></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    
</script>