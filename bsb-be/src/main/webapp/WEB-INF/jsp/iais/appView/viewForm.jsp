<%@ page import="sg.gov.moh.iais.egp.bsb.util.TableDisplayUtil"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
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
                                                    <c:if test="${appViewModuleType eq 'viewNewFacility'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/facility/view.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'viewNewApprovalApp'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/approval/view.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'viewNewFacCerReg'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/afc/view.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'viewDeRegistrationFacility'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/deregistration/deregistrationFacilityView.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'viewCancellationApprovalApp'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/deregistration/cancellationApprovalView.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'viewDeRegistrationFacCerReg'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/deregistration/deregistrationAFCView.jsp" %>
                                                    </c:if>

                                                    <%-- Data Submission --%>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE001'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewConsumePage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE002'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewDisposalPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE003'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewExportPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE005'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewTransferPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE006'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewReceiptPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE007'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewRedTeamingReportPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${appViewModuleType eq 'DATTYPE008'}">
                                                        <%@include file="/WEB-INF/jsp/iais/appView/datasubmission/viewBatInventory.jsp" %>
                                                    </c:if>

                                                    <%-- inspection --%>
                                                    <c:if test="${appViewModuleType eq 'viewInspectionFollowUpItems'}">
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