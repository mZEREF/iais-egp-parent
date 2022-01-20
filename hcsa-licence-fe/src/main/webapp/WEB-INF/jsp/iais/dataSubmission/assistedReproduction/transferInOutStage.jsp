<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please key in the cycle information below.</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:choose>
                        <c:when test="${not empty outStageDsNo}">
                            <%@include file="section/transferInOutStageReceiveSection.jsp" %>
                        </c:when>
                        <c:otherwise>
                            <%@include file="section/transferInOutStageSection.jsp" %>
                        </c:otherwise>
                    </c:choose>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
