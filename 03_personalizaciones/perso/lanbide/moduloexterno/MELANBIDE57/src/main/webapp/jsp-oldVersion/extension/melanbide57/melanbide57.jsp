<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO"%>
<%@page import="java.util.List" %>

<script type="text/javascript">
    var listaSustitucion = new Array();
    <%  		
        TramiteVO objectVO = null;
        List<TramiteVO> listaTramites = (List<TramiteVO>)request.getAttribute("listaTramitesAreas");													
        if (listaTramites!= null && listaTramites.size() >0){
            for (int indice=0;indice<listaTramites.size();indice++)
            {
                objectVO = listaTramites.get(indice);

    %>
                //por cada posición de la lista guardo el nombre original del trámite a sustituir y el área
                listaSustitucion[<%=indice%>] = ['<%=objectVO.getTramiteNombre()%>','<%=objectVO.getTramiteNombreArea()%>'];
    <%
            }// for
        }// if
    %>
        
    //cuando se pincha en la pestana de datos suplementarios tengo que cambiar literales
    var cambiarLiterales = true;
    //ya que la carga de datos suplementarios se hace con una llamada ajax aprovecho esto para detectar cuándo hacer el cambio de los literales (he hecho múltiples pruebas de otras maneras como capturando el click de la pestaña pero se ejecuta antes que el código de fichaExpediente.jsp y no he conseguido que haga la espera)
    $( document ).ajaxComplete(function() {
        if(estanCargadosCamposSuplementarios() && cambiarLiterales){
            $('.deTramite').each(function () {
                /*if("Trámite GESTION POR AREA ASIGNADA 1"===$(this).html()){
                    $(this).text("prueba jess");
                }*/
                //recorro la lista de trámites a sustituir y si coincide con el elemento actual lo sustituyo
                var textoASustituir;
                for(var i = 0;i< listaSustitucion.length; i++){
                    //si el texto original del trámite coincide con el elemento actual sustituyo el AREA
                    if($(this).html().match(new RegExp(listaSustitucion[i][0] + "$"))){//regexp porque flexia añade antes del nombre original la palabra "trámite"
                        //sustituyo el texto a partir de la palabra "AREA" hasta el final
                        textoASustituir = (listaSustitucion[i][0]).substring(listaSustitucion[i][0].indexOf("AREA"));
                        $(this).text($(this).html().replace(new RegExp(textoASustituir + "$"), listaSustitucion[i][1]));
                    }
                }
            });
            cambiarLiterales = false;
        }
    });
</script>