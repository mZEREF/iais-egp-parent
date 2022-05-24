<c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}"/>
<c:set var="familyPlanDto" value="${terminationOfPregnancyDto.familyPlanDto}"/>
<c:set var="patientInformationDto" value="${terminationOfPregnancyDto.patientInformationDto}"/>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<div class="form-horizontal patientPatails">
    <iais:row>
        <iais:field width="5" value="Whether Given Counselling" mandatory="true"/>
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
        <div id="numCounsellingGiven" <c:if test="${preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
            <iais:row>
                <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="counsellor"/></c:set>
                <iais:field width="5" id="counsellorIdTypeLabel" value="Counsellor ID Type" mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }" info="${toolMsg}"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${preTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <%--<c:set var="toolMsg"><iais:message key="DS_MSG018" escape="false" paramKeys="1" paramValues="counsellor"/></c:set>--%>
                <iais:field width="5" id="counsellorIdNoLabel" value="Counsellor ID No." mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="20" type="text" name="counsellorIdNo" value="${preTerminationDto.counsellorIdNo}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdNo"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <label class="col-xs-5 col-md-4 control-label">Name of Counsellor
                    <span id="counsellorName" class="mandatory">
                        <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
                    </span>
                </label>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="counsellorName" value="${preTerminationDto.counsellorName}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <c:set var="toolMsg"><iais:message key="DS_MSG014" escape="false" paramKeys="1" paramValues="patient"/></c:set>
                <iais:field width="5" value="Doctor's Professional Reign / MRC No." info="${toolMsg}" style="padding-right: 0px;"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="20" type="text" name="counsellingReignNo" value="${preTerminationDto.counsellingReignNo}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <label class="col-xs-5 col-md-4 control-label">Date of Counselling
                    <span id="counsellingDate" class="mandatory">
                        <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
                    </span>
                </label>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:datePicker name="counsellingDate" id="counsellingGivenDate" value="${preTerminationDto.counsellingDate}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <label class="col-xs-5 col-md-4 control-label">Place Where Counselling Was Done
                    <span id="counsellingPlace" class="mandatory">
                <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
            </span>
                </label>
                <iais:value width="7" cssClass="col-md-7">
                    <%--<iais:select name="counsellingPlace" firstOption="Please Select" codeCategory="TOP_PRE_COUNSELLING_PLACE"
                                value="${preTerminationDto.counsellingPlace}" cssClass="counsellingPlace"/>--%>
                    <iais:select name="counsellingPlace" options="CounsellingPlace" value="${preTerminationDto.counsellingPlace}"  id="counsellingPlaces" cssClass="counsellingPlace"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <label class="col-xs-5 col-md-4 control-label">Result of First Counselling
                    <span id="counsellingResult" class="mandatory">
                        <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
                    </span>
                </label>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="counsellingResult" firstOption="Please Select" codeCategory="TOP_CONSULTATION_RESULTS" value="${preTerminationDto.counsellingResult}" id="counsellingResults" cssClass="counsellingResult"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingResult"></span>
                </iais:value>
            </iais:row>
        </div>
        <iais:row>
            <iais:field width="5" value="Patient Age (Years)"/>
            <iais:value width="7" cssClass="col-md-7" display="true" id="age">
                ${patientInformationDto.patientAge}
        </iais:value>
        </iais:row>
            <div id="preCounsNoCondReasons" <c:if test="${preTerminationDto.counsellingGiven != true || patientInformationDto.patientAge>=16 || patientInformationDto.maritalStatus =='TOPMS002' || preTerminationDto.counsellingPlace == 'AR_SC_001'}">style="display: none"</c:if> >
                <iais:row>
                    <iais:field width="5" value="Reason why pre-Counselling was Not Conducted at HPB Counselling Centre" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" name="preCounsNoCondReason" value="${preTerminationDto.preCounsNoCondReason}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_preCounsNoCondReason"></span>
                    </iais:value>
                </iais:row>
            </div>
        <div id="patientAppointments" <c:if test="${preTerminationDto.counsellingGiven != true || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Did Patient Make Appointment for Additional Pre-Counselling Sessions?" mandatory="true"/>
                <%--<iais:value width="7" cssClass="col-md-7">
                    <iais:select name="patientAppointment" firstOption="Please Select" id="secCounsellings" codeCategory="CATE_ID_BSB_GAZETTED_AREA"
                         value="${preTerminationDto.patientAppointment}" cssClass="patientAppointment"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_patientAppointment"></span>
                </iais:value>--%>
                <%--<div class="col-sm-7 col-md-5 col-xs-7 col-md-7">
                    <select name="patientAppointment" id="secCounsellings" class="patientAppointment" style="display: none;">
                        <option value="">Please Select</option>
                        <option value="Yes" <c:if test="${preTerminationDto.patientAppointment eq 'Yes'}">selected = 'selected'</c:if>>Yes</option>
                        <option value="No" <c:if test="${preTerminationDto.patientAppointment eq 'No'}">selected = 'selected'</c:if>>No</option>
                    </select>
                    <div class="nice-select patientAppointment" tabindex="0">
                        <span class="current">Please Select</span>
                        <ul class="list">
                            <li data-value="" class="option selected">Please Select</li>
                            <li data-value="Yes" class="option">Yes</li>
                            <li data-value="No" class="option">No</li>
                        </ul>
                    </div>
                    <span class="error-msg" name="iaisErrorMsg" id="error_patientAppointment"></span>
                </div>--%>
                <div class="col-sm-7 col-md-7 col-xs-10" style="width: 930px;">
                    <select name="patientAppointment" id="secCounsellings" class="patientAppointment">
                        <option value="">Please Select</option>
                        <option value="Yes" <c:if test="${preTerminationDto.patientAppointment eq 'Yes'}">selected = 'selected'</c:if>>Yes</option>
                        <option value="No" <c:if test="${preTerminationDto.patientAppointment eq 'No'}">selected = 'selected'</c:if>>No</option>
                    </select>
                    <span class="error-msg" name="iaisErrorMsg" id="error_patientAppointment"></span>
                </div>
            </iais:row>
        </div>
    <div id="secCounsellingDates" <c:if test="${preTerminationDto.patientAppointment!='Yes' || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
        <iais:row>
            <c:set var="toolMsg"><iais:message key="DS_MSG017" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" id="secCounsellingDateLabel" style="padding-right: 50px;" value="Date of Second or Final Pre-Counselling" mandatory="true" info="${toolMsg}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker name="secCounsellingDate" id="secCounsellingDateDate" value="${preTerminationDto.secCounsellingDate}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingDate"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="secCounsellingResults" <c:if test="${preTerminationDto.patientAppointment!='Yes' || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
        <iais:row>
            <c:set var="toolMsg"><iais:message key="DS_MSG016" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" id="secCounsellingResultLabel" style="padding-right: 50px;" value="Result of Second or Final Counselling" mandatory="true" info="${toolMsg}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="secCounsellingResult" firstOption="Please Select" id="secCounsellingResult" codeCategory="TOP_FINAL_PRE_COUNSELLING_RESULT" value="${preTerminationDto.secCounsellingResult}" cssClass="secCounsellingResult"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingResult"></span>
            </iais:value>
        </iais:row>
    </div>
