<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<c:set var="husband" value="${patientInfoDto.husband}" />
<input type="hidden" name="registeredPatient">
<input type="hidden" name="hasCycle">
<iais:row cssClass="form-check-gp">
    <p class="form-check-title">Does the patient have a NRIC/FIN number?</p>
    <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="ptHasIdNumberNo" type="radio" name="ptHasIdNumber" value="N"
               <c:if test="${patient.idType eq 'AR_IT_004'}">checked</c:if>/>
        <label class="form-check-label" for="ptHasIdNumberNo">
            <span class="check-circle"></span>No
        </label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="ptHasIdNumber" type="radio" name="ptHasIdNumber" value="Y"
               <c:if test="${patient.idType eq 'AR_IT_001' or patient.idType eq 'AR_IT_002' or patient.idType eq 'AR_IT_003'}">checked</c:if>/>
        <label class="form-check-label" for="ptHasIdNumber">
            <span class="check-circle"></span>Yes
        </label>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumber"></span>
</iais:row>

<iais:row cssClass="form-check-gp" style="display: none" id="indicateIdentitySection">
    <div id="passportIdentify">
        <p class="form-check-title">Please indicate the patient's passport number</p>
    </div>
    <div id="idIdentify">
        <p class="form-check-title">Please indicate the patient's NRIC/FIN number</p>
    </div>
    <p>Note: Data for ID no. should be stored in the same field within the database. Only the label is changed.</p>
    <div class="row">
        <div class="col-xs-9 col-md-6">
            <iais:input maxLength="20" type="text" name="identityNo" id="identityNo" value="${patient.idNumber}"/>
        </div>
        <div class="col-xs-3 col-md-6">
            <a href="#" class="triggerObj" id="validatePAT" style="text-decoration:underline">Validate Patient</a>
        </div>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_idNumber"></span>
</iais:row>

<div class="form-check-gp" id="amendPatientSection" style="display: none">
    <iais:row>
        <div style="border-bottom: 1px #333333 solid">
            <span style="font-size:14px">This patient was previously registered in your AR Centre. Please confirm the patient's details below, or click "Amend" to update the details.</span>
        </div>
        <div id="registeredPTDetail" class="col-xs-12 col-md-12"
             style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0">
            <span style="display: block">Details of Patient</span>
            <span style="display: block">Name (as per NRIC/Passport): <span id="ptName"></span></span>
            <span style="display: block">Date of Birth: <span id="ptBirth"></span></span>
            <span style="display: block">Nationality: <span id="ptNat"></span></span>
            <span style="display: block">Ethnicity: <span id="ptEth"></span></span>
        </div>
        <div id="registeredTRTDetail" class="col-xs-12 col-md-12"
             style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0">
            <span style="display: block">Other Identification ID Used in Previous AR Treatment</span>
            <span style="display: block">ID No.: <span id="ptPreId"></span></span>
            <span style="display: block">Name: <span id="ptPreName"></span></span>
            <span style="display: block">Nationality: <span id="ptPreNat"></span></span>
        </div>
        <div id="registeredHBDetail" class="col-xs-12 col-md-12"
             style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0">
            <span style="display: block">Husband Details (If Applicable)</span>
            <span style="display: block">Name (as per NRIC/Passport): <span id="husName"></span></span>
            <span style="display: block">Date of Birth: <span id="husBirth"></span></span>
            <span style="display: block">Nationality: <span id="husNat"></span></span>
            <span style="display: block">Ethnicity: <span id="husEth"></span></span>
        </div>
        <a class="btn btn-primary" href="javascript:void(0);" id="pt-amend">Amend</a>
    </iais:row>

    <iais:row cssClass="form-check-gp" id="cycleRadioRow">
        <p class="form-check-title" id="cycleRadioStart">There is at least one open data submission for
            <span id="ptNameTitle">${patient.name}</span> (<span id="patIdNoTitle">${patient.idNumber}</span>).
            Please indicate whether you intend to resume an open submission, or create a new submission.</p>
        <div class="form-check col-xs-12" style="padding: 0;" id="newCycleRadio">
            <input class="form-check-input" id="cycleRadio" type="radio" name="cycleRadio"
                   value="newCycle"/>
            <label class="form-check-label" for="cycleRadio">
                <span class="check-circle"></span>Create a New Submission
            </label>
            <span class="error-msg" name="iaisErrorMsg" id="error_cycleRadio${suffix}"></span>
        </div>
    </iais:row>

    <iais:row cssClass="form-check-gp" id="nextStageRow">
        <p class="form-check-title">What information do you want to submit? </p>
        <iais:select name="nextStage" firstOption="Please Select" id="nextStage"/>
    </iais:row>
</div>

<div id="registerPatientSection" style="display: none">

    <%@include file="patientDetail.jsp" %>

    <div id="previousPatientSection" style="display: none">

        <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Has patient registered for AR/IUI Treatment
            using another Identification Number before?:</p>
        <%@include file="previousPatient.jsp" %>
    </div>
    <%@include file="husbandPatientDetail.jsp" %>
</div>

