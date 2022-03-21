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
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
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
            <iais:select name="ethnicGroup" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                         onchange ="toggleOnSelect(this, 'ETHG005', 'ethnicOthers')"
                         value="${treatmentDto.ethnicGroup}"/>
        </iais:value>
    </iais:row>
    <iais:row id="ethnicOthers" style="${treatmentDto.ethnicGroup eq 'ETHG005' ? '' : 'display: none'}">
        <iais:field value="Other Ethnic Group" width="5" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="200" type="text" name="otherEthnicGroup" value="${treatmentDto.otherEthnicGroup}"/>
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
                         value="${treatmentDto.educationLevel}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Occupation" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
           <%-- <iais:select name="occupation" firstOption="Please Select" codeCategory=""
                         value="${treatmentDto.occupation}"/>--%>
            <iais:input type="text" name="occupation" value="${treatmentDto.occupation}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Main Reason for Sterilization" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="sterilizationReason" firstOption="Please Select"  codeCategory="VSS_STERILIZATION_REASON"
                         value="${treatmentDto.sterilizationReason}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="No. of Living Children " width="5" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" name="livingChildrenNo" value="${treatmentDto.livingChildrenNo}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth of Last Child"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="lastChildBirthday" value="${treatmentDto.lastChildBirthday}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_lastChildBirthday"></span>
        </iais:value>
    </iais:row>
</div>