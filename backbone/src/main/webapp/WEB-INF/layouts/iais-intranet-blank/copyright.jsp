<!-- start of /_themes/sop6/jsp/copyright.jsp -->
<%@page import="ecq.commons.helper.StringHelper"%>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%!
	private static boolean isIPAddress(String str1){
		if(str1==null || str1.trim().length()<7 || str1.trim().length()>15){
			return false;
		}else{
			return true;
		}
	}

%>
<div id="footer">
	<div class="container">
		<p class="copyright text-center"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=Calendar.getInstance().get(Calendar.YEAR) %> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel> </p>
	</div>
</div>