<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>

<webui:setLayout name="iais-internet"/>

<c:set var="hasError" value="${not empty errorMsg}" />
<c:set var="itemSize" value="${not empty fileItemSize ? fileItemSize : 0}" />
<style>
    #tipTitle,#selectTitle{
        padding-left: 0px;
        padding-right: 0px;
        padding-bottom: 10px;
    }
    #clickingHere,#maxNumber,#maxFile,#fileFormat,#records{
        line-height: revert;!important;
    }
</style>

<div class="row form-group" style="margin-bottom: 10px;">
    <div id="selectTitle" class="col-xs-12 col-md-10">
        <strong style="font-size: 2rem;">What information do you want to submit?</strong>
    </div>
</div>

<div class="row form-group">
    <div class="col-xs-12 col-md-5" style="padding-left: 0px;">
        <iais:select name="sumbitType" id="sumbitType" firstOption="Please Select" value="${arSuperDataSubmissionDto.batchUploadType}" codeCategory="CATE_ID_AR_UPLOAD_TYPE"/>
    </div>
</div>
<div id="uploadFileDiv">
    <div class="row form-group" style="border-bottom: 1px solid #D1D1D1;">
        <div id="tipTitle" class="col-xs-12 col-md-10">
            <strong style="font-size: 2rem;">Please upload the completed file.</strong>
        </div>
        <br>
    </div>
    <div class="row form-group">
        <div class="tab-pane active" id="tabInbox" role="tabpanel">
            <div class="document-info-list">
                <ul>
                    <li>
                        <%--                    <a href="${pageContext.request.contextPath}/ds/dp/si-file" >here</a>--%>
                        <p id="maxFile">Please download the template by clicking <a href="#" >here</a>.</p>
                    </li>
                    <li>
                        <%--                    <p id="fileFormat"><iais:message key="GENERAL_ERR0052" params="maxCountMap" /></p>--%>
                        <p id="fileFormat">The maximum number of records is 10,000.</p>
                    </li>
                    <li>
                        <%--                    <p id="clickingHere"><iais:message key="GENERAL_ERR0043" replaceName="configNum" propertiesKey="iais.system.upload.file.limit" /></p>--%>
                        <p id="clickingHere">The maximum file size for each upload is 10MB.</p>
                    </li>
                    <li>
                        <p id="maxNumber">Acceptable file format is XLSX, CSV.</p>
                    </li>
                    <li>
                        <p id="records">You may retrieve the records uploaded via Online Enquiry after the file has been successfully uploaded.</p>
                    </li>
                </ul>
            </div>
            <div class="file-upload-gp" style="background-color: rgba(242, 242, 242, 1);padding: 35px;border-radius: 15px;">
                <h3 style="font-size: 16px;">
                    Patient Information
                    (<span id="itemSize"><fmt:formatNumber value="${itemSize}" pattern="#,##0"/></span>
                    records uploaded)
                </h3>
                <div class="col-xs-12">
                    <span id="error_uploadFileError" name="iaisErrorMsg" class="error-msg"></span>
                </div>
                <c:if test="${not empty fileItemErrorMsgs}">
                    <div class="col-xs-12 col-sm-12 margin-btm table-responsive itemErrorTableDiv">
                        <span class="error-msg">There are invalid record(s) in the file. Please rectify them and reupload the file</span>
                        <table aria-describedby="" class="table">
                            <thead>
                            <tr>
                                <th scope="col" >Sheet</th>
                                <th scope="col" >Row</th>
                                <th scope="col" >Field Name (Column)</th>
                                <th scope="col" >Error Message</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${fileItemErrorMsgs}">
                                <tr>
                                    <td>${item.sheetAt}</td>
                                    <td>${item.row}</td>
                                    <td>${item.cellName} (${item.colHeader})</td>
                                    <td>${item.message}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if>
                <div id="uploadFileShowId">
                    <c:if test="${not empty showPatientFile && !hasError}">
                        <div id="${showPatientFile.fileMapId}">
                            <iais:downloadLink fileRepoIdName="fileRo0" fileRepoId="${showPatientFile.fileUploadUrl}" docName="${showPatientFile.fileName}"/>
                            <button type="button" class="btn btn-secondary btn-sm" onclick="javascript:deleteFileFeAjax('uploadFile',${showPatientFile.index});">Delete</button>
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
    </div>
</div>
