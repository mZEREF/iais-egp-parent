<%--
  Created by IntelliJ IDEA.
  User: yichen
  Date: 9/17/2019
  Time: 3:26 PM
  To change this template use File | Settings | File Templates.
--%>
--%>
--%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-intranet"/>

<div class="main-content">
    <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <div class="bg-title"><h2>Audit Trail Full Module</h2></div>

        <%@ include file="maincontent.jsp"%>


    </form>
</div>


