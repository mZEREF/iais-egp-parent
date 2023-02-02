<c:set var="preTerminationDto" value="${terminationOfPregnancyDto.preTerminationDto}"/>
<c:if test="${preTerminationDto.secCounsellingResult !='TOPSP001' && preTerminationDto.secCounsellingResult !='TOPSP003' && preTerminationDto.counsellingResult!='TOPPCR003'}">
    <c:set var="terminationOfPregnancyDto" value="${topSuperDataSubmissionDto.terminationOfPregnancyDto}"/>
    <c:set var="doctorInformationDto" value="${topSuperDataSubmissionDto.doctorInformationDto}"/>
    <c:set var="terminationDto" value="${terminationOfPregnancyDto.terminationDto}"/>
    <%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
    <div class="form-horizontal patientPatails">
        <iais:row>
            <iais:field width="5" value="Type of Termination of Pregnancy" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select name="topType" firstOption="Please Select" id="topType"
                             codeCategory="TOP_TYPE_TERMINATION_PREGNANCY"
                             value="${terminationDto.topType}" cssClass="topType"/>
            </iais:value>
        </iais:row>
        <div id="spTypes"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Type of Surgical Procedure" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="spType" firstOption="Please Select" id="spType"
                                 codeCategory="TOP_TYPE_OF_SURGICAL_PROCEDURE"
                                 value="${terminationDto.spType}" cssClass="spType"/>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_spType"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="otherSpTypes"
             <c:if test="${terminationDto.spType!='TOPTSP003' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003')}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Other Type of Surgical Procedure" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="otherSpType" id="otherSpType"
                                value="${terminationDto.otherSpType}"/>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_otherSpType"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="anTypes"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Type of Anaesthesia" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="anType" firstOption="Please Select" codeCategory="TOP_TYPE_OF_ANAESTHESIA"
                                 value="${terminationDto.anType}" cssClass="anType" id="anType"/>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_anType"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="otherAnTypes"
             <c:if test="${terminationDto.anType!='TOPTA004' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003')}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Other Type of Anaesthesia" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="otherAnType" id="otherAnType"
                                value="${terminationDto.otherAnType}"/>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_otherAnType"></span>
                </iais:value>
            </iais:row>
        </div>
        <div id="drugTypes"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Type of Drug" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:select name="drugType" firstOption="Please Select" id="drugType"
                                 codeCategory="TOP_TYPE_OF_DRUG"
                                 value="${terminationDto.drugType}" cssClass="drugType"/>
                </iais:value>
            </iais:row>
        </div>
        <div id="otherDrugTypes"
             <c:if test="${terminationDto.drugType!='TOPTOD005' || (terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002')}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Other Type of Drug" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="100" type="text" name="otherDrugType" id="otherDrugType"
                                value="${terminationDto.otherDrugType}"/>
                </iais:value>
            </iais:row>
        </div>
        <iais:row>
            <iais:field width="5" value="Result of Termination of Pregnancy - Any Complications" mandatory="true"/>
            <iais:value width="3" cssClass="col-md-3">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="complicationForOperRslt"
                           value="1"
                           id="complicationForOperRsltYes"
                           <c:if test="${terminationDto.complicationForOperRslt}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="complicationForOperRsltYes"><span
                            class="check-circle"></span>Yes</label>
                </div>
                <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_complicationForOperRslt"></span>
            </iais:value>
            <iais:value width="4" cssClass="col-md-4">
                <div class="form-check">
                    <input class="form-check-input"
                           type="radio"
                           name="complicationForOperRslt"
                           value="0"
                           id="complicationForOperRsltNo"
                           <c:if test="${terminationDto.complicationForOperRslt == false}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="complicationForOperRsltNo"><span
                            class="check-circle"></span>No</label>
                </div>
            </iais:value>
        </iais:row>
        <div id="ariseOperationComplications"
             <c:if test="${terminationDto.complicationForOperRslt != true}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Complications Arising From Operation" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="66" type="text" name="ariseOperationComplication"
                                value="${terminationDto.ariseOperationComplication}"/>
                </iais:value>
            </iais:row>
        </div>
        <iais:row>
            <c:set var="toolMsg"><iais:message key="DS_MSG015" paramKeys="1" paramValues="counsellor"/></c:set>
            <iais:field width="5" value="Date of Termination of Pregnancy" mandatory="true" info="${toolMsg}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:datePicker name="topDate" value="${terminationDto.topDate}"/>
            </iais:value>
        </iais:row>
        <div id="performedOwns"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Is Surgical Termination of Pregnancy Performed in Own Premises?"
                            mandatory="true"/>
                <iais:value width="3" cssClass="col-md-3">
                    <div class="form-check">
                        <input class="form-check-input"
                               type="radio"
                               name="performedOwn"
                               value="1"
                               id="performedOwnYes"
                               <c:if test="${terminationDto.performedOwn}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="performedOwnYes"><span
                                class="check-circle"></span>Yes</label>
                    </div>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_performedOwn"></span>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                    <div class="form-check">
                        <input class="form-check-input"
                               type="radio"
                               name="performedOwn"
                               value="0"
                               id="performedOwnNo"
                               <c:if test="${terminationDto.performedOwn == false}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="performedOwnNo"><span
                                class="check-circle"></span>No</label>
                    </div>
                </iais:value>
            </iais:row>
        </div>
        <div id="topPlacelaceLabel"
             <c:if test="${(terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP003') || terminationDto.performedOwn==null}">style="display: none"</c:if>>
            <div id="topPlaceYes"
                 <c:if test="${terminationDto.performedOwn == null || terminationDto.performedOwn == false}">style="display: none"</c:if>>
                <iais:row cssClass="topPlace">
                    <iais:field width="5" value="Place of Surgical Termination of Pregnancy" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="topPlace">
                        ${topSuperDataSubmissionDto.premisesDto.premiseLabel}
                    </iais:value>
                </iais:row>
            </div>
            <div id="topPlaceNo"
                 <c:if test="${terminationDto.performedOwn == null || terminationDto.performedOwn == true}">style="display: none"</c:if>>
                <iais:row>
                    <iais:field width="5" value="Place of Surgical Termination of Pregnancy" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7 partial-search-container">
                        <iais:select name="topPlace" id="topPlacePerformedOwnNo" options="TopPlace"
                                     value="${terminationDto.topPlace}" cssClass="topPlace"/>
                    </iais:value>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_topPlace"></span>
                </iais:row>
            </div>
        </div>
        <div id="pregnancyOwns"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Is Drug for Termination of Pregnancy Prescribed in Own Premises?"
                            mandatory="true"/>
                <iais:value width="3" cssClass="col-md-3">
                    <div class="form-check">
                        <input class="form-check-input"
                               type="radio"
                               name="pregnancyOwn"
                               value="1"
                               id="pregnancyOwnYes"
                               <c:if test="${terminationDto.pregnancyOwn == true}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="pregnancyOwnYes"><span
                                class="check-circle"></span>Yes</label>
                    </div>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_pregnancyOwn"></span>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                    <div class="form-check">
                        <input class="form-check-input"
                               type="radio"
                               name="pregnancyOwn"
                               value="0"
                               id="pregnancyOwnNo"
                               <c:if test="${terminationDto.pregnancyOwn == false}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="pregnancyOwnNo"><span
                                class="check-circle"></span>No</label>
                    </div>
                </iais:value>
            </iais:row>
        </div>
        <div id="prescribeTopPlaceLabel"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
            <div id="prescribeTopPlace"
                 <c:if test="${terminationDto.pregnancyOwn == false}">style="display: none"</c:if>>
                <iais:row cssClass="topPlace">
                    <iais:field width="5" value="Place where Drug for Termination of Pregnancy was Prescribed"
                                mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="prescribeTopPlace">
                        ${topSuperDataSubmissionDto.premisesDto.premiseLabel}
                    </iais:value>
                </iais:row>
            </div>
            <div id="prescribeTopPlaces"
                 <c:if test="${terminationDto.pregnancyOwn == null || terminationDto.pregnancyOwn == true}">style="display: none"</c:if>>
                <iais:row>
                    <iais:field width="5" value="Place where Drug for Termination of Pregnancy was Prescribed"
                                mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7 partial-search-container">
                        <iais:select name="prescribeTopPlace" id="prescribeTopPlaceNo" options="TopPlace"
                                     value="${terminationDto.prescribeTopPlace}"
                                     cssClass="prescribeTopPlace"/>
                    </iais:value>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_prescribeTopPlace"></span>
                </iais:row>
            </div>
        </div>
        <div id="takenOwns"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Is Drug for Termination of Pregnancy Taken in Own Premises?"
                            mandatory="true"/>
                <iais:value width="3" cssClass="col-md-3">
                    <div class="form-check">
                        <input class="form-check-input"
                               type="radio"
                               name="takenOwn"
                               value="1"
                               id="takenOwnYes"
                               <c:if test="${terminationDto.takenOwn == true}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="takenOwnYes"><span
                                class="check-circle"></span>Yes</label>
                    </div>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_takenOwn"></span>
                </iais:value>
                <iais:value width="4" cssClass="col-md-4">
                    <div class="form-check">
                        <input class="form-check-input"
                               type="radio"
                               name="takenOwn"
                               value="0"
                               id="takenOwnNo"
                               <c:if test="${terminationDto.takenOwn == false}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="takenOwnNo"><span
                                class="check-circle"></span>No</label>
                    </div>
                </iais:value>
            </iais:row>
        </div>
        <div id="takenOwnLabel"
             <c:if test="${terminationDto.topType !='TOPTTP001' && terminationDto.topType !='TOPTTP002'}">style="display: none"</c:if>>
            <div id="topDrugPlace" <c:if test="${terminationDto.takenOwn == false}">style="display: none"</c:if>>
                <iais:row cssClass="topDrugPlace">
                    <iais:field width="5" value="Place where Drug for Termination of Pregnancy was Taken"
                                mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="topDrugPlace">
                        ${topSuperDataSubmissionDto.premisesDto.premiseLabel}
                    </iais:value>
                </iais:row>
            </div>
            <div id="topDrugPlaces"
                 <c:if test="${terminationDto.takenOwn == null || terminationDto.takenOwn == true}">style="display: none"</c:if>>
                <iais:row>
                    <iais:field width="5" value="Place where Drug for Termination of Pregnancy was Taken"
                                mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7 partial-search-container">
                        <iais:select name="topDrugPlace" options="TopDrugPlace" id="otherTopDrugPlace"
                                     value="${terminationDto.topDrugPlace}"
                                     cssClass="topDrugPlace"/>
                    </iais:value>
                    <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_topDrugPlace"></span>
                </iais:row>
                <div id="otherTopDrugPlaces"
                     <c:if test="${terminationDto.topDrugPlace!='AR_SC_001'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Other Places where Drug for Termination of Pregnancy is used"
                                    mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="100" type="text" name="otherTopDrugPlace" id="otherTopDrugPlaceText"
                                        value="${terminationDto.otherTopDrugPlace}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
        <iais:row>
            <iais:value width="10" cssClass="col-md-10">
                <strong style="font-size: 2.0rem;">Doctor who Performed the Termination of Pregnancy</strong>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="3" value="Doctor's Professional Regn / MCR No." mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7" style="width: 380px;">
                <iais:input maxLength="20" type="text" id="doctorRegnNo" name="doctorRegnNo"
                            value="${terminationDto.doctorRegnNo}" onchange="clearDockerSelection()"/>
                <span id="doctorRegnNoMsg" name="iaisErrorMsg" class="error-msg"></span>
            </iais:value>
            <iais:value width="3" cssClass="col-md-3" display="true">
                <a class="ValidateDoctor" onclick="validateDoctors()">
                    Validate Doctor
                </a>
                <%--<div>
                    <span id="msg" name="iaisErrorMsg" class="error-msg"></span>
                </div>--%>
            </iais:value>
        </iais:row>
        <div id="doctorInformation"
             <c:if test="${terminationDto.topDoctorInformations eq 'true'}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Name of Doctor"/>
                <iais:value width="7" cssClass="col-md-7" display="true" id="names">
                    ${terminationDto.doctorName}
                </iais:value>
            </iais:row>
            <div id="doctorInformationPrs"
                 <c:if test="${terminationDto.doctorInformationPE eq 'true'}">style="display: none"</c:if>>
                <iais:row>
                    <iais:field width="5" value="Specialty"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="specialty">
                        ${terminationDto.specialty}
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Sub-Specialty"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="subSpecialty">
                        ${terminationDto.subSpecialty}
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Qualification"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="qualification">
                        ${terminationDto.qualification}
                    </iais:value>
                </iais:row>
            </div>
            <div id="doctorInformationElis"
                 <c:if test="${terminationDto.doctorInformationPE eq 'false' || terminationDto.doctorInformationPE eq null}">style="display: none"</c:if>>
                <iais:row>
                    <iais:field width="5" value="Specialty" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:input maxLength="1024" type="text" id="dSpecialitys" name="dSpecialitys"
                                    value="${terminationDto.specialty}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_dSpecialitys"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Sub-Specialty" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:input maxLength="1024" type="text" id="dSubSpecialitys" name="dSubSpecialitys"
                                    value="${terminationDto.subSpecialty}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_dSubSpecialitys"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Qualification" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:input maxLength="1024" type="text" id="dQualifications" name="dQualifications"
                                    value="${terminationDto.qualification}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_dQualifications"></span>
                    </iais:value>
                </iais:row>
            </div>
        </div>
        <div id="doctorInformationText"
             <c:if test="${terminationDto.topDoctorInformations eq 'false' || terminationDto.topDoctorInformations eq null}">style="display: none"</c:if>>
            <iais:row>
                <iais:field width="5" value="Name of Doctor" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <iais:input maxLength="66" type="text" id="dNameText" name="dName"
                                value="${doctorInformationDto.name}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_dName"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Specialty" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <iais:input maxLength="1024" type="text" id="dSpecialityText" name="dSpeciality"
                                value="${doctorInformationDto.speciality}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_dSpeciality"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Sub-Specialty" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <iais:input maxLength="1024" type="text" id="dSubSpecialityText" name="dSubSpeciality"
                                value="${doctorInformationDto.subSpeciality}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_dSubSpeciality"></span>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Qualification" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <iais:input maxLength="1024" type="text" id="dQualificationText" name="dQualification"
                                value="${doctorInformationDto.qualification}"/>
                    <span class="error-msg" name="iaisErrorMsg" id="error_dQualification"></span>
                </iais:value>
            </iais:row>
        </div>
        <iais:row>
            <iais:field width="5" value="Other Qualification"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <iais:input maxLength="100" type="text" name="otherQualification"
                            value="${terminationDto.otherQualification}"/>
            </iais:value>
        </iais:row>
    </div>
