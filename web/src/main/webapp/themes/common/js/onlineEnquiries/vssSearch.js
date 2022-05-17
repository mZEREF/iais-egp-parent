
function doClear() {
    $('input[type="text"]').val("");
    $('input[type="checkbox"]').prop("checked", false);
    $("select option").prop("selected", false);
    $(".clearSel").children(".current").text("Please Select");

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
    $("[name='crud_action_type']").val('search');
    $('#mainForm').submit();
}

function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    $("[name='crud_action_type']").val('search');
    $('#mainForm').submit();
}


var fullDetailsView = function (submissionNo) {

    showWaiting();
    $("[name='crud_action_value']").val(submissionNo);
    $("[name='crud_action_type']").val('info');
    $('#mainForm').submit();
}

var dividajaxlist = [];

var getVssByIdType = function (patientIdNo,patientIdType, divid) {
    showWaiting();
    var advfiltersonList=$("[id^='advfilterson']");
    var lenSon = advfiltersonList.length;
    for (var i = 0;i<lenSon;i++){
        var hideSon = $(advfiltersonList[i]);
        if(hideSon.prop('id') !=='advfilterson'+divid){
            hideSon.hide();
        }
    }
    var advfilterList=$("a[data-target^='#dropdown']");
    var len = advfilterList.length;
    for (var j = 0;j<len;j++){
        var hide = $(advfilterList[j]);
        if(hide.prop('data-target') !=='#dropdown'+divid){
            hide.addClass('collapsed')
            hide.prop('aria-expanded', false);
        }
    }
    var dropdownList=$("[id^='dropdown']");
    var lendropdown = dropdownList.length;
    for (var k = 0;k<lendropdown;k++){
        var dropdown = $(dropdownList[k]);
        if(dropdown.prop('id') !=='dropdown'+divid){
            dropdown.removeClass('in')
            dropdown.prop('aria-expanded', false);
        }
    }
    if (!isInArray(dividajaxlist,divid)) {
        groupAjax(patientIdNo,patientIdType, divid);
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
var groupAjax = function (patientIdNo,patientIdType, divid) {
    dividajaxlist.push(divid);
    $.post(
        '/hcsa-licence-web/hcsa/enquiry/ar/vssDetail.do',
        {patientIdNo: patientIdNo,
            patientIdType: patientIdType
        },
        function (data) {
            let result = data.result;
            if('Success' == result) {
                let res = data.ajaxResult;
                let html = '<tr style="background-color: #F3F3F3;" class="p" id="advfilterson' + divid + '">' +
                    '<td colspan="9" class="hiddenRow">' +
                    '<div class="accordian-body collapse in" id="dropdown' + divid + '" >' +
                    '<table class="table application-item" style="background-color: #F3F3F3;" >' +
                    '<thead>' +
                    '<tr>';

                html += '<th colspan="9" style=" text-align: center">Previous Submissions</th>' +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                for (let i = 0; i < res.rowCount; i++) {
                    var color = "black";

                    html += '<tr style = "color : ' + color + ';">';

                    html += '<td width="15.6%" style="vertical-align:middle;"><p>' + res.rows[i].centerName + '<p></td>';
                    html += '<td width="10.3%" style="vertical-align:middle;"><p><a href="#" onclick="javascript:fullDetailsView(' + "'" + res.rows[i].submissionNo + "'" + ');">' + res.rows[i].submissionNo + '</a></p></td>';

                    html += '</p></td>' +
                        '<td width="9.4%" style="vertical-align:middle;"><p>' + res.rows[i].patientName + '<p></td>' +
                        '<td width="8.6%" style="vertical-align:middle;"><p>' + res.rows[i].patientIdType + '<p></td>' +
                        '<td width="10.4%" style="vertical-align:middle;"><p>' + res.rows[i].patientIdNo + '</p></td>' +
                        '<td width="11%" style="vertical-align:middle;"><p>' + res.rows[i].patientBirthdayStr + '</p></td>' +
                        '<td width="8.3%" style="vertical-align:middle;"><p>' + res.rows[i].maritalStatus + '</p></td>' +
                        '<td width="15%" style="vertical-align:middle;"><p>' + res.rows[i].sterilisationReason + '</p></td>' +
                        '<td width="9.4%" style="vertical-align:middle;"><p>' + res.rows[i].submitDtStr + '</p></td>' +
                        '</tr>';
                }
                html += '</tbody></table></div></td></tr>';
                $('#advfilter' + divid).after(html);
            }
        }
    )

};