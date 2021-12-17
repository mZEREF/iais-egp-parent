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

<c:set var="patientInfoDto" value="${arSuperDataSubmissionDto.patientInfoDto}" />
<c:set var="patient" value="${patientInfoDto.patient}" />
<c:set var="previous" value="${patientInfoDto.previous}" />
<c:set var="husband" value="${patientInfoDto.husband}" />
<c:set var="dataSubmission" value="${arSuperDataSubmissionDto.dataSubmissionDto}" />

<c:set var="hasError" value="${not empty errorMsg}" />
<c:set var="itemSize" value="${not empty fileItemSize ? fileItemSize : 0}" />
<c:set var="hasItems" value="${not empty PATIENT_INFO_LIST ? 1 : 0}" />

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
                        <c:if test="${not empty errorMsg}">
                        <div class="col-xs-12">
                            <span id="error_uploadFileError" name="iaisErrorMsg" class="error-msg"></span>
                        </div>
                        </c:if>
                        <c:if test="${not empty fileItemErrorMsgs}">
                        <div class="col-xs-12 col-sm-12 margin-btm table-responsive itemErrorTableDiv">
                            <span class="error-msg">There are invalid record(s) in the file. Please rectify them and reupload the file</span>
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" >Row</th>
                                    <th scope="col" >Field Name (Column)</th>
                                    <th scope="col" >Error Message</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="${fileItemErrorMsgs}">
                                <tr>
                                    <td>${item.row}</td>
                                    <td>${item.fieldName}</td>
                                    <td>${item.message}</td>
                                </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        </c:if>
                        <div name="uploadFileShowId" id="uploadFileShowId">
                            <c:if test="${not empty pageShowFileDto && !hasError}">
                            <div id="${pageShowFileDto.fileMapId}">
                                <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                    <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${pageShowFileDto.fileUploadUrl}" docName="${pageShowFileDto.fileName}"/>
                                </span>
                                <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:deleteFileFeAjax('selected${sec}File',${pageShowFileDto.index});">Delete</button>
                            </div>
                            </c:if>
                        </div>
                        <br/>
                        <input id="uploadFile" name="selectedFile"
                               class="uploadFile commDoc" style="display: none;"
                               type="file" aria-label="uploadFile"
                               onclick="fileClicked(event)"
                               onchange="ajaxCallUploadForMax('mainForm', 'uploadFile', false);"/>
                        <a class="btn btn-file-upload btn-secondary" onclick="clearFlagValueFEFile()">Upload</a>
                        <input type="hidden" id="hasItems" name="hasItems" value="${hasItems}" />
                    </div>
                </div>
                <br/><br/>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
    <%@ include file="../../appeal/FeFileCallAjax.jsp" %>
</form>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/patientInfoUpload.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX, CSV");

        $('#saveDraftBtn').remove();
    });

    function cloneUploadFile() {
        var fileId= '#uploadFile';
        $(fileId).after( $( fileId).clone().val(""));
        $(fileId).remove();
        $('.itemErrorTableDiv').remove();
        var $btns = $('#uploadFileShowId').find('button');
        if ($btns.length >= 2) {
            $btns.not(':last').trigger('click');
        }
        $('#hasItems').val('0');
        $('#itemSize').html('0');
        $('#nextBtn').html('Preview');
    }

    function deleteFileFeAjax(id,fileIndex) {
        $('#hasItems').val('0');
        $('#itemSize').html('0');
        callAjaxDeleteFile(id,fileIndex);
    }
</script>