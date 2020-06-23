<%@page import="com.ecquaria.egp.core.payment.api.config.GatewayConstants"%>
<%@page import="com.ecquaria.egp.core.payment.api.services.GatewayAPI"%>
<%@page import="java.security.NoSuchAlgorithmException"%>
<%@page import="java.util.Enumeration"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ include file="/WEB-INF/jsp/inc/script.jsp" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="none"/>
<form action="commpymt.jsp">
	SVC refno:<input type="text" name="svc_refno"/><br/>
	Payment description:<textarea name="description"></textarea><br/>
	Amount:<input type="text" name="amount"/><br/>
	<input type="submit" value="Pay"/>
</form>
<hr/>
<form action="commpymt_check.jsp">
	SVC refno:<input type="text" name="svc_refno"/><br/>
	<input type="submit" value="Check"/>
</form>

<hr/>

<form action="commpymt_update.jsp">
	SVC refno:<input type="text" name="svc_refno"/><br/>
	CPS refNo:<input type="text" name="cps_refno"/><br/>
	Amount:<input type="text" name="amount"/><br/>
	Payment Status:<input type="text" name="pymt_status"/><br/>
	<input type="submit" value="Update"/>
</form>