<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%><webui:setLayout name="NONE"/><%@page contentType="application/json;"%><%
sop.webflow.rt.api.BaseProcessClass process =
(sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
String res = (String)request.getAttribute("res");
out.print(res);
%>