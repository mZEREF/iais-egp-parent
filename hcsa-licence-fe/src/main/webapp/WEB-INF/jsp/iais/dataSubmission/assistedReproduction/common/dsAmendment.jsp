<c:if test="${arSuperDataSubmissionDto.appType eq 'DSTY_005'}">
    <c:set var="dataSubmission" value="${arSuperDataSubmissionDto.dataSubmissionDto}" />
 <div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#arAmendment">
                Amendment
            </a>
        </h4>
    </div>
    <div id="arAmendment" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="amendReasonCodeCategory" value="${dataSubmission.submissionType == 'AR_TP003' ? 'DATA_SUBMISSION_DONOR_SMAPLE_AMENDMENT' :
                        (dataSubmission.submissionType == 'AR_TP002' ? 'DATA_SUBMISSION_CYCLE_STAGE_AMENDMENT' : 'DATA_SUBMISSION_PATIENT_AMENDMENT')}"/>
                <c:set var="amendReasonCodeCategoryOther" value="${dataSubmission.submissionType == 'AR_TP003' ? 'DSA_002' :
                        (dataSubmission.submissionType == 'AR_TP002' ? 'PCS_002' : 'PTA_003')}"/>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Reason for Amendment" id="amendReasonLabel" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="amendReason" firstOption="Please Select" codeCategory="${amendReasonCodeCategory}"
                                     value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                     onchange="toggleOnSelect(this, '${amendReasonCodeCategoryOther}', 'amendReasonOtherDiv')"/>
                    </iais:value>
                </iais:row>
                <iais:row id="amendReasonOtherDiv" style="${dataSubmission.amendReason ne amendReasonCodeCategoryOther ? 'display:none'  : null }" >
                    <iais:field width="6" cssClass="col-md-6" value="Reason for Amendment (Others)" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="50" type="text" name="amendReasonOther" id="amendReasonOther"
                                    value="${dataSubmission.amendReasonOther}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
</c:if>