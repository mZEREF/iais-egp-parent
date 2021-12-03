<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<div class="main-content">
    <form class="form-horizontal" id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
        <input type="hidden" name="action_type" value="">
        <input type="hidden" name="action_value" value="">
        <input type="hidden" name="action_additional" value="">
        <input type="hidden" name="decision" id="decision" value="${processData.aoDecision}">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content" id="clearSelect">
                        <div class="bg-title">
                            <h2>Cancel Audit</h2>
                        </div>
                        <%--@elvariable id="processData" type="sg.gov.moh.iais.egp.bsb.dto.audit.OfficerProcessAuditDto"--%>
                        <iais:row>
                            <iais:field value="Facility Name" width="15" required="false"/>
                            <iais:value width="10"><c:out value="${processData.facName}"/></iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Activity Type" width="15" required="false"/>
                            <iais:value width="10"><c:out value="${processData.activityType}"/></iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Audit Date" width="15" required="false"/>
                            <iais:value width="10"><fmt:formatDate value="${processData.auditDate}" pattern="dd/MM/yyyy"/></iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Audit Type" width="15" required="false"/>
                            <iais:value width="10"><iais:code code="${processData.auditType}"/></iais:value>
                        </iais:row>

                        <iais:row>
                            <iais:field value="Duty Officer Cancellation Reasons" width="15" required="false"/>
                            <iais:value width="10"><c:out value="${processData.cancelReason}"/></iais:value>
                        </iais:row>
                        <div id="processingDecision">
                            <iais:row>
                                <iais:field value="Processing Decision" required="false"/>
                                <iais:value width="10">
                                    <iais:code code="${processData.aoDecision}"/>
                                </iais:value>
                            </iais:row>
                        </div>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6">
                                <a class="back" id="back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                            </div>
                            <div align="right">
                                <button id="AOProcessCancelAudit" type="button" class="btn btn-primary">
                                    Submit
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
