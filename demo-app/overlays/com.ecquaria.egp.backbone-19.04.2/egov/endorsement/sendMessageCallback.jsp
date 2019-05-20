<%@page import="ecq.commons.helper.StringHelper"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="NONE"/>
<%
	response.setContentType("text/html;charset=UTF-8");
	String status = (String)request.getAttribute("status");
	String msg = (String)request.getAttribute("message");
	String data = (String)request.getAttribute("data");
	if(StringHelper.isEmpty(msg))
		msg = "";
%>
[{'status':'<%=status %>', 'message':'<%=msg %>', 'data':<%=data %>}]