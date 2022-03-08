<%--<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/dp_drugSubmission.js"></script>--%>
<div id="flagDocMessage" hidden><iais:message key="GENERAL_ERR0042"/> </div>
<div id="flagInvaMessage" hidden><iais:message key="GENERAL_ERR0048"/> </div>
<div id="flagPrnMessage" hidden><iais:message key="GENERAL_ERR0054"/> </div>
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
                <c:set var="suffix" value="" />
                <c:set var="drug" value="${drugSubmission}"/>
                <iais:row>
                    <iais:field width="5" value="Patient's ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE" value="${drugSubmission.idType}"
                                     cssClass="idTypeSel"/>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="idNumber" value="${drugSubmission.idNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7" style="width: 232px;">
                        <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${drugSubmission.nationality}" cssClass="nationalitySel"/>
                    </iais:value>
                    <iais:value width="2" cssClass="col-md-3 patientData" display="true" style="width: 330px;">
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
                    <iais:field width="3" value="Doctor's Professional Registration No." mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" style="width: 380px;">
                        <iais:input maxLength="20" type="text" name="doctorReignNo" value="${drugSubmission.doctorReignNo}" />
                        <span class="error-msg" name="iaisErrorMsg" id="error_doctorReignNo"></span>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3" display="true">
                        <a class="ValidateDoctor" onclick="validateDoctors()">
                            Validate Doctor
                        </a>
                    </iais:value>
                </iais:row>
                <iais:row id="doctorname">
                    <iais:field width="5" value="Doctor's Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="names">
                        ${drugSubmission.doctorName}
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Drug Prescribed or Dispensed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="drugType"  name="drugType" firstOption="Please Select" codeCategory="DP_DRUG_PRESCRIBED_OR_DISPENSED" value="${drugSubmission.drugType}"/>
                    </iais:value>
                </iais:row>
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
                    <iais:field width="5" value="Date of Dispensing" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="dispensingDate" value="${drugSubmission.dispensingDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_dispensingDate"></span>
                    </iais:value>
                </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Medication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="medication"  name="medication" firstOption="Please Select" codeCategory="DP_MEDICATION" value="${drugSubmission.medication}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Start Date" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="startDate" value="${drugSubmission.startDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="End Date" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="endDate" value="${drugSubmission.endDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Diagnosis" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="50" type="text" name="diagnosis" value="${drugSubmission.diagnosis}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<div class="selectionHidden">
    <input type="hidden" name="name" id="patientNameHidden" value="${drugSubmission.name}">
    <input type="hidden" name="names" id="doctorNameHidden" value="${drugSubmission.doctorName}">
    <input type="hidden" name="prsFlag" value="${prsFlag}"/>
</div>
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
            <span style="font-size: 2rem;" id="prsErrorMsg">
              <iais:message key="GENERAL_ERR0048" escape="false" />
            </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancels()">OK</button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT" >
<script>
    $(document).ready(function() {
        $('#drugType').change(function () {
            var drugtype= $('#drugType option:selected').val();
            if(drugtype == "DPD001"){
                $('#prescriptionDate').attr("style","display: block");
            }else {
                $('#prescriptionDate').attr("style","display: none");
            }
            if(drugtype == "DPD002"){
                $('#dispensingDate').attr("style","display: block");
            }else {
                $('#dispensingDate').attr("style","display: none");
            }
        });
    });

    $(document).ready(function() {
        console.log("showValidatePT!")
        if ("1" == $('#showValidatePT').val()) {
            $('#validatePT').modal('show');
        } else if ("1" == $('#showValidatePT').val()) {
            $('#noFoundDiv').modal('show');
        } /*else if ("1" == $('#showValidatePT').val()) {
            $('#validateVD').modal('show');
        }*/
    });
    $(document).ready(function() {
        console.log("showValidatePT!")
       if ("0" == $('#showValidatePT').val()) {
            $('#validateVD').modal('show');
        }
    });

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

    $(document).ready(function() {
        if($('#PRS_SERVICE_DOWN_INPUT').val()=='PRS_SERVICE_DOWN'){
            $('#PRS_SERVICE_DOWN').modal('show');
        }
    });

    function validateDoctors() {
        console.log('loading info ...');
        showWaiting();
        var prgNo =  $('input[name="doctorReignNo"]').val();
        console.log('1');
        if(prgNo == "" || prgNo == null || prgNo == undefined){
            clearPrsInfo();
            dismissWaiting();
            return;
        }
        var no = $('input[name="doctorReignNo"]').val();
        var jsonData = {
            'prgNo': no
        };
        console.log('2');
        $.ajax({
            'url': '${pageContext.request.contextPath}/dp/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                console.log('3');
                if (isEmpty(data)) {
                    console.log("The return data is null");
                } else if('-1' == data.statusCode || '-2' == data.statusCode) {
                    $('#prsErrorMsg').val($('#flagDocMessage').html());
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo();
                } else if (data.hasException) {
                    $('#prsErrorMsg').val($('#flagInvaMessage').html());
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo();
                } else if ('401' == data.statusCode) {
                    $('#prsErrorMsg').val($('#flagPrnMessage').html());
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo();
                } else {
                    loadingSp(data);
                }
                dismissWaiting();
            },
        });
    }

    function cancels() {
        $('#PRS_SERVICE_DOWN').modal('hide');
    }
    var clearPrsInfo = function () {
        $('#names').find('p').text('');
    };
    function loadingSp(data) {
        const name = data.name;
        $('#names').find('p').text(name);
        $('#doctorNameHidden').val(name);
    }
</script>