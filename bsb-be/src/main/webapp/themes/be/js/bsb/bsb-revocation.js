function doProcess(id){
    showWaiting();
    $("#appId").val(id);
    $("[name='action_type']").val("doProcess");
    $("#mainForm").submit();
}

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
//     showWaiting();
//     if(validateUploadInternal()) {
//         $('#uploadFile').val('Y');
//         // callAjaxUploadFile();
//         $('#uploadFileButton').attr("disabled", "disabled");
//         $('#fileUploadForm').submit();
//     }
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
//             closeUploadDoc();
//             window.location.reload();
//
//         },
//         error: function (msg) {
//         }
//     });
// }

$(function () {
    $("#clearButton1").click(function () {
        $('#ReasonId').val("");
        $('#AORemarks').val("");
    });

    $("#submitButton1").click(function () {
        showWaiting();
        var reasonValue = $("#ReasonId").val();
        if (reasonValue == "" || reasonValue == null) {
            $("#error_reason").html("Please enter the reason");
        }else{
            $("[name='action_type']").val("doSubmit");
            $("#mainForm").submit();
        }
    });

    $("#searchBtn2").click(function () {
        showWaiting();
        $("[name='action_type']").val("doSearch");
        $("#mainForm").submit();
    });

    $("#clearBtn2").click(function () {
        $('#facilityName').val("");
        $('#facilityAddress').val("");
        $('#applicationNo').val("");
        $('#applicationStatus').val("");
        $("#searchAppDateFrom").val("");
        $("#searchAppDateTo").val("");
        $("#facilityClassification option:first").prop("selected",'selected');
        $("#facilityType option:first").prop("selected",'selected');
        $("#processType option:first").prop("selected",'selected');
        $("#applicationType option:first").prop("selected",'selected');
        $("#beInboxFilter .current").text("Please Select");
    });

    $("#clearButton3").click(function () {
        $('#number').val("");
        $("#toxinsSelect option:first").prop("selected",'selected');
        $("#stateSelect option:first").prop("selected",'selected');
        $("#clearSelect .current").text("Please Select");
    });

    $("#submitButton3").click(function () {
        showWaiting();
        $("[name='action_type']").val("doUpdate");
        $("#mainForm").submit();
    });

    $("#submitButton").click(function () {

        var optionValue = $("#decision option:selected").val();
        if (optionValue == "BSBAOPD001") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "approve");
        }
        if (optionValue == "BSBAOPD002") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "reject");
        }
        if (optionValue == "BSBAOPD003") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "routeBack");
        }
        if (optionValue == "BSBAOPD004") {
            showWaiting();
            SOP.Crud.cfxSubmit("mainForm", "submit");
        }
        if (optionValue == "Please Select" || optionValue == "") {
            $("#error_decision").html("Please select valid options!");
        }
    });

    $("#backToTask").click(function (){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm", "doBack");
    });

    $("#backToProcess").click(function (){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm");
    });

    $("#backToSubmit2").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    });

    $("#backToSubmit1").click(function (){
        showWaiting();
        SOP.Crud.cfxSubmit("mainForm");
    });

    $("#back").click(function (){
        showWaiting();
        $("[name='action_type']").val("doBack");
        $("#mainForm").submit();
    })

    $("#backFromDoc").click(function (){
        $('#documenta').removeClass("active");
        $('#infoa').click();
        $('#infoa').addClass("active");
    })

    $("#backFromProcess").click(function (){
        $('#processa').removeClass("active");
        $('#documenta').click();
        $('#documenta').addClass("active");
    })
});