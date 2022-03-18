<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Counselling Given" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="counsellingGiven"
                       value="1"
                       id="counsellingYes"
                       <c:if test="${preTerminationDto.counsellingGiven}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="counsellingYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingGiven"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="counsellingGiven"
                       value="0"
                       id="counsellingNo"
                       <c:if test="${preTerminationDto.counsellingGiven == 'false'}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="counsellingNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Given Counselling On Mid-Trimester Pregnancy Termination" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="counsellingGivenOnMin"
                       value="1"
                       id="pregnancyYes"
                       <c:if test="${preTerminationDto.counsellingGivenOnMin}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="pregnancyYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingGivenOnMin"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="counsellingGivenOnMin"
                       value="0"
                       id="pregnancyNo"
                       <c:if test="${preTerminationDto.counsellingGivenOnMin == 'false'}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="pregnancyNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Patient Sign the Acknowledgement For Counselling On Mid-Trimester Pregnancy Termination"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="patientSign"
                       value="1"
                       id="acknowledgementYes"
                       <c:if test="${preTerminationDto.patientSign}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="acknowledgementYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_patientSign"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="patientSign"
                       value="0"
                       id="acknowledgementNo"
                       <c:if test="${preTerminationDto.patientSign == 'false'}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="acknowledgementNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <%--<label class="col-xs-6 col-md-6 control-label">Pre-Termination Counsellor ID Type
            <span id="counsellorIdType" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven == 'Yes'}">*</c:if>
            </span>
        </label>--%>
        <iais:field width="5" value="Pre-Termination Counsellor ID Type"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${preTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
        </iais:value>

    </iais:row>
    <iais:row>
        <iais:field width="5" value="Pre-Termination Counsellor ID No."/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${preTerminationDto.counsellorIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Pre-Termination Counsellor Name"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellorName" value="${preTerminationDto.counsellorName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Pre-Termination Counselling Date"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="counsellingDate" value="${preTerminationDto.counsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Pre-Counselling Place"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellingPlace" value="${preTerminationDto.counsellingPlace}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Pre-Counselling Result"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellingResult" firstOption="Please Select" codeCategory="TOP_CONSULTATION_RESULTS"
                         value="${preTerminationDto.counsellingResult}" cssClass="counsellingResult"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingResult"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="If undecided, date of second pre-counselling"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="secCounsellingDate" value="${preTerminationDto.secCounsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Result of Second Pre-Counselling"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="secCounsellingResult" firstOption="Please Select" codeCategory="TOP_CONSULTATION_RESULTS"
                         value="${preTerminationDto.secCounsellingResult}" cssClass="secCounsellingResult"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingResult"></span>
        </iais:value>
    </iais:row>
</div>
<script>
    $(document).ready(function () {
        $('input[name=counsellingGiven]:checked').change(function () {
            if($('#pregnancyYes').prop('checked')) {
                console.log("1");
                $('#counsellorIdType').text('*');
            }else {
                $('#counsellorIdType').text('');
            }
        });
    });
</script>
