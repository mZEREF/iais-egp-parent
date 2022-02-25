<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-file.js"></script>
<%@include file="dashboard.jsp" %>
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
                                                <%--@elvariable id="dataSubInfo" type="sg.gov.moh.iais.egp.bsb.dto.submission.DataSubmissionInfo"--%>
                                                <div class="panel-group" role="tablist" aria-multiselectable="true">
                                                    <br/>
                                                    <%-- Data Submission --%>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE001'}">
                                                        <%@include file="viewConsumePage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE002'}">
                                                        <%@include file="viewDisposalPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE003'}">
                                                        <%@include file="viewExportPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE005'}">
                                                        <%@include file="viewTransferPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE006'}">
                                                        <%@include file="viewReceiptPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE007'}">
                                                        <%@include file="viewRedTeamingReportPage.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE008'}">
                                                        <%@include file="viewBatInventory.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE009'}">
                                                        <%@include file="viewRequestForTransfer.jsp" %>
                                                    </c:if>
                                                    <c:if test="${dataSubInfo.type eq 'DATTYPE010'}">
                                                        <%@include file="viewAckOfTransferOfReceipt.jsp" %>
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