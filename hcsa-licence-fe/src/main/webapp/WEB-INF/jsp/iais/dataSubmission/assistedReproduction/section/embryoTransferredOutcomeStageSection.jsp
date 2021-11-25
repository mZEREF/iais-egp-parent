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
                            <div class="form-check"
                                 style="padding-left: 0px;<c:if test="${OutcomeEmbryoTransferred.codeValue eq 'Unknown'}">width: 140px;</c:if>" >
                                <input class="form-check-input"
                                       type="radio"
                                       name="transferedOutcome"
                                       value="${OutcomeEmbryoTransferredCode}"
                                       id="transferedOutcomeCheck${OutcomeEmbryoTransferredCode}"
                                       <c:if test="${OutcomeEmbryoTransferredCode eq embryoTransferredOutcomeStageDto.transferedOutcome}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label">
                                    <span class="check-circle" for="transferedOutcomeCheck${OutcomeEmbryoTransferredCode}"></span>
                                    <c:out value="${OutcomeEmbryoTransferred.codeValue}"></c:out>
                                </label>
                                <c:if test="${OutcomeEmbryoTransferred.codeValue eq 'Unknown'}">
                                    <a class="btn-tooltip styleguide-tooltip" href="javascript:void(0);" data-toggle="tooltip"
                                       data-html="true" title="" data-original-title='Unknown outcomes refer to scenarios where no further follow up with patient post-transfer procedure'
                                       style="z-index: 999;position: absolute; right: 0px; top: 0px;">!</a>
                                </c:if>
                            </div>

                        </c:forEach>
                        <span id="error_transferedOutcome" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>