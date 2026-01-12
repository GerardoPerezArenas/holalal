/**
 * @author jaime.hermoso
 * @version 15/12/2022 1.0
 * Historial de cambios:
 * <ol>
 *  <li>jaime.hermoso * 15/12/2022 * Edición inicial</li>
 * </ol> 
 */

var prepararJsonDatosGAvisos ={
        codAsunto:null
        ,codProcedimiento:null
        ,codUOR:null
        ,codEvento:null
        ,desEmail:null
        ,desAsunto:null
        ,desContenido:null
        ,checkConfActiva:null
};

var parametrosLLamadaM06={
    tarea:'preparar'
    ,modulo:'MELANBIDE06'
    ,operacion:null
    ,numero:null
    ,tipo:0
    ,control:new Date().getTime()
    ,tipoOperacion:null
    ,filaIDseleccionada:null
};

var tablaGestionAvisos;
var tablaEtiquetas;
var etiquetasGA;
var documentoSeleccionado;
var datosGA = [];
var filaIDseleccionada;

$(document).ready(function() {
    if($("#listaEtiquetasGestionAvisos") == undefined || $("#listaEtiquetasGestionAvisos").length == 0){
        cargarTablaGestionAvisos();
        cargarCombos();
        limpiarFiltros();
    }else{
        cargarTablaEtiquetas();
    }
});


function cargarCombos(){
    comboOrganizacion();
    comboEntidad();
    comboProcedimiento();
    comboUors();
    comboAsuntos();
    comboEventos();
}

function comboProcedimiento(codigoPro){
    
    var listaProcedimientosGestionAvisosJSON = JSON.parse($("#listaProcedimientosGestionAvisos").val());
    var comboProcedimientos = new Combo("Procedimiento");
    comboProcedimientos.change = changeComboUors;
    var lcodProcedimientoGA = new Array();
    var ldescProcedimientoGA = new Array();
        
    if (listaProcedimientosGestionAvisosJSON != null && listaProcedimientosGestionAvisosJSON.length > 0) 
    {      
        listaProcedimientosGestionAvisosJSON.forEach(function(procedimiento, index) {
            lcodProcedimientoGA.push(procedimiento.codigo);
            ldescProcedimientoGA.push(procedimiento.descripcion);
          });
       comboProcedimientos.addItems(lcodProcedimientoGA, ldescProcedimientoGA);
    }  
    
    if(codigoPro != undefined){
      document.getElementById("codUOR").style.pointerEvents = 'auto';
      document.getElementById("anchorUOR").style.pointerEvents = 'auto';
      if(codigoPro == 'EMPTY'){
         document.getElementById("anchorProcedimiento").style.pointerEvents = 'auto';
         document.getElementById("codProcedimiento").style.pointerEvents = 'auto';
         document.getElementById("codProcedimiento").value = '';
         document.getElementById("descProcedimiento").value = '';
         document.getElementById("codUOR").value = '';
         document.getElementById("descUOR").value = '';
         document.getElementById("anchorUOR").style.pointerEvents = 'none';
         document.getElementById("codUOR").style.pointerEvents = 'none';
      }else{
          comboProcedimientos.buscaLinea(codigoPro);
          document.getElementById("anchorProcedimiento").style.pointerEvents = 'none';
          document.getElementById("codProcedimiento").style.pointerEvents = 'none';
          changeComboUors();
      }

    }   
}

function comboUors(unidadesOrganicas){
    
    var listaUorsGestionAvisosJSON = JSON.parse($("#listaUorsGestionAvisos").val());
    var comboUor = new Combo("UOR");
    var lcodUorGA = new Array();
    var ldescUorGA = new Array();
        
    if (listaUorsGestionAvisosJSON != null && listaUorsGestionAvisosJSON.length > 0) 
    {      
        listaUorsGestionAvisosJSON.forEach(function(uor, index) {
            lcodUorGA.push(uor.codigo);
            ldescUorGA.push(uor.descripcion);
          });
       comboUor.addItems(lcodUorGA, ldescUorGA);
    }
    
    if(unidadesOrganicas == 'EMPTY'){
     document.getElementById("anchorUOR").style.pointerEvents = 'auto';
     document.getElementById("codUOR").style.pointerEvents = 'auto';
     document.getElementById("codUOR").value = '';
     document.getElementById("descUOR").value = '';
    }else{
        if(unidadesOrganicas != undefined){
        //iterator de unidadesOrganicas
        var codigoUor;
        lcodUorGA = new Array();
        ldescUorGA = new Array();
        unidadesOrganicas.forEach(function(uor, index) {
        lcodUorGA.push(uor.codigo);
        ldescUorGA.push(uor.descripcion);
        codigoUor = uor.codigo;
        });
        comboUor.clearItems();
        comboUor.addItems(lcodUorGA, ldescUorGA);

        if(unidadesOrganicas.length == 1){
            comboUor.buscaLinea(codigoUor);
            document.getElementById("anchorUOR").style.pointerEvents = 'none';
            document.getElementById("codUOR").style.pointerEvents = 'none';
        }else{
            document.getElementById("anchorUOR").style.pointerEvents = 'auto';
            document.getElementById("codUOR").style.pointerEvents = 'auto';
        }
      }
    } 
}

