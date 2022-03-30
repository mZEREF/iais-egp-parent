<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}" />
<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}" />
<c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}" />
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
                       <c:if test="${preTerminationDto.counsellingGiven == true}">checked</c:if>
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
                       <c:if test="${preTerminationDto.counsellingGiven == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="counsellingNo"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <div id="noCounsReason" <c:if test="${preTerminationDto.counsellingGiven != false}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Reason for No Counselling" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="noCounsReason" value="${preTerminationDto.noCounsReason}" />
            </iais:value>
        </iais:row>
    </div>
    <div id="noCounsReason" <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek<13 && familyPlanDto.gestAgeBaseOnUltrWeek>24}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Given Counselling On Mid-Trimester Pregnancy Termination" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="counsellingGivenOnMin"
                           value="1"
                           id="pregnancyYes"
                           <c:if test="${preTerminationDto.counsellingGivenOnMin== true}">checked</c:if>
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
                           <c:if test="${preTerminationDto.counsellingGivenOnMin == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="pregnancyNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
    </div>
    <div id="noCounsReason" <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek<13 && familyPlanDto.gestAgeBaseOnUltrWeek>24}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Patient Sign the Acknowledgement For Counselling On Mid-Trimester Pregnancy Termination" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="patientSign"
                           value="1"
                           id="acknowledgementYes"
                           <c:if test="${preTerminationDto.patientSign == true}">checked</c:if>
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
                           <c:if test="${preTerminationDto.patientSign == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="acknowledgementNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="counsellor"/></c:set>
        <iais:field width="5" id="counsellorIdTypeLabel" value="Pre-Termination Counsellor ID Type"
                    mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }" info="${toolMsg}"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${preTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
        </iais:value>

    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Pre-Termination Counsellor ID No.
            <span id="counsellorIdNo" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${preTerminationDto.counsellorIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Pre-Termination Counsellor Name
            <span id="counsellorName" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="counsellorName" value="${preTerminationDto.counsellorName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Pre-Termination Counselling Date
            <span id="counsellingDate" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="counsellingDate" value="${preTerminationDto.counsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Pre-Counselling Place
            <span id="counsellingPlace" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <%--<iais:select name="counsellingPlace" firstOption="Please Select" codeCategory="TOP_PRE_COUNSELLING_PLACE"
                         value="${preTerminationDto.counsellingPlace}" cssClass="counsellingPlace"/>--%>
            <iais:select name="counsellingPlace" options="CounsellingPlace"  value="${preTerminationDto.counsellingPlace}"
                         cssClass="counsellingPlace"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Reason why pre-Counselling was Not Conducted at HPB Counselling Centre" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" name="preCounsNoCondReason" value="${preTerminationDto.preCounsNoCondReason}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">First Pre-Counselling Result
            <span id="counsellingResult" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="counsellingResult" firstOption="Please Select" codeCategory="TOP_CONSULTATION_RESULTS"
                         value="${preTerminationDto.counsellingResult}" cssClass="counsellingResult"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_counsellingResult"></span>
        </iais:value>
    </iais:row>
    <div id="patientAppointments" style="${familyPlanDto.subRopReason ==null || preTerminationDto.counsellingGiven == true && !preTerminationDto.counsellingResults eq 'TOPPCR001' ? 'display: none' : ''}" >
        <iais:row>
            <%--<label class="col-xs-5 col-md-4 control-label">Did Patient Make Appointment for Additional Pre-Counselling Sessions?
                <span id="patientAppointment" class="mandatory">
                    <c:if test="${preTerminationDto.counsellingResult =='TOPPCR001'}">*</c:if>
                </span>
            </label>--%>
            <iais:field width="5" value="Did Patient Make Appointment for Additional Pre-Counselling Sessions?" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="patientAppointment"
                           value="1"
                           id="patientAppointmentYes"
                           <c:if test="${preTerminationDto.patientAppointment== true}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="patientAppointmentYes"><span
                            class="check-circle"></span>Yes</label>
                </div>
                <span class="error-msg" name="iaisErrorMsg" id="error_patientAppointment"></span>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="patientAppointment"
                           value="0"
                           id="patientAppointmentNo"
                           <c:if test="${preTerminationDto.patientAppointment == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="patientAppointmentNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Date of Second or Final Pre-Counselling
            <span id="secCounsellingDate" class="mandatory">
                <c:if test="${preTerminationDto.patientAppointment== true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="secCounsellingDate" value="${preTerminationDto.secCounsellingDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingDate"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Second or Final Pre-Counselling result
            <span id="secCounsellingResult" class="mandatory">
                <c:if test="${preTerminationDto.patientAppointment ==true}">*</c:if>
            </span>
        </label>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="secCounsellingResult" firstOption="Please Select" codeCategory="TOP_FINAL_PRE_COUNSELLING_RESULT"
                         value="${preTerminationDto.secCounsellingResult}" cssClass="secCounsellingResult"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingResult"></span>
        </iais:value>
    </iais:row>
</div>
<script>
    $(document).ready(function() {
        $('#counsellingNo').change(function () {
            counsellingNo();
        });
        $('[name=counsellingResult]').change(function () {
            counsellingResult();
        });
    });

    function counsellingNo() {
        var counsellingNo= $('#counsellingNo').val();

        if(counsellingNo == false){
            $('#noCounsReason').show();
        }else {
            $('#noCounsReason').hide();
        }
    }
    $(document).ready(function () {
        $('input[name=counsellingGiven]').change(function () {
            if($('#counsellingYes').prop('checked')) {
                $('#counsellorIdNo').text('*');
                $('#counsellorName').text('*');
                $('#counsellingDate').text('*');
                $('#counsellingPlace').text('*');
                $('#counsellingResult').text('*');
            }
            if($('#counsellingNo').prop('checked')) {
                $('#counsellorIdNo').text('');
                $('#counsellorName').text('');
                $('#counsellingDate').text('');
                $('#counsellingPlace').text('');
                $('#counsellingResult').text('');
            }
            checkMantory('#counsellingYes', "#counsellorIdTypeLabel");
        });
    });
    /*$(document).ready(function () {
        $('input[name=counsellingGiven]').change(function () {
            if($('#counsellingNo').prop('checked')) {
                $('#noCounsReason').text('*');
            }
            if($('#counsellingYes').prop('checked')) {
                $('#noCounsReason').text('');
            }
        });
    });*/
    $(document).ready(function () {
        $('input[name=patientAppointment]').change(function () {
            if($('#patientAppointmentYes').prop('checked')) {
                $('#secCounsellingDate').text('*');
                $('#secCounsellingResult').text('*');
            }
            if($('#patientAppointmentNo').prop('checked')) {
                $('#secCounsellingDate').text('');
                $('#secCounsellingResult').text('');
            }
        });
    });
    $(document).ready(function () {
        $('input[name=counsellingGiven]').change(function () {
            if($('#counsellingYes').prop('checked')) {
                $('#noCounsReason').show();
            }
            if($('#counsellingNo').prop('checked')) {
                $('#noCounsReason').hide();
            }
        });
    });
    function counsellingResult() {
        var counsellingResults = $('[name=counsellingResult] option:selected').val();
        var counsellingNo = $('#counsellingNo').val();
        if (counsellingResults == "TOPPCR001" || counsellingNo == true) {
            $('#patientAppointments').show();
        } else {
            $('#patientAppointments').hide();
        }
    }
</script>