</c:if>
<input type="hidden" name="docSource" value="TOP"/>
<input type="hidden" name="hciCode" value="${topSuperDataSubmissionDto.hciCode}"/>
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsErrorMsg">
                            <iais:message key="DS_MSG011" escape="false"/>
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                        data-dismiss="modal" onclick="cancels()">CLOSE
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="ELIS_SERVICE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="elisMsg">
                            <iais:message key="GENERAL_ERR0063" escape="false"/>
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                        data-dismiss="modal" onclick="cancels()">CLOSE
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="PRS_SERVICE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsMsg">
                            <iais:message key="GENERAL_ERR0064" escape="false"/>
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                        data-dismiss="modal" onclick="cancels()">CLOSE
                </button>
            </div>
        </div>
    </div>
</div>
<%--<div class="modal fade" id="NO_PRS_ELIS_SERVICE" role="dialog" aria-labelledby="myModalLabel">--%>
<%--    <div class="modal-dialog modal-dialog-centered" role="document">--%>
<%--        <div class="modal-content">--%>
<%--            <div class="modal-body">--%>
<%--                <div class="row">--%>
<%--                    <div class="col-md-12">--%>
<%--                        <span style="font-size: 2rem;" id="noMsg">--%>
<%--                            <iais:message key="GENERAL_ERR0065" escape="false"/>--%>
<%--                        </span>--%>
<%--                    </div>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div class="row " style="margin-top: 5%;margin-bottom: 5%">--%>
<%--                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"--%>
<%--                        data-dismiss="modal" onclick="cancels()">CLOSE--%>
<%--                </button>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</div>--%>
<div class="modal fade" id="PRS_CLOSE" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsCloseMsg">
                            <iais:message key="GENERAL_ERR0066" escape="false"/>
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                        data-dismiss="modal" onclick="cancels()">CLOSE
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="PRS_PRN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        <span style="font-size: 2rem;" id="prsPrn">
                            <iais:message key="GENERAL_ERR0054" escape="false"/>
                        </span>
                    </div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6"
                        data-dismiss="modal" onclick="cancels()">CLOSE
                </button>
            </div>
        </div>
    </div>
