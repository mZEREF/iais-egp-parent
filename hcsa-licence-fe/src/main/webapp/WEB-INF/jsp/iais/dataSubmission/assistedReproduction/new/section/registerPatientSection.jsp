<span class="error-msg" id="error_newPatient">This patient was never registered by your AR Centre before. Please provide details of the patient below.</span>

<p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Patient</p>

<iais:row>
    <iais:field cssClass="col-md-6" value="Name (as per NRIC/FIN/Passport)" mandatory="true"/>
    <iais:value width="12">
        <iais:input maxLength="66" type="text" name="name" value="${patient.name}"/>
    </iais:value>
</iais:row>

<iais:row id="birthDates">
    <iais:field cssClass="col-md-6" value="Date of Birth" mandatory="true"/>
    <iais:value width="12">
        <iais:datePicker id ="birthDate" name="birthDate" value="${patient.birthDate}"/>
    </iais:value>
</iais:row>

<iais:row id="currentPatientNationality">
    <iais:field cssClass="col-md-6" value="Nationality" mandatory="true"/>
    <iais:value width="12">
        <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                     value="${patient.nationality}"
                     cssClass="nationalitySel" onchange="checkMantory(this, '#ptEthnicGroupLabel', 'NAT0001')"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field cssClass="col-md-6" value="Ethnic Group" id="ptEthnicGroupLabel"
                mandatory="${patient.nationality eq 'NAT0001' ? true : false}"/>
    <iais:value width="12">
        <iais:select name="ethnicGroup" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                     value="${patient.ethnicGroup}"
                     cssClass="ethnicGroupSel"
                     onchange="toggleOnSelect(this, 'ETHG005', 'ptEthnicGroupOtherDiv')"/>
    </iais:value>
</iais:row>