function comboAsuntos(){
    
    var listaAsuntosGestionAvisosJSON = JSON.parse($("#listaAsuntosGestionAvisos").val());
    var comboAsunto = new Combo("Asunto");
    comboAsunto.change = changeComboProcedimiento;
    var lcodAsuntoGA = new Array();
    var ldescAsuntoGA = new Array();
        
    if (listaAsuntosGestionAvisosJSON != null && listaAsuntosGestionAvisosJSON.length > 0) 
    {      
        listaAsuntosGestionAvisosJSON.forEach(function(asunto, index) {
            lcodAsuntoGA.push(asunto.codigo);
            ldescAsuntoGA.push(asunto.descripcion);
          });
       comboAsunto.addItems(lcodAsuntoGA, ldescAsuntoGA);
    }
}

function comboEventos(){
    
    var listaEventosGestionAvisosJSON = JSON.parse($("#listaEventosGestionAvisos").val());
    var comboEvento = new Combo("Evento");
    var lcodEventoGA = new Array();
    var ldescEventoGA = new Array();
        
    if (listaEventosGestionAvisosJSON != null && listaEventosGestionAvisosJSON.length > 0) 
    {      
        listaEventosGestionAvisosJSON.forEach(function(evento, index) {
            lcodEventoGA.push(evento.codigo);
            ldescEventoGA.push(evento.descripcion);
          });
       comboEvento.addItems(lcodEventoGA, ldescEventoGA);
    }
}

function comboOrganizacion(){
    
    var listaOrganizacionesGestionAvisosJSON = JSON.parse($("#listaOrganizacionesGestionAvisos").val());
    var comboOrganizacion = new Combo("Organizaciones");
    var lcodOrganizacionGA = new Array();
    var ldescOrganizacionGA = new Array();
        
    if (listaOrganizacionesGestionAvisosJSON != null && listaOrganizacionesGestionAvisosJSON.length > 0) 
    {      
        listaOrganizacionesGestionAvisosJSON.forEach(function(organizacion, index) {
            lcodOrganizacionGA.push(organizacion.codigo);
            ldescOrganizacionGA.push(organizacion.descripcion);
          });
       comboOrganizacion.addItems(lcodOrganizacionGA, ldescOrganizacionGA);
    }
    
    comboOrganizacion.buscaLinea($("#organizacion").val());
    
}

function comboEntidad(){
    
    var listaEntidadesGestionAvisosJSON = JSON.parse($("#listaEntidadesGestionAvisos").val());
    var comboEntidad = new Combo("Entidades");
    var lcodEntidadGA = new Array();
    var ldescEntidadGA = new Array();
        
    if (listaEntidadesGestionAvisosJSON != null && listaEntidadesGestionAvisosJSON.length > 0) 
    {      
        listaEntidadesGestionAvisosJSON.forEach(function(entidad, index) {
            lcodEntidadGA.push(entidad.codigo);
            ldescEntidadGA.push(entidad.descripcion);
          });
       comboEntidad.addItems(lcodEntidadGA, ldescEntidadGA);
    }
    
    comboEntidad.buscaLinea($("#entidad").val());
    
}

function changeComboProcedimiento(){
    var respuesta = rescatarProcedimientoByCodAsunto($('[name="codAsunto"]').val());
    if(respuesta.codigo == null){
      respuesta = "EMPTY";  
    }else{
      respuesta = respuesta.codigo;    
    }
    comboProcedimiento(respuesta);
}

function changeComboUors(){
    var unidadesOrganicas = rescatarUorByCodProcedimiento($('[name="codProcedimiento"]').val());
    var respuesta;
    if(unidadesOrganicas == null || unidadesOrganicas == undefined || unidadesOrganicas == ""){
      respuesta = "EMPTY";  
    }else{
      respuesta = unidadesOrganicas;    
    }
    comboUors(respuesta);
}

