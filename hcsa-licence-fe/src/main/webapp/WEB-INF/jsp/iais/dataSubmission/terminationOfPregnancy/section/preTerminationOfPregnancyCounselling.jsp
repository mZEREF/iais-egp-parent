<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}"/>
<c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}"/>
<c:set var="patientInformationDto" value="${topSuperDataSubmissionDto.patientInformationDto}"/>
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
               <c:if test="${preTerminationDto.counsellingGiven == false}">checked</c:if>
               aria-invalid="false">
        <label class="form-check-label"
               for="counsellingNo"><span
                class="check-circle"></span>No</label>
    </div>
    </iais:value>
    </iais:row>
    <div id="noCounsReason"
         <c:if test="${preTerminationDto.counsellingGiven != false}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Reason for No Counselling" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="noCounsReason"
                            value="${preTerminationDto.noCounsReason}"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="counsellingGivenOnMin"
         <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek<13 || familyPlanDto.gestAgeBaseOnUltrWeek>24}">style="display: none"</c:if> >
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
    <div id="patientSign"
         <c:if test="${familyPlanDto.gestAgeBaseOnUltrWeek<13 || familyPlanDto.gestAgeBaseOnUltrWeek>24}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5"
                        value="Patient Sign the Acknowledgement For Counselling On Mid-Trimester Pregnancy Termination"
                        mandatory="true"/>
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
    <c:set var="toolMsg">
        <iais:message key="DS_MSG014" paramKeys="1" paramValues="counsellor"/>
    </c:set>
        <iais:field width="5" id="counsellorIdTypeLabel" value="Pre-Termination Counsellor ID Type"
                    mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }"
                    info="${toolMsg}"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                     value="${preTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
    </iais:value>

    </iais:row>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG018" escape="false" paramKeys="1" paramValues="counsellor"/></c:set>
        <iais:field width="5" id="counsellorIdNoLabel" value="Pre-Termination Counsellor ID No."
                    mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }" info="${toolMsg}"/>
    <iais:value width="7" cssClass="col-md-7">
        <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${preTerminationDto.counsellorIdNo}"/>
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
        <iais:input maxLength="66" type="text" name="counsellorName" value="${preTerminationDto.counsellorName}"/>
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
        <iais:select name="counsellingPlace" options="CounsellingPlace" value="${preTerminationDto.counsellingPlace}"
                     cssClass="counsellingPlace"/>
    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
    </iais:value>
    </iais:row>

    <div id="preCounsNoCondReasons" <c:if test="${preTerminationDto.counsellingGiven != false}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Reason why pre-Counselling was Not Conducted at HPB Counselling Centre" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="preCounsNoCondReason"
                            value="${preTerminationDto.preCounsNoCondReason}"/>
            </iais:value>
        </iais:row>
    </div>
    <iais:row>
    <label class="col-xs-5 col-md-4 control-label">First Pre-Counselling Result
        <span id="counsellingResult" class="mandatory">
                    <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
                </span>
    </label>
    <iais:value width="7" cssClass="col-md-7">
        <iais:select name="counsellingResult" firstOption="Please Select" codeCategory="TOP_CONSULTATION_RESULTS"
                     value="${preTerminationDto.counsellingResult}" id="counsellingResults"
                     cssClass="counsellingResult"/>
    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingResult"></span>
    </iais:value>
    </iais:row>
        <div id="patientAppointments" <c:if test="${preTerminationDto.counsellingGiven != true || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>">
            <iais:row>
                <iais:field width="5" value="Did Patient Make Appointment for Additional Pre-Counselling Sessions?" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="patientAppointment" firstOption="Please Select" id="patientAppointment" codeCategory="CATE_ID_BSB_GAZETTED_AREA"
                         value="${preTerminationDto.patientAppointment}" cssClass="preTerminationDto.patientAppointment"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_patientAppointment"></span>
                </iais:value>
            </iais:row>
        </div>
        <iais:row>
            <c:set var="toolMsg"><iais:message key="DS_MSG017" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" id="secCounsellingDateLabel" value="Date of Second or Final Pre-Counselling"
                        mandatory="${preTerminationDto.patientAppointment != true ? false : preTerminationDto.patientAppointment }"
                        info="${toolMsg}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker name="secCounsellingDate" value="${preTerminationDto.secCounsellingDate}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingDate"></span>
            </iais:value>
        </iais:row>
        <iais:row>
            <c:set var="toolMsg"><iais:message key="DS_MSG016" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" id="secCounsellingResultLabel" value="Second or Final Pre-Counselling result"
                        mandatory="${preTerminationDto.patientAppointment != true ? false : preTerminationDto.patientAppointment }"
                        info="${toolMsg}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="secCounsellingResult" firstOption="Please Select"
                             codeCategory="TOP_FINAL_PRE_COUNSELLING_RESULT"
                             value="${preTerminationDto.secCounsellingResult}" cssClass="secCounsellingResult"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingResult"></span>
            </iais:value>
        </iais:row>
    </div>
    <script>
        $(document).ready(function () {
            $('#counsellingNo').change(function () {
                counsellingNo();
            });
            $('#counsellingResults,input[name=counsellingGiven]').change(function () {
                counselling();
            });
            $('#counsellingResults').change(function () {
                counsellingResults();
            });
        });

        function counsellingNo() {
            var counsellingNo = $('#counsellingNo').val();

            if (counsellingNo == false) {
                $('#noCounsReason').show();
            } else {
                $('#noCounsReason').hide();
            }
        }

        function counsellingResults() {
            var counsellingResults = $('#counsellingResults').val();

            if (counsellingResults == "TOPPCR003") {
                $('#firstHide').hide();
            } else {
                $('#firstHide').show();
            }
        }

        $(document).ready(function () {
            $('input[name=counsellingGiven]').change(function () {
                if ($('#counsellingYes').prop('checked')) {
                    $('#counsellorName').text('*');
                    $('#counsellingDate').text('*');
                    $('#counsellingPlace').text('*');
                    $('#counsellingResult').text('*');
                }
                if ($('#counsellingNo').prop('checked')) {
                    $('#counsellorName').text('');
                    $('#counsellingDate').text('');
                    $('#counsellingPlace').text('');
                    $('#counsellingResult').text('');
                }
                checkMantory('#counsellingYes', "#counsellorIdTypeLabel");
                checkMantory('#counsellingYes', "#counsellorIdNoLabel");
            });
        });
        $(document).ready(function () {
            $('input[name=patientAppointment]').change(function () {
                checkMantory('#patientAppointmentYes', "#secCounsellingResultLabel");
                checkMantory('#patientAppointmentYes', "#secCounsellingDateLabel");

            });
        });
        $(document).ready(function () {
            $('input[name=counsellingGiven]').change(function () {
                if ($('#counsellingNo').prop('checked')) {
                    $('#noCounsReason').show();
                }
                if ($('#counsellingYes').prop('checked')) {
                    $('#noCounsReason').hide();
                }
            });
        });

        function counselling() {
            var counsellingResults = $('#counsellingResults').val();
            if ($('#counsellingYes').prop('checked') && counsellingResults == "TOPPCR001") {
                $('#patientAppointments').show();
            }else {
                $('#patientAppointments').hide();
            }
        }
    </script>
