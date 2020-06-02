<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<webui:setLayout name="iais-intranet"/>

<%@ page contentType="text/html; charset=UTF-8" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<style>
    .col-md-10 {
        width: 100%;
    }

    .btn.btn-primary {
        font-size: 12px;
    }

</style>
<div class="main-content">
    <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
        <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br>
        <div class="tab-pane active" id="tabInbox" role="tabpanel">
            <div class="document-info-list">
                <ul>
                    <li>
                        <p>The maximum file size for each upload is 10MB. </p>
                    </li>
                    <li>
                        <p>Acceptable file formats are XLSX. </p>
                    </li>
                </ul>
            </div>
            <div class="tab-content">
                <div class="document-upload-gp">
                    <div class="document-upload-list">
                        <div class="file-upload-gp">
                            <div class="fileNameDisplay"></div>
                            <input id="selectedFile" name="selectedFile" type="file" style="display: none;" aria-label="selectedFile1"><a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                        </div>
                    </div>
                </div>
            </div>
            <c:choose>
                <c:when test="${empty ERR_CONTENT}">
                </c:when>
                <c:otherwise>
                    <table class="table">
                        <thead>
                        <tr>
                            <th>Code Value</th>
                            <th>Error Message</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="errDuplicateCodeResult" items="${ERR_DUPLICATE_CODE}" varStatus="status">
                                <tr>
                                    <td>${errDuplicateCodeResult}</td>
                                    <td>Duplicate value</td>
                                </tr>
                            </c:forEach>
                            <c:forEach var="errDuplicateCodeResult" items="${ERR_EMPTY_CODE}" varStatus="status">
                                <tr>
                                    <td>${errDuplicateCodeResult}</td>
                                    <td>Empty value</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="row">
            <div class="col-xs-6 col-md-4">
                <p><a id = "docBack" class="back" href="/system-admin-web/eservice/INTRANET/MohMasterCode"><em class="fa fa-angle-left"></em> Back</a></p>
            </div>
            <div class="col-xs-5 col-md-8 text-right">
                <div class="nav">
                    <br><br><br>
                    <div class="text-right text-center-mobile">
                        <div class="button-group"><a class="btn btn-primary next" id="docNext">Next</a></div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>

<script type="text/javascript">
    $('#docNext').click(function () {
        SOP.Crud.cfxSubmit("mainForm", "doUpload");
    });
</script>