function rescatarProcedimientoByCodAsunto(codAsunto){
    var respuesta;
    var dataParameter = $.extend({}, parametrosLLamadaM06);
    dataParameter.codAsunto = codAsunto;
    dataParameter.codOrg = $("#organizacion").val();
    dataParameter.opcion = 'rescatarProcedimientoByCodAsunto';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/gestionAvisos/GestionAvisosAction.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if(response != null){
                respuesta = JSON.parse(response);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = $('#gestionAvisos_descriptor_error_get_codpro').val() + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    
    return respuesta;
}

function changeComboUors(){
    var unidadesOrganicas = rescatarUorByCodProcedimiento($('[name="codProcedimiento"]').val());
    var respuesta;
    if(unidadesOrganicas == null || unidadesOrganicas == undefined || unidadesOrganicas == ""){
      respuesta = "EMPTY";  
    }else{
      respuesta = unidadesOrganicas;    
    }
    comboUors(respuesta);
}

function rescatarUorByCodProcedimiento(codPro){
    var respuesta;
    var dataParameter = $.extend({}, parametrosLLamadaM06);
    dataParameter.codPro = codPro;
    dataParameter.codOrg = $("#organizacion").val();
    dataParameter.opcion = 'rescatarUorByCodProcedimiento';
    var urlBaseLlamada = APP_CONTEXT_PATH + "/gestionAvisos/GestionAvisosAction.do"; //Ubicado en struts.xml de Flexia
    $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            if(response != null){
                respuesta = JSON.parse(response);
            }
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = $('#gestionAvisos_descriptor_error_get_uor').val() + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    });
    
    return respuesta;
}

function cambioValorCheck(check){
    if(check.checked){
            check.value="S";
    }
    else{
            check.value="N";
        }
}

function prepararJsonDatosGA(){
    var  respuesta = $.extend({}, prepararJsonDatosGAvisos);
    respuesta.codAsunto=$("#codAsunto").val()!= undefined && $("#codAsunto").val()!="" ? $("#codAsunto").val() : null;
    respuesta.codProcedimiento=$("#codProcedimiento").val()!= undefined && $("#codProcedimiento").val()!="" ? $("#codProcedimiento").val() : null;
    respuesta.codUOR=$("#codUOR").val()!= undefined && $("#codUOR").val()!="" ? $("#codUOR").val() : null; 
    respuesta.codEvento=$("#codEvento").val()!= undefined && $("#codEvento").val()!="" ? $("#codEvento").val() : null; 
    respuesta.desEmail=$("#desEmail").val()!= undefined && $("#desEmail").val()!="" ? $("#desEmail").val() : null;
    respuesta.desAsunto=$("#desAsunto").val()!= undefined && $("#desAsunto").val()!="" ? $("#desAsunto").val() : null;
    respuesta.desContenido=$("#desContenido").val()!= undefined && $("#desContenido").val()!="" ? $("#desContenido").val() : null;
    respuesta.checkConfActiva=$("#checkConfActiva").val() != undefined && $("#checkConfActiva").val() !="" ? $("#checkConfActiva").val() : null;
    return respuesta;
}

function pulsarAnadirGA(){
    var respuesta;
    var emailError = false;
    var dataParameter = $.extend({}, parametrosLLamadaM06);
    dataParameter.codOrg = $("#organizacion").val();
    dataParameter.opcion = 'anadirGA';
    dataParameter.jsonGA = JSON.stringify(prepararJsonDatosGA());
    
    if(($("#codAsunto").val() == undefined || $("#codAsunto").val() == '')
            || ($("#codProcedimiento").val() == undefined || $("#codProcedimiento").val() == '')
            || ($("#codUOR").val() == undefined || $("#codUOR").val() == '')
            || ($("#codEvento").val() == undefined || $("#codEvento").val() == '')
            || ($("#desEmail").val() == undefined || $("#desEmail").val() == '')
            || ($("#desAsunto").val() == undefined || $("#desAsunto").val() == '')
            || ($("#desContenido").val() == undefined || $("#desContenido").val() == '')
            || ($("#checkConfActiva").val() == undefined || $("#checkConfActiva").val() == '')){
      var mensajeError = $('#gestionAvisos_descriptor_campos_oblig').val();
      jsp_alerta("A", mensajeError);  
    }else{      
        //Comprobación del email       
        var emailArray = $("#desEmail").val().split(';');
        var emailRegex = new RegExp("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}");
        emailArray.forEach(function(email) {          
            if (emailRegex.test(email.trim())) {
              emailError = false;
            } else {
              emailError = true;
            }
        });
       
       if(!emailError){
       var urlBaseLlamada = APP_CONTEXT_PATH + "/gestionAvisos/GestionAvisosAction.do";
            $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (response) {
                cargarTablaGestionAvisos();
                limpiarFiltros();
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = $('#gestionAvisos_descriptor_error_add_config').val() + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
            });
    
        }else{
            jsp_alerta("A",$('#gestionAvisos_descriptor_email_incorrecto').val());
        }
    }
}

