<div class="form-check-gp col-xs-12 col-md-12">
  <div class="col-xs-12 col-md-6">
    <p>Is the sample donated from overseas or locally? <span class="mandatory">*</span></p>
  </div>
  <div class="col-xs-12 col-md-6">
    <div class="form-check form-check-inline">
      <input class="form-check-input triggerObj" id="localDonate" type="radio" name="sampleRoot" value="local"/>
      <label class="form-check-label" for="localDonate">
        <span class="check-circle"></span>Local
      </label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input triggerObj" id="overseasDonate" type="radio" name="sampleRoot" value="overseas"/>
      <label class="form-check-label" for="overseasDonate">
        <span class="check-circle"></span>Overseas
      </label>
    </div>
  </div>
  <span class="error-msg" name="iaisErrorMsg" id="error_sampleRoot"></span>
</div>

<div class="col-xs-12 col-md-12">
  <div class="col-xs-12 col-md-6">
    <label for="sampleType">Type of Sample <span class="mandatory">*</span></label></div>

  <div class="col-xs-12 col-md-6">
    <select name="sampleType" id="sampleType" class="triggerObj">
      <option value="">Please Select</option>
      <option value="DONTY001">Fresh Oocyte(s)</option>
      <option value="DONTY002">Frozen Oocyte(s)</option>
      <option value="DONTY003">Frozen Embryo(s)</option>
      <option value="DONTY004">Frozen Sperm</option>
    </select>
  </div>

  <span class="error-msg" name="iaisErrorMsg" id="error_sampleType"></span>
</div>


<div id="femaleSampleType"  <c:if test="${sampleType ne 'DONTY001' or sampleType ne 'DONTY002' or sampleType ne 'DONTY003'}">style="display: none" </c:if>>
  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label>Is the Female Donor's Identity Known? <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="knownF" type="radio" name="isKnowIdentityF" value="N"/>
        <label class="form-check-label" for="knownF">
          <span class="check-circle"></span>No
        </label>
      </div>
      <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="unKnownF" type="radio" name="isKnowIdentityF" value="Y"/>
        <label class="form-check-label" for="unKnownF">
          <span class="check-circle"></span>Yes
        </label>
      </div>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_isKnowIdentityF"></span>
  </div>

  <div id="isKnowIdentityFSection"  <c:if test="${isKnowIdentityF ne 'Y'}">style="display: none" </c:if>>
    <div class="col-xs-12 col-md-12">
      <div class="col-xs-12 col-md-6">
        <label for="sampleType">Does the Female Donor have a NRIC/FIN number? <span class="mandatory">*</span></label>
      </div>

      <div class="col-xs-12 col-md-6">
        <div class="form-check form-check-inline">
          <input class="form-check-input triggerObj" id="noIdNUmberF" type="radio" name="hasIdNumberF" value="N"/>
          <label class="form-check-label" for="noIdNUmberF">
            <span class="check-circle"></span>No
          </label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input triggerObj" id="hasIdNumberF" type="radio" name="hasIdNumberF" value="Y"/>
          <label class="form-check-label" for="hasIdNumberF">
            <span class="check-circle"></span>Yes
          </label>
        </div>
      </div>
      <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumberF"></span>
    </div>

    <div class="col-xs-12 col-md-12" id="noIdSectionF"  <c:if test="${hasIdNumberF ne 'N'}">style="display: none" </c:if>>
        <div class="col-xs-12 col-md-6">
          <label for="donorPassportF">Female Donor's Passport Number <span class="mandatory">*</span></label>
        </div>
        <div class="col-xs-12 col-md-6">
          <input type="text" id="donorPassportF" name="donorPassportF" maxlength="" autocomplete="off">
        </div>
      <span class="error-msg" name="iaisErrorMsg" id="error_donorPassportF"></span>
    </div>

    <div class="col-xs-12 col-md-12" id="hasIdSectionF"  <c:if test="${hasIdNumberF ne 'Y'}">style="display: none" </c:if>>
      <div class="col-xs-12 col-md-6">
        <label for="donorIdNumberF">Female Donor's NRIC/FIN Number <span class="mandatory">*</span></label>
      </div>
      <div class="col-xs-12 col-md-6">
        <input type="text" id="donorIdNumberF" name="donorIdNumberF" maxlength="" autocomplete="off">
      </div>
    </div>

  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="sampleCodeF">Female Donor Sample Code <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="sampleCodeF" name="sampleCodeF" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_sampleCodeF"></span>
  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="donationAgeF">Age of Female Donor at the Point of Donation <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="donationAgeF" name="donationAgeF" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_donationAgeF"></span>
  </div>

