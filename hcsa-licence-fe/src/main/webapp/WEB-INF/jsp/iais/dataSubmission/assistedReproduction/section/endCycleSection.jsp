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
                    <iais:field width="6" value="Reason for Abandonment" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="reasonSelect"  name="abandonReason" firstOption="Please Select" options="endReasonSelectOption" value="${endCycleStageDto.abandonReason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_reason"></span>
                    </iais:value>
                </iais:row>
                <div id="othersAbandonment" <c:if test="${endCycleStageDto.abandonReason!=''}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="6" value="" mandatory="false"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="20"   name="otherAbandonReason" value="${endCycleStageDto.otherAbandonReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherAbandonReason"></span>
                        </iais:value>
                    </iais:row>
            </div>
        </div>
    </div>
</div>