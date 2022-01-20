<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="husband" value="${patientInfoDto.husband}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#husbanDetails">
                Details of Husband
            </a>
        </h4>
    </div>
    <div id="husbanDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="person" value="${husband}" />
                <%@include file="previewPersonSection.jsp" %>
            </div>
        </div>
    </div>
</div>