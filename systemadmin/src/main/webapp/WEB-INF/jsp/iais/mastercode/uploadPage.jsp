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
<div class="main-content" style="min-height: 73vh;">
    <form  method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <br><br>
        <div class="tab-pane active" id="tabInbox" role="tabpanel">
            <div class="document-info-list">
                <ul>
                    <li>
                        <p><iais:message key="GENERAL_ERR0043" replaceName="configNum" propertiesKey="iais.system.upload.file.limit"></iais:message></p>
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
                            <input id="selectedFile" name="selectedFile" class="selectedFile commDoc"
                                   type="file" style="display: none;"
                                   aria-label="selectedFile1"
                                   onclick="fileClicked(event)"
                                   onchange="fileChanged(event)">
                            <a class="btn btn-file-upload btn-secondary"
                               href="#">Upload</a>
                            <span id="delFile" hidden="hidden"><strong id="fileName"></strong>&nbsp;&nbsp;<button type="button" class="btn btn-secondary-del btn-sm" onclick="deleteWdFile()">Delete</button></span>
                        </div>
                    </div>
                </div>
            </div>
            <span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>
        </div>
        <div class="row">
            <div class="col-xs-6 col-md-4" style="padding-top: 4%">
                <div><a href="/system-admin-web/eservice/INTRANET/MohMasterCode"><em class="fa fa-angle-left"></em> Back</a></div>
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

    $(".selectedFile").change(function () {
        // $("#delFile").removeAttr("hidden");
        // var fileName = $("#selectedFile").val();
        // console.log(typeof(fileName));
        // if(fileName != null){
        //     var pos = fileName.lastIndexOf("\\");
        //     $("#fileName").html(fileName.substring(pos + 1));
        // }
        var file = $(this).val();
        var fileName = getFileName(file);
        $("#fileName").html(fileName);
        $("#delFile").removeAttr("hidden");
        // $("#fileName").next().html('&nbsp;&nbsp;<button type="button" class="btn btn-secondary btn-sm">Delete</button>');
        // $("#fileName").val('N');
    });

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    function deleteWdFile() {
        // document.getElementById("withdrawFile").files[0] = null;
        let wdfile = $("#selectedFile");
        wdfile.val("");
        // wdfile.remove();
        $("#delFile").attr("hidden", "hidden");
    }
</script>