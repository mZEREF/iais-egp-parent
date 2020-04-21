



<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais"   prefix="iais"%>
<webui:setLayout name="iais-intranet"/>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<webui:setLayout name="iais-internet"/>

    <br><br><br><br>
    <div id="intro">
        <p style="padding-left: 400px;"><strong><c:out value="${errorMsg}"></c:out></strong></p>
        <br><br><br><br><br><br><br>
    </div>
