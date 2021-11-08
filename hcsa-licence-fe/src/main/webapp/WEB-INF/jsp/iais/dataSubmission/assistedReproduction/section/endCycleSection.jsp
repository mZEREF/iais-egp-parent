<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
               End Cycle
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="endCycleStageDto" value="${arSuperDataSubmissionDto.endCycleStageDto}" />
                <iais:row>
                    <iais:field width="6" value="Is it Medically Indicated?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="cycleAbandoned"
                                   value="true"
                                   id="radioYes"
                                   <c:if test="">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="cycleAbandoned"
                                   value="false"
                                   id="radioNo"
                                   <c:if test="">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No, Cycle has ended</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reason for Abandonment" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="abandonReasonSelect"  name="abandonReasonSelect" firstOption="Please Select" codeCategory="END_CYCLE_REASON_FOR_ABANDONMENT" value="${endCycleStageDto.abandonReason}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_abandonReason"></span>
                    </iais:value>
                </iais:row>
                <div <c:if test="${endCycleStageDto.abandonReason!='ENDRA005'}">style="display: none"</c:if> >
                    <iais:row  id="otherAbandonReasonRow" >
                        <iais:field width="5" value="" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="20" type="text" name="otherAbandonReason" value="${endCycleStageDto.otherAbandonReason}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherAbandonReason"></span>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
