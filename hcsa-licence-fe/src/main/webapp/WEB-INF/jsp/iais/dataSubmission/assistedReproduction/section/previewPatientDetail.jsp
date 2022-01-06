<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />

<c:set var="isRFC" value="${'DSTY_005' == arSuperDataSubmissionDto.appType}" />
<c:set var="showPrevious" value="${patient.previousIdentification && isRFC}" />

<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="person" value="${patient}" />
                <%@include file="previewPersonSection.jsp" %>
                <c:if test="${showPrevious}">
                    <c:set var="person" value="${previous}" />
                    <%@include file="previewPatientPreviousSection.jsp" %>
                </c:if>
            </div>
        </div>
    </div>
</div>