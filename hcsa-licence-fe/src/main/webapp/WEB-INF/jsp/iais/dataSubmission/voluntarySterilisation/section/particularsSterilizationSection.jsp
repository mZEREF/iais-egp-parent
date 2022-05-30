<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="sexualSterilizationDto" value="${vssTreatmentDto.sexualSterilizationDto}" />
<div id="flagDocMessage" hidden><iais:message key="GENERAL_ERR0042"/> </div>
<div id="flagInvaMessage" hidden><iais:message key="GENERAL_ERR0057"/> </div>
<div id="flagPrnMessage" hidden><iais:message key="GENERAL_ERR0054"/> </div>
<div class="form-horizontal treatmentDetails">
   <%-- <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            &lt;%&ndash;<strong class="app-font-size-22 premHeader">title</strong>&ndash;%&gt;
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>--%>
    <iais:row>
        <iais:field width="5" value="Doctor Professional Registration No." mandatory="true"/>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="20" type="text" name="doctorReignNo"
                        onchange="clearDockerSelection()"
                        value="${sexualSterilizationDto.doctorReignNo}" />
            <span id="doctorRegnNoMsg" name="iaisErrorMsg" class="error-msg"></span>
        </iais:value>
        <iais:value width="3" cssClass="col-md-3" display="true">
            <a class="ValidateDoctor" onclick="validateDoctors()">
                Validate Doctor
            </a>

        </iais:value>
    </iais:row>

    <iais:row id="doctorname">
        <iais:field width="5" value="Name of Doctor who performed the sterilization"/>
        <iais:value width="7" cssClass="col-md-7" display="true" id="names">
            ${sexualSterilizationDto.doctorName}
        </iais:value>
    </iais:row>
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
    <input type="hidden" name="doctorName" id="doctorNameHidden" value=" ${sexualSterilizationDto.doctorName}">
</div>

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
        const name = data.name;
        $('#names').find('p').text(name);
        $('#doctorNameHidden').val(name);
    }

    function clearDockerSelection(){
        console.log("clearDockerSelection!")
        clearErrorMsg();
        $('#names').find('p').text('');
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
        var jsonData = {
            'prgNo': no
        };
        console.log('2');
        $.ajax({
            'url': '${pageContext.request.contextPath}/vss/prg-input-info',
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
            'error': function () {
                console.log('error');
                clearPrsInfo;
                dismissWaiting();
            },
        });
    }
</script>