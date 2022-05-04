<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>
<c:set var="arTreatmentSubsidiesStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.arTreatmentSubsidiesStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                AR Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
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
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:select  id="oldDsSelect" name="oldDsSelect" options="versionOptions" value="${arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Is the ART cycle being co-funded" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDtoVersion.coFunding}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Is there an approved appeal?" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arTreatmentSubsidiesStageDto.isThereAppeal?'Yes':'No'}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${arTreatmentSubsidiesStageDtoVersion.isThereAppeal?'Yes':'No'}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>