<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants" %>
<%@ page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-data-file.js"></script>
<%--@elvariable id="appViewModuleType" type="java.lang.String"--%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <div class="preview-gp">
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <br/>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_NEW_FACILITY}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/facility/view.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_NEW_APPROVAL_APP}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/approval/view.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_NEW_FAC_CER_REG}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/afc/view.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_DEREGISTRATION_FACILITY}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/deregistration/deregistrationFacilityView.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_CANCELLATION_APPROVAL_APP}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/deregistration/cancellationApprovalView.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_DEREGISTRATION_FAC_CER_REG}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/deregistration/deregistrationAFCView.jsp" %>
                                                    </c:if>

                                                    <%-- Data Submission --%>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_CONSUME}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewConsumePage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_DISPOSAL}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewDisposalPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_EXPORT}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewExportPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_TRANSFER}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewTransferPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_RECEIPT}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewReceiptPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_RED_TEAMING_REPORT}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewRedTeamingReportPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq MasterCodeConstants.KEY_DATA_SUBMISSION_TYPE_BAT_INVENTORY}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewBatInventory.jsp" %>
                                                    </c:if>

                                                    <%-- inspection --%>
                                                    <c:if test="${appViewModuleType eq AppViewConstants.MODULE_VIEW_INSPECTION_FOLLOW_UP_ITEMS}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/inspection/doReviewInspectionFollowUpItemsView.jsp" %>
                                                    </c:if>
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
        </div>
    </div>
</form>