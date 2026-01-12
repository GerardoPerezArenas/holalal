<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>


<%
 int apl = 5;
 String css = "";
    int idiomaUsuario = 1;
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
     //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
   String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
   
    String numDoc = (String)request.getAttribute("numDoc");
    String codTram = request.getParameter("codigoTramite");

%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
<script>
    
   function  pulsarAltaRegistro(){
       // alert("Has pulsado enviar!");
    }
	
    function  pulsarAltaRegistro2(){
		// alert("codigoTramite<%=codTram%>");
        //alert("numDoc<%=numDoc%>");
		var numdocumento= <%=numDoc%>;
                var codigotramite= <%=codTram%>;
               // alert("numDocUMENTO "+ numdocumento);
        //Necesito recuperar el codTramite para poder variar los ids de los campos a nivel tramite
		var valor1=document.getElementById("T_"+codigotramite+"_1_FECREGSALIDA").value;
		var valor2=document.getElementById("T_"+codigotramite+"_1_FECREGSALIDA").value;
		
		//alert("VALOR 1 "+ valor1);
		//alert("VALOR 2 "+ valor2);
        //Si los campos de fecha y numero registro estan rellenos no permitimos hacer nuevo registro
        if(valor1!="" && valor2!="" && numdocumento==0){
			alert("No se permite la generación de un registro de salida, los campos de fecha y número de registro ya están rellenos");
		}else{

        //alert("pulsarAltaRegistro2:<%=numExpediente%>");
        //document.getElementById("mensajeProceso").textContent="Procesando peticion de Alta.Esto puede tardar unos minutos....";
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE50&operacion=altaRegistro&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
		try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                //var formData = new FormData(document.getElementById('formContrato'));
                ajax.send(parametros);
                if (ajax.readyState==4 && ajax.status==200){
                    var xmlDoc = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async="false";
                        xmlDoc.loadXML(text);
                    }else{
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                //alert(nodos);
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                var listaNueva = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var numsalida;
                var fecRegSalida;
                var codTramite;	
				var COD_DOCUMENTO;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        listaNueva[j] = codigoOperacion;
                        //alert(codigoOperacion);
                    }
                    
                    if(hijos[j].nodeName=="NUM_SALIDA"){                            
                        numsalida = hijos[j].childNodes[0].nodeValue;
                        listaNueva[j] = numsalida;
                        //alert("NUM_SALIDA"+numsalida);
                        //document.getElementById("T_14_1_NUMREGSALIDA").value=numsalida;
                    }
                    
                    if(hijos[j].nodeName=="COD_TRAMITE"){                            
                        codTramite = hijos[j].childNodes[0].nodeValue;
                        listaNueva[j] = codTramite;
                        //alert("COD_TRAMITE"+codTramite);
                        
                    }
                    
                    if(hijos[j].nodeName=="FEC_SALIDA"){                            
                        fecRegSalida = hijos[j].childNodes[0].nodeValue;
                        listaNueva[j] = fecRegSalida;
                         //alert("FEC_SALIDA"+fecRegSalida);
                        // document.getElementById("T_8_1_FECREGSALIDA").value=fecRegSalida;
                    }
					if(hijos[j].nodeName=="COD_DOCUMENTO"){                            
                        COD_DOCUMENTO = hijos[j].childNodes[0].nodeValue;
                        listaNueva[j] = COD_DOCUMENTO;
                         //alert("COD_DOCUMENTO"+COD_DOCUMENTO);
                        // document.getElementById("T_8_1_FECREGSALIDA").value=fecRegSalida;
                    }
                    
                    //pendiente recoger cvalorese numREg y fechaSalida
                }
            }catch(Err){
               // alert("Error de envio ajax post no es correcto");
            }

             //document.getElementById("mensajeProceso").textContent="Peticion de Alta procesada con exit!";
             /// alert("Peticion de Alta procesada con exito!");
           // alert("FIN");

          actualizarValoresCamposSuplementarios(fecRegSalida,numsalida,codTramite);
		   }		   
    }    
    function actualizarValoresCamposSuplementarios(fecRegSalida,numsalida){
            var codTramite = document.forms[0].codTramite.value;
            var ocuTramite = document.forms[0].ocurrenciaTramite.value;
            var campo1=document.getElementById("T_"+codTramite+"_1_FECREGSALIDA").value;
            var campo2=document.getElementById("T_"+codTramite+"_1_NUMREGSALIDA").value;
            
            
            try{
                if (campo1!=null){
                   eval("document.forms[0].T_" + String(codTramite) + "_" + String(ocuTramite) + "_FECREGSALIDA.value='" + fecRegSalida + "'"); 
                }
                
                if (campo2!=null){
                    eval("document.forms[0].T_" + String(codTramite) + "_" + String(ocuTramite) + "_NUMREGSALIDA.value='" + numsalida + "'");            
                }
               
                
                 
            }catch(Err){
                alert("actualizarValoresCamposSuplementarios : " + Err.description)
            }
            //alert("Peticion de Alta procesada con exito!");
            jsp_alerta('A', 'Peticion de Alta procesada con exito!');
            
        }
</script>

<body>
    <div class="tab-page" id="tabPage331" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana331"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.alta")%></h2>
        <script type="text/javascript">
            tp1_p331 = tp1.addTabPage( document.getElementById( "tabPage331" ) );
        </script>
        
        <h2>Alta Registro</h2>
        <input type="button" name="btnAltaRegistro" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario,"btn.alta")%>" class="botonGeneral" onclick="pulsarAltaRegistro2()" />
        <div id="mensajeProceso"></div>
    </div>
</body>