<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="sexualSterilizationDto" value="${vssTreatmentDto.sexualSterilizationDto}" />
<c:set var="doctorInformationDto" value="${vssSuperDataSubmissionDto.doctorInformationDto}"/>
<div id="flagDocMessage" hidden><iais:message key="GENERAL_ERR0042"/> </div>
<div id="flagInvaMessage" hidden><iais:message key="GENERAL_ERR0057"/> </div>
<div id="flagPrnMessage" hidden><iais:message key="GENERAL_ERR0054"/> </div>
<input type="hidden" name="doctorSource" value="VSS">
<div class="form-horizontal treatmentDetails">
    <iais:row>
        <iais:field width="5" value="Doctor Professional Registration No." mandatory="true"/>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="20" type="text" name="doctorReignNo"
                        onchange="clearDockerSelection()"
                        value="${sexualSterilizationDto.doctorReignNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_doctorReignNo"></span>
            <span id="doctorRegnNoMsg" name="iaisErrorMsg" class="error-msg"></span>
        </iais:value>
        <iais:value width="3" cssClass="col-md-3" display="true">
            <a class="ValidateDoctor" onclick="validateDoctors()">
                Validate Doctor
            </a>
        </iais:value>
    </iais:row>

<%--    <iais:row id="doctorname">
        <iais:field width="5" value="Name of Doctor who performed the sterilization"/>
        <iais:value width="7" cssClass="col-md-7" display="true" id="names">
            ${sexualSterilizationDto.doctorName}
        </iais:value>
    </iais:row>--%>
       <div id="doctorInformation" <c:if test="${sexualSterilizationDto.doctorInformations eq 'true'}">style="display: none"</c:if>>
           <iais:row>
               <iais:field width="5" value="Doctor's Name"/>
               <iais:value width="7" cssClass="col-md-7" display="true" id="names">
                   ${sexualSterilizationDto.doctorName}
               </iais:value>
           </iais:row>
           <iais:row >
               <iais:field width="5" value="Specialty"/>
               <iais:value width="7" cssClass="col-md-7" display="true" id="specialty">
                   ${sexualSterilizationDto.specialty}
               </iais:value>
           </iais:row>
           <iais:row >
               <iais:field width="5" value="Sub-Specialty"/>
               <iais:value width="7" cssClass="col-md-7" display="true" id="subSpecialty">
                   ${sexualSterilizationDto.subSpecialty}
               </iais:value>
           </iais:row>
           <iais:row >
               <iais:field width="5" value="Qualification"/>
               <iais:value width="7" cssClass="col-md-7" display="true" id="qualification">
                   ${sexualSterilizationDto.qualification}
               </iais:value>
           </iais:row>
       </div>
       <div id="doctorInformationText" <c:if test="${sexualSterilizationDto.doctorInformations eq 'false' || sexualSterilizationDto.doctorInformations eq null}">style="display: none"</c:if>>
           <iais:row>
               <iais:field width="5" value="Doctor's Name" mandatory="true"/>
               <iais:value width="7" cssClass="col-md-7" display="true">
                   <iais:input  type="text" name="dName" value="${doctorInformationDto.name}" />
                   <span class="error-msg" name="iaisErrorMsg" id="error_dName"></span>
               </iais:value>
           </iais:row>
           <iais:row >
               <iais:field width="5" value="Specialty" mandatory="true"/>
               <iais:value width="7" cssClass="col-md-7" display="true">
                   <iais:input  type="text" name="dSpeciality" value="${doctorInformationDto.speciality}" />
                   <span class="error-msg" name="iaisErrorMsg" id="error_dSpeciality"></span>
               </iais:value>
           </iais:row>
           <iais:row >
               <iais:field width="5" value="Sub-Specialty" mandatory="true"/>
               <iais:value width="7" cssClass="col-md-7" display="true">
                   <iais:input  type="text" name="dSubSpeciality" value="${doctorInformationDto.subSpeciality}" />
                   <span class="error-msg" name="iaisErrorMsg" id="error_dSubSpeciality"></span>
               </iais:value>
           </iais:row>
           <iais:row >
               <iais:field width="5" value="Qualification" mandatory="true"/>
               <iais:value width="7" cssClass="col-md-7" display="true">
                   <iais:input type="text" name="dQualification" value="${doctorInformationDto.qualification}" />
                   <span class="error-msg" name="iaisErrorMsg" id="error_dQualification"></span>
               </iais:value>
           </iais:row>
       </div>
    <iais:row>
        <iais:field width="5" value="Hospital/Clinic where the sterilization was performed" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input type="text" name="sterilizationHospital" value="${sexualSterilizationDto.sterilizationHospital}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Method of Sterilization" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="sterilizationMethod" firstOption="Please Select"  options="sterilizationLists"
                         value="${sexualSterilizationDto.sterilizationMethod}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Operation " mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="" name="operationDate" dateVal="${sexualSterilizationDto.operationDate}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Reviewed by Hospital Ethics Committee (HEC) (state name of hospital)" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="reviewedByHec"
                       value="true"
                       id="genderMale"
                       <c:if test="${sexualSterilizationDto.reviewedByHec == true}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="genderMale"><span
                        class="check-circle"></span>Yes</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_reviewedByHec"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="reviewedByHec"
                       value="false"
                       id="genderFemale"
                       <c:if test="${sexualSterilizationDto.reviewedByHec == false}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="genderFemale"><span
                        class="check-circle"></span>No</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name of Hospital" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input type="text" maxLength="100" name="hecReviewedHospital" value="${sexualSterilizationDto.hecReviewedHospital}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_hecReviewedHospital"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" id="hecReviewDateLabel" value="Date of HEC Review" mandatory="${sexualSterilizationDto.reviewedByHec ==true ? true : false}" />
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="hecReviewDate" name="hecReviewDate" dateVal="${sexualSterilizationDto.hecReviewDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_hecReviewDate"></span>
        </iais:value>
    </iais:row>

