<input type="hidden" name="uploadFormId" id="uploadFormId" value="">
<input type="hidden" name="fileAppendId" id="fileAppendId" value="">
<input type="hidden" name="reloadIndex" id="reloadIndex" value="-1">

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
                        $("#"+fileAppendId+"ErrorMsg").html("");
                    }else {
                        $("#"+fileAppendId+"ErrorMsg").html(data.description);
                    }
                }
            },
            error: function (msg) {
                alert("error");
            }
        });
        dismissWaiting();
    }
    
    function clearFlagValueFEFile() {
        $("#reloadIndex").val(-1);
        $("#fileAppendId").val("");
        $("#uploadFormId").val("");
    }
</script>
