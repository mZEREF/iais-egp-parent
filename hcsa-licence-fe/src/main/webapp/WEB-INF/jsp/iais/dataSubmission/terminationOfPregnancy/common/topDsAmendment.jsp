<c:if test="${topSuperDataSubmissionDto.appType eq 'DSTY_005'}">
    <c:set var="dataSubmission" value="${topSuperDataSubmissionDto.dataSubmissionDto}" />
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" href="#patientAmentment">
                    Amendment
                </a>
            </h4>
        </div>
        <div id="patientAmentment" class="panel-collapse collapse in">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                    <iais:row>
                        <iais:field width="5" value="Reason for Amendment" id="amendReasonLabel" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="amendReason" firstOption="Please Select" codeCategory="REASON_FOR_TOP_AMENDMENT"
                                         value="${dataSubmission.amendReason}" id="amendReason" cssClass="amendReasonSel"
                                         onchange="toggleOnSelect(this, 'TCT_002', 'amendReasonOtherDiv')"/>
                        </iais:value>
                    </iais:row>
                    <iais:row id="amendReasonOtherDiv" style="${dataSubmission.amendReason ne 'TCT_002' ? 'display:none'  : null }" >
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