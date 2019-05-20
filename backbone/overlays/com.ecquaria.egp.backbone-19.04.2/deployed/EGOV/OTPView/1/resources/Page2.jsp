<%@page import="com.ecquaria.egp.core.helper.OTPHelper"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="com.ecquaria.egov.core.helper.UserDomainHelper"%>
<%@page import="sop.rbac.user.UserIdentifier"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="none"/>

<%
	String defautlDomain = UserDomainHelper.DOMAIN_CITIZEN;
	UserIdentifier uid = new UserIdentifier();
	String userIdAndReturnDomain = request.getParameter("userId");

	String userId = null;
	String returnDomain = null;
	if(userIdAndReturnDomain.indexOf("@")!=-1){
		String[] userinfo = userIdAndReturnDomain.split("@");
		userId = userinfo[0];
		returnDomain = userinfo[1];
	}else{
		userId = userIdAndReturnDomain;
		returnDomain = defautlDomain;
	}

	String otp = "";
	if(!StringHelper.isEmpty(userId) && !StringHelper.isEmpty(returnDomain)){
		uid.setId(userId);
		uid.setUserDomain(returnDomain);
		otp = OTPHelper.generateOTPByUser(uid);
	}
%>
OTP: <%=otp %>
