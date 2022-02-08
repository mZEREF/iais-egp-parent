<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<%@include file="dashboard.jsp"%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="row col-xs-12 col-sm-12">
            <div class="dashboard-page-title" style="border-bottom: 1px solid black;">
                <h1>Notification of Consumption Submission</h1>
                <p>You are submitting for Notification of Consumption</p>
            </div>
        </div>
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p><strong>Successful Submission</strong></p>
            <p>Thank you for your submission.</p>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-2"></div>
                <div class="col-xs-12 col-md-10">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" href="javascript:void(0);">PRINT</a>
                        <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBsbReportableEvents">START ANOTHER SUBMISSION</a>
                        <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg">GO TO DASHBOARD</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
