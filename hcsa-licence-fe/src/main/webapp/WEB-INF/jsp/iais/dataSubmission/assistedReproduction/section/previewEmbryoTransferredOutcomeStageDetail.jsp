<c:set var="headingSign" value="completed"/>
<c:set var="fertilisationDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Outcome of Embryo Transferred
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Outcome of Embryo Transferred" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7"  label="true">
                        <iais:code code="${embryoTransferredOutcomeStageDto.transferedOutcome}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>