<div class="form-horizontal treatmentDetails">
    <iais:row>
        <iais:value width="6" cssClass="col-md-6">
            <%--<strong class="app-font-size-22 premHeader">title</strong>--%>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Patient" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="patientName" value="" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="ID No." mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="" />
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="20" type="text" name="idNumber" value="" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="birthData" value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Gender" mandatory="true"/>
        <iais:value width="3" cssClass="col-md-3">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="gender"
                       value="1"
                       id="genderMale"
                       <c:if test="">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="genderMale"><span
                        class="check-circle"></span>Male</label>
            </div>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <div class="form-check">
                <input class="form-check-input"
                       type="radio"
                       name="gender"
                       value="0"
                       id="genderFemale"
                       <c:if test="">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="genderFemale"><span
                        class="check-circle"></span>Female</label>
            </div>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Residence Status" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="residenceStatus" codeCategory="VSS_RESIDENCE_STATUS" firstOption="Please Select"
                         value="" />
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Other Residence Status"  width="5"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input type="text" name="otherResidenceStatus" maxLength="200" value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Ethnic Group" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="ethnicGroup" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                         value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="Other Ethnic Group" width="5"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="200" type="text" name="otherEthnicGroup" value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Marital Status" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="maritalStatus" firstOption="Please Select" codeCategory="VSS_MARITAL_STATUS"
                         value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Education Leve" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="educationLevel" firstOption="Please Select" codeCategory="VSS_EDUCATION_LEVEL"
                         value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Occupation" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="occupation" firstOption="Please Select" codeCategory=""
                         value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Main Reason for Sterilization" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:select name="sterilizationReason" firstOption="Please Select" codeCategory=""
                         value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field value="No. of Living Children " width="5" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="2" type="text" name="livingChildrenNo" value=""/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth of Last Child"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="" name="lastChildBirthday" value=""/>
        </iais:value>
    </iais:row>
</div>