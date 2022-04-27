<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
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
    String webroot1= IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT;
%>
<webui:setLayout name="iais-intranet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=webroot1%>js/file-upload.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection.js"></script>


<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>

<div class="main-content">
    <div class="center-content">
        <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
            <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
            <input type="hidden" name="action_type" value="">
            <input type="hidden" name="action_value" value="">
            <input type="hidden" name="action_additional" value="">
            <span class="error-msg"><c:out value="${error_message}"/></span>

            <div class="col-xs-12">
                <h3>Please upload inspection checklist file</h3>
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
                                <p>You may download the template by clicking <a href="${pageContext.request.contextPath}/inspection/checklist/exporting-template">here</a>.</p>
                            </li>
                        </ul>
                    </div>
                </div>

                <div class="document-content">
                    <div class="document-upload-gp">
                        <div class="document-upload-list">
                            <h3>Inspection Checklist Information</h3>
                            <div class="file-upload-gp">
                                <span data-err-ind="checklistData" id="error_uploadFileError" class="error-msg"></span>
                                <c:if test="${not empty fileItemErrorMsgs}">
                                    <div class="col-xs-12 col-sm-12 margin-btm table-responsive itemErrorTableDiv">
                                        <span class="error-msg">There are invalid record(s) in the file. Please rectify them and reupload the file</span>
                                        <table aria-describedby="" class="table">
                                            <thead>
                                            <tr>
                                                <th scope="col" >Sheet Name</th>
                                                <th scope="col" >Row (S/N)</th>
                                                <th scope="col" >Field Name (Column)</th>
                                                <th scope="col" >Error Message</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <c:forEach var="item" items="${fileItemErrorMsgs}">
                                                <tr>
                                                    <td>${item.sheetName}</td>
                                                    <td>${item.row} (${item.indicator})</td>
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