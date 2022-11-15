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
         <c:if test="${ empty preTerminationDto.counsellingGiven || preTerminationDto.counsellingGiven != false}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Reason for No Counselling" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="100" type="text" name="noCounsReason"
                            value="${preTerminationDto.noCounsReason}"/>
            </iais:value>
        </iais:row>
    </div>
    <%
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = (TopSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, "topSuperDataSubmissionDto");
            FamilyPlanDto familyPlanDto =topSuperDataSubmissionDto.getTerminationOfPregnancyDto().getFamilyPlanDto();
            int weeksD = 0;
            int weeksU = 0;
            if (familyPlanDto != null) {
                int weeks = Integer.parseInt(familyPlanDto.getGestAgeBaseOnUltrWeek());
                BigDecimal b1 = new BigDecimal(familyPlanDto.getGestAgeBaseOnUltrDay());
                BigDecimal b2 = new BigDecimal(Integer.toString(7));
                weeksU = weeks + b1.divide(b2, 0, BigDecimal.ROUND_UP).intValue();
                weeksD = weeks + b1.divide(b2, 0, BigDecimal.ROUND_DOWN).intValue();
            }
    %>
        <div id="counsellingGivenOnMin" <%if (weeksD < 13 || weeksU > 24) {%>style="display: none"<%}%>>
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
         <%if (weeksD < 13 || weeksU > 24) {%>style="display: none"<%}%>>
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
    <div id="numCounsellingGiven"
         <c:if test="${preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
        <iais:row>
            <c:set var="toolMsg14"><iais:message key="DS_MSG014" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" id="counsellorIdTypeLabel" value="Counsellor ID Type"
                        mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }"
                        info="${toolMsg14}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="counsellorIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE_DTV"
                             value="${preTerminationDto.counsellorIdType}" cssClass="counsellorIdType"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_counsellorIdType"></span>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" id="counsellorIdNoLabel" value="Counsellor ID No."
                        mandatory="${preTerminationDto.counsellingGiven != true ? false : preTerminationDto.counsellingGiven }"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="20" type="text" name="counsellorIdNo"
                            value="${preTerminationDto.counsellorIdNo}"/>
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
                <iais:input maxLength="66" type="text" name="counsellorName"
                            value="${preTerminationDto.counsellorName}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_counsellorName"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="numCounsellingGivenDoc"
         <c:if test="${empty preTerminationDto.counsellingGiven ||preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
        <iais:row>
            <c:set var="toolMsgPre"><iais:message key="DS_MSG018" escape="false" paramKeys="1"
                                                  paramValues="patient"/></c:set>
            <iais:field width="5" value="Doctor's Professional Regn / MCR No." info="${toolMsgPre}"
                        style="padding-right: 0px;"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="20" type="text" name="counsellingReignNo"
                            value="${preTerminationDto.counsellingReignNo}"/>
            </iais:value>
        </iais:row>
    </div>
    <div id="numCounsellingGivens"
         <c:if test="${preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
        <iais:row>
            <label class="col-xs-5 col-md-4 control-label">Date of Counselling
                <span id="counsellingDate" class="mandatory">
                        <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
                    </span>
            </label>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker name="counsellingDate" id="counsellingGivenDate"
                                 value="${preTerminationDto.counsellingDate}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_counsellingDate"></span>
            </iais:value>
        </iais:row>
        <div id="counsellingPlaceAge"
             <c:if test="${preTerminationDto.counsellingAge < 16}">style="display: none"</c:if> >
            <iais:row id="counsellingPlaceRow">
                <iais:field width="5" value="Place Where Counselling Was Done" mandatory="true"/>
                <iais:value width="7" id="counsellingPlaceDiv" cssClass="col-md-7">
                    <%--<iais:select name="counsellingPlace" firstOption="Please Select" codeCategory="TOP_PRE_COUNSELLING_PLACE"
                                value="${preTerminationDto.counsellingPlace}" cssClass="counsellingPlace"/>--%>
                    <iais:select name="counsellingPlace" options="CounsellingPlace"
                                 value="${preTerminationDto.counsellingPlace}"
                                 id="counsellingPlaces" cssClass="counsellingPlace partial-search-container"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlace"></span>
                    <%--<iais:input maxLength="100" type="text" name="counsellingPlace" id="counsellingPlaceValue" value="${preTerminationDto.counsellingPlace}"/>--%>
                </iais:value>
            </iais:row>
        </div>
        <div id="counsellingPlaceAges"
             <c:if test="${preTerminationDto.counsellingAge == null || preTerminationDto.counsellingAge >= 16 }">style="display: none"</c:if> >
            <iais:row>
                <iais:field width="5" value="Place Where Counselling Was Done" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="counsellingPlaceAge" options="CounsellingPlacea"
                                 value="${preTerminationDto.counsellingPlace}"
                                 id="counsellingPlaceAgeSelect" cssClass="counsellingPlace partial-search-container"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_counsellingPlaceAge"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="preCounsNoCondReasons"
             <c:if test="${preTerminationDto.counsellingGiven != true || preTerminationDto.counsellingAge>=16 || patientInformationDto.maritalStatus =='TOPMS002' || preTerminationDto.counsellingPlace == 'AR_SC_001' || preTerminationDto.counsellingPlace ==null || preTerminationDto.counsellingAge ==null}">style="display: none"</c:if> >
            <iais:row>
                <iais:field width="5" value="Reason why Counselling was Not Conducted at HPB Counselling Centre"
                            mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="preCounsNoCondReason"
                                value="${preTerminationDto.preCounsNoCondReason}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_preCounsNoCondReason"></span>
                </iais:value>
            </iais:row>
        </div>
        <iais:row>
            <label class="col-xs-5 col-md-4 control-label">Result of Counselling
                <span id="counsellingResult" class="mandatory">
                        <c:if test="${preTerminationDto.counsellingGiven ==true}">*</c:if>
                    </span>
            </label>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="counsellingResult" firstOption="Please Select"
                             codeCategory="TOP_CONSULTATION_RESULTS"
                             value="${preTerminationDto.counsellingResult}" id="counsellingResults"
                             cssClass="counsellingResult"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_counsellingResult"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="patientAppointments"
         <c:if test="${preTerminationDto.counsellingGiven != true || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" value="Did Patient Make Appointment for Additional Counselling Sessions?"
                        mandatory="true"/>

            <div class="col-sm-7 col-md-7 col-xs-10" style="width: 930px;">
                <select name="patientAppointment" id="secCounsellings" class="patientAppointment">
                    <option value="">Please Select</option>
                    <option value="1"
                            <c:if test="${preTerminationDto.patientAppointment eq '1'}">selected='selected'</c:if>>Yes
                    </option>
                    <option value="0"
                            <c:if test="${preTerminationDto.patientAppointment eq '0'}">selected='selected'</c:if>>No
                    </option>
                </select>
                <span class="error-msg" name="iaisErrorMsg" id="error_patientAppointment"></span>
            </div>
        </iais:row>
    </div>
    <div id="secCounsellingDates"
         <c:if test="${preTerminationDto.patientAppointment!='1' || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
        <iais:row>
            <c:set var="toolMsg17"><iais:message key="DS_MSG017" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" id="secCounsellingDateLabel" style="padding-right: 50px;"
                        value="Date of Second or Final Counselling" mandatory="true" info="${toolMsg17}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker name="secCounsellingDate" id="secCounsellingDateDate"
                                 value="${preTerminationDto.secCounsellingDate}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingDate"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="secCounsellingResults"
         <c:if test="${preTerminationDto.patientAppointment!='1' || preTerminationDto.counsellingResult !='TOPPCR001'}">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="5" id="secCounsellingResultLabel" style="padding-right: 50px;"
                        value="Result of Second or Final Counselling" mandatory="true" />
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="secCounsellingResult" firstOption="Please Select" id="secCounsellingResult"
                             codeCategory="TOP_FINAL_PRE_COUNSELLING_RESULT"
                             value="${preTerminationDto.secCounsellingResult}" cssClass="secCounsellingResult"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_secCounsellingResult"></span>
            </iais:value>
        </iais:row>
    </div>
    <div id="numCounsellingGivenAge"
         <c:if test="${ empty preTerminationDto.counsellingGiven ||preTerminationDto.counsellingGiven != true}">style="display: none"</c:if> >
        <iais:row>
            <iais:field width="5" value="Patient Age (Years)"/>
            <iais:value width="7" cssClass="col-md-7" display="true" id="counsellingAge">
                ${preTerminationDto.counsellingAge}
            </iais:value>
        </iais:row>
    </div>
    <input type="hidden" id="maritalStatus" value="${patientInformationDto.maritalStatus}"/>
    <input type="hidden" id="patientAge" value="${patientInformationDto.patientAge}"/>
    <input type="hidden" id="birthData" value="${patientInformationDto.birthData}"/>
    <input type="hidden" id="counselling" name="counsellingAge" value="${preTerminationDto.counsellingAge}"/>
    <%--<%@include file="../common/topCounselling.jsp" %>--%>
    <input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT">
    <div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-12">
                                <span style="font-size: 2rem;" id="prsErrorMsg">
                                    <iais:message
                                            key="The patient age to the date of counselling is within the range of <=10 or >=65. Please check that the details have been accurately entered."
                                            escape="false"/>
                                </span>
                        </div>
                    </div>
                </div>
                <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                    <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                            data-dismiss="modal" onclick="cancels()">CLOSE
                    </button>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(document).ready(function () {
            $('#counsellingNo').change(function () {
                counsellingNo();
            });
            $('#counsellingResults').change(function () {
                counselling();
                counsellingResults();
            });
            $('#secCounsellings').change(function () {
                secCounsellingDate();
                secCounsellingResult();
            });
            changeDate();

            // Initialize select2
            $("#counsellingPlaces").select2({
                matcher: dropdownFilterShowMatchOnly
            });
            $("#counsellingPlaceAgeSelect").select2({
                matcher: dropdownFilterShowMatchOnly
            });
            $('.select2-container--default').attr('style', 'width:100%');
            $('.partial-search-container').hide();

            $('#counsellingPlaceAgeSelect').change(function () {
                counsellingPlace();
            });
            $('input[name=counsellingGiven]').change(function () {
                counselling();
                counsellingPlace();
                if ($('#counsellingNo').prop('checked')) {
                    $('#noCounsReason').show();
                    $('#numCounsellingGiven').hide();
                    $('#numCounsellingGivens').hide();
                    $('#numCounsellingGivenDoc').hide();
                    $('#numCounsellingGivenAge').hide();
                    $('#counsellorName').text('');
                    $('#counsellingDate').text('');
                    $('#counsellingResult').text('');
                    fillValue($('#counsellingPlaceAge'), null);
                }
                if ($('#counsellingYes').prop('checked')) {
                    $('#counsellorName').text('*');
                    $('#counsellingDate').text('*');
                    $('#counsellingResult').text('*');
                    $('#noCounsReason').hide();
                    $('#numCounsellingGiven').show();
                    $('#numCounsellingGivens').show();
                    $('#numCounsellingGivenDoc').show();
                    $('#numCounsellingGivenAge').show();
                }
                checkMantory('#counsellingYes', "#counsellorIdTypeLabel");
                checkMantory('#counsellingYes', "#counsellorIdNoLabel");
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


        function counselling() {
            var counsellingResults = $('#counsellingResults').val();
            if ($('#counsellingYes').prop('checked') && counsellingResults == "TOPPCR001") {
                $('#patientAppointments').show();
            } else {
                $('#patientAppointments').hide();
                $('#secCounsellingDates').hide();
                $('#secCounsellingResults').hide();
                fillValue($('#patientAppointments'), null);
                $('#secCounsellingDateDate').val(null);
                fillValue($('#secCounsellingResults'), null);
            }
        }

        function counsellingPlace() {
            var counsellingPlace = $('#counsellingPlaceAgeSelect option:selected').val();
            var maritalStatus = $('#maritalStatus').val();
            var patientAge = $('#counselling').val();
            if ($('#counsellingYes').prop('checked')) {
                console.log("true");
                console.log(counsellingPlace);
                if (counsellingPlace != null && counsellingPlace != '' && counsellingPlace != "AR_SC_001" && maritalStatus != 'TOPMS002' && patientAge < 16) {
                    $('#preCounsNoCondReasons').show();
                } else {
                    console.log("1");
                    $('#preCounsNoCondReasons').hide();
                }
            } else {
                $('#preCounsNoCondReasons').hide();
            }
        }

        function secCounsellingDate() {
            var patientAppointment = $('#secCounsellings').val();
            if (patientAppointment == "1") {
                $('#secCounsellingDates').show();
            } else {
                $('#secCounsellingDates').hide();
                $('#secCounsellingDateDate').val(null);

            }
        }

        function secCounsellingResult() {
            var patientAppointment = $('#secCounsellings').val();
            if (patientAppointment == "1") {
                $('#secCounsellingResults').show();
            } else {
                $('#secCounsellingResults').hide();
                fillValue($('#secCounsellingResult'), null);
            }
        }

        function changeDate() {
            var birthData = $('#birthData').val();
            var counsellingGiven = $('#counsellingGivenDate').val();

            let reg = /^(0?[1-9]|([1-2][0-9])|30|31)\/(1[0-2]|0?[1-9])\/(\d{4})$/;
            let validC = reg.test(counsellingGiven);
            let validB = reg.test(birthData);
            console.log("validCounsellingGiven: " + validC);
            console.log("validBirthData: " + validB);
            if (validC && validB) {
                showWaiting();
                var url = $('#_contextPath').val() + '/top/counselling-age';
                var options = {
                    birthData: birthData,
                    counsellingGiven: counsellingGiven,
                    url: url
                }
                callCommonAjax(options, checkBirthDateCallbacks);
            }
        }

        $("#counsellingGivenDate").change(function () {
            changeDate();
        });


        function cancels() {
            $('#PRS_SERVICE_DOWN').modal('hide');
        }

        function checkBirthDateCallbacks(data) {
            if (isEmpty(data)) {
                return;
            }
            if (data.selection.counsellingAge <= 10 || data.selection.counsellingAge >= 65) {
                $('#PRS_SERVICE_DOWN').modal('show');
            }

            var counsellingPlace = $('select[name="counsellingPlaceAge"]').val();
            var maritalStatus = $('#maritalStatus').val();
            if (counsellingPlace != null && counsellingPlace != '' && counsellingPlace != "AR_SC_001" && maritalStatus != 'TOPMS002' && data.selection.counsellingAge < 16) {
                $('#preCounsNoCondReasons').show();
            }
            console.log("counselling");
            if (isEmpty(data) || data.birthDate) {
                $('#counsellingAge').html(null);
                return;
            }
            console.log("counsellingAge");
            $('#counsellingAge').html(data.selection.counsellingAge);
            $('#counselling').val(data.selection.counsellingAge);
            if (data.selection.counsellingAge < 16) {
                $('#counsellingPlaceAge').hide();
                $('#counsellingPlaceAges').show();
            } else {
                $('#counsellingPlaceAges').hide();
                $('#counsellingPlaceAge').show();

            }
        }

        /*function age(){
            var counsellingAge = $('#counselling').val();

        }*/
        /*function checkDate(){
            var counsellingGivenDate = $("#counsellingGivenDate").val();
            var counsellingPlace = $("#counsellingPlaces").val();
            if(counsellingGivenDate != "" || counsellingPlace != "" ){
                var data = {
                    'counsellingGivenDate':counsellingGivenDate,
                    'counsellingPlace':counsellingPlace
                };
                showWaiting();
                $.ajax({
                    'url':$('#_contextPath').val()+'/top/check-date',
                    'dataType':'json',
                    'data':data,
                    'type':'POST',
                    'success':function (data) {
                        $("#counsellingPlaceDiv").html(data.resultJson + '');
                        $("#counsellingPlaces").niceSelect();
                    },
                    'error':function () {
                    }
                });
                dismissWaiting();
            }
        }*/
    </script>
