<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="firstScheduleOp" required="true" type="java.lang.String" %>
<%@attribute name="scheduleOps" required="true" type="java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>" %>
<%@attribute name="scheduleBatMap" required="true" type="java.util.Map<java.lang.String, java.util.List<com.ecquaria.cloud.moh.iais.common.dto.SelectOption>>" %>
<%@attribute name="batInfo" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-add-section.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<script>
    <% String jsonStr = (String) request.getAttribute("scheduleBatMapJson");
        if (jsonStr == null || "".equals(jsonStr)) {
            jsonStr = "undefined";
        }
    %>
    var scheduleBatDataJson = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<jsp:invoke fragment="dashboardFrag"/>
<iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="batInfo" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToSpecialDto"--%>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <input type="hidden" name="sectionIdx" value="<%=sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil.indexes(batInfo.getWorkActivities().size())%>">

    <input type="hidden" id="section_repeat_section_idx_name" value="sectionIdx" readonly disabled>
    <input type="hidden" id="section_repeat_section_id_prefix" value="workActivitySection" readonly disabled>
    <input type="hidden" id="section_repeat_section_group_id" value="sectionGroup" readonly disabled>
    <input type="hidden" id="section_repeat_separator" value="--v--" readonly disabled>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/approvalBatAndActivity/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="appInfoPanel" role="tabpanel">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <%@include file="/WEB-INF/jsp/iais/approvalBatAndActivity/subStepNavTab.jsp"%>
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
                                                                <label id="schedule" name="schedule">Second Schedule</label>
                                                            </div>
                                                        </div>
                                                        <div class="form-group ">
                                                            <div class="col-sm-5 control-label">
                                                                <label for="batName">Name of Biological Agent</label>
                                                                <span class="mandatory otherQualificationSpan">*</span>
                                                            </div>
                                                            <div class="col-sm-6">
                                                                <select name="batName"  class="batNameDropdown" id="batName">
                                                                    <c:set var="batNameOps" value="${scheduleBatMap.get(batInfo.schedule == null ? firstScheduleOp : batInfo.schedule)}"/>
                                                                    <c:forEach items="${batNameOps}" var="name">
                                                                        <option value="${name.value}" <c:if test="${batInfo.batName eq name.value}">selected="selected"</c:if>>${name.text}</option>
                                                                    </c:forEach>
                                                                </select>
                                                                <span data-err-ind="batName" class="error-msg"></span>
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
                                                                            <input maxlength="250" type="text" autocomplete="off" name="intendedWorkActivity--v--${status.index}" id="intendedWorkActivity--v--${status.index}" value='<c:out value="${activity.intendedWorkActivity}"/>'/>
                                                                            <span data-err-ind="intendedWorkActivity--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="activityStartDate--v--${status.index}">Start Date</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input type="text" autocomplete="off" name="activityStartDate--v--${status.index}" id="activityStartDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${activity.activityStartDate}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                            <span data-err-ind="activityStartDate--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="activityEndDate--v--${status.index}">End Date</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <input type="text" autocomplete="off" name="activityEndDate--v--${status.index}" id="activityEndDate--v--${status.index}" data-date-start-date="01/01/1900" value="<c:out value="${activity.activityEndDate}"/>" placeholder="dd/mm/yyyy" maxlength="250" class="date_picker form-control"/>
                                                                            <span data-err-ind="endDate--v--${status.index}" class="error-msg"></span>
                                                                        </div>
                                                                    </div>
                                                                    <div class="form-group ">
                                                                        <div class="col-sm-5 control-label">
                                                                            <label for="activityRemarks--v--${status.index}">Remarks</label>
                                                                            <span class="mandatory otherQualificationSpan">*</span>
                                                                        </div>
                                                                        <div class="col-sm-6">
                                                                            <textarea autocomplete="off" class="col-xs-12" name="activityRemarks--v--${status.index}" id="activityRemarks--v--${status.index}" maxlength="1000" style="width: 100%"><c:out value="${activity.activityRemarks}"/></textarea>
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
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
