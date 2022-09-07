<%--@elvariable id="arSuperDataSubmissionDto" type="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto"--%>
<c:set var="selectionDto" value="${arSuperDataSubmissionDto.selectionDto}" />

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Cycle Stage Selection
            </strong>
        </h4>
    </div>
    <div id="cycleStageSectionPanel" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient ID No."/>
                    <iais:value width="2" cssClass="col-md-3" display="true">
                        <iais:code code="${selectionDto.patientIdType}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-3" display="true">
                        <c:out value="${selectionDto.patientIdNumber}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient Nationality."/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:code code="${selectionDto.patientNationality}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient Name."/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${selectionDto.patientName}"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Last Stage Submitted"/>
                    <iais:value width="6" cssClass="col-md-6" display="true" id="lastStage">
                        <iais:code code="${selectionDto.lastStage}" needEscapHtml="false" viewEmptyStr="true"/>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Stage" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="stage" options="stage_options"
                                     value="${selectionDto.stage}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