</div>

<div class="doctorNameSelectionHidden">
    <input type="hidden" name="names" id="doctorNameHidden" value="${sexualSterilizationDto.doctorName}">
    <input type="hidden" name="specialty" id="specialtyHidden" value="${sexualSterilizationDto.specialty}">
    <input type="hidden" name="subSpecialty" id="subSpecialtyHidden" value="${sexualSterilizationDto.subSpecialty}">
    <input type="hidden" name="qualification" id="qualificationHidden" value="${sexualSterilizationDto.qualification}">
</div>
<input type="hidden" name="doctorInformations" id="doctorInformations" value="${sexualSterilizationDto.doctorInformations}">
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12">
            <span style="font-size: 2rem;" id="prsErrorMsg">
              <iais:message key="GENERAL_ERR0057" escape="false" />
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
        $('input[name=reviewedByHec]').change(function () {
            if($('#genderMale').is(':checked')){
                $('#hecReviewDateLabel').append('<span class="mandatory">&nbsp;*</span>');
            }
            if($('#genderFemale').is(':checked')){
                $('#hecReviewDateLabel').find('.mandatory').remove();

            }
        });
    });
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
        console.log('1');
        if(prgNo == "" || prgNo == null || prgNo == undefined){
            clearPrsInfo();
            dismissWaiting();
            clearErrorMsg();
            $('#doctorRegnNoMsg').text('This is a mandatory field.');
            return;
        }
        var no = $('input[name="doctorReignNo"]').val();
        var doctorSource = $('input[name="doctorSource"]').val();
        var jsonData = {
            'prgNo': no,
            'docSource': doctorSource
        };
        console.log('2');
        $.ajax({
            'url': '${pageContext.request.contextPath}/doc/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                console.log('3');
                if (isEmpty(data) || isEmpty(data.selection)) {
                    $('#doctorInformations').val(true);
                    console.log("The return data is null");
                    $('#doctorInformationText').show();
                    $('#doctorInformation').hide();
                } else if('-1' == data.selection.statusCode || '-2' == data.statusCode) {
                    $('#prsErrorMsg').val($('#flagDocMessage').html());
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo();
                } else if (data.selection.hasException) {
                    $('#prsErrorMsg').val($('#flagInvaMessage').html());
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo();
                } else if ('401' == data.selection.statusCode) {
                    $('#prsErrorMsg').val($('#flagPrnMessage').html());
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo();
                } else {
                    $('#doctorInformations').val(false);
                    loadingSp(data);
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
</script>