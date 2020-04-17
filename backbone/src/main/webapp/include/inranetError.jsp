<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/4/17
  Time: 14:01
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>

<br><br><br><br>
<div id="intro">
    <p style="padding-left: 450px;"><strong><c:out value="${errorMsg}"></c:out></strong></p>
    <br><br><br><br><br><br><br>
</div>
