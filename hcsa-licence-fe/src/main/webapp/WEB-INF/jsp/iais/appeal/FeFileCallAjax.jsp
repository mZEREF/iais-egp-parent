<%@ page import="com.ecquaria.cloud.helper.ConfigHelper" %>
<%
  String limit = ConfigHelper.getInt("iais.system.upload.file.type", 10);
%>

<input type="hidden" name="uploadFormId" id="uploadFormId" value="">
<input type="hidden" name="fileAppendId" id="fileAppendId" value="">
<input type="hidden" name="reloadIndex" id="reloadIndex" value="-1">
<input type="hidden" name="fileMaxSize" id="fileMaxSize" value="<%=limit%>>">

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
        var data = {"fileAppendId":repoId,"fileIndex":fileIndex};
        $.post(
            "${pageContext.request.contextPath}/deleteFeCallFile",
            data,
            function (data) {
                if(data != null && data == 1){
                    deleteFileFeDiv(repoId+"Div"+fileIndex);
                }
            }
        )
    }
    function ajaxCallUpload(idForm,fileAppendId){
        showWaiting();
        var reloadIndex =  $("#reloadIndex").val();
        if(reloadIndex == -1){
            $("#fileAppendId").val(fileAppendId);
        }
        fileAppendId =  $("#fileAppendId").val();
        $("#uploadFormId").val(idForm);
        var form = new FormData($("#"+idForm)[0]);
        var maxFileSize = $("#fileMaxSize").val();
        var rslt = validateFileSizeMaxOrEmpty(maxFileSize,fileAppendId);
        if (rslt == 'N') {
          $("#error_"+fileAppendId+"Error").html('The file has exceeded the maximum upload size of '+ maxFileSize + 'M.');
        } else if (rslt == 'E') {
          $("#error_"+fileAppendId+"Error").html('This field is mandatory.');
        } else {
          $.ajax({
            type:"post",
            url:"${pageContext.request.contextPath}/ajax-upload-file",
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
                    }else {
                        $("#error_"+fileAppendId+"Error").html(data.description);
                    }
                }
            },
            error: function (msg) {
                alert("error");
            }
          });
        }

        dismissWaiting();
    }
    
    function clearFlagValueFEFile() {
        $("#reloadIndex").val(-1);
        $("#fileAppendId").val("");
        $("#uploadFormId").val("");
    }

    function validateFileSizeMaxOrEmpty(maxSize,selectedFileId) {
        var fileId= '#'+selectedFileId;
        var fileV = $( fileId).val();
        var file = $(fileId).get(0).files[0];
        if(fileV == null || fileV == "" ||file==null|| file==undefined){
            return "E";
        }
        var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
        fileSize = parseInt(fileSize);
        if(fileSize>= maxSize){
            return "N";
        }
        return "Y";
    }
</script>
