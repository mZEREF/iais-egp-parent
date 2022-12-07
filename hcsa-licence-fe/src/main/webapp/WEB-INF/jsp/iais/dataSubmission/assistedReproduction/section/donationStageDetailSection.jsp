<%@ page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>

<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Donation
            </strong>
        </h4>
    </div>
    <div id="donationDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field cssClass="col-md-6" value="Is the sample donated locally or from overseas?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-6" >
                        <div class="form-check col-md-6" style="padding-left: 0;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="localOrOversea"
                                   value="1"
                                   id="local"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.localOrOversea == 1}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="local"><span
                                    class="check-circle"></span>Local</label>
                        </div>
                        <div class="form-check col-md-6" style="padding-left: 0;">
                            <input class="form-check-input"
                                   type="radio"
                                   name="localOrOversea"
                                   value="0"
                                   id="oversea"
                                   <c:if test="${ arSuperDataSubmissionDto.donationStageDto.localOrOversea == 0}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="oversea"><span
                                    class="check-circle"></span>Overseas</label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_localOrOversea"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <label class="col-xs-6 col-md-6 control-label">What was Donated? <span class="mandatory">*</span>
                        <a id="frozenSpermSelected" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                           title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>"
                           style="z-index: 10"
                           data-original-title="">i</a>
                    </label>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="donatedType" id="donatedType" firstOption="Please Select" codeCategory="CATE_ID_DONATED_TYPE"
                                         value="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_donatedType"></span>
                        </iais:value>
                </iais:row>
                <c:set var="donatedType" value="${arSuperDataSubmissionDto.donationStageDto.donatedType}"/>
                <div id="displayOocyteDonorPatient" <c:if test="${donatedType != 'DONTY001' && donatedType != 'DONTY002' && donatedType != 'DONTY003'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Is the Oocyte Donor the Patient?" mandatory="true"/>
                        <iais:value cssClass="col-md-3" >
                            <div class="form-check" style="padding-left: 0;">
                                <input class="form-check-input"
                                       type="radio"
                                       name="isOocyteDonorPatient"
                                       value="1"
                                       id="isOocyteDonorPatientYes"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient eq 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isOocyteDonorPatientYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_oocyteDonorPatient"></span>
                        </iais:value>
                        <iais:value cssClass="col-md-3" >
                            <div class="form-check" style="padding-left: 0;">
                                <input class="form-check-input" type="radio"
                                       name="isOocyteDonorPatient"
                                       value="0"
                                       id="isOocyteDonorPatientNo"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient eq 0}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isOocyteDonorPatientNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                        </iais:value>
                    </iais:row>
                    <div id="displayIsFemaleIdentityKnown" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isOocyteDonorPatient != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Is the Female Donor's Identity Known" mandatory="true"/>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isFemaleIdentityKnown"
                                           value="1"
                                           id="isFemaleIdentityKnownYes"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown eq 1}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="isFemaleIdentityKnownYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_isFemaleIdentityKnown"></span>
                            </iais:value>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input" type="radio"
                                           name="isFemaleIdentityKnown"
                                           value="0"
                                           id="isFemaleIdentityKnownNo"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown eq 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="isFemaleIdentityKnownNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemaleHaveNricFin" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isFemaleIdentityKnown != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Does the Female Donor have a NRIC/FIN number?" mandatory="true"/>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="femaleIdType"
                                           value="1"
                                           id="femaleIdTypeYes"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType eq 1}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="femaleIdTypeYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_femaleIdType"></span>
                            </iais:value>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input" type="radio"
                                           name="femaleIdType"
                                           value="0"
                                           id="femaleIdTypeNo"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType eq 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="femaleIdTypeNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemaleNricFinNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType != 1}">style="display: none"</c:if>>
                        <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Female Donor's NRIC/FIN Number" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="20" type="text" name="femaleIdNumber" id="femaleIdNumber" value="${arSuperDataSubmissionDto.donationStageDto.femaleIdNumber}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_femaleIdNumber"></span>
                        </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayFemalePassportNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.femaleIdType != 0}">style="display: none"</c:if>>
                        <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Female Donor's Passport Number" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="20" type="text" name="femaleIdNumberPassport" id="femaleIdNumberPassport" value="${arSuperDataSubmissionDto.donationStageDto.femaleIdNumber}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_femaleIdNumberPassport"></span>
                        </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Female Donor Sample Code" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="20" type="text" name="femaleDonorSampleCode" id="femaleDonorSampleCode" value="${arSuperDataSubmissionDto.donationStageDto.femaleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Age of Female Donor at the Point of Donation" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="femaleDonorAge" id="femaleDonorAge" value="${arSuperDataSubmissionDto.donationStageDto.femaleDonorAgeStr==null?arSuperDataSubmissionDto.donationStageDto.femaleDonorAge:arSuperDataSubmissionDto.donationStageDto.femaleDonorAgeStr}"/>
                        </iais:value>
                    </iais:row>
                </div>

                <div id="displaySpermDonorPatient" <c:if test="${donatedType != 'DONTY003' && donatedType != 'DONTY004' && donatedType != 'DONTY005'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Is the Sperm Donor the Patient's Husband?" mandatory="true"/>
                        <iais:value cssClass="col-md-3" >
                            <div class="form-check" style="padding-left: 0;">
                                <input class="form-check-input"
                                       type="radio"
                                       name="isSpermDonorPatient"
                                       value="1"
                                       id="isSpermDonorPatientYes"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient eq 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isSpermDonorPatientYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_spermDonorPatient"></span>
                        </iais:value>
                        <iais:value cssClass="col-md-3" >
                            <div class="form-check" style="padding-left: 0;">
                                <input class="form-check-input" type="radio"
                                       name="isSpermDonorPatient"
                                       value="0"
                                       id="isSpermDonorPatientNo"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient eq 0}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="isSpermDonorPatientNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                        </iais:value>
                    </iais:row>
                    <div id="displayIsMaleIdentityKnown" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isSpermDonorPatient != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Is the Male Donor's Identity Known?" mandatory="true"/>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="isMaleIdentityKnown"
                                           value="1"
                                           id="isMaleIdentityKnownYes"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown eq 1}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="isMaleIdentityKnownYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_isMaleIdentityKnown"></span>
                            </iais:value>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input" type="radio"
                                           name="isMaleIdentityKnown"
                                           value="0"
                                           id="isMaleIdentityKnownNo"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown eq 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="isMaleIdentityKnownNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMaleHaveNricFin" <c:if test="${arSuperDataSubmissionDto.donationStageDto.isMaleIdentityKnown != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Does the Male Donor have a NRIC/FIN number?" mandatory="true"/>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input"
                                           type="radio"
                                           name="maleIdType"
                                           value="1"
                                           id="maleIdTypeYes"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType eq 1}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="maleIdTypeYes"><span
                                            class="check-circle"></span>Yes</label>
                                </div>
                                <span class="error-msg" name="iaisErrorMsg" id="error_maleIdType"></span>
                            </iais:value>
                            <iais:value cssClass="col-md-3" >
                                <div class="form-check" style="padding-left: 0;">
                                    <input class="form-check-input" type="radio"
                                           name="maleIdType"
                                           value="0"
                                           id="maleIdTypeNo"
                                           <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType eq 0}">checked</c:if>
                                           aria-invalid="false">
                                    <label class="form-check-label"
                                           for="maleIdTypeNo"><span
                                            class="check-circle"></span>No</label>
                                </div>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMaleNricFinNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType != 1}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Male Donor's NRIC/FIN Number" mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <iais:input maxLength="20" type="text" name="maleIdNumber" id="maleIdNumber" value="${arSuperDataSubmissionDto.donationStageDto.maleIdNumber}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_maleIdNumber"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <div id="displayMalePassportNumber" <c:if test="${arSuperDataSubmissionDto.donationStageDto.maleIdType != 0}">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Male Donor's Passport Number" mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <iais:input maxLength="20" type="text" name="maleIdNumberPassport" id="maleIdNumberPassport" value="${arSuperDataSubmissionDto.donationStageDto.maleIdNumber}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_maleIdNumberPassport"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Male Donor Sample Code" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="20" type="text" name="maleDonorSampleCode" id="maleDonorSampleCode" value="${arSuperDataSubmissionDto.donationStageDto.maleDonorSampleCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Age of Male Donor at the Point of Donation" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="maleDonorAge" id="maleDonorAge" value="${arSuperDataSubmissionDto.donationStageDto.maleDonorAgeStr==null?arSuperDataSubmissionDto.donationStageDto.maleDonorAge:arSuperDataSubmissionDto.donationStageDto.maleDonorAgeStr}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="sampleFromLocal" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.localOrOversea != 1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Donated to" id="donatedCentreField" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <select name="donatedCentre" id="donatedCentre" class="donatedCentreSel">
                                <c:forEach items="${curCenDonatedSelectOption}" var="selectOption">
                                    <option value="${selectOption.value}" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedCentre ==selectOption.value}">selected="selected"</c:if>>${selectOption.text}</option>
                                </c:forEach>
                            </select>
                            <span class="error-msg" name="iaisErrorMsg" id="error_donatedCentre"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="sampleFromOversea" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.localOrOversea != 0}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Which Institution was the Sample Donated From?" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="256" value="${arSuperDataSubmissionDto.donationStageDto.overseaDonatedCentre}" name="overseaDonatedCentre" id="overseaDonatedCentre">
                            <span class="error-msg" name="iaisErrorMsg" id="error_overseaDonatedCentre"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Reason for Donation" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="donationReason"  name="donationReason" firstOption="Please Select" options="donationReasonSelectOption" value="${arSuperDataSubmissionDto.donationStageDto.donationReason}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donationReason"></span>
                    </iais:value>
                </iais:row>
                <div id="otherDonationReasonDisplay" <c:if test="${arSuperDataSubmissionDto.donationStageDto.donationReason!='DONRES004'}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Other Reason for Donation" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="text" maxlength="100"   name="otherDonationReason" value="${arSuperDataSubmissionDto.donationStageDto.otherDonationReason}" >
                            <span class="error-msg" name="iaisErrorMsg" id="error_otherDonationReason"></span>
                        </iais:value>
                    </iais:row>
                </div>


                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Purpose of Donation" mandatory="true"/>
                    <div class="col-md-6" style="padding-right: 0;padding-left: 0;">
                        <iais:value width="12" cssClass="col-md-12" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearch"
                                       id="donatedForResearch"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearch"><span
                                        class="check-square"></span>Research</label>
                            </div>
                        </iais:value>
                        <iais:value width="12" cssClass="col-md-12" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox"
                                       name="donatedForTraining" id="donatedForTraining"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.donatedForTraining == 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForTraining"><span
                                        class="check-square"></span>Training</label>
                            </div>
                        </iais:value>
                        <iais:value width="12" cssClass="col-md-12" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForTreatment"
                                       id="donatedForTreatment"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForTreatment"><span
                                        class="check-square"></span>Treatment</label>
                            </div>
                        </iais:value>
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedFor" style="padding-right: 15px;padding-left: 15px;"></span>
                    </div>

                </iais:row>
                <div id="researchDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearch !=1 }">style="display: none"</c:if>>

                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Donated for Research (Usable for Treatment)" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"  id="donResForTreatNum" name="donResForTreatNum" value="${arSuperDataSubmissionDto.donationStageDto.donResForTreatNumStr==null?arSuperDataSubmissionDto.donationStageDto.donResForTreatNum:arSuperDataSubmissionDto.donationStageDto.donResForTreatNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_donResForTreatNum"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Donated to current AR centre for Research (Not Usable for Treatment)" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"   name="donResForCurCenNotTreatNum" id="donResForCurCenNotTreatNum" value="${arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNumStr==null?arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNum:arSuperDataSubmissionDto.donationStageDto.donResForCurCenNotTreatNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_donResForCurCenNotTreatNum"></span>
                        </iais:value>
                    </iais:row>

                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Type of Research for Which Gamete(s) was Donated" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6" style="padding-right: 0;padding-left: 0;">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearchHescr"
                                       id="donatedForResearchHescr"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchHescr ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearchHescr"><span
                                        class="check-square"></span>Human Embryonic Stem Cell Research</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearchRrar"
                                       id="donatedForResearchRrar"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchRrar ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearchRrar"><span
                                        class="check-square"></span>Research Related to Assisted Reproduction</label>
                            </div>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="checkbox"
                                       name="donatedForResearchOther"
                                       id="donatedForResearchOther"
                                       <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther ==1 }">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="donatedForResearchOther"><span
                                        class="check-square"></span>Other Type of Research</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_donatedForResearchBox" style="padding-right: 15px;padding-left: 15px;"></span>
                        </iais:value>
                    </iais:row>
                    <div id="donatedForResearchOtherDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForResearchOther !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="Please Indicate the Other Type of Research"  mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <input type="text" maxlength="100"   name="donatedForResearchOtherType" value="${arSuperDataSubmissionDto.donationStageDto.donatedForResearchOtherType}" >
                                <span class="error-msg" name="iaisErrorMsg" id="error_donatedForResearchOtherType"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div id="trainingDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTraining !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Used for Training" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"  id="trainingNum" name="trainingNum" value="${arSuperDataSubmissionDto.donationStageDto.trainingNumStr==null?arSuperDataSubmissionDto.donationStageDto.trainingNum:arSuperDataSubmissionDto.donationStageDto.trainingNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_trainingNum"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div id="treatmentDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.donatedForTreatment !=1 }">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Is the Sample from a Directed Donation?" mandatory="true"/>
                        <iais:value cssClass="col-md-3" >
                            <div class="form-check" style="padding-left: 0;">
                                <input class="form-check-input"
                                       type="radio"
                                       name="directedDonation"
                                       value="1"
                                       id="directedYes"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.isDirectedDonation eq 1}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="directedYes"><span
                                        class="check-circle"></span>Yes</label>
                            </div>
                            <span class="error-msg" name="iaisErrorMsg" id="error_directedDonation"></span>
                        </iais:value>
                        <iais:value cssClass="col-md-3" >
                            <div class="form-check" style="padding-left: 0;">
                                <input class="form-check-input" type="radio"
                                       name="directedDonation"
                                       value="0"
                                       id="directedNo"
                                       <c:if test="${arSuperDataSubmissionDto.donationStageDto.isDirectedDonation eq 0}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="directedNo"><span
                                        class="check-circle"></span>No</label>
                            </div>
                        </iais:value>
                    </iais:row>
                    <div id="recipientNoDisplay" <c:if test="${ arSuperDataSubmissionDto.donationStageDto.isDirectedDonation !=1 }">style="display: none"</c:if>>
                        <iais:row>
                            <iais:field width="6" cssClass="col-md-6" value="ID No. of Donation Recipient" mandatory="true"/>
                            <iais:value width="6" cssClass="col-md-6">
                                <iais:input maxLength="20" type="text"  id="recipientNo" name="recipientNo" value="${arSuperDataSubmissionDto.donationStageDto.recipientNo}" />
                                <span class="error-msg" name="iaisErrorMsg" id="error_recipientNo"></span>
                            </iais:value>
                        </iais:row>
                    </div>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. Donated For Treatment" mandatory="true"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text"  id="treatNum" name="treatNum" value="${arSuperDataSubmissionDto.donationStageDto.treatNumStr==null?arSuperDataSubmissionDto.donationStageDto.treatNum:arSuperDataSubmissionDto.donationStageDto.treatNumStr}" />
                            <span class="error-msg" name="iaisErrorMsg" id="error_treatNum"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Total No. Donated" mandatory="false"/>
                    <iais:value width="6" cssClass="col-md-6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.donationStageDto.totalNum}</div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_totalNum"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="ID of Donated Recipient" />
                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="9"   name="donatedRecipientNum" value="${arSuperDataSubmissionDto.donationStageDto.donatedRecipientNum}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_donatedRecipientNum"></span>
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/donationSection.js"></script>