

<div class="modal fade" id="uploadDoc" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <input type="hidden" id="fileMaxSize" name="fileMaxSize" value="${applicationViewDto.systemMaxFileSize}">
    <input type="hidden" id="fileUploadType" name="fileUploadType" value="${applicationViewDto.systemFileType}">
    <input type="hidden" id="fileMaxLengthMessage" name="fileMaxLengthMessage" value="<iais:message key="GENERAL_ERR0022"/>">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="closeUploadDoc()">&times;</button>
                <div class="modal-title" id="myModalLabel" style="font-size: 2rem;">Upload Internal Document</div>
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
                            <div style="margin-left: -5%" class="col-md-5">
                            <p><input  id = "selectedFileShowText" name = "selectedFileShowText"  type="text"   value="Select File" readonly>
                                </p>
                            </div>
                            <div style="margin-left: -8%" class="col-md-8">
                                <input  id = "selectedFileShowTextName" name = "selectedFileShowTextName"  type="text"   readonly>
                                <small class="error"><span id="selectedFileShow" style="color: #D22727; font-size: 1.6rem"></span></small>
                            </div>
                            <div hidden><input class = "inputtext-required" id = "selectedFile" name = "selectedFile" type="file"></div>
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
                $('#selectedFileShow').html('The file has exceeded the maximum upload size of '+ maxSize + 'M.');
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
