<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%-- current page: stage--%>
<input type="hidden" name="ar_page" value="stage"/>
<%-- pregnancyOutcome --%>
<input type="hidden" name="pregnancyOutcome" value="section/pregnancyOutStageSection.jsp"/>
<%--preview/ack--%>
<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <%@include file="common/headStepNavTab.jsp" %>

                <h3>Please key in the cycle information below.</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/outcomeStageSection.jsp" %>
                    <div id="pregnancy">
                        <%@include file="section/pregnancyOutcomeStageSection.jsp" %>
                    </div>
                    <%@include file="section/disposalStageDetailSection.jsp" %>
                    <%@include file="common/dsAmendment.jsp" %>
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
        if (document.getElementById('radioYes').checked) {
            $("#pregnancy").show();
        } else {
            $("#pregnancy").hide();
        }
    }
</script>