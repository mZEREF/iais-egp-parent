<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/ar_common.js"></script>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/transferInOutStageReceiveSection.js"></script>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <c:if test="${not empty bindStageIsRfc}">
                    <h3>${bindStageIsRfc}</h3>
                </c:if>
                <c:if test="${not empty hasConfirmationStage}">
                    <h3>${hasConfirmationStage}</h3>
                </c:if>
                <div class="col-12">
                    <div class="button-group">
                        <a class="btn btn-primary pull-right" id="goToBashboardBtn">Go to <br>Dashboard</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
