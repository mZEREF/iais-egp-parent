var dividajaxlist = [];

function doArBack() {
    showWaiting();
    $("[name='adv_action_type']").val('back');
    $('#mainForm').submit();
}

function doClear() {
    $('input[type="text"]').val("");
    $('input[type="number"]').val("");
    $('input[type="radio"]').prop("checked", false);
    $('input[type="checkbox"]').prop("checked", false);
    $("select option").prop("selected", false);
    $("#searchCondition input[type='checkbox']").prop('checked', false);
    $("#searchCondition .multi-select-button").html("-- Select --");
    $(".clearSel").children(".current").text("Please Select");
    $('.date_picker').val("");


}


function jumpToPagechangePage() {
    search();
}

function doSearch() {
    $('input[name="pageJumpNoTextchangePage"]').val(1);
    search();
}

function search() {
    showWaiting();
    $("[name='adv_action_type']").val('search');
    $('#mainForm').submit();
}

function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    $("[name='adv_action_type']").val('search');
    $('#mainForm').submit();
}


var fullDetailsView = function (patientCode) {

    showWaiting();
    $("[name='crud_action_additional']").val('patient');
    $("[name='crud_action_value']").val(patientCode);
    $("[name='adv_action_type']").val('viewFull');
    $('#mainForm').submit();
};
var getPatientByPatientCode = function (patientCode, divid) {
    showWaiting();
    if (!isInArray(dividajaxlist,divid)) {
        groupAjax(patientCode, divid);
    } else {
        var display = $('#advfilterson' + divid).css('display');
        if (display == 'none') {
            $('#advfilterson' + divid).show();
        } else {
            $('#advfilterson' + divid).hide();
        }
    }
    dismissWaiting();
};

function isInArray(arr,value){
    for(var i = 0; i < arr.length; i++){
        if(value === arr[i]){
            return true;
        }
    }
    return false;
}

var groupAjax = function (patientCode, divid) {
    dividajaxlist.push(divid);
    $.post(
        '/hcsa-licence-web/hcsa/enquiry/ar/patientDetail.do',
        {patientCode: patientCode},
        function (data) {
            let result = data.result;
            if('Success' == result) {
                let res = data.ajaxResult;
                let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                    '<td colspan="7" class="hiddenRow">' +
                    '<div class="accordian-body p-3 collapse in" id="dropdown' + divid + '" >' +
                    '<table class="table application-item" style="background-color: #F3F3F3;" >' +
                    '<thead>' +
                    '<tr>';


                html += '<th width="10%">AR/IUI/EFO</th>' +
                    '<th width="20%">AR Treatment Cycle Type</th>' +
                    '<th width="25%">AR Centre</th>' +
                    '<th width="15%">Cycle Start Date</th>' +
                    '<th width="20%">Co-funding Claimed</th>' +
                    '<th width="15%">Status</th>'  +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                for (let i = 0; i < res.rowCount; i++) {
                    var color = "black";

                    html += '<tr style = "color : ' + color + ';">';

                    html += '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR/IUI/EFO</p><p>' + res.rows[i].dsType + '<p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR Treatment Cycle Type</p><p>' + res.rows[i].arTreatment + '<p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR Centre</p><p>' + res.rows[i].arCentre + '<p></td>';
                    html += '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Cycle Start Date</p><p><a href="#" onclick="javascript:fullStagesView(' + "'" + res.rows[i].cycleId + "'" + ');">' + res.rows[i].cycleStartDateStr + '</a></p></td>';

                    html += '</p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Co-funding Claimed</p><p>' + res.rows[i].coFunding + '</p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Status</p><p>' + res.rows[i].status + '</p></td>' +
                        '</tr>';
                }
                html += '</tbody></table></div></td></tr>';
                $('#advfilter' + divid).after(html);
            }
        }
    )

};


var quickView = function (patientCode) {

    var jsonData={
        'patientCode': patientCode
    };
    $.ajax({
        'url':'/hcsa-licence-web/hcsa/enquiry/ar/ar-quick-view',
        'dataType':'text',
        'data':jsonData,
        'type':'GET',
        'success':function (data) {
            if(data == null){
                return;
            }
            $('.quickBodyDiv').html(data);


        },
        'error':function () {
        }
    });
}

var fullStagesView = function (submissionIdNo) {

    showWaiting();
    $("[name='crud_action_value']").val(submissionIdNo);
    $("[name='adv_action_type']").val('viewStage');
    $('#mainForm').submit();
}