</div>
<div class="doctorNameSelectionHidden">
    <input type="hidden" name="names" id="doctorNameHidden" value="${terminationDto.doctorName}">
    <input type="hidden" name="specialty" id="specialtyHidden" value="${terminationDto.specialty}">
    <input type="hidden" name="subSpecialty" id="subSpecialtyHidden" value="${terminationDto.subSpecialty}">
    <input type="hidden" name="qualification" id="qualificationHidden" value="${terminationDto.qualification}">
</div>
<input type="hidden" name="topDoctorInformations" id="topDoctorInformations"
       value="${terminationDto.topDoctorInformations}">
<input type="hidden" name="doctorInformationPE" id="doctorInformationPE" value="${terminationDto.doctorInformationPE}">
<script>
    $(document).ready(function () {
        $('#topType').change(function () {
            topType();

            spTypes();

            performedOwns();

            pregnancyOwns();

            prescribeTopPlaceLabel();

            takenOwns();

            takenOwnLabel();

            anTypes();
        });
        $('#anType').change(function () {
            anType();
        });
        $('#spType').change(function () {
            spType();
        });
        $('#drugType').change(function () {
            drugType();
        });
        $('input[name=complicationForOperRslt]').change(function () {
            complicationForOperRslt();
        });
        $('input[name=performedOwn]').change(function () {
            topPlacelaceLabel();
            performedOwn();
        });
        $('input[name=pregnancyOwn]').change(function () {
            pregnancyOwn();
        });
        $('input[name=takenOwn]').change(function () {
            takenOwn();
        });
        $('#otherTopDrugPlace').change(function () {
            otherTopDrugPlace();
        });
        if ("1" == $('#showValidatePT').val()) {
            $('#PRS_SERVICE_DOWN').modal('show');
        }


    });

    function spTypes() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP003") {
            $('#spTypes').show();
        } else {
            $('#spTypes').hide();
            $('#otherSpTypes').hide();
            fillValue($('#spTypes'), null);
            $('#otherSpType').val(null);
        }
    }

    function performedOwns() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP003") {
            $('#performedOwns').show();
        } else {
            $('#performedOwns').hide();
            fillValue($('input[name=performedOwn]').removeAttr('checked'));
            $('#topPlacelaceLabel').hide();
        }
    }

    function topPlacelaceLabel() {
        /*if ($('input[name=performedOwn]').prop('checked')) {
            $('#topPlacelaceLabel').show();
        }*/
        if ($('#performedOwnYes').prop('checked')) {
            $('#topPlacelaceLabel').show();
        } else if ($('#performedOwnNo').prop('checked')) {
            $('#topPlacelaceLabel').show();
        }

    }

    function pregnancyOwns() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP002") {
            $('#pregnancyOwns').show();
        } else {
            $('#pregnancyOwns').hide();
        }
    }

    function prescribeTopPlaceLabel() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP002") {
            $('#prescribeTopPlaceLabel').show();
        } else {
            $('#prescribeTopPlaceLabel').hide();
        }
    }

    function takenOwns() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP002") {
            $('#takenOwns').show();
        } else {
            $('#takenOwns').hide();
        }
    }

    function takenOwnLabel() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP002") {
            $('#takenOwnLabel').show();
        } else {
            $('#takenOwnLabel').hide();
            $('#otherTopDrugPlaceText').val(null);
            fillValue($('#otherTopDrugPlace'), null);
        }
    }

    function anType() {
        var anType = $('#anType').val();
        if (anType == "TOPTA004") {
            $('#otherAnTypes').show();
        } else {
            $('#otherAnTypes').hide();
            $('#otherAnType').val(null);
        }
    }

    function spType() {
        var spType = $('#spType').val();
        if (spType == "TOPTSP003") {
            $('#otherSpTypes').show();
        } else {
            $('#otherSpTypes').hide();
            $('#otherSpType').val(null);
        }
    }

    function anTypes() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP003") {
            $('#anTypes').show();
        } else {
            $('#anTypes').hide();
            $('#otherAnTypes').hide();
            fillValue($('#anTypes'), null);
            $('#otherAnType').val(null);
        }
    }

    function topType() {
        var topType = $('#topType').val();
        if (topType == "TOPTTP001" || topType == "TOPTTP002") {
            $('#pregnancyOwnYes').prop('checked',true);
            $('#prescribeTopPlace').show();
            $('#prescribeTopPlaces').hide();
            $('#takenOwnYes').prop('checked',true);
            $('#topDrugPlace').show();
            $('#topDrugPlaces').hide();
            $('#drugTypes').show();
        } else {
            $('#drugTypes').hide();
            $('#otherDrugTypes').hide();
            fillValue($('#drugTypes'), null);
            $('#otherDrugType').val(null);
        }
    }

    function drugType() {
        var drugType = $('#drugType').val();
        if (drugType == "TOPTOD005") {
            $('#otherDrugTypes').show();
        } else {
            $('#otherDrugTypes').hide();
            $('#otherDrugType').val(null);
        }
    }

    function complicationForOperRslt() {
        if ($('#complicationForOperRsltYes').prop('checked')) {
            $('#ariseOperationComplications').show();
        }
        if ($('#complicationForOperRsltNo').prop('checked')) {
            $('#ariseOperationComplications').hide();
        }
    }

    function performedOwn() {
        console.log("p");
        if ($('#performedOwnYes').prop('checked')) {
            console.log("yes");
            $('#topPlaceYes').show();
        } else {
            $('#topPlaceYes').hide();
        }
        if ($('#performedOwnNo').prop('checked')) {
            console.log("no");
            $('#topPlaceNo').show();
        } else {
            $('#topPlaceNo').hide();
        }
    }

    function pregnancyOwn() {
        if ($('#pregnancyOwnYes').prop('checked')) {
            $('#prescribeTopPlace').show();
            $('#prescribeTopPlaces').hide();
        }
        if ($('#pregnancyOwnNo').prop('checked')) {
            $('#prescribeTopPlaces').show();
            $('#prescribeTopPlace').hide();
        }
    }

    function takenOwn() {
        if ($('#takenOwnYes').prop('checked')) {
            $('#topDrugPlace').show();
            $('#topDrugPlaces').hide();

        }
        if ($('#takenOwnNo').prop('checked')) {
            $('#topDrugPlaces').show();
            $('#topDrugPlace').hide();
        }
    }

    function otherTopDrugPlace() {
        var otherTopDrugPlace = $('#otherTopDrugPlace').val();
        if (otherTopDrugPlace == "AR_SC_001") {
            $('#otherTopDrugPlaces').show();
        } else {
            $('#otherTopDrugPlaces').hide();
        }
    }

    function validateDoctors() {
        console.log('loading info ...');
        showWaiting();
        var prgNo = $('input[name="doctorRegnNo"]').val();
        if (prgNo == "" || prgNo == null || prgNo == undefined) {
            clearPrsInfo();
            dismissWaiting();
            clearErrorMsg();
            $('#doctorRegnNoMsg').text('This is a mandatory field.');
            return;
        }
        var no = $('input[name="doctorRegnNo"]').val();
        var docSource = $('input[name="docSource"]').val();
        const hciCode = $('input[name="hciCode"]').val();
        var jsonData = {
            'prgNo': no,
            'docSource': docSource,
            'hciCode': hciCode
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/doc/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if (isEmpty(data.selection) && isEmpty(data.selections)) {
                    clearPrsInfoText();
                    $('#topDoctorInformations').val(true);
                    console.log("The return data is null");
                    $('#doctorInformationText').show();
                    $('#doctorInformation').hide();
                    console.log("1");
                } else if (isEmpty(data.selection) && isEmpty(!data.selections)) {
                    clearPrsInfoElis();
                    $('#doctorInformationPE').val(true);
                    $('#ELIS_SERVICE').modal('show');
                    $('#doctorInformationElis').show();
                    $('#doctorInformationPrs').hide();
                    console.log("2");
                } else if (isEmpty(data.selections) && ('-1' == data.selection.statusCode || '-2' == data.selection.statusCode)) {
                    clearPrsInfoText();
                    $('#topDoctorInformations').val(true);
                    console.log("The return data is null");
                    $('#doctorInformationText').show();
                    $('#doctorInformation').hide();
                    // $('#NO_PRS_ELIS_SERVICE').modal('show');
                    console.log("3");
                } else if (isEmpty(!data.selection)) {
                    $('#topDoctorInformations').val(false);
                    loadingSp(data);
                    console.log("4");
                    if ('-1' == data.selection.statusCode || '-2' == data.selection.statusCode) {
                        clearPrsInfoElis();
                        $('#doctorInformationPE').val(true);
                        // $('#ELIS_SERVICE').modal('show');
                        $('#doctorInformationElis').show();
                        $('#doctorInformationPrs').hide();
                        console.log("5");
                    } else if (isEmpty(data.selections) && data.selection.hasException == false) {
                        $('#doctorInformationPE').val(false);
                        // $('#PRS_SERVICE').modal('show');
                        $('#doctorInformationElis').hide();
                        $('#doctorInformationPrs').show();
                        console.log("6");
                    } else if (data.selection.hasException && !isEmpty(data.selections)) {
                        clearPrsInfoElis();
                        $('#doctorInformationPE').val(true);
                        $('#PRS_CLOSE').modal('show');
                        $('#doctorInformationElis').show();
                        $('#doctorInformationPrs').hide();
                        console.log("7");
                    } else if (data.selection.hasException && isEmpty(data.selections)) {
                        clearPrsInfoText();
                        $('#topDoctorInformations').val(true);
                        $('#PRS_CLOSE').modal('show');
                        $('#doctorInformation').hide();
                        $('#doctorInformationText').show();
                        console.log("8");
                    } else if ('401' == data.selection.statusCode) {
                        $('#topDoctorInformations').val(true);
                        $('#PRS_PRN').modal('show');
                        $('#doctorInformation').hide();
                        $('#doctorInformationText').show();
                        console.log("9");
                    } else if (!isEmpty(data.selections)) {

                        $('#doctorInformationPE').val(false);
                        $('#doctorInformationElis').hide();
                        $('#doctorInformationPrs').show();
                        console.log("10");
                    }
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

    function cancels() {
        $('#PRS_SERVICE_DOWN').modal('hide');
        $('#ELIS_SERVICE').modal('hide');
        $('#PRS_SERVICE').modal('hide');
        // $('#NO_PRS_ELIS_SERVICE').modal('hide');
        $('#PRS_CLOSE').modal('hide');
        $('#PRS_PRN').modal('hide');
    }

    var clearPrsInfo = function () {
        $('#names').find('p').text('');
        $('#specialty').find('p').text('');
        $('#subSpecialty').find('p').text('');
        $('#qualification').find('p').text('');
    };
    var clearPrsInfoElis = function () {
        $('#dSpecialitys').val(null);
        $('#dSubSpecialitys').val(null);
        $('#dQualifications').val(null);
    };
    var clearPrsInfoText = function () {
        $('#dNameText').val(null);
        $('#dSpecialityText').val(null);
        $('#dSubSpecialityText').val(null);
        $('#dQualificationText').val(null);
    };

    function loadingSp(data) {
        $('#doctorInformationText').hide();
        $('#doctorInformation').show();
        const name = data.selection.name;
        console.log(name);
        $('#names').find('p').text(name);
        $('#doctorNameHidden').val(name);

        $('#specialty').find('p').text(data.selection.specialty);
        $('#specialtyHidden').val(data.selection.specialty);

        $('#subSpecialty').find('p').text(data.selection.subspecialty);
        $('#subSpecialtyHidden').val(data.selection.subspecialty);

        $('#qualification').find('p').text(data.selection.qualification);
        $('#qualificationHidden').val(data.selection.qualification);
    }

    function clearDockerSelection() {
        console.log("clearDockerSelection!")
        clearErrorMsg();
        $('#names').find('p').text('');
        $('#specialty').find('p').text('');
        $('#subSpecialty').find('p').text('');
        $('#qualification').find('p').text('');
        clearFields('.doctorNameSelectionHidden');
    }

    $(document).ready(function () {
        // Initialize select2
        $("#topPlacePerformedOwnNo").select2({
            matcher: dropdownFilterShowMatchOnly
        });
        $("#prescribeTopPlaceNo").select2({
            matcher: dropdownFilterShowMatchOnly
        });
        <c:if test="${!(terminationDto.pregnancyOwn == null || terminationDto.pregnancyOwn == true)}">
        $("#prescribeTopPlaceNo").select2({
            matcher: dropdownFilterShowMatchOnly
        });
        </c:if>
        $("#otherTopDrugPlace").select2({
            matcher: dropdownFilterShowMatchOnly
        });
        $('.select2-container--default').attr('style', 'width:100%');
        $('.select2-search__field').attr('maxLength', '100');
    });

</script>
