<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/arHeader.jsp" %>

<c:set var="headingSign" value="completed"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <c:choose>
                    <c:when test="${arSuperDataSubmissionDto.selectionDto.lastCycle eq 'DSCL_009'}">
                        <%@include file="common/iuiHeadStepNavTab.jsp" %>
                    </c:when>
                    <c:when test="${arSuperDataSubmissionDto.selectionDto.lastCycle eq 'DSCL_008'}">
                        <%@include file="common/headStepNavTab.jsp" %>
                    </c:when>
                </c:choose>
                <%@include file="common/viewTitle.jsp" %>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/previewOutcomeStageDetail.jsp"%>
<%--                    <div id="pregnancy">--%>
<%--                        <%@include file="section/pregnancyOutcomeStageSection.jsp" %>--%>
<%--                    </div>--%>
                    <%@include file="common/previewDsAmendment.jsp" %>
                    <%@include file="common/arDeclaration.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    $(document).ready(function() {
        pregnancyDetect()
    })
    function pregnancyDetect() {
        console.log(document.getElementById('pregnancyDetected'));
        if (document.getElementById('pregnancyDetected').innerText == 'Yes') {
            $("#pregnancy").show();
        } else {
            $("#pregnancy").hide();
        }
    }
</script>