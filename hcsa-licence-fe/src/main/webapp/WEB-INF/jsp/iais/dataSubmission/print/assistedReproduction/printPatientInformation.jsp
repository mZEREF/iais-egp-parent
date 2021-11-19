<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<c:set var="husband" value="${patientInfoDto.husband}" />
<c:set var="declaration" value="${arSuperDataSubmissionDto.dataSubmissionDto.declaration}" />

<div class="main-content">
    <div class="container center-content">
        <div class="col-xs-12">
            <div class="row">
                <div class="col-xs-12 col-md-10">
                    <h3>Preview & Submit</h3>
                </div>
            </div>
            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                <%@include file="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewPatientDetail.jsp" %>
                <%@include file="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/section/previewHusbandDetail.jsp" %>
                <%@include file="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/common/arDeclaration.jsp" %>
            </div>
        </div>
    </div>
</div>
