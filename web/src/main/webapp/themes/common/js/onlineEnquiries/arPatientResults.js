var dividajaxlist = [];

$(document).ready(function () {

    var panelTriggers = document.getElementsByClassName('js-cd-panel-trigger');
    if( panelTriggers.length > 0 ) {
        for(var i = 0; i < panelTriggers.length; i++) {
            (function(i){
                var panelClass = 'js-cd-panel-'+panelTriggers[i].getAttribute('data-panel'),
                    panel = document.getElementsByClassName(panelClass)[0];
                // open panel when clicking on trigger btn
                panelTriggers[i].addEventListener('click', function(event){
                    event.preventDefault();
                    addClass(panel, 'cd-panel--is-visible');
                });
                //close panel when clicking on 'x' or outside the panel
                panel.addEventListener('click', function(event){
                    if( hasClass(event.target, 'js-cd-close') || hasClass(event.target, panelClass)) {
                        event.preventDefault();
                        removeClass(panel, 'cd-panel--is-visible');
                    }
                });
            })(i);
        }
    }

    //class manipulations - needed if classList is not supported
    //https://jaketrent.com/post/addremove-classes-raw-javascript/
    function hasClass(el, className) {
        if (el.classList) return el.classList.contains(className);
        else return !!el.className.match(new RegExp('(\\s|^)' + className + '(\\s|$)'));
    }
    function addClass(el, className) {
        if (el.classList) el.classList.add(className);
        else if (!hasClass(el, className)) el.className += " " + className;
    }
    function removeClass(el, className) {
        if (el.classList) el.classList.remove(className);
        else if (hasClass(el, className)) {
            var reg = new RegExp('(\\s|^)' + className + '(\\s|$)');
            el.className=el.className.replace(reg, ' ');
        }
    }

    $('#searchByPatient').change(function () {
        search();
    });

    $('#searchBySubmission').change(function () {
        search();
    });

    $('#submissionType').change(function () {

        var reason= $('#submissionType option:selected').val();

        if("AR_TP002"==reason){
            $('#cycleStageDisplay').attr("style","display: block");
        }else {
            $('#cycleStageDisplay').attr("style","display: none");
        }

    });
})


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

function doClear() {
    $('input[type="text"]').val("");
    $('input[type="checkbox"]').prop("checked", false);
    $("select option").prop("selected", false);
    $(".clearSel").children(".current").text("Please Select");
    $("#patientInformationFilter input[type='checkbox']").prop('checked', false);
    $("#patientInformationFilter .multi-select-button").html("-- Select --");
    $('.date_picker').val("");

    $('#cycleStageDisplay').attr("style","display: none");
}

function doAdvancedSearch() {
    showWaiting();
    $("[name='base_action_type']").val('advanced');
    $('#mainForm').submit();
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
    $("[name='base_action_type']").val('search');
    $('#mainForm').submit();
}

function sortRecords(sortFieldName, sortType) {
    $("[name='crud_action_value']").val(sortFieldName);
    $("[name='crud_action_additional']").val(sortType);
    $("[name='base_action_type']").val('search');
    $('#mainForm').submit();
}

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

var fullDetailsView = function (patientCode) {

    showWaiting();
    $("[name='crud_action_additional']").val('patient');
    $("[name='crud_action_value']").val(patientCode);
    $("[name='base_action_type']").val('viewFull');
    $('#mainForm').submit();
}

var fullDetailsViewBySubId = function (submissionId,submissionType,submissionIdNo) {

    showWaiting();
    $("[name='crud_action_additional']").val('submission');
    $("[name='crud_action_type']").val(submissionType);
    $("[name='crud_type']").val(submissionIdNo);
    $("[name='crud_action_value']").val(submissionId);
    $("[name='base_action_type']").val('viewFull');
    $('#mainForm').submit();
}

var fullStagesView = function (submissionIdNo) {

    showWaiting();
    $("[name='crud_action_value']").val(submissionIdNo);
    $("[name='base_action_type']").val('viewStage');
    $('#mainForm').submit();
}