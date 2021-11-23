<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Outcome of Embryo Transferred
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="embryoTransferredOutcomeStageDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}"/>
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Outcome of Embryo Transferred" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${OutcomeEmbryoTransferreds}" var="OutcomeEmbryoTransferred">
                            <c:set var="OutcomeEmbryoTransferredCode" value="${OutcomeEmbryoTransferred.code}"/>
                            <div class="form-check" style="padding-left: 0px;">
                                <input class="form-check-input"
                                       type="radio"
                                       name="transferedOutcome"
                                       value="${OutcomeEmbryoTransferredCode}"
                                       id="pleaseIndicateIuiCoFundCheck${OutcomeEmbryoTransferredCode}"
                                       <c:if test="${OutcomeEmbryoTransferredCode eq embryoTransferredOutcomeStageDto.transferedOutcome}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label">
                                    <span class="check-circle" for="radioNo"></span>
                                    <c:out value="${OutcomeEmbryoTransferred.codeValue}"></c:out>
                                </label>
                            </div>
                        </c:forEach>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>