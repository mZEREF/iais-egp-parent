<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="notExistFacActivityTypeApprovalList" required="true" type="java.util.List<java.lang.String>" %>
<%@attribute name="approvalToActivityDto" required="true" type="sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToActivityDto" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>
<%@attribute name="editJudge" type="java.lang.Boolean" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<jsp:invoke fragment="dashboardFrag"/>
<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="appInfoPanel" role="tabpanel">
                                    <%@ include file="/WEB-INF/jsp/iais/approvalBatAndActivity/InnerNavTab.jsp" %>
                                    <div class="form-horizontal">
                                        <p class="assessment-title" style="font-size:18px; font-weight: bold">Facility Activity Type <span style="color: red">*</span></p>
                                        <p class="assessment-title" style="border-bottom: 1px solid black; font-size:15px; padding-bottom: 10px;">Please select the facility activity type from the following:</p>
                                        <c:if test="${editJudge}"><div class="text-right app-font-size-16"><a id="edit" href="javascript:void(0)"><em class="fa fa-pencil-square-o"></em>Edit</a></div></c:if>
                                        <c:forEach items="${notExistFacActivityTypeApprovalList}" var="facActivity">
                                            <div>
                                                <input type="checkbox" name="facActivityTypes" id="facActivityTypes" <c:if test="${approvalToActivityDto.facActivityTypes.contains(facActivity)}">checked="checked"</c:if> value="${facActivity}"/>
                                                <label for="facActivityTypes" class="form-check-label"><span class="check-square"><iais:code code="${facActivity}"/></span></label>
                                            </div>
                                        </c:forEach>
                                        <div>
                                            <span data-err-ind="facActivityTypes" class="error-msg"></span>
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