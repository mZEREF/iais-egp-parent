<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Patient Selection
            </strong>
        </h4>
    </div>
    <div id="cycleStageSectionPanel" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="patientInformationDto" value="${topSuperDataSubmissionDto.patientInformationDto}"/>
                <iais:row>
                    <iais:field width="5" value="Patient ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                     value="${patientInformationDto.idType}" cssClass="idTypeSel" onchange="clearSelection()"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="idNumber" value="${patientInformationDto.idNumber}" onchange="clearSelection()"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient Nationality" mandatory="true"/>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${patientInformationDto.nationality}" cssClass="nationalitySel" onchange="clearSelection()"/>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3 patientData" display="true" id="retrieveDataDiv">
                        <a class="retrieveIdentification" onclick="retrieveValidateTop()">
                            Validate Patient
                        </a>
                        <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_retrieveData"></span>
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Patient Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="name">
                        ${patientInformationDto.patientName}
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Age"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="age">
                        ${patientInformationDto.patientAge}
                    </iais:value>
                </iais:row>
                <iais:row cssClass="patientData">
                    <iais:field width="5" value="Date Of Birth"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="data">
                        ${patientInformationDto.birthData}
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
<div class="selectionHidden">
    <input type="hidden" name="name" id="patientNameHidden" value="${patientInformationDto.patientName}">
    <input type="hidden" name="age" id="patientAgeHidden" value="${patientInformationDto.patientAge}">
    <input type="hidden" name="data" id="birthDataHidden" value="${patientInformationDto.birthData}">
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
<script>
    $(document).ready(function() {
        console.log("showValidatePT!")
        if ("1" == $('#showValidatePT').val()) {
            console.log("PT!")
            $('#validatePT').modal('show');
        } else if ("1" == $('#showValidatePT').val()) {
            console.log("no!")
            $('#noFoundDiv').modal('show');
        }
    });

    function retrieveValidateTop() {
        showWaiting();
        var idType = $('#idType').val();
        var idNo = $('input[name="idNumber"]').val();
        var nationality = $('#nationality').val();
        var url = $('#_contextPath').val() + '/top/retrieve-identification';
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
        if (isEmpty(data) || isEmpty(data.selection) || isEmpty(data.selection.patientName) || isEmpty(data.selection.patientAge) || isEmpty(data.selection.birthData)  || !isEmpty(data.errorMsg)) {
            console.log("10!")
            if (!isEmpty(data.errorMsg)) {
                console.log("11!")
                doValidationParse(data.errorMsg);
            } else {
                $('#noFoundDiv').modal('show');
            }
            return;
        }
        clearSelection();
        $('#name').find('p').text(data.selection.patientName);
        $('[name="name"]').val('1');
        $('#patientNameHidden').val(data.selection.patientName);
        $('#age').find('p').text(data.selection.patientAge);
        $('[name="age"]').val('');
        $('#patientAgeHidden').val(data.selection.patientAge);
        $('#data').find('p').text(data.selection.birthData);
        $('[name="data"]').val('1');
        $('#birthDataHidden').val(data.selection.birthData);


    }
    function clearSelection(){
        console.log("clearSelection!")
        clearErrorMsg();
        $('#name').find('p').text('');
        $('#data').find('p').text('');
        $('#age').find('p').text('');
        clearFields('.selectionHidden');
    }
</script>
