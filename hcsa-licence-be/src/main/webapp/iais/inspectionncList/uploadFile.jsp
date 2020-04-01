

<div class="modal fade" id="uploadDoc" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">Upload Internal Document</h4>
            </div>
            <div class="modal-body">
                <form id="fileUploadForm" name="fileUploadForm" enctype="multipart/form-data"
                  action="" method="post">
                    <iais:field value="Upload your files" required="true"/>
                    <input type="hidden" id="uploadFile" name="uploadFile" value="">
                    <input class = "inputtext-required" id = "selectedFile" name = "selectedFile" type="file"/>
                    <br /> <small class="error" style="margin: 0 0 0 140px;"></small>
                    <span id="error_fileUploadForm" name="iaisErrorMsg" class="error-msg"></span>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" id="cancelDoc" onclick="closeUploadDoc()">cancel</button>
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
        $('#uploadDoc').dialog('close');
        doDeleteShowFileName();
    };

    function uploadInternalDoc(){
        $('#uploadDoc small.error').html('').hide();
        var selectedFile = $('#uploadDoc').find('[name="selectedFile"]').val();
        if(selectedFile != null && selectedFile != "" )
             callAjaxUploadFile();
    }

    function deleteFile(row,repoId) {
        $(row).parent('td').parent('tr').remove();
         callAjaxDeleteFile(repoId);
    }
    function callAjaxDeleteFile(repoId){
        var data = {"appDocId":repoId};
        $.post(
            "${pageContext.request.contextPath}/deleteInternalFile",
            data,
            function (data) {
                if(data != null && data.fileSn != -1){
                    if(data.fileSn == 0){
                        var tr ="<tr><td colspan='6'> " +data.noFilesMessage +"</td></tr> " ;
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
               $("#cancelDoc").click();
                if(data != null && data.fileSn != -1){
                    if(data.fileSn == 0){
                        removeNoData();
                    }
                    var tr = "<tr>"+"<td><p>" +data.docName+"</p></td>" +"<td><p>"+  data.url +data.docName+"."+data.docType+"</p></td>"+
                        "<td><p>" +data.docSize+"KB"+"</p></td>"+ "<td><p>" +data.submitByName+"</p></td>"+ "<td><p>" +data.submitDtString+"</p></td>"
                        + "<td>" +  " <a  onclick =\"javascript:deleteFile(this,'"+data.id+"');\""+"><label style=\"color: #D22727; font-size: 2rem; cursor:pointer;\">X</label></a>"+"</td>"+"</tr>";
                    doAddTr(tr);
                }
            },
            error: function (msg) {
                alert("error");
            }
        });
    }

    function doDeleteShowFileName() {
        var file = $("#selectedFile");
        file.after(file.clone().val(""));
        file.remove();
    }
    
    function doAddTr(tr) {
        $("#tbodyFileListId").append(tr);
    }
    function removeNoData() {
        $("#tbodyFileListId").find("tr")[0].remove();
    }
</script>
