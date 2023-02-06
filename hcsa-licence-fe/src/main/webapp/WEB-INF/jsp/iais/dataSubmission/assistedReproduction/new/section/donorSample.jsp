<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/arSelection/ar_donor_sample.js"></script>
<c:set var="donorSampleDto" value="${arSuperDataSubmissionDto.donorSampleDto}"/>
<iais:row>
    <iais:field style="padding-left:0" width="6" value="Is the sample donated from overseas or locally?"
                cssClass="col-md-6" mandatory="true"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
            <div class="form-check" style="padding: 0;">
                <input class="form-check-input" id="localDonate" type="radio" name="localOrOversea"
                       <c:if test="${donorSampleDto.localOrOversea}">checked</c:if>
                       value="1"/>
                <label class="form-check-label" for="localDonate">
                    <span class="check-circle"></span>Local
                </label>
            </div>
        </iais:value>
        <iais:value width="6" cssClass="col-md-6">
            <div class="form-check">
                <input class="form-check-input" id="overseasDonate" type="radio" name="localOrOversea"
                       <c:if test="${donorSampleDto.localOrOversea eq false}">checked</c:if>
                       value="0"/>
                <label class="form-check-label" for="overseasDonate">
                    <span class="check-circle"></span>Overseas
                </label>
            </div>
        </iais:value>
        <span class="error-msg" name="iaisErrorMsg" id="error_localOrOversea"></span>
    </iais:value>
</iais:row>

<iais:row>
    <label class="col-xs-6 col-md-6 control-label" style="padding-left:0">Type of Sample <span class="mandatory">*</span>
        <a id="frozenSpermSelected" class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
           title="<span style='font-size: 1.5rem;'>${MessageUtil.getMessageDesc("DS_MSG013")}</span>"
           style="z-index: 10"
           data-original-title="">i</a>
    </label>
    <iais:value width="6" cssClass="col-md-6">
        <iais:select name="sampleType" id="sampleType" firstOption="Please Select" codeCategory="CATE_ID_DONATED_TYPE"
                     value="${donorSampleDto.sampleType}" onchange="showToolTip()"/>
    </iais:value>
</iais:row>

<div id="femaleDonorDiv">
    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Is the Female Donor's Identity Known? " cssClass="col-md-6"
                    mandatory="true"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
                <div class="form-check" style="padding: 0;">
                    <input class="form-check-input" type="radio" id="knownF" name="donorIdentityKnown"
                           <c:if test="${donorSampleDto.donorIdentityKnown eq 'DIK002'}">checked</c:if>
                           value="DIK002"/>
                    <label class="form-check-label" for="knownF">
                        <span class="check-circle"></span>No
                    </label>
                </div>
            </iais:value>
            <iais:value width="6" cssClass="col-md-6">
                <div class="form-check">
                    <input class="form-check-input" id="unKnownF" type="radio" name="donorIdentityKnown"
                           <c:if test="${donorSampleDto.donorIdentityKnown eq 'DIK001' }">checked</c:if>
                           value="DIK001"/>
                    <label class="form-check-label" for="unKnownF">
                        <span class="check-circle"></span>Yes
                    </label>
                </div>
            </iais:value>
            <span class="error-msg" name="iaisErrorMsg" id="error_donorIdentityKnown"></span>
        </iais:value>
    </iais:row>

    <div id="knowFIdDiv">
        <iais:row>
            <iais:field style="padding-left:0" width="6" value="Does the Female Donor have a NRIC/FIN number? "
                        cssClass="col-md-6"
                        mandatory="true"/>
            <iais:value width="6" cssClass="col-md-6">
                <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
                    <div class="form-check" style="padding: 0;">
                        <input class="form-check-input" id="noIdNUmberF" type="radio" name="hasIdNumberF"
                               <c:if test="${donorSampleDto.idType eq 'DTV_IT003' || hasIdNumberF eq 0}">checked</c:if>
                               value="0"/>
                        <label class="form-check-label" for="noIdNUmberF">
                            <span class="check-circle"></span>No
                        </label>
                    </div>
                </iais:value>
                <iais:value width="6" cssClass="col-md-6">
                    <div class="form-check">
                        <input class="form-check-input" id="hasIdNumberF" type="radio" name="hasIdNumberF"
                               <c:if test="${donorSampleDto.idType eq 'DTV_IT001' || donorSampleDto.idType eq 'DTV_IT002' || hasIdNumberF eq 1 }">checked</c:if>
                               value="1"/>
                        <label class="form-check-label" for="hasIdNumberF">
                            <span class="check-circle"></span>Yes
                        </label>
                    </div>
                </iais:value>
                <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumberF"></span>
            </iais:value>
        </iais:row>

        <iais:row id="idNumberRow">
            <iais:field style="padding-left:0" width="6" value="Female Donor's Passport Number " cssClass="col-md-6"
                        mandatory="true" id="fPassportNumberField"/>
            <iais:field style="padding-left:0" width="6" value="Female Donor's NRIC/FIN Number " cssClass="col-md-6"
                        mandatory="true" id="fNricNumberField"/>
            <iais:value width="6" cssClass="col-md-6">
                <iais:input maxLength="20" type="text" name="idNumber" value="${donorSampleDto.idNumber}"/>
            </iais:value>
        </iais:row>
    </div>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Female Donor Sample Code " cssClass="col-md-6"
                    mandatory="true"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" name="donorSampleCode" value="${donorSampleDto.donorSampleCode}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Age of Female Donor at the Point of Donation "
                    mandatory="true"
                    cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="2" type="text" name="donorSampleAge" id="donorSampleAge"
                        value="${donorSampleDto.donorSampleAge}"/>
        </iais:value>
    </iais:row>
