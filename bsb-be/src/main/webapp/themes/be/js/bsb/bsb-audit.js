//audit date
function doSpecifyDt(id){
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("specifyDt");
    $("#mainForm").submit();
}

function dochangeDt(id){
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("changeDt");
    $("#mainForm").submit();
}

function submitReport(id){
    showWaiting();
    $("#auditId").val(id);
    $("[name='action_type']").val("doSelfAudit");
    $("#mainForm").submit();
}

//upload file
// function openUploadDoc(){
//     $('#uploadDoc').dialog('open');
// };
//
// function closeUploadDoc(){
//     $('#uploadDoc small.error').html('').hide();
//     $('#error_selectedFile').html('').hide();
//     $('#uploadDoc').dialog('close');
// };
//
// function validateUploadInternal(){
//     var flag = true;
//     $('#uploadDoc small.error').html('').hide();
//
//     var fileRemarkMaxLength = 50;
//     var fileRemarkLength = $('#fileRemark').val().length;
//     if(fileRemarkLength > fileRemarkMaxLength){
//         $('[name="fileRemark"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">Exceeding the maximum length by ' + fileRemarkMaxLength + '.</span>');
//         flag = flag && false;
//     }
//
//     var file = $('#selectedFile').get(0).files[0];
//     if(file==null || file=="" || file==undefined){
//         $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file cannot be empty.</span>');
//         flag = flag && false;
//     }else{
//         var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
//         var selectedFile = $('#uploadDoc').find('[name="selectedFile"]').val();
//         var fileName = $('#selectedFile').get(0).files[0].name;
//         var fileType = fileName.substr(fileName.lastIndexOf(".")).toUpperCase();
//
//         //check file type
//         if (fileType != ".PDF" && fileType != ".PNG" && fileType != ".DOCX" && fileType != ".JPG" && fileType != ".JPEG" && fileType != ".DOC") {
//             $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file type is invalid.</span>');
//             flag = flag && false;
//         }
//         //check file size
//         if(selectedFile==""){
//             $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file cannot be empty.</span>');
//             flag = flag && false;
//         }else if(fileSize> 4){
//             $('[name="selectedFile"]').parent().nextAll('.error').html('<span style="color: #D22727; font-size: 1.6rem">The file size must less than 4M.</span>');
//             flag = flag && false;
//         }
//     }
//
//     if(!flag){
//         $('#uploadDoc small.error').each(function(){
//             var html = $(this).html();
//             if(html){
//                 $(this).show();
//             }
//         });
//     }
//     return flag;
// }
//
// function uploadInternalDoc(){
//     $('#uploadFileButton').attr("disabled",true);
//     showWaiting();
//     if(validateUploadInternal())
//         callAjaxUploadFile();
// }
//
// function deleteFile(repoId) {
//     showWaiting();
//     $('#interalFileId').val(repoId);
//     $('#mainForm').submit();
// }
//
// function callAjaxUploadFile(){
//     var form = new FormData($("#fileUploadForm")[0]);
//     $.ajax({
//         type: "post",
//         url:  "${pageContext.request.contextPath}/uploadInternalFile",
//         data: form,
//         async:true,
//         processData: false,
//         contentType: false,
//         dataType: "json",
//         success: function (data) {
//
//             if(data != null && data.fileSn != null && data.fileSn != -1){
//                 if(data.fileSn == 0){
//                     removeNoData();
//                 }
//                 var tr = "<tr>"+"<td width=\"30%\"><p>" +data.docDesc+"</p></td>" +"<td  width=\"20%\"><p>"+  data.url +data.docName+"."+data.docType+"</p></td>"+
//                     "<td width=\"10%\"><p>" +data.docSize+"KB"+"</p></td>"+ "<td width=\"20%\"><p>" +data.submitByName+"</p></td>"+ "<td width=\"10%\"><p>" +data.submitDtString+"</p></td>"
//                     + "<td width=\"10%\">" + "  <button type=\"button\" class=\"btn btn-secondary-del btn-sm\" onclick=\"javascript:deleteFile(this,'"+data.maskId+"');\">Delete</button>" +"</td>"+"</tr>";
//                 doAddTr(tr);
//                 $("#cancelDoc").click();
//             }else if(data != null && data.fileSn ==-1){
//                 $('#selectedFileShow').html(data.noFilesMessage);
//             }else if(data != null && data.fileSn == null){
//                 $('#selectedFileShow').html('The file size must less than 4M.');
//             }
//             $('#uploadFileButton').attr("disabled", false);
//         },
//         error: function (msg) {
//             alert("error");
//         }
//     });
// }

