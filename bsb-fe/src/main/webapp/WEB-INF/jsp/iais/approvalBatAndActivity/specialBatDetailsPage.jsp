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
<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="${TableDisplayUtil.indexes(batInfo.batInfos.size())}">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="workActivitySection" readonly disabled>
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
                                                        <p class="assessment-title" style="border-bottom: 1px solid black; font-size:18px; padding-bottom: 10px; font-weight: bold">Details of Biological Agent / Toxin</p>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="schedule">Schedule</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <select name="schedule" class="scheduleDropdown" id="schedule" data-cascade-dropdown="schedule-bat">
                                                                    <c:forEach items="${scheduleOps}" var="schedule">
                                                                        <option value="${schedule.value}" <c:if test="${batInfo.schedule eq schedule.value}">selected="selected"</c:if>>${schedule.text}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <span data-err-ind="schedule" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="batName">Name of Biological Agent</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <select name="batName"  class="batNameDropdown" id="batName">
                                                                    <c:set var="batNameOps" value="${scheduleBatMap.get(info.schedule == null ? firstScheduleOp : info.schedule)}"/>
                                                                    <c:forEach items="${batNameOps}" var="name">
                                                                        <option value="${name.value}" <c:if test="${batInfo.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <span data-err-ind="batName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="procurementMode">Mode of Procurement</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="projectName">Name of Project</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="projectName" id="projectName" value='<c:out value="${batInfo.projectName}"/>'/>
                                                                <span data-err-ind="projectName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="principalInvestigatorName">Name of Principal Investigator</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="principalInvestigatorName" id="principalInvestigatorName" value='<c:out value="${batInfo.principalInvestigatorName}"/>'/>
                                                                <span data-err-ind="principalInvestigatorName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Transferring Facility:</p>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="facilityName">Facility Name</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="facilityName" id="facilityName" value='<c:out value="${batInfo.facilityName}"/>'/>
                                                                <span data-err-ind="facilityName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="postalCode">Postal Code</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="postalCode" id="postalCode" value='<c:out value="${batInfo.postalCode}"/>'/>
                                                                <span data-err-ind="postalCode" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="addressType">Address Type</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <select name="addressType" class="addressTypeDropdown" id="addressType">
                                                                    <c:forEach items="${addressTypeOps}" var="name">
                                                                        <option value="${name.value}" <c:if test="${batInfo.addressType eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <span data-err-ind="addressType" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="blockNo">Block / House No.</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-2">
                                                                <input type="text" autocomplete="off" name="blockNo" id="blockNo" value='${batInfo.blockNo}' maxlength="250"/>
                                                                <span data-err-ind="blockNo" class="error-msg"></span>
                                                            </div>
                                                            <div class="hidden-xs col-sm-1" style="text-align: center">
                                                                <p>-</p>
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <input type="text" autocomplete="off" name="houseNo" id="houseNo" value='${batInfo.houseNo}' maxlength="250"/>
                                                                <span data-err-ind="houseNo" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="floorNo">Floor / Unit No.</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-2">
                                                                <input type="text" autocomplete="off" name="floorNo" id="floorNo" value='${batInfo.floorNo}' maxlength="250"/>
                                                                <span data-err-ind="floorNo" class="error-msg"></span>
                                                            </div>
                                                            <div class="hidden-xs col-sm-1" style="text-align: center">
                                                                <p>-</p>
                                                            </div>
                                                            <div class="col-sm-3">
                                                                <input type="text" autocomplete="off" name="unitNo" id="unitNo" value='${batInfo.unitNo}' maxlength="250"/>
                                                                <span data-err-ind="unitNo" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="facilityName">Street Name</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="streetName" id="streetName" value='<c:out value="${batInfo.streetName}"/>'/>
                                                                <span data-err-ind="streetName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="buildingName">Building Name</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="buildingName" id="buildingName" value='<c:out value="${batInfo.buildingName}"/>'/>
                                                                <span data-err-ind="buildingName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Contact Person from Transferring Facility:</p>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="contactPersonName">Name</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="contactPersonName" id="contactPersonName" value='<c:out value="${batInfo.contactPersonName}"/>'/>
                                                                <span data-err-ind="contactPersonName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="emailAddress">Email address</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="emailAddress" id="emailAddress" value='<c:out value="${batInfo.emailAddress}"/>'/>
                                                                <span data-err-ind="emailAddress" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="contactNo">Contact No.</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="contactNo" id="contactNo" value='<c:out value="${batInfo.contactNo}"/>'/>
                                                                <span data-err-ind="contactNo" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="expectedDate">Expected Date of Transfer</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input type="text" autocomplete="off" name="expectedDate" id="expectedDate" data-date-start-date="01/01/1900" value="<c:out value="${batInfo.expectedDate}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                <span data-err-ind="expectedDate" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="courierServiceProviderName">Name of Courier Service Provider</label>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="courierServiceProviderName" id="courierServiceProviderName" value='<c:out value="${batInfo.courierServiceProviderName}"/>'/>
                                                                <span data-err-ind="courierServiceProviderName" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label>Note: The driver of the conveyance must have a valid Hazardous Materials Transport Driver Permit, where applicable, in accordance with BATA Transportation Regulations.</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="remarks">Remarks</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <input maxlength="250" type="text" autocomplete="off" name="remarks" id="remarks" value='<c:out value="${batInfo.remarks}"/>'/>
                                                                <span data-err-ind="remarks" class="error-msg"></span>
                                                            </div>
                                                        </div>
                                                        <div id="sectionGroup">
                                                            <c:forEach var="activity" items="${batInfo.workActivities}" varStatus="status">
                                                                <section id="workActivitySection--v--${status.index}">
                                                                    <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Work Activity</p>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="intendedWorkActivity--v--${status.index}">Intended Work Activity</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input maxlength="250" type="text" autocomplete="off" name="intendedWorkActivity--v--${status.index}" id="intendedWorkActivity--v--${status.index}" value='<c:out value="${info.intendedWorkActivity}"/>'/>
                                                                            <span data-err-ind="intendedWorkActivity--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="startDate--v--${status.index}">Start Date</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input type="text" autocomplete="off" name="startDate--v--${status.index}" id="startDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.startDate}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                            <span data-err-ind="startDate--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="endDate--v--${status.index}">End Date</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input type="text" autocomplete="off" name="endDate--v--${status.index}" id="endDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.endDate}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                            <span data-err-ind="endDate--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="activityRemarks--v--${status.index}">Remarks</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input maxlength="250" type="text" autocomplete="off" name="activityRemarks--v--${status.index}" id="activityRemarks--v--${status.index}" value='<c:out value="${info.activityRemarks}"/>'/>
                                                                            <span data-err-ind="activityRemarks--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="form-group">
                                                            <div class="col-12">
                                                                <a id="addNewBatSection" style="text-decoration: none" href="javascript:void(0)">+ Add Another Work Activity</a>
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