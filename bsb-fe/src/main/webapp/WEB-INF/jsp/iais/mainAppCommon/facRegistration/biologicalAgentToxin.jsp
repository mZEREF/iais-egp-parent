<div id="sectionGroup">
    <c:forEach var="info" items="${batInfo.batInfos}" varStatus="status">
        <section id="batInfoSection--v--${status.index}" style="margin-bottom: 100px">
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="schedule--v--${status.index}">Schedule</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6" style="z-index: 30;">
                    <select name="schedule--v--${status.index}" id="schedule--v--${status.index}">
                        <c:forEach items="${ScheduleOps}" var="schedule">
                            <option value="${schedule.value}" <c:if test="${info.schedule eq schedule.value}">selected="selected"</c:if>>${schedule.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="schedule--v--${status.index}" class="error-msg"></span>
                </div>
                <c:if test="${status.index gt 0}">
                    <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                </c:if>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="batName--v--${status.index}">Name of Biological Agent/Toxin</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6" style="z-index: 20;">
                    <select name="batName--v--${status.index}" id="batName--v--${status.index}">
                        <c:forEach items="${batNameOps}" var="name">
                            <option value="${name.value}" <c:if test="${info.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="batName--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label>Types of samples that will be handled <span class="mandatory otherQualificationSpan">*</span></label>
                </div>
                <div class="col-sm-6" style="z-index: 10;">
                    <div class="self-assessment-checkbox-gp">
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleCultureIsolate--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS001')}">checked="checked"</c:if> value="BNOTS001"/>
                            <label for="sampleCultureIsolate--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culture/isolate of biological agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="samplePureToxin--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS002')}">checked="checked"</c:if> value="BNOTS002"/>
                            <label for="samplePureToxin--v--${status.index}" class="form-check-label"><span class="check-square"></span>Pure toxin</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleClinical--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS003')}">checked="checked"</c:if> value="BNOTS003"/>
                            <label for="sampleClinical--v--${status.index}" class="form-check-label"><span class="check-square"></span>Clinical sample e.g. blood, serum, respiratory swab, containing biological agent or toxin</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleAnimal--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS004')}">checked="checked"</c:if> value="BNOTS004"/>
                            <label for="sampleAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal sample containing biological agent or toxin</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleEnv--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS005')}">checked="checked"</c:if> value="BNOTS005"/>
                            <label for="sampleEnv--v--${status.index}" class="form-check-label"><span class="check-square"></span>Environmental samples containing biological agent or toxin</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleFood--v--${status.index}" <c:if test="${info.sampleType.contains('BNOTS006')}">checked="checked"</c:if> value="BNOTS006"/>
                            <label for="sampleFood--v--${status.index}" class="form-check-label"><span class="check-square"></span>Food sample containing biological agent or toxin</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleOthers--v--${status.index}" data-custom-ind="batOthersSampleType" <c:if test="${info.sampleType.contains('BNOTS007')}">checked="checked"</c:if> value="BNOTS007"/>
                            <label for="sampleOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details</label>
                        </div>
                    </div>
                    <span data-err-ind="sampleType--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label>Type of work that will be carried out involving the biological agent/toxin <span class="mandatory otherQualificationSpan">*</span></label>
                </div>
                <div class="col-sm-6" style="z-index: 10;">
                    <div class="self-assessment-checkbox-gp">
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workCultureIsolation--v--${status.index}" <c:if test="${info.workType.contains('BNOTW001')}">checked="checked"</c:if> value="BNOTW001"/>
                            <label for="workCultureIsolation--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culturing/isolation of biological agent</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workSerological--v--${status.index}" <c:if test="${info.workType.contains('BNOTW002')}">checked="checked"</c:if> value="BNOTW002"/>
                            <label for="workSerological--v--${status.index}" class="form-check-label"><span class="check-square"></span>Serological test</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workMolecular--v--${status.index}" <c:if test="${info.workType.contains('BNOTW003')}">checked="checked"</c:if> value="BNOTW003"/>
                            <label for="workMolecular--v--${status.index}" class="form-check-label"><span class="check-square"></span>Molecular test</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workAnimal--v--${status.index}" <c:if test="${info.workType.contains('BNOTW004')}">checked="checked"</c:if> value="BNOTW004"/>
                            <label for="workAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal studies (specify the type of animal under details)</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workBiomanufacturing--v--${status.index}" <c:if test="${info.workType.contains('BNOTW005')}">checked="checked"</c:if> value="BNOTW005"/>
                            <label for="workBiomanufacturing--v--${status.index}" class="form-check-label"><span class="check-square"></span>Biomanufacturing involving biological agent. Please specify expected maximum handling volume under details</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workOthers--v--${status.index}" data-custom-ind="batOthersWorkType" <c:if test="${info.workType.contains('BNOTW006')}">checked="checked"</c:if> value="BNOTW006"/>
                            <label for="workOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details.</label>
                        </div>
                    </div>
                    <span data-err-ind="workType--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div id="sampleWorkDetailDiv--v--${status.index}" class="form-group" <c:if test="${!info.sampleType.contains('BNOTS007') && !info.workType.contains('BNOTW006')}">style="display: none"</c:if>>
                <div class="col-sm-5 control-label">
                    <label for="sampleWorkDetail--v--${status.index}">Details regarding the type of sample that will be handled and the intended work <span class="mandatory otherQualificationSpan">*</span></label>
                </div>
                <div class="col-sm-6">
                    <textarea maxLength="250" class="col-xs-12" name="sampleWorkDetail--v--${status.index}" id="sampleWorkDetail--v--${status.index}" rows="3"><c:out value="${info.sampleWorkDetail}"/></textarea>
                    <span data-err-ind="remarks--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="procurementMode--v--${status.index}">Mode of Procurement</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <div class="col-sm-4" style="margin-top: 8px">
                        <label for="procurementModeLocalTransfer--v--${status.index}">Yes</label>
                        <input type="radio" name="procurementMode--v--${status.index}" id="procurementModeLocalTransfer--v--${status.index}" value="BMOP001" <c:if test="${info.procurementMode eq 'BMOP001'}">checked="checked"</c:if> />
                    </div>
                    <div class="col-sm-4" style="margin-top: 8px">
                        <label for="procurementModeImport--v--${status.index}">No</label>
                        <input type="radio" name="procurementMode--v--${status.index}" id="procurementModeImport--v--${status.index}" value="BMOP002" <c:if test="${info.procurementMode eq 'BMOP002'}">checked="checked"</c:if> />
                    </div>
                    <span data-err-ind="procurementMode--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Transferring Facility:</p>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="postalCode--v--${status.index}">Postal Code</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-5">
                    <input maxlength="250" type="text" autocomplete="off" name="postalCode--v--${status.index}" id="postalCode--v--${status.index}" value='<c:out value="${info.postalCode}"/>'/>
                    <span data-err-ind="postalCode--v--${status.index}" class="error-msg"></span>
                </div>
                <div class="col-sm-2">
                    <a href="#">Retrieve your address</a>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="addressType--v--${status.index}">Address Type</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <select name="addressType--v--${status.index}" id="addressType--v--${status.index}">
                        <option value="">Please Select</option>
                        <c:forEach items="${addressTypeOps}" var="name">
                            <option value="${name.value}" <c:if test="${info.addressType eq name.value}">selected="selected"</c:if>>${name.text}</option>
                        </c:forEach>
                    </select>
                    <span data-err-ind="addressType--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="blockNo--v--${status.index}">Block / House No.</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="blockNo--v--${status.index}" id="blockNo--v--${status.index}" value='<c:out value="${info.blockNo}"/>'/>
                    <span data-err-ind="blockNo--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="floorNo--v--${status.index}">Floor / Unit No.</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-2">
                    <input type="text" autocomplete="off" name="floorNo--v--${status.index}" id="floorNo--v--${status.index}" value='${info.floorNo}' maxlength="250"/>
                    <span data-err-ind="floorNo" class="error-msg"></span>
                </div>
                <div class="hidden-xs col-sm-1" style="text-align: center">
                    <p>-</p>
                </div>
                <div class="col-sm-3">
                    <input type="text" autocomplete="off" name="unitNo--v--${status.index}" id="unitNo--v--${status.index}" value='${info.unitNo}' maxlength="250"/>
                    <span data-err-ind="unitNo" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="facilityName--v--${status.index}">Street Name</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="streetName--v--${status.index}" id="streetName--v--${status.index}" value='<c:out value="${info.streetName}"/>'/>
                    <span data-err-ind="streetName--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="buildingName--v--${status.index}">Building Name</label>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="buildingName--v--${status.index}" id="buildingName--v--${status.index}" value='<c:out value="${info.buildingName}"/>'/>
                    <span data-err-ind="buildingName--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Contact Person from Transferring Facility:</p>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="contactPersonName--v--${status.index}">Name</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="contactPersonName--v--${status.index}" id="contactPersonName--v--${status.index}" value='<c:out value="${info.contactPersonName}"/>'/>
                    <span data-err-ind="contactPersonName--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="emailAddress--v--${status.index}">Email address</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="emailAddress--v--${status.index}" id="emailAddress--v--${status.index}" value='<c:out value="${info.emailAddress}"/>'/>
                    <span data-err-ind="emailAddress--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="contactNo--v--${status.index}">Contact No.</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="contactNo--v--${status.index}" id="contactNo--v--${status.index}" value='<c:out value="${info.contactNo}"/>'/>
                    <span data-err-ind="contactNo--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-5 control-label">
                    <label for="expectedDate--v--${status.index}">Expected Date of Transfer</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <input type="text" autocomplete="off" name="expectedDate--v--${status.index}" id="expectedDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.expectedDate}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                    <span data-err-ind="expectedDate--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="courierServiceProviderName--v--${status.index}">Name of Courier Service Provider</label>
                </div>
                <div class="col-sm-6">
                    <input maxlength="250" type="text" autocomplete="off" name="courierServiceProviderName--v--${status.index}" id="courierServiceProviderName--v--${status.index}" value='<c:out value="${info.courierServiceProviderName}"/>'/>
                    <span data-err-ind="courierServiceProviderName--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label style="font-size: 10px">Note: The driver of the conveyance must have a valid Hazardous Materials Transport Driver Permit, where applicable, in accordance with BATA Transportation Regulations.</label>
                </div>
            </div>
            <div class="form-group ">
                <div class="col-sm-5 control-label">
                    <label for="remarks--v--${status.index}">Remarks</label>
                    <span class="mandatory otherQualificationSpan">*</span>
                </div>
                <div class="col-sm-6">
                    <textarea maxLength="250" class="col-xs-12" name="remarks--v--${status.index}" id="remarks--v--${status.index}" rows="3"><c:out value="${info.remarks}"/></textarea>
                    <span data-err-ind="remarks--v--${status.index}" class="error-msg"></span>
                </div>
            </div>
        </section>
    </c:forEach>
</div>
<div class="form-group">
    <div class="col-12">
        <a id="addNewBatSection" style="text-decoration: none" href="javascript:void(0)">+ Add New Biological Agent/Toxins</a>
    </div>
</div>