function limpiarFiltros(){
    document.getElementById("codAsunto").value = '';
    document.getElementById("descAsunto").value = '';
    document.getElementById("codProcedimiento").value = '';
    document.getElementById("descProcedimiento").value = '';
    document.getElementById("codUOR").value = '';
    document.getElementById("descUOR").value = '';
    document.getElementById("codEvento").value = '';
    document.getElementById("descEvento").value = '';
    document.getElementById("desEmail").value = '';
    document.getElementById("desAsunto").value = '';
    document.getElementById("desContenido").value = '';
    document.getElementById("checkConfActiva").checked = false;
    document.getElementById("checkConfActiva").value = "N";
    document.getElementById("btnModificar").disabled = true;
    document.getElementById("btnEliminar").disabled = true;
    document.getElementById("btnAnadir").disabled = false;
    filaIDseleccionada = null;
}

function cerrarVentana(){
    if(navigator.appName=="Microsoft Internet Explorer") {
          window.parent.window.opener=null;
          window.parent.window.close();
    } else if(navigator.appName=="Netscape") {
          top.window.opener = top;
          top.window.open('','_parent','');
          top.window.close();
     }else{
         window.close();
     }
  }

function cargarTablaGestionAvisos(){
      
    var respuesta;
    var dataParameter = $.extend({}, parametrosLLamadaM06);
    dataParameter.codOrg = $("#organizacion").val();
    dataParameter.opcion = 'cargarDatosGA';

    var urlBaseLlamada = APP_CONTEXT_PATH + "/gestionAvisos/GestionAvisosAction.do"; //Ubicado en struts.xml de Flexia
     $.ajax({
     type: 'POST',
     url: urlBaseLlamada,
     data: dataParameter,
     success: function (response) {   
        tablaGestionAvisos = new Tabla(true,$('#gestionAvisos_descriptor_buscar').val(),$('#gestionAvisos_descriptor_anterior').val(),$('#gestionAvisos_descriptor_siguiente').val(),$('#gestionAvisos_descriptor_mosFilasPag').val(),$('#gestionAvisos_descriptor_msgNoResultBusq').val(),$('#gestionAvisos_descriptor_mosPagDePags').val(), $('#gestionAvisos_descriptor_noRegDisp').val(),$('#gestionAvisos_descriptor_filtrDeTotal').val(),$('#gestionAvisos_descriptor_primero').val(),$('#gestionAvisos_descriptor_ultimo').val(),document.getElementById('tablaGestionAvisos'));
        tablaGestionAvisos.addColumna('50','center',$('#gestionAvisos_descriptor_procedimiento').val());
        tablaGestionAvisos.addColumna('150','center',$('#gestionAvisos_descriptor_evento').val());
        tablaGestionAvisos.addColumna('90','center',$('#gestionAvisos_descriptor_mailUOR').val());
        tablaGestionAvisos.addColumna('100','center',$('#gestionAvisos_descriptor_asunto').val());
        tablaGestionAvisos.addColumna('300','center',$('#gestionAvisos_descriptor_contenido').val());
        tablaGestionAvisos.displayCabecera=true;
        
        datosGA = JSON.parse(response);
        var datos = new Array();
        var contador = 0;
        
        datosGA.forEach(function(datoGA) {       
            datos[contador] = [datoGA.codProcedimiento,datoGA.evento,datoGA.email,decodeURIComponent(escape(datoGA.asunto)),decodeURIComponent(escape(datoGA.contenido))];         
            contador++;
            //decodeURIComponent(escape(datoGA.contenido))
        });
        
        tablaGestionAvisos.lineas=datos; 
        tablaGestionAvisos.displayTabla();
     },
     error: function (jqXHR, textStatus, errorThrown) {
         var mensajeError = $('#gestionAvisos_descriptor_error_mostrar_tabla').val() + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
         jsp_alerta("A", mensajeError);
     },
     async: false
 });        
}


