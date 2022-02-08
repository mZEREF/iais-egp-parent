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
            <div class="row">
                <div class="col-xs-12">
                    <div class="center-content">
                        <div><span style="font-size:2rem;"><strong>Successful Submission</strong></span></div>
                        <div><span style="font-size:2rem;">Thank you for your submission.</span></div>
                    </div>
                </div>
                <p class="print"><div style="font-size: 16px;text-align: right;"><a href="javascript:void(0)" <%--onclick="printWDPDF()"--%>> <em class="fa fa-print"></em>Print</a></div></p>
            </div>
            <br/>
            <div class="row">
                <div class="row">
                    <div class="container">
                        <div class="col-xs-12 col-md-6 text-left">
                            <a class="back" href="/bsb-fe/eservice/INTERNET/MohBSBInboxApp"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-md-6 text-right">
                            <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBsbReportableEvents">START ANOTHER SUBMISSION</a>
                            <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg">GO TO DASHBOARD</a>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
