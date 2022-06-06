<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}" />
<div class="form-horizontal treatmentDetails">
    <%--<iais:row>
        <iais:value width="6" cssClass="col-md-6">
           &lt;%&ndash; <strong class="app-font-size-22 premHeader">title</strong>&ndash;%&gt;
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
        <iais:field width="5" value="Name of Patient" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="patientName" value="${treatmentDto.patientName}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="patient"/></c:set>
        <iais:field width="5" value="ID No." mandatory="true" info="${toolMsg}"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE_DTV"
                         value="${treatmentDto.idType}" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="idNumber" value="${treatmentDto.idNumber}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="birthData" value="${treatmentDto.birthData}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Gender" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="gender" firstOption="Please Select" codeCategory="TOP_GENDER_OF_PREGNANT_CHILDREN"
                         value="${treatmentDto.gender}" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Residence Status" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="residenceStatus" codeCategory="VSS_RESIDENCE_STATUS" firstOption="Please Select"
                         onchange ="toggleOnSelect(this, 'VSSRS003', 'residenceStatusOthers')"
                         value="${treatmentDto.residenceStatus}" />
        </iais:value>
    </iais:row>
    <iais:row id="residenceStatusOthers" style="${treatmentDto.residenceStatus eq 'VSSRS003' ? '' : 'display: none'}">
        <iais:field value="Other Residence Status"  width="5" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input type="text" name="otherResidenceStatus" maxLength="200" value="${treatmentDto.otherResidenceStatus}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Ethnic Group" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="ethnicGroup" firstOption="Please Select" codeCategory="VSS_ETHNIC_GROUP"
                         onchange ="toggleOnSelect(this, 'ECGP004', 'ethnicOthers')"
                         value="${treatmentDto.ethnicGroup}"/>
        </iais:value>
    </iais:row>
    <iais:row id="ethnicOthers" style="${treatmentDto.ethnicGroup eq 'ECGP004' ? '' : 'display: none'}">
        <iais:field value="Other Ethnic Group" width="5" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="otherEthnicGroup" value="${treatmentDto.otherEthnicGroup}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_otherEthnicGroup"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Marital Status" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="maritalStatus" firstOption="Please Select" codeCategory="VSS_MARITAL_STATUS"
                         value="${treatmentDto.maritalStatus}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Education Level" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="educationLevel" firstOption="Please Select" codeCategory="VSS_EDUCATION_LEVEL"
                         onchange ="toggleOnSelect(this, 'VSSEL006', 'educationLevelOthers')"
                         value="${treatmentDto.educationLevel}"/>
        </iais:value>
    </iais:row>
        <iais:row id="educationLevelOthers" style="${treatmentDto.educationLevel eq 'VSSEL006' ? '' : 'display: none'}">
            <iais:field width="5" value="Other Education Level" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:input maxLength="200" type="text" name="otherEducationLevel" value="${treatmentDto.otherEducationLevel}"/>
                <span class="error-msg" name="iaisErrorMsg" id="error_otherEducationLevel"></span>
            </iais:value>
        </iais:row>
    <iais:row>
        <iais:field width="5" value="Occupation" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="occupation" firstOption="Please Select" codeCategory="VSS_OCCUPATION"
                         onchange ="toggleOnSelect(this, 'VSSOP013', 'occupationOthers')"
                         value="${treatmentDto.occupation}"/>
        </iais:value>
    </iais:row>
    <iais:row id="occupationOthers" style="${treatmentDto.occupation eq 'VSSOP013' ? '' : 'display: none'}">
        <iais:field width="5" value="Other Occupation" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="otherOccupation" value="${treatmentDto.otherOccupation}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_otherOccupation"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Main Reason for Sterilization" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="sterilizationReason" firstOption="Please Select"  codeCategory="VSS_STERILIZATION_REASON"
                         onchange ="toggleOnSelect(this, 'VSSRFS009', 'sterilizationReasonOthers')"
                         value="${treatmentDto.sterilizationReason}"/>
        </iais:value>
    </iais:row>
    <iais:row id="sterilizationReasonOthers" style="${treatmentDto.sterilizationReason eq 'VSSRFS009' ? '' : 'display: none'}">
        <iais:field width="5" value="Other Main Reason for Sterilization" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="100" type="text" name="otherSterilizationReason" value="${treatmentDto.otherSterilizationReason}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_otherSterilizationReason"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="No. of Living Children " width="5" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" onchange=" controlMandatory()"  name="livingChildrenNo" value="${treatmentDto.livingChildrenNo}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_livingChildrenNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <label class="col-xs-5 col-md-4 control-label">Date of Birth of Last Child
            <span id="lastChildBirthdayLabel" class="mandatory">
                <c:if test="${!treatmentDto.livingChildrenNo eq '0'}">*</c:if>
            </span>
        </label>
        <%--<iais:field id = "lastChildBirthdayLabel" width="5" value="Date of Birth of Last Child"/>--%>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="lastChildBirthday" value="${treatmentDto.lastChildBirthday}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_lastChildBirthday"></span>
        </iais:value>
        </div>
    </iais:row>
<script>
    function controlMandatory() {
            var num = $("[name=livingChildrenNo]").val()

            console.log(num)
            if(num >0){
                $('#lastChildBirthdayLabel').text("*")
            }else {
                $('#lastChildBirthdayLabel').text("")
            }
    }
</script>