function pulsarModificarGA(){
    var respuesta;
    var emailError = false;
    var dataParameter = $.extend({}, parametrosLLamadaM06);
    dataParameter.codOrg = $("#organizacion").val();
    dataParameter.opcion = 'modificarGA';
    dataParameter.filaIDseleccionada = filaIDseleccionada;
    dataParameter.jsonGA = JSON.stringify(prepararJsonDatosGA());
    
    if(($("#codAsunto").val() == undefined || $("#codAsunto").val() == '')
            || ($("#codProcedimiento").val() == undefined || $("#codProcedimiento").val() == '')
            || ($("#codUOR").val() == undefined || $("#codUOR").val() == '')
            || ($("#codEvento").val() == undefined || $("#codEvento").val() == '')
            || ($("#desEmail").val() == undefined || $("#desEmail").val() == '')
            || ($("#desAsunto").val() == undefined || $("#desAsunto").val() == '')
            || ($("#desContenido").val() == undefined || $("#desContenido").val() == '')
            || ($("#checkConfActiva").val() == undefined || $("#checkConfActiva").val() == '')){
      var mensajeError = $('#gestionAvisos_descriptor_campos_oblig').val();
      jsp_alerta("A", mensajeError);  
    }else{
        //Comprobación del email       
        var emailArray = $("#desEmail").val().split(';');
        var emailRegex = new RegExp("[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}");
        emailArray.forEach(function(email) {          
            if (emailRegex.test(email.trim())) {
              emailError = false;
            } else {
              emailError = true;
            }
        });
        
        if(!emailError){
            var urlBaseLlamada = APP_CONTEXT_PATH + "/gestionAvisos/GestionAvisosAction.do";
            $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (response) {
                cargarTablaGestionAvisos();
                limpiarFiltros();
            },
            //dataType: dataType,
            error: function (jqXHR, textStatus, errorThrown) {
                var mensajeError = $('#gestionAvisos_descriptor_error_modif_config').val() + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                jsp_alerta("A", mensajeError);
            },
            async: false
            });
        }else{
           jsp_alerta("A",$('#gestionAvisos_descriptor_email_incorrecto').val());
        }
    }
}


function pulsarEliminarGA(){
   var resultado = jsp_alerta("",$('#gestionAvisos_descriptor_eliminarGA').val());
   if (resultado == 1){
       var urlBaseLlamada = APP_CONTEXT_PATH + "/gestionAvisos/GestionAvisosAction.do";
       var dataParameter = $.extend({}, parametrosLLamadaM06);
       dataParameter.opcion = 'eliminarGA';
       dataParameter.codOrg = $("#organizacion").val();
       dataParameter.filaIDseleccionada = filaIDseleccionada;
        $.ajax({
        type: 'POST',
        url: urlBaseLlamada,
        data: dataParameter,
        success: function (response) {
            cargarTablaGestionAvisos();
            limpiarFiltros();
        },
        //dataType: dataType,
        error: function (jqXHR, textStatus, errorThrown) {
            var mensajeError = $('#gestionAvisos_descriptor_error_eliminar').val() + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
            jsp_alerta("A", mensajeError);
        },
        async: false
    }); 
   }
}

function cargarTablaEtiquetas(){
    document.getElementById("tituloModalEtiquetas").innerHTML = $("#titModalEtiquetas").val();
    tablaEtiquetas = new Tabla(true,$('#gestionAvisos_descriptor_buscar').val(),$('#gestionAvisos_descriptor_anterior').val(),$('#gestionAvisos_descriptor_siguiente').val(),$('#gestionAvisos_descriptor_mosFilasPag').val(),$('#gestionAvisos_descriptor_msgNoResultBusq').val(),$('#gestionAvisos_descriptor_mosPagDePags').val(), $('#gestionAvisos_descriptor_noRegDisp').val(),$('#gestionAvisos_descriptor_filtrDeTotal').val(),$('#gestionAvisos_descriptor_primero').val(),$('#gestionAvisos_descriptor_ultimo').val(),document.getElementById('tablaEtiquetas'));
    tablaEtiquetas.addColumna('50','center',$('#codigoEtiq').val());
    tablaEtiquetas.addColumna('150','center',$('#descEtiq').val());
    tablaEtiquetas.displayCabecera=true;
          
    var listaEtiquetasGestionAvisosJSON = JSON.parse($("#listaEtiquetasGestionAvisos").val());
    var datos = new Array();
    var contador = 0;
        
    if (listaEtiquetasGestionAvisosJSON != null && listaEtiquetasGestionAvisosJSON.length > 0) 
    {      
        listaEtiquetasGestionAvisosJSON.forEach(function(etiqueta, index) {
            datos[contador] = [etiqueta.codigo, etiqueta.descripcion];
            contador++;
          });
    }
 
    tablaEtiquetas.lineas=datos; 
    tablaEtiquetas.displayTabla();
}


