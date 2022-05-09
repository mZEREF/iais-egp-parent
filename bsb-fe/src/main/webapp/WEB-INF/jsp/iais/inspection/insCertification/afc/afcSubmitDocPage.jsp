<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-ins-afc-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-ins-afc.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<%@include file="../dashboard.jsp" %>
<div class="main-content">
    <div class="container">
        <div class="row">
            <div class="col-xs-12">
                <div style="padding: 50px 0">
                    <form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>">
                        <%--@elvariable id="followUpViewDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto"--%>
                        <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
                        <input type="hidden" name="action_type" value="">
                        <input type="hidden" name="action_value" value="">
                        <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">

                        <%@ include file="../facilityInfo.jsp"%>
                        <br><br>
                        <ul>
                            <li>Pls include additional instructions for AFC and rearrange the instructions in the following order:</li>
                            <li>Please upload the Certification Report and other supporting documents related to the facility certification for review by the Facility and MOH.</li>
                            <li>Please ensure that the corresponding Document Type is selected for each document that is uploaded.</li>
                            <li>The maximum file size per document is 10 MB.</li>
                            <li>Acceptable file formats: CSV, DOC, DOCX, JPG, JPEG, PNG. PDF, XLS, XLSX</li>
                            <li>After each round of review, if a document has not been marked final by the Facility and MOH, the AFC shall address the comments (from the Facility and/or MOH) within before uploading the revised version of the document.</li>
                            <li>If a document has been marked final by both the Facility and MOH, the AFC can also mark the document final. The review cycle for the document would conclude when the document has been marked final by all three parties (the Facility, AFC and MOH), or at the end of 5 rounds of review, whichever is earlier. The last uploaded version of the document would be available in the facility's records.</li>
                        </ul>
                        <%@ include file="../upload.jsp" %>
                        <br><br>
                        <%@ include file="afcCertificationDocumentsPage.jsp" %>
                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12">
                                    <div class="button-group">
                                        <a href="/bsb-fe/eservice/INTERNET/MohBSBInboxMsg" class="btn btn-secondary">CANCEL</a>
                                        <a class="btn btn-primary" id="saveBtn">SUBMIT</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                    <%@ include file="/WEB-INF/jsp/iais/document/fileUploadModal.jsp" %>
                </div>
            </div>
        </div>
    </div>
</div>