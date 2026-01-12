<%@ page import="org.apache.logging.log4j.Logger"%>
<%@ page import="org.apache.logging.log4j.LogManager"%>
<jsp:include page="/jsp/registro/tpls/app-constants.jsp" />
<%
  response.setContentType("application/vnd.adobe.xfdf");
  String solicitud = (String)session.getAttribute("solicitud");
  Logger m_log = LogManager.getLogger(this.getClass().getName());
  if(m_log.isDebugEnabled()) m_log.debug(solicitud);
  session.removeAttribute("solicitud");
%><%=solicitud%>
