var dividajaxlist = [];

$(document).ready(function() {
    if ($("#unlockSuccessFlag").val() == "success") {
        $('#unlockSucConfirm').modal('show');
    }
});

$("#clearBtn").click(function () {
    $('input[name="submissionNoFilter"]').val("");
    $('input[name="submitDateFromFilter"]').val("");
    $('input[name="submitDateToFilter"]').val("");
    $("#arCenterFilter option:first").prop("selected", 'selected');
    $("#cycleStageFilter option:first").prop("selected", 'selected');
})

$("#searchBtn").click(function () {
    showWaiting();
    document.getElementById('crud_action_type').value = 'search';
    document.getElementById("mainForm").submit();
})

$("#unlockBtn").click(function () {
    $('#unlockConfirm').modal('show');
})

function submitUnlockStage() {
    showWaiting();
    $("#unlockStgBtn").prop( "disabled", true );
    $("#unlockCycBtn").prop( "disabled", true );
    $("#unlockType").val("stage");
    document.getElementById('crud_action_type').value = 'unlock';
    document.getElementById("mainForm").submit();
}

function submitUnlockCycle() {
    showWaiting();
    $("#unlockStgBtn").prop( "disabled", true );
    $("#unlockCycBtn").prop( "disabled", true );
    $("#unlockType").val("cycle");
    document.getElementById('crud_action_type').value = 'unlock';
    document.getElementById("mainForm").submit();
}

var getPatientByPatientCode = function (patientCode, divid) {
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


                html += '<th width="5%"></th>' +
                    '<th width="5%">AR/IUI/OFO/SFO</th>' +
                    '<th width="10%">AR Treatment Cycle Type</th>' +
                    '<th width="15%">AR Centre</th>' +
                    '<th width="15%">Cycle Start Date</th>' +
                    '<th width="20%">Co-funding Claimed</th>' +
                    '<th width="10%">Status</th>'  +
                    '<th width="10%">Submission ID</th>'  +
                    '<th width="10%">Cycle Stage</th>'  +
                    '</tr>' +
                    '</thead>' +
                    '<tbody>';
                for (let i = 0; i < res.rowCount; i++) {
                    var color = "black";

                    html += '<tr style = "color : ' + color + ';">';

                    html += '<td style="vertical-align:middle;"><input type="checkbox" name="subId" onclick="javascript:chooseFirstcheckBox(' + divid + ');" value="' + res.rows[i].submissionNo + '"></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR/IUI/OFO/SFO</p><p>' + res.rows[i].dsType + '<p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR Treatment Cycle Type</p><p>' + res.rows[i].arTreatment + '<p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">AR Centre</p><p>' + res.rows[i].arCentre + '<p></td>';
                    html += '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Cycle Start Date</p><p>' + res.rows[i].cycleStartDateStr + '</p></td>';

                    html += '</p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Co-funding Claimed</p><p>' + res.rows[i].coFunding + '</p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Status</p><p>' + res.rows[i].status + '</p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Submission ID</p><p>' + res.rows[i].submissionNo + '</p></td>' +
                        '<td style="vertical-align:middle;"><p class="visible-xs visible-sm table-row-title">Cycle Stage</p><p>' + res.rows[i].stage + '</p></td>' +
                        '</tr>';
                }
                html += '</tbody></table></div></td></tr>';
                $('#advfilter' + divid).after(html);
                $('#patientId' + divid).show();
            }
        }
    )
};

function chooseAllcheckBox(id) {
    if ($('#patientId' + id).prop('checked')) {
        $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", true)
    } else {
        $('#advfilterson' + id + ' input[type="checkbox"]').prop("checked", false)
    }
}

function chooseFirstcheckBox(id) {
    var divid = 'checkbox' + id;
    var flag = true;

    $('#advfilterson' + id + ' input[type="checkbox"]').each(function () {
        if ($(this).attr('id') != divid) {
            if (!$(this).is(':checked')) {
                flag = false;
            }
        }
    });
    if (flag) {
        $('#patientId' + id).prop("checked", true)
    } else {
        $('#patientId' + id).prop("checked", false)
    }
}

function isInArray(arr,value){
    for(var i = 0; i < arr.length; i++){
        if(value === arr[i]){
            return true;
        }
    }
    return false;
}
