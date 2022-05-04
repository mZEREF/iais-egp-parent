
<c:set var="embryoTransferredOutcomeStageDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}"/>
<c:set var="embryoTransferredOutcomeStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.embryoTransferredOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Outcome of Embryo Transferred
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="4" value="Outcome of Embryo Transferred"/>
                    <iais:value width="4" cssClass="col-md-4"  display="true">
                        <iais:code code="${embryoTransferredOutcomeStageDto.transferedOutcome}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4"  display="true">
                        <iais:code code="${embryoTransferredOutcomeStageDtoVersion.transferedOutcome}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>