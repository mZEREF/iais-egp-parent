<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@ include file="common/dpHeader.jsp" %>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Submit Patient Information</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <span id="error_topErrorMsg" name="iaisErrorMsg" class="error-msg"></span>
                    <%@include file="section/patientInformationSection.jsp" %>
                    <%@include file="common/dpDsAmendment.jsp" %>
                </div>
                <%@include file="common/dpFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
<c:if test="${hasDraft && dpSuperDataSubmissionDto.submissionType eq 'DP_TP001'}">
    <iais:confirm msg="DS_MSG001" callBack="submit('confirm', 'resume');" popupOrder="_draftModal"  yesBtnDesc="Resume from draft"
                  cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelBtnDesc="Continue" cancelFunc="submit('confirm', 'delete')" />
</c:if>
