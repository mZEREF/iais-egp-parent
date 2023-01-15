<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
<%@ include file="common/header.jsp" %>
<c:set var="headingSign" value="completed"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <%@include file="../common/headStepNavTab.jsp" %>
                <%@include file="../common/viewTitle.jsp" %>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <c:choose>
                        <c:when test="${arSuperDataSubmissionDto.submissionType eq 'AR_TP003'}">
                            <%@include file="section/previewSampleSection.jsp" %>
                        </c:when>
                        <c:otherwise>
                            <%@include file="../section/previewPatientDetail.jsp" %>
                            <%@include file="../section/previewHusbandDetail.jsp" %>
                        </c:otherwise>
                    </c:choose>
                    <%@include file="../common/previewDsAmendment.jsp" %>
                    <%@include file="../common/arDeclaration.jsp" %>
                </div>
                <%@include file="common/footer.jsp" %>
            </div>
        </div>
    </div>
</form>