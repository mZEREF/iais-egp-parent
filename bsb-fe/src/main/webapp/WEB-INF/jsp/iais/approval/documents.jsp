<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>
<%@ include file="common/dashboard.jsp" %>
<form method="post" id="mainForm" enctype="multipart/form-data" action=<%=process.runtime.continueURL()%>>
    <input type="hidden" id="page_id" name="page_id" value="document_page">
    <input type="hidden" id="actionType" name="actionType" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane active" id="documentsTab" role="tabpanel">
                            </div>
                            <div class="document-content ">
                                <input type="hidden" name="uploadKey" value=""/>
                                <div id="selectFileDiv">
                                    <input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">
                                </div>
                                <div class="document-info-list">
                                    <ul>
                                        <li>
                                            <p>The maximum file size for each upload is <c:out value="${sysFileSize}"/>MB. </p>
                                        </li>
                                        <li>
                                            <p>Acceptable file formats are<c:out value="${sysFileType}"/></p>
                                        </li>
                                    </ul>
                                </div>
                                <div class="document-upload-gp">
                                    <h2>PRIMARY DOCUMENTS</h2>
                                    <%@include file="common/docContent.jsp"%>
                                </div>
                                <div class="application-tab-footer">
                                    <div class="row">
                                        <div class="col-xs-12 col-sm-6"><a class="back" id="Back"><em class="fa fa-angle-left"></em>Back</a></div>
                                        <div class="col-xs-12 col-sm-6">
                                            <div class="button-group">
                                                <a class="btn btn-secondary" id = "SaveDraft"  href="javascript:void(0);">Save as Draft</a>
                                                <a class="btn btn-primary" id="Next" href="javascript:void(0);">Next</a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
    <%@ include file="common/FeFileCallAjax.jsp" %>
</form>
<script type="text/javascript">
    $(document).ready(function () {
        $('.commDoc').change(function () {
            var maxFileSize = $('#sysFileSize').val();
            var error = validateUploadSizeMaxOrEmpty(maxFileSize, $(this));
            if (error == "N") {
                $(this).closest('.file-upload-gp').find('.error-msg').html($("#fileMaxMBMessage").val());
                $(this).closest('.file-upload-gp').find('span.delBtn').trigger('click');
                dismissWaiting();
            } else {
                $(this).closest('.file-upload-gp').find('.error-msg').html('');
                dismissWaiting();
            }

        });

        doEdit();
        if($("#errorMapIs").val()=='error'){
            $('#edit').trigger("click");
        }

        $('.file-upload').click(function () {
            var index = $(this).closest('.file-upload-gp').find('input[name="configIndex"]').val();
            $('input[name="uploadKey"]').val(index);
            clearFlagValueFEFile();
            $('#selectFileDiv').html('<input id="selectedFile" class="selectedFile"  name="selectedFile" type="file" style="display: none;" onclick="fileClicked(event)" onchange="fileChangedLocal(this,event)" aria-label="selectedFile1">');
            $('input[type="file"]').click();
        });
    });

    function fileClicked(event) {
        var fileElement = event.target;
        if (fileElement.value != "") {
            console.log("Clone( #" + fileElement.id + " ) : " + fileElement.value.split("\\").pop())
            clone[fileElement.id] = $(fileElement).clone(); //'Saving Clone'
        }
        //What ever else you want to do when File Chooser Clicked
    }

    // FileChanged()
    function fileChangedLocal(obj, event) {
        var fileElement = event.target;
        if (fileElement.value == "") {
            fileChanged(event);
        } else {
            var file = obj.value;
            if (file != null && file != '' && file != undefined) {
                var configIndex = $('input[name="uploadKey"]').val();
                ajaxCallUploadForMax('mainForm',configIndex,true);
            }
        }
    }


    function validateUploadSizeMaxOrEmpty(maxSize, $fileEle) {
        var fileV = $fileEle.val();
        var file = $fileEle.get(0).files[0];
        if (fileV == null || fileV == "" || file == null || file == undefined) {
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if (fileSize >= maxSize) {
            return "N";
        }
        return "Y";
    }


    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }


    $('.delBtn').click(function () {
        $(this).parent().children('span:eq(0)').html('');
        $(this).parent().children('span:eq(0)').next().html();
        $(this).parent().children('span:eq(0)').next().addClass("hidden");
        $(this).parent().children('input.selectedFile').val('');
        $(this).parent().children('input.delFlag').val('Y');

    });

    var doEdit = function () {
        $('#edit').click(function () {
            $('#edit-content').addClass('hidden');
            $('#isEditHiddenVal').val('1');
            $('input[type="file"]').prop('disabled', false);
            $('.existFile').removeClass('hidden');
            $('.existFile').removeClass('existFile');
            $('.file-upload').removeClass('hidden');
            $('.delFileBtn').removeClass('hidden');
            $('.reUploadFileBtn').removeClass('hidden');
        });
    }

    function saveDraft() {
        submit('documents', 'saveDraft', $('#selectDraftNo').val());
    }

    function cancelSaveDraft() {
        submit('documents', 'saveDraft', 'cancelSaveDraft');
    }

    function cancel() {
        $('#saveDraft').modal('hide');
    }

    function jumpPage() {
        submit('premises', 'saveDraft', 'jumpPage');
    }






    /*tangtangde*/
    function openUploadDoc(){
        $('#uploadDoc').dialog('open');
    };

    function closeUploadDoc(){
        $('#selectedFileShow').html('')
        $('#fileRemarkShow').html('')
        $('#fileRemark').val('');
        $('#uploadDoc').dialog().dialog('close');
        $('#uploadDoc').dialog('open');
        doDeleteShowFileName();
    };

    function uploadInternalDoc(){
        $('#uploadFileButton').attr("disabled",true);
        showWaiting();
        if(validateUploadInternal())
            callAjaxUploadFile();
        dismissWaiting();
        // closeUploadDoc();

    }

    function deleteFile(row,repoId) {
        showWaiting();
        $(row).parent('td').parent('tr').remove();
        callAjaxDeleteFile(repoId);
        dismissWaiting();
    }
    function callAjaxDeleteFile(repoId){
        var data = {"appDocId":repoId};
        $.post(
            "${pageContext.request.contextPath}/deleteInternalFile",
            data,
            function (data) {
                if(data != null && data.fileSn != -1){
                    if(data.fileSn == 0){
                        var tr ="<tr><td colspan='6'  align=\"left\" > " +data.noFilesMessage +"</td></tr> " ;
                        doAddTr(tr);
                    }
                }
            }
        )
    }

    function callAjaxUploadFile(){
        var form = new FormData($("#fileUploadForm")[0]);
        $.ajax({
            type: "post",
            url:  "${pageContext.request.contextPath}/uploadInternalFile",
            data: form,
            async:true,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function (data) {

                if(data != null && data.fileSn != null && data.fileSn != -1){
                    if(data.fileSn == 0){
                        removeNoData();
                    }
                    var tr = "<tr>"+"<td width=\"20%\"><p>" +data.name+"</p></td>" +"<td  width=\"20%\"><p>"+ data.url +data.name +"</p></td>"+
                        "<td width=\"15%\"><p>" +data.size+"KB"+"</p></td>"+ "<td width=\"20%\"><p>" +data.submitByName+"</p></td>"+ "<td width=\"25%\"><p>" +data.submitAtStr+"</p></td>"
                        + "<td width=\"10%\">" + "  <button type=\"button\" class=\"btn btn-secondary-del btn-sm\" onclick=\"javascript:deleteFile(this,'"+data.maskId+"');\">Delete</button>" +"</td>"+"</tr>";
                    doAddTr(tr);
                    $("#cancelDoc").click();
                }else if(data != null && data.fileSn ==-1){
                    $('#selectedFileShow').html(data.noFilesMessage);
                }else if(data != null && data.fileSn == null){
                    $('#selectedFileShow').html('The file size must less than 4M.');
                }
                $('#uploadFileButton').attr("disabled", false);
            },
            error: function (msg) {
                alert("error");
            }
        });
    }

    function doDeleteShowFileName() {
        var file = $("#selectedFile");
        var fileClone = file.clone();
        fileClone.change(function (){
            var file = $(this).val();
            if(file != null && file !=""){
                $('#selectedFileShowTextName').val(getFileName(file));
            }
        });
        file.after(fileClone.val(""));
        file.remove();
        $('#selectedFileShowTextName').val("");
    }

    function doAddTr(tr) {
        $("#tbodyFileListId").append(tr);
    }
    function removeNoData() {
        if(isIE()||isIE11()){
            $("#tbodyFileListId").find("tr")[0].removeNode(true);
        }else{
            $("#tbodyFileListId").find("tr")[0].remove();
        }
    }
    function isIE(){
        if(!!window.ActiveXObject||"ActiveXObject" in window){
            return true;
        }else{
            return false;
        }
    }
    function isIE11(){
        if((/Trident\/7\./).test(navigator.userAgent)){
            return true;
        }else{
            return false;
        }
    }

    function validateUploadInternal(){
        var maxSize = $("#fileMaxSize").val();
        var fileType = $("#fileUploadType").val();
        var flag = true;
        $('#selectedFileShow').html('')
        $('#fileRemarkShow').html('')
        var selectedFile = $('#uploadDoc').find('[name="selectedFile"]').val();

        var file = $('#selectedFile').get(0).files[0];
        if(selectedFile == null || selectedFile== "" ||file==null|| file==undefined){
            $('#selectedFileShow').html('This is mandatory.');
            $('#uploadFileButton').attr("disabled", false);
            return false;
        }else {
            if(maxSize == null ||  maxSize == ""){
                maxSize = 4;
            } else{
                maxSize =  parseInt(maxSize);
            }
            var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
            fileSize = parseInt(fileSize);
            if(fileSize>= maxSize){
                $('#selectedFileShow').html($("#fileMaxMBMessage").val());
                if(fileSize >= 100){
                    doDeleteShowFileName();
                }
                $('#uploadFileButton').attr("disabled", false);
                return false;
            }
            if(fileType == null || fileType == ""){
                fileType = "PDF,JPG,PNG,DOCX,DOC";
            }

            try {
                var fileName =  getFileName($("#selectedFile").val());
                if(fileName.length > 100){
                    $('#selectedFileShow').html($("#fileMaxLengthMessage").val());
                    $('#uploadFileButton').attr("disabled", false);
                    return false;
                }
                var list = fileName.split(".");
                fileName = list[list.length-1];
                if(fileType.indexOf(fileName.toUpperCase()) == -1){
                    var fileTypelist = fileType.split(",");
                    if(fileTypelist.length >5) {
                        var stringBiff = "";
                        for(var indexlist = 0;indexlist <fileTypelist.length; indexlist++){
                            if(indexlist== 0){
                                stringBiff += fileTypelist[indexlist];
                            }else if(indexlist== fileTypelist.length-1){
                                stringBiff += fileTypelist[indexlist]+"<br/>";
                            }  else if(indexlist %5 == 0) {
                                stringBiff += fileTypelist[indexlist] +"," +"<br/>";

                            }else {
                                stringBiff += fileTypelist[indexlist] +",";
                            }
                        }
                        fileType = stringBiff;
                    }
                    $('#selectedFileShow').html('Only files with the following extensions are allowed:'+ fileType +'. Please re-upload the file.');
                }
            }catch (e){
            }
        }
        var fileRemarkMaxLength = 50;
        var fileRemarkLength = $('#fileRemark').val().length;
        if(fileRemarkLength > fileRemarkMaxLength){
            $('#fileRemarkShow').html('Exceeding the maximum length by ' + fileRemarkMaxLength );
            $('#uploadFileButton').attr("disabled", false);
            flag = flag && false;
        }
        return flag;
    }

    $('#selectedFileShowText').click(function (){
        $('#selectedFile').click();
    });
    $('#selectedFileShowTextName').click(function (){
        $('#selectedFile').click();
    });


    $('#selectedFile').change(
        fileChange()
    );
    function fileChange(){
        var file = $("#selectedFile").val();
        if(file != null && file !=""){
            $('#selectedFileShowTextName').val(getFileName(file));
        }else {
            $('#selectedFileShowTextName').val("");
        }
    }
    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }

    // FileChanged()
    function fileChanged(event) {
        var fileElement = event.target;
        if (fileElement.value == "") {
            clone[fileElement.id].insertBefore(fileElement); //'Restoring Clone'
            $(fileElement).remove(); //'Removing Original'
            if (evenMoreListeners) { addEventListenersTo(clone[fileElement.id]) }//If Needed Re-attach additional Event Listeners
        }else {
            $('#selectedFile').change(
                fileChange()
            );
        }
        //What ever else you want to do when File Chooser Changed
    }
</script>

