<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Outcome of IUI Cycle
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="outcomeStageDto" value="${arSuperDataSubmissionDto.outcomeStageDto}" />
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Is Clinical Pregnancy Detected?" mandatory="true"/>
                    <iais:value width="2" cssClass="col-md-2"  style="padding-right: 0;padding-left: 0;">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="pregnancyDetected"
                                   value="Y"
                                   <c:if test="${outcomeStageDto.pregnancyDetected == 'Y'}">checked</c:if>
                                   id="radioYes"
                                   aria-invalid="false" onclick="pregnancyDetect()">
                            <label class="form-check-label"
                                   for="radioYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_pregnancyDetected"></span>
                    </iais:value>
                    <iais:value width="2" cssClass="col-md-2"  style="padding-right: 0;padding-left: 0;">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="pregnancyDetected"
                                   value="N"
                                   <c:if test="${outcomeStageDto.pregnancyDetected=='N'}">checked</c:if>
                                   id="radioNo"
                                   aria-invalid="false" onclick="pregnancyDetect()">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                    <iais:value width="2" cssClass="col-md-2"  style="padding-right: 0;padding-left: 0;">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="pregnancyDetected"
                                   value="U"
                                   <c:if test="${outcomeStageDto.pregnancyDetected=='U'}">checked</c:if>
                                   id="radioUnknown"
                                   aria-invalid="false" onclick="pregnancyDetect()">
                            <label class="form-check-label"
                                   for="radioUnknown"><span
                                    class="check-circle"></span>Unknown</label>
                        </div>
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>