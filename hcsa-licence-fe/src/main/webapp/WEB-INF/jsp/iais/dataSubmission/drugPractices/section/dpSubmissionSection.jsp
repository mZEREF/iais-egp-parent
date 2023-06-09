<div class="panel panel-default">
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <div class="form-check-gp col-xs-12 col-md-12">
                        <p class="form-check-title">
                            Acute hospital licence / Nursing home licence / Community hospital licence that
                            is performing this submission
                        </p>
                    </div>
                    <div class="col-xs-12 col-md-8">
                        <c:if test="${not empty premisesLabel}">
                            <p>${premisesLabel}</p>
                        </c:if>
                        <c:if test="${not empty premisesOpts && premisesOpts.size() > 1}">
                            <iais:select name="premises" options="premisesOpts" needErrorSpan="false"
                                         cssClass="premisesSel" value="${dpSuperDataSubmissionDto.premises}"/>
                        </c:if>
                        <span class="error-msg" name="iaisErrorMsg" id="error_premises"></span>
                    </div>
                </iais:row>
                <iais:row>
                    <div class="form-check-gp col-xs-12 col-md-12">
                        <p class="form-check-title">
                            Please select the type of data that you will be submitting
                        </p>
                        <div class="form-check">
                            <input class="form-check-input" id="DP_TP001" type="radio" name="submissionType" value="DP_TP001"
                                   <c:if test="${dpSuperDataSubmissionDto.submissionType eq 'DP_TP001'}">checked</c:if>/>
                            <label class="form-check-label" for="DP_TP001">
                                <span class="check-circle"></span><iais:code code="DP_TP001"/>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" id="DP_TP002" type="radio" name="submissionType" value="DP_TP002"
                                   <c:if test="${dpSuperDataSubmissionDto.submissionType eq 'DP_TP002'}">checked</c:if>/>
                            <label class="form-check-label" for="DP_TP002">
                                <span class="check-circle"></span><iais:code code="DP_TP002"/>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" id="DP_TP003" type="radio" name="submissionType" value="DP_TP003"
                                   <c:if test="${dpSuperDataSubmissionDto.submissionType eq 'DP_TP003'}">checked</c:if>/>
                            <label class="form-check-label" for="DP_TP003">
                                <span class="check-circle"></span><iais:code code="DP_TP003"/>
                            </label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_submissionType"></span>
                    </div>
                </iais:row>
            </div>
        </div>
    </div>
</div>