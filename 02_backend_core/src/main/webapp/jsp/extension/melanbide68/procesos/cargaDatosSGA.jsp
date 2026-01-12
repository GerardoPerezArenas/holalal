<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConstantesMeLanbide68" %>

<%
    int idiomaUsuario = 0;
    int codOrganizacion = 0;
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
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
%>
    
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<!--script type="text/javascript" src="/LCE_16_00/scripts/general.js"></script-->   
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>

<script type="text/javascript">
        
    function inicializar(){
        window.focus();
        APP_CONTEXT_PATH='<%=request.getContextPath()%>';
    }
    
    function pulsarExaminar(){
        
        document.getElementById('divCargaFicheroSGA').style.display = 'block';
        
    }  
    
    function cargarFicheroSGA(){
        var hayFichero=false;
        if(document.getElementById('ficheroSGA').files){       
            if(document.getElementById('ficheroSGA').files[0]){
                hayFichero=true;
                
            }
        } else if(document.getElementById('ficheroSGA').value != ''){
            hayFichero = true;
        } 
        
        if (!hayFichero) {
            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"mensaje.SGA.noArchivo")%>');
            return false;
        } else {
            var fichero = document.getElementById('ficheroSGA').value;
                if (comprueba_extension(fichero)){
                    if (jsp_alerta("B",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"pregunta.SGA.carga")%>')){
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                        var control = new Date();
                        var parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=procesarExcelSGA&tipo=0&control='+control.getTime();
                        document.forms[0].action = url+'?'+parametros;
                        document.forms[0].enctype = 'multipart/form-data';
                        document.forms[0].encoding = 'multipart/form-data';
                        document.forms[0].method = 'POST';
                        document.forms[0].submit();
                        jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"mensaje.SGA.carga")%>');
                    } else {
                        jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"mensaje.SGA.cancelado")%>');
                    }
                    
                } else {
                    return false;
                }
        }
    }  
    
    function comprueba_extension(archivo) { 
        var extensiones_permitidas = new Array(".xls"); 
         
        //recupero la extensión de este nombre de archivo 
        var extension = (archivo.substring(archivo.lastIndexOf("."))).toLowerCase(); 

        //compruebo si la extensión está entre las permitidas 
        var permitida = false; 
        for (var i = 0; i < extensiones_permitidas.length; i++) { 
            if (extensiones_permitidas[i] == extension) { 
                permitida = true; 
                break; 
            } 
        } 
        if (permitida) {          
            return true; 
        } 

        jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"mensaje.SGA.extInvalida")%>');
        return false; 
     }
    
</script>

<body class="bandaBody" onload="inicializar();">
    <form id="formProcesoSGA">
        <div style="height:550px; width: 100%;">
            <table width="100%" style="height: 550px;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td class="txttitblanco"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%> </td>
                </tr>
                <tr>
                    <td class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                        <div class="botonera" style="padding-top: 20px;">

                            <input type="button" id="btnExaminar" name="btnExaminar" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.examinar")%>" onclick="pulsarExaminar();">

                        </div> 
                            
                        <div id="divCargaFicheroSGA" style="clear: both; display: none; padding-top: 20px;">
                            <div style="width: 70%; text-align: center; height: 70px;">
                                
                                    <fieldset style="width: 100%; float: left; padding-left: 10px; padding-right: 10px;">
                                        <legend class="legendAzul" align="left"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.cargarFichero")%></legend>
                                        
                                        <input type="file"  name="ficheroSGA" id="ficheroSGA" class="inputTexto" size="57" accept=".xls">
                                        <input type="button" id="btnCargarFicheroSGA" name="btnCargarFicheroSGA" class="botonMasLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cargar.fichero")%>" onclick="cargarFicheroSGA();">
                                    </fieldset>    
                                
                            </div>
                        </div>    
                    </td>
                    
                </tr>
            </table>
         
        
            
        </div>                          
       
          
    </form>
</body>
