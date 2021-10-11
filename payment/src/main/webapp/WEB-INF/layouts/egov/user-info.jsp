<!-- start of /_themes/sop6/jsp/user-info.jsp -->
<%@page import="com.ecquaria.cloud.client.rbac.UserService"%>
<%@page import="sop.iwe.SessionManager"%>
<%@page import="sop.rbac.user.User"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c_rt"%>
<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<% 
	SessionManager sessionManager = SessionManager.getInstance(request);
	User currentUser = sessionManager.getCurrentUser();
	String userId = sessionManager.getCurrentUserID();
	String userDomain = sessionManager.getCurrentUserDomain();
	User user2 = UserService.getInstance().getRbacUserByIdentifier(userId, userDomain);
	byte[] picture = (user2 == null || user2.getPicture() == null) ? null : user2.getPicture();
	//System.out.println("userId====:"+userId);
	//System.out.println("userDomain====:"+userDomain);
	//System.out.println("user====:"+user2);
	String existImg = "true";
	if (picture == null || picture.length == 0){
		existImg = "false";
	}
	request.setAttribute("existImg",existImg);
	pageContext.setAttribute("model", user2);
%>
<a href="javascript:;" id="hide-menu">&#xAB;</a>
<div id="user-profile">
<div class="user-pict">
	<c:url var="pictureLink" value="/process/EGPCLOUD/UserPicture_Get/Start">
		<%if(user2!=null){%>
			<c:param name="userDomain" value="${model.userDomain}" />
			<c:param name="id" value="${model.id}" />
		<%}%>

	</c:url>
	<a href="#">
		<c:if test="${existImg eq 'true'}"> 
			<img height="63" border="0" width="62" alt="Profile Picture" src="<c:out value="${pictureLink}" />">
		</c:if>
		<c:if test="${existImg eq 'false'}">
			<img height="63" border="0" width="62" alt="Profile Picture" src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/general/default-user-picture.jpg" />
		</c:if>
	</a>
</div>

<div class="user-pict"><%=currentUser==null?"":currentUser.getDisplayName() %></div>
</div>
<!-- end of /_themes/sop6/jsp/user-info.jsp -->
