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

<%@include file="../facRegistration/dashboard.jsp"%>

<%
    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd-MM-yyyy");
    java.util.Date currentTime = new java.util.Date();
    String currentDate = formatter.format(currentTime);
%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <h2><strong>Submission successful</strong></h2>
            <br/>
            <h2><strong>Application For Renewal of <iais:code code="${instructionInfo.type}"/></strong></h2>
            <br/>
            <p>A confirmation email will be sent to xxxxx.</p>
            <br/>
            <p>We will review your application and notify you if any changes are required.</p>
            <p>An inspection date will be arranged if necessary.</p>
            <div class="row">
                <div class="col-xs-12 col-md-2"></div>
                <div class="col-xs-12 col-md-10">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" href="javascript:void(0);">PRINT</a>
                        <a class="btn btn-secondary" href="/bsb-fe/eservice/INTERNET/MohBsbInboxApprovaAfc">HOME</a>
                        <a class="btn btn-secondary" href="javascript:void(0);">Preferred Date Range for Inspection/Certification</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
