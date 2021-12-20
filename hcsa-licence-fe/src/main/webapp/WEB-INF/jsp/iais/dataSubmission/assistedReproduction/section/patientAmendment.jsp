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
                        <c:choose>
                            <c:when test="${dataSubmission.submissionType == 'AR_TP003'}">
                                <iais:select name="amendReason" firstOption="Please Select" codeCategory="DATA_SUBMISSION_PATIENT_AMENDMENT"
                                             value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                             onchange="toggleOnSelect(this, 'PTA_003', 'amendReasonOtherDiv')"/>
                            </c:when>
                            <c:otherwise>
                                <iais:select name="amendReason" firstOption="Please Select" codeCategory="DATA_SUBMISSION_DONOR_SMAPLE_AMENDMENT"
                                             value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                             onchange="toggleOnSelect(this, 'DSA_002', 'amendReasonOtherDiv')"/>
                            </c:otherwise>
                        </c:choose>
                    </iais:value>
                </iais:row>
                <div class="form-group" id="amendReasonOtherDiv" style="<c:if test="${dataSubmission.amendReason ne 'PTA_003' && dataSubmission.amendReason ne 'DSA_002'}">display:none</c:if>">
                    <iais:field width="5" value="Reason for Amendment (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="50" type="text" name="amendReasonOther" id="amendReasonOther"
                                    value="${dataSubmission.amendReasonOther}"/>
                    </iais:value>
                </div>
            </div>
        </div>
    </div>
</div>