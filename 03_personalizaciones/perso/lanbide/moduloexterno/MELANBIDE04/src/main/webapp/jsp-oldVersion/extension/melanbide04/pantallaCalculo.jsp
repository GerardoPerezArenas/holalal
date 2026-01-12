<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide04.i18n.MeLanbide04I18N" %>
<%@page import="java.util.ArrayList" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide04I18N meLanbide04I18n = MeLanbide04I18N.getInstance();

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");
    String ejercicio        = request.getParameter("ejercicio");
   
%>
<script type="text/javascript">
    var mensajeErrorCalculo = '<%=(String)request.getAttribute("error_campos_suplementarios_origen")%>';
            
</script>

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide04/melanbide04.css"/>

<div class="tab-page" id="tabPage901" style="height:380px; width: 100%;">
    <h2 class="tab" id="pestana901"><%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.cabecera")%></h2>
    <script type="text/javascript">tp1_p901 = tp1.addTabPage( document.getElementById( "tabPage901" ) );</script>
    <form action="/PeticionModuloIntegracion.do" method="POST">
        <table width="100%" border="0">
            <tr style="padding-left:10px">
                <td align="center">
                    <table border="0" width="95%" cellspacing="2" cellpadding="2" align="center" class="contenidoPestanha">
                        <tr>
                            <td id="tdOcultos">
                            </td>
                        </tr>
                        <tr>
                            <td align="left">
                                
                                
                                <table border="0" width="50%" cellpadding="3" cellspacing="3" align="left">
                                    <tr>
                                        <td class="etiqueta" style="width: 40%">                                            
                                            <%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.subvencionTotal")%>                                            
                                        </td>
                                        <td class="etiqueta">                                            
                                            <input class="inputTexto" type="text" name="subvencionTotal" id="subvencionTotal" size="20" value="" readonly/>
                                            <input type="hidden" name="hSubvencionTotal" id="hSubvencionTotal" value="<%=(Double)request.getAttribute("subvencionTotal") %>"/>
                                        </td>                                        
                                    </tr>
                                    <tr>
                                        <td class="etiqueta" style="width: 40%">
                                            <%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.importePrimerPago")%>
                                        </td>
                                        <td class="etiqueta">
                                            <input class="inputTexto" type="text" name="importePrimerPago" id="importePrimerPago" size="20" value="" readonly/>                                            
                                            <input type="hidden" name="hImportePrimerPago" id="hImportePrimerPago" value="<%=(Double)request.getAttribute("importePrimerPago") %>"/>
                                        </td>
                                        
                                    </tr>
                                    <tr>
                                        <td class="etiqueta" style="width: 40%">
                                            <%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.importeSegundoPago")%>
                                        </td>
                                        <td class="etiqueta">
                                            <input class="inputTexto" type="text" name="importeSegundoPago" id="importeSegundoPago" size="20" value="" readonly/>                                            
                                            <input type="hidden" name="hImporteSegundoPago" id="hImporteSegundoPago" value="<%=(Double)request.getAttribute("importeSegundoPago") %>"/>
                                        </td>                                        
                                    </tr>
                                    <tr>
                                        <td class="etiqueta" style="width: 40%">
                                            <%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.cantidadSobranteNoPagada")%>
                                        </td>
                                        <td class="etiqueta">
                                            <input class="inputTexto" type="text" name="cantidadSobranteNoPagada" id="cantidadSobranteNoPagada" size="20" value="" readonly/>                                            
                                            <input type="hidden" name="hCantidadSobranteNoPagada" id="hCantidadSobranteNoPagada" value="<%=(Double)request.getAttribute("cantidadSobranteNoPagada") %>"/>
                                        </td>                                        
                                    </tr>
                                    
                                    
                                    <tr>
                                        <td class="etiqueta" style="width: 40%">
                                            <%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.cantidadTotalPagada")%>
                                        </td>
                                        <td class="etiqueta">
                                            <input class="inputTexto" type="text" name="cantidadTotalPagada" id="cantidadTotalPagada" size="20" value="" readonly/>                                            
                                            <input type="hidden" name="hCantidadTotalPagada" id="hCantidadTotalPagada" value="<%=(Double)request.getAttribute("cantidadTotalPagada") %>"/>
                                        </td>                                        
                                    </tr>
                                    
                                </table>
                                
                            </td>
                        </tr>    
                        <tr>
                            <td align="center">
                                <div id="mensajeError" name="mensajeError" class="mensajeError">
                                    
                                </div>
                            </td>    
                        </tr>
                       
                        <tr style="height:200px;">
                            <td>
                                &nbsp;
                            </td>
                        </tr>                        
                        <tr>
                            <td>
                                <center>
                                    <input type= "button" class="botonGeneral" value="<%=meLanbide04I18n.getMensaje(idiomaUsuario,"boton.grabar")%>" name="cmdGuardar" id="cmdGuardar" onclick="grabarCalculos();">                                    
                                </center
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </form>
</div>                              
                                
