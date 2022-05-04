
<c:set var="embryoTransferredOutcomeStageDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}"/>
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
                <iais:row style="margin-bottom: 0;">
                    <label class="col-xs-4 col-md-4 control-label"><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/>
                        <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                    </label>
                    <label class="col-xs-8 col-md-8 control-label">Submission ID : <span style="font-weight:normal"><c:out value="${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}"/></span>
                    </label>
                </iais:row>
                <hr/>
                <iais:row>
                    <iais:field width="5"  value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Outcome of Embryo Transferred"/>
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                        <iais:code code="${embryoTransferredOutcomeStageDto.transferedOutcome}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>