</div>

<div id="maleDonorDiv">
    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Is the Male Donor's Identity Known? " cssClass="col-md-6"
                    mandatory="true"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
                <div class="form-check" style="padding: 0;">
                    <input class="form-check-input" id="knownM" type="radio" name="maleDonorIdentityKnow"
                           <c:if test="${donorSampleDto.maleDonorIdentityKnow eq false}">checked</c:if>
                           value="0"/>
                    <label class="form-check-label" for="knownM">
                        <span class="check-circle"></span>No
                    </label>
                </div>
            </iais:value>
            <iais:value width="6" cssClass="col-md-6">
                <div class="form-check">
                    <input class="form-check-input" id="unKnownM" type="radio" name="maleDonorIdentityKnow"
                           <c:if test="${donorSampleDto.maleDonorIdentityKnow}">checked</c:if>
                           value="1"/>
                    <label class="form-check-label" for="unKnownM">
                        <span class="check-circle"></span>Yes
                    </label>
                </div>
            </iais:value>
            <span class="error-msg" name="iaisErrorMsg" id="error_maleDonorIdentityKnow"></span>
        </iais:value>
    </iais:row>

    <div id="knowMIdDiv">
        <iais:row>
            <iais:field style="padding-left:0" width="6" value="Does the Male Donor have a NRIC/FIN number? "
                        cssClass="col-md-6"
                        mandatory="true"/>
            <iais:value width="6" cssClass="col-md-6">
                <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
                    <div class="form-check" style="padding: 0;">
                        <input class="form-check-input" id="noIdNUmberM" type="radio" name="hasIdNumberM"
                               <c:if test="${donorSampleDto.idTypeMale eq 'DTV_IT003' || hasIdNumberM eq 0}">checked</c:if>
                               value="0"/>
                        <label class="form-check-label" for="noIdNUmberM">
                            <span class="check-circle"></span>No
                        </label>
                    </div>
                </iais:value>
                <iais:value width="6" cssClass="col-md-6">
                    <div class="form-check">
                        <input class="form-check-input" id="hasIdNumberM" type="radio" name="hasIdNumberM"
                               <c:if test="${donorSampleDto.idTypeMale eq 'DTV_IT001' || donorSampleDto.idTypeMale eq 'DTV_IT002' || hasIdNumberM eq 1}">checked</c:if>
                               value="1"/>
                        <label class="form-check-label" for="hasIdNumberM">
                            <span class="check-circle"></span>Yes
                        </label>
                    </div>
                </iais:value>
                <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumberM"></span>
            </iais:value>
        </iais:row>

        <iais:row id="mIdNumberRow">
            <iais:field style="padding-left:0" width="6" value="Male Donor's Passport Number " mandatory="true"
                        cssClass="col-md-6" id="mPassportNumberField"/>
            <iais:field style="padding-left:0" width="6" value="Male Donor's NRIC/FIN Number " mandatory="true"
                        cssClass="col-md-6" id="mNricNumberField"/>
            <iais:value width="6" cssClass="col-md-6">
                <iais:input maxLength="20" type="text" name="idNumberMale" id="donorPassportM"
                            value="${donorSampleDto.idNumberMale}"/>
            </iais:value>
        </iais:row>
    </div>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Male Donor Sample Code " mandatory="true"
                    cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="20" type="text" name="maleDonorSampleCode" id="maleDonorSampleCode"
                        value="${donorSampleDto.maleDonorSampleCode}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Age of Male Donor at the Point of Donation "
                    mandatory="true" cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="2" type="text" name="maleDonorSampleAge" id="maleDonorSampleAge"
                        value="${donorSampleDto.maleDonorSampleAge}"/>
        </iais:value>
    </iais:row>
