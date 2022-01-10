<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

<c:set var="dataSubmission" value="${arSuperDataSubmissionDto.dataSubmissionDto}" />

<c:set var="itemSize" value="${not empty fileItemSize ? fileItemSize : 0}" />

<%@ include file="common/arHeader.jsp" %>

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
                                <p>Acceptable file formats are XLSX, CSV.</p>
                            </li>
                            <li>
                                <p>You may download the template by clicking <a href="${pageContext.request.contextPath}/ds/ar/patient-info-file" >here</a>.</p>
                            </li>
                            <li>
                                <p><iais:message key="GENERAL_ERR0052" params="maxCountMap" /></p>
                            </li>
                            <li>
                                <p>Records uploaded will be available in Online Enquiry after successful submission.</p>
                            </li>
                        </ul>
                    </div>
                    <div class="file-upload-gp" style="background-color: rgba(242, 242, 242, 1);padding: 20px;">
                        <h3 style="font-size: 16px;">
                            Patient Information
                            (<span id="itemSize"><fmt:formatNumber value="${itemSize}" pattern="#,##0"/></span>
                            records uploaded)
                        </h3>
                        <div name="uploadFileShowId" id="uploadFileShowId">
                            <div id="${showPatientFile.fileMapId}">
                                <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${showPatientFile.fileUploadUrl}" docName="${showPatientFile.fileName}"/>
                            </div>
                        </div>
                    </div>
                    <%--<div style="padding: 20px;">
                        <h3>Declarations</h3>
                        <div class="col-xs-12"><iais:message key="DS_DEC001" /></div>
                    </div>--%>
                </div>
                <br/><br/>

                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="common/arDeclaration.jsp" %>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
</form>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/patientInfoUpload.js"></script>

