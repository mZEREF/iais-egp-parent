<!-- start of /_themes/egov/jsp/left-menu.jsp -->
<%@page import="com.ecquaria.cloud.client.menu.Menu"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@page import="java.io.*, java.util.*"%>
<%@ page import="ecq.commons.helper.ArrayHelper" %>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%@ page import="ecq.commons.helper.StringHelper" %>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="http://www.ecquaria.com/menu" prefix="menu"%>


<menu:load id="menus">
	<menu:exclude name="TOP_MENU" />
	<menu:exclude name="EGOV_CORE_TOP" />
	<menu:exclude name="EGOV_CLOUD_TOP" />
  <menu:exclude name="CLOUD_CC" />
	<menu:exclude name="CLOUD_PAYMENT" />
	<menu:exclude name="CLOUD_WORKSPACE" />
	<menu:exclude name="WORKSPACE" />
</menu:load>

<ul class="list-unstyled components" id="main-menu">
<menu:iterate id="menus" var="item" varStatus="status">
	<c:choose>
		<c:when test="${!status.last and status.next.depth > 0}">
			<c:set var="nextDepth" value="${status.next.depth}" />
		</c:when>
		<c:otherwise>
			<c:set var="nextDepth" value="0" />
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${item.depth > 0}">
			<c:set var="currDepth" value="${item.depth}" />
		</c:when>
		<c:otherwise>
			<c:set var="currDepth" value="0" />
		</c:otherwise>
	</c:choose>

	<c:choose>
		<c:when test="${item.depth == 0}">
			<li >
		</c:when>
		<c:otherwise>
			<li>
		</c:otherwise>
	</c:choose>

	<%
		Menu temp = (Menu)pageContext.getAttribute("item");
		if ( temp != null && temp.getMenuStyle() != null && !temp.getMenuStyle().equals("")){
			if (temp.getMenuStyle().equals("default")){
	%>
	<span class="menu-icon default"></span>
	<%
	} else if (temp.getMenuStyle().startsWith("url:")){
		String menuStyleDis = temp.getMenuStyle().substring(4);
		pageContext.setAttribute("menuStyleDis", menuStyleDis);
	%>
	<c:if test="${!empty menuStyleDis}">
		<span class="menu-icon">		
			<img src="<c:out value="${menuStyleDis}" />" width="16px" height="16px" alt="menu"/>
		</span>
	</c:if>
	<%
	} else if (temp.getMenuStyle().startsWith("css:")){
		String menuStyleCssDis = temp.getMenuStyle().substring(4);
		pageContext.setAttribute("menuStyleCssDis", menuStyleCssDis);
	%>
	<span class="menu-icon <c:out value="${menuStyleCssDis}" />"></span>
	<%
			}
		}
	%>

	<c:set var="url" value="${item.url}" scope="page" />
	<%
		String url = (String) pageContext.getAttribute("url");
		if (url == null || "".equals(url) || "#".equals(url)) {
	%>
	<a href="#<c:out value="${item.id}" />" data-toggle="collapse" aria-expanded="false">
		<egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
	</a>
	<%
	} else if (url.startsWith("/")) {
	%>
	<a href="<c:out value="${item.url}" />">
		<egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
	</a>
	<%
	} else if (url.startsWith("ctx:")) {
		String u = url.substring(4);
		String[] args = u.split("\\@\\|\\@");
		String path = "";
		if(!ArrayHelper.isEmpty(args) && args.length == 2){
			path = ConfigHelper.getString(args[0]) + args[1];
		}
	%>
	<a href="<%=path%>">
	 <egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
	</a>
	<%
	} else {
	%>
	<a href="<c:out value="${item.url}" />">
		<egov-smc:commonLabel><c:out value="${item.displayLabel}" /></egov-smc:commonLabel>
	</a>
	<%
		}
	%>

	<c:choose>
		<c:when test="${currDepth == nextDepth}">
			</li>
		</c:when>
		<c:when test="${currDepth > nextDepth}">
			<c:forEach begin="0" end="${currDepth - nextDepth - 1}">
				</li>
				</ul>
			</c:forEach>
			</li>
		</c:when>
		<c:otherwise>

			<ul class="collapse list-unstyled" id = '<c:out value="${item.id}" />' >
		</c:otherwise>
	</c:choose>
</menu:iterate>
</ul>
<!-- end of /_themes/egov/jsp/left-menu.jsp -->