</div>

<div id="maleSampleType"  <c:if test="${sampleType ne 'DONTY004' or sampleType ne 'DONTY003'}">style="display: none" </c:if>>
  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label>Is the Male Donor's Identity Known? <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="knownM" type="radio" name="isKnowIdentityM" value="N"/>
        <label class="form-check-label" for="knownM">
          <span class="check-circle"></span>No
        </label>
      </div>
      <div class="form-check form-check-inline">
        <input class="form-check-input triggerObj" id="unKnownM" type="radio" name="isKnowIdentityM" value="Y"/>
        <label class="form-check-label" for="unKnownM">
          <span class="check-circle"></span>Yes
        </label>
      </div>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_isKnowIdentityN"></span>
  </div>

  <div id="isKnowIdentityMSection"  <c:if test="${isKnowIdentityF ne 'Y'}">style="display: none" </c:if>>
    <div class="col-xs-12 col-md-12">
      <div class="col-xs-12 col-md-6">
        <label>Does the Male Donor have a NRIC/FIN number? <span class="mandatory">*</span></label>
      </div>

      <div class="col-xs-12 col-md-6">
        <div class="form-check form-check-inline">
          <input class="form-check-input triggerObj" id="noIdNUmberM" type="radio" name="hasIdNumberM" value="N"/>
          <label class="form-check-label" for="noIdNUmberM">
            <span class="check-circle"></span>No
          </label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input triggerObj" id="hasIdNumberM" type="radio" name="hasIdNumberM" value="Y"/>
          <label class="form-check-label" for="hasIdNumberM">
            <span class="check-circle"></span>Yes
          </label>
        </div>
      </div>
      <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumberM"></span>
    </div>

    <div class="col-xs-12 col-md-12" id="noIdSectionM"  <c:if test="${hasIdNumberM ne 'N'}">style="display: none" </c:if>>
      <div class="col-xs-12 col-md-6">
        <label for="donorPassportM">Male Donor's Passport Number <span class="mandatory">*</span></label>
      </div>
      <div class="col-xs-12 col-md-6">
        <input type="text" id="donorPassportM" name="donorPassportM" maxlength="" autocomplete="off">
      </div>
      <span class="error-msg" name="iaisErrorMsg" id="error_donorPassportM"></span>
    </div>

    <div class="col-xs-12 col-md-12" id="hasIdSectionM"  <c:if test="${hasIdNumberM ne 'Y'}">style="display: none" </c:if>>
      <div class="col-xs-12 col-md-6">
        <label for="donorIdNumberM">Male Donor's NRIC/FIN Number <span class="mandatory">*</span></label>
      </div>
      <div class="col-xs-12 col-md-6">
        <input type="text" id="donorIdNumberM" name="donorIdNumberM" maxlength="" autocomplete="off">
      </div>
    </div>

  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="sampleCodeM">Male Donor Sample Code <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="sampleCodeM" name="sampleCodeM" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_sampleCodeM"></span>
  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="donationAgeM">Age of Male Donor at the Point of Donation <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="donationAgeM" name="donationAgeM" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_donationAgeM"></span>
  </div>
</div>

<div id="sampleRootSection"  <c:if test="${sampleRoot ne 'local' or sampleRoot ne 'overseas'}">style="display:none;"</c:if>>
  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="institutionLocal">Which Institution was the Sample Donated From? <span class="mandatory">*</span></label>
    </div>

    <div class="col-xs-12 col-md-6">
      <div id="localDonateSection"  <c:if test="${sampleRoot ne 'local'}">style="display: none"</c:if>>
        <select name="institution" id="institutionLocal">
          <option value="">Please Select</option>
        </select>
      </div>

      <div id="overseasDonateSection"  <c:if test="${sampleRoot ne 'overseas'}">style="display: none"</c:if>>
        <input type="text" id="institutionOverseas" name="institution" maxlength="" autocomplete="off">
      </div>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_institution"></span>
  </div>
</div>

<div class="col-xs-12 col-md-12">
  <div class="col-xs-12 col-md-6">
    <label for="donationReason">Reason(s) for Donation <span class="mandatory">*</span></label>
  </div>

  <div class="col-xs-12 col-md-6">
    <select name="donationReason" id="donationReason">
      <option value="">Please Select</option>
      <option value="DONRES001">Gamete / embryos not usable for treatment</option>
      <option value="DONRES002">Completed treatment</option>
      <option value="DONRES003">Abandoned treatment</option>
      <option value="DONRES004">Others</option>
    </select>
  </div>

  <span class="error-msg" name="iaisErrorMsg" id="error_donationReason"></span>
