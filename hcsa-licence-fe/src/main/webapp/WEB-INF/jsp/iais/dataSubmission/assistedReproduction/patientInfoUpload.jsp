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
                                <p>Acceptable file formats are XLSX, CSV.</p>
                            </li>
                            <li>
                                <p>You may download the template by clicking <a href="/ds/ar/patient-info-file">here</a>.</p>
                            </li>
                            <li>
                                <p><iais:message key="GENERAL_ERR0052" params="maxCountMap" /></p>
                            </li>
                            <li>
                                <p>Records uploaded will be available in Online Enquiry after successful submission.</p>
                            </li>
                        </ul>
                    </div>
                    <div class="file-upload-gp">
                        <h4>Patient Information (<span id="itemSize">0</span> records uploaded)</h4>
                        <%--<h4>Patient Information (1,000 records uploaded)</h4>--%>
                        <c:if test="${not empty fileItemErrorMsgs}">
                        <div class="col-xs-12 col-sm-12 margin-btm table-responsive">
                            <div class="error-msg">There are invalid record(s) in the file. Please rectify them and reupload the file</div>
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" >Row</th>
                                    <th scope="col" >Field Name (Column)</th>
                                    <th scope="col" >Error Message</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="item" items="fileItemErrorMsgs">
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
                            <c:if test="${not empty pageShowFileDto}">
                            <div id="${pageShowFileDto.fileMapId}">
                                <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                <a href="${pageContext.request.contextPath}/file-repo?filerepo=fileRo0&fileRo0=<iais:mask name="fileRo0" value="${pageShowFileDto.fileUploadUrl}"/>&fileRepoName=${pageShowFileDto.fileName}"
                                   title="Download" class="downloadFile">${pageShowFileDto.fileName}</a>
                                </span>
                                <%--<span class="error-msg" name="iaisErrorMsg" id="file"></span>--%>
                                <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:deleteFileFeAjax('selected${sec}File',${pageShowFileDto.index});">Delete</button>
                                <%--<button type="button" class="btn btn-secondary btn-sm" onclick="javascript:reUploadFileFeAjax('selected${sec}File',${pageShowFileDto.index},'mainForm');">ReUpload</button>--%>
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
                    </div>
                    <span id="error_uploadFileError" name="iaisErrorMsg" class="error-msg"></span>
                </div>
                <%@include file="common/arFooter.jsp" %>
            </div>
        </div>
    </div>
    <%--<input type="hidden" name="fileMaxSize" id="fileMaxSize" value="${String.valueOf(ConfigHelper.getInt("iais.system.upload.file.limit", 10))}">--%>
    <%@ include file="../../appeal/FeFileCallAjax.jsp" %>
</form>
<script type="text/javascript">
    $(document).ready(function () {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX, CSV");
    });

    function cloneUploadFile() {
        var fileId= '#uploadFile';
        $(fileId).after( $( fileId).clone().val(""));
        $(fileId).remove();
    }
   /* function ajaxCallUploadForMax(idForm, fileAppendId, uploadFileShowId) {
        showWaiting();
        /!**
        * <input type="hidden" name="uploadFormId" id="uploadFormId" value="">
<input type="hidden" name="fileAppendId" id="fileAppendId" value="">
<input type="hidden" name="reloadIndex" id="reloadIndex" value="-1">
<input type="hidden" name="needGlobalMaxIndex" id="needGlobalMaxIndex" value="0">
<input type="hidden" name="fileMaxSize" id="fileMaxSize" value="${String.valueOf(ConfigHelper.getInt("iais.system.upload.file.limit", 10))}">
<input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">

        * *!/
        var $form = $("#"+idForm);
        if ($form.find('#_fileAppendId').length <= 0) {
            $form.append('<input type="hidden" name="_fileAppendId" id="_fileAppendId" value="">');
        }
        $form.find('#_fileAppendId').val(fileAppendId);
        if ($form.find('#_uploadFileShowId').length <= 0) {
            $form.append('<input type="hidden" name="_uploadFileShowId" id="_uploadFileShowId" value="">');
        }
        if (isEmpty(uploadFileShowId)) {
            uploadFileShowId = fileAppendId + 'ShowId';
        }
        $form.find('#_uploadFileShowId').val(uploadFileShowId);
        var form = new FormData($("#"+idForm)[0]);
        var maxFileSize = $("#fileMaxSize").val();
        var rslt = validateFileSizeMaxOrEmpty(maxFileSize, fileAppendId);
        //alert('rslt:'+rslt);
        if (rslt == 'N') {
            $("#error_"+fileAppendId+"Error").html($("#fileMaxMBMessage").val());
            clearFlagValueFEFile();
        } else if (rslt == 'E') {
            clearFlagValueFEFile();
        } else {
            $.ajax({
                type:"post",
                url:"${pageContext.request.contextPath}/ajax-upload-file?stamp="+new Date().getTime(),
                data: form,
                async:true,
                dataType: "json",
                processData: false,
                contentType: false,
                success: function (data) {
                    if(data != null && data.description != null){
                        if( data.msgType == "Y"){
                            if(reloadIndex != -1){
                                $("#"+fileAppendId+"Div"+reloadIndex).after("<Div id = '" +fileAppendId+"Div"+reloadIndex+"Copy' ></Div>");
                                deleteFileFeDiv(fileAppendId+"Div"+reloadIndex);
                                $("#reloadIndex").val(-1);
                                $("#"+fileAppendId+"Div"+reloadIndex+"Copy").after(data.description);
                                deleteFileFeDiv(fileAppendId+"Div"+reloadIndex+"Copy");
                            }else {
                                $("#"+fileAppendId+"ShowId").append(data.description);
                            }
                            $("#error_"+fileAppendId+"Error").html("");
                            cloneUploadFile();
                        }else {
                            $("#error_"+fileAppendId+"Error").html(data.description);
                        }
                    }
                    dismissWaiting();
                },
                error: function (msg) {
                    //alert("error");
                    dismissWaiting();
                }
            });
        }
    }

    function validateFileSizeMaxOrEmpty(maxSize,selectedFileId) {
        if (isNaN(maxSize)) {
            return 'Y';
        }
        var fileId= '#'+selectedFileId;
        var fileV = $(fileId).val();
        var file = $(fileId).get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if(fileSize>= parseInt(maxSize)){
            $(fileId).after($(fileId).clone().val(""));
            $(fileId).remove();
            return "N";
        }
        return "Y";
    }*/
</script>