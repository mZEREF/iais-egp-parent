<%--@elvariable id="cycleRadio" type="java.lang.String"--%>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/arSelection/patient.js"></script>
<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}"/>
<c:set var="patient" value="${patientInfoDto.patient}"/>
<c:set var="previous" value="${patientInfoDto.previous}"/>
<c:set var="husband" value="${patientInfoDto.husband}"/>
<input type="hidden" name="existedPatient" value="${existedPatient}">
<input type="hidden" name="hasCycle" value="${arSuperDataSubmissionDto.selectionDto.dsCycleRadioDtos.size()>0?'Y':'N'}">
<iais:row cssClass="form-check-gp">
    <p class="form-check-title">Does the patient have a NRIC/FIN number?</p>
    <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="ptHasIdNumberNo" type="radio" name="ptHasIdNumber" value="0"
               <c:if test="${patient.idType eq 'DTV_IT003'}">checked</c:if>/>
        <label class="form-check-label" for="ptHasIdNumberNo">
            <span class="check-circle"></span>No
        </label>
    </div>
    <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="ptHasIdNumber" type="radio" name="ptHasIdNumber" value="1"
               <c:if test="${patient.idType eq 'DTV_IT001' or patient.idType eq 'DTV_IT002'}">checked</c:if>/>
        <label class="form-check-label" for="ptHasIdNumber">
            <span class="check-circle"></span>Yes
        </label>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_ptHasIdNumberNo"></span>
</iais:row>

<iais:row cssClass="form-check-gp" id="indicateIdentitySection">
    <div id="passportIdentify">
        <p class="form-check-title">Please indicate the patient's passport number</p>
    </div>
    <div id="idIdentify">
        <p class="form-check-title">Please indicate the patient's NRIC/FIN number</p>
    </div>
    <div class="row">
        <div class="col-xs-9 col-md-6">
            <iais:input maxLength="20" type="text" name="identityNo" id="identityNo" value="${patient.idNumber}"/>
        </div>
        <div class="col-xs-3 col-md-6">
            <a href="#" class="triggerObj" id="validatePAT" style="text-decoration:underline">Validate Patient</a>
        </div>
    </div>
</iais:row>

<div class="form-check-gp" id="amendPatientSection">
    <iais:row>
        <div style="border-bottom: 1px #333333 solid">
            <span style="font-size:14px">This patient was previously registered in your AR Centre. Please confirm the patient's details below, or click "Amend" to update the details.</span>
        </div>
        <div id="registeredPTDetail" class="col-xs-12 col-md-12"
             style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0">
            <span style="display: block">Details of Patient</span>
            <span style="display: block">Name (as per NRIC/Passport): <span id="ptName">${patient.name}</span></span>
            <span style="display: block">Date of Birth: <span id="ptBirth">${patient.birthDate}</span></span>
            <span style="display: block">Nationality: <span id="ptNat"><iais:code code="${patient.nationality}"/></span></span>
            <span style="display: block">Ethnicity: <span id="ptEth"><iais:code code="${patient.ethnicGroup}"/></span></span>
        </div>
        <div id="registeredTRTDetail" class="col-xs-12 col-md-12"
             style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0; ${previous eq null?'display:none;':''}">
            <span style="display: block">Other Identification ID Used in Previous AR Treatment</span>
            <span style="display: block">ID No.: <span id="ptPreId">${previous.idNumber}</span></span>
            <span style="display: block">Name: <span id="ptPreName">${previous.name}</span></span>
            <span style="display: block">Nationality: <span id="ptPreNat"><iais:code code="${previous.nationality}"/></span></span>
        </div>
        <div id="registeredHBDetail" class="col-xs-12 col-md-12"
             style="color: #7F7F7F;font-size: 13px;font-weight: 400;padding: 10px 0">
            <span style="display: block">Husband Details (If Applicable)</span>
            <span style="display: block">Name (as per NRIC/Passport): <span id="husName"${husband.name}></span></span>
            <span style="display: block">Date of Birth: <span id="husBirth">${husband.birthDate}</span></span>
            <span style="display: block">Nationality: <span id="husNat"><iais:code code="${husband.nationality}"/></span></span>
            <span style="display: block">Ethnicity: <span id="husEth"><iais:code code="${husband.ethnicGroup}"/></span></span>
        </div>
        <a class="btn btn-primary" href="javascript:void(0);" id="pt-amend">Amend</a>
    </iais:row>

    <iais:row cssClass="form-check-gp" id="cycleRadioRow">
        <p class="form-check-title" id="cycleRadioStart">There is at least one open data submission for
            <span id="ptNameTitle">${patient.name}</span> (<span id="patIdNoTitle">${patient.idNumber}</span>).
            Please indicate whether you intend to resume an open submission, or create a new submission.</p>
        <c:forEach var="cycle" items="${arSuperDataSubmissionDto.selectionDto.dsCycleRadioDtos}" varStatus="varStatus">
            <div class="form-check col-xs-12" style="padding: 0;">
                <input class="form-check-input" id="cycleRadio${varStatus.index}" type="radio" name="cycleRadio" value="${cycle.cycleId}">
                <label class="form-check-label" for="cycleRadio${varStatus.index}">
                    <span class="check-circle"></span>[${cycle.displayType}] Submission ID ${cycle.displaySubmissionNo}
                </label>
            </div>
        </c:forEach>
        <div class="form-check col-xs-12" style="padding: 0;" id="newCycleRadio">
            <input class="form-check-input" id="cycleRadio" type="radio" name="cycleRadio"
                   <c:if test="${cycleRadio eq 'newCycle'}">checked</c:if>
                   value="newCycle"/>
            <label class="form-check-label" for="cycleRadio">
                <span class="check-circle"></span>Create a New Submission
            </label>
            <span class="error-msg" name="iaisErrorMsg" id="error_cycleRadio"></span>
        </div>
    </iais:row>

    <iais:row cssClass="form-check-gp" id="nextStageRow">
        <p class="form-check-title">What information do you want to submit? </p>
        <iais:select name="nextStage" firstOption="Please Select" id="nextStage"
                     options="newCycleOpts"
                     value="${arSuperDataSubmissionDto.selectionDto.stage}"/>
    </iais:row>
    <iais:row cssClass="form-check-gp" id="nextOffStageRow">
        <p class="form-check-title">What information do you want to submit? </p>
        <iais:select name="nextNunCycleStage" firstOption="Please Select" id="nextNunCycleStage"
                     options="offCycleOps"
                     value="${arSuperDataSubmissionDto.selectionDto.stage}"/>
    </iais:row>
</div>

<div id="registerPatientSection" style="margin-left: -15px;">
    <%@include file="registerPatientSection.jsp" %>
</div>

<iais:confirm msg="${ageMsg}" callBack="$('#ageMsgDiv').modal('hide');" popupOrder="ageMsgDiv" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />
<iais:confirm msg="${hbdAgeMsg}" callBack="$('#hbdAgeMsgDiv').modal('hide');" popupOrder="hbdAgeMsgDiv" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />

