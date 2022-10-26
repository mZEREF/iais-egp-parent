<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-audit.js"></script>
<%@include file="dashboard.jsp" %>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <div class="container">
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="internet-content">
                    <div class="bg-title">
                        <h2>You have successfully specified the audit date.</h2>
                    </div>
                </div>
                <div align="left">
                    <span><a href="/bsb-web/eservice/INTERNET/OngoingAuditList"><em class="fa fa-angle-left"></em> Back</a></span>
                </div>
            </div>
        </div>
    </div>
</form>