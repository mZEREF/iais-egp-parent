<%--<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/dp_drugSubmission.js"></script>--%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<div id="flagDocMessage" hidden><iais:message key="GENERAL_ERR0042"/> </div>
<div id="flagInvaMessage" hidden><iais:message key="GENERAL_ERR0057"/> </div>
<div id="flagPrnMessage" hidden><iais:message key="GENERAL_ERR0054"/> </div>
<c:set var="doctorInformationDto" value="${dpSuperDataSubmissionDto.doctorInformationDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Submission Details
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <input type="hidden" name="docSource" value="DRP"/>
                <c:set var="suffix" value="" />
                <c:set var="drug" value="${drugSubmission}"/>
                <div class="patient">
                    <iais:row>
                        <iais:field width="5" value="Patient's ID No." mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE_DTV" value="${drugSubmission.idType}"
                                         cssClass="idTypeSel" onchange="clearSelection()"/>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-4">
                            <iais:input maxLength="20" type="text" name="idNumber" value="${drugSubmission.idNumber}" onchange="clearSelection()"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Nationality" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" style="width: 232px;">
                        <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${drugSubmission.nationality}" cssClass="nationalitySel" onchange="clearSelection()"/>
                    </iais:value>
                    <iais:value width="2" cssClass="col-md-3 patientData" display="true" style="width: 330px;" id="retrieveDataDiv">
                        <a class="retrieveIdentification" onclick="retrieveValidateDrug()">
                            Validate Patient
                        </a>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Patient's Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="name">
                        ${drugSubmission.name}
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Doctor's Professional Registration No." mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-5" >
                        <iais:input maxLength="20" type="text" id="doctorRegnNo" name="doctorReignNo" value="${drugSubmission.doctorReignNo}" onchange="clearDockerSelection();"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_doctorReignNo"></span>
                        <span id="doctorRegnNoMsg" name="iaisErrorMsg" class="error-msg"></span>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3" display="true">
                        <a class="ValidateDoctor" onclick="validateDoctors()">
                            Validate Doctor
                        </a>
                    </iais:value>
                </iais:row>
                <div id="doctorInformation" <c:if test="${drugSubmission.doctorInformations eq 'true'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Doctor's Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="names">
                            ${drugSubmission.doctorName}
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="specialty">
                            ${drugSubmission.specialty}
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Sub-Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="subSpecialty">
                            ${drugSubmission.subSpecialty}
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Qualification"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="qualification">
                            ${drugSubmission.qualification}
                        </iais:value>
                    </iais:row>
                </div>
                <div id="doctorInformationText" <c:if test="${drugSubmission.doctorInformations eq 'false' || drugSubmission.doctorInformations eq null}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Doctor's Name" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:input maxLength="66" type="text" name="dName" value="${doctorInformationDto.name}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_dName"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Specialty" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:input maxLength="100" type="text" name="dSpeciality" value="${doctorInformationDto.speciality}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_dSpeciality"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Sub-Specialty" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:input maxLength="100" type="text" name="dSubSpeciality" value="${doctorInformationDto.subSpeciality}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_dSubSpeciality"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Qualification" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:input maxLength="100" type="text" name="dQualification" value="${doctorInformationDto.qualification}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_dQualification"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Other Qualification" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="100" type="text" id ="otherQualification" name="otherQualification"
                                    value="${drugSubmission.otherQualification}" />
                    </iais:value>
                </iais:row>
              <div class="drugType">
                <iais:row>
                    <iais:field width="5" value="Drug Prescribed or Dispensed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="drugType" id="drugType" name="drugType" firstOption="Please Select" codeCategory="DP_DRUG_PRESCRIBED_OR_DISPENSED"
                                     value="${drugSubmission.drugType}"/>
                    </iais:value>
                </iais:row>
                </div>
                <div  id="prescriptionDate" <c:if test="${drugSubmission.drugType!='DPD001'}">style="display: none"</c:if> >
                <iais:row>
                    <iais:field width="5" value="Date of Prescription" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="prescriptionDate" value="${drugSubmission.prescriptionDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_prescriptionDate"></span>
                    </iais:value>
                </iais:row>
                </div>
                <div  id="dispensingDate" <c:if test="${drugSubmission.drugType!='DPD002'}">style="display: none"</c:if> >
                    <iais:row>
                        <c:set var="toolMsg"><iais:message key="DS_MSG026"/></c:set>
                        <iais:field width="5" value="Prescription Submission ID" info="${toolMsg}" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="16" type="text" id ="prescriptionSubmissionId" name="prescriptionSubmissionId"
                                        value="${drugSubmission.prescriptionSubmissionId}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_prescriptionSubmissionId"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Start Date of Dispensing" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker name="dispensingDate" id="startDispensingDate" value="${drugSubmission.dispensingDate}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_dispensingDate"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div class="medication">
                    <iais:row>
                        <iais:field width="5" value="Medication" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="medication" id = "medication" name="medication"
                                         firstOption="Please Select" codeCategory="DP_MEDICATION" value="${drugSubmission.medication}"/>
                        </iais:value>
                    </iais:row>
                </div>
               <%-- <iais:row>
                    <iais:field width="5" value="Prescribing Duration Start Date" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="startDate" value="${drugSubmission.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>--%>
                <div  id="ddEndDate" <c:if test="${drugSubmission.drugType!='DPD002'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="End Date of Dispensing" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:datePicker name="endDate" value="${drugSubmission.endDate}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="diagnosi" <c:if test="${drugSubmission.drugType!='DPD001'}">style="display: none"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Diagnosis" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <textarea rows="" maxlength="1000" style="width: 100%;overflow: auto;word-break: break-all;" name="diagnosis">${drugSubmission.diagnosis}</textarea>
                            <span id="error_diagnosis" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="urineTest" <c:if test="${drugSubmission.drugType!='DPD002'|| drugSubmission.medication !='MED002'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Urine Test Type" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="urineTestType" id = "urineTestType" name="urineTestType"
                                         firstOption="Please Select" codeCategory="DP_URINETESTTYPE" value="${drugSubmission.urineTestType}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Urine Test Result" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select cssClass="urineTestResult" id = "urineTestResult" name="urineTestResult"
                                         firstOption="Please Select" codeCategory="DP_URINETESTRESULT" value="${drugSubmission.urineTestResult}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="nurse" <c:if test="${drugSubmission.medication != 'MED001'}">style="display: none;"</c:if> >
                    <iais:row>
                        <iais:field width="5" value="Nurse/Pharmacist's Registration No." mandatory="true" />
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="20" type="text" id ="nurseRegistrationNo" name="nurseRegistrationNo"
                                        value="${drugSubmission.nurseRegistrationNo}" />
                            <span id="error_nurseRegistrationNo" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Nurse/Pharmacist's Name"  mandatory="true" />
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="66" type="text" id ="nurseName" name="nurseName"
                                        value="${drugSubmission.nurseName}" />
                            <span id="error_nurseName" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="selectionHidden">
    <input type="hidden" name="name" id="patientNameHidden" value="${drugSubmission.name}">
    <input type="hidden" name="prsFlag" value="${prsFlag}"/>
