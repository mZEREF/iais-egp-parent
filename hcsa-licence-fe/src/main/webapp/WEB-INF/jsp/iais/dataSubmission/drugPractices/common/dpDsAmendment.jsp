<c:if test="${dpSuperDataSubmissionDto.appType eq 'DSTY_005'}">
    <c:set var="dataSubmission" value="${dpSuperDataSubmissionDto.dataSubmissionDto}" />
    <c:set var="drugPrescribedDispensedDto" value="${dpSuperDataSubmissionDto.drugPrescribedDispensedDto}" />
    <c:set var="drugSubmission" value="${drugPrescribedDispensedDto.drugSubmission}" />
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
                <c:if test="${dpSuperDataSubmissionDto.dataSubmissionDto.submissionType eq 'DP_TP001'}">
                    <iais:row>
                        <iais:field width="5" value="Reason for Amendment" id="amendReasonLabel" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="amendReason" firstOption="Please Select" codeCategory="DP_PATIENT_INFO_AMENDMENT_REASON"
                                         value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                         onchange="toggleOnSelect(this, 'DPPIRE_002', 'amendReasonOtherDiv')"/>
                        </iais:value>
                    </iais:row>
                    <iais:row id="amendReasonOtherDiv" style="${dataSubmission.amendReason ne 'DPPIRE_002' ? 'display:none'  : null }" >
                        <iais:field width="5" value="Reason for Amendment (Others)" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="50" type="text" name="amendReasonOther" id="amendReasonOther"
                                        value="${dataSubmission.amendReasonOther}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <c:if test="${dpSuperDataSubmissionDto.dataSubmissionDto.submissionType eq 'DP_TP002'}">
                    <c:if test="${drugSubmission.drugType eq 'DPD001'}">
                        <iais:row>
                            <iais:field width="5" value="Reason for Amendment" id="amendReasonLabel" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="amendReason" firstOption="Please Select" codeCategory="DP_DRUG_PRESCRIBED"
                                             value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                             onchange="toggleOnSelect(this, 'SDP_002', 'amendReasonOtherDiv')"/>
                            </iais:value>
                        </iais:row>
                        <iais:row id="amendReasonOtherDiv" style="${dataSubmission.amendReason ne 'SDP_002' ? 'display:none'  : null }" >
                            <iais:field width="5" value="Reason for Amendment (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="50" type="text" name="amendReasonOther" id="amendReasonOther"
                                            value="${dataSubmission.amendReasonOther}"/>
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <c:if test="${drugSubmission.drugType eq 'DPD002'}">
                        <iais:row>
                            <iais:field width="5" value="Reason for Amendment" id="amendReasonLabel" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:select name="amendReason" firstOption="Please Select" codeCategory="DP_DRUG_DISPENSED"
                                             value="${dataSubmission.amendReason}" cssClass="amendReasonSel"
                                             onchange="toggleOnSelect(this, 'SDD_002', 'amendReasonOtherDiv')"/>
                            </iais:value>
                        </iais:row>
                        <iais:row id="amendReasonOtherDiv" style="${dataSubmission.amendReason ne 'SDD_002' ? 'display:none'  : null }" >
                            <iais:field width="5" value="Reason for Amendment (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="50" type="text" name="amendReasonOther" id="amendReasonOther"
                                            value="${dataSubmission.amendReasonOther}"/>
                            </iais:value>
                        </iais:row>
                    </c:if>
                </c:if>
            </div>
        </div>
    </div>
</div>
</c:if>