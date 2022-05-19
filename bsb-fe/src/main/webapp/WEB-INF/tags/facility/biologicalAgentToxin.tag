<%@tag description="Biological agent/toxin tag of facility registration" pageEncoding="UTF-8" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="bat" tagdir="/WEB-INF/tags/common" %>

<%@attribute name="activityTypes" required="true" type="java.util.List<java.lang.String>" %>
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
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-cascade-dropdown.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-biological-agent-toxin.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<script>
    <% String jsonStr = (String) request.getAttribute("scheduleBatMapJson");
       if (jsonStr == null || "".equals(jsonStr)) {
           jsonStr = "undefined";
       }
    %>
    var scheduleBatDataJson = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">

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
                                                <c:if test="${editJudge}"><div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div></c:if>
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
                                                    <c:if test='${activityTypes.contains(masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED)}'>
                                                        <li <c:if test="${activeNodeKey eq masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${masterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}" role="tab">Handling of Fifth Schedule Toxin for Exempted Purposes</a></li>
                                                    </c:if>
                                                </ul>
                                                <%@include file="../bat/atpBatInfo.tag"%>
                                            </div>
                                        </div>
                                    </div>
                                    <jsp:invoke fragment="innerFooterFrag"/>
                                </div>
                            </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>