function pulsarSalirGA(){
    //document.forms[0].target = "mainFrame";
    //document.forms[0].action = '<%=request.getContextPath()%>/jsp/usuariosForms/presentacionADM.jsp';
    //document.forms[0].submit();
    retornoSeleccion('0','FLBPRU','1','FLBPRU - LANBIDE ENTORNO PRUEBAS','0_1','/images/org/logo.png','');
    $("#listaEtiquetasGestionAvisos").value = null;
}

function retornoSeleccion(unidadOrgCod,unidadOrg) {
    var organizacion = new Object();
    organizacion.orgCod = unidadOrgCod;
    organizacion.org = unidadOrg;
    self.parent.opener.retornoXanelaAuxiliar(organizacion);
}


function cerrarVentanaGA(){
    if(navigator.appName=="Microsoft Internet Explorer") {
          window.parent.window.opener=null;
          window.parent.window.close();
    } else if(navigator.appName=="Netscape") {
          top.window.opener = top;
          top.window.open('','_parent','');
          top.window.close();
     }else{
         window.close();
     }
  }


function pulsarEtiquetasGA(){
    pleaseWait('on'); 
    lanzarPopUpModal(APP_CONTEXT_PATH+'/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE06_608094&operacion=cargarEtiquetas&tipo=0', 450, 850, 'no', 'no', function (result) {
    pleaseWait('off');
    $("#listaEtiquetasGestionAvisos").value = null;
    });
}


function selectRow(rowID, tab) {
    filaIDseleccionada = datosGA[rowID].id;
    if(tab.tabla.focusedIndex>=-1){
       if(tab.tabla.focusedIndex==rowID){
           limpiarFiltros(); 
           tab.tabla.selectLinea(rowID);
           document.getElementById("btnModificar").disabled = true;
           document.getElementById("btnEliminar").disabled = true;
           document.getElementById("btnAnadir").disabled = false;   
       }else{
           cargarMetadatosGA(datosGA[rowID]);
           tab.tabla.selectLinea(rowID);
           document.getElementById("btnModificar").disabled = false;
           document.getElementById("btnEliminar").disabled = false;
           document.getElementById("btnAnadir").disabled = true;
       }
    }
    callClick(rowID, tab.tabla);
    return false;
}

function cargarMetadatosGA(datosGA){  
    document.getElementById("codProcedimiento").value = datosGA.codProcedimiento;
    document.getElementById("descProcedimiento").value = datosGA.procedimiento;
    document.getElementById("codUOR").value = datosGA.codUor;
    document.getElementById("descUOR").value = datosGA.uor;
    document.getElementById("codEvento").value = datosGA.codEvento;
    document.getElementById("descEvento").value = datosGA.evento;
    document.getElementById("desEmail").value = datosGA.email;
    document.getElementById("desAsunto").value = decodeURIComponent(escape(datosGA.asunto));
    document.getElementById("desContenido").value = decodeURIComponent(escape(datosGA.contenido));
    document.getElementById("codAsunto").value = datosGA.codAsuntoRegistro;
    document.getElementById("descAsunto").value = datosGA.asuntoRegistro;
    if(datosGA.configuracion_activa == 0){
        document.getElementById("checkConfActiva").checked = false;
        document.getElementById("checkConfActiva").value = "N";
    }else{
        document.getElementById("checkConfActiva").checked = true;
        document.getElementById("checkConfActiva").value = "S";
    }
}

function SoloDigitosNumericosLanbide(e) {
      var tecla, caracter;
      var numeros= "0123456789";

      tecla = (document.all)?e.keyCode:e.which;

      if (tecla == null) return true;
      if ((tecla == 0)|| (tecla == 8)) return true;


      caracter = String.fromCharCode(tecla);

      if (numeros.indexOf(caracter) != -1) {
          return true;
      } else {
      return false;
      }
  }

