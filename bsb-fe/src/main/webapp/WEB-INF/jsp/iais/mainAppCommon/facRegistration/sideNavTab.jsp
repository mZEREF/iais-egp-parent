<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%--@elvariable id="activityTypes" type="java.util.List<java.lang.String>"--%>
<%--@elvariable id="activeNodeKey" type="java.lang.String"--%>
<ul id = "tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SECOND_SCHEDULE}" role="tab">Possession of First and/or Second Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_POSSESS_FIRST_SCHEDULE}" role="tab">Possession of First Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_LSP_FIRST_SCHEDULE}" role="tab">Large-Scale Production of First Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_LSP_THIRD_SCHEDULE}" role="tab">Large-Scale Production of Third Schedule Biological Agent</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_POSSESS_FIFTH_SCHEDULE}" role="tab">Possession of Fifth Schedule Toxin</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_SP_HANDLE_NON_FIRST_SCHEDULE_PV}" role="tab">Handling of non-First Schedule Poliovirus Infectious Materials</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL}" role="tab">Handling of Poliovirus Potentially Infectious Materials</a></li>
    </c:if>
    <c:if test='${activityTypes.contains(MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED)}'>
        <li <c:if test="${activeNodeKey eq MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}">class="active"</c:if> role="presentation"><a data-step-key="batInfo_${MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED}" role="tab">Handling of Fifth Schedule Toxin for Exempted Purposes</a></li>
    </c:if>
</ul>