</div>
<div class="doctorNameSelectionHidden">
    <input type="hidden" name="names" id="doctorNameHidden" value="${drugSubmission.doctorName}">
    <input type="hidden" name="specialty" id="specialtyHidden" value="${drugSubmission.specialty}">
    <input type="hidden" name="subSpecialty" id="subSpecialtyHidden" value="${drugSubmission.subSpecialty}">
    <input type="hidden" name="qualification" id="qualificationHidden" value="${drugSubmission.qualification}">
</div>
<input type="hidden" name="doctorInformations" id="doctorInformations" value="${drugSubmission.doctorInformations}">
<div class="modal fade" id="START_DATE_OF_DISPENSING" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="startDateErrorMsg">
                            <iais:message key="The current date of submission is more than two days from the start date of dispensing. For future submissions, please ensure that data is submitted promptly within two days after the start date of dispensing" escape="false" />
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
<div class="modal fade" id="ELIS_SERVICE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="elisMsg">
                            <iais:message key="GENERAL_ERR0063" escape="false" />
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
<div class="modal fade" id="PRS_SERVICE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsMsg">
                            <iais:message key="GENERAL_ERR0064" escape="false" />
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
<div class="modal fade" id="NO_PRS_ELIS_SERVICE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="noMsg">
                            <iais:message key="GENERAL_ERR0065" escape="false" />
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
<div class="modal fade" id="PRS_CLOSE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsCloseMsg">
                            <iais:message key="GENERAL_ERR0066" escape="false" />
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
<div class="modal fade" id="PRS_PRN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsPrn">
                            <iais:message key="GENERAL_ERR0054" escape="false" />
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
    $(document).ready(function() {
        <c:if test="${dpSuperDataSubmissionDto.appType eq 'DSTY_005'}">
        disableContent('div.patient');
        $('#retrieveDataDiv').hide();
        </c:if>
        ifClickValidateButton();
        $('#drugType').change(function () {
            drugTypeChange();
            diagnosi();
        });
        $('#drugType,#medication').change(function (){
            changeStrength();
        });
        $('#prescriptionSubmissionId').change(function(){
            checkPrescriptionSubmissionId();
        });

        /*changeStrength();*/
        <c:if test="${dpSuperDataSubmissionDto.appType eq 'DSTY_005'}">
        disableContent('div.drugType');
        </c:if>

        checkPrescriptionSubmissionId();
    });
    function diagnosi(){
        var drugtype= $('#drugType option:selected').val();
        if(drugtype == "DPD001"){
            $('#diagnosi').show();
        } else {
            $('#diagnosi').hide();
        }
    }


    function drugTypeChange(){
        var drugtype= $('#drugType option:selected').val();
        if(drugtype == "DPD001"){
            $('#dispensingDate').hide();
            $('#ddEndDate').hide();
            $('#prescriptionDate').show();
            unDisableContent('div.medication');
            fillValue($('#medication'),null);
        } else if(drugtype == "DPD002"){
            $('#dispensingDate').show();
            $('#ddEndDate').show();
            $('#prescriptionSubmissionId').val('');
            $('#error_prescriptionSubmissionId').html('');
            $('#prescriptionDate').hide();
        }else{
            $('#dispensingDate').hide();
            $('#ddEndDate').hide();
            $('#prescriptionDate').hide();
        }
        changeStrength();
    }

    function checkPrescriptionSubmissionId(){
        var prescriptionSubmissionId = $('#prescriptionSubmissionId').val();
        if(prescriptionSubmissionId != ""){
            var data = {
                'prescriptionSubmissionId':prescriptionSubmissionId

            };
            showWaiting();
            $.ajax({
                'url':'${pageContext.request.contextPath}/checkPrescriptionSubmissionId',
                'dataType':'json',
                'data':data,
                'type':'POST',
                'success':function (data) {
                    if('<%=AppConsts.AJAX_RES_CODE_SUCCESS%>' == data.resCode){
                        $("#error_prescriptionSubmissionId").html('');
                        $("#medication").val(data.resultJson);
                        changeStrength();
                        disableContent('div.medication');
                    }else if('<%=AppConsts.AJAX_RES_CODE_VALIDATE_ERROR%>' == data.resCode){
                        $("#error_prescriptionSubmissionId").html(data.resultJson + '');
                    }else if('<%=AppConsts.AJAX_RES_CODE_ERROR%>' == data.resCode){
                        $("#error_prescriptionSubmissionId").html('');
                    }else{
                        unDisableContent('div.medication');
                    }
                },
                'error':function () {

                }
            });
            dismissWaiting();
        }
    }

    function changeStrength(){
        var drugtype= $('#drugType option:selected').val();
        var medication= $('#medication').val();
        if('MED001' == medication){
            $('label[name="strengthlabel"]').html("Strength (&micro;g/hr)&nbsp;<span class=\"mandatory\">*</span>");
            $('#urineTest').hide();
            $('#nurse').show();
        }else if(('MED002' == medication && 'DPD002' == drugtype)){
            console.log('DPD002')
            $('label[name="strengthlabel"]').html("Strength (mg)&nbsp;<span class=\"mandatory\">*</span>");
            $('#urineTest').show();
            $('#nurse').hide();
        } else{
            $('label[name="strengthlabel"]').html("Strength (mg)&nbsp;<span class=\"mandatory\">*</span>");
            $('#urineTest').hide();
            $('#nurse').hide();
        }
    }
    function ifClickValidateButton(){
        if ("1" == $('#showValidatePT').val()) {
            $('#validatePT').modal('show');
        }
        if ("1" == $('#showValidateVD').val()) {
            $('#validateVD').modal('show');
        }
    }

    function retrieveValidateDrug() {
        showWaiting();
        var idType = $('#idType').val();
        var idNo = $('input[name="idNumber"]').val();
        var nationality = $('#nationality').val();
        var url = $('#_contextPath').val() + '/dp/retrieve-identification';
        var options = {
            idType: idType,
            idNo: idNo,
            nationality: nationality,
            url: url
        }
        callCommonAjax(options, validatePatientName);
    }
    function validatePatientName(data){
        clearErrorMsg();
        clearSelection();
        console.log("success!")
        if (isEmpty(data) || isEmpty(data.selection) || isEmpty(data.selection.name) || !isEmpty(data.errorMsg)) {

            if (!isEmpty(data.errorMsg)) {
                doValidationParse(data.errorMsg);
            } else {
                $('#noFoundDiv').modal('show');
            }
            return;
        }
        clearSelection();
        $('#name').find('p').text(data.selection.name);
        $('[name="name"]').val('1');
        $('#patientNameHidden').val(data.selection.name);

    }
    function clearSelection(){
        console.log("clearSelection!")
        clearErrorMsg();
        $('#name').find('p').text('');
        clearFields('.selectionHidden');
    }
    function clearDockerSelection(){
        console.log("clearDockerSelection!")
        clearErrorMsg();
        $('#names').find('p').text('');
        $('#specialty').find('p').text('');
        $('#subSpecialty').find('p').text('');
        $('#qualification').find('p').text('');
        clearFields('.doctorNameSelectionHidden');
    }
    function validateDoctors() {
        console.log('loading info ...');
        showWaiting();
        var prgNo =  $('input[name="doctorReignNo"]').val();
        if(prgNo == "" || prgNo == null || prgNo == undefined){
            clearPrsInfo();
            dismissWaiting();
            clearErrorMsg();
            $('#doctorRegnNoMsg').text('This is a mandatory field.');
            return;
        }
        var no = $('input[name="doctorReignNo"]').val();
        var docSource = $('input[name="docSource"]').val();
        var jsonData = {
            'prgNo': no,
            'docSource': docSource
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/doc/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (isEmpty(data.selection)) {
                    $('#doctorInformations').val(true);
                    console.log("The return data is null");
                    $('#doctorInformationText').show();
                    $('#doctorInformation').hide();
                    $('#NO_PRS_ELIS_SERVICE').modal('show');
                } else if(isEmpty(!data.selection)) {
                    $('#doctorInformations').val(false);
                    loadingSp(data);
                    if ('-1' == data.selection.statusCode || '-2' == data.selection.statusCode) {
                        $('#ELIS_SERVICE').modal('show');
                    }else if(isEmpty(data.selections) && data.selection.hasException==false){
                        $('#PRS_SERVICE').modal('show');
                    }else if (data.selection.hasException) {
                        $('#doctorInformations').val(true);
                        $('#PRS_CLOSE').modal('show');
                        $('#doctorInformation').hide();
                        $('#doctorInformationText').show();
                    }else if ('401' == data.selection.statusCode) {
                        $('#doctorInformations').val(true);
                        $('#PRS_PRN').modal('show');
                        $('#doctorInformation').hide();
                        $('#doctorInformationText').show();
                    }
                }
                dismissWaiting();
            },
            'error': function () {
                console.log('error');
                clearPrsInfo();
                dismissWaiting();
            },
        });
    }

    var clearPrsInfo = function () {
        $('#names').find('p').text('');
    };
    function loadingSp(data) {
        $('#doctorInformationText').hide();
        $('#doctorInformation').show();
        const name = data.selection.name;
        $('#names').find('p').text(name);
        $('#doctorNameHidden').val(name);

        $('#specialty').find('p').text(data.selection.specialty);
        $('#specialtyHidden').val(data.selection.specialty);

        $('#subSpecialty').find('p').text(data.selection.subspecialty);
        $('#subSpecialtyHidden').val(data.selection.subspecialty);

        $('#qualification').find('p').text(data.selection.qualification);
        $('#qualificationHidden').val(data.selection.qualification);
    }

    //Prompt 2 days after the assignment start date
    $("#startDispensingDate").on('blur, change', function () {
        showWaiting();
        var dispensingDate=$('#startDispensingDate').val();
        var url = $('#_contextPath').val() + '/dp/startdispensing-date';
        var options = {
            dispensingDate: dispensingDate,
            url: url
        }
        callCommonAjax(options, checkBirthDateCallback);
    });

    function checkBirthDateCallback(data) {
        if (isEmpty(data) || isEmpty(data.showDate) || !data.showDate) {
            return;
        }
        $('#START_DATE_OF_DISPENSING').modal('show');
    }

    function cancels() {
        $('#START_DATE_OF_DISPENSING').modal('hide');
        $('#ELIS_SERVICE').modal('hide');
        $('#NO_PRS_ELIS_SERVICE').modal('hide');
        $('#PRS_SERVICE').modal('hide');
        $('#PRS_CLOSE').modal('hide');
        $('#PRS_PRN').modal('hide');
    }
</script>