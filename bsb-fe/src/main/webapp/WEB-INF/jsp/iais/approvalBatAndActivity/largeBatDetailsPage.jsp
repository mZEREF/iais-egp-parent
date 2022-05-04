<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-cascade-dropdown.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-bat-and-activity.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-biological-agent-toxin.js"></script>

<script>
    <% String jsonStr = (String) request.getAttribute("scheduleBatMapJson");
        if (jsonStr == null || "".equals(jsonStr)) {
            jsonStr = "undefined";
        }
    %>
    var scheduleBatDataJson = <%=jsonStr%>;
</script>


<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="dashboard.jsp"%>
<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(batInfo.batInfos.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="batInfoSection" readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="appInfoPanel" role="tabpanel">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <%@include file="subStepNavTab.jsp"%>
                                            <div class="tab-content">
                                                <div role="tabpanel">
                                                    <div class="form-horizontal">
                                                        <div id="sectionGroup">
                                                            <c:forEach var="info" items="${batInfo.batInfos}" varStatus="status">
                                                                <section id="batInfoSection--v--${status.index}">
                                                                    <%--@elvariable id="scheduleOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                                    <%--@elvariable id="batNameOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                                    <%--@elvariable id="addressTypeOps" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>"--%>
                                                                    <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Details of Biological Agent</p>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="schedule--v--${status.index}">Schedule <span class="mandatory otherQualificationSpan">*</span></label>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 30;">
                                                                            <select name="schedule--v--${status.index}" class="scheduleDropdown${status.index}" id="schedule--v--${status.index}" data-cascade-dropdown="schedule-bat">
                                                                                <c:forEach items="${scheduleOps}" var="schedule">
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
                                                                            <label for="batName--v--${status.index}">Name of Biological Agent</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 20;">
                                                                            <select name="batName--v--${status.index}" class="batNameDropdown${status.index}" id="batName--v--${status.index}">
                                                                                <c:set var="batNameOps" value="${scheduleBatMap.get(info.schedule == null ? firstScheduleOp : info.schedule)}"/>
                                                                                <c:forEach items="${batNameOps}" var="name">
                                                                                    <option value="${name.value}" <c:if test="${info.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                                </c:forEach>
                                                                            </select>
                                                                            <span data-err-ind="batName--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="estimatedMaximumVolume--v--${status.index}">Estimated maximum volume (in litres) of production at any one time</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input maxlength="250" type="text" autocomplete="off" name="estimatedMaximumVolume--v--${status.index}" id="estimatedMaximumVolume--v--${status.index}" value='<c:out value="${info.estimatedMaximumVolume}"/>'/>
                                                                            <span data-err-ind="estimatedMaximumVolume--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="methodOrSystem--v--${status.index}">Method or system used for large scale production</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input maxlength="250" type="text" autocomplete="off" name="methodOrSystem--v--${status.index}" id="methodOrSystem--v--${status.index}" value='<c:out value="${info.methodOrSystem}"/>'/>
                                                                            <span data-err-ind="methodOrSystem--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="procurementMode--v--${status.index}">Mode of Procurement</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <div class="col-sm-5" style="margin-top: 8px">
                                                                                <label for="procurementModeLocal--v--${status.index}">Local Transfer</label>
                                                                                <input type="radio" name="procurementMode--v--${status.index}" id="procurementModeLocal--v--${status.index}" data-custom-ind="batProcurementModeLocal" value="${MasterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}" <c:if test="${info.procurementMode eq MasterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}">checked="checked"</c:if> />
                                                                            </div>
                                                                            <div class="col-sm-5" style="margin-top: 8px">
                                                                                <label for="procurementModeImport--v--${status.index}">Import</label>
                                                                                <input type="radio" name="procurementMode--v--${status.index}" id="procurementModeImport--v--${status.index}" data-custom-ind="batProcurementModeImport" value="${MasterCodeConstants.PROCUREMENT_MODE_IMPORT}" <c:if test="${info.procurementMode eq MasterCodeConstants.PROCUREMENT_MODE_IMPORT}">checked="checked"</c:if> />
                                                                            </div>
                                                                            <div class="col-sm-5" style="margin-top: 8px">
                                                                                <label for="procurementModePossession--v--${status.index}">Already in possession</label>
                                                                                <input type="radio" name="procurementMode--v--${status.index}" id="procurementModePossession--v--${status.index}" data-custom-ind="batProcurementModePossession" value="${MasterCodeConstants.PROCUREMENT_MODE_ALREADY_IN_POSSESSION}" <c:if test="${info.procurementMode eq MasterCodeConstants.PROCUREMENT_MODE_ALREADY_IN_POSSESSION}">checked="checked"</c:if> />
                                                                            </div>
                                                                            <span data-err-ind="procurementMode--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div id="transferringFacilityDiv--v--${status.index}" <c:if test="${info.procurementMode ne MasterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}">style="display: none;"</c:if>>
                                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Transferring Facility:</p>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="facilityName--v--${status.index}">Facility Name</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="facilityName--v--${status.index}" id="facilityName--v--${status.index}" value='<c:out value="${info.facilityName}"/>'/>
                                                                                <span data-err-ind="facilityName--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="postalCode--v--${status.index}">Postal Code</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="postalCode--v--${status.index}" id="postalCode--v--${status.index}" value='<c:out value="${info.postalCode}"/>'/>
                                                                                <span data-err-ind="postalCode--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="addressType--v--${status.index}">Address Type</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <select name="addressType--v--${status.index}" class="addressTypeDropdown${status.index}" id="addressType--v--${status.index}">
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
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="blockNo--v--${status.index}" id="blockNo--v--${status.index}" value='<c:out value="${info.blockNo}"/>'/>
                                                                                <span data-err-ind="blockNo--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="floorNo--v--${status.index}">Floor / Unit No.</label>
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
                                                                                <label for="streetName--v--${status.index}">Street Name</label>
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
                                                                                <label>Note: The driver of the conveyance must have a valid Hazardous Materials Transport Driver Permit, where applicable, in accordance with BATA Transportation Regulations.</label>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="remarks--v--${status.index}">Remarks</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="remarks--v--${status.index}" id="remarks--v--${status.index}" value='<c:out value="${info.remarks}"/>'/>
                                                                                <span data-err-ind="remarks--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div id="exportingFacilityDiv--v--${status.index}" <c:if test="${info.procurementMode ne MasterCodeConstants.PROCUREMENT_MODE_IMPORT}">style="display: none;"</c:if>>
                                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Exporting Facility:</p>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="facilityName--v--${status.index}">Facility Name</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="facilityName--v--${status.index}" id="facilityName--v--${status.index}" value='<c:out value="${info.facilityName}"/>'/>
                                                                                <span data-err-ind="facilityName--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="blockNo--v--${status.index}">Block No.</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="blockNo--v--${status.index}" id="blockNo--v--${status.index}" value='<c:out value="${info.blockNo}"/>'/>
                                                                                <span data-err-ind="blockNo--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="floorNo--v--${status.index}">Floor & Unit</label>
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
                                                                                <label for="streetName--v--${status.index}">Street</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="streetName--v--${status.index}" id="streetName--v--${status.index}" value='<c:out value="${info.streetName}"/>'/>
                                                                                <span data-err-ind="streetName--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="postalCode--v--${status.index}">Postal Code</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="postalCode--v--${status.index}" id="postalCode--v--${status.index}" value='<c:out value="${info.postalCode}"/>'/>
                                                                                <span data-err-ind="postalCode--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="country--v--${status.index}">Country</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="country--v--${status.index}" id="country--v--${status.index}" value='<c:out value="${info.country}"/>'/>
                                                                                <span data-err-ind="country--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="city--v--${status.index}">City</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="city--v--${status.index}" id="city--v--${status.index}" value='<c:out value="${info.city}"/>'/>
                                                                                <span data-err-ind="city--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="state--v--${status.index}">State</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="state--v--${status.index}" id="state--v--${status.index}" value='<c:out value="${info.state}"/>'/>
                                                                                <span data-err-ind="state--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Contact Person from Exporting Facility:</p>
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
                                                                                <label for="expectedDate--v--${status.index}">Expected Date of Import</label>
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
                                                                                <label>Note: The driver of the conveyance must have a valid Hazardous Materials Transport Driver Permit, where applicable, in accordance with BATA Transportation Regulations.</label>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="remarks--v--${status.index}">Remarks</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="remarks--v--${status.index}" id="remarks--v--${status.index}" value='<c:out value="${info.remarks}"/>'/>
                                                                                <span data-err-ind="remarks--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div id="sourceFacilityDiv--v--${status.index}" <c:if test="${info.procurementMode ne MasterCodeConstants.PROCUREMENT_MODE_ALREADY_IN_POSSESSION}">style="display: none;"</c:if>>
                                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Source Facility:</p>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label>Facility Name <span class="mandatory otherQualificationSpan">*</span></label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <label><c:out value="${batInfo.facilityName}"/></label>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label>Block No. <span class="mandatory otherQualificationSpan">*</span></label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <label><c:out value="${batInfo.blockNo}"/></label>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label>Floor & Unit <span class="mandatory otherQualificationSpan">*</span></label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <label><c:out value="${batInfo.floorAndUnit}"/></label>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label>Street <span class="mandatory otherQualificationSpan">*</span></label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <label><c:out value="${batInfo.street}"/></label>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label>Postal Code <span class="mandatory otherQualificationSpan">*</span></label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <label><c:out value="${batInfo.postalCode}"/></label>
                                                                            </div>
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
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <%@ include file="InnerFooter.jsp" %>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>