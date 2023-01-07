<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                Outcome of IUI Cycle
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <c:set var="outcomeStageDto" value="${arSuperDataSubmissionDto.outcomeStageDto}" />
                <iais:row>
                    <iais:field width="6" value="Is Clinical Pregnancy Detected?"/>
                    <iais:value width="6" display="true" id="pregnancyDetected">
                        <c:if test="${outcomeStageDto.pregnancyDetected == 'Y' }">Yes</c:if>
                        <c:if test="${outcomeStageDto.pregnancyDetected == 'N' }">No</c:if>
                        <c:if test="${outcomeStageDto.pregnancyDetected == 'U' }">Unknown</c:if>
                    </iais:value>
                </iais:row>
                <jsp:include page="/WEB-INF/jsp/iais/dataSubmission/assistedReproduction/common/patientInventoryTable.jsp"/>
            </div>
        </div>
    </div>
</div>