<script type="text/javascript">    
   
   function formatNumeroDecimal(valor){   
    var salida;
	  if (valor != "")
	  {
		// si no hay puntos ni comas
		if ((valor.indexOf(".")==-1) && (valor.indexOf(",")==-1))
		{
		  salida = processIntPart(valor);
		}
		// si hay punto pero no hay coma
		if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")==-1))
		{
		  pos = valor.lastIndexOf(".");
		  intPart = processIntPart(valor.substring(0,pos));
		  decPart = processDecPart(valor.substring(pos+1,valor.length));
		  salida = intPart+ ","+ decPart;
		}
		// si hay coma pero no hay punto
		if ((valor.indexOf(".")==-1) && (valor.indexOf(",")!=-1))
		{
		  pos = valor.lastIndexOf(",");
		  intPart = processIntPart(valor.substring(0,pos));
		  decPart = processDecPart(valor.substring(pos+1,valor.length));
		  salida = intPart+ ","+ decPart;
		}
        // si hay punto pero no hay coma
        if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")==-1))
        {
          pos = valor.lastIndexOf(".");
          intPart = processIntPart(valor.substring(0,pos));
          if (valor.substring(pos+1,valor.length) != "00")
          {
              decPart = processDecPart(valor.substring(pos+1,valor.length));
              salida =  intPart+ ","+ decPart;
          }
          else
          {
              salida =  intPart;
          }
        }
		// si hay puntos y comas
		if ((valor.indexOf(".")!=-1) && (valor.indexOf(",")!=-1))
		{
		  posComa = valor.lastIndexOf(",");
		  posPunt = valor.lastIndexOf(".");
		  pos = posComa>posPunt? posComa:posPunt;
		  intPart = processIntPart(valor.substring(0,pos));
		  decPart = processDecPart(valor.substring(pos+1,valor.length));
		  salida = intPart+ ","+ decPart;
		}
	  }

   return salida;
}


function processIntPart(valor){
    
  if (valor==""){
    valor = "0";
  }
// quitar los puntos y comas
  valor = trimPointers(valor);
// quitar los ceros a la izquierda
  valor = trimLeftZeroes(valor);
// añadir los puntos dando la vuelta
  var valor1 = "";
  var cont = 0;
  for(n=valor.length; n>=0; n--){
    valor1 += valor.charAt(n);
    if (cont == 3){
      cont = 0;
      valor1 += ".";
    }
    cont ++;
  }
// dar la vuelta
  valor1 = reverse(valor1);
// si hay un punto inicial lo quitamos
  if (valor1.charAt(0)=="."){
    valor1 = valor1.substring(1,valor1.length);
  }
  return valor1;
}

function trimPointers(valor){
  var valor1 = "";
  for(n=0; n<valor.length; n++){
    if ((valor.charAt(n)!=".") && (valor.charAt(n)!=",")){
      valor1 += valor.charAt(n);
    }
  }
  return valor1;
}

function trimLeftZeroes(valor){
  var valor1 = "";
  var trimer = true;
  for(n=0; n<valor.length; n++){
    if (!((valor.charAt(n)=="0") && (trimer))){
      valor1 += valor.charAt(n);
      trimer = false;
    }
  }
  if (valor1==""){
    valor1 = "0";
  }
  return valor1;
}


function processDecPart(valor){
  if (valor==""){
    valor = "0";
  }
  valor = trimPointers(valor);
  valor = trimRightZeroes(valor);
  // Trunco a dos decimales
  if (valor.length > 2) {
    valor = valor.substring(0,2);
  }
  return valor;
}


