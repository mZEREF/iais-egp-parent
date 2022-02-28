
var dividajaxlist = [];

var dividajaxNonlist = [];


function doStageSearch(cycleId,submissionNo){
    showWaiting();
    $("[name='crud_action_value']").val(cycleId);
    $("[name='crud_action_additional']").val(submissionNo);
    $("[name='crud_action_type']").val('perStage');
    $('#mainForm').submit();
}
function doBack(arAdv,arBase){
    showWaiting();
    if(arAdv==1){
        $("[name='crud_action_type']").val('backAdv');
    }
    if(arBase==1){
        $("[name='crud_action_type']").val('backBase');
    }
    $('#mainForm').submit();
}
$(document).ready(function() {
    $('#inventoryTab').click(function(){
        showWaiting();
        $("[name='crud_action_type']").val('searchInv');
        $('#mainForm').submit();
    });
    $('#cycleStageTab').click(function(){
        showWaiting();
        $("[name='crud_action_type']").val('searchCyc');
        $('#mainForm').submit();
    });

});

function searchInventory() {
    showWaiting();
    $('input[name="pageJumpNoTextchangePage"]').val(1);
    $("[name='crud_action_type']").val('searchInv');
    $('#mainForm').submit();
}

function jumpToPagechangePage() {
    var preActiveHidden=$("[name='preActiveHidden']").val();

    if(preActiveHidden==='1'){
        showWaiting();
        $("[name='crud_action_type']").val('searchInv');
        $('#mainForm').submit();
    }
    if(preActiveHidden==='2'){
        showWaiting();
        $("[name='crud_action_type']").val('searchCyc');
        $('#mainForm').submit();
    }

}


function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    var preActiveHidden=$("[name='preActiveHidden']").val();

    if(preActiveHidden==='1'){
        $("[name='crud_action_type']").val('searchInv');
    }
    if(preActiveHidden==='2'){
        $("[name='crud_action_type']").val('searchCyc');
    }
    $('#mainForm').submit();
}

var getStageByCycleId = function (cycleId, divid) {
    showWaiting();
    if (!isInArray(dividajaxlist,divid)) {
        stageAjax(cycleId, divid);
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

var getStageByNonCycleId = function (cycleId, divid) {
    showWaiting();
    if (!isInArray(dividajaxNonlist,divid)) {
        stageAjaxNon(cycleId, divid);
    } else {
        var display = $('#advfiltersonNon' + divid).css('display');
        if (display == 'none') {
            $('#advfiltersonNon' + divid).show();
        } else {
            $('#advfiltersonNon' + divid).hide();
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
};
var stageAjax = function (cycleIder, divid) {
    dividajaxlist.push(divid);
    $.post(
        '/hcsa-licence-web/hcsa/enquiry/ar/cycleStageDetail.do',
        {cycleIder: cycleIder},
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
                html += '<th width="25%">Submission ID</th>' +
                    '<th width="25%">Date</th>' +
                    '<th width="25%">Stage</th>' +
                    '<th width="25%">Action</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                for (let i = 0; i < res.length; i++) {
                    var color = "black";
                    html += '<tr style = "color : ' + color + ';">';
                    html += '<td><p class="visible-xs visible-sm table-row-title">Submission ID</p><p>' + res[i].submissionNo + '<p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">Date</p><p>' + res[i].submitDtStr + '<p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">Stage</p><p>' + res[i].cycleStageStr + '<p></td>';
                    html += '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Action</p><p>' +
                        '<button type="button" onclick="doStageSearch(' + "'" + res[i].cycleId + "','"+ res[i].submissionNo + "'" + ')" class="btn btn-default btn-sm">'+
                        'View Full Details</button></p></td>'+
                        '</tr>';
                }
                html += '</tbody></table></div></td></tr>';
                $('#advfilter' + divid).after(html);
            }
        }
    )

};

var stageAjaxNon = function (cycleIder, divid) {
    dividajaxNonlist.push(divid);
    $.post(
        '/hcsa-licence-web/hcsa/enquiry/ar/cycleStageDetail.do',
        {cycleIder: cycleIder},
        function (data) {
            let result = data.result;
            if('Success' == result) {
                let res = data.ajaxResult;
                let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfiltersonNon' + divid + '">' +
                    '<td colspan="7" class="hiddenRow">' +
                    '<div class="accordian-body p-3 collapse in" id="dropdownNon' + divid + '" >' +
                    '<table class="table application-item" style="background-color: #F3F3F3;" >' +
                    '<thead>' +
                    '<tr>';
                html += '<th width="25%">Submission ID</th>' +
                    '<th width="25%">Date</th>' +
                    '<th width="25%">Stage</th>' +
                    '<th width="25%">Action</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                for (let i = 0; i < res.length; i++) {
                    var color = "black";
                    html += '<tr style = "color : ' + color + ';">';
                    html += '<td><p class="visible-xs visible-sm table-row-title">Submission ID</p><p>' + res[i].submissionNo + '<p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">Date</p><p>' + res[i].submitDtStr + '<p></td>' +
                        '<td><p class="visible-xs visible-sm table-row-title">Stage</p><p>' + res[i].cycleStageStr + '<p></td>';
                    html += '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Action</p><p>' +
                        '<button type="button" onclick="doStageSearch(' + "'" + res[i].cycleId + "','"+ res[i].submissionNo + "'" + ')" class="btn btn-default btn-sm">'+
                        'View Full Details</button></p></td>'+
                        '</tr>';
                }
                html += '</tbody></table></div></td></tr>';
                $('#advfilterNon' + divid).after(html);
            }
        }
    )
};

function doInvClear() {
    $('input[type="number"]').val("");
    $('input[type="checkbox"]').prop("checked", false);
    $("option:first").prop("selected", 'selected');
    $(".clearSel").children(".current").text("Please Select");
    $('.date_picker').val("");
}