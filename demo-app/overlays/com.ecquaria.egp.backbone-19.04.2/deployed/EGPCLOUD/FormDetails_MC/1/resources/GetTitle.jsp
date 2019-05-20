<%@page contentType="text/javascript" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@page import="com.ecquaria.egp.core.forms.instance.Form"%>
<webui:setLayout name="none"/>
if(setFormDlgTitle) {
	setFormDlgTitle("<%=((Form)request.getAttribute("egov.form")).getFormDef().getName() %>");
}
