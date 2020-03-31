<div class="row">
    <div class="col-xs-12">
        <h4>MedAlert Person</h4>
        <br/>
        <div class="medAlertContent">
            <div class="">
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-6 control-label formtext col-md-5">
                                <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">Assign a Principal Officer</label>
                                <span class="upload_controls"></span>
                            </div>
                            <div class="col-sm-5 col-md-7" id="assignSelect${suffix}">
                                <div class="">
                                    <iais:select cssClass="assignSel"  name="assignSel" options="MedAlertAssignSelect"  value="${principalOfficer.assignSelect}" ></iais:select>
                                    <div id="control--runtime--2--errorMsg_right" style="display: none;" class="error_placements"></div>
                                    <span id="error_assignSelect${suffix}" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="principalOfficers">
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-5">
                                <label  class="control-label control-set-font control-font-label">Name</label>
                                <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-3" id="salutation${suffix}">
                                <iais:select cssClass="salutation"  name="salutation" codeCategory="CATE_ID_SALUTATION" value="${principalOfficer.salutation}" firstOption="Please Select"></iais:select>
                            </div>

                            <div class="col-sm-4">
                                <input name="name" maxlength="66" id="cr-po-name" type="text"  class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.name}" >
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-5">
                                <label id="control--runtime--33--label" class="control-label control-set-font control-font-label">ID No.
                                    <span class="mandatory">*</span>
                                </label>
                            </div>
                            <div class="col-sm-3">
                                <div class="" id="idType${suffix}">
                                    <iais:select cssClass="idType"  name="idType"  value="${principalOfficer.idType}" options="IdTypeSelect"></iais:select>
                                </div>
                            </div>
                            <div class="col-sm-4">
                                <input id="idType-idNo" name="idNo" type="text" maxlength="9"  class="idNoVal form-control control-input control-set-font control-font-normal" value="${principalOfficer.idNo}" >
                                <span class="error-msg" id="error_poNRICFIN${status.index}" name="iaisErrorMsg"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-5">
                                <label  class="control-label control-set-font control-font-label">Mobile No.</label>
                                <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-7">
                                <input name="mobileNo" type="text"   maxlength="8" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.mobileNo}" >
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-5">
                                <label  class="control-label control-set-font control-font-label">Email Address</label>
                                <span class="mandatory">*</span>
                            </div>
                            <div class="col-sm-4 col-md-7">
                                <input name="emailAddress" maxlength="66" type="text" id="emailAdress" class="form-control control-input control-set-font control-font-normal" value="${principalOfficer.emailAddr}" >
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="control control-caption-horizontal">
                        <div class=" form-group form-horizontal formgap">
                            <div class="col-sm-3 control-label formtext col-md-5">
                                <label  class="control-label control-set-font control-font-label">Preferred Mode of Receiving MedAlert</label>
                                <span class="mandatory">*</span>
                            </div>
                            <div class="">
                                <div class="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="preferredMode" value = "Email" aria-invalid="false">
                                        <label class="form-check-label"><span class="check-circle"></span>
                                            Email
                                        </label>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-check">
                                        <input class="form-check-input" type="radio" name="preferredMode" value = "SMS" aria-invalid="false">
                                        <label class="form-check-label"><span class="check-circle"></span>
                                            SMS
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>