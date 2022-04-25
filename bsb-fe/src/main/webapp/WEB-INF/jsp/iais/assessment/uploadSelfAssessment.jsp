<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.ecq.com/iais-bsb" prefix="iais-bsb" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
    String webroot1=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<webui:setLayout name="iais-internet"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-assessment.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<%--<script type="text/javascript" src="<%=webroot1%>js/file-upload.js"></script>--%>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<%@include file="dashboard.jsp" %>

<div class="main-content">
    <div class="container">
        <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="action_type" value="">
            <input type="hidden" name="action_value" value="">
            <input type="hidden" name="action_additional" value="">
            <span class="error-msg"><c:out value="${error_message}"/></span>

            <div class="col-xs-12">
                <h3>Please upload self assessment file</h3>
                <div class="tab-pane active" id="tabInbox" role="tabpanel">
                    <div class="document-info-list">
                        <ul>
                            <li>
                                <p><iais:message key="GENERAL_ERR0043" replaceName="configNum" propertiesKey="iais.system.upload.file.limit"></iais:message></p>
                            </li>
                            <li>
                                <p>Acceptable file formats is XLSX.</p>
                            </li>
                            <li>
                                <c:set var="maskApp"><iais:mask name="selfAssessAppId" value="${appId}"/></c:set>
                                <p>You may download the template by clicking <a href="${pageContext.request.contextPath}/self-assessment/exporting-template?appId=${maskApp}">here</a>.</p>
                            </li>
                            <li>
                                <p><iais:message key="GENERAL_ERR0052" params="maxCountMap" /></p>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="document-content">
                    <div class="document-upload-gp">
                        <div class="document-upload-list">
                            <h3>Self Assessment Information (${facCommittee.amount} records uploaded)</h3>
                            <div class="file-upload-gp">
                                <span data-err-ind="selfAssessmentData" id="error_uploadFileError" class="error-msg"></span>
                                <c:if test="${not empty fileItemErrorMsgs}">
                                    <div class="col-xs-12 col-sm-12 margin-btm table-responsive itemErrorTableDiv">
                                        <span class="error-msg">There are invalid record(s) in the file. Please rectify them and reupload the file</span>
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr>
                                                <th scope="col" >Row (S/N)</th>
                                                <th scope="col" >Field Name (Column)</th>
                                                <th scope="col" >Error Message</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="item" items="${fileItemErrorMsgs}">
                                                <tr>
                                                    <td>${item.row} (${item.indicator})</td>
                                                    <td>${item.cellName} (${item.sheetName})</td>
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
                                <%--<c:if test="${facCommittee.savedFile ne null}">
                                    <c:set var="repoId"><iais:mask name="file" value="${facCommittee.savedFile.repoId}"/></c:set>
                                    <div id="${repoId}FileDiv">
                                        <a href="/bsb-fe/ajax/doc/download/facReg/committee/repo/${repoId}" style="text-decoration: underline"><span id="${repoId}Span">${facCommittee.savedFile.filename}</span></a>(<fmt:formatNumber value="${facCommittee.savedFile.size/1024.0}" type="number" pattern="0.0"/>KB)
                                        <button type="button" class="btn btn-secondary btn-sm"
                                                onclick="deleteDataFile('${repoId}')">Delete</button>
                                        <span data-err-ind="${facCommittee.savedFile.repoId}" class="error-msg"></span>
                                    </div>
                                </c:if>
                                <c:if test="${facCommittee.newFile ne null}">
                                    <c:set var="tmpId"><iais:mask name="file" value="${facCommittee.newFile.tmpId}"/></c:set>
                                    <div id="${tmpId}FileDiv">
                                        <a href="/bsb-fe/ajax/doc/download/facReg/committee/new/${tmpId}" style="text-decoration: underline"><span id="${tmpId}Span">${facCommittee.newFile.filename}</span></a>(<fmt:formatNumber value="${facCommittee.newFile.size/1024.0}" type="number" pattern="0.0"/>KB)
                                        <button type="button" class="btn btn-secondary btn-sm"
                                                onclick="delete1DataFile('${tmpId}')">Delete</button>
                                        <span data-err-ind="${facCommittee.newFile.tmpId}" class="error-msg"></span>
                                    </div>
                                </c:if>

                                <div><a class="btn file-upload btn-secondary" data-upload-data-file="committeeData" href="javascript:void(0);">Upload</a></div>
                                <input type="file" id="committeeData" name="committeeData" data-data-file-input="committeeData" style="display: none">--%>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="alignctr" style="text-align: left">
                    <a id="back" href="javascript:void(0)"><em class="fa fa-angle-left"> </em> Back</a>
                </div>
                <div class="text-right text-center-mobile">
                    <a class="btn btn-primary" id="save" href="javascript:void(0);">Submit</a>
                </div>
            </div>
        </form>
    </div>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#_needReUpload').val(0);
        $('#_fileType').val("XLSX");
    });

    function clearFlagValueFEFile() {
        $("#reloadIndex").val(-1);
        $("#fileAppendId").val("");
        $("#uploadFormId").val("");
        $('.itemErrorTableDiv').hide();
        dismissWaiting();
    }
</script>