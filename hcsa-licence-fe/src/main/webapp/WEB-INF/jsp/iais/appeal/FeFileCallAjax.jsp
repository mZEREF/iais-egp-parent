<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<input type="hidden" name="uploadFormId" id="uploadFormId" value="">
<input type="hidden" name="fileAppendId" id="fileAppendId" value="">
<input type="hidden" name="reloadIndex" id="reloadIndex" value="-1">
<input type="hidden" name="needGlobalMaxIndex" id="needGlobalMaxIndex" value="0">
<input type="hidden" name="fileMaxSize" id="fileMaxSize" value="${String.valueOf(ConfigHelper.getInt("iais.system.upload.file.limit", 10))}">
<input type="hidden" id="fileMaxMBMessage" name="fileMaxMBMessage" value="<iais:message key="GENERAL_ERR0019" propertiesKey="iais.system.upload.file.limit" replaceName="sizeMax" />">
<script type="text/javascript">
    function deleteFileFeAjax(id,fileIndex) {
        callAjaxDeleteFile(id,fileIndex);
    }
    function reUploadFileFeAjax(fileAppendId,index,idForm) {
         $("#reloadIndex").val(index);
         $("#fileAppendId").val(fileAppendId);
         $("#uploadFormId").val(idForm);
         $("#selectedFile").click();
    }

    function deleteFileFeDiv(id) {
        $("#"+id).remove();
    }
    function callAjaxDeleteFile(repoId,fileIndex){
        showWaiting();
        var data = {"fileAppendId":repoId,"fileIndex":fileIndex};
        $.post(
            "${pageContext.request.contextPath}/deleteFeCallFile",
            data,
            function (data) {
                if(data != null && data == 1){
                    deleteFileFeDiv(repoId+"Div"+fileIndex);
                }
                dismissWaiting();
            }
        )
    }

    function ajaxCallUpload(idForm,fileAppendId) {
        ajaxCallUploadForMax(idForm,fileAppendId,false);
    }

    function ajaxCallUploadForMax(idForm,fileAppendId,needMaxGlobalIndex){
        showWaiting();
        var reloadIndex =  $("#reloadIndex").val();
        if(reloadIndex == -1){
            $("#fileAppendId").val(fileAppendId);
        }
        $("#needGlobalMaxIndex").val(needMaxGlobalIndex);
        fileAppendId =  $("#fileAppendId").val();
        $("#uploadFormId").val(idForm);
        var form = new FormData($("#"+idForm)[0]);
        var maxFileSize = $("#fileMaxSize").val();
        var rslt = validateFileSizeMaxOrEmpty(maxFileSize,'selectedFile');
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
    
    function clearFlagValueFEFile() {
        $("#reloadIndex").val(-1);
        $("#fileAppendId").val("");
        $("#uploadFormId").val("");
        dismissWaiting();
    }

    function validateFileSizeMaxOrEmpty(maxSize,selectedFileId) {
        var fileId= '#'+selectedFileId;
        var fileV = $( fileId).val();
        var file = $(fileId).get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        //alert('fileSize:'+fileSize);
        //alert('maxSize:'+maxSize);
        fileSize = parseInt(fileSize);
        if(fileSize>= maxSize){
            $(fileId).after( $( fileId).clone().val(""));
            $(fileId).remove();
            return "N";
        }
        return "Y";
    }
    function cloneUploadFile() {
        var fileId= '#selectedFile';
        $(fileId).after( $( fileId).clone().val(""));
        $(fileId).remove();
    }
</script>
