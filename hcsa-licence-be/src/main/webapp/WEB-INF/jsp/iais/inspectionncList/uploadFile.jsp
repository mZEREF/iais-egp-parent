

<div class="modal fade" id="uploadDoc" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeUploadDoc()">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Upload Internal Document</h4>
            </div>
            <div class="modal-body">
                <form id="fileUploadForm" name="fileUploadForm" enctype="multipart/form-data"
                  action="" method="post">
                    <div class="form-horizontal">
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">Document</label>
                        <div class="col-xs-8 col-sm-8 col-md-8">
                            <p><input type="text" maxlength="50" id="fileRemark" name="fileRemark" value="${fileRemarkString}"></p>
                            <br /> <small class="error" ><span id ="fileRemarkShow" style="color: #D22727; font-size: 1.6rem"></span></small>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-xs-12 col-md-4 control-label">Upload your files <span style="color: red"> *</span></label>
                        <div class="col-xs-8 col-sm-8 col-md-8">
                            <div style="margin-left: -15px" class="col-md-5">
                            <p><input  id = "selectedFileShowText" name = "selectedFileShowText"  type="text"   value="Select File" readonly>
                                </p>
                            </div>
                            <div style="margin-left: -15px" class="col-md-8">
                                <input  id = "selectedFileShowTextName" name = "selectedFileShowTextName"  type="text"   readonly>
                            </div>
                            <div hidden><input class = "inputtext-required" id = "selectedFile" name = "selectedFile" type="file"></div>
                          <small class="error"><span id="selectedFileShow" style="color: #D22727; font-size: 1.6rem"></span></small>
                        </div>
                    </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="cancelDoc" onclick="closeUploadDoc()">cancel</button>
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
        $('#selectedFileShow').html('')
        $('#fileRemarkShow').html('')
        $('#fileRemark').val('');
        $('#uploadDoc').dialog('close');
        doDeleteShowFileName();
    };

    function uploadInternalDoc(){
            $('#uploadFileButton').attr("disabled",true);
            showWaiting();
            if(validateUploadInternal())
                callAjaxUploadFile();
            dismissWaiting();
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
                        var tr ="<tr><td colspan='6'  align=\"center\" > " +data.noFilesMessage +"</td></tr> " ;
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
                    var tr = "<tr>"+"<td width=\"30%\"><p>" +data.docDesc+"</p></td>" +"<td  width=\"20%\"><p>"+  data.url +data.docName+"."+data.docType+"</p></td>"+
                        "<td width=\"10%\"><p>" +data.docSize+"KB"+"</p></td>"+ "<td width=\"20%\"><p>" +data.submitByName+"</p></td>"+ "<td width=\"15%\"><p>" +data.submitDtString+"</p></td>"
                        + "<td width=\"5%\">" + "  <button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"javascript:deleteFile(this,'"+data.maskId+"');\"><i class=\"fa fa-times\"></i></button>" +"</td>"+"</tr>";
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
        $("#tbodyFileListId").find("tr")[0].remove();
    }

    function validateUploadInternal(){
        var flag = true;
        $('#selectedFileShow').html('')
        $('#fileRemarkShow').html('')
        var selectedFile = $('#uploadDoc').find('[name="selectedFile"]').val();

        var file = $('#selectedFile').get(0).files[0];
        if(selectedFile == null || selectedFile== "" ||file==null|| file==undefined){
            $('#selectedFileShow').html('The file cannot be empty.');
            $('#uploadFileButton').attr("disabled", false);
            return false;
        }else {
            var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
            if(fileSize> 4){
                $('#selectedFileShow').html('The file size must less than 4M.');
                $('#uploadFileButton').attr("disabled", false);
                return false;
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


    $('#selectedFile').change(function () {
        var file = $(this).val();
        if(file != null && file !=""){
            $('#selectedFileShowTextName').val(getFileName(file));
        }
    });

    function getFileName(o) {
        var pos = o.lastIndexOf("\\");
        return o.substring(pos + 1);
    }
</script>
