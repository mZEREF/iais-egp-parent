<%@tag description="Biological agent/toxin tag of Facility Details" pageEncoding="UTF-8" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="addressTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="nationalityOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="status" required="true" type="javax.servlet.jsp.jstl.core.LoopTagStatus" %>
<%@attribute name="detail" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.bat.ProcModeLSPDetails" %>
<%@attribute name="procModeLSPJudge" required="false" type="java.lang.Boolean" %>

<iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
<div class="form-group ">
    <div class="col-sm-5 control-label">
        <label for="procurementMode--v--${status.index}">Mode of Procurement</label>
        <span class="mandatory otherQualificationSpan">*</span>
    </div>
    <div class="col-sm-6">
        <div class="form-check col-xs-4" style="margin-top: 8px; padding-left:0; padding-right:0">
            <input type="radio" class="form-check-input" name="procurementMode--v--${status.index}" id="procurementModeLocalTransfer--v--${status.index}" data-custom-ind="batProcurementModeLocal" value="${masterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}" <c:if test="${detail.procurementMode eq masterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}">checked="checked"</c:if> />
            <label for="procurementModeLocalTransfer--v--${status.index}" class="form-check-label"><span class="check-circle"></span>Local Transfer</label>
        </div>
        <div class="form-check col-xs-4" style="margin-top: 8px; padding-left:0; padding-right:0">
            <input type="radio" class="form-check-input" name="procurementMode--v--${status.index}" id="procurementModeImport--v--${status.index}" data-custom-ind="batProcurementModeImport" value="${masterCodeConstants.PROCUREMENT_MODE_IMPORT}" <c:if test="${detail.procurementMode eq masterCodeConstants.PROCUREMENT_MODE_IMPORT}">checked="checked"</c:if> />
            <label for="procurementModeImport--v--${status.index}" class="form-check-label"><span class="check-circle"></span>Import</label>
        </div>
        <c:if test="${procModeLSPJudge}">
            <div class="form-check col-xs-4" style="margin-top: 8px; padding-left:0; padding-right:0">
                <input type="radio" class="form-check-input" name="procurementMode--v--${status.index}" id="procurementModeAlreadyInProcession--v--${status.index}" data-custom-ind="procurementModeAlreadyInProcession" value="${masterCodeConstants.PROCUREMENT_MODE_ALREADY_IN_POSSESSION}" <c:if test="${detail.procurementMode eq masterCodeConstants.PROCUREMENT_MODE_ALREADY_IN_POSSESSION}">checked="checked"</c:if> />
                <label for="procurementModeAlreadyInProcession--v--${status.index}" class="form-check-label"><span class="check-circle"></span>Already in possession</label>
            </div>
        </c:if>
        <span data-err-ind="procurementMode--v--${status.index}" class="error-msg"></span>
    </div>
</div>
<iais-bsb:single-constant constantName="ADDRESS_TYPE_APT_BLK" classFullName="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" attributeKey="aptBlk"/>

