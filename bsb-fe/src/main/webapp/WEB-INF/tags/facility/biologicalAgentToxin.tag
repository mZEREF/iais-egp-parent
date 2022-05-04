<%@tag description="Biological agent/toxin tag of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="batInfo" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.facility.BiologicalAgentToxinDto" %>
<%@attribute name="activityTypes" required="true" type="java.util.List<java.lang.String>" %>
<%@attribute name="activeNodeKey" required="true" type="java.lang.String" %>
<%@attribute name="scheduleOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="addressTypeOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="firstScheduleOp" required="true" type="java.lang.String" %>
<%@attribute name="scheduleBatMap" required="true" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>" %>
<%@attribute name="nationalityOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-add-section.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-cascade-dropdown.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-biological-agent-toxin.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-facility-register.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-facility-register.js"></script>

<script>
    <% String jsonStr = (String) request.getAttribute("scheduleBatMapJson");
       if (jsonStr == null || "".equals(jsonStr)) {
           jsonStr = "undefined";
       }
    %>
    var scheduleBatDataJson = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<%@include file="/WEB-INF/jsp/iais/facRegistration/dashboard.jsp" %>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="<%=sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil.indexes(batInfo.getBatInfos().size())%>">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="batInfoSection" readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="batInfoPanel" role="tabpanel">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
                                            <%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
                                            <ul id = "tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE}" role="tab">Possession of First and/or Second Schedule Biological Agent</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE}" role="tab">Possession of First Schedule Biological Agent</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}" role="tab">Possession of Fifth Schedule Toxin</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}" role="tab">Large-Scale Production of First Schedule Biological Agent</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}" role="tab">Large-Scale Production of Third Schedule Biological Agent</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_LSP_FIRST_THIRD_SCHEDULE)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_LSP_FIRST_THIRD_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_LSP_FIRST_THIRD_SCHEDULE}" role="tab">Large-Scale Production of First and/or Third Schedule Biological Agent</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV}" role="tab">Handling of non-First Schedule Poliovirus Infectious Materials</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL}" role="tab">Handling of Poliovirus Potentially Infectious Materials</a></li>
                                                </c:if>
                                                <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED)}'>
                                                    <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}" role="tab">Handling of Fifth Schedule Toxin for Exempted Purposes</a></li>
                                                </c:if>
                                            </ul>
                                            <div class="tab-content">
                                                <div role="tabpanel">
                                                    <div class="form-horizontal">
                                                        <div id="sectionGroup">
                                                            <c:forEach var="info" items="${batInfo.batInfos}" varStatus="status">
                                                                <section id="batInfoSection--v--${status.index}" style="margin-bottom: 100px">
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="schedule--v--${status.index}">Schedule</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 30;">
                                                                            <c:choose>
                                                                                <c:when test="${activeNodeKey eq masterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}">
                                                                                    <label>Fifth Schedule</label>
                                                                                    <input type="hidden" name="schedule--v--${status.index}" id="schedule--v--${status.index}" value="${masterCodeConstants.FIFTH_SCHEDULE}"/>
                                                                                </c:when>
                                                                                <c:otherwise>
                                                                                    <select name="schedule--v--${status.index}" class="scheduleDropdown${status.index}" id="schedule--v--${status.index}" data-cascade-dropdown="schedule-bat">
                                                                                        <option value="">Please Select</option>
                                                                                        <c:forEach items="${scheduleOps}" var="schedule">
                                                                                            <option value="${schedule.value}" <c:if test="${info.schedule eq schedule.value}">selected="selected"</c:if>>${schedule.text}</option>
                                                                                        </c:forEach>
                                                                                    </select>
                                                                                    <span data-err-ind="schedule--v--${status.index}" class="error-msg"></span>
                                                                                </c:otherwise>
                                                                            </c:choose>
                                                                        </div>
                                                                        <c:if test="${status.index gt 0}">
                                                                            <div class="col-sm-1"><h4 class="text-danger"><em data-current-idx="${status.index}" class="fa fa-times-circle del-size-36 cursorPointer removeBtn"></em></h4></div>
                                                                        </c:if>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="batName--v--${status.index}">Name of ${(empty info.schedule and firstScheduleOp eq masterCodeConstants.FIFTH_SCHEDULE) or (info.schedule eq masterCodeConstants.FIFTH_SCHEDULE) ? "Toxin" : "Biological Agent"}</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 20;">
                                                                            <select name="batName--v--${status.index}"  class="batNameDropdown${status.index}" id="batName--v--${status.index}">
                                                                                <c:set var="batNameOps" value="${scheduleBatMap.get(empty info.schedule ? firstScheduleOp : info.schedule)}"/>
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
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleCultureIsolate--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_CULTURE_ISOLATE)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_CULTURE_ISOLATE}"/>
                                                                                    <label for="sampleCultureIsolate--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culture/isolate of biological agent</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="samplePureToxin--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_PURE_TOXIN)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_PURE_TOXIN}"/>
                                                                                    <label for="samplePureToxin--v--${status.index}" class="form-check-label"><span class="check-square"></span>Pure toxin</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleClinical--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_CLINICAL)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_CLINICAL}"/>
                                                                                    <label for="sampleClinical--v--${status.index}" class="form-check-label"><span class="check-square"></span>Clinical sample e.g. blood, serum, respiratory swab, containing biological agent or toxin</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleAnimal--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_ANIMAL)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_ANIMAL}"/>
                                                                                    <label for="sampleAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal sample containing biological agent or toxin</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleEnv--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_ENVIRONMENTAL)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_ENVIRONMENTAL}"/>
                                                                                    <label for="sampleEnv--v--${status.index}" class="form-check-label"><span class="check-square"></span>Environmental samples containing biological agent or toxin</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleFood--v--${status.index}" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_FOOD)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_FOOD}"/>
                                                                                    <label for="sampleFood--v--${status.index}" class="form-check-label"><span class="check-square"></span>Food sample containing biological agent or toxin</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="sampleType--v--${status.index}" id="sampleOthers--v--${status.index}" data-custom-ind="batOthersSampleType" <c:if test="${info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER)}">checked="checked"</c:if> value="${masterCodeConstants.SAMPLE_NATURE_OTHER}"/>
                                                                                    <label for="sampleOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details</label>
                                                                                </div>
                                                                            </div>
                                                                            <span data-err-ind="sampleType--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label>Type of work that will be carried out involving the ${(empty info.schedule and firstScheduleOp eq masterCodeConstants.FIFTH_SCHEDULE) or (info.schedule eq masterCodeConstants.FIFTH_SCHEDULE) ? "Toxin" : "Biological Agent"} <span class="mandatory otherQualificationSpan">*</span></label>
                                                                        </div>
                                                                        <div class="col-sm-6" style="z-index: 10;">
                                                                            <div class="self-assessment-checkbox-gp">
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workCultureIsolation--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_CULTURING_ISOLATION_BAT)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_CULTURING_ISOLATION_BAT}"/>
                                                                                    <label for="workCultureIsolation--v--${status.index}" class="form-check-label"><span class="check-square"></span>Culturing/isolation of biological agent</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workSerological--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_SEROLOGICAL_TEST)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_SEROLOGICAL_TEST}"/>
                                                                                    <label for="workSerological--v--${status.index}" class="form-check-label"><span class="check-square"></span>Serological test</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workMolecular--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_MOLECULAR_TEST)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_MOLECULAR_TEST}"/>
                                                                                    <label for="workMolecular--v--${status.index}" class="form-check-label"><span class="check-square"></span>Molecular test</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workAnimal--v--${status.index}" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_ANIMAL_STUDIES)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_ANIMAL_STUDIES}"/>
                                                                                    <label for="workAnimal--v--${status.index}" class="form-check-label"><span class="check-square"></span>Animal studies (specify the type of animal under details)</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workBiomanufacturing--v--${status.index}" data-custom-ind="batBmfWorkType" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT}"/>
                                                                                    <label for="workBiomanufacturing--v--${status.index}" class="form-check-label"><span class="check-square"></span>Biomanufacturing involving biological agent. Please specify expected maximum handling volume under details</label>
                                                                                </div>
                                                                                <div class="form-check">
                                                                                    <input type="checkbox" class="form-check-input" name="workType--v--${status.index}" id="workOthers--v--${status.index}" data-custom-ind="batOthersWorkType" <c:if test="${info.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}">checked="checked"</c:if> value="${masterCodeConstants.WORK_TYPE_OTHERS}"/>
                                                                                    <label for="workOthers--v--${status.index}" class="form-check-label"><span class="check-square"></span>Others. Please specify under details.</label>
                                                                                </div>
                                                                            </div>
                                                                            <span data-err-ind="workType--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div id="sampleWorkDetailDiv--v--${status.index}" class="form-group" <c:if test="${!info.sampleType.contains(masterCodeConstants.SAMPLE_NATURE_OTHER) && !info.workType.contains(masterCodeConstants.WORK_TYPE_BIOMANUFACTURING_INVOLVING_BAT) && !info.workType.contains(masterCodeConstants.WORK_TYPE_OTHERS)}">style="display: none"</c:if>>
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="sampleWorkDetail--v--${status.index}">Details regarding the type of sample that will be handled and the intended work <span class="mandatory otherQualificationSpan">*</span></label>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <textarea maxLength="250" class="col-xs-12" name="sampleWorkDetail--v--${status.index}" id="sampleWorkDetail--v--${status.index}" rows="3"><c:out value="${info.sampleWorkDetail}"/></textarea>
                                                                            <span data-err-ind="sampleWorkDetail--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="procurementMode--v--${status.index}">Mode of Procurement</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <div class="form-check col-xs-4" style="margin-top: 8px; padding-left:0; padding-right:0">
                                                                                <input type="radio" class="form-check-input" name="procurementMode--v--${status.index}" id="procurementModeLocalTransfer--v--${status.index}" data-custom-ind="batProcurementModeLocal" value="${masterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}" <c:if test="${info.procurementMode eq masterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}">checked="checked"</c:if> />
                                                                                <label for="procurementModeLocalTransfer--v--${status.index}" class="form-check-label"><span class="check-circle"></span>Local Transfer</label>
                                                                            </div>
                                                                            <div class="form-check col-xs-4" style="margin-top: 8px; padding-left:0; padding-right:0">
                                                                                <input type="radio" class="form-check-input" name="procurementMode--v--${status.index}" id="procurementModeImport--v--${status.index}" data-custom-ind="batProcurementModeImport" value="${masterCodeConstants.PROCUREMENT_MODE_IMPORT}" <c:if test="${info.procurementMode eq masterCodeConstants.PROCUREMENT_MODE_IMPORT}">checked="checked"</c:if> />
                                                                                <label for="procurementModeImport--v--${status.index}" class="form-check-label"><span class="check-circle"></span>Import</label>
                                                                            </div>
                                                                            <span data-err-ind="procurementMode--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <iais-bsb:single-constant constantName="ADDRESS_TYPE_APT_BLK" classFullName="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" attributeKey="aptBlk"/>
                                                                        <%--@elvariable id="aptBlk" type="java.lang.String"--%>
                                                                    <div id="transferringFacilityDiv--v--${status.index}" <c:if test="${info.procurementMode ne null and info.procurementMode ne masterCodeConstants.PROCUREMENT_MODE_LOCAL_TRANSFER}">style="display: none;"</c:if>>
                                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Transferring Facility:</p>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="postalCodeT--v--${status.index}">Postal Code</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-5">
                                                                                <input maxlength="6" type="text" autocomplete="off" name="postalCodeT--v--${status.index}" id="postalCodeT--v--${status.index}" value='<c:out value="${info.postalCodeT}"/>'/>
                                                                                <span data-err-ind="postalCodeT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                            <div class="col-sm-2">
                                                                                <a id="localTransferRetrieveAddressBtn" href="javascript:void(0)" data-current-idx="${status.index}" data-section-separator="--v--">Retrieve your address</a>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="addressTypeT--v--${status.index}">Address Type</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <select name="addressTypeT--v--${status.index}" class="addressTypeTDropdown${status.index}" id="addressTypeT--v--${status.index}" data-custom-ind="addressTypeT">
                                                                                    <option value="">Please Select</option>
                                                                                    <c:forEach items="${addressTypeOps}" var="name">
                                                                                        <option value="${name.value}" <c:if test="${info.addressTypeT eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                                <span data-err-ind="addressTypeT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="blockNoT--v--${status.index}">Block / House No.</label>
                                                                                <span id="aptMandatoryBlkT--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${info.addressTypeT ne aptBlk}">style="display:none;"</c:if>>*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="blockNoT--v--${status.index}" id="blockNoT--v--${status.index}" value='<c:out value="${info.blockNoT}"/>'/>
                                                                                <span data-err-ind="blockNoT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="floorNoT--v--${status.index}">Floor</label>
                                                                                <span id="aptMandatoryFloorT--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${info.addressTypeT ne aptBlk}">style="display:none;"</c:if>>*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="floorNoT--v--${status.index}" id="floorNoT--v--${status.index}" value='${info.floorNoT}' maxlength="250"/>
                                                                                <span data-err-ind="floorNoT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="unitNoT--v--${status.index}">Unit No.</label>
                                                                                <span id="aptMandatoryUnitT--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${info.addressTypeT ne aptBlk}">style="display:none;"</c:if>>*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="unitNoT--v--${status.index}" id="unitNoT--v--${status.index}" value='${info.unitNoT}' maxlength="250"/>
                                                                                <span data-err-ind="unitNoT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="streetNameT--v--${status.index}">Street Name</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="streetNameT--v--${status.index}" id="streetNameT--v--${status.index}" value='<c:out value="${info.streetNameT}"/>'/>
                                                                                <span data-err-ind="streetNameT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="buildingNameT--v--${status.index}">Building Name</label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="buildingNameT--v--${status.index}" id="buildingNameT--v--${status.index}" value='<c:out value="${info.buildingNameT}"/>'/>
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
                                                                                <input maxlength="250" type="text" autocomplete="off" name="contactPersonNameT--v--${status.index}" id="contactPersonNameT--v--${status.index}" value='<c:out value="${info.contactPersonNameT}"/>'/>
                                                                                <span data-err-ind="contactPersonNameT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="emailAddressT--v--${status.index}">Email address</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="emailAddressT--v--${status.index}" id="emailAddressT--v--${status.index}" value='<c:out value="${info.emailAddressT}"/>'/>
                                                                                <span data-err-ind="emailAddressT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="contactNoT--v--${status.index}">Contact No.</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="contactNoT--v--${status.index}" id="contactNoT--v--${status.index}" value='<c:out value="${info.contactNoT}"/>'/>
                                                                                <span data-err-ind="contactNoT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="expectedDateT--v--${status.index}">Expected Date of Transfer</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="expectedDateT--v--${status.index}" id="expectedDateT--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.expectedDateT}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                                <span data-err-ind="expectedDateT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="courierServiceProviderNameT--v--${status.index}">Name of Courier Service Provider</label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="courierServiceProviderNameT--v--${status.index}" id="courierServiceProviderNameT--v--${status.index}" value='<c:out value="${info.courierServiceProviderNameT}"/>'/>
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
                                                                                <textarea maxLength="250" class="col-xs-12" name="remarksT--v--${status.index}" id="remarksT--v--${status.index}" rows="3"><c:out value="${info.remarksT}"/></textarea>
                                                                                <span data-err-ind="remarksT--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <div id="exportingFacilityDiv--v--${status.index}" <c:if test="${info.procurementMode ne masterCodeConstants.PROCUREMENT_MODE_IMPORT}">style="display: none;"</c:if>>
                                                                        <p class="assessment-title" style="font-size:15px; padding-bottom: 10px; font-weight: bold">Details of Exporting Facility:</p>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="facNameE--v--${status.index}">Facility Name</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="facNameE--v--${status.index}" id="facNameE--v--${status.index}" value='<c:out value="${info.facNameE}"/>'/>
                                                                                <span data-err-ind="facNameE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="postalCodeE--v--${status.index}">Postal Code</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="6" type="text" autocomplete="off" name="postalCodeE--v--${status.index}" id="postalCodeE--v--${status.index}" value='<c:out value="${info.postalCodeE}"/>'/>
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
                                                                                        <option value="${name.value}" <c:if test="${info.addressTypeE eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                                    </c:forEach>
                                                                                </select>
                                                                                <span data-err-ind="addressTypeE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="blockNoE--v--${status.index}">Block / House No.</label>
                                                                                <span id="aptMandatoryBlkE--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${info.addressTypeE ne aptBlk}">style="display:none;"</c:if>>*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="blockNoE--v--${status.index}" id="blockNoE--v--${status.index}" value='<c:out value="${info.blockNoE}"/>'/>
                                                                                <span data-err-ind="blockNoE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="floorNoE--v--${status.index}">Floor</label>
                                                                                <span id="aptMandatoryFloorE--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${info.addressTypeE ne aptBlk}">style="display:none;"</c:if>>*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="floorNoE--v--${status.index}" id="floorNoE--v--${status.index}" value='${info.floorNoE}' maxlength="250"/>
                                                                                <span data-err-ind="floorNoE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="unitNoE--v--${status.index}">Unit No.</label>
                                                                                <span id="aptMandatoryUnitE--v--${status.index}" class="mandatory otherQualificationSpan" <c:if test="${info.addressTypeE ne aptBlk}">style="display:none;"</c:if>>*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="unitNoE--v--${status.index}" id="unitNoE--v--${status.index}" value='${info.unitNoE}' maxlength="250"/>
                                                                                <span data-err-ind="unitNoE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="streetNameE--v--${status.index}">Street Name</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="streetNameE--v--${status.index}" id="streetNameE--v--${status.index}" value='<c:out value="${info.streetNameE}"/>'/>
                                                                                <span data-err-ind="streetNameE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="buildingNameE--v--${status.index}">Building Name</label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="buildingNameE--v--${status.index}" id="buildingNameE--v--${status.index}" value='<c:out value="${info.buildingNameE}"/>'/>
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
                                                                                        <option value="${na.value}" <c:if test="${info.countryE eq na.value}">selected="selected"</c:if>>${na.text}</option>
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
                                                                                <input maxlength="250" type="text" autocomplete="off" name="stateE--v--${status.index}" id="stateE--v--${status.index}" value='<c:out value="${info.stateE}"/>'/>
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
                                                                                <input maxlength="250" type="text" autocomplete="off" name="contactPersonNameE--v--${status.index}" id="contactPersonNameE--v--${status.index}" value='<c:out value="${info.contactPersonNameE}"/>'/>
                                                                                <span data-err-ind="contactPersonNameE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="emailAddressE--v--${status.index}">Email address</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="emailAddressE--v--${status.index}" id="emailAddressE--v--${status.index}" value='<c:out value="${info.emailAddressE}"/>'/>
                                                                                <span data-err-ind="emailAddressE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="contactNoE--v--${status.index}">Contact No.</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="contactNoE--v--${status.index}" id="contactNoE--v--${status.index}" value='<c:out value="${info.contactNoE}"/>'/>
                                                                                <span data-err-ind="contactNoE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="expectedDateE--v--${status.index}">Expected Date of Import</label>
                                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input type="text" autocomplete="off" name="expectedDateE--v--${status.index}" id="expectedDateE--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${info.expectedDateE}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                                <span data-err-ind="expectedDateE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="form-group ">
                                                                            <div class="col-sm-5 control-label">
                                                                                <label for="courierServiceProviderNameE--v--${status.index}">Name of Courier Service Provider</label>
                                                                            </div>
                                                                            <div class="col-sm-6">
                                                                                <input maxlength="250" type="text" autocomplete="off" name="courierServiceProviderNameE--v--${status.index}" id="courierServiceProviderNameE--v--${status.index}" value='<c:out value="${info.courierServiceProviderNameE}"/>'/>
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
                                                                                <textarea maxLength="250" class="col-xs-12" name="remarksE--v--${status.index}" id="remarksE--v--${status.index}" rows="3"><c:out value="${info.remarksE}"/></textarea>
                                                                                <span data-err-ind="remarksE--v--${status.index}" class="error-msg"></span>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </section>
                                                            </c:forEach>
                                                        </div>
                                                        <div class="form-group">
                                                            <div class="col-12">
                                                                <a id="addNewBatSection" style="text-decoration: none" href="javascript:void(0)">+ Add New ${(empty info.schedule and firstScheduleOp eq masterCodeConstants.FIFTH_SCHEDULE) or (info.schedule eq masterCodeConstants.FIFTH_SCHEDULE) ? "Toxin" : "Biological Agent"}</a>
                                                            </div>
                                                        </div>


                                                        <div class="modal fade" id="invalidPostalCodeModal" role="dialog">
                                                            <div class="modal-dialog modal-dialog-centered" role="document">
                                                                <div class="modal-content">
                                                                    <div class="modal-body">
                                                                        <div class="row">
                                                                            <div class="col-md-12"><span>The postal code is invalid</span></div>
                                                                        </div>
                                                                    </div>
                                                                    <div class="modal-footer" style="justify-content: center">
                                                                        <button type="button" class="btn btn-primary btn-lg" data-dismiss="modal">OK</button>
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
                                <%@include file="/WEB-INF/jsp/iais/facRegistration/InnerFooter.jsp" %>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>