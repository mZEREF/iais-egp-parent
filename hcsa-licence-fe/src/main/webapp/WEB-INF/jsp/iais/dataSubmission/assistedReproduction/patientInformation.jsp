<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String continueURL = "";
    if (process != null && process.runtime != null && process.runtime.getBaseProcessClass() != null) {
        continueURL = process.runtime.continueURL();
    }
%>
<webui:setLayout name="iais-internet"/>

<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<c:set var="husband" value="${patientInfoDto.husband}" />

<%@ include file="common/arHeader.jsp" %>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please key in patient information</h3>
                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="section/patientDetailSection.jsp" %>
                    <%@include file="section/husbandDetailSection.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
