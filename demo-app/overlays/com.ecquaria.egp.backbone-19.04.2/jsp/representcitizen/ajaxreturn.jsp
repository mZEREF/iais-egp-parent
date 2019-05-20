<%@page import="ecq.commons.helper.StringHelper"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="NONE"/>
<%
	String status = (String)request.getAttribute("status");
	String msg = (String)request.getAttribute("message");
	if(StringHelper.isEmpty(msg))
		msg = "";
%>
[{'status':'<%=status %>', 'message':'<%=msg %>'}]