<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%--@elvariable id="instructionInfo" type="sg.gov.moh.iais.egp.bsb.dto.renewal.FacilityInstructionDto"--%>
<%--@elvariable id="appNo" type="java.lang.String"--%>
<%--@elvariable id="appDt" type="java.util.Date"--%>
<webui:setLayout name="iais-internet"/>

<%@include file="../facRegistration/dashboard.jsp"%>

<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <div class="container">
        <div class="component-gp col-xs-12 col-sm-11 col-md-10 col-lg-8">
            <br/>
            <p style="font-size: 20px"><strong>Your submission is successful.</strong></p>
            <br/>
            <p>Application to renew the registration for Facility No. ${instructionInfo.facilityNo}</p>
            <br/>
            <p>We will notify you of the status of the application or if additional information is required.</p>
            <br/>
            <div class="col-xs-12" style="padding: 0 0 10px 0; border-bottom: 1px solid black">
                <div class="col-xs-4" style="padding: 0"><strong>Application Number</strong></div>
                <div class="col-xs-4" style="padding: 0"><strong>Date &amp; Time</strong></div>
            </div>
            <div class="col-xs-12" style="padding: 0 0 10px 0; margin-bottom: 10px">
                <div class="col-xs-4" style="padding: 0">${appNo}</div>
                <div class="col-xs-4" style="padding: 0">
                <fmt:formatDate value="${appDt}" pattern="dd/MM/yyyy HH:mm:ss"/></div>
            </div>
            <br/>
            <div class="row">
                <div class="col-xs-12 col-md-2"></div>
                <div class="col-xs-12 col-md-10">
                    <div class="text-right text-center-mobile">
                        <a class="btn btn-secondary" href="javascript:void(0);">PRINT</a>
                        <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg">HOME</a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</form>
