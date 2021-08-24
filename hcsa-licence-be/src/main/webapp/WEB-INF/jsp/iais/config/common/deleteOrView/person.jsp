<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Principal Officer (PO)&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input type="text" name="man-principalOfficer" maxlength="2" readonly value="${PO.mandatoryCount}" placeholder="mandatory count">
    </div>
    <div class="col-xs-12 col-md-2">
      <input type="text" name="mix-principalOfficer" maxlength="2"  readonly value="${PO.maximumCount}" placeholder="maximum count">
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label">Nominee&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input  type="text" name="man-DeputyPrincipalOfficer" maxlength="2" readonly value="${DPO.mandatoryCount}" placeholder="mandatory count">
    </div>
    <div class="col-xs-12 col-md-2">
      <input  type="text" name="mix-DeputyPrincipalOfficer" maxlength="2" readonly  value="${DPO.maximumCount}"  placeholder="maximum count">
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Business Name<span class="mandatory">*</span></label>
    <div class="cl-xs-12 col-md-4">
      <div class="col-xs-12 col-md-6 form-check">
        <input  type="radio" disabled <c:if test="${businessName=='1'}"> checked</c:if> class="form-check-input other-lic co-location" name="business-name"  value="1" >
        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
      </div>
      <div class="col-xs-12 col-md-6 form-check">
        <input  type="radio" disabled <c:if test="${businessName=='0'}"> checked</c:if> class="form-check-input other-lic co-location" name="business-name"  value="0">
        <label class="form-check-label" ><span class="check-circle"></span>No</label>
      </div>
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Clinical Director&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="man-clinical_director" maxlength="2" placeholder="minimum count" value="${CD.pageMandatoryCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount5"></span>
    </div>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="mix-clinical_director" maxlength="2" placeholder="maximum count" value="${CD.pageMaximumCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount5"></span>
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Clinical Governance Officer (CGO)&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input  type="text" name="man-ClinicalGovernanceOfficer" maxlength="2"  readonly value="${CGO.mandatoryCount}" placeholder="mandatory count">
    </div>
    <div class="col-xs-12 col-md-2">
      <input  type="text" name="mix-ClinicalGovernanceOfficer" maxlength="2"  readonly value="${CGO.maximumCount}"  placeholder="maximum count">
    </div>
  </div>
</div>



<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Service Personnel&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input  type="text" name="man-ServicePersonnel" maxlength="2" readonly value="${SVCPSN.mandatoryCount}" placeholder="mandatory count">
    </div>
    <div class="col-xs-12 col-md-2">
      <input  type="text" name="mix-ServicePersonnel" maxlength="2" readonly value="${SVCPSN.maximumCount}" placeholder="maximum count">
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Vehicles&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="man-vehicles" maxlength="2" placeholder="minimum count" value="${VEH.pageMandatoryCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount6"></span>
    </div>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="mix-vehicles" maxlength="2" placeholder="maximum count" value="${VEH.pageMaximumCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount6"></span>
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >General Conveyance Charges<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="man-charges" maxlength="2" placeholder="minimum count" value="${CHA.pageMandatoryCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount7"></span>
    </div>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="mix-charges" maxlength="2" placeholder="maximum count" value="${CHA.pageMaximumCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount7"></span>
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Medical Equipment and Other Charges<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="man-other-charges" maxlength="2" placeholder="minimum count" value="${CHAO.pageMandatoryCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_mandatoryCount8"></span>
    </div>
    <div class="col-xs-12 col-md-2">
      <input readonly type="text" name="mix-other-charges" maxlength="2" placeholder="maximum count" value="${CHAO.pageMaximumCount}">
      <span class="error-msg" name="iaisErrorMsg" id="error_maximumCount8"></span>
    </div>
  </div>
</div>


<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >MedAlert Person&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <input disabled value="${MAP.id}" name="svcpsnId" maxlength="2" style="display:none;" type="text">
      <input disabled type="text" name="man-MedalertPerson" maxlength="2" value="${MAP.mandatoryCount}" placeholder="minimum count">
    </div>
    <div class="col-xs-12 col-md-2">
      <input disabled type="text" name="mix-MedalertPerson" maxlength="2" value="${MAP.maximumCount}"  placeholder="maximum count">
    </div>
  </div>
</div>

<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Section Leader&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <iais:input type="hidden" name="secldrId" value="${SECLDR.id}" needDisabled="true"/>
      <iais:input maxLength="2" type="text" name="man-SectionLeader" value="${SECLDR.mandatoryCount}"
                  placeholder="minimum count" needDisabled="true"/>
    </div>
    <div class="col-xs-12 col-md-2">
      <iais:input maxLength="2" type="text" name="mix-SectionLeader" value="${SECLDR.maximumCount}"
                  placeholder="maximum count" needDisabled="true"/>
    </div>
  </div>
</div>
<div class="form-group">
  <div class="col-xs-12 col-md-9">
    <label class="col-xs-12 col-md-7 control-label" >Key Appointment Holder&nbsp;<span class="mandatory">*</span></label>
    <div class="col-xs-12 col-md-2">
      <iais:input type="hidden" name="kahId" value="${KAH.id}" needDisabled="true"/>
      <iais:input maxLength="2" type="text" name="man-KAH" value="${KAH.mandatoryCount}"
                  placeholder="minimum count" needDisabled="true"/>
    </div>
    <div class="col-xs-12 col-md-2">
      <iais:input maxLength="2" type="text" name="mix-KAH" value="${KAH.maximumCount}"
                  placeholder="maximum count" needDisabled="true"/>
    </div>
  </div>
</div>
