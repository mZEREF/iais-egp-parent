<!-- start of /_themes/sop6/jsp/copyright.jsp -->
<%@page import="ecq.commons.helper.StringHelper"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
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
        	<ul>
				<li>copy right</li>
				<!-- Project can indicate their respective Terms & Condition and Privacy Policy here -->				
				<!-- 
				<li class="divider-right"></li>
				<li><a href="<c:url value="/TermsOfServices.jsp"/>" target="_BLANK">Terms of Services</a></li>
				<li><a href="<c:url value="/PrivacyPolicy.jsp"/>"  target="_BLANK">Privacy Policy</a></li>
				 -->
            </ul>
</div>
<!-- end of /_themes/sop6/jsp/copyright.jsp -->