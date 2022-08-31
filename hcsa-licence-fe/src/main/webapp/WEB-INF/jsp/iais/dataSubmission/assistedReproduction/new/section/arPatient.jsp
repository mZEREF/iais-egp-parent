<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<c:set var="husband" value="${patientInfoDto.husband}" />
<input type="hidden" name="registeredPatient">
<iais:row cssClass="form-check-gp">
    <p class="form-check-title">Does the patient have a NRIC/FIN number?</p>
    <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="noIdNumber" type="radio" name="hasIdNumber" value="N" <c:if test="${patient.hasIdNumber eq 'N'}">checked</c:if>/>
        <label class="form-check-label" for="noIdNumber">
            <span class="check-circle"></span>No
        </label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="hasIdNumber" type="radio" name="hasIdNumber" value="Y" <c:if test="${patient.hasIdNumber eq 'Y'}">checked</c:if>/>
        <label class="form-check-label" for="hasIdNumber">
            <span class="check-circle"></span>Yes
        </label>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumber"></span>
</iais:row>

<iais:row cssClass="form-check-gp" style="display: none" id="indicateIdentitySection">
    <div id="passportIdentify" style="display: none">
        <p style="font-weight: 600;font-size: 2rem">Please indicate the patient's passport number</p>
    </div>
    <div style="display: none" id="idIdentify">
        <p style="font-weight: 600;font-size: 2rem">Please indicate the patient's NRIC/FIN number</p>
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

<iais:row cssClass="form-check-gp"  id="amendPatientSection" style="display: none">
    <div style="border-bottom: 1px #333333 solid">
        <span style="font-size:14px">This patient was previously registered in your AR Centre. Please confirm the patient's details below, or click "Amend" to update the details.</span>
    </div>
    <div id="registeredPTDetail" class="col-xs-12 col-md-12" style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0"></div>
    <div id="registeredTRTDetail" class="col-xs-12 col-md-12" style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0"></div>
    <div id="registeredHBDetail" class="col-xs-12 col-md-12" style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0"></div>
    <a class="btn btn-primary" href="javascript:void(0);" id="pt-amend">Amend</a>
    <%@include file="cycleStage.jsp"%>
</iais:row>

<div id="registerPatientSection" style="display: none">

    <%@include file="patientDetail.jsp" %>

    <div id="previousPatientSection" style="display: none">

        <p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Has patient registered for AR/IUI Treatment using another Identification Number before?:</p>
        <%@include file="previousPatient.jsp" %>
    </div>
    <%@include file="husbandPatientDetail.jsp" %>
</div>

