<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
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
                            <span id="delFile" hidden="hidden"><strong id="fileName"></strong><button type="button" class="btn btn-danger btn-sm" onclick="deleteWdFile()"> <em
                                    class="fa fa-times"></em></button></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6 col-md-4" style="padding-top: 4%">
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

    $("#selectedFile").change(function () {
        $("#delFile").removeAttr("hidden");
        var fileName = $("#selectedFile").val();
        var pos = fileName.lastIndexOf("\\");
        $("#fileName").html(fileName.substring(pos + 1));
    });

    function deleteWdFile() {
        // document.getElementById("withdrawFile").files[0] = null;
        let wdfile = $("#selectedFile");
        wdfile.val("");
        // wdfile.remove();
        $("#delFile").attr("hidden", "hidden");
    }
</script>