<div class="form-group" id="ptEthnicGroupOtherDiv"
     style="<c:if test="${patient.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
    <iais:field cssClass="col-md-6" value="Ethnic Group (Others)" mandatory="true"/>
    <iais:value width="12">
        <iais:input maxLength="20" type="text" name="ethnicGroupOther" value="${patient.ethnicGroupOther}"/>
    </iais:value>
</div>

<iais:row>
    <iais:field cssClass="col-md-6"
                value="Has patient registered for AR/IUI Treatment using other identification number before (e.g. passport number)?"
                mandatory="true"/>
    <iais:value width="12">
        <iais:value width="12">
            <div class="form-check form-check-inline">
                <input class="form-check-input triggerObj" id="isArIUIRegisteredN" type="radio"
                       name="previousIdentification" value="0"
                        <%--@elvariable id="jumpValidateHusband" type="java.lang.String"--%>
                       <c:if test="${patient.previousIdentification eq false}">checked</c:if> />
                <label class="form-check-label" for="isArIUIRegisteredN">
                    <span class="check-circle"></span>No
                </label>
            </div>
        </iais:value>
        <iais:value width="12">
            <div class="form-check form-check-inline">
                <input class="form-check-input triggerObj" id="isArIUIRegisteredY" type="radio"
                       name="previousIdentification" value="1"
                       <c:if test="${patient.previousIdentification}">checked</c:if> />
                <label class="form-check-label" for="isArIUIRegisteredY">
                    <span class="check-circle"></span>Yes
                </label>
            </div>
        </iais:value>
        <span class="error-msg" name="iaisErrorMsg" id="error_previousIdentification"></span>
    </iais:value>
</iais:row>

<div id="previousPatientSection">

    <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Patient's identification details used for previous AR/IUI treatment:</p>

    <iais:row>
        <iais:field cssClass="col-md-6" value="ID No." mandatory="true"/>
        <iais:value width="12">
            <iais:input maxLength="20" type="text" name="preIdNumber"
                        value="${previous.idNumber}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field cssClass="col-md-6" value="Nationality" mandatory="true"/>
        <iais:value width="12">
            <iais:select name="preNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                         value="${previous.nationality}"
                         cssClass="nationalitySel"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field cssClass="col-md-6" value="Name" mandatory="true"/>
        <iais:value width="12">
            <iais:input maxLength="66" type="text" name="preName" value="${previous.name}"/>
        </iais:value>
    </iais:row>
</div>


<div id="husDiv">
    <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Husband</p>
    <iais:row>
        <iais:field cssClass="col-md-6" value="Does the patient's husband have a NRIC/FIN number?" mandatory="true"/>
        <iais:value width="12">
            <iais:value width="12">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" id="hubNoIdNUmberN" type="radio" name="hubHasIdNumber" value="0"
                           <c:if test="${husband.idType eq 'DTV_IT003'}">checked</c:if> />
                    <label class="form-check-label" for="hubNoIdNUmberN">
                        <span class="check-circle"></span>No
                    </label>
                </div>
            </iais:value>
            <iais:value width="12">
                <div class="form-check form-check-inline">
                    <input class="form-check-input" id="hubHasIdNUmber" type="radio" name="hubHasIdNumber" value="1"
                           <c:if test="${husband.idType eq 'DTV_IT001' or husband.idType eq 'DTV_IT002'}">checked</c:if> />
                    <label class="form-check-label" for="hubHasIdNUmber">
                        <span class="check-circle"></span>Yes
                    </label>
                </div>
            </iais:value>
            <span class="error-msg" name="iaisErrorMsg" id="error_hubHasIdNumber"></span>
        </iais:value>
    </iais:row>

    <div id="hubContentDiv">
        <iais:row>
            <iais:field cssClass="col-md-6" value="Please indicate the husband's NRIC/FIN number" mandatory="true"
                        id="hubNricField"/>
            <iais:field cssClass="col-md-6" value="Please indicate the husband's passport number" mandatory="true"
                        id="hubPassportField"/>
            <iais:value width="12">
                <iais:input maxLength="20" type="text" name="idNumberHbd"
                            value="${husband.idNumber}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field cssClass="col-md-6" value="Name (as per NRIC/FIN/Passport)" mandatory="true"/>
            <iais:value width="12">
                <iais:input maxLength="66" type="text" name="nameHbd" value="${husband.name}"/>
            </iais:value>
        </iais:row>

        <iais:row id="birthHusbandDate">
            <iais:field cssClass="col-md-6" value="Date of Birth" mandatory="true"/>
            <iais:value width="12">
                <iais:datePicker name="birthDateHbd" id="birthDateHbd" value="${husband.birthDate}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field cssClass="col-md-6" value="Nationality" mandatory="true"/>
            <iais:value width="12">
                <iais:select name="nationalityHbd" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                             value="${husband.nationality}"
                             cssClass="nationalitySel"
                             onchange="checkMantory(this, '#hubEthnicGroupLabel', 'NAT0001')"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field cssClass="col-md-6" value="Ethnic Group" id="hubEthnicGroupLabel"
                        mandatory="${husband.nationality eq 'NAT0001' ? true : false}"/>
            <iais:value width="12">
                <iais:select name="ethnicGroupHbd" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                             value="${husband.ethnicGroup}"
                             cssClass="ethnicGroupSel"
                             onchange="toggleOnSelect(this, 'ETHG005', 'hubEthnicGroupOtherDiv')"/>
            </iais:value>
        </iais:row>

        <div class="form-group" id="hubEthnicGroupOtherDiv"
             style="<c:if test="${husband.ethnicGroup ne 'ETHG005'}">display:none</c:if>">
            <iais:field cssClass="col-md-6" value="Ethnic Group (Others)" mandatory="true"/>
            <iais:value width="12">
                <iais:input maxLength="20" type="text" name="ethnicGroupOtherHbd"
                            value="${husband.ethnicGroupOther}"/>
            </iais:value>
        </div>
    </div>
</div>