<c:if test="${dpSuperDataSubmissionDto.appType eq 'DSTY_005'}">
    <c:set var="dataSubmission" value="${dpSuperDataSubmissionDto.dataSubmissionDto}" />
 <div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Amendment
            </strong>
        </h4>
    </div>
    <div id="patientAmentment" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Reason for Amendment" id="amendReasonLabel" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="amendReason" firstOption="Please Select" codeCategory="DATA_SUBMISSION_CYCLE_STAGE_AMENDMENT"
                                     value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                     onchange="toggleOnSelect(this, 'PCS_002', 'amendReasonOtherDiv')"/>
                    </iais:value>
                </iais:row>
                <iais:row id="amendReasonOtherDiv" style="${dataSubmission.amendReason ne 'PCS_002' ? 'display:none'  : null }" >
                    <iais:field width="5" value="Reason for Amendment (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="50" type="text" name="amendReasonOther" id="amendReasonOther"
                                    value="${dataSubmission.amendReasonOther}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
</c:if>