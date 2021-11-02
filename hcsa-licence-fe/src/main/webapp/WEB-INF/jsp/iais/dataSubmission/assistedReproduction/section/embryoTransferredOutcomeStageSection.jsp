<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Outcome of Embryo Transferred
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="arCycleStageDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}" />
                <iais:row>
                    <iais:field width="5" value="Outcome of Embryo Transferred" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="transferedOutcome"  firstOption="Clinical Pregnancy Detected" codeCategory="OUTCOME_OF_EMBRYO_TRANSFERRED" value="${embryoTransferredOutcomeStageDto.transferedOutcome}" />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>