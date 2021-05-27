<!-- start of /_themes/sop6/jsp/copyright.jsp -->
<%@page import="ecq.commons.helper.StringHelper"%>
<%@ page import="java.util.Calendar" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@ taglib prefix="egov-smc" uri="ecquaria/sop/egov-smc" %>
<%!
	private static boolean isIPAddress(String str1){
		if(str1==null || str1.trim().length()<7 || str1.trim().length()>15){
			return false;
		}else{
			return true;
		}
	}

%>
<div id="footer" class="footerlogin">
    <div class="container">
        <%
            Calendar ca = Calendar.getInstance();
            int year = ca.get(Calendar.YEAR);
        %>
        <p class="white text-muted text-center copyright"><egov-smc:commonLabel>Copyright</egov-smc:commonLabel> &copy; <%=year%> <egov-smc:commonLabel>Ecquaria Technologies Pte Ltd.</egov-smc:commonLabel> <egov-smc:commonLabel>All Rights Reserved.</egov-smc:commonLabel></p>
    </div>
</div>
<!-- end of /_themes/sop6/jsp/copyright.jsp -->