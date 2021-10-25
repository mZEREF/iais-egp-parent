function doAdjust(){
    var flag = true;
    if($("select[name= 'adjustment_type']").val() == null || $("select[name= 'adjustment_type']").val() == ""){
        $("#errMsg01").html("This field is mandatory");
        flag = false;
    }
    if($("select[name='type_of_transfer']").val() == null || $("select[name='type_of_transfer']").val() == ""){
        $("#errMsg02").html("This field is mandatory");
        flag = false;
    }
    if($("input[name= 'initial_quantity']").val() == null || $("input[name= 'initial_quantity']").val() == ""){
        $("#errMsg03").html("This field is mandatory");
        flag = false;
    }
    if($("input[name='quantity_to_change']").val() == null || $("input[name='quantity_to_change']").val() == ""){
        $("#errMsg04").html("This field is mandatory");
        flag = false;
    }
    if(flag){
        $("[name='action_type']").val("basicAdjust");
        $("#mainForm").submit();
    }
}

function doBasicClear() {
    showWaiting();
    $("#selectSearchChkMsg").hide();
    $('input[name="searchNo"]').val("");
    $('input[type="radio"]').prop("checked", false);
}

function doAdvancedSearch() {
    showWaiting();
    var chk = $("[name='searchChk']:checked");
    var dropIds = new Array();
    chk.each(function () {
        dropIds.push($(this).val());
    });
    if (dropIds.length === 0) {
        $("#selectSearchChkMsg").show();
        dismissWaiting();
    } else {
        $("[name='action_type']").val("advSearch");
        $("#mainForm").submit();
    }
}


function doSearch() {
    showWaiting();
    var chk = $("[name='searchChk']:checked");
    var dropIds = new Array();
    chk.each(function () {
        dropIds.push($(this).val());
    });
    if (dropIds.length === 0) {
        $("#selectSearchChkMsg").show();
        dismissWaiting();
    } else {
        $("[name='action_type']").val("doSearch");
        $("#mainForm").submit();
    }
}
function doHisInfo() {
    showWaiting();
    $("[name='action_type']").val("adjust");
    $("#mainForm").submit();
}


function doClear(){
    $('select option[firstOption="Please Select"] option:first').prop("selected",'selected');
    for(var i=1;i<=4;i++){
        $("#errMsg0"+i).html("");
    }
    $("input[type='text']").val("");
}


function doAdvSearch() {
    showWaiting();
    $("[name='action_type']").val("search");
    $("#mainForm").submit();
}

function doBack() {
    showWaiting();
    $("[name='action_type']").val("back");
    $("#mainForm").submit();
}
