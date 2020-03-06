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
	User user = UserService.getInstance().getRbacUserByIdentifier(userId, userDomain);
	byte[] picture = (user == null || user.getPicture() == null) ? null : user.getPicture();
	//System.out.println("userId====:"+userId);
	//System.out.println("userDomain====:"+userDomain);
	//System.out.println("user====:"+user);
	String existImg = "true";
	if (picture == null || picture.length == 0){
		existImg = "false";
	}
	request.setAttribute("existImg",existImg);
	pageContext.setAttribute("model", user);
%>
<div class = "sidebar-header" id="user-profile">
	<div class="sidebar-profile">
		<c:url var="pictureLink" value="/process/EGPCLOUD/UserPicture_Get/Start">
			<%if(user!=null){%>
			<c:param name="userDomain" value="${model.userDomain}" />
			<c:param name="id" value="${model.id}" />
			<%}%>

		</c:url>
		<a href="#">
			<div class="sidebar-profile-image">
			<c:if test="${existImg eq 'true'}">
				<img class="img-circle img-responsive"  alt="Profile Picture" src="<c:out value="${pictureLink}" />">
			</c:if>
			<c:if test="${existImg eq 'false'}">
				<img class="img-circle img-responsive"  alt="Profile Picture" src="<%=EgpcloudPortFactory.webContext%>/_themes/egov/images/general/default-user-picture.jpg" />
			</c:if>
			</div>
	<div class="sidebar-profile-details">
		<span style="text-align: center"><small>Welcome</small><br>
		<%=currentUser==null?"":currentUser.getDisplayName() %></span>
	</div>
		</a>
		<a href="${pageContext.request.contextPath}/eservice/INTRANET/IntraLogout">
			<div class="sidebar-profile-details">
		    <span style="text-align: center"><small>Logout</small></span>
			</div></a>
	</div>
</div>
<!-- end of /_themes/sop6/jsp/user-info.jsp -->
