
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
    var advfiltersonList=$("[id^='advfiltersonCycle']");
    var lenSon = advfiltersonList.length;
    for (var i = 0;i<lenSon;i++){
        var hideSon = $(advfiltersonList[i]);
        if(hideSon.prop('id') !=='advfiltersonCycle'+divid){
            hideSon.hide();
        }
    }
    var advfilterList=$("a[data-target^='#dropdownCycle']");
    var len = advfilterList.length;
    for (var j = 0;j<len;j++){
        var hide = $(advfilterList[j]);
        if(hide.prop('data-target') !=='#dropdownCycle'+divid){
            hide.addClass('collapsed')
            hide.prop('aria-expanded', false);
        }
    }
    var dropdownList=$("[id^='dropdownCycle']");
    var lendropdown = dropdownList.length;
    for (var k = 0;k<lendropdown;k++){
        var dropdown = $(dropdownList[k]);
        if(dropdown.prop('id') !=='dropdownCycle'+divid){
            dropdown.removeClass('in')
            dropdown.prop('aria-expanded', false);
        }
    }
    if (!isInArray(dividajaxlist,divid)) {
        stageAjax(cycleId, divid);
    } else {
        var display = $('#advfiltersonCycle' + divid).css('display');
        if (display == 'none') {
            $('#advfiltersonCycle' + divid).show();
        } else {
            $('#advfiltersonCycle' + divid).hide();
        }
    }
    dismissWaiting();
};

var getStageByNonCycleId = function (cycleId, divid) {
    var advfiltersonList=$("[id^='advfiltersonNon']");
    var lenSon = advfiltersonList.length;
    for (var i = 0;i<lenSon;i++){
        var hideSon = $(advfiltersonList[i]);
        if(hideSon.prop('id') !=='advfiltersonNon'+divid){
            hideSon.hide();
        }
    }
    var advfilterList=$("a[data-target^='#dropdownNon']");
    var len = advfilterList.length;
    for (var j = 0;j<len;j++){
        var hide = $(advfilterList[j]);
        if(hide.prop('data-target') !=='#dropdownNon'+divid){
            hide.addClass('collapsed')
            hide.prop('aria-expanded', false);
        }
    }
    var dropdownList=$("[id^='dropdownNon']");
    var lendropdown = dropdownList.length;
    for (var k = 0;k<lendropdown;k++){
        var dropdown = $(dropdownList[k]);
        if(dropdown.prop('id') !=='dropdownNon'+divid){
            dropdown.removeClass('in')
            dropdown.prop('aria-expanded', false);
        }
    }
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
                let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfiltersonCycle' + divid + '">' +
                    '<td colspan="7" class="hiddenRow">' +
                    '<div class="accordian-body p-3 collapse in" id="dropdownCycle' + divid + '" >' +
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
                $('#advfilterCycle' + divid).after(html);
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