<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
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

<script type="text/javascript" src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/file-upload.js"></script>

<c:set var="itemSize" value="${not empty fileItemSize ? fileItemSize : 0}" />
<c:set var="hasItems" value="${not empty SOVENOR_INVENTORY_LIST ? 1 : 0}" />
<%@ include file="common/dpHeader.jsp" %>


<form method="post" id="mainForm" action=<%=continueURL%>>
    <div class="main-content">
        <div class="container center-content">
            <div class="col-xs-12">
                <h3>Please upload Sovenor Inventory file</h3>
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
                                <p>You may download the template by clicking <a href="${pageContext.request.contextPath}/ds/dp/si-file" >here</a>.</p>
                            </li>
                            <li>
                                <p><iais:message key="GENERAL_ERR0052" params="maxCountMap" /></p>
                            </li>

                        </ul>
                    </div>
                    <div class="file-upload-gp" style="background-color: rgba(242, 242, 242, 1);padding: 20px;">
                        <h3 style="font-size: 16px;">
                            Sovenor Inventory
                            (<span id="itemSize"><fmt:formatNumber value="${itemSize}" pattern="#,##0"/></span>
                            records uploaded)
                        </h3>
                        <div class="col-xs-12">
                            <span id="error_uploadFileError" name="iaisErrorMsg" class="error-msg"></span>
                            <div id="iaisErrorMsgDS_ERR068" <c:if test="${!DS_ERR068}"> style="display: none" </c:if>>
                                <span class="error-msg" >
                                    There are errors in the file uploaded, which can be found <a href="${pageContext.request.contextPath}/ds/dp/si-err-file" >here</a>. Please rectify the errors and re-upload the file.
                                </span>
                            </div>

                        </div>
<%--                        <c:if test="${not empty fileItemErrorMsgs}">--%>
<%--                            <div class="col-xs-12 col-sm-12 margin-btm table-responsive itemErrorTableDiv">--%>
<%--&lt;%&ndash;                                <span class="error-msg">There are invalid record(s) in the file. Please rectify them and reupload the file</span>&ndash;%&gt;--%>
<%--                                <table aria-describedby="" class="table">--%>
<%--                                    <thead>--%>
<%--                                    <tr>--%>
<%--                                        <th scope="col" >Row</th>--%>
<%--                                        <th scope="col" >Field Name (Column)</th>--%>
<%--                                        <th scope="col" >Error Message</th>--%>
<%--                                    </tr>--%>
<%--                                    </thead>--%>
<%--                                    <tbody>--%>
<%--                                    <c:forEach var="item" items="${fileItemErrorMsgs}">--%>
<%--                                        <tr>--%>
<%--                                            <td>${item.row}</td>--%>
<%--                                            <td>${item.cellName} (${item.colHeader})</td>--%>
<%--                                            <td>${item.message}</td>--%>
<%--                                        </tr>--%>
<%--                                    </c:forEach>--%>
<%--                                    </tbody>--%>
<%--                                </table>--%>
<%--                            </div>--%>
<%--                        </c:if>--%>
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
                               onclick="$('#iaisErrorMsgDS_ERR068').hide();fileClicked(event)"
                               onchange="ajaxCallUploadForMax('mainForm', 'uploadFile', false);"/>
                        <a class="btn btn-file-upload btn-secondary" onclick="clearFlagValueFEFile()">Upload</a>
                        <input type="hidden" id="hasItems" name="hasItems" value="${hasItems}" />
                    </div>
                    <%--<div style="padding: 20px;">
                        <h3>Declarations</h3>
                        <div class="col-xs-12"><iais:message key="DS_DEC001" /></div>
                    </div>--%>
                </div>
                <br/><br/>

                <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                    <%@include file="common/dpDeclaration.jsp" %>
                </div>
                <br/><br/>
                <%@include file="common/dpFooter.jsp" %>
            </div>


        </div>
    </div>
</form>

<script type="text/javascript" >
    $(document).ready(function () {
        $('#_needReUpload').val(0);
        $('#_fileType').val("DS_DP");

        $('#saveDraftBtn').remove();
        $('#backBtn').unbind('click');
        $('#backBtn').click(function () {
            showWaiting();
            submit('back');
        });
    });

    function cloneUploadFile() {
        var fileId= '#uploadFile';
        $(fileId).after( $( fileId).clone().val(""));
        $(fileId).remove();
        var $btns = $('#uploadFileShowId').find('button');
        if ($btns.length >= 2) {
            $btns.not(':last').trigger('click');
        }
        $('#hasItems').val('0');
        $('#itemSize').html('0');
        $('#nextBtn').html('Submit');
        clearErrorMsg();
        $('#iaisErrorMsgDS_ERR068').hide();
        $('.itemErrorTableDiv').remove();
        submit('submission','preview');

    }

    function deleteFileFeAjax(id,fileIndex) {
        $('#hasItems').val('0');
        $('#itemSize').html('0');
        $('#nextBtn').html('Submit');
        callAjaxDeleteFile(id,fileIndex);
    }

    function doActionWhenError(data) {
        $('#uploadFileShowId div').hide();
        var $btns = $('#uploadFileShowId').find('button');
        if ($btns.length >= 0) {
            $btns.trigger('click');
        }
    }

</script>