function trimRightZeroes(valor){
  var valor1 = "";
  var trimer = true;
  for(n=valor.length-1; n>=0; n--){
    if (!((valor.charAt(n)=="0") && (trimer))){
      valor1 += valor.charAt(n);
      trimer = false;
    }
  }
  valor1 = reverse(valor1);
  if (valor1==""){
    valor1 = "0";
  }
  return valor1;
}



 function actualizarResultadoCalculos(){
            
    var ajax = getXMLHttpRequest();
    var nodos = null;
    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        
    if(ajax!=null){
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "&tarea=procesar&operacion=recalcular" + "&modulo=" + escape('<%=nombreModulo%>') + "&codOrganizacion=" + escape('<%=codOrganizacion%>')
                        + "&numero=" + escape('<%=numExpediente%>') + "&tipo=0";

        ajax.open("POST",url,false);
        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
        ajax.send(parametros);

        try{
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
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var subvencionTotal = null;
            var importePrimerPago = null;
            var importeSegundoPago = null;
            var cantidadSobranteNoPagada = null;
            var cantidadTotalPagada = null;
            
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                
                if(hijos[j].nodeName=="STATUS"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }else                
                if(hijos[j].nodeName=="SUBVENCION_TOTAL"){                            
                    subvencionTotal = hijos[j].childNodes[0].nodeValue;
                }else
                if(hijos[j].nodeName=="IMPORTE_PRIMER_PAGO"){                            
                    importePrimerPago = hijos[j].childNodes[0].nodeValue;
                }else
                if(hijos[j].nodeName=="IMPORTE_SEGUNDO_PAGO"){                            
                    importeSegundoPago = hijos[j].childNodes[0].nodeValue;
                }else
                if(hijos[j].nodeName=="CANTIDAD_SOBRANTE_NO_PAGADA"){                            
                    cantidadSobranteNoPagada = hijos[j].childNodes[0].nodeValue;
                }else
                if(hijos[j].nodeName=="CANTIDAD_TOTAL_PAGADA"){                            
                    cantidadTotalPagada = hijos[j].childNodes[0].nodeValue;
                }
            }
            
            if(codigoOperacion==0){
                if(subvencionTotal!=null && subvencionTotal!='' && subvencionTotal!=null && subvencionTotal!='' 
                        && importePrimerPago!=null && importePrimerPago!='' && importeSegundoPago!=null && importeSegundoPago!=''){
                        
                        document.getElementById("subvencionTotal").value= formatNumeroDecimal(subvencionTotal);
                        document.getElementById("importePrimerPago").value= formatNumeroDecimal(importePrimerPago);
                        document.getElementById("importeSegundoPago").value= formatNumeroDecimal(importeSegundoPago);     
                        document.getElementById("cantidadSobranteNoPagada").value = formatNumeroDecimal(cantidadSobranteNoPagada);     
                        document.getElementById("cantidadTotalPagada").value = formatNumeroDecimal(cantidadTotalPagada);     
                        document.getElementById('mensajeError').innerHTML= "";
                        document.getElementById('cmdGuardar').disabled = false;                        
                }
            }else
            if(codigoOperacion==-1){    
                document.getElementById('mensajeError').innerHTML = '<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorRecuperarNombreCamposSuplementarios")%>';                
                document.getElementById("subvencionTotal").value= "";
                document.getElementById("importePrimerPago").value= "";
                document.getElementById("importeSegundoPago").value= "";     
                document.getElementById("cantidadSobranteNoPagada").value = "";     
                document.getElementById("cantidadTotalPagada").value = "";     
            }else
            if(codigoOperacion==-2){    
                document.getElementById('mensajeError').innerHTML = '<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorRecuperarValoresCamposSuplementarios")%>';
                document.getElementById("subvencionTotal").value= "";
                document.getElementById("importePrimerPago").value= "";
                document.getElementById("importeSegundoPago").value= "";     
                document.getElementById("cantidadSobranteNoPagada").value = "";     
                document.getElementById("cantidadTotalPagada").value = "";     
            }else
            if(codigoOperacion==-3){    
                document.getElementById('mensajeError').innerHTML = '<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorTecnico")%>';
                document.getElementById("subvencionTotal").value= "";
                document.getElementById("importePrimerPago").value= "";
                document.getElementById("importeSegundoPago").value= "";     
                document.getElementById("cantidadSobranteNoPagada").value = "";     
                document.getElementById("cantidadTotalPagada").value = "";     
            }else
            if(codigoOperacion==-4){    
                document.getElementById('mensajeError').innerHTML = '<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorGrabarResultadoCalculosCamposSuplementarios")%>';
                document.getElementById("subvencionTotal").value= "";
                document.getElementById("importePrimerPago").value= "";
                document.getElementById("importeSegundoPago").value= "";     
                document.getElementById("cantidadSobranteNoPagada").value = "";    
                document.getElementById("cantidadTotalPagada").value = "";     
            }
                
        }catch(Err){
            alert("Error.descripcion: " + Err.description);
        }
    }
        
 }
    

    // Se comprueba el mensaje de error
    
    if(mensajeErrorCalculo!=null && mensajeErrorCalculo!=undefined && mensajeErrorCalculo=='SI'){
            
        var objeto = document.getElementById('mensajeError');
        if(objeto!=null){        
            document.getElementById('mensajeError').innerHTML = '<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.faltanValoresCamposSuplementarios")%>';
            document.getElementById('cmdGuardar').disabled = true;
        }
    }else{
        
        var hSubvencionTotal    = document.forms[0].hSubvencionTotal;
        var hImportePrimerPago  = document.forms[0].hImportePrimerPago;
        var hImporteSegundoPago = document.forms[0].hImporteSegundoPago;
        var hCantidadSobranteNoPagada = document.forms[0].hCantidadSobranteNoPagada;
        var hCantidadTotalPagada = document.forms[0].hCantidadTotalPagada;

        if(hSubvencionTotal!=null && hImportePrimerPago!=null && hImporteSegundoPago!=null){

            var subvencionTotal    = hSubvencionTotal.value;
            var importePrimerPago  = hImportePrimerPago.value;
            var importeSegundoPago = hImporteSegundoPago.value;
        
            if(subvencionTotal!="" && importePrimerPago!="" && importeSegundoPago!=""){        
                document.getElementById("subvencionTotal").value= formatNumeroDecimal(subvencionTotal);
                document.getElementById("importePrimerPago").value= formatNumeroDecimal(importePrimerPago);
                document.getElementById("importeSegundoPago").value= formatNumeroDecimal(importeSegundoPago);
                
                var cantidadSobranteNoPagada = hCantidadSobranteNoPagada.value;
                var cantidadTotalPagada = hCantidadTotalPagada.value;
                if(cantidadSobranteNoPagada!=null && cantidadSobranteNoPagada!=""){
                    document.getElementById("cantidadSobranteNoPagada").value= formatNumeroDecimal(cantidadSobranteNoPagada);                    
                }
                
                if(cantidadTotalPagada!=null && cantidadTotalPagada!=""){
                    document.getElementById("cantidadTotalPagada").value= formatNumeroDecimal(cantidadTotalPagada);                                        
                }
                
            }//if
        }//if        
    }



    function grabarCalculos(){    
                
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>';
            
        if(jsp_alerta('C','<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.msg.confirmacionGrabacionDatos")%>')){

            if(ajax!=null){
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var subvencionTotal    = document.getElementById("subvencionTotal").value;
                var importePrimerPago  = document.getElementById("importePrimerPago").value;
                var importeSegundoPago = document.getElementById("importeSegundoPago").value;     
                var cantidadSobranteNoPagada = document.getElementById("cantidadSobranteNoPagada").value;     
                var cantidadTotalPagada  = document.getElementById("cantidadTotalPagada").value;     


                var parametros = "&tarea=procesar&operacion=grabarCalculosCampos" + "&modulo=" + escape('<%=nombreModulo%>') + "&codOrganizacion=" + escape('<%=codOrganizacion%>')
                                + "&numero=" + escape('<%=numExpediente%>') + "&tipo=0&subvencionTotal=" + subvencionTotal + "&importePrimerPago=" + importePrimerPago + "&importeSegundoPago=" + importeSegundoPago +  "&cantidadSobranteNoPagada=" + cantidadSobranteNoPagada + "&cantidadTotalPagada=" + cantidadTotalPagada;

                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                ajax.send(parametros);



                try{
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
                        }
                    }

                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;


                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="STATUS"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }
                    }

                    if(codigoOperacion==0){                    
                        document.getElementById('mensajeError').innerHTML= "";
                        document.getElementById('cmdGuardar').disabled = false;
                        jsp_alerta("A",'<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.mensaje.grabadoCalculosCorrectamente")%>');
                        recargarFichaExpediente();                    
                    }else
                    if(codigoOperacion==-1){    
                        jsp_alerta("A",'<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorRecuperarNombreCamposSuplementarios")%>');                    

                    }else
                    if(codigoOperacion==-2){    
                        jsp_alerta("A",'<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorRecuperarValoresCamposSuplementarios")%>');                                        

                    }else
                    if(codigoOperacion==-3){    
                        jsp_alerta("A",'<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorTecnico")%>');                                                            
                    }else
                    if(codigoOperacion==-4){    
                        jsp_alerta("A",'<%=meLanbide04I18n.getMensaje(idiomaUsuario,"label.error.errorGrabarResultadoCalculosCamposSuplementarios")%>');                    
                    }

                }catch(Err){
                    alert("Error.descripcion: " + Err.description);
                }
            }        
       }//if
    }
    
    
    function recargarFichaExpediente(){
        var parametros = "?opcion=cargar&codMunicipio=" + '<%=codOrganizacion%>' + "&ejercicio=" + '<%=ejercicio%>' + "&numero=" + '<%=numExpediente%>' + "&codProcedimiento=" + '<%=codProcedimiento%>';        
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>" + parametros;            
        document.forms[0].submit();

    }


    
</script>
