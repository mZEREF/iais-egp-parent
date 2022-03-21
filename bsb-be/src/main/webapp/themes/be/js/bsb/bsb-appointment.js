$(document).ready(function() {
    var sysInspDateFlag = $("#sysInspDateFlag").val();
    var sysSpecDateFlag = $("#sysSpecDateFlag").val();
    if('true' == sysInspDateFlag){
        $("#disApptSysInspDate").hide();
        $("#apptSysInspDate").show();
        $("#apptThreeInspDate").show();
    } else {
        $("#disApptSysInspDate").show();
        $("#apptSysInspDate").hide();
        $("#apptThreeInspDate").hide();
    }
    if('true' == sysSpecDateFlag){
        $("#disApptSpecInspDate").hide();
        $("#apptSpecInspDate").show();
    } else {
        $("#disApptSpecInspDate").show();
        $("#apptSpecInspDate").hide();
    }
    // var apptBackShow = $("#apptBackShow").val();
    // if('back' == apptBackShow){
    //     apptInspectionDateJump();
    // }
});

// function apptInspectionDateJump(){
//     $("#apptInspTabInfo").removeClass('active');
//     $("#apptInspTabDocuments").removeClass('active');
//     $("#apptInspTabProcessing").removeClass('active');
//     $('#apptInspectionDate').trigger("click");
//     $("#apptInspectionDateProcess").click();
//     $("#apptInspTabProcessing").addClass('active');
// }

function apptInspectionDateConfirm() {
    showWaiting();
    $("#processDec").val('confirm');
    $("[name='action_type']").val("next");
    $("#mainForm").submit();
}

function apptInspectionDateGetDate() {
    showWaiting();
    var sysInspDateFlag = $("#sysInspDateFlag").val();
    if('true' == sysInspDateFlag){
        $("#disApptSysInspDate").hide();
        $("#apptSysInspDate").show();
        $("#apptThreeInspDate").show();
        dismissWaiting();
    } else {
        $.post(
            '/bsb-be/online-appt/insp.date',
            function (data) {
                dismissWaiting();
                var ajaxFlag = data.buttonFlag;
                var inspDateList = data.inspDateList;
                var specButtonFlag = data.specButtonFlag;
                if('true' == specButtonFlag){
                    $("#disApptSpecInspDate").hide();
                    $("#apptSpecInspDate").show();
                } else {
                    $("#disApptSpecInspDate").show();
                    $("#apptSpecInspDate").hide();
                }
                if('true' == ajaxFlag){
                    $("#disApptSysInspDate").hide();
                    $("#apptSysInspDate").show();
                    $("#apptThreeInspDate").show();
                    var html = '<div class="row">' +
                        '<div class="col-md-6">' +
                        '<ul>';
                    for(var i = 0; i < inspDateList.length; i++){
                        html += '<li class="apptInspScheduleUl"><span style="font-size: 16px">' + inspDateList[i] + '</span></li>';
                    }
                    html += '</ul>' +
                        '</div>' +
                        '</div>';
                    $("#apptDateTitle").after(html);
                    $("#sysInspDateFlag").val('true');
                } else {
                    $("#disApptSysInspDate").show();
                    $("#apptSysInspDate").hide();
                    $("#apptThreeInspDate").hide();
                }
            }
        )
    }
}

function apptInspectionDateSpecific() {
    showWaiting();
    $("#processDec").val('specify');
    $("[name='action_type']").val("specify");
    $("#mainForm").submit();
}

function downloadSupportDocument(appId, repoId, docName) {
    var url = "/bsb-be/ajax/doc/download/appointment/fac/repo/" + repoId;
    window.open(url);
}

//specify date page
function apptInspectionSpecDateConfirm() {
    showWaiting();
    $("#mainForm").submit();
}

function apptInspectionSpecDateBack() {
    showWaiting();
    $("#actionValue").val('back');
    $("#mainForm").submit();
}