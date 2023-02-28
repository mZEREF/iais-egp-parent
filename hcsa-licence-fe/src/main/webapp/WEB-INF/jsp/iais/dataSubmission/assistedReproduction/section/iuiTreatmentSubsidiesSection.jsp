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
                    <iais:field width="6" cssClass="col-md-6" value="Is the IUI treatment co-funded" mandatory="true"/>
                        <c:forEach items="${pleaseIndicateIuiCoFunding}" var="pleaseIndicateIui">
                            <iais:value cssClass="col-md-3">
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
                            </iais:value>
                        </c:forEach>
                        <span class="error-msg" name="iaisErrorMsg" id="error_artCoFunding"></span>
                </iais:row>
                <div id="thereAppealRow">
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Is there an Approved Appeal?" mandatory="true"/>
                    <iais:value  cssClass="col-md-2">
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="thereAppeal"
                                   value="Yes"
                                   <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal eq 'Yes'}">checked</c:if>
                                   id="thereAppealYes"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="thereAppealYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="thereAppeal"
                                   value="No"
                                   <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal eq 'No'}">checked</c:if>
                                   id="thereAppealNo"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="thereAppealNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                        <div class="form-check" style="padding-left: 0px;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="thereAppeal"
                                   value="N/A"
                                   <c:if test="${iuiTreatmentSubsidiesDto.thereAppeal eq 'N/A'}">checked</c:if>
                                   id="thereAppealNA"
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="thereAppealNA"><span
                                    class="check-circle"></span>N/A</label>
                        </div>
                    </iais:value>
                    <span id="error_thereAppeal" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Please indicate appeal reference number (if applicable)" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input type="text" maxLength="10" value="${iuiTreatmentSubsidiesDto.appealNumber}" name="appealNumber"/>
                    </iais:value>
                    <span id="error_appealNumber" name="iaisErrorMsg" class="error-msg"></span>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>
<input type="hidden" name="iuiCount" value="${iuiCount}">
<script>
    $(document).ready(function () {
        let iuiCount = $("input[name='iuiCount']").val();
        if (iuiCount >=3) {
            $('#thereAppealRow').show();
        } else {
            $('#thereAppealRow').hide();
        }
    });
</script>
