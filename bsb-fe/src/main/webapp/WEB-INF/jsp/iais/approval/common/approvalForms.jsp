<div id="formPanel" class="sopform ui-tabs ui-widget ui-widget-content ui-corner-all" style="display: block;">
    <div class="form-tab-panel ui-tabs-panel ui-widget-content ui-corner-bottom" id="tab_page_0">
        <div id="control--runtime--0" class="page control control-area  container-p-1">
            <div id="control--runtime--0--errorMsg_page_top" class="error_placements"></div>
            <table class="control-grid columns1 " style="width: 100%;">
                <tbody>
                <tr height="1">
                    <td class="first last" style="width: 100%;">
                        <div id="control--runtime--1" class="section control  container-s-1">
                            <div class="control-set-font control-font-header section-header"></div>
                            <div class="cgo-content">
                                <table class="assignContent control-grid" style="width:100%;">
                                    <tbody>
                                    <tr height="1">
                                        <td class="first last" style="width: 100%;">
                                            <div id="control--runtime--">
                                                <div class=" form-group form-horizontal formgap">
                                                    <div class="col-sm-8 control-label formtext control">
                                                        <div class="cgo-header">
                                                            <strong><iais:code code="${taskList}"></iais:code></strong>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div id="newOfficer" class="new-officer-form">
                                                <table class="control-grid">
                                                    <tbody>
                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Facility Name</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <input type="hidden" id="facilityId" name="facilityId" value="${approvalApplicationDto.facility.id}">
                                                                        <iais:select name="facilityName" id="facilityName" disabled="false" options="facilityNameSelect" firstOption="Please Select" value="${approvalApplicationDto.facility.facilityName}"></iais:select>
                                                                        <span id="error_facility" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr height="1">
                                                        <td class="first last">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">Schedule</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <iais:select id="schedule" name="schedule" disabled="false" codeCategory="CATE_ID_BSB_SCHEDULE_TYPE" firstOption="Please Select" value="${approvalApplicationDto.schedule}"></iais:select>
                                                                        <span id="error_schedule" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext ">
                                                                        <label class="control-label control-set-font control-font-label">List of Agents/Toxins</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7 control-font-label">
                                                                        <input type="hidden" id="biologicalId" name="biologicalId" value="">
                                                                        <iais:select id="listOfAgentsOrToxins" name="listOfAgentsOrToxins" disabled="false" multiValues="${agentsOrToxinsList}" multiSelect="true"></iais:select>
                                                                        <span id="error_biologicalId" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <c:if test="${taskList=='APPRTY001'}">
                                                        <tr height="1">
                                                            <td class="first last" style="">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class=" form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext ">
                                                                            <label class="control-label control-set-font control-font-label">Nature of the Sample</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-4 col-md-7 control-font-label">
                                                                            <iais:select name="natureOfTheSample" disabled="false" codeCategory="CATE_ID_BSB_NATURE_OF_TTE_SAMPLE" multiValues="${natureList}" multiSelect="true"></iais:select>
                                                                            <span id="error_sampleNature" name="iaisErrorMsg" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" id="others">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Others,please specify</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7" >
                                                                            <div class="">
                                                                                <iais:input maxLength="100" type="text" name="others" value="${approvalApplicationDto.sampleNatureOth}"></iais:input>
                                                                                <span id="error_sampleNatureOth" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                    <c:if test="${taskList=='APPRTY002'}">
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Estimated maximum volume (in litres) of production at any one time</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7" >
                                                                            <div class="">
                                                                                <iais:input maxLength="10" type="text" name="estimatedMaximumVolume" value="${approvalApplicationDto.prodMaxVolumeLitres}"></iais:input>
                                                                                <span id="error_prodMaxVolumeLitres" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Method or system used for large scale production</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="input-group">
                                                                                <div class="ax_default text_area">
                                                                                    <textarea name="methodOrSystemUsedForLargeScaleProduction" value="${approvalApplicationDto.lspMethod}" cols="64" rows="4" maxlength="250"></textarea>
                                                                                    <span id="error_lspMethod" name="iaisErrorMsg" class="error-msg"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                    <tr height="1">
                                                        <td class="first last" style="">
                                                            <div class="control control-caption-horizontal">
                                                                <div class=" form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Mode of Procurement</label>
                                                                        <span class="mandatory">*</span>
                                                                    </div>
                                                                    <div class="col-sm-4 col-md-7">
                                                                        <iais:select name="modeOfProcurement" disabled="false" codeCategory="CATE_ID_BSB_MODE_OF_PROCUREMENT" firstOption="Please Select" value="${approvalApplicationDto.procurementMode}"></iais:select>
                                                                        <span id="error_procurementMode" name="iaisErrorMsg" class="error-msg"></span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <c:if test="${(taskList=='APPRTY001' || taskList=='APPRTY002')}">
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_LocalTransfer">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Transfer From Facility Name</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="250" type="text" name="transferFromFacilityName" value="${approvalApplicationDto.facTransferForm}"></iais:input>
                                                                                <span id="error_facTransferForm" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_LocalTransfer">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Expected Date of Transfer</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:datePicker name="expectedDateOfTransfer" dateVal="${approvalApplicationDto.transferExpectedDate}"></iais:datePicker>
                                                                                <span id="error_transferExpectedDate" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_LocalTransfer">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Contact Person from Transferring Facility</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="132" type="text" name="contactPersonFromTransferringFacility" value="${approvalApplicationDto.impCtcPersonName}"></iais:input>
                                                                                <span id="error_impCtcPersonName" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_LocalTransfer">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Contact No of Contact Person from Transferring Facility</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7" >
                                                                            <div class="">
                                                                                <iais:input maxLength="20" type="text" name="contactNoOfContactPersonFromTransferringFacility" value="${approvalApplicationDto.impCtcPersonNo}"></iais:input>
                                                                                <span id="error_impCtcPersonNo" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_LocalTransfer">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Email Address of Contact Person from Transferring Facility</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="66" type="text" name="emailAddressOfContactPersonFromTransferringFacility" value="${approvalApplicationDto.impCtcPersonEmail}"></iais:input>
                                                                                <span id="error_impCtcPersonEmail" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_Import">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Overseas Facility Name</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="250" type="text" name="overseasFacilityName" value="${approvalApplicationDto.facTransferForm}"></iais:input>
                                                                                <span id="error_facTransferForm" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal" name="ModeOfProcurement_Import">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Expected Date of Import</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:datePicker name="expectedDateOfImport" dateVal="${approvalApplicationDto.transferExpectedDate}"></iais:datePicker>
                                                                                <span id="error_transferExpectedDate" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_Import">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Contact person from Source Facility</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="132" type="text" name="contactPersonFromSourceFacility" value="${approvalApplicationDto.impCtcPersonName}"></iais:input>
                                                                                <span id="error_impCtcPersonName" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap" name="ModeOfProcurement_Import">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Email Address of Contact person from Source Facility</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="66" type="text" name="emailAddressOfContactPersonFromSourceFacility" value="${approvalApplicationDto.impCtcPersonEmail}"></iais:input>
                                                                                <span id="error_impCtcPersonEmail" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Facility Address 1</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="35" type="text" name="facilityAddress1" value="${approvalApplicationDto.transferFacAddr1}"></iais:input>
                                                                                <span id="error_transferFacAddr1" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Facility Address 2</label>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="35" type="text" name="facilityAddress2" value="${approvalApplicationDto.transferFacAddr2}"></iais:input>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Facility Address 3</label>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="35" type="text" name="facilityAddress3" value="${approvalApplicationDto.transferFacAddr3}"></iais:input>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class=" form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext ">
                                                                            <label class="control-label control-set-font control-font-label">Country</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-4 col-md-7">
                                                                            <iais:select name="country" disabled="false" codeCategory="CATE_ID_BSB_COUNTRY" firstOption="Please Select" value="${approvalApplicationDto.transferCountry}"></iais:select>
                                                                            <span id="error_transferCountry" name="iaisErrorMsg" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">City</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="30" type="text" name="city" value="${approvalApplicationDto.transferCity}"></iais:input>
                                                                                <span id="error_transferCity" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">State</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="66" type="text" name="state" value="${approvalApplicationDto.transferState}"></iais:input>
                                                                                <span id="error_transferState" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Postal Code</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="15" type="text" name="postalCode" value="${approvalApplicationDto.transferPostalCode}"></iais:input>
                                                                                <span id="error_transferPostalCode" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Name of Courier Service Provider</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7" id="postalCode">
                                                                            <div class="">
                                                                                <iais:input maxLength="100" type="text" name="nameOfCourierServiceProvider" value="${approvalApplicationDto.courierServiceProviderName}"></iais:input>
                                                                                <span id="error_courierServiceProviderName" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                    <c:if test="${taskList=='APPRTY003'}">
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Name of Project</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="300" type="text" name="nameOfProject" value="${approvalApplicationDto.prjName}"></iais:input>
                                                                                <span id="error_prjName" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Name of Principal Investigator</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:input maxLength="100" type="text" name="nameOfPrincipalInvestigator" value="${approvalApplicationDto.principalInvestigatorName}"></iais:input>
                                                                                <span id="error_principalInvestigatorName" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Intended Work Activity</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="input-group">
                                                                                <div class="ax_default text_area">
                                                                                    <textarea name="intendedWorkActivity" value="${approvalApplicationDto.workActivityIntended}" cols="64" rows="4" maxlength="1000"></textarea>
                                                                                    <span id="error_workActivityIntended" name="iaisErrorMsg" class="error-msg"></span>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">Start Date</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:datePicker name="startDate" dateVal="${approvalApplicationDto.startDate}"></iais:datePicker>
                                                                                <span id="error_startDate" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <div class="form-group form-horizontal formgap">
                                                                        <div class="col-sm-4 control-label formtext">
                                                                            <label class="control-label control-set-font control-font-label">End Date</label>
                                                                            <span class="mandatory">*</span>
                                                                        </div>
                                                                        <div class="col-sm-5 col-md-7">
                                                                            <div class="">
                                                                                <iais:datePicker name="endDate" dateVal="${approvalApplicationDto.endDate}"></iais:datePicker>
                                                                                <span id="error_endDate" name="iaisErrorMsg" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <div class="form-group form-horizontal formgap">
                                                                    <div class="col-sm-4 control-label formtext">
                                                                        <label class="control-label control-set-font control-font-label">Remarks</label>
                                                                    </div>
                                                                    <div class="col-sm-5 col-md-7">
                                                                        <div class="input-group">
                                                                            <div class="ax_default text_area">
                                                                                <textarea name="remarks" value="${approvalApplicationDto.remarks}" cols="64" rows="4" maxlength="1000"></textarea>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <c:if test="${(taskList=='APPRTY001' || taskList=='APPRTY002')}">
                                                        <tr height="1">
                                                            <td class="first last" style="width: 100%;">
                                                                <div class="control control-caption-horizontal">
                                                                    <input type="checkbox" name="checkbox_1"> I will ensure to the best of my ability that the packaging of the materials and the transfer are carried out in accordance with the requirements stipulated in the BATA Transportation Regulations.
                                                                    <span id="error_checkbox1" name="iaisErrorMsg" class="error-msg"></span>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:if>
                                                    <tr height="1">
                                                        <td class="first last" style="width: 100%;">
                                                            <div class="control control-caption-horizontal">
                                                                <input type="checkbox" name="checkbox_2"> I, hereby declare that all the information I have provided here is true and accurate. If any of the information given herein changes or becomes inaccurate in any way, I shall immediately notify MOH Biosafety Branch of such change or inaccuracy.
                                                                <span id="error_checkbox2" name="iaisErrorMsg" class="error-msg"></span>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>