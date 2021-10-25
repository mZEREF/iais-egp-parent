<div class="panel panel-default">
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <%--<iais:field width="12" cssClass="col-md-12" value="AR Centre that is performing this submission"/>--%>
                    <div class="form-check-gp col-xs-12 col-md-12">
                        <p class="form-check-title">AR Centre that is performing this submission</p>
                    </div>
                    <div class="col-xs-12 col-md-12">
                        <c:if test="${not empty premisesLabel}">
                        <p>${premisesLabel}</p>
                        </c:if>
                        <c:if test="${not empty premisesOpts && premisesOpts.size() > 1}">
                        <iais:select name="premises" options="premisesOpts" needErrorSpan="false"/>
                        </c:if>
                        <span class="error-msg" name="iaisErrorMsg" id="error_premises"></span>
                    </div>
                </iais:row>
                <iais:row>
                    <div class="form-check-gp col-xs-12 col-md-12">
                        <p class="form-check-title">Please select the type of data that you will be submitting</p>
                        <div class="form-check">
                            <input class="form-check-input" id="AR_TP001" type="radio" name="submissionType" value="AR_TP001"
                                   onclick="toggleOnCheck(this, 'submission-method')"/>
                            <label class="form-check-label" for="AR_TP001">
                                <span class="check-circle"></span><iais:code code="AR_TP001"/>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" id="AR_TP002" type="radio" name="submissionType" value="AR_TP002"
                                   onclick="toggleOnCheck(this, 'submission-method')"/>
                            <label class="form-check-label" for="AR_TP002">
                                <span class="check-circle"></span><iais:code code="AR_TP002"/>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" id="AR_TP003" type="radio" name="submissionType" value="AR_TP003" />
                            <label class="form-check-label" for="AR_TP003">
                                <span class="check-circle"></span><iais:code code="AR_TP003"/>
                            </label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_submissionType"></span>
                    </div>
                </iais:row>
                <iais:row id="submission-method" style="display:none">
                    <div class="form-check-gp">
                        <p class="form-check-title">Please select the method of submission</p>
                        <div class="form-check">
                            <input class="form-check-input" id="DS_MTD001" type="radio" name="submissionMethod" value="DS_MTD001" />
                            <label class="form-check-label" for="DS_MTD001">
                                <span class="check-circle"></span><iais:code code="DS_MTD001"/>
                            </label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" id="DS_MTD002" type="radio" name="submissionMethod" value="DS_MTD002" />
                            <label class="form-check-label" for="DS_MTD002">
                                <span class="check-circle"></span><iais:code code="DS_MTD002"/>
                            </label>
                        </div>
                    </div>
                </iais:row>
            </div>
        </div>
    </div>
</div>