$(function () {
    //Manual Audit Creation List
    $("#createList").click(function (){
        $("[name='action_type']").val("createList");
        if ($("input:checkbox:checked").length > 0) {
            showWaiting();
            $("#mainForm").submit();
        }else{
            alert("please select one");
        }
    });

    $("#searchBtn").click(function (){
        showWaiting();
        $("[name='action_type']").val("doSearch");
        $("#mainForm").submit();
    });

    $("#clearBtn").click(function () {
        $("#facilityClassification option:first").prop("selected",'selected');
        $("#facilityType option:first").prop("selected",'selected');
        $("#auditType option:first").prop("selected",'selected');
        $("#facilityName option:first").prop("selected",'selected');
        $("#beInboxFilter .current").text("Please Select");
    });

    $("#submitAudit").click(function () {
        $("[name='action_type']").val("doSubmit");
        var optionValue = $("#auditType option:selected").val();
        if (optionValue == "Please Select" || optionValue == "") {
            $("#auditTypeError").html("Please select valid options!");
        }else {
            showWaiting();
            $("#mainForm").submit();
        }
    });

    //specify And Change Date
    $("#submitChangeAuditDt").click(function (){
        $("[name='action_type']").val("doSubmit");
        var auditDate = $("#auditDate").val();
        var reason = $("#reasonForChange").val();
        if (auditDate==null || auditDate == ""){
            $("#auditDateError").html("Please Select Date");
        }else if (reason==null||reason==""){
            $("#auditDateError").html("");
            $("#reasonError").html("Please enter change reason");
        }else{
            showWaiting();
            $("#mainForm").submit();
        }
    });

    $("#submitSpecifyAuditDt").click(function (){
        $("[name='action_type']").val("doSubmit");
        var auditDate = $("#auditDate").val();
        if (auditDate==null || auditDate == ""){
            $("#auditDateError").html("Please Select Date");
        }else{
            showWaiting();
            $("#mainForm").submit();
        }
    });
    //AO And DO Process Audit Date
    $("#rejectReason").hide();
    $("#decision").change(function (){
        var optionValue1 = $("#decision option:selected").val();
        if (optionValue1 == "AUDTAO002" || optionValue1 == "AUDTDO002") {
            $("#rejectReason").show();
        }else{
            $("#rejectReason").hide();
        }
    })
    $("#submitButton").click(function () {
        var optionValue = $("#decision option:selected").val();
        if (optionValue == "AUDTAO001" || optionValue == "AUDTDO001") {
            $("#error_decision").html("");
            showWaiting();
            $("[name='action_type']").val("doApprove");
            $("#mainForm").submit();
        }
        if (optionValue == "AUDTAO002" || optionValue == "AUDTDO002") {
            var reasonValue = $("#reason").val();
            if (reasonValue == "" || reasonValue == null) {
                $("#error_decision").html("");
                $("#error_reason").html("Please enter the reason");
            } else {
                $("#error_decision").html("");
                $("#error_reason").html("");
                showWaiting();
                $("[name='action_type']").val("doReject");
                $("#mainForm").submit();
            }
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("Please select valid options");
        }
    })

    //back
    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#backFromAckPage").click(function (){
        showWaiting();
        $("#mainForm").submit();
    });
});