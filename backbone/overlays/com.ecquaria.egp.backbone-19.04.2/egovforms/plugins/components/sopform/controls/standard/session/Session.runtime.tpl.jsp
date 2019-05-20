<%@ page import="com.ecquaria.egp.core.forms.definition.Element" %>
<%@ page import="com.ecquaria.egp.core.forms.servlet.ElementUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="sop.iwe.SessionManager" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page isELIgnored="true" %>
<%
List elements = (List) request.getAttribute("ELEMENTS");
ElementUtil elementUtil = (ElementUtil) request.getAttribute("ELEMENT_UTIL");
SessionManager sessionManager = SessionManager.getInstance(request);
if (sessionManager != null) {
	pageContext.setAttribute("loginInfo", sessionManager.getLoginInfo());
}
%>
{var id = control.getElementId()}
{var type = control.properties.type}
{var contextData}
{eval}
<% if (elementUtil != null) { %>
contextData = <%=elementUtil.getContextDataAsJson(pageContext, "key", null, "isEL") %>;
<% } else { %>
contextData = {};
<% }%>
{/eval}
<div id="${id}" class="control">
	<div class="control-label-span control-set-alignment">
		<label id="${id}**label"
				class="${type} control-label control-set-font">
			{if control.properties.key}
				${contextData[control.properties.key + '||session||' + control.properties.isEL]|default:' '|escape}
			{else}
				&nbsp;
			{/if}
		</label>
	</div>
</div>
