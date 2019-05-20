<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%
	String url = (String)request.getAttribute("nextUrl");
	RequestDispatcher rd = request.getRequestDispatcher(url);
	rd.forward(request, response);
%>
