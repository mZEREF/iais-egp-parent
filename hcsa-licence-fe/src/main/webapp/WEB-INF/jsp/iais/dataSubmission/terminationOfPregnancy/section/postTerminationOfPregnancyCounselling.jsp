<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="postTerminationDto" value="${terminationOfPregnancyDto.postTerminationDto}" />
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Whether given Post-counselling" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="givenPostCounselling"
                       value="1"
                       id="radioYes"
                       <c:if test="${postTerminationDto.givenPostCounselling == true}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="radioYes"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_givenPostCounselling"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="givenPostCounselling"
                       value="0"
                       id="radioNo"
                       <c:if test="${postTerminationDto.givenPostCounselling == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="radioNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">If post-counselling was not given
            <span id="ifCounsellingNotGiven" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="ifCounsellingNotGiven" firstOption="Please Select" codeCategory=""
                         value="${postTerminationDto.ifCounsellingNotGiven}" cssClass="ifCounsellingNotGiven"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_ifCounsellingNotGiven"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post-Termination Counsellor ID Type
            <span id="counsellorIdType" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${postTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post-Termination Counsellor ID No.
            <span id="counsellorIdNo" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${postTerminationDto.counsellorIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post Termination Counsellor Name
            <span id="counsellorName" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellorName" value="${postTerminationDto.counsellorName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Post-Counselling Date
            <span id="counsellingDate" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="counsellingDate" value="${postTerminationDto.counsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Place of Post-Counselling
            <span id="counsellingPlace" class="mandatory">
                <c:if test="${postTerminationDto.givenPostCounselling == true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellingPlace" value="${postTerminationDto.counsellingPlace}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
        </iais:value>
    </iais:row>
</div>
<script>
    $(document).ready(function () {
        $('input[name=givenPostCounselling]').change(function () {
            if($('#radioYes').prop('checked')) {
                $('#ifCounsellingNotGiven').text('*');
                $('#counsellorIdType').text('*');
                $('#counsellorIdNo').text('*');
                $('#counsellorName').text('*');
                $('#counsellingDate').text('*');
                $('#counsellingPlace').text('*');
            }
            if($('#radioNo').prop('checked')) {
                $('#ifCounsellingNotGiven').text('');
                $('#counsellorIdType').text('');
                $('#counsellorIdNo').text('');
                $('#counsellorName').text('');
                $('#counsellingDate').text('');
                $('#counsellingPlace').text('');
            }
        });
    });
</script>