</div>

<div class="col-xs-12 col-md-12">
  <div class="col-xs-12 col-md-6">
    <label>Purpose of Donation <span class="mandatory">*</span></label>
  </div>
  <div class="col-xs-12 col-md-6">
    <div class="form-check">
      <input class="form-check-input triggerObj" type="checkbox" value="research" id="donPurposeResearch" name="donationPurpose">
      <label class="form-check-label" for="donPurposeResearch">
        <span class="check-square"></span>Research
      </label>
    </div>
    <div class="form-check">
      <input class="form-check-input triggerObj" type="checkbox" value="training" id="donPurposeTraining" name="donationPurpose">
      <label class="form-check-label" for="donPurposeTraining">
        <span class="check-square"></span>Training
      </label>
    </div>
    <div class="form-check">
      <input class="form-check-input triggerObj" type="checkbox" value="treatment" id="donPurposeTreatment" name="donationPurpose">
      <label class="form-check-label" for="donPurposeTreatment">
        <span class="check-square"></span>Treatment
      </label>
    </div>
  </div>

  <span class="error-msg" name="iaisErrorMsg" id="error_donationPurpose"></span>
</div>

<div id="dpResearchSection"   <c:if test="${donationPurpose ne 'research'}">style="display: none"</c:if>>
  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="usableTRTResearchDONNo">No. Donated for Research (Usable for Treatment) <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="usableTRTResearchDONNo" name="usableTRTResearchDONNo" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_usableTRTResearchDONNo"></span>
  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="uselessTRTResearchDONNo">No. Donated for Research (Usable for Treatment) <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="uselessTRTResearchDONNo" name="uselessTRTResearchDONNo" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_uselessTRTResearchDONNo"></span>
  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label>Type of Research for Which Gamete(s) Was Donated <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <div class="form-check">
        <input class="form-check-input" type="checkbox" value="GRESTY001" id="hescrResearch" name="gameteResType">
        <label class="form-check-label" for="hescrResearch">
          <span class="check-square"></span>Human Embryonic Stem Cell Research
        </label>
      </div>
      <div class="form-check">
        <input class="form-check-input" type="checkbox" value="GRESTY002" id="rrtarResearch" name="gameteResType">
        <label class="form-check-label" for="rrtarResearch">
          <span class="check-square"></span>Research Related to Assisted Reproduction
        </label>
      </div>
      <div class="form-check">
        <input class="form-check-input" type="checkbox" value="GRESTY003" id="otherResearch" name="gameteResType">
        <label class="form-check-label" for="otherResearch">
          <span class="check-square"></span>Other Type of Research
        </label>
      </div>
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_gameteResType"></span>
  </div>

  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="otherResType">Please indicate the Other Type of Research
        <span class="mandatory" id="otherResTypeMandatory" <c:if test="${gameteResType ne 'GRESTY003'}">style="display: none" </c:if>>*</span>
      </label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="otherResType" name="otherResType" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_otherResType"></span>
  </div>
</div>

<div id="dpTrainingSection"  <c:if test="${donationPurpose ne 'training'}">style="display: none"</c:if>>
  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="trainingDONNo">No. Donated for Training <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="trainingDONNo" name="trainingDONNo" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_trainingDONNo"></span>
  </div>
</div>

<div id="dpTreatmentSection"  <c:if test="${donationPurpose ne 'treatment'}">style="display: none"</c:if>>
  <div class="col-xs-12 col-md-12">
    <div class="col-xs-12 col-md-6">
      <label for="treatmentDONNo">No. Donated for Treatment <span class="mandatory">*</span></label>
    </div>
    <div class="col-xs-12 col-md-6">
      <input type="text" id="treatmentDONNo" name="treatmentDONNo" maxlength="" autocomplete="off">
    </div>
    <span class="error-msg" name="iaisErrorMsg" id="error_treatmentDONNo"></span>
  </div>
</div>

<div class="col-xs-12 col-md-12">
  <div class="col-xs-12 col-md-6">
    <label>Total No. Donated</label>
  </div>
  <div class="col-xs-12 col-md-6">
    <label>0</label>
  </div>
</div>
