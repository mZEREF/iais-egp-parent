<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                IUI Treatment Co-funding
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body" style="padding-left: 50px">
            <div class="panel-main-content form-horizontal">
                <c:set var="iuiTreatmentSubsidiesDto" value="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto}" />
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field value="Please indicate IUI Co-funding?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <c:forEach items="${pleaseIndicateIuiCoFunding}" var="pleaseIndicateIui">
                            <c:set var="pleaseIndicateIuiValue" value="${pleaseIndicateIui.value}"/>
                            <div class="form-check" style="padding-left: 0px;">
                                <input class="form-check-input"
                                       type="radio"
                                       name="pleaseIndicateIui"
                                       value="${pleaseIndicateIuiValue}"
                                       id="pleaseIndicateIuiCoFundCheck${pleaseIndicateIuiCode}"
                                       <c:if test="${pleaseIndicateIuiValue eq iuiTreatmentSubsidiesDto.artCoFunding}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label">
                                    <span class="check-circle" for="radioNo"></span>
                                    <c:out value="${pleaseIndicateIui.text}"></c:out>
                                </label>
                            </div>
                        </c:forEach>
                        <span class="error-msg" name="iaisErrorMsg" id="error_artCoFunding"></span>
                    </iais:value>
                </iais:row>
                <div id="thereAppealRow">
                <iais:row>
                    <iais:field width="6" value="Is there an Appeal?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="thereAppeal"
                                   value="true"
                                   <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal eq true}">checked</c:if>
                                   id="thereAppealYes"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="thereAppealYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <span id="error_thereAppeal" name="iaisErrorMsg" class="error-msg"></span>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="thereAppeal"
                                   value="false"
                                   <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal eq false}">checked</c:if>
                                   id="thereAppealNo"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="thereAppealNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
            </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" name="count" value="${count}">
<script>
    $(document).ready(function () {
        var count = $("input:hidden[name='count']").val();
            if (count >=3) {
                $('#thereAppealRow').show();
            } else {
                $('#thereAppealRow').hide();
            }
    });
</script>