</div>

<iais:row id="sampleFromLocal">
    <iais:field style="padding-left:0" width="6" value="Donated to" cssClass="col-md-6"/>
    <iais:value width="6" cssClass="col-md-6">
        <c:if test="${not empty localPremisesLabel}">
            <p>${localPremisesLabel}</p>
        </c:if>
        <span name="localDsCenter" id="localDsCenter"></span>
    </iais:value>
</iais:row>

<iais:row id="sampleFromOversea">
    <iais:field style="padding-left:0" width="6" value="Which Institution was the Sample Donated From?"
                cssClass="col-md-6"
                mandatory="true"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:input maxLength="256" type="text" name="sampleFromOthers" value="${donorSampleDto.sampleFromOthers}"/>
        <span class="error-msg" name="iaisErrorMsg" id="error_sampleFromOthers"></span>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field style="padding-left:0" width="6" value="Reason(s) for Donation " cssClass="col-md-6" mandatory="true"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:select name="donationReason" id="donationReason" firstOption="Please Select"
                     codeCategory="CATE_ID_DONATION_REASON"
                     value="${donorSampleDto.donationReason}"/>
    </iais:value>
</iais:row>

<iais:row id="reasonOtherRow">
    <iais:field style="padding-left:0" width="6" value="If 'Others', please specify the reason for donation "
                cssClass="col-md-6" mandatory="true"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:input maxLength="100" type="text" name="otherDonationReason"
                    value="${donorSampleDto.otherDonationReason}"/>
    </iais:value>
</iais:row>

<iais:row>
    <iais:field style="padding-left:0" width="6" value="Purpose of Donation " cssClass="col-md-6" mandatory="true"/>
    <iais:value width="6" cssClass="col-md-6">
            <div class="form-check col-xs-12" style="padding: 0;">
                <input class="form-check-input" type="checkbox"
                       name="donatedForResearch"
                       value="1"
                       id="donatedForResearch"
                       <c:if test="${donorSampleDto.donatedForResearch}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="donatedForResearch"><span
                        class="check-square"></span>Research</label>
            </div>
            <div class="form-check col-xs-12" style="padding: 0;">
                <input class="form-check-input" type="checkbox"
                       name="donatedForTraining"
                       value="1"
                       id="donatedForTraining"
                       <c:if test="${donorSampleDto.donatedForTraining}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="donatedForTraining"><span
                        class="check-square"></span>Training</label>
            </div>
            <div class="form-check col-xs-12" style="padding: 0;">
                <input class="form-check-input" type="checkbox"
                       name="donatedForTreatment"
                       value="1"
                       id="donatedForTreatment"
                       <c:if test="${donorSampleDto.donatedForTreatment}">checked</c:if>
                       aria-invalid="false">
                <label class="form-check-label"
                       for="donatedForTreatment"><span
                        class="check-square"></span>Treatment</label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_donationPurpose"></span>
    </iais:value>
</iais:row>

