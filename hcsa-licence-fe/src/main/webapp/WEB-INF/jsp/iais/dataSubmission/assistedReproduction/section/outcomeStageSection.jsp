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
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="pregnancyDetected"
                                   value="true"
                                   <c:if test="${outcomeStageDto.pregnancyDetected}">checked</c:if>
                                   id="radioYes"
                                   aria-invalid="false" onclick="pregnancyDetect()">
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
                                   <c:if test="${outcomeStageDto.pregnancyDetected=='false'}">checked</c:if>
                                   id="radioNo"
                                   aria-invalid="false" onclick="pregnancyDetect()">
                            <label class="form-check-label"
                                   for="radioNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>

<div id="pregnancy">
    <%@include file="pregnancyOutcomeStageSection.jsp" %>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        pregnancyDetect()
    })
    function pregnancyDetect() {
        if (document.getElementById('radioYes').checked) {
            $("#pregnancy").show();
        } else {
            $("#pregnancy").hide();
        }
    }
</script>