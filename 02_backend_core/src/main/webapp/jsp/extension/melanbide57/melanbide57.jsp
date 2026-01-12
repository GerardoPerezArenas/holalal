<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

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
                //por cada posici�n de la lista guardo el nombre original del tr�mite a sustituir y el �rea
                listaSustitucion[<%=indice%>] = ['<%=objectVO.getTramiteNombre()%>','<%=objectVO.getTramiteNombreArea()%>'];
    <%
            }// for
        }// if
    %>
        
    //cuando se pincha en la pestana de datos suplementarios tengo que cambiar literales
    var cambiarLiterales = true;
    //ya que la carga de datos suplementarios se hace con una llamada ajax aprovecho esto para detectar cu�ndo hacer el cambio de los literales (he hecho m�ltiples pruebas de otras maneras como capturando el click de la pesta�a pero se ejecuta antes que el c�digo de fichaExpediente.jsp y no he conseguido que haga la espera)
    $( document ).ajaxComplete(function() {
        if(estanCargadosCamposSuplementarios() && cambiarLiterales){
            $('.deTramite').each(function () {
                /*if("Tr�mite GESTION POR AREA ASIGNADA 1"===$(this).html()){
                    $(this).text("prueba jess");
                }*/
                //recorro la lista de tr�mites a sustituir y si coincide con el elemento actual lo sustituyo
                var textoASustituir;
                for(var i = 0;i< listaSustitucion.length; i++){
                    //si el texto original del tr�mite coincide con el elemento actual sustituyo el AREA
                    if($(this).html().match(new RegExp(listaSustitucion[i][0] + "$"))){//regexp porque flexia a�ade antes del nombre original la palabra "tr�mite"
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
