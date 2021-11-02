<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>
<webui:setLayout name="iais-internet"/>

<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<c:set var="husband" value="${patientInfoDto.husband}" />

<%@ include file="common/arHeader.jsp" %>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/cycleStageSelection.js"></script>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please key in a valid patient ID No. and select the next stage to continue</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/cycleStageSelectionSection.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
<c:if test="${hasDraft}">
    <iais:confirm msg="DS_MSG002" callBack="submit('confirm', 'resume');" popupOrder="_draftModal"  yesBtnDesc="Continue"
                  cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelBtnDesc="Delete" cancelFunc="submit('confirm', 'delete');" />
</c:if>
<iais:confirm msg="DS_MSG003" callBack="$('#noFoundDiv').modal('hide');" popupOrder="noFoundDiv"  yesBtnDesc="Close"
              cancelBtnDesc="Register Patient" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
              cancelFunc="submit('', 'patient');" />
<script type="text/javascript">
    <c:if test="${hasDraft}">
    //$('#_draftModal').modal('show');
    </c:if>
</script>