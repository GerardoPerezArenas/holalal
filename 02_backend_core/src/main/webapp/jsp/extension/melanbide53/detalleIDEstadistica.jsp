<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleEstadisticaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <%
        DetalleEstadisticaVO datEstadistica = new DetalleEstadisticaVO();
        
        int idRegistro;
        String idReg = "";
        String fechaRegistro = "";
        String idProcedimiento = "";        
        String numeroExpediente = "";
        String clave = "";
        String resultado = "";  
        String idError = "";
        String evento = "";
        String observaciones = "";  
        String xmlReglasSuscripcion = "";          
        MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();        
        
        int idiomaUsuario = 1;
        try
        {
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

            }

            if(request.getAttribute("datEstadistica") != null)
            {
                datEstadistica = (DetalleEstadisticaVO)request.getAttribute("datEstadistica");
                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
                idRegistro = datEstadistica.getId();
                idReg=String.valueOf(idRegistro);
                fechaRegistro = datEstadistica.getFechaRegistro()!=null ? datEstadistica.getFechaRegistro().substring(0,24):"";
                idProcedimiento = datEstadistica.getIdProcedimiento()!=null ? datEstadistica.getIdProcedimiento():"";       
                numeroExpediente = datEstadistica.getNumeroExpediente()!=null ? datEstadistica.getNumeroExpediente():""; 
                clave = datEstadistica.getClave()!=null ? datEstadistica.getClave():""; 
                resultado = datEstadistica.getResultado()!=null ? datEstadistica.getResultado():"";   
                idError = datEstadistica.getIdError()!=null ? datEstadistica.getIdError():""; 
                evento = datEstadistica.getEvento()!=null ? datEstadistica.getEvento():""; 
                observaciones = datEstadistica.getObservaciones()!=null ? datEstadistica.getObservaciones():"";   
                xmlReglasSuscripcion = datEstadistica.getXmlReglasSuscripcion()!=null ? datEstadistica.getXmlReglasSuscripcion():"";  				                
            }
        }
        catch(Exception ex)
        {
            %>
            alert("Tenemos una excepción");
            <%
        }
    %>    
</head>
<body>
    <div>		
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
	<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
	<script type="text/javascript">
		var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
	</script>   
        <script type="text/javascript">
            function cerrarVentana() {
                if (navigator.appName == "Microsoft Internet Explorer") {
                    window.parent.window.opener = null;
                    window.parent.window.close();
                } else if (navigator.appName == "Netscape") {
                    top.window.opener = top;
                    top.window.open('', '_parent', '');
                    top.window.close();
                } else {
                    window.close();
                }
            }
        </script>
               
        <div class="contenidoPantalla">
            <form>
		<div style="width: 100%; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
			<span>
                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.mantenimientoEstadisticas")%>
			</span>
                    </div>
			
                    <fieldset style="border: 1px darkgrey solid; margin-top: 10px;margin-left: 200px;width: 375px;">
			<legend><label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.area.estadisticasId")%></label></legend>
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53IdRegistro")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53IdRegistro" name="meLanbide53IdRegistro" maxlength="15"  size="15" value="<%=idReg%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br>
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53FechaRegistro")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
                                    <input readonly type="text" class="inputTxtFecha" id="meLanbide53FechaRegistro" name="meLanbide53FechaRegistro" maxlength="24"  size="24" value="<%=fechaRegistro%>"/>
				</div>
                            </div>
			</div>
			<div style="clear: both;"></div>
			<br>
                    </fieldset>   

                    <fieldset style="border: 1px darkgrey solid; margin-top: 20px;width: 750px;align-content: center;">
                        <legend><label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.area.estadisticasDetalle")%></label></legend>
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53IProcedimiento")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53IProcedimiento" name="meLanbide53IProcedimiento" maxlength="30"  size="30" value="<%=idProcedimiento%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br>   
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53NumeroExpediente")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53numeroExpediente" name="meLanbide53numeroExpediente" maxlength="30"  size="30" value="<%=numeroExpediente%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br>   
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53Clave")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53Clave" name="meLanbide53Clave" maxlength="60"  size="60" value="<%=clave%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br>  
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53Evento")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53Evento" name="meLanbide53Evento" maxlength="60"  size="60" value="<%=evento%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br>                        
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53Errores")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53idError" name="meLanbide53idError" maxlength="60"  size="60" value="<%=idError%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br>   
			<div class="lineaFormulario" style="margin: 2px">
                            <div style="width: 120px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53Resultado")%>
                            </div>
                            <div style="width: 250px; float: left;">
                               	<div style="float: left;">
				     <input readonly type="text" class="inputTexto" id="meLanbide53resultado" name="meLanbide53resultado" maxlength="2"  size="2" value="<%=resultado%>"/>
				</div>
                            </div>
			</div>                                                
                        <br><br><br>
			<div class="lineaFormulario">
                            <div style="width: 190px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53Reglas")%>
                            </div>
			</div>
                        <div style="clear: both;"></div>  
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea readonly class="inputTextoM53" rows="10" cols="130" id="meLanbide53reglas" name="meLanbide53reglas" maxlength="30000"><%=xmlReglasSuscripcion%></textarea>
                            </div>
                        </div>        
			<div style="clear: both;"></div>      
                        <br>                  
			<div class="lineaFormulario">
                            <div style="width: 190px; float: left;" class="etiqueta">
				<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.meLanbide53Observaciones")%>
                            </div>
			</div>
			<div style="clear: both;"></div>                          
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea readonly class="inputTextoM53" rows="4" cols="130" id="meLanbide53observaciones" name="meLanbide53observaciones" maxlength="500"><%=observaciones%></textarea>
                            </div>
                        </div>  
			<div style="clear: both;"></div>                                 
                    </fieldset>                                               
                                       
		</div>   
                <br>
		<div class="lineaFormulario">
                    <div class="botonera" style="text-align: center;">
			<input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();"/> 
                    </div>
		</div>                            
            </form>
        </div>
    </div>                   
</body>    

