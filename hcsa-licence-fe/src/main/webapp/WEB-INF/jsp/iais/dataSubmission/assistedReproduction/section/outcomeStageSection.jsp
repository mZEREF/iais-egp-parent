<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Outcome
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="outcomeStageDto" value="${arSuperDataSubmissionDto.outcomeStageDto}" />
                <iais:row>
                    <iais:field width="6" value="Is Clinical Pregnancy Detected?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="pregnancyDetected"
                                   value="true"
                                   <c:if test="${outcomeStageDto.pregnancyDetected eq true}">checked</c:if>
                                   id="radioYes"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_pregnancyDetected"></span>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="pregnancyDetected"
                                   value="false"
                                   <c:if test="${outcomeStageDto.pregnancyDetected eq false}">checked</c:if>
                                   id="radioNo"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>