<div id="researchDiv">
    <iais:row>
        <iais:field style="padding-left:0" width="6" value="No. Donated for Research (Usable for Treatment) "
                    mandatory="true"
                    cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="2" type="text" name="donResForTreatNum" value="${donorSampleDto.donResForTreatNum}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="No. Donated for Research (Not Usable for Treatment) "
                    mandatory="true"
                    cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="2" type="text" name="donResForCurCenNotTreatNum"
                        value="${donorSampleDto.donResForCurCenNotTreatNum}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Type of Research for Which Gamete(s) Was Donated "
                    mandatory="true"
                    cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <div class="form-check" style="padding-left:0">
                <input class="form-check-input" type="checkbox" value="1" id="donatedForResearchHescr"
                       <c:if test="${donorSampleDto.donatedForResearchHescr}">checked</c:if>
                       name="donatedForResearchHescr">
                <label class="form-check-label" for="donatedForResearchHescr">
                    <span class="check-square"></span>Human Embryonic Stem Cell Research
                </label>
            </div>
            <div class="form-check" style="padding-left:0">
                <input class="form-check-input" type="checkbox" value="1" id="donatedForResearchRrar"
                       <c:if test="${donorSampleDto.donatedForResearchRrar}">checked</c:if>
                       name="donatedForResearchRrar">
                <label class="form-check-label" for="donatedForResearchRrar">
                    <span class="check-square"></span>Research Related to Assisted Reproduction
                </label>
            </div>
            <div class="form-check" style="padding-left:0">
                <input class="form-check-input" type="checkbox" value="1" id="donatedForResearchOther"
                       <c:if test="${donorSampleDto.donatedForResearchOther}">checked</c:if>
                       name="donatedForResearchOther">
                <label class="form-check-label" for="donatedForResearchOther">
                    <span class="check-square"></span>Other Type of Research
                </label>
            </div>
            <span class="error-msg" name="iaisErrorMsg" id="error_gameteResType"></span>
        </iais:value>
    </iais:row>

    <div id="donatedForResearchOtherDisplay" <c:if test="${!donorSampleDto.donatedForResearchOther }">style="display: none"</c:if>>
        <iais:row>
            <iais:field width="6" cssClass="col-md-6" value="Please Indicate the Other Type of Research"  mandatory="true"
                        style="padding-left: 0px;padding-right: 0px;"/>
            <iais:value width="6" cssClass="col-md-6">
                <iais:input maxLength="100" type="text" name="donatedForResearchOtherType"
                            value="${donorSampleDto.donatedForResearchOtherType}"/>
            </iais:value>
        </iais:row>
    </div>
</div>

<iais:row id="trainingNumRow">
    <iais:field style="padding-left:0" width="6" value="No. Donated for Training " mandatory="true"
                cssClass="col-md-6"/>
    <iais:value width="6" cssClass="col-md-6">
        <iais:input maxLength="2" type="text" name="trainingNum" value="${donorSampleDto.trainingNum}"/>
    </iais:value>
</iais:row>

<div id="treatmentNumRow">
    <iais:row>
        <iais:field style="padding-left:0" width="6" value="Is the sample from a directed donation?"
                    cssClass="col-md-6" mandatory="true"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:value width="6" cssClass="col-md-6" style="padding-left:0">
                <div class="form-check" style="padding: 0;">
                    <input class="form-check-input" id="directedDonationY" type="radio" name="directedDonation"
                           <c:if test="${donorSampleDto.directedDonation}">checked</c:if>
                           value="1"/>
                    <label class="form-check-label" for="directedDonationY">
                        <span class="check-circle"></span>Yes
                    </label>
                </div>
            </iais:value>
            <iais:value width="6" cssClass="col-md-6">
                <div class="form-check">
                    <input class="form-check-input" id="directedDonationN" type="radio" name="directedDonation"
                           <c:if test="${donorSampleDto.directedDonation eq false}">checked</c:if>
                           value="0"/>
                    <label class="form-check-label" for="directedDonationN">
                        <span class="check-circle"></span>No
                    </label>
                </div>
            </iais:value>
            <span class="error-msg" name="iaisErrorMsg" id="error_directedDonation"></span>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field style="padding-left:0" width="6" value="No. Donated for Treatment " mandatory="true"
                    cssClass="col-md-6"/>
        <iais:value width="6" cssClass="col-md-6">
            <iais:input maxLength="2" type="text" name="treatNum" value="${donorSampleDto.treatNum}"/>
        </iais:value>
    </iais:row>
</div>

<iais:row>
    <iais:field style="padding-left:0" width="6" value="Total No. Donated " mandatory="false"
                cssClass="col-md-6"/>
    <iais:value width="6" cssClass="col-md-6">
        <p id="donatedNum">0</p>
    </iais:value>
</iais:row>

<iais:confirm msg="DS_ERR044"
              callBack="$('#spermAgeConfirm').hide();" popupOrder="spermAgeConfirm" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />
<iais:confirm msg="DS_ERR045"
              callBack="$('#oocyteAgeConfirm').hide();" popupOrder="oocyteAgeConfirm" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />