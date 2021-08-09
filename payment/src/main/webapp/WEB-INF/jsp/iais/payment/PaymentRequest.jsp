<%--
  Created by IntelliJ IDEA.
  User: ecquaria
  Date: 2019/11/11
  Time: 14:41
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<%

    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>


<webui:setAttribute name="header-ext">
    <%
        /* You can add additional content (SCRIPT, STYLE elements)
         * which need to be placed inside HEAD element here.
         */
    %>
</webui:setAttribute>

<webui:setAttribute name="title">
    <%
        /* You can set your page title here. */
    %>

    <%=process.runtime.getCurrentComponentName()%>

</webui:setAttribute>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>ToBank</title>
</head>
<body>
<jsp:forward page="ToPayment.jsp"></jsp:forward>
</body>
</html>