<input type="hidden" id="maritalStatus" value="${patientInformationDto.maritalStatus}"/>
<input type="hidden" id="patientAge" value="${patientInformationDto.patientAge}"/>
<input type="hidden" id="birthData" value="${patientInformationDto.birthData}"/>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT" >
        <div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-body" >
                        <div class="row">
                            <div class="col-md-12">
                                <span style="font-size: 2rem;" id="prsErrorMsg">
                                    <iais:message key="The patient age to the date of counselling is within the range of <=10 or >=65. Please check that the details have been accurately entered." escape="false" />
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                        <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancels()">CLOSE</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="hpbConsult" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-body" >
                        <div class="row">
                            <div class="col-md-12">
                                <span style="font-size: 2rem;" id="ages">
                                    <iais:message key="please go to HPB for consultation." escape="false" />
                                </span>
                            </div>
                        </div>
                    </div>
                    <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                        <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancels()">CLOSE</button>
                    </div>
                </div>
            </div>
        </div>
        <script>
    $(document).ready(function () {
        var patientAge = $('#patientAge').val();
        if(patientAge<16){
            $('#hpbConsult').modal('show');
        }

        $('#counsellingNo').change(function () {
            counsellingNo();
        });
        $('#counsellingResults,input[name=counsellingGiven]').change(function () {
            counselling();
        });
        $('#counsellingPlaces,input[name=counsellingGiven]').change(function () {
            counsellingPlace();
        });
        $('#counsellingResults').change(function () {
            counsellingResults();
        });
        $('#secCounsellings').change(function () {
            secCounsellingDate();
            secCounsellingResult();
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

        if (counsellingResults == "TOPPCR004") {
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
        $('input[name=counsellingGiven]').change(function () {
            if ($('#counsellingNo').prop('checked')) {
                $('#noCounsReason').show();
                $('#numCounsellingGiven').hide();
                fillValue($('#counsellingPlaces'),null);
            }
            if ($('#counsellingYes').prop('checked')) {
                $('#noCounsReason').hide();
                $('#numCounsellingGiven').show();
            }
        });
    });

    function counselling() {
        var counsellingResults = $('#counsellingResults').val();
        if ($('#counsellingYes').prop('checked') && counsellingResults == "TOPPCR001") {
            $('#patientAppointments').show();
        }else {
            $('#patientAppointments').hide();
            $('#secCounsellingDates').hide();
            $('#secCounsellingResults').hide();
            fillValue($('#patientAppointments'),null);
            $('#secCounsellingDateDate').val(null);
            fillValue($('#secCounsellingResults'),null);
        }
    }
    function counsellingPlace() {
        var counsellingPlace = $('#counsellingPlaces').val();
        var maritalStatus = $('#maritalStatus').val();
        var patientAge = $('#patientAge').val();
        if($('#counsellingYes').prop('checked')){
            console.log("true");
            if (counsellingPlace == "AR_SC_001" || maritalStatus =='TOPMS002' || patientAge>=16) {
                $('#preCounsNoCondReasons').hide();
            }else {
                console.log("1");
                $('#preCounsNoCondReasons').show();
            }
        }else {
            $('#preCounsNoCondReasons').hide();
        }
    }

    function secCounsellingDate(){
        var patientAppointment = $('#secCounsellings').val();
        if (patientAppointment =="Yes") {
            $('#secCounsellingDates').show();
        }else {
            $('#secCounsellingDates').hide();
            $('#secCounsellingDateDate').val(null);

        }
    }
    function secCounsellingResult(){
        var patientAppointment = $('#secCounsellings').val();
        if (patientAppointment =="Yes") {
            $('#secCounsellingResults').show();
        }else {
            $('#secCounsellingResults').hide();
            fillValue($('#secCounsellingResult'),null);
        }
    }

    $("#counsellingGivenDate").on('blur, change', function () {
        showWaiting();
        var birthData=$('#birthData').val();
        var counsellingGiven=$('#counsellingGivenDate').val();
        var url = $('#_contextPath').val() + '/top/patient-age';
        var options = {
            birthData: birthData,
            counsellingGiven: counsellingGiven,
            url: url
        }
        callCommonAjax(options, checkBirthDateCallback);
    });

    function checkBirthDateCallback(data) {
        if (isEmpty(data) || isEmpty(data.showAge) || !data.showAge) {
            return;
        }
        $('#PRS_SERVICE_DOWN').modal('show');
    }

    function cancels() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }
</script>