<%--@elvariable id="aptBlk" type="java.lang.String"--%>
<div id="transferringFacilityDiv--v--${status.index}" <c:if test="${detail.procurementMode ne null and detail.procurementMode ne masterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}">style="display: none;"</c:if>>
    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Transferring Facility:</p>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="facNameT--v--${status.index}">Facility Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="facNameT--v--${status.index}" id="facNameT--v--${status.index}" value='${detail.facNameT}' maxlength="250"/>
            <span data-err-ind="facNameT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="postalCodeT--v--${status.index}">Postal Code</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-5">
            <input maxlength="6" type="text" autocomplete="off" name="postalCodeT--v--${status.index}" id="postalCodeT--v--${status.index}" value='<c:out value="${detail.postalCodeT}"/>'/>
            <span data-err-ind="postalCodeT--v--${status.index}" class="error-msg"></span>
        </div>
        <div class="col-sm-2">
            <a id="localTransferRetrieveAddressBtn" href="javascript:void(0)" data-current-idx="${status.index}" data-section-separator="--v--">Retrieve your address </a>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="addressTypeT--v--${status.index}">Address Type </label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <select name="addressTypeT--v--${status.index}" class="addressTypeTDropdown${status.index}" id="addressTypeT--v--${status.index}" data-custom-ind="addressTypeT">
                <option value="">Please Select</option>
                <c:forEach items="${addressTypeOps}" var="name">
                    <option value="${name.value}" <c:if test="${detail.addressTypeT eq name.value}">selected="selected"</c:if>>${name.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="addressTypeT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="blockNoT--v--${status.index}">Block / House No.</label>
            <span id="aptMandatoryBlkT--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${detail.addressTypeT ne aptBlk}">style="display:none;"</c:if>>*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="blockNoT--v--${status.index}" id="blockNoT--v--${status.index}" value='<c:out value="${detail.blockNoT}"/>'/>
            <span data-err-ind="blockNoT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="floorNoT--v--${status.index}">Floor</label>
            <span id="aptMandatoryFloorT--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${detail.addressTypeT ne aptBlk}">style="display:none;"</c:if>>*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="floorNoT--v--${status.index}" id="floorNoT--v--${status.index}" value='${detail.floorNoT}' maxlength="250"/>
            <span data-err-ind="floorNoT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="unitNoT--v--${status.index}">Unit No.</label>
            <span id="aptMandatoryUnitT--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${detail.addressTypeT ne aptBlk}">style="display:none;"</c:if>>*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="unitNoT--v--${status.index}" id="unitNoT--v--${status.index}" value='${detail.unitNoT}' maxlength="250"/>
            <span data-err-ind="unitNoT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="streetNameT--v--${status.index}">Street Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="streetNameT--v--${status.index}" id="streetNameT--v--${status.index}" value='<c:out value="${detail.streetNameT}"/>'/>
            <span data-err-ind="streetNameT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="buildingNameT--v--${status.index}">Building Name</label>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="buildingNameT--v--${status.index}" id="buildingNameT--v--${status.index}" value='<c:out value="${detail.buildingNameT}"/>'/>
            <span data-err-ind="buildingNameT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Contact Person from Transferring Facility:</p>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactPersonNameT--v--${status.index}">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="contactPersonNameT--v--${status.index}" id="contactPersonNameT--v--${status.index}" value='<c:out value="${detail.contactPersonNameT}"/>'/>
            <span data-err-ind="contactPersonNameT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailAddressT--v--${status.index}">Email address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="emailAddressT--v--${status.index}" id="emailAddressT--v--${status.index}" value='<c:out value="${detail.emailAddressT}"/>'/>
            <span data-err-ind="emailAddressT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoT--v--${status.index}">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="contactNoT--v--${status.index}" id="contactNoT--v--${status.index}" value='<c:out value="${detail.contactNoT}"/>'/>
            <span data-err-ind="contactNoT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-5 control-label">
            <label for="expectedDateT--v--${status.index}">Expected Date of Transfer</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="expectedDateT--v--${status.index}" id="expectedDateT--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${detail.expectedDateT}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
            <span data-err-ind="expectedDateT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="courierServiceProviderNameT--v--${status.index}">Name of Courier Service Provider</label>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="courierServiceProviderNameT--v--${status.index}" id="courierServiceProviderNameT--v--${status.index}" value='<c:out value="${detail.courierServiceProviderNameT}"/>'/>
            <span data-err-ind="courierServiceProviderNameT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label style="font-size: 10px">Note: The driver of the conveyance must have a valid Hazardous Materials Transport Driver Permit, where applicable, in accordance with BATA Transportation Regulations.</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="remarksT--v--${status.index}">Remarks</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <textarea maxLength="250" class="col-xs-12" name="remarksT--v--${status.index}" id="remarksT--v--${status.index}" rows="3"><c:out value="${detail.remarksT}"/></textarea>
            <span data-err-ind="remarksT--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
</div>
<div id="exportingFacilityDiv--v--${status.index}" <c:if test="${detail.procurementMode ne masterCodeConstants.PROCUREMENT_MODE_IMPORT}">style="display: none;"</c:if>>
    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Exporting Facility:</p>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="facNameE--v--${status.index}">Facility Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="facNameE--v--${status.index}" id="facNameE--v--${status.index}" value='<c:out value="${detail.facNameE}"/>'/>
            <span data-err-ind="facNameE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="postalCodeE--v--${status.index}">Postal Code</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="6" type="text" autocomplete="off" name="postalCodeE--v--${status.index}" id="postalCodeE--v--${status.index}" value='<c:out value="${detail.postalCodeE}"/>'/>
            <span data-err-ind="postalCodeE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="addressTypeE--v--${status.index}">Address Type</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <select name="addressTypeE--v--${status.index}" id="addressTypeE--v--${status.index}" data-custom-ind="addressTypeE">
                <option value="">Please Select</option>
                <c:forEach items="${addressTypeOps}" var="name">
                    <option value="${name.value}" <c:if test="${detail.addressTypeE eq name.value}">selected="selected"</c:if>>${name.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="addressTypeE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="blockNoE--v--${status.index}">Block / House No.</label>
            <span id="aptMandatoryBlkE--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${detail.addressTypeE ne aptBlk}">style="display:none;"</c:if>>*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="blockNoE--v--${status.index}" id="blockNoE--v--${status.index}" value='<c:out value="${detail.blockNoE}"/>'/>
            <span data-err-ind="blockNoE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="floorNoE--v--${status.index}">Floor</label>
            <span id="aptMandatoryFloorE--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${detail.addressTypeE ne aptBlk}">style="display:none;"</c:if>>*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="floorNoE--v--${status.index}" id="floorNoE--v--${status.index}" value='${detail.floorNoE}' maxlength="250"/>
            <span data-err-ind="floorNoE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="unitNoE--v--${status.index}">Unit No.</label>
            <span id="aptMandatoryUnitE--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${detail.addressTypeE ne aptBlk}">style="display:none;"</c:if>>*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="unitNoE--v--${status.index}" id="unitNoE--v--${status.index}" value='${detail.unitNoE}' maxlength="250"/>
            <span data-err-ind="unitNoE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="streetNameE--v--${status.index}">Street Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="streetNameE--v--${status.index}" id="streetNameE--v--${status.index}" value='<c:out value="${detail.streetNameE}"/>'/>
            <span data-err-ind="streetNameE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="buildingNameE--v--${status.index}">Building Name</label>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="buildingNameE--v--${status.index}" id="buildingNameE--v--${status.index}" value='<c:out value="${detail.buildingNameE}"/>'/>
            <span data-err-ind="buildingNameE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="countryE--v--${status.index}">Country</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <select name="countryE--v--${status.index}" id="countryE--v--${status.index}" class="countryEDropdown${status.index}">
                <option value="">Please Select</option>
                <c:forEach items="${nationalityOps}" var="na">
                    <option value="${na.value}" <c:if test="${detail.countryE eq na.value}">selected="selected"</c:if>>${na.text}</option>
                </c:forEach>
            </select>
            <span data-err-ind="countryE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="stateE--v--${status.index}">State</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="stateE--v--${status.index}" id="stateE--v--${status.index}" value='<c:out value="${detail.stateE}"/>'/>
            <span data-err-ind="stateE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Contact Person from Exporting Facility:</p>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactPersonNameE--v--${status.index}">Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="contactPersonNameE--v--${status.index}" id="contactPersonNameE--v--${status.index}" value='<c:out value="${detail.contactPersonNameE}"/>'/>
            <span data-err-ind="contactPersonNameE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="emailAddressE--v--${status.index}">Email address</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="emailAddressE--v--${status.index}" id="emailAddressE--v--${status.index}" value='<c:out value="${detail.emailAddressE}"/>'/>
            <span data-err-ind="emailAddressE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="contactNoE--v--${status.index}">Contact No.</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="contactNoE--v--${status.index}" id="contactNoE--v--${status.index}" value='<c:out value="${detail.contactNoE}"/>'/>
            <span data-err-ind="contactNoE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-5 control-label">
            <label for="expectedDateE--v--${status.index}">Expected Date of Import</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <input type="text" autocomplete="off" name="expectedDateE--v--${status.index}" id="expectedDateE--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${detail.expectedDateE}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
            <span data-err-ind="expectedDateE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="courierServiceProviderNameE--v--${status.index}">Name of Courier Service Provider</label>
        </div>
        <div class="col-sm-6">
            <input maxlength="250" type="text" autocomplete="off" name="courierServiceProviderNameE--v--${status.index}" id="courierServiceProviderNameE--v--${status.index}" value='<c:out value="${detail.courierServiceProviderNameE}"/>'/>
            <span data-err-ind="courierServiceProviderNameE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label style="font-size: 10px">Note: The driver of the conveyance must have a valid Hazardous Materials Transport Driver Permit, where applicable, in accordance with BATA Transportation Regulations.</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="remarksE--v--${status.index}">Remarks</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <textarea maxLength="250" class="col-xs-12" name="remarksE--v--${status.index}" id="remarksE--v--${status.index}" rows="3"><c:out value="${detail.remarksE}"/></textarea>
            <span data-err-ind="remarksE--v--${status.index}" class="error-msg"></span>
        </div>
    </div>
</div>
<div id="sourceFacilityDiv--v--${status.index}" <c:if test="${detail.procurementMode ne masterCodeConstants.PROCUREMENT_MODE_ALREADY_IN_POSSESSION}">style="display: none;"</c:if>>
    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Source Facility:</p>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="facNameS--v--${status.index}">Facility Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <label id="facNameS--v--${status.index}">${detail.facNameS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="postalCodeS--v--${status.index}">Postal Code</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <label id="postalCodeS--v--${status.index}">${detail.postalCodeS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="addressTypeS--v--${status.index}">Address Type</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <label id="addressTypeS--v--${status.index}">${detail.addressTypeS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="blockNoS--v--${status.index}">Block / House No.</label>
        </div>
        <div class="col-sm-6">
            <label id="blockNoS--v--${status.index}">${detail.blockNoS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="floorNoS--v--${status.index}">Floor</label>
        </div>
        <div class="col-sm-6">
            <label id="floorNoS--v--${status.index}">${detail.floorNoS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="unitNoS--v--${status.index}">Unit No.</label>
        </div>
        <div class="col-sm-6">
            <label id="unitNoS--v--${status.index}">${detail.unitNoS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="streetNameS--v--${status.index}">Street Name</label>
            <span class="mandatory otherQualificationSpan">*</span>
        </div>
        <div class="col-sm-6">
            <label id="streetNameS--v--${status.index}">${detail.streetNameS}</label>
        </div>
    </div>
    <div class="form-group ">
        <div class="col-sm-5 control-label">
            <label for="buildingNameS--v--${status.index}">Building Name</label>
        </div>
        <div class="col-sm-6">
            <label id="buildingNameS--v--${status.index}">${detail.buildingNameS}</label>
        </div>
    </div>
</div>