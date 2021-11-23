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
<c:set var="dataSubmission" value="${arSuperDataSubmissionDto.dataSubmissionDto}" />

<%@ include file="common/arHeader.jsp" %>

<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/patientInformation.js"></script>

<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please upload patient information file</h3>
                <div class="tab-pane active" id="tabInbox" role="tabpanel">
                    <div class="document-info-list">
                        <ul>
                            <li>
                                <p><iais:message key="GENERAL_ERR0043" replaceName="configNum" propertiesKey="iais.system.upload.file.limit"></iais:message></p>
                            </li>
                            <li>
                                <p>Acceptable file formats are XLSX.</p>
                            </li>
                            <li>
                                <p>You may download the template by clicking <a href="/ds/ar/patient-info-file">here</a>.</p>
                            </li>
                            <li>
                                <p>The maximum number of records allowed is 10,000.</p>
                            </li>
                            <li>
                                <p>Records uploaded will be available in Online Enquiry after successful submission.</p>
                            </li>
                        </ul>
                    </div>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>