

<div class="modal fade" id="uploadDoc" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Upload Internal Document</h4>
            </div>
            <div class="modal-body">
                <form id="fileUploadForm" name="fileUploadForm" enctype="multipart/form-data"
                      action="<%=process.runtime.continueURL()%>" method="post">
                    <input type="hidden" id="uploadFile" name="uploadFile" value="">
                    <input type="hidden" name="taskId" value="${taskId}">
                    <input type="hidden" name="sopEngineTabRef"
                           value="<%=process.rtStatus.getTabRef()%>">
<%--                    action="" method="post">--%>
                      <div class="form-horizontal">
                        <div class="form-group">
                            <label class="col-xs-12 col-md-4 control-label">Document</label>
                            <div class="col-xs-8 col-sm-8 col-md-8">
                                <p><input type="text" maxlength="50" id="fileRemark" name="fileRemark" value="${fileRemarkString}"></p>
                                <br /> <small class="error"></small>
                                <span id="error_fileRemark" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>
                          <div class="form-group">
                              <label class="col-xs-12 col-md-4 control-label">Upload your files <span style="color: red"> *</span></label>
                              <div class="col-xs-8 col-sm-8 col-md-8">
                                  <p><input class = "inputtext-required" id = "selectedFile" name = "selectedFile" type="file"/></p>
                                  <br /> <small class="error"></small>
                                  <span id="error_selectedFile" name="iaisErrorMsg" class="error-msg"></span>
                              </div>
                          </div>

                      </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="closeUploadDoc()">cancel</button>
                <button type="button" id="uploadFileButton" class="btn btn-primary" onclick="uploadInternalDoc()">upload</button>
            </div>
        </div>
    </div>
</div>






<script type="text/javascript">

    function openUploadDoc(){
        $('#uploadDoc').dialog('open');
    };

    function closeUploadDoc(){
        $('#uploadDoc small.error').html('').hide();
        $('#error_selectedFile').html('').hide();
        $('#uploadDoc').dialog('close');
    };

    function validateUploadInternal(){
        var flag = true;
        $('#uploadDoc small.error').html('').hide();

        var fileRemarkMaxLength = 50;
        var fileRemarkLength = $('#fileRemark').val().length;
        if(fileRemarkLength > fileRemarkMaxLength){
            $('[name="fileRemark"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">Exceeding the maximum length by ' + fileRemarkMaxLength + '.</span>');
            flag = flag && false;
        }

        var file = $('#selectedFile').get(0).files[0];
        if(file==null || file=="" || file==undefined){
            $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file cannot be empty.</span>');
            flag = flag && false;
        }else{
            var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
            var selectedFile = $('#uploadDoc').find('[name="selectedFile"]').val();
            var fileName = $('#selectedFile').get(0).files[0].name;
            var fileType = fileName.substr(fileName.lastIndexOf(".")).toUpperCase();

            //check file type
            if (fileType != ".PDF" && fileType != ".PNG" && fileType != ".DOCX" && fileType != ".JPG" && fileType != ".JPEG" && fileType != ".DOC") {
                $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file type is invalid.</span>');
                flag = flag && false;
            }
            //check file size
            if(selectedFile==""){
                $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file cannot be empty.</span>');
                flag = flag && false;
            }else if(fileSize> 4){
                $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file size must less than 4M.</span>');
                flag = flag && false;
            }
        }

        if(!flag){
            $('#uploadDoc small.error').each(function(){
                var html = $(this).html();
                if(html){
                    $(this).show();
                }
            });
        }
        return flag;
    }

    function uploadInternalDoc(){
        if(validateUploadInternal()) {
            $('#uploadFile').val('Y');
            // callAjaxUploadFile();
            $('#uploadFileButton').attr("disabled", "disabled");
            $('#fileUploadForm').submit();
        }
    }

    function deleteFile(repoId) {
        $('#interalFileId').val(repoId);
        $('#mainForm').submit();
        //alert(repoId);
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
                alert('data name: ' + data.docName + 'data size: ' + data.docSize + ' ' + 'data type: ' +data.docType);
                closeUploadDoc();
                window.location.reload();

            },
            error: function (msg) {
                alert("error");
            